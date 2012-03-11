/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilFactory.java,v 1.2 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.util.UtilPackage
 * @generated
 */
public interface UtilFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UtilFactory eINSTANCE = org.eclipse.e4.tm.util.impl.UtilFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>List Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>List Data</em>'.
	 * @generated
	 */
	ListData createListData();

	/**
	 * Returns a new object of class '<em>Tree Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tree Data</em>'.
	 * @generated
	 */
	TreeData createTreeData();

	/**
	 * Returns a new object of class '<em>Object Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object Data</em>'.
	 * @generated
	 */
	ObjectData createObjectData();

	/**
	 * Returns a new object of class '<em>Labeled</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Labeled</em>'.
	 * @generated
	 */
	Labeled createLabeled();

	/**
	 * Returns a new object of class '<em>Features List Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Features List Data</em>'.
	 * @generated
	 */
	FeaturesListData createFeaturesListData();

	/**
	 * Returns a new object of class '<em>Features Labeled</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Features Labeled</em>'.
	 * @generated
	 */
	FeaturesLabeled createFeaturesLabeled();

	/**
	 * Returns a new object of class '<em>Feature Names</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Feature Names</em>'.
	 * @generated
	 */
	FeatureNames createFeatureNames();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	UtilPackage getUtilPackage();

} //UtilFactory
