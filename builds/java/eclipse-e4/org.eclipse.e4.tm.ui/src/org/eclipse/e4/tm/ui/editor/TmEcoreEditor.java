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
package org.eclipse.e4.tm.ui.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.tm.ui.Activator;
import org.eclipse.e4.tm.ui.EcorePaletteView;
import org.eclipse.e4.tm.ui.EditorPartModelContext;
import org.eclipse.e4.tm.ui.ModelPartContent;
import org.eclipse.e4.tm.ui.TmLabelProvider;
import org.eclipse.e4.tm.ui.TmPartStyle;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;

public class TmEcoreEditor extends EcoreEditor {

	protected ModelPartContent modelContent;
	
	private TmPartStyle partStyle = new TmPartStyle.SashStyle();
	
	protected Composite createPageContainer(Composite parent) {
		partStyle.createPageContainer(parent);
		partStyle.addTmComposite(this);
		return partStyle.getTreeParent();
	}

	private boolean autoLoadToolkitModels = true;

	private IPostProcessor[] postProcessors; 
		
	public void createPages() {
		postProcessors = Activator.getDefault().getPostProcessors();

		super.createPages();
		
		selectionViewer.setLabelProvider(new TmLabelProvider((ILabelProvider)selectionViewer.getLabelProvider()));
		
		modelContent = new ModelPartContent(partStyle.getTmComposite(this)) {
			protected void buildModel(EObject model) {
				super.buildModel(model);
				for (IPostProcessor postProcessor: postProcessors) {
					postProcessor.postBuildModel(model, this.getBuilder(), new IAdaptable(){
						public Object getAdapter(Class key) {
							if (key.equals(Composite.class)) {
								return getModelComposite();
							}
							return TmEcoreEditor.this.getAdapter(key);
						}
					});
				}
				EList<Resource> resources = getEditingDomain().getResourceSet().getResources();
				if (resources.size() > 0) {
					Resource resource = resources.get(0);
					if ("xmi".equals(resource.getURI().fileExtension()) && resource.getContents().size() > 0) {
						((Control)model).setDataObject(resource.getContents().get(0));
					}
				}
			}
		};
		if (autoLoadToolkitModels) {
			loadToolkitModel();
		}
		for (IPostProcessor postProcessor: postProcessors) {
			postProcessor.postLoadModel(this);
		}
		modelContent.setModelContext(new EditorPartModelContext(this));
	}

	public Object getAdapter(Class key) {
		if (key.equals(EditingDomain.class)) {
			return getEditingDomain();
		} else if (key.equals(URIConverter.class)) {
			return getEditingDomain().getResourceSet().getURIConverter();
		} else if (key.equals(Composite.class)) {
			return getContainer();
		} else if (IEditorInput.class.isAssignableFrom(key) && key.isInstance(getEditorInput())) {
			return getEditorInput();
		}
		for (IPostProcessor postProcessor: postProcessors) {
			Object adapted = postProcessor.getAdapter(key);
			if (key.isInstance(adapted)) {
				return adapted;
			}
		}
		return super.getAdapter(key);
	}

	public final static String TM_FILE_EXTENSION = "tm";

	private void loadToolkitModel() {
		ResourceSet resSet = getEditingDomain().getResourceSet();
		List<Resource> resources = resSet.getResources();
		Resource tmResource = null;
		for (Resource res: resources.toArray(new Resource[resources.size()])) {
			URI uri = res.getURI();
			URI tmUri = uri.trimFileExtension().appendFileExtension(TM_FILE_EXTENSION);
			if ("xmi".equals(uri.fileExtension()) && resSet.getURIConverter().exists(tmUri, null)) {
				try {
					tmResource = resSet.getResource(tmUri, true);
					break;
				} catch (RuntimeException re) {
				}
			}
		}
	}

	protected void createContextMenuForGen(StructuredViewer viewer) {
		Transfer[] transferTypes = new Transfer[]{
				TextTransfer.getInstance(),
				LocalTransfer.getInstance()
		};
		viewer.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE, transferTypes, new EditingDomainViewerDropAdapter(getEditingDomain(), viewer) {

			private boolean isTransfer(Transfer transfer, DropTargetEvent event) {
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (transfer.isSupportedType(event.dataTypes[i])) {
						return true;
					}
				}
				return false;
			}
			protected void helper(DropTargetEvent event) {
				if (isTransfer(TextTransfer.getInstance(), event)) {
					event.feedback = DND.FEEDBACK_SELECT | getAutoFeedback();
					originalOperation = DND.DROP_COPY;
					event.detail = DND.DROP_COPY;
				} else {
					super.helper(event);
				}
			}
			protected Collection<?> getDragSource(DropTargetEvent event) {
				if (isTransfer(TextTransfer.getInstance(), event)) {
					return null;
				}
				return super.getDragSource(event);
			}
			protected Collection<?> extractDragSource(Object object) {
				if (object instanceof String) {
					return TmEcoreEditor.this.getDragSource((String)object);
				}
				return super.extractDragSource(object);
			}
		});
		try {
			super.createContextMenuForGen(viewer);
		} catch (SWTError swtError) {
			// this is expected, since we have already added our own, hacked drop support
			if (swtError.code != DND.ERROR_CANNOT_INIT_DROP) {
				throw swtError;
			}
		}
	}

	private Collection<EObject> getDragSource(String uris) {
		Collection<EObject> eObjects = new ArrayList<EObject>();
		ResourceSet resSet = new ResourceSetImpl();
		StringTokenizer tokens = new StringTokenizer(uris);
		while (tokens.hasMoreTokens()) {
			String uri = tokens.nextToken();
			int pos = uri.indexOf(EcorePaletteView.FRAGMENT_SEPARATOR);
			if (pos > 0) {
				Resource res = resSet.getResource(URI.createURI(uri.substring(0, pos)), true);
				EObject eObject = res.getEObject(uri.substring(pos + EcorePaletteView.FRAGMENT_SEPARATOR.length()));
				if (eObject != null) {
					eObjects.add(EcoreUtil.copy(eObject));
				}
			}
		}
		return eObjects;
	}
}
