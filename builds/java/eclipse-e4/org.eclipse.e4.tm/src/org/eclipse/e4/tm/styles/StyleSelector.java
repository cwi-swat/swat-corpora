/**
 * <copyright>
 * </copyright>
 *
 * $Id: StyleSelector.java,v 1.2 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Style Selector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.styles.StyleSelector#getProperty <em>Property</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.styles.StyleSelector#getSelector <em>Selector</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyleSelector()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore binderClass='org.eclipse.e4.tm.builder.AbstractBinder' invalidates='Style'"
 *        annotation="http://www.eclipse.org/e4/emf/ecore/javascript/nameFeature name='property'"
 * @generated
 */
public interface StyleSelector extends EObject {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' attribute.
	 * @see #setProperty(String)
	 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyleSelector_Property()
	 * @model
	 * @generated
	 */
	String getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.styles.StyleSelector#getProperty <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' attribute.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(String value);

	/**
	 * Returns the value of the '<em><b>Selector</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selector</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selector</em>' attribute.
	 * @see #setSelector(String)
	 * @see org.eclipse.e4.tm.styles.StylesPackage#getStyleSelector_Selector()
	 * @model
	 * @generated
	 */
	String getSelector();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.styles.StyleSelector#getSelector <em>Selector</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selector</em>' attribute.
	 * @see #getSelector()
	 * @generated
	 */
	void setSelector(String value);

} // StyleSelector
