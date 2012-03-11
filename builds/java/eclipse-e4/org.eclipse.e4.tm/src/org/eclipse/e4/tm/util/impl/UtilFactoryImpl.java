/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilFactoryImpl.java,v 1.4 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import org.eclipse.e4.tm.util.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.e4.tm.widgets.WidgetsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UtilFactoryImpl extends EFactoryImpl implements UtilFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static UtilFactory init() {
		try {
			UtilFactory theUtilFactory = (UtilFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/plugin/org.eclipse.e4.tm/model/tm/util.ecore"); 
			if (theUtilFactory != null) {
				return theUtilFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new UtilFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilFactoryImpl() {
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
			case UtilPackage.LIST_DATA: return createListData();
			case UtilPackage.TREE_DATA: return createTreeData();
			case UtilPackage.OBJECT_DATA: return createObjectData();
			case UtilPackage.LABELED: return createLabeled();
			case UtilPackage.FEATURES_LIST_DATA: return createFeaturesListData();
			case UtilPackage.FEATURES_LABELED: return createFeaturesLabeled();
			case UtilPackage.FEATURE_NAMES: return createFeatureNames();
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
			case UtilPackage.LIMITED_STRING:
				return createLimitedStringFromString(eDataType, initialValue);
			case UtilPackage.URI:
				return createURIFromString(eDataType, initialValue);
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
			case UtilPackage.LIMITED_STRING:
				return convertLimitedStringToString(eDataType, instanceValue);
			case UtilPackage.URI:
				return convertURIToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListData createListData() {
		ListDataImpl listData = new ListDataImpl();
		return listData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeData createTreeData() {
		TreeDataImpl treeData = new TreeDataImpl();
		return treeData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ObjectData createObjectData() {
		ObjectDataImpl objectData = new ObjectDataImpl();
		return objectData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Labeled createLabeled() {
		LabeledImpl labeled = new LabeledImpl();
		return labeled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesListData createFeaturesListData() {
		FeaturesListDataImpl featuresListData = new FeaturesListDataImpl();
		return featuresListData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeaturesLabeled createFeaturesLabeled() {
		FeaturesLabeledImpl featuresLabeled = new FeaturesLabeledImpl();
		return featuresLabeled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureNames createFeatureNames() {
		FeatureNamesImpl featureNames = new FeatureNamesImpl();
		return featureNames;
	}

	//
	
	private String createLimitedStringFromStringHelper(EDataType eDataType, String initialValue) {
		String pattern = EcoreUtil.getAnnotation(eDataType, UtilPackage.eNS_URI, "stringPattern");
		if (pattern == null || pattern.length() == 0) {
			pattern = ".+";
		}
		if (! initialValue.matches(pattern)) {
			return null;
		}
		return (String)super.createFromString(eDataType, initialValue);
	}
	private String convertLimitedStringToStringHelper(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String createLimitedStringFromString(EDataType eDataType, String initialValue) {
		return createLimitedStringFromStringHelper(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertLimitedStringToString(EDataType eDataType, Object instanceValue) {
		return convertLimitedStringToStringHelper(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URI createURIFromString(EDataType eDataType, String initialValue) {
		return (URI)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertURIToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilPackage getUtilPackage() {
		return (UtilPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static UtilPackage getPackage() {
		return UtilPackage.eINSTANCE;
	}

} //UtilFactoryImpl
