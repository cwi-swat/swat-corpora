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

/**
 * 
 *If <True>, the value of attribute should be generated like:
 * 
 *<code><Composite dataContext="{StaticResource myData}"></code>
 * 
 *otherwise if <False>, the value should be like:
 * 
 *<code>
 * <Composite>
 * 		<Composite.dataContext>
 * 			<StaticResource>myData<StaticResource>
 * 		</Composite.dataContext>
 * </Composite>
 * </code>
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#isUseFlatValue <em>Use Flat Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlAttribute()
 * @model
 * @generated
 */
public interface XamlAttribute extends XamlNode {
	/**
	 * Returns the value of the '<em><b>Use Flat Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Flat Value</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Flat Value</em>' attribute.
	 * @see #setUseFlatValue(boolean)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlAttribute_UseFlatValue()
	 * @model
	 * @generated
	 */
	boolean isUseFlatValue();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#isUseFlatValue <em>Use Flat Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Flat Value</em>' attribute.
	 * @see #isUseFlatValue()
	 * @generated
	 */
	void setUseFlatValue(boolean value);

	/**
	 * Returns the value of the '<em><b>Group Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group Name</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group Name</em>' attribute.
	 * @see #setGroupName(String)
	 * @see org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage#getXamlAttribute_GroupName()
	 * @model
	 * @generated
	 */
	String getGroupName();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute#getGroupName <em>Group Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group Name</em>' attribute.
	 * @see #getGroupName()
	 * @generated
	 */
	void setGroupName(String value);

} // XamlAttribute
