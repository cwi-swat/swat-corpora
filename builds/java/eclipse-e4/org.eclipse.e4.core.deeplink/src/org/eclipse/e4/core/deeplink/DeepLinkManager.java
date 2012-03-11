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
package org.eclipse.e4.core.deeplink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.deeplink.internal.DeepLinkProperties;
import org.eclipse.e4.core.deeplink.internal.DeepLinkProxy;
import org.eclipse.e4.core.deeplink.internal.DeeplinkPortAssigner;
import org.eclipse.e4.core.deeplink.internal.InstallationLauncher;
import org.eclipse.e4.core.deeplink.internal.ParsedDeepLinkURL;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * The DeepLinkManager is the entry-point for the DeepLinking API. This class
 * can be used as a singleton, but you do not have to. Using it as a singleton
 * has some minor performance benefits.
 */
public class DeepLinkManager {
	
	private static final String DEEPLINK_PROPERTIES_FILE_NAME = "/deeplink4.properties";
	private static final String PROP_FILE_HEADER = "Deep Link 4.0 Configuration";
	private static DeepLinkManager deepLinkManager = null;

	// We'll provide a reasonable default for win32.  Clients should normally inject their own
	// path explicitly.
	private static final String DEFAULT_SERVER_PATH="C:\\Program Files\\Deeplink";
	
	private final String SERVER_CONFIG_PATH;
	private final String SERVER_ROOT_PATH;
	private final String APPLICATION_PROPS;

	private ILog logger;
	private DeepLinkProperties properties;
	private DeeplinkPortAssigner portAssigner;
	private DeepLinkProxy deepLinkProxy;
	
	
	public static DeepLinkManager getDefault() {
		return deepLinkManager;
	}

	/**
	 * Initialize DeepLinking.
	 * 
	 * @param regeneratePortNumbersOnClash true if port numbers should be automatically generated.  false otherwise.
	 * @param logger an ILog implementation to use for logging.
	 * @param rootPath Directories containing applications that use deeplinking live under this folder/path.
	 * @param configPath The directory containing the deeplink4.properties file
	 */
	public DeepLinkManager(boolean regeneratePortNumbersOnClash, ILog logger, String rootPath, String configPath) {
		SERVER_ROOT_PATH = rootPath;
		SERVER_CONFIG_PATH = configPath;
		APPLICATION_PROPS = SERVER_CONFIG_PATH + DEEPLINK_PROPERTIES_FILE_NAME;
		init(regeneratePortNumbersOnClash, logger);
	}
	
	/**
	 * Initialize DeepLinking.  Initialize with Win32-specific paths, assuming that "c:\Program Files\DeepLink" is
	 * writable by the user who will be running the application.
	 * 
	 * @param regeneratePortNumbersOnClash true if port numbers should be automatically generated.  false otherwise.
	 * @param logger an ILog implementation to use for logging.
	 */
	public DeepLinkManager(boolean regeneratePortNumbersOnClash, ILog logger) {
		SERVER_CONFIG_PATH = DEFAULT_SERVER_PATH;
		SERVER_ROOT_PATH = DEFAULT_SERVER_PATH;
		APPLICATION_PROPS = SERVER_CONFIG_PATH + DEEPLINK_PROPERTIES_FILE_NAME;
		init(regeneratePortNumbersOnClash, logger);
	}

	private void init(boolean regeneratePortNumbersOnClash, ILog logger) {
		deepLinkManager = this;
		this.logger = logger;
		properties = getDeepLinkProperties();
		this.portAssigner = new DeeplinkPortAssigner(properties, regeneratePortNumbersOnClash);
		InstallationLauncher installationLauncher = new InstallationLauncher(SERVER_ROOT_PATH, properties);
		this.deepLinkProxy = new DeepLinkProxy(installationLauncher);
	}
	
	// TODO: Test drive this?
	public String getInstallationIDFromPath(URL url) {
		String fileURL = url.toString();
		String path = fileURL.replace("file:/", "");
		if (path.lastIndexOf('/') == path.length()-1) {
			path = path.substring(0, path.length()-1);
		}
		int endOfPrefix = path.lastIndexOf('/');
		if (endOfPrefix > 0) {
			path = path.substring(endOfPrefix+1);
		}
		return path;
	}
	
	public int getPortNumberForInstallation(String installation) {
		int result = portAssigner.getPortNumberForInstallation(installation);
		try {
			properties.store(propsOutputStream(), PROP_FILE_HEADER);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * Takes a deeplink URL as a String and composes the appropriate URL object, looking
	 * up the correct port number for the application, and returns the result.
	 */
	public DeepLinkResult processDeepLink(String deepLinkURL) {
		try {
			ParsedDeepLinkURL deepLink = new ParsedDeepLinkURL(deepLinkURL);
			int portNumber = portAssigner.getPortNumberForInstallation(deepLink.installation);
			URL httpLink = new URL("http", "localhost", portNumber, "/deeplink" + deepLink.restOfURL);
			DeepLinkResult result = deepLinkProxy.execute(deepLink.installation, httpLink, deepLinkURL);
			logInfo("INVOKED: " + deepLink.installation + deepLink.restOfURL + 
					"\" loaded=\"" + result.loaded + "\" callbackRan=\"" + result.callbackRan + "\" exception=\"" + result.exception + "\"");
			return result;
		} catch (DeepLinkURLException e) {
			logFailure("Unable to open URL: " + deepLinkURL, e);
			throw e;
		} catch (MalformedURLException e) {
			logFailure("Malformed deep link URL (" + e.getMessage() + "): " + deepLinkURL, e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			logFailure("Unable to contact deep link handler for: " + deepLinkURL, e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (DeepLinkResultException e) {
			logFailure("Unable to parse result XML: " + e.getMessage(), e);
			throw e;
		}
	}

	private DeepLinkProperties getDeepLinkProperties() {
		DeepLinkProperties result = new DeepLinkProperties();
		try {
			result.load(propsInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	private OutputStream propsOutputStream() {
		try {
			return new FileOutputStream(new File(APPLICATION_PROPS));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private InputStream propsInputStream() {
		try {
			return new FileInputStream(new File(APPLICATION_PROPS));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void logInfo(String message) {
		IStatus status = new Status(Status.INFO, Activator.PLUGIN_ID, message);
		logger.log(status);
	}
	
	private void logFailure(String message, Throwable t) {
		IStatus status = new Status(Status.ERROR, Activator.PLUGIN_ID, message, t);
		logger.log(status);
	}

	/**
	 * Start the deep linking HTTP server if it isn't already started.  If it 
	 * is started, restart it using the port specified by the deep linking
	 * configuration.
	 * 
	 * @throws BundleException if a bundle cannot be resolved or loaded.
	 */
	public void startServer() throws BundleException {
		String installation = getInstallationIDFromPath(Platform.getInstallLocation().getURL());
		int serverPort = getPortNumberForInstallation(installation);
		
		System.getProperties().setProperty("org.osgi.service.http.port", Integer.toString(serverPort));
		BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
		new DeepLinkBundleList(bundleContext).startupBundlesForHttpServlets();
	}

	private static final int ONE_SECOND = 1000;
	private final String COMMANDLINE_KEY = "application.args";
	
	/**
	 * Any command line arguments that begin with "deeplink://" will be interpreted
	 * as a deeplink URL, and those deep links will be scheduled to be invoked.
	 * <p>
	 * A Job is used to actually run the deep link because this method is 
	 * typically called before the Workbench has been loaded, so we wait for 
	 * the Workbench before running deep links that might reference 
	 * workbench resources like perspectives, commands, etc.
	 * 
	 * @param context The IApplicationContext containing the command line arguments to examine.
	 */
	public void processCommandLineArguments(IApplicationContext context) {
		final String[] args = (String[]) context.getArguments().get(COMMANDLINE_KEY);
		Job deepLinkJob = new Job("Deep Link Launcher") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (String arg : args) {
					if (arg.startsWith("deeplink://")) {
						deepLinkManager.processDeepLink(arg);
					}
				}
				return Status.OK_STATUS;
			}};
		deepLinkJob.schedule(ONE_SECOND);
	}

}
