/**
 * <copyright>
 * </copyright>
 *
 * $Id: TableViewer.java,v 1.2 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.e4.tm.util.ListData;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Table Viewer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.TableViewer#getViewProviders <em>View Providers</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.TableViewer#getContentProvider <em>Content Provider</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTableViewer()
 * @model
 * @generated
 */
public interface TableViewer extends Control, IndexSelection {
	/**
	 * Returns the value of the '<em><b>View Providers</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.tm.widgets.Control}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>View Providers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>View Providers</em>' containment reference list.
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTableViewer_ViewProviders()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore realName='labelProvider'"
	 * @generated
	 */
	EList<Control> getViewProviders();

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
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getTableViewer_ContentProvider()
	 * @model containment="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore realName='contentProvider'"
	 * @generated
	 */
	ListData getContentProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.TableViewer#getContentProvider <em>Content Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Provider</em>' containment reference.
	 * @see #getContentProvider()
	 * @generated
	 */
	void setContentProvider(ListData value);

} // TableViewer
