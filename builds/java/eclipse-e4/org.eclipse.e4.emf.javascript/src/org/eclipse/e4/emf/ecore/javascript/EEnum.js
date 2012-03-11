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
function literal(value) {
	var literal = null;
	var type = typeof value;
	if (type == "function") {
		literal = this.getEEnumLiteral(value);
		if (literal == null) {
			literal = this.getEEnumLiteralByLiteral(value);
		}
	} else if (type == "number") {
		literal = this.getEEnumLiteral(value);
	}
	return literal;
}
