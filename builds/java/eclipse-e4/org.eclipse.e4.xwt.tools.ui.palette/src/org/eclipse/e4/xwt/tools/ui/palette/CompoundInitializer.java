/**
 * <copyright>
 * </copyright>
 *
 * $Id: CompoundInitializer.java,v 1.2 2010/03/16 20:44:28 yvyang Exp $
 */
package org.eclipse.e4.xwt.tools.ui.palette;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Compound Initializer</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.CompoundInitializer#getInitializers <em>Initializers</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.xwt.tools.ui.palette.PalettePackage#getCompoundInitializer()
 * @model
 * @generated
 */
public interface CompoundInitializer extends Initializer {
	/**
	 * Returns the value of the '<em><b>Initializers</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.e4.xwt.tools.ui.palette.Initializer}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initializers</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Initializers</em>' containment reference
	 *         list.
	 * @see org.eclipse.e4.xwt.tools.ui.palette.PalettePackage#getCompoundInitializer_Initializers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Initializer> getInitializers();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Initializer unwrap();

} // CompoundInitializer
