/**
 * <copyright>
 * </copyright>
 *
 * $Id: AbstractSelection.java,v 1.1 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Selection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.AbstractSelection#getSelectionEvent <em>Selection Event</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getAbstractSelection()
 * @model abstract="true"
 * @generated
 */
public interface AbstractSelection extends EObject {
	/**
	 * Returns the value of the '<em><b>Selection Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selection Event</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selection Event</em>' attribute.
	 * @see #setSelectionEvent(Object)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getAbstractSelection_SelectionEvent()
	 * @model dataType="org.eclipse.e4.tm.widgets.RuntimeEvent" transient="true"
	 *        annotation="http://www.eclipse.org/e4/swt.ecore realName='Selection' access='event' invalidates='selectionIndex selectionIndices'"
	 * @generated
	 */
	Object getSelectionEvent();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.AbstractSelection#getSelectionEvent <em>Selection Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selection Event</em>' attribute.
	 * @see #getSelectionEvent()
	 * @generated
	 */
	void setSelectionEvent(Object value);

} // AbstractSelection
