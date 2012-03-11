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
package org.eclipse.e4.xwt.tools.ui.designer.databinding.ui;

import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.internal.forms.widgets.FormFonts;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class AdvancedBindingDialog extends TitleAreaDialog {

	private BindingInfo binding;
	private OptionsGroup optionsGroup;
	private CodeGenGroup codeGenGroup;
	private Text elementNameText;

	public AdvancedBindingDialog(Shell parentShell, BindingInfo binding) {
		super(parentShell);
		this.binding = binding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Advanced Binding Dialog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		Composite control = new Composite(comp, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		control.setLayout(new GridLayout(2, false));

		createBoldLabel(control, "Target:");
		Label targetLabel = new Label(control, SWT.NONE);
		targetLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		BindingContext bindingContext = binding.getBindingContext();
		{
			IObservable target = bindingContext.getTarget();
			String observeName = target.getDisplayName();
			Property property = bindingContext.getTargetProperty();
			if (observeName != null && property != null) {
				targetLabel.setText(observeName + "." + property);
			}
		}

		createBoldLabel(control, "Model: ");
		Label modelLabel = new Label(control, SWT.NONE);
		modelLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		IObservable model = bindingContext.getModel();
		{
			String observeName = model.getDisplayName();
			Property property = bindingContext.getModelProperty();
			if (observeName != null && property != null) {
				modelLabel.setText(observeName + "." + property);
			}
		}
		if (model.getType() == IObservable.OBSERVE_SWT_JFACE) {
			createBoldLabel(control, "ElementName: ");
			elementNameText = new Text(control, SWT.BORDER);
			elementNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			if (binding.getElementName() != null) {
				elementNameText.setEditable(false);
				elementNameText.setText(binding.getElementName());
			}
		}

		// Label separator = new Label(control, SWT.HORIZONTAL | SWT.SEPARATOR);
		// GridData dd = new GridData(GridData.FILL_HORIZONTAL);
		// dd.horizontalSpan = 2;
		// separator.setLayoutData(dd);

		final Composite bottom = new Composite(control, SWT.BORDER);
		bottom.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		bottom.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalSpan = 2;
		bottom.setLayoutData(layoutData);

		optionsGroup = new OptionsGroup();
		final ExpandableComposite optGroup = optionsGroup.createGroup(bottom, SWT.NONE);
		optGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		optionsGroup.setInput(binding);

		codeGenGroup = new CodeGenGroup();
		final ExpandableComposite codeGroup = codeGenGroup.createGroup(bottom, SWT.NONE);
		codeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		codeGenGroup.setInput(binding.getCodeStyles());

		bottom.getDisplay().asyncExec(new Runnable() {
			public void run() {
				optGroup.setExpanded(false);
				codeGroup.setExpanded(false);
				bottom.layout();
			}
		});

		setTitle("Advanced Options");
		setMessage("Choose mode and converter for create data binding.");
		return control;
	}

	@SuppressWarnings("restriction")
	private void createBoldLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		GridData gd = new GridData();
		gd.horizontalIndent = 5;
		label.setLayoutData(gd);
		label.setFont(FormFonts.getInstance().getBoldFont(label.getDisplay(), label.getFont()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		binding.setBindingMode(optionsGroup.getBindingMode());
		binding.setTriggerMode(optionsGroup.getUpdateSourceTrigger());
		Class<?> converter = optionsGroup.getConverter();
		if (converter != null) {
			binding.setConverter(converter.getName());
		}
		if (elementNameText != null) {
			binding.setElementName(elementNameText.getText());
		}
		binding.setCodeStyles(codeGenGroup.getCodeStyles());
		super.okPressed();
	}
}
