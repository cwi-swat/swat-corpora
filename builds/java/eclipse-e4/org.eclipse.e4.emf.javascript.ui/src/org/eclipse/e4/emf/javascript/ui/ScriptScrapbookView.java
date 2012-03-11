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

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.mozilla.javascript.Scriptable;

public class ScriptScrapbookView extends AbstractSelectionView {

	public void dispose() {
		disposeTextControl(scriptTextControl);
		disposeTextControl(evalResultControl);
		super.dispose();
	}

	private String getScriptControlText(boolean preferSelection) {
		String script = scriptTextControl.getText();
		if (preferSelection) {
			int start = scriptTextControl.getSelection().x, end = scriptTextControl.getSelection().y;
			if (start < end) {
				script = script.substring(start, end);
			}
		}
		return script.trim();
	}

	protected void updateView() {
		executeScriptAsCommandAction.setEnabled(editingDomainProvider != null);
	}

//	private void evaluateScript(String script) {
//		JavascriptSupport javascriptSupport = (JavascriptSupport)getAdapter(JavascriptSupport.class);
//		ScriptCommand command = new ScriptCommand(javascriptSupport);
//		ISelection selection = null;
//		ISelectionProvider selectionProvider = null;
//		if (editingDomainProvider instanceof ISelectionProvider) {
//			selectionProvider = (ISelectionProvider)editingDomainProvider;
//		} else if (editingDomainProvider instanceof IAdaptable) {
//			selectionProvider = (ISelectionProvider)((IAdaptable)editingDomainProvider).getAdapter(ISelectionProvider.class);
//		}
//		if (selectionProvider != null) {
//			selection = selectionProvider.getSelection();
//		}
//		if (selection instanceof IStructuredSelection) {
//			Object o = ((IStructuredSelection)selection).getFirstElement();
//			command.setScope(o);
//		}
//		if (editingDomainProvider != null) {
//			editingDomainProvider.getEditingDomain().getCommandStack().execute(command);
//		} else {
//			javascriptSupport.evaluate(script, null, true);
//		}
//	}

	private void evalScript(String script, boolean evalAsCommand) {
		JavascriptSupport javascriptSupport = (JavascriptSupport)getAdapter(JavascriptSupport.class);
		if (javascriptSupport == null) {
			setEvalExceptionText("No javascript support");
			return;
		}
		if (selection == null) {
			setEvalExceptionText("No selection");
			return;
		}
		try {
			Scriptable scope = javascriptSupport.createScope("ScriptScrapBookView");
			scope.setParentScope(javascriptSupport.getScope(selection));
			scope.setPrototype((Scriptable)javascriptSupport.wrap(selection));
			script = getFunctionSource(script);
			evalResultControl.setToolTipText("Result of evaluating and calling:\n" + script);
			Object result = javascriptSupport.evaluate(script, scope, true);
			result = javascriptSupport.callMethod(scope, "ScriptScrapBookView", (Object[])null, true);
			setEvalResultText(result);
		} catch (Exception e) {
			setEvalResultText(e);
			e.printStackTrace();
		}
	}

private String getFunctionSource(String script) {
	String[] lines = script.trim().split("[\n\r]");
	script = "function ScriptScrapBookView() { ";
	for (int i = 0; i < lines.length; i++) {
		String line = lines[i];
		if (i == lines.length - 1 && (! line.startsWith("return"))) {
			line = "return " + line;
		}
		script += line;
	}
	script += ";}";
	return script;
}

	private void setEvalExceptionText(String message) {
		setEvalResultText(new IllegalStateException(message));
	}

	private void setEvalResultText(Object result) {
		evalResultControl.setBackground(result instanceof Throwable ? evalResultControl.getDisplay().getSystemColor(SWT.COLOR_RED) : scriptTextControl.getBackground());
		evalResultControl.setText(String.valueOf(result));
	}
	
	private Text scriptTextControl, evalResultControl;
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		SashForm sash = new SashForm(parent, SWT.VERTICAL);
		scriptTextControl = createTextControl(sash, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		evalResultControl = createTextControl(sash, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		evalResultControl.setEditable(false);
		evalResultControl.setBackground(scriptTextControl.getBackground());
		sash.setWeights(new int[]{70, 30});

		scriptTextControl.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				// ENTER + MOD1
				if (e.keyCode == 13 && e.stateMask == SWT.MOD1) {
					evalScript(getScriptControlText(true), false);
				}
//				System.out.println("keyCode: " + e.keyCode + ", stateMask: " + e.stateMask);
//				System.out.println("1: " + SWT.MOD1 + ", " + "2: " + SWT.MOD2 + ", " + "3: " + SWT.MOD3 + ", " + "4: " + SWT.MOD4);
			}
		});
	}

	public void setFocus() {
		scriptTextControl.setFocus();
	}
	
	private IAction evalScriptAction = new Action("Eval") {
		public void run() {
			evalScript(getScriptControlText(true), false);
		}
	};
	private IAction executeScriptAsCommandAction = new Action("Execute") {
		public void run() {
			evalScript(getScriptControlText(true), true);
		}
	};

	protected void createMenu(IMenuManager mgr) {
		super.createMenu(mgr);
		mgr.add(evalScriptAction);
//		mgr.add(executeScriptAsCommandAction);
	}

	protected void createToolbar(IToolBarManager mgr) {
		super.createToolbar(mgr);
		mgr.add(evalScriptAction);
//		mgr.add(executeScriptAsCommandAction);
	}
}
