/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilPackage.java,v 1.4 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.util;

import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.e4.tm.util.UtilFactory
 * @model kind="package"
 * @generated
 */
public interface UtilPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "util";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/plugin/org.eclipse.e4.tm/model/tm/util.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tm.util";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UtilPackage eINSTANCE = org.eclipse.e4.tm.util.impl.UtilPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.ScriptedImpl <em>Scripted</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.ScriptedImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getScripted()
	 * @generated
	 */
	int SCRIPTED = 3;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPTED__SCRIPT_SOURCE = 0;

	/**
	 * The number of structural features of the '<em>Scripted</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPTED_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.ObjectDataImpl <em>Object Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.ObjectDataImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getObjectData()
	 * @generated
	 */
	int OBJECT_DATA = 2;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DATA__SCRIPT_SOURCE = SCRIPTED__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DATA__DATA_OBJECT = SCRIPTED_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Object Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DATA_FEATURE_COUNT = SCRIPTED_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.ListDataImpl <em>List Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.ListDataImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getListData()
	 * @generated
	 */
	int LIST_DATA = 0;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_DATA__SCRIPT_SOURCE = OBJECT_DATA__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_DATA__DATA_OBJECT = OBJECT_DATA__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Data Objects</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_DATA__DATA_OBJECTS = OBJECT_DATA_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>List Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_DATA_FEATURE_COUNT = OBJECT_DATA_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.TreeDataImpl <em>Tree Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.TreeDataImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getTreeData()
	 * @generated
	 */
	int TREE_DATA = 1;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA__SCRIPT_SOURCE = LIST_DATA__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA__DATA_OBJECT = LIST_DATA__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Data Objects</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA__DATA_OBJECTS = LIST_DATA__DATA_OBJECTS;

	/**
	 * The feature id for the '<em><b>Parent Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA__PARENT_DATA_OBJECT = LIST_DATA_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Leaf</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA__LEAF = LIST_DATA_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Tree Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_DATA_FEATURE_COUNT = LIST_DATA_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.LabeledImpl <em>Labeled</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.LabeledImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getLabeled()
	 * @generated
	 */
	int LABELED = 4;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED__SCRIPT_SOURCE = OBJECT_DATA__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED__DATA_OBJECT = OBJECT_DATA__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED__TEXT = OBJECT_DATA_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Image</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED__IMAGE = OBJECT_DATA_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED__FORMAT = OBJECT_DATA_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Labeled</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABELED_FEATURE_COUNT = OBJECT_DATA_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.FeaturesListDataImpl <em>Features List Data</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.FeaturesListDataImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeaturesListData()
	 * @generated
	 */
	int FEATURES_LIST_DATA = 5;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LIST_DATA__SCRIPT_SOURCE = LIST_DATA__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LIST_DATA__DATA_OBJECT = LIST_DATA__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Data Objects</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LIST_DATA__DATA_OBJECTS = LIST_DATA__DATA_OBJECTS;

	/**
	 * The feature id for the '<em><b>Feature Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LIST_DATA__FEATURE_NAMES = LIST_DATA_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Features List Data</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LIST_DATA_FEATURE_COUNT = LIST_DATA_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.FeaturesLabeledImpl <em>Features Labeled</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.FeaturesLabeledImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeaturesLabeled()
	 * @generated
	 */
	int FEATURES_LABELED = 6;

	/**
	 * The feature id for the '<em><b>Script Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__SCRIPT_SOURCE = LABELED__SCRIPT_SOURCE;

	/**
	 * The feature id for the '<em><b>Data Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__DATA_OBJECT = LABELED__DATA_OBJECT;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__TEXT = LABELED__TEXT;

	/**
	 * The feature id for the '<em><b>Image</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__IMAGE = LABELED__IMAGE;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__FORMAT = LABELED__FORMAT;

	/**
	 * The feature id for the '<em><b>Feature Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED__FEATURE_NAMES = LABELED_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Features Labeled</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURES_LABELED_FEATURE_COUNT = LABELED_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.e4.tm.util.impl.FeatureNamesImpl <em>Feature Names</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.e4.tm.util.impl.FeatureNamesImpl
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeatureNames()
	 * @generated
	 */
	int FEATURE_NAMES = 7;

	/**
	 * The feature id for the '<em><b>Feature Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_NAMES__FEATURE_NAMES = 0;

	/**
	 * The number of structural features of the '<em>Feature Names</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_NAMES_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '<em>Limited String</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getLimitedString()
	 * @generated
	 */
	int LIMITED_STRING = 8;


	/**
	 * The meta object id for the '<em>URI</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.URI
	 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getURI()
	 * @generated
	 */
	int URI = 9;


	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.ListData <em>List Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>List Data</em>'.
	 * @see org.eclipse.e4.tm.util.ListData
	 * @generated
	 */
	EClass getListData();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.tm.util.ListData#getDataObjects <em>Data Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Data Objects</em>'.
	 * @see org.eclipse.e4.tm.util.ListData#getDataObjects()
	 * @see #getListData()
	 * @generated
	 */
	EAttribute getListData_DataObjects();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.TreeData <em>Tree Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tree Data</em>'.
	 * @see org.eclipse.e4.tm.util.TreeData
	 * @generated
	 */
	EClass getTreeData();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.TreeData#getParentDataObject <em>Parent Data Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parent Data Object</em>'.
	 * @see org.eclipse.e4.tm.util.TreeData#getParentDataObject()
	 * @see #getTreeData()
	 * @generated
	 */
	EAttribute getTreeData_ParentDataObject();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.TreeData#isLeaf <em>Leaf</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Leaf</em>'.
	 * @see org.eclipse.e4.tm.util.TreeData#isLeaf()
	 * @see #getTreeData()
	 * @generated
	 */
	EAttribute getTreeData_Leaf();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.ObjectData <em>Object Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Data</em>'.
	 * @see org.eclipse.e4.tm.util.ObjectData
	 * @generated
	 */
	EClass getObjectData();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.ObjectData#getDataObject <em>Data Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Object</em>'.
	 * @see org.eclipse.e4.tm.util.ObjectData#getDataObject()
	 * @see #getObjectData()
	 * @generated
	 */
	EAttribute getObjectData_DataObject();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.Scripted <em>Scripted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scripted</em>'.
	 * @see org.eclipse.e4.tm.util.Scripted
	 * @generated
	 */
	EClass getScripted();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.Scripted#getScriptSource <em>Script Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Script Source</em>'.
	 * @see org.eclipse.e4.tm.util.Scripted#getScriptSource()
	 * @see #getScripted()
	 * @generated
	 */
	EAttribute getScripted_ScriptSource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.Labeled <em>Labeled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Labeled</em>'.
	 * @see org.eclipse.e4.tm.util.Labeled
	 * @generated
	 */
	EClass getLabeled();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.Labeled#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.e4.tm.util.Labeled#getText()
	 * @see #getLabeled()
	 * @generated
	 */
	EAttribute getLabeled_Text();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.Labeled#getImage <em>Image</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Image</em>'.
	 * @see org.eclipse.e4.tm.util.Labeled#getImage()
	 * @see #getLabeled()
	 * @generated
	 */
	EAttribute getLabeled_Image();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.e4.tm.util.Labeled#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see org.eclipse.e4.tm.util.Labeled#getFormat()
	 * @see #getLabeled()
	 * @generated
	 */
	EAttribute getLabeled_Format();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.FeaturesListData <em>Features List Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features List Data</em>'.
	 * @see org.eclipse.e4.tm.util.FeaturesListData
	 * @generated
	 */
	EClass getFeaturesListData();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.FeaturesLabeled <em>Features Labeled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Features Labeled</em>'.
	 * @see org.eclipse.e4.tm.util.FeaturesLabeled
	 * @generated
	 */
	EClass getFeaturesLabeled();

	/**
	 * Returns the meta object for class '{@link org.eclipse.e4.tm.util.FeatureNames <em>Feature Names</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Names</em>'.
	 * @see org.eclipse.e4.tm.util.FeatureNames
	 * @generated
	 */
	EClass getFeatureNames();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.e4.tm.util.FeatureNames#getFeatureNames <em>Feature Names</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Feature Names</em>'.
	 * @see org.eclipse.e4.tm.util.FeatureNames#getFeatureNames()
	 * @see #getFeatureNames()
	 * @generated
	 */
	EAttribute getFeatureNames_FeatureNames();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Limited String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Limited String</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        annotation="http://www.eclipse.org/e4/tm/util.ecore stringPattern='.+'"
	 * @generated
	 */
	EDataType getLimitedString();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.common.util.URI <em>URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>URI</em>'.
	 * @see org.eclipse.emf.common.util.URI
	 * @model instanceClass="org.eclipse.emf.common.util.URI"
	 * @generated
	 */
	EDataType getURI();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	UtilFactory getUtilFactory();

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
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.ListDataImpl <em>List Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.ListDataImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getListData()
		 * @generated
		 */
		EClass LIST_DATA = eINSTANCE.getListData();
		/**
		 * The meta object literal for the '<em><b>Data Objects</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LIST_DATA__DATA_OBJECTS = eINSTANCE.getListData_DataObjects();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.TreeDataImpl <em>Tree Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.TreeDataImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getTreeData()
		 * @generated
		 */
		EClass TREE_DATA = eINSTANCE.getTreeData();
		/**
		 * The meta object literal for the '<em><b>Parent Data Object</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TREE_DATA__PARENT_DATA_OBJECT = eINSTANCE.getTreeData_ParentDataObject();
		/**
		 * The meta object literal for the '<em><b>Leaf</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TREE_DATA__LEAF = eINSTANCE.getTreeData_Leaf();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.ObjectDataImpl <em>Object Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.ObjectDataImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getObjectData()
		 * @generated
		 */
		EClass OBJECT_DATA = eINSTANCE.getObjectData();
		/**
		 * The meta object literal for the '<em><b>Data Object</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OBJECT_DATA__DATA_OBJECT = eINSTANCE.getObjectData_DataObject();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.ScriptedImpl <em>Scripted</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.ScriptedImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getScripted()
		 * @generated
		 */
		EClass SCRIPTED = eINSTANCE.getScripted();
		/**
		 * The meta object literal for the '<em><b>Script Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCRIPTED__SCRIPT_SOURCE = eINSTANCE.getScripted_ScriptSource();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.LabeledImpl <em>Labeled</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.LabeledImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getLabeled()
		 * @generated
		 */
		EClass LABELED = eINSTANCE.getLabeled();
		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABELED__TEXT = eINSTANCE.getLabeled_Text();
		/**
		 * The meta object literal for the '<em><b>Image</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABELED__IMAGE = eINSTANCE.getLabeled_Image();
		/**
		 * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABELED__FORMAT = eINSTANCE.getLabeled_Format();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.FeaturesListDataImpl <em>Features List Data</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.FeaturesListDataImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeaturesListData()
		 * @generated
		 */
		EClass FEATURES_LIST_DATA = eINSTANCE.getFeaturesListData();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.FeaturesLabeledImpl <em>Features Labeled</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.FeaturesLabeledImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeaturesLabeled()
		 * @generated
		 */
		EClass FEATURES_LABELED = eINSTANCE.getFeaturesLabeled();
		/**
		 * The meta object literal for the '{@link org.eclipse.e4.tm.util.impl.FeatureNamesImpl <em>Feature Names</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.e4.tm.util.impl.FeatureNamesImpl
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getFeatureNames()
		 * @generated
		 */
		EClass FEATURE_NAMES = eINSTANCE.getFeatureNames();
		/**
		 * The meta object literal for the '<em><b>Feature Names</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_NAMES__FEATURE_NAMES = eINSTANCE.getFeatureNames_FeatureNames();
		/**
		 * The meta object literal for the '<em>Limited String</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getLimitedString()
		 * @generated
		 */
		EDataType LIMITED_STRING = eINSTANCE.getLimitedString();
		/**
		 * The meta object literal for the '<em>URI</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.common.util.URI
		 * @see org.eclipse.e4.tm.util.impl.UtilPackageImpl#getURI()
		 * @generated
		 */
		EDataType URI = eINSTANCE.getURI();

	}

} //UtilPackage
