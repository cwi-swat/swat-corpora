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
package org.eclipse.e4.xwt.forms;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.callback.ICreatedCallback;
import org.eclipse.e4.xwt.callback.ILoadedCallback;
import org.eclipse.e4.xwt.forms.metaclass.ButtonMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.CompositeMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.ExpandableCompositeMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.FormMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.FormTextMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.HyperlinkMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.ImageHyperlinkMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.LabelMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.ScrolledFormMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.ScrolledPageBookMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.SectionMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.TableMetaclass;
import org.eclipse.e4.xwt.forms.metaclass.TextMetaclass;
import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public class XWTForms {
	private static Object FormsProfile;
	
	private static ICreatedCallback CreatedAction = new ICreatedCallback() {
		
		public void onCreated(Object sender) {
			Widget widget = UserData.getWidget(sender);
			if (widget instanceof Control) {
				ToolKitUtil.adapt((Control)widget);
			}
		}
	};
			
	private static boolean applyFormsProfile () {
		if (FormsProfile == null) {
			FormsProfile = XWT.createUIProfile();
			XWT.registerMetaclass(new FormMetaclass());
			XWT.registerMetaclass(new ButtonMetaclass());
			XWT.registerMetaclass(new LabelMetaclass());
			XWT.registerMetaclass(new TextMetaclass());
			XWT.registerMetaclass(new TableMetaclass());
			XWT.registerMetaclass(new CompositeMetaclass());
			XWT.registerMetaclass(new FormTextMetaclass());
			XWT.registerMetaclass(new HyperlinkMetaclass());
			XWT.registerMetaclass(new ImageHyperlinkMetaclass());
			XWT.registerMetaclass(new ExpandableCompositeMetaclass());
			XWT.registerMetaclass(new SectionMetaclass());
			XWT.registerMetaclass(new ScrolledPageBookMetaclass());
			XWT.registerMetaclass(new ScrolledFormMetaclass());
		}
		return XWT.applyProfile(FormsProfile);
	}
			
	/**
	 * Load the file content. All widget will be created but they are showed. This method return the root element.
	 * 
	 */
	static public synchronized Object load(URL file) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		return loadWithOptions(file, options);
	}

	/**
	 * Load the file content. All widget will be created but they are showed. This method return the root element.
	 * 
	 */
	static public synchronized Object load(URL file, Object dataContext) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(IXWTLoader.DATACONTEXT_PROPERTY, dataContext);
		return loadWithOptions(file, options);
	}

	/**
	 * Load the file content under a Composite. All widget will be created. This method returns the root element. The DataContext will be associated to the root element.
	 */
	static public synchronized Object load(Composite parent, URL file) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(IXWTLoader.CONTAINER_PROPERTY, parent);
		return loadWithOptions(file, options);
	}

	/**
	 * Load the file content under a Composite with a DataContext. All widget will be created. This method returns the root element. The DataContext will be associated to the root element.
	 */
	static public synchronized Object load(Composite parent, URL file, Object dataContext) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(IXWTLoader.CONTAINER_PROPERTY, parent);
		options.put(IXWTLoader.DATACONTEXT_PROPERTY, dataContext);
		return loadWithOptions(file, options);
	}

	/**
	 * Open and show the file content in a new Shell.
	 */
	static public synchronized void open(URL url) throws Exception {
		open(url, new HashMap<String, Object>());
	}

	/**
	 * load the content from a stream with a style, a DataContext and a ResourceDictionary. The root elements will be hold by Composite parent
	 */
	static public synchronized Object load(Composite parent, InputStream stream, URL file, Object dataContext) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(IXWTLoader.CONTAINER_PROPERTY, parent);
		options.put(IXWTLoader.DATACONTEXT_PROPERTY, dataContext);
		return loadWithOptions(stream, file, options);
	}

	/**
	 * load the file content. The corresponding UI element is not yet created
	 */
	static public synchronized void open(URL url, Object dataContext) throws Exception {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put(IXWTLoader.DATACONTEXT_PROPERTY, dataContext);
		open(url, options);
	}

	/**
	 * load the file content. The corresponding UI element is not yet created
	 */
	static public synchronized void open(Class<?> type, Object dataContext) throws Exception {
		open(type.getResource(type.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX), dataContext);
	}

	/**
	 * load the file content. The corresponding UI element is not yet created
	 */
	static public synchronized void open(URL url, Map<String, Object> options) throws Exception {
		boolean applied = applyFormsProfile();
		try {
			options.put(IXWTLoader.CREATED_CALLBACK, CreatedAction);
			XWT.open(url, options);
		} 
		finally {
			if (applied) {
				XWT.restoreProfile();
			}
		}
	}

	static public synchronized Object loadWithOptions(URL url, Map<String, Object> options) throws Exception {
		boolean applied = applyFormsProfile();
		try {
			options.put(IXWTLoader.CREATED_CALLBACK, CreatedAction);
			return XWT.loadWithOptions(url, options);
		} 
		finally {
			if (applied) {
				XWT.restoreProfile();
			}
		}
	}

	/**
	 * 
	 * @param stream
	 * @param url
	 * @param options
	 * @return
	 * @throws Exception
	 */
	static public synchronized Object load(InputStream stream, URL url) throws Exception {
		return loadWithOptions(stream, url, new HashMap<String, Object>());
	}

	/**
	 * 
	 * @param stream
	 * @param url
	 * @param options
	 * @return
	 * @throws Exception
	 */
	static public synchronized Object loadWithOptions(InputStream stream, URL url, Map<String, Object> options) throws Exception {
		boolean applied = applyFormsProfile();
		try {
			options.put(IXWTLoader.CREATED_CALLBACK, CreatedAction);
			return XWT.loadWithOptions(stream, url, options);
		} 
		finally {
			if (applied) {
				XWT.restoreProfile();
			}
		}
	}
}
