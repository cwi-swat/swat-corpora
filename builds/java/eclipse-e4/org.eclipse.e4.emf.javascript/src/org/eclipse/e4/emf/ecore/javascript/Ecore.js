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
function $(object, name) {
	var predicate = function(object) { return name == object.name;};
	var depth = -1, last = null;
    while (object != null) {
    	var value = object.__().findOne(predicate, depth, last);
    	if (value != undefined) {
    		return value;
    	}
    	last = object;
    	object = object._();
    }
    return undefined;
}

function contains(container, object) {
	return container.__().contains(eObject);
}

function findOne(container, test) {
	return container.__().findOne(test, -1);
}

function findInstances(container, eClassifier) {
	return container.__().findInstances(eClassifier);
}

function findInstance(container, eClassifier) {
	return container.__().findInstance(eClassifier);
}

function mapList(container, fun, result) {
	return container.__().mapList(fun, result);
}

function map(container, fun) {
	return container.__().map(fun);
}

function reduce(container, fun, result) {
	return container.__().reduce(fun, result);
}

function filterList(container, fun, logic, result) {
	return container.__().filterList(fun, logic, result);
}

function collect(container, fun) {
	return container.__().collect(fun);
}

function reject(container, fun) {
	return container.__().reject(fun);
}

function count(container, fun) {
	return container.__().count(fun);
}

function single(container, fun) {
	return container.__().one(fun);
}

function exists(container, fun) {
	return container.__().exists(fun);
}

function every(container, fun) {
	return container.__().every(fun);
}

function none(container, fun) {
	return container.__().none(fun);
}
