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
package org.eclipse.e4.xwt.emf;

import org.eclipse.e4.xwt.IDataProvider.DataModelService;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EMFDataModelService implements DataModelService {

	public Object toModelType(Object data) {
		return EMFHelper.toType(data);
	}

	public Object loadModelType(String className) {
		throw new UnsupportedOperationException();
	}

	public Object toModelPropertyType(Object object, String propertyName) {
		EClass type = null;
		if (object instanceof EClass) {
			type = (EClass) object;
		} else if (object instanceof EObject) {
			type = ((EObject) object).eClass();
		}
		if (type == null) {
			throw new XWTException(" Type for\"" + propertyName
					+ "\" is not found ");
		}
		EStructuralFeature structuralFeature = type
				.getEStructuralFeature(propertyName);

		if (structuralFeature == null) {
			throw new XWTException(" Property \"" + propertyName
					+ "\" is not found in the class " + type.getName());
		}
		return structuralFeature.getEType();
	}
}