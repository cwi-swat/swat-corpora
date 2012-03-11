/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class EditJavascriptCommandHandler extends AbstractHandler {

	private Logger log = Logger.getLogger(Activator.getDefault().getBundle().getSymbolicName());
	
	public EditJavascriptCommandHandler() {
		super();
	}
	
	public void dispose() {
	}

	private EObject getScriptedSelection(IWorkbenchWindow wbw) {
		return (wbw != null && wbw.getActivePage() != null ? getScriptedSelection(wbw.getActivePage().getSelection()) : null);
	}
	private IEditingDomainProvider getEditingDomainProvider(IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? (IEditingDomainProvider)part : null);
	}

	private EObject getScriptedSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).getFirstElement() instanceof EObject) {
			EObject eObject = (EObject)((IStructuredSelection)selection).getFirstElement();
			EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(eObject, JavascriptSupport.JAVASCRIPT_EXTENSION);
			if (scriptAttr != null) {
				return eObject;
			}
		}
		return null;
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EObject eObject = getScriptedSelection(HandlerUtil.getActiveWorkbenchWindow(event));
		openEditorForScriptedEObject(eObject, getEditingDomainProvider(HandlerUtil.getActiveWorkbenchWindow(event)));
		return eObject;
	}

	private void openEditorForScriptedEObject(final EObject eObject, final IEditingDomainProvider editingDomainProvider) {
		if (eObject != null) {
			final EAttribute scriptAttr = JavascriptSupport.getScriptSourceAttribute(eObject, JavascriptSupport.JAVASCRIPT_EXTENSION);
			if (scriptAttr != null) {
				Object attrValue = eObject.eGet(scriptAttr);
				final String scriptSource = (attrValue != null ? (String)attrValue : "");
				Resource resource = eObject.eResource();
				if (resource != null) {
					final String fragment = scriptAttr.getName() + convertFragmentToTempName(resource.getURIFragment(eObject));
					URI resUri = resource.getURI();
					URI jsUri = resUri.trimSegments(1).appendSegment(resUri.lastSegment() + "." + fragment);
					jsUri = jsUri.appendFileExtension(JavascriptSupport.JAVASCRIPT_EXTENSION);
					Path storagePath = new Path(jsUri.toPlatformString(true));
					try {
//						IFile jsFile = ResourcesPlugin.getWorkspace().getRoot().getFile(storagePath);
//						updateJsFile(scriptSource, jsFile);
//						final IEditorInput editorInput = new FileEditorInput(jsFile);
						String editorId = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(jsUri.lastSegment()).getId();
						final IEditorInput editorInput = new StringEditorInput(resUri + "#" + fragment, storagePath, scriptSource);

						final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, editorId);
						editor.addPropertyListener(new IPropertyListener() {
							public void propertyChanged(Object source, int propId) {
								if (IEditorPart.PROP_DIRTY == propId) {
									if (editor instanceof AbstractDecoratedTextEditor && (! editor.isDirty())) {
										IDocumentProvider documentProvider = ((AbstractDecoratedTextEditor)editor).getDocumentProvider();
										String newSource = documentProvider.getDocument(editorInput).get();
										try {
											ScriptSourceView.commitScriptText(eObject, newSource, editingDomainProvider);
										} catch (Exception e) {
											log.log(Level.WARNING, "Exception setting script text to " + newSource + ": " + e, e);
										}
									}
								}
							}
						});
					} catch (PartInitException e) {
						System.err.println(e);
//					} catch (CoreException e) {
//						System.err.println(e);
					}
				}				
			}
		}
	}

//	private void updateJsFile(String scriptSource, IFile jsFile) throws CoreException {
//		if (jsFile.exists()) {
//			jsFile.setContents(new StringBufferInputStream(scriptSource), true, false, null);
//		} else {
//			jsFile.create(new StringBufferInputStream(scriptSource), true, null);
//		}
//	}

	private String convertFragmentToTempName(String fragment) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < fragment.length(); i++) {
			char c = fragment.charAt(i);
			builder.append(Character.isJavaIdentifierPart(c) ? c : '_');
		}
		return builder.toString();
	}

	public boolean isEnabled() {
		EObject eObject = getScriptedSelection(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		return (eObject != null);
	}

	public boolean isHandled() {
		return true;
	}

	public void addHandlerListener(IHandlerListener handlerListener) {
	}
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}
}
