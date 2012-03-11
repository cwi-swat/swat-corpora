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
package org.eclipse.e4.xwt.tools.ui.palette;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Context Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.xwt.tools.ui.palette.PalettePackage#getContextType()
 * @model
 * @generated
 */
public enum ContextType implements Enumerator {
	/**
	 * The '<em><b>Xml tag</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #XML_TAG_VALUE
	 * @generated
	 * @ordered
	 */
	XML_TAG(5, "xml_tag", "xml_tag"),

	/**
	 * The '<em><b>Xml attribute value</b></em>' literal object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #XML_ATTRIBUTE_VALUE_VALUE
	 * @generated
	 * @ordered
	 */
	XML_ATTRIBUTE_VALUE(1, "xml_attribute_value", "xml_attribute_value"),

	/**
	 * The '<em><b>Xml attribute</b></em>' literal object.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #XML_ATTRIBUTE_VALUE_
	 * @generated
	 * @ordered
	 */
	XML_ATTRIBUTE(2, "xml_attribute", "xml_attribute"),

	/**
	 * The '<em><b>Xml all</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #XML_ALL_VALUE
	 * @generated
	 * @ordered
	 */
	XML_ALL(3, "xml_all", "xml_all"),

	/**
	 * The '<em><b>Xml new</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #XML_NEW_VALUE
	 * @generated
	 * @ordered
	 */
	XML_NEW(4, "xml_new", "xml_new"),

	/**
	 * The '<em><b>None</b></em>' literal object.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @see #NONE_VALUE
	 * @generated
	 * @ordered
	 */
	NONE(0, "none", "none");

	/**
	 * The '<em><b>Xml tag</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Xml tag</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #XML_TAG
	 * @model name="xml_tag"
	 * @generated
	 * @ordered
	 */
	public static final int XML_TAG_VALUE = 5;

	/**
	 * The '<em><b>Xml attribute value</b></em>' literal value. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Xml attribute value</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #XML_ATTRIBUTE_VALUE
	 * @model name="xml_attribute_value"
	 * @generated
	 * @ordered
	 */
	public static final int XML_ATTRIBUTE_VALUE_VALUE = 1;

	/**
	 * The '<em><b>Xml attribute</b></em>' literal value.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Xml attribute</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #XML_ATTRIBUTE
	 * @model name="xml_attribute"
	 * @generated
	 * @ordered
	 */
	public static final int XML_ATTRIBUTE_VALUE_ = 2;

	/**
	 * The '<em><b>Xml all</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Xml all</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #XML_ALL
	 * @model name="xml_all"
	 * @generated
	 * @ordered
	 */
	public static final int XML_ALL_VALUE = 3;

	/**
	 * The '<em><b>Xml new</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Xml new</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #XML_NEW
	 * @model name="xml_new"
	 * @generated
	 * @ordered
	 */
	public static final int XML_NEW_VALUE = 4;

	/**
	 * The '<em><b>None</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NONE
	 * @model name="none"
	 * @generated
	 * @ordered
	 */
	public static final int NONE_VALUE = 0;

	/**
	 * An array of all the '<em><b>Context Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ContextType[] VALUES_ARRAY = new ContextType[] {
			XML_TAG,
			XML_ATTRIBUTE_VALUE,
			XML_ATTRIBUTE,
			XML_ALL,
			XML_NEW,
			NONE,
		};

	/**
	 * A public read-only list of all the '<em><b>Context Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ContextType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Context Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ContextType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ContextType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Context Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ContextType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ContextType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Context Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ContextType get(int value) {
		switch (value) {
			case XML_TAG_VALUE: return XML_TAG;
			case XML_ATTRIBUTE_VALUE_VALUE: return XML_ATTRIBUTE_VALUE;
			case XML_ATTRIBUTE_VALUE_: return XML_ATTRIBUTE;
			case XML_ALL_VALUE: return XML_ALL;
			case XML_NEW_VALUE: return XML_NEW;
			case NONE_VALUE: return NONE;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private ContextType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

} // ContextType
