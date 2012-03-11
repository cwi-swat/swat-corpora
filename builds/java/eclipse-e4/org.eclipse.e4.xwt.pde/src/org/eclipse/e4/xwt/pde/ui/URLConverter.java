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
package org.eclipse.e4.xwt.pde.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * Data type converter
 * 
 * @author yyang
 */
public class URLConverter implements IConverter {

	public Object convert(Object fromObject) {
		try {
			return new URL((String) fromObject);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getFromType() {
		return String.class;
	}

	public Object getToType() {
		return URL.class;
	}
}
