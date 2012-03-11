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
package org.eclipse.e4.xwt.tools.ui.xaml;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory
 * @model kind="package"
 * @generated
 */
public interface XamlPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "xaml";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/e4/xwt/tools/designer/xaml";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "xaml";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	XamlPackage eINSTANCE = org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotatedObjectImpl <em>Annotated Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotatedObjectImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getAnnotatedObject()
	 * @generated
	 */
	int ANNOTATED_OBJECT = 1;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_OBJECT__ANNOTATIONS = 0;

	/**
	 * The number of structural features of the '<em>Annotated Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_OBJECT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl <em>Document</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlDocument()
	 * @generated
	 */
	int XAML_DOCUMENT = 0;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_DOCUMENT__ANNOTATIONS = ANNOTATED_OBJECT__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Root Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_DOCUMENT__ROOT_ELEMENT = ANNOTATED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Declared Namespaces</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_DOCUMENT__DECLARED_NAMESPACES = ANNOTATED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Document</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_DOCUMENT_FEATURE_COUNT = ANNOTATED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlNode()
	 * @generated
	 */
	int XAML_NODE = 2;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__ANNOTATIONS = ANNOTATED_OBJECT__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__NAME = ANNOTATED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__PREFIX = ANNOTATED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__NAMESPACE = ANNOTATED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__VALUE = ANNOTATED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Child Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__CHILD_NODES = ANNOTATED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__ATTRIBUTES = ANNOTATED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__ID = ANNOTATED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Comments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__COMMENTS = ANNOTATED_OBJECT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Widget</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE__WIDGET = ANNOTATED_OBJECT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_NODE_FEATURE_COUNT = ANNOTATED_OBJECT_FEATURE_COUNT + 9;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlElementImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlElement()
	 * @generated
	 */
	int XAML_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__ANNOTATIONS = XAML_NODE__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__NAME = XAML_NODE__NAME;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__PREFIX = XAML_NODE__PREFIX;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__NAMESPACE = XAML_NODE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__VALUE = XAML_NODE__VALUE;

	/**
	 * The feature id for the '<em><b>Child Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__CHILD_NODES = XAML_NODE__CHILD_NODES;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__ATTRIBUTES = XAML_NODE__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__ID = XAML_NODE__ID;

	/**
	 * The feature id for the '<em><b>Comments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__COMMENTS = XAML_NODE__COMMENTS;

	/**
	 * The feature id for the '<em><b>Widget</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT__WIDGET = XAML_NODE__WIDGET;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ELEMENT_FEATURE_COUNT = XAML_NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlAttribute()
	 * @generated
	 */
	int XAML_ATTRIBUTE = 4;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__ANNOTATIONS = XAML_NODE__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__NAME = XAML_NODE__NAME;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__PREFIX = XAML_NODE__PREFIX;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__NAMESPACE = XAML_NODE__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__VALUE = XAML_NODE__VALUE;

	/**
	 * The feature id for the '<em><b>Child Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__CHILD_NODES = XAML_NODE__CHILD_NODES;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__ATTRIBUTES = XAML_NODE__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__ID = XAML_NODE__ID;

	/**
	 * The feature id for the '<em><b>Comments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__COMMENTS = XAML_NODE__COMMENTS;

	/**
	 * The feature id for the '<em><b>Widget</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__WIDGET = XAML_NODE__WIDGET;

	/**
	 * The feature id for the '<em><b>Use Flat Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__USE_FLAT_VALUE = XAML_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Group Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE__GROUP_NAME = XAML_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XAML_ATTRIBUTE_FEATURE_COUNT = XAML_NODE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotationImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 5;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Details</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__DETAILS = 1;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.CommentImpl <em>Comment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.CommentImpl
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getComment()
	 * @generated
	 */
	int COMMENT = 6;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMENT__CONTENT = 0;

	/**
	 * The feature id for the '<em><b>Prev</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMENT__PREV = 1;

	/**
	 * The feature id for the '<em><b>Next</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMENT__NEXT = 2;

	/**
	 * The number of structural features of the '<em>Comment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '<em>Node</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.w3c.dom.Node
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 7;

	/**
	 * The meta object id for the '<em>Document</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.w3c.dom.Document
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getDocument()
	 * @generated
	 */
	int DOCUMENT = 8;

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument <em>Document</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument
	 * @generated
	 */
	EClass getXamlDocument();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getRootElement <em>Root Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Root Element</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getRootElement()
	 * @see #getXamlDocument()
	 * @generated
	 */
	EReference getXamlDocument_RootElement();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getDeclaredNamespaces <em>Declared Namespaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Declared Namespaces</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getDeclaredNamespaces()
	 * @see #getXamlDocument()
	 * @generated
	 */
	EReference getXamlDocument_DeclaredNamespaces();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.AnnotatedObject <em>Annotated Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Object</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.AnnotatedObject
	 * @generated
	 */
	EClass getAnnotatedObject();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.tools.ui.xaml.AnnotatedObject#getAnnotations <em>Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Annotations</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.AnnotatedObject#getAnnotations()
	 * @see #getAnnotatedObject()
	 * @generated
	 */
	EReference getAnnotatedObject_Annotations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode
	 * @generated
	 */
	EClass getXamlNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getName()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getPrefix <em>Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getPrefix()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Prefix();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getNamespace()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Namespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getValue()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Value();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getChildNodes <em>Child Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Child Nodes</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getChildNodes()
	 * @see #getXamlNode()
	 * @generated
	 */
	EReference getXamlNode_ChildNodes();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getAttributes()
	 * @see #getXamlNode()
	 * @generated
	 */
	EReference getXamlNode_Attributes();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getId()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Id();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getComments <em>Comments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Comments</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getComments()
	 * @see #getXamlNode()
	 * @generated
	 */
	EReference getXamlNode_Comments();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getWidget <em>Widget</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Widget</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlNode#getWidget()
	 * @see #getXamlNode()
	 * @generated
	 */
	EAttribute getXamlNode_Widget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlElement
	 * @generated
	 */
	EClass getXamlElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute
	 * @generated
	 */
	EClass getXamlAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#isUseFlatValue <em>Use Flat Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Flat Value</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#isUseFlatValue()
	 * @see #getXamlAttribute()
	 * @generated
	 */
	EAttribute getXamlAttribute_UseFlatValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#getGroupName <em>Group Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Name</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#getGroupName()
	 * @see #getXamlAttribute()
	 * @generated
	 */
	EAttribute getXamlAttribute_GroupName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.Annotation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Annotation#getSource()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Source();

	/**
	 * Returns the meta object for the map '{@link org.eclipse.e4.xwt.tools.ui.xaml.Annotation#getDetails <em>Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Details</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Annotation#getDetails()
	 * @see #getAnnotation()
	 * @generated
	 */
	EReference getAnnotation_Details();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Comment</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Comment
	 * @generated
	 */
	EClass getComment();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getContent <em>Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Comment#getContent()
	 * @see #getComment()
	 * @generated
	 */
	EAttribute getComment_Content();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getPrev <em>Prev</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Prev</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Comment#getPrev()
	 * @see #getComment()
	 * @generated
	 */
	EReference getComment_Prev();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getNext <em>Next</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Next</em>'.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.Comment#getNext()
	 * @see #getComment()
	 * @generated
	 */
	EReference getComment_Next();

	/**
	 * Returns the meta object for data type '{@link org.w3c.dom.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Node</em>'.
	 * @see org.w3c.dom.Node
	 * @model instanceClass="org.w3c.dom.Node"
	 * @generated
	 */
	EDataType getNode();

	/**
	 * Returns the meta object for data type '{@link org.w3c.dom.Document <em>Document</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Document</em>'.
	 * @see org.w3c.dom.Document
	 * @model instanceClass="org.w3c.dom.Document"
	 * @generated
	 */
	EDataType getDocument();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	XamlFactory getXamlFactory();

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
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl <em>Document</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlDocumentImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlDocument()
		 * @generated
		 */
		EClass XAML_DOCUMENT = eINSTANCE.getXamlDocument();

		/**
		 * The meta object literal for the '<em><b>Root Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XAML_DOCUMENT__ROOT_ELEMENT = eINSTANCE
				.getXamlDocument_RootElement();

		/**
		 * The meta object literal for the '<em><b>Declared Namespaces</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XAML_DOCUMENT__DECLARED_NAMESPACES = eINSTANCE
				.getXamlDocument_DeclaredNamespaces();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotatedObjectImpl <em>Annotated Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotatedObjectImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getAnnotatedObject()
		 * @generated
		 */
		EClass ANNOTATED_OBJECT = eINSTANCE.getAnnotatedObject();

		/**
		 * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATED_OBJECT__ANNOTATIONS = eINSTANCE
				.getAnnotatedObject_Annotations();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlNode()
		 * @generated
		 */
		EClass XAML_NODE = eINSTANCE.getXamlNode();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__NAME = eINSTANCE.getXamlNode_Name();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__PREFIX = eINSTANCE.getXamlNode_Prefix();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__NAMESPACE = eINSTANCE.getXamlNode_Namespace();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__VALUE = eINSTANCE.getXamlNode_Value();

		/**
		 * The meta object literal for the '<em><b>Child Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XAML_NODE__CHILD_NODES = eINSTANCE.getXamlNode_ChildNodes();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XAML_NODE__ATTRIBUTES = eINSTANCE.getXamlNode_Attributes();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__ID = eINSTANCE.getXamlNode_Id();

		/**
		 * The meta object literal for the '<em><b>Comments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XAML_NODE__COMMENTS = eINSTANCE.getXamlNode_Comments();

		/**
		 * The meta object literal for the '<em><b>Widget</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_NODE__WIDGET = eINSTANCE.getXamlNode_Widget();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlElementImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlElement()
		 * @generated
		 */
		EClass XAML_ELEMENT = eINSTANCE.getXamlElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getXamlAttribute()
		 * @generated
		 */
		EClass XAML_ATTRIBUTE = eINSTANCE.getXamlAttribute();

		/**
		 * The meta object literal for the '<em><b>Use Flat Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_ATTRIBUTE__USE_FLAT_VALUE = eINSTANCE
				.getXamlAttribute_UseFlatValue();

		/**
		 * The meta object literal for the '<em><b>Group Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute XAML_ATTRIBUTE__GROUP_NAME = eINSTANCE
				.getXamlAttribute_GroupName();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.AnnotationImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__SOURCE = eINSTANCE.getAnnotation_Source();

		/**
		 * The meta object literal for the '<em><b>Details</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION__DETAILS = eINSTANCE.getAnnotation_Details();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.CommentImpl <em>Comment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.CommentImpl
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getComment()
		 * @generated
		 */
		EClass COMMENT = eINSTANCE.getComment();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMENT__CONTENT = eINSTANCE.getComment_Content();

		/**
		 * The meta object literal for the '<em><b>Prev</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMENT__PREV = eINSTANCE.getComment_Prev();

		/**
		 * The meta object literal for the '<em><b>Next</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMENT__NEXT = eINSTANCE.getComment_Next();

		/**
		 * The meta object literal for the '<em>Node</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.w3c.dom.Node
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getNode()
		 * @generated
		 */
		EDataType NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em>Document</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.w3c.dom.Document
		 * @see org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlPackageImpl#getDocument()
		 * @generated
		 */
		EDataType DOCUMENT = eINSTANCE.getDocument();

	}

} //XamlPackage
