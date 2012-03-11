/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilSwitch.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.util;

import java.util.List;

import org.eclipse.e4.tm.util.*;
import org.eclipse.e4.tm.util.ListData;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.Scripted;
import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.e4.tm.util.UtilPackage;
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
 * @see org.eclipse.e4.tm.util.UtilPackage
 * @generated
 */
public class UtilSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UtilPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilSwitch() {
		if (modelPackage == null) {
			modelPackage = UtilPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
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
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case UtilPackage.LIST_DATA: {
				ListData listData = (ListData)theEObject;
				T result = caseListData(listData);
				if (result == null) result = caseObjectData(listData);
				if (result == null) result = caseScripted(listData);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.TREE_DATA: {
				TreeData treeData = (TreeData)theEObject;
				T result = caseTreeData(treeData);
				if (result == null) result = caseListData(treeData);
				if (result == null) result = caseObjectData(treeData);
				if (result == null) result = caseScripted(treeData);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.OBJECT_DATA: {
				ObjectData objectData = (ObjectData)theEObject;
				T result = caseObjectData(objectData);
				if (result == null) result = caseScripted(objectData);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.SCRIPTED: {
				Scripted scripted = (Scripted)theEObject;
				T result = caseScripted(scripted);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.LABELED: {
				Labeled labeled = (Labeled)theEObject;
				T result = caseLabeled(labeled);
				if (result == null) result = caseObjectData(labeled);
				if (result == null) result = caseScripted(labeled);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.FEATURES_LIST_DATA: {
				FeaturesListData featuresListData = (FeaturesListData)theEObject;
				T result = caseFeaturesListData(featuresListData);
				if (result == null) result = caseListData(featuresListData);
				if (result == null) result = caseFeatureNames(featuresListData);
				if (result == null) result = caseObjectData(featuresListData);
				if (result == null) result = caseScripted(featuresListData);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.FEATURES_LABELED: {
				FeaturesLabeled featuresLabeled = (FeaturesLabeled)theEObject;
				T result = caseFeaturesLabeled(featuresLabeled);
				if (result == null) result = caseLabeled(featuresLabeled);
				if (result == null) result = caseFeatureNames(featuresLabeled);
				if (result == null) result = caseObjectData(featuresLabeled);
				if (result == null) result = caseScripted(featuresLabeled);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case UtilPackage.FEATURE_NAMES: {
				FeatureNames featureNames = (FeatureNames)theEObject;
				T result = caseFeatureNames(featureNames);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseListData(ListData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tree Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tree Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTreeData(TreeData object) {
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
	public T caseObjectData(ObjectData object) {
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
	public T caseScripted(Scripted object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Labeled</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Labeled</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLabeled(Labeled object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Features List Data</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Features List Data</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFeaturesListData(FeaturesListData object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Features Labeled</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Features Labeled</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFeaturesLabeled(FeaturesLabeled object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Names</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Names</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFeatureNames(FeatureNames object) {
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
	public T defaultCase(EObject object) {
		return null;
	}

} //UtilSwitch
