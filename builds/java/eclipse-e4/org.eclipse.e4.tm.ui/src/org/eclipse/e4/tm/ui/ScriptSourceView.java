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
package org.eclipse.e4.tm.ui;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ScriptSourceView extends AbstractTmSelectionView {

	public void dispose() {
		disposeTextControl(scriptTextControl);
		super.dispose();
	}

	protected void selectionChanged(ISelection selection) {
		commitScriptText();
		String scriptText = null;
		super.selectionChanged(selection);
		if (getSelectedEObject() != null) {
			scriptText = getScriptText();
		}
		setScriptControlText(scriptText);
	}
	
	private String getScriptControlText() {
		return scriptTextControl.getText();
	}
	private void setScriptControlText(String scriptText) {
		scriptTextControl.setText(scriptText != null ? scriptText : "");
	}
	
	protected boolean isValidSelection(Object o) {
		return o instanceof EObject;
	}

	private String getScriptText() {
		EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(getSelectedEObject(), JavascriptSupport.JAVASCRIPT_EXTENSION);
		if (scriptAttr != null) {
			Object value = getSelectedEObject().eGet(scriptAttr);
			if (value != null) {
				return value.toString().trim();
			}
		}
		return "";
	}

	private EObject getSelectedEObject() {
		return (EObject)this.selection;
	}

	private void commitScriptText() {
		if (editingDomainProvider != null && selection != null) {
			EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(getSelectedEObject(), JavascriptSupport.JAVASCRIPT_EXTENSION);
			if (scriptAttr != null) {
				EditingDomain editingDomain = editingDomainProvider.getEditingDomain();
				String scriptText = getScriptControlText();
				if (! scriptText.equals(getScriptText())) {
					Command command = new SetCommand(editingDomain, getSelectedEObject(), scriptAttr, scriptTextControl.getText().trim());
					if (command.canExecute()) {
						editingDomain.getCommandStack().execute(command);
					}
				}
			}
		}
	}
	
	private Text scriptTextControl;
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		scriptTextControl = createTextControl(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		scriptTextControl.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
			}
			public void focusLost(FocusEvent e) {
				commitScriptText();
			}
		});
	}

	public void setFocus() {
		scriptTextControl.setFocus();
	}
}
