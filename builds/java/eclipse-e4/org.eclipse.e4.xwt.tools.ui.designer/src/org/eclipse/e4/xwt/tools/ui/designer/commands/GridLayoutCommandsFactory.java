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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.InitializeHelper;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class GridLayoutCommandsFactory extends LayoutCommandsFactory {

	public GridLayoutCommandsFactory(CompositeEditPart host) {
		super(host);
	}

	public Command getCreateCommand(List<XamlNode> constraints,
			List<XamlNode> children, Object beforeObject) {
		return new CreationCommand(constraints, children, beforeObject);
	}

	public Command getAddCommand(List<XamlNode> constraints,
			List<XamlNode> children, Object beforeObject) {
		return getCreateCommand(constraints, children, beforeObject);
	}

	public Command getMoveChildrenCommand(List<XamlNode> children,
			XamlNode before) {
		if (children == null) {
			return NoOpCommand.INSTANCE;
		}
		return new MoveChildrenCommand(children, before);
	}

	public Command getDeleteDependentCommand(
			final List<XamlNode> deletedComponents) {
		return new DeleteCommand(deletedComponents);
	}

	public Command getOrphanChildrenCommand(
			final List<XamlNode> orphanedComponents) {
		return new Command() {
			public void execute() {
				System.out.println(orphanedComponents);
			}
		};
	}

	public Command getResizeChildrenCommand(EditPart child,
			ChangeBoundsRequest request) {
		return new ResizeCommand(child, request.getSizeDelta());
	}

	class CreationCommand extends Command {
		private List<XamlNode> children;
		private Object beforeObject;

		private CompoundCommand cmd;

		/**
		 * 
		 */
		public CreationCommand(List<XamlNode> constraints,
				List<XamlNode> children, Object beforeObject) {
			this.children = children;
			this.beforeObject = beforeObject;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#canExecute()
		 */
		public boolean canExecute() {
			XamlNode model = getModel();
			if (model == null || children == null || children.isEmpty()) {
				return false;
			}
			for (XamlNode child : children) {
				if (!CreateReqHelper.canCreate(model, child))
					return false;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#execute()
		 */
		public void execute() {
			int index = -1;
			if (beforeObject != null) {
				index = getModel().getChildNodes().indexOf(beforeObject);
			}
			cmd = new CompoundCommand();
			for (XamlNode child : children) {
				if (!InitializeHelper.checkValue(child)) {
					break;
				}// add the text dialog when add a new child
				cmd.add(new AddNewChildCommand(getModel(), child, index));
			}
			cmd.execute();
		}

		public boolean canUndo() {
			return cmd != null && cmd.canUndo();
		}

		public void undo() {
			if (cmd != null) {
				cmd.undo();
			}
		}
	}

	class MoveChildrenCommand extends Command {
		private List<XamlNode> children;
		private XamlNode insertBeforeValue;
		private int insertAt = -1;
		private Map<XamlElement, Integer> moved = null;

		public MoveChildrenCommand(List<XamlNode> children,
				XamlNode insertBeforeValue) {
			this.children = children;
			this.insertBeforeValue = insertBeforeValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#canExecute()
		 */
		public boolean canExecute() {
			return getModel() != null && children != null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#execute()
		 */
		public void execute() {
			EList<XamlElement> childNodes = getModel().getChildNodes();
			List<XamlNode> orderedChildren = new ArrayList<XamlNode>(childNodes);
			orderedChildren.removeAll(children);
			if (insertBeforeValue != null) {
				insertAt = orderedChildren.indexOf(insertBeforeValue);
			}
			if (insertAt != -1) {
				orderedChildren.addAll(insertAt, children);
			} else {
				orderedChildren.addAll(children);
			}
			moved = new HashMap<XamlElement, Integer>();
			for (XamlElement child : new ArrayList<XamlElement>(childNodes)) {
				int newPosition = orderedChildren.indexOf(child);
				int oldPosition = childNodes.indexOf(child);
				if (newPosition == oldPosition) {
					continue;
				}
				moved.put(childNodes.move(newPosition, oldPosition),
						oldPosition);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#canUndo()
		 */
		public boolean canUndo() {
			return getModel() != null && moved != null && !moved.isEmpty();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.commands.Command#undo()
		 */
		public void undo() {
			EList<XamlElement> childNodes = getModel().getChildNodes();
			for (XamlElement child : moved.keySet()) {
				int newPosition = moved.get(child);
				int oldPosition = childNodes.indexOf(child);
				if (newPosition == oldPosition) {
					continue;
				}
				childNodes.move(newPosition, oldPosition);
			}
			moved.clear();
			moved = null;
		}
	}
}
