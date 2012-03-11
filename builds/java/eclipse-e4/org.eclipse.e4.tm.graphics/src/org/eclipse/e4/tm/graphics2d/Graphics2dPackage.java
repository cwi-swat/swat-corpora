/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dPackage.java,v 1.2 2009/10/23 20:32:21 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.styles.StylesPackage;

import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dFactory
 * @model kind="package"
 * @generated
 */
public interface Graphics2dPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "graphics2d";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/e4/tm/graphics2d/graphics2d.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tm.graphics2d";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Graphics2dPackage eINSTANCE = org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl <em>Graphical2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getGraphical2d()
	 * @generated
	 */
	int GRAPHICAL2D = 0;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__SCRIPT_SOURCE = UtilPackage.OBJECT_DATA__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__DATA_OBJECT = UtilPackage.OBJECT_DATA__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__NAME = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__ROLE = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__STYLE = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__CHILDREN = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__PARENT = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__BOUNDS = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__VISIBLE = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D__TRANSFORM = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Graphical2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPHICAL2D_FEATURE_COUNT = UtilPackage.OBJECT_DATA_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Polyline2dImpl <em>Polyline2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Polyline2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getPolyline2d()
	 * @generated
	 */
	int POLYLINE2D = 1;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The feature id for the '<em><b>Points</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D__POINTS = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Polyline2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POLYLINE2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Rect2dImpl <em>Rect2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Rect2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRect2d()
	 * @generated
	 */
	int RECT2D = 2;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The number of structural features of the '<em>Rect2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RECT2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Oval2dImpl <em>Oval2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Oval2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getOval2d()
	 * @generated
	 */
	int OVAL2D = 3;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The number of structural features of the '<em>Oval2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OVAL2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.RRect2dImpl <em>RRect2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.RRect2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRRect2d()
	 * @generated
	 */
	int RRECT2D = 4;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__SCRIPT_SOURCE = RECT2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__DATA_OBJECT = RECT2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__NAME = RECT2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__ROLE = RECT2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__STYLE = RECT2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__CHILDREN = RECT2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__PARENT = RECT2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__BOUNDS = RECT2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__VISIBLE = RECT2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__TRANSFORM = RECT2D__TRANSFORM;

	/**
	 * The feature id for the '<em><b>Corner Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D__CORNER_SIZE = RECT2D_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>RRect2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RRECT2D_FEATURE_COUNT = RECT2D_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Arc2dImpl <em>Arc2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Arc2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getArc2d()
	 * @generated
	 */
	int ARC2D = 5;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The feature id for the '<em><b>Start Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__START_ANGLE = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Angle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D__ANGLE = GRAPHICAL2D_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Arc2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARC2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Text2dImpl <em>Text2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Text2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getText2d()
	 * @generated
	 */
	int TEXT2D = 6;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D__TEXT = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Text2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Image2dImpl <em>Image2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Image2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getImage2d()
	 * @generated
	 */
	int IMAGE2D = 7;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The number of structural features of the '<em>Image2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.CanvasImpl <em>Canvas</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.CanvasImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getCanvas()
	 * @generated
	 */
	int CANVAS = 8;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__SCRIPT_SOURCE = WidgetsPackage.COMPOSITE__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__DATA_OBJECT = WidgetsPackage.COMPOSITE__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__NAME = WidgetsPackage.COMPOSITE__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__ROLE = WidgetsPackage.COMPOSITE__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__STYLE = WidgetsPackage.COMPOSITE__STYLE;

	/**
	 * The feature id for the '<em><b>Composite</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__COMPOSITE = WidgetsPackage.COMPOSITE__COMPOSITE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__ENABLED = WidgetsPackage.COMPOSITE__ENABLED;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__VISIBLE = WidgetsPackage.COMPOSITE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Layout Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__LAYOUT_DATA = WidgetsPackage.COMPOSITE__LAYOUT_DATA;

	/**
	 * The feature id for the '<em><b>Tool Tip</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__TOOL_TIP = WidgetsPackage.COMPOSITE__TOOL_TIP;

	/**
	 * The feature id for the '<em><b>Controls</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__CONTROLS = WidgetsPackage.COMPOSITE__CONTROLS;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__STYLES = WidgetsPackage.COMPOSITE__STYLES;

	/**
	 * The feature id for the '<em><b>Layout</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__LAYOUT = WidgetsPackage.COMPOSITE__LAYOUT;

	/**
	 * The feature id for the '<em><b>Layers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS__LAYERS = WidgetsPackage.COMPOSITE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Canvas</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CANVAS_FEATURE_COUNT = WidgetsPackage.COMPOSITE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.graphics2d.impl.Layer2dImpl <em>Layer2d</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics2d.impl.Layer2dImpl
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getLayer2d()
	 * @generated
	 */
	int LAYER2D = 9;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__SCRIPT_SOURCE = GRAPHICAL2D__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__DATA_OBJECT = GRAPHICAL2D__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__NAME = GRAPHICAL2D__NAME;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__ROLE = GRAPHICAL2D__ROLE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__STYLE = GRAPHICAL2D__STYLE;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__CHILDREN = GRAPHICAL2D__CHILDREN;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__PARENT = GRAPHICAL2D__PARENT;

	/**
	 * The feature id for the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__BOUNDS = GRAPHICAL2D__BOUNDS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__VISIBLE = GRAPHICAL2D__VISIBLE;

	/**
	 * The feature id for the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D__TRANSFORM = GRAPHICAL2D__TRANSFORM;

	/**
	 * The number of structural features of the '<em>Layer2d</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER2D_FEATURE_COUNT = GRAPHICAL2D_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '<em>Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics.util.Point
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getPoint()
	 * @generated
	 */
	int POINT = 10;

	/**
	 * The meta object id for the '<em>Dimension</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics.util.Dimension
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getDimension()
	 * @generated
	 */
	int DIMENSION = 11;

	/**
	 * The meta object id for the '<em>Rectangle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics.util.Rectangle
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRectangle()
	 * @generated
	 */
	int RECTANGLE = 12;

	/**
	 * The meta object id for the '<em>Transform</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.graphics.util.Transform
	 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getTransform()
	 * @generated
	 */
	int TRANSFORM = 13;


	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Graphical2d <em>Graphical2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graphical2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d
	 * @generated
	 */
	EClass getGraphical2d();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getChildren()
	 * @see #getGraphical2d()
	 * @generated
	 */
	EReference getGraphical2d_Children();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getParent()
	 * @see #getGraphical2d()
	 * @generated
	 */
	EReference getGraphical2d_Parent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getBounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bounds</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getBounds()
	 * @see #getGraphical2d()
	 * @generated
	 */
	EAttribute getGraphical2d_Bounds();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#isVisible()
	 * @see #getGraphical2d()
	 * @generated
	 */
	EAttribute getGraphical2d_Visible();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getTransform <em>Transform</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transform</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getTransform()
	 * @see #getGraphical2d()
	 * @generated
	 */
	EAttribute getGraphical2d_Transform();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Polyline2d <em>Polyline2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Polyline2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Polyline2d
	 * @generated
	 */
	EClass getPolyline2d();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.tm.graphics2d.Polyline2d#getPoints <em>Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Points</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Polyline2d#getPoints()
	 * @see #getPolyline2d()
	 * @generated
	 */
	EAttribute getPolyline2d_Points();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Rect2d <em>Rect2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rect2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Rect2d
	 * @generated
	 */
	EClass getRect2d();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Oval2d <em>Oval2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Oval2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Oval2d
	 * @generated
	 */
	EClass getOval2d();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.RRect2d <em>RRect2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>RRect2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.RRect2d
	 * @generated
	 */
	EClass getRRect2d();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.RRect2d#getCornerSize <em>Corner Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Corner Size</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.RRect2d#getCornerSize()
	 * @see #getRRect2d()
	 * @generated
	 */
	EAttribute getRRect2d_CornerSize();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Arc2d <em>Arc2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Arc2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Arc2d
	 * @generated
	 */
	EClass getArc2d();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Arc2d#getStartAngle <em>Start Angle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Angle</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Arc2d#getStartAngle()
	 * @see #getArc2d()
	 * @generated
	 */
	EAttribute getArc2d_StartAngle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Arc2d#getAngle <em>Angle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Angle</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Arc2d#getAngle()
	 * @see #getArc2d()
	 * @generated
	 */
	EAttribute getArc2d_Angle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Text2d <em>Text2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Text2d
	 * @generated
	 */
	EClass getText2d();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.graphics2d.Text2d#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Text2d#getText()
	 * @see #getText2d()
	 * @generated
	 */
	EAttribute getText2d_Text();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Image2d <em>Image2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Image2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Image2d
	 * @generated
	 */
	EClass getImage2d();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Canvas <em>Canvas</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Canvas</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Canvas
	 * @generated
	 */
	EClass getCanvas();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.e4.tm.graphics2d.Canvas#getLayers <em>Layers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Layers</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Canvas#getLayers()
	 * @see #getCanvas()
	 * @generated
	 */
	EReference getCanvas_Layers();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.graphics2d.Layer2d <em>Layer2d</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layer2d</em>'.
	 * @see org.eclipse.e4.tm.graphics2d.Layer2d
	 * @generated
	 */
	EClass getLayer2d();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.e4.tm.graphics.util.Point <em>Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Point</em>'.
	 * @see org.eclipse.e4.tm.graphics.util.Point
	 * @model instanceClass="org.eclipse.e4.tm.graphics.util.Point"
	 * @generated
	 */
	EDataType getPoint();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.e4.tm.graphics.util.Dimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Dimension</em>'.
	 * @see org.eclipse.e4.tm.graphics.util.Dimension
	 * @model instanceClass="org.eclipse.e4.tm.graphics.util.Dimension"
	 * @generated
	 */
	EDataType getDimension();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.e4.tm.graphics.util.Rectangle <em>Rectangle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Rectangle</em>'.
	 * @see org.eclipse.e4.tm.graphics.util.Rectangle
	 * @model instanceClass="org.eclipse.e4.tm.graphics.util.Rectangle"
	 * @generated
	 */
	EDataType getRectangle();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.e4.tm.graphics.util.Transform <em>Transform</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Transform</em>'.
	 * @see org.eclipse.e4.tm.graphics.util.Transform
	 * @model instanceClass="org.eclipse.e4.tm.graphics.util.Transform"
	 * @generated
	 */
	EDataType getTransform();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Graphics2dFactory getGraphics2dFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl <em>Graphical2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphical2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getGraphical2d()
		 * @generated
		 */
		EClass GRAPHICAL2D = eINSTANCE.getGraphical2d();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPHICAL2D__CHILDREN = eINSTANCE.getGraphical2d_Children();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPHICAL2D__PARENT = eINSTANCE.getGraphical2d_Parent();

		/**
		 * The meta object literal for the '<em><b>Bounds</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPHICAL2D__BOUNDS = eINSTANCE.getGraphical2d_Bounds();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPHICAL2D__VISIBLE = eINSTANCE.getGraphical2d_Visible();

		/**
		 * The meta object literal for the '<em><b>Transform</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPHICAL2D__TRANSFORM = eINSTANCE.getGraphical2d_Transform();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Polyline2dImpl <em>Polyline2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Polyline2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getPolyline2d()
		 * @generated
		 */
		EClass POLYLINE2D = eINSTANCE.getPolyline2d();

		/**
		 * The meta object literal for the '<em><b>Points</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POLYLINE2D__POINTS = eINSTANCE.getPolyline2d_Points();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Rect2dImpl <em>Rect2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Rect2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRect2d()
		 * @generated
		 */
		EClass RECT2D = eINSTANCE.getRect2d();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Oval2dImpl <em>Oval2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Oval2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getOval2d()
		 * @generated
		 */
		EClass OVAL2D = eINSTANCE.getOval2d();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.RRect2dImpl <em>RRect2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.RRect2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRRect2d()
		 * @generated
		 */
		EClass RRECT2D = eINSTANCE.getRRect2d();

		/**
		 * The meta object literal for the '<em><b>Corner Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RRECT2D__CORNER_SIZE = eINSTANCE.getRRect2d_CornerSize();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Arc2dImpl <em>Arc2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Arc2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getArc2d()
		 * @generated
		 */
		EClass ARC2D = eINSTANCE.getArc2d();

		/**
		 * The meta object literal for the '<em><b>Start Angle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARC2D__START_ANGLE = eINSTANCE.getArc2d_StartAngle();

		/**
		 * The meta object literal for the '<em><b>Angle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ARC2D__ANGLE = eINSTANCE.getArc2d_Angle();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Text2dImpl <em>Text2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Text2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getText2d()
		 * @generated
		 */
		EClass TEXT2D = eINSTANCE.getText2d();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEXT2D__TEXT = eINSTANCE.getText2d_Text();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Image2dImpl <em>Image2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Image2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getImage2d()
		 * @generated
		 */
		EClass IMAGE2D = eINSTANCE.getImage2d();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.CanvasImpl <em>Canvas</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.CanvasImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getCanvas()
		 * @generated
		 */
		EClass CANVAS = eINSTANCE.getCanvas();

		/**
		 * The meta object literal for the '<em><b>Layers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CANVAS__LAYERS = eINSTANCE.getCanvas_Layers();

		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.graphics2d.impl.Layer2dImpl <em>Layer2d</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics2d.impl.Layer2dImpl
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getLayer2d()
		 * @generated
		 */
		EClass LAYER2D = eINSTANCE.getLayer2d();

		/**
		 * The meta object literal for the '<em>Point</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics.util.Point
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getPoint()
		 * @generated
		 */
		EDataType POINT = eINSTANCE.getPoint();

		/**
		 * The meta object literal for the '<em>Dimension</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics.util.Dimension
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getDimension()
		 * @generated
		 */
		EDataType DIMENSION = eINSTANCE.getDimension();

		/**
		 * The meta object literal for the '<em>Rectangle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics.util.Rectangle
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getRectangle()
		 * @generated
		 */
		EDataType RECTANGLE = eINSTANCE.getRectangle();

		/**
		 * The meta object literal for the '<em>Transform</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.graphics.util.Transform
		 * @see org.eclipse.e4.tm.graphics2d.impl.Graphics2dPackageImpl#getTransform()
		 * @generated
		 */
		EDataType TRANSFORM = eINSTANCE.getTransform();

	}

} //Graphics2dPackage
