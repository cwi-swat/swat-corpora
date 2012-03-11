/**
 * <copyright>
 * </copyright>
 *
 * $Id: Layout.java,v 1.6 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.layouts;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Layout</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.e4.tm.layouts.LayoutsPackage#getLayout()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore access='field'"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore constraints='validLayoutData'"
 * @generated
 */
public interface Layout<T extends LayoutData> extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	T createLayoutData();

} // Layout
