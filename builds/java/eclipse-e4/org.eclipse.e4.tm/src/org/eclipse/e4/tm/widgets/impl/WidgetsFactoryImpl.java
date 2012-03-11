/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: WidgetsFactoryImpl.java,v 1.6 2010/03/18 10:23:24 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.impl;

import org.eclipse.e4.tm.widgets.*;
import org.eclipse.e4.tm.widgets.Browser;
import org.eclipse.e4.tm.widgets.CheckBox;
import org.eclipse.e4.tm.widgets.ComboBox;
import org.eclipse.e4.tm.widgets.Composite;
import org.eclipse.e4.tm.widgets.GroupBox;
import org.eclipse.e4.tm.widgets.IndexSelection;
import org.eclipse.e4.tm.widgets.Label;
import org.eclipse.e4.tm.widgets.ListViewer;
import org.eclipse.e4.tm.widgets.MultipleSelectionList;
import org.eclipse.e4.tm.widgets.PasswordField;
import org.eclipse.e4.tm.widgets.PushButton;
import org.eclipse.e4.tm.widgets.Separator;
import org.eclipse.e4.tm.widgets.Shell;
import org.eclipse.e4.tm.widgets.SingleSelectionList;
import org.eclipse.e4.tm.widgets.SplitPane;
import org.eclipse.e4.tm.widgets.Tab;
import org.eclipse.e4.tm.widgets.TabFolder;
import org.eclipse.e4.tm.widgets.Text;
import org.eclipse.e4.tm.widgets.ToggleButton;
import org.eclipse.e4.tm.widgets.TreeViewer;
import org.eclipse.e4.tm.widgets.WidgetsFactory;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WidgetsFactoryImpl extends EFactoryImpl implements WidgetsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WidgetsFactory init() {
		try {
			WidgetsFactory theWidgetsFactory = (WidgetsFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/plugin/org.eclipse.e4.tm/model/tm/widgets.ecore"); 
			if (theWidgetsFactory != null) {
				return theWidgetsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new WidgetsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WidgetsFactoryImpl() {
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
			case WidgetsPackage.LABEL: return createLabel();
			case WidgetsPackage.TEXT: return createText();
			case WidgetsPackage.SINGLE_SELECTION_LIST: return createSingleSelectionList();
			case WidgetsPackage.COMBO_BOX: return createComboBox();
			case WidgetsPackage.MULTIPLE_SELECTION_LIST: return createMultipleSelectionList();
			case WidgetsPackage.BROWSER: return createBrowser();
			case WidgetsPackage.PUSH_BUTTON: return createPushButton();
			case WidgetsPackage.CHECK_BOX: return createCheckBox();
			case WidgetsPackage.TOGGLE_BUTTON: return createToggleButton();
			case WidgetsPackage.COMPOSITE: return createComposite();
			case WidgetsPackage.GROUP_BOX: return createGroupBox();
			case WidgetsPackage.TAB_FOLDER: return createTabFolder();
			case WidgetsPackage.TAB: return createTab();
			case WidgetsPackage.SHELL: return createShell();
			case WidgetsPackage.SEPARATOR: return createSeparator();
			case WidgetsPackage.LIST_VIEWER: return createListViewer();
			case WidgetsPackage.SPLIT_PANE: return createSplitPane();
			case WidgetsPackage.PASSWORD_FIELD: return createPasswordField();
			case WidgetsPackage.INDEX_SELECTION: return createIndexSelection();
			case WidgetsPackage.TREE_VIEWER: return createTreeViewer();
			case WidgetsPackage.TABLE_VIEWER: return createTableViewer();
			case WidgetsPackage.OBJECT_SELECTION: return createObjectSelection();
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
			case WidgetsPackage.RUNTIME_EVENT:
				return createRuntimeEventFromString(eDataType, initialValue);
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
			case WidgetsPackage.RUNTIME_EVENT:
				return convertRuntimeEventToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Label createLabel() {
		LabelImpl label = new LabelImpl();
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Text createText() {
		TextImpl text = new TextImpl();
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SingleSelectionList createSingleSelectionList() {
		SingleSelectionListImpl singleSelectionList = new SingleSelectionListImpl();
		return singleSelectionList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComboBox createComboBox() {
		ComboBoxImpl comboBox = new ComboBoxImpl();
		return comboBox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MultipleSelectionList createMultipleSelectionList() {
		MultipleSelectionListImpl multipleSelectionList = new MultipleSelectionListImpl();
		return multipleSelectionList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Browser createBrowser() {
		BrowserImpl browser = new BrowserImpl();
		return browser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PushButton createPushButton() {
		PushButtonImpl pushButton = new PushButtonImpl();
		return pushButton;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CheckBox createCheckBox() {
		CheckBoxImpl checkBox = new CheckBoxImpl();
		return checkBox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ToggleButton createToggleButton() {
		ToggleButtonImpl toggleButton = new ToggleButtonImpl();
		return toggleButton;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Composite createComposite() {
		CompositeImpl composite = new CompositeImpl();
		return composite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupBox createGroupBox() {
		GroupBoxImpl groupBox = new GroupBoxImpl();
		return groupBox;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TabFolder createTabFolder() {
		TabFolderImpl tabFolder = new TabFolderImpl();
		return tabFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Tab createTab() {
		TabImpl tab = new TabImpl();
		return tab;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Shell createShell() {
		ShellImpl shell = new ShellImpl();
		return shell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Separator createSeparator() {
		SeparatorImpl separator = new SeparatorImpl();
		return separator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListViewer createListViewer() {
		ListViewerImpl listViewer = new ListViewerImpl();
		return listViewer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SplitPane createSplitPane() {
		SplitPaneImpl splitPane = new SplitPaneImpl();
		return splitPane;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PasswordField createPasswordField() {
		PasswordFieldImpl passwordField = new PasswordFieldImpl();
		return passwordField;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IndexSelection createIndexSelection() {
		IndexSelectionImpl indexSelection = new IndexSelectionImpl();
		return indexSelection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeViewer createTreeViewer() {
		TreeViewerImpl treeViewer = new TreeViewerImpl();
		return treeViewer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TableViewer createTableViewer() {
		TableViewerImpl tableViewer = new TableViewerImpl();
		return tableViewer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ObjectSelection createObjectSelection() {
		ObjectSelectionImpl objectSelection = new ObjectSelectionImpl();
		return objectSelection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createRuntimeEventFromString(EDataType eDataType, String initialValue) {
		return super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRuntimeEventToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WidgetsPackage getWidgetsPackage() {
		return (WidgetsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static WidgetsPackage getPackage() {
		return WidgetsPackage.eINSTANCE;
	}

} //WidgetsFactoryImpl
