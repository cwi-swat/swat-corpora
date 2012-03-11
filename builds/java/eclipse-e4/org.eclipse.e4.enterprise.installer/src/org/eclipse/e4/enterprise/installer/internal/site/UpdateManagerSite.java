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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.enterprise.installer.BundleUpdater;
import org.eclipse.e4.enterprise.installer.InstallError;
import org.eclipse.e4.enterprise.installer.Activator;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.ILocalSite;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.SiteManager;
import org.eclipse.update.core.VersionedIdentifier;
import org.eclipse.update.operations.IBatchOperation;
import org.eclipse.update.operations.IInstallFeatureOperation;
import org.eclipse.update.operations.IOperationFactory;
import org.eclipse.update.operations.OperationsManager;


/**
 * An update manager-based implementation of an InstallationSite.
 */
public class UpdateManagerSite implements InstallationSite {

	private IConfiguredSite configuredSite;
	
	private ILog logger = null;
	
	private IOperationFactory batchOperationFactory;

	private ILocalSite localSite;	// Eclipse's container for 1..n configuredSites

	@SuppressWarnings("unused")
	private UpdateManagerSite() {
		logger = Activator.getDefault().getLog();
	}

	public UpdateManagerSite(IConfiguredSite installSite) {
		logger = Activator.getDefault().getLog();
		try {
			localSite = SiteManager.getLocalSite();
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
		batchOperationFactory = OperationsManager.getOperationFactory();
		this.configuredSite = installSite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#getInstalledFeatures()
	 */
	public IFeatureReference[] getInstalledFeatures() throws InstallError {
		URL url = configuredSite.getSite().getURL();
		try {
			return SiteManager.getSite(url, false, new NullProgressMonitor()).getFeatureReferences();
		} catch (CoreException e) {
			throw new InstallError(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.e4.enterprise.installer.internal.site.InstallationSite#install(org.eclipse.update.core.IFeatureReference[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean install(IFeatureReference[] updateSiteFeatures, IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException {
		List<String> featuresBeforeUpdate = BundleUpdater.getInstalledFeatures();

		logFeaturesList(featuresBeforeUpdate.iterator(), "install()...features before update:");
		
		IInstallFeatureOperation[] featureInstallOperations = computeUpdateOperations(updateSiteFeatures, configuredSite);

		// installFeatures does a validate in the Eclipse code and throws out a
		// CoreException with a wrapped IStatus to
		// tell you what the problem is
		installFeatures(featureInstallOperations, progressMonitor);

		List<String> featuresAfterUpdate = BundleUpdater.getInstalledFeatures();
		
		logFeaturesList(featuresAfterUpdate.iterator(), "install()...features after update:");
		
		return areFeaturesDifferent(featuresBeforeUpdate, featuresAfterUpdate);
	}

	class UpdateSiteFeatureXML {
		public String jarRelativePath = "";
		public String id = "";
		public String version = "";
		
		@Override
		public String toString() {
			return "<feature url=\"" + jarRelativePath + 
				"\" id=\"" + id + 
				"\" version=\"" + version + "\"/>\r\n";
		}
	}
	
	public void convertIntoUpdateSite() {
		IFeatureReference[] featureReferences = configuredSite.getFeatureReferences();
		UpdateSiteFeatureXML[] siteFeatures = computeUpdateSiteFeatureData(featureReferences);
		File installationDirectory = new File(configuredSite.getSite().getURL().getPath());
		createSiteXMLFile(siteFeatures, installationDirectory);
		jarUnjarredDirectories(new File(installationDirectory, "/features"));
		jarUnjarredDirectories(new File(installationDirectory, "/plugins"));
	}

	private void jarUnjarredDirectories(File rootDirectory) {
		File[] filesInDirectory = rootDirectory.listFiles();
		for (File fileOrSubdirectory : filesInDirectory) {
			if (fileOrSubdirectory.isDirectory()) {
				jarDirectory(fileOrSubdirectory);
			}
		}
	}

	private void jarDirectory(File subdirectory) {
	    try {
	        // Create the ZIP file
	        String outFilename = subdirectory.getAbsolutePath() + ".jar";
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

	        Path subdirectoryToZip = new Path(subdirectory.getAbsolutePath());
			jarSubdirectory(out, subdirectoryToZip, subdirectoryToZip.segmentCount());
	    
	        // Complete the ZIP file
	        out.close();
	    } catch (IOException e) {
	    	// FIXME: Is this what we want to do?
	    	throw new RuntimeException(e);
	    }
	}

	private void jarSubdirectory(ZipOutputStream out, IPath subdirectoryPath, int rootPathSegmentCount) throws IOException {
		byte[] buf = new byte[1024];
		File subdirectory = subdirectoryPath.toFile();
		String[] subdirectoryContents = subdirectory.list();
		
		for (String fileOrDirectoryName : subdirectoryContents) {
			File fileOrSubdirectory = new File(subdirectory, fileOrDirectoryName);
			IPath fileOrSubdirectoryPath = subdirectoryPath.append(fileOrDirectoryName);
			
			if (fileOrSubdirectory.isDirectory()) {
				jarSubdirectory(out, fileOrSubdirectoryPath, rootPathSegmentCount);
			} else {
				FileInputStream in = new FileInputStream(fileOrSubdirectory);
				
			    // Add ZIP entry to output stream.
				String zipRelativePath = fileOrSubdirectoryPath.removeFirstSegments(rootPathSegmentCount).toString();
			    out.putNextEntry(new ZipEntry(zipRelativePath));
	   
			    // Transfer bytes from the file to the ZIP file
			    int len;
			    while ((len = in.read(buf)) > 0) {
			        out.write(buf, 0, len);
			    }
	   
			    // Complete the entry
			    out.closeEntry();
			    in.close();
			}
		}
	}
	
	UpdateSiteFeatureXML[] computeUpdateSiteFeatureData(IFeatureReference[] featureReferences) {
		UpdateSiteFeatureXML[] result = new UpdateSiteFeatureXML[featureReferences.length];
		for (int i = 0; i < featureReferences.length; i++) {
			result[i] = computeFeatureXMLData(featureReferences[i]);
		}
		return result;
	}

	UpdateSiteFeatureXML computeFeatureXMLData(IFeatureReference currentFeatureReference) {
		UpdateSiteFeatureXML result = new UpdateSiteFeatureXML();
		try {
			String[] urlPathParts = currentFeatureReference.getURL().getPath().split("/");
			result.jarRelativePath = urlPathParts[urlPathParts.length-2] + "/" + urlPathParts[urlPathParts.length-1]+".jar";
			result.id = currentFeatureReference.getVersionedIdentifier().getIdentifier();

			// NB: no way to get a version directly from the VersionIdentifier, so need to manipulate the strings...
			String idAndVersion = currentFeatureReference.getVersionedIdentifier().toString();
			int versionPosition = result.id.length()+1;
			String version = idAndVersion.substring(versionPosition);

			result.version = version;
		} catch (CoreException e) {
			throw new IllegalStateException("The only implementation, FeatureReference#getVersionedIdentifer, can't throw this!", e);
		}
		return result;
	}

	private void createSiteXMLFile(UpdateSiteFeatureXML[] siteFeatures, File installDirectory) {
		// Build a site.xml file as a StringBuffer
		String xmlFileHeader = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<site>\r\n"; 
		String featureLines = buildFeatureXMLTagString(siteFeatures);
		String xmlFileFooter = 
				"</site>\r\n";
		String xmlFile = xmlFileHeader + featureLines + xmlFileFooter;

		// Write to a site.xml file inside installation target directory
		try {
			PrintWriter siteXml = new PrintWriter(new File(installDirectory, "/site.xml"));
			siteXml.print(xmlFile);
			siteXml.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("installDirectory was not found; could not create site.xml", e);
		}
	}
	
	String buildFeatureXMLTagString(UpdateSiteFeatureXML[] siteFeatures) {
		StringBuffer stringBuffer = new StringBuffer();
		for (UpdateSiteFeatureXML siteFeature : siteFeatures) {
			stringBuffer.append(siteFeature.toString());
		}
		return stringBuffer.toString();
	}


	public boolean hasIdenticalFeatures(List<IFeatureReference> updateSiteFeatures) throws InstallError {
		List<IFeatureReference> currentConfiguredFeatures = Arrays.asList(this.getConfiguredSite().getConfiguredFeatures());
		List<String> currentConfiguredFeatureStrings;
		List<String> updateSiteFeatureStrings;
		try {
			currentConfiguredFeatureStrings = convertFeaturesToStringList(currentConfiguredFeatures);
			updateSiteFeatureStrings = convertFeaturesToStringList(updateSiteFeatures);
		} catch (CoreException e) {
			throw new InstallError(e);
		}
		return ! areFeaturesDifferent(currentConfiguredFeatureStrings, updateSiteFeatureStrings);
	}

	private List<String> convertFeaturesToStringList(List<IFeatureReference> features) throws CoreException {
		List<String> list = new ArrayList<String>();

		for (IFeatureReference feature : features) {
			VersionedIdentifier versionedIdentifier = feature.getVersionedIdentifier();
			list.add(versionedIdentifier.getIdentifier() + versionedIdentifier.getVersion());
		}
		
		return list;
	}

	IConfiguredSite getConfiguredSite() {
		return configuredSite;
	}
	
	public void addToConfiguredSites() {
		localSite.getCurrentConfiguration().addConfiguredSite(configuredSite);
	}

	public void removeFromConfiguredSites() {
		localSite.getCurrentConfiguration().removeConfiguredSite(configuredSite);
	}

	// NB this has minor side effects: it sorts the lists in place.w
	// At time of writing, the lists passed into this method were created
	// specifically and only so they could be compared (and then discarded.
	boolean areFeaturesDifferent(List<String> featuresBeforeUpdate, List<String> featuresAfterUpdate) {
		Collections.sort(featuresBeforeUpdate);
		Collections.sort(featuresAfterUpdate);
		
		logFeaturesList(featuresBeforeUpdate.iterator(), "Features before update (areFeaturesDifferent):");		
		logFeaturesList(featuresAfterUpdate.iterator(), "Features after update (areFeaturesDifferent):");
		
		return !featuresBeforeUpdate.equals(featuresAfterUpdate);
	}

	private void logFeaturesList(Iterator<String> beforeIter, String message) {
		StringBuffer buf = new StringBuffer();
		buf.append(message+"\n");
		buf.append("Features we have:\n");
		while(beforeIter.hasNext()) {
			buf.append("\t"+beforeIter.next()+"\n");			
		}
		logger.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, buf.toString()));
	}

	// TODO: Replace NullProgressMonitor with a proper progress monitor
	private IInstallFeatureOperation[] computeUpdateOperations(IFeatureReference[] updateSiteFeatures, IConfiguredSite targetSite) throws CoreException {
		IInstallFeatureOperation[] featureInstallOperations = new IInstallFeatureOperation[updateSiteFeatures.length];
		for (int i = 0; i < updateSiteFeatures.length; i++) {
			IFeature feature = updateSiteFeatures[i].getFeature(new NullProgressMonitor());
			featureInstallOperations[i] = batchOperationFactory.createInstallOperation(targetSite, feature, null, null, null);
		}
		return featureInstallOperations;
	}

	private boolean installFeatures(IInstallFeatureOperation[] featureInstallOperations, IProgressMonitor progressMonitor) throws CoreException, InvocationTargetException {
		IBatchOperation batchOp = batchOperationFactory.createBatchInstallOperation(featureInstallOperations);
		return batchOp.execute(progressMonitor, null);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuredSite == null) ? 0 : configuredSite.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateManagerSite other = (UpdateManagerSite) obj;
		if (configuredSite == null) {
			if (other.configuredSite != null)
				return false;
		} else if (!configuredSite.equals(other.configuredSite))
			return false;
		return true;
	}

}
