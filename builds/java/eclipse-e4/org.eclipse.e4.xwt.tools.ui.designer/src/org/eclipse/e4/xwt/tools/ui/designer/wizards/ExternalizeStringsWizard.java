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
package org.eclipse.e4.xwt.tools.ui.designer.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.contents.ProperyContents;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.contents.SourceContents;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.pages.ExternalizeStringsWizardPage;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class ExternalizeStringsWizard extends Wizard implements INewWizard {
	private ExternalizeStringsWizardPage externalizeStringsWizardPage;

	private ISelection selection;

	private TextValueModel textValueEntrys;

	private XWTDesigner designer;

	private String classPerfixName;

	private String classFilePath;

	private String className;

	private String propertyFilePath;

	private String propertyName;

	private TextValueModel checkedItems;

	private boolean openFlag;

	private CompoundCommand commandList;

	/**
	 * Constructor for ExternalizeStringsWizard.
	 */
	public ExternalizeStringsWizard(TextValueModel textValueEntrys, XWTDesigner designer) {
		super();
		this.textValueEntrys = textValueEntrys;
		this.designer = designer;
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		setWindowTitle("Externalize Strings");
		externalizeStringsWizardPage = new ExternalizeStringsWizardPage(selection, textValueEntrys);
		addPage(externalizeStringsWizardPage);
		getDefaultAccessorContents();
	}

	private void getDefaultAccessorContents() {
		IFile inputFile = designer.getFile();

		IContainer container = inputFile.getParent();
		IJavaElement javaElement =  JavaCore.create(container);
		IPackageFragmentRoot defaultRoot = null;
		if (javaElement != null && javaElement.exists()) {
			IJavaProject javaProject = JavaCore.create(inputFile.getProject());
			try {
				IPackageFragmentRoot fragmentRoot[] = javaProject.getAllPackageFragmentRoots();
				for (int i = 0; i < fragmentRoot.length; i++) {
					if (fragmentRoot[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
						defaultRoot = fragmentRoot[i];
						for (IJavaElement element : defaultRoot.getChildren()) {
							if (element.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
								javaElement = element;
							}
						}
						break;
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		if (javaElement == null || !(javaElement instanceof IPackageFragment)) {
			String projectName = inputFile.getProject().getName();
			externalizeStringsWizardPage.setErrorMessage("The project " + projectName + " has not source folder.");
			return;
		}

		externalizeStringsWizardPage.setDefaultRoot(defaultRoot);
		externalizeStringsWizardPage.setDefaultFolder(defaultRoot.getResource().getFullPath().toString());
		externalizeStringsWizardPage.setDefaultPackage(javaElement.getElementName());
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		classFilePath = externalizeStringsWizardPage.getInfo().getClassFilePath();
		className = externalizeStringsWizardPage.getInfo().getClassName() + ".java";
		propertyFilePath = externalizeStringsWizardPage.getInfo().getPropertyFilePath();
		propertyName = externalizeStringsWizardPage.getInfo().getPropertyName() + ".properties";
		try {
			doFinish(new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (returnWizardPage e) {
			return false;
		}
		return true;
	}

	/**
	 * Show a dialog to ask user whether to open the created files.
	 * 
	 * @throws returnWizardPage
	 */
	private void showOpenFileDialog() throws returnWizardPage {
		String dialogMessage = "Do you want to open " + externalizeStringsWizardPage.getInfo().getClassName() + ".java and " + externalizeStringsWizardPage.getInfo().getPropertyName() + ".properties after finish?";
		String[] dialogButtonLabels = { "Open", "Cancel" };
		MessageDialog messageDialog = new MessageDialog(getShell(), "Open created files", null, dialogMessage, MessageDialog.QUESTION, dialogButtonLabels, 1);
		messageDialog.open();
		if (messageDialog.getReturnCode() == 0) {
			openFlag = true;
		} else if (messageDialog.getReturnCode() == -1) {
			throw new returnWizardPage();
		} else {
			openFlag = false;
		}
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just replace its nameMap, and open the editor on the newly created file.
	 * 
	 * @param monitor
	 * @throws CoreException
	 * @throws IOException
	 * @throws returnWizardPage
	 */
	private void doFinish(IProgressMonitor monitor) throws CoreException, IOException, returnWizardPage {
		String propertyPerfixName = propertyName.substring(0, propertyName.length() - 11);
		classPerfixName = className.substring(0, className.length() - 5);
		checkedItems = externalizeStringsWizardPage.getCheckedItems();

		showOpenFileDialog();

		// create a property file
		createFile(propertyFilePath, propertyName, propertyPerfixName, false, monitor);
		// create a class file
		createFile(classFilePath, className, propertyPerfixName, true, monitor);
		// externalize string
		final Command changTextCmd = changText();
		DisplayUtil.asyncExec(new Runnable() {
			public void run() {
				RootEditPart rootEditPart = designer.getGraphicalViewer().getRootEditPart();
				rootEditPart.getViewer().getEditDomain().getCommandStack().execute(changTextCmd);
			}
		});
		monitor.worked(1);
	}

	/**
	 * Create a file with file path and file name.
	 * 
	 * @param containerName
	 * @param fileName
	 * @param isClassFile
	 * @param monitor
	 * @throws CoreException
	 * @throws returnWizardPage
	 */
	private void createFile(String containerName, String fileName, String propertyfileName, boolean isClassFile, IProgressMonitor monitor) throws CoreException, returnWizardPage {
		monitor.beginTask("Creating " + fileName, 2);
		final IFile file = ExternalizeStringsCommon.getIFile(containerName, fileName);
		try {
			InputStream stream;
			if (!isClassFile) {
				stream = openPropertyContentStream(checkedItems);
			} else {
				if (file.exists()) {
					// Get the nameMap of the exist file.
					StringBuffer historyContent = ExternalizeStringsCommon
							.getHistoryContents(file);
					// Create inputStream with exist files nameMap.
					stream = openSourceContentStream(checkedItems, containerName, fileName, propertyfileName, historyContent);
				} else {
					// Create a new inputStream.
					stream = openSourceContentStream(checkedItems, containerName, fileName, propertyfileName, null);
				}
			}
			if (file.exists()) {
				if (isClassFile) {
					file.setContents(stream, true, true, monitor);
				} else {
					file.appendContents(stream, true, true, monitor);
				}
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
			// build class file
			IProject project = file.getProject();
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (openFlag) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, file, true);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Initialize source file nameMap with a text.
	 */
	private InputStream openSourceContentStream(TextValueModel checkedItems, String containerName, String fileName, String porpertyfileName, StringBuffer historyContent) {
		IPackageFragmentRoot classRoot = externalizeStringsWizardPage.getInfo().getClassRoot();
		SourceContents sourceContents = new SourceContents(checkedItems, containerName, propertyFilePath, fileName, porpertyfileName, historyContent, classRoot);
		String contents = sourceContents.getSourceContents();
		return new ByteArrayInputStream(contents.getBytes());
	}

	/**
	 * Initialize property file nameMap with a text.
	 */
	private InputStream openPropertyContentStream(TextValueModel checkedItems) {
		ProperyContents properyContents = new ProperyContents(checkedItems);
		String contents = properyContents.getPropertyContents();
		return new ByteArrayInputStream(contents.getBytes());
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * Externalize string.
	 */
	private Command changText() {
		commandList = new CompoundCommand();
		RootEditPart rootEditPart = designer.getGraphicalViewer().getRootEditPart();
		EditPart contents = rootEditPart.getContents();
		Object textValueEntry[] = externalizeStringsWizardPage.getCheckedItems().elements();
		for (int i = 0; i < textValueEntry.length; i++) {
			if (textValueEntry[i] instanceof TextValueEntry) {
				String value = ((TextValueEntry) textValueEntry[i]).getValue();
				String key = ((TextValueEntry) textValueEntry[i]).getKey();
				externalizeText(contents, value, key, "text");
				externalizeText(contents, value, key, "toolTipText");
			}
		}
		return commandList.unwrap();
	}

	/**
	 * Change the parent editPart text value to the key.
	 * 
	 * @param editPart
	 * @param value
	 * @param key
	 */
	private void externalizeText(EditPart editPart, String value, String key, String type) {
		if (editPart instanceof WidgetEditPart) {
			XamlNode node = ((WidgetEditPart) editPart).getCastModel();
			XamlAttribute attribute = node.getAttribute(type, IConstants.XWT_NAMESPACE);
			if (attribute != null) {
				if (value.equals(attribute.getValue())) {

					commandList.add(new DeleteCommand(attribute));
					attribute = null;
					if (attribute == null) {
						attribute = XamlFactory.eINSTANCE.createAttribute(type, IConstants.XWT_NAMESPACE);
					}
					attribute.setUseFlatValue(true);
					XamlElement child = attribute.getChild("Static", IConstants.XWT_X_NAMESPACE);
					if (child == null) {
						child = XamlFactory.eINSTANCE.createElement("Static", IConstants.XWT_X_NAMESPACE);
					}
					String classPakage = externalizeStringsWizardPage.getInfo().getSourcePackage();
					XamlElement childElement = child.getChild(classPerfixName + "." + key, IConstants.XAML_CLR_NAMESPACE_PROTO + classPakage);
					if (childElement != null) {
						child.getChildNodes().remove(childElement);
					}
					childElement = XamlFactory.eINSTANCE.createElement(classPerfixName + "." + key, IConstants.XAML_CLR_NAMESPACE_PROTO + classPakage);

					commandList.add(new AddNewChildCommand(child, childElement));
					commandList.add(new AddNewChildCommand(attribute, child));
					if (!node.getAttributes().contains(attribute)) {
						commandList.add(new AddNewChildCommand(node, attribute));
					}
				}
			}
			if (editPart.getChildren().size() > 0) {
				externalizeChildren(editPart, value, key, type);
			}
		} else if (editPart.getChildren().size() > 0) {
			externalizeChildren(editPart, value, key, type);
		}
	}

	/**
	 * Change the editPart's children text value to the key.
	 * 
	 * @param editPart
	 */
	private void externalizeChildren(EditPart editPart, String value, String key, String type) {
		List child = editPart.getChildren();
		for (int i = 0; i < child.size(); i++) {
			if (child.get(i) instanceof EditPart) {
				externalizeText((EditPart) child.get(i), value, key, type);
			}
		}
	}
}

/**
 * Create a exception to cancel externalize.
 * 
 * @author chenxiaoru
 * 
 */
class returnWizardPage extends Exception {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4274509502796514787L;

	returnWizardPage() {
		super("Return the wizard page");
	}
}