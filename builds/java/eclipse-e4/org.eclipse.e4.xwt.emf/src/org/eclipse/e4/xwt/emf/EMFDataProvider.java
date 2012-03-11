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
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.observable.masterdetail.DetailObservableValue;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.dataproviders.AbstractDataProvider;
import org.eclipse.e4.xwt.internal.core.UpdateSourceTrigger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.EObjectObservableValue;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class EMFDataProvider extends AbstractDataProvider {
	private DataModelService dataModelService;
	
	private URI typeURI;
	private URI objectURI;

	private ResourceSet resourceSet;

	private String featureName;
	private Object objectInstance;

	public EMFDataProvider(DataModelService dataModelService) {
		this.dataModelService = dataModelService;
	}
	
	public EMFDataProvider() {
	}
	
	@Override
	protected IObservableValue observeDetailValue(IObservableValue bean,
			Object ownerType, String propertyName, Object propertyType) {
		EClass type = null;
		if (ownerType instanceof EClass) {
			type = (EClass) ownerType;
		}
		if (type == null) {
			type = (EClass) getModelService().toModelType(bean);
		}
		EStructuralFeature feature = type.getEStructuralFeature(propertyName);
		if (feature == null) {
			throw new XWTException(propertyName + " feature is not found in "
					+ EMFHelper.getQualifiedName(type));
		}
		return EMFObservables
				.observeDetailValue(bean.getRealm(), bean, feature);
	}

	@Override
	protected IObservableValue observeValue(Object bean, String propertyName) {
		EClass type = (EClass) getModelService().toModelType(bean);
		EStructuralFeature feature = type.getEStructuralFeature(propertyName);
		if (feature == null) {
			throw new XWTException(propertyName + " feature is not found in "
					+ EMFHelper.getQualifiedName(type));
		}
		return EMFObservables.observeValue(XWT.getRealm(), (EObject) bean,
				feature);
	}

	public IValueProperty observeValueProperty(Object valueType, String path,
			UpdateSourceTrigger updateSourceTrigger) {
		EClass type = null;
		if (valueType instanceof EClass) {
			type = (EClass) valueType;
		} else if (valueType instanceof EObject) {
			EObject object = (EObject) valueType;
			type = object.eClass();
		} else {
			throw new IllegalStateException();
		}
		EStructuralFeature feature = type.getEStructuralFeature(path);
		if (feature == null) {
			throw new XWTException(path + " feature is not found in "
					+ EMFHelper.getQualifiedName(type));
		}
		return EMFProperties.value(feature);
	}

	public URI getObjectURI() {
		return objectURI;
	}

	public void setObjectURI(URI objectURI) {
		this.objectURI = objectURI;
	}

	public URI getTypeURI() {
		return typeURI;
	}

	public void setTypeURI(URI typeURI) {
		this.typeURI = typeURI;
	}

	public Object getObjectInstance() {
		if (objectInstance == null) {
			if (objectURI != null) {
				objectInstance = getResourceSet().getEObject(objectURI, true);
			} else if (typeURI != null) {
				EClass eClass = (EClass) getResourceSet().getEObject(typeURI,
						true);
				objectInstance = eClass.getEPackage().getEFactoryInstance()
						.create(eClass);
			}
		}
		return objectInstance;
	}

	protected ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
		}
		return resourceSet;
	}

	protected void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}

	public void setObjectInstance(Object eObject) {
		this.objectInstance = eObject;
	}

	public Object getTarget() {
		Object instance = getObjectInstance();
		if (instance instanceof EObject) {
			EObject eObj = (EObject) instance;
			if (eObj != null && featureName != null) {
				return EMFBinding.getEObject(eObj, featureName);
			}
			return eObj;
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.IDataProvider#getData(java.lang.String)
	 */
	public Object getData(String path) {
		Object instance = getTarget();
		if (!(instance instanceof EObject)) {
			if (path == null || path.length() == 0 || path.equals(".")) {
				return instance;
			}
			throw new IllegalStateException();
		}
		EObject eObj = (EObject) instance;
		if (path == null || ".".equals(path)) {
			return eObj;
		}
		if (eObj != null) {
			String featureName = path;
			int index = path.lastIndexOf(".");
			if (index != -1) {
				String parent = path.substring(0, index);
				eObj = (EObject) getData(eObj, parent);
				featureName = path.substring(index + 1);
			}
			EStructuralFeature feature = eObj.eClass().getEStructuralFeature(
					featureName);
			if (feature != null) {
				return eObj.eGet(feature);
			}
		}
		return null;
	}

	public boolean isPropertyReadOnly(String path) {
		EClass classifier = getCurrentType();
		if (classifier != null && path != null) {
			EStructuralFeature feature = classifier
					.getEStructuralFeature(path);
			if (feature != null) {
				return !feature.isChangeable();
			}
		}
		return true;
	}

	protected EClass getCurrentType() {
		Object instance = getTarget();
		EClass eObj = null;
		if (instance instanceof EObjectObservableValue) {
			EObjectObservableValue observableValue = (EObjectObservableValue) instance;
			EStructuralFeature valueType = (EStructuralFeature) observableValue
					.getValueType();
			EClassifier classifier = valueType.getEType();
			if (classifier instanceof EClass) {
				eObj = (EClass) classifier;				
			} else {// EDataType, maybe we should change the return type to
					// access EDataType.
				return null;
			}
		} else if (instance instanceof EClass) {
			eObj = (EClass) instance;
		} else if (instance instanceof EObject) {
			EObject object = (EObject) instance;
			eObj = object.eClass();
		} else if (instance instanceof DetailObservableValue) {
			EObject eObject = (EObject) ((DetailObservableValue) instance)
					.getValueType();
			eObj = eObject.eClass();
		} else {
			if (typeURI != null) {
				EObject element = getResourceSet().getEObject(typeURI, true);
				if (element instanceof EClass) {
					eObj = (EClass) element;
				}
			}
		}
		if (eObj == null) {
			throw new IllegalStateException();
		}

		return eObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.IDataProvider#getDataType(java.lang.String)
	 */
	public EClassifier getDataType(String path) {
		EClass classifier = getCurrentType();
		if (path == null || path.trim().length() == 0 || path.equals(".")) {
			return classifier;
		}
		if (classifier != null) {
			String featureName = path;
			EStructuralFeature feature = classifier
					.getEStructuralFeature(featureName);
			if (feature != null) {
				return feature.getEType();
			}
		}

		return classifier;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFeatureName() {
		return featureName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.IDataProvider#getData(java.lang.Object,
	 * java.lang.String)
	 */
	public Object getData(Object target, String path) {
		if (target instanceof EObject) {
			if (path == null || ".".equals(path)) {
				return target;
			}
			return EMFBinding.getEObject((EObject) target, path);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.IDataProvider#setData(java.lang.String,
	 * java.lang.Object)
	 */
	public void setData(String path, Object value) {
		setData(getTarget(), path, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.IDataProvider#setData(java.lang.Object,
	 * java.lang.String, java.lang.Object)
	 */
	public void setData(Object target, String path, Object value) {
		if (target instanceof EObject) {
			EObject eObj = (EObject) target;
			String featureName = path;
			int index = path.lastIndexOf(".");
			if (index != -1) {
				String parent = path.substring(0, index);
				eObj = EMFBinding.getEObject(eObj, parent);
				featureName = path.substring(index + 1);
			}
			EStructuralFeature feature = eObj.eClass().getEStructuralFeature(
					featureName);
			if (feature != null) {
				eObj.eSet(feature, value);
			}
		}
	}

	protected DataModelService createDataModelService() {
		return new EMFDataModelService();
	}
	
	public DataModelService getModelService() {
		if (dataModelService == null) {
			dataModelService = createDataModelService();
		}
		return dataModelService;
	}

	public IValueProperty createValueProperty(Object type, String fullPath) {
		if (type == null || fullPath == null) {
			return null;
		}
		EClass eClass = null;
		if (type instanceof EClass) {
			eClass = (EClass) type;
		} else if (type instanceof EObject) {
			eClass = ((EObject) type).eClass();
		}

		if (eClass == null) {
			return null;
		}
		EStructuralFeature feature = eClass.getEStructuralFeature(fullPath);
		if (feature != null) {
			return EMFProperties.value(feature);
		}
		return null;
	}
}
