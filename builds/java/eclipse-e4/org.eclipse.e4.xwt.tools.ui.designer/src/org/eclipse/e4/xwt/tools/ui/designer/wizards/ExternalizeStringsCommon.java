/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class ExternalizeStringsCommon {
	/**
	 * Get the history nameMap of the class file.
	 * 
	 * @param iFile
	 * @return getHistoryContents
	 * @throws IOException
	 */
	public static StringBuffer getHistoryContents(IFile iFile) throws IOException {
		StringBuffer fileContent = new StringBuffer();
		File file = iFile.getLocation().toFile();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while (line != null) {
			if (line.indexOf("static {") != -1) {
				break;
			}
			fileContent.append(line + "\r");
			line = br.readLine();
		}
		fr.close();
		br.close();
		return fileContent;
	}

	/**
	 * Get file with path and name.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return
	 * @throws CoreException
	 */
	public static IFile getIFile(String folderName, String fileName) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(folderName));
		if (resource == null || !resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + folderName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		return file;
	}

	public static void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "org.eclipse.e4.xwt.tools.ui.designer", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
