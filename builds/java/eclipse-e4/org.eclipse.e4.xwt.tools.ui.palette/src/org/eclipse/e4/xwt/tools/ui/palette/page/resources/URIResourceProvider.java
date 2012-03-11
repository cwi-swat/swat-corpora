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
package org.eclipse.e4.xwt.tools.ui.palette.page.resources;

import org.eclipse.e4.xwt.tools.ui.palette.impl.PalettePackageImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class URIResourceProvider extends EntryResourceProvider {
	private Resource resource;

	public Resource getPaletteResource() {
		if (resource == null) {
			URI input = getPaletteResourceURI();
			if (input != null) {
				resource = loadResource(input);
			}
		}
		return resource;
	}

	private Resource loadResource(URI input) {
		PalettePackageImpl.init();
		ResourceSet rs = new ResourceSetImpl();
		Resource result = rs.getResource(input, true);
		return result;
	}

	protected abstract URI getPaletteResourceURI();

}
