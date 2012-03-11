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
package org.eclipse.e4.core.deeplink.launchproxy;

import java.util.Date;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.deeplink.DeepLinkManager;
import org.eclipse.e4.core.deeplink.DeepLinkResult;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;


/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private final String COMMANDLINE_KEY = "application.args";
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(COMMANDLINE_KEY);
		ILog systemLog = Platform.getLog(Platform.getBundle(Activator.PLUGIN_ID));
		TeeLogger logger = new TeeLogger(systemLog);
		DeepLinkManager installationManager = new DeepLinkManager(false, logger);
		try {
			DeepLinkResult result = installationManager.processDeepLink(getDeepLinkURL(args, logger));
			for (Object key : result.outputData.keySet()) {
				Object value = result.outputData.get(key);
				System.out.println(key + " ==> " + value);
			}
		} catch (Exception e) {
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, "Unhandled exception: " + e.getMessage(), e));
		}
		return IApplication.EXIT_OK;
	}

	private static String getDeepLinkURL(String[] args, ILog logger) {
		if (args.length != 1) {
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, "Wrong number of arguments: " + args.length));
			throw new IllegalArgumentException("Wrong number of arguments: " + args.length);
		}
		return args[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}

	private class TeeLogger implements ILog {
		private ILog delegate;

		public TeeLogger(ILog delegate) {
			this.delegate = delegate;
		}
		
		public void addLogListener(ILogListener listener) {
			delegate.addLogListener(listener);
		}

		public Bundle getBundle() {
			return delegate.getBundle();
		}

		public void log(IStatus status) {
			/* 
			 * 2010-02-25
			 * 
			 * FIXME: Eclipse creates a separate 12345.log file in the configuration
			 * directory PER-INVOCATION.  So we only delegate to the Eclipse logger
			 * if there is an error to log, and not otherwise.
			 * 
			 * Ideally, we would figure out how to make Eclipse log to a single .log
			 * file like it does with the Workbench up and running.
			 */
			if (Status.ERROR == status.getSeverity()) {
				delegate.log(status);
				System.err.println(new Date() + ": " + status.toString());
				Throwable e = status.getException();
				if (e != null) {
					e.printStackTrace(System.err);
				}
			} else {
				System.out.println(new Date() + ": " + status.toString());
			}
			System.err.flush();
			System.out.flush();
		}

		public void removeLogListener(ILogListener listener) {
			delegate.removeLogListener(listener);
		}
	}

	// Command-line version ----------------------------------------------------
	
	static ILog stubLogger = new ILog() {
		public void addLogListener(ILogListener listener) {
		}

		public Bundle getBundle() {
			// No bundle as we're not in an OSGI container
			return null;
		}

		public void log(IStatus status) {
			// TODO: If using command-line version, put your own custom (proper) logging here
			if (Status.ERROR == status.getSeverity()) {
				System.err.println(new Date() + ": " + status.toString());
				Throwable e = status.getException();
				if (e != null) {
					e.printStackTrace(System.err);
				}
			} else {
				System.out.println(new Date() + ": " + status.toString());
			}
			System.err.flush();
			System.out.flush();
		}

		public void removeLogListener(ILogListener listener) {
		}
	};
	
	public static void main(String[] args) {
		DeepLinkManager installationManager = new DeepLinkManager(false, stubLogger);
		installationManager.processDeepLink(getDeepLinkURL(args, stubLogger));
	}

}
