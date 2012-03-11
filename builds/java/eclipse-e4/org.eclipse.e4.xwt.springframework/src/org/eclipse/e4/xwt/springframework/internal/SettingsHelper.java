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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.e4.xwt.springframework.internal.utils.StringUtils;

/**
 * Helper for get SpringCLRFactory Settings.
 * 
 */
public class SettingsHelper {

	/**
	 * Properties files (coming from fragment linked to this bundle) which
	 * configure SpringCLRFactory.
	 */
	private static final String SPRINGCLRFACTORY_PROPERTIES = "springclrfactory.properties";

	/**
	 * Load springclrfactory.properties from OSGi fragments linked to this
	 * bundle..
	 * 
	 * @param cl
	 * @return
	 */
	public static Properties load(ClassLoader cl) {

		Properties springclrfactoryProps = new Properties();

		Enumeration<URL> springclrfactoryProperties = null;
		try {
			springclrfactoryProperties = cl == null ? ClassLoader
					.getSystemResources(SPRINGCLRFACTORY_PROPERTIES) : cl
					.getResources(SPRINGCLRFACTORY_PROPERTIES);
		} catch (IOException e) {
			if (DebugHelper.DEBUG) {
				DebugHelper.logError(e);
			}
		}

		while (springclrfactoryProperties.hasMoreElements()) {
			URL url = springclrfactoryProperties.nextElement();
			if (url != null) {
				try {
					springclrfactoryProps.load(url.openStream());
				} catch (IOException e) {
					if (DebugHelper.DEBUG) {
						DebugHelper.logError(e);
					}
				}
			}
		}
		return springclrfactoryProps;
	}

	/**
	 * Get String value of the name parameter.
	 * 
	 * @param name
	 * @param properties
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String name, Properties properties,
			String defaultValue) {
		String s = getValue(name, properties);
		if (StringUtils.isEmpty(s)) {
			if (DebugHelper.DEBUG) {
				if (StringUtils.isEmpty(defaultValue)) {
					DebugHelper.log("Property <" + name + "> not founded.", 1);
				} else {
					DebugHelper.log("Property <" + name + ">=" + defaultValue
							+ " [from DEFAULT].", 1);
				}
			}
			return defaultValue;
		}
		return s;
	}

	/**
	 * Get int value of the name parameter.
	 * 
	 * @param name
	 * @param properties
	 * @param defaultValue
	 * @return
	 */
	public static int getValue(String name, Properties properties,
			int defaultValue) {
		String s = getValue(name, properties);
		if (StringUtils.isEmpty(s)) {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("Property <" + name + ">=" + defaultValue
						+ " [from DEFAULT].", 1);
			}
			return defaultValue;
		}
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * Get boolean value of the name parameter.
	 * 
	 * @param name
	 * @param properties
	 * @param defaultValue
	 * @return
	 */
	public static boolean getValue(String name, Properties properties,
			boolean defaultValue) {
		String s = getValue(name, properties);
		if (StringUtils.isEmpty(s)) {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("Property <" + name + ">=" + defaultValue
						+ " [from DEFAULT].", 1);
			}
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * Get value of the name parameter.
	 * 
	 * @param name
	 * @param properties
	 * @return
	 */
	private static String getValue(String name, Properties properties) {
		if (properties != null) {
			Object value = properties.get(name);
			if (!StringUtils.isEmpty(value == null ? null : value.toString())) {
				if (DebugHelper.DEBUG) {
					DebugHelper
							.log("Property <" + name + ">=" + value + " [from "
									+ SPRINGCLRFACTORY_PROPERTIES + "].", 1);
				}
				return value.toString();
			}
		}
		String value = System.getProperty(name);
		if (!StringUtils.isEmpty(value)) {
			if (DebugHelper.DEBUG) {
				DebugHelper.log("Property <" + name + ">=" + value
						+ " [from JVM Parameters].", 1);
			}
		}
		return value;
	}
}
