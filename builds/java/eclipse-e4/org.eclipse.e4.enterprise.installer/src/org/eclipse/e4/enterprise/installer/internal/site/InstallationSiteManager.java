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
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.ILocalSite;
import org.eclipse.update.core.SiteManager;

/**
 * InstallationSiteManager creates a new local Site timestamped with the current
 * date/time whenever an install operation takes place. After a successful
 * install, all features that aren't a part of the core RCP platform will be in
 * this new Site and any old ones may be deleted, thus removing any Features
 * from the disk that are no longer needed. If anything fails during an update,
 * the new (possibly-corrupted) Site is deleted, thus rolling the configuration
 * back to the last known-good configuration.
 */
public class InstallationSiteManager {
	
	static final String CONFIG_SITE_DIR_DATE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
	static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CONFIG_SITE_DIR_DATE_NAME_FORMAT);
	
	private File downloadRootDir;
	private ILocalSite localSite;
	private RestartManager restartManager;

	public InstallationSiteManager(File downloadRootDir) {
		restartManager = makeRestartManager(downloadRootDir);
		this.downloadRootDir = downloadRootDir;
		try {
			localSite = SiteManager.getLocalSite();
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected RestartManager makeRestartManager(File downloadRootDir) {
		return new RestartManager(downloadRootDir);
	}

	public void restartInitiated() throws IOException {
		restartManager.restartInitiated();
	}

	public void restartCompleted() {
		restartManager.restartCompleted();
	}

	public boolean isOnRestart() {
		return restartManager.isOnRestart();
	}

	public InstallationSite create() throws CoreException {
		IConfiguredSite newSite = localSite.getCurrentConfiguration().createConfiguredSite(new File(downloadRootDir,makeFileName()));
		newSite.verifyUpdatableStatus();
		return new UpdateManagerSite(newSite);
	}
	
	private String makeFileName() {
		String filename = simpleDateFormat.format(new Date());
		return filename;
	}
		
	public InstallationSite find() {
		IConfiguredSite[] configuredSites = localSite.getCurrentConfiguration().getConfiguredSites();
		IConfiguredSite currentSite = findCurrentDownloadSite(downloadRootDir, configuredSites);
		if (currentSite != null) {
			return new UpdateManagerSite(currentSite);
		} else {
			return new NullInstallationSite();
		}
	}
	
	IConfiguredSite findCurrentDownloadSite(File downloadRootDir, IConfiguredSite[] configuredSites) {
		//iterate through configured sites and find one that is underneath the downloadParentDirectory
		for (int i = 0; i < configuredSites.length; i++) {
			File target = new File(configuredSites[i].getSite().getURL().getPath());
			if (isSubdirectory(downloadRootDir, target)) {
				return configuredSites[i];
			}
		}
		return null;
	}
	
	boolean isSubdirectory(File parentDir,final File possibleSubDir) {
		if (parentDir == null || possibleSubDir == null) {
			return false;
		}
		
		//if the target starts with the source path BUT contains more
		if (possibleSubDir.getAbsolutePath().toLowerCase().startsWith(parentDir.getAbsolutePath().toLowerCase())) {
			if (possibleSubDir.getAbsolutePath().length() > parentDir.getAbsolutePath().length()) { 
				if (possibleSubDir.getAbsolutePath().charAt(parentDir.getAbsolutePath().length()) == File.separatorChar) {
					return true;
				}
			}
		}
		
		return false;
	}	
	
	public void cleanUp()
	{
		File[] directoriesUnderDownloadRootDir = listDirectoriesUnderDownloadRootDir();
		File[] directoriesThatLookLikeOBInstallationSites = filterOutFilesThatDoNotMatchExpectedFormat(directoriesUnderDownloadRootDir);
		sortInChronologicallyAscendingOrder(directoriesThatLookLikeOBInstallationSites);
		deleteAllButTheYoungest(directoriesThatLookLikeOBInstallationSites);
	}
	
	File[] filterOutFilesThatDoNotMatchExpectedFormat(File[] filesToFilter) {
		ArrayList<File> result = new ArrayList<File>();

		for (File file : filesToFilter) {
			String name = file.getName();
			try {
				if (name.length() != CONFIG_SITE_DIR_DATE_NAME_FORMAT.length()) {
					continue;
				}
				simpleDateFormat.parse(name);
				result.add(file);
			} catch (ParseException e) {
				// do nothing - means we have found something that doesn't
				// match.
			}
		}
		return result.toArray(new File[result.size()]);
	}

	private void deleteAllButTheYoungest(File[] directoriesUnderDownloadRootDir) {
		for (int i = 0; i < directoriesUnderDownloadRootDir.length - 1; i++) {
			File file = directoriesUnderDownloadRootDir[i];
			deleteDir(file);
		}
	}

	private void sortInChronologicallyAscendingOrder(File[] directoriesUnderDownloadRootDir) {
		Arrays.sort(directoriesUnderDownloadRootDir, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	private File[] listDirectoriesUnderDownloadRootDir() {
		File[] directoriesUnderDownloadRootDir = downloadRootDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		return directoriesUnderDownloadRootDir;
	}

	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}
}
