/**
 * <copyright>
 * </copyright>
 *
 * $Id: Initializer.java,v 1.4 2010/03/16 20:44:28 yvyang Exp $
 */
package org.eclipse.e4.xwt.tools.ui.palette;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Initializer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.Initializer#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.xwt.tools.ui.palette.PalettePackage#getInitializer()
 * @model abstract="true"
 * @generated
 */
public interface Initializer extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.e4.xwt.tools.ui.palette.PalettePackage#getInitializer_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.palette.Initializer#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean initialize(Object element);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Object parse(Entry entry);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean initialize(Entry entry, Object newObject);

} // Initializer
