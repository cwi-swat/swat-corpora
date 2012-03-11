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
 * Null object pattern implementation of an installation site.
 */
public class NullInstallationSite implements InstallationSite {
	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#addToConfiguredSites()
	 */
	public void addToConfiguredSites() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#hasIdenticalFeatures(java.util.List)
	 */
	public boolean hasIdenticalFeatures(List<IFeatureReference> updateSiteFeatures) throws InstallError {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#install(org.eclipse.update.core.IFeatureReference[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean install(IFeatureReference[] updateSiteFeatures, IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#removeFromConfiguredSites()
	 */
	public void removeFromConfiguredSites() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#convertIntoUpdateSite()
	 */
	public void convertIntoUpdateSite() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#getInstalledFeatures()
	 */
	public IFeatureReference[] getInstalledFeatures() {
		return new IFeatureReference[] {};
	}
}