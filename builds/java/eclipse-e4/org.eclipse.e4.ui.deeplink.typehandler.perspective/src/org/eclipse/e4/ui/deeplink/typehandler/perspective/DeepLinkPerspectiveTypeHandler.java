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
package org.eclipse.e4.ui.deeplink.typehandler.perspective;

import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkInstanceHandler;
import org.eclipse.e4.core.deeplink.api.AbstractDeepLinkTypeHandler;
import org.eclipse.e4.core.deeplink.api.Activator;
import org.eclipse.e4.core.deeplink.api.ParameterProcessResults;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;


/**
 * Implements a type handler for deeplinks of the following form:
 * <p>
 * deeplink://application/perspective/eclipse.perspective.id/action?param1=value1&param2=value2
 * <p>
 * where the RCP perspective factory object may optionally be an
 * {@link AbstractDeepLinkInstanceHandler}.  If the perspective factory is an
 * instanceof {@link AbstractDeepLinkInstanceHandler}, the type handler will
 * automatically call 
 * {@link AbstractDeepLinkInstanceHandler#activate(String, String, java.util.Map)}
 * whenever the perspective is opened or activated through a deeplink.
 */
public class DeepLinkPerspectiveTypeHandler extends AbstractDeepLinkTypeHandler {

	private static final String PERSPECTIVE_ID_ATTRIBUTE = "id";
	private static final long serialVersionUID = 1L;

	@Override
	public void processDeepLink() throws IOException {
		final ParameterProcessResults[] results = new ParameterProcessResults[] { new ParameterProcessResults() };
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				processPerspectiveOpen(results);
				IConfigurationElement[] congfigElementsFromRegistry = getCongfigElementsFromRegistry(PlatformUI.PLUGIN_ID, "perspectives");
				findInstanceHandlerAndExecuteCallback(results, congfigElementsFromRegistry, getParameterMap(), PERSPECTIVE_ID_ATTRIBUTE);
			}
		});
		outputResponse(results[0]);
	}

	private void processPerspectiveOpen(final ParameterProcessResults[] results) {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IPreferenceStore store = PlatformUI.getPreferenceStore();
		final ILog logger = Activator.getDefault().getLog();

		// NOTE: method delegation to enable dependency injection/testing
		results[0].loaded = openPerspective(getHandlerId(), ResourcesPlugin.getWorkspace(), workbench, store, logger);
	}

	boolean openPerspective(final String perspId, final IAdaptable input, final IWorkbench workbench, final IPreferenceStore store, final ILog logger) {
		try {
			// Verify the requested perspective ID
			IPerspectiveRegistry reg = workbench.getPerspectiveRegistry();
			IPerspectiveDescriptor perspective = reg.findPerspectiveWithId(perspId);
			if (perspective == null) {
				logger.log(statusFactory.error("Unable to open perspective: " + perspId));
				return false;
			}

			// Get "Open Behavior" preference.
			String pref = store.getString(IWorkbenchPreferenceConstants.OPEN_NEW_PERSPECTIVE);

			// Implement open behavior.
			if (pref.equals(IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_WINDOW)) {
				workbench.openWorkbenchWindow(perspId, input);
			} else if (pref.equals(IWorkbenchPreferenceConstants.OPEN_PERSPECTIVE_REPLACE)) {
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage activePage = window.getActivePage();
				if (activePage != null) {
					activePage.setPerspective(perspective);
				} else {
					window.openPage(perspId, input);
				}
			}

			
			Shell shell = workbench.getActiveWorkbenchWindow().getShell();
			shell.setMinimized(false);
			shell.forceActive();
			logger.log(statusFactory.info("Opened perspective: " + perspId));
			return true;
		} catch (WorkbenchException e) {
			logger.log(statusFactory.error("Error opening perspective: " + e.getMessage(), e));
			return false;
		}
	}

}