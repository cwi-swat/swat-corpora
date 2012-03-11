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
package org.eclipse.e4.core.deeplink.internal;

import java.util.Collection;
import java.util.Properties;

/**
 * A Java Properties object defining default values for all of deeplinking's
 * property values.
 * <p>
 * Concerns handled here include: Assigning port numbers to RCP application
 * instances, specifying a base port number for computing new port numbers, and
 * specifying the command used to launch a particular RCP application instance
 * if it isn't already running.
 */
public class DeepLinkProperties extends Properties {
	private static final long serialVersionUID = 1L;

	static final int DEFAULT_BASE_PORT_NUMBER = 9000;
	
	private static final String BASE_PORT_NUMBER_KEY = "base.port.number";
	private static final String INSTALLATION_PREFIX = "instance.";
	private static final String INSTALLATION_PORT_SUFFIX = ".port";
	private static final String INSTALLATION_COMMAND_SUFFIX = ".command";

	private int nextPortNumber;

	public DeepLinkProperties() {
		super();
		nextPortNumber = getBasePortNumber();
	}

	public DeepLinkProperties(Properties defaults) {
		super(defaults);
		nextPortNumber = getBasePortNumber();
	}

	private String installationPortKey(String myInstallation) {
		return INSTALLATION_PREFIX + myInstallation + INSTALLATION_PORT_SUFFIX;
	}

	public int getBasePortNumber() {
		int basePortNumber;
		String basePortString = getProperty(BASE_PORT_NUMBER_KEY);
		if (basePortString == null) {
			basePortNumber = DEFAULT_BASE_PORT_NUMBER;
		} else {
			basePortNumber = Integer.parseInt(basePortString);
		}
		return basePortNumber;
	}

	public String getInstallationPort(String myInstallation) {
		return getProperty(installationPortKey(myInstallation));
	}

	public void setInstallationPort(String myInstallation, int port) {
		setProperty(installationPortKey(myInstallation), Integer.toString(port));
	}
	
	public void removeInstallationPort(String myInstallation) {
		remove(installationPortKey(myInstallation));
	}

	public boolean isPortNumberUnique(String myInstallation, String portNumber) {
		Properties tempProperties = (Properties) clone();
		tempProperties.remove(BASE_PORT_NUMBER_KEY);
		tempProperties.remove(installationPortKey(myInstallation));

		return !tempProperties.values().contains(portNumber);
	}

	@SuppressWarnings("unchecked")
	public int calculateNextPortNumber() {
		Properties tempProperties = (Properties) clone();
		tempProperties.remove(BASE_PORT_NUMBER_KEY);

		Collection values = tempProperties.values();
		while (values.contains(Integer.toString(nextPortNumber))) {
			nextPortNumber++;
		}

		return nextPortNumber;
	}
	
	public String getInstallationCommand(String installation) {
		String key = INSTALLATION_PREFIX + installation + INSTALLATION_COMMAND_SUFFIX;
		return getProperty(key);
	}

}
