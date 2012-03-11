/**
 * <copyright>
 * </copyright>
 *
 * $Id: ListViewer.java,v 1.2 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.e4.tm.util.ListData;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Viewer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.ListViewer#getViewProvider <em>View Provider</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.ListViewer#getContentProvider <em>Content Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getListViewer()
 * @model
 * @generated
 */
public interface ListViewer extends Control, IndexSelection {
	/**
	 * Returns the value of the '<em><b>View Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>View Provider</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>View Provider</em>' containment reference.
	 * @see #setViewProvider(Control)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getListViewer_ViewProvider()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore realName='labelProvider'"
	 * @generated
	 */
	Control getViewProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.ListViewer#getViewProvider <em>View Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>View Provider</em>' containment reference.
	 * @see #getViewProvider()
	 * @generated
	 */
	void setViewProvider(Control value);

	/**
	 * Returns the value of the '<em><b>Content Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content Provider</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content Provider</em>' containment reference.
	 * @see #setContentProvider(ListData)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getListViewer_ContentProvider()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore realName='contentProvider'"
	 * @generated
	 */
	ListData getContentProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.ListViewer#getContentProvider <em>Content Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Provider</em>' containment reference.
	 * @see #getContentProvider()
	 * @generated
	 */
	void setContentProvider(ListData value);

} // ListViewer
