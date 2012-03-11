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
package org.eclipse.e4.tm.builder.swt;

import java.util.StringTokenizer;

import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.stringconverter.StringConversion;
import org.eclipse.e4.tm.stringconverters.ClassStringConverter;
import org.eclipse.e4.tm.stringconverters.StaticFieldsStringConverter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public class SwtBuilder extends AbstractBuilder {

	public SwtBuilder() {
		super();
		StringConversion stringConverter = getStringConverter();
		ClassStringConverter classParser = new ClassStringConverter();
		stringConverter.registerStringConverter(Class.class, classParser);
		getClassResolver().importPackage("org.eclipse.swt.widgets");
		getClassResolver().importPackage("org.eclipse.swt.layout");
	}

	public final static String separators = " ;:,+|";

	public static StringTokenizer getSeparatedTokens(String s) {
		return new StringTokenizer(s != null ? s : "", SwtBuilder.separators);
	}

	public <T> T adapt(Object value, Class<T> c) {
		if (c == Display.class || c == Device.class) {
			if (value instanceof Widget) {
				return (T)((Widget)value).getDisplay();
			} else if (value == null) {
				Display display = Display.getCurrent();
				if (display == null) {
					display = Display.getDefault();
				}
				return (T)display;
			}
//		} else if (c == Color.class && value instanceof RGB) {
//			return (T)new Color(getRootObject(org.eclipse.swt.widgets.Control.class).getDisplay(),(RGB)value);
		}
		return super.adapt(value, c);
	}

	public static <T> T getStaticField(Class<?> constantsClass, String name, Class<T> valueClass, T def) {
		return StaticFieldsStringConverter.convert(constantsClass, valueClass, null, name, def);
	}

	public static String getAnnotationUri() {
		return "http://www.eclipse.org/e4/swt.ecore";
	}

	public void build(Resource res, Object context) {
		if (context instanceof Composite) {
			super.build(res, context);
			((Composite)context).layout();
		}
	}
}
