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
import org.eclipse.e4.xwt.animation.Duration;
import org.eclipse.e4.xwt.animation.TimeSpan;

/**
 * Safely SWT context values, it used to avoid a NullPointerException when set text values to SWT Widgets.
 * 
 * @author jliu
 */
public class StringToTimeSpan implements IConverter {
	public static StringToTimeSpan instance = new StringToTimeSpan();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	public Object convert(Object fromObject) {
		return TimeSpan.parse(fromObject.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
	 */
	public Object getFromType() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
	 */
	public Object getToType() {
		return TimeSpan.class;
	}

}
