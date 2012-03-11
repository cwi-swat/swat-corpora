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
 * $Id: StylesFactoryImpl.java,v 1.4 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.styles.impl;

import org.eclipse.e4.tm.styles.*;
import org.eclipse.e4.tm.styles.ImageItem;
import org.eclipse.e4.tm.styles.ImageResource;
import org.eclipse.e4.tm.styles.Resource;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.StyleSelector;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.styles.StylesFactory;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StylesFactoryImpl extends EFactoryImpl implements StylesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StylesFactory init() {
		try {
			StylesFactory theStylesFactory = (StylesFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/plugin/org.eclipse.e4.tm/model/tm/styles.ecore"); 
			if (theStylesFactory != null) {
				return theStylesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new StylesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StylesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case StylesPackage.STYLE_SELECTOR: return createStyleSelector();
			case StylesPackage.STYLE: return createStyle();
			case StylesPackage.STYLED: return createStyled();
			case StylesPackage.COLOR_ITEM: return createColorItem();
			case StylesPackage.IMAGE_ITEM: return createImageItem();
			case StylesPackage.FONT_ITEM: return createFontItem();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StyleSelector createStyleSelector() {
		StyleSelectorImpl styleSelector = new StyleSelectorImpl();
		return styleSelector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Style createStyle() {
		StyleImpl style = new StyleImpl();
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Styled createStyled() {
		StyledImpl styled = new StyledImpl();
		return styled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ColorItem createColorItem() {
		ColorItemImpl colorItem = new ColorItemImpl();
		return colorItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImageItem createImageItem() {
		ImageItemImpl imageItem = new ImageItemImpl();
		return imageItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FontItem createFontItem() {
		FontItemImpl fontItem = new FontItemImpl();
		return fontItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StylesPackage getStylesPackage() {
		return (StylesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static StylesPackage getPackage() {
		return StylesPackage.eINSTANCE;
	}

} //StylesFactoryImpl
