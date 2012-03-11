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
package org.eclipse.e4.emf.javascript.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class ScriptSourceView extends AbstractSelectionView {

	private Logger log = Logger.getLogger(Activator.getDefault().getBundle().getSymbolicName());

	public void dispose() {
		disposeTextControl(scriptTextControl);
		super.dispose();
	}

	protected void selectionChanged(ISelection selection) {
		commitScriptText(getSelectedEObject());
		String scriptText = null;
		super.selectionChanged(selection);
		if (getSelectedEObject() != null) {
			scriptText = getScriptText(getSelectedEObject());
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

	private String getScriptText(EObject eObject) {
		EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(eObject, JavascriptSupport.JAVASCRIPT_EXTENSION);
		return (scriptAttr != null ? getScriptText(eObject, scriptAttr) : "");
	}
	private static String getScriptText(EObject eObject, EAttribute scriptAttr) {
		Object value = eObject.eGet(scriptAttr);
		return (value != null ? value.toString().trim() : "");
	}

	private EObject getSelectedEObject() {
		return (EObject)this.selection;
	}

	private void commitScriptText(EObject eObject) {
		if (selection != null) {
			String scriptText = getScriptControlText();
			try {
				commitScriptText(eObject, scriptText, editingDomainProvider);
			} catch (Exception e) {
				log.log(Level.WARNING, "Exception setting script text to " + scriptText + ": " + e, e);
			}
		}
	}

	static void commitScriptText(EObject eObject, String scriptText, IEditingDomainProvider editingDomainProvider) {
		EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(eObject, JavascriptSupport.JAVASCRIPT_EXTENSION);
		if (editingDomainProvider != null && scriptAttr != null) {
			EditingDomain editingDomain = editingDomainProvider.getEditingDomain();
			if (! scriptText.equals(getScriptText(eObject, scriptAttr))) {
				Command command = new SetCommand(editingDomain, eObject, scriptAttr, scriptText);
				if (command.canExecute()) {
					editingDomain.getCommandStack().execute(command);
				}
			}
		} else if (scriptAttr != null) {
			eObject.eSet(scriptAttr, scriptText);
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
				commitScriptText(getSelectedEObject());
			}
		});
	}

	public void setFocus() {
		scriptTextControl.setFocus();
	}
}
