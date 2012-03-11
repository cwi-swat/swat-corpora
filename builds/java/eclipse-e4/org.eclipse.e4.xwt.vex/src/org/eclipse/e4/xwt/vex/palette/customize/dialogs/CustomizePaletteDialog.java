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
package org.eclipse.e4.xwt.vex.palette.customize.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.xwt.vex.EditorMessages;
import org.eclipse.e4.xwt.vex.palette.customize.CustomizeComponentFactory;
import org.eclipse.e4.xwt.vex.palette.customize.InvokeType;
import org.eclipse.e4.xwt.vex.palette.customize.model.CustomizeComponent;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class CustomizePaletteDialog extends TitleAreaDialog {
	// private PaletteViewer paletteViewer;
	private Text textName;
	private Text textScope;
	private Text textIcon;
	private Text textLargeIcon;
	private Text textToolTip;
	private Text textContent;
	InvokeType invokeType;
	CustomizeComponent customizeComponent;
	String templateDnDText;

	public CustomizePaletteDialog(InvokeType invokeType, String componentName, String templateDnDText) {
		super(null);

		this.invokeType = invokeType;
		if (invokeType == InvokeType.Modify) {
			customizeComponent = CustomizeComponentFactory.loadComponent(componentName);
		}
		if (invokeType == InvokeType.DragAdd) {
			this.templateDnDText = templateDnDText;
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		Rectangle screenSize = Display.getDefault().getClientArea();
		Rectangle frameSize = this.getShell().getBounds();
		this.getShell().setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		// Set the title
		setTitle(EditorMessages.CustomizePaletteDialog_Title);
		// Set the message
		setMessage(EditorMessages.CustomizePaletteDialog_Message, IMessageProvider.INFORMATION);
		return contents;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText(EditorMessages.CustomizePaletteDialog_Header);
		newShell.setSize(500, 500);
	}

	/**
	 * create the dialog area
	 * */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		final Composite compositeNew = new Composite(composite, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		compositeNew.setLayout(layout);

		GridData data;
		data = new GridData(GridData.FILL_BOTH);
		compositeNew.setLayoutData(data);

		// name
		Label labelName = new Label(compositeNew, SWT.LEFT);
		labelName.setText(EditorMessages.CustomizePaletteDialog_Name);
		textName = new Text(compositeNew, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		textName.setLayoutData(data);
		textName.setToolTipText(EditorMessages.CustomizePaletteDialog_Name_ToolTip);

		// scope
		Label labelScope = new Label(compositeNew, SWT.LEFT);
		labelScope.setText(EditorMessages.CustomizePaletteDialog_Scope);
		textScope = new Text(compositeNew, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		textScope.setLayoutData(data);
		textScope.setToolTipText(EditorMessages.CustomizePaletteDialog_Scope_ToolTip);

		// icon
		Label labelIcon = new Label(compositeNew, SWT.LEFT);
		labelIcon.setText(EditorMessages.CustomizePaletteDialog_Icon);
		textIcon = new Text(compositeNew, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		data.minimumWidth = 280;
		textIcon.setLayoutData(data);
		textIcon.setToolTipText(EditorMessages.CustomizePaletteDialog_Icon_ToolTip);
		Button buttonBrowseIcon = new Button(compositeNew, SWT.PUSH);
		buttonBrowseIcon.setText(EditorMessages.CustomizePaletteDialog_BrowseIcon);
		buttonBrowseIcon.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog selectionDialog = new ElementTreeSelectionDialog(compositeNew.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				selectionDialog.setAllowMultiple(false);
				selectionDialog.setTitle(EditorMessages.CustomizePaletteDialog_SelectionDialog_Title);
				selectionDialog.setMessage(EditorMessages.CustomizePaletteDialog_SelectionDialog_Text);
				selectionDialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				if (selectionDialog.open() == Window.OK) {
					IFile file = (IFile) selectionDialog.getFirstResult();
					String workspacePath = file.getWorkspace().getRoot().getLocation().toOSString();
					String filePath = file.getFullPath().toOSString();
					textIcon.setText(workspacePath + filePath);
				}
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.minimumWidth = 100;
		buttonBrowseIcon.setLayoutData(data);

		// large icon
		Label labelLargeIcon = new Label(compositeNew, SWT.LEFT);
		labelLargeIcon.setText(EditorMessages.CustomizePaletteDialog_LargeIcon);
		textLargeIcon = new Text(compositeNew, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		textLargeIcon.setLayoutData(data);
		textLargeIcon.setToolTipText(EditorMessages.CustomizePaletteDialog_LargeIcon_ToolTip);
		Button buttonBrowseLargeIcon = new Button(compositeNew, SWT.PUSH);
		buttonBrowseLargeIcon.setText(EditorMessages.CustomizePaletteDialog_BrowseLargeIcon);
		buttonBrowseLargeIcon.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog selectionDialog = new ElementTreeSelectionDialog(compositeNew.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
				selectionDialog.setAllowMultiple(false);
				selectionDialog.setTitle(EditorMessages.CustomizePaletteDialog_SelectionDialog_LargeTitle);
				selectionDialog.setMessage(EditorMessages.CustomizePaletteDialog_SelectionDialog_LargeText);
				selectionDialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				if (selectionDialog.open() == Window.OK) {
					IFile file = (IFile) selectionDialog.getFirstResult();
					String workspacePath = file.getWorkspace().getRoot().getLocation().toOSString();
					String filePath = file.getFullPath().toOSString();
					textLargeIcon.setText(workspacePath + filePath);
				}
			}
		});
		data = new GridData(GridData.FILL_HORIZONTAL);
		buttonBrowseLargeIcon.setLayoutData(data);

		// ToolTip
		Label labelToolTip = new Label(compositeNew, SWT.LEFT);
		labelToolTip.setText(EditorMessages.CustomizePaletteDialog_ToolTip);
		textToolTip = new Text(compositeNew, SWT.SINGLE | SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		textToolTip.setLayoutData(data);
		textToolTip.setToolTipText(EditorMessages.CustomizePaletteDialog_ToolTip_ToolTip);

		// Content
		Label labelContent = new Label(compositeNew, SWT.LEFT);
		labelContent.setText(EditorMessages.CustomizePaletteDialog_Content);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 5;
		labelContent.setLayoutData(data);
		textContent = new Text(compositeNew, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 5;
		textContent.setLayoutData(data);
		textContent.setToolTipText(EditorMessages.CustomizePaletteDialog_Content_ToolTip);

		if (invokeType == InvokeType.DragAdd) {
			textContent.setText(templateDnDText);
		}

		setModifyComponentContent();

		return composite;
	}

	private void setModifyComponentContent() {
		if (invokeType == InvokeType.Modify) {
			textName.setText(customizeComponent.getName());
			textScope.setText(customizeComponent.getScope());
			textIcon.setText(customizeComponent.getIcon());
			textLargeIcon.setText(customizeComponent.getLargeIcon());
			textToolTip.setText(customizeComponent.getTooptip());
			textContent.setText(customizeComponent.getContent());
		}
	}

	@Override
	protected void okPressed() {
		if (doAddCustomizePaletteComponent() == true) {
			super.okPressed();
		}
	}

	private boolean doAddCustomizePaletteComponent() {
		if (!validInput()) {
			return false;
		}

		CustomizeComponentFactory customizeComponentFactory = CustomizeComponentFactory.getCustomizeComponentFactory();

		if ((invokeType != InvokeType.Modify) && customizeComponentFactory.isComponentExist(textName.getText())) {
			MessageDialog.openError(null, EditorMessages.CustomizePaletteDialog_ErrorTitle, EditorMessages.CustomizePaletteDialog_ErrorMessage);
			return false;
		} else if (invokeType == InvokeType.Modify) {
			if (!textName.getText().equals(customizeComponent.getName()) && customizeComponentFactory.isComponentExist(textName.getText())) {
				MessageDialog.openError(null, EditorMessages.CustomizePaletteDialog_ErrorTitle, EditorMessages.CustomizePaletteDialog_ErrorMessage);
				return false;
			}
		}
		CustomizeComponent component = new CustomizeComponent();
		component.setName(textName.getText());
		component.setScope(textScope.getText());
		component.setIcon(textIcon.getText());
		component.setLargeIcon(textLargeIcon.getText());
		component.setTooptip(textToolTip.getText());
		component.setContent(textContent.getText());
		// InvokeType.Modify or add an new one
		if (invokeType == InvokeType.Modify) {
			// modify
			customizeComponentFactory.modifyComponent(component, customizeComponent.getName());
		} else {
			customizeComponentFactory.addComponent(component);
		}
		return true;
	}

	private boolean validInput() {
		boolean result = true;

		// name
		if (textName.getText() == null || textName.getText().trim().equals("")) { //$NON-NLS-1$
			MessageDialog.openError(null, EditorMessages.CustomizePaletteDialog_ErrorTitle, EditorMessages.CustomizePaletteDialog_NameError);
			textName.setFocus();
			textName.selectAll();
			return false;
		}

		if (textContent.getText() == null || textContent.getText().trim().equals("")) { //$NON-NLS-1$
			MessageDialog.openError(null, EditorMessages.CustomizePaletteDialog_ErrorTitle, EditorMessages.CustomizePaletteDialog_ContentError);
			textContent.setFocus();
			textContent.selectAll();
			return false;
		}

		return result;
	}
}
