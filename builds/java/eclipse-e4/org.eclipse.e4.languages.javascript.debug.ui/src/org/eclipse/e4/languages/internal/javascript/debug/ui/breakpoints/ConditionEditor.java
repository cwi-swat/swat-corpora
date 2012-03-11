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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.languages.internal.javascript.debug.ui.InternalConstants;
import org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.wst.jsdt.internal.ui.text.java.ContentAssistProcessor;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;
import org.eclipse.wst.jsdt.ui.text.JavaScriptSourceViewerConfiguration;
import org.eclipse.wst.jsdt.ui.text.JavaScriptTextTools;

/**
 * Widget set for editing breakpoint conditions
 * 
 * @since 0.9
 */
public class ConditionEditor {

	private JavaSourceViewer fViewer;
	private String fOldValue;
	private String fErrorMessage;
	IHandlerService fHandlerService;
	IHandler fHandler;
	IHandlerActivation fActivation;
    IDocumentListener fDocumentListener;
	IContentAssistProcessor fCompletionProcessor;	
	JSDIBreakpoint fBreakpoint = null;
	
	/**
	 * Create the editor in the given parent
	 * @param parent
	 */
	public void createEditor(Composite parent, JSDIBreakpoint breakpoint) {
		this.fBreakpoint = breakpoint;
		String condition = new String();
		IPreferenceStore store= JavaScriptPlugin.getDefault().getCombinedPreferenceStore();
		try {
			JavaScriptTextTools tools = JavaScriptPlugin.getDefault().getJavaTextTools();
			condition = fBreakpoint.getCondition();
			fErrorMessage  = "Enter a condition"; 
			fOldValue = InternalConstants.EMPTY_STRING;
			
			fViewer = new JavaSourceViewer(parent, null, null, false, SWT.V_SCROLL | SWT.H_SCROLL, store);
			fViewer.setInput(parent);
			IDocument document = new Document();
			tools.setupJavaDocumentPartitioner(document, IJavaScriptPartitions.JAVA_PARTITIONING);
			IType type = BreakpointUtils.getType(fBreakpoint);
			if (type == null) {
			} 
			else {
				try {	
					String source = null;
					IJavaScriptUnit junit = type.getJavaScriptUnit();
					if (junit != null && junit.getJavaScriptProject().getProject().exists()) {
						source = junit.getSource();
					} 
					else {
						IClassFile classFile = type.getClassFile();
						if (classFile != null) {
							source = classFile.getSource();
						}
					}
					int lineNumber = fBreakpoint.getMarker().getAttribute(IMarker.LINE_NUMBER, -1);
					int position= -1;
					if (source != null && lineNumber != -1) {
						try {
							position = new Document(source).getLineOffset(lineNumber - 1);
						} 
						catch (BadLocationException e) {
							//TODO log this
						}
					}
				} 
				catch (CoreException e) {
					//TODO log this
				}
			}
			JavaScriptSourceViewerConfiguration config = new JavaScriptSourceViewerConfiguration(tools.getColorManager(), store, null, IJavaScriptPartitions.JAVA_PARTITIONING);
			fViewer.configure(config);
			fCompletionProcessor = new ContentAssistProcessor((ContentAssistant) config.getContentAssistant(fViewer), IJavaScriptPartitions.JAVA_PARTITIONING);
			fViewer.setEditable(true);
		//if we don't check upstream tracing can throw assertion exceptions see bug 181914
			document.set((condition == null ? "" : condition)); //$NON-NLS-1$
			fViewer.setDocument(document);
			fViewer.setUndoManager(new TextViewerUndoManager(10));
			fViewer.getUndoManager().connect(fViewer);
			fDocumentListener = new IDocumentListener() {
	            public void documentAboutToBeChanged(DocumentEvent event) {
	            }
	            public void documentChanged(DocumentEvent event) {
	                valueChanged();
	            }
	        };
			fViewer.getDocument().addDocumentListener(fDocumentListener);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			fViewer.getControl().setLayoutData(gd);
			fHandler = new AbstractHandler() {
				public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
					fViewer.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
					return null;
				}
			};
			fHandlerService = (IHandlerService) PlatformUI.getWorkbench().getAdapter(IHandlerService.class);
			
			fViewer.getControl().addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					activateContentAssist();
				}
				public void focusLost(FocusEvent e) {
					deactivateContentAssist();
				}				
			});
		} 
		catch (CoreException exception) {
			//TODO log this
		}
	}
	
	/**
	 * Returns the condition defined in the source viewer.
	 * @return the contents of this condition editor
	 */
	public String getCondition() {
		return fViewer.getDocument().get();
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#refreshValidState()
	 */
	protected void refreshValidState() {
		if (!fViewer.isEditable()) {
			/*fPage.removeErrorMessage(fErrorMessage);*/
			
		} else {
			String text = fViewer.getDocument().get();
			if (!(text != null && text.trim().length() > 0)) {
				/*fPage.addErrorMessage(fErrorMessage);*/
			} else {
				/*fPage.removeErrorMessage(fErrorMessage);*/
			}
		}
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean, org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled) {
	    fViewer.setEditable(enabled);
	    fViewer.getTextWidget().setEnabled(enabled);
		if (enabled) {
			fViewer.getTextWidget().setFocus();			
		} else {
			Color color = fViewer.getControl().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			fViewer.getTextWidget().setBackground(color);			
		}
		valueChanged();
	}
	
	/**
	 * Handle that the value changed
	 */
	protected void valueChanged() {
		String newValue = fViewer.getDocument().get();
		if (!newValue.equals(fOldValue)) {
			fOldValue = newValue;
		}
		refreshValidState();
	}
	
	/**
	 * Dispose of the handlers, etc
	 */
	public void dispose() {
		deactivateContentAssist();
	    fViewer.getDocument().removeDocumentListener(fDocumentListener);
	}
	
	private void activateContentAssist() {
		fActivation = fHandlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, fHandler);
	}

	private void deactivateContentAssist() {
		if(fActivation != null) {
			fHandlerService.deactivateHandler(fActivation);
			fActivation = null;
		}
	}
}
