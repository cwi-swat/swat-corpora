/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.properties;

import org.eclipse.core.runtime.QualifiedName;

public class StoredProperty {
	protected QualifiedName name = null;
	protected String value = null;

	public StoredProperty(QualifiedName name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public QualifiedName getName() {
		return name;
	}

	public String getStringValue() {
		return value;
	}
}
