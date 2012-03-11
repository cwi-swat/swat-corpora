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
package org.eclipse.e4.xwt.springframework;

import static java.text.MessageFormat.format;
import static org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.getValue;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.xwt.ICLRFactory;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.springframework.internal.Activator;
import org.eclipse.e4.xwt.springframework.internal.ApplicationContextTracker;
import org.eclipse.e4.xwt.springframework.internal.DebugHelper;
import org.eclipse.e4.xwt.springframework.internal.Messages;
import org.eclipse.e4.xwt.springframework.internal.utils.ArgumentsUtils.Result;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.springframework.context.ApplicationContext;

/**
 * {@link ICLRFactory} Spring implementation to use into OSGi context (with
 * Spring DM).
 * 
 * <p>
 * Example :
 * 
 * <code>
 * <Shell xmlns="http://www.eclipse.org/xwt/presentation"
    xmlns:x="http://www.eclipse.org/xwt"
    x:ClassFactory="org.eclipse.e4.xwt.springframework.SpringCLRFactory.INSTANCE bean=myUI bundle=org.eclipse.e4.xwt.springframework.osgi.sample1">
    ...
    </code>
 * </p>
 */
public class SpringCLRFactory extends AbstractNoCachableSpringCLRFactory {

	public static SpringCLRFactory INSTANCE = new SpringCLRFactory();

	private static final String BUNDLE_PARAMETER_KEY = "bundle";

	protected SpringCLRFactory() {
		// Don't Instantiate this class!!! Use SpringCLRFactory.INSTANCE into
		// the x:ClassFactory
	}

	protected ApplicationContext getApplicationContext(IArguments arguments) {
		if (Activator.getDefault() == null) {
			throw new XWTException(format(
					Messages.ACTIVATOR_NOT_STARTED));
		}		
		
		Result symbolicNameResult = getValue(BUNDLE_PARAMETER_KEY, arguments,
				Activator.getDefault().getDefaultBundle(),
				Messages.BUNDLE_PARAM_REQUIRED_ERROR);
		String symbolicName = symbolicNameResult.value;
		if (DebugHelper.DEBUG) {
			DebugHelper.log("bundle parameter=" + symbolicName + " (from "
					+ symbolicNameResult.source + ") [OK].", 1);
		}

		return getApplicationContext(symbolicName);
	}

	@Override
	protected void throwApplicationContextNotAvailable(IArguments arguments) {
		throw new XWTException(format(
				Messages.OSGI_APPLICATION_CONTEXT_NOT_AVAILABLE,
				arguments.getValue(BUNDLE_PARAMETER_KEY)));
	}

	private ApplicationContext getApplicationContext(String symbolicName) {
		// Get the bundle from the Platform.
		Bundle contributorBundle = Platform.getBundle(symbolicName);
		if (contributorBundle == null) {
			throw new XWTException(format(Messages.BUNDLE_NOT_FOUNDED,
					symbolicName));
		}

		if (DebugHelper.DEBUG) {
			DebugHelper.log("OSGi Bundle <" + symbolicName + "> founded [OK].",
					1);
		}

		// Bundle is founded, check the ACTIVE state.
		if (contributorBundle.getState() != Bundle.ACTIVE) {
			try {
				contributorBundle.start();
			} catch (BundleException e) {
				if (DebugHelper.DEBUG) {
					DebugHelper.logError(e);
				}
				// throw new XWTException(e);
			}
		}
		if (DebugHelper.DEBUG) {
			DebugHelper.log("OSGi Bundle <" + symbolicName + "> started [OK].",
					1);
		}

		// Use ServiceTracket to get Spring ApplicationContext from the bundle.
		int timeout = Activator.getDefault().getTimeout();
		if (DebugHelper.DEBUG) {
			DebugHelper.log(
					"Searching Spring ApplicationContext from the OSGi Bundle=<"
							+ symbolicName + "> with timeout=<" + timeout
							+ "(ms)>...", 1);
		}
		final ApplicationContextTracker applicationContextTracker = new ApplicationContextTracker(
				contributorBundle, Activator.getDefault().getBundleContext());
		ApplicationContext applicationContext = null;
		try {
			applicationContext = applicationContextTracker
					.getApplicationContext(timeout);
		} finally {
			applicationContextTracker.close();
		}
		return applicationContext;
	}

}
