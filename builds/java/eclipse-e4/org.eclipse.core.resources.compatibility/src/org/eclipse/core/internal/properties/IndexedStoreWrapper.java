/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.properties;

import org.eclipse.core.internal.indexing.*;
import org.eclipse.core.internal.resources.*;
import org.eclipse.core.internal.utils.Policy;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.osgi.util.NLS;

public class IndexedStoreWrapper {

	private IndexedStore store;
	private IPath location;

	/* constants */
	private static final String INDEX_NAME = "index"; //$NON-NLS-1$

	public IndexedStoreWrapper(IPath location) {
		this.location = location;
	}

	private void open() throws CoreException {
		try {
			String name = location.toOSString();
			store = IndexedStore.find(name);
			if (store == null) {
				store = new IndexedStore();
				store.open(name);
			}
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotOpen, location.toOSString());
			ResourceStatus status = new ResourceStatus(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e);
			throw new CoreException(status);
		}
	}

	private void recreate() throws CoreException {
		close();
		// Rename the problematic store for future analysis.
		java.io.File file = location.toFile();
		file.renameTo(location.addFileExtension("001").toFile()); //$NON-NLS-1$
		file.delete();
		if (!file.exists()) {
			try {
				open();
			} catch (CoreException e) {
				//failed again, null the store to make sure we
				//don't attempt to access an invalid store.
				store = null;
				throw e;
			}
		}
	}

	public synchronized void close() {
		if (store == null)
			return;
		try {
			store.close();
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotClose, location.toOSString());
			Policy.log(new ResourceStatus(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e));
		} finally {
			store = null;
		}
	}

	public synchronized void commit() throws CoreException {
		if (store == null)
			return;
		try {
			store.commit();
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotCommit, location.toOSString());
			ResourceStatus status = new ResourceStatus(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e);
			throw new ResourceException(status);
		}
	}

	private void create() throws CoreException {
		try {
			open();
		} catch (CoreException e) {
			Policy.log(e.getStatus());
			//failed to open -- copy store elsewhere and create a new one
			recreate();
			if (store == null) {
				String message = NLS.bind(CompatibilityMessages.indexed_couldNotCreate, location.toOSString());
				ResourceStatus status = new ResourceStatus(IResourceStatus.FAILED_WRITE_LOCAL, location, message, null);
				throw new ResourceException(status);
			}
		}
	}

	private Index createIndex() throws CoreException {
		try {
			return getStore().createIndex(INDEX_NAME);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotCreateIndex, location.toOSString());
			ResourceStatus status = new ResourceStatus(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e);
			throw new ResourceException(status);
		}
	}

	public synchronized Index getIndex() throws CoreException {
		Exception problem = null;
		try {
			return getStore().getIndex(INDEX_NAME);
		} catch (IndexedStoreException e) {
			if (e.id == IndexedStoreException.IndexNotFound)
				return createIndex();
			problem = e;
			return null;
		} catch (CoreException e) {
			//just rethrow
			throw e;
		} catch (Exception e) {
			problem = e;
			return null;
		} finally {
			if (problem != null) {
				String message = NLS.bind(CompatibilityMessages.indexed_couldNotGetIndex, location.toOSString());
				ResourceStatus status = new ResourceStatus(IResourceStatus.FAILED_READ_LOCAL, location, message, problem);
				throw new ResourceException(status);
			}
		}
	}

	public synchronized String getObjectAsString(ObjectID id) throws CoreException {
		try {
			return getStore().getObjectAsString(id);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotRead, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_READ_LOCAL, location, message, e);
		}
	}

	private IndexedStore getStore() throws CoreException {
		if (store == null)
			create();
		return store;
	}

	public synchronized IndexCursor getCursor() throws CoreException {
		try {
			return getIndex().open();
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotCreateCursor, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_READ_LOCAL, location, message, e);
		}
	}

	public synchronized ObjectID createObject(String s) throws CoreException {
		try {
			return getStore().createObject(s);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotWrite, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e);
		}
	}

	public synchronized ObjectID createObject(byte[] b) throws CoreException {
		try {
			return getStore().createObject(b);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotWrite, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_WRITE_LOCAL, location, message, e);
		}
	}

	public synchronized void removeObject(ObjectID id) throws CoreException {
		try {
			getStore().removeObject(id);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotDelete, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_DELETE_LOCAL, location, message, e);
		}
	}

	public synchronized byte[] getObject(ObjectID id) throws CoreException {
		try {
			return getStore().getObject(id);
		} catch (Exception e) {
			String message = NLS.bind(CompatibilityMessages.indexed_couldNotRead, location.toOSString());
			throw new ResourceException(IResourceStatus.FAILED_READ_LOCAL, location, message, e);
		}
	}

	/**
	 * Something has gone wrong.  Make a best effort at copying the file 
	 * elsewhere and creating a new one.  Log exceptions.
	 */
	public synchronized void reset() {
		try {
			recreate();
		} catch (CoreException e) {
			Policy.log(e.getStatus());
		}
	}
}
