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
package org.eclipse.e4.xwt.tools.ui.designer.editor.outline;

import java.text.MessageFormat;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.parts.DiagramEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ViewerEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutPolicyHelper;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.tools.AnnotationTools;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class OutlineLabelProvider extends LabelProvider {

	private GraphicalViewer graphicalViewer;

	public OutlineLabelProvider() {
		this(null);
	}

	public OutlineLabelProvider(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
	}

	public String getText(Object element) {
		if (element instanceof XamlNode) {
			return getText((XamlNode) element);
		} else if (element instanceof XamlDocument) {
			return "(widget)";
		} else if (element instanceof EditPart) {
			return getText(((EditPart) element).getModel());
		} else if (element instanceof IStructuredSelection) {
			return getText(((IStructuredSelection) element).getFirstElement());
		}
		return super.getText(element);
	}

	private String getText(XamlNode node) {
		if (AnnotationTools.isAnnotated(node,
				GridLayoutPolicyHelper.FILLER_DATA)) {
			return "(filler)";
		}
		String name = node.getName();
		IMetaclass metaclass = XWTUtility.getMetaclass(node);
		if (metaclass != null) {
			XamlAttribute textAttr = node.getAttribute("text");
			if (textAttr == null) {
				textAttr = node.getAttribute("text", IConstants.XWT_NAMESPACE);
			}
			XamlAttribute nameAttr = node.getAttribute("name",
					IConstants.XWT_NAMESPACE);
			if (nameAttr == null) {
				nameAttr = node
						.getAttribute("name", IConstants.XWT_X_NAMESPACE);
			}
			String value = null;
			if (nameAttr != null && nameAttr.getValue() != null) {
				value = MessageFormat.format("\"{0}\"", nameAttr.getValue());
			} else if (textAttr != null && textAttr.getValue() != null) {
				value = MessageFormat.format("\"{0}\"", textAttr.getValue());
			}
			if (value != null) {
				name = MessageFormat.format("{0} - {1}", new Object[] { name,
						value });
			}
		}
		return name;
	}

	public Image getImage(Object element) {
		if (element instanceof XamlDocument) {
			return ImageShop.get(ImageShop.IMG_XWT);
		}
		if (element instanceof XamlNode) {
			XamlNode xamlNode = (XamlNode) element;
			if (graphicalViewer != null) {
				EditPart editPart = (EditPart) graphicalViewer
						.getEditPartRegistry().get(xamlNode);
				Image image = getImageFrom(editPart);
				if (image != null) {
					return image;
				}
			}
			String name = xamlNode.getName();
			return ImageShop.getObj16(name.toLowerCase());
		}
		return super.getImage(element);
	}

	private Image getImageFrom(EditPart editPart) {

		if (editPart instanceof WidgetEditPart) {
			Widget widget = ((WidgetEditPart) editPart).getWidget();
			return ImageShop.getImageForWidget(widget);
		} else if (editPart instanceof ViewerEditPart) {
			Viewer viewer = ((ViewerEditPart) editPart).getJfaceViewer();
			if (viewer != null) {
				String name = viewer.getClass().getSimpleName().toLowerCase();
				return ImageShop.getObj16(name);
			}
		} else if (editPart instanceof DiagramEditPart) {
			return ImageShop.get(ImageShop.IMG_XWT);
		}
		return null;
	}
}
