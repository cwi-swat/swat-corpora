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
package org.eclipse.e4.xwt.tools.ui.palette.contribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.xwt.tools.ui.palette.CompoundInitializer;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Initializer;
import org.eclipse.e4.xwt.tools.ui.palette.PaletteFactory;
import org.eclipse.e4.xwt.tools.ui.palette.page.CustomPalettePage;
import org.eclipse.e4.xwt.tools.ui.palette.page.CustomPaletteViewerProvider;
import org.eclipse.e4.xwt.tools.ui.palette.page.resources.IPaletteResourceProvider;
import org.eclipse.e4.xwt.tools.ui.palette.root.PaletteRootFactory;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteRoot;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class PaletteContribution implements IExecutableExtension {

	public static final String EXTENSION_POINT_ID = "org.eclipse.e4.xwt.tools.ui.palette.paletteContribution";
	public static final String CONTRIBUTION = "Contribution";
	public static final String CONTRIBUTION_TARGET_ID = "targetId";
	public static final String RESOURCE = "Resource";
	public static final String RESOURCE_URI = "uri";
	public static final String RESOURCE_PROVIDER = "provider";
	public static final String INITIALIZER = "Initializer";
	public static final String INITIALIZER_TARGET = "target";
	public static final String INITIALIZER_TARGET_ID = "id";
	public static final String INITIALIZER_TARGET_OVERRIDE_ID = "overrideId";
	public static final String INITIALIZER_TARGET_GLOBAL = "*";
	public static final String INITIALIZER_CLASS = "class";
	public static final String TOOL = "Tool";
	public static final String TOOL_CLASS = "class";
	public static final String TOOL_TYPE = "type";
	public static final String TOOL_TYPE_CREATION = "creation";
	public static final String TOOL_TYPE_SELECTION = "selection";

	private String editorId;
	private Map<String, Initializer> initializersMap;
	private List<IPaletteResourceProvider> resourceProviders;

	private Class<? extends Tool> creationTool;
	private Class<? extends Tool> selectionTool;

	private static final Map<String, PaletteContribution> contributions = new HashMap<String, PaletteContribution>();

	private PaletteContribution(String editorId) {
		this.editorId = editorId;
		loadFromExtensions();
	}

	private void loadFromExtensions() {
		IConfigurationElement[] configurations = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						EXTENSION_POINT_ID);
		for (IConfigurationElement ctrib : configurations) {
			String targetId = ctrib.getAttribute(CONTRIBUTION_TARGET_ID);
			if (!editorId.equals(targetId)) {
				continue;
			}
			loadResources(ctrib.getChildren(RESOURCE));
			loadInitializers(ctrib.getChildren(INITIALIZER));
			loadTools(ctrib.getChildren(TOOL));
		}
	}

	private void loadResources(IConfigurationElement[] resources) {
		if (resources == null || resources.length == 0) {
			return;
		}
		if (resourceProviders == null) {
			resourceProviders = new ArrayList<IPaletteResourceProvider>();
		}
		for (IConfigurationElement resConfig : resources) {
			// String uri = resConfig.getAttribute(RESOURCE_URI);
			try {
				IPaletteResourceProvider provider = (IPaletteResourceProvider) resConfig
						.createExecutableExtension(RESOURCE_PROVIDER);
				resourceProviders.add(provider);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadInitializers(IConfigurationElement[] initializers) {
		if (initializers == null || initializers.length == 0) {
			return;
		}

		for (IConfigurationElement initConfig : initializers) {
			String target = normalizeTarget(initConfig
					.getAttribute(INITIALIZER_TARGET));
			String id = initConfig.getAttribute(INITIALIZER_TARGET_ID);
			String overrideId = initConfig
					.getAttribute(INITIALIZER_TARGET_OVERRIDE_ID);
			Initializer initializer = null;
			try {
				initializer = (Initializer) initConfig
						.createExecutableExtension(INITIALIZER_CLASS);

			} catch (CoreException e) {
				continue;
			}
			addInitializer(target, id, overrideId, initializer);
		}
	}

	private void addInitializer(String target, String id, String overrideId,
			Initializer initializer) {
		if (id == null || initializer == null) {
			return;
		}
		initializer.setId(id);

		if (initializersMap == null) {
			initializersMap = new HashMap<String, Initializer>();
		}
		// 1. quick fix orveride.
		if (overrideId != null && findInitializerById(overrideId) != null) {
			Initializer idOfInit = findInitializerById(overrideId);
			if (idOfInit.equals(initializersMap.get(target))) {
				initializersMap.put(target, initializer);
			} else {
				for (String key : new ArrayList<String>(initializersMap
						.keySet())) {
					if (idOfInit.equals(initializersMap.get(key))) {
						initializersMap.put(key, initializer);
					}
				}
			}
		} else {
			if (target != null && target.indexOf(",") != -1) {
				StringTokenizer stk = new StringTokenizer(target, ",");
				while (stk.hasMoreTokens()) {
					addInitializer(stk.nextToken().trim(), id, null,
							initializer);
				}
				return;
			}
			target = normalizeTarget(target);
			Initializer oldInit = initializersMap.get(target);
			if (oldInit == null) {
				initializersMap.put(target, initializer);
			} else if (oldInit instanceof CompoundInitializer) {
				((CompoundInitializer) oldInit).getInitializers().add(
						initializer);
			} else {
				CompoundInitializer newInit = PaletteFactory.eINSTANCE
						.createCompoundInitializer();
				newInit.getInitializers().add(oldInit);
				newInit.getInitializers().add(initializer);
				initializersMap.put(target, newInit);
			}
		}
	}

	public Initializer findInitializerById(String id) {
		if (id == null || initializersMap == null) {
			return null;
		}
		for (Initializer init : initializersMap.values()) {
			if (id.equals(init.getId())) {
				return init;
			}
		}
		return null;
	}

	private String normalizeTarget(String target) {
		if (target == null || "".equals(target)) {
			target = INITIALIZER_TARGET_GLOBAL;
		}
		return target.toUpperCase();
	}

	private void loadTools(IConfigurationElement[] tools) {
		if (tools == null || tools.length == 0) {
			return;
		}
		for (IConfigurationElement toolConfig : tools) {
			String type = toolConfig.getAttribute(TOOL_TYPE);
			try {
				Tool tool = (Tool) toolConfig
						.createExecutableExtension(TOOL_CLASS);
				if (TOOL_TYPE_CREATION.equals(type)) {
					creationTool = tool.getClass();
				} else if (TOOL_TYPE_SELECTION.equals(type)) {
					selectionTool = tool.getClass();
				}
			} catch (CoreException e) {
				continue;
			}
		}
	}

	public Class<? extends Tool> getCreationTool() {
		return creationTool;
	}

	public Class<? extends Tool> getSelectionTool() {
		return selectionTool;
	}

	public String getEditorId() {
		return editorId;
	}

	public Initializer getInitializer(String type) {
		if (initializersMap == null) {
			return null;
		}
		type = normalizeTarget(type);
		return initializersMap.get(type);
	}

	public void applyInitializer(Entry entry) {
		if (entry == null || !entry.getEntries().isEmpty()) {
			return;
		}

		List<Initializer> initializers = new ArrayList<Initializer>();

		// get from NAME.
		String name = entry.getName();
		if (name != null) {
			merge(initializersMap.get(name.toUpperCase()), initializers);
		}
		// get from ID.
		String id = entry.getId();
		if (id != null) {
			merge(initializersMap.get(id.toUpperCase()), initializers);
		}
		// add old initializer.
		Initializer oldInitializer = entry.getInitializer();
		if (oldInitializer != null) {
			merge(oldInitializer, initializers);
		}
		Initializer globalInitializer = getGlobalInitializer();
		if (globalInitializer != null) {
			merge(globalInitializer, initializers);
		}
		if (!initializers.isEmpty()) {
			CompoundInitializer initializer = PaletteFactory.eINSTANCE
					.createCompoundInitializer();
			initializer.getInitializers().addAll(initializers);
			entry.setInitializer(initializer.unwrap());
		}
	}

	private void merge(Initializer initializer, List<Initializer> initList) {
		if (initializer == null || initList == null) {
			return;
		}
		if (initializer instanceof CompoundInitializer) {
			initList.addAll(((CompoundInitializer) initializer)
					.getInitializers());
		} else {
			initList.add(initializer);
		}
	}

	public Initializer getGlobalInitializer() {
		return getInitializer(INITIALIZER_TARGET_GLOBAL);
	}

	public boolean hasInitialiers() {
		return initializersMap != null && initializersMap.size() > 0;
	}

	public List<IPaletteResourceProvider> getResourceProviders() {
		return resourceProviders;
	}

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

	}

	public CustomPalettePage createPalette(String editorId,
			EditDomain editDomain) {
		PaletteRootFactory factory = new PaletteRootFactory(resourceProviders,
				creationTool, selectionTool);
		PaletteRoot paletteRoot = factory.createPaletteRoot();
		if (paletteRoot != null) {
			editDomain.setPaletteRoot(paletteRoot);
		}
		CustomPaletteViewerProvider provider = new CustomPaletteViewerProvider(
				editDomain);
		return new CustomPalettePage(provider);
	}

	public static PaletteContribution getContribution(String editorId) {
		PaletteContribution paletteContribution = contributions.get(editorId);
		if (paletteContribution == null) {
			paletteContribution = new PaletteContribution(editorId);
			contributions.put(editorId, paletteContribution);
		}
		return paletteContribution;
	}
}
