/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeViewerImpl.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.TreeViewer;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tree Viewer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.TreeViewerImpl#getViewProvider <em>View Provider</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.impl.TreeViewerImpl#getContentProvider <em>Content Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TreeViewerImpl extends ControlImpl implements TreeViewer {
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
	protected TreeData contentProvider;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeViewerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WidgetsPackage.Literals.TREE_VIEWER;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER, oldViewProvider, newViewProvider);
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
				msgs = ((InternalEObject)viewProvider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER, null, msgs);
			if (newViewProvider != null)
				msgs = ((InternalEObject)newViewProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER, null, msgs);
			msgs = basicSetViewProvider(newViewProvider, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER, newViewProvider, newViewProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeData getContentProvider() {
		return contentProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContentProvider(TreeData newContentProvider, NotificationChain msgs) {
		TreeData oldContentProvider = contentProvider;
		contentProvider = newContentProvider;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER, oldContentProvider, newContentProvider);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContentProvider(TreeData newContentProvider) {
		if (newContentProvider != contentProvider) {
			NotificationChain msgs = null;
			if (contentProvider != null)
				msgs = ((InternalEObject)contentProvider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER, null, msgs);
			if (newContentProvider != null)
				msgs = ((InternalEObject)newContentProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER, null, msgs);
			msgs = basicSetContentProvider(newContentProvider, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER, newContentProvider, newContentProvider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER:
				return basicSetViewProvider(null, msgs);
			case WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER:
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
			case WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER:
				return getViewProvider();
			case WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER:
				return getContentProvider();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER:
				setViewProvider((Control)newValue);
				return;
			case WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER:
				setContentProvider((TreeData)newValue);
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
			case WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER:
				setViewProvider((Control)null);
				return;
			case WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER:
				setContentProvider((TreeData)null);
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
			case WidgetsPackage.TREE_VIEWER__VIEW_PROVIDER:
				return viewProvider != null;
			case WidgetsPackage.TREE_VIEWER__CONTENT_PROVIDER:
				return contentProvider != null;
		}
		return super.eIsSet(featureID);
	}

} //TreeViewerImpl
