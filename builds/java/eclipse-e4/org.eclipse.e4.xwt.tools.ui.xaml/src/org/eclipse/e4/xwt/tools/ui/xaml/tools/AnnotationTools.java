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
package org.eclipse.e4.xwt.tools.ui.xaml.tools;

import org.eclipse.e4.xwt.tools.ui.xaml.Annotation;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class AnnotationTools {

	public static void addAnnotation(XamlNode node, String key, String value) {
		if (node == null) {
			return;
		}
		Annotation annotation = node.getAnnotation(key);
		if (annotation == null) {
			annotation = XamlFactory.eINSTANCE.createAnnotation();
			annotation.setSource(key);
			node.getAnnotations().add(annotation);
		}
		annotation.getDetails().put(key, value);
	}

	public static void removeAnnotation(XamlNode node, String key) {
		if (node == null) {
			return;
		}
		Annotation annotation = node.getAnnotation(key);
		if (annotation != null) {
			node.getAnnotations().remove(annotation);
		}
	}

	public static boolean isAnnotated(XamlNode node, String key) {
		if (node == null) {
			return false;
		}
		Annotation annotation = node.getAnnotation(key);
		return annotation != null && annotation.getDetails().containsKey(key);
	}

	public static String getAnnotationValue(XamlNode node, String key) {
		if (node == null) {
			return null;
		}
		if (isAnnotated(node, key)) {
			return node.getAnnotation(key).getDetails().get(key);
		}
		return null;
	}
}
