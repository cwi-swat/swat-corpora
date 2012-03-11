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
package org.eclipse.e4.tm.stringconverter;

import java.util.ArrayList;
import java.util.List;

public class CompositeStringConverter implements StringConverter {

	private List<StringConverter> stringConverters;

	public CompositeStringConverter() {
	}
	public CompositeStringConverter(StringConverter stringConverter) {
		addStringConverter(stringConverter);
	}

	public void addStringConverter(StringConverter stringConverter) {
		if (stringConverters == null) {
			stringConverters = new ArrayList<StringConverter>();
		}
		stringConverters.add(stringConverter);
	}

	public void removeDataParser(StringConverter stringConverter) {
		if (stringConverters != null) {
			stringConverters.remove(stringConverter);
		}
	}

	public <T> T convert(String source, Class<T> klass, StringConverterContext context) {
		for (int i = 0; i < stringConverters.size(); i++) {
			StringConverter	stringConverter = stringConverters.get(i);
			T o = null;
			try {
				o = stringConverter.convert(source, klass, context);
			} catch (Exception e) {
			}
			if (o != null) {
				return o;
			}
		}
		return null;
	}
}
