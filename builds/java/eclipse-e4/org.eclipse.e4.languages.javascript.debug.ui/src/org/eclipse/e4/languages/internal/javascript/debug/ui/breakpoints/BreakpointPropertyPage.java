/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.internal.javascript.debug.ui.breakpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.languages.internal.javascript.debug.ui.InternalConstants;
import org.eclipse.e4.languages.internal.javascript.debug.ui.SWTFactory;
import org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Abstract class for breakpoint property pages
 * 
 * @since 0.9
 */
public class BreakpointPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	private Button fEnableConditionButton, suspendwhentrue = null;
	/*private ConditionEditor fConditionEditor;*/
	private Text conditiontext = null;
	protected Button enabledbutton;
	protected Button hitcountbutton;
	protected Text hitcounttext;
	protected Combo suspendpolicycombo;
	protected List errors = new ArrayList();
	
	/**
	 * @return the underlying {@link JSDIBreakpoint} element this page was opened on
	 */
	protected JSDIBreakpoint getBreakpoint() {
		return (JSDIBreakpoint) getElement();
	}
	
	/**
	 * Allows subclasses to add type specific labels to the common Java
	 * breakpoint page.
	 * @param parent
	 */
	protected void createTypeSpecificLabels(Composite parent) {}
	
	/**
	 * Allows subclasses to add type specific editors to the common Java
	 * breakpoint page.
	 * @param parent
	 * @throws CoreException
	 */
	protected void createTypeSpecificEditors(Composite parent) throws CoreException {}
	
	/**
	 * Stores the values configured in this page. This method
	 * should be called from within a workspace runnable to
	 * reduce the number of resource deltas.
	 */
	protected void doStore() throws CoreException {
		JSDIBreakpoint breakpoint = getBreakpoint();
		//store hit count
		int hitCount = -1;
		if (hitcountbutton.getSelection()) {
			try {
				hitCount = Integer.parseInt(hitcounttext.getText());
			} 
			catch (NumberFormatException e) {
				//TODO log this
			}
			breakpoint.setHitCount(hitCount);
		}
		else {
			breakpoint.setHitCount(0);
		}
		breakpoint.setEnabled(enabledbutton.getSelection());
		breakpoint.setSuspendPolicy(suspendpolicycombo.getSelectionIndex()+1);
		if(fEnableConditionButton != null && fEnableConditionButton.getSelection()) {
			getBreakpoint().setConditionEnabled(true);
			getBreakpoint().setCondition(this.conditiontext.getText().trim());
			getBreakpoint().setConditionSuspendOnTrue(this.suspendwhentrue.getSelection());
		}
		else {
			//do not erase the old condition in case the user wants to turn it back on
			getBreakpoint().setConditionEnabled(false);
		}
	}
	
	/**
	 * Creates the button to toggle enablement of the breakpoint
	 * @param parent
	 * @throws CoreException
	 */
	protected void createEnabledButton(Composite parent) throws CoreException {
		enabledbutton = SWTFactory.createCheckButton(parent, "&Enabled", null, false, 1); 
		enabledbutton.setSelection(getBreakpoint().isEnabled());
	}
	
	/**
	 * Creates a default hit count editor
	 * @param parent 
	 * @throws CoreException
	 */
	protected void createHitCountEditor(Composite parent) throws CoreException {
		Composite hitCountComposite = SWTFactory.createComposite(parent, parent.getFont(), 2, 1, GridData.FILL_HORIZONTAL, 0, 0);
		hitcountbutton = SWTFactory.createCheckButton(hitCountComposite, "&Hit Count:", null, false, 1); 
		hitcountbutton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				hitcounttext.setEnabled(hitcountbutton.getSelection());
				hitCountChanged();
			}
		});
		int hitCount = getBreakpoint().getHitCount();
		String hitCountString= InternalConstants.EMPTY_STRING;
		if (hitCount > 0) {
			hitCountString = new Integer(hitCount).toString();
			hitcountbutton.setSelection(true);
		} else {
			hitcountbutton.setSelection(false);
		}
		hitcounttext = SWTFactory.createSingleText(hitCountComposite, 1);
		hitcounttext.setText(hitCountString);
		if (hitCount <= 0) {
			hitcounttext.setEnabled(false);
		}
		hitcounttext.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				hitCountChanged();
			}
		});
	}
	
	/**
	 * Validates the current state of the hit count editor.
	 * Hit count value must be a positive integer.
	 */
	void hitCountChanged() {
		if (!hitcountbutton.getSelection()) {
			removeErrorMessage("Hit count must be a positive integer");
			return;
		}
		String hitCountText= hitcounttext.getText();
		int hitCount= -1;
		try {
			hitCount = Integer.parseInt(hitCountText);
		} 
		catch (NumberFormatException e1) {
			addErrorMessage("Hit count must be a positive integer");
			return;
		}
		if (hitCount < 1) {
			addErrorMessage("Hit count must be a positive integer");
		} else {
			removeErrorMessage("Hit count must be a positive integer");
		}
	}
	
	/**
	 * Creates the controls that allow the user to specify the breakpoint's
	 * condition
	 * @param parent the composite in which the condition editor should be created
	 * @throws CoreException if an exception occurs accessing the breakpoint
	 */
	protected void createConditionEditor(Composite parent) throws CoreException {
		JSDIBreakpoint breakpoint = getBreakpoint();
		String label = null;
		/*if (breakpoint.getModelIdentifier() != null) {
			IBindingService bindingService = (IBindingService)PlatformUI.getWorkbench().getAdapter(IBindingService.class);
			if(bindingService != null) {
				TriggerSequence keyBinding = bindingService.getBestActiveBindingFor(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
				if (keyBinding != null) {
					label = NLS.bind("E&nable Condition ({0} for code assist)", keyBinding.format()); 
				} 
			}
		}*/
		if (label == null) {
			label = "E&nable Condition (code assist not available)";
		}
		
		Composite conditionComposite = SWTFactory.createGroup(parent, InternalConstants.EMPTY_STRING, 1, 1, GridData.FILL_BOTH);
		fEnableConditionButton = SWTFactory.createCheckButton(conditionComposite, label, null, false, 1);
		fEnableConditionButton.setSelection(breakpoint.isConditionEnabled());
		fEnableConditionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setConditionEnabled(fEnableConditionButton.getSelection());
			}
		});
		this.conditiontext = SWTFactory.createText(conditionComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, 1, GridData.FILL_BOTH);
		String condition = breakpoint.getCondition();
		if(condition != null) {
			this.conditiontext.setText(condition);
		}
		this.suspendwhentrue = SWTFactory.createCheckButton(conditionComposite, "Suspend &when the condition is true", null, false, 1);
		this.suspendwhentrue.setSelection(breakpoint.isConditionSuspendOnTrue());
		/*fConditionEditor = new ConditionEditor();
		fConditionEditor.createEditor(conditionComposite, getBreakpoint());
		fSuspendWhenLabel = SWTFactory.createLabel(conditionComposite, "Suspend when:", 1);*/ 
		setConditionEnabled(fEnableConditionButton.getSelection());
	}
	
	/**
	 * Sets the enabled state of the condition editing controls.
	 * @param enabled
	 */
	private void setConditionEnabled(boolean enabled) {
		this.conditiontext.setEnabled(enabled);
		this.suspendwhentrue.setEnabled(enabled);
		//fConditionEditor.setEnabled(enabled);
		//fSuspendWhenLabel.setEnabled(enabled);
	}
	
	/**
	 * Creates the editor for configuring the suspend policy (suspend target or suspend thread) of the breakpoint.
	 * @param parent 
	 */
	protected void createSuspendPolicyEditor(Composite parent) throws CoreException {
		Composite comp = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
		SWTFactory.createLabel(comp, "Suspend &Policy:", 1); 
		boolean suspendThread = getBreakpoint().getSuspendPolicy() == JSDIBreakpoint.SUSPEND_THREAD;
		suspendpolicycombo = new Combo(comp, SWT.BORDER | SWT.READ_ONLY);
		suspendpolicycombo.add("Suspend Thread");
		suspendpolicycombo.add("Suspend Target");
		suspendpolicycombo.select(1);
		if(suspendThread) {
			suspendpolicycombo.select(0);
		}
	}
	
	/**
	 * Creates the labels displayed for the breakpoint.
	 * @param parent
	 */
	protected void createLabels(Composite parent) {
		Composite labelComposite = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
		try {
			String name = getBreakpoint().getTypeName();
			if (name != null) {
				SWTFactory.createLabel(labelComposite, "&Type Name:", 1); 
				Text text = SWTFactory.createText(labelComposite, SWT.READ_ONLY | SWT.SINGLE, 1, GridData.FILL_HORIZONTAL);
				text.setText(name);
				text.setBackground(parent.getBackground());
			}
			name = getBreakpoint().getScriptPath();
			if(name != null) {
				SWTFactory.createLabel(labelComposite, "&Script Path:", 1);
				Text text = SWTFactory.createText(labelComposite, SWT.READ_ONLY, 1, GridData.FILL_HORIZONTAL);
				text.setText(name);
				text.setBackground(parent.getBackground());
			}
			createTypeSpecificLabels(labelComposite);
		} catch (CoreException ce) {
			//TODO log this
		}
	}
	
	/**
	 * Adds the given error message to the errors currently displayed on this page.
	 * The page displays the most recently added error message.
	 * Clients should retain messages that are passed into this method as the
	 * message should later be passed into removeErrorMessage(String) to clear the error.
	 * This method should be used instead of setErrorMessage(String).
	 * @param message the error message to display on this page.
	 */
	protected void addErrorMessage(String message) {
		errors.remove(message);
		errors.add(message);
		setErrorMessage(message);
		setValid(message == null);
	}
	
	/**
	 * Removes the given error message from the errors currently displayed on this page.
	 * When an error message is removed, the page displays the error that was added
	 * before the given message. This is akin to popping the message from a stack.
	 * Clients should call this method instead of setErrorMessage(null).
	 * @param message the error message to clear
	 */
	protected void removeErrorMessage(String message) {
		errors.remove(message);
		if (errors.isEmpty()) {
			addErrorMessage(null);
		} else {
			addErrorMessage((String) errors.get(errors.size() - 1));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		setTitle("Script Load Breakpoint");
		noDefaultAndApplyButton();
		Composite comp = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_BOTH);
		createLabels(comp);
		try {
			createEnabledButton(comp);
			createHitCountEditor(comp);
			createTypeSpecificEditors(comp);
			createSuspendPolicyEditor(comp); // Suspend policy is considered uncommon. Add it last.
		} 
		catch (CoreException e) {
			//TODO log this
		}
		setValid(true);
		setControl(parent);
		return comp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		IWorkspaceRunnable wr = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				doStore();
			}
		};
		try {
			ResourcesPlugin.getWorkspace().run(wr, null, 0, null);
		} 
		catch (CoreException e) {
			//TODO log this
		}
		return super.performOk();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		//TODO add help hooks here
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	public void dispose() {
		/*if (fConditionEditor != null) {
		fConditionEditor.dispose();
		}*/
		super.dispose();
	}
}
