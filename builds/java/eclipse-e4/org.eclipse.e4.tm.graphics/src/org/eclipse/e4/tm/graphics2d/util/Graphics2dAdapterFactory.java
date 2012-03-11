/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dAdapterFactory.java,v 1.2 2009/10/23 20:32:21 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.util;

import org.eclipse.e4.tm.graphics2d.Arc2d;
import org.eclipse.e4.tm.graphics2d.Canvas;
import org.eclipse.e4.tm.graphics2d.Graphical2d;
import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.e4.tm.graphics2d.Image2d;
import org.eclipse.e4.tm.graphics2d.Layer2d;
import org.eclipse.e4.tm.graphics2d.Oval2d;
import org.eclipse.e4.tm.graphics2d.Polyline2d;
import org.eclipse.e4.tm.graphics2d.RRect2d;
import org.eclipse.e4.tm.graphics2d.Rect2d;
import org.eclipse.e4.tm.graphics2d.Text2d;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.Scripted;
import org.eclipse.e4.tm.widgets.AbstractComposite;
import org.eclipse.e4.tm.widgets.Composite;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage
 * @generated
 */
public class Graphics2dAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Graphics2dPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphics2dAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Graphics2dPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Graphics2dSwitch<Adapter> modelSwitch =
		new Graphics2dSwitch<Adapter>() {
			@Override
			public Adapter caseGraphical2d(Graphical2d object) {
				return createGraphical2dAdapter();
			}
			@Override
			public Adapter casePolyline2d(Polyline2d object) {
				return createPolyline2dAdapter();
			}
			@Override
			public Adapter caseRect2d(Rect2d object) {
				return createRect2dAdapter();
			}
			@Override
			public Adapter caseOval2d(Oval2d object) {
				return createOval2dAdapter();
			}
			@Override
			public Adapter caseRRect2d(RRect2d object) {
				return createRRect2dAdapter();
			}
			@Override
			public Adapter caseArc2d(Arc2d object) {
				return createArc2dAdapter();
			}
			@Override
			public Adapter caseText2d(Text2d object) {
				return createText2dAdapter();
			}
			@Override
			public Adapter caseImage2d(Image2d object) {
				return createImage2dAdapter();
			}
			@Override
			public Adapter caseCanvas(Canvas object) {
				return createCanvasAdapter();
			}
			@Override
			public Adapter caseLayer2d(Layer2d object) {
				return createLayer2dAdapter();
			}
			@Override
			public Adapter caseScripted(Scripted object) {
				return createScriptedAdapter();
			}
			@Override
			public Adapter caseObjectData(ObjectData object) {
				return createObjectDataAdapter();
			}
			@Override
			public Adapter caseStyled(Styled object) {
				return createStyledAdapter();
			}
			@Override
			public Adapter caseControl(Control object) {
				return createControlAdapter();
			}
			@Override
			public <T extends Control> Adapter caseAbstractComposite(AbstractComposite<T> object) {
				return createAbstractCompositeAdapter();
			}
			@Override
			public Adapter caseComposite(Composite object) {
				return createCompositeAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Graphical2d <em>Graphical2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d
	 * @generated
	 */
	public Adapter createGraphical2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Polyline2d <em>Polyline2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Polyline2d
	 * @generated
	 */
	public Adapter createPolyline2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Rect2d <em>Rect2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Rect2d
	 * @generated
	 */
	public Adapter createRect2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Oval2d <em>Oval2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Oval2d
	 * @generated
	 */
	public Adapter createOval2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.RRect2d <em>RRect2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.RRect2d
	 * @generated
	 */
	public Adapter createRRect2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Arc2d <em>Arc2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Arc2d
	 * @generated
	 */
	public Adapter createArc2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Text2d <em>Text2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Text2d
	 * @generated
	 */
	public Adapter createText2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Image2d <em>Image2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Image2d
	 * @generated
	 */
	public Adapter createImage2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Canvas <em>Canvas</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Canvas
	 * @generated
	 */
	public Adapter createCanvasAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.graphics2d.Layer2d <em>Layer2d</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.graphics2d.Layer2d
	 * @generated
	 */
	public Adapter createLayer2dAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.styles.Styled <em>Styled</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.styles.Styled
	 * @generated
	 */
	public Adapter createStyledAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.Scripted <em>Scripted</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.Scripted
	 * @generated
	 */
	public Adapter createScriptedAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.ObjectData <em>Object Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.ObjectData
	 * @generated
	 */
	public Adapter createObjectDataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.widgets.Control <em>Control</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.widgets.Control
	 * @generated
	 */
	public Adapter createControlAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.widgets.AbstractComposite <em>Abstract Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.widgets.AbstractComposite
	 * @generated
	 */
	public Adapter createAbstractCompositeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.widgets.Composite <em>Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.widgets.Composite
	 * @generated
	 */
	public Adapter createCompositeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //Graphics2dAdapterFactory
