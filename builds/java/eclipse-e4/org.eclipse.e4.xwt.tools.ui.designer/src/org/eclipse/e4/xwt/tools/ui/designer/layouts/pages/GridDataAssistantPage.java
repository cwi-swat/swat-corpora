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

import java.lang.reflect.Field;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ResizeCommand;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutDataType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.FieldEditorEvent;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.control.SpinnerFieldEditor;
import org.eclipse.e4.xwt.tools.ui.designer.model.RefreshAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.Messages;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class GridDataAssistantPage extends LayoutDataAssistantPage {

	public GridDataAssistantPage() {
		super(LayoutDataType.GridData);
	}

	/**
	 * @see org.soyatec.xaml.ve.xwt.editparts.layouts.pages.CustomizeLayoutPage#creatControl(org.eclipse.swt.widgets.Composite)
	 */
	public Composite createControl(Composite parent) {
		Composite composite = createComposite(parent);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		composite.setLayout(grid);
		// create alignment area.
		createAlignmentArea(composite);
		// create hints area.
		createHintsAndSpanningArea(composite);
		// create minimum area.
		createMinimumArea(composite);
		// create indents area.
		createIndentsArea(composite);
		return composite;
	}

	private void createAlignmentArea(Composite parent) {
		Group aligmentGroup = createGroup(parent, Messages.GridDataAssistantPage_ALIGNS_GROUP_LABEL, 2);

		Group horizontalGroup = createGroup(aligmentGroup, Messages.GridDataAssistantPage_HORIZONTAL_GROUP_LABEL, 1);
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.FILL;
		horizontalGroup.setLayoutData(gd);

		String[][] hAlignRadios = new String[][] { { "Left", "SWT.LEFT" }, { "Center", "GRIDDATA.CENTER" }, { "Right", "SWT.RIGHT" }, { "Fill", "SWT.FILL" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		createRadio(horizontalGroup, null, Assistants.LAYOUTDATA_GRID_HORIZONTAL_ALIGNMENT, hAlignRadios);

		createCheckBox(horizontalGroup, Assistants.LAYOUTDATA_GRID_GRAB_EXCESS_HORIZONTAL_SPACE, Messages.GridDataAssistantPage_H_GRAB_LABEL);

		Group verticalGroup = createGroup(aligmentGroup, Messages.GridDataAssistantPage_VERTICAL_GROUP_LABEL, 1);
		gd = new GridData();
		gd.verticalAlignment = GridData.FILL;
		verticalGroup.setLayoutData(gd);

		String[][] vAlignRadios = new String[][] { { "Top", "SWT.TOP" }, { "Center", "GRIDDATA.CENTER" }, { "Bottom", "SWT.BOTTOM" }, { "Fill", "SWT.FILL" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		createRadio(verticalGroup, null, Messages.GridDataAssistantPage_V_ALIGN_LABEL, vAlignRadios);

		createCheckBox(verticalGroup, Assistants.LAYOUTDATA_GRID_GRAB_EXCESS_VERTICAL_SPACE, Messages.GridDataAssistantPage_V_GRAB_LABEL);

	}

	private void createHintsAndSpanningArea(Composite parent) {
		Composite composite = createComposite(parent);
		GridLayout grid = new GridLayout();
		grid.horizontalSpacing = 0;
		grid.marginWidth = 0;
		grid.marginHeight = 0;
		composite.setLayout(grid);

		Group hintsGroup = createGroup(composite, Messages.GridDataAssistantPage_HINTS_GROUP_NAME, 2);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		hintsGroup.setLayoutData(gd);

		// Create a new width spinner for GridData, so that, we can change both width of the control and widthHint of the GridData now.
		SpinnerFieldEditor widthSpinner = new SpinnerFieldEditor(Assistants.LAYOUTDATA_GRID_WIDTH_HINT, Messages.GridDataAssistantPage_WIDTH_LABEL, hintsGroup) {
			public void apply(Object source) {
				if (!isValid() || source == null) {
					return;
				}
				try {
					Field field = source.getClass().getField(fieldName);
					Object value = field.get(source);
					if (SWT.DEFAULT == (Integer) value) {
						value = getHintValue(Assistants.WIDTH);
					}
					doUpdate(value == null ? "" : value.toString()); //$NON-NLS-1$
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		addEditor(widthSpinner);

		SpinnerFieldEditor heightSpinner = new SpinnerFieldEditor(Assistants.LAYOUTDATA_GRID_HEIGHT_HINT, Messages.GridDataAssistantPage_HEIGHT_LABEL, hintsGroup) {
			public void apply(Object source) {
				if (!isValid() || source == null) {
					return;
				}
				try {
					Field field = source.getClass().getField(fieldName);
					Object value = field.get(source);
					if (SWT.DEFAULT == (Integer) value) {
						value = getHintValue(Assistants.HEIGHT);
					}
					doUpdate(value == null ? "" : value.toString()); //$NON-NLS-1$
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		addEditor(heightSpinner);

		Group spanningGroup = createGroup(composite, Messages.GridDataAssistantPage_SPANNING_GROUP_LABEL, 2);
		spanningGroup.setText("Spanning"); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		spanningGroup.setLayoutData(gd);

		createSpinner(spanningGroup, Assistants.LAYOUTDATA_GRID_HORIZONTAL_SPAN, Messages.GridDataAssistantPage_COLUMN_SPAN_LABEL);
		createSpinner(spanningGroup, Assistants.LAYOUTDATA_GRID_VERTICAL_SPAN, Messages.GridDataAssistantPage_ROW_SPAN_LABEL);

	}

	protected Command computeCommand(FieldEditorEvent event) {
		String field = event.field;
		if (Assistants.LAYOUTDATA_GRID_WIDTH_HINT.equals(field)) {
			int oldIntValue = Integer.parseInt(event.oldValue.trim());
			int newIntValue = Integer.parseInt(event.newVlaue.trim());
			Dimension growth = new Dimension(newIntValue - oldIntValue, 0);
			return new ResizeCommand(getEditPart(), growth);
		} else if (Assistants.LAYOUTDATA_GRID_HEIGHT_HINT.equals(field)) {
			int oldIntValue = Integer.parseInt(event.oldValue.trim());
			int newIntValue = Integer.parseInt(event.newVlaue.trim());
			Dimension growth = new Dimension(0, newIntValue - oldIntValue);
			return new ResizeCommand(getEditPart(), growth);
		}
		return super.computeCommand(event);
	}

	protected void setUpRefresher(FieldEditor editor) {
		super.setUpRefresher(editor);
		String fieldName = editor.getFieldName();
		final FieldEditor forRefresh = editor;
		XamlNode model = getModel();
		if (model == null) {
			return;
		}
		if (Assistants.LAYOUTDATA_GRID_WIDTH_HINT.equals(fieldName)) {
			getRefreshers(editor).add(new RefreshAdapter(model, Assistants.WIDTH) {
				protected void performRefresh(Notification msg) {
					forRefresh.apply(getAssistant());
				}
			});
		} else if (Assistants.LAYOUTDATA_GRID_HEIGHT_HINT.equals(fieldName)) {
			getRefreshers(editor).add(new RefreshAdapter(model, Assistants.HEIGHT) {
				protected void performRefresh(Notification msg) {
					forRefresh.apply(getAssistant());
				}
			});
		}
	}

	public int getHintValue(String hint) {
		EditPart editPart = getEditPart();
		if (editPart == null || !(editPart instanceof WidgetEditPart)) {
			return -1;
		}
		Widget widget = ((WidgetEditPart) editPart).getWidget();
		if (widget == null || widget.isDisposed()) {
			return -1;
		}
		try {
			IMetaclass metaclass = XWT.getMetaclass(widget);
			if (metaclass != null) {
				IProperty prop = metaclass.findProperty(hint);
				if (prop != null) {
					return (Integer) prop.getValue(widget);
				}
			}
		} catch (Exception e) {
		}
		return -1;
	}

	private void createMinimumArea(Composite parent) {
		Composite composite = createComposite(parent);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		composite.setLayout(grid);

		Group minimumGroup = createGroup(composite, Messages.GridDataAssistantPage_MINIMUM_GROUP_LABEL, 2);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		minimumGroup.setLayoutData(gd);

		createSpinner(minimumGroup, Assistants.LAYOUTDATA_GRID_MINIMUM_WIDTH, Messages.GridDataAssistantPage_MIN_WIDTH_LABEL);
		createSpinner(minimumGroup, Assistants.LAYOUTDATA_GRID_MINIMUM_HEIGHT, Messages.GridDataAssistantPage_MIN_HEIGHT_LABEL);

		createCheckBox(composite, Assistants.LAYOUTDATA_GRID_EXCLUDE, Messages.GridDataAssistantPage_EXCLUDE_LABEL);
	}

	private void createIndentsArea(Composite parent) {
		Group indentsGroup = createGroup(parent, Messages.GridDataAssistantPage_INDENTS_GROUP_LABEL, 2);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		indentsGroup.setLayoutData(gd);

		createSpinner(indentsGroup, Assistants.LAYOUTDATA_GRID_HORIZONTAL_INDENT, Messages.GridDataAssistantPage_H_INDENT_LABEL);
		createSpinner(indentsGroup, Assistants.LAYOUTDATA_GRID_VERTICAL_INDENT, Messages.GridDataAssistantPage_V_INDENT_LABEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistantPage#getAssistant()
	 */
	protected Object getAssistant() {
		Object assistant = super.getAssistant();
		if (assistant == null) {
			assistant = new GridData();
		}
		return assistant;
	}

}
