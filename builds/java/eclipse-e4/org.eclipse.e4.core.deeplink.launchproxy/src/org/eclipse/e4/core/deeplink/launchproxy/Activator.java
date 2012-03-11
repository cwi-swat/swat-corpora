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

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.e4.core.deeplink.launchproxy";
	
	private static Activator bundle = null;
	
	public static Activator getDefault() {
		return bundle;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		bundle = this;
	}
	
}
