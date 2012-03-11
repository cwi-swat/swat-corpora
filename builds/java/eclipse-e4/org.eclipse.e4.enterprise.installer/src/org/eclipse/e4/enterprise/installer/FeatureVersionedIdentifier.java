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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.VersionedIdentifier;

/**
 * A lightweight IFeatureReference replacement with proper 
 * {@link #equals(Object)} and {@link #hashCode()} implementations.
 */
public class FeatureVersionedIdentifier {
	public final String featureID;
	public final String featureVersion;

	public FeatureVersionedIdentifier(IFeatureReference featureRef) throws  InstallError
	{
//		this(featureRef.getVersionedIdentifier().getIdentifier(), featureRef.getVersionedIdentifier().getVersion().toString());
		try {
			VersionedIdentifier versionedIdentifier = featureRef.getVersionedIdentifier();
			this.featureID = versionedIdentifier.getIdentifier();
			this.featureVersion = versionedIdentifier.toString().substring(featureID.length() + 1);
		} catch (CoreException e) {
			throw new InstallError(e);
		}
	}
	
	/**
	 * Note: feature version must comply with valid plugin version identifiers.
	 * 
	 * @see org.eclipse.core.runtime.PluginVersionIdentifier
	 * 
	 * @param featureID
	 * @param featureVersion
	 */
	public FeatureVersionedIdentifier(String featureID, String featureVersion) {
		this.featureID = featureID;
		this.featureVersion = featureVersion;

		if (!PluginVersionIdentifier.validateVersion(featureVersion).isOK()) {
			throw new IllegalArgumentException("invalid version: [" + featureVersion + "]");
		}
	}

	@Override
	public String toString() {
		return "featureID :[" + featureID + "] featureVersion: [" + featureVersion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureID == null) ? 0 : featureID.hashCode());
		result = prime * result + ((featureVersion == null) ? 0 : featureVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureVersionedIdentifier other = (FeatureVersionedIdentifier) obj;
		if (featureID == null) {
			if (other.featureID != null)
				return false;
		} else if (!featureID.equals(other.featureID))
			return false;
		if (featureVersion == null) {
			if (other.featureVersion != null)
				return false;
		} else if (!featureVersion.equals(other.featureVersion))
			return false;
		return true;
	}
}