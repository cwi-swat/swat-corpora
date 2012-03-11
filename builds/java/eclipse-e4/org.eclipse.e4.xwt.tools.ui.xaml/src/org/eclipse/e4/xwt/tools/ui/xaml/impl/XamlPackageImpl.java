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

import org.eclipse.e4.xwt.tools.ui.xaml.AnnotatedObject;
import org.eclipse.e4.xwt.tools.ui.xaml.Annotation;
import org.eclipse.e4.xwt.tools.ui.xaml.Comment;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XamlPackageImpl extends EPackageImpl implements XamlPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xamlDocumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xamlNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xamlElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xamlAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType nodeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType documentEDataType = null;

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
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private XamlPackageImpl() {
		super(eNS_URI, XamlFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link XamlPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static XamlPackage init() {
		if (isInited)
			return (XamlPackage) EPackage.Registry.INSTANCE
					.getEPackage(XamlPackage.eNS_URI);

		// Obtain or create and register package
		XamlPackageImpl theXamlPackage = (XamlPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof XamlPackageImpl ? EPackage.Registry.INSTANCE
				.get(eNS_URI)
				: new XamlPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theXamlPackage.createPackageContents();

		// Initialize created meta-data
		theXamlPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theXamlPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(XamlPackage.eNS_URI, theXamlPackage);
		return theXamlPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getXamlDocument() {
		return xamlDocumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getXamlDocument_RootElement() {
		return (EReference) xamlDocumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getXamlDocument_DeclaredNamespaces() {
		return (EReference) xamlDocumentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedObject() {
		return annotatedObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedObject_Annotations() {
		return (EReference) annotatedObjectEClass.getEStructuralFeatures().get(
				0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getXamlNode() {
		return xamlNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Name() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Prefix() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Namespace() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Value() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getXamlNode_ChildNodes() {
		return (EReference) xamlNodeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getXamlNode_Attributes() {
		return (EReference) xamlNodeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Id() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getXamlNode_Comments() {
		return (EReference) xamlNodeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlNode_Widget() {
		return (EAttribute) xamlNodeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getXamlElement() {
		return xamlElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getXamlAttribute() {
		return xamlAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlAttribute_UseFlatValue() {
		return (EAttribute) xamlAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getXamlAttribute_GroupName() {
		return (EAttribute) xamlAttributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotation() {
		return annotationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotation_Source() {
		return (EAttribute) annotationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotation_Details() {
		return (EReference) annotationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComment() {
		return commentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComment_Content() {
		return (EAttribute) commentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComment_Prev() {
		return (EReference) commentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComment_Next() {
		return (EReference) commentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getNode() {
		return nodeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDocument() {
		return documentEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XamlFactory getXamlFactory() {
		return (XamlFactory) getEFactoryInstance();
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
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		xamlDocumentEClass = createEClass(XAML_DOCUMENT);
		createEReference(xamlDocumentEClass, XAML_DOCUMENT__ROOT_ELEMENT);
		createEReference(xamlDocumentEClass, XAML_DOCUMENT__DECLARED_NAMESPACES);

		annotatedObjectEClass = createEClass(ANNOTATED_OBJECT);
		createEReference(annotatedObjectEClass, ANNOTATED_OBJECT__ANNOTATIONS);

		xamlNodeEClass = createEClass(XAML_NODE);
		createEAttribute(xamlNodeEClass, XAML_NODE__NAME);
		createEAttribute(xamlNodeEClass, XAML_NODE__PREFIX);
		createEAttribute(xamlNodeEClass, XAML_NODE__NAMESPACE);
		createEAttribute(xamlNodeEClass, XAML_NODE__VALUE);
		createEReference(xamlNodeEClass, XAML_NODE__CHILD_NODES);
		createEReference(xamlNodeEClass, XAML_NODE__ATTRIBUTES);
		createEAttribute(xamlNodeEClass, XAML_NODE__ID);
		createEReference(xamlNodeEClass, XAML_NODE__COMMENTS);
		createEAttribute(xamlNodeEClass, XAML_NODE__WIDGET);

		xamlElementEClass = createEClass(XAML_ELEMENT);

		xamlAttributeEClass = createEClass(XAML_ATTRIBUTE);
		createEAttribute(xamlAttributeEClass, XAML_ATTRIBUTE__USE_FLAT_VALUE);
		createEAttribute(xamlAttributeEClass, XAML_ATTRIBUTE__GROUP_NAME);

		annotationEClass = createEClass(ANNOTATION);
		createEAttribute(annotationEClass, ANNOTATION__SOURCE);
		createEReference(annotationEClass, ANNOTATION__DETAILS);

		commentEClass = createEClass(COMMENT);
		createEAttribute(commentEClass, COMMENT__CONTENT);
		createEReference(commentEClass, COMMENT__PREV);
		createEReference(commentEClass, COMMENT__NEXT);

		// Create data types
		nodeEDataType = createEDataType(NODE);
		documentEDataType = createEDataType(DOCUMENT);
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
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE
				.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		xamlDocumentEClass.getESuperTypes().add(this.getAnnotatedObject());
		xamlNodeEClass.getESuperTypes().add(this.getAnnotatedObject());
		xamlElementEClass.getESuperTypes().add(this.getXamlNode());
		xamlAttributeEClass.getESuperTypes().add(this.getXamlNode());

		// Initialize classes and features; add operations and parameters
		initEClass(xamlDocumentEClass, XamlDocument.class, "XamlDocument",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getXamlDocument_RootElement(), this.getXamlElement(),
				null, "rootElement", null, 1, 1, XamlDocument.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getXamlDocument_DeclaredNamespaces(), theEcorePackage
				.getEStringToStringMapEntry(), null, "declaredNamespaces",
				null, 0, -1, XamlDocument.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(xamlDocumentEClass, null,
				"addDeclaredNamespace", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "prefix", 0, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "namespace", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlDocumentEClass, ecorePackage.getEString(),
				"getDeclaredNamespace", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "prefix", 0, 1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(annotatedObjectEClass, AnnotatedObject.class,
				"AnnotatedObject", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnnotatedObject_Annotations(), this.getAnnotation(),
				null, "annotations", null, 0, -1, AnnotatedObject.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		op = addEOperation(annotatedObjectEClass, this.getAnnotation(),
				"getAnnotation", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "source", 0, 1, IS_UNIQUE,
				IS_ORDERED);

		initEClass(xamlNodeEClass, XamlNode.class, "XamlNode", IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getXamlNode_Name(), ecorePackage.getEString(), "name",
				null, 1, 1, XamlNode.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getXamlNode_Prefix(), ecorePackage.getEString(),
				"prefix", null, 1, 1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getXamlNode_Namespace(), ecorePackage.getEString(),
				"namespace", null, 1, 1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getXamlNode_Value(), ecorePackage.getEString(), "value",
				null, 0, 1, XamlNode.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getXamlNode_ChildNodes(), this.getXamlElement(), null,
				"childNodes", null, 0, -1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getXamlNode_Attributes(), this.getXamlAttribute(), null,
				"attributes", null, 0, -1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getXamlNode_Id(), ecorePackage.getEString(), "id", null,
				0, 1, XamlNode.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getXamlNode_Comments(), this.getComment(), null,
				"comments", null, 0, -1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getXamlNode_Widget(), ecorePackage.getEJavaObject(),
				"widget", null, 0, 1, XamlNode.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getXamlAttribute(),
				"getAttribute", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "namespace", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getXamlAttribute(),
				"getAttribute", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "name", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getXamlElement(), "getChild",
				0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE,
				IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "namespace", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getXamlElement(), "getChild",
				0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "name", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getXamlElement(), "getChild",
				0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "index", 0, 1, IS_UNIQUE,
				IS_ORDERED);

		addEOperation(xamlNodeEClass, this.getXamlDocument(),
				"getOwnerDocument", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(xamlNodeEClass, ecorePackage.getEString(),
				"attributeNames", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, ecorePackage.getEString(),
				"attributeNames", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "namespace", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		addEOperation(xamlNodeEClass, ecorePackage.getEString(),
				"attributeNamespaces", 0, -1, IS_UNIQUE, IS_ORDERED);

		addEOperation(xamlNodeEClass, this.getXamlNode(), "getParent", 0, 1,
				IS_UNIQUE, IS_ORDERED);

		op = addEOperation(xamlNodeEClass, this.getNode(), "generate", 0, 1,
				IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getDocument(), "document", 0, 1, IS_UNIQUE,
				IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType(ecorePackage.getEString());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(ecorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "options", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(xamlNodeEClass, ecorePackage.getEString(),
				"getFlatValue", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(xamlElementEClass, XamlElement.class, "XamlElement",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(xamlAttributeEClass, XamlAttribute.class, "XamlAttribute",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getXamlAttribute_UseFlatValue(), ecorePackage
				.getEBoolean(), "useFlatValue", null, 0, 1,
				XamlAttribute.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getXamlAttribute_GroupName(), ecorePackage.getEString(),
				"groupName", null, 0, 1, XamlAttribute.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(annotationEClass, Annotation.class, "Annotation",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotation_Source(), theEcorePackage.getEString(),
				"source", null, 0, 1, Annotation.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getAnnotation_Details(), theEcorePackage
				.getEStringToStringMapEntry(), null, "details", null, 0, -1,
				Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(commentEClass, Comment.class, "Comment", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComment_Content(), theEcorePackage.getEString(),
				"content", null, 0, 1, Comment.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getComment_Prev(), this.getXamlNode(), null, "prev",
				null, 0, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComment_Next(), this.getXamlNode(), null, "next",
				null, 0, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(nodeEDataType, Node.class, "Node", IS_SERIALIZABLE,
				!IS_GENERATED_INSTANCE_CLASS);
		initEDataType(documentEDataType, Document.class, "Document",
				IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //XamlPackageImpl
