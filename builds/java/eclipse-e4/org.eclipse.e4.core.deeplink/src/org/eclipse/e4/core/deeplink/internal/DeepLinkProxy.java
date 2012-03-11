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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.e4.core.deeplink.DeepLinkResult;
import org.eclipse.e4.core.deeplink.DeepLinkResultException;

/**
 * A simple HTTP proxy that executes deep links on a remote deep link aware
 * application.
 * <p>
 * FIXME: This code runs in the UI thread. A limitation of the current
 * implementation is that it does not detect if the deep link request is back to
 * the same application as is initiating the request. If this type of request
 * occurs, it is possible for the background servlet thread receiving the
 * request to deadlock the UI thread. This is because the UI thread (running
 * around line 68 below) will be waiting for a response from the "remote"
 * servlet, but the "remote" servlet may be waiting to get on the UI thread in
 * order to process the request and produce a response.
 */
public class DeepLinkProxy {
	private InstallationLauncher installationLauncher;
	
	private static final int INITIAL_WAIT = 1500;
	private static final int SUBSEQUENT_WAIT = 500;
	private final long TIMEOUT_DURATION = 60 * 1000;

	public DeepLinkProxy(InstallationLauncher installationLauncher) {
		this.installationLauncher = installationLauncher;
	}

	/**
	 * @param installation The installation that should handle this request
	 * @param httpDeepLink The http:// form of the URL
	 * @param originalDeepLink The deeplink:// form of the URL
	 * 
	 * @return DeepLinkResult the results of executing the deep link on success
	 * 
	 * @throws IOException If we couldn't communicate with the deep link server
	 * @throws DeepLinkResultException If the results the deep link server couldn't be parsed
	 */
	public DeepLinkResult execute(String installation, URL httpDeepLink, String originalDeepLink) throws IOException, DeepLinkResultException {
		try {
			return proxyDeepLinkURL(httpDeepLink);
		} catch (IOException e) {
			// Assume that the reason for the IOException is that the 
			// installation isn't running.
			installationLauncher.startInstallation(installation, originalDeepLink);
			return invokeDeepLinkWithTimeout(installation, httpDeepLink);
		}
	}

	private DeepLinkResult invokeDeepLinkWithTimeout(String installation, URL httpDeepLink) {
		pause(INITIAL_WAIT);
		long timeout = System.currentTimeMillis() + TIMEOUT_DURATION;
		while (true) {
			try {
				return proxyDeepLinkURL(httpDeepLink);
			} catch (IOException e2) {
				if (System.currentTimeMillis() > timeout) {
					throw new DeepLinkResultException("Timeout invoking deep link: [" + installation + "]: " + httpDeepLink);
				}
				pause(SUBSEQUENT_WAIT);
			}
		}
	}

	private void pause(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e1) {
		}
	}

	private DeepLinkResult proxyDeepLinkURL(URL httpDeepLink) throws IOException, DeepLinkResultException {
		InputStream resultStream = httpDeepLink.openStream();
		try {
			return new DeepLinkResult(resultStream);
		} finally {
			if (resultStream != null) {
				resultStream.close();
			}
		}
	}
}
