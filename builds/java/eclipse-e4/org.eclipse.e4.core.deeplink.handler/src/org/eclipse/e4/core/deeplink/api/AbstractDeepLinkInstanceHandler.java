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
package org.eclipse.e4.core.deeplink.api;

import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * This class defines the abstract behavior for all deeplink-addressable objects
 * that define method callbacks.
 */
public abstract class AbstractDeepLinkInstanceHandler {

	/**
	 * Activate a deeplink callback on this object.
	 * <p>
	 * 
	 * @param handlerInstanceID
	 *            The application instance (or RCP container) that this deeplink
	 *            currently is living in.
	 * @param action
	 *            the "action" or verb part of the URL or an empty string if
	 *            none.
	 * @param params
	 *            Any deeplink parameters specified as a part of the URL.
	 * @return A Map<String, String> containing any arbitrary results.
	 */
	public abstract Map<String, String> activate(String handlerInstanceID, String action, Map<String, String[]> params);

	/**
	 * Execute the application-specified handler for the URL
	 * 
	 * @param results
	 * @throws LinkFailureException
	 */
	public void handle(final ParameterProcessResults[] results, final String handlerInstanceId, final String action, final Map<String, String[]> params) {
		debug("Command received: " + handlerInstanceId);
		debug("Running action for app: " + handlerInstanceId);
		try {
			results[0].outputData = activate(handlerInstanceId, action , params);
			results[0].activatedParameterCallback = true;
		} catch (Throwable t) {
			error(handlerInstanceId);
			rethrowException(t);
		}
	}

	protected IStatus status(int severity, String message) {
		return new Status(severity, Activator.PLUGIN_ID, message);
	}

	protected void error(String message) {
		getLog().log(status(Status.ERROR, message));
	}

	protected void info(String message) {
		getLog().log(status(Status.INFO, message));
	}

	protected void debug(String message) {
		getLog().log(status(Status.WARNING, message));
	}

	private ILog getLog() {
		return Activator.getDefault().getLog();
	}

	private void rethrowException(Throwable terminatingException) {
		if (terminatingException != null) {
			if (!(terminatingException instanceof RuntimeException)) {
				throw new RuntimeException(terminatingException);
			}
			throw (RuntimeException) terminatingException;
		}
	}

}