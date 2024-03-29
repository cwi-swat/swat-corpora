/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi.request;

import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;

/**
 * {@link VirtualMachine} request indicating it should be shutdown / disposed
 * 
 * @since 1.0
 */
public interface VMDeathRequest extends EventRequest {

}
