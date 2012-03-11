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

import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class ObserveGroup implements IGroup {

	private Label messageLabel;
	private Text filterText;
	protected BindingContext bindingContext;
	protected ObservableList observableList;
	protected TreeViewer propertiesTree;

	private boolean dispatching = false;

	private EditPart rootEditPart;
	private TreeViewer treeViewer;

	public ObserveGroup(BindingContext bindingContext, EditPart rootEditPart) {
		this.bindingContext = bindingContext;
		this.rootEditPart = rootEditPart;
	}

	public Composite createGroup(Composite parent, int style) {
		Composite group = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		group.setLayout(layout);

		messageLabel = new Label(group, SWT.NONE);
		updateMessage();

		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		messageLabel.setLayoutData(layoutData);

		filterText = new Text(group, SWT.SEARCH | SWT.ICON_CANCEL);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		filterText.setLayoutData(layoutData);
		filterText.setMessage("type filter text");
		ToolBar toolBar = new ToolBar(group, SWT.FLAT);
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(ImageShop.get(ImageShop.IMG_CLEAR_FILTER));
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				filterText.setText("");
				filterText.setMessage("type filter text");
			}
		});
		filterText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail != SWT.ICON_CANCEL) {
					applyFilter();
				}
			}
		});

		observableList = new ObservableList(bindingContext, group);
		Control control = observableList.getControl();
		layoutData = new GridData(GridData.FILL_BOTH);
		control.setLayoutData(layoutData);

		Composite propComp = new Composite(group, SWT.NONE);
		layout = new GridLayout();
		layout.marginWidth = 0;
		propComp.setLayout(layout);

		new Label(propComp, SWT.NONE).setText("Properties");

		treeViewer = observableList.getTreeViewer();
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleObservableChanged(event);
			}
		});
		treeViewer.setInput(computeObservables(rootEditPart));

		propertiesTree = new TreeViewer(propComp, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		propertiesTree.setContentProvider(new ITreeContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			public void dispose() {

			}

			public boolean hasChildren(Object element) {
				if (element instanceof Property) {
					return ((Property) element).hasChildren();
				}
				return false;
			}

			public Object getParent(Object element) {
				if (element instanceof Property) {
					return ((Property) element).getParent();
				}
				return null;
			}

			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof Property) {
					return ((Property) parentElement).getProperties();
				}
				return new Object[0];
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IObservable) {
					return ((IObservable) inputElement).getProperties();
				}
				return new Object[0];
			}
		});
		propertiesTree.setLabelProvider(new LabelProvider() {
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
		propertiesTree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		propertiesTree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handlePropertyChanged(event);
			}
		});

		layoutData = new GridData(GridData.FILL_BOTH);
		propComp.setLayoutData(layoutData);

		bindingContext.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				refreshGroup(evt);
			}
		});
		return group;
	}

	private void refreshGroup(PropertyChangeEvent evt) {
		if (dispatching) {
			return;
		}
		selectAndReveal(evt);
	}

	protected abstract IObservable[] computeObservables(EditPart rootEditPart);

	protected abstract void selectAndReveal(PropertyChangeEvent evt);

	protected StructuredSelection createSelection(Object object) {
		if (object == null) {
			return StructuredSelection.EMPTY;
		}
		return new StructuredSelection(object);
	}

	private void handleObservableChanged(SelectionChangedEvent event) {
		dispatching = true;
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object firstElement = selection.getFirstElement();
		propertiesTree.setInput(firstElement);
		handleSelection(selection);
		updateMessage();
		dispatching = false;
	}

	private void handlePropertyChanged(SelectionChangedEvent event) {
		dispatching = true;
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		handleSelection(selection);
		updateMessage();
		dispatching = false;
	}

	protected abstract void handleSelection(IStructuredSelection selection);

	protected void applyFilter() {
		observableList.applyFilter(filterText.getText());
	}

	protected void updateMessage() {
		if (messageLabel == null || messageLabel.isDisposed()) {
			return;
		}
		String message = computeMessage();
		if (message == null) {
			message = "";
		}
		messageLabel.setText(message);
	}

	protected abstract String computeMessage();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		if (observableList == null) {
			return;
		}
		observableList.setInput(input);
	}

	public BindingContext getBindingContext() {
		return bindingContext;
	}

	public void setRootEditPart(EditPart rootEditPart) {
		this.rootEditPart = rootEditPart;
		setInput(computeObservables(rootEditPart));
	}

}
