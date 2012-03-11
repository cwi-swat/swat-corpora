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
 * $Id: StylesPackage.java,v 1.4 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles;

import org.eclipse.emf.ecore.EAttribute;
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
 * @see org.eclipse.e4.tm.styles.StylesFactory
 * @model kind="package"
 * @generated
 */
public interface StylesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "styles";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/plugin/org.eclipse.e4.tm/model/tm/styles.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tm.styles";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StylesPackage eINSTANCE = org.eclipse.e4.tm.styles.impl.StylesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.StyleItemImpl <em>Style Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.StyleItemImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyleItem()
	 * @generated
	 */
	int STYLE_ITEM = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_ITEM__NAME = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_ITEM__SOURCE = 1;

	/**
	 * The number of structural features of the '<em>Style Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_ITEM_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.StyleSelectorImpl <em>Style Selector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.StyleSelectorImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyleSelector()
	 * @generated
	 */
	int STYLE_SELECTOR = 1;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_SELECTOR__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Selector</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_SELECTOR__SELECTOR = 1;

	/**
	 * The number of structural features of the '<em>Style Selector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_SELECTOR_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.StyleImpl <em>Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.StyleImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyle()
	 * @generated
	 */
	int STYLE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Style Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE__STYLE_ITEMS = 1;

	/**
	 * The feature id for the '<em><b>Style Selectors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE__STYLE_SELECTORS = 2;

	/**
	 * The number of structural features of the '<em>Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.StyledImpl <em>Styled</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.StyledImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyled()
	 * @generated
	 */
	int STYLED = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLED__NAME = 0;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLED__ROLE = 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLED__STYLE = 2;

	/**
	 * The number of structural features of the '<em>Styled</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLED_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.ColorItemImpl <em>Color Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.ColorItemImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getColorItem()
	 * @generated
	 */
	int COLOR_ITEM = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLOR_ITEM__NAME = STYLE_ITEM__NAME;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLOR_ITEM__SOURCE = STYLE_ITEM__SOURCE;

	/**
	 * The number of structural features of the '<em>Color Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLOR_ITEM_FEATURE_COUNT = STYLE_ITEM_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.ImageItemImpl <em>Image Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.ImageItemImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getImageItem()
	 * @generated
	 */
	int IMAGE_ITEM = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_ITEM__NAME = STYLE_ITEM__NAME;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_ITEM__SOURCE = STYLE_ITEM__SOURCE;

	/**
	 * The number of structural features of the '<em>Image Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_ITEM_FEATURE_COUNT = STYLE_ITEM_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.styles.impl.FontItemImpl <em>Font Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.styles.impl.FontItemImpl
	 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getFontItem()
	 * @generated
	 */
	int FONT_ITEM = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_ITEM__NAME = STYLE_ITEM__NAME;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_ITEM__SOURCE = STYLE_ITEM__SOURCE;

	/**
	 * The number of structural features of the '<em>Font Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_ITEM_FEATURE_COUNT = STYLE_ITEM_FEATURE_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.StyleItem <em>Style Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Style Item</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleItem
	 * @generated
	 */
	EClass getStyleItem();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.StyleItem#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleItem#getName()
	 * @see #getStyleItem()
	 * @generated
	 */
	EAttribute getStyleItem_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.StyleItem#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleItem#getSource()
	 * @see #getStyleItem()
	 * @generated
	 */
	EAttribute getStyleItem_Source();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.StyleSelector <em>Style Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Style Selector</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleSelector
	 * @generated
	 */
	EClass getStyleSelector();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.StyleSelector#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleSelector#getProperty()
	 * @see #getStyleSelector()
	 * @generated
	 */
	EAttribute getStyleSelector_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.StyleSelector#getSelector <em>Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selector</em>'.
	 * @see org.eclipse.e4.tm.styles.StyleSelector#getSelector()
	 * @see #getStyleSelector()
	 * @generated
	 */
	EAttribute getStyleSelector_Selector();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.Style <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Style</em>'.
	 * @see org.eclipse.e4.tm.styles.Style
	 * @generated
	 */
	EClass getStyle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.Style#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.tm.styles.Style#getName()
	 * @see #getStyle()
	 * @generated
	 */
	EAttribute getStyle_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.tm.styles.Style#getStyleItems <em>Style Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Style Items</em>'.
	 * @see org.eclipse.e4.tm.styles.Style#getStyleItems()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_StyleItems();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.tm.styles.Style#getStyleSelectors <em>Style Selectors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Style Selectors</em>'.
	 * @see org.eclipse.e4.tm.styles.Style#getStyleSelectors()
	 * @see #getStyle()
	 * @generated
	 */
	EReference getStyle_StyleSelectors();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.Styled <em>Styled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Styled</em>'.
	 * @see org.eclipse.e4.tm.styles.Styled
	 * @generated
	 */
	EClass getStyled();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.Styled#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.tm.styles.Styled#getName()
	 * @see #getStyled()
	 * @generated
	 */
	EAttribute getStyled_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.styles.Styled#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see org.eclipse.e4.tm.styles.Styled#getRole()
	 * @see #getStyled()
	 * @generated
	 */
	EAttribute getStyled_Role();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.e4.tm.styles.Styled#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.e4.tm.styles.Styled#getStyle()
	 * @see #getStyled()
	 * @generated
	 */
	EReference getStyled_Style();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.ColorItem <em>Color Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Color Item</em>'.
	 * @see org.eclipse.e4.tm.styles.ColorItem
	 * @generated
	 */
	EClass getColorItem();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.ImageItem <em>Image Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Image Item</em>'.
	 * @see org.eclipse.e4.tm.styles.ImageItem
	 * @generated
	 */
	EClass getImageItem();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.styles.FontItem <em>Font Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Font Item</em>'.
	 * @see org.eclipse.e4.tm.styles.FontItem
	 * @generated
	 */
	EClass getFontItem();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	StylesFactory getStylesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.StyleItemImpl <em>Style Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.StyleItemImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyleItem()
		 * @generated
		 */
		EClass STYLE_ITEM = eINSTANCE.getStyleItem();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLE_ITEM__NAME = eINSTANCE.getStyleItem_Name();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLE_ITEM__SOURCE = eINSTANCE.getStyleItem_Source();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.StyleSelectorImpl <em>Style Selector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.StyleSelectorImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyleSelector()
		 * @generated
		 */
		EClass STYLE_SELECTOR = eINSTANCE.getStyleSelector();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLE_SELECTOR__PROPERTY = eINSTANCE.getStyleSelector_Property();

		/**
		 * The meta object literal for the '<em><b>Selector</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLE_SELECTOR__SELECTOR = eINSTANCE.getStyleSelector_Selector();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.StyleImpl <em>Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.StyleImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyle()
		 * @generated
		 */
		EClass STYLE = eINSTANCE.getStyle();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLE__NAME = eINSTANCE.getStyle_Name();

		/**
		 * The meta object literal for the '<em><b>Style Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STYLE__STYLE_ITEMS = eINSTANCE.getStyle_StyleItems();

		/**
		 * The meta object literal for the '<em><b>Style Selectors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STYLE__STYLE_SELECTORS = eINSTANCE.getStyle_StyleSelectors();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.StyledImpl <em>Styled</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.StyledImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getStyled()
		 * @generated
		 */
		EClass STYLED = eINSTANCE.getStyled();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLED__NAME = eINSTANCE.getStyled_Name();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STYLED__ROLE = eINSTANCE.getStyled_Role();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STYLED__STYLE = eINSTANCE.getStyled_Style();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.ColorItemImpl <em>Color Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.ColorItemImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getColorItem()
		 * @generated
		 */
		EClass COLOR_ITEM = eINSTANCE.getColorItem();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.ImageItemImpl <em>Image Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.ImageItemImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getImageItem()
		 * @generated
		 */
		EClass IMAGE_ITEM = eINSTANCE.getImageItem();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.styles.impl.FontItemImpl <em>Font Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.styles.impl.FontItemImpl
		 * @see org.eclipse.e4.tm.styles.impl.StylesPackageImpl#getFontItem()
		 * @generated
		 */
		EClass FONT_ITEM = eINSTANCE.getFontItem();

	}

} //StylesPackage
