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
package org.eclipse.e4.xwt.vex.palette.customize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.vex.Activator;
import org.eclipse.e4.xwt.vex.EditorMessages;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.e4.xwt.vex.palette.CustomPalettePage;
import org.eclipse.e4.xwt.vex.palette.PaletteResourceManager;
import org.eclipse.e4.xwt.vex.palette.customize.model.CustomizeComponent;
import org.eclipse.e4.xwt.vex.palette.part.CustomizePaletteViewer;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPalette;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory;
import org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPaletteImpl;
import org.eclipse.e4.xwt.vex.util.ImageHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * @author BOB
 * 
 */
public class CustomizeComponentFactory {
	private final static String CUSTOMER_COMPONENT_ROOT = "VEX_CUSTOMIZE_COMPONENT_LIST"; //$NON-NLS-1$

	private List<VEXEditor> allVEXEditorList = new ArrayList<VEXEditor>(); // to support multiple vex editor

	// prefix and postfix strings
	private final static String PREFIX_COMPONENT = "CUSTOMIZE_COMPONENT_"; //$NON-NLS-1$
	private final static String POSTFIX_NAME = "_name"; //$NON-NLS-1$
	private final static String POSTFIX_SCOPE = "_scope"; //$NON-NLS-1$
	private final static String POSTFIX_ICON = "_icon"; //$NON-NLS-1$
	private final static String POSTFIX_LARGE_ICON = "_large_icon"; //$NON-NLS-1$
	private final static String POSTFIX_TOOLTIP = "_tooltip"; //$NON-NLS-1$
	private final static String POSTFIX_CONTENT = "_content"; //$NON-NLS-1$

	private static CustomizeComponentFactory customizeComponentFactory;
	private static List<CustomizeComponent> customizeComponentList = new ArrayList<CustomizeComponent>();

	// factory
	public static CustomizeComponentFactory getCustomizeComponentFactory() {
		if (customizeComponentFactory == null) {
			customizeComponentFactory = new CustomizeComponentFactory();
		}
		return customizeComponentFactory;
	}

	private void refreshVEXEditorList() {
		/* --------------------------support multiple editor----------------------------- */
		allVEXEditorList.clear();
		IEditorReference[] editorList = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		for (IEditorReference editorReference : editorList) {
			IEditorPart editorTemp = editorReference.getEditor(false);
			if (editorTemp instanceof VEXEditor) {
				allVEXEditorList.add((VEXEditor) editorTemp);
			}
		}
		/* --------------------------support multiple editor----------------------------- */

	}

	// constructor
	private CustomizeComponentFactory() {
		customizeComponentList.addAll(loadCustomizeComponents());
	}

	// Load Customize Components, then refresh the customize palette
	public static List<CustomizeComponent> loadCustomizeComponents() {
		List<CustomizeComponent> result = new ArrayList<CustomizeComponent>();
		String customizeComponents = getCustomizeComponentNames();
		if ((customizeComponents != null) && (!customizeComponents.equals(""))) { //$NON-NLS-1$
			List<String> componentNameList = getSubString(customizeComponents);
			for (String componentName : componentNameList) {
				result.add(loadComponent(componentName));
			}
		}
		return result;
	}

	private static String getCustomizeComponentNames() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		String customizeComponents = preferences.getString(CUSTOMER_COMPONENT_ROOT);
		return customizeComponents;
	}

	/**
	 * save current customize components in memory to the preference storage
	 * 
	 * @author BOB
	 * */
	private void saveChangeAndRefresh() {
		// 1. delete the existing storage
		deleteRootComponentPreferenceStorage();
		// 2.add the current memory customize component to preference
		addComponentPreferenceStorage();
		// 3.refresh palette
		refreshPalette();
		return;
	}

	private void deleteRootComponentPreferenceStorage() {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		preferences.setValue(CUSTOMER_COMPONENT_ROOT, ""); //$NON-NLS-1$
		try {
			((ScopedPreferenceStore) preferences).save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	private void addComponentPreferenceStorage() {
		if (customizeComponentList.size() > 0) {
			IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();

			String customizeComponents = ""; //$NON-NLS-1$
			StringBuilder sb = new StringBuilder();

			for (CustomizeComponent customizeComponent : customizeComponentList) {
				updateComponentInPreference(preferences, customizeComponent);
				sb.append(customizeComponent.getName() + ","); //$NON-NLS-1$
			}
			customizeComponents = sb.substring(0, sb.length() - 1);
			// set the root value
			preferences.setValue(CUSTOMER_COMPONENT_ROOT, customizeComponents);
			try {
				((ScopedPreferenceStore) preferences).save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	private void updateComponentInPreference(IPreferenceStore preferences, CustomizeComponent customizeComponent) {
		preferences.setValue(getComponentNameKey(customizeComponent.getName()), customizeComponent.getName());
		preferences.setValue(getComponentScopeKey(customizeComponent.getName()), customizeComponent.getScope());
		preferences.setValue(getComponentIconKey(customizeComponent.getName()), customizeComponent.getIcon());
		preferences.setValue(getComponentLargeIconKey(customizeComponent.getName()), customizeComponent.getLargeIcon());
		preferences.setValue(getComponentTooltipKey(customizeComponent.getName()), customizeComponent.getTooptip());
		preferences.setValue(getComponentContentKey(customizeComponent.getName()), customizeComponent.getContent());
		try {
			((ScopedPreferenceStore) preferences).save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/** Refresh Customize Palette */
	public void refreshPalette() {
		// 1. get the vex editor
		refreshVEXEditorList();

		for (VEXEditor editor : allVEXEditorList) {
			PaletteViewer paletteViewer;
			PaletteResourceManager tResourceManager;

			paletteViewer = ((CustomPalettePage) ((VEXEditor) editor).getVEXEditorPalettePage()).getPaletteViewer();
			tResourceManager = ((VEXEditor) editor).getPaletteResourceManager();
			if (tResourceManager == null) {
				return;
			}

			Resource resource = tResourceManager.getCustomizeResource();
			CustomizePaletteViewer customizePaletteViewer = null;
			Object objectPaletteViewer = paletteViewer.getProperty("Customize_PaletteViewer");
			if (objectPaletteViewer instanceof CustomizePaletteViewer) {
				customizePaletteViewer = (CustomizePaletteViewer) objectPaletteViewer;
			}
			if (customizePaletteViewer == null) {
				return;
			}
			PaletteRoot root = customizePaletteViewer.getPaletteRoot();
			List paletteChildren = root.getChildren();
			PaletteGroup customizePaletteGroup = null;
			for (Object object : paletteChildren) {
				if (((PaletteGroup) object).getLabel().equals(EditorMessages.CustomizeComponentFactory_Customize)) {
					customizePaletteGroup = (PaletteGroup) object;
					break;
				}
			}
			if (customizePaletteGroup == null) {
				customizePaletteGroup = new PaletteGroup(EditorMessages.CustomizeComponentFactory_Customize);
			}
			List children = customizePaletteGroup.getChildren();
			int count = children.size();
			for (int i = 0; i < count; i++) {
				customizePaletteGroup.remove((PaletteEntry) children.get(0));
			}
			root.remove(customizePaletteGroup);

			// 5. add customize components to emf model and gef palette drawer
			if (customizeComponentList.size() > 0) {
				// add customize components
				for (CustomizeComponent customizeComponent : customizeComponentList) {
					Entry subEntry = ToolPaletteFactory.eINSTANCE.createEntry();
					subEntry.setName(customizeComponent.getName());
					subEntry.setScope(customizeComponent.getScope());
					subEntry.setIcon(customizeComponent.getIcon());
					subEntry.setLargeIcon(customizeComponent.getLargeIcon());
					subEntry.setToolTip(customizeComponent.getTooptip());
					subEntry.setContent(customizeComponent.getContent());

					CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(subEntry.getName(), subEntry.getToolTip(), subEntry, new SimpleFactory(resource.getClass()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getIcon()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getLargeIcon()));

					customizePaletteGroup.add(component);
				}
			}
			root.add(customizePaletteGroup);

		}
	}

	/**
	 * @return
	 */
	public ToolPalette getCustomizeToolPalette() {
		ToolPalette toolPalette = ToolPaletteFactory.eINSTANCE.createToolPalette();
		if (customizeComponentList.size() > 0) {
			// add customize components
			for (CustomizeComponent customizeComponent : customizeComponentList) {
				Entry entry = ToolPaletteFactory.eINSTANCE.createEntry();
				entry.setName(customizeComponent.getName());
				entry.setScope(customizeComponent.getScope());
				entry.setIcon(customizeComponent.getIcon());
				entry.setLargeIcon(customizeComponent.getLargeIcon());
				entry.setToolTip(customizeComponent.getTooptip());
				entry.setContent(customizeComponent.getContent());

				toolPalette.getEntries().add(entry);
			}
		}
		return toolPalette;
	}

	/**
	 * @param resource
	 */
	public void importCustomizeTool(Resource resource) {
		EList<EObject> contents = resource.getContents();
		for (EObject object : contents) {
			if (object instanceof ToolPaletteImpl) {
				ToolPaletteImpl toolPaletteImpl = (ToolPaletteImpl) object;
				EList<Entry> entries = toolPaletteImpl.getEntries();
				for (Entry entry : entries) {
					CustomizeComponent component = new CustomizeComponent();
					component.setName(entry.getName());
					component.setScope(entry.getScope());
					component.setIcon(entry.getIcon());
					component.setLargeIcon(entry.getLargeIcon());
					component.setTooptip(entry.getToolTip());
					component.setContent(entry.getContent());
					customizeComponentList.add(component);
				}
			}
		}
		refreshPalette();
	}

	/**
	 * return customize components model list
	 * */
	public List<CustomizeComponent> getComponents() {
		return customizeComponentList;
	}

	/**
	 * add component to preference storage, then refresh the customize palette
	 * */
	public void addComponent(CustomizeComponent component) {
		customizeComponentList.add(component);
		saveChangeAndRefresh();
	}

	/**
	 * modify component, and save to preference storage, then refresh palette
	 * 
	 * @param component
	 */
	public void modifyComponent(CustomizeComponent component, String oldName) {
		if (component == null) {
			return;
		}

		for (CustomizeComponent customizeComponent : customizeComponentList) {
			if (customizeComponent.getName().equals(oldName)) {
				customizeComponent.setName(component.getName());
				customizeComponent.setScope(component.getScope());
				customizeComponent.setIcon(component.getIcon());
				customizeComponent.setLargeIcon(component.getLargeIcon());
				customizeComponent.setTooptip(component.getTooptip());
				customizeComponent.setContent(component.getContent());
			}
		}
		saveChangeAndRefresh();
	}

	/**
	 * delete component to preference storage, then refresh the customize palette
	 */
	public void deleteComponent(String componentName) {
		if (customizeComponentList.size() > 0) {
			for (CustomizeComponent customizeComponent : customizeComponentList) {
				if (customizeComponent.getName().equals(componentName)) {
					customizeComponentList.remove(customizeComponent);
					break;
				}
			}
			saveChangeAndRefresh();
		}
	}

	/** get string list from a string, compart by "," */
	private static List<String> getSubString(String str) {
		ArrayList<String> arrayList = new ArrayList<String>();
		String[] parts = str.split(","); //$NON-NLS-1$

		for (int i = 0; i < parts.length; i++) {
			arrayList.add(parts[i]);
		}
		return arrayList;
	}

	private static String getComponentNameKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_NAME;
	}

	private static String getComponentScopeKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_SCOPE;
	}

	private static String getComponentIconKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_ICON;
	}

	private static String getComponentLargeIconKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_LARGE_ICON;
	}

	private static String getComponentTooltipKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_TOOLTIP;
	}

	private static String getComponentContentKey(String componentName) {
		return PREFIX_COMPONENT + componentName + POSTFIX_CONTENT;
	}

	/**
	 * load customize component preference model object according to componentName
	 */
	public static CustomizeComponent loadComponent(String componentName) {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		CustomizeComponent component = new CustomizeComponent();
		// set values
		component.setName(preferences.getString(getComponentNameKey(componentName)));
		component.setScope(preferences.getString(getComponentScopeKey(componentName)));
		component.setIcon(preferences.getString(getComponentIconKey(componentName)));
		component.setLargeIcon(preferences.getString(getComponentLargeIconKey(componentName)));
		component.setTooptip(preferences.getString(getComponentTooltipKey(componentName)));
		component.setContent(preferences.getString(getComponentContentKey(componentName)));
		return component;
	}

	public boolean isComponentExist(String componentName) {
		if (customizeComponentList == null) {
			return false;
		}
		for (CustomizeComponent component : customizeComponentList) {
			if (component.getName().equals(componentName)) {
				return true;
			}
		}
		return false;
	}

	public List<String> getCustomizeComponentNameList() {
		List<String> customizeComponentNameList = new ArrayList<String>();

		if (customizeComponentList != null) {
			for (CustomizeComponent component : customizeComponentList) {
				String name = component.getName();
				customizeComponentNameList.add(name);
			}
		}
		return customizeComponentNameList;
	}

	public CustomizeComponent getCustomizeComponentByName(String name) {
		CustomizeComponent component = null;
		if (customizeComponentList != null) {
			for (CustomizeComponent comp : customizeComponentList) {
				if (comp.getName().equals(name)) {
					component = comp;
					break;
				}
			}
		}
		return component;
	}
}
