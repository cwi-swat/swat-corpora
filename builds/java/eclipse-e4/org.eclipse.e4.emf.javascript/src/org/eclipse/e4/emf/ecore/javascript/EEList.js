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
    return this;
}

function filter(value) {
	return (value != false && value != null && value != undefined);
}

function asPredicate(fun) {
	var type = typeof fun;
	if (type == "function") {
		return fun;
	} else if (type == "string") {
		return function(arg) { return arg == fun || filter(arg[fun]);};
	} else if (type == "object") {
		return fun.asPredicate();
	}
	return function(arg) { return arg == fun;};
}

function _findOne(predicate, depth, except) {
	var it = this.iterator();
	while (it.hasNext()) {
		var object = it.next();
		if (object == except);
		else if (this.filter(predicate(object))) {
			return object;
		} else if (depth != 0) {
			var result = object.__()._findOne(predicate, depth - 1);
            if (result != undefined) {
                return result;
            }
		}
	}
	return undefined;
}

function findOne(predicate, depth) {
	if (depth == undefined) {
		depth = 0;
	}
	return this._findOne(this.asPredicate(predicate), depth);
}

function _findMany(predicate, depth, limit, result) {
	var it = this.iterator();
	while (it.hasNext()) {
		var object = it.next();
		if (limit > 0 && limit <= result.size()) {
			break;
		} else if (this.filter(predicate(object))) {
			result.add(object);
		}
		if (depth != 0) {
			object.__()._findMany(predicate, depth - 1, limit, result);
		}
	}
	return result;
}

function newList(content) {
	var size = 4;
	if (typeof content == "number") {
		size = content;
	} else if (content instanceof java.util.Collection) {
		size += content.size();
	}
	var list = new org.eclipse.emf.common.util.BasicEList(size); // java.util.ArrayList(size);
	if (content instanceof java.util.Collection) {
		list.addAll(content);
	}
	return list;
}

function findMany(predicate, depth, limit, result) {
	if (depth == undefined) {
		depth = 0;
	}
	if (limit == undefined) {
		limit = -1;
	}
	if (result == undefined) {
		result = this.newList();
	}
	return this._findMany(this.asPredicate(predicate), depth, limit, result);
}

function findInstances(eClassifier) {
	return this.findMany(eClassifier, -1);
}

function findInstance(eClassifier) {
	return this.findOne(eClassifier, -1);
}

function asFunction(fun) {
	var type = typeof fun;
	if (type == "function") {
		return fun;
	} else if (type == "string" || type == "number") {
		return function (arg) { return arg[fun];};
	}
}

function mapList(fun, result) {
	if (result == undefined) {
		result = this.newList();
	}
	fun = asFunction(fun);
	var it = this.iterator();
	while (it.hasNext()) {
		var value = fun(it.next());
		result.add(value);
	}
	return result;
}

function map(fun) {
	return this.mapList(fun);
}

function mapProperty(prop) {
	var fun = function (item) { return item[prop];};
	return this.mapList(fun, this.newList());
}

function mapMethod(prop, args) {
	var fun = function (item) { return item[prop].apply(item, args);};
	return this.mapList(fun, this.newList());
}

function reduce(fun, result) {
	var it = this.iterator();
	while (it.hasNext()) {
		result = fun(result, it.next());
	}
	return result;
}

function filterList(predicate, logic, result) {
	if (logic == undefined) {
		logic = true;
	}
	if (result == undefined) {
		result = this.newList();
	}
	predicate = this.asPredicate(predicate);
	var it = this.iterator();
	while (it.hasNext()) {
		var value = it.next();
		if (this.filter(predicate(value)) == logic) {
			result.add(value);
		}
	}
	return result;
}

function select(fun) {
	return this.filterList(fun);
}

function reject(fun) {
	return this.filterList(fun, false);
}

function countList(fun, logic, limit) {
	if (logic == undefined) {
		logic = true;
	}
	if (limit == undefined) {
		limit = -1;
	}
	var count = 0;
	var it = this.iterator();
	while (it.hasNext()) {
		var value = it.next();
		var filterValue = fun(value);
		if (this.filter(filterValue) == logic) {
			count++;
		}
		if (limit >= 0 && count >= limit) {
			return count;
		}
	}
	return count;
}

function count(fun) {
	return this.countList(fun);
}

function single(fun) {
	return this.countList(fun, true, 2) == 1;
}

function exists(fun) {
	return this.countList(fun, true, 1) >= 1;
}

function every(fun) {
	return this.count(fun) == this.size();
}

function none(fun) {
	return (! this.countList(fun, true, 1));
}

function listWith(element) {
	var result = this;
	if (! this.contains(element)) {
		result = this.newList(this);
		result.add(element);
	}
	return result;
}

function listWithout(element) {
	var result = this;
	var pos = result.lastIndexOf(element);
	while (pos >= 0) {
		if (result == this) {
			result = this.newList(this);
		}
		result.remove(pos);
		pos = result.lastIndexOf(element);
	}
	return result;
}

//function copy() {
//	return Packages.org.eclipse.emf.ecore.util.EcoreUtil.copy(this);
//}

// generic second-order function for doing "everything"

function generic(valueFun, testFun, reduceFun, result, stopFun, depth) {
	var it = this.iterator();
	while (it.hasNext()) {
		var element = it.next();
		var value = (valueFun != undefined ? valueFun(element) : element);
        if (testFun == undefined || testFun(element, value)) {
        	result = reduceFun(result, element, value);
        	if (stopFun != undefined && stopFun(result)) {
        		return result;
        	}
		}
		if (depth != 0) {
			result = element.__().generic(valueFun, testFun, reduceFun, result, stopFun, depth - 1);
		}
	}
	return result;
}

function findInstancesGen(eClassifier) {
	return this.generic(
		undefined,
		eClassifier.asPredicate(),
		function (list, element) { list.add(element); return list;},
		this.newList(),
		undefined,
		-1
	);
}
function findInstanceGen(eClassifier) {
	return this.generic(
		undefined,
		eClassifier.asPredicate(),
		function (ignore, element) { return element;},
		undefined,
		function (element) { return element != undefined;},
		0
	);
}
function mapListGen(fun, result) {
	if (result == undefined) {
		result = this.newList();
	}
	fun = asFunction(fun);
	return this.generic(
		fun,
		undefined,
		function (list, element, value) { list.add(value); return list;},
		result,
		undefined,
		0
	);
}
function filterListGen(predicate, logic, result) {
	if (logic == undefined) {
		logic = true;
	}
	if (result == undefined) {
		result = this.newList();
	}
	predicate = this.asPredicate(predicate);
	return this.generic(
		undefined,
		function (element) { return predicate(element) == logic;},
		function (list, element) { list.add(element); return list;},
		result,
		undefined,
		0
	);
}
function countListGen(predicate, logic, limit) {
	if (logic == undefined) {
		logic = true;
	}
	if (limit == undefined) {
		limit = -1;
	}
	predicate = this.asPredicate(predicate);
	return this.generic(
		undefined,
		function (element) { return predicate(element) == logic;},
		function (count) { return count + 1;},
		0,
		function (count) { return limit >= 0 && count >= limit;},
		0
	);
}
