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
package org.eclipse.e4.xwt.tools.ui.designer.policies.layout.form;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.Draw2dTools;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ShellEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FormLayoutHelper {

	private CompositeEditPart compositeEp;

	public FormLayoutHelper(CompositeEditPart compositeEditPart) {
		this.compositeEp = compositeEditPart;
	}

	public Rectangle getClientArea() {
		if (compositeEp instanceof ShellEditPart) {
			return ((ShellEditPart) compositeEp).getFigure().getBounds();
		} else if (compositeEp != null && compositeEp.getVisualInfo() != null) {
			return compositeEp.getVisualInfo().getClientArea();
		}
		return new Rectangle();
	}

	private Composite getParent() {
		if (compositeEp != null) {
			return (Composite) compositeEp.getWidget();
		}
		return null;
	}

	public FormLayoutData computeData(Rectangle bounds, Control control) {
		if (bounds.isEmpty()) {
			return null;
		}
		Composite parent = getParent();
		if (parent == null) {
			return null;
		}
		Rectangle clientArea = getClientArea();
		if (!clientArea.intersects(bounds)) {
			return null;
		}
		FormData formData = new FormData();
		if (!clientArea.isEmpty()) {
			computeVerticalAttachment(formData, clientArea, bounds, control);
			computeHorizontalAttachment(formData, clientArea, bounds, control);
		}
		return new FormLayoutData(formData, bounds);
	}

	private void computeHorizontalAttachment(FormData formData,
			Rectangle clientArea, Rectangle bounds, Control control) {
		int marginLeft = bounds.x;
		int marginRight = clientArea.width - bounds.right();
		// 1. alignment
		Composite parent = getParent();
		for (Control child : parent.getChildren()) {
			Rectangle r = getBounds(child);
			if (r.equals(bounds) || child == control) {
				continue;
			}
			if (r.y == bounds.y) {
				formData.top = new FormAttachment(child, 0, SWT.TOP);
				formData.bottom = null;
			} else if (r.bottom() == bounds.bottom()) {
				formData.bottom = new FormAttachment(child, 0, SWT.BOTTOM);
				formData.top = null;
			} else {
				continue;
			}
			if (bounds.right() > r.right()) {
				int offset = bounds.right() - r.right() - bounds.width;
				if (offset < marginRight) {
					formData.left = new FormAttachment(child, offset);
				} else {
					formData.right = new FormAttachment(100, -marginRight);
				}
			} else {
				int offset = r.right() + bounds.width - bounds.right();
				if (offset < marginLeft) {
					formData.right = new FormAttachment(child, offset);
				} else {
					formData.left = new FormAttachment(0, marginLeft);
				}
			}
			// return;
		}
		if (formData.left != null || formData.right != null) {
			return;
		}
		Control nearest = getHorizontalNearest(clientArea, bounds, control);
		if (nearest != null) {
			Rectangle r = getBounds(nearest);
			if (bounds.right() > r.right()) {
				int distance = bounds.right() - r.right() - bounds.width;
				if (distance < marginRight) {
					formData.left = new FormAttachment(nearest, distance);
				} else {
					formData.right = new FormAttachment(100, -marginRight);
				}
			} else {
				int distance = r.right() - bounds.right() - r.width;
				if (distance < marginLeft) {
					formData.right = new FormAttachment(nearest, distance);
				} else {
					formData.left = new FormAttachment(0, marginLeft);
				}
			}
		} else {
			if (marginLeft < marginRight) {
				formData.left = new FormAttachment(0, marginLeft);
			} else {
				formData.right = new FormAttachment(100, -marginRight);
			}
		}
	}

	private void computeVerticalAttachment(FormData formData,
			Rectangle clientArea, Rectangle bounds, Control control) {
		int marginTop = bounds.y;
		int marginBottom = clientArea.height - bounds.bottom();
		// 1. alignment
		// TODO: get the nearest alignment....
		Composite parent = getParent();
		for (Control child : parent.getChildren()) {
			Rectangle r = getBounds(child);
			if (r.equals(bounds) || child == control) {
				continue;
			}
			if (r.x == bounds.x) {
				formData.left = new FormAttachment(child, 0, SWT.LEFT);
				formData.right = null;
			} else if (r.right() == bounds.right()) {
				formData.right = new FormAttachment(child, 0, SWT.RIGHT);
				formData.left = null;
			} else {
				continue;
			}
			if (bounds.bottom() > r.bottom()) {
				int topOffset = bounds.bottom() - r.bottom() - bounds.height;
				if (topOffset < marginBottom) {
					formData.top = new FormAttachment(child, topOffset);
				} else {
					formData.bottom = new FormAttachment(100, -marginBottom);
				}
			} else {
				int bottomOffset = bounds.bottom() - r.bottom() + r.height;
				if (bottomOffset < marginTop) {
					formData.bottom = new FormAttachment(child, bottomOffset);
				} else {
					formData.top = new FormAttachment(0, marginTop);
				}
			}
			return;
		}
		if (formData.top != null || formData.bottom != null) {
			return;
		}
		// 2. vertical area.
		Control nearest = getVerticalNearest(clientArea, bounds, control);
		if (nearest != null) {
			Rectangle r = getBounds(nearest);
			if (bounds.bottom() > r.bottom()) {
				int distance = bounds.bottom() - r.bottom() - bounds.height;
				if (distance < marginBottom) {
					formData.top = new FormAttachment(nearest, distance);
				} else {
					formData.bottom = new FormAttachment(100, -marginBottom);
				}
			} else {
				int distance = r.bottom() - bounds.bottom() - r.height;
				if (distance < marginTop) {
					formData.bottom = new FormAttachment(nearest, distance);
				} else {
					formData.top = new FormAttachment(0, marginTop);
				}
			}
		} else {
			if (marginTop < marginBottom) {
				formData.top = new FormAttachment(0, marginTop);
			} else {
				formData.bottom = new FormAttachment(100, -marginBottom);
			}
		}
	}

	public Control getVerticalNearest(Rectangle clientArea, Rectangle bounds,
			Control control) {
		Control nearest = null;
		int distance = 0;
		Composite parent = getParent();
		for (Control child : parent.getChildren()) {
			Rectangle r = getBounds(child);
			if (bounds.y < r.bottom() || r.equals(bounds) || child == control) {
				continue;
			}
			if (bounds.right() < r.x || bounds.x > r.right()) {
				continue;
			}
			int dis = 0;
			if (bounds.bottom() > r.bottom()) {
				dis = bounds.bottom() - r.bottom() - bounds.height;
			} else {
				dis = r.bottom() - bounds.bottom() - r.height;
			}
			if (nearest == null) {
				nearest = child;
				distance = dis;
			} else if (dis < distance) {
				nearest = child;
				distance = dis;
			}
		}
		return nearest;
	}

	public Control getHorizontalNearest(Rectangle clientArea, Rectangle bounds,
			Control control) {
		Control nearest = null;
		int distance = 0;
		Composite parent = getParent();
		for (Control child : parent.getChildren()) {
			Rectangle r = getBounds(child);
			if (bounds.x < r.right() || r.equals(bounds) || child == control) {
				continue;
			}
			if (bounds.bottom() < r.y || bounds.y > r.bottom()) {
				continue;
			}
			int dis = 0;
			if (bounds.right() > r.right()) {
				dis = bounds.right() - r.right() - bounds.width;
			} else {
				dis = r.right() - bounds.right() - r.width;
			}
			if (nearest == null) {
				nearest = child;
				distance = dis;
			} else if (dis < distance) {
				nearest = child;
				distance = dis;
			}
		}
		return nearest;
	}

	public Rectangle getBounds(Control control) {
		Rectangle r = Draw2dTools.toDraw2d(SWTTools.getBounds(control));
		org.eclipse.swt.graphics.Point offset = SWTTools.getOffset(getParent());
		// return FigureUtil.translateToRelative(compositeEp, r);
		return r.translate(-offset.x, -offset.y);
	}

	public static Control[] getDependencies(Control control) {
		if (control == null || control.isDisposed()) {
			return new Control[0];
		}
		List<Control> dependencies = new ArrayList<Control>();
		Composite parent = control.getParent();
		for (Control child : parent.getChildren()) {
			if (child == control || child.getLayoutData() == null) {
				continue;
			}
			Object layoutData = child.getLayoutData();
			if (layoutData == null || !(layoutData instanceof FormData)) {
				continue;
			}
			if (isDepend((FormData) layoutData, control)) {
				dependencies.add(child);
			}
		}
		return dependencies.toArray(new Control[dependencies.size()]);
	}

	private static boolean isDepend(FormData formData, Control control) {
		if (formData == null || control == null) {
			return false;
		}
		if (formData.left != null && control == formData.left.control) {
			return true;
		}
		if (formData.top != null && control == formData.top.control) {
			return true;
		}
		if (formData.right != null && control == formData.right.control) {
			return true;
		}
		if (formData.bottom != null && control == formData.bottom.control) {
			return true;
		}
		return false;
	}
}
