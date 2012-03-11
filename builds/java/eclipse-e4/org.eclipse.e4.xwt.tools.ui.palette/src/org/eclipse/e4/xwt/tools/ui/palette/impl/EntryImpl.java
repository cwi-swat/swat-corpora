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
package org.eclipse.e4.xwt.tools.ui.palette.impl;

import java.util.Collection;

import org.eclipse.e4.xwt.tools.ui.palette.ContextType;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Initializer;
import org.eclipse.e4.xwt.tools.ui.palette.PalettePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Entry</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getToolTip <em>Tool Tip</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getLargeIcon <em>Large Icon</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getEntries <em>Entries</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getScope <em>Scope</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getInitializer <em>Initializer</em>}</li>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.EntryImpl#getDataContext <em>Data Context</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EntryImpl extends EObjectImpl implements Entry {
	/**
	 * The default value of the '{@link #getToolTip() <em>Tool Tip</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getToolTip()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOL_TIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToolTip() <em>Tool Tip</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getToolTip()
	 * @generated
	 * @ordered
	 */
	protected String toolTip = TOOL_TIP_EDEFAULT;

	/**
	 * The default value of the '{@link #getLargeIcon() <em>Large Icon</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLargeIcon()
	 * @generated
	 * @ordered
	 */
	protected static final String LARGE_ICON_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLargeIcon() <em>Large Icon</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLargeIcon()
	 * @generated
	 * @ordered
	 */
	protected String largeIcon = LARGE_ICON_EDEFAULT;

	/**
	 * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContent()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContent() <em>Content</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContent()
	 * @generated
	 * @ordered
	 */
	protected String content = CONTENT_EDEFAULT;

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
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<Entry> entries;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = "\"\"";

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getIcon() <em>Icon</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getIcon()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIcon() <em>Icon</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getIcon()
	 * @generated
	 * @ordered
	 */
	protected String icon = ICON_EDEFAULT;

	/**
	 * The default value of the '{@link #getContext() <em>Context</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected static final ContextType CONTEXT_EDEFAULT = ContextType.XML_TAG;

	/**
	 * The cached value of the '{@link #getContext() <em>Context</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected ContextType context = CONTEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated
	 * @ordered
	 */
	protected static final String SCOPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated
	 * @ordered
	 */
	protected String scope = SCOPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected EClass type;

	/**
	 * The cached value of the '{@link #getInitializer() <em>Initializer</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getInitializer()
	 * @generated
	 * @ordered
	 */
	protected Initializer initializer;

	/**
	 * The default value of the '{@link #getDataContext() <em>Data Context</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDataContext()
	 * @generated
	 * @ordered
	 */
	protected static final Object DATA_CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataContext() <em>Data Context</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDataContext()
	 * @generated
	 * @ordered
	 */
	protected Object dataContext = DATA_CONTEXT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.ENTRY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setToolTip(String newToolTip) {
		String oldToolTip = toolTip;
		toolTip = newToolTip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__TOOL_TIP, oldToolTip, toolTip));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getLargeIcon() {
		return largeIcon;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLargeIcon(String newLargeIcon) {
		String oldLargeIcon = largeIcon;
		largeIcon = newLargeIcon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__LARGE_ICON, oldLargeIcon, largeIcon));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getContent() {
		return content;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setContent(String newContent) {
		String oldContent = content;
		content = newContent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__CONTENT, oldContent, content));
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
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Entry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<Entry>(Entry.class, this, PalettePackage.ENTRY__ENTRIES);
		}
		return entries;
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
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setIcon(String newIcon) {
		String oldIcon = icon;
		icon = newIcon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__ICON, oldIcon, icon));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ContextType getContext() {
		return context;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setContext(ContextType newContext) {
		ContextType oldContext = context;
		context = newContext == null ? CONTEXT_EDEFAULT : newContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__CONTEXT, oldContext, context));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setScope(String newScope) {
		String oldScope = scope;
		scope = newScope;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__SCOPE, oldScope, scope));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__VISIBLE, oldVisible, visible));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Initializer getInitializer() {
		if (initializer != null && initializer.eIsProxy()) {
			InternalEObject oldInitializer = (InternalEObject)initializer;
			initializer = (Initializer)eResolveProxy(oldInitializer);
			if (initializer != oldInitializer) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PalettePackage.ENTRY__INITIALIZER, oldInitializer, initializer));
			}
		}
		return initializer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Initializer basicGetInitializer() {
		return initializer;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitializer(Initializer newInitializer) {
		Initializer oldInitializer = initializer;
		initializer = newInitializer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__INITIALIZER, oldInitializer, initializer));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object getDataContext() {
		return dataContext;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataContext(Object newDataContext) {
		Object oldDataContext = dataContext;
		dataContext = newDataContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__DATA_CONTEXT, oldDataContext, dataContext));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (EClass)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PalettePackage.ENTRY__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(EClass newType) {
		EClass oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PalettePackage.ENTRY__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID,
			NotificationChain msgs) {
		switch (featureID) {
			case PalettePackage.ENTRY__ENTRIES:
				return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
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
			case PalettePackage.ENTRY__TOOL_TIP:
				return getToolTip();
			case PalettePackage.ENTRY__LARGE_ICON:
				return getLargeIcon();
			case PalettePackage.ENTRY__CONTENT:
				return getContent();
			case PalettePackage.ENTRY__NAME:
				return getName();
			case PalettePackage.ENTRY__ENTRIES:
				return getEntries();
			case PalettePackage.ENTRY__ID:
				return getId();
			case PalettePackage.ENTRY__ICON:
				return getIcon();
			case PalettePackage.ENTRY__CONTEXT:
				return getContext();
			case PalettePackage.ENTRY__SCOPE:
				return getScope();
			case PalettePackage.ENTRY__VISIBLE:
				return isVisible();
			case PalettePackage.ENTRY__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case PalettePackage.ENTRY__INITIALIZER:
				if (resolve) return getInitializer();
				return basicGetInitializer();
			case PalettePackage.ENTRY__DATA_CONTEXT:
				return getDataContext();
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
			case PalettePackage.ENTRY__TOOL_TIP:
				setToolTip((String)newValue);
				return;
			case PalettePackage.ENTRY__LARGE_ICON:
				setLargeIcon((String)newValue);
				return;
			case PalettePackage.ENTRY__CONTENT:
				setContent((String)newValue);
				return;
			case PalettePackage.ENTRY__NAME:
				setName((String)newValue);
				return;
			case PalettePackage.ENTRY__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends Entry>)newValue);
				return;
			case PalettePackage.ENTRY__ID:
				setId((String)newValue);
				return;
			case PalettePackage.ENTRY__ICON:
				setIcon((String)newValue);
				return;
			case PalettePackage.ENTRY__CONTEXT:
				setContext((ContextType)newValue);
				return;
			case PalettePackage.ENTRY__SCOPE:
				setScope((String)newValue);
				return;
			case PalettePackage.ENTRY__VISIBLE:
				setVisible((Boolean)newValue);
				return;
			case PalettePackage.ENTRY__TYPE:
				setType((EClass)newValue);
				return;
			case PalettePackage.ENTRY__INITIALIZER:
				setInitializer((Initializer)newValue);
				return;
			case PalettePackage.ENTRY__DATA_CONTEXT:
				setDataContext(newValue);
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
			case PalettePackage.ENTRY__TOOL_TIP:
				setToolTip(TOOL_TIP_EDEFAULT);
				return;
			case PalettePackage.ENTRY__LARGE_ICON:
				setLargeIcon(LARGE_ICON_EDEFAULT);
				return;
			case PalettePackage.ENTRY__CONTENT:
				setContent(CONTENT_EDEFAULT);
				return;
			case PalettePackage.ENTRY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ENTRIES:
				getEntries().clear();
				return;
			case PalettePackage.ENTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case PalettePackage.ENTRY__ICON:
				setIcon(ICON_EDEFAULT);
				return;
			case PalettePackage.ENTRY__CONTEXT:
				setContext(CONTEXT_EDEFAULT);
				return;
			case PalettePackage.ENTRY__SCOPE:
				setScope(SCOPE_EDEFAULT);
				return;
			case PalettePackage.ENTRY__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case PalettePackage.ENTRY__TYPE:
				setType((EClass)null);
				return;
			case PalettePackage.ENTRY__INITIALIZER:
				setInitializer((Initializer)null);
				return;
			case PalettePackage.ENTRY__DATA_CONTEXT:
				setDataContext(DATA_CONTEXT_EDEFAULT);
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
			case PalettePackage.ENTRY__TOOL_TIP:
				return TOOL_TIP_EDEFAULT == null ? toolTip != null : !TOOL_TIP_EDEFAULT.equals(toolTip);
			case PalettePackage.ENTRY__LARGE_ICON:
				return LARGE_ICON_EDEFAULT == null ? largeIcon != null : !LARGE_ICON_EDEFAULT.equals(largeIcon);
			case PalettePackage.ENTRY__CONTENT:
				return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
			case PalettePackage.ENTRY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PalettePackage.ENTRY__ENTRIES:
				return entries != null && !entries.isEmpty();
			case PalettePackage.ENTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PalettePackage.ENTRY__ICON:
				return ICON_EDEFAULT == null ? icon != null : !ICON_EDEFAULT.equals(icon);
			case PalettePackage.ENTRY__CONTEXT:
				return context != CONTEXT_EDEFAULT;
			case PalettePackage.ENTRY__SCOPE:
				return SCOPE_EDEFAULT == null ? scope != null : !SCOPE_EDEFAULT.equals(scope);
			case PalettePackage.ENTRY__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case PalettePackage.ENTRY__TYPE:
				return type != null;
			case PalettePackage.ENTRY__INITIALIZER:
				return initializer != null;
			case PalettePackage.ENTRY__DATA_CONTEXT:
				return DATA_CONTEXT_EDEFAULT == null ? dataContext != null : !DATA_CONTEXT_EDEFAULT.equals(dataContext);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (toolTip: ");
		result.append(toolTip);
		result.append(", largeIcon: ");
		result.append(largeIcon);
		result.append(", content: ");
		result.append(content);
		result.append(", name: ");
		result.append(name);
		result.append(", id: ");
		result.append(id);
		result.append(", icon: ");
		result.append(icon);
		result.append(", context: ");
		result.append(context);
		result.append(", scope: ");
		result.append(scope);
		result.append(", visible: ");
		result.append(visible);
		result.append(", dataContext: ");
		result.append(dataContext);
		result.append(')');
		return result.toString();
	}

} // EntryImpl
