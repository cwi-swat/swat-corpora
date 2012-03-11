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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualRenderer;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingHelper;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.DataContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTVisualRenderer;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DataContextEditPart extends AbstractGraphicalEditPart {

	private DataContext dataContext;

	public DataContextEditPart(DataContext dataContext) {
		setModel(dataContext);
		this.dataContext = dataContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		XWTVisualRenderer controlRender = (XWTVisualRenderer) EditDomain.getEditDomain(this).getViewerData(getViewer(), IVisualRenderer.KEY);
		Object root = controlRender.getRoot();
		if (root != null && root instanceof Widget) {
			root = XWTProxy.getModel((Widget) root);
			EditPart rootEp = (EditPart) getViewer().getEditPartRegistry().get(root);
			if (rootEp != null) {
				List<BindingInfo> sources = new ArrayList<BindingInfo>();
				List<BindingInfo> allBindings = BindingHelper.getBindings(rootEp);
				for (BindingInfo bindingInfo : allBindings) {
					IObservable observeModel = bindingInfo.getModel();
					if (dataContext.equals(observeModel.getSource())) {
						sources.add(bindingInfo);
					}
				}
				return sources;
			}
		}
		return super.getModelSourceConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Ellipse figure = new Ellipse();
		figure.setAntialias(SWT.ON);
		figure.setForegroundColor(ColorConstants.blue);
		figure.setBackgroundColor(ColorConstants.yellow);
		figure.setLayoutManager(new ToolbarLayout(true));
		// Label title = new Label("D");
		// title.setTextAlignment(Label.CENTER);
		// title.setSize(15, 15);
		// title.setTextPlacement(PositionConstants.EAST);
		// figure.add(title);
		// ImageFigure imageFigure = new ImageFigure(ImageShop.get(ImageShop.IMAGE_OBSERVE_BEANS));
		// figure.add(imageFigure);
		Label label = new Label();
		if (dataContext != null && dataContext.getKey() != null) {
			label.setText(dataContext.getKey());
		}
		figure.setToolTip(label);
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		GraphicalEditPart parent = (GraphicalEditPart) getParent();
		Rectangle bounds = parent.getFigure().getBounds().getCopy();
		Rectangle rect = new Rectangle();
		rect.setLocation(bounds.x + 1, bounds.y + 1);
		rect.setSize(16, 16);
		getFigure().setBounds(rect);
		super.refreshVisuals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {

	}

}
