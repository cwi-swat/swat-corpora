/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.       *
 * All rights reserved. This program and the accompanying materials            *
 * are made available under the terms of the Eclipse Public License v1.0       *
 * which accompanies this distribution, and is available at                    *
 * http://www.eclipse.org/legal/epl-v10.html                                   *  
 * Contributors:                                                               *  
 *     Soyatec - initial API and implementation                                * 
 *******************************************************************************/
package org.eclipse.e4.xwt.emf;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.emf.converters.StringToURI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class EMFBinding {
	public static void initialze() {
		XWT.registerMetaclass(EMFDataProvider.class);
		XWT.registerConvertor(StringToURI.instance);
		XWT.addDataProviderFactory(EMFDataProviderFactory.EMF_DATA_PROVIDER_FACTORY, new EMFDataProviderFactory());
	}

	public static EObject getEObject(EObject eObj, String featureName) {
		if (eObj == null) {
			return null;
		}
		if (featureName != null) {
			int index = featureName.indexOf(".");
			while (eObj != null && index != -1) {
				String prefix = featureName.substring(0, index);
				eObj = getEObject(eObj, prefix);
				featureName = featureName.substring(index + 1);
				index = featureName.indexOf(".");
			}
			index = featureName.indexOf(".");
			if (eObj != null && index == -1) {
				EStructuralFeature sf = eObj.eClass().getEStructuralFeature(
						featureName);
				if (sf != null) {
					Object newValue = eObj.eGet(sf);
					if (newValue == null && sf instanceof EReference) {
						EObject newEObj = EcoreUtil.create(((EReference) sf)
								.getEReferenceType());
						eObj.eSet(sf, newEObj);
						eObj = newEObj;
					} else if (newValue instanceof EObject) {
						eObj = (EObject) newValue;
					}
				}
			}
		}
		return eObj;
	}
}
