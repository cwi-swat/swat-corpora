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
package org.eclipse.e4.xwt.tools.ui.designer.core.properties.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ArrayCellEditor extends AbstractCellEditor {

	public ArrayCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.properties.editors.AbstractCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		Object value = getValue();
		String[] initValue = null;
		if (value instanceof String[]) {
			initValue = (String[]) value;
		}
		ArrayDialog dialog = new ArrayDialog(cellEditorWindow.getShell(), initValue);
		if (dialog.open() == Window.OK) {
			initValue = dialog.getArray();
		}
		return initValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.properties.editors.AbstractCellEditor#updateContents(java.lang.Object)
	 */
	protected void updateContents(Object value) {
		super.updateContents("[...]");
	}

	static class ArrayDialog extends Dialog {
		private Text text;
		private String result;
		private String[] initValue;

		protected ArrayDialog(Shell parentShell, String[] initValue) {
			super(parentShell);
			setShellStyle(SWT.SHELL_TRIM);
			this.initValue = initValue;
		}

		protected Control createDialogArea(Composite parent) {
			Composite control = (Composite) super.createDialogArea(parent);
			control.setLayout(new FillLayout());
			text = new Text(control, SWT.BORDER | SWT.MULTI | SWT.WRAP);
			if (initValue != null) {
				StringBuilder sb = new StringBuilder();
				for (String item : initValue) {
					sb.append(item);
					sb.append("\n");
				}
				result = sb.toString();
			}
			if (result != null) {
				text.setText(result);
			}
			text.addListener(SWT.Modify, new Listener() {
				public void handleEvent(Event event) {
					result = text.getText();
				}
			});
			text.addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_RETURN) {
						text.getParent().layout(true, true);
						getDialogArea().getParent().layout();
					}
				}
			});
			return control;
		}

		public String getResult() {
			return result;
		}

		public String[] getArray() {
			if (result == null) {
				return new String[0];
			}
			List<String> resultList = new ArrayList<String>();
			StringTokenizer stk = new StringTokenizer(result, "\n");
			while (stk.hasMoreElements()) {
				resultList.add(stk.nextToken().trim());
			}
			return resultList.toArray(new String[0]);
		}
	}
}
