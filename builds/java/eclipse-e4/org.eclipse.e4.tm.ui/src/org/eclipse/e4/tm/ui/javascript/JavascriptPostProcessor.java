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
package org.eclipse.e4.tm.ui.javascript;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.emf.command.javascript.ApplyAsCommand;
import org.eclipse.e4.emf.ecore.javascript.DisplayAsyncSupport;
import org.eclipse.e4.emf.ecore.javascript.JavascriptNotificationSupport;
import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.e4.emf.ecore.javascript.functions.AbstractFunction;
import org.eclipse.e4.emf.ecore.javascript.functions.AdaptTo;
import org.eclipse.e4.emf.ecore.javascript.functions.ApplyAsync;
import org.eclipse.e4.emf.ecore.javascript.functions.BindingApply;
import org.eclipse.e4.tm.builder.IBuilder;
import org.eclipse.e4.tm.swt.widgets.WidgetsPackage;
import org.eclipse.e4.tm.ui.editor.IPostProcessor;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

public class JavascriptPostProcessor implements IPostProcessor {

	private JavascriptSupport javascriptSupport = null;

	public void postLoadModel(IAdaptable adaptable) {
		javascriptSupport = null;
	}

	private JavascriptSupport getJavascriptSupport(IAdaptable adaptable) {
		if (javascriptSupport == null) {
			javascriptSupport = new JavascriptSupport();

			IProject project = ((IFileEditorInput)adaptable.getAdapter(IFileEditorInput.class)).getFile().getProject();
			JavaProjectClassLoader classLoader = new JavaProjectClassLoader(project, javascriptSupport.getClass().getClassLoader());
			javascriptSupport.setApplicationClassLoader(classLoader);
			
			javascriptSupport.getURIConverter().getURIMap().put(URI.createURI(WidgetsPackage.eNS_URI).trimSegments(1), URI.createPlatformPluginURI("/org.eclipse.e4.tm/model/", true));
			
			Composite composite = (Composite)adaptable.getAdapter(Composite.class);
			EditingDomain editingDomain = (EditingDomain)adaptable.getAdapter(EditingDomain.class);
			javascriptSupport.defineConstantFunction("getEditingDomain", editingDomain, null, javascriptSupport.getScope(null));
			// pre-register EPackages, to avoid duplicate EPackage with same nsUri
			for (Resource resource: editingDomain.getResourceSet().getResources()) {
				if ("ecore".equals(resource.getURI().fileExtension()) && resource.getContents().size() > 0) {
					EObject contents = resource.getContents().get(0);
					if (contents instanceof EPackage) {
						javascriptSupport.registerPackage((EPackage)contents, null);
					}
				}
			}
			
			javascriptSupport.setVariable(null, "adaptTo", new AdaptTo());
			javascriptSupport.setVariable(null, "applyAsCommand", new ApplyAsCommand(editingDomain));
			DisplayAsyncSupport asyncSupport = new DisplayAsyncSupport(composite.getDisplay());
			javascriptSupport.setVariable(null, "applyAsync", new ApplyAsync(asyncSupport));
			javascriptSupport.setVariable(null, "bindingApply", new BindingApply(asyncSupport));
			javascriptSupport.setVariable(null, "supportNotifications", new AbstractFunction() {
				public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
					Notifier notifier = typeCheckArgument(args, 0, Notifier.class);
					javascriptSupport.supportNotifications(notifier);
					return Undefined.instance;
				}
			});
		}
		return javascriptSupport;
	}
	
	private JavascriptNotificationSupport javascriptNotificationSupport = null;

	public void postBuildModel(EObject model, IBuilder builder, IAdaptable adaptable) {
		// remove old JavascriptNotificationSupport adapter
		if (javascriptNotificationSupport != null) {
			javascriptNotificationSupport.unsetTarget(model);
		}
		JavascriptSupport javascriptSupport = getJavascriptSupport(adaptable);
//		javascriptNotificationSupport = javascriptSupport.supportNotifications(model);
		// trigger initialization
		javascriptSupport.wrap(model);
	}

	public Object getAdapter(Class key) {
		if (key.equals(JavascriptSupport.class)) {
			return javascriptSupport;
		}
		return null;
	}
}
