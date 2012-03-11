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

import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Document</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getRootElement <em>Root Element</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getDeclaredNamespaces <em>Declared Namespaces</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlDocument()
 * @model
 * @generated
 */
public interface XamlDocument extends AnnotatedObject {
	/**
	 * Returns the value of the '<em><b>Root Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Root Element</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Root Element</em>' containment reference.
	 * @see #setRootElement(XamlElement)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlDocument_RootElement()
	 * @model containment="true" required="true"
	 * @generated
	 */
	XamlElement getRootElement();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument#getRootElement <em>Root Element</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Root Element</em>' containment reference.
	 * @see #getRootElement()
	 * @generated
	 */
	void setRootElement(XamlElement value);

	/**
	 * Returns the value of the '<em><b>Declared Namespaces</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Declared Namespaces</em>' map isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Declared Namespaces</em>' map.
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlDocument_DeclaredNamespaces()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getDeclaredNamespaces();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void addDeclaredNamespace(String prefix, String namespace);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String getDeclaredNamespace(String prefix);

} // XamlDocument
