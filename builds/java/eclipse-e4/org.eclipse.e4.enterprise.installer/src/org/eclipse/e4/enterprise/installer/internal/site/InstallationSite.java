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
package org.eclipse.e4.enterprise.installer.internal.site;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.enterprise.installer.InstallError;
import org.eclipse.update.core.IFeatureReference;

/**
 * An abstraction representing a particular installation site.  The default
 * implementation is for classic update sites.  In the future, this could be
 * implemented for P2, Maven, etc... repos as well.
 */
public interface InstallationSite {

	/**
	 * Installs all features from a specific update site on this site
	 * @param progressMonitor An {@link IProgressMonitor} for displaying progress messages.
	 * @param updateSiteURL The remote repository URL.
	 * 
	 * @return true if an install/update occurred and the platform must restart; false otherwise.
	 * @throws CoreException if Something Bad happened.
	 * @throws InvocationTargetException if Something Else Bad happened. :-D
	 */
	public abstract boolean install(IFeatureReference[] updateSiteFeatures, IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException;

	/**
	 * Return true if the currently-configured Features are identical to the set
	 * of features represented by the List<IFeatureReference>, presumably having
	 * come from one or more remote repositories.
	 * 
	 * @param updateSiteFeatures
	 *            IFeatureReferences from the remote repo(s). Even though this
	 *            is represented as a List<IFeatureReference>, duplicate
	 *            IFeatureReferences are not allowed.
	 * 
	 * @return true if the set passed is identical to the set of
	 *         currently-configured Features.
	 * 
	 * @throws InstallError
	 *             if we can't properly query the set of currently-configured
	 *             Features.
	 */
	public abstract boolean hasIdenticalFeatures(List<IFeatureReference> updateSiteFeatures) throws InstallError;

	/**
	 * Add the specified (presumably local) site to the set of configured 
	 * sites from which Eclipse loads Features/Bundles.
	 */
	public abstract void addToConfiguredSites();

	/**
	 * Remove the specified (presumably local) site from the set of configured 
	 * sites from which Eclipse loads Features/Bundles.
	 */
	public abstract void removeFromConfiguredSites();

	/**
	 * Convert this site into a properly-formatted update site so that we
	 * can use install/update operations to copy its Features into another
	 * InstallationSite.
	 */
	public abstract void convertIntoUpdateSite();

	/**
	 * Return the set of features that are installed in this Site.
	 * 
	 * @return An IFeatureReference[] containing the set of features in this Site.
	 * @throws InstallError If an unexpected error occurred and we have to give up.
	 */
	public abstract IFeatureReference[] getInstalledFeatures() throws InstallError;

}