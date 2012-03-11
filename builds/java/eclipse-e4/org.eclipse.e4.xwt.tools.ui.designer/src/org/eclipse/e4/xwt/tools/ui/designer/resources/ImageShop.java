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
package org.eclipse.e4.xwt.tools.ui.designer.resources;

import java.util.Collection;

import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.utils.StyleHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ImageShop {
	private static final String ICON_PATH = "/icons/full";
	public static final String OBJ16 = ICON_PATH + "/obj16/";
	public static final String TOOLS = ICON_PATH + "/tools/";

	public static final String IMG_PREVIEW = TOOLS + "preview.png";
	public static final String IMG_LAYOUT_ASSIST = TOOLS + "layout_assist.gif";
	public static final String IMG_RESOURCES = TOOLS + "resources.gif";
	public static final String IMG_ELEMENT = TOOLS + "element.png";
	public static final String IMG_EVENT = TOOLS + "event.png";
	public static final String IMG_LISTENER_METHOD = TOOLS + "listener_method.gif";

	public static final String IMG_BINDING = TOOLS + "binding/binding.png";
	public static final String IMG_BINDING_ADD = TOOLS + "binding/binding_add.png";
	public static final String IMG_BINDING_DELETE = TOOLS + "binding/binding_delete.png";
	public static final String IMG_BINDING_REMOVE_ALL = TOOLS + "binding/binding_remove_all.png";
	public static final String IMG_BINDING_EDIT = TOOLS + "binding/binding_edit.png";

	public static final String IMG_ARRAY = TOOLS + "array.png";
	public static final String IMG_BOOLEAN = TOOLS + "boolean.png";
	public static final String IMG_COLLECTION = TOOLS + "collection.png";
	public static final String IMG_COLOR = TOOLS + "color.png";
	public static final String IMG_FONT = TOOLS + "font.png";
	public static final String IMG_IMAGE = TOOLS + "image.png";
	public static final String IMG_NUMBER = TOOLS + "number.png";
	public static final String IMG_OBJECT = TOOLS + "object.png";
	public static final String IMG_STRING = TOOLS + "string.png";
	public static final String IMG_VIEWER_COLLECTION = TOOLS + "viewer_collection.png";
	public static final String IMG_VIEWER = TOOLS + "viewer.png";
	public static final String IMG_CLEAR_FILTER = TOOLS + "clear_filter.gif";
	public static final String IMG_CLEAR = TOOLS + "clear.gif";

	public static final String IMG_BOLD = TOOLS + "bold.gif";
	public static final String IMG_ITALIC = TOOLS + "italic.gif";
	public static final String IMG_IMAGE_EDIT = TOOLS + "image_edit.png";
	public static final String IMG_BACKGROUND_IMAGE_EDIT = TOOLS + "background_image_edit.png";
	public static final String IMG_BACKGROUND_FILL_COLOR = TOOLS + "background_fill_color.png";
	public static final String IMG_FOREGROUND_FILL_COLOR = TOOLS + "foreground_fill_color.png";
	public static final String IMG_FONT_DELETE = TOOLS + "font_delete.png";

	public static final String IMAGE_OBSERVE_BEANS = TOOLS + "observe/beans.gif";
	public static final String IMAGE_OBSERVE_EMF = TOOLS + "observe/emf.png";
	public static final String IMAGE_OBSERVE_OBJECT = TOOLS + "observe/object.gif";
	public static final String IMAGE_OBSERVE_WIDGETS = TOOLS + "observe/widgets.gif";
	public static final String IMAGE_OBSERVE_XML = TOOLS + "observe/xml.gif";
	public static final String IMAGE_OBSERVE_CUSTOM = TOOLS + "observe/custom.png";
	public static final String IMG_XWT = TOOLS + "xaml.png";

	public static final String IMG_GOTO_DEFINITION = TOOLS + "goto_definition.gif";

	public static Image get(String imageFilePath) {
		ImageDescriptor imageDesc = getImageDescriptor(imageFilePath);
		if (imageDesc != null) {
			return imageDesc.createImage();
		}
		return getImageRegistry().get(imageFilePath);
	}

	public static Image getObj16(String name) {
		return get(OBJ16 + name + "_obj.gif");
	}

	public static Image getImageForWidget(Widget widget) {
		if (widget == null || widget.isDisposed()) {
			return null;
		}
		String name = widget.getClass().getSimpleName().toLowerCase();
		if ("combo".equals(name)) {
			name = "choice";
		} else if ("coolitem".endsWith(name)) {
			name = "coolbar";
		} else if ("toolitem".equals(name)) {
			if (StyleHelper.checkStyle(widget, SWT.PUSH)) {
				name = "toolitempush";
			} else if (StyleHelper.checkStyle(widget, SWT.DROP_DOWN)) {
				name = "toolitemdrop";
			} else if (StyleHelper.checkStyle(widget, SWT.CHECK)) {
				name = "toolitemcheck";
			} else if (StyleHelper.checkStyle(widget, SWT.RADIO)) {
				name = "toolitemradio";
			} else if (StyleHelper.checkStyle(widget, SWT.SEPARATOR)) {
				name = "toolitemseparator";
			}
		} else if ("label".equals(name) && StyleHelper.checkStyle(widget, SWT.SEPARATOR)) {
			name = "separator";
		} else if ("list".equals(name)) {
			name = "listbox";
		} else if ("button".equals(name)) {
			if (StyleHelper.checkStyle(widget, SWT.RADIO)) {
				name = "radiobutton";
			} else if (StyleHelper.checkStyle(widget, SWT.CHECK)) {
				name = "checkbox";
			} else if (StyleHelper.checkStyle(widget, SWT.TOGGLE)) {
				name = "toggledbutton";
			} else if (StyleHelper.checkStyle(widget, SWT.ARROW)) {
				name = "radiobutton";
			}
		}
		Image obj16 = getObj16(name);
		if (obj16 == null) {
			obj16 = getObj16("composite");
		}
		return obj16;
	}

	public static Image getImageForType(Class<?> type) {
		if (type == null) {
			return null;
		}
		if (type.isArray()) {
			return ImageShop.get(ImageShop.IMG_ARRAY);
		} else if (type == String.class) {
			return ImageShop.get(ImageShop.IMG_STRING);
		} else if (type == Color.class) {
			return ImageShop.get(ImageShop.IMG_COLOR);
		} else if (type == Font.class) {
			return ImageShop.get(ImageShop.IMG_FONT);
		} else if (type == int.class || type == Integer.class || float.class == type || Float.class == type) {
			return ImageShop.get(ImageShop.IMG_NUMBER);
		} else if (type == boolean.class || type == Boolean.class) {
			return ImageShop.get(ImageShop.IMG_BOOLEAN);
		} else if (Collection.class.isAssignableFrom(type)) {
			return ImageShop.get(ImageShop.IMG_COLLECTION);
		} else {
			String name = type.getSimpleName().toLowerCase();
			Image image = ImageShop.getObj16(name);
			if (image != null) {
				return image;
			}
		}
		return ImageShop.get(ImageShop.IMG_OBJECT);
	}

	public static ImageRegistry getImageRegistry() {
		return JFaceResources.getImageRegistry();
	}

	public static ImageDescriptor getImageDescriptor(String imageFilePath) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(XWTDesignerPlugin.PLUGIN_ID, imageFilePath);
	}
}
