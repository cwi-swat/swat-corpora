/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilAdapterFactory.java,v 1.1 2009/10/23 12:40:32 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.util;

import org.eclipse.e4.tm.util.*;
import org.eclipse.e4.tm.util.ListData;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.Scripted;
import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.util.UtilPackage
 * @generated
 */
public class UtilAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UtilPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = UtilPackage.eINSTANCE;
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
	protected UtilSwitch<Adapter> modelSwitch =
		new UtilSwitch<Adapter>() {
			@Override
			public Adapter caseListData(ListData object) {
				return createListDataAdapter();
			}
			@Override
			public Adapter caseTreeData(TreeData object) {
				return createTreeDataAdapter();
			}
			@Override
			public Adapter caseObjectData(ObjectData object) {
				return createObjectDataAdapter();
			}
			@Override
			public Adapter caseScripted(Scripted object) {
				return createScriptedAdapter();
			}
			@Override
			public Adapter caseLabeled(Labeled object) {
				return createLabeledAdapter();
			}
			@Override
			public Adapter caseFeaturesListData(FeaturesListData object) {
				return createFeaturesListDataAdapter();
			}
			@Override
			public Adapter caseFeaturesLabeled(FeaturesLabeled object) {
				return createFeaturesLabeledAdapter();
			}
			@Override
			public Adapter caseFeatureNames(FeatureNames object) {
				return createFeatureNamesAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.ListData <em>List Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.ListData
	 * @generated
	 */
	public Adapter createListDataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.TreeData <em>Tree Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.TreeData
	 * @generated
	 */
	public Adapter createTreeDataAdapter() {
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
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.Labeled <em>Labeled</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.Labeled
	 * @generated
	 */
	public Adapter createLabeledAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.FeaturesListData <em>Features List Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.FeaturesListData
	 * @generated
	 */
	public Adapter createFeaturesListDataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.FeaturesLabeled <em>Features Labeled</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.FeaturesLabeled
	 * @generated
	 */
	public Adapter createFeaturesLabeledAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.tm.util.FeatureNames <em>Feature Names</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.tm.util.FeatureNames
	 * @generated
	 */
	public Adapter createFeatureNamesAdapter() {
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

} //UtilAdapterFactory
