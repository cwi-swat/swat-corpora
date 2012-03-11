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
package org.eclipse.e4.xwt.tools.ui.designer.policies;

import org.eclipse.e4.xwt.tools.ui.designer.commands.AttachedPropertyCreateCommand;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.NonResizableLayoutEditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public class CoolItemEditPolicy extends NonResizableLayoutEditPolicy {

	protected Command getCreateCommand(CreateRequest request) {
		return new AttachedPropertyCreateCommand(getHost(), request, "control");
	}
}
