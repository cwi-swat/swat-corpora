/**
 * <copyright>
 * </copyright>
 *
 * $Id: ObjectData.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.ObjectData#getDataObject <em>Data Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.util.UtilPackage#getObjectData()
 * @model
 * @generated
 */
public interface ObjectData extends Scripted {
	/**
	 * Returns the value of the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Object</em>' attribute.
	 * @see #setDataObject(Object)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getObjectData_DataObject()
	 * @model unique="false" transient="true"
	 * @generated
	 */
	Object getDataObject();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.ObjectData#getDataObject <em>Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Object</em>' attribute.
	 * @see #getDataObject()
	 * @generated
	 */
	void setDataObject(Object value);

} // ObjectData
