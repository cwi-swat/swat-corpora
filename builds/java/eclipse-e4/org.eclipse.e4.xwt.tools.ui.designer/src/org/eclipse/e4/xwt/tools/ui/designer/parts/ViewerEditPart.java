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
package org.eclipse.e4.xwt.tools.ui.designer.parts;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.IVisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt.ViewerInfo;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ViewerEditPart extends VisualEditPart {

	protected Viewer viewer;

	public ViewerEditPart(Viewer viewer, XamlNode model) {
		super(model);
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.soyatec.tools.designer.parts.GraphicalNodeEditPart#createVisualInfo()
	 */
	protected IVisualInfo createVisualInfo() {
		return new ViewerInfo(viewer, isRoot());
	}

	/*
	 * (non-Javadoc)
	 * @see org.soyatec.tools.designer.parts.GraphicalNodeEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		ContentPaneFigure figure = new ContentPaneFigure();
		Label pane = new Label();
		// pane.setForegroundColor(ColorConstants.red);
		pane.setLayoutManager(new AbstractLayout() {
			public void layout(IFigure parent) {
				Rectangle r = parent.getBounds().getCopy();
				Iterator children = parent.getChildren().iterator();
				IFigure f;
				while (children.hasNext()) {
					f = (IFigure) children.next();
					f.setBounds(r.shrink(1, 1));
				}
			}

			protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
				return container.getPreferredSize();
			}
		});
		figure.setContentPane(pane);
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.parts.GraphicalNodeEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		if (viewer != null) {
			Control control = viewer.getControl();
			XamlNode model = XWTProxy.getModel(control);
			if (model == null) {
				model = XWTModelUtil.getAdaptableAttribute(getCastModel(), "control", IConstants.XWT_NAMESPACE);
			}
			if (model == null) {
				model = XamlFactory.eINSTANCE.createAttribute("control", IConstants.XWT_NAMESPACE);
				getCastModel().getAttributes().add((XamlAttribute) model);
			}
			return Collections.singletonList(model);
		}
		return super.getModelChildren();
	}

	public Viewer getJfaceViewer() {
		return viewer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ConstrainedLayoutEditPolicy() {
			protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
				return null;
			}

			protected Object getConstraintFor(Point point) {
				return null;
			}

			protected Object getConstraintFor(Rectangle rect) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart#getCastModel()
	 */
	public XamlNode getCastModel() {
		return (XamlNode) super.getCastModel();
	}
}
