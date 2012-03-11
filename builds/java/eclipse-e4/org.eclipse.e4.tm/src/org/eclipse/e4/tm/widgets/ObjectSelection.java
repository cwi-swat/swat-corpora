/**
 * <copyright>
 * </copyright>
 *
 * $Id: ObjectSelection.java,v 1.1 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Selection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.ObjectSelection#getSelectedObject <em>Selected Object</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.widgets.ObjectSelection#getSelectedObjects <em>Selected Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getObjectSelection()
 * @model
 * @generated
 */
public interface ObjectSelection extends AbstractSelection {
	/**
	 * Returns the value of the '<em><b>Selected Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected Object</em>' attribute.
	 * @see #setSelectedObject(Object)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getObjectSelection_SelectedObject()
	 * @model transient="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore access='getSelectionIndex setSelection(int)' invalidatedBy='selectionEvent'"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore#ComboBox access='getSelectionIndex select(int)'"
	 * @generated
	 */
	Object getSelectedObject();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.ObjectSelection#getSelectedObject <em>Selected Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selected Object</em>' attribute.
	 * @see #getSelectedObject()
	 * @generated
	 */
	void setSelectedObject(Object value);

	/**
	 * Returns the value of the '<em><b>Selected Objects</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected Objects</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected Objects</em>' attribute list.
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getObjectSelection_SelectedObjects()
	 * @model transient="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore access='getSelectionIndices setSelection(int[])' invalidatedBy='selection'"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore#ComboBox access=''"
	 * @generated
	 */
	EList<Object> getSelectedObjects();

} // ObjectSelection
