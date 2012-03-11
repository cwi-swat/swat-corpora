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
package org.eclipse.e4.xwt.tools.ui.designer.layouts.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ApplyAttributeSettingCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.CheckBoxFieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditorEvent;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditorListener;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.RadioGroupFieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.SpinnerFieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.model.RefreshAdapter;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class AssistantPage implements IAssistantPage, FieldEditorListener {
	private EditPart editPart;
	private Control control;
	private List<FieldEditor> editors = new ArrayList<FieldEditor>();
	private XamlAttribute assistParentAttr;
	private XamlElement assistModel;

	private Map<FieldEditor, List<RefreshAdapter>> editor2refresher;

	public void setEditPart(EditPart editPart) {
		if (editPart != null && editPart != this.editPart) {
			this.editPart = editPart;
		}
		refresh();
	}

	public EditPart getEditPart() {
		return editPart;
	}

	public XamlNode getModel() {
		if (editPart == null) {
			return null;
		}
		return (XamlNode) editPart.getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#getControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control getControl(Composite parent) {
		if (control == null || control.isDisposed()) {
			control = createControl(parent);
			control.setData(this);
		}
		return control;
	}

	protected void execute(Command command) {
		if (editPart == null || command == null || !command.canExecute()) {
			return;
		}
		EditDomain editDomain = EditDomain.getEditDomain(editPart);
		if (editDomain == null) {
			return;
		}
		CommandStack commandStack = editDomain.getCommandStack();
		if (commandStack == null) {
			return;
		}
		commandStack.execute(command);
	}

	protected RadioGroupFieldEditor createRadio(Composite parent, String groupName, String fieldName, String[][] labelsAndValues) {
		RadioGroupFieldEditor editor = new RadioGroupFieldEditor(fieldName, labelsAndValues, parent, groupName);
		addEditor(editor);
		return editor;
	}

	protected CheckBoxFieldEditor createCheckBox(Composite parent, String fieldName, String labelText) {
		CheckBoxFieldEditor editor = new CheckBoxFieldEditor(fieldName, labelText, parent);
		addEditor(editor);
		return editor;
	}

	protected SpinnerFieldEditor createSpinner(Composite parent, String fieldName, String labelText) {
		SpinnerFieldEditor editor = new SpinnerFieldEditor(fieldName, labelText, parent);
		addEditor(editor);
		return editor;
	}

	protected void addEditor(final FieldEditor editor) {
		editor.addListener(this);
		editors.add(editor);
		setUpRefresher(editor);
	}

	protected void setUpRefresher(final FieldEditor editor) {
		XamlNode model = getAssistModel();
		if (model != null) {
			String fieldName = editor.getFieldName();
			RefreshAdapter refresher = new RefreshAdapter(model, fieldName) {
				protected void performRefresh(Notification msg) {
					Display display = editor.getEditor().getDisplay();
					if (Display.getCurrent() == display) {
						editor.apply(getAssistant());						
					}
					else {
						display.asyncExec(new Runnable() {
							public void run() {
								editor.apply(getAssistant());						
							}
						});
					}
				}
			};
			getRefreshers(editor).add(refresher);
		}
	}

	protected Group createGroup(Composite composite, String groupName, int numColumns) {
		Group group = new Group(composite, SWT.NONE);
		group.setText(groupName);
		group.setLayout(new GridLayout(numColumns, false));
		adapt(group);
		return group;
	}

	protected Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		adapt(composite);
		return composite;
	}

	protected void adapt(Control control) {
		control.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditor.FieldEditorListener#handleEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	public final void handleEvent(FieldEditorEvent event) {
		FieldEditor editor = event.editor;
		List<RefreshAdapter> refreshers = getRefreshers(editor);
		for (RefreshAdapter refresher : refreshers) {
			refresher.setRefreshRequired(false);
		}
		Command command = computeCommand(event);
		if (command != null && command.canExecute()) {
			execute(command);
		}
		for (RefreshAdapter refresher : refreshers) {
			refresher.setRefreshRequired(true);
		}
	}

	protected List<RefreshAdapter> getRefreshers(FieldEditor editor) {
		if (editor == null) {
			return Collections.emptyList();
		}
		if (editor2refresher == null) {
			editor2refresher = new HashMap<FieldEditor, List<RefreshAdapter>>();
		}
		List<RefreshAdapter> result = editor2refresher.get(editor);
		if (result == null) {
			result = new ArrayList<RefreshAdapter>();
			editor2refresher.put(editor, result);
		}
		return result;
	}

	protected Command computeCommand(FieldEditorEvent event) {
		return createCommand(event.field, event.newVlaue);
	}

	protected Command createCommand(String attrName, String attrValue) {
		CompoundCommand command = new CompoundCommand();
		XamlElement assistModel = getAssistModel();
		if (assistModel == null) {
			return null;
		}
		XamlAttribute assistParent = getAssistParent();
		if (assistModel.eContainer() == null) {
			command.add(new AddNewChildCommand(assistParent, assistModel));
		}
		if (assistParent.eContainer() == null) {
			command.add(new AddNewChildCommand(getModel(), assistParent));
		}
		command.add(new ApplyAttributeSettingCommand(assistModel, attrName, IConstants.XWT_NAMESPACE, attrValue));
		return command.unwrap();
	}

	private XamlElement getAssistModel() {
		if (assistModel == null && editPart != null) {
			assistModel = createAssistModel(getModel());
		}
		return assistModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#refresh()
	 */
	public void refresh() {
		// Clear cache models, so we can use latest ones when refreshing.
		this.assistModel = null;
		this.assistParentAttr = null;
		apply(getAssistant());
	}

	protected Object getAssistant() {
		XamlElement assistModel = getAssistModel();
		if (assistModel == null) {
			return null;
		}
		return XWTProxy.createValue(assistModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#performDefault()
	 */
	public void performDefault() {
		XamlElement assistModel = getAssistModel();
		if (assistModel != null) {
			execute(new DeleteCommand(assistModel.getAttributes()));
		}
	}

	protected XamlAttribute getAssistParent() {
		if (assistParentAttr == null) {
			assistParentAttr = createAssistParent(getModel());
		}
		return assistParentAttr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage#dispose()
	 */
	public void dispose() {
		if (control != null) {
			control.dispose();
		}
		editPart = null;
		assistParentAttr = null;
		assistModel = null;
		for (FieldEditor editor : editors) {
			List<RefreshAdapter> list = editor2refresher.get(editor);
			for (RefreshAdapter refresher : list) {
				refresher.dispose();
			}
		}
		editor2refresher.clear();
		editors.clear();
	}

	protected void apply(Object assistant) {
		for (FieldEditor fEditor : editors) {
			setUpRefresher(fEditor);
			fEditor.apply(assistant);
		}
	}

	protected abstract XamlElement createAssistModel(XamlNode parent);

	protected abstract XamlAttribute createAssistParent(XamlNode parent);

	protected abstract Control createControl(Composite parent);

}
