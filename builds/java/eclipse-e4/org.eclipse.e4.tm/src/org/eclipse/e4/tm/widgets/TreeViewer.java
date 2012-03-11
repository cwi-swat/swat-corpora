/**
 * <copyright>
 * </copyright>
 *
 * $Id: TreeViewer.java,v 1.1 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.e4.tm.util.TreeData;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tree Viewer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.TreeViewer#getViewProvider <em>View Provider</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.TreeViewer#getContentProvider <em>Content Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTreeViewer()
 * @model
 * @generated
 */
public interface TreeViewer extends Control {
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
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTreeViewer_ViewProvider()
	 * @model containment="true"
	 * @generated
	 */
	Control getViewProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.TreeViewer#getViewProvider <em>View Provider</em>}' containment reference.
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
	 * @see #setContentProvider(TreeData)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTreeViewer_ContentProvider()
	 * @model containment="true"
	 * @generated
	 */
	TreeData getContentProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.TreeViewer#getContentProvider <em>Content Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Provider</em>' containment reference.
	 * @see #getContentProvider()
	 * @generated
	 */
	void setContentProvider(TreeData value);

} // TreeViewer
