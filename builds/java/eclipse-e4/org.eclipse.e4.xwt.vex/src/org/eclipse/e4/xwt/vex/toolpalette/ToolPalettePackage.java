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
package org.eclipse.e4.xwt.vex.toolpalette;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory
 * @model kind="package"
 * @generated
 */
public interface ToolPalettePackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "toolpalette";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/ve/xml/toolpalette";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "com.soyatec.eface.tools.ui.editors.toolpalette";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	ToolPalettePackage eINSTANCE = org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.vex.toolpalette.impl.EntryImpl <em>Entry</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.EntryImpl
	 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getEntry()
	 * @generated
	 */
	int ENTRY = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPaletteImpl <em>Tool Palette</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPaletteImpl
	 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getToolPalette()
	 * @generated
	 */
	int TOOL_PALETTE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOL_PALETTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOL_PALETTE__ENTRIES = 1;

	/**
	 * The number of structural features of the '<em>Tool Palette</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOL_PALETTE_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Tool Tip</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__TOOL_TIP = 0;

	/**
	 * The feature id for the '<em><b>Large Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__LARGE_ICON = 1;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__CONTENT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__NAME = 3;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__ENTRIES = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__ID = 5;

	/**
	 * The feature id for the '<em><b>Icon</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__ICON = 6;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__CONTEXT = 7;

	/**
	 * The feature id for the '<em><b>Scope</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY__SCOPE = 8;

	/**
	 * The number of structural features of the '<em>Entry</em>' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENTRY_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.vex.toolpalette.ContextType <em>Context Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ContextType
	 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getContextType()
	 * @generated
	 */
	int CONTEXT_TYPE = 2;

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette <em>Tool Palette</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Tool Palette</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalette
	 * @generated
	 */
	EClass getToolPalette();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getName <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getName()
	 * @see #getToolPalette()
	 * @generated
	 */
	EAttribute getToolPalette_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getEntries <em>Entries</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getEntries()
	 * @see #getToolPalette()
	 * @generated
	 */
	EReference getToolPalette_Entries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry <em>Entry</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Entry</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry
	 * @generated
	 */
	EClass getEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getToolTip <em>Tool Tip</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Tool Tip</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getToolTip()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_ToolTip();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getLargeIcon <em>Large Icon</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Large Icon</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getLargeIcon()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_LargeIcon();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContent <em>Content</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Content</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getContent()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Content();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getName <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getName()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getEntries <em>Entries</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getEntries()
	 * @see #getEntry()
	 * @generated
	 */
	EReference getEntry_Entries();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getId <em>Id</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getId()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getIcon <em>Icon</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Icon</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getIcon()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Icon();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContext <em>Context</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Context</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getContext()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Context();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getScope <em>Scope</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Scope</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.Entry#getScope()
	 * @see #getEntry()
	 * @generated
	 */
	EAttribute getEntry_Scope();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.e4.xwt.vex.toolpalette.ContextType <em>Context Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Context Type</em>'.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ContextType
	 * @generated
	 */
	EEnum getContextType();

	/**
	 * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ToolPaletteFactory getToolPaletteFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPaletteImpl <em>Tool Palette</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPaletteImpl
		 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getToolPalette()
		 * @generated
		 */
		EClass TOOL_PALETTE = eINSTANCE.getToolPalette();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TOOL_PALETTE__NAME = eINSTANCE.getToolPalette_Name();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TOOL_PALETTE__ENTRIES = eINSTANCE.getToolPalette_Entries();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.vex.toolpalette.impl.EntryImpl <em>Entry</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.EntryImpl
		 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getEntry()
		 * @generated
		 */
		EClass ENTRY = eINSTANCE.getEntry();

		/**
		 * The meta object literal for the '<em><b>Tool Tip</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__TOOL_TIP = eINSTANCE.getEntry_ToolTip();

		/**
		 * The meta object literal for the '<em><b>Large Icon</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__LARGE_ICON = eINSTANCE.getEntry_LargeIcon();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__CONTENT = eINSTANCE.getEntry_Content();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__NAME = eINSTANCE.getEntry_Name();

		/**
		 * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ENTRY__ENTRIES = eINSTANCE.getEntry_Entries();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__ID = eINSTANCE.getEntry_Id();

		/**
		 * The meta object literal for the '<em><b>Icon</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__ICON = eINSTANCE.getEntry_Icon();

		/**
		 * The meta object literal for the '<em><b>Context</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__CONTEXT = eINSTANCE.getEntry_Context();

		/**
		 * The meta object literal for the '<em><b>Scope</b></em>' attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENTRY__SCOPE = eINSTANCE.getEntry_Scope();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.vex.toolpalette.ContextType <em>Context Type</em>}' enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.e4.xwt.vex.toolpalette.ContextType
		 * @see org.eclipse.e4.xwt.vex.toolpalette.impl.ToolPalettePackageImpl#getContextType()
		 * @generated
		 */
		EEnum CONTEXT_TYPE = eINSTANCE.getContextType();

	}

} // ToolPalettePackage
