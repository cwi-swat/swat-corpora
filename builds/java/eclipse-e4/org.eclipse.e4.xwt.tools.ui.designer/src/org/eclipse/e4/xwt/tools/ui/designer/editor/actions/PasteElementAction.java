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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import java.lang.reflect.Constructor;
import java.util.List;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.WorkbenchMessages;

public class PasteElementAction extends SelectionAction {
	private XWTDesigner part;
	private Object contents;
	private EditPart parent;

	public PasteElementAction(IWorkbenchPart part) {
		super(part);
		this.part = (XWTDesigner) part;
		setText(WorkbenchMessages.Workbench_paste);
		setToolTipText(WorkbenchMessages.Workbench_pasteToolTip);
		setId(ActionFactory.PASTE.getId());
		setAccelerator(SWT.MOD1 | 'v');
		ISharedImages sharedImages = part.getSite().getWorkbenchWindow().getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		contents = Clipboard.getDefault().getContents();
		if (contents == null) {
			return false;
		}

		if (part.getGraphicalViewer() == null) {
			return false;
		}

		List<?> parts = part.getGraphicalViewer().getSelectedEditParts();
		if (parts == null || parts.isEmpty()) {
			return false;
		}
		parent = (EditPart) parts.get(0);
		Object model = parent.getModel();
		if (model == null || !(model instanceof XamlElement)) {
			return false;
		}
		return canPaste((XamlElement) model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		XamlElement parentNode = (XamlElement) parent.getModel();
		List<XamlElement> elements = (List<XamlElement>) contents;
		CompoundCommand cmd = new CompoundCommand("Paste");
		for (XamlElement child : elements) {
			XamlElement newChild = (XamlElement) EcoreUtil.copy(child);
			cmd.add(new AddNewChildCommand(parentNode, newChild));
		}
		Command command = cmd.unwrap();
		if (command.canExecute()) {
			EditDomain.getEditDomain(parent).getCommandStack().execute(command);
		}
	}

	public boolean canPaste(XamlElement parent) {
		Class<?> parentType = null;
		parentType = getType(parent);
		if (parentType == null) {
			return false;
		}
		List<XamlElement> contents = (List<XamlElement>) Clipboard.getDefault().getContents();

		for (XamlElement element : contents) {
			Class<?> createtype = getType(element);
			if (createtype == null) {
				return false;
			}
			if (!canPaste(createtype, parentType, parent))
				return false;
		}
		return true;
	}

	public Class<?> getType(XamlNode element) {
		if (element == null) {
			return null;
		}
		IMetaclass metaclass = XWTUtility.getMetaclass(element);
		if (metaclass != null) {
			return metaclass.getType();
		}
		return null;
	}

	public boolean canPaste(Object createType, Class<?> parentType, XamlElement parent) {
		boolean canCreate = false;
		if (createType instanceof Class<?>) {
			Class<?> type = (Class<?>) createType;
			// the child of coolBar/expandBar only is coolItem/expandItem.
			if (CoolBar.class.isAssignableFrom(parentType)) {
				if (!CoolItem.class.isAssignableFrom(type)) {
					return false;
				}
			}
			if (ExpandBar.class.isAssignableFrom(parentType)) {
				if (!ExpandItem.class.isAssignableFrom(type)) {
					return false;
				}
			}// the coolItem/expandItem is permitted to add a control except its parent and itself.
			if (CoolItem.class.isAssignableFrom(parentType)) {
				if (Control.class.isAssignableFrom(type) && !CoolBar.class.isAssignableFrom(type)) {
					return true;
				}
			}
			if (ExpandItem.class.isAssignableFrom(parentType)) {
				if (Control.class.isAssignableFrom(type) && !ExpandBar.class.isAssignableFrom(type)) {
					return true;
				}
			}
			Constructor<?>[] constructors = ((Class<?>) createType).getConstructors();
			for (Constructor<?> constructor : constructors) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length == 0) {
					continue;
				}
				if (parameterTypes[0].isAssignableFrom(parentType)) {
					canCreate = true;
					break;
				}
			}
		} else if (createType instanceof String) {
			String stringType = (String) createType;
			IMetaclass metaclass = XWT.getMetaclass(parentType);
			if (metaclass != null) {
				canCreate = metaclass.findProperty((String) createType) != null;
				if ("menuBar".equals(stringType) || "menu".equals(stringType) || "layoutData".equals(stringType)) {
					canCreate = canCreate && parent.getAttribute(stringType) == null;
				}
			}
		}
		return canCreate;
	}
}
