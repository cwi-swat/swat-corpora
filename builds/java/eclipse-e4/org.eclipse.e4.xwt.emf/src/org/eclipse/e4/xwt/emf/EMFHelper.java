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

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EMFHelper {

	public static String getQualifiedName(ENamedElement namedElement) {
		EObject object = namedElement.eContainer();
		if (object instanceof ENamedElement) {
			String parentQN = getQualifiedName((ENamedElement) namedElement
					.eContainer());

			if (parentQN != null && !parentQN.equals("")) {
				return parentQN + "." + namedElement.getName();
			}
		}
		return namedElement.getName();
	}

	public static Object toType(Object data) {
		if (data instanceof EClassifier) {
			return (EClassifier) data;
		}
		if (data instanceof IObservableValue) {
			IObservableValue observableValue = (IObservableValue) data;
			Object element = observableValue.getValueType();
			if (element instanceof EStructuralFeature) {
				// A bug of EMF databinding
				return ((EStructuralFeature) element).getEType();
			}
			return element;
		}
		if (data instanceof EObject) {
			EObject object = (EObject) data;
			return object.eClass();
		}
		else if (data instanceof EMFDataProvider) {
			EMFDataProvider dataProvider = (EMFDataProvider) data;
			return dataProvider.getDataType(null);
		}
		return null;
	}
	
	public static EReference findReference(EClass eClass, String name) {
			for (EStructuralFeature feature : eClass.getEStructuralFeatures()) {
				if (feature instanceof EReference && name.equals(feature.getName())) {
					return (EReference)feature;
				}
			}
		
		for (EClass superType : eClass.getESuperTypes()) {
			EReference reference = findReference(superType, name);
			if (reference != null) {
				return reference;
			}			
		}
		return null;
	}
}
