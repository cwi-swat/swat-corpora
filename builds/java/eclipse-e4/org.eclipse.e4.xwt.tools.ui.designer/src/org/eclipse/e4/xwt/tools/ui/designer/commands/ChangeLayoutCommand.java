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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.IVisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt.CompositeInfo;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class ChangeLayoutCommand extends Command {

	private LayoutType layoutType;
	private EditPart editPart;
	private XamlElement element;
	private XamlAttribute layoutAttr;
	private XamlElement newLayout;
	private XamlElement oldLayout;
	private boolean isNewAttr;

	/**
	 * @param editPart
	 * @param text
	 */
	public ChangeLayoutCommand(EditPart editPart, LayoutType layoutType) {
		this.editPart = editPart;
		this.layoutType = layoutType;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (editPart == null || !(editPart instanceof CompositeEditPart)) {
			return false;
		}
		if (layoutType == null || layoutType == LayoutsHelper.getLayoutType(editPart)) {
			return false;
		}
		return editPart.getModel() instanceof XamlElement;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		element = (XamlElement) editPart.getModel();
		updateLayoutData(element, layoutType);
		layoutAttr = element.getAttribute("layout");
		if (layoutType == LayoutType.NullLayout) {
			element.getAttributes().remove(layoutAttr);
			return;
		}
		if (layoutAttr == null) {
			layoutAttr = XamlFactory.eINSTANCE.createAttribute("layout", IConstants.XWT_NAMESPACE);
			isNewAttr = true;
		}
		EList<XamlElement> childNodes = layoutAttr.getChildNodes();
		if (childNodes.size() == 1) {
			oldLayout = childNodes.get(0);
		}
		childNodes.clear();
		if (layoutType != LayoutType.NullLayout) {
			newLayout = XamlFactory.eINSTANCE.createElement(layoutType.value(), IConstants.XWT_NAMESPACE);
			childNodes.add(newLayout);
		}
		if (isNewAttr && layoutType != LayoutType.NullLayout) {
			element.getAttributes().add(layoutAttr);
		}
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		if (element == null) {
			return false;
		}
		if (isNewAttr) {
			return layoutAttr != null;
		}
		return layoutAttr != null && oldLayout != null;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		if (isNewAttr) {
			element.getAttributes().remove(layoutAttr);
		} else if (oldLayout != null) {
			layoutAttr.getChildNodes().clear();
			layoutAttr.getChildNodes().add(oldLayout);
		} else if (layoutAttr != null && element.getAttribute("layout", IConstants.XWT_NAMESPACE) == null) {
			element.getAttributes().add(layoutAttr);
		}
	}

	private void updateLayoutData(XamlElement element, LayoutType layoutType) {
		if (element == null) {
			return;
		}
		if (layoutType != LayoutType.NullLayout) {
			// just remove them.
			EList<XamlElement> childNodes = element.getChildNodes();
			for (XamlElement subElement : childNodes) {
				XamlAttribute attribute = subElement.getAttribute("layoutData");
				if (attribute != null) {
					subElement.getAttributes().remove(attribute);
				}
			}
		} else if (editPart != null && editPart instanceof VisualEditPart) {
			IVisualInfo visualInfo = ((VisualEditPart) editPart).getVisualInfo();
			Composite parent = null;
			if (visualInfo != null && visualInfo instanceof CompositeInfo) {
				parent = (Composite) ((CompositeInfo) visualInfo).getVisualObject();
			}
			if (parent == null || parent.isDisposed()) {
				return;
			}
			Map<Object, Control> model2control = new HashMap<Object, Control>();
			for (Control child : parent.getChildren()) {
				model2control.put(XWTProxy.getModel(child), child);
			}
			EList<XamlElement> childNodes = element.getChildNodes();
			for (XamlElement xamlElement : childNodes) {
				Control control = model2control.get(xamlElement);
				IMetaclass metaclass = XWTUtility.getMetaclass(xamlElement);
				if (metaclass == null || !Control.class.isAssignableFrom(metaclass.getType()) || control == null) {
					continue;
				}
				Rectangle r = control.getBounds();
				boolean boundsNoExisted = true, sizeNoExisted = true, locationNoExisted = true;
				XamlAttribute boundsAttr = xamlElement.getAttribute("bounds");
				if (boundsAttr != null) {
					boundsAttr.setValue(toString(r));
					boundsNoExisted = false;
				}
				XamlAttribute sizeAttr = xamlElement.getAttribute("size");
				if (sizeAttr != null) {
					sizeAttr.setValue(toString(new Point(r.width, r.height)));
					sizeNoExisted = false;
				}
				XamlAttribute locationAttr = xamlElement.getAttribute("location");
				if (locationAttr != null) {
					locationAttr.setValue(toString(new Point(r.x, r.y)));
					locationNoExisted = false;
				}

				if (boundsNoExisted && sizeNoExisted && locationNoExisted) {
					boundsAttr = XamlFactory.eINSTANCE.createAttribute("bounds", IConstants.XWT_NAMESPACE);
					boundsAttr.setValue(toString(r));
					xamlElement.getAttributes().add(boundsAttr);
				} else if (boundsNoExisted && sizeNoExisted) {
					sizeAttr = XamlFactory.eINSTANCE.createAttribute("size", IConstants.XWT_NAMESPACE);
					sizeAttr.setValue(toString(new Point(r.width, r.height)));
					xamlElement.getAttributes().add(sizeAttr);
				} else if (boundsNoExisted && locationNoExisted) {
					locationAttr = XamlFactory.eINSTANCE.createAttribute("location", IConstants.XWT_NAMESPACE);
					locationAttr.setValue(toString(new Point(r.x, r.y)));
					xamlElement.getAttributes().add(locationAttr);
				}
			}
		}
	}

	private String toString(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof Rectangle) {
			Rectangle r = (Rectangle) object;
			return r.x + "," + r.y + "," + r.width + "," + r.height;
		} else if (object instanceof Point) {
			Point p = (Point) object;
			return p.x + "," + p.y;
		}
		return object.toString();
	}
}
