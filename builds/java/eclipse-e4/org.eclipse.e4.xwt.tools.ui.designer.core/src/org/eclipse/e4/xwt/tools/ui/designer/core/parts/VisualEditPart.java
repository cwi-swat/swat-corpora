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
package org.eclipse.e4.xwt.tools.ui.designer.core.parts;

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ImageFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.OutlineBorder;
import org.eclipse.e4.xwt.tools.ui.designer.core.images.IImageListener;
import org.eclipse.e4.xwt.tools.ui.designer.core.images.ImageFigureController;
import org.eclipse.e4.xwt.tools.ui.designer.core.policies.DefaultComponentEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.JavaModelUtil;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.IVisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt.WidgetInfo;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

/**
 * @author jliu jin.liu@soyatec.com
 */
public abstract class VisualEditPart extends AbstractGraphicalEditPart implements IActionFilter {

	private boolean useBorder = false;
	private boolean transparent;
	private ImageFigureController imageFigureController;
	private IImageListener imageListener;
	private IVisualInfo visualInfo;

	public VisualEditPart(EObject model) {
		setModel(model);
	}

	public boolean testAttribute(Object target, String name, String value) {
		if (ActionFilterConstants.MODEL_TYPE.equals(name)) {
			Object model = getModel();
			if (model != null) {
				Class<?> type = model.getClass();
				return JavaModelUtil.isKindOf(type, value);
			}
		}
		return false;
	}
	
	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		ContentPaneFigure figure = new ContentPaneFigure();
		IFigure contentPane = null;
		if (!isRoot()) {
			contentPane = createContentPane();
		} else {
			ImageFigure imageFigure = new ImageFigure();
			if (isUseBorder())
				imageFigure.setBorder(new OutlineBorder(150,
						ColorConstants.lightGray, null, Graphics.LINE_SOLID));
			imageFigure.setOpaque(!isTransparent());
			if (!isTransparent()) {
				imageFigureController = new ImageFigureController();
				imageFigureController.setImageFigure(imageFigure);
			}
			contentPane = imageFigure;
		}
		contentPane.setLayoutManager(new XYLayout());
		figure.setContentPane(contentPane);
		return figure;
	}

	protected IFigure createContentPane() {
		return new Label();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return getContentPaneFigure().getContentPane();
	}

	protected ContentPaneFigure getContentPaneFigure() {
		return ((ContentPaneFigure) getFigure());
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// Default component role allows delete and basic behavior of a
		// component within a parent edit part that contains it
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DefaultComponentEditPolicy());

	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		if (!isTransparent() && imageFigureController != null) {
			imageFigureController.setImageNotifier(getVisualInfo());
		}
		if (imageListener == null) {
			imageListener = new IImageListener() {
				public void imageChanged(Image image) {
					refreshVisuals();
					for (Iterator iterator = getChildren().iterator(); iterator
							.hasNext();) {
						EditPart child = (EditPart) iterator.next();
						if (child instanceof VisualEditPart) {
							((VisualEditPart) child).refreshVisuals();
						}
					}
				}
			};
		}
		getVisualInfo().addImageListener(imageListener);
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refresh()
	 */
	public void refresh() {
		// Do nothing here, sub class can add some other refreshes.
	}

	protected boolean isVisualInfoObsolate() {
		return false;
	}
	
	public final void refresh(RefreshContext context) {
		if (isVisualInfoObsolate()) {
			visualInfo = null; // to create a new one
		}
		
		if (context == null) {
			return;
		}
		if (!validateVisuals()) {
			return;
		}
		// refresh children here.
		if (context.refreshChildren()) {
			refreshChildren();
		}

		// try to refresh image and visuals.
		if (context.refreshImage()) {
			getUIDisplay().asyncExec(new Runnable() {
				public void run() {
					refreshImage();
				}
			});
			context.setImageRefreshed();
		}
		if (context.refreshVisuals()) {
			refreshVisuals();
		}
		// allow subclass refresh.
		refresh();
	}

	protected boolean validateVisuals() {
		return getVisualInfo() != null
				&& getVisualInfo().getVisualObject() != null;
	}

	/**
	 * This method has declared to final, please use refresh(RefreshContext).
	 */
	protected final void refreshImage() {
		VisualEditPart root = getRootVisualEditPart();
		if (root.validateVisuals()) {
			root.getVisualInfo().refreshImage();
		}
	}

	/**
	 * This method has declared to final, please use refresh(RefreshContext).
	 */
	protected final void refreshVisuals() {
		if (!validateVisuals()) {
			return;
		}
		IFigure figure = getFigure();
		if (figure != null && figure.getParent() != null) {
			Rectangle r = new Rectangle(getBounds());
			figure.setBounds(r);
			setLayoutConstraint(VisualEditPart.this, figure, r);
		}
	}

	/**
	 * Don't remove this overwrite method. see RefreshJob. This method has
	 * declared to final, please use refresh(RefreshContext).
	 */
	protected final void refreshChildren() {
		super.refreshChildren();
	}

	protected Display getUIDisplay() {
		IVisualInfo visualInfo = getVisualInfo();
		if (visualInfo instanceof WidgetInfo) {
			return ((WidgetInfo) visualInfo).getDisplay();
		}
		return Display.getCurrent();
	}

	protected VisualEditPart getRootVisualEditPart() {
		EditPart root = this;
		while (!(root.getParent() instanceof AbstractDiagramEditPart)) {
			if (root.getParent() == null) {
				break;
			}
			root = root.getParent();
		}
		return (VisualEditPart) root;
	}

	protected Rectangle getBounds() {
		return getVisualInfo().getBounds();
	}

	/**
	 * @return the visualComponent
	 */
	public IVisualInfo getVisualInfo() {
		if (visualInfo == null) {
			visualInfo = createVisualInfo();
		}
		return visualInfo;
	}

	/**
	 * @return
	 */
	protected abstract IVisualInfo createVisualInfo();

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		if (imageFigureController != null) {
			imageFigureController.deactivate();
		}
		IVisualInfo visualInfo = getVisualInfo();
		if (imageListener != null && visualInfo != null) {
			visualInfo.removeImageListener(imageListener);
		}
	}

	/**
	 * @param useBorder
	 *            the useBorder to set
	 */
	public void setUseBorder(boolean useBorder) {
		this.useBorder = useBorder;
	}

	/**
	 * @return the useBorder
	 */
	public boolean isUseBorder() {
		return useBorder;
	}

	/**
	 * @param transparent
	 *            the transparent to set
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * @return the transparent
	 */
	public boolean isTransparent() {
		return transparent;
	}

	public boolean isRoot() {
		return getParent() instanceof AbstractDiagramEditPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModel()
	 */
	public EObject getCastModel() {
		return (EObject) super.getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString() {
		IVisualInfo visualInfo = getVisualInfo();
		if (visualInfo != null && visualInfo.getVisualObject() != null) {
			return "EditPart("
					+ visualInfo.getVisualObject().getClass().getSimpleName()
					+ ")";
		}
		return "";
	}

}
