/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.internal.utils.LoggerManager;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.AccessorConfigureInfo;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.ExternalizeStringsCommon;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.progress.IProgressService;

/**
 * @author chenxiaoru (xiaoru.chen@soyatec.com)
 */
@SuppressWarnings("restriction")
public class AccessorConfigurationDialog extends StatusDialog {

	private String sourceFolder;
	private String sourcePackage;
	private String className;
	private String propertyFolder;
	private String propertyPackage;
	private String propertyName;

	private Text classNameText;
	private Text sourcePackageText;
	private Text sourceFolderText;
	private Text propertyNameText;
	private Text propertyPackageText;
	private Text propertyFolderText;

	private Button sourcePackagButton;
	private Button chooseClassTypeButton;
	private Button propertyPackagButton;
	private Button choosePropertyTypeButton;
	private Button propertyFolderButton;

	private Label classDefaultLabel;
	private Label propertyDefaultLabel;
	private Label propertyLabel;

	private boolean isError = false;

	private IPackageFragmentRoot classRoot;
	private IPackageFragmentRoot propertyRoot;
	private IStatus fLastStatus;

	private IPackageFragment propertyFragment;
	private IPackageFragment classFragment;

	private AccessorConfigureInfo info;
	Object[] propertyFileNames;
	Object[] classFileNames;

	public AccessorConfigurationDialog(Shell parent, AccessorConfigureInfo info) {
		super(parent);
		this.info = info;
	}

	protected Control createDialogArea(Composite ancestor) {
		if (info.getClassRoot() != null) {
			classRoot = info.getClassRoot();
			classFragment = classRoot.getPackageFragment(info.getSourcePackage());
			classFileNames = createFileListInput(".java", classFragment);
		}

		if (info.getPropertyRoot() != null) {
			propertyRoot = info.getPropertyRoot();
			propertyFragment = propertyRoot.getPackageFragment(info.getPropertyPackage());
			propertyFileNames = createFileListInput(".properties", propertyFragment);
		}
		Composite parent = (Composite) super.createDialogArea(ancestor);

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		container.setLayout(layout);

		String resourceLabelContens = "Resource bundle accessor class(will be created if it does not exist):";
		createClassLabel(container, resourceLabelContens);

		createClassFolderField(container);
		createClassPackageField(container);
		createClassNameField(container);

		drawLine(container);

		String propertyLabelContens = "Property file location and name:";
		createPropertyLabel(container, propertyLabelContens);

		createPropertyFolderField(container);
		createPropertyPackageField(container);
		createPropertyNameField(container);

		getExistPropertyFile();

		return parent;
	}

	/**
	 * draw a line on container.
	 * 
	 * @param container
	 */
	private void drawLine(Composite container) {
		Label hLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		gridData.heightHint = 20;
		hLabel.setLayoutData(gridData);
	}

	private void createClassLabel(Composite container, String labelContents) {
		Label classLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 20;
		classLabel.setText(labelContents);
		classLabel.setLayoutData(gridData);
	}

	private void createPropertyLabel(Composite container, String labelContents) {
		propertyLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 20;
		gridData.widthHint = 350;
		propertyLabel.setText(labelContents);
		propertyLabel.setLayoutData(gridData);
	}

	private void createClassFolderField(Composite container) {
		Label sourceFolderLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		sourceFolderLabel.setText("Source folder:");
		sourceFolderLabel.setLayoutData(gridData);

		sourceFolderText = new Text(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		sourceFolderText.setLayoutData(gridData);
		sourceFolderText.setText(info.getSourceFolder());
		sourceFolder = sourceFolderText.getText();
		sourceFolderText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				sourceFolder = sourceFolderText.getText();
				setNewRoot("/" + sourceFolder, true);
				checkFomat();
			}
		});
		Button sourceFolderButton = new Button(container, SWT.PUSH);
		sourceFolderButton.setText("Browse...");
		sourceFolderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				classRoot = chooseSourceContainer(classRoot);
				IPath path = classRoot.getPath();
				sourceFolderText.setText(path.toString().substring(1));
			}
		});
	}

	private void createClassPackageField(Composite container) {
		Label sourcePackageLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		sourcePackageLabel.setText("Package:");
		sourcePackageLabel.setLayoutData(gridData);

		Composite classPackageField = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		classPackageField.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		classPackageField.setLayoutData(gridData);

		sourcePackageText = new Text(classPackageField, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		sourcePackageText.setLayoutData(gridData);
		sourcePackageText.setText(info.getSourcePackage());
		sourcePackage = sourcePackageText.getText();
		sourcePackageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				sourcePackage = sourcePackageText.getText();
				checkFomat();
				if (getSourcePackage() == null || ("").equals(getSourcePackage())) {
					classDefaultLabel.setText("(default)");
				}
			}
		});

		classDefaultLabel = new Label(classPackageField, SWT.NONE);
		gridData = new GridData();
		gridData.widthHint = 100;
		classDefaultLabel.setLayoutData(gridData);

		sourcePackagButton = new Button(container, SWT.PUSH);
		sourcePackagButton.setText("Browse...");
		sourcePackagButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Widget widget = e.widget;
				choosePackage(widget, classRoot);
			}
		});
	}

	private void createClassNameField(Composite container) {
		Label classNameLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		classNameLabel.setText("Class name:");
		classNameLabel.setLayoutData(gridData);

		classNameText = new Text(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		classNameText.setLayoutData(gridData);
		classNameText.setText(info.getClassName());
		className = classNameText.getText();
		classNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				className = classNameText.getText();
				checkFomat();
			}
		});

		chooseClassTypeButton = new Button(container, SWT.PUSH);
		chooseClassTypeButton.setText("Browse...");
		chooseClassTypeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseForAccessorClass(classRoot);
			}
		});
	}

	private void createPropertyFolderField(Composite container) {
		Label propertyFolderLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		propertyFolderLabel.setText("Source folder:");
		propertyFolderLabel.setLayoutData(gridData);

		propertyFolderText = new Text(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		propertyFolderText.setLayoutData(gridData);
		propertyFolderText.setText(info.getPropertyFolder());
		propertyFolder = propertyFolderText.getText();
		propertyFolderText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				propertyFolder = propertyFolderText.getText();
				setNewRoot("/" + propertyFolder, false);
				checkFomat();
			}
		});

		propertyFolderButton = new Button(container, SWT.PUSH);
		propertyFolderButton.setText("Browse...");
		propertyFolderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				propertyRoot = chooseSourceContainer(propertyRoot);
				IPath path = propertyRoot.getPath();
				propertyFolderText.setText(path.toString().substring(1));
			}
		});
	}

	private void createPropertyPackageField(Composite container) {
		Label propertyPackageLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		propertyPackageLabel.setText("Package:");
		propertyPackageLabel.setLayoutData(gridData);

		Composite propertyPackageField = new Composite(container, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		propertyPackageField.setLayout(layout);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		propertyPackageField.setLayoutData(gridData);

		propertyPackageText = new Text(propertyPackageField, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		propertyPackageText.setLayoutData(gridData);
		propertyPackageText.setText(info.getPropertyPackage());
		propertyPackage = propertyPackageText.getText();
		propertyPackageText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				propertyPackage = propertyPackageText.getText();
				checkFomat();
				if (getPropertyPackage() == null || ("").equals(getPropertyPackage())) {
					propertyDefaultLabel.setText("(default)");
				}
			}
		});

		propertyDefaultLabel = new Label(propertyPackageField, SWT.NONE);
		gridData = new GridData();
		gridData.widthHint = 100;
		propertyDefaultLabel.setLayoutData(gridData);

		propertyPackagButton = new Button(container, SWT.PUSH);
		propertyPackagButton.setText("Browse...");
		propertyPackagButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Widget widget = e.widget;
				choosePackage(widget, propertyRoot);
			}
		});
	}

	private void createPropertyNameField(Composite container) {
		Label propertyNameLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData();
		propertyNameLabel.setText("Porperty File name:");
		propertyNameLabel.setLayoutData(gridData);

		propertyNameText = new Text(container, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		propertyNameText.setLayoutData(gridData);
		propertyNameText.setText(info.getPropertyName());
		propertyName = propertyNameText.getText();
		propertyNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				propertyName = propertyNameText.getText();
				checkFomat();
			}
		});

		choosePropertyTypeButton = new Button(container, SWT.PUSH);
		choosePropertyTypeButton.setText("Browse...");
		choosePropertyTypeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				browseForPropertyFile();
			}
		});
	}

	protected void browseForAccessorClass(IPackageFragmentRoot root) {
		IProgressService service = PlatformUI.getWorkbench().getProgressService();
		IJavaSearchScope scope = root != null ? SearchEngine.createJavaSearchScope(new IJavaElement[] { root }) : SearchEngine.createWorkspaceScope();
		FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(getShell(), false, service, scope, IJavaSearchConstants.CLASS);
		dialog.setTitle("Choose a Type");
		dialog.setInitialPattern("*Messages");
		classFileNames = createFileListInput(".java", classFragment);
		if (dialog.open() == Window.OK) {
			IType selectedType = (IType) dialog.getFirstResult();
			if (selectedType != null) {
				classNameText.setText(selectedType.getElementName());
			}
		}
	}

	private void browseForPropertyFile() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider());
		dialog.setIgnoreCase(false);
		dialog.setTitle("Choose the property file:");
		dialog.setMessage("Choose the property file:");
		dialog.setElements(createFileListInput(".properties", propertyFragment));
		propertyFileNames = createFileListInput(".properties", propertyFragment);
		dialog.setFilter('*' + "properties");
		if (dialog.open() == Window.OK) {
			IFile selectedFile = (IFile) dialog.getFirstResult();
			if (selectedFile != null)
				propertyNameText.setText(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf(".properties")));
		}
	}

	private Object[] createFileListInput(String extension, IPackageFragment fragment) {
		try {
			if (fragment == null)
				return new Object[0];
			List result = new ArrayList(1);
			if (".java".equals(extension)) {
				Object[] java = fragment.getChildren();
				return java;
			}
			Object[] nonjava = fragment.getNonJavaResources();
			for (int i = 0; i < nonjava.length; i++) {
				if (isAlrightFile(nonjava[i], extension))
					result.add(nonjava[i]);
			}
			return result.toArray();
		} catch (JavaModelException e) {
			return new Object[0];
		}
	}

	private static boolean isAlrightFile(Object o, String extension) {
		if (!(o instanceof IFile))
			return false;
		IFile file = (IFile) o;
		return (extension.equals('.' + file.getFileExtension()));
	}

	/**
	 * Set the root null.
	 * 
	 * @param isClass
	 */
	private void setRootNull(boolean isClass) {
		if (isClass) {
			classRoot = null;
		} else {
			propertyRoot = null;
		}
	}

	/**
	 * While user change the sourceFolderText or propertyFolderText's nameMap,refresh the root.
	 * 
	 * @param path
	 */
	private void setNewRoot(String path, boolean isClass) {
		int start = path.indexOf("/");
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
		if (path.length() == 0 || container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			setRootNull(isClass);
		} else if ((path.indexOf("/", path.indexOf("/", start + 1) + 1) != -1) || (path.indexOf("\\") != -1)) {
			setRootNull(isClass);
		} else {
			try {
				final IFile file = ExternalizeStringsCommon.getIFile(path, "");
				IJavaProject javaProject = JavaCore.create(file.getProject());
				IPackageFragmentRoot fragmentRoot[] = javaProject.getAllPackageFragmentRoots();
				for (int i = 0; i < fragmentRoot.length; i++) {
					if (fragmentRoot[i].getResource() != null) {
						if (isClass) {
							classRoot = fragmentRoot[i];
						} else {
							propertyRoot = fragmentRoot[i];
						}
						break;
					}
				}

			} catch (CoreException e) {
				LoggerManager.log(e);
			}
		}
	}

	protected void choosePackage(Widget widget, IPackageFragmentRoot root) {
		IJavaElement[] packages = null;
		try {
			if (root != null && root.exists()) {
				packages = root.getChildren();
			}
		} catch (JavaModelException e) {
			// no need to react
		}
		if (packages == null) {
			packages = new IJavaElement[0];
		}
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setIgnoreCase(true);

		dialog.setTitle("Package Selection");
		dialog.setMessage("Choose the destination package:");
		dialog.setElements(packages);

		if (dialog.open() == Window.OK) {
			IPackageFragment fragment = (IPackageFragment) dialog.getFirstResult();
			if (widget.equals(sourcePackagButton)) {
				sourcePackageText.setText(fragment.getElementName());
				// classFilePath = fragment.getPath().toString();
				classFragment = fragment;
			} else if (widget.equals(propertyPackagButton)) {
				propertyPackageText.setText(fragment.getElementName());
				// propertyFilePath = fragment.getPath().toString();
				propertyFragment = fragment;
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for the container field.
	 */
	private IPackageFragmentRoot chooseSourceContainer(IPackageFragmentRoot root) {
		Class[] acceptedClasses = new Class[] { IPackageFragmentRoot.class, IJavaProject.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(acceptedClasses, false) {
			public boolean isSelectedValid(Object element) {
				try {
					if (element instanceof IJavaProject) {
						IJavaProject jproject = (IJavaProject) element;
						IPath path = jproject.getProject().getFullPath();
						return (jproject.findPackageFragmentRoot(path) != null);
					} else if (element instanceof IPackageFragmentRoot) {
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					}
					return true;
				} catch (JavaModelException e) {
					JavaPlugin.log(e.getStatus()); // just log, no ui in validation
				}
				return false;
			}
		};

		acceptedClasses = new Class[] { IJavaModel.class, IPackageFragmentRoot.class, IJavaProject.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IPackageFragmentRoot) {
					try {
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					} catch (JavaModelException e) {
						JavaPlugin.log(e.getStatus()); // just log, no ui in validation
						return false;
					}
				}
				return super.select(viewer, parent, element);
			}
		};

		StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setComparator(new JavaElementComparator());
		dialog.setTitle("Source Folder Selection");
		dialog.setMessage("Choose a source folder:");
		dialog.addFilter(filter);
		dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));

		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			if (element instanceof IJavaProject) {
				IJavaProject jproject = (IJavaProject) element;
				return jproject.getPackageFragmentRoot(jproject.getProject());
			} else if (element instanceof IPackageFragmentRoot) {
				return (IPackageFragmentRoot) element;
			}
			return null;
		}
		return root;
	}

	/**
	 * Ensures that both file path are set.
	 */
	private void checkFilePath(String path, boolean isPackage, boolean isClass) {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(path));
		if (path.length() == 0 && !isPackage) {
			updateStatus("Both sources folder must be specified");
			return;
		}
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			if (isPackage && !("").equals(path)) {
				updateStatus("The apecified package for the resource bundle is invalid.");
				return;
			} else if (!isPackage) {
				updateStatus("Both source folders must exist");
				return;
			}
		}
		if (!container.isAccessible()) {
			updateStatus("Both projects must be writable");
			return;
		}
		if (!isPackage) {
			int start = path.indexOf("/");
			if ((path.indexOf("/", path.indexOf("/", start + 1) + 1) != -1) || (path.indexOf("\\") != -1)) {
				updateStatus("Both source folders must exist");
				setRootNull(isClass);
				return;
			}
		}
		updateStatus("");
	}

	/**
	 * Ensures that both file path are set.
	 */
	private void checkPackage(String path, String packageName, boolean isProperty) {
		if (packageName == null || packageName.length() == 0) {
			if (isProperty) {
				updateStatus("Incorret package.");
				return;
			}
		}
		if ((packageName.indexOf("/") != -1) || (packageName.indexOf("\\") != -1)) {
			updateStatus("The apecified package for the resource bundle is invalid.");
			return;
		}
		checkFilePath(path, true, !isProperty);
	}

	/**
	 * check the name of the file.
	 */
	private void checkFileName() {
		String regex = "^([_|a-zA-Z])[_\\w]*$";
		if ("".equals(getClassName())) {
			updateStatus("The class name must be specified.");
		} else {
			Matcher matcher = Pattern.compile(regex).matcher(className);
			if (!(matcher.find())) {
				updateStatus("The type name\"" + className + "\"is not a valid indentifier.");
			} else {
				updateStatus("");
			}
		}
	}

	/**
	 * Check all the text value fomat.
	 * 
	 * @param message
	 */
	private void checkFomat() {
		checkFilePath("/" + sourceFolder, false, true);
		if (isError)
			return;
		checkFilePath("/" + propertyFolder, false, false);
		if (isError)
			return;
		checkPackage(getClassFilePath(), getSourcePackage(), false);
		if (isError)
			return;
		checkFileName();
		if (isError)
			return;
		checkPackage(getPropertyFilePath(), getPropertyPackage(), true);
		if (isError)
			return;
		if ("".equals(getPropertyName())) {
			updateStatus("The Property File Name must be specified. ");
		} else {
			updateStatus("");
			getExistPropertyFile();
		}
	}

	private void updateStatus(String message) {
		if (message != null && !"".equals(message)) {
			isError = true;
			fLastStatus = new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, -1, message, null);
			setPropertyFieldEnable(true);
		} else {
			isError = false;
			fLastStatus = new Status(IStatus.OK, Policy.JFACE, IStatus.OK, Util.ZERO_LENGTH_STRING, null);
		}
		updateStatus(fLastStatus);
	}

	private void getExistPropertyFile() {
		try {
			final IFile file = ExternalizeStringsCommon.getIFile(getClassFilePath(), className + ".java");
			int keyBegin = getClassFilePath().indexOf("/", 1) + 1;
			int keyEnd = getClassFilePath().indexOf("/", keyBegin + 1);
			String sourceKey = getClassFilePath().substring(keyBegin, keyEnd);
			if (file.exists()) {
				// Get the property file's folder.
				IJavaProject javaProject = JavaCore.create(file.getProject());
				String propertyFolder = javaProject.getElementName() + "/" + sourceKey;
				// Get the property file's package and name.
				StringBuffer contents = ExternalizeStringsCommon.getHistoryContents(file);
				String findKey = "BUNDLE_NAME";
				int index = contents.toString().indexOf(findKey);
				int start = contents.indexOf("\"", index);
				int end = contents.indexOf("\"", start + 1);
				String path = contents.substring(start + 1, end);
				String propertyPackage = path.substring(0, path.lastIndexOf("."));
				String propertyName = path.substring(path.lastIndexOf(".") + 1);
				if (!propertyFolder.equals(propertyFolderText.getText())) {
					propertyFolderText.setText(propertyFolder);
				}
				if (!propertyPackage.equals(propertyPackageText.getText())) {
					propertyPackageText.setText(propertyPackage);
				}
				if (!propertyName.equals(propertyNameText.getText())) {
					propertyNameText.setText(propertyName);
				}
				setPropertyFieldEnable(false);
			} else {
				setPropertyFieldEnable(true);
			}
		} catch (CoreException e) {
			LoggerManager.log(e);
		} catch (IOException e) {
			LoggerManager.log(e);
		}
	}

	/**
	 * Notifies that the ok button of this dialog has been pressed.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method sets this dialog's return code to <code>Window.OK</code> and closes the dialog. Subclasses may override.
	 * </p>
	 */
	protected void okPressed() {
		if (checkFileExist(getClassName(), ".java", classFileNames, getClassFilePath())) {
			return;
		}
		if (checkFileExist(getPropertyName(), ".properties", propertyFileNames, getPropertyFilePath())) {
			return;
		}
		setReturnCode(OK);
		close();
	}

	/**
	 * Check the file whether already exist with different case.
	 */
	private boolean checkFileExist(String name, String extension, Object[] names, String path) {
		Object[] fileNames = names;
		String fileExist;
		for (int i = 0; i < fileNames.length; i++) {
			if (".java".equals(extension)) {
				fileExist = ((IJavaElement) fileNames[i]).getElementName();
			} else {
				fileExist = ((IFile) fileNames[i]).getName();
			}
			String fileExistName = fileExist.substring(0, fileExist.lastIndexOf(extension));
			if ((!name.equals(fileExistName)) && name.toLowerCase().equals(fileExistName.toLowerCase())) {
				showFileExistDialog(name, extension, path);
				return true;
			}
		}
		return false;
	}

	/**
	 * If file already exist but with different case,show the dialog.
	 */
	private void showFileExistDialog(String name, String extension, String path) {
		String dialogMessage = "File '" + path.replace(".", "/") + "/" + name + extension + "' already exist with different case.";
		String[] dialogButtonLabels = { "Ok" };
		MessageDialog messageDialog = new MessageDialog(getShell(), "Configure Accessor Class", null, dialogMessage, MessageDialog.ERROR, dialogButtonLabels, 1);
		messageDialog.open();
	}

	/**
	 * Set the property file configure field disabled or enabled.
	 * 
	 * @param b
	 */
	private void setPropertyFieldEnable(boolean b) {
		String propertyLabelContens;
		if (b) {
			propertyLabelContens = "Property file location and name:";
		} else {
			propertyLabelContens = "Property file location and name(Default exist file):";
		}
		if (!(propertyLabelContens).equals(propertyLabel.getText())) {
			propertyLabel.setText(propertyLabelContens);
		}
		propertyFolderButton.setEnabled(b);
		propertyPackagButton.setEnabled(b);
		choosePropertyTypeButton.setEnabled(b);
		propertyFolderText.setEnabled(b);
		propertyPackageText.setEnabled(b);
		propertyNameText.setEnabled(b);
	}

	public IPackageFragmentRoot getclassRoot() {
		return classRoot;
	}

	public String getClassFilePath() {
		return "/" + sourceFolder + "/" + getSourcePackage().replace(".", "/");
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public String getSourcePackage() {
		return sourcePackage;
	}

	public String getClassName() {
		return className;
	}

	public IPackageFragmentRoot getPropertyRoot() {
		return propertyRoot;
	}

	public String getPropertyFolder() {
		return propertyFolder;
	}

	public String getPropertyFilePath() {
		return "/" + propertyFolder + "/" + getPropertyPackage().replace(".", "/");
	}

	public String getPropertyPackage() {
		return propertyPackage;
	}

	public String getPropertyName() {
		return propertyName;
	}

}
