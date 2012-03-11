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

import java.util.Iterator;
import java.util.StringTokenizer;

import org.eclipse.e4.tm.builder.IBuilderListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class EcorePaletteView extends ViewPart {

	private TmPartStyle partStyle = new TmPartStyle.SashStyle();
	
	private EditingDomainModelContext editingDomainModelContext = new EditingDomainModelContext(partStyle);
	
	private TreeViewer viewer;
	
	public void createPartControl(Composite parent) {
		final Transfer[] dragTransfers = new Transfer[]{TextTransfer.getInstance()};
		final DragSourceListener dragSourceListener = new DragSourceListener() {

			private String uris;

			public void dragFinished(DragSourceEvent event) {
				uris = null;
			}
			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = uris;
				}
			}
			public void dragStart(DragSourceEvent event) {
				Control control = ((DragSource)event.getSource()).getControl();
				uris = null;
				if (control == viewer.getControl() && viewer.getSelection() instanceof IStructuredSelection) {
					uris = getUriString(((IStructuredSelection)viewer.getSelection()).iterator());
				} else {
					Object controlData = control.getData("modelUri");
					if (controlData != null) {
						uris = controlData.toString();
					}
				}
				event.doit = (uris != null);
			}
		};

		editingDomainModelContext.addBuilderListener(new IBuilderListener() {
			public void objectHandled(int id, EObject eObject, Object object) {
				if (id == IBuilderListener.BUILD && object instanceof Control) {
					Control control = (Control)object;
					DragSource ds = new DragSource(control, DND.DROP_COPY);
					ds.setTransfer(dragTransfers);
					ds.addDragListener(dragSourceListener);
				}
			}
		});

		editingDomainModelContext.createControls(parent);

		partStyle.getTreeParent().setLayout(new FillLayout());
		viewer = new TreeViewer(new Tree(partStyle.getTreeParent(), SWT.SINGLE | SWT.V_SCROLL));

		// force creation of adapterFactory
		viewer.setContentProvider(new AdapterFactoryContentProvider(editingDomainModelContext.getAdapterFactory()));
		viewer.setLabelProvider(new TmLabelProvider(new AdapterFactoryLabelProvider(editingDomainModelContext.getAdapterFactory())));
		viewer.setInput(editingDomainModelContext.getResourceSet());

		viewer.addDragSupport(DND.DROP_COPY, dragTransfers, dragSourceListener);

		final Transfer[] dropTransfers = new Transfer[]{
				// ResourceTransfer.getInstance(),
				FileTransfer.getInstance()
		};
		viewer.addDropSupport(DND.DROP_COPY | DND.DROP_LINK , dropTransfers, new ViewerDropAdapter(viewer) {

			public boolean validateDrop(Object target, int operation, TransferData transferType) {
				setFeedbackEnabled(false);
				for (int i = 0; i < dropTransfers.length; i++) {
					if (dropTransfers[i].isSupportedType(transferType)) {
						return true;
					}
				}
				return false;
			}
			public boolean performDrop(Object data) {
				boolean result = false;
				if (data instanceof String[]) {
					String[] filenames = (String[])data;
					for (int i = 0; i < filenames.length; i++) {
						result |= editingDomainModelContext.addResource(URI.createFileURI(filenames[i]));
					}
				}
				return result;
			}
		});

		createActions();
		createMenu();
		createToolbar();
	}

	public void dispose() {
		editingDomainModelContext.dispose();
		super.dispose();
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	//

	public final static String FRAGMENT_SEPARATOR = "#";

	private String getUriString(Iterator<?> it) {
		String uris = null;
		while (it.hasNext()) {
			Object o = it.next();
			URI uri = null;
			if (o instanceof EObject) {
				uri = EcoreUtil.getURI((EObject)o);
			} else if (o instanceof Resource) {
				uri = ((Resource)o).getURI();
			}
			if (uri != null) {
				if (uris == null) {
					uris = uri.toString();
				} else {
					uris += " " + uri;
				}
			}
		}
		return uris;
	}

	//

	private final static String STORE_URIS = "EcorePaletteView.uris";

	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(STORE_URIS, getUriString(editingDomainModelContext.getResourceSet().getResources().iterator()));
	}

	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		if (memento != null) {
			String uris = memento.getString(STORE_URIS);
			if (uris != null) {
				for (StringTokenizer tokens = new StringTokenizer(uris); tokens.hasMoreTokens();) {
					editingDomainModelContext.addResource(URI.createURI(tokens.nextToken()));
				}
			}
		}
	}

	//

	private IAction deleteAction;

	private void createActions() {
		deleteAction = new Action("Delete") {
			public void run() {
				ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();) {
						Object o = it.next();
						if (o instanceof Resource) {
							Resource resource = (Resource)o;
							editingDomainModelContext.getResourceSet().getResources().remove(resource);
						}
					}
				}
			}
		};
	}

	//

	private void createMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.add(editingDomainModelContext.loadResourceAction);
//		menuManager.add(deleteAction);
	}

	private void createToolbar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(editingDomainModelContext.loadResourceAction);
//		toolBarManager.add(deleteAction);
	}
}
