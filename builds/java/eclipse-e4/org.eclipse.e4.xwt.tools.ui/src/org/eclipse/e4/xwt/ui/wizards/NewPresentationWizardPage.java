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
package org.eclipse.e4.xwt.ui.wizards;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.javabean.metadata.Metaclass;
import org.eclipse.e4.xwt.ui.ExceptionHandle;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.e4.xwt.ui.jdt.ASTHelper;
import org.eclipse.e4.xwt.ui.jdt.ProjectHelper;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

public class NewPresentationWizardPage extends org.eclipse.jdt.ui.wizards.NewClassWizardPage {
	protected String superClass;

	protected IResource guiResource;

	private boolean shouldGenMetaclass = false;

	private StringButtonDialogField fieldEditor;
	private Button metaclassButton;

	private IPackageFragment packageFragment;
	
	private String dataContext;

	public NewPresentationWizardPage() {
		setTitle("New Wizard Creation");
		setDescription("This wizard creates a data presentation.");
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// pick & choose the wanted UI components

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);

		createTypeNameControls(composite, nColumns);

		createSeparator(composite, nColumns);
		{
			new Label(composite, SWT.NONE);
			metaclassButton = new Button(composite, SWT.CHECK);
			metaclassButton.setSelection(shouldGenMetaclass);
			metaclassButton.setText("Generating Metaclass");
			GridData data = new GridData();
			data.horizontalSpan = 3;
			metaclassButton.setLayoutData(data);
			metaclassButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shouldGenMetaclass = metaclassButton.getSelection();
				}
			});

			createDataContextComp(composite);
		}

		createCommentControls(composite, nColumns);
		enableCommentControl(true);

		createSeparator(composite, nColumns);

		setControl(composite);

		NewElementWizard wizard = (NewElementWizard) getWizard();
		IStructuredSelection selection = (IStructuredSelection) wizard.getSelection();
		IType dataContextType = null;
		for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			if (element instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) element;
				dataContextType = unit.findPrimaryType();
				break;
			}
			if (element instanceof IType) {
				dataContextType = (IType) element;
				break;
			}
		}

		if (dataContextType != null) {
			setTypeName(dataContextType.getElementName(), true);
			fieldEditor.setText(dataContextType.getFullyQualifiedName('.'));
			metaclassButton.setSelection(shouldGenMetaclass);
		}
		
		Dialog.applyDialogFont(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
	}

	/**
	 * @param composite
	 * @return
	 */
	private void createDataContextComp(Composite composite) {
		DataContextFieldAdapter adapter = new DataContextFieldAdapter();
		fieldEditor = new StringButtonDialogField(adapter);
		fieldEditor.setDialogFieldListener(adapter);
		fieldEditor.setButtonLabel("Browse...");
		fieldEditor.setLabelText("Data Context Type:");
		fieldEditor.doFillIntoGrid(composite, 4);
		LayoutUtil.setWidthHint(fieldEditor.getTextControl(null), getMaxFieldWidth());
	}

	private class DataContextFieldAdapter implements IStringButtonAdapter, IDialogFieldListener {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			IType type = chooseDataContext();
			if (type != null) {
				((StringButtonDialogField) field).setText(type.getFullyQualifiedName());
			}
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			dataContext = ((StringButtonDialogField) field).getText();
			validateDataContext();
		}
	}

	public boolean isCreateConstructors() {
		return true;
	}

	/**
	 * 
	 */
	public void validateDataContext() {
		String newMessage = "Invalid Java Type for initializing DataContext.";
		String errorMessage = getErrorMessage();
		if (dataContext != null) {
			ProjectContext context = ProjectContext.getContext(getJavaProject());
			try {
				context.getClassLoader().loadClass(dataContext);
				if (newMessage.equals(errorMessage)) {
					setErrorMessage(null);
				} else {
					setErrorMessage(errorMessage);
				}
			} catch (ClassNotFoundException e) {
				setErrorMessage(newMessage);
			}
		} else {
			if (newMessage.equals(errorMessage)) {
				setErrorMessage(null);
			} else {
				setErrorMessage(errorMessage);
			}
		}
	}

	/**
	 * @param field
	 */
	public IType chooseDataContext() {
		IJavaProject project = getJavaProject();
		if (project == null) {
			return null;
		}

		IJavaElement[] elements = new IJavaElement[] { project };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

		FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(getShell(), false, getWizard().getContainer(), scope, IJavaSearchConstants.CLASS);
		dialog.setTitle("Choose a JavaBean");
		dialog.setMessage("Choose a JavaBean as a DataContext Type.");
		dialog.setInitialPattern("java.lang.Object");

		if (dialog.open() == Window.OK) {
			return (IType) dialog.getFirstResult();
		}
		return null;
	}

	@Override
	protected void initTypePage(IJavaElement elem) {
		super.initTypePage(elem);
		setSuperClass(getSuperClassName(), false);
	}
	
	@Override
	public void setPackageFragment(IPackageFragment pack, boolean canBeModified) {
		
		
		String name = pack.getElementName() + ".ui";
		IPackageFragmentRoot root = (IPackageFragmentRoot) pack.getParent();
		IPackageFragment packageFragment = root.getPackageFragment(name);
		super.setPackageFragment(packageFragment, canBeModified);
	}
	
	protected String getSuperClassName() {
		return Composite.class.getName();
	}

	public int getModifiers() {
		return F_PUBLIC;
	}

	/**
	 * Returns the chosen super interfaces.
	 * 
	 * @return a list of chosen super interfaces. The list's elements are of type <code>String</code>
	 */
	public List getSuperInterfaces() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the current selection state of the 'Create Main' checkbox.
	 * 
	 * @return the selection state of the 'Create Main' checkbox
	 */
	public boolean isCreateMain() {
		return false;
	}

	protected InputStream getContentStream() {
		IType type = getCreatedType();
		String hostClassName = type.getFullyQualifiedName();
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(arrayOutputStream);

		printStream.println("<Composite xmlns=\"" + IConstants.XWT_NAMESPACE + "\"");

		printStream.println("\t xmlns:x=\"" + IConstants.XWT_X_NAMESPACE + "\"");
		String packageName = type.getPackageFragment().getElementName();
		if (packageName != null/* && packageName.length() > 0 */) {
			printStream.println("\t xmlns:c=\"" + IConstants.XAML_CLR_NAMESPACE_PROTO + packageName + "\"");
		}
		printStream.println("\t xmlns:j=\"" + IConstants.XAML_CLR_NAMESPACE_PROTO +  "java.lang\"");
		printStream.println("\t x:Class=\"" + hostClassName + "\">");
		printStream.println("\t <Composite.layout>");
		printStream.println("\t\t <GridLayout " + " numColumns=\"4\" />");
		printStream.println("\t </Composite.layout>");

		appendBeanContent(printStream);

		printStream.println("</Composite>");

		try {
			byte[] content = arrayOutputStream.toByteArray();
			printStream.close();
			arrayOutputStream.close();
			return new ByteArrayInputStream(content);
		} catch (Exception e) {
			XWTUIPlugin.log(e);
			ExceptionHandle.handle(e, "save failed in the file: " + getModifiedResource().getLocation());
		}
		return new ByteArrayInputStream(new byte[] {});
	}

	/**
	 * @param printStream
	 */
	private void appendBeanContent(PrintStream printStream) {
		Class<?> type = getDataContextType();
		if (type == null) {
			return;
		}
		try {
			BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {
				String name = pd.getName();
				if (name == null || "class".equals(name)) {
					continue;
				}
				Class<?> propertyType = pd.getPropertyType();
				if (propertyType.isPrimitive() || propertyType == String.class || propertyType == URL.class) {
					printStream.println("\t <Label text=\"" + pd.getDisplayName() + "\"/>");
					printStream.println("\t <Text x:Style=\"Border\" text=\"{Binding path=" + pd.getName() + "}\">");
					printStream.println("\t\t <Text.layoutData>");
					printStream.println("\t\t\t <GridData grabExcessHorizontalSpace=\"true\"");
					printStream.println("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"100\"/>");
					printStream.println("\t\t </Text.layoutData>");
					printStream.println("\t </Text>");
				} else if (propertyType.isEnum()) {
					printStream.println("\t <Label text=\"" + pd.getDisplayName() + "\"/>");
					printStream.println("\t <Combo text=\"{Binding path=" + pd.getName() + "}\">");
					printStream.println("\t\t <Combo.layoutData>");
					printStream.println("\t\t\t <GridData grabExcessHorizontalSpace=\"true\"");
					printStream.println("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"100\"/>");
					printStream.println("\t\t </Combo.layoutData>");
					
					printStream.println("\t\t <Combo.items>");
					for (Object object : propertyType.getEnumConstants()) {
						printStream.println("\t\t\t <j:String>" + object.toString() + "</j:String>");						
					}
					printStream.println("\t\t </Combo.items>");
					printStream.println("\t </Combo>");
					
				} else {
					printStream.println("\t <Group text=\"" + pd.getDisplayName() + "\">");
					printStream.println("\t\t <Group.layout>");
					printStream.println("\t\t\t <FillLayout/>");
					printStream.println("\t\t </Group.layout>");

					String elementType = propertyType.getSimpleName();
					printStream.println("\t\t <c:" + elementType + " DataContext=\"{Binding path=" + pd.getName() + "}\"/>");

					printStream.println("\t\t <Group.layoutData>");
					printStream.println("\t\t\t <GridData grabExcessHorizontalSpace=\"true\" horizontalSpan=\"4\"");
					printStream.println("\t\t\t\t horizontalAlignment=\"GridData.FILL\" widthHint=\"200\"/>");
					printStream.println("\t\t </Group.layoutData>");

					printStream.println("\t </Group>");
				}
			}
		} catch (IntrospectionException e) {
		}
	}

	public void createType(IProgressMonitor monitor) throws CoreException, InterruptedException {
		// Add external Jars before create a new Java Source Type.
		ProjectHelper.checkDependenceJars(getJavaProject());
		super.createType(monitor);

		if (shouldGenMetaclass) {
			createMetaclass();
		}

		IResource resource = getModifiedResource();
		IPath resourcePath = resource.getProjectRelativePath().removeFileExtension();
		resourcePath = resourcePath.addFileExtension(IConstants.XWT_EXTENSION);
		try {
			IFile file = resource.getProject().getFile(resourcePath);
			file.create(getContentStream(), IResource.FORCE | IResource.KEEP_HISTORY, monitor);
			guiResource = file;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandle.handle(e, "save failed in the file: " + getModifiedResource().getLocation());
		}
		return;
	}

	public void createMetaclass() {
		IType type = getCreatedType();
		String typeName = type.getElementName();
		String metaclassName = typeName + "Metaclass";
		IType metaclassType = ASTHelper.createType(getPackageFragment(), metaclassName, null, Metaclass.class);
		if (metaclassType != null && dataContext != null) {
			ASTHelper.createMethod(metaclassType, "getDataContentType", getDataContextType().getClass(), "" + getDataContextType().getName() + ".class", null);
		}
		ICompilationUnit cu = metaclassType.getCompilationUnit();
		try {
			cu.commitWorkingCopy(true, new NullProgressMonitor());
		} catch (JavaModelException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.NewTypeWizardPage#getTypeName()
	 */
	public String getTypeName() {
		String typeName = super.getTypeName();
		if (typeName == null || typeName.equals("")) {
			return typeName;
		}
		/*
		 * Make sure the first character of the new Class name is a upperCase one. Because the Element parser of the XWT file convert the top element to this format.
		 */
		return Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
	}

	/**
	 * Returns the current selection state of the 'Create inherited abstract methods' checkbox.
	 * 
	 * @return the selection state of the 'Create inherited abstract methods' checkbox
	 */
	public boolean isCreateInherited() {
		return true;
	}

	public boolean shouldGenMetaclass() {
		return shouldGenMetaclass;
	}

	public Class<?> getDataContextType() {
		if (dataContext == null || dataContext.trim().length() == 0) {
			return null;
		}
		ProjectContext context = ProjectContext.getContext(getJavaProject());
		try {
			return context.getClassLoader().loadClass(dataContext);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	public IResource getGuiResource() {
		return guiResource;
	}
}
