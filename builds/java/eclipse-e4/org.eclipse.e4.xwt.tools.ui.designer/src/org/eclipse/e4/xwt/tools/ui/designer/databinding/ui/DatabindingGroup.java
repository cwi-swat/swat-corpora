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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingHelper;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DatabindingGroup implements IGroup {

	private ObserveTargetGroup targetComp;
	private ObserveModelGroup modelComp;
	private Button bindingAction;
	private BindingTableGroup bindingsTableGroup;
	private static final String ACTION_TOOLTIP_TEXT = "Create data binding use this selection";
	private static final String ACTION_TOOLTIP_MESSAGE = "Use Ctrl for create binding without dialog(if this is possible)";

	private Composite targetGroup;
	private Composite modelGroup;

	private CommandStack commandStack;
	private BindingContext bindingContext;

	private Object input;

	public DatabindingGroup(CommandStack commandStack, BindingContext bindingContext) {
		this.commandStack = commandStack;
		this.bindingContext = bindingContext;
	}

	public Composite createGroup(Composite control, int style) {
		Composite group = new Composite(control, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		Label label = new Label(group, SWT.NONE);
		label.setText("Bound properties");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 5;
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		bindingsTableGroup = new BindingTableGroup(commandStack);
		Composite table = bindingsTableGroup.createGroup(group, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 3;
		table.setLayoutData(layoutData);
		bindingsTableGroup.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				BindingInfo selection = bindingsTableGroup.getSelection();
				handleSelection(selection);
			}
		});

		GridData sepGd = new GridData(GridData.FILL_HORIZONTAL);
		sepGd.horizontalIndent = 5;
		sepGd.horizontalSpan = 3;
		new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(sepGd);

		targetComp = new ObserveTargetGroup(bindingContext, null);
		bindingContext.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				updateBindingAction();
			}
		});
		targetGroup = targetComp.createGroup(group, SWT.NONE);
		targetGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		// targetGroup.setEnabled(false);
		targetGroup.setToolTipText("please select a operation");

		Composite separator = createButtonSeparator(group);
		int gdStyle = GridData.FILL_VERTICAL;
		separator.setLayoutData(new GridData(gdStyle));

		modelComp = new ObserveModelGroup(bindingContext, null);
		modelGroup = modelComp.createGroup(group, SWT.NONE);
		modelGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		// modelGroup.setEnabled(false);
		return group;
	}

	protected void handleSelection(BindingInfo binding) {
		if (binding == null) {
			return;
		}
		BindingContext bc = binding.getBindingContext();
		bindingContext.setModel(bc.getModel());
		bindingContext.setModelProperty(bc.getModelProperty());
		bindingContext.setTarget(bc.getTarget());
		bindingContext.setTargetProperty(bc.getTargetProperty());
		bindingAction.setEnabled(false);
	}

	private Composite createButtonSeparator(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);

		int orientation = SWT.SEPARATOR | SWT.VERTICAL;
		int gdStyle = GridData.FILL_VERTICAL | GridData.HORIZONTAL_ALIGN_CENTER;

		new Label(control, orientation).setLayoutData(new GridData(gdStyle));
		bindingAction = new Button(control, SWT.PUSH);
		bindingAction.setToolTipText(ACTION_TOOLTIP_TEXT + "\n" + ACTION_TOOLTIP_MESSAGE);
		bindingAction.setImage(ImageShop.get(ImageShop.IMG_BINDING_ADD));
		bindingAction.setEnabled(false);
		final Listener listener = new Listener() {
			private boolean useCtrl = false;

			public void handleEvent(Event e) {
				if (SWT.KeyDown == e.type) {
					if (!useCtrl && e.keyCode == SWT.CTRL) {
						useCtrl = true;
					}
				} else if (SWT.KeyUp == e.type) {
					if (useCtrl && e.keyCode == SWT.CTRL) {
						useCtrl = false;
					}
				} else if (SWT.Selection == e.type) {
					createDataBindings(useCtrl);
				} else if (SWT.Dispose == e.type) {
					e.display.removeFilter(SWT.KeyUp, this);
					e.display.removeFilter(SWT.KeyDown, this);
				}
			}
		};
		Display display = bindingAction.getDisplay();
		display.addFilter(SWT.KeyDown, listener);
		display.addFilter(SWT.KeyUp, listener);
		bindingAction.addListener(SWT.Selection, listener);
		bindingAction.addListener(SWT.Dispose, listener);
		new Label(control, orientation).setLayoutData(new GridData(gdStyle));
		return control;
	}

	protected void updateBindingAction() {
		if (bindingAction == null || bindingAction.isDisposed()) {
			return;
		}
		bindingAction.setEnabled(bindingContext.isValid());
	}

	protected void createDataBindings(boolean useCtrl) {
		BindingInfo bindingInfo = new BindingInfo(bindingContext);
		if (useCtrl) {
			createBinding(bindingInfo);
		} else {
			AdvancedBindingDialog dialog = new AdvancedBindingDialog(new Shell(), bindingInfo);
			if (dialog.open() == Window.OK) {
				createBinding(bindingInfo);
			}
		}
	}

	private void createBinding(BindingInfo bindingInfo) {
		Command bindCmd = bindingInfo.bindWithCommand();
		if (bindCmd != null) {
			if (commandStack != null) {
				commandStack.execute(bindCmd);
			} else if (bindCmd.canExecute()) {
				bindCmd.execute();
			}
			if (input == null || !(input instanceof EditPart)) {
				return;
			}
			DisplayUtil.asyncExec(new Runnable() {
				public void run() {
					List<BindingInfo> bindings = BindingHelper.getBindings((EditPart) input);
					bindingsTableGroup.setInput(bindings);
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		if (input == null || !(input instanceof EditPart)) {
			return;
		}
		this.input = input;
		EditPart editPart = (EditPart) input;
		List<BindingInfo> bindings = BindingHelper.getBindings(editPart);
		if (bindings != null && bindingsTableGroup != null) {
			bindingsTableGroup.setInput(bindings);
		}
		if (targetComp != null) {
			targetComp.setRootEditPart(editPart);
		}
		if (modelComp != null) {
			modelComp.setRootEditPart(editPart);
		}
	}

}
