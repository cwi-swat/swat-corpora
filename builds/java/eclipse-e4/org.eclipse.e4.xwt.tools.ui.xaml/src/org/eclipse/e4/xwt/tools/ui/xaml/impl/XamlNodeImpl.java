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

import java.util.Collection;
import java.util.Map;

import org.eclipse.e4.xwt.tools.ui.xaml.Comment;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Node</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getChildNodes <em>Child Nodes</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getComments <em>Comments</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.xaml.impl.XamlNodeImpl#getWidget <em>Widget</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class XamlNodeImpl extends AnnotatedObjectImpl implements
		XamlNode {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected String prefix = PREFIX_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected String namespace = NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChildNodes() <em>Child Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getChildNodes()
	 * @generated
	 * @ordered
	 */
	protected EList<XamlElement> childNodes;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<XamlAttribute> attributes;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getComments() <em>Comments</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getComments()
	 * @generated
	 * @ordered
	 */
	protected EList<Comment> comments;

	/**
	 * The default value of the '{@link #getWidget() <em>Widget</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidget()
	 * @generated
	 * @ordered
	 */
	protected static final Object WIDGET_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWidget() <em>Widget</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidget()
	 * @generated
	 * @ordered
	 */
	protected Object widget = WIDGET_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected XamlNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return XamlPackage.Literals.XAML_NODE;
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
	 * 
	 * @generated NOT
	 */
	public void setName(String newName) {
		if (equals(name, newName)) {
			return;
		}
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getPrefix() {
		if (prefix == null && namespace != null && getOwnerDocument() != null) {
			EMap<String, String> declaredNamespaces = getOwnerDocument()
					.getDeclaredNamespaces();
			for (String key : declaredNamespaces.keySet()) {
				if (namespace.equals(declaredNamespaces.get(key))) {
					prefix = key;
					break;
				}
			}
		}
		return prefix;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setPrefix(String newPrefix) {
		if (equals(prefix, newPrefix)) {
			return;
		}
		String oldPrefix = prefix;
		prefix = newPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__PREFIX, oldPrefix, prefix));
	}

	boolean equals(Object source, Object target) {
		return (source == null && target == null)
				|| (source != null && source.equals(target));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setNamespace(String newNamespace) {
		if (equals(name, newNamespace)) {
			return;
		}
		String oldNamespace = namespace;
		namespace = newNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__NAMESPACE, oldNamespace, namespace));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setValue(String newValue) {
		if (equals(value, newValue)) {
			return;
		}
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<XamlElement> getChildNodes() {
		if (childNodes == null) {
			childNodes = new EObjectContainmentEList<XamlElement>(
					XamlElement.class, this, XamlPackage.XAML_NODE__CHILD_NODES);
		}
		return childNodes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<XamlAttribute> getAttributes() {
		if (attributes == null) {
			attributes = new EObjectContainmentEList<XamlAttribute>(
					XamlAttribute.class, this,
					XamlPackage.XAML_NODE__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setId(String newId) {
		if (equals(id, newId)) {
			return;
		}
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Comment> getComments() {
		if (comments == null) {
			comments = new EObjectContainmentEList<Comment>(Comment.class,
					this, XamlPackage.XAML_NODE__COMMENTS);
		}
		return comments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getWidget() {
		return widget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWidget(Object newWidget) {
		Object oldWidget = widget;
		widget = newWidget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					XamlPackage.XAML_NODE__WIDGET, oldWidget, widget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlAttribute getAttribute(String name, String namespace) {
		if (name == null || namespace == null) {
			return null;
		}
		EList<XamlAttribute> attributes = getAttributes();
		for (XamlAttribute attribute : attributes) {
			String attrName = attribute.getName();
			String attrNS = attribute.getNamespace();
			if (name.equalsIgnoreCase(attrName)
					&& namespace.equalsIgnoreCase(attrNS)) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlAttribute getAttribute(String name) {
		if (name == null) {
			return null;
		}
		return getAttribute(name, namespace);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlElement getChild(String name, String namespace) {
		if (name == null) {
			return null;
		}
		EList<XamlElement> children = getChildren(namespace);
		for (XamlElement element : children) {
			if (name.equalsIgnoreCase(element.getName())
					&& equals(namespace, element.getNamespace())) {
				return element;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<XamlElement> getChildren(String namespace) {
		EList<XamlElement> children = new BasicEList<XamlElement>();
		EList<XamlElement> all = getChildNodes();
		for (XamlElement element : all) {
			if ((namespace == null && element.getNamespace() == null)
					|| (namespace != null && namespace.equals(element
							.getNamespace()))) {
				children.add(element);
			}
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlElement getChild(String name) {
		if (name == null) {
			return null;
		}
		String ns = getNamespace();
		XamlDocument ownerDocument = getOwnerDocument();
		if (ownerDocument != null) {
			ns = ownerDocument.getDeclaredNamespace(null);
		}
		return getChild(name, ns);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public XamlElement getChild(int index) {
		try {
			return getChildNodes().get(index);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlDocument getOwnerDocument() {
		EObject container = eContainer();
		while (container != null) {
			if (container instanceof XamlDocument) {
				return (XamlDocument) container;
			}
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<String> attributeNames() {
		EList<String> attributeNames = new BasicEList<String>();
		EList<XamlAttribute> attributes = getAttributes();
		for (XamlAttribute attribute : attributes) {
			String name = attribute.getName();
			if (!attributeNames.contains(name)) {
				attributeNames.add(name);
			}
		}
		return attributeNames;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public EList<String> attributeNames(String namespace) {
		EList<String> attributeNames = new BasicEList<String>();
		if (namespace != null) {
			EList<XamlAttribute> attributes = getAttributes();
			for (XamlAttribute attribute : attributes) {
				String name = attribute.getName();
				String ns = attribute.getNamespace();
				if (!attributeNames.contains(name) && namespace.equals(ns)) {
					attributeNames.add(name);
				}
			}
		}
		return attributeNames;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	public EList<String> attributeNamespaces() {
		EList<String> attributeNamespaces = new BasicEList<String>();
		EList<XamlAttribute> attributes = getAttributes();
		for (XamlAttribute attribute : attributes) {
			String ns = attribute.getNamespace();
			if (!attributeNamespaces.contains(ns)) {
				attributeNamespaces.add(ns);
			}
		}
		return attributeNamespaces;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public XamlNode getParent() {
		if (eContainer() instanceof XamlNode) {
			return (XamlNode) eContainer();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Node generate(Document document, Map<String, Object> options) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getFlatValue() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case XamlPackage.XAML_NODE__CHILD_NODES:
			return ((InternalEList<?>) getChildNodes()).basicRemove(otherEnd,
					msgs);
		case XamlPackage.XAML_NODE__ATTRIBUTES:
			return ((InternalEList<?>) getAttributes()).basicRemove(otherEnd,
					msgs);
		case XamlPackage.XAML_NODE__COMMENTS:
			return ((InternalEList<?>) getComments()).basicRemove(otherEnd,
					msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case XamlPackage.XAML_NODE__NAME:
			return getName();
		case XamlPackage.XAML_NODE__PREFIX:
			return getPrefix();
		case XamlPackage.XAML_NODE__NAMESPACE:
			return getNamespace();
		case XamlPackage.XAML_NODE__VALUE:
			return getValue();
		case XamlPackage.XAML_NODE__CHILD_NODES:
			return getChildNodes();
		case XamlPackage.XAML_NODE__ATTRIBUTES:
			return getAttributes();
		case XamlPackage.XAML_NODE__ID:
			return getId();
		case XamlPackage.XAML_NODE__COMMENTS:
			return getComments();
		case XamlPackage.XAML_NODE__WIDGET:
			return getWidget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case XamlPackage.XAML_NODE__NAME:
			setName((String) newValue);
			return;
		case XamlPackage.XAML_NODE__PREFIX:
			setPrefix((String) newValue);
			return;
		case XamlPackage.XAML_NODE__NAMESPACE:
			setNamespace((String) newValue);
			return;
		case XamlPackage.XAML_NODE__VALUE:
			setValue((String) newValue);
			return;
		case XamlPackage.XAML_NODE__CHILD_NODES:
			getChildNodes().clear();
			getChildNodes()
					.addAll((Collection<? extends XamlElement>) newValue);
			return;
		case XamlPackage.XAML_NODE__ATTRIBUTES:
			getAttributes().clear();
			getAttributes().addAll(
					(Collection<? extends XamlAttribute>) newValue);
			return;
		case XamlPackage.XAML_NODE__ID:
			setId((String) newValue);
			return;
		case XamlPackage.XAML_NODE__COMMENTS:
			getComments().clear();
			getComments().addAll((Collection<? extends Comment>) newValue);
			return;
		case XamlPackage.XAML_NODE__WIDGET:
			setWidget(newValue);
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
		case XamlPackage.XAML_NODE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case XamlPackage.XAML_NODE__PREFIX:
			setPrefix(PREFIX_EDEFAULT);
			return;
		case XamlPackage.XAML_NODE__NAMESPACE:
			setNamespace(NAMESPACE_EDEFAULT);
			return;
		case XamlPackage.XAML_NODE__VALUE:
			setValue(VALUE_EDEFAULT);
			return;
		case XamlPackage.XAML_NODE__CHILD_NODES:
			getChildNodes().clear();
			return;
		case XamlPackage.XAML_NODE__ATTRIBUTES:
			getAttributes().clear();
			return;
		case XamlPackage.XAML_NODE__ID:
			setId(ID_EDEFAULT);
			return;
		case XamlPackage.XAML_NODE__COMMENTS:
			getComments().clear();
			return;
		case XamlPackage.XAML_NODE__WIDGET:
			setWidget(WIDGET_EDEFAULT);
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
		case XamlPackage.XAML_NODE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case XamlPackage.XAML_NODE__PREFIX:
			return PREFIX_EDEFAULT == null ? prefix != null : !PREFIX_EDEFAULT
					.equals(prefix);
		case XamlPackage.XAML_NODE__NAMESPACE:
			return NAMESPACE_EDEFAULT == null ? namespace != null
					: !NAMESPACE_EDEFAULT.equals(namespace);
		case XamlPackage.XAML_NODE__VALUE:
			return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT
					.equals(value);
		case XamlPackage.XAML_NODE__CHILD_NODES:
			return childNodes != null && !childNodes.isEmpty();
		case XamlPackage.XAML_NODE__ATTRIBUTES:
			return attributes != null && !attributes.isEmpty();
		case XamlPackage.XAML_NODE__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case XamlPackage.XAML_NODE__COMMENTS:
			return comments != null && !comments.isEmpty();
		case XamlPackage.XAML_NODE__WIDGET:
			return WIDGET_EDEFAULT == null ? widget != null : !WIDGET_EDEFAULT
					.equals(widget);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", prefix: ");
		result.append(prefix);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", value: ");
		result.append(value);
		result.append(", id: ");
		result.append(id);
		result.append(", widget: ");
		result.append(widget);
		result.append(')');
		return result.toString();
	}

	protected String getQualifiedName() {
		return getQualifiedName(name);
	}

	protected String getQualifiedName(String name) {
		if (prefix != null) {
			return prefix + ":" + name;
		}
		return name;
	}

} // XamlNodeImpl
