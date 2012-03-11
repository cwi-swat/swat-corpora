/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphics2dSwitch.java,v 1.2 2009/10/23 20:32:21 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d.util;

import java.util.List;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage
 * @generated
 */
public class Graphics2dSwitch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Graphics2dPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Graphics2dSwitch() {
		if (modelPackage == null) {
			modelPackage = Graphics2dPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T1 doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T1 doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case Graphics2dPackage.GRAPHICAL2D: {
				Graphical2d graphical2d = (Graphical2d)theEObject;
				T1 result = caseGraphical2d(graphical2d);
				if (result == null) result = caseObjectData(graphical2d);
				if (result == null) result = caseStyled(graphical2d);
				if (result == null) result = caseScripted(graphical2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.POLYLINE2D: {
				Polyline2d polyline2d = (Polyline2d)theEObject;
				T1 result = casePolyline2d(polyline2d);
				if (result == null) result = caseGraphical2d(polyline2d);
				if (result == null) result = caseObjectData(polyline2d);
				if (result == null) result = caseStyled(polyline2d);
				if (result == null) result = caseScripted(polyline2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.RECT2D: {
				Rect2d rect2d = (Rect2d)theEObject;
				T1 result = caseRect2d(rect2d);
				if (result == null) result = caseGraphical2d(rect2d);
				if (result == null) result = caseObjectData(rect2d);
				if (result == null) result = caseStyled(rect2d);
				if (result == null) result = caseScripted(rect2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.OVAL2D: {
				Oval2d oval2d = (Oval2d)theEObject;
				T1 result = caseOval2d(oval2d);
				if (result == null) result = caseGraphical2d(oval2d);
				if (result == null) result = caseObjectData(oval2d);
				if (result == null) result = caseStyled(oval2d);
				if (result == null) result = caseScripted(oval2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.RRECT2D: {
				RRect2d rRect2d = (RRect2d)theEObject;
				T1 result = caseRRect2d(rRect2d);
				if (result == null) result = caseRect2d(rRect2d);
				if (result == null) result = caseGraphical2d(rRect2d);
				if (result == null) result = caseObjectData(rRect2d);
				if (result == null) result = caseStyled(rRect2d);
				if (result == null) result = caseScripted(rRect2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.ARC2D: {
				Arc2d arc2d = (Arc2d)theEObject;
				T1 result = caseArc2d(arc2d);
				if (result == null) result = caseGraphical2d(arc2d);
				if (result == null) result = caseObjectData(arc2d);
				if (result == null) result = caseStyled(arc2d);
				if (result == null) result = caseScripted(arc2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.TEXT2D: {
				Text2d text2d = (Text2d)theEObject;
				T1 result = caseText2d(text2d);
				if (result == null) result = caseGraphical2d(text2d);
				if (result == null) result = caseObjectData(text2d);
				if (result == null) result = caseStyled(text2d);
				if (result == null) result = caseScripted(text2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.IMAGE2D: {
				Image2d image2d = (Image2d)theEObject;
				T1 result = caseImage2d(image2d);
				if (result == null) result = caseGraphical2d(image2d);
				if (result == null) result = caseObjectData(image2d);
				if (result == null) result = caseStyled(image2d);
				if (result == null) result = caseScripted(image2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.CANVAS: {
				Canvas canvas = (Canvas)theEObject;
				T1 result = caseCanvas(canvas);
				if (result == null) result = caseComposite(canvas);
				if (result == null) result = caseAbstractComposite(canvas);
				if (result == null) result = caseControl(canvas);
				if (result == null) result = caseObjectData(canvas);
				if (result == null) result = caseStyled(canvas);
				if (result == null) result = caseScripted(canvas);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Graphics2dPackage.LAYER2D: {
				Layer2d layer2d = (Layer2d)theEObject;
				T1 result = caseLayer2d(layer2d);
				if (result == null) result = caseGraphical2d(layer2d);
				if (result == null) result = caseObjectData(layer2d);
				if (result == null) result = caseStyled(layer2d);
				if (result == null) result = caseScripted(layer2d);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Graphical2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Graphical2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseGraphical2d(Graphical2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Polyline2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Polyline2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePolyline2d(Polyline2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rect2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rect2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRect2d(Rect2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Oval2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Oval2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseOval2d(Oval2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>RRect2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>RRect2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRRect2d(RRect2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Arc2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Arc2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseArc2d(Arc2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseText2d(Text2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Image2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Image2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseImage2d(Image2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Canvas</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Canvas</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCanvas(Canvas object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Layer2d</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Layer2d</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseLayer2d(Layer2d object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Styled</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Styled</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseStyled(Styled object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Scripted</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scripted</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseScripted(Scripted object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseObjectData(ObjectData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Control</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Control</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseControl(Control object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Composite</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Composite</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends Control> T1 caseAbstractComposite(AbstractComposite<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Composite</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Composite</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseComposite(Composite object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T1 defaultCase(EObject object) {
		return null;
	}

} //Graphics2dSwitch
