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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.tm.builder.IBuilderListener;
import org.eclipse.e4.tm.ui.editor.TmEcoreEditor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.dialogs.ResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

public class EditingDomainModelContext implements IEditingDomainProvider, IBuilderListener {

	private AdapterFactoryEditingDomain editingDomain;

	private void createEditingDomain() {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
	}

	public AdapterFactory getAdapterFactory() {
		if (editingDomain == null) {
			createEditingDomain();
		}
		return editingDomain.getAdapterFactory();
	}

	public EditingDomain getEditingDomain() {
		if (editingDomain == null) {
			createEditingDomain();
		}
		return editingDomain;
	}

	public ResourceSet getResourceSet() {
		return getEditingDomain().getResourceSet();
	}

	private TmPartStyle partStyle;

	public EditingDomainModelContext(AdapterFactoryEditingDomain editingDomain, TmPartStyle partStyle) {
		this.editingDomain = editingDomain;
		this.partStyle = partStyle;
	}
	public EditingDomainModelContext(TmPartStyle partStyle) {
		this.partStyle = partStyle;
	}

	private Adapter resourcesChangedHandler = new AdapterImpl() {
		public void notifyChanged(Notification notification) {
			if (notification.getNotifier() instanceof ResourceSet && notification.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
				if (notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.ADD_MANY) {
					for (Resource resource: getResourceSet().getResources()) {
						if (! resourceModelContentMap.containsKey(resource)) {
							resourceAdded(resource);
						}
					}
				} else if (notification.getEventType() == Notification.REMOVE || notification.getEventType() == Notification.REMOVE_MANY) {
					for (Resource resource: resourceModelContentMap.keySet()) {
						if (! getResourceSet().getResources().contains(resource)) {
							resourceRemoved(resource);
						}
					}
				}
			} else if (notification.getNotifier() instanceof Resource && notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_LOADED) {
				Resource resource = (Resource)notification.getNotifier();
				if (resource.isLoaded() && (! resourceModelContentMap.containsKey(resource))) {
					resourceAdded(resource);
				}
			}
		}
	};

	public void createControls(Composite parent) {
		partStyle.createTreeParent(parent);
		for (final Resource resource: getEditingDomain().getResourceSet().getResources()) {
			resourceAdded(resource);
		}
		getResourceSet().eAdapters().add(resourcesChangedHandler);
	}

	private Map<Resource, ModelPartContent> resourceModelContentMap = new HashMap<Resource, ModelPartContent>(); 

	private void resourceAdded(final Resource resource) {
		URI uri = resource.getURI();
		if (! TmEcoreEditor.TM_FILE_EXTENSION.equals(uri.fileExtension())) {
			return;
		}
		if (! resource.isLoaded()) {
			resource.eAdapters().add(resourcesChangedHandler);
		} else {
			resource.eAdapters().remove(resourcesChangedHandler);
			final ModelPartContent modelContent = new ModelPartContent(partStyle.addTmComposite(uri));
			modelContent.getBuilder().addBuilderListener(this);
			resourceModelContentMap.put(resource, modelContent);
			modelContent.setModelContext(new AbstractModelContext() {
				public EObject getModel() {
					return (resource.getContents().size() > 0 ? resource.getContents().get(0) : null);
				}
			});
		}
	}

	private void resourceRemoved(final Resource resource) {
		ModelPartContent modelContent = resourceModelContentMap.get(resource);
		resourceModelContentMap.remove(resource);
		modelContent.dispose();
		partStyle.disposeTmComposite(resource);
	}

	public void dispose() {
		for (Resource resource: resourceModelContentMap.keySet()) {
			resourceRemoved(resource);
		}
	}

	//

	public boolean addResource(URI uri) {
		try {
			getResourceSet().getResource(uri, true);
			return true;
		} catch (RuntimeException e) {
			System.err.println("Error when getting " + uri + " resource: " + e);
			return false;
		}
	}

	private final static String DEFAULT_RESOURCE_URI = "platform:/plugin/org.eclipse.e4.tm.examples/templates/template.tm";

	public final IAction loadResourceAction = new Action("Load resource...") {
		public void run() {
			new ResourceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Load resource...", SWT.OPEN | SWT.MULTI) {
				protected Control createDialogArea(Composite parent) {
					Control result = super.createDialogArea(parent);
					uriField.setText(DEFAULT_RESOURCE_URI);
					return result;
				}
				protected boolean processResources() {
					boolean result = true;
					for (URI uri : getURIs()) {
						result &= addResource(uri);
					}
					return result;
				}
			}.open();
		}
	};

	//

	private List<IBuilderListener> builderListeners = new ArrayList<IBuilderListener>();

	public void addBuilderListener(IBuilderListener builderListener) {
		builderListeners.add(builderListener);
	}
	public void removeBuilderListener(IBuilderListener builderListener) {
		builderListeners.remove(builderListener);
	}

	public void objectHandled(int id, EObject eObject, Object object) {
		for (IBuilderListener builderListener : builderListeners) {
			builderListener.objectHandled(id, eObject, object);
		}
	}
}
