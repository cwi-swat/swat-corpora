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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.e4.xwt.tools.ui.designer.core.figures.ContentPaneFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.IVisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.swt.CompositeInfo;
import org.eclipse.e4.xwt.tools.ui.designer.editor.sash.SashFormChildResizableEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.LayoutEditPolicyFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class CompositeEditPart extends ControlEditPart {
	static final int WIDTH = 10;

	private ILayoutEditPolicy layoutEditPolicy;

	public CompositeEditPart(Composite composite, XamlNode model) {
		super(composite, model);
		setUseBorder(true);
	}

	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}

	/**
	 * @see org.VisualEditPart.xaml.ve.editor.editparts.GraphicalNodeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		layoutEditPolicy = LayoutEditPolicyFactory.getLayoutEditPolicy(this);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutEditPolicy);
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refresh()
	 */
	public void refresh() {
		super.refresh();
		// When the layout of the Composite is changed, we need to install a new one.
		ILayoutEditPolicy policy = LayoutEditPolicyFactory.getLayoutEditPolicy(this);
		if (layoutEditPolicy != null && layoutEditPolicy.getType() != policy.getType()) {
			removeEditPolicy(EditPolicy.LAYOUT_ROLE);
			installEditPolicy(EditPolicy.LAYOUT_ROLE, layoutEditPolicy = policy);
		}
		if (layoutEditPolicy != null) {
			layoutEditPolicy.refresh();
		}
	}

	public Layout getLayout() {
		Widget widget = getWidget();
		if (widget == null || widget.isDisposed() || !(widget instanceof Composite)) {
			return null;
		}
		return ((Composite) widget).getLayout();
	}

	public XamlElement getLayoutModel() {
		XamlElement model = (XamlElement) getModel();
		XamlAttribute attribute = model.getAttribute("layout");
		if (attribute == null) {
			return null;
		}
		EList<XamlElement> childNodes = attribute.getChildNodes();
		if (childNodes.size() == 1) {
			return childNodes.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class && (Boolean) getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED)) {
			if (layoutEditPolicy != null && layoutEditPolicy.getType() == LayoutType.NullLayout) {
				return new SnapToGeometry(this);
			}
		} else if (key == ILayoutEditPolicy.class) {
			return layoutEditPolicy;
		}
		return super.getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.soyatec.xaml.ve.xwt.editparts.ControlEditPart#createVisualComponent()
	 */
	public IVisualInfo createVisualInfo() {
		return new CompositeInfo((Composite) getWidget(), isRoot());
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (getParent() instanceof SashFormEditPart) {
			EditPart editPart = SashFormChildResizableEditPolicy.getTargetEditPart(request, this);
			if (editPart != null) {
				return editPart;
			}
		}
		return super.getTargetEditPart(request);
	}
}
