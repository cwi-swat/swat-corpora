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
 
function __() {
    return this.eContents();
}
function _() {
	return this.eContainer();
}

function initialize(initArgs) {
	for (prop in initArgs) {
		this[prop] = initArgs[prop];
	}
	return this;
}

function adaptTo(notifier) {
	return adaptTo(notifier, this);
}

function isA(eClass) {
    return eClass.isInstance(this);
}

//function copy() {
//	return Packages.org.eclipse.emf.ecore.util.EcoreUtil.copy(this);
//}

function findContainer(predicate) {
	var container = this._();
	predicate = this.__().asPredicate(predicate);
	while (container != null && (! predicate(container))) {
		container = container._();
	}
	return container;
}
