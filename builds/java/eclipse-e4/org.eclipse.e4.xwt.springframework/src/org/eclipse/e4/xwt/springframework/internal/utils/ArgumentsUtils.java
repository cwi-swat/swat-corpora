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
package org.eclipse.e4.xwt.springframework.internal.utils;

import static java.text.MessageFormat.format;

import java.net.URL;
import java.util.Map;

import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.springframework.IArguments;
import org.eclipse.e4.xwt.springframework.IBeanNameProvider;
import org.eclipse.e4.xwt.springframework.internal.Arguments;
import org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.Result.Source;

/**
 * Utilities used to parse the arguments of the value x:ClassFactory.
 * 
 */
public class ArgumentsUtils {

	public static class Result {

		public enum Source {
			Default, Arguments
		}

		public final String value;
		public final Source source;

		public Result(String value, Source source) {
			this.value = value;
			this.source = source;
		}
	}

	/**
	 * Parse the properties arguments and return a {@link Arguments} which
	 * contains the parsed parameters. The parameter (name=value) are separated
	 * with ' ' character. The '=' character is used to assign value of a
	 * parameter.
	 * 
	 * <p>
	 * Example : "bean=myCLR bundle=org.eclipse.e4.xwt.spring.tests.osgi" will
	 * return a {@link Arguments} with parameters :
	 * 
	 * <table border="1" >
	 * <tr>
	 * <td>Key</td>
	 * <td>Value</td>
	 * </tr>
	 * <tr>
	 * <td>bean</td>
	 * <td>myCLR</td>
	 * </tr>
	 * <tr>
	 * <td>bundle</td>
	 * <td>org.eclipse.e4.xwt.spring.tests.osgi</td>
	 * </tr>
	 * </table>
	 * 
	 * </p>
	 * 
	 * @param args
	 *            of the x:ClassFactory
	 * @return {@link Map} parameters of the parsed arguments.
	 */
	public static IArguments createArguments(String args) {
		return createArguments(args, Arguments.DEFAULT_NAME_SEPARATOR,
				Arguments.DEFAULT_VALUE_SEPARATOR);
	}

	/**
	 * Parse the properties arguments and return a {@link Arguments} which
	 * contains the parsed parameters. The parameter (name=value) are separated
	 * with nameSeparator character. The valueSeparator character is used to
	 * assign value of a parameter.
	 * 
	 * @param arguments
	 *            of the x:ClassFactory
	 * @param nameSeparator
	 *            separator for name parameter.
	 * @param valueSeparator
	 *            separator for value parameter.
	 * @return {@link Arguments} parameters of the parsed arguments.
	 */

	public static IArguments createArguments(String arguments,
			String nameSeparator, String valueSeparator) {
		if (arguments == null || arguments.length() < 1) {
			return Arguments.EMPTY_PARAMETERS;
		}
		return new Arguments(arguments, nameSeparator, valueSeparator);
	}

	/**
	 * Returns the value of the parameter name of the parameters or null
	 * otherwise. If required is true, this method check that value is required
	 * otherwise it throws a {@link XWTException}
	 * 
	 * @param name
	 *            parameter name.
	 * @param arguments
	 *            {@link Map}
	 * @param errorMessage
	 *            error message to display if value parameter is null.
	 * @return the parameter value.
	 * 
	 * @throws XWTException
	 */
	public static Result getValue(String name, IArguments arguments,
			String defaultValue, String errorMessage) throws XWTException {
		String value = arguments.getValue(name);
		if (StringUtils.isEmpty(value)) {
			if (!StringUtils.isEmpty(defaultValue)) {
				return new Result(defaultValue, Source.Default);
			}
			throw new XWTException(format(errorMessage, name,
					arguments.getSource()));
		}
		return new Result(value, Source.Arguments);

	}

	/**
	 * Returns the value of the parameter name of the parameters or null
	 * otherwise. If required is true, this method check that value is required
	 * otherwise it throws a {@link XWTException}
	 * 
	 * @param name
	 *            parameter name.
	 * @param arguments
	 *            {@link Map}
	 * @param errorMessage
	 *            error message to display if value parameter is null.
	 * @return the parameter value.
	 * 
	 * @throws XWTException
	 */
	public static Result getValue(String name, IArguments arguments,
			IBeanNameProvider beanNameProvider, URL url, String errorMessage)
			throws XWTException {
		String value = arguments.getValue(name);
		if (StringUtils.isEmpty(value)) {
			if (beanNameProvider != null && url != null) {
				String beanName = beanNameProvider.getBeanName(url);
				if (!StringUtils.isEmpty(beanName)) {
					return new Result(beanName, Source.Arguments);
				}
			}
			throw new XWTException(format(errorMessage, name,
					arguments.getSource()));
		}
		return new Result(value, Source.Arguments);
	}
}
