/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: ControlImpl.java,v 1.7 2010/03/18 14:01:52 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.layouts.LayoutData;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.util.impl.ObjectDataImpl;
import org.eclipse.e4.tm.widgets.AbstractComposite;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getRole <em>Role</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getComposite <em>Composite</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getLayoutData <em>Layout Data</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ControlImpl#getToolTip <em>Tool Tip</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ControlImpl extends ObjectDataImpl implements Control {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
	protected static final String ROLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
	protected String role = ROLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected Style style;

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLayoutData() <em>Layout Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayoutData()
	 * @generated
	 * @ordered
	 */
	protected LayoutData layoutData;

	/**
	 * The default value of the '{@link #getToolTip() <em>Tool Tip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToolTip()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOL_TIP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToolTip() <em>Tool Tip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToolTip()
	 * @generated
	 * @ordered
	 */
	protected String toolTip = TOOL_TIP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ControlImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.CONTROL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRole() {
		return role;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRole(String newRole) {
		String oldRole = role;
		role = newRole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__ROLE, oldRole, role));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStyle(Style newStyle, NotificationChain msgs) {
		Style oldStyle = style;
		style = newStyle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__STYLE, oldStyle, newStyle);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStyle(Style newStyle) {
		if (newStyle != style) {
			NotificationChain msgs = null;
			if (style != null)
				msgs = ((InternalEObject)style).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.CONTROL__STYLE, null, msgs);
			if (newStyle != null)
				msgs = ((InternalEObject)newStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.CONTROL__STYLE, null, msgs);
			msgs = basicSetStyle(newStyle, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__STYLE, newStyle, newStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public AbstractComposite<Control> getComposite() {
		if (eContainerFeatureID() != WidgetsPackage.CONTROL__COMPOSITE) return null;
		return (AbstractComposite<Control>)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractComposite<Control> basicGetComposite() {
		if (eContainerFeatureID() != WidgetsPackage.CONTROL__COMPOSITE) return null;
		return (AbstractComposite<Control>)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComposite(AbstractComposite<Control> newComposite, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newComposite, WidgetsPackage.CONTROL__COMPOSITE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComposite(AbstractComposite<Control> newComposite) {
		if (newComposite != eInternalContainer() || (eContainerFeatureID() != WidgetsPackage.CONTROL__COMPOSITE && newComposite != null)) {
			if (EcoreUtil.isAncestor(this, newComposite))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newComposite != null)
				msgs = ((InternalEObject)newComposite).eInverseAdd(this, WidgetsPackage.ABSTRACT_COMPOSITE__CONTROLS, AbstractComposite.class, msgs);
			msgs = basicSetComposite(newComposite, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__COMPOSITE, newComposite, newComposite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnabled(boolean newEnabled) {
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__ENABLED, oldEnabled, enabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__VISIBLE, oldVisible, visible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LayoutData getLayoutData() {
		return layoutData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLayoutData(LayoutData newLayoutData, NotificationChain msgs) {
		LayoutData oldLayoutData = layoutData;
		layoutData = newLayoutData;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__LAYOUT_DATA, oldLayoutData, newLayoutData);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLayoutData(LayoutData newLayoutData) {
		if (newLayoutData != layoutData) {
			NotificationChain msgs = null;
			if (layoutData != null)
				msgs = ((InternalEObject)layoutData).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.CONTROL__LAYOUT_DATA, null, msgs);
			if (newLayoutData != null)
				msgs = ((InternalEObject)newLayoutData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.CONTROL__LAYOUT_DATA, null, msgs);
			msgs = basicSetLayoutData(newLayoutData, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__LAYOUT_DATA, newLayoutData, newLayoutData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToolTip(String newToolTip) {
		String oldToolTip = toolTip;
		toolTip = newToolTip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.CONTROL__TOOL_TIP, oldToolTip, toolTip));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__COMPOSITE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetComposite((AbstractComposite<Control>)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__STYLE:
				return basicSetStyle(null, msgs);
			case WidgetsPackage.CONTROL__COMPOSITE:
				return basicSetComposite(null, msgs);
			case WidgetsPackage.CONTROL__LAYOUT_DATA:
				return basicSetLayoutData(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case WidgetsPackage.CONTROL__COMPOSITE:
				return eInternalContainer().eInverseRemove(this, WidgetsPackage.ABSTRACT_COMPOSITE__CONTROLS, AbstractComposite.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__NAME:
				return getName();
			case WidgetsPackage.CONTROL__ROLE:
				return getRole();
			case WidgetsPackage.CONTROL__STYLE:
				return getStyle();
			case WidgetsPackage.CONTROL__COMPOSITE:
				if (resolve) return getComposite();
				return basicGetComposite();
			case WidgetsPackage.CONTROL__ENABLED:
				return isEnabled();
			case WidgetsPackage.CONTROL__VISIBLE:
				return isVisible();
			case WidgetsPackage.CONTROL__LAYOUT_DATA:
				return getLayoutData();
			case WidgetsPackage.CONTROL__TOOL_TIP:
				return getToolTip();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__NAME:
				setName((String)newValue);
				return;
			case WidgetsPackage.CONTROL__ROLE:
				setRole((String)newValue);
				return;
			case WidgetsPackage.CONTROL__STYLE:
				setStyle((Style)newValue);
				return;
			case WidgetsPackage.CONTROL__COMPOSITE:
				setComposite((AbstractComposite<Control>)newValue);
				return;
			case WidgetsPackage.CONTROL__ENABLED:
				setEnabled((Boolean)newValue);
				return;
			case WidgetsPackage.CONTROL__VISIBLE:
				setVisible((Boolean)newValue);
				return;
			case WidgetsPackage.CONTROL__LAYOUT_DATA:
				setLayoutData((LayoutData)newValue);
				return;
			case WidgetsPackage.CONTROL__TOOL_TIP:
				setToolTip((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case WidgetsPackage.CONTROL__ROLE:
				setRole(ROLE_EDEFAULT);
				return;
			case WidgetsPackage.CONTROL__STYLE:
				setStyle((Style)null);
				return;
			case WidgetsPackage.CONTROL__COMPOSITE:
				setComposite((AbstractComposite<Control>)null);
				return;
			case WidgetsPackage.CONTROL__ENABLED:
				setEnabled(ENABLED_EDEFAULT);
				return;
			case WidgetsPackage.CONTROL__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case WidgetsPackage.CONTROL__LAYOUT_DATA:
				setLayoutData((LayoutData)null);
				return;
			case WidgetsPackage.CONTROL__TOOL_TIP:
				setToolTip(TOOL_TIP_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WidgetsPackage.CONTROL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case WidgetsPackage.CONTROL__ROLE:
				return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
			case WidgetsPackage.CONTROL__STYLE:
				return style != null;
			case WidgetsPackage.CONTROL__COMPOSITE:
				return basicGetComposite() != null;
			case WidgetsPackage.CONTROL__ENABLED:
				return enabled != ENABLED_EDEFAULT;
			case WidgetsPackage.CONTROL__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case WidgetsPackage.CONTROL__LAYOUT_DATA:
				return layoutData != null;
			case WidgetsPackage.CONTROL__TOOL_TIP:
				return TOOL_TIP_EDEFAULT == null ? toolTip != null : !TOOL_TIP_EDEFAULT.equals(toolTip);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == Styled.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.CONTROL__NAME: return StylesPackage.STYLED__NAME;
				case WidgetsPackage.CONTROL__ROLE: return StylesPackage.STYLED__ROLE;
				case WidgetsPackage.CONTROL__STYLE: return StylesPackage.STYLED__STYLE;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == Styled.class) {
			switch (baseFeatureID) {
				case StylesPackage.STYLED__NAME: return WidgetsPackage.CONTROL__NAME;
				case StylesPackage.STYLED__ROLE: return WidgetsPackage.CONTROL__ROLE;
				case StylesPackage.STYLED__STYLE: return WidgetsPackage.CONTROL__STYLE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", role: ");
		result.append(role);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(", visible: ");
		result.append(visible);
		result.append(", toolTip: ");
		result.append(toolTip);
		result.append(')');
		return result.toString();
	}

} //ControlImpl
