/**
 * <copyright>
 * </copyright>
 *
 * $Id: Arc2d.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Arc2d</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Arc2d#getStartAngle <em>Start Angle</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Arc2d#getAngle <em>Angle</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getArc2d()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaClass='edu.umd.cs.piccolox.nodes.PArc'"
 * @generated
 */
public interface Arc2d extends Graphical2d {
	/**
	 * Returns the value of the '<em><b>Start Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Angle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Angle</em>' attribute.
	 * @see #setStartAngle(double)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getArc2d_StartAngle()
	 * @model
	 * @generated
	 */
	double getStartAngle();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Arc2d#getStartAngle <em>Start Angle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Angle</em>' attribute.
	 * @see #getStartAngle()
	 * @generated
	 */
	void setStartAngle(double value);

	/**
	 * Returns the value of the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Angle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Angle</em>' attribute.
	 * @see #setAngle(double)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getArc2d_Angle()
	 * @model
	 * @generated
	 */
	double getAngle();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Arc2d#getAngle <em>Angle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Angle</em>' attribute.
	 * @see #getAngle()
	 * @generated
	 */
	void setAngle(double value);

} // Arc2d
