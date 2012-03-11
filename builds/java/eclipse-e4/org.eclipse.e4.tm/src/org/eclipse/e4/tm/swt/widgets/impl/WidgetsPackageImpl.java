/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: WidgetsPackageImpl.java,v 1.3 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.swt.widgets.impl;

import org.eclipse.e4.tm.layouts.LayoutsPackage;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.swt.layouts.impl.LayoutsPackageImpl;
import org.eclipse.e4.tm.swt.styles.impl.StylesPackageImpl;
import org.eclipse.e4.tm.swt.widgets.Scale;
import org.eclipse.e4.tm.swt.widgets.Slider;
import org.eclipse.e4.tm.swt.widgets.Spinner;
import org.eclipse.e4.tm.swt.widgets.WidgetsFactory;
import org.eclipse.e4.tm.swt.widgets.WidgetsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WidgetsPackageImpl extends EPackageImpl implements WidgetsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sliderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spinnerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scaleEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.e4.tm.swt.widgets.WidgetsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WidgetsPackageImpl() {
		super(eNS_URI, WidgetsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link WidgetsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WidgetsPackage init() {
		if (isInited) return (WidgetsPackage)EPackage.Registry.INSTANCE.getEPackage(WidgetsPackage.eNS_URI);

		// Obtain or create and register package
		WidgetsPackageImpl theWidgetsPackage = (WidgetsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WidgetsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new WidgetsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		org.eclipse.e4.tm.widgets.WidgetsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theWidgetsPackage.createPackageContents();

		// Initialize created meta-data
		theWidgetsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWidgetsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WidgetsPackage.eNS_URI, theWidgetsPackage);
		return theWidgetsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSlider() {
		return sliderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpinner() {
		return spinnerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScale() {
		return scaleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WidgetsFactory getWidgetsFactory() {
		return (WidgetsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		sliderEClass = createEClass(SLIDER);

		spinnerEClass = createEClass(SPINNER);

		scaleEClass = createEClass(SCALE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		org.eclipse.e4.tm.widgets.WidgetsPackage theWidgetsPackage_1 = (org.eclipse.e4.tm.widgets.WidgetsPackage)EPackage.Registry.INSTANCE.getEPackage(org.eclipse.e4.tm.widgets.WidgetsPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		EGenericType g1 = createEGenericType(theWidgetsPackage_1.getBoundedValueControl());
		EGenericType g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		sliderEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theWidgetsPackage_1.getBoundedValueControl());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		spinnerEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theWidgetsPackage_1.getBoundedValueControl());
		g2 = createEGenericType(ecorePackage.getEIntegerObject());
		g1.getETypeArguments().add(g2);
		scaleEClass.getEGenericSuperTypes().add(g1);

		// Initialize classes and features; add operations and parameters
		initEClass(sliderEClass, Slider.class, "Slider", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(spinnerEClass, Spinner.class, "Spinner", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(scaleEClass, Scale.class, "Scale", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/e4/swt.ecore
		createSwtAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/swt.ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createSwtAnnotations() {
		String source = "http://www.eclipse.org/e4/swt.ecore";		
		addAnnotation
		  (this, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.widgets"
		   });
	}

} //WidgetsPackageImpl
