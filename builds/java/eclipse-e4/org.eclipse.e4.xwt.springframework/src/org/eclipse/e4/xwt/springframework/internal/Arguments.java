/*******************************************************************************
 * Copyright (c) 2010 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.springframework.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.springframework.IArguments;

/**
 * {@link IArguments} implementation. 
 * 
 * <p>
 * Example for the declaration x:ClassFactory=
 * "org.eclipse.e4.xwt.springframework.SpringCLRFactory.INSTANCE bean=myUI,bundle=org.eclipse.e4.xwt.springframework.sample.osgi"
 * will be :
 * 
 * <table border="1" >
 * <tr>
 * <td>Key</td>
 * <td>Value</td>
 * </tr>
 * <tr>
 * <td>bean</td>
 * <td>myUI</td>
 * </tr>
 * <tr>
 * <td>bundle</td>
 * <td>org.eclipse.e4.xwt.springframework.sample.osgi</td>
 * </tr>
 * </table>
 * </p>
 * 
 */
public class Arguments extends HashMap<String, String> implements IArguments {

	private static final long serialVersionUID = 1L;

	public static final IArguments EMPTY_PARAMETERS = new Arguments(
			"");
	private String args;

	public Arguments(String arguments) {
		this(arguments, DEFAULT_NAME_SEPARATOR, DEFAULT_VALUE_SEPARATOR);
	}

	public Arguments(String args, String nameSeparator,
			String valueSeparator) {
		super(args.length());
		this.args = args;
		parse(args, nameSeparator, valueSeparator);
	}

	/**
	 * Parse the arguments and store it into this {@link Map}.
	 * 
	 * @param arguments
	 * @param nameSeparator
	 * @param valueSeparator
	 */
	private void parse(String arguments, String nameSeparator,
			String valueSeparator) {
		String key = null;
		String value = null;

		String[] parameters = arguments.split(nameSeparator);
		for (int i = 0; i < parameters.length; i++) {
			String parameterNameValue = parameters[i];
			String[] splittedParameterNameValue = parameterNameValue
					.split(valueSeparator);
			if (splittedParameterNameValue.length > 0) {
				key = splittedParameterNameValue[0];
				if (splittedParameterNameValue.length > 1) {
					value = splittedParameterNameValue[1];
				}
				super.put(key, value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.springframework.IArguments#getSource()
	 */
	public String getSource() {
		return args;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.e4.xwt.springframework.IArguments#getValue(java.lang.String)
	 */
	public String getValue(String name) {
		return super.get(name);
	}
}
