/**
 * <copyright>
 * </copyright>
 *
 * $Id: CompoundInitializerImpl.java,v 1.2 2010/03/16 20:44:28 yvyang Exp $
 */
package org.eclipse.e4.xwt.tools.ui.palette.impl;

import java.util.Collection;

import org.eclipse.e4.xwt.tools.ui.palette.CompoundInitializer;
import org.eclipse.e4.xwt.tools.ui.palette.Entry;
import org.eclipse.e4.xwt.tools.ui.palette.Initializer;
import org.eclipse.e4.xwt.tools.ui.palette.PalettePackage;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Compound Initializer</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.xwt.tools.ui.palette.impl.CompoundInitializerImpl#getInitializers <em>Initializers</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompoundInitializerImpl extends InitializerImpl implements CompoundInitializer {
	/**
	 * The cached value of the '{@link #getInitializers() <em>Initializers</em>}
	 * ' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getInitializers()
	 * @generated
	 * @ordered
	 */
	protected EList<Initializer> initializers;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected CompoundInitializerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PalettePackage.Literals.COMPOUND_INITIALIZER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Initializer> getInitializers() {
		if (initializers == null) {
			initializers = new EObjectResolvingEList<Initializer>(Initializer.class, this, PalettePackage.COMPOUND_INITIALIZER__INITIALIZERS);
		}
		return initializers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Initializer unwrap() {
		if (getInitializers().size() == 1) {
			return getInitializers().get(0);
		}
		return this;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean initialize(Object element) {
		boolean result = initializers.size() > 0;
		EList<Initializer> initializers = getInitializers();
		for (Initializer initializer : initializers) {
			result &= initializer.initialize(element);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Object parse(Entry entry) {
		EList<Initializer> initializers = getInitializers();
		for (Initializer initializer : initializers) {
			Object parse = initializer.parse(entry);
			if (parse != null) {
				return parse;
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PalettePackage.COMPOUND_INITIALIZER__INITIALIZERS:
				return getInitializers();
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
			case PalettePackage.COMPOUND_INITIALIZER__INITIALIZERS:
				getInitializers().clear();
				getInitializers().addAll((Collection<? extends Initializer>)newValue);
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
			case PalettePackage.COMPOUND_INITIALIZER__INITIALIZERS:
				getInitializers().clear();
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
			case PalettePackage.COMPOUND_INITIALIZER__INITIALIZERS:
				return initializers != null && !initializers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.xwt.tools.ui.palette.impl.InitializerImpl#initialize(org
	 * .eclipse.e4.xwt.tools.ui.palette.Entry, java.lang.Object)
	 */
	public boolean initialize(Entry entry, Object newObject) {
		boolean result = initializers.size() > 0;
		EList<Initializer> initializers = getInitializers();
		for (Initializer initializer : initializers) {
			result &= initializer.initialize(entry, newObject);
		}
		return result;
	}

} // CompoundInitializerImpl
