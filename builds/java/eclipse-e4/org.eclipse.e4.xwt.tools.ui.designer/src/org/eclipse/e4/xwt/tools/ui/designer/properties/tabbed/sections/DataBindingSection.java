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
package org.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Observable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.ObservableUtil;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.ObserveModelGroup;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class DataBindingSection extends AbstractPropertySection {
	private ComboViewer propertiesComboViewer;
	private WidgetEditPart editPart;
	private ObserveModelGroup observeModelGroup;
	private Button bindButton;
	private List<BindingInfo> createdBindings;

	private BindingContext bindingContext;
	
	private boolean needToRefresh = false; 

	public boolean isNeedToRefresh() {
		return needToRefresh;
	}

	public void setNeedToRefresh(boolean needToRefresh) {
		this.needToRefresh = needToRefresh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory().createComposite(parent);
		composite.setLayout(new GridLayout(4, false));
		getWidgetFactory().createCLabel(composite, "Properties");
		CCombo propertiesCombo = getWidgetFactory().createCCombo(composite, SWT.READ_ONLY);
		propertiesComboViewer = new ComboViewer(propertiesCombo);
		propertiesComboViewer.setContentProvider(new IStructuredContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IObservable) {
					return ((IObservable) inputElement).getProperties();
				}
				return new Object[0];
			}
		});
		propertiesComboViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof Property) {
					return ((Property) element).getName();
				}
				return super.getText(element);
			}

			public Image getImage(Object element) {
				if (element instanceof Property) {
					return ImageShop.getImageForType(((Property) element).getType());
				}
				return super.getImage(element);
			}
		});
		propertiesComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				bindingContext.setTargetProperty((Property) selection.getFirstElement());
			}
		});

		bindButton = getWidgetFactory().createButton(composite, "Bind", SWT.PUSH);
		bindButton.setEnabled(false);
		bindButton.setImage(ImageShop.get(ImageShop.IMG_BINDING_ADD));
		bindButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				createBinding();
			}
		});
		createClearAction(composite);

		bindingContext = new BindingContext();
		bindingContext.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				bindButton.setEnabled(bindingContext.isValid());
			}
		});

		observeModelGroup = new ObserveModelGroup(bindingContext, null);
		Composite modelsGroup = observeModelGroup.createGroup(composite, SWT.INHERIT_FORCE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 4;
		getWidgetFactory().adapt(modelsGroup);
		modelsGroup.setBackgroundMode(SWT.INHERIT_FORCE);
		modelsGroup.setLayoutData(layoutData);
	}

	public void setObserveModel(IObservable observeModel) {
		if (bindButton != null && !bindButton.isDisposed()) {
			bindButton.setEnabled(observeModel != null);
		}
	}

	protected void createBinding() {
		if (editPart == null || editPart.getWidget() == null) {
			return;
		}
		BindingInfo bindingInfo = new BindingInfo(bindingContext);
		Command bound = bindingInfo.bindWithCommand();
		editPart.getViewer().getEditDomain().getCommandStack().execute(bound);
		if (createdBindings == null) {
			createdBindings = new ArrayList<BindingInfo>();
		}
		createdBindings.add(bindingInfo);
	}

	protected void createClearAction(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		toolBar.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		ToolItem clearAction = new ToolItem(toolBar, SWT.PUSH);
		clearAction.setImage(ImageShop.get(ImageShop.IMG_CLEAR_FILTER));
		clearAction.setToolTipText("Clear setting values.");
		clearAction.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				deleteBinding();
			}
		});
	}

	protected void deleteBinding() {
		if (createdBindings == null) {
			return;
		}
		CompoundCommand commands = new CompoundCommand();
		for (BindingInfo binding : createdBindings) {
			commands.add(binding.deleteWithCommand());
		}
		editPart.getViewer().getEditDomain().getCommandStack().execute(commands);
	}

	private EditPart getRoot(EditPart child) {
		if (child == null) {
			return null;
		}
		if (child instanceof WidgetEditPart && ((WidgetEditPart) child).isRoot()) {
			return child;
		}
		return getRoot(child.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#refresh()
	 */
	public void refresh() {
		if (editPart == null || propertiesComboViewer == null || observeModelGroup == null) {
			return;
		}
		if (!isNeedToRefresh()) {
			return;
		}
		setNeedToRefresh(false);
		Widget widget = editPart.getWidget();
		if (widget == null || widget.isDisposed()) {
			return;
		}
		Observable observable = ObservableUtil.getObservable(widget);
		bindingContext.setTarget(observable);
		propertiesComboViewer.setInput(observable);

		EditPart root = getRoot(editPart);
		observeModelGroup.setRootEditPart(root);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Object object = ((IStructuredSelection) selection).getFirstElement();
		if (object instanceof WidgetEditPart) {
			this.editPart = (WidgetEditPart) object;
		}
		setNeedToRefresh(true);
	}
}
