/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Anaik Trihoreau <anaik@anyware-tech.com> - inital API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.vex.palette;

import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.vex.palette.customize.CustomWidgetManager;
import org.eclipse.e4.xwt.vex.palette.customize.CustomWidgetManager.Tool;
import org.eclipse.e4.xwt.vex.palette.customize.CustomWidgetManager.Category;
import org.eclipse.e4.xwt.vex.palette.customize.CustomWidgetManager.WidgetIcon;
import org.eclipse.e4.xwt.vex.Activator;
import org.eclipse.e4.xwt.vex.palette.part.ToolPaletteDrawer;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

public class CustomWidgetDrawer
{

	public static void addCustomWidgets(PaletteDrawer parentDrawer)
	{

		Map<Category, List<Tool>> widgetMap = CustomWidgetManager.getInstance().getWidgetCategories();

		for (Map.Entry<Category, List<Tool>> widgetMapEntry: widgetMap.entrySet())
		{
			Category widgetCategory = widgetMapEntry.getKey();
			List<Tool> widgetList = widgetMapEntry.getValue();
			if (!widgetList.isEmpty())
			{
				// Create a new palette drawer
				String nameCat = widgetCategory.getName();
				PaletteDrawer componentsDrawer = findPaletteDrawer(parentDrawer, nameCat);
				if (componentsDrawer == null)
				{
					componentsDrawer = new ToolPaletteDrawer(widgetCategory.getName());
					componentsDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
					ImageDescriptor iconCat = _getImageDescriptor(widgetCategory.getIcon());
					componentsDrawer.setSmallIcon(iconCat);
					componentsDrawer.setLargeIcon(iconCat);
					parentDrawer.add(componentsDrawer);
				}

				for (Tool widget: widgetList)
				{
					Entry entry = ToolPaletteFactory.eINSTANCE.createEntry();
					entry.setName(widget.getName());
					entry.setScope(widget.getScope());
					entry.setToolTip(widget.getToolTip());
					entry.setContent(widget.getContent());
					ImageDescriptor icon = _getImageDescriptor(widget.getIcon());

					PaletteEntry paletteEntry = new CombinedTemplateCreationEntry(entry.getName(),
						entry.getToolTip(), entry, new SimpleFactory(entry.getClass()), icon, icon);
					componentsDrawer.add(paletteEntry);
				}
			}
		}
	}

	/**
	 * Finds the PaletteDrawer corresponding to the given name.
	 * 
	 * @param name
	 *        the name
	 * @return the PaletteDrawer
	 */
	private static PaletteDrawer findPaletteDrawer(PaletteDrawer parentDrawer, String name)
	{
		if (name != null)
		{
			for (Object object: parentDrawer.getChildren())
			{
				if (object instanceof PaletteDrawer)
				{
					PaletteDrawer paletteDrawer = (PaletteDrawer) object;
					if (name.equals(paletteDrawer.getLabel()))
					{
						return paletteDrawer;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the <code>ImageDescriptor</code> corresponding to the specified
	 * <code>WidgetIcon</code>
	 * 
	 * @param widgetIcon
	 *        the <code>WidgetIcon</code>
	 * @return the <code>ImageDescriptor</code>
	 */
	private static ImageDescriptor _getImageDescriptor(WidgetIcon widgetIcon)
	{
		return Activator.getImageDescriptor(widgetIcon.getBundleID(), widgetIcon.getPath());
	}
}
