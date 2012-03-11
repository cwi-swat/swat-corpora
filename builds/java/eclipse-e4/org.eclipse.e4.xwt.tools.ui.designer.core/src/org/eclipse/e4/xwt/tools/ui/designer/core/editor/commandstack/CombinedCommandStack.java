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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.commandstack;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;

/**
 * A combined CommandStack which combine GEF CommandStack and EMF CommandStack together.
 * 
 * @author jliu (jin.liu@soyatec.com)
 */
public class CombinedCommandStack extends BasicCommandStack {

	private boolean stopped;
	private final List<Object> stackListeners;
	private GEFCommandStack gefCommandStack = new GEFCommandStack();

	public CombinedCommandStack() {
		stackListeners = new ArrayList<Object>();
	}

	public CommandStack getCommandStack4GEF() {
		return gefCommandStack;
	}

	public void addCommandStackListener(org.eclipse.emf.common.command.CommandStackListener listener) {
		stackListeners.add(listener);
	}

	public void removeCommandStackListener(org.eclipse.emf.common.command.CommandStackListener listener) {
		stackListeners.remove(listener);
	}

	protected void notifyListeners() {
		for (Object listener : stackListeners) {
			if (listener instanceof CommandStackListener) {
				((CommandStackListener) listener).commandStackChanged(new EventObject(gefCommandStack));
			} else if (listener instanceof org.eclipse.emf.common.command.CommandStackListener) {
				((org.eclipse.emf.common.command.CommandStackListener) listener).commandStackChanged(new EventObject(this));
			}
		}
	}

	/**
	 * Introduced a <code>stopped</code> argument, which is use to prevent adding coming command to redo/undo command list.
	 */
	public void execute(org.eclipse.emf.common.command.Command command) {
		if (stopped) {
			if (command != null && command.canExecute()) {
				command.execute();
			}
		} else {
			super.execute(command);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.command.BasicCommandStack#handleError(java.lang.Exception)
	 */
	protected void handleError(Exception exception) {
		if (exception != null) {
			System.out.println("Execute command error:");
			exception.printStackTrace();
		}
		super.handleError(exception);
	}

	/**
	 * Try to stop this command stack, it means all coming commands will be executed directly from now.
	 */
	public void stop() {
		stopped = true;
	}

	/**
	 * Try to start this command stack, all coming commands will be executed by the command stack.
	 */
	public void start() {
		stopped = false;
	}

	private class GEFCommandStack extends CommandStack {
		public void addCommandStackListener(CommandStackListener listener) {
			stackListeners.add(listener);
		}

		public void removeCommandStackListener(CommandStackListener listener) {
			stackListeners.remove(listener);
		}

		public boolean canRedo() {
			return CombinedCommandStack.this.canRedo();
		}

		public boolean canUndo() {
			return CombinedCommandStack.this.canUndo();
		}

		public void execute(Command command) {
			if (command != null) {
				org.eclipse.emf.common.command.Command emfCommand = null;
				if (command instanceof CommandWrap4EMF) {
					emfCommand = ((CommandWrap4EMF) command).unwrap();
				} else {
					emfCommand = new CommandWrap4GEF(command);
				}
				CombinedCommandStack.this.execute(emfCommand);
			}
		}

		public void flush() {
			CombinedCommandStack.this.flush();
		}

		public Command getRedoCommand() {
			org.eclipse.emf.common.command.Command command = CombinedCommandStack.this.getRedoCommand();
			Command gefCommand = null;
			if (command != null) {
				if (command instanceof CommandWrap4GEF) {
					gefCommand = ((CommandWrap4GEF) command).unwrap();
				} else {
					gefCommand = new CommandWrap4EMF(command);
				}
			}
			return gefCommand;
		}

		public Command getUndoCommand() {
			org.eclipse.emf.common.command.Command command = CombinedCommandStack.this.getUndoCommand();
			Command gefCommand = null;
			if (command != null) {
				if (command instanceof CommandWrap4GEF) {
					gefCommand = ((CommandWrap4GEF) command).unwrap();
				} else {
					gefCommand = new CommandWrap4EMF(command);
				}
			}
			return gefCommand;
		}

		public void redo() {
			CombinedCommandStack.this.redo();
		}

		public void undo() {
			CombinedCommandStack.this.undo();
		}
	}

}
