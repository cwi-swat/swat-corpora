/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dPackageImpl.java,v 1.2 2009/10/23 20:32:22 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.impl;

import org.eclipse.e4.tm.graphics.util.Dimension;
import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics.util.Transform;

import org.eclipse.e4.tm.graphics2d.Arc2d;
import org.eclipse.e4.tm.graphics2d.Canvas;
import org.eclipse.e4.tm.graphics2d.Graphical2d;
import org.eclipse.e4.tm.graphics2d.Graphics2dFactory;
import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.e4.tm.graphics2d.Image2d;
import org.eclipse.e4.tm.graphics2d.Layer2d;
import org.eclipse.e4.tm.graphics2d.Oval2d;
import org.eclipse.e4.tm.graphics2d.Polyline2d;
import org.eclipse.e4.tm.graphics2d.RRect2d;
import org.eclipse.e4.tm.graphics2d.Rect2d;
import org.eclipse.e4.tm.graphics2d.Text2d;

import org.eclipse.e4.tm.layouts.LayoutsPackage;

import org.eclipse.e4.tm.styles.StylesPackage;

import org.eclipse.e4.tm.util.UtilPackage;

import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Graphics2dPackageImpl extends EPackageImpl implements Graphics2dPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass graphical2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass polyline2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rect2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oval2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rRect2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass arc2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass text2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass image2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass canvasEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass layer2dEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType pointEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType dimensionEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType rectangleEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType transformEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Graphics2dPackageImpl() {
		super(eNS_URI, Graphics2dFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Graphics2dPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Graphics2dPackage init() {
		if (isInited) return (Graphics2dPackage)EPackage.Registry.INSTANCE.getEPackage(Graphics2dPackage.eNS_URI);

		// Obtain or create and register package
		Graphics2dPackageImpl theGraphics2dPackage = (Graphics2dPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Graphics2dPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Graphics2dPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		WidgetsPackage.eINSTANCE.eClass();
		StylesPackage.eINSTANCE.eClass();
		LayoutsPackage.eINSTANCE.eClass();
		UtilPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theGraphics2dPackage.createPackageContents();

		// Initialize created meta-data
		theGraphics2dPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theGraphics2dPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Graphics2dPackage.eNS_URI, theGraphics2dPackage);
		return theGraphics2dPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGraphical2d() {
		return graphical2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGraphical2d_Children() {
		return (EReference)graphical2dEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGraphical2d_Parent() {
		return (EReference)graphical2dEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGraphical2d_Bounds() {
		return (EAttribute)graphical2dEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGraphical2d_Visible() {
		return (EAttribute)graphical2dEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGraphical2d_Transform() {
		return (EAttribute)graphical2dEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPolyline2d() {
		return polyline2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPolyline2d_Points() {
		return (EAttribute)polyline2dEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRect2d() {
		return rect2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOval2d() {
		return oval2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRRect2d() {
		return rRect2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRRect2d_CornerSize() {
		return (EAttribute)rRect2dEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArc2d() {
		return arc2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArc2d_StartAngle() {
		return (EAttribute)arc2dEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArc2d_Angle() {
		return (EAttribute)arc2dEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getText2d() {
		return text2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText2d_Text() {
		return (EAttribute)text2dEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImage2d() {
		return image2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCanvas() {
		return canvasEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCanvas_Layers() {
		return (EReference)canvasEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLayer2d() {
		return layer2dEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getPoint() {
		return pointEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDimension() {
		return dimensionEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRectangle() {
		return rectangleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTransform() {
		return transformEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphics2dFactory getGraphics2dFactory() {
		return (Graphics2dFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		graphical2dEClass = createEClass(GRAPHICAL2D);
		createEReference(graphical2dEClass, GRAPHICAL2D__CHILDREN);
		createEReference(graphical2dEClass, GRAPHICAL2D__PARENT);
		createEAttribute(graphical2dEClass, GRAPHICAL2D__BOUNDS);
		createEAttribute(graphical2dEClass, GRAPHICAL2D__VISIBLE);
		createEAttribute(graphical2dEClass, GRAPHICAL2D__TRANSFORM);

		polyline2dEClass = createEClass(POLYLINE2D);
		createEAttribute(polyline2dEClass, POLYLINE2D__POINTS);

		rect2dEClass = createEClass(RECT2D);

		oval2dEClass = createEClass(OVAL2D);

		rRect2dEClass = createEClass(RRECT2D);
		createEAttribute(rRect2dEClass, RRECT2D__CORNER_SIZE);

		arc2dEClass = createEClass(ARC2D);
		createEAttribute(arc2dEClass, ARC2D__START_ANGLE);
		createEAttribute(arc2dEClass, ARC2D__ANGLE);

		text2dEClass = createEClass(TEXT2D);
		createEAttribute(text2dEClass, TEXT2D__TEXT);

		image2dEClass = createEClass(IMAGE2D);

		canvasEClass = createEClass(CANVAS);
		createEReference(canvasEClass, CANVAS__LAYERS);

		layer2dEClass = createEClass(LAYER2D);

		// Create data types
		pointEDataType = createEDataType(POINT);
		dimensionEDataType = createEDataType(DIMENSION);
		rectangleEDataType = createEDataType(RECTANGLE);
		transformEDataType = createEDataType(TRANSFORM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		UtilPackage theUtilPackage = (UtilPackage)EPackage.Registry.INSTANCE.getEPackage(UtilPackage.eNS_URI);
		StylesPackage theStylesPackage = (StylesPackage)EPackage.Registry.INSTANCE.getEPackage(StylesPackage.eNS_URI);
		WidgetsPackage theWidgetsPackage = (WidgetsPackage)EPackage.Registry.INSTANCE.getEPackage(WidgetsPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		graphical2dEClass.getESuperTypes().add(theUtilPackage.getObjectData());
		graphical2dEClass.getESuperTypes().add(theStylesPackage.getStyled());
		polyline2dEClass.getESuperTypes().add(this.getGraphical2d());
		rect2dEClass.getESuperTypes().add(this.getGraphical2d());
		oval2dEClass.getESuperTypes().add(this.getGraphical2d());
		rRect2dEClass.getESuperTypes().add(this.getRect2d());
		arc2dEClass.getESuperTypes().add(this.getGraphical2d());
		text2dEClass.getESuperTypes().add(this.getGraphical2d());
		image2dEClass.getESuperTypes().add(this.getGraphical2d());
		canvasEClass.getESuperTypes().add(theWidgetsPackage.getComposite());
		layer2dEClass.getESuperTypes().add(this.getGraphical2d());

		// Initialize classes and features; add operations and parameters
		initEClass(graphical2dEClass, Graphical2d.class, "Graphical2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGraphical2d_Children(), this.getGraphical2d(), this.getGraphical2d_Parent(), "children", null, 0, -1, Graphical2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGraphical2d_Parent(), this.getGraphical2d(), this.getGraphical2d_Children(), "parent", null, 0, 1, Graphical2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGraphical2d_Bounds(), this.getRectangle(), "bounds", null, 0, 1, Graphical2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGraphical2d_Visible(), ecorePackage.getEBoolean(), "visible", "true", 0, 1, Graphical2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGraphical2d_Transform(), this.getTransform(), "transform", null, 0, 1, Graphical2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(polyline2dEClass, Polyline2d.class, "Polyline2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPolyline2d_Points(), this.getPoint(), "points", null, 0, -1, Polyline2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rect2dEClass, Rect2d.class, "Rect2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(oval2dEClass, Oval2d.class, "Oval2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(rRect2dEClass, RRect2d.class, "RRect2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRRect2d_CornerSize(), this.getDimension(), "cornerSize", null, 0, 1, RRect2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(arc2dEClass, Arc2d.class, "Arc2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArc2d_StartAngle(), ecorePackage.getEDouble(), "startAngle", null, 0, 1, Arc2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArc2d_Angle(), ecorePackage.getEDouble(), "angle", null, 0, 1, Arc2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(text2dEClass, Text2d.class, "Text2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getText2d_Text(), ecorePackage.getEString(), "text", null, 0, 1, Text2d.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(image2dEClass, Image2d.class, "Image2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(canvasEClass, Canvas.class, "Canvas", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCanvas_Layers(), this.getLayer2d(), null, "layers", null, 0, -1, Canvas.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(layer2dEClass, Layer2d.class, "Layer2d", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize data types
		initEDataType(pointEDataType, Point.class, "Point", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(dimensionEDataType, Dimension.class, "Dimension", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(rectangleEDataType, Rectangle.class, "Rectangle", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(transformEDataType, Transform.class, "Transform", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/e4/swt.ecore
		createSwtAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/swt.ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createSwtAnnotations() {
		String source = "http://www.eclipse.org/e4/swt.ecore";		
		addAnnotation
		  (graphical2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolo.PNode",
			 "access", "property",
			 "binderPackage", "org.eclipse.e4.tm.graphics.builder"
		   });		
		addAnnotation
		  (rect2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolo.nodes.PRect"
		   });		
		addAnnotation
		  (oval2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolox.nodes.POval"
		   });		
		addAnnotation
		  (rRect2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolox.nodes.PRoundedRect"
		   });		
		addAnnotation
		  (arc2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolox.nodes.PArc"
		   });		
		addAnnotation
		  (text2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolo.nodes.PText"
		   });		
		addAnnotation
		  (image2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolo.nodes.PImage"
		   });		
		addAnnotation
		  (canvasEClass, 
		   source, 
		   new String[] {
			 "javaClass", "org.eclipse.swt.widgets.Composite",
			 "binderPackage", "org.eclipse.e4.tm.graphics.builder"
		   });		
		addAnnotation
		  (layer2dEClass, 
		   source, 
		   new String[] {
			 "javaClass", "edu.umd.cs.piccolo.PLayer"
		   });
	}

} //Graphics2dPackageImpl
