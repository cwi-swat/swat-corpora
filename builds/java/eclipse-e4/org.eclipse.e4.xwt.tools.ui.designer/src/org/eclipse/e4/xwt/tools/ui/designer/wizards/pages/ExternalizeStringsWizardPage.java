/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.wizards.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.AccessorConfigurationDialog;
import org.eclipse.e4.xwt.tools.ui.designer.providers.ExternalizeStringContentProvider;
import org.eclipse.e4.xwt.tools.ui.designer.providers.ExternalizeStringLabelProvider;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.AccessorConfigureInfo;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.ExternalizeStringsCommon;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.TableCellModifier;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.TableViewerListener;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class ExternalizeStringsWizardPage extends WizardPage {

	private TextValueModel checkedItems;

	private TextValueModel textValueEntrys;

	private TableViewer tableViewer;

	private Text commonPrefixText;

	private boolean isChecked = false;

	private int checkCount = 0;

	public static final String CHECK_COLUMN_ID = "";

	public static final String VALUE_COLUMN_ID = "Value";

	public static final String KEY_COLUMN_ID = "Key";

	private String defaultFolder;

	private String defaultPackage;

	private IPackageFragmentRoot defaultRoot;

	private AccessorConfigureInfo info = new AccessorConfigureInfo();

	private Combo combo;

	private ArrayList<String> duplicateKeys;

	// create a PropertyFileListener instance.
	private final TableViewerListener tableViewerListener = new TableViewerListener() {

		public void valueChanged(TextValueEntry textValueEntry) {
			tableViewer.refresh(textValueEntry, true);
			changeContents();
		}

	};

	/**
	 * Constructor for ExternalizeStringsWizardPage.
	 * 
	 * @param selection
	 * @param textValueEntrys
	 */
	public ExternalizeStringsWizardPage(ISelection selection, TextValueModel textValueEntrys) {
		super("wizardPage");
		setTitle("Externalize Strings");
		setDescription("Externalizes strings from code into a properties file. The auto-generated keys should be adjusted.");
		this.textValueEntrys = textValueEntrys;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		// Create filed for user to change the prefix of the key
		Composite childcontainer = new Composite(container, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		childcontainer.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		childcontainer.setLayoutData(gridData);

		Label commonPrefixLabel = new Label(childcontainer, SWT.NONE);
		commonPrefixLabel.setText("Enter common prefix for generated keys(optional):");

		commonPrefixText = new Text(childcontainer, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		commonPrefixText.setText("Text_");
		commonPrefixText.setLayoutData(gridData);
		commonPrefixText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				tableViewer.setLabelProvider(new ExternalizeStringLabelProvider(commonPrefixText.getText()));
				if (checkCount > 0) {
					checkPrefix();
				}
				changeContents();
			}
		});
		setPageComplete(false);

		// Create the tableViewer of the text value which have not been externalized or been binded.
		createWidgetObserve(container);

		// Create select all button.
		createSelectAllButton(container);

		// Create the the configure field.
		createAccessorConfigure(container);

		setControl(container);

		// Initial the configure files information.
		String defaultFilePath = "/" + defaultFolder + "/" + defaultPackage.replace(".", "/");

		info.setClassFilePath(defaultFilePath);
		info.setClassRoot(defaultRoot);
		info.setSourceFolder(defaultFolder);
		info.setSourcePackage(defaultPackage);
		info.setClassName("Messages");

		info.setPropertyFilePath(defaultFilePath);
		info.setPropertyRoot(defaultRoot);
		info.setPropertyFolder(defaultFolder);
		info.setPropertyPackage(defaultPackage);
		info.setPropertyName("messages");
	}

	/**
	 * Create select all button
	 */
	private void createSelectAllButton(Composite container) {
		Button button = new Button(container, SWT.PUSH);
		button.setText("Select All");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		button.setLayoutData(gridData);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int length = tableViewer.getTable().getItems().length;
				if (checkCount < length) {
					for (int i = 0; i < length; i++) {
						tableViewer.getTable().getItem(i).setChecked(true);
						tableViewer.getTable().getItem(i).setBackground(new Color(Display.getDefault(), 226, 226, 226));
					}
					checkCount = length;
				} else {
					for (int i = 0; i < length; i++) {
						tableViewer.getTable().getItem(i).setChecked(false);
						tableViewer.getTable().getItem(i).setBackground(ColorConstants.listBackground);
					}
					checkCount = 0;
				}
				changeContents();
				updatePageComplete(checkCount);
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void createAccessorConfigure(Composite container) {
		combo = new Combo(container, SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = convertWidthInCharsToPixels(30);
		combo.setLayoutData(gridData);
		combo.setItems(new String[] { defaultPackage + ".Messages.java - " + defaultPackage + ".messages.properties" });
		combo.select(0);

		Button configureButton = new Button(container, SWT.PUSH);
		configureButton.setText("Configure...");
		gridData = new GridData();
		configureButton.setLayoutData(gridData);
		configureButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				AccessorConfigurationDialog dialog = new AccessorConfigurationDialog(getShell(), info);
				dialog.setTitle("Configure Accessor Class");

				if (dialog.open() == Window.OK) {
					// Get the information of configure files.
					info.setClassFilePath(dialog.getClassFilePath());
					info.setClassRoot(dialog.getclassRoot());
					info.setSourceFolder(dialog.getSourceFolder());
					info.setSourcePackage(dialog.getSourcePackage());
					info.setClassName(dialog.getClassName());

					info.setPropertyFilePath(dialog.getPropertyFilePath());
					info.setPropertyRoot(dialog.getPropertyRoot());
					info.setPropertyFolder(dialog.getPropertyFolder());
					info.setPropertyPackage(dialog.getPropertyPackage());
					info.setPropertyName(dialog.getPropertyName());
					combo.setItems(new String[] { dialog.getSourcePackage() + ((dialog.getSourcePackage() == null || "".equals(dialog.getSourcePackage())) ? "" : ".") + dialog.getClassName() + ".java - " + dialog.getPropertyPackage() + "." + dialog.getPropertyName() + ".properties" });
					combo.select(0);
					changeContents();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	/**
	 * Create the widget's text values observe container
	 * 
	 * @param container
	 */
	private void createWidgetObserve(Composite container) {
		Composite viewContainer = new Composite(container, SWT.BORDER);
		GridLayout layout = new GridLayout();
		viewContainer.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;

		viewContainer.setLayoutData(gridData);

		tableViewer = new TableViewer(viewContainer, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		gridData.heightHint = table.getItemHeight() * 11;
		table.setLayoutData(gridData);
		// Create the header of the table
		String[] colNames = { "", "Value", "Key" };
		int[] colWidths = { 50, 300, 300 };
		for (int i = 0; i < colNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText(colNames[i]);
			tableColumn.setWidth(colWidths[i]);
		}
		tableViewer.setLabelProvider(new ExternalizeStringLabelProvider(commonPrefixText.getText()));
		tableViewer.setContentProvider(new ExternalizeStringContentProvider());
		tableViewer.setInput(textValueEntrys);
		tableViewer.getTable().getDisplay().asyncExec(new Runnable() {
			public void run() {
				TextValueModel textValueModel = (TextValueModel) tableViewer.getInput();
				textValueModel.addPropertyChangeListener(tableViewerListener);
			}
		});
		initTableEditor();
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				changeContents();
				updatePageComplete(checkCount);
			}
		});
	}

	/**
	 * make cell of the tableViewer editable.
	 */
	private void initTableEditor() {
		tableViewer.setColumnProperties(new String[] { CHECK_COLUMN_ID, VALUE_COLUMN_ID, KEY_COLUMN_ID });
		final TextCellEditor valueEditor = new TextCellEditor(tableViewer.getTable());
		final TextCellEditor keyEditor = new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(new CellEditor[] { null, valueEditor, keyEditor });
		tableViewer.setCellModifier(new TableCellModifier());
	}

	public TextValueModel getCheckedItems() {
		return checkedItems;
	}

	private void updatePageComplete(int checkNum) {
		isChecked = false;
		if (checkNum > 0) {
			isChecked = true;
			if (getErrorMessage() != null) {
				setPageComplete(false);
			} else {
				setPageComplete(true);
			}
		} else {
			updateStatus(null);
		}

	}

	/**
	 * check the prefix string format.
	 */
	private void checkPrefix() {
		String checkValue = commonPrefixText.getText();
		if (!"".equals(checkValue)) {
			String regex = "^([_|a-zA-Z])[_\\w]*$";
			checkFormat(checkValue, regex);
		}
	}

	/**
	 * check the prefix string format.
	 */
	private boolean checkKeyValue(String keyValue) {
		String regex = "^([_|a-zA-Z])[_\\w]*$";
		return checkFormat(keyValue, regex);
	}

	/**
	 * check string format
	 * 
	 * @param value
	 */
	private boolean checkFormat(String value, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(value);
		if (!(matcher.find())) {
			updateStatus("Some selected keys are not valid Java identifiers.");
			return false;
		} else {
			updateStatus(null);
			return true;
		}
	}

	/**
	 * Update the page's status.
	 * 
	 * @param message
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete((message == null) && isChecked);
	}

	/**
	 * Check the change nameMap and get the selected values.
	 */
	private void changeContents() {
		int count = tableViewer.getTable().getItemCount();
		String checkValue = "";
		checkedItems = new TextValueModel();
		for (int i = 0; i < count; i++) {
			if (tableViewer.getTable().getItem(i).getChecked()) {
				tableViewer.getTable().getItem(i).setBackground(new Color(Display.getDefault(), 226, 226, 226));
				if (tableViewer.getElementAt(i) instanceof TextValueEntry) {
					String value = ((TextValueEntry) (tableViewer.getElementAt(i))).getValue();
					String key = commonPrefixText.getText() + ((TextValueEntry) (tableViewer.getElementAt(i))).getKey();
					checkedItems.add(new TextValueEntry(value, key));
				}
				checkValue = commonPrefixText.getText() + ((TextValueEntry) (tableViewer.getElementAt(i))).getKey();
				if (!checkKeyValue(checkValue)) {
					break;
				}
			} else {
				tableViewer.getTable().getItem(i).setBackground(ColorConstants.listBackground);
			}
		}
		checkSameValue();
		checkDuplicate(info.getClassFilePath(), info.getClassName() + ".java", info.getPropertyFilePath(), info.getPropertyName() + ".properties");
		checkCount = getCheckNum();
	}

	/**
	 * Get the checked number of tableViewer's items.
	 * 
	 * @return checkNum
	 */
	private int getCheckNum() {
		int checkNum = 0;
		for (int i = 0; i < tableViewer.getTable().getItems().length; i++) {
			if (tableViewer.getTable().getItem(i).getChecked()) {
				checkNum++;
			}
		}
		return checkNum;
	}

	/**
	 * check the selected keys whether have a same key.
	 */
	private void checkSameValue() {
		Object selectedKeys[] = checkedItems.elements();
		String key;
		String value;
		String compareKey;
		String comparevalue;
		String errorMsg = "Conflicting entries: Same key but different values";
		for (int i = 0; i < selectedKeys.length; i++) {
			if (selectedKeys[i] instanceof TextValueEntry) {
				key = ((TextValueEntry) selectedKeys[i]).getKey();
				value = ((TextValueEntry) selectedKeys[i]).getValue();
				for (int j = i + 1; j < selectedKeys.length; j++) {
					compareKey = ((TextValueEntry) selectedKeys[j]).getKey();
					comparevalue = ((TextValueEntry) selectedKeys[j]).getValue();
					if (compareKey.equals(key) && !comparevalue.equals(value)) {
						updateStatus(errorMsg);
						break;
					}
				}
			}
		}
	}

	/**
	 * Check whether have the same key values in the File.
	 * 
	 * @param sourceFolderName
	 * @param className
	 * @param propertyFolderName
	 * @param propertyName
	 * @throws CoreException
	 * @throws IOException
	 * @throws returnWizardPage
	 */
	private void checkDuplicate(String sourceFolderName, String className, String propertyFolderName, String propertyName) {
		try {
			duplicateKeys = new ArrayList<String>();
			if (ExternalizeStringsCommon.getIFile(sourceFolderName, className).exists()) {
				StringBuffer classHistoryContents = ExternalizeStringsCommon.getHistoryContents((ExternalizeStringsCommon.getIFile(sourceFolderName, className)));
				checkDuplicateKey(checkedItems, classHistoryContents, true);
			}
			if (ExternalizeStringsCommon.getIFile(propertyFolderName, propertyName).exists()) {
				StringBuffer PropertyHistoryContents = ExternalizeStringsCommon.getHistoryContents(ExternalizeStringsCommon.getIFile(propertyFolderName, propertyName));
				checkDuplicateKey(checkedItems, PropertyHistoryContents, false);
			}

			if (duplicateKeys.size() > 0) {
				StringBuffer dialogMessage = new StringBuffer("Key: ");
				ArrayList<String> keys = new ArrayList<String>();
				for (int i = 0; i < duplicateKeys.size(); i++) {
					if (keys.indexOf(duplicateKeys.get(i)) == -1) {
						keys.add(duplicateKeys.get(i));
						dialogMessage.append("\"" + duplicateKeys.get(i)
								+ "\" ");
					}
				}
				dialogMessage.append("already exist in files. Please rename.");
				updateStatus(dialogMessage.toString());
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * To match the duplicate key values in the File.
	 * 
	 * @param checkedItems
	 * @param historyContent
	 */
	private void checkDuplicateKey(TextValueModel checkedItems, StringBuffer historyContent, boolean isClassFile) {
		String regexSuffix;
		String regexPrefix;
		if (isClassFile) {
			regexPrefix = "[\t ]+";
			regexSuffix = "([\t ]*[\r ]*[\t ]*)*[;]";
		} else {
			regexPrefix = "[\t ]*";
			regexSuffix = "[\t ]*[=]";
		}
		for (int i = 0; i < checkedItems.elements().length; i++) {
			String key = ((TextValueEntry) checkedItems.elements()[i]).getKey();
			if (!checkKeyValue(key))
				return;
			String regex = regexPrefix + key + regexSuffix;

			Matcher matcher = Pattern.compile(regex).matcher(historyContent.toString());
			if (matcher.find()) {
				duplicateKeys.add(key);
			}
		}
	}

	public void setDefaultFolder(String defaultFolder) {
		this.defaultFolder = defaultFolder;
	}

	public void setDefaultRoot(IPackageFragmentRoot defaultRoot) {
		this.defaultRoot = defaultRoot;
	}

	public void setDefaultPackage(String defaultPackage) {
		this.defaultPackage = defaultPackage;
	}

	public AccessorConfigureInfo getInfo() {
		return info;
	}
}
