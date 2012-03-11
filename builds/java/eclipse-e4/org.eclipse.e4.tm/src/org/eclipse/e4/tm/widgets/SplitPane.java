/**
 * <copyright>
 * </copyright>
 *
 * $Id: SplitPane.java,v 1.2 2009/10/23 12:40:31 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Split Pane</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.widgets.SplitPane#getOrientation <em>Orientation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getSplitPane()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaPackage='org.eclipse.swt.custom' javaClass='SashForm'"
 * @generated
 */
public interface SplitPane extends AbstractComposite<Control> {
	/**
	 * Returns the value of the '<em><b>Orientation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Orientation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Orientation</em>' attribute.
	 * @see #setOrientation(String)
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#getSplitPane_Orientation()
	 * @model annotation="http://www.eclipse.org/e4/swt.ecore type='int'"
	 * @generated
	 */
	String getOrientation();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.widgets.SplitPane#getOrientation <em>Orientation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Orientation</em>' attribute.
	 * @see #getOrientation()
	 * @generated
	 */
	void setOrientation(String value);

} // SplitPane
