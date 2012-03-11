/**
 * <copyright>
 * </copyright>
 *
 * $Id: Text2d.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text2d</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Text2d#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getText2d()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaClass='edu.umd.cs.piccolo.nodes.PText'"
 * @generated
 */
public interface Text2d extends Graphical2d {
	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getText2d_Text()
	 * @model
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Text2d#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

} // Text2d
