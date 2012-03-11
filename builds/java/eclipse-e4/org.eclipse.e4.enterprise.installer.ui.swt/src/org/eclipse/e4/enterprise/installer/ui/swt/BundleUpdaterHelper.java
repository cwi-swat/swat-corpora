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

import static org.eclipse.e4.enterprise.installer.BundleUpdaterConfig.findDownloadDirectoryRoot;
import static org.eclipse.e4.enterprise.installer.BundleUpdaterConfig.retrieveUpdateSiteFromConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.enterprise.installer.BundleUpdater;
import org.eclipse.e4.enterprise.installer.FeatureVersionedIdentifier;
import org.eclipse.e4.enterprise.installer.InstallError;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * Supplies API implementing the most common enterprise update use-cases on top
 * of the installer's BundleUpdater.
 */
public class BundleUpdaterHelper {
	protected BundleUpdater bundleUpdater;

	public BundleUpdaterHelper() {
		bundleUpdater = new BundleUpdater();
	}

	/**
	 * Runs an update operation using the update site defined in the config
	 * service (by key org.eclipse.e4.enterprise.installer.UPDATE_SITE) within a
	 * progress dialog so that a user can see updates are occurring
	 * 
	 * @return UpdateStatus represent success/failure and containing an
	 *         exception if something failed
	 */
	public UpdateStatus updateWithProgressDialog() throws InstallError {
		return updateWithProgressDialog(retrieveUpdateSiteFromConfig(), findDownloadDirectoryRoot());
	}

	/**
	 * getInstalledFeatures retrieves you the list of installed features as
	 * Eclipse sees it. It is often worth using this method and logging the list
	 * of features before and after update operations
	 * 
	 * @return List of strings with all the installed feature names
	 * @throws InstallError
	 *             if an exception occurs accessing Eclipse update manager APIs
	 */
	public List<String> getInstalledFeatures() throws InstallError {
		return BundleUpdater.getInstalledFeatures();
	}

	/**
	 * Runs an update operation using the update site passed in within a
	 * progress dialog so that a user can see updates are occurring
	 * 
	 * @return UpdateStatus represent success/failure and containing an
	 *         exception if something failed
	 */
	public UpdateStatus updateWithProgressDialog(final URL updateURL, final File downloadRootDir) {
		return updateWithProgressDialog(new URL[] { updateURL }, downloadRootDir);
	}

	/**
	 * Runs an update operation using the update site, download root directory, and set of provisioned features passed in within a
	 * progress dialog so that a user can see updates are occurring
	 * 
	 * @return UpdateStatus represent success/failure and containing an
	 *         exception if something failed
	 */
	public UpdateStatus updateWithProgressDialog(final URL[] updateURL, final File downloadRootDir, final Set<FeatureVersionedIdentifier> featuresToProvision) {
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(new Shell(Display.getDefault(), SWT.NULL));
		final UpdateStatus[] result = new UpdateStatus[] { null };

		try {
			progressDialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						BundleUpdater bundleUpdater = new BundleUpdater(monitor);
						boolean restartRequired = bundleUpdater.update(updateURL, downloadRootDir, featuresToProvision);
						if(restartRequired) {
							result[0] = new UpdateStatus(UpdateResult.SUCCESS_RESTART_REQUIRED);
						} else {
							result[0] = new UpdateStatus(UpdateResult.SUCCESS_RESTART_NOT_REQUIRED);
						}						
					} catch (InstallError e) {
						result[0] = new UpdateStatus(UpdateResult.FAILURE_INSTALL_ERROR, e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			result[0] = new UpdateStatus(UpdateResult.FAILURE_INSTALL_ERROR, e);
		} catch (InterruptedException e) {
			// ProgressMonitorDialog indicates a cancel via
			// InterruptedException.
			result[0] = new UpdateStatus(UpdateResult.FAILURE_USER_CANCELLED);
		}

		return result[0];
	}
	
	/**
	 * Runs an update operation using the update site passed in within a
	 * progress dialog so that a user can see updates are occurring
	 * 
	 * @return UpdateStatus represent success/failure and containing an
	 *         exception if something failed
	 */
	public UpdateStatus updateWithProgressDialog(final URL[] updateURL, final File downloadRootDir) {
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(new Shell(Display.getDefault(), SWT.NULL));
		final UpdateStatus[] result = new UpdateStatus[] { null };

		try {
			progressDialog.run(true, false, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						result[0] = new BundleUpdater(monitor).update(updateURL, downloadRootDir) ? new UpdateStatus(UpdateResult.SUCCESS_RESTART_REQUIRED) : new UpdateStatus(UpdateResult.SUCCESS_RESTART_NOT_REQUIRED);
					} catch (InstallError e) {
						result[0] = new UpdateStatus(UpdateResult.FAILURE_INSTALL_ERROR, e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			result[0] = new UpdateStatus(UpdateResult.FAILURE_INSTALL_ERROR, e);
		} catch (InterruptedException e) {
			// ProgressMonitorDialog indicates a cancel via
			// InterruptedException.
			result[0] = new UpdateStatus(UpdateResult.FAILURE_USER_CANCELLED);
		}

		return result[0];
	}

	/**
	 * Runs an update operation in a background thread (via an Eclipse Job)
	 * every number of minutes that you specify pulling the update site to poll
	 * and the download root directory (where downloaded features/plug-ins are
	 * placed) from the configuration service or will be in your install
	 * directory if not specified
	 * 
	 * @param minutes
	 *            Check for updates every x minutes
	 * @return Background thread uses to schedule update jobs
	 */
	public Thread updateOnScheduleInBackgroundThread(int minutes) throws InstallError {
		return updateOnScheduleInBackgroundThread(retrieveUpdateSiteFromConfig(), findDownloadDirectoryRoot(), minutes);
	}

	/**
	 * Runs an update operation in a background thread (via an Eclipse Job)
	 * every number of minutes that you specify pulling the update site to poll
	 * and the download root directory (where downloaded features/plug-ins are
	 * placed) from the configuration service or will be in your install
	 * directory if not specified
	 * 
	 * @param updateURL The update site URL
	 * @param downloadDirectoryRoot The directory in which to create the update site.
	 * @param minutes
	 *            Check for updates every x minutes
	 * @return Background thread uses to schedule update jobs
	 */
	public Thread updateOnScheduleInBackgroundThread(URL updateURL, File downloadDirectoryRoot, int minutes) {
		return updateOnScheduleInBackgroundThread(new URL[] { updateURL }, downloadDirectoryRoot, minutes);
	}
	
	/**
	 * Runs an update operation in a background thread (via an Eclipse Job)
	 * every number of minutes that you specify pulling the update site to poll
	 * and the download root directory (where downloaded features/plug-ins are
	 * placed) from the configuration service or will be in your install
	 * directory if not specified
	 * 
	 * @param updateURL The URLs identifying update sites from where to get updates.
	 * @param downloadDirectoryRoot The directory in which to create the update site.
	 * @param minutes
	 *            Check for updates every x minutes
	 * @return Background thread uses to schedule update jobs
	 */
	public Thread updateOnScheduleInBackgroundThread(URL[] updateURL, File downloadDirectoryRoot, int minutes) {
		Thread updateThread = new Thread(new BackgroundUpdater(updateURL, downloadDirectoryRoot, minutes));
		updateThread.setName("Background updater");
		updateThread.setDaemon(true);
		updateThread.start();
		return updateThread;
	}

	/**
	 * Runs an update operation in a background thread (via an Eclipse Job)
	 * every number of minutes that you specify pulling the update site to poll, 
	 * the download root directory (where downloaded features/plug-ins are
	 * placed) from the configuration service or will be in your install
	 * directory if not specified, and the set of features to be provisioned.
	 * 
	 * @param updateURL The URLs identifying update sites from where to get updates.
	 * @param downloadDirectoryRoot The directory in which to create the update site.
	 * @param set of features to be provisioned
	 * @param minutes Check for updates every x minutes
	 * @return Background thread uses to schedule update jobs
	 */
	public Thread updateOnScheduleInBackgroundThread(URL[] updateURL, File downloadDirectoryRoot, 
			Set<FeatureVersionedIdentifier> featuresToProvision, int minutes) {
		
		Thread updateThread = new Thread(new BackgroundUpdater(updateURL, downloadDirectoryRoot, featuresToProvision, minutes));
		updateThread.setName("Background updater");
		updateThread.setDaemon(true);
		updateThread.start();
		
		return updateThread;
	}
	
	/**
	 * Possible results from update operations
	 */
	public enum UpdateResult {
		SUCCESS_RESTART_NOT_REQUIRED, FAILURE_USER_CANCELLED, SUCCESS_RESTART_REQUIRED, FAILURE_INSTALL_ERROR;
	}

	/**
	 * Encapsulates the result of an update operation and possibly an exception
	 */
	public static class UpdateStatus {
		public UpdateResult result;

		public Exception failureException;

		public UpdateStatus(UpdateResult result) {
			this.result = result;
		}

		public UpdateStatus(UpdateResult result, Exception failureException) {
			this.result = result;
			this.failureException = failureException;
		}
	}
}
