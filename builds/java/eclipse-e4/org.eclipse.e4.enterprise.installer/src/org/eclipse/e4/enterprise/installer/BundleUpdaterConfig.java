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
package org.eclipse.e4.enterprise.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.Platform;

/**
 * Retrieve the BundleUpdater's default values from the configuration file using
 * the configuration service.
 */
public class BundleUpdaterConfig {
	/*
	 * FIXME: Need to somehow inject this into the installer rather than introducing
	 * a dependency on the configuration service since not everyone wants to use the
	 * configuration service.
	 * 
	 * One idea is to use "import package" and specify a package that must be 
	 * defined in some platform with this class's API?  Then OSGI will resolve 
	 * this for us...
	 */
	public static final String UPDATE_SITE_KEY = "org.eclipse.e4.installer.UPDATE_SITE";
	public static final String DOWNLOAD_ROOT_KEY = "org.eclipse.e4.installer.DOWNLOAD_ROOT";
	public static final String PROVISIONING_LOCATION_KEY = "org.eclipse.e4.installer.PROVISIONING_LOCATION";

	private static File defaultDownloadRoot;

	static Properties props = null;

	private static Properties getConfigurationProperties() {
		if (props == null) {
			props = Activator.getDefault().getConfiguration().getProperties();
		}
		return props;
	}

	// provisioning------------------------------------------------------------------------------

	public static Set<FeatureVersionedIdentifier> retrieveProvisioningFromConfig() throws InstallError {
		String urlAsString = (String) getConfigurationProperties().get(PROVISIONING_LOCATION_KEY);
		if (null == urlAsString) {
			return null;
		}

		URL url = convertToUrl(urlAsString);

		Set<FeatureVersionedIdentifier> features;
		try {
			features = convertToFeatureVersionSet(url.openStream());
		} catch (IOException e) {
			throw new InstallError(e);
		}
		return features;
	}

	static Set<FeatureVersionedIdentifier> convertToFeatureVersionSet(InputStream stream) throws InstallError {
		HashSet<FeatureVersionedIdentifier> result = new HashSet<FeatureVersionedIdentifier>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			String line = reader.readLine();
			while (line != null) {
				line = trimComments(line);
				if (isNotBlankLine(line)) {
					result.add(convertSpaceSeparated(line));
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new InstallError(e);
		}
		return result;
	}

	private static String trimComments(String line) {
		return line.split("#")[0];
	}

	private static boolean isNotBlankLine(String line) {
		return !"".equals(line.trim());
	}

	private static FeatureVersionedIdentifier convertSpaceSeparated(String line) throws InstallError {
		String[] split = line.split("\\s+");
		if (split.length > 2) {
			throw new InstallError("Too many arguments (should be max 2: 'feature' and 'version' ) on this line: " + line);
		}

		String version = null;
		if (split.length == 1) {
			version = "0.0.0";
		} else {
			version = split[1];
		}
		return new FeatureVersionedIdentifier(split[0], version);
	}

	// download Root
	// ---------------------------------------------------------------------------------

	public static File findDownloadDirectoryRoot() {
		if (null == getConfigurationProperties()) {
			return getDefaultDownloadRoot();
		}
		String downloadRootAsString = (String) getConfigurationProperties().get(DOWNLOAD_ROOT_KEY);
		if (null != downloadRootAsString) {
			return new File(downloadRootAsString);
		} else {
			return getDefaultDownloadRoot();
		}
	}

	static synchronized File getDefaultDownloadRoot() {
		if (defaultDownloadRoot == null) {
			// turns the platform installation directory in to an absolute
			// path
			String installDir = Platform.getInstallLocation().getURL().getPath();
			
			// FIXME: Breaks Linux
//			// strip out any leading / as this (wrongly) turns this absolute
//			// path in to a relative one
//			if (installDir.startsWith("/")) {
//				installDir = installDir.substring(1);
//			}
			defaultDownloadRoot = new File(installDir + "/DownloadRoot");
			defaultDownloadRoot.mkdir();
		}
		return defaultDownloadRoot;
	}

	// update sites
	// ---------------------------------------------------------------------------------

	public static URL[] retrieveUpdateSiteFromConfig() throws InstallError {
		return parseURLs((String) getConfigurationProperties().get(UPDATE_SITE_KEY));
	}

	static URL convertToUrl(String urlAsString) throws InstallError {
		try {
			return new URL(urlAsString.trim());
		} catch (MalformedURLException e) {
			throw new InstallError(e);
		}
	}

	static URL[] parseURLs(String spaceSeparatedURLs) throws InstallError {
		if (spaceSeparatedURLs == null) {
			throw new InstallError("spaceSeparatedURLs cannot be null");
		}
		spaceSeparatedURLs = spaceSeparatedURLs.trim();
		String[] urls = spaceSeparatedURLs.split("\\s+");
		URL[] result = new URL[urls.length];
		for (int i = 0; i < urls.length; i++) {
			result[i] = convertToUrl(urls[i]);
		}
		return result;
	}

}
