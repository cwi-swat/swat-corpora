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

import org.eclipse.e4.xwt.tools.ui.designer.commands.SurroundWithCommand;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SurroundWithAction extends AbstractDropDownAction {

	public static final String ID = "org.eclipse.e4.xwt.tools.ui.designer.editor.actions.SurroundWithAction";

	private Class<?>[] actionList;

	private SurroundWithCommand command;

	public SurroundWithAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText("Surround With");
		initActions();
		setMenuCreator(this);
	}

	private void initActions() {
		actionList = new Class<?>[] {Composite.class, Group.class, ScrolledComposite.class, SashForm.class, TabFolder.class, CTabFolder.class};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		command = calculateCommand();
		return command != null && command.canExecute();
	}

	protected SurroundWithCommand calculateCommand() {
		return new SurroundWithCommand(getSelectedObjects());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.editor.actions.AbstractDropDownAction#createMenuManager()
	 */
	protected MenuManager createMenuManager() {
		MenuManager mm = new MenuManager();
		for (final Class<?> type : actionList) {
			Action action = new Action(type.getName()) {
				public void run() {
					command.setType(type);
					execute(command);
				}
			};
			Image image = ImageShop.getImageForType(type);
			if (image != null) {
				action.setImageDescriptor(ImageDescriptor.createFromImage(image));
			}
			mm.add(action);
		}
		return mm;
	}

}
