/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.categorynode.node;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.xwt.tools.categorynode.node.NodeFactory
 * @model kind="package"
 * @generated
 */
public interface NodePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "node";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/e4/tools/workbench/node";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "node";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NodePackage eINSTANCE = org.eclipse.e4.xwt.tools.categorynode.node.impl.NodePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.categorynode.node.impl.CategoryNodeImpl <em>Category Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.categorynode.node.impl.CategoryNodeImpl
	 * @see org.eclipse.e4.xwt.tools.categorynode.node.impl.NodePackageImpl#getCategoryNode()
	 * @generated
	 */
	int CATEGORY_NODE = 0;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_NODE__REFERENCE = 0;

	/**
	 * The feature id for the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_NODE__OBJECT = 1;

	/**
	 * The number of structural features of the '<em>Category Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATEGORY_NODE_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode <em>Category Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category Node</em>'.
	 * @see org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode
	 * @generated
	 */
	EClass getCategoryNode();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode#getReference()
	 * @see #getCategoryNode()
	 * @generated
	 */
	EReference getCategoryNode_Reference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Object</em>'.
	 * @see org.eclipse.e4.xwt.tools.categorynode.node.CategoryNode#getObject()
	 * @see #getCategoryNode()
	 * @generated
	 */
	EReference getCategoryNode_Object();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	NodeFactory getNodeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.categorynode.node.impl.CategoryNodeImpl <em>Category Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.categorynode.node.impl.CategoryNodeImpl
		 * @see org.eclipse.e4.xwt.tools.categorynode.node.impl.NodePackageImpl#getCategoryNode()
		 * @generated
		 */
		EClass CATEGORY_NODE = eINSTANCE.getCategoryNode();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CATEGORY_NODE__REFERENCE = eINSTANCE.getCategoryNode_Reference();

		/**
		 * The meta object literal for the '<em><b>Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CATEGORY_NODE__OBJECT = eINSTANCE.getCategoryNode_Object();

	}

} //NodePackage
