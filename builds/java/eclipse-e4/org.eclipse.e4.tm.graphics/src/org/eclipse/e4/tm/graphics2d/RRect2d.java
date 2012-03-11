/**
 * <copyright>
 * </copyright>
 *
 * $Id: RRect2d.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.e4.tm.graphics.util.Dimension;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RRect2d</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.RRect2d#getCornerSize <em>Corner Size</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getRRect2d()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaClass='edu.umd.cs.piccolox.nodes.PRoundedRect'"
 * @generated
 */
public interface RRect2d extends Rect2d {
	/**
	 * Returns the value of the '<em><b>Corner Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Corner Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Corner Size</em>' attribute.
	 * @see #setCornerSize(Dimension)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getRRect2d_CornerSize()
	 * @model dataType="org.eclipse.e4.tm.graphics2d.Dimension"
	 * @generated
	 */
	Dimension getCornerSize();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.RRect2d#getCornerSize <em>Corner Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Corner Size</em>' attribute.
	 * @see #getCornerSize()
	 * @generated
	 */
	void setCornerSize(Dimension value);

} // RRect2d
