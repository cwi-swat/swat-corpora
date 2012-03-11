/**
 * <copyright>
 * </copyright>
 *
 * $Id: WidgetsSwitch.java,v 1.5 2010/03/18 10:23:25 htraetteb Exp $
 */
package org.eclipse.e4.tm.widgets.util;

import java.util.List;

import org.eclipse.e4.tm.styles.Styled;

import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.e4.tm.util.ObjectData;
import org.eclipse.e4.tm.util.Scripted;

import org.eclipse.e4.tm.widgets.*;

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
 * @see org.eclipse.e4.tm.widgets.WidgetsPackage
 * @generated
 */
public class WidgetsSwitch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static WidgetsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WidgetsSwitch() {
		if (modelPackage == null) {
			modelPackage = WidgetsPackage.eINSTANCE;
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
			case WidgetsPackage.CONTROL: {
				Control control = (Control)theEObject;
				T1 result = caseControl(control);
				if (result == null) result = caseObjectData(control);
				if (result == null) result = caseStyled(control);
				if (result == null) result = caseScripted(control);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.LABEL: {
				Label label = (Label)theEObject;
				T1 result = caseLabel(label);
				if (result == null) result = caseControl(label);
				if (result == null) result = caseLabeled(label);
				if (result == null) result = caseObjectData(label);
				if (result == null) result = caseStyled(label);
				if (result == null) result = caseScripted(label);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TEXT: {
				Text text = (Text)theEObject;
				T1 result = caseText(text);
				if (result == null) result = caseControl(text);
				if (result == null) result = caseObjectData(text);
				if (result == null) result = caseStyled(text);
				if (result == null) result = caseScripted(text);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.LIST: {
				org.eclipse.e4.tm.widgets.List list = (org.eclipse.e4.tm.widgets.List)theEObject;
				T1 result = caseList(list);
				if (result == null) result = caseControl(list);
				if (result == null) result = caseIndexSelection(list);
				if (result == null) result = caseObjectData(list);
				if (result == null) result = caseStyled(list);
				if (result == null) result = caseAbstractSelection(list);
				if (result == null) result = caseScripted(list);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.SINGLE_SELECTION_LIST: {
				SingleSelectionList singleSelectionList = (SingleSelectionList)theEObject;
				T1 result = caseSingleSelectionList(singleSelectionList);
				if (result == null) result = caseList(singleSelectionList);
				if (result == null) result = caseControl(singleSelectionList);
				if (result == null) result = caseIndexSelection(singleSelectionList);
				if (result == null) result = caseObjectData(singleSelectionList);
				if (result == null) result = caseStyled(singleSelectionList);
				if (result == null) result = caseAbstractSelection(singleSelectionList);
				if (result == null) result = caseScripted(singleSelectionList);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.COMBO_BOX: {
				ComboBox comboBox = (ComboBox)theEObject;
				T1 result = caseComboBox(comboBox);
				if (result == null) result = caseList(comboBox);
				if (result == null) result = caseText(comboBox);
				if (result == null) result = caseControl(comboBox);
				if (result == null) result = caseIndexSelection(comboBox);
				if (result == null) result = caseObjectData(comboBox);
				if (result == null) result = caseStyled(comboBox);
				if (result == null) result = caseAbstractSelection(comboBox);
				if (result == null) result = caseScripted(comboBox);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.MULTIPLE_SELECTION_LIST: {
				MultipleSelectionList multipleSelectionList = (MultipleSelectionList)theEObject;
				T1 result = caseMultipleSelectionList(multipleSelectionList);
				if (result == null) result = caseList(multipleSelectionList);
				if (result == null) result = caseControl(multipleSelectionList);
				if (result == null) result = caseIndexSelection(multipleSelectionList);
				if (result == null) result = caseObjectData(multipleSelectionList);
				if (result == null) result = caseStyled(multipleSelectionList);
				if (result == null) result = caseAbstractSelection(multipleSelectionList);
				if (result == null) result = caseScripted(multipleSelectionList);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.BROWSER: {
				Browser browser = (Browser)theEObject;
				T1 result = caseBrowser(browser);
				if (result == null) result = caseControl(browser);
				if (result == null) result = caseObjectData(browser);
				if (result == null) result = caseStyled(browser);
				if (result == null) result = caseScripted(browser);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.BUTTON: {
				Button button = (Button)theEObject;
				T1 result = caseButton(button);
				if (result == null) result = caseControl(button);
				if (result == null) result = caseLabeled(button);
				if (result == null) result = caseObjectData(button);
				if (result == null) result = caseStyled(button);
				if (result == null) result = caseScripted(button);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.PUSH_BUTTON: {
				PushButton pushButton = (PushButton)theEObject;
				T1 result = casePushButton(pushButton);
				if (result == null) result = caseButton(pushButton);
				if (result == null) result = caseControl(pushButton);
				if (result == null) result = caseLabeled(pushButton);
				if (result == null) result = caseObjectData(pushButton);
				if (result == null) result = caseStyled(pushButton);
				if (result == null) result = caseScripted(pushButton);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.CHECK_BOX: {
				CheckBox checkBox = (CheckBox)theEObject;
				T1 result = caseCheckBox(checkBox);
				if (result == null) result = caseButton(checkBox);
				if (result == null) result = caseControl(checkBox);
				if (result == null) result = caseLabeled(checkBox);
				if (result == null) result = caseObjectData(checkBox);
				if (result == null) result = caseStyled(checkBox);
				if (result == null) result = caseScripted(checkBox);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TOGGLE_BUTTON: {
				ToggleButton toggleButton = (ToggleButton)theEObject;
				T1 result = caseToggleButton(toggleButton);
				if (result == null) result = caseButton(toggleButton);
				if (result == null) result = caseControl(toggleButton);
				if (result == null) result = caseLabeled(toggleButton);
				if (result == null) result = caseObjectData(toggleButton);
				if (result == null) result = caseStyled(toggleButton);
				if (result == null) result = caseScripted(toggleButton);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.BOUNDED_VALUE_CONTROL: {
				BoundedValueControl<?> boundedValueControl = (BoundedValueControl<?>)theEObject;
				T1 result = caseBoundedValueControl(boundedValueControl);
				if (result == null) result = caseControl(boundedValueControl);
				if (result == null) result = caseObjectData(boundedValueControl);
				if (result == null) result = caseStyled(boundedValueControl);
				if (result == null) result = caseScripted(boundedValueControl);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.ABSTRACT_COMPOSITE: {
				AbstractComposite<?> abstractComposite = (AbstractComposite<?>)theEObject;
				T1 result = caseAbstractComposite(abstractComposite);
				if (result == null) result = caseControl(abstractComposite);
				if (result == null) result = caseObjectData(abstractComposite);
				if (result == null) result = caseStyled(abstractComposite);
				if (result == null) result = caseScripted(abstractComposite);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.COMPOSITE: {
				Composite composite = (Composite)theEObject;
				T1 result = caseComposite(composite);
				if (result == null) result = caseAbstractComposite(composite);
				if (result == null) result = caseControl(composite);
				if (result == null) result = caseObjectData(composite);
				if (result == null) result = caseStyled(composite);
				if (result == null) result = caseScripted(composite);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.GROUP_BOX: {
				GroupBox groupBox = (GroupBox)theEObject;
				T1 result = caseGroupBox(groupBox);
				if (result == null) result = caseAbstractComposite(groupBox);
				if (result == null) result = caseLabeled(groupBox);
				if (result == null) result = caseControl(groupBox);
				if (result == null) result = caseObjectData(groupBox);
				if (result == null) result = caseStyled(groupBox);
				if (result == null) result = caseScripted(groupBox);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TAB_FOLDER: {
				TabFolder tabFolder = (TabFolder)theEObject;
				T1 result = caseTabFolder(tabFolder);
				if (result == null) result = caseAbstractComposite(tabFolder);
				if (result == null) result = caseIndexSelection(tabFolder);
				if (result == null) result = caseControl(tabFolder);
				if (result == null) result = caseAbstractSelection(tabFolder);
				if (result == null) result = caseObjectData(tabFolder);
				if (result == null) result = caseStyled(tabFolder);
				if (result == null) result = caseScripted(tabFolder);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TAB: {
				Tab tab = (Tab)theEObject;
				T1 result = caseTab(tab);
				if (result == null) result = caseComposite(tab);
				if (result == null) result = caseLabeled(tab);
				if (result == null) result = caseAbstractComposite(tab);
				if (result == null) result = caseControl(tab);
				if (result == null) result = caseObjectData(tab);
				if (result == null) result = caseStyled(tab);
				if (result == null) result = caseScripted(tab);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.SHELL: {
				Shell shell = (Shell)theEObject;
				T1 result = caseShell(shell);
				if (result == null) result = caseAbstractComposite(shell);
				if (result == null) result = caseControl(shell);
				if (result == null) result = caseObjectData(shell);
				if (result == null) result = caseStyled(shell);
				if (result == null) result = caseScripted(shell);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.SEPARATOR: {
				Separator separator = (Separator)theEObject;
				T1 result = caseSeparator(separator);
				if (result == null) result = caseControl(separator);
				if (result == null) result = caseObjectData(separator);
				if (result == null) result = caseStyled(separator);
				if (result == null) result = caseScripted(separator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.LIST_VIEWER: {
				ListViewer listViewer = (ListViewer)theEObject;
				T1 result = caseListViewer(listViewer);
				if (result == null) result = caseControl(listViewer);
				if (result == null) result = caseIndexSelection(listViewer);
				if (result == null) result = caseObjectData(listViewer);
				if (result == null) result = caseStyled(listViewer);
				if (result == null) result = caseAbstractSelection(listViewer);
				if (result == null) result = caseScripted(listViewer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.SPLIT_PANE: {
				SplitPane splitPane = (SplitPane)theEObject;
				T1 result = caseSplitPane(splitPane);
				if (result == null) result = caseAbstractComposite(splitPane);
				if (result == null) result = caseControl(splitPane);
				if (result == null) result = caseObjectData(splitPane);
				if (result == null) result = caseStyled(splitPane);
				if (result == null) result = caseScripted(splitPane);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.PASSWORD_FIELD: {
				PasswordField passwordField = (PasswordField)theEObject;
				T1 result = casePasswordField(passwordField);
				if (result == null) result = caseText(passwordField);
				if (result == null) result = caseControl(passwordField);
				if (result == null) result = caseObjectData(passwordField);
				if (result == null) result = caseStyled(passwordField);
				if (result == null) result = caseScripted(passwordField);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.INDEX_SELECTION: {
				IndexSelection indexSelection = (IndexSelection)theEObject;
				T1 result = caseIndexSelection(indexSelection);
				if (result == null) result = caseAbstractSelection(indexSelection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TREE_VIEWER: {
				TreeViewer treeViewer = (TreeViewer)theEObject;
				T1 result = caseTreeViewer(treeViewer);
				if (result == null) result = caseControl(treeViewer);
				if (result == null) result = caseObjectData(treeViewer);
				if (result == null) result = caseStyled(treeViewer);
				if (result == null) result = caseScripted(treeViewer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.TABLE_VIEWER: {
				TableViewer tableViewer = (TableViewer)theEObject;
				T1 result = caseTableViewer(tableViewer);
				if (result == null) result = caseControl(tableViewer);
				if (result == null) result = caseIndexSelection(tableViewer);
				if (result == null) result = caseObjectData(tableViewer);
				if (result == null) result = caseStyled(tableViewer);
				if (result == null) result = caseAbstractSelection(tableViewer);
				if (result == null) result = caseScripted(tableViewer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.OBJECT_SELECTION: {
				ObjectSelection objectSelection = (ObjectSelection)theEObject;
				T1 result = caseObjectSelection(objectSelection);
				if (result == null) result = caseAbstractSelection(objectSelection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case WidgetsPackage.ABSTRACT_SELECTION: {
				AbstractSelection abstractSelection = (AbstractSelection)theEObject;
				T1 result = caseAbstractSelection(abstractSelection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
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
	public T1 caseLabeled(Labeled object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Label</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Label</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseLabel(Label object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseText(Text object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseList(org.eclipse.e4.tm.widgets.List object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Single Selection List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Single Selection List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSingleSelectionList(SingleSelectionList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Combo Box</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Combo Box</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseComboBox(ComboBox object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Multiple Selection List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Multiple Selection List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseMultipleSelectionList(MultipleSelectionList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Browser</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Browser</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBrowser(Browser object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Button</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Button</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseButton(Button object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Push Button</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Push Button</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePushButton(PushButton object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Check Box</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Check Box</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCheckBox(CheckBox object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Toggle Button</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Toggle Button</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseToggleButton(ToggleButton object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bounded Value Control</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bounded Value Control</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> T1 caseBoundedValueControl(BoundedValueControl<T> object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Group Box</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group Box</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseGroupBox(GroupBox object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tab Folder</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tab Folder</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTabFolder(TabFolder object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tab</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tab</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTab(Tab object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Shell</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Shell</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseShell(Shell object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Separator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Separator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSeparator(Separator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List Viewer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseListViewer(ListViewer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Split Pane</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Split Pane</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseSplitPane(SplitPane object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Password Field</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Password Field</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePasswordField(PasswordField object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Index Selection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Index Selection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseIndexSelection(IndexSelection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Tree Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Tree Viewer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTreeViewer(TreeViewer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Table Viewer</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Table Viewer</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTableViewer(TableViewer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Selection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Selection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseObjectSelection(ObjectSelection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Selection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Selection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseAbstractSelection(AbstractSelection object) {
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

} //WidgetsSwitch
