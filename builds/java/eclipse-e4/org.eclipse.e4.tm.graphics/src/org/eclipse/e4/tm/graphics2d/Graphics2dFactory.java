/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dFactory.java,v 1.1 2009/08/27 09:15:04 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage
 * @generated
 */
public interface Graphics2dFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Graphics2dFactory eINSTANCE = org.eclipse.e4.tm.graphics2d.impl.Graphics2dFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Graphical2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graphical2d</em>'.
	 * @generated
	 */
	Graphical2d createGraphical2d();

	/**
	 * Returns a new object of class '<em>Polyline2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Polyline2d</em>'.
	 * @generated
	 */
	Polyline2d createPolyline2d();

	/**
	 * Returns a new object of class '<em>Rect2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rect2d</em>'.
	 * @generated
	 */
	Rect2d createRect2d();

	/**
	 * Returns a new object of class '<em>Oval2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Oval2d</em>'.
	 * @generated
	 */
	Oval2d createOval2d();

	/**
	 * Returns a new object of class '<em>RRect2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>RRect2d</em>'.
	 * @generated
	 */
	RRect2d createRRect2d();

	/**
	 * Returns a new object of class '<em>Arc2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Arc2d</em>'.
	 * @generated
	 */
	Arc2d createArc2d();

	/**
	 * Returns a new object of class '<em>Text2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Text2d</em>'.
	 * @generated
	 */
	Text2d createText2d();

	/**
	 * Returns a new object of class '<em>Image2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Image2d</em>'.
	 * @generated
	 */
	Image2d createImage2d();

	/**
	 * Returns a new object of class '<em>Canvas</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Canvas</em>'.
	 * @generated
	 */
	Canvas createCanvas();

	/**
	 * Returns a new object of class '<em>Layer2d</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Layer2d</em>'.
	 * @generated
	 */
	Layer2d createLayer2d();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Graphics2dPackage getGraphics2dPackage();

} //Graphics2dFactory
