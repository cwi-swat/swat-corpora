/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package org.eclipse.e4.tm.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class UriModelContext extends AbstractModelContext {

	private URIConverter uriConverter = new ExtensibleURIConverterImpl();
	private URI uri;

	public UriModelContext(URI uri) {
		super();
		this.uri = uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public EObject getModel() {
		ResourceSet resSet = new ResourceSetImpl();
		resSet.setURIConverter(uriConverter);
		return getModel(resSet.getResource(uri, true));
	}
}
