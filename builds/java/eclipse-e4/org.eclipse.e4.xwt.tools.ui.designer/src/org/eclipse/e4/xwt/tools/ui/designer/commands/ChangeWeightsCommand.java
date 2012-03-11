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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.e4.xwt.tools.ui.designer.core.visuals.IVisualInfo;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashFormEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;

/**
 * @author jin.liu (jin.liu@soyatec.com)
 * 
 */
public class ChangeWeightsCommand extends Command {
	private SashFormEditPart parent;
	private ChangeBoundsRequest request;
	private Command command;
	private Integer[] weights;

	public ChangeWeightsCommand(SashFormEditPart parent,
			ChangeBoundsRequest request) {
		super("Change Weights");
		this.parent = parent;
		this.request = request;
	}

	public boolean canExecute() {
		if (parent == null || request == null || request.getEditParts() == null) {
			return false;
		}
		IVisualInfo visualInfo = ((VisualEditPart) parent).getVisualInfo();
		SashForm sashForm = (SashForm) visualInfo.getVisualObject();
		weights = computeWeights(sashForm);
		if (weights == null) {
			return false;
		}
		for (Integer integer : weights) {
			if (integer == null || integer.intValue() < 0) {
				return false;
			}
		}
		return true;
	}

	public void execute() {
		command = new ApplyAttributeSettingCommand(
				(XamlNode) parent.getModel(), "weights",
				IConstants.XWT_NAMESPACE, StringUtil.format(weights));
		command.execute();
	}

	public boolean canUndo() {
		return command != null && command.canUndo();
	}

	public void undo() {
		command.undo();
	}

	private VisualEditPart getEditPart() {
		List<VisualEditPart> editParts = new ArrayList<VisualEditPart>(request
				.getEditParts());
		if (editParts.size() > 0) {
			return editParts.get(0);
		}
		return null;
	}

	private Integer[] computeWeights(SashForm sashForm) {

		int[] weights = sashForm.getWeights();
		int[] newWeights = weights;

		VisualEditPart editPart = getEditPart();
		if (editPart != null) {
			XamlNode parentNode = (XamlNode) parent.getModel();
			int index = parentNode.getChildNodes().indexOf(editPart.getModel());
			Object visualObject = editPart.getVisualInfo().getVisualObject();
			if (index != -1) {
				int resizeDirection = request.getResizeDirection();
				Dimension sizeDelta = request.getSizeDelta();
				int offset = getResizeOffset((Control) visualObject, null,
						newWeights, index, sizeDelta.width, sizeDelta.height,
						resizeDirection == PositionConstants.EAST
								|| resizeDirection == PositionConstants.WEST);
				for (int i = 0; i < newWeights.length; i++) {
					if (i == index) {
						newWeights[i] += offset;
					} else {
						newWeights[i] -= offset / (newWeights.length - 1);
					}
				}
			}
			// SashEditPart.
			else {
				Sash sash = (Sash) visualObject;
				int sashIndex = getSashIndex(sashForm, sash);
				org.eclipse.draw2d.geometry.Point moveDelta = request
						.getMoveDelta();
				boolean horizontal = (sash.getStyle() & SWT.VERTICAL) != 0;
				Control[] controls = getControls(sashForm);
				if (controls != null) {
					int resizeOffset = getResizeOffset(controls[sashIndex], sash, 
							newWeights, sashIndex, moveDelta.x, moveDelta.y,
							horizontal);
					newWeights[sashIndex] += resizeOffset;
					newWeights[sashIndex + 1] -= resizeOffset;
				}
			}
		}
		List<Integer> ws = new ArrayList<Integer>();
		for (int i = 0; i < newWeights.length; i++) {
			ws.add(newWeights[i]);
		}
		return ws.toArray(new Integer[ws.size()]);
	}

	private int getSashIndex(SashForm sashForm, Sash sash) {
		try {
			Field field = SashForm.class.getDeclaredField("sashes");
			field.setAccessible(true);
			Sash[] sashes = (Sash[]) field.get(sashForm);
			for (int i = 0; i < sashes.length; i++) {
				if (sashes[i].equals(sash)) {
					return i;
				}
			}
		} catch (Exception e) {
			XWTDesignerPlugin.logError(e);
		}
		return -1;
	}

	private Control[] getControls(SashForm sashForm) {
		try {
			Field field = SashForm.class.getDeclaredField("controls");
			field.setAccessible(true);
			return (Control[]) field.get(sashForm);
		} catch (Exception e) {
		}
		return null;
	}

	private int getResizeOffset(Control control, Sash sash, int[] weights, int index,
			int x, int y, boolean horizontal) {
		float total = 0;
		for (int i : weights) {
			total += i;
		}

		Rectangle bounds = SWTTools.getBounds(control);
				
		if (horizontal) {
			int w = bounds.width;
			if (sash != null) {
				// in case the child doesn't fill the cell
				Rectangle sashBounds = SWTTools.getBounds(sash);
				w = Math.max(w, sashBounds.x - bounds.x);
			}
			float percent = weights[index] / total;
			float width = (w / percent);
			float newPercent = (w + x) / width;
			int newWeight = (int) (total * newPercent);
			return newWeight - weights[index];
		} else {
			int h = bounds.height;
			if (sash != null) {
				// in case the child doesn't fill the cell
				Rectangle sashBounds = SWTTools.getBounds(sash);
				h = Math.max(h, sashBounds.y - bounds.y);
			}
			
			float percent = weights[index] / total;
			float height = (h / percent);
			float newPercent = (h + y) / height;
			int newWeight = (int) (total * newPercent);
			return newWeight - weights[index];
		}
	}
}
