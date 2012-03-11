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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Comment</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getPrev <em>Prev</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getNext <em>Next</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getComment()
 * @model
 * @generated
 */
public interface Comment extends EObject {
	/**
	 * Returns the value of the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' attribute.
	 * @see #setContent(String)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getComment_Content()
	 * @model
	 * @generated
	 */
	String getContent();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getContent <em>Content</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content</em>' attribute.
	 * @see #getContent()
	 * @generated
	 */
	void setContent(String value);

	/**
	 * Returns the value of the '<em><b>Prev</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prev</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prev</em>' reference.
	 * @see #setPrev(XamlNode)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getComment_Prev()
	 * @model
	 * @generated
	 */
	XamlNode getPrev();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getPrev <em>Prev</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prev</em>' reference.
	 * @see #getPrev()
	 * @generated
	 */
	void setPrev(XamlNode value);

	/**
	 * Returns the value of the '<em><b>Next</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next</em>' reference.
	 * @see #setNext(XamlNode)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getComment_Next()
	 * @model
	 * @generated
	 */
	XamlNode getNext();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.Comment#getNext <em>Next</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next</em>' reference.
	 * @see #getNext()
	 * @generated
	 */
	void setNext(XamlNode value);

} // Comment
