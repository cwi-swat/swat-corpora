/**
 * <copyright>
 * </copyright>
 *
 * $Id: WidgetsPackageImpl.java,v 1.10 2010/03/18 14:01:52 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.layouts.LayoutsPackage;
import org.eclipse.e4.tm.styles.StylesPackage;
import org.eclipse.e4.tm.util.UtilPackage;
import org.eclipse.e4.tm.widgets.AbstractComposite;
import org.eclipse.e4.tm.widgets.AbstractSelection;
import org.eclipse.e4.tm.widgets.BoundedValueControl;
import org.eclipse.e4.tm.widgets.Browser;
import org.eclipse.e4.tm.widgets.Button;
import org.eclipse.e4.tm.widgets.CheckBox;
import org.eclipse.e4.tm.widgets.ComboBox;
import org.eclipse.e4.tm.widgets.Composite;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.GroupBox;
import org.eclipse.e4.tm.widgets.IndexSelection;
import org.eclipse.e4.tm.widgets.Label;
import org.eclipse.e4.tm.widgets.List;
import org.eclipse.e4.tm.widgets.ListViewer;
import org.eclipse.e4.tm.widgets.MultipleSelectionList;
import org.eclipse.e4.tm.widgets.ObjectSelection;
import org.eclipse.e4.tm.widgets.PasswordField;
import org.eclipse.e4.tm.widgets.PushButton;
import org.eclipse.e4.tm.widgets.Separator;
import org.eclipse.e4.tm.widgets.Shell;
import org.eclipse.e4.tm.widgets.SingleSelectionList;
import org.eclipse.e4.tm.widgets.SplitPane;
import org.eclipse.e4.tm.widgets.Tab;
import org.eclipse.e4.tm.widgets.TabFolder;
import org.eclipse.e4.tm.widgets.TableViewer;
import org.eclipse.e4.tm.widgets.Text;
import org.eclipse.e4.tm.widgets.ToggleButton;
import org.eclipse.e4.tm.widgets.TreeViewer;
import org.eclipse.e4.tm.widgets.WidgetsFactory;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.e4.tm.widgets.util.WidgetsValidator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WidgetsPackageImpl extends EPackageImpl implements WidgetsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass controlEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass labelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass textEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass singleSelectionListEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comboBoxEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass multipleSelectionListEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass browserEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buttonEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pushButtonEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass checkBoxEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass toggleButtonEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boundedValueControlEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractCompositeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass compositeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupBoxEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tabFolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tabEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass shellEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass separatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listViewerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass splitPaneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass passwordFieldEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass indexSelectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass treeViewerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tableViewerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectSelectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractSelectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType runtimeEventEDataType = null;

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
	 * @see org.eclipse.e4.tm.widgets.WidgetsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WidgetsPackageImpl() {
		super(eNS_URI, WidgetsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link WidgetsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WidgetsPackage init() {
		if (isInited) return (WidgetsPackage)EPackage.Registry.INSTANCE.getEPackage(WidgetsPackage.eNS_URI);

		// Obtain or create and register package
		WidgetsPackageImpl theWidgetsPackage = (WidgetsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WidgetsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new WidgetsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		LayoutsPackage.eINSTANCE.eClass();
		StylesPackage.eINSTANCE.eClass();
		UtilPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theWidgetsPackage.createPackageContents();

		// Initialize created meta-data
		theWidgetsPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theWidgetsPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return WidgetsValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theWidgetsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WidgetsPackage.eNS_URI, theWidgetsPackage);
		return theWidgetsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getControl() {
		return controlEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getControl_Composite() {
		return (EReference)controlEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getControl_Enabled() {
		return (EAttribute)controlEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getControl_Visible() {
		return (EAttribute)controlEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getControl_LayoutData() {
		return (EReference)controlEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getControl_ToolTip() {
		return (EAttribute)controlEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLabel() {
		return labelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getText() {
		return textEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText_Editable() {
		return (EAttribute)textEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText_Modify() {
		return (EAttribute)textEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText_KeyUp() {
		return (EAttribute)textEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getText_Text() {
		return (EAttribute)textEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getList() {
		return listEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getList_Items() {
		return (EAttribute)listEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSingleSelectionList() {
		return singleSelectionListEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComboBox() {
		return comboBoxEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMultipleSelectionList() {
		return multipleSelectionListEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBrowser() {
		return browserEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBrowser_Location() {
		return (EAttribute)browserEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBrowser_Url() {
		return (EAttribute)browserEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBrowser_Text() {
		return (EAttribute)browserEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getButton() {
		return buttonEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getButton_SelectionEvent() {
		return (EAttribute)buttonEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPushButton() {
		return pushButtonEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCheckBox() {
		return checkBoxEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCheckBox_Selection() {
		return (EAttribute)checkBoxEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getToggleButton() {
		return toggleButtonEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getToggleButton_Selection() {
		return (EAttribute)toggleButtonEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBoundedValueControl() {
		return boundedValueControlEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoundedValueControl_Minimum() {
		return (EAttribute)boundedValueControlEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoundedValueControl_Maximum() {
		return (EAttribute)boundedValueControlEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoundedValueControl_ValueEvent() {
		return (EAttribute)boundedValueControlEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoundedValueControl_Value() {
		return (EAttribute)boundedValueControlEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractComposite() {
		return abstractCompositeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractComposite_Controls() {
		return (EReference)abstractCompositeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractComposite_Styles() {
		return (EReference)abstractCompositeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAbstractComposite_Layout() {
		return (EReference)abstractCompositeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComposite() {
		return compositeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupBox() {
		return groupBoxEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTabFolder() {
		return tabFolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTab() {
		return tabEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getShell() {
		return shellEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSeparator() {
		return separatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListViewer() {
		return listViewerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListViewer_ViewProvider() {
		return (EReference)listViewerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListViewer_ContentProvider() {
		return (EReference)listViewerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSplitPane() {
		return splitPaneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSplitPane_Orientation() {
		return (EAttribute)splitPaneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPasswordField() {
		return passwordFieldEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIndexSelection() {
		return indexSelectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexSelection_SelectionIndex() {
		return (EAttribute)indexSelectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIndexSelection_SelectionIndices() {
		return (EAttribute)indexSelectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTreeViewer() {
		return treeViewerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTreeViewer_ViewProvider() {
		return (EReference)treeViewerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTreeViewer_ContentProvider() {
		return (EReference)treeViewerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTableViewer() {
		return tableViewerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTableViewer_ViewProviders() {
		return (EReference)tableViewerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTableViewer_ContentProvider() {
		return (EReference)tableViewerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getObjectSelection() {
		return objectSelectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getObjectSelection_SelectedObject() {
		return (EAttribute)objectSelectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getObjectSelection_SelectedObjects() {
		return (EAttribute)objectSelectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractSelection() {
		return abstractSelectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAbstractSelection_SelectionEvent() {
		return (EAttribute)abstractSelectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRuntimeEvent() {
		return runtimeEventEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WidgetsFactory getWidgetsFactory() {
		return (WidgetsFactory)getEFactoryInstance();
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
		controlEClass = createEClass(CONTROL);
		createEReference(controlEClass, CONTROL__COMPOSITE);
		createEAttribute(controlEClass, CONTROL__ENABLED);
		createEAttribute(controlEClass, CONTROL__VISIBLE);
		createEReference(controlEClass, CONTROL__LAYOUT_DATA);
		createEAttribute(controlEClass, CONTROL__TOOL_TIP);

		labelEClass = createEClass(LABEL);

		textEClass = createEClass(TEXT);
		createEAttribute(textEClass, TEXT__EDITABLE);
		createEAttribute(textEClass, TEXT__MODIFY);
		createEAttribute(textEClass, TEXT__KEY_UP);
		createEAttribute(textEClass, TEXT__TEXT);

		listEClass = createEClass(LIST);
		createEAttribute(listEClass, LIST__ITEMS);

		singleSelectionListEClass = createEClass(SINGLE_SELECTION_LIST);

		comboBoxEClass = createEClass(COMBO_BOX);

		multipleSelectionListEClass = createEClass(MULTIPLE_SELECTION_LIST);

		browserEClass = createEClass(BROWSER);
		createEAttribute(browserEClass, BROWSER__LOCATION);
		createEAttribute(browserEClass, BROWSER__URL);
		createEAttribute(browserEClass, BROWSER__TEXT);

		buttonEClass = createEClass(BUTTON);
		createEAttribute(buttonEClass, BUTTON__SELECTION_EVENT);

		pushButtonEClass = createEClass(PUSH_BUTTON);

		checkBoxEClass = createEClass(CHECK_BOX);
		createEAttribute(checkBoxEClass, CHECK_BOX__SELECTION);

		toggleButtonEClass = createEClass(TOGGLE_BUTTON);
		createEAttribute(toggleButtonEClass, TOGGLE_BUTTON__SELECTION);

		boundedValueControlEClass = createEClass(BOUNDED_VALUE_CONTROL);
		createEAttribute(boundedValueControlEClass, BOUNDED_VALUE_CONTROL__MINIMUM);
		createEAttribute(boundedValueControlEClass, BOUNDED_VALUE_CONTROL__MAXIMUM);
		createEAttribute(boundedValueControlEClass, BOUNDED_VALUE_CONTROL__VALUE_EVENT);
		createEAttribute(boundedValueControlEClass, BOUNDED_VALUE_CONTROL__VALUE);

		abstractCompositeEClass = createEClass(ABSTRACT_COMPOSITE);
		createEReference(abstractCompositeEClass, ABSTRACT_COMPOSITE__CONTROLS);
		createEReference(abstractCompositeEClass, ABSTRACT_COMPOSITE__STYLES);
		createEReference(abstractCompositeEClass, ABSTRACT_COMPOSITE__LAYOUT);

		compositeEClass = createEClass(COMPOSITE);

		groupBoxEClass = createEClass(GROUP_BOX);

		tabFolderEClass = createEClass(TAB_FOLDER);

		tabEClass = createEClass(TAB);

		shellEClass = createEClass(SHELL);

		separatorEClass = createEClass(SEPARATOR);

		listViewerEClass = createEClass(LIST_VIEWER);
		createEReference(listViewerEClass, LIST_VIEWER__VIEW_PROVIDER);
		createEReference(listViewerEClass, LIST_VIEWER__CONTENT_PROVIDER);

		splitPaneEClass = createEClass(SPLIT_PANE);
		createEAttribute(splitPaneEClass, SPLIT_PANE__ORIENTATION);

		passwordFieldEClass = createEClass(PASSWORD_FIELD);

		indexSelectionEClass = createEClass(INDEX_SELECTION);
		createEAttribute(indexSelectionEClass, INDEX_SELECTION__SELECTION_INDEX);
		createEAttribute(indexSelectionEClass, INDEX_SELECTION__SELECTION_INDICES);

		treeViewerEClass = createEClass(TREE_VIEWER);
		createEReference(treeViewerEClass, TREE_VIEWER__VIEW_PROVIDER);
		createEReference(treeViewerEClass, TREE_VIEWER__CONTENT_PROVIDER);

		tableViewerEClass = createEClass(TABLE_VIEWER);
		createEReference(tableViewerEClass, TABLE_VIEWER__VIEW_PROVIDERS);
		createEReference(tableViewerEClass, TABLE_VIEWER__CONTENT_PROVIDER);

		objectSelectionEClass = createEClass(OBJECT_SELECTION);
		createEAttribute(objectSelectionEClass, OBJECT_SELECTION__SELECTED_OBJECT);
		createEAttribute(objectSelectionEClass, OBJECT_SELECTION__SELECTED_OBJECTS);

		abstractSelectionEClass = createEClass(ABSTRACT_SELECTION);
		createEAttribute(abstractSelectionEClass, ABSTRACT_SELECTION__SELECTION_EVENT);

		// Create data types
		runtimeEventEDataType = createEDataType(RUNTIME_EVENT);
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
		LayoutsPackage theLayoutsPackage = (LayoutsPackage)EPackage.Registry.INSTANCE.getEPackage(LayoutsPackage.eNS_URI);

		// Create type parameters
		ETypeParameter boundedValueControlEClass_T = addETypeParameter(boundedValueControlEClass, "T");
		ETypeParameter abstractCompositeEClass_T = addETypeParameter(abstractCompositeEClass, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getControl());
		abstractCompositeEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		controlEClass.getESuperTypes().add(theUtilPackage.getObjectData());
		controlEClass.getESuperTypes().add(theStylesPackage.getStyled());
		labelEClass.getESuperTypes().add(this.getControl());
		labelEClass.getESuperTypes().add(theUtilPackage.getLabeled());
		textEClass.getESuperTypes().add(this.getControl());
		listEClass.getESuperTypes().add(this.getControl());
		listEClass.getESuperTypes().add(this.getIndexSelection());
		singleSelectionListEClass.getESuperTypes().add(this.getList());
		comboBoxEClass.getESuperTypes().add(this.getList());
		comboBoxEClass.getESuperTypes().add(this.getText());
		multipleSelectionListEClass.getESuperTypes().add(this.getList());
		browserEClass.getESuperTypes().add(this.getControl());
		buttonEClass.getESuperTypes().add(this.getControl());
		buttonEClass.getESuperTypes().add(theUtilPackage.getLabeled());
		pushButtonEClass.getESuperTypes().add(this.getButton());
		checkBoxEClass.getESuperTypes().add(this.getButton());
		toggleButtonEClass.getESuperTypes().add(this.getButton());
		boundedValueControlEClass.getESuperTypes().add(this.getControl());
		abstractCompositeEClass.getESuperTypes().add(this.getControl());
		g1 = createEGenericType(this.getAbstractComposite());
		EGenericType g2 = createEGenericType(this.getControl());
		g1.getETypeArguments().add(g2);
		compositeEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getAbstractComposite());
		g2 = createEGenericType(this.getControl());
		g1.getETypeArguments().add(g2);
		groupBoxEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theUtilPackage.getLabeled());
		groupBoxEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getAbstractComposite());
		g2 = createEGenericType(this.getTab());
		g1.getETypeArguments().add(g2);
		tabFolderEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getIndexSelection());
		tabFolderEClass.getEGenericSuperTypes().add(g1);
		tabEClass.getESuperTypes().add(this.getComposite());
		tabEClass.getESuperTypes().add(theUtilPackage.getLabeled());
		g1 = createEGenericType(this.getAbstractComposite());
		g2 = createEGenericType(this.getControl());
		g1.getETypeArguments().add(g2);
		shellEClass.getEGenericSuperTypes().add(g1);
		separatorEClass.getESuperTypes().add(this.getControl());
		listViewerEClass.getESuperTypes().add(this.getControl());
		listViewerEClass.getESuperTypes().add(this.getIndexSelection());
		g1 = createEGenericType(this.getAbstractComposite());
		g2 = createEGenericType(this.getControl());
		g1.getETypeArguments().add(g2);
		splitPaneEClass.getEGenericSuperTypes().add(g1);
		passwordFieldEClass.getESuperTypes().add(this.getText());
		indexSelectionEClass.getESuperTypes().add(this.getAbstractSelection());
		treeViewerEClass.getESuperTypes().add(this.getControl());
		tableViewerEClass.getESuperTypes().add(this.getControl());
		tableViewerEClass.getESuperTypes().add(this.getIndexSelection());
		objectSelectionEClass.getESuperTypes().add(this.getAbstractSelection());

		// Initialize classes and features; add operations and parameters
		initEClass(controlEClass, Control.class, "Control", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getAbstractComposite());
		g2 = createEGenericType(this.getControl());
		g1.getETypeArguments().add(g2);
		initEReference(getControl_Composite(), g1, this.getAbstractComposite_Controls(), "composite", null, 0, 1, Control.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getControl_Enabled(), ecorePackage.getEBoolean(), "enabled", "true", 0, 1, Control.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getControl_Visible(), ecorePackage.getEBoolean(), "visible", "true", 0, 1, Control.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getControl_LayoutData(), theLayoutsPackage.getLayoutData(), null, "layoutData", null, 0, 1, Control.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getControl_ToolTip(), ecorePackage.getEString(), "toolTip", null, 0, 1, Control.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(labelEClass, Label.class, "Label", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(textEClass, Text.class, "Text", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getText_Editable(), ecorePackage.getEBoolean(), "editable", "true", 0, 1, Text.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getText_Modify(), this.getRuntimeEvent(), "modify", null, 0, 1, Text.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getText_KeyUp(), this.getRuntimeEvent(), "keyUp", null, 0, 1, Text.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getText_Text(), ecorePackage.getEString(), "text", "", 0, 1, Text.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listEClass, List.class, "List", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getList_Items(), ecorePackage.getEString(), "items", null, 0, -1, List.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(singleSelectionListEClass, SingleSelectionList.class, "SingleSelectionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(comboBoxEClass, ComboBox.class, "ComboBox", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(multipleSelectionListEClass, MultipleSelectionList.class, "MultipleSelectionList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(browserEClass, Browser.class, "Browser", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBrowser_Location(), this.getRuntimeEvent(), "location", null, 0, 1, Browser.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBrowser_Url(), theUtilPackage.getLimitedString(), "url", null, 0, 1, Browser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBrowser_Text(), ecorePackage.getEString(), "text", null, 0, 1, Browser.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(buttonEClass, Button.class, "Button", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getButton_SelectionEvent(), this.getRuntimeEvent(), "selectionEvent", null, 0, 1, Button.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pushButtonEClass, PushButton.class, "PushButton", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(checkBoxEClass, CheckBox.class, "CheckBox", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCheckBox_Selection(), ecorePackage.getEBoolean(), "selection", null, 0, 1, CheckBox.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(toggleButtonEClass, ToggleButton.class, "ToggleButton", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getToggleButton_Selection(), ecorePackage.getEBoolean(), "selection", null, 0, 1, ToggleButton.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(boundedValueControlEClass, BoundedValueControl.class, "BoundedValueControl", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(boundedValueControlEClass_T);
		initEAttribute(getBoundedValueControl_Minimum(), g1, "minimum", null, 0, 1, BoundedValueControl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(boundedValueControlEClass_T);
		initEAttribute(getBoundedValueControl_Maximum(), g1, "maximum", null, 0, 1, BoundedValueControl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBoundedValueControl_ValueEvent(), this.getRuntimeEvent(), "valueEvent", null, 0, 1, BoundedValueControl.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(boundedValueControlEClass_T);
		initEAttribute(getBoundedValueControl_Value(), g1, "value", null, 0, 1, BoundedValueControl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractCompositeEClass, AbstractComposite.class, "AbstractComposite", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(abstractCompositeEClass_T);
		initEReference(getAbstractComposite_Controls(), g1, this.getControl_Composite(), "controls", null, 0, -1, AbstractComposite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAbstractComposite_Styles(), theStylesPackage.getStyle(), null, "styles", null, 0, -1, AbstractComposite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(theLayoutsPackage.getLayout());
		g2 = createEGenericType(theLayoutsPackage.getLayoutData());
		g1.getETypeArguments().add(g2);
		initEReference(getAbstractComposite_Layout(), g1, null, "layout", null, 0, 1, AbstractComposite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(compositeEClass, Composite.class, "Composite", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(groupBoxEClass, GroupBox.class, "GroupBox", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(tabFolderEClass, TabFolder.class, "TabFolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(tabEClass, Tab.class, "Tab", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(shellEClass, Shell.class, "Shell", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(separatorEClass, Separator.class, "Separator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(listViewerEClass, ListViewer.class, "ListViewer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getListViewer_ViewProvider(), this.getControl(), null, "viewProvider", null, 0, 1, ListViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getListViewer_ContentProvider(), theUtilPackage.getListData(), null, "contentProvider", null, 0, 1, ListViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(splitPaneEClass, SplitPane.class, "SplitPane", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSplitPane_Orientation(), ecorePackage.getEString(), "orientation", null, 0, 1, SplitPane.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(passwordFieldEClass, PasswordField.class, "PasswordField", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(indexSelectionEClass, IndexSelection.class, "IndexSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIndexSelection_SelectionIndex(), ecorePackage.getEInt(), "selectionIndex", "-1", 0, 1, IndexSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIndexSelection_SelectionIndices(), ecorePackage.getEInt(), "selectionIndices", null, 0, -1, IndexSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(treeViewerEClass, TreeViewer.class, "TreeViewer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTreeViewer_ViewProvider(), this.getControl(), null, "viewProvider", null, 0, 1, TreeViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTreeViewer_ContentProvider(), theUtilPackage.getTreeData(), null, "contentProvider", null, 0, 1, TreeViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tableViewerEClass, TableViewer.class, "TableViewer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTableViewer_ViewProviders(), this.getControl(), null, "viewProviders", null, 0, -1, TableViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTableViewer_ContentProvider(), theUtilPackage.getListData(), null, "contentProvider", null, 0, 1, TableViewer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectSelectionEClass, ObjectSelection.class, "ObjectSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjectSelection_SelectedObject(), ecorePackage.getEJavaObject(), "selectedObject", null, 0, 1, ObjectSelection.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getObjectSelection_SelectedObjects(), ecorePackage.getEJavaObject(), "selectedObjects", null, 0, -1, ObjectSelection.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractSelectionEClass, AbstractSelection.class, "AbstractSelection", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractSelection_SelectionEvent(), this.getRuntimeEvent(), "selectionEvent", null, 0, 1, AbstractSelection.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(runtimeEventEDataType, Object.class, "RuntimeEvent", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/e4/swt.ecore
		createSwtAnnotations();
		// http://www.eclipse.org/e4/swt.ecore#ComboBox
		createSwt_1Annotations();
		// http://www.eclipse.org/emf/2002/Ecore
		createEcoreAnnotations();
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
		  (this, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.widgets"
		   });		
		addAnnotation
		  (controlEClass, 
		   source, 
		   new String[] {
			 "access", "property"
		   });		
		addAnnotation
		  (getControl_ToolTip(), 
		   source, 
		   new String[] {
			 "realName", "toolTipText"
		   });		
		addAnnotation
		  (labelEClass, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.custom",
			 "javaClass", "CLabel"
		   });		
		addAnnotation
		  (textEClass, 
		   source, 
		   new String[] {
			 "javaClass", "Text"
		   });			
		addAnnotation
		  (getText_Modify(), 
		   source, 
		   new String[] {
			 "access", "event",
			 "invalidates", "text",
			 "realName", "Modify"
		   });		
		addAnnotation
		  (getText_KeyUp(), 
		   source, 
		   new String[] {
			 "access", "event"
		   });		
		addAnnotation
		  (getText_Text(), 
		   source, 
		   new String[] {
			 "invalidatedBy", "modify"
		   });		
		addAnnotation
		  (listEClass, 
		   source, 
		   new String[] {
			 "javaClass", "List"
		   });		
		addAnnotation
		  (singleSelectionListEClass, 
		   source, 
		   new String[] {
			 "style", "SINGLE"
		   });		
		addAnnotation
		  (comboBoxEClass, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.custom",
			 "realName", "CCombo"
		   });		
		addAnnotation
		  (multipleSelectionListEClass, 
		   source, 
		   new String[] {
			 "style", "MULTI"
		   });		
		addAnnotation
		  (browserEClass, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.browser"
		   });		
		addAnnotation
		  (getBrowser_Location(), 
		   source, 
		   new String[] {
			 "access", "event",
			 "invalidates", "url text"
		   });		
		addAnnotation
		  (getBrowser_Url(), 
		   source, 
		   new String[] {
			 "access", "property",
			 "invalidatedBy", "location"
		   });		
		addAnnotation
		  (getBrowser_Text(), 
		   source, 
		   new String[] {
			 "access", "property",
			 "invalidatedBy", "location"
		   });		
		addAnnotation
		  (buttonEClass, 
		   source, 
		   new String[] {
			 "javaClass", "Button"
		   });		
		addAnnotation
		  (getButton_SelectionEvent(), 
		   source, 
		   new String[] {
			 "realName", "Selection",
			 "access", "event",
			 "invalidates", "selection"
		   });		
		addAnnotation
		  (pushButtonEClass, 
		   source, 
		   new String[] {
			 "style", "PUSH"
		   });		
		addAnnotation
		  (checkBoxEClass, 
		   source, 
		   new String[] {
			 "style", "CHECK"
		   });		
		addAnnotation
		  (getCheckBox_Selection(), 
		   source, 
		   new String[] {
			 "invalidatedBy", "selectionEvent"
		   });		
		addAnnotation
		  (toggleButtonEClass, 
		   source, 
		   new String[] {
			 "style", "TOGGLE"
		   });		
		addAnnotation
		  (getToggleButton_Selection(), 
		   source, 
		   new String[] {
			 "invalidatedBy", "selectionEvent"
		   });		
		addAnnotation
		  (getBoundedValueControl_ValueEvent(), 
		   source, 
		   new String[] {
			 "realName", "Selection",
			 "access", "event",
			 "invalidates", "value"
		   });		
		addAnnotation
		  (getBoundedValueControl_Value(), 
		   source, 
		   new String[] {
			 "realName", "selection",
			 "invalidatedBy", "selectionEvent"
		   });			
		addAnnotation
		  (getAbstractComposite_Controls(), 
		   source, 
		   new String[] {
			 "access", "binder"
		   });		
		addAnnotation
		  (getAbstractComposite_Styles(), 
		   source, 
		   new String[] {
			 "access", "binder"
		   });		
		addAnnotation
		  (compositeEClass, 
		   source, 
		   new String[] {
			 "javaClass", "Composite"
		   });		
		addAnnotation
		  (groupBoxEClass, 
		   source, 
		   new String[] {
			 "realName", "Group"
		   });		
		addAnnotation
		  (separatorEClass, 
		   source, 
		   new String[] {
			 "realName", "Label",
			 "style", "SEPARATOR"
		   });		
		addAnnotation
		  (listViewerEClass, 
		   source, 
		   new String[] {
		   });		
		addAnnotation
		  (getListViewer_ViewProvider(), 
		   source, 
		   new String[] {
			 "realName", "labelProvider"
		   });		
		addAnnotation
		  (getListViewer_ContentProvider(), 
		   source, 
		   new String[] {
			 "realName", "contentProvider"
		   });		
		addAnnotation
		  (splitPaneEClass, 
		   source, 
		   new String[] {
			 "javaPackage", "org.eclipse.swt.custom",
			 "javaClass", "SashForm"
		   });		
		addAnnotation
		  (getSplitPane_Orientation(), 
		   source, 
		   new String[] {
			 "type", "int"
		   });		
		addAnnotation
		  (passwordFieldEClass, 
		   source, 
		   new String[] {
			 "style", "PASSWORD"
		   });		
		addAnnotation
		  (getIndexSelection_SelectionIndex(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndex setSelection(int)",
			 "invalidatedBy", "selectionEvent"
		   });			
		addAnnotation
		  (getIndexSelection_SelectionIndices(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndices setSelection(int[])",
			 "invalidatedBy", "selection"
		   });			
		addAnnotation
		  (treeViewerEClass, 
		   source, 
		   new String[] {
		   });		
		addAnnotation
		  (tableViewerEClass, 
		   source, 
		   new String[] {
		   });		
		addAnnotation
		  (getTableViewer_ViewProviders(), 
		   source, 
		   new String[] {
			 "realName", "labelProvider"
		   });		
		addAnnotation
		  (getTableViewer_ContentProvider(), 
		   source, 
		   new String[] {
			 "realName", "contentProvider"
		   });		
		addAnnotation
		  (getObjectSelection_SelectedObject(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndex setSelection(int)",
			 "invalidatedBy", "selectionEvent"
		   });			
		addAnnotation
		  (getObjectSelection_SelectedObjects(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndices setSelection(int[])",
			 "invalidatedBy", "selection"
		   });			
		addAnnotation
		  (getAbstractSelection_SelectionEvent(), 
		   source, 
		   new String[] {
			 "realName", "Selection",
			 "access", "event",
			 "invalidates", "selectionIndex selectionIndices"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/e4/swt.ecore#ComboBox</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createSwt_1Annotations() {
		String source = "http://www.eclipse.org/e4/swt.ecore#ComboBox";							
		addAnnotation
		  (getText_Editable(), 
		   source, 
		   new String[] {
			 "access", ""
		   });																																			
		addAnnotation
		  (getIndexSelection_SelectionIndex(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndex select(int)"
		   });			
		addAnnotation
		  (getIndexSelection_SelectionIndices(), 
		   source, 
		   new String[] {
			 "access", ""
		   });							
		addAnnotation
		  (getObjectSelection_SelectedObject(), 
		   source, 
		   new String[] {
			 "access", "getSelectionIndex select(int)"
		   });			
		addAnnotation
		  (getObjectSelection_SelectedObjects(), 
		   source, 
		   new String[] {
			 "access", ""
		   });	
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createEcoreAnnotations() {
		String source = "http://www.eclipse.org/emf/2002/Ecore";																												
		addAnnotation
		  (abstractCompositeEClass, 
		   source, 
		   new String[] {
			 "constraints", "validControls"
		   });																								
	}

} //WidgetsPackageImpl
