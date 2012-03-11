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
package org.eclipse.e4.xwt.tools.ui.designer.editor.outline.commands;

import java.util.Iterator;

import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.CreateReqHelper;
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.EntryHelper;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class MoveOnCommand extends MoveCommand {

	public MoveOnCommand(IStructuredSelection source, Object target, int operation) {
		super(source, target, operation);
	}
	
	@Override
	public boolean canExecute() {
		boolean canExecute = super.canExecute();
		if (!canExecute) {
			return false;
		}
		
		IStructuredSelection sourceNodes = getSource();
		for (Iterator iterator = sourceNodes.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			XamlNode sourceNode = null;
			if (element instanceof Entry) {
				continue;
			}
			else if (!(element instanceof XamlNode)) {
				return false;
			}
			else {
				sourceNode = (XamlNode) element;
				XamlNode sourceParent = sourceNode.getParent();
				if (sourceParent == null) {
					return false;					
				}
				if (sourceParent == getTarget()) {
					return false;
				}
			}
		}
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.outline.commands.MoveAfterCommand#collectCommands(org.eclipse.gef.commands.CompoundCommand)
	 */
	protected void collectCommands(CompoundCommand command) {
		IStructuredSelection sourceNodes = getSource();
		XamlNode target = getTarget();
		for (Iterator iterator = sourceNodes.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			XamlNode sourceNode = null;
			if (element instanceof Entry) {
				sourceNode = EntryHelper.getNode((Entry) element);
			}
			else {
				sourceNode = (XamlNode) element;
			}
			if (CreateReqHelper.canCreate(target, sourceNode)) {
				XamlNode newChild = null;
				if (sourceNode.eContainer() != null) {
					newChild = (XamlNode) EcoreUtil.copy(sourceNode);
				} else {
					newChild = sourceNode;
				}
				command.add(new AddNewChildCommand(target, newChild));
				if (isMove() && sourceNode.eContainer() != null) {
					command.add(new DeleteCommand(sourceNode));
				}
			} 
		}
	}
}
