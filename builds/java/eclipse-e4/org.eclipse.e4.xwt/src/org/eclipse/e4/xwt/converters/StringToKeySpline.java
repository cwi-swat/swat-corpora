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
package org.eclipse.e4.xwt.converters;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.animation.KeySpline;

/**
 * String to Font converter
 * 
 * @author yyang
 */
public class StringToKeySpline implements IConverter {
	public static StringToKeySpline instance = new StringToKeySpline();

	public Object convert(Object fromObject) {
		return KeySpline.fromString((String) fromObject);
	}

	public Object getFromType() {
		return String.class;
	}

	public Object getToType() {
		return KeySpline.class;
	}
}
