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
package org.eclipse.e4.xwt.tools.ui.xaml.impl;

import org.eclipse.e4.xwt.tools.ui.xaml.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * @generated
 */
public class XamlFactoryImpl extends EFactoryImpl implements XamlFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static XamlFactory init() {
		try {
			XamlFactory theXamlFactory = (XamlFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/e4/xwt/tools/designer/xaml");
			if (theXamlFactory != null) {
				return theXamlFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new XamlFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case XamlPackage.XAML_DOCUMENT:
			return createXamlDocument();
		case XamlPackage.ANNOTATED_OBJECT:
			return createAnnotatedObject();
		case XamlPackage.XAML_ELEMENT:
			return createXamlElement();
		case XamlPackage.XAML_ATTRIBUTE:
			return createXamlAttribute();
		case XamlPackage.ANNOTATION:
			return createAnnotation();
		case XamlPackage.COMMENT:
			return createComment();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case XamlPackage.NODE:
			return createNodeFromString(eDataType, initialValue);
		case XamlPackage.DOCUMENT:
			return createDocumentFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case XamlPackage.NODE:
			return convertNodeToString(eDataType, instanceValue);
		case XamlPackage.DOCUMENT:
			return convertDocumentToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '"
					+ eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlDocument createXamlDocument() {
		XamlDocumentImpl xamlDocument = new XamlDocumentImpl();
		return xamlDocument;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotatedObject createAnnotatedObject() {
		AnnotatedObjectImpl annotatedObject = new AnnotatedObjectImpl();
		return annotatedObject;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlElement createXamlElement() {
		XamlElementImpl xamlElement = new XamlElementImpl();
		return xamlElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlAttribute createXamlAttribute() {
		XamlAttributeImpl xamlAttribute = new XamlAttributeImpl();
		return xamlAttribute;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Annotation createAnnotation() {
		AnnotationImpl annotation = new AnnotationImpl();
		return annotation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Comment createComment() {
		CommentImpl comment = new CommentImpl();
		return comment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Node createNodeFromString(EDataType eDataType, String initialValue) {
		return (Node) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertNodeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Document createDocumentFromString(EDataType eDataType,
			String initialValue) {
		return (Document) super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDocumentToString(EDataType eDataType,
			Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public XamlPackage getXamlPackage() {
		return (XamlPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static XamlPackage getPackage() {
		return XamlPackage.eINSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.XamlFactory#createAttribute(java.lang.String, java.lang.String)
	 */
	public XamlAttribute createAttribute(String name, String ns) {
		XamlAttribute attr = createXamlAttribute();
		attr.setName(name);
		attr.setNamespace(ns);
		return attr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.XamlFactory#createElement(java.lang.String, java.lang.String)
	 */
	public XamlElement createElement(String name, String ns) {
		XamlElement ele = createXamlElement();
		ele.setName(name);
		ele.setNamespace(ns);
		return ele;
	}

} // XamlFactoryImpl
