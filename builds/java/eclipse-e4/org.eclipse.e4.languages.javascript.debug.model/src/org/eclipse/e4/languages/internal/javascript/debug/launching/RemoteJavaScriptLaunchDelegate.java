/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.internal.javascript.debug.launching;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.e4.languages.javascript.debug.model.JSDIDebugTarget;
import org.eclipse.e4.languages.javascript.debug.model.JSDIModelActivator;
import org.eclipse.e4.languages.javascript.jsdi.VirtualMachine;
import org.eclipse.e4.languages.javascript.jsdi.connect.AttachingConnector;

public class RemoteJavaScriptLaunchDelegate extends LaunchConfigurationDelegate {

	final static String LAUNCH_URI = "launch_uri"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org. eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode, final ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String name = configuration.getAttribute(ILaunchConstants.CONNECTOR_ID, (String) null);
		AttachingConnector connector = (AttachingConnector) JSDIModelActivator.getConnectionsManager().getConnector(name);
		if (connector == null) {
			// TODO NLS this
			Status status = new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Error occured while launching - could not locate connector: " + name); //$NON-NLS-1$
			throw new CoreException(status);
		}
		Map argmap = configuration.getAttribute(ILaunchConstants.ARGUMENT_MAP, (Map) null);
		if (argmap == null) {
			// TODO NLS this
			Status status = new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Error occured while launching - connecting argument map was null"); //$NON-NLS-1$
			throw new CoreException(status);
		}
		// TODO we should also set the timeout here as well
		VirtualMachine vm;
		try {
			vm = connector.attach(argmap);
		} catch (IOException e) {
			Status status = new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Error occured while launching", e); //$NON-NLS-1$
			throw new CoreException(status);
		}

		// TODO the process should be the OSGi launched process
		JSDIDebugTarget target = new JSDIDebugTarget(vm, null, launch, vm.name(), true, true);
		launch.addDebugTarget(target);
	}
}
