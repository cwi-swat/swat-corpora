/**
 * <copyright>
 * </copyright>
 *
 * $Id: IndexSelection.java,v 1.2 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index Selection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.IndexSelection#getSelectionIndex <em>Selection Index</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.IndexSelection#getSelectionIndices <em>Selection Indices</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getIndexSelection()
 * @model
 * @generated
 */
public interface IndexSelection extends AbstractSelection {
	/**
	 * Returns the value of the '<em><b>Selection Index</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selection Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selection Index</em>' attribute.
	 * @see #setSelectionIndex(int)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getIndexSelection_SelectionIndex()
	 * @model default="-1"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore access='getSelectionIndex setSelection(int)' invalidatedBy='selectionEvent'"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore#ComboBox access='getSelectionIndex select(int)'"
	 * @generated
	 */
	int getSelectionIndex();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.IndexSelection#getSelectionIndex <em>Selection Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selection Index</em>' attribute.
	 * @see #getSelectionIndex()
	 * @generated
	 */
	void setSelectionIndex(int value);

	/**
	 * Returns the value of the '<em><b>Selection Indices</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selection Indices</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selection Indices</em>' attribute list.
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getIndexSelection_SelectionIndices()
	 * @model annotation="http://www.eclipse.org/e4/swt.ecore access='getSelectionIndices setSelection(int[])' invalidatedBy='selection'"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore#ComboBox access=''"
	 * @generated
	 */
	EList<Integer> getSelectionIndices();

} // IndexSelection
