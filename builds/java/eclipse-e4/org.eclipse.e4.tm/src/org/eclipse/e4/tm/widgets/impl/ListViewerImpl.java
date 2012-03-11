/**
 * <copyright>
 * </copyright>
 *
 * $Id: ListViewerImpl.java,v 1.3 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import java.util.Collection;

import org.eclipse.e4.tm.util.ListData;

import org.eclipse.e4.tm.widgets.AbstractSelection;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.IndexSelection;
import org.eclipse.e4.tm.widgets.ListViewer;
import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List Viewer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListViewerImpl#getSelectionEvent <em>Selection Event</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListViewerImpl#getSelectionIndex <em>Selection Index</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListViewerImpl#getSelectionIndices <em>Selection Indices</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListViewerImpl#getViewProvider <em>View Provider</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.ListViewerImpl#getContentProvider <em>Content Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListViewerImpl extends ControlImpl implements ListViewer {
	/**
	 * The default value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected static final Object SELECTION_EVENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSelectionEvent() <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionEvent()
	 * @generated
	 * @ordered
	 */
	protected Object selectionEvent = SELECTION_EVENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSelectionIndex() <em>Selection Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SELECTION_INDEX_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getSelectionIndex() <em>Selection Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndex()
	 * @generated
	 * @ordered
	 */
	protected int selectionIndex = SELECTION_INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSelectionIndices() <em>Selection Indices</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectionIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> selectionIndices;

	/**
	 * The cached value of the '{@link #getViewProvider() <em>View Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewProvider()
	 * @generated
	 * @ordered
	 */
	protected Control viewProvider;

	/**
	 * The cached value of the '{@link #getContentProvider() <em>Content Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContentProvider()
	 * @generated
	 * @ordered
	 */
	protected ListData contentProvider;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ListViewerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.LIST_VIEWER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getSelectionEvent() {
		return selectionEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionEvent(Object newSelectionEvent) {
		Object oldSelectionEvent = selectionEvent;
		selectionEvent = newSelectionEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__SELECTION_EVENT, oldSelectionEvent, selectionEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSelectionIndex() {
		return selectionIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectionIndex(int newSelectionIndex) {
		int oldSelectionIndex = selectionIndex;
		selectionIndex = newSelectionIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__SELECTION_INDEX, oldSelectionIndex, selectionIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getSelectionIndices() {
		if (selectionIndices == null) {
			selectionIndices = new EDataTypeUniqueEList<Integer>(Integer.class, this, WidgetsPackage.LIST_VIEWER__SELECTION_INDICES);
		}
		return selectionIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Control getViewProvider() {
		return viewProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetViewProvider(Control newViewProvider, NotificationChain msgs) {
		Control oldViewProvider = viewProvider;
		viewProvider = newViewProvider;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER, oldViewProvider, newViewProvider);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setViewProvider(Control newViewProvider) {
		if (newViewProvider != viewProvider) {
			NotificationChain msgs = null;
			if (viewProvider != null)
				msgs = ((InternalEObject)viewProvider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER, null, msgs);
			if (newViewProvider != null)
				msgs = ((InternalEObject)newViewProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER, null, msgs);
			msgs = basicSetViewProvider(newViewProvider, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER, newViewProvider, newViewProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListData getContentProvider() {
		return contentProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContentProvider(ListData newContentProvider, NotificationChain msgs) {
		ListData oldContentProvider = contentProvider;
		contentProvider = newContentProvider;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER, oldContentProvider, newContentProvider);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContentProvider(ListData newContentProvider) {
		if (newContentProvider != contentProvider) {
			NotificationChain msgs = null;
			if (contentProvider != null)
				msgs = ((InternalEObject)contentProvider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER, null, msgs);
			if (newContentProvider != null)
				msgs = ((InternalEObject)newContentProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER, null, msgs);
			msgs = basicSetContentProvider(newContentProvider, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER, newContentProvider, newContentProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER:
				return basicSetViewProvider(null, msgs);
			case WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER:
				return basicSetContentProvider(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WidgetsPackage.LIST_VIEWER__SELECTION_EVENT:
				return getSelectionEvent();
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDEX:
				return getSelectionIndex();
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDICES:
				return getSelectionIndices();
			case WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER:
				return getViewProvider();
			case WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER:
				return getContentProvider();
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
			case WidgetsPackage.LIST_VIEWER__SELECTION_EVENT:
				setSelectionEvent(newValue);
				return;
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDEX:
				setSelectionIndex((Integer)newValue);
				return;
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDICES:
				getSelectionIndices().clear();
				getSelectionIndices().addAll((Collection<? extends Integer>)newValue);
				return;
			case WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER:
				setViewProvider((Control)newValue);
				return;
			case WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER:
				setContentProvider((ListData)newValue);
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
			case WidgetsPackage.LIST_VIEWER__SELECTION_EVENT:
				setSelectionEvent(SELECTION_EVENT_EDEFAULT);
				return;
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDEX:
				setSelectionIndex(SELECTION_INDEX_EDEFAULT);
				return;
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDICES:
				getSelectionIndices().clear();
				return;
			case WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER:
				setViewProvider((Control)null);
				return;
			case WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER:
				setContentProvider((ListData)null);
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
			case WidgetsPackage.LIST_VIEWER__SELECTION_EVENT:
				return SELECTION_EVENT_EDEFAULT == null ? selectionEvent != null : !SELECTION_EVENT_EDEFAULT.equals(selectionEvent);
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDEX:
				return selectionIndex != SELECTION_INDEX_EDEFAULT;
			case WidgetsPackage.LIST_VIEWER__SELECTION_INDICES:
				return selectionIndices != null && !selectionIndices.isEmpty();
			case WidgetsPackage.LIST_VIEWER__VIEW_PROVIDER:
				return viewProvider != null;
			case WidgetsPackage.LIST_VIEWER__CONTENT_PROVIDER:
				return contentProvider != null;
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
		if (baseClass == AbstractSelection.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.LIST_VIEWER__SELECTION_EVENT: return WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT;
				default: return -1;
			}
		}
		if (baseClass == IndexSelection.class) {
			switch (derivedFeatureID) {
				case WidgetsPackage.LIST_VIEWER__SELECTION_INDEX: return WidgetsPackage.INDEX_SELECTION__SELECTION_INDEX;
				case WidgetsPackage.LIST_VIEWER__SELECTION_INDICES: return WidgetsPackage.INDEX_SELECTION__SELECTION_INDICES;
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
		if (baseClass == AbstractSelection.class) {
			switch (baseFeatureID) {
				case WidgetsPackage.ABSTRACT_SELECTION__SELECTION_EVENT: return WidgetsPackage.LIST_VIEWER__SELECTION_EVENT;
				default: return -1;
			}
		}
		if (baseClass == IndexSelection.class) {
			switch (baseFeatureID) {
				case WidgetsPackage.INDEX_SELECTION__SELECTION_INDEX: return WidgetsPackage.LIST_VIEWER__SELECTION_INDEX;
				case WidgetsPackage.INDEX_SELECTION__SELECTION_INDICES: return WidgetsPackage.LIST_VIEWER__SELECTION_INDICES;
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
		result.append(" (selectionEvent: ");
		result.append(selectionEvent);
		result.append(", selectionIndex: ");
		result.append(selectionIndex);
		result.append(", selectionIndices: ");
		result.append(selectionIndices);
		result.append(')');
		return result.toString();
	}

} //ListViewerImpl
