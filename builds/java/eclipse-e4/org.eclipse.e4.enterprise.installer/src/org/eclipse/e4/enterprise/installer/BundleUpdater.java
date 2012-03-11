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
package org.eclipse.e4.enterprise.installer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.enterprise.installer.internal.FeatureReferenceTree;
import org.eclipse.e4.enterprise.installer.internal.site.InstallationSite;
import org.eclipse.e4.enterprise.installer.internal.site.InstallationSiteManager;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.IInstallConfiguration;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.SiteManager;
import org.eclipse.update.core.VersionedIdentifier;

import static org.eclipse.e4.enterprise.installer.BundleUpdaterConfig.findDownloadDirectoryRoot;
import static org.eclipse.e4.enterprise.installer.BundleUpdaterConfig.retrieveUpdateSiteFromConfig;
import static org.eclipse.e4.enterprise.installer.BundleUpdaterConfig.retrieveProvisioningFromConfig;


/**
 * {@link BundleUpdater} and {@link BundleUpdaterHelper} are the main entry
 * points into the installer API.  BundleUpdater is a slightly lower-level API
 * and may be used to tweak operation more closely.  BundleUpdaterHelper 
 * implements the most common use-cases directly. 
 */
public class BundleUpdater {

	private static final String LATEST_VERSION = "0.0.0";
	private IProgressMonitor progressMonitor = null;
	
	private ILog logger = null;
	
	/**
	 * Construct a BundleUpdater that will not display an IProgressMonitor.
	 */
	public BundleUpdater() {
		logger = Activator.getDefault().getLog();
	}

	/**
	 * Construct a BundleUpdater that will report progress to the user via
	 * the specified {@link IProgressMonitor}.
	 * 
	 * @param progressMonitor The {@link IProgressMonitor} to specify.
	 */
	public BundleUpdater(IProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
		logger = Activator.getDefault().getLog();
	}

	/**
	 * getInstalledFeatures retrieves you the list of installed features as
	 * Eclipse sees it. It is often worth using this method and logging the list
	 * of features before and after update operations. Note this is both
	 * configured and non-configured features
	 * 
	 * @see IConfiguredSite#getFeatureReferences()
	 * 
	 * @return List of strings with all the installed feature names
	 */
	public static List<String> getInstalledFeatures() {
		try {
			List<String> result = new ArrayList<String>();
			IInstallConfiguration config = SiteManager.getLocalSite().getCurrentConfiguration();
			IConfiguredSite[] sites = config.getConfiguredSites();
			for (IConfiguredSite configuredSite : sites) {
				IFeatureReference[] featureReferences = configuredSite.getFeatureReferences();
				for (IFeatureReference featureReference : featureReferences) {
					VersionedIdentifier versionedIdentifier = featureReference.getVersionedIdentifier();
					result.add(versionedIdentifier.getIdentifier() + versionedIdentifier.getVersion());
				}
			}
			return result;
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * getInstalledFeatures retrieves you the list of installed features as
	 * Eclipse sees it. It is often worth using this method and logging the list
	 * of features before and after update operations. Note this is only
	 * configured features; ie: old versions still on disk that are no not  
	 * configured for loading will not be included.
	 * 
	 * @see IConfiguredSite#getConfiguredFeatures()
	 * 
	 * @return List of strings with all the installed feature names
	 */
	public static List<String> getConfiguredFeatures() {
		try {
			List<String> result = new ArrayList<String>();
			IInstallConfiguration config = SiteManager.getLocalSite().getCurrentConfiguration();
			IConfiguredSite[] sites = config.getConfiguredSites();
			for (IConfiguredSite configuredSite : sites) {
				IFeatureReference[] featureReferences = configuredSite.getConfiguredFeatures();
				for (IFeatureReference featureReference : featureReferences) {
					VersionedIdentifier versionedIdentifier = featureReference.getVersionedIdentifier();
					result.add(versionedIdentifier.getIdentifier() + versionedIdentifier.getVersion());
				}
			}
			return result;
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Method update. Connects to the update site specified in your
	 * application's configuration file by the
	 * org.eclipse.e4.enterprise.installer.UPDATE_SITE property and places
	 * downloaded features/plug-ins in to the directory specified by the
	 * org.eclipse.e4.enterprise.installer.DOWNLOAD_ROOT property or if this isn't
	 * specified in your install location, in the default download folder next
	 * to your .exe.
	 * 
	 * @return true if a restart is recommended by the platform and false
	 *         otherwise
	 * @throws InstallError
	 *             If an error requiring logging or user notification is
	 *             detected.
	 */
	public boolean update() throws InstallError {
		return update(retrieveUpdateSiteFromConfig(), findDownloadDirectoryRoot(), retrieveProvisioningFromConfig());
	}
	
	/**
	 * Method update. Update the current configuration to the configuration on
	 * the specified update. Both up and downgrades are performed.
	 * 
	 * @param updateSite
	 *            The URL for the update site location
	 * @return true if a restart is recommended by the platform and false
	 *         otherwise
	 * @throws InstallError
	 *             If an error requiring logging or user notification is
	 *             detected.
	 * @deprecated Use update() or update(URL, File) instead. Retained for
	 *             API compatibility.
	 */
	public boolean update(String updateSite) throws InstallError {
		try {
			return update(new URL[] { new URL(updateSite) }, findDownloadDirectoryRoot(), retrieveProvisioningFromConfig());
		} catch (MalformedURLException e) {
			throw new InstallError(new Status(Status.ERROR, Activator.PLUGIN_ID, "updateSite is not a legal URL: " + updateSite, e), e);
		}
	}

	/**
	 * Method update. Update the current configuration to the configuration on
	 * the specified update placing any downloded features/plug-ins in the
	 * downloadRootDir. Both up and downgrades are performed.
	 * 
	 * @param updateSiteURL
	 *            The URL for the update site location
	 * @param downloadRootDir
	 *            The directory where downloaded features/plug-ins are placed
	 * @return true if a restart is recommended by the platform and false
	 *         otherwise
	 * @throws InstallError
	 *             If an error requiring logging or user notification is
	 *             detected.
	 */
	public boolean update(URL updateSiteURL, File downloadRootDir) throws InstallError {
		return update(new URL[] { updateSiteURL }, downloadRootDir);
	}

	/**
	 * Method update. Update the current configuration to the configuration on
	 * the specified update sites placing any downloded features/plug-ins in the
	 * downloadRootDir. Both up and downgrades are performed. Uses the union of
	 * the features on all the update sites.
	 * 
	 * @param updateSiteURLs
	 *            Array of URLs for the update site locations
	 * @param downloadRootDir
	 *            The directory where downloaded features/plug-ins are placed
	 * @return true if a restart is recommended by the platform and false
	 *         otherwise
	 * @throws InstallError
	 *             If an error requiring logging or user notification is
	 *             detected.
	 */
	public boolean update(URL[] updateSiteURLs, File downloadRootDir) throws InstallError {
		return update(updateSiteURLs, downloadRootDir, retrieveProvisioningFromConfig());
	}

	/*
	 * Terminology :
	 * 
	 * U = union operator n = intersection operator
	 * 
	 * F(p) = featureIDsToProvision
	 * 
	 * Expressing the install operations as sets of IFeatureReferences where:
	 * 
	 * updatesite[R] = the set of all features that are available on remote
	 * update sites
	 * 
	 * updatesite[R] = updateSite[0] U updateSite[1] U ... U updateSite[n]
	 * 
	 * and
	 * 
	 * F(p) MUST be a subset of updatesite[R]
	 * 
	 * Let updatesite[L] = The current local download site turned into an update
	 * site. This is useful so that we can avoid downloading things we already
	 * have locally; i.e. an optimization.
	 * 
	 * Then:
	 * 
	 * featureReferencesToInstall = the work we need to do
	 * 
	 * featureReferencesToInstall = (updatesite[L] U (updatesite[R] - updatesite[L])) n F(p)
	 * 
	 * - Distribute F(p) over the U operation:
	 * 
	 * featureReferencesToInstall = (updatesite[L] n F(p) U (updatesite[R] - updatesite[L] n F(p) ))
	 * 
	 * - Perform extract local variable refactorings:
	 * 
	 * Let featuresWeWantFromRemoteSites = updatesite[R] n featureIDsToProvision
	 * 
	 * Let featuresWeWantFromLocalSite = updatesite[L] n featureIDsToProvision
	 * 
	 * Then:
	 * 
	 * featureReferencesToInstall = featuresWeWantFromLocalSite +
	 * (featuresWeWantFromRemoteSites - featuresWeWantFromLocalSite)
	 */

	/**
	 * Method update. Update the current configuration to that specified in the
	 * Set "featureIDsToProvision", placing any down-loaded features/plug-ins in
	 * the downloadRootDir. Both up and downgrades are performed.
	 * 
	 * All features in the Set must exist in one of the update sites defined by
	 * the url[]; InstallError is thrown otherwise.
	 * 
	 * Note: if a feature with the correct version already exists locally (i.e.
	 * has previously been installed) then this will NOT be down-loaded again.
	 * Instead, the previously downloaded version will be re-used.
	 * 
	 * @param updateSiteURLs
	 *            Array of URLs for the update site locations
	 * @param downloadRootDir
	 *            The directory where downloaded features/plug-ins are placed
	 * @param featureIDsToProvision
	 *            The target set of features to find and install from the update
	 *            sites.
	 * @return true if a restart is recommended by the platform and false
	 *         otherwise
	 * @throws InstallError
	 *             If an error requiring logging or user notification is
	 *             detected.
	 */
	public boolean update(URL[] updateSiteURLs, File downloadRootDir, Set<FeatureVersionedIdentifier> featuresRequested) throws InstallError {
		
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Update started..."));
		
		InstallationSiteManager siteManager = initializeSite(downloadRootDir);
		if (onRestart(siteManager)) {
			// on restart, the siteManager will be null
			logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "this is a restart, so exit update(...)"));
			return false;
		}

		InstallationSite currentLocalDownloadSite = siteManager.find();		
		// TODO: BUG! When we get all updates, we need to include the URL of the DLR as well, otherwise resolveVersionNumbersAsFVISet(...) 
		// will fail to remove older versions from the local update site.		
		FeatureReferenceTree allRemoteFeaturesReferences = getAllFeaturesFromUpdateSites(updateSiteURLs);
		
		Set<FeatureVersionedIdentifier> featureIDsToProvision = 
			setFeaturesToProvisionToBeOfAllFeaturesOnRemoteSitesIfUnspecified(featuresRequested, allRemoteFeaturesReferences);

		// Let featuresWeWantFromRemoteSites = updatesite[R] n F(p) (n = intersection) : this might be out of date now
		Set<FeatureVersionedIdentifier> fviRequestedWithExactVersions = resolveVersionNumbersAsFVISet(allRemoteFeaturesReferences, featureIDsToProvision, true);
		
		// Enrich the Set<FVI> being requested for provision: put children on any nested features
		addChildrenOfRequestedFeatures(fviRequestedWithExactVersions, allRemoteFeaturesReferences);		
		
		// Convert FVIs to IFRs
		List<IFeatureReference> ifrWeWantFromRemoteSites = new ArrayList<IFeatureReference>();
		for (FeatureVersionedIdentifier fvi : fviRequestedWithExactVersions) {			
			ifrWeWantFromRemoteSites.add(allRemoteFeaturesReferences.getFeatureReference(fvi));
		}
		
		if (currentLocalDownloadSite.hasIdenticalFeatures(ifrWeWantFromRemoteSites)) {
			logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Feature sets IDENTICAL, no work to do!"));
			logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exiting update(...)"));
			return false;
		}
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Feature sets are DIFFERENT, we have work to do!"));

		
		// Let featuresAvailableInLocalSite = updatesite[L] n F(p)
		List<IFeatureReference> allLocalFeatures = Arrays.asList(currentLocalDownloadSite.getInstalledFeatures());
		List<IFeatureReference> featuresWeWantFromLocalSite = resolveVersionNumbersAsIFRList(allLocalFeatures, featureIDsToProvision, false);

		// featureReferencesToInstall = featuresWeWantFromLocalSite +
		// (featuresWeWantFromRemoteSites - featuresWeWantFromLocalSite)
		List<IFeatureReference> featureReferencesToInstall = calculateFeaturesReferencesToInstall(ifrWeWantFromRemoteSites, featuresWeWantFromLocalSite);

		boolean restartNeeded = installAndRollBackOnError(siteManager, currentLocalDownloadSite, featureReferencesToInstall);

		// NB: old / no longer latest config sites are deleted on restart
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "Exiting updater normally, restartNeeded == "+restartNeeded));
		return restartNeeded;
	}
	
	
	List<IFeatureReference> calculateFeaturesReferencesToInstall(List<IFeatureReference> featuresWeWantFromRemoteSites, List<IFeatureReference> featuresWeWantFromLocalSite) {		
		// Preserving the order/contents of the lists passed in
		List<IFeatureReference> remoteFeatures = new ArrayList<IFeatureReference>(featuresWeWantFromRemoteSites);
		List<IFeatureReference> localFeatures = new ArrayList<IFeatureReference>(featuresWeWantFromLocalSite);
		
		remoteFeatures.removeAll(localFeatures);
		localFeatures.addAll(remoteFeatures);
		
		return localFeatures;
	}

	Set<FeatureVersionedIdentifier> setFeaturesToProvisionToBeOfAllFeaturesOnRemoteSitesIfUnspecified(
			Set<FeatureVersionedIdentifier> featureIDsToProvision, FeatureReferenceTree allFeaturesFromRemoteUpdateSites) throws InstallError {
		// if no provisioning defined, define as set of all features from remote
		// update sites.
		if (featureIDsToProvision == null) {
			featureIDsToProvision = new HashSet<FeatureVersionedIdentifier>();
			for (IFeatureReference featureReference : allFeaturesFromRemoteUpdateSites.getAllReferences()) {
				featureIDsToProvision.add(new FeatureVersionedIdentifier(featureReference));
			}
		}
		return featureIDsToProvision;
	}
	
	List<IFeatureReference> resolveVersionNumbersAsIFRList( List<IFeatureReference> featuresToLookIn, 
			Set<FeatureVersionedIdentifier> featuresToProvision, 
			boolean allFeaturesToProvisionMustBePresent) throws InstallError {

		List<IFeatureReference> result = new ArrayList<IFeatureReference>();
		
		for (FeatureVersionedIdentifier fvi : featuresToProvision) {
			IFeatureReference item = findAppropriateFeature(featuresToLookIn, fvi);			
			
			if(item == null && allFeaturesToProvisionMustBePresent) {
				throw new InstallError("Could not find: "+fvi.toString());
			}
			
			if(item != null) {
				result.add(item);
			}
		}
								
		return result;
	}
	
	Set<FeatureVersionedIdentifier> resolveVersionNumbersAsFVISet( FeatureReferenceTree tree, 
			Set<FeatureVersionedIdentifier> featuresToProvision, 
			boolean allFeaturesToProvisionMustBePresent) throws InstallError {

		Set<FeatureVersionedIdentifier> result = new HashSet<FeatureVersionedIdentifier>();
		
		for (FeatureVersionedIdentifier fvi : featuresToProvision) {
			IFeatureReference item = findAppropriateFeature(tree.getAllReferences(), fvi);
			if(item == null && allFeaturesToProvisionMustBePresent) {
				throw new InstallError("Could not find: "+fvi.toString());
			}
			if(item != null) {
				result.add(new FeatureVersionedIdentifier(item));
			}
		}
								
		return result;
	}

	private IFeatureReference findAppropriateFeature(List<IFeatureReference> featuresToLookIn, FeatureVersionedIdentifier fvi)
			throws InstallError {
		IFeatureReference item;
		if( LATEST_VERSION.equals(fvi.featureVersion)) {
			item = findHighestMatchingFeature(featuresToLookIn, fvi);
		} else {
			item = findFirstMatchingFeatureVersion(featuresToLookIn, fvi);
		}
		return item;
	}
	
	private IFeatureReference findFirstMatchingFeatureVersion(List<IFeatureReference> featuresListToPrune,
			FeatureVersionedIdentifier fvi) throws InstallError {
						
		for (IFeatureReference featureReference : featuresListToPrune) {
			FeatureVersionedIdentifier current = new FeatureVersionedIdentifier(featureReference);
			if( fvi.equals(current)) {
				return featureReference;
			}
		}
		return null;
	}

	private IFeatureReference findHighestMatchingFeature(List<IFeatureReference> featuresListToPrune,
			FeatureVersionedIdentifier fvi) throws InstallError {
		
		IFeatureReference highestVersion = null;
		String targetFeature = fvi.featureID;
				
		for (IFeatureReference featureReference : featuresListToPrune) {
			FeatureVersionedIdentifier current = new FeatureVersionedIdentifier(featureReference);
			if( targetFeature.equals(current.featureID)) {
				if( highestVersion==null || isGreaterThan(featureReference, highestVersion) ) {
					highestVersion = featureReference; 
				}				
			}
		}
		
		return highestVersion;
	}

	private boolean isGreaterThan(IFeatureReference a,
			IFeatureReference b) throws InstallError {
		try {
			return a.getVersionedIdentifier().getVersion().isGreaterThan(b.getVersionedIdentifier().getVersion());
		} catch (CoreException e) {			
			e.printStackTrace();
			throw new InstallError(e);
		}
	}

	
	
	private boolean onRestart(InstallationSiteManager siteManager) {
		// on restart, the siteManager will be null
		return siteManager == null;
	}
	
//	private boolean isInstallable(Set<FeatureVersionedIdentifier> featuresWeCanInstall, FeatureVersionedIdentifier feature) {
//		FeatureVersionedIdentifier duplicateFeatureVersion000 = new FeatureVersionedIdentifier(feature.featureID, LATEST_VERSION);
//		return (featuresWeCanInstall.remove(feature) | featuresWeCanInstall.remove(duplicateFeatureVersion000));
//	}

	private InstallationSiteManager initializeSite(File downloadRootDir) {
		InstallationSiteManager siteManager;
		siteManager = new InstallationSiteManager(downloadRootDir);

		// if we are on a restart then signal restartCompleted and clean up
		// the old installations and return from install method
		if (siteManager.isOnRestart()) {
			siteManager.restartCompleted();
			siteManager.cleanUp();
			return null;
		}
		return siteManager;
	}

	private InstallationSite createAndConfigureNewDownloadSite(InstallationSiteManager siteManager) throws InstallError {
		InstallationSite newDownloadSite = null;
		try {
			newDownloadSite = siteManager.create();
			newDownloadSite.addToConfiguredSites();
		} catch (CoreException e) {
			throw new InstallError(e);
		}
		return newDownloadSite;
	}

	boolean installAndRollBackOnError(InstallationSiteManager siteManager, InstallationSite currentSite, List<IFeatureReference> featuresFromUpdateSiteList) throws InstallError {
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "InstallAndRollBackOnError() started..."));
		
		boolean restartNeeded;
		IFeatureReference[] featuresFromUpdateSite = featuresFromUpdateSiteList.toArray(new IFeatureReference[featuresFromUpdateSiteList.size()]);
			
		InstallationSite newSite = beginNewSiteTransaction(siteManager, currentSite);
		try {
			restartNeeded = newSite.install(featuresFromUpdateSite, progressMonitor);

			if (restartNeeded) {
				siteManager.restartInitiated();
			}
		}catch (RuntimeException e) {  // alleged best practice is to not try to alter unchecked exceptions (Runtimes and Errors).
			rollback(newSite, currentSite);
			throw e;
		} 
		catch (Error e) {
			rollback(newSite, currentSite);
			throw e;
		}
		catch (Throwable t) {    // this includes checked (non runtime) Exceptions
			rollback(newSite, currentSite);
			throw new InstallError(t);
		}
		
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, "InstallAndRollBackOnError() exiting, restartNeeded == "+restartNeeded));
		return restartNeeded;
	}

	private InstallationSite beginNewSiteTransaction(
			InstallationSiteManager siteManager, InstallationSite currentSite)
			throws InstallError {
		InstallationSite newSite = createAndConfigureNewDownloadSite(siteManager);
		currentSite.removeFromConfiguredSites();
		return newSite;
	}

	private void rollback(InstallationSite newSite, InstallationSite currentSite) {
		logger.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Rollback called!"));
		// NB: the newSite still exists on disc even though it is removed from
		// the ConfiguredSites.
		// This may be useful for debugging purposes. Maybe not... :)
		// it will be cleaned up on the next successful
		// installation (see RestartManager.restartCompleted() )
		currentSite.addToConfiguredSites();
		newSite.removeFromConfiguredSites();
	}

	
	FeatureReferenceTree getAllFeaturesFromUpdateSites(URL[] remoteUpdateSiteURLs) throws InstallError {
		FeatureReferenceTree result = new FeatureReferenceTree();
		
		for (URL url : remoteUpdateSiteURLs) {
			addFeaturesFromUrlToFeatureTree(result, url);
		}
		
		return result;
	}

	private void addFeaturesFromUrlToFeatureTree(FeatureReferenceTree result, URL url) throws InstallError {
		try {
			IFeatureReference[] featuresFromUpdateSite = getFeaturesFromUpdateSite(url);
			
			for (IFeatureReference featureReference : featuresFromUpdateSite) {
				result.add(featureReference);
			}
		} catch (CoreException e) {
			/* FIXME: djo: Changing this code makes the tests pass.  Before, we were
			 * synchronizing the current platform with the union of the set of features 
			 * available on all update sites.  However, if any update site is unavailable,
			 * we have no way to know if a feature that is no longer available on any
			 * remote site should be retained.  The behavior that the test expects is
			 * for the update operation to fail in this case, and the current set of
			 * features to be retained.
			 * 
			 * However, I'm not sure this is the correct behavior.  An argument could 
			 * be made that any PROD environment should have a redundant set of update 
			 * site servers so all features should always be available.
			 * 
			 * I have changed the behavior to conform to the behavior that we have
			 * tested for, and thus to the API contract that we have established.
			 * A reasonable question is if we should add API to enable updating through
			 * a set of redundant update site servers as described above.
			 */
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not get FeatureReferences from: "+url + "\n"+e.getMessage());
			logger.log(status);
			throw new InstallError(status, e);
		}
	}

	private IFeatureReference[] getFeaturesFromUpdateSite(URL remoteUpdateSiteURL) throws CoreException {
		logger.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "BundleUpdater.getFeaturesFromUpdateSite()"));
		return SiteManager.getSite(remoteUpdateSiteURL, false, progressMonitor).getFeatureReferences();
	}

	public void addChildrenOfRequestedFeatures(Set<FeatureVersionedIdentifier> requestedFeatures, FeatureReferenceTree tree) throws InstallError {
		
		List<FeatureVersionedIdentifier> result = new ArrayList<FeatureVersionedIdentifier>();
		
		for (FeatureVersionedIdentifier fvi : requestedFeatures) {
			
			List<IFeatureReference> references = tree.getFeatureReferenceWithDescendants(fvi);
			for (IFeatureReference feature : references) {
				result.add(new FeatureVersionedIdentifier(feature));
			}			
		}
		requestedFeatures.addAll(result);		
		
	}

}
