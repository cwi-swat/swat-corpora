/**
 * <copyright>
 * </copyright>
 *
 * $Id: Canvas.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.e4.tm.widgets.Composite;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Canvas</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Canvas#getLayers <em>Layers</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getCanvas()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaClass='org.eclipse.swt.widgets.Composite' binderPackage='org.eclipse.e4.tm.graphics.builder'"
 * @generated
 */
public interface Canvas extends Composite {
	/**
	 * Returns the value of the '<em><b>Layers</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.tm.graphics2d.Layer2d}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Layers</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Layers</em>' containment reference list.
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getCanvas_Layers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Layer2d> getLayers();

} // Canvas
