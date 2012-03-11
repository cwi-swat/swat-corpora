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
package org.eclipse.e4.xwt.vex.toolpalette;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Entry</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getToolTip <em>Tool Tip</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getLargeIcon <em>Large Icon</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContent <em>Content</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getEntries <em>Entries</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getId <em>Id</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getIcon <em>Icon</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContext <em>Context</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getScope <em>Scope</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry()
 * @model
 * @generated
 */
public interface Entry extends EObject {
	/**
	 * Returns the value of the '<em><b>Tool Tip</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tool Tip</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Tool Tip</em>' attribute.
	 * @see #setToolTip(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_ToolTip()
	 * @model
	 * @generated
	 */
	String getToolTip();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getToolTip <em>Tool Tip</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Tool Tip</em>' attribute.
	 * @see #getToolTip()
	 * @generated
	 */
	void setToolTip(String value);

	/**
	 * Returns the value of the '<em><b>Large Icon</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Large Icon</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Large Icon</em>' attribute.
	 * @see #setLargeIcon(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_LargeIcon()
	 * @model
	 * @generated
	 */
	String getLargeIcon();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getLargeIcon <em>Large Icon</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Large Icon</em>' attribute.
	 * @see #getLargeIcon()
	 * @generated
	 */
	void setLargeIcon(String value);

	/**
	 * Returns the value of the '<em><b>Content</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Content</em>' attribute.
	 * @see #setContent(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Content()
	 * @model
	 * @generated
	 */
	String getContent();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContent <em>Content</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Content</em>' attribute.
	 * @see #getContent()
	 * @generated
	 */
	void setContent(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getName <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list. The list nameMap are of type {@link org.eclipse.e4.xwt.vex.toolpalette.Entry}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entry> getEntries();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute. The default value is <code>"\"\""</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Id()
	 * @model default="\"\"" id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getId <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Icon</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icon</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Icon</em>' attribute.
	 * @see #setIcon(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Icon()
	 * @model
	 * @generated
	 */
	String getIcon();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getIcon <em>Icon</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Icon</em>' attribute.
	 * @see #getIcon()
	 * @generated
	 */
	void setIcon(String value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' attribute. The default value is <code>""</code>. The literals are from the enumeration {@link org.eclipse.e4.xwt.vex.toolpalette.ContextType}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Context</em>' attribute.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ContextType
	 * @see #setContext(ContextType)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Context()
	 * @model default=""
	 * @generated
	 */
	ContextType getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getContext <em>Context</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Context</em>' attribute.
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ContextType
	 * @see #getContext()
	 * @generated
	 */
	void setContext(ContextType value);

	/**
	 * Returns the value of the '<em><b>Scope</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scope</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Scope</em>' attribute.
	 * @see #setScope(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getEntry_Scope()
	 * @model
	 * @generated
	 */
	String getScope();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.Entry#getScope <em>Scope</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Scope</em>' attribute.
	 * @see #getScope()
	 * @generated
	 */
	void setScope(String value);

} // Entry
