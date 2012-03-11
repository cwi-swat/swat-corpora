/**
 * <copyright>
 * </copyright>
 *
 * $Id: Graphical2d.java,v 1.2 2009/10/23 20:32:21 htraetteb Exp $
 */
package org.eclipse.e4.tm.graphics2d;

import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics.util.Transform;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Graphical2d</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getChildren <em>Children</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getBounds <em>Bounds</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Graphical2d#isVisible <em>Visible</em>}</li>
 *   <li>{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getTransform <em>Transform</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d()
 * @model annotation="http://www.eclipse.org/e4/swt.ecore javaClass='edu.umd.cs.piccolo.PNode' access='property' binderPackage='org.eclipse.e4.tm.graphics.builder'"
 * @generated
 */
public interface Graphical2d extends ObjectData, Styled {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.e4.tm.graphics2d.Graphical2d}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d_Children()
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<Graphical2d> getChildren();

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(Graphical2d)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d_Parent()
	 * @see org.eclipse.e4.tm.graphics2d.Graphical2d#getChildren
	 * @model opposite="children" transient="false"
	 * @generated
	 */
	Graphical2d getParent();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(Graphical2d value);

	/**
	 * Returns the value of the '<em><b>Bounds</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bounds</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bounds</em>' attribute.
	 * @see #setBounds(Rectangle)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d_Bounds()
	 * @model dataType="org.eclipse.e4.tm.graphics2d.Rectangle"
	 * @generated
	 */
	Rectangle getBounds();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getBounds <em>Bounds</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bounds</em>' attribute.
	 * @see #getBounds()
	 * @generated
	 */
	void setBounds(Rectangle value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #setVisible(boolean)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d_Visible()
	 * @model default="true"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#isVisible <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Returns the value of the '<em><b>Transform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transform</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transform</em>' attribute.
	 * @see #setTransform(Transform)
	 * @see org.eclipse.e4.tm.graphics2d.Graphics2dPackage#getGraphical2d_Transform()
	 * @model dataType="org.eclipse.e4.tm.graphics2d.Transform"
	 * @generated
	 */
	Transform getTransform();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.tm.graphics2d.Graphical2d#getTransform <em>Transform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transform</em>' attribute.
	 * @see #getTransform()
	 * @generated
	 */
	void setTransform(Transform value);

} // Graphical2d
