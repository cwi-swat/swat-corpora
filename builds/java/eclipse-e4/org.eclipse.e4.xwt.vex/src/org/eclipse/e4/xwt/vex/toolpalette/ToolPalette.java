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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Tool Palette</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getToolPalette()
 * @model
 * @generated
 */
public interface ToolPalette extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getToolPalette_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.vex.toolpalette.ToolPalette#getName <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
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
	 * @see org.eclipse.e4.xwt.vex.toolpalette.ToolPalettePackage#getToolPalette_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entry> getEntries();

} // ToolPalette
