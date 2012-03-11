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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class RowLayoutCommandsFactory extends LayoutCommandsFactory {

	/**
	 * @param host
	 */
	public RowLayoutCommandsFactory(EditPart host) {
		super(host);
	}

	public Command getResizeChildCommand(Object newSize) {
		return new ResizeCommand(getHost(), (Dimension) newSize);
	}

	public Command getCreateCommand(CreateRequest createRequest, EditPart after) {
		return new InsertCreateCommand(getHost(), after, createRequest);
	}


	class ResizeChildCommand extends Command {

		private Object newSize;

		/**
		 * 
		 * @param child
		 * @param newSize
		 */
		public ResizeChildCommand(Object newSize) {
			this.newSize = newSize;

		}

		/**
		 * @see org.eclipse.gef.commands.Command#execute()
		 */
		public void execute() {
			EList<XamlAttribute> attributes = getModel().getAttributes();

			// Create LayoutData attribute if it does not exist.
			XamlAttribute layoutDataAttr = getModel().getAttribute("layoutData");
			if (layoutDataAttr == null) {
				layoutDataAttr = XamlFactory.eINSTANCE.createAttribute("layoutData", IConstants.XWT_NAMESPACE);
			}

			// Create RowData element if it does not exist.
			if (newSize instanceof Dimension) {
				Dimension dimension = (Dimension) newSize;
				XamlElement attrElement = layoutDataAttr.getChild("RowData");
				if (attrElement == null) {
					attrElement = XamlFactory.eINSTANCE.createElement("RowData", IConstants.XWT_NAMESPACE);
					layoutDataAttr.getChildNodes().add(attrElement);
				}

				// Set the value of layoutData attribute.
				XamlAttribute width = attrElement.getAttribute("width");
				if (width == null) {
					width = XamlFactory.eINSTANCE.createAttribute("width", IConstants.XWT_NAMESPACE);
					int newvalue = getSize().width + dimension.width;
					width.setValue(newvalue + "");
					attrElement.getAttributes().add(width);
				} else if (!Integer.toString(dimension.width).equals(width.getValue())) {
					int newvalue = Integer.parseInt(width.getValue()) + dimension.width;
					width.setValue(newvalue + "");
				}
				XamlAttribute height = attrElement.getAttribute("height");
				if (height == null) {
					height = XamlFactory.eINSTANCE.createAttribute("height", IConstants.XWT_NAMESPACE);
					int newvalue = getSize().height + dimension.height;
					height.setValue(newvalue + "");
					attrElement.getAttributes().add(height);
				} else if (!Integer.toString(dimension.height).equals(height.getValue())) {
					int newvalue = Integer.parseInt(height.getValue()) + dimension.height;
					height.setValue(newvalue + "");
				}
			}

			// And the LayoutData attribute to model if it does not exist.
			if (!attributes.contains(layoutDataAttr)) {
				attributes.add(layoutDataAttr);
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#canExecute()
		 */
		public boolean canExecute() {
			return getModel() != null;
		}

		private Dimension getSize() {
			IFigure contentPane = ((GraphicalEditPart) getHost()).getContentPane();
			return contentPane.getSize();
		}
	}

}
