/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *     Anaik Trihoreau <anaik@anyware-tech.com> - Bug 274057
 *******************************************************************************/
package org.eclipse.e4.xwt.vex.palette.customize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * 
 * Class allowing to include custom widgets in the XWT palette
 * Contributors:
 *     Anaik Trihoreau <anaik@anyware-tech.com> - inital API and implementation
 */
public class CustomWidgetManager
{
	private static final String CUSTOM_PALETTE_EXTENSION_ID = "org.eclipse.e4.xwt.vex.customPalette";
	private static final String WIDGET_CATEGORY_ELEMENT = "category";
	private static final String WIDGET_CATEGORY_NAME_ATTR = "name";
	private static final String WIDGET_CATEGORY_ICON_ATTR = "icon";
	private static final String WIDGET_ELEMENT = "tool";
	private static final String WIDGET_NAME_ATTR = "name";
	private static final String WIDGET_CLASS_ATTR = "class";
	private static final String WIDGET_SCOPE_ATTR = "scope";
	private static final String WIDGET_ICON_ATTR = "icon";
	private static final String WIDGET_TOOLTIP_ATTR = "tooltip";
	private static final String WIDGET_CONTENT_ATTR = "content";

	private static CustomWidgetManager __instance;

	private Map<Category, List<Tool>> widgetMap = null;
	private Collection<Class<?>> widgetClassList = null;

	/**
	 * Private constructor
	 */
	private CustomWidgetManager()
	{
		initWidgetCategories();
	}

	/**
	 * Returns the singleton
	 * 
	 * @return
	 */
	public static CustomWidgetManager getInstance()
	{
		if (__instance == null)
		{
			__instance = new CustomWidgetManager();
		}
		return __instance;
	}

	/**
	 * Returns the custom widgets which are defined in the extension. The custom widget are grouped
	 * by categories
	 * 
	 * @return the custom widgets grouped by categories
	 */
	public Map<Category, List<Tool>> getWidgetCategories()
	{
		return widgetMap;
	}

	/**
	 * Returns the list of the classes corresponding to the custom widgets which are defined in the
	 * extension.
	 * 
	 * @return the list of the classes
	 */
	public Collection<Class<?>> getWidgetClassList()
	{
		return widgetClassList;
	}

	/**
	 * Initialize the custom widgets which are defined in the extension
	 */
	private void initWidgetCategories()
	{
		// Init
		widgetMap = new HashMap<Category, List<Tool>>();
		widgetClassList = new ArrayList<Class<?>>();

		List<Tool> widgetList = null;

		List<IConfigurationElement> confEltList = getExtensions(CUSTOM_PALETTE_EXTENSION_ID);
		if (confEltList != null && !confEltList.isEmpty())
		{
			for (Iterator<IConfigurationElement> it = confEltList.iterator(); it.hasNext();)
			{
				IConfigurationElement cfgElt = it.next();
				String eltName = cfgElt.getName();

				if (WIDGET_CATEGORY_ELEMENT.equals(eltName))
				{
					// Name of the category
					String nameCat = cfgElt.getAttribute(WIDGET_CATEGORY_NAME_ATTR);
					if (nameCat == null || nameCat.length() == 0)
					{
						continue;
					}

					// Gets the extension bundle
					String extPlugInID = cfgElt.getContributor().getName();

					// Gets the icon of the category
					WidgetIcon iconCat = new WidgetIcon(extPlugInID,
						cfgElt.getAttribute(WIDGET_CATEGORY_ICON_ATTR));

					// Adds the new category
					Category widgetCat = new Category(nameCat, iconCat);
					widgetList = widgetMap.get(widgetCat);
					if (widgetList == null)
					{
						widgetList = new ArrayList<Tool>();
						widgetMap.put(widgetCat, widgetList);
					}
					// Gets the Widget elements
					IConfigurationElement[] widgetElts = cfgElt.getChildren(WIDGET_ELEMENT);
					if (widgetElts != null && widgetElts.length > 0)
					{
						for (IConfigurationElement widgetElt: widgetElts)
						{
							try
							{
								String bundleName = widgetElt.getContributor().getName();
								// Load the class of the widget
								Bundle bundle = Platform.getBundle(bundleName);
								String className = widgetElt.getAttribute(WIDGET_CLASS_ATTR);
								if (className != null) {
									Class<?> theClass = bundle.loadClass(widgetElt.getAttribute(WIDGET_CLASS_ATTR));
									widgetClassList.add(theClass);
								}

								// Get the icons
								WidgetIcon icon = new WidgetIcon(extPlugInID,
									widgetElt.getAttribute(WIDGET_ICON_ATTR));

								// Creates the entry corresponding to the widget
								String name = widgetElt.getAttribute(WIDGET_NAME_ATTR);
								String scope = widgetElt.getAttribute(WIDGET_SCOPE_ATTR);
								String tooltip = widgetElt.getAttribute(WIDGET_TOOLTIP_ATTR);
								String content = widgetElt.getAttribute(WIDGET_CONTENT_ATTR);

								widgetList.add(new Tool(name, scope, tooltip, content, icon));
							}
							catch (Exception e)
							{
								// Nothing to do
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the <code>IConfigurationElement</code> list corresponding to the extension defined by
	 * <code>extensionId</code>
	 * 
	 * @param extensionId
	 *        the extension identifier
	 * @return the <code>IConfigurationElement</code> list
	 */
	private List<IConfigurationElement> getExtensions(String extensionId)
	{

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);
		IExtension[] extensions = extensionPoint.getExtensions();

		List<IConfigurationElement> resList = new ArrayList<IConfigurationElement>();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				resList.add(elements[j]);
			}
		}
		return resList;
	}

	/**
	 * Class which defines a widget category
	 */
	public class Category
	{
		private String name = null;
		private WidgetIcon icon = null;

		public Category(String name, WidgetIcon icon)
		{
			this.name = name;
			this.icon = icon;
		}

		public String getName()
		{
			return name;
		}

		public WidgetIcon getIcon()
		{
			return icon;
		}

		@Override
		public boolean equals(Object obj)
		{

			boolean equals = super.equals(obj);
			if (!equals && obj instanceof Category)
			{
				Category widgetCat = (Category) obj;
				equals = (name == null && widgetCat.name == null)
					|| (name != null && name.equals(widgetCat.name));
			}
			return equals;
		}

		@Override
		public int hashCode()
		{
			if (name != null)
			{
				return name.hashCode();
			}
			return super.hashCode();
		}
	}

	/**
	 * Class which defines a widget
	 */
	public class Tool
	{
		private String name = null;
		private String scope = null;
		private String tooltip = null;
		private String content = null;
		private WidgetIcon icon = null;

		public Tool(String name, String scope, String tooltip, String content, WidgetIcon icon)
		{
			this.name = name;
			this.scope = scope;
			this.tooltip = tooltip;
			this.content = content;
			this.icon = icon;
		}

		public String getName()
		{
			return name;
		}

		public String getScope()
		{
			return scope;
		}

		public String getToolTip()
		{
			return tooltip;
		}

		public String getContent()
		{
			return content;
		}

		public WidgetIcon getIcon()
		{
			return icon;
		}
	}

	/**
	 * Class which defines a widget icon
	 */
	public class WidgetIcon
	{
		private String bundleID = null;
		private String path = null;

		public WidgetIcon(String bundleID, String path)
		{
			this.bundleID = bundleID;
			this.path = path;
		}

		public String getBundleID()
		{
			return bundleID;
		}

		public String getPath()
		{
			return path;
		}
	}
}
