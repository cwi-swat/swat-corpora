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

import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Attribute</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl#isUseFlatValue <em>Use Flat Value</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlAttributeImpl#getGroupName <em>Group Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XamlAttributeImpl extends XamlNodeImpl implements XamlAttribute {

	/**
	 * The default value of the '{@link #isUseFlatValue() <em>Use Flat Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isUseFlatValue()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_FLAT_VALUE_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isUseFlatValue() <em>Use Flat Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isUseFlatValue()
	 * @generated
	 * @ordered
	 */
	protected boolean useFlatValue = USE_FLAT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGroupName() <em>Group Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupName()
	 * @generated
	 * @ordered
	 */
	protected static final String GROUP_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getGroupName() <em>Group Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupName()
	 * @generated
	 * @ordered
	 */
	protected String groupName = GROUP_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected XamlAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return XamlPackage.Literals.XAML_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseFlatValue() {
		return useFlatValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseFlatValue(boolean newUseFlatValue) {
		boolean oldUseFlatValue = useFlatValue;
		useFlatValue = newUseFlatValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_ATTRIBUTE__USE_FLAT_VALUE,
					oldUseFlatValue, useFlatValue));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setGroupName(String newGroupName) {
		if (equals(groupName, newGroupName)) {
			return;
		}
		String oldGroupName = groupName;
		groupName = newGroupName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_ATTRIBUTE__GROUP_NAME, oldGroupName,
					groupName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case XamlPackage.XAML_ATTRIBUTE__USE_FLAT_VALUE:
			return isUseFlatValue();
		case XamlPackage.XAML_ATTRIBUTE__GROUP_NAME:
			return getGroupName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case XamlPackage.XAML_ATTRIBUTE__USE_FLAT_VALUE:
			setUseFlatValue((Boolean) newValue);
			return;
		case XamlPackage.XAML_ATTRIBUTE__GROUP_NAME:
			setGroupName((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case XamlPackage.XAML_ATTRIBUTE__USE_FLAT_VALUE:
			setUseFlatValue(USE_FLAT_VALUE_EDEFAULT);
			return;
		case XamlPackage.XAML_ATTRIBUTE__GROUP_NAME:
			setGroupName(GROUP_NAME_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case XamlPackage.XAML_ATTRIBUTE__USE_FLAT_VALUE:
			return useFlatValue != USE_FLAT_VALUE_EDEFAULT;
		case XamlPackage.XAML_ATTRIBUTE__GROUP_NAME:
			return GROUP_NAME_EDEFAULT == null ? groupName != null
					: !GROUP_NAME_EDEFAULT.equals(groupName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (useFlatValue: ");
		result.append(useFlatValue);
		result.append(", groupName: ");
		result.append(groupName);
		result.append(')');
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.Generation#generate(org.w3c.dom.Document, org.w3c.dom.Node, java.util.Map)
	 */
	public Node generate(Document document, Map<String, Object> options) {
		if (document == null) {
			return null;
		}
		Node attrNode = null;
		String name = getName();
		String value = getValue();
		if (value != null) {// Just create a simple attribute.
			attrNode = document.createAttributeNS(getNamespace(),
					getQualifiedName());
			attrNode.setNodeValue(value);
		} else if (isUseFlatValue()) {
			attrNode = document.createAttributeNS(getNamespace(),
					getQualifiedName());
			attrNode.setNodeValue("{" + getFlatValue() + "}");
		} else if (!getAttributes().isEmpty() || !getChildNodes().isEmpty()) {
			// 1. <Control.layoutData><GridData/></Control.layoutData>
			// 2. <TableViewer.table.layoutData><GridData/></TableViewer.table.layoutData>
			// 3. <TableViewer.table HeadVisible="false"/>
			String parentName = null;
			XamlNode parentElement = null;
			if (getParent() != null) {
				parentElement = getParent();
				while (parentElement != null) {
					if (parentName == null) {
						parentName = parentElement.getName();
					} else {
						parentName = parentElement.getName() + "." + parentName;
					}
					if (parentElement instanceof XamlElement) {
						break;
					}
					parentElement = parentElement.getParent();
				}
			}
			if (parentName != null) {
				name = parentName + "." + name;
			}
			attrNode = document.createElementNS(namespace,
					getQualifiedName(name));
			for (XamlAttribute childAttr : getAttributes()) {
				Node newAttr = childAttr.generate(document, options);
				if (newAttr == null) {
					continue;
				}
				if (newAttr instanceof Attr) {
					((Element) attrNode).setAttributeNode((Attr) newAttr);
				} else {
					((Element) attrNode).appendChild(newAttr);
				}
			}
			for (XamlElement childEle : getChildNodes()) {
				Node newChild = childEle.generate(document, options);
				if (newChild != null) {
					attrNode.appendChild(newChild);
				}
			}
		}
		return attrNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.xaml.XamlNode#getFlatValue()
	 */
	public String getFlatValue() {
		EList<XamlElement> childNodes = getChildNodes();
		if (childNodes.isEmpty()) {
			return getValue();
		}
		return getChildNodes().get(0).getFlatValue();
	}

} // XamlAttributeImpl
