/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.stringconverters;

import java.io.InputStream;

import org.eclipse.e4.tm.stringconverter.AbstractStringConverter;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class ImageStringConverter extends AbstractStringConverter {

	public Object convert(String source, StringConverterContext context) throws Exception {
		URI uri = context.convert(source, URI.class);
		if (uri == null) {
			return null;
		}
		InputStream inputStream = context.adapt(null, URIConverter.class).createInputStream(uri);
		if (inputStream == null) {
			throw new IllegalArgumentException("Couldn't create InputStream for " + uri);
		}
		ImageLoader imageLoader = new ImageLoader();
		ImageData[] imageData = imageLoader.load(inputStream);
		if (imageData == null || imageData.length == 0) {
			throw new IllegalArgumentException("Couldn't load image data for " + uri);
		}
		Device device = context.adapt(null, Device.class);
		return new Image(device, imageData[0]);
	}
}
