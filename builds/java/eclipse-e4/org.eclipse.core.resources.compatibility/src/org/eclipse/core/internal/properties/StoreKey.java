/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.internal.properties;

import java.io.*;
import org.eclipse.core.internal.resources.CompatibilityMessages;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.internal.utils.Convert;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.*;
import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class StoreKey {
	protected byte[] value = null;
	protected boolean matchPrefix = false;
	protected ResourceName resourceName = null;
	protected String qualifier = null;
	protected String localName = null;

	public StoreKey(byte[] bytes) throws CoreException {
		super();
		value = bytes;
		initializeObjects();
	}

	/*
	 * The key is a full key
	 */
	public StoreKey(ResourceName resourceName, QualifiedName propertyName) throws CoreException {
		super();
		this.resourceName = resourceName;
		qualifier = propertyName.getQualifier();
		localName = propertyName.getLocalName();
		initializeBytes();
	}

	/*
	 * The key is a resource name -- either a full resource name
	 * or a resource name prefix (for deep matching)
	 * (i.e. no property name)
	 */
	public StoreKey(ResourceName resourceName, boolean matchPrefix) throws CoreException {
		super();
		this.resourceName = resourceName;
		this.matchPrefix = matchPrefix;
		initializeBytes();
	}

	public String getLocalName() {
		return localName;
	}

	public QualifiedName getPropertyName() {
		return new QualifiedName(qualifier, localName);
	}

	public String getQualifier() {
		return qualifier;
	}

	public ResourceName getResourceName() {
		return resourceName;
	}

	private void initializeBytes() throws CoreException {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			// Step 0: the resource name prefix -- always considered in full
			writeNullTerminated(buffer, resourceName.getQualifier());
			String path = resourceName.getPath().toString();
			// Step 1: the resource path
			if (matchPrefix) {
				// don't null terminate resource name
				writeBytes(buffer, path);
				// If prefix matching, cannot allow other fields to be specified
				if (qualifier != null || localName != null) {
					String message = NLS.bind(CompatibilityMessages.properties_invalidPropName, qualifier, localName);
					throw new ResourceException(IResourceStatus.INVALID_VALUE, null, message, null);
				}
			} else {
				// Zero depth requires full path matching including null
				writeNullTerminated(buffer, path);
			}
			// Step 2: the name space (no prefix matching)
			if (qualifier != null) {
				writeNullTerminated(buffer, qualifier);
				// Step 3: the local name (no prefix matching)
				if (localName != null)
					writeNullTerminated(buffer, localName);
			} else if (localName != null) {
				// Specifying a local name without a qualifier is illegal
				String message = NLS.bind(CompatibilityMessages.properties_invalidPropName, qualifier, localName);
				throw new ResourceException(IResourceStatus.INVALID_VALUE, null, message, null);
			}
			value = buffer.toByteArray();
		} catch (IOException e) {
			// should never happen
			throw new ResourceException(IResourceStatus.INTERNAL_ERROR, null, CompatibilityMessages.properties_storeProblem, e);
		}
	}

	/**
	 * Assumes value bytes are fully defined -- i.e. initializing
	 * from a stored key.
	 */
	protected void initializeObjects() throws CoreException {
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(value);
			String prefix = readNullTerminated(stream);
			String path = readNullTerminated(stream);
			resourceName = new ResourceName(prefix, new Path(path));
			qualifier = readNullTerminated(stream);
			localName = readNullTerminated(stream);
		} catch (IOException e) {
			// should never happen
			throw new ResourceException(IResourceStatus.INTERNAL_ERROR, null, CompatibilityMessages.properties_storeProblem, e);
		}
	}

	private String readNullTerminated(ByteArrayInputStream stream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int b;
		while ((b = stream.read()) > 0)
			buffer.write(b);
		//should not reach end of stream without first hitting null (0) byte
		if (b == -1)
			throw new EOFException();
		return Convert.fromUTF8(buffer.toByteArray());
	}

	public byte[] toBytes() {
		return value;
	}

	//For debugging
	public String toString() {
		return new String(toBytes());
	}

	private void writeBytes(ByteArrayOutputStream stream, String value) throws IOException {
		byte[] bytes = Convert.toUTF8(value);
		stream.write(bytes);
	}

	private void writeNullTerminated(ByteArrayOutputStream stream, String value) throws IOException {
		writeBytes(stream, value);
		stream.write(0);
	}
}
