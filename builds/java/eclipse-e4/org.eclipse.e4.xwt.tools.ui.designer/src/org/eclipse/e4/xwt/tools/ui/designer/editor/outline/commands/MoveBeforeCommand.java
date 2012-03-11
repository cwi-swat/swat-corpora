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
import org.eclipse.e4.xwt.tools.ui.designer.editor.palette.EntryHelper;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class MoveBeforeCommand extends MoveCommand {

	public MoveBeforeCommand(IStructuredSelection source, Object target, int operation) {
		super(source, target, operation);
	}

	@Override
	public boolean canExecute() {
		return super.canExecute() && getTarget().getParent() != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.outline.commands.MoveCommand#collectCommands(org.eclipse.gef.commands.CompoundCommand)
	 */
	protected void collectCommands(CompoundCommand command) {
		IStructuredSelection sourceNodes = getSource();
		XamlNode targetNode = (XamlNode) getTarget();
		XamlNode parent = targetNode.getParent();
		int index = parent.getChildNodes().indexOf(targetNode);

		XamlNode newNode = null;
		for (Iterator iterator = sourceNodes.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			XamlNode sourceNode = null;
			if (element instanceof Entry) {
				sourceNode = EntryHelper.getNode((Entry) element);
			}
			else {
				sourceNode = (XamlNode) element;
			}
			
			if (sourceNode.eContainer() != null) {
				newNode = (XamlNode) EcoreUtil.copy(sourceNode);
			} else {
				newNode = sourceNode;
			}
	
			command.add(new AddNewChildCommand(parent, newNode, index++));
	
			if (isMove() && sourceNode.eContainer() != null) {
				command.add(new DeleteCommand(sourceNode));
			}
		}
	}

}
