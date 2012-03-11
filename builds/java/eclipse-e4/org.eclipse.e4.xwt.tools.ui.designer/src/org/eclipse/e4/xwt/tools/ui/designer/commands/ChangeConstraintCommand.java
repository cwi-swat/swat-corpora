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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.swt.SWTTools;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ControlEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.utils.OffsetUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ChangeConstraintCommand extends Command {

	private EditPart editPart;
	private XamlNode model;
	private Rectangle constraint;

	private boolean transform;
	private CompoundCommand changeBoundCommand;

	public ChangeConstraintCommand(EditPart editPart, Rectangle constraint) {
		this(editPart, constraint, false);
	}

	public ChangeConstraintCommand(EditPart editPart, Rectangle constraint, boolean transform) {
		this.editPart = editPart;
		this.constraint = constraint;
		this.transform = transform;
	}

	ChangeConstraintCommand(XamlNode model, Rectangle constraint) {
		this((EditPart) null, constraint);
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (editPart != null) {
			model = (XamlNode) editPart.getModel();
		}
		return model != null;
	}

	private int getXOffset() {
		if (editPart == null) {
			return 0;
		}
		/*
		 * Fixed bug: http://www.soyatec.cn/bugs/view.php?id=813, When the root is not a Shell, the parent host maybe the Diagram directly, we should found the correctly Scrollable Parent to compute the offset value.
		 */
		if (editPart instanceof ControlEditPart) {
			Control control = (Control) ((ControlEditPart) editPart).getWidget();
			if (control != null) {
				Composite parent = control.getParent();
				if (parent instanceof Group) {
					return 0;
				} else {
					return SWTTools.getOffset(parent).x;
				}
			}
		}
		return OffsetUtil.getXOffset(editPart.getParent());
	}

	private int getYOffset() {
		if (editPart == null) {
			return 0;
		}
		/*
		 * Fixed bug: http://www.soyatec.cn/bugs/view.php?id=813, When the root is not a Shell, the parent host maybe the Diagram directly, we should found the correctly Scrollable Parent to compute the offset value.
		 */
		if (editPart instanceof ControlEditPart) {
			Control control = (Control) ((ControlEditPart) editPart).getWidget();
			if (control != null) {
				Composite parent = control.getParent();
				if (parent instanceof Group) {
					return 0;
				} else {
					return SWTTools.getOffset(parent).y;
				}
			}
		}
		return OffsetUtil.getYOffset(editPart.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		IMetaclass metaclass = XWTUtility.getMetaclass(model);
		if (metaclass == null) {
			return;
		}
		changeBoundCommand = new CompoundCommand("Change Bounds");

		XamlAttribute bounds = XWTModelUtil.getChildAttribute(model, "bounds", IConstants.XWT_NAMESPACE);
		XamlAttribute location = XWTModelUtil.getChildAttribute(model, "location", IConstants.XWT_NAMESPACE);
		XamlAttribute size = XWTModelUtil.getChildAttribute(model, "size", IConstants.XWT_NAMESPACE);

		XamlNode boundsParent = null, locationParent = null, sizeParent = null;
		Class<?> type = metaclass.getType();
		if (Viewer.class.isAssignableFrom(type)) {
			XamlNode controlNode = null;
			if (bounds != null) {
				controlNode = boundsParent = (XamlNode) bounds.eContainer();
			}
			if (size != null) {
				controlNode = sizeParent = (XamlNode) size.eContainer();
			}
			if (location != null) {
				controlNode = locationParent = (XamlNode) location.eContainer();
			}
			if (controlNode == null) {
				controlNode = XamlFactory.eINSTANCE.createAttribute("control", IConstants.XWT_NAMESPACE);
				changeBoundCommand.add(new AddNewChildCommand(model, controlNode));
			}
			if (boundsParent == null) {
				boundsParent = controlNode;
			}
			if (sizeParent == null) {
				sizeParent = controlNode;
			}
			if (locationParent == null) {
				locationParent = controlNode;
			}
		} else {
			boundsParent = locationParent = sizeParent = model;
		}

		Rectangle r = (Rectangle) constraint;
		if (transform) {
			r.x = r.x - getXOffset();
			r.y = r.y - getYOffset();
		}

		String boundsValue = StringUtil.format(r);
		String locationValue = StringUtil.format(r.getLocation());
		String sizeValue = StringUtil.format(r.getSize());

		Command createBounds = ApplyAttributeSettingCommand.createCommand(boundsParent, "bounds", IConstants.XWT_NAMESPACE, boundsValue);
		Command createLocation = ApplyAttributeSettingCommand.createCommand(locationParent, "location", IConstants.XWT_NAMESPACE, locationValue);
		Command createSize = ApplyAttributeSettingCommand.createCommand(sizeParent, "size", IConstants.XWT_NAMESPACE, sizeValue);
		if (bounds == null) {
			if (location == null && size == null) {
				changeBoundCommand.add(createBounds);
			} else {
				changeBoundCommand.add(createSize);
				changeBoundCommand.add(createLocation);
			}
		} else {
			changeBoundCommand.add(createBounds);
			if (location != null) {
				changeBoundCommand.add(createLocation);
			}
			if (size != null) {
				changeBoundCommand.add(createSize);
			}
		}
		if (changeBoundCommand.canExecute()) {
			changeBoundCommand.execute();
		}
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		changeBoundCommand.undo();
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return changeBoundCommand != null && changeBoundCommand.canUndo();
	}
}
