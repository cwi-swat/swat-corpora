/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.renderers.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ToolBar;

public class TrimBarLayout extends Layout {
	class TrimLine {
		Map<Control, Point> sizeMap = new HashMap<Control, Point>();
		List<Control> ctrls = new ArrayList<Control>();
		int spacerCount = 0;
		int extraSpace = 0;
		int major = 0;
		int minor = 0;

		public void addControl(Control ctrl) {
			Point ctrlSize = computeSize(ctrl);
			int ctrlMajor = horizontal ? ctrlSize.x : ctrlSize.y;
			int ctrlMinor = horizontal ? ctrlSize.y : ctrlSize.x;

			major += ctrlMajor;
			if (ctrlMinor > minor)
				minor = ctrlMinor;

			sizeMap.put(ctrl, ctrlSize);
			ctrls.add(ctrl);

			if (isSpacer(ctrl))
				spacerCount++;
		}

		public void mergeSegment(TrimLine segment) {
			sizeMap.putAll(segment.sizeMap);
			ctrls.addAll(segment.ctrls);

			major += segment.major;
			if (segment.minor > minor)
				minor = segment.minor;

			spacerCount += segment.spacerCount;
		}
	}

	private List<TrimLine> lines = new ArrayList<TrimLine>();

	public static String SPACER = "stretch"; //$NON-NLS-1$
	public static String GLUE = "glue"; //$NON-NLS-1$

	private boolean horizontal;

	public int marginLeft = 0;
	public int marginRight = 0;
	public int marginTop = 0;
	public int marginBottom = 0;
	public int wrapSpacing = 0;

	public TrimBarLayout(boolean horizontal) {
		this.horizontal = horizontal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Layout#computeSize(org.eclipse.swt.widgets.Composite
	 * , int, int, boolean)
	 */
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		// Clear the current cache
		lines.clear();

		int totalMajor = horizontal ? wHint - (marginLeft + marginRight)
				: hHint - (marginTop + marginBottom);
		int totalMinor = 0;
		int spaceLeft = totalMajor;

		TrimLine curLine = new TrimLine();
		Control[] kids = composite.getChildren();
		for (int i = 0; i < kids.length; i++) {
			Control ctrl = kids[i];

			// GLUE Handling; gather any glued controls up into a 'segment'
			TrimLine segment = new TrimLine();
			segment.addControl(ctrl);
			while (i < (kids.length - 2) && isGlue(kids[i + 1])) {
				segment.addControl(kids[i + 1]);
				segment.addControl(kids[i + 2]);
				i += 2;
			}

			// Do we have enough space ?
			if (segment.major <= spaceLeft) {
				// Yes, add the segment to the current line
				curLine.mergeSegment(segment);
				spaceLeft -= segment.major;
			} else {
				// No, cache the current line and start a new one
				curLine.extraSpace = spaceLeft;
				lines.add(curLine);
				totalMinor += curLine.minor;

				curLine = segment;
				spaceLeft = totalMajor - segment.major;
			}
		}

		if (curLine.ctrls.size() > 0) {
			curLine.extraSpace = spaceLeft;
			lines.add(curLine);
			totalMinor += curLine.minor;
		}

		// Adjust the 'totalMinor' to account for the margins
		int totalWrapSpacing = (lines.size() - 1) * wrapSpacing;
		totalMinor += horizontal ? (marginTop + marginBottom)
				+ totalWrapSpacing : (marginLeft + marginRight)
				+ totalWrapSpacing;
		return horizontal ? new Point(wHint, totalMinor) : new Point(
				totalMinor, hHint);
	}

	private Point computeSize(Control ctrl) {
		Point ctrlSize = ctrl.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		// Hack! the StatusLine doesn't compute a useable size
		if (isStatusLine(ctrl))
			ctrlSize.x = 375;

		if (ctrl instanceof ToolBar) {
			ToolBar tb = (ToolBar) ctrl;
			if (tb.getItemCount() == 0)
				return new Point(0, 0);
		} else if (ctrl instanceof Composite && hideManagedTB((Composite) ctrl)) {
			return new Point(0, 0);
		}

		return ctrlSize;
	}

	/**
	 * This is a HACK ! Due to compatibility restrictions we have the case where
	 * we <b>must</b> leave 'empty' toolbars in the trim. This code detects this
	 * particular scenario and hides any TB's of this type...
	 * 
	 * @param tbComp
	 *            The proposed composite in the trim
	 * @return <code>true</code> iff this composite represents an empty managed
	 *         TB.
	 */
	private boolean hideManagedTB(Composite tbComp) {
		if (!(tbComp.getData(AbstractPartRenderer.OWNING_ME) instanceof MToolBar))
			return false;
		MToolBar tbModel = (MToolBar) tbComp
				.getData(AbstractPartRenderer.OWNING_ME);
		if (!(tbModel.getRenderer() instanceof ToolBarManagerRenderer))
			return false;

		Control[] kids = tbComp.getChildren();
		if (kids.length != 2 || !(kids[1] instanceof ToolBar))
			return false;

		boolean barVisible = ((ToolBar) kids[1]).getItemCount() > 0;
		tbComp.setVisible(barVisible);
		return !barVisible;
	}

	protected void layout(Composite composite, boolean flushCache) {
		Rectangle bounds = composite.getBounds();

		// offset the rectangle to allow for the margins
		bounds.x = marginLeft;
		bounds.y = marginTop;
		bounds.width -= (marginLeft + marginRight);
		bounds.height -= (marginTop + marginBottom);

		// If we were called directly we need to fill the caches
		if (lines.size() == 0) {
			if (horizontal)
				computeSize(composite, bounds.width, SWT.DEFAULT, true);
			else
				computeSize(composite, SWT.DEFAULT, bounds.height, true);
		}
		if (lines.size() == 0)
			return;

		for (TrimLine curLine : lines) {
			tileLine(curLine, bounds);
			if (horizontal)
				bounds.y += curLine.minor + wrapSpacing;
			else
				bounds.x += curLine.minor = wrapSpacing;
		}
	}

	/**
	 * @param curLine
	 * @param bounds
	 */
	private void tileLine(TrimLine curLine, Rectangle bounds) {
		int curX = bounds.x;
		int curY = bounds.y;
		for (Control ctrl : curLine.ctrls) {
			if (ctrl.isDisposed()) {
				continue;
			}
			Point ctrlSize = curLine.sizeMap.get(ctrl);
			boolean zeroSize = ctrlSize.x == 0 && ctrlSize.y == 0;

			// If its a 'spacer' then add any available 'extra' space to it
			if (isSpacer(ctrl)) {
				int extra = curLine.extraSpace / curLine.spacerCount--;
				if (horizontal)
					ctrlSize.x += extra;
				else
					ctrlSize.y += extra;

				curLine.extraSpace -= extra;
				curLine.spacerCount--;
			}

			if (horizontal) {
				int offset = (curLine.minor - ctrlSize.y) / 2;
				if (!zeroSize)
					ctrl.setBounds(curX, curY + offset, ctrlSize.x, ctrlSize.y);
				else
					ctrl.setBounds(curX, curY, 0, 0);
				curX += ctrlSize.x;
			} else {
				int offset = (curLine.minor - ctrlSize.x) / 2;
				ctrl.setBounds(curX + offset, curY, ctrlSize.x, ctrlSize.y);
				curY += ctrlSize.y;
			}
		}
	}

	private boolean isSpacer(Control ctrl) {
		MUIElement element = (MUIElement) ctrl
				.getData(AbstractPartRenderer.OWNING_ME);
		if (element != null && element.getTags().contains(SPACER))
			return true;

		return false;
	}

	private boolean isGlue(Control ctrl) {
		MUIElement element = (MUIElement) ctrl
				.getData(AbstractPartRenderer.OWNING_ME);
		if (element != null && element.getTags().contains(GLUE))
			return true;

		return false;
	}

	private boolean isStatusLine(Control ctrl) {
		MUIElement element = (MUIElement) ctrl
				.getData(AbstractPartRenderer.OWNING_ME);
		if (element != null && element.getElementId() != null
				&& element.getElementId().equals("org.eclipse.ui.StatusLine")) //$NON-NLS-1$
			return true;

		return false;
	}
}
