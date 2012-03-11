/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphical2dImpl.java,v 1.2 2009/10/23 20:32:26 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.impl;

import java.util.Collection;

import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics.util.Transform;
import org.eclipse.e4.tm.graphics2d.Graphical2d;
import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.util.impl.ObjectDataImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graphical2d</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getRole <em>Role</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getBounds <em>Bounds</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl#getTransform <em>Transform</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Graphical2dImpl extends ObjectDataImpl implements Graphical2d {
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
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<Graphical2d> children;

	/**
	 * The default value of the '{@link #getBounds() <em>Bounds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBounds()
	 * @generated
	 * @ordered
	 */
	protected static final Rectangle BOUNDS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBounds() <em>Bounds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBounds()
	 * @generated
	 * @ordered
	 */
	protected Rectangle bounds = BOUNDS_EDEFAULT;

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
	 * The default value of the '{@link #getTransform() <em>Transform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransform()
	 * @generated
	 * @ordered
	 */
	protected static final Transform TRANSFORM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTransform() <em>Transform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransform()
	 * @generated
	 * @ordered
	 */
	protected Transform transform = TRANSFORM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Graphical2dImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Graphics2dPackage.Literals.GRAPHICAL2D;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__ROLE, oldRole, role));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Style getStyle() {
		if (style != null && style.eIsProxy()) {
			InternalEObject oldStyle = (InternalEObject)style;
			style = (Style)eResolveProxy(oldStyle);
			if (style != oldStyle) {
				InternalEObject newStyle = (InternalEObject)style;
				NotificationChain msgs = oldStyle.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Graphics2dPackage.GRAPHICAL2D__STYLE, null, null);
				if (newStyle.eInternalContainer() == null) {
					msgs = newStyle.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Graphics2dPackage.GRAPHICAL2D__STYLE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Graphics2dPackage.GRAPHICAL2D__STYLE, oldStyle, style));
			}
		}
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Style basicGetStyle() {
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__STYLE, oldStyle, newStyle);
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
				msgs = ((InternalEObject)style).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Graphics2dPackage.GRAPHICAL2D__STYLE, null, msgs);
			if (newStyle != null)
				msgs = ((InternalEObject)newStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Graphics2dPackage.GRAPHICAL2D__STYLE, null, msgs);
			msgs = basicSetStyle(newStyle, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__STYLE, newStyle, newStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Graphical2d> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<Graphical2d>(Graphical2d.class, this, Graphics2dPackage.GRAPHICAL2D__CHILDREN, Graphics2dPackage.GRAPHICAL2D__PARENT);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphical2d getParent() {
		if (eContainerFeatureID() != Graphics2dPackage.GRAPHICAL2D__PARENT) return null;
		return (Graphical2d)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(Graphical2d newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParent, Graphics2dPackage.GRAPHICAL2D__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(Graphical2d newParent) {
		if (newParent != eInternalContainer() || (eContainerFeatureID() != Graphics2dPackage.GRAPHICAL2D__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, newParent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, Graphics2dPackage.GRAPHICAL2D__CHILDREN, Graphical2d.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBounds(Rectangle newBounds) {
		Rectangle oldBounds = bounds;
		bounds = newBounds;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__BOUNDS, oldBounds, bounds));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__VISIBLE, oldVisible, visible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transform getTransform() {
		return transform;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransform(Transform newTransform) {
		Transform oldTransform = transform;
		transform = newTransform;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Graphics2dPackage.GRAPHICAL2D__TRANSFORM, oldTransform, transform));
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
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParent((Graphical2d)otherEnd, msgs);
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
			case Graphics2dPackage.GRAPHICAL2D__STYLE:
				return basicSetStyle(null, msgs);
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				return basicSetParent(null, msgs);
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
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				return eInternalContainer().eInverseRemove(this, Graphics2dPackage.GRAPHICAL2D__CHILDREN, Graphical2d.class, msgs);
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
			case Graphics2dPackage.GRAPHICAL2D__NAME:
				return getName();
			case Graphics2dPackage.GRAPHICAL2D__ROLE:
				return getRole();
			case Graphics2dPackage.GRAPHICAL2D__STYLE:
				if (resolve) return getStyle();
				return basicGetStyle();
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				return getChildren();
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				return getParent();
			case Graphics2dPackage.GRAPHICAL2D__BOUNDS:
				return getBounds();
			case Graphics2dPackage.GRAPHICAL2D__VISIBLE:
				return isVisible();
			case Graphics2dPackage.GRAPHICAL2D__TRANSFORM:
				return getTransform();
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
			case Graphics2dPackage.GRAPHICAL2D__NAME:
				setName((String)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__ROLE:
				setRole((String)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__STYLE:
				setStyle((Style)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends Graphical2d>)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				setParent((Graphical2d)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__BOUNDS:
				setBounds((Rectangle)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__VISIBLE:
				setVisible((Boolean)newValue);
				return;
			case Graphics2dPackage.GRAPHICAL2D__TRANSFORM:
				setTransform((Transform)newValue);
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
			case Graphics2dPackage.GRAPHICAL2D__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Graphics2dPackage.GRAPHICAL2D__ROLE:
				setRole(ROLE_EDEFAULT);
				return;
			case Graphics2dPackage.GRAPHICAL2D__STYLE:
				setStyle((Style)null);
				return;
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				getChildren().clear();
				return;
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				setParent((Graphical2d)null);
				return;
			case Graphics2dPackage.GRAPHICAL2D__BOUNDS:
				setBounds(BOUNDS_EDEFAULT);
				return;
			case Graphics2dPackage.GRAPHICAL2D__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case Graphics2dPackage.GRAPHICAL2D__TRANSFORM:
				setTransform(TRANSFORM_EDEFAULT);
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
			case Graphics2dPackage.GRAPHICAL2D__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case Graphics2dPackage.GRAPHICAL2D__ROLE:
				return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
			case Graphics2dPackage.GRAPHICAL2D__STYLE:
				return style != null;
			case Graphics2dPackage.GRAPHICAL2D__CHILDREN:
				return children != null && !children.isEmpty();
			case Graphics2dPackage.GRAPHICAL2D__PARENT:
				return getParent() != null;
			case Graphics2dPackage.GRAPHICAL2D__BOUNDS:
				return BOUNDS_EDEFAULT == null ? bounds != null : !BOUNDS_EDEFAULT.equals(bounds);
			case Graphics2dPackage.GRAPHICAL2D__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case Graphics2dPackage.GRAPHICAL2D__TRANSFORM:
				return TRANSFORM_EDEFAULT == null ? transform != null : !TRANSFORM_EDEFAULT.equals(transform);
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
				case Graphics2dPackage.GRAPHICAL2D__NAME: return StylesPackage.STYLED__NAME;
				case Graphics2dPackage.GRAPHICAL2D__ROLE: return StylesPackage.STYLED__ROLE;
				case Graphics2dPackage.GRAPHICAL2D__STYLE: return StylesPackage.STYLED__STYLE;
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
				case StylesPackage.STYLED__NAME: return Graphics2dPackage.GRAPHICAL2D__NAME;
				case StylesPackage.STYLED__ROLE: return Graphics2dPackage.GRAPHICAL2D__ROLE;
				case StylesPackage.STYLED__STYLE: return Graphics2dPackage.GRAPHICAL2D__STYLE;
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
		result.append(", bounds: ");
		result.append(bounds);
		result.append(", visible: ");
		result.append(visible);
		result.append(", transform: ");
		result.append(transform);
		result.append(')');
		return result.toString();
	}

} //Graphical2dImpl
