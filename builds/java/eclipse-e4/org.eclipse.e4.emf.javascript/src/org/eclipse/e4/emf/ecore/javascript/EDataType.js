/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
function create(string) {
	var factory = this.ePackage.eFactoryInstance;
	var object = factory.createFromString(this, string);
	return object;
}

function toString(object) {
	var factory = this.ePackage.eFactoryInstance;
	var string = factory.convertToString(this, object);
	return string;
}

function isA(object) {
	return this.isInstance(object);
}
