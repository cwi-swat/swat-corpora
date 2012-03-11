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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.SimpleEtchedBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class LoadingFigureController {
	protected static final Insets INSETS = new Insets(10, 25, 10, 25);

	protected GraphicalViewer viewer;
	protected Label loadingFigure;
	protected boolean showingLoadingFigure = true;

	public IFigure getRootFigure(IFigure target) {
		IFigure parent = target.getParent();
		while (parent.getParent() != null)
			parent = parent.getParent();
		return parent;
	}

	/**
	 * Call when we have a viewer to actually work with. At this point in time we can now display the current loading status.
	 * 
	 * This allows us to start listening before we have a viewer. This should only be called once.
	 */
	public void startListener(GraphicalViewer viewer) {
		this.viewer = viewer;
		loadingFigure = new Label("Loading...") {
			Locator locator = new Locator() {
				public void relocate(IFigure target) {
					// Center the figure in the middle of the canvas
					Dimension canvasSize = getRootFigure(target).getSize();
					Dimension prefSize = target.getPreferredSize();
					int newX = (canvasSize.width - prefSize.width) / 2;
					int newY = (canvasSize.height - prefSize.height) / 2;
					Rectangle b = new Rectangle(newX, newY, prefSize.width, prefSize.height).expand(INSETS);
					target.translateToRelative(b);
					target.setBounds(b);
				}
			};

			public void validate() {
				if (!isValid())
					locator.relocate(this);
				super.validate();
			}
		};
		loadingFigure.setEnabled(true);
		loadingFigure.setOpaque(true);
		loadingFigure.setBorder(SimpleEtchedBorder.singleton);
		if (showingLoadingFigure)
			showLoadingFigure();
	}

	public void showLoadingFigure(boolean show) {
		if (show) {
			if (!showingLoadingFigure)
				showLoadingFigure();
		} else if (showingLoadingFigure)
			removeLoadingFigure();
	}

	protected Layer getLoadingLayer() {
		return (Layer) LayerManager.Helper.find(viewer.getRootEditPart()).getLayer(LayerConstants.HANDLE_LAYER);
	}

	private FigureListener rootFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			loadingFigure.revalidate();
		}
	};

	private PropertyChangeListener scrolledListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (RangeModel.PROPERTY_VALUE.equals(evt.getPropertyName()))
				loadingFigure.revalidate(); // Scrollbar has moved, so revalidate.
		}

	};

	protected void removeLoadingFigure() {
		showingLoadingFigure = false;
		if (viewer != null) {
			Layer layer = getLoadingLayer();
			if (layer.getChildren().contains(loadingFigure)) {
				layer.remove(loadingFigure);
			}
			Viewport vp = getViewport(layer);
			if (vp != null) {
				vp.getHorizontalRangeModel().removePropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().removePropertyChangeListener(scrolledListener);
			}
			getRootFigure(layer).removeFigureListener(rootFigureListener);
		}
	}

	protected Viewport getViewport(IFigure figure) {
		IFigure f = figure;
		while (f != null && !(f instanceof Viewport))
			f = f.getParent();
		return (Viewport) f;
	}

	protected void showLoadingFigure() {
		showingLoadingFigure = true;
		if (viewer != null) {
			Layer layer = getLoadingLayer();
			layer.add(loadingFigure);
			Viewport vp = getViewport(layer);
			if (vp != null) {
				vp.getHorizontalRangeModel().addPropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().addPropertyChangeListener(scrolledListener);
			}
			getRootFigure(layer).addFigureListener(rootFigureListener);
			loadingFigure.revalidate();
		}
	}
}
