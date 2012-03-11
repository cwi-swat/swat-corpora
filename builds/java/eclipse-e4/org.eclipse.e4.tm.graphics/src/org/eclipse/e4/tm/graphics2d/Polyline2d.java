/**
 * <copyright>
 * </copyright>
 *
 * $Id: Polyline2d.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.e4.tm.graphics.util.Point;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Polyline2d</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Polyline2d#getPoints <em>Points</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getPolyline2d()
 * @model
 * @generated
 */
public interface Polyline2d extends Graphical2d {
	/**
	 * Returns the value of the '<em><b>Points</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.e4.tm.graphics.util.Point}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Points</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Points</em>' attribute list.
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getPolyline2d_Points()
	 * @model dataType="org.eclipse.e4.tm.graphics2d.Point"
	 * @generated
	 */
	EList<Point> getPoints();

} // Polyline2d
