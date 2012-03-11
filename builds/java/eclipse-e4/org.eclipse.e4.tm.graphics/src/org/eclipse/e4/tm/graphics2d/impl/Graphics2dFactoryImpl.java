/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dFactoryImpl.java,v 1.2 2009/10/23 20:32:28 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.impl;

import org.eclipse.e4.tm.graphics.util.Dimension;
import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics.util.Transform;

import org.eclipse.e4.tm.graphics2d.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Graphics2dFactoryImpl extends EFactoryImpl implements Graphics2dFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Graphics2dFactory init() {
		try {
			Graphics2dFactory theGraphics2dFactory = (Graphics2dFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/e4/tm/graphics2d/graphics2d.ecore"); 
			if (theGraphics2dFactory != null) {
				return theGraphics2dFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Graphics2dFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphics2dFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Graphics2dPackage.GRAPHICAL2D: return createGraphical2d();
			case Graphics2dPackage.POLYLINE2D: return createPolyline2d();
			case Graphics2dPackage.RECT2D: return createRect2d();
			case Graphics2dPackage.OVAL2D: return createOval2d();
			case Graphics2dPackage.RRECT2D: return createRRect2d();
			case Graphics2dPackage.ARC2D: return createArc2d();
			case Graphics2dPackage.TEXT2D: return createText2d();
			case Graphics2dPackage.IMAGE2D: return createImage2d();
			case Graphics2dPackage.CANVAS: return createCanvas();
			case Graphics2dPackage.LAYER2D: return createLayer2d();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Graphics2dPackage.POINT:
				return createPointFromString(eDataType, initialValue);
			case Graphics2dPackage.DIMENSION:
				return createDimensionFromString(eDataType, initialValue);
			case Graphics2dPackage.RECTANGLE:
				return createRectangleFromString(eDataType, initialValue);
			case Graphics2dPackage.TRANSFORM:
				return createTransformFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Graphics2dPackage.POINT:
				return convertPointToString(eDataType, instanceValue);
			case Graphics2dPackage.DIMENSION:
				return convertDimensionToString(eDataType, instanceValue);
			case Graphics2dPackage.RECTANGLE:
				return convertRectangleToString(eDataType, instanceValue);
			case Graphics2dPackage.TRANSFORM:
				return convertTransformToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphical2d createGraphical2d() {
		Graphical2dImpl graphical2d = new Graphical2dImpl();
		return graphical2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Polyline2d createPolyline2d() {
		Polyline2dImpl polyline2d = new Polyline2dImpl();
		return polyline2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rect2d createRect2d() {
		Rect2dImpl rect2d = new Rect2dImpl();
		return rect2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Oval2d createOval2d() {
		Oval2dImpl oval2d = new Oval2dImpl();
		return oval2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RRect2d createRRect2d() {
		RRect2dImpl rRect2d = new RRect2dImpl();
		return rRect2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Arc2d createArc2d() {
		Arc2dImpl arc2d = new Arc2dImpl();
		return arc2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Text2d createText2d() {
		Text2dImpl text2d = new Text2dImpl();
		return text2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Image2d createImage2d() {
		Image2dImpl image2d = new Image2dImpl();
		return image2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Canvas createCanvas() {
		CanvasImpl canvas = new CanvasImpl();
		return canvas;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer2d createLayer2d() {
		Layer2dImpl layer2d = new Layer2dImpl();
		return layer2d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Point createPointFromString(EDataType eDataType, String initialValue) {
		return (Point)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPointToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dimension createDimensionFromString(EDataType eDataType, String initialValue) {
		return (Dimension)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDimensionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rectangle createRectangleFromString(EDataType eDataType, String initialValue) {
		return (Rectangle)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRectangleToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transform createTransformFromString(EDataType eDataType, String initialValue) {
		return (Transform)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTransformToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphics2dPackage getGraphics2dPackage() {
		return (Graphics2dPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Graphics2dPackage getPackage() {
		return Graphics2dPackage.eINSTANCE;
	}

} //Graphics2dFactoryImpl
