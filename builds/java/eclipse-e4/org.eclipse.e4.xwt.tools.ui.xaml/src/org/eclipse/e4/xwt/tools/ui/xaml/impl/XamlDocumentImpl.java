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
package org.eclipse.e4.xwt.tools.ui.xaml.impl;

import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Document</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl#getRootElement <em>Root Element</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl#getDeclaredNamespaces <em>Declared Namespaces</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XamlDocumentImpl extends AnnotatedObjectImpl implements
		XamlDocument {
	/**
	 * The cached value of the '{@link #getRootElement() <em>Root Element</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRootElement()
	 * @generated
	 * @ordered
	 */
	protected XamlElement rootElement;

	/**
	 * The cached value of the '{@link #getDeclaredNamespaces() <em>Declared Namespaces</em>}' map.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDeclaredNamespaces()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> declaredNamespaces;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected XamlDocumentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return XamlPackage.Literals.XAML_DOCUMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlElement getRootElement() {
		return rootElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRootElement(XamlElement newRootElement,
			NotificationChain msgs) {
		XamlElement oldRootElement = rootElement;
		rootElement = newRootElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this,
					Notification.SET, XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT,
					oldRootElement, newRootElement);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootElement(XamlElement newRootElement) {
		if (newRootElement != rootElement) {
			NotificationChain msgs = null;
			if (rootElement != null)
				msgs = ((InternalEObject) rootElement).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE
								- XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT,
						null, msgs);
			if (newRootElement != null)
				msgs = ((InternalEObject) newRootElement).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE
								- XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT,
						null, msgs);
			msgs = basicSetRootElement(newRootElement, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT, newRootElement,
					newRootElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getDeclaredNamespaces() {
		if (declaredNamespaces == null) {
			declaredNamespaces = new EcoreEMap<String, String>(
					EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY,
					EStringToStringMapEntryImpl.class, this,
					XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES);
		}
		return declaredNamespaces;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void addDeclaredNamespace(String prefix, String namespace) {
		getDeclaredNamespaces().put(prefix, namespace);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getDeclaredNamespace(String prefix) {
		return getDeclaredNamespaces().get(prefix);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT:
			return basicSetRootElement(null, msgs);
		case XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES:
			return ((InternalEList<?>) getDeclaredNamespaces()).basicRemove(
					otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT:
			return getRootElement();
		case XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES:
			if (coreType)
				return getDeclaredNamespaces();
			else
				return getDeclaredNamespaces().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT:
			setRootElement((XamlElement) newValue);
			return;
		case XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES:
			((EStructuralFeature.Setting) getDeclaredNamespaces())
					.set(newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT:
			setRootElement((XamlElement) null);
			return;
		case XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES:
			getDeclaredNamespaces().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case XamlPackage.XAML_DOCUMENT__ROOT_ELEMENT:
			return rootElement != null;
		case XamlPackage.XAML_DOCUMENT__DECLARED_NAMESPACES:
			return declaredNamespaces != null && !declaredNamespaces.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // XamlDocumentImpl
