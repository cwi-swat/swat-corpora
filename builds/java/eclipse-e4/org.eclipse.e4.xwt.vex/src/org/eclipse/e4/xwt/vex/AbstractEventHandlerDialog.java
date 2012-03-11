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
package org.eclipse.e4.xwt.vex;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.w3c.dom.Node;

public abstract class AbstractEventHandlerDialog {
	public enum Operation {
		Cancel, New, Select, Rename;
	};

	protected Operation operation = Operation.Cancel;
	protected String inputHandler = "";
	protected String oldHandler;
	protected String attrValue;
	protected String[] handlers;
	protected StructuredTextViewer textViewer;
	protected int count;

	public AbstractEventHandlerDialog(StructuredTextViewer textViewer, String oldHandler, String attrValue, String[] handlers) {
		this.textViewer = textViewer;
		this.oldHandler = oldHandler;
		this.attrValue = attrValue;
		this.handlers = handlers;

	}

	public void run(Shell parentShell, String title, Point position) {
		Shell shell = new Shell(parentShell, SWT.NONE);
		shell.setText(title);
		createContents(shell);
		shell.setLocation(position);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));
		Group group = new Group(shell, SWT.LEFT);
		group.setText(EditorMessages.EventHandleDialog_Operation);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		GridLayout gridLayout = new GridLayout();
		group.setLayout(gridLayout);

		final Button rbt1 = new Button(group, SWT.RADIO);
		rbt1.setText(EditorMessages.EventHandleDialog_New_Handler);
		rbt1.setSelection(true);
		GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
		gridData1.horizontalSpan = 2;
		rbt1.setLayoutData(gridData1);
		RBSelectionAdapter rbSelectionAdapter1 = new RBSelectionAdapter(1);
		rbt1.addSelectionListener(rbSelectionAdapter1);
		rbt1.setToolTipText(EditorMessages.EventHandleDialog_New_Handler_ToolTip);
		rbt1.setForeground(rbt1.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));

		final Button rbt2 = new Button(group, SWT.RADIO);
		rbt2.setText(EditorMessages.EventHandleDialog_Rename_Handler);
		GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.horizontalSpan = 2;
		rbt2.setLayoutData(gridData2);
		RBSelectionAdapter rbSelectionAdapter2 = new RBSelectionAdapter(2);
		rbt2.addSelectionListener(rbSelectionAdapter2);
		rbt2.setToolTipText(EditorMessages.EventHandleDialog_Rename_Handler_ToolTip);
		rbt2.setForeground(rbt2.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
		rbt2.setEnabled(oldHandler != null && oldHandler.length() > 0);

		final Button rbt3 = new Button(group, SWT.RADIO);
		rbt3.setText(EditorMessages.EventHandleDialog_Select_Existing_Handler);
		GridData gridData3 = new GridData(GridData.FILL_HORIZONTAL);
		gridData3.horizontalSpan = 2;
		rbt3.setLayoutData(gridData3);
		RBSelectionAdapter rbSelectionAdapter3 = new RBSelectionAdapter(3);
		rbt3.addSelectionListener(rbSelectionAdapter3);
		rbt3.setToolTipText(EditorMessages.EventHandleDialog_Select_Existing_Handler_ToolTip);
		rbt3.setForeground(rbt3.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
		rbt3.setEnabled(handlers != null && handlers.length > 0);

		Label hNameLabel = new Label(shell, SWT.LEFT);
		hNameLabel.setText(EditorMessages.EventHandleDialog_Input_Handler_Name);
		final Text nameText = new Text(shell, SWT.BORDER);
		nameText.setText(attrValue);
		GridData gridData4 = new GridData();
		gridData4.widthHint = 100;
		nameText.setLayoutData(gridData4);
		rbSelectionAdapter1.setText(nameText);
		rbSelectionAdapter2.setText(nameText);
		rbSelectionAdapter3.setText(nameText);

		Label selectHandlerLabel = new Label(shell, SWT.LEFT);
		selectHandlerLabel.setText(EditorMessages.EventHandleDialog_Select_Handler);
		GridData gridData5 = new GridData(GridData.FILL_HORIZONTAL);
		selectHandlerLabel.setLayoutData(gridData5);
		final Combo handlersCombo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		if (handlers != null && handlers.length != 0) {
			handlersCombo.setItems(handlers);
			handlersCombo.setText(handlers[0]);
		}
		handlersCombo.setEnabled(false);
		GridData gridData6 = new GridData(GridData.FILL_HORIZONTAL);
		gridData6.horizontalAlignment = SWT.LEFT;
		handlersCombo.setLayoutData(gridData6);
		rbSelectionAdapter1.setCombo(handlersCombo);
		rbSelectionAdapter2.setCombo(handlersCombo);
		rbSelectionAdapter3.setCombo(handlersCombo);

		Button okButton = new Button(shell, SWT.PUSH | SWT.CR);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean exists = false;
				if (!rbt3.getSelection()) {
					if (nameText.getText().trim().equals("")) {
						MessageDialog.openInformation(shell, "Dialog", EditorMessages.EventHandleDialog_Error_Handler_Name_Null);
						nameText.forceFocus();
						return;
					}

					exists = handlerExists(nameText.getText(), handlers);
					if (exists) {
						int length = nameText.getText().length();
						nameText.setSelection(0, length);
						nameText.forceFocus();
						return;
					}
				}
				if (rbt1.getSelection()) {
					operation = Operation.New;
					inputHandler = nameText.getText();
				} else if (rbt2.getSelection()) {
					inputHandler = nameText.getText();
					Node rootNode = (Node) ContentAssistUtils.getNodeAt(textViewer, 0);
					count = 0;
					boolean usedByOther = usedByOther(rootNode, oldHandler);
					if (usedByOther) {
						String message = NLS.bind(EditorMessages.EventHandleDialog_Question_Handler_Name_Refactoring, oldHandler);
						boolean confirm = MessageDialog.openConfirm(shell, "Dialog", message);
						if (!confirm) {
							int length = nameText.getText().length();
							nameText.setSelection(0, length);
							nameText.forceFocus();
							return;
						}
					}
					operation = Operation.Rename;
				} else if (rbt3.getSelection()) {
					operation = Operation.Select;
					inputHandler = handlersCombo.getText();
				}

				if (exists == false) {
					shell.dispose();
				}
			}

			private boolean handlerExists(String handlerName, String[] handlers) {
				for (int i = 0; i < handlers.length; i++) {
					if (handlerName != null && handlerName.equals(handlers[i])) {
						MessageDialog.openInformation(shell, "Dialog", EditorMessages.EventHandleDialog_Error_Handler_Name_Exists);
						return true;
					}
				}
				return false;
			}
		});
		okButton.setText("OK");
		GridData okData = new GridData();
		okData.horizontalAlignment = GridData.CENTER;
		okButton.setLayoutData(okData);

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelButton.setText("Cancel");
		GridData cancelData = new GridData();
		cancelData.horizontalAlignment = GridData.CENTER;
		cancelData.grabExcessHorizontalSpace = true;
		shell.setDefaultButton(okButton);
	}

	public Operation getOperation() {
		return operation;
	}

	public String getInputHandler() {
		return inputHandler;
	}

	private class RBSelectionAdapter extends SelectionAdapter {
		private Text text;
		private Combo combo;
		private int number;

		protected RBSelectionAdapter(int number) {
			this.number = number;
		}

		public void widgetSelected(SelectionEvent e) {

			if (number == 3) {
				text.setEnabled(false);
				combo.setEnabled(true);
			} else {
				text.setEnabled(true);
				combo.setEnabled(false);
			}
		}

		protected void setText(Text text) {
			this.text = text;
		}

		protected void setCombo(Combo combo) {
			this.combo = combo;
		}
	}

	protected abstract boolean usedByOther(Node node, String oldHandler);
}