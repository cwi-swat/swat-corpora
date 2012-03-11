/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.metaconfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;

/**
 * Class Configuration. Supply overall configuration for an RCP container.
 * <p>
 * Configuration is the object that hosts all configuration information for a
 * particular RCP container. While most of the configuration will be exposed in
 * the Properties object returned from getProperties(), we actually return a
 * Configuration object itself from our OSGI configuration service to facilitate
 * API evolution for the future.
 */
public class Configuration {
	/*
	 * These are methods rather than constants so that we can run this class in
	 * a JUnit test without running up a full OSGI container.
	 */

	/**
	 * @return the installation directory path. This works fine as-of Eclipse
	 *         3.x, but e4 may introduce http: or other URLs where more things
	 *         are remote.
	 */
	public String installLocation() {
		return Platform.getInstallLocation().getURL().getPath();
	}

	/**
	 * @return the user home directory path. This works fine as-of Eclipse 3.x,
	 *         but e4 may introduce http: or other URLs where more things are
	 *         remote.
	 */
	public String userLocation() {
		return Platform.getUserLocation().getURL().getPath();
	}

	/**
	 * @return the workspace's file system path. This works fine as-of Eclipse
	 *         3.x, but e4 may introduce http: or other URLs where more things
	 *         are remote.
	 */
	public String workspaceLocation() {
		return Platform.getInstanceLocation().getURL().getPath();
	}

	/**
	 * @return the location of the system's meta-properties file. Note that
	 *         similar to the previous methods, this works fine in Eclipse 3.x.
	 *         But e4 may introduce more remoting, which could break this
	 */
	String metaPropertiesFileLocation() {
		return installLocation() + "propertiesLocation.ini";
	}

	// The following is package-private for testability

	InputStream openMetaPropertiesFile() throws FileNotFoundException {
		return new FileInputStream(metaPropertiesFileLocation());
	}

	static final String CONFIGURATION_KEY = "ConfigurationLocation";

	String getPropertiesFileLocation() {
		try {
			InputStream in = openMetaPropertiesFile();
			Properties properties = new Properties();
			properties.load(in);
			return properties.getProperty(CONFIGURATION_KEY);
		} catch (Exception e) {
			throw new ConfigurationException("Problem opening/reading meta-properties file: " + metaPropertiesFileLocation(), e);
		}
	}

	String substitute(String source, String substituteFor, String substitution) {
		nullCheck(source, "Source cannot be null");
		nullCheck(substituteFor, "SubstituteFor cannot be null");
		nullCheck(substitution, "Substitution cannot be null");

		int position = source.indexOf(substituteFor);
		if (position >= 0) {
			String head = source.substring(0, position);
			String tail = source.substring(position + substituteFor.length());
			return head + substitution + tail;
		} else {
			return source;
		}
	}

	private void nullCheck(String source, String exceptionMessage) {
		if (source == null) {
			throw new IllegalArgumentException(exceptionMessage);
		}
	}

	/*
	 * We support the following variables for substitution in path URLs:
	 * <ul>
	 * <li>${install_location} - The directory containing your.exe
	 * <li>${user_location} - The user's home directory 
	 * <li>${workspace_location} - The RCP workspace location
	 * </ul>
	 */
	static final String INSTALL_VAR = "${install_location}";
	static final String USER_VAR = "${user_location}";
	static final String WORKSPACE_VAR = "${workspace_location}";

	String substituteVariables(String input) {
		nullCheck(input, "Attempted to substitute into a null string");

		input = substitute(input, INSTALL_VAR, installLocation());
		input = substitute(input, USER_VAR, userLocation());
		input = substitute(input, WORKSPACE_VAR, workspaceLocation());
		return input;
	}

	private Properties configurationProperties = null;
	private Object lock = new Object();

	/**
	 * @return A Properties object representing the system configuration file.
	 *         Note that this configuration file may be remote and thus should
	 *         be considered read-only.
	 */
	public Properties getProperties() throws ConfigurationException {
		/*
		 * this is acting like a singleton, so we synchronize to prevent any
		 * race condition.
		 */
		synchronized (lock) {
			if (configurationProperties == null) {
				configurationProperties = populateProperties(getPropertiesFileLocation());
			}
		}

		Properties clone = (Properties) configurationProperties.clone();
		return clone;
	}

	Properties populateProperties(String configFileSource)  {
		Properties props = new Properties();
		if (null == configFileSource) {
			throw new ConfigurationException("null configFileSource");
		}
		configFileSource = substituteVariables(configFileSource);

		try {
			URL configFileURL;
			configFileURL = new URL(configFileSource);
			InputStream configFile = configFileURL.openStream();
			props.load(configFile);
		} catch (Exception e) {
			throw new ConfigurationException("could not load properties from config file with URL string: [" + configFileSource +  
					"]" , e);
		}
		return props;
	}
}
