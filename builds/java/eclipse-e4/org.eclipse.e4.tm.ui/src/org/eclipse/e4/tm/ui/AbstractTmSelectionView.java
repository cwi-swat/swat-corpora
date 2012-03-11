/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractTmSelectionView extends ViewPart {

	protected IEditingDomainProvider editingDomainProvider;
	protected ISelectionProvider selectionProvider;
	
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
	}
	public void dispose() {
		getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
		setEditingDomainProvider(null);
		super.dispose();
	}

	protected void setEditingDomainProvider(IEditingDomainProvider editingDomainProvider) {
		if (this.editingDomainProvider == editingDomainProvider) {
			return;
		}
		if (this.editingDomainProvider != null) {
		}
		this.editingDomainProvider = editingDomainProvider;
		if (this.editingDomainProvider != null) {
			
		}
		updateView();
	}

	protected void updateView() {
	}
	
	public Object getAdapter(Class type) {
		Object o =  super.getAdapter(type);
		if (o == null && editingDomainProvider instanceof IAdaptable) {
			o = ((IAdaptable)editingDomainProvider).getAdapter(type);
		}
		return o;
	}

	private void updateProviders(IWorkbenchPart part) {
		setSelectionProvider(getAdapter(part, ISelectionProvider.class));
		setEditingDomainProvider(getAdapter(part, IEditingDomainProvider.class));
	}

	protected <T> T getAdapter(IWorkbenchPart part, Class<T> c) {
		IWorkbenchPage workbenchPage = getViewSite().getWorkbenchWindow().getActivePage();
		if (workbenchPage == null) {
			return null;
		}
		IEditorPart editorPart = workbenchPage.getActiveEditor();
		return (editorPart != null ? (T)editorPart.getAdapter(c) : null);
	}
	
	private IPartListener partListener = new IPartListener() {
		public void partActivated(IWorkbenchPart part) {
			updateProviders(part);
		}
		public void partBroughtToTop(IWorkbenchPart part) {
			updateProviders(part);
		}
		public void partClosed(IWorkbenchPart part) {
			updateProviders(part);
		}
		public void partDeactivated(IWorkbenchPart part) {
			updateProviders(part);
		}
		public void partOpened(IWorkbenchPart part) {
			updateProviders(part);
		}
	};

	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener(){
		public void selectionChanged(SelectionChangedEvent event) {
			AbstractTmSelectionView.this.selectionChanged(event.getSelection());
		}
	};
	
	protected void setSelectionProvider(ISelectionProvider selectionProvider) {
		if (this.selectionProvider != null) {
			this.selectionProvider.removeSelectionChangedListener(selectionChangedListener);
			selectionChanged(StructuredSelection.EMPTY);
		}
		this.selectionProvider = selectionProvider;
		if (this.selectionProvider != null) {
			this.selectionProvider.addSelectionChangedListener(selectionChangedListener);
			selectionChanged(this.selectionProvider.getSelection());
		}
	}
	
	protected Object selection = null;
	
	public void selectionChanged(SelectionChangedEvent event) {
		selectionChanged(event.getSelection());
	}
	protected void selectionChanged(ISelection selection) {
		Object oldSelection = this.selection;
		this.selection = null;
		if (selection.isEmpty()) {
		} else if (selection instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection)selection).getFirstElement();
			if (isValidSelection(o)) {
				this.selection = o;
			}
		}
		if (this.selection != oldSelection) {
			updateView();
		}
	}
	protected boolean isValidSelection(Object o) {
		return true;
	}
	
	private TextActionHandler textActionHandler;
	
	void registerTextControl(final Text textControl) {
		textControl.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				textActionHandler.addText(textControl);
			}
			public void focusLost(FocusEvent e) {
				textActionHandler.removeText(textControl);
			}
		});
	}

	public void createPartControl(Composite parent) {
		textActionHandler = new TextActionHandler(getViewSite().getActionBars());
		createActions();
		createMenu();
		createToolbar();
	}
	protected Text createTextControl(Composite parent, int style) {
		Text textControl = new Text(parent, style);
		textActionHandler.addText(textControl);
		return textControl;
	}
	protected void disposeTextControl(Text textControl) {
		if (! textControl.isDisposed()) {
			textActionHandler.removeText(textControl);
		}
	}

	protected void createActions() {
	}

	private void createMenu() {
		createMenu(getViewSite().getActionBars().getMenuManager());
	}
	protected void createMenu(IMenuManager mgr) {
	}

	private void createToolbar() {
		createToolbar(getViewSite().getActionBars().getToolBarManager());
	}
	protected void createToolbar(IToolBarManager mgr) {
	}
}
