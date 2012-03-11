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

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension;
import org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDIDebugModel;
import org.eclipse.e4.languages.javascript.debug.model.JSDIFunctionBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDILineBreakpoint;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.WorkingCopyManager;
import org.eclipse.wst.jsdt.ui.IWorkingCopyManager;

/**
 * Javascript adapter for toggling breakpoints in the JSDT editor
 * 
 * @since 1.0
 */
public class ToggleBreakpointAdapter implements IToggleBreakpointsTargetExtension {
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension#canToggleBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}

	/**
	 * Toggles a line breakpoint 
	 * @param part
	 * @param selection
	 * @param element
	 */
	void toggleLineBreakpoint(final IWorkbenchPart part, final ITextSelection selection, final IJavaScriptElement element, final int linenumber) {
		Job job = new Job("Toggle Line Breakpoints") { //$NON-NLS-1$
            protected IStatus run(IProgressMonitor monitor) {
            	try {
            		ITextEditor editor = getTextEditor(part);
					if(editor != null && part instanceof IEditorPart) {
						if(element == null) {
							reportToStatusLine(part, "Failed to create Javascript line breakpoint");
							return Status.CANCEL_STATUS;
						}
						IResource resource = getBreakpointResource(element);
						IBreakpoint bp = lineBreakpointExists(resource, linenumber);
						if(bp != null) {
							DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(bp, true);
							return Status.OK_STATUS;
						}
						else {
							IDocumentProvider documentProvider = editor.getDocumentProvider();
							IDocument document = documentProvider.getDocument(editor.getEditorInput());
							int charstart = -1, charend = -1;
							try {
								IRegion line = document.getLineInformation(linenumber - 1);
								charstart = line.getOffset();
								charend = charstart + line.getLength();
							}
							catch (BadLocationException ble) {}
							HashMap attributes = new HashMap();
							attributes.put(JSDIBreakpoint.TYPE_NAME, getTypeName(element));
							attributes.put(JSDIBreakpoint.SCRIPT_PATH, getScriptPath(element));
							attributes.put(JSDIBreakpoint.HANDLE, element.getHandleIdentifier());
							new JSDILineBreakpoint(resource, linenumber, charstart, charend, attributes, true);
							return Status.OK_STATUS;
						}
					}
					reportToStatusLine(part, "Failed to create Javascript line breakpoint");
					return Status.CANCEL_STATUS;
	            }
	        	catch(CoreException ce) {
	        		return ce.getStatus();
        		}
        	}
		};
		job.setPriority(Job.INTERACTIVE);
        job.setSystem(true);
        job.schedule();
	}
	
	/**
	 * Returns the path to the script in the workspace or the name of the script in the event it is
	 * and external or virtual script
	 * @param element
	 * @return the path to the script 
	 */
	String getScriptPath(IJavaScriptElement element) {
		switch(element.getElementType()) {
		case IJavaScriptElement.TYPE: {
			return ((IType)element).getPath().toOSString();
		}
		case IJavaScriptElement.METHOD:
		case IJavaScriptElement.FIELD: {
			IMember member = (IMember) element;
			IType type = member.getDeclaringType();
			if(type == null) {
				IJavaScriptElement parent = element.getParent();
				switch(parent.getElementType()) {
					case IJavaScriptElement.TYPE: {
						return ((IType)parent).getPath().toOSString();
					}
					case IJavaScriptElement.JAVASCRIPT_UNIT: 
					case IJavaScriptElement.CLASS_FILE: {
						return ((ITypeRoot)parent).getPath().toOSString();
					}
				}
				return element.getParent().getElementName();
			}
			return type.getPath().toOSString();
		}
		default: {
			return element.getElementName();
		}
	}
	}
	
	/**
	 * Resolves the type name from the given element
	 * @param element
	 * @return
	 */
	String getTypeName(IJavaScriptElement element) {
		switch(element.getElementType()) {
			case IJavaScriptElement.TYPE: {
				return ((IType)element).getFullyQualifiedName();
			}
			case IJavaScriptElement.METHOD:
			case IJavaScriptElement.FIELD: {
				IMember member = (IMember) element;
				IType type = member.getDeclaringType();
				if(type != null) {
					return type.getFullyQualifiedName();
				}
			}
			default: {
				return null;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleLineBreakpoints(final IWorkbenchPart part, final ISelection selection) throws CoreException {
		//do nothing
	}
	
	/**
	 * Resolves the declaring type for the member
	 * @param member
	 * @return the declaring type for the member
	 */
	IType resolveType(IJavaScriptElement element) {
		switch(element.getElementType()) {
			case IJavaScriptElement.TYPE: {
				return (IType) element;
			}
			case IJavaScriptElement.METHOD:
			case IJavaScriptElement.FIELD: {
				IMember member = (IMember) element;
				IType type = member.getDeclaringType();
				if(type == null) {
					ITypeRoot root = (ITypeRoot) member.getParent();
					type = root.findPrimaryType();
				}
				return type;
			}
			default: {
				//already a type root
				if(element instanceof ITypeRoot) {
					return ((ITypeRoot)element).findPrimaryType();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the resource on which a breakpoint marker should
	 * be created for the given member. The resource returned is the 
	 * associated file, or workspace root in the case of a binary in 
	 * an external archive.
	 * 
	 * @param element member in which a breakpoint is being created
	 * @return resource the resource on which a breakpoint marker
	 *  should be created
	 */
	IResource getBreakpointResource(IJavaScriptElement element) {
		IResource res = element.getResource();
		if (res == null) {
			res = ResourcesPlugin.getWorkspace().getRoot();
		}
		else if(!res.getProject().exists()) {
			res = ResourcesPlugin.getWorkspace().getRoot();
		}
		return res;
	}
	
    /**
     * Returns the {@link IJavaScriptElement} that we want to set the breakpoint on or in.
     * In the source editor case we further visit the element to find the correct location.
     * @param part
     * @param selection
     * @return the {@link IJavaScriptElement} to set the breakpoint in or on, or <code>null</code> if the lookup fails
     * @throws CoreException
     */
    protected IJavaScriptElement getElement(IWorkbenchPart part, ISelection selection) throws CoreException {
    	ITextEditor textEditor = getTextEditor(part);
        if (textEditor != null && selection instanceof ITextSelection) {
            ITextSelection textSelection = (ITextSelection) selection;
            IEditorInput editorInput = textEditor.getEditorInput();
            IDocumentProvider documentProvider = textEditor.getDocumentProvider();
            if (documentProvider == null) {
                throw new CoreException(Status.CANCEL_STATUS);
            }
            IDocument document = documentProvider.getDocument(editorInput);
            int offset = textSelection.getOffset();
            if (document != null) {
                try {
                    IRegion region = document.getLineInformationOfOffset(offset);
                    int end = region.getOffset() + region.getLength();
                    while (Character.isWhitespace(document.getChar(offset)) && offset < end) {
                        offset++;
                    }
                } catch (BadLocationException e) {}
            }
            ITypeRoot root = getTypeRoot(editorInput);
            if(root instanceof IJavaScriptUnit) {
            	IJavaScriptUnit unit = (IJavaScriptUnit) root;
                synchronized (unit) {
                    unit.reconcile(IJavaScriptUnit.NO_AST , false, null, null);
                }
            }
            if(root != null){
                IJavaScriptElement e = root.getElementAt(offset);
                if (e instanceof IMember) {
                    return (IMember) e;
                }
                if(e == null) {
                	return root;
                }
            }
        }
        else {
        	//direct selection in the outline
        	IStructuredSelection ss = (IStructuredSelection) selection;
        	Object o = ss.getFirstElement();
        	if(o instanceof IMember) {
        		return (IMember) o;
        	}
        }
        return null;
    }
	
	/**
	 * Returns the {@link ITypeRoot} for the given editor input
	 * @param input
	 * @return the {@link ITypeRoot} for the editor input or <code>null</code>
	 */
	ITypeRoot getTypeRoot(IEditorInput input) {
		ITypeRoot root = (ITypeRoot) input.getAdapter(IClassFile.class);
		if(root == null) {
			IWorkingCopyManager manager = JavaScriptPlugin.getDefault().getWorkingCopyManager();
			root = manager.getWorkingCopy(input);
		}
		if(root == null) {
			root = ((WorkingCopyManager)JavaScriptPlugin.getDefault().getWorkingCopyManager()).getWorkingCopy(input, false);
		}
		return root;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}
	
	/**
	 * Delegate for toggling a method breakpoint
	 * @param part
	 * @param element
	 */
	void toggleMethodBreakpoint(final IWorkbenchPart part, final IJavaScriptElement element) {
		Job job = new Job("Toggle Function Breakpoints") { //$NON-NLS-1$
            protected IStatus run(IProgressMonitor monitor) {
            	try {
					if(element == null) {
						reportToStatusLine(part, "Failed to create Javascript function breakpoint");
						return Status.CANCEL_STATUS;
					}
					if(element.getElementType() == IJavaScriptElement.METHOD) {
						IFunction method = (IFunction) element;
						IResource resource = getBreakpointResource(element);
						IBreakpoint bp = methodBreakpointExists(resource, method.getElementName(), method.getSignature());
						if(bp != null) {
							DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(bp, true);
							return Status.OK_STATUS;
						}
						else {
							ISourceRange range = method.getNameRange();
							int start = -1, end = -1;
							if(range != null) {
								start = range.getOffset();
								end = start + range.getLength();
							}
							HashMap attributes = new HashMap();
							//hack if the type is null the member is declared in a top-level type
							//nothing else we can do
							attributes.put(JSDIBreakpoint.TYPE_NAME, getTypeName(element));
							attributes.put(JSDIBreakpoint.SCRIPT_PATH, getScriptPath(element));
							attributes.put(JSDIBreakpoint.HANDLE, element.getHandleIdentifier());
							new JSDIFunctionBreakpoint(resource, method.getElementName(), method.getSignature(), start, end, attributes, true);
							return Status.OK_STATUS;
						}
						
					}
					reportToStatusLine(part, "Failed to create Javascript function breakpoint");
				}
	        	catch(CoreException ce) {
	        		return ce.getStatus();
        		}
	        	return Status.CANCEL_STATUS;
        	}
		};
		job.setPriority(Job.INTERACTIVE);
        job.setSystem(true);
        job.schedule();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleMethodBreakpoints(final IWorkbenchPart part, final ISelection selection) throws CoreException {
		//do nothing
	}

	/**
	 * Sends a friendly message to the status line about why we could not set a breakpoint
	 * @param part
	 * @param message
	 */
	void reportToStatusLine(final IWorkbenchPart part, final String message) {
		getStandardDisplay().asyncExec(new Runnable() {
            public void run() {
				IEditorStatusLine statusLine = (IEditorStatusLine) part.getAdapter(IEditorStatusLine.class);
		        if (statusLine != null) {
		            if (message != null) {
		                statusLine.setMessage(true, message, null);
		            } else {
		                statusLine.setMessage(true, null, null);
		            }
		        }
            }
		});
	}
	
	/**
	 * Returns the standard display to be used. 
	 */
	Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension#toggleBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		IJavaScriptElement element = getElement(part, selection);
		if(selection instanceof ITextSelection) {
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			if(element instanceof IMember) {
				parser.setSource(((IMember)element).getTypeRoot());
			}
			else {
				parser.setSource((ITypeRoot)element);
			}
			JavaScriptUnit jsunit = (JavaScriptUnit) parser.createAST(null);
			BreakpointLocationFinder finder = new BreakpointLocationFinder(jsunit, ((TextSelection)selection).getStartLine()+1, false);
			jsunit.accept(finder);
			switch(finder.getLocation()) {
				case BreakpointLocationFinder.UNKNOWN : {
					reportToStatusLine(part, "A valid location could not be found for the breakpoint");
					return;
				}
				case BreakpointLocationFinder.LINE: {
					toggleLineBreakpoint(part, (ITextSelection) selection, element, finder.getLineNumber());
					return;
				}
				case BreakpointLocationFinder.FUNCTION: {
					toggleMethodBreakpoint(part, element);
					return;
				}
			}
		}
		else {
			if(element.getElementType() == IJavaScriptElement.METHOD) {
				toggleMethodBreakpoint(part, element);
			}
		}
	}
	
	/**
	 * Returns if a line breakpoint exists on the given line in the given resource
	 * @param resource
	 * @param linenumber
	 * @return true if a line breakpoint exists on the given line in the given resource, false otherwise
	 */
	IBreakpoint lineBreakpointExists(IResource resource, int linenumber) {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSDIDebugModel.MODEL_ID);
		JSDIBreakpoint breakpoint = null;
		for (int i = 0; i < breakpoints.length; i++) {
			breakpoint = (JSDIBreakpoint) breakpoints[i];
			try {
				if(JSDIBreakpoint.JSDI_LINE_BREAKPOINT.equals(breakpoint.getMarker().getType()) &&
					resource.equals(breakpoint.getMarker().getResource()) &&
					linenumber == breakpoint.getLineNumber()) {
					return breakpoint;
				}
			} catch (CoreException e) {}
		}
		return null;
	}
	
	/**
	 * Returns if a method breakpoint exists with the given name and signature in the given resource
	 * @param resource
	 * @param name
	 * @param signature
	 * @return true if a method breakpoint exists with the given name and signature in the given resource, false otherwise
	 */
	IBreakpoint methodBreakpointExists(IResource resource, String name, String signature) {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JSDIDebugModel.MODEL_ID);
		JSDIBreakpoint breakpoint = null;
		for (int i = 0; i < breakpoints.length; i++) {
			breakpoint = (JSDIBreakpoint) breakpoints[i];
			try {
				if(JSDIBreakpoint.JSDI_FUNCTION_BREAKPOINT.equals(breakpoint.getMarker().getType()) &&
					resource.equals(breakpoint.getMarker().getResource()) &&
					name.equals(breakpoint.getMarker().getAttribute(JSDIBreakpoint.FUNCTION_NAME)) &&
					signature.equals(breakpoint.getMarker().getAttribute(JSDIBreakpoint.FUNCTION_SIGNAURE))) {
					return breakpoint;
				}
			} catch (CoreException e) {}
		}
		return null;
	}
	
	/**
     * Returns the resource associated with the specified editor part
     * @param editor the currently active editor part
     * @return the corresponding <code>IResource</code> from the editor part
     */
    IResource getResource(IEditorPart editor) {
        IEditorInput editorInput = editor.getEditorInput();
        IResource resource = (IResource) editorInput.getAdapter(IFile.class);
        if (resource == null) {
            resource = ResourcesPlugin.getWorkspace().getRoot();
        }
        return resource;
    }
	
    /**
     * Returns the text editor associated with the given part or <code>null</code>
     * if none. In case of a multi-page editor, this method should be used to retrieve
     * the correct editor to perform the breakpoint operation on.
     * 
     * @param part workbench part
     * @return text editor part or <code>null</code>
     */
    ITextEditor getTextEditor(IWorkbenchPart part) {
    	if (part instanceof ITextEditor) {
    		return (ITextEditor) part;
    	}
    	return (ITextEditor) part.getAdapter(ITextEditor.class);
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {}
}
