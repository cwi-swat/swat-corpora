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
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages a semaphore file that indicates if the current startup is due to
 * a pending restart operation.
 */
public class RestartManager {

	private static final String RESTART_FILENAME = "onrestart";
	private File restartFile;

	public RestartManager(File downloadRootDir) {
		downloadRootDir.mkdirs();
		this.restartFile = new File(downloadRootDir, RESTART_FILENAME);
	}

	public void restartInitiated() throws IOException {
		// write a temporary file
		FileWriter fw;
		fw = new FileWriter(restartFile);
		fw.close();
	}

	public void restartCompleted() {
		if (!restartFile.exists()) {
			return;
		}
		restartFile.delete();
	}

	public boolean isOnRestart() {
		return restartFile.exists();
	}
}
