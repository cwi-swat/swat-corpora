/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.localstore;

import org.eclipse.core.internal.resources.CompatibilityMessages;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.internal.utils.Policy;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;

public class HistoryStoreConverter {
	/**
	 * Converts an existing history store lying on disk to the new history store.
	 * Returns Status.OK_STATUS if nothing is done, an IStatus.INFO status if
	 * the conversion happens successfully or an IStatus.ERROR status if an error
	 * happened during the conversion process.
	 */
	public IStatus convertHistory(Workspace workspace, IPath location, int limit, final HistoryStore2 destination, boolean rename) {
		if (!location.toFile().isDirectory())
			// nothing to be converted
			return Status.OK_STATUS;
		IPath indexFile = location.append(HistoryStore.INDEX_FILE);
		if (!indexFile.toFile().isFile())
			// nothing to be converted		
			return Status.OK_STATUS;
		// visit all existing entries and add them to the new history store
		long start = System.currentTimeMillis();
		final CoreException[] exception = new CoreException[1];
		final BucketTree tree = destination.getTree();
		final HistoryBucket currentBucket = (HistoryBucket) tree.getCurrent();
		HistoryStore source = new HistoryStore(workspace, location, limit);
		source.accept(Path.ROOT, new IHistoryStoreVisitor() {
			public boolean visit(HistoryStoreEntry state) {
				try {
					tree.loadBucketFor(state.getPath());
				} catch (CoreException e) {
					// failed while loading bucket
					exception[0] = e;
					return false;
				}
				currentBucket.addBlob(state.getPath(), state.getUUID(), state.getLastModified());
				return true;
			}
		}, true);
		try {
			// the last bucket changed will not have been saved
			tree.getCurrent().save();
			// we are done using the old history store instance		
			source.shutdown(null);
		} catch (CoreException e) {
			// failed during save
			exception[0] = e;
		}
		if (Policy.DEBUG_HISTORY)
			Policy.debug("Time to convert local history: " + (System.currentTimeMillis() - start) + "ms."); //$NON-NLS-1$ //$NON-NLS-2$
		if (exception[0] != null) {
			// failed while visiting the old data or saving the new data
			String conversionFailed = CompatibilityMessages.history_conversionFailed;
			Status failure = new MultiStatus(ResourcesPlugin.PI_RESOURCES, IResourceStatus.FAILED_READ_METADATA, new IStatus[] {exception[0].getStatus()}, conversionFailed, null);
			// we failed, so don't do anything else - we might try converting again later
			return failure;
		}
		// everything went fine
		// if requested rename the index file to something else
		// so we don't try converting again in the future
		if (rename)
			indexFile.toFile().renameTo(indexFile.addFileExtension(Long.toString(System.currentTimeMillis())).toFile());
		String conversionOk = CompatibilityMessages.history_conversionSucceeded;
		// leave a note to the user so this does not happen silently
		return new Status(IStatus.INFO, ResourcesPlugin.PI_RESOURCES, IStatus.OK, conversionOk, null);
	}
}
