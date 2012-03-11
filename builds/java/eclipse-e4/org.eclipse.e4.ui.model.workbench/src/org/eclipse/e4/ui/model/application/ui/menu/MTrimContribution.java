/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 */
package org.eclipse.e4.ui.model.application.ui.menu;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trim Contribution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution#getParentId <em>Parent Id</em>}</li>
 *   <li>{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution#getPositionInParent <em>Position In Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @model
 * @generated
 */
public interface MTrimContribution extends MElementContainer<MTrimElement> {

	/**
	 * Returns the value of the '<em><b>Parent Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Id</em>' attribute.
	 * @see #setParentId(String)
	 * @model
	 * @generated
	 */
	String getParentId();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution#getParentId <em>Parent Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Id</em>' attribute.
	 * @see #getParentId()
	 * @generated
	 */
	void setParentId(String value);

	/**
	 * Returns the value of the '<em><b>Position In Parent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Position In Parent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Position In Parent</em>' attribute.
	 * @see #setPositionInParent(String)
	 * @model
	 * @generated
	 */
	String getPositionInParent();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution#getPositionInParent <em>Position In Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position In Parent</em>' attribute.
	 * @see #getPositionInParent()
	 * @generated
	 */
	void setPositionInParent(String value);
} // MTrimContribution
