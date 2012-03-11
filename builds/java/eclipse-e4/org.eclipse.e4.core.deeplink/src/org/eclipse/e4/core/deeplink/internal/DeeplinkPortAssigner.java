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

/**
 * This class manages the assignment of socket port numbers to each application,
 * and the persistence of that information in a properties file. You can choose
 * to regenerate port numbers if they are already assigned, or throw a
 * RuntimeException.
 */
public class DeeplinkPortAssigner {
	private final DeepLinkProperties properties;
	private boolean regeneratePortNumbersOnClash;

	public DeeplinkPortAssigner(DeepLinkProperties props, boolean regeneratePortNumbersOnClash) {
		this.regeneratePortNumbersOnClash = regeneratePortNumbersOnClash;
		
		if (props == null) {
			throw new IllegalArgumentException(
					"Properties object passed to DeeplinkPortAssigner was null.");
		}

		this.properties = props;
	}

	public int getPortNumberForInstallation(String myInstallation) {
		String installationPortNumberString = properties.getInstallationPort(myInstallation);
		if (installationPortNumberString == null) {
			int portNumber = properties.calculateNextPortNumber();
			properties.setInstallationPort(myInstallation, portNumber);
			return portNumber;
		} else {
			if (!properties.isPortNumberUnique(myInstallation, installationPortNumberString)) {
				return regeneratePortNumber(myInstallation);
			} else {
				return Integer.parseInt(installationPortNumberString);
			}
		}
	}

	private int regeneratePortNumber(String myInstallation) {
		if(!regeneratePortNumbersOnClash) {
			throw new RuntimeException("Port number already assigned to another installation.");
		}
		properties.removeInstallationPort(myInstallation);
		int portNumber = properties.calculateNextPortNumber();
		properties.setInstallationPort(myInstallation, portNumber);
		return portNumber;
	}

}
