/**
 * <copyright>
 * </copyright>
 *
 * $Id: LayoutsFactory.java,v 1.3 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.layouts;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.layouts.LayoutsPackage
 * @generated
 */
public interface LayoutsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LayoutsFactory eINSTANCE = org.eclipse.e4.tm.layouts.impl.LayoutsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Layout Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Layout Data</em>'.
	 * @generated
	 */
	LayoutData createLayoutData();

	/**
	 * Returns a new object of class '<em>Layout</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Layout</em>'.
	 * @generated
	 */
	<T extends LayoutData> Layout<T> createLayout();

	/**
	 * Returns a new object of class '<em>Rectangle Layout</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rectangle Layout</em>'.
	 * @generated
	 */
	Layout createRectangleLayout();

	/**
	 * Returns a new object of class '<em>Rectangle Layout Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rectangle Layout Data</em>'.
	 * @generated
	 */
	LayoutData createRectangleLayoutData();

	/**
	 * Returns a new object of class '<em>Position</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Position</em>'.
	 * @generated
	 */
	EObject createPosition();

	/**
	 * Returns a new object of class '<em>Dimension</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dimension</em>'.
	 * @generated
	 */
	EObject createDimension();

	/**
	 * Returns a new object of class '<em>Rectangle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rectangle</em>'.
	 * @generated
	 */
	EObject createRectangle();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	LayoutsPackage getLayoutsPackage();

} //LayoutsFactory
