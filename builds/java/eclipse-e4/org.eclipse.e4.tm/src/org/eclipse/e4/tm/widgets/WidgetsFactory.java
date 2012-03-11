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
 * $Id: WidgetsFactory.java,v 1.5 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage
 * @generated
 */
public interface WidgetsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	WidgetsFactory eINSTANCE = org.eclipse.e4.tm.widgets.impl.WidgetsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Label</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Label</em>'.
	 * @generated
	 */
	Label createLabel();

	/**
	 * Returns a new object of class '<em>Text</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Text</em>'.
	 * @generated
	 */
	Text createText();

	/**
	 * Returns a new object of class '<em>Single Selection List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Single Selection List</em>'.
	 * @generated
	 */
	SingleSelectionList createSingleSelectionList();

	/**
	 * Returns a new object of class '<em>Combo Box</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Combo Box</em>'.
	 * @generated
	 */
	ComboBox createComboBox();

	/**
	 * Returns a new object of class '<em>Multiple Selection List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Multiple Selection List</em>'.
	 * @generated
	 */
	MultipleSelectionList createMultipleSelectionList();

	/**
	 * Returns a new object of class '<em>Browser</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Browser</em>'.
	 * @generated
	 */
	Browser createBrowser();

	/**
	 * Returns a new object of class '<em>Push Button</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Push Button</em>'.
	 * @generated
	 */
	PushButton createPushButton();

	/**
	 * Returns a new object of class '<em>Check Box</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Check Box</em>'.
	 * @generated
	 */
	CheckBox createCheckBox();

	/**
	 * Returns a new object of class '<em>Toggle Button</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Toggle Button</em>'.
	 * @generated
	 */
	ToggleButton createToggleButton();

	/**
	 * Returns a new object of class '<em>Composite</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Composite</em>'.
	 * @generated
	 */
	Composite createComposite();

	/**
	 * Returns a new object of class '<em>Group Box</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Box</em>'.
	 * @generated
	 */
	GroupBox createGroupBox();

	/**
	 * Returns a new object of class '<em>Tab Folder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tab Folder</em>'.
	 * @generated
	 */
	TabFolder createTabFolder();

	/**
	 * Returns a new object of class '<em>Tab</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tab</em>'.
	 * @generated
	 */
	Tab createTab();

	/**
	 * Returns a new object of class '<em>Shell</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shell</em>'.
	 * @generated
	 */
	Shell createShell();

	/**
	 * Returns a new object of class '<em>Separator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Separator</em>'.
	 * @generated
	 */
	Separator createSeparator();

	/**
	 * Returns a new object of class '<em>List Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>List Viewer</em>'.
	 * @generated
	 */
	ListViewer createListViewer();

	/**
	 * Returns a new object of class '<em>Split Pane</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Split Pane</em>'.
	 * @generated
	 */
	SplitPane createSplitPane();

	/**
	 * Returns a new object of class '<em>Password Field</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Password Field</em>'.
	 * @generated
	 */
	PasswordField createPasswordField();

	/**
	 * Returns a new object of class '<em>Index Selection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Index Selection</em>'.
	 * @generated
	 */
	IndexSelection createIndexSelection();

	/**
	 * Returns a new object of class '<em>Tree Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tree Viewer</em>'.
	 * @generated
	 */
	TreeViewer createTreeViewer();

	/**
	 * Returns a new object of class '<em>Table Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Table Viewer</em>'.
	 * @generated
	 */
	TableViewer createTableViewer();

	/**
	 * Returns a new object of class '<em>Object Selection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object Selection</em>'.
	 * @generated
	 */
	ObjectSelection createObjectSelection();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	WidgetsPackage getWidgetsPackage();

} //WidgetsFactory
