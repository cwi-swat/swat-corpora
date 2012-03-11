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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.parts.figures.Expandable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ExpandableEditPolicy extends GraphicalEditPolicy implements EditPartListener {

	private List<Object> listenedParts = new ArrayList<Object>();

	protected FocusListener focusListener = new FocusListener();

	private class FocusListener extends EditPartListener.Stub {
		public void selectedStateChanged(EditPart part) {
			if (shouldExpanded(part)) {
				expand();
			} else {
				collapse();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		addFocusListener(getHost());
	}

	private void addFocusListener(EditPart part) {
		if (part == null) {
			return;
		}
		if (!listenedParts.contains(part)) {
			part.addEditPartListener(focusListener);
			part.addEditPartListener(this);
			listenedParts.add(part);
		}
		List<EditPart> childList = part.getChildren();
		if (childList != null) {
			for (EditPart ep : childList) {
				addFocusListener(ep);
			}
		}
	}

	private void removeFocusListener(EditPart part) {
		if (part == null) {
			return;
		}
		if (listenedParts.remove(part)) {
			part.removeEditPartListener(focusListener);
			part.removeEditPartListener(this);
		}
		List<EditPart> childList = part.getChildren();
		if (childList != null) {
			for (EditPart ep : childList) {
				removeFocusListener(ep);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		removeFocusListener(getHost());
		IFigure figure = getHostFigure();
		if (figure instanceof Expandable && getLayer() == figure.getParent()) {
			getLayer().remove(figure);
		}
	}

	public void expand() {
		IFigure figure = getHostFigure();
		if (figure instanceof Expandable && getLayer() != figure.getParent()) {
			getLayer().add(figure);
			((Expandable) figure).expand();
		}
	}

	public IFigure getLayer() {
		return getLayer(LayerConstants.CONNECTION_LAYER);
	}

	public void collapse() {
		IFigure figure = getHostFigure();
		if (figure instanceof Expandable) {
			((Expandable) figure).collapse();
			// getLayer().remove(figure);
		}
	}

	protected boolean shouldExpanded(EditPart part) {
		return part.getSelected() != EditPart.SELECTED_NONE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartListener#childAdded(org.eclipse.gef.EditPart, int)
	 */
	public void childAdded(final EditPart child, int index) {
		addFocusListener(child);
		if (child instanceof GraphicalEditPart) {
			IFigure figure = ((GraphicalEditPart) child).getFigure();
			if (figure instanceof Expandable) {
				((Expandable) figure).expand();
				getLayer().add(figure);
			}
		}
		DisplayUtil.asyncExec(new Runnable() {
			public void run() {
				child.getViewer().select(child);
				expand();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartListener#partActivated(org.eclipse.gef.EditPart)
	 */
	public void partActivated(EditPart editpart) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartListener#partDeactivated(org.eclipse.gef.EditPart)
	 */
	public void partDeactivated(EditPart editpart) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartListener#removingChild(org.eclipse.gef.EditPart, int)
	 */
	public void removingChild(EditPart child, int index) {
		if (child instanceof GraphicalEditPart) {
			IFigure figure = ((GraphicalEditPart) child).getFigure();
			if (figure instanceof Expandable && figure.getParent() == getLayer()) {
				((Expandable) figure).collapse();
				getLayer().remove(figure);
			}
		}
		removeFocusListener(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartListener#selectedStateChanged(org.eclipse.gef.EditPart)
	 */
	public void selectedStateChanged(EditPart editpart) {
		// do nothing
	}
}
