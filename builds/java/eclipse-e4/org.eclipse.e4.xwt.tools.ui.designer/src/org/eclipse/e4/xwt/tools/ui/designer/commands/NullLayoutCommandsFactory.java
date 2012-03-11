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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.FigureUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class NullLayoutCommandsFactory extends LayoutCommandsFactory {

	/**
	 * @param host
	 */
	public NullLayoutCommandsFactory(EditPart host) {
		super(host);
	}

	public Command getCreateCommand(CreateRequest createRequest) {
		return new CreateCommand(getHost(), createRequest);
	}

	public Command getChangeConstraintCommand(Object constraint) {
		if (constraint instanceof Rectangle) {
			return new ChangeConstraintCommand(getHost(), (Rectangle) constraint, true);
		}
		return null;
	}

	static class CreateCommand extends AbstractCreateCommand {
		private static final int DEFAULT_WIDTH_WIDGET = 48;
		private static final int DEFAULT_HEIGHT_WIDGET = 26;
		private static final int DEFAULT_HEIGHT_COMPOSITE = 100;
		private static final int DEFAULT_WIDTH_COMPOSITE = 100;

		public CreateCommand(EditPart parent, CreateRequest createRequest) {
			super(parent, createRequest);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.e4.xwt.tools.ui.designer.commands.AbstractCreationCommand#preExecute(org.soyatec.tools.designer.xaml.XamlNode, org.eclipse.gef.requests.CreateRequest)
		 */
		protected void preExecute(XamlNode newNode, CreateRequest createRequest) {
			Point location = createRequest.getLocation();
			Dimension size = createRequest.getSize();
			if (location == null) {
				return;
			}
			location = FigureUtil.translateToRelative(parent, location);
			int x = location.x;
			int y = location.y;

			IMetaclass metaclass = XWTUtility.getMetaclass(newNode);
			if (metaclass == null) {
				return;
			}

			int width = DEFAULT_WIDTH_WIDGET, height = DEFAULT_HEIGHT_WIDGET;
			if (size != null) {
				width = size.width;
				height = size.height;
			} else if (Composite.class.isAssignableFrom(metaclass.getType())) {
				width = DEFAULT_WIDTH_COMPOSITE;
				height = DEFAULT_HEIGHT_COMPOSITE;
			}
			String boundsValue = StringUtil.format(new Object[] { x, y, width, height });
			if (newNode instanceof XamlElement) {
				XamlElement childElement = (XamlElement) newNode;
				if (Viewer.class.isAssignableFrom(metaclass.getType())) {
					XamlAttribute attribute = XWTModelUtil.getChildAttribute(childElement, "bounds", IConstants.XWT_NAMESPACE);
					if (attribute == null) {
						XamlAttribute childAttr = childElement.getAttribute("control");
						if (childAttr == null) {
							childAttr = XamlFactory.eINSTANCE.createAttribute("control", IConstants.XWT_NAMESPACE);
							childElement.getAttributes().add(childAttr);
						}
						attribute = XamlFactory.eINSTANCE.createAttribute("bounds", IConstants.XWT_NAMESPACE);
						childAttr.getAttributes().add(attribute);
					}
					attribute.setValue(boundsValue);
				} else {
					XamlAttribute boundsAttr = childElement.getAttribute("bounds", IConstants.XWT_NAMESPACE);
					if (boundsAttr == null) {
						boundsAttr = XamlFactory.eINSTANCE.createAttribute("bounds", IConstants.XWT_NAMESPACE);
						childElement.getAttributes().add(boundsAttr);
					}
					boundsAttr.setValue(boundsValue);
				}
			}

		}
	}

}
