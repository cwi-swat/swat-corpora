/**
 * <copyright>
 * </copyright>
 *
 * $Id: FeatureNames.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Names</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.util.FeatureNames#getFeatureNames <em>Feature Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.util.UtilPackage#getFeatureNames()
 * @model
 * @generated
 */
public interface FeatureNames extends EObject {
	/**
	 * Returns the value of the '<em><b>Feature Names</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Names</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Names</em>' attribute list.
	 * @see org.eclipse.e4.tm.util.UtilPackage#getFeatureNames_FeatureNames()
	 * @model
	 * @generated
	 */
	EList<String> getFeatureNames();

} // FeatureNames
