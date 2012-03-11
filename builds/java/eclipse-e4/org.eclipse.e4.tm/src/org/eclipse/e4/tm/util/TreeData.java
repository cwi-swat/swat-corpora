/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeData.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tree Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.TreeData#getParentDataObject <em>Parent Data Object</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.util.TreeData#isLeaf <em>Leaf</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.util.UtilPackage#getTreeData()
 * @model
 * @generated
 */
public interface TreeData extends ListData {
	/**
	 * Returns the value of the '<em><b>Parent Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Data Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Data Object</em>' attribute.
	 * @see #setParentDataObject(Object)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getTreeData_ParentDataObject()
	 * @model unique="false" transient="true"
	 * @generated
	 */
	Object getParentDataObject();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.TreeData#getParentDataObject <em>Parent Data Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Data Object</em>' attribute.
	 * @see #getParentDataObject()
	 * @generated
	 */
	void setParentDataObject(Object value);

	/**
	 * Returns the value of the '<em><b>Leaf</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Leaf</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Leaf</em>' attribute.
	 * @see #setLeaf(boolean)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getTreeData_Leaf()
	 * @model
	 * @generated
	 */
	boolean isLeaf();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.TreeData#isLeaf <em>Leaf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Leaf</em>' attribute.
	 * @see #isLeaf()
	 * @generated
	 */
	void setLeaf(boolean value);

} // TreeData
