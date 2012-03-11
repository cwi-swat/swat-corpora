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

import java.util.Collection;

import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class BindingTableGroup implements IGroup {

	private static final String[] COLUMNS = new String[] { "Target", "Target Properties", "Model", "Model Properties" };
	private TableViewer viewer;
	private ToolItem editAction;
	private ToolItem deleteAction;
	private ToolItem deleteAllAction;
	private ToolItem gotoAction;

	private CommandStack commandStack;

	public BindingTableGroup(CommandStack commandStack) {
		this.commandStack = commandStack;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#createGroup(org.eclipse.swt.widgets.Composite, int)
	 */
	public Composite createGroup(final Composite parent, int style) {
		Composite group = new Composite(parent, style);
		group.setLayout(new GridLayout(2, false));
		Composite tableComp = new Composite(group, SWT.NONE);
		GridData layoutData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		tableComp.setLayoutData(layoutData);
		TableColumnLayout layout = new TableColumnLayout();
		tableComp.setLayout(layout);
		viewer = new TableViewer(tableComp, SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		viewer.setColumnProperties(COLUMNS);
		viewer.setLabelProvider(new LabelProvider());
		viewer.setContentProvider(new ContentProvider());

		Table table = viewer.getTable();
		for (String columnName : COLUMNS) {
			TableColumn column = new TableColumn(table, SWT.CENTER);
			column.setText(columnName);
			layout.setColumnData(column, new ColumnWeightData(columnName.length()));
		}

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				enableActions();
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				edit();
			}
		});

		Composite toolsComp = new Composite(group, SWT.NONE);
		toolsComp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL));
		toolsComp.setLayout(new FillLayout());

		ToolBar toolBar = new ToolBar(toolsComp, SWT.VERTICAL | SWT.FLAT);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (SWT.Selection != event.type) {
					return;
				}
				if (event.widget == editAction) {
					edit();
				} else if (event.widget == deleteAction) {
					delete();
				} else if (event.widget == deleteAllAction) {
					deleteAll();
				} else if (event.widget == gotoAction) {
					goTo();
					parent.getShell().setMinimized(true);
				}
			}
		};

		editAction = new ToolItem(toolBar, SWT.PUSH);
		editAction.setImage(ImageShop.get(ImageShop.IMG_BINDING_EDIT));
		editAction.setToolTipText("Edit");
		editAction.setEnabled(false);
		editAction.addListener(SWT.Selection, listener);
		new ToolItem(toolBar, SWT.SEPARATOR | SWT.HORIZONTAL);

		deleteAction = new ToolItem(toolBar, SWT.PUSH);
		deleteAction.setToolTipText("Delete");
		deleteAction.setImage(ImageShop.get(ImageShop.IMG_BINDING_DELETE));
		deleteAction.setEnabled(false);
		deleteAction.addListener(SWT.Selection, listener);

		deleteAllAction = new ToolItem(toolBar, SWT.PUSH);
		deleteAllAction.setToolTipText("Delete All");
		deleteAllAction.setImage(ImageShop.get(ImageShop.IMG_BINDING_REMOVE_ALL));
		deleteAllAction.addListener(SWT.Selection, listener);
		deleteAllAction.setEnabled(false);
		new ToolItem(toolBar, SWT.SEPARATOR | SWT.HORIZONTAL);

		gotoAction = new ToolItem(toolBar, SWT.PUSH);
		gotoAction.setImage(ImageShop.get(ImageShop.IMG_GOTO_DEFINITION));
		gotoAction.setEnabled(false);
		gotoAction.setToolTipText("Go to definition");
		gotoAction.addListener(SWT.Selection, listener);

		Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		layoutData.heightHint = size.y;
		layoutData.minimumHeight = size.y;
		return group;
	}

	private void executeCommand(Command command) {
		if (command == null || !command.canExecute()) {
			return;
		}
		if (commandStack == null) {
			command.execute();
		} else {
			commandStack.execute(command);
		}
	}

	protected void edit() {
		BindingInfo binding = getSelection();
		AdvancedBindingDialog dialog = new AdvancedBindingDialog(new Shell(), binding);
		if (dialog.open() == Window.OK) {
			executeCommand(binding.bindWithCommand());
		}
	}

	protected void delete() {
		BindingInfo binding = getSelection();
		executeCommand(computeDeleteCommand(binding));
		viewer.remove(binding);
	}

	private Command computeDeleteCommand(final BindingInfo binding) {
		XamlElement bindingNode = binding.getBindingNode();
		if (bindingNode == null) {
			return null;
		}
		final XamlNode parent = bindingNode.getParent();
		if (parent == null) {
			return null;
		}
		parent.eContainer().eAdapters().add(new AdapterImpl() {
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.ADD && parent == msg.getNewValue()) {
					viewer.add(binding);
				}
			}
		});
		return binding.deleteWithCommand();
	}

	protected void deleteAll() {
		Object input = viewer.getInput();
		if (input instanceof BindingInfo[]) {
			CompoundCommand cmd = new CompoundCommand("Delete All");
			for (BindingInfo binding : (BindingInfo[]) input) {
				Command deleteCmd = computeDeleteCommand(binding);
				if (deleteCmd == null || !deleteCmd.canExecute()) {
					continue;
				}
				cmd.add(deleteCmd);
			}
			executeCommand(cmd.unwrap());
		}
		viewer.setInput(null);
	}

	protected void goTo() {
		BindingInfo binding = getSelection();
		if (binding == null) {
			return;
		}
		XamlElement bindingNode = binding.getBindingNode();
		if (bindingNode != null && bindingNode.eContainer() != null) {
			XWTDesigner designer = XWTDesignerPlugin.getDefault().getActiveDesigner();
			if (designer != null) {
				designer.gotoDefinition((XamlNode) bindingNode.eContainer());
			}
		}
	}

	public TableViewer getViewer() {
		return viewer;
	}

	protected void enableActions() {
		ISelection selection = viewer.getSelection();
		boolean enabled = !selection.isEmpty();
		if (editAction != null) {
			editAction.setEnabled(enabled);
		}
		if (deleteAction != null) {
			deleteAction.setEnabled(enabled);
		}
		if (gotoAction != null) {
			gotoAction.setEnabled(enabled);
		}
	}

	public BindingInfo getSelection() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if (selection != null)
			return (BindingInfo) selection.getFirstElement();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		if (viewer != null) {
			viewer.setInput(input);
			if (deleteAllAction != null) {
				deleteAllAction.setEnabled(true);
			}
		}
	}

	private static class LabelProvider extends
			org.eclipse.jface.viewers.LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element == null)
				return "";
			if (element instanceof BindingInfo) {
				BindingInfo binding = (BindingInfo) element;
				BindingContext bindingContext = binding.getBindingContext();
				IObservable target = bindingContext.getTarget();
				IObservable model = bindingContext.getModel();
				Property targetProperty = bindingContext.getTargetProperty();
				Property modelProperty = bindingContext.getModelProperty();
				switch (columnIndex) {
				case 0:
					return target.getDisplayName();
				case 1:
					return targetProperty == null ? "" : targetProperty.toString();
				case 2:
					return model.getDisplayName();

				case 3:
					return modelProperty == null ? "" : modelProperty.toString();
				}
			}
			return "";
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

	}

	private static class ContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement == null)
				return new Object[0];
			if (inputElement.getClass().isArray()) {
				return (Object[]) inputElement;
			} else if (inputElement instanceof Collection) {
				return ((Collection) inputElement).toArray(new Object[0]);
			}
			return new Object[0];
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

}
