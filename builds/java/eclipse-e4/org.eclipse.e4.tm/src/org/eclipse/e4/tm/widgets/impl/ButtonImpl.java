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
 * $Id: ButtonImpl.java,v 1.3 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.widgets.Button;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Button</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ButtonImpl#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ButtonImpl#getImage <em>Image</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ButtonImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ButtonImpl#getSelectionEvent <em>Selection Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ButtonImpl extends ControlImpl implements Button {
	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getImage() <em>Image</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImage()
	 * @generated
	 * @ordered
	 */
	protected static final URI IMAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getImage() <em>Image</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImage()
	 * @generated
	 * @ordered
	 */
	protected URI image = IMAGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String FORMAT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
	protected String format = FORMAT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected static final Object SELECTION_EVENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected Object selectionEvent = SELECTION_EVENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ButtonImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.BUTTON;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.BUTTON__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URI getImage() {
		return image;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImage(URI newImage) {
		URI oldImage = image;
		image = newImage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.BUTTON__IMAGE, oldImage, image));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormat(String newFormat) {
		String oldFormat = format;
		format = newFormat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.BUTTON__FORMAT, oldFormat, format));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getSelectionEvent() {
		return selectionEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionEvent(Object newSelectionEvent) {
		Object oldSelectionEvent = selectionEvent;
		selectionEvent = newSelectionEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.BUTTON__SELECTION_EVENT, oldSelectionEvent, selectionEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.BUTTON__TEXT:
				return getText();
			case WidgetsPackage.BUTTON__IMAGE:
				return getImage();
			case WidgetsPackage.BUTTON__FORMAT:
				return getFormat();
			case WidgetsPackage.BUTTON__SELECTION_EVENT:
				return getSelectionEvent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WidgetsPackage.BUTTON__TEXT:
				setText((String)newValue);
				return;
			case WidgetsPackage.BUTTON__IMAGE:
				setImage((URI)newValue);
				return;
			case WidgetsPackage.BUTTON__FORMAT:
				setFormat((String)newValue);
				return;
			case WidgetsPackage.BUTTON__SELECTION_EVENT:
				setSelectionEvent(newValue);
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
			case WidgetsPackage.BUTTON__TEXT:
				setText(TEXT_EDEFAULT);
				return;
			case WidgetsPackage.BUTTON__IMAGE:
				setImage(IMAGE_EDEFAULT);
				return;
			case WidgetsPackage.BUTTON__FORMAT:
				setFormat(FORMAT_EDEFAULT);
				return;
			case WidgetsPackage.BUTTON__SELECTION_EVENT:
				setSelectionEvent(SELECTION_EVENT_EDEFAULT);
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
			case WidgetsPackage.BUTTON__TEXT:
				return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
			case WidgetsPackage.BUTTON__IMAGE:
				return IMAGE_EDEFAULT == null ? image != null : !IMAGE_EDEFAULT.equals(image);
			case WidgetsPackage.BUTTON__FORMAT:
				return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
			case WidgetsPackage.BUTTON__SELECTION_EVENT:
				return SELECTION_EVENT_EDEFAULT == null ? selectionEvent != null : !SELECTION_EVENT_EDEFAULT.equals(selectionEvent);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == Labeled.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.BUTTON__TEXT: return UtilPackage.LABELED__TEXT;
				case WidgetsPackage.BUTTON__IMAGE: return UtilPackage.LABELED__IMAGE;
				case WidgetsPackage.BUTTON__FORMAT: return UtilPackage.LABELED__FORMAT;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == Labeled.class) {
			switch (baseFeatureID) {
				case UtilPackage.LABELED__TEXT: return WidgetsPackage.BUTTON__TEXT;
				case UtilPackage.LABELED__IMAGE: return WidgetsPackage.BUTTON__IMAGE;
				case UtilPackage.LABELED__FORMAT: return WidgetsPackage.BUTTON__FORMAT;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (text: ");
		result.append(text);
		result.append(", image: ");
		result.append(image);
		result.append(", format: ");
		result.append(format);
		result.append(", selectionEvent: ");
		result.append(selectionEvent);
		result.append(')');
		return result.toString();
	}

} //ButtonImpl
