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
/**
 * <copyright>
 * </copyright>
 *
 * $Id: StyleImpl.java,v 1.5 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles.impl;

import java.util.Collection;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.StyleItem;
import org.eclipse.e4.tm.styles.StyleSelector;
import org.eclipse.e4.tm.styles.StylesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Style</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.styles.impl.StyleImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.styles.impl.StyleImpl#getStyleItems <em>Style Items</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.styles.impl.StyleImpl#getStyleSelectors <em>Style Selectors</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StyleImpl extends EObjectImpl implements Style {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStyleItems() <em>Style Items</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyleItems()
	 * @generated
	 * @ordered
	 */
	protected EList<StyleItem> styleItems;

	/**
	 * The cached value of the '{@link #getStyleSelectors() <em>Style Selectors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyleSelectors()
	 * @generated
	 * @ordered
	 */
	protected EList<StyleSelector> styleSelectors;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StyleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StylesPackage.Literals.STYLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StylesPackage.STYLE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StyleItem> getStyleItems() {
		if (styleItems == null) {
			styleItems = new EObjectContainmentEList<StyleItem>(StyleItem.class, this, StylesPackage.STYLE__STYLE_ITEMS);
		}
		return styleItems;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StyleSelector> getStyleSelectors() {
		if (styleSelectors == null) {
			styleSelectors = new EObjectContainmentEList<StyleSelector>(StyleSelector.class, this, StylesPackage.STYLE__STYLE_SELECTORS);
		}
		return styleSelectors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case StylesPackage.STYLE__STYLE_ITEMS:
				return ((InternalEList<?>)getStyleItems()).basicRemove(otherEnd, msgs);
			case StylesPackage.STYLE__STYLE_SELECTORS:
				return ((InternalEList<?>)getStyleSelectors()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case StylesPackage.STYLE__NAME:
				return getName();
			case StylesPackage.STYLE__STYLE_ITEMS:
				return getStyleItems();
			case StylesPackage.STYLE__STYLE_SELECTORS:
				return getStyleSelectors();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case StylesPackage.STYLE__NAME:
				setName((String)newValue);
				return;
			case StylesPackage.STYLE__STYLE_ITEMS:
				getStyleItems().clear();
				getStyleItems().addAll((Collection<? extends StyleItem>)newValue);
				return;
			case StylesPackage.STYLE__STYLE_SELECTORS:
				getStyleSelectors().clear();
				getStyleSelectors().addAll((Collection<? extends StyleSelector>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case StylesPackage.STYLE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case StylesPackage.STYLE__STYLE_ITEMS:
				getStyleItems().clear();
				return;
			case StylesPackage.STYLE__STYLE_SELECTORS:
				getStyleSelectors().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case StylesPackage.STYLE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case StylesPackage.STYLE__STYLE_ITEMS:
				return styleItems != null && !styleItems.isEmpty();
			case StylesPackage.STYLE__STYLE_SELECTORS:
				return styleSelectors != null && !styleSelectors.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //StyleImpl
