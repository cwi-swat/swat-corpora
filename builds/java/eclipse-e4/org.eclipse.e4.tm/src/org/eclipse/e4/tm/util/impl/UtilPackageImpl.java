/**
 * <copyright>
 * </copyright>
 *
 * $Id: UtilPackageImpl.java,v 1.6 2010/03/18 14:01:52 htraetteb Exp $
 */
package org.eclipse.e4.tm.util.impl;

import org.eclipse.e4.tm.layouts.LayoutsPackage;
import org.eclipse.e4.tm.layouts.impl.LayoutsPackageImpl;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.styles.impl.StylesPackageImpl;
import org.eclipse.e4.tm.util.FeatureNames;
import org.eclipse.e4.tm.util.FeaturesLabeled;
import org.eclipse.e4.tm.util.FeaturesListData;
import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.e4.tm.util.ListData;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.Scripted;
import org.eclipse.e4.tm.util.TreeData;
import org.eclipse.e4.tm.util.UtilFactory;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.e4.tm.widgets.impl.WidgetsPackageImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UtilPackageImpl extends EPackageImpl implements UtilPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass treeDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scriptedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass labeledEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featuresListDataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featuresLabeledEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureNamesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType limitedStringEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uriEDataType = null;

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
	 * @see org.eclipse.e4.tm.util.UtilPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UtilPackageImpl() {
		super(eNS_URI, UtilFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link UtilPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UtilPackage init() {
		if (isInited) return (UtilPackage)EPackage.Registry.INSTANCE.getEPackage(UtilPackage.eNS_URI);

		// Obtain or create and register package
		UtilPackageImpl theUtilPackage = (UtilPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UtilPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UtilPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theUtilPackage.createPackageContents();

		// Initialize created meta-data
		theUtilPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUtilPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(UtilPackage.eNS_URI, theUtilPackage);
		return theUtilPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListData() {
		return listDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getListData_DataObjects() {
		return (EAttribute)listDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTreeData() {
		return treeDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTreeData_ParentDataObject() {
		return (EAttribute)treeDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTreeData_Leaf() {
		return (EAttribute)treeDataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getObjectData() {
		return objectDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getObjectData_DataObject() {
		return (EAttribute)objectDataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScripted() {
		return scriptedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScripted_ScriptSource() {
		return (EAttribute)scriptedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLabeled() {
		return labeledEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLabeled_Text() {
		return (EAttribute)labeledEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLabeled_Image() {
		return (EAttribute)labeledEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLabeled_Format() {
		return (EAttribute)labeledEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeaturesListData() {
		return featuresListDataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeaturesLabeled() {
		return featuresLabeledEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeatureNames() {
		return featureNamesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureNames_FeatureNames() {
		return (EAttribute)featureNamesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLimitedString() {
		return limitedStringEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getURI() {
		return uriEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UtilFactory getUtilFactory() {
		return (UtilFactory)getEFactoryInstance();
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
		listDataEClass = createEClass(LIST_DATA);
		createEAttribute(listDataEClass, LIST_DATA__DATA_OBJECTS);

		treeDataEClass = createEClass(TREE_DATA);
		createEAttribute(treeDataEClass, TREE_DATA__PARENT_DATA_OBJECT);
		createEAttribute(treeDataEClass, TREE_DATA__LEAF);

		objectDataEClass = createEClass(OBJECT_DATA);
		createEAttribute(objectDataEClass, OBJECT_DATA__DATA_OBJECT);

		scriptedEClass = createEClass(SCRIPTED);
		createEAttribute(scriptedEClass, SCRIPTED__SCRIPT_SOURCE);

		labeledEClass = createEClass(LABELED);
		createEAttribute(labeledEClass, LABELED__TEXT);
		createEAttribute(labeledEClass, LABELED__IMAGE);
		createEAttribute(labeledEClass, LABELED__FORMAT);

		featuresListDataEClass = createEClass(FEATURES_LIST_DATA);

		featuresLabeledEClass = createEClass(FEATURES_LABELED);

		featureNamesEClass = createEClass(FEATURE_NAMES);
		createEAttribute(featureNamesEClass, FEATURE_NAMES__FEATURE_NAMES);

		// Create data types
		limitedStringEDataType = createEDataType(LIMITED_STRING);
		uriEDataType = createEDataType(URI);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		listDataEClass.getESuperTypes().add(this.getObjectData());
		treeDataEClass.getESuperTypes().add(this.getListData());
		objectDataEClass.getESuperTypes().add(this.getScripted());
		labeledEClass.getESuperTypes().add(this.getObjectData());
		featuresListDataEClass.getESuperTypes().add(this.getListData());
		featuresListDataEClass.getESuperTypes().add(this.getFeatureNames());
		featuresLabeledEClass.getESuperTypes().add(this.getLabeled());
		featuresLabeledEClass.getESuperTypes().add(this.getFeatureNames());

		// Initialize classes and features; add operations and parameters
		initEClass(listDataEClass, ListData.class, "ListData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getListData_DataObjects(), ecorePackage.getEJavaObject(), "dataObjects", null, 0, -1, ListData.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(treeDataEClass, TreeData.class, "TreeData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTreeData_ParentDataObject(), ecorePackage.getEJavaObject(), "parentDataObject", null, 0, 1, TreeData.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTreeData_Leaf(), ecorePackage.getEBoolean(), "leaf", null, 0, 1, TreeData.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectDataEClass, ObjectData.class, "ObjectData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjectData_DataObject(), ecorePackage.getEJavaObject(), "dataObject", null, 0, 1, ObjectData.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scriptedEClass, Scripted.class, "Scripted", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScripted_ScriptSource(), ecorePackage.getEString(), "scriptSource", null, 0, 1, Scripted.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(labeledEClass, Labeled.class, "Labeled", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLabeled_Text(), ecorePackage.getEString(), "text", null, 0, 1, Labeled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLabeled_Image(), this.getURI(), "image", null, 0, 1, Labeled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLabeled_Format(), ecorePackage.getEString(), "format", null, 0, 1, Labeled.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(featuresListDataEClass, FeaturesListData.class, "FeaturesListData", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(featuresLabeledEClass, FeaturesLabeled.class, "FeaturesLabeled", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(featureNamesEClass, FeatureNames.class, "FeatureNames", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeatureNames_FeatureNames(), ecorePackage.getEString(), "featureNames", null, 0, -1, FeatureNames.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(limitedStringEDataType, String.class, "LimitedString", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(uriEDataType, org.eclipse.emf.common.util.URI.class, "URI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/e4/tm/util.ecore
		createUtilAnnotations();
		// http://www.eclipse.org/e4/emf/ecore/javascript/sourceFeature
		createSourceFeatureAnnotations();
		// http://www.eclipse.org/e4/swt.ecore
		createSwtAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/tm/util.ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createUtilAnnotations() {
		String source = "http://www.eclipse.org/e4/tm/util.ecore";		
		addAnnotation
		  (limitedStringEDataType, 
		   source, 
		   new String[] {
			 "stringPattern", ".+"
		   });				
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/emf/ecore/javascript/sourceFeature</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createSourceFeatureAnnotations() {
		String source = "http://www.eclipse.org/e4/emf/ecore/javascript/sourceFeature";			
		addAnnotation
		  (getScripted_ScriptSource(), 
		   source, 
		   new String[] {
			 "js", "eval, listen"
		   });			
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
		  (getScripted_ScriptSource(), 
		   source, 
		   new String[] {
			 "access", "binder"
		   });		
		addAnnotation
		  (labeledEClass, 
		   source, 
		   new String[] {
			 "access", "property"
		   });		
		addAnnotation
		  (getLabeled_Format(), 
		   source, 
		   new String[] {
			 "invalidates", "text",
			 "access", "binder"
		   });
	}

} //UtilPackageImpl
