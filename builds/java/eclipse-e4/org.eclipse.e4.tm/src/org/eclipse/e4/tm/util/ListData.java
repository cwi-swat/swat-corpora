/**
 * <copyright>
 * </copyright>
 *
 * $Id: ListData.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.ListData#getDataObjects <em>Data Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.util.UtilPackage#getListData()
 * @model
 * @generated
 */
public interface ListData extends ObjectData {
	/**
	 * Returns the value of the '<em><b>Data Objects</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Objects</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Objects</em>' attribute list.
	 * @see org.eclipse.e4.tm.util.UtilPackage#getListData_DataObjects()
	 * @model unique="false" transient="true"
	 * @generated
	 */
	EList<Object> getDataObjects();

} // ListData
