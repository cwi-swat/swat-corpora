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
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ObservableList {

	protected TreeViewer treeViewer;
	protected ViewerFilter viewerFilter;
	protected String filter;

	private BindingContext bindingContext;
	private Control control;

	public ObservableList(BindingContext bindingContext, Composite parent) {
		this.bindingContext = bindingContext;
		control = createControl(parent);
	}

	public Control getControl() {
		return control;
	}

	protected Control createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		control.setLayout(layout);

		Label label = new Label(control, SWT.NONE);
		label.setText("Widgets:");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		treeViewer = createControlsViewer(control);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		viewerFilter = createControlsFilter();

		return control;
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	protected TreeViewer createControlsViewer(Composite control) {
		TreeViewer treeViewer = new TreeViewer(control, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		treeViewer.getTree().setLayoutData(layoutData);
		treeViewer.setContentProvider(new ITreeContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IObservable[]) {
					return (IObservable[]) inputElement;
				}
				return new Object[0];
			}

			public boolean hasChildren(Object element) {
				if (element instanceof IObservable) {
					return ((IObservable) element).hasChildren();
				}
				return false;
			}

			public Object getParent(Object element) {
				if (element instanceof IObservable) {
					return ((IObservable) element).getParent();
				}
				return null;
			}

			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof IObservable) {
					return ((IObservable) parentElement).getChildren();
				}
				return null;
			}
		});
		treeViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof IObservable) {
					return ((IObservable) element).getDisplayName();
				}
				return super.getText(element);
			}

			public Image getImage(Object element) {
				if (element instanceof IObservable) {
					Object source = ((IObservable) element).getSource();
					if (source == null) {
						return super.getImage(element);
					}
					if (source instanceof Widget) {
						return ImageShop.getImageForWidget((Widget) source);
					} else if (source instanceof Viewer) {
						return ImageShop.getObj16(((Viewer) source).getClass().getSimpleName().toLowerCase());
					} else {
						return ImageShop.getImageForType(source.getClass());
					}
				}
				return super.getImage(element);
			}
		});
		treeViewer.setAutoExpandLevel(3);
		treeViewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IObservable) {
					return ((IObservable) element).getProperties().length > 0;
				}
				return false;
			}
		});
		return treeViewer;
	}

	protected ViewerFilter createControlsFilter() {
		return new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return isFiltered(element);
			}
		};
	}

	protected boolean isFiltered(Object element) {
		ITreeContentProvider contentProvider = (ITreeContentProvider) treeViewer.getContentProvider();
		Object[] children = contentProvider.getChildren(element);

		ILabelProvider labelProvider = (ILabelProvider) treeViewer.getLabelProvider();
		String text = labelProvider.getText(element);
		boolean filtered = text.toLowerCase().startsWith(filter.toLowerCase());
		if (!filtered) {
			for (Object child : children) {
				filtered = isFiltered(child);
				if (filtered) {
					break;
				}
			}
		}
		return filtered;
	}

	protected void controlSelected() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		Object firstElement = selection.getFirstElement();
		bindingContext.setTarget((IObservable) firstElement);
	}

	public void applyFilter(String filter) {
		this.filter = filter;
		if (filter == null || filter.length() == 0) {
			treeViewer.removeFilter(viewerFilter);
		} else {
			treeViewer.addFilter(viewerFilter);
		}
	}

	public void setInput(Object input) {
		if (treeViewer != null) {
			treeViewer.setInput(input);
		}
	}

}
