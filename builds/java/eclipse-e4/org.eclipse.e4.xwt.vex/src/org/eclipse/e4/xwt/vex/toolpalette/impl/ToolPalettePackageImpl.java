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
package org.eclipse.e4.xwt.vex.toolpalette.impl;

import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPalette;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class ToolPalettePackageImpl extends EPackageImpl implements ToolPalettePackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass toolPaletteEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass entryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EEnum contextTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also performs initialization of the package, or returns the registered package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ToolPalettePackageImpl() {
		super(eNS_URI, ToolPaletteFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends. Simple dependencies are satisfied by calling this method on all dependent packages before doing anything else. This method drives initialization for interdependent packages directly, in parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not yet been registered by their URI values are first created and registered. The packages are then initialized in two steps: meta-model objects for all of the packages are created before any are initialized, since one package's meta-model objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already been initialized. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ToolPalettePackage init() {
		if (isInited)
			return (ToolPalettePackage) EPackage.Registry.INSTANCE.getEPackage(ToolPalettePackage.eNS_URI);

		// Obtain or create and register package
		ToolPalettePackageImpl theToolPalettePackage = (ToolPalettePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ToolPalettePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ToolPalettePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theToolPalettePackage.createPackageContents();

		// Initialize created meta-data
		theToolPalettePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theToolPalettePackage.freeze();

		return theToolPalettePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getToolPalette() {
		return toolPaletteEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getToolPalette_Name() {
		return (EAttribute) toolPaletteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getToolPalette_Entries() {
		return (EReference) toolPaletteEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getEntry() {
		return entryEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_ToolTip() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_LargeIcon() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Content() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Name() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EReference getEntry_Entries() {
		return (EReference) entryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Id() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Icon() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Context() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getEntry_Scope() {
		return (EAttribute) entryEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EEnum getContextType() {
		return contextTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ToolPaletteFactory getToolPaletteFactory() {
		return (ToolPaletteFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		toolPaletteEClass = createEClass(TOOL_PALETTE);
		createEAttribute(toolPaletteEClass, TOOL_PALETTE__NAME);
		createEReference(toolPaletteEClass, TOOL_PALETTE__ENTRIES);

		entryEClass = createEClass(ENTRY);
		createEAttribute(entryEClass, ENTRY__TOOL_TIP);
		createEAttribute(entryEClass, ENTRY__LARGE_ICON);
		createEAttribute(entryEClass, ENTRY__CONTENT);
		createEAttribute(entryEClass, ENTRY__NAME);
		createEReference(entryEClass, ENTRY__ENTRIES);
		createEAttribute(entryEClass, ENTRY__ID);
		createEAttribute(entryEClass, ENTRY__ICON);
		createEAttribute(entryEClass, ENTRY__CONTEXT);
		createEAttribute(entryEClass, ENTRY__SCOPE);

		// Create enums
		contextTypeEEnum = createEEnum(CONTEXT_TYPE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(toolPaletteEClass, ToolPalette.class, "ToolPalette", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getToolPalette_Name(), ecorePackage.getEString(), "name", null, 0, 1, ToolPalette.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getToolPalette_Entries(), this.getEntry(), null, "entries", null, 0, -1, ToolPalette.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entryEClass, Entry.class, "Entry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntry_ToolTip(), ecorePackage.getEString(), "toolTip", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_LargeIcon(), ecorePackage.getEString(), "largeIcon", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Content(), ecorePackage.getEString(), "content", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Name(), ecorePackage.getEString(), "name", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntry_Entries(), this.getEntry(), null, "entries", null, 0, -1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Id(), ecorePackage.getEString(), "id", "\"\"", 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Icon(), ecorePackage.getEString(), "icon", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Context(), this.getContextType(), "context", "", 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntry_Scope(), ecorePackage.getEString(), "scope", null, 0, 1, Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(contextTypeEEnum, ContextType.class, "ContextType");
		addEEnumLiteral(contextTypeEEnum, ContextType.XML_TAG);
		addEEnumLiteral(contextTypeEEnum, ContextType.XML_ATTRIBUTE_VALUE);
		addEEnumLiteral(contextTypeEEnum, ContextType.XML_ATTRIBUTE);
		addEEnumLiteral(contextTypeEEnum, ContextType.XML_ALL);
		addEEnumLiteral(contextTypeEEnum, ContextType.XML_NEW);
		addEEnumLiteral(contextTypeEEnum, ContextType.NONE);

		// Create resource
		createResource(eNS_URI);
	}

} // ToolPalettePackageImpl
