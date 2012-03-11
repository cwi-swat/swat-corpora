/**
 * <copyright>
 * </copyright>
 *
 * $Id: Labeled.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

import org.eclipse.emf.common.util.URI;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Labeled</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.Labeled#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.util.Labeled#getImage <em>Image</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.util.Labeled#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.util.UtilPackage#getLabeled()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore access='property'"
 * @generated
 */
public interface Labeled extends ObjectData {
	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getLabeled_Text()
	 * @model
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.Labeled#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

	/**
	 * Returns the value of the '<em><b>Image</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Image</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Image</em>' attribute.
	 * @see #setImage(URI)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getLabeled_Image()
	 * @model dataType="org.eclipse.e4.tm.util.URI"
	 * @generated
	 */
	URI getImage();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.Labeled#getImage <em>Image</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Image</em>' attribute.
	 * @see #getImage()
	 * @generated
	 */
	void setImage(URI value);

	/**
	 * Returns the value of the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' attribute.
	 * @see #setFormat(String)
	 * @see org.eclipse.e4.tm.util.UtilPackage#getLabeled_Format()
	 * @model annotation="http://www.eclipse.org/e4/swt.ecore invalidates='text' access='binder'"
	 * @generated
	 */
	String getFormat();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.util.Labeled#getFormat <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' attribute.
	 * @see #getFormat()
	 * @generated
	 */
	void setFormat(String value);

} // Labeled
