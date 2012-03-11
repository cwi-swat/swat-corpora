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
 * $Id: Style.java,v 1.5 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.styles.Style#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.styles.Style#getStyleItems <em>Style Items</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.styles.Style#getStyleSelectors <em>Style Selectors</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyle()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore binderClass='org.eclipse.e4.tm.builder.AbstractBinder' invalidates='Style'"
 * @generated
 */
public interface Style extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyle_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.styles.Style#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Style Items</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.tm.styles.StyleItem}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style Items</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style Items</em>' containment reference list.
	 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyle_StyleItems()
	 * @model containment="true"
	 * @generated
	 */
	EList<StyleItem> getStyleItems();

	/**
	 * Returns the value of the '<em><b>Style Selectors</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.tm.styles.StyleSelector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style Selectors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style Selectors</em>' containment reference list.
	 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyle_StyleSelectors()
	 * @model containment="true"
	 * @generated
	 */
	EList<StyleSelector> getStyleSelectors();

} // Style
