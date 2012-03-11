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
 * $Id: List.java,v 1.4 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.List#getItems <em>Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getList()
 * @model abstract="true"
 *        annotation="http://www.eclipse.org/e4/swt.ecore javaClass='List'"
 * @generated
 */
public interface List extends Control, IndexSelection {
	/**
	 * Returns the value of the '<em><b>Items</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' attribute list.
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getList_Items()
	 * @model unique="false"
	 * @generated
	 */
	EList<String> getItems();

} // List
