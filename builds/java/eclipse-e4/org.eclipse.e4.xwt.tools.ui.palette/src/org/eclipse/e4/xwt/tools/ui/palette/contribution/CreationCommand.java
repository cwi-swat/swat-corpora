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
package org.eclipse.e4.xwt.tools.ui.palette.contribution;

import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Initializer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public abstract class CreationCommand extends Command {

	private CreateRequest createReq;
	private Entry entry;

	public CreationCommand(CreateRequest createRequest) {
		this.createReq = createRequest;
	}

	public boolean canExecute() {
		if (createReq == null) {
			return false;
		}
		return (entry = (Entry) createReq.getNewObject()) != null;
	}

	final public void execute() {
		Initializer initializer = entry.getInitializer();
		if (initializer != null) {
			Object newObject = initializer.parse(entry);
			if (initializer.initialize(entry, newObject)) {
				doCreate(entry, newObject);
			}
		} else {
			doCreate(entry, null);
		}
	}

	/**
	 * Doing command here.
	 */
	protected abstract void doCreate(Entry entry, Object newObject);

	public Entry getEntry() {
		return entry;
	}

	/**
	 * Doing something before command execute.
	 */
	protected boolean preExecute() {
		if (entry == null) {
			return false;
		}
		Initializer initializer = entry.getInitializer();
		if (initializer != null) {
			Object parse = initializer.parse(entry);
			return initializer.initialize(parse);
		}
		return false;
	}
}
