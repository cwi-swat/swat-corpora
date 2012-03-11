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
package org.eclipse.e4.enterprise.installer.ui.swt;

import java.io.File;
import java.net.URL;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.enterprise.installer.Activator;
import org.eclipse.e4.enterprise.installer.BundleUpdater;
import org.eclipse.e4.enterprise.installer.FeatureVersionedIdentifier;
import org.eclipse.e4.enterprise.installer.InstallError;
import org.eclipse.ui.PlatformUI;

/**
 * A Runnable intended to start an update after a certain number of minutes have
 * elapsed.
 */
class BackgroundUpdater implements Runnable {
	private URL[] updateURL;
	private File downloadDirectoryRoot;
	private int minutes;
	private Set<FeatureVersionedIdentifier> featuresToProvision = null; 
	
	public BackgroundUpdater(URL[] updateURL, File downloadDirectoryRoot, int minutes) {
		this.updateURL = updateURL;
		this.downloadDirectoryRoot = downloadDirectoryRoot;
		this.minutes = minutes;
	}
	
	public BackgroundUpdater(URL[] updateURL, File downloadDirectoryRoot, Set<FeatureVersionedIdentifier> featuresToProvision, int minutes) {
		this.updateURL = updateURL;
		this.downloadDirectoryRoot = downloadDirectoryRoot;
		this.minutes = minutes;
		this.featuresToProvision = featuresToProvision;
	}
	
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000*60 * minutes);  // update every x minutes
			} catch (InterruptedException e) {
				// Application is closing; return
				return;
			}
			Job updateJob = new Job("Updating application") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						BundleUpdater bundleUpdater = new BundleUpdater(monitor);
						boolean restartRequired = update(bundleUpdater);
						if (restartRequired) {
							PlatformUI.getWorkbench().restart();
						}						
						return Status.OK_STATUS;
					} catch (InstallError e) {
						return new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Failed to execute scheduled update",e);
					}
				}

				private boolean update(BundleUpdater bundleUpdater)
						throws InstallError {
					if (useConfigurationFileForProvisioning() ) {
						return bundleUpdater.update(updateURL, downloadDirectoryRoot);
					} else {
						return bundleUpdater.update(updateURL, downloadDirectoryRoot, featuresToProvision);
					}
				}

				private boolean useConfigurationFileForProvisioning() {
					return featuresToProvision == null;
				}
			};
			updateJob.schedule();
		}
	}
	
}

