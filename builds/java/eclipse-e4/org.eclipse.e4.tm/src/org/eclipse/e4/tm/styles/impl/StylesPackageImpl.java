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
 * $Id: StylesPackageImpl.java,v 1.7 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles.impl;

import org.eclipse.e4.tm.layouts.LayoutsPackage;
import org.eclipse.e4.tm.layouts.impl.LayoutsPackageImpl;
import org.eclipse.e4.tm.styles.ColorItem;
import org.eclipse.e4.tm.styles.FontItem;
import org.eclipse.e4.tm.styles.ImageItem;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.StyleItem;
import org.eclipse.e4.tm.styles.StyleSelector;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.styles.StylesFactory;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.util.impl.UtilPackageImpl;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.e4.tm.widgets.impl.WidgetsPackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StylesPackageImpl extends EPackageImpl implements StylesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styleItemEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styleSelectorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass styledEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass colorItemEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imageItemEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fontItemEClass = null;

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
	 * @see org.eclipse.e4.tm.styles.StylesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private StylesPackageImpl() {
		super(eNS_URI, StylesFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link StylesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static StylesPackage init() {
		if (isInited) return (StylesPackage)EPackage.Registry.INSTANCE.getEPackage(StylesPackage.eNS_URI);

		// Obtain or create and register package
		StylesPackageImpl theStylesPackage = (StylesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof StylesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new StylesPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theStylesPackage.createPackageContents();

		// Initialize created meta-data
		theStylesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theStylesPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(StylesPackage.eNS_URI, theStylesPackage);
		return theStylesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyleItem() {
		return styleItemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyleItem_Name() {
		return (EAttribute)styleItemEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyleItem_Source() {
		return (EAttribute)styleItemEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyleSelector() {
		return styleSelectorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyleSelector_Property() {
		return (EAttribute)styleSelectorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyleSelector_Selector() {
		return (EAttribute)styleSelectorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyle() {
		return styleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyle_Name() {
		return (EAttribute)styleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_StyleItems() {
		return (EReference)styleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyle_StyleSelectors() {
		return (EReference)styleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStyled() {
		return styledEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyled_Name() {
		return (EAttribute)styledEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStyled_Role() {
		return (EAttribute)styledEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStyled_Style() {
		return (EReference)styledEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getColorItem() {
		return colorItemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImageItem() {
		return imageItemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFontItem() {
		return fontItemEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StylesFactory getStylesFactory() {
		return (StylesFactory)getEFactoryInstance();
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
		styleItemEClass = createEClass(STYLE_ITEM);
		createEAttribute(styleItemEClass, STYLE_ITEM__NAME);
		createEAttribute(styleItemEClass, STYLE_ITEM__SOURCE);

		styleSelectorEClass = createEClass(STYLE_SELECTOR);
		createEAttribute(styleSelectorEClass, STYLE_SELECTOR__PROPERTY);
		createEAttribute(styleSelectorEClass, STYLE_SELECTOR__SELECTOR);

		styleEClass = createEClass(STYLE);
		createEAttribute(styleEClass, STYLE__NAME);
		createEReference(styleEClass, STYLE__STYLE_ITEMS);
		createEReference(styleEClass, STYLE__STYLE_SELECTORS);

		styledEClass = createEClass(STYLED);
		createEAttribute(styledEClass, STYLED__NAME);
		createEAttribute(styledEClass, STYLED__ROLE);
		createEReference(styledEClass, STYLED__STYLE);

		colorItemEClass = createEClass(COLOR_ITEM);

		imageItemEClass = createEClass(IMAGE_ITEM);

		fontItemEClass = createEClass(FONT_ITEM);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		colorItemEClass.getESuperTypes().add(this.getStyleItem());
		imageItemEClass.getESuperTypes().add(this.getStyleItem());
		fontItemEClass.getESuperTypes().add(this.getStyleItem());

		// Initialize classes and features; add operations and parameters
		initEClass(styleItemEClass, StyleItem.class, "StyleItem", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStyleItem_Name(), ecorePackage.getEString(), "name", null, 0, 1, StyleItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStyleItem_Source(), ecorePackage.getEString(), "source", null, 0, 1, StyleItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(styleSelectorEClass, StyleSelector.class, "StyleSelector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStyleSelector_Property(), ecorePackage.getEString(), "property", null, 0, 1, StyleSelector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStyleSelector_Selector(), ecorePackage.getEString(), "selector", null, 0, 1, StyleSelector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(styleEClass, Style.class, "Style", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStyle_Name(), ecorePackage.getEString(), "name", null, 0, 1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStyle_StyleItems(), this.getStyleItem(), null, "styleItems", null, 0, -1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStyle_StyleSelectors(), this.getStyleSelector(), null, "styleSelectors", null, 0, -1, Style.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(styledEClass, Styled.class, "Styled", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStyled_Name(), ecorePackage.getEString(), "name", null, 0, 1, Styled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStyled_Role(), ecorePackage.getEString(), "role", null, 0, 1, Styled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStyled_Style(), this.getStyle(), null, "style", null, 0, 1, Styled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(colorItemEClass, ColorItem.class, "ColorItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(imageItemEClass, ImageItem.class, "ImageItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fontItemEClass, FontItem.class, "FontItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/e4/swt.ecore
		createSwtAnnotations();
		// http://www.eclipse.org/e4/emf/ecore/javascript/nameFeature
		createNameFeatureAnnotations();
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
		  (styleItemEClass, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.graphics",
			 "invalidates", "Style",
			 "binderClass", "org.eclipse.e4.tm.builder.StyleItemBinder"
		   });		
		addAnnotation
		  (getStyleItem_Source(), 
		   source, 
		   new String[] {
			 "invalidates", "Object Style"
		   });		
		addAnnotation
		  (styleSelectorEClass, 
		   source, 
		   new String[] {
			 "binderClass", "org.eclipse.e4.tm.builder.AbstractBinder",
			 "invalidates", "Style"
		   });			
		addAnnotation
		  (styleEClass, 
		   source, 
		   new String[] {
			 "binderClass", "org.eclipse.e4.tm.builder.AbstractBinder",
			 "invalidates", "Style"
		   });		
		addAnnotation
		  (getStyled_Style(), 
		   source, 
		   new String[] {
			 "access", "binder"
		   });		
		addAnnotation
		  (colorItemEClass, 
		   source, 
		   new String[] {
			 "realName", "Color"
		   });		
		addAnnotation
		  (imageItemEClass, 
		   source, 
		   new String[] {
			 "realName", "Image"
		   });		
		addAnnotation
		  (fontItemEClass, 
		   source, 
		   new String[] {
			 "realName", "Font"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/emf/ecore/javascript/nameFeature</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNameFeatureAnnotations() {
		String source = "http://www.eclipse.org/e4/emf/ecore/javascript/nameFeature";					
		addAnnotation
		  (styleSelectorEClass, 
		   source, 
		   new String[] {
			 "name", "property"
		   });					
	}

} //StylesPackageImpl
