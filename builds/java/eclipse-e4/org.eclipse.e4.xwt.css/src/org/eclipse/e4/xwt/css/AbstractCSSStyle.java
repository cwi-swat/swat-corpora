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
package org.eclipse.e4.xwt.css;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.e4.xwt.IStyle;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Base class for CSS Style.
 * 
 * @author yyang
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 */
public abstract class AbstractCSSStyle implements IStyle, CSSErrorHandler {
	protected URL url;
	protected String content;

	private CSSEngine engine;
	private Display display;

	private Class<?> jfaceViewerClass;
	private Method getControl;

	public AbstractCSSStyle() {
		this((String) null);
	}

	public AbstractCSSStyle(URL url) {
		this.url = url;
		init();
	}

	public AbstractCSSStyle(String content) {
		this.content = content;
		init();
	}

	public AbstractCSSStyle(CSSEngine engine) {
		this.engine = engine;
		init();
	}

	/**
	 * Initialize class and method of JFace Viewer.
	 * 
	 */
	private void init() {
		try {
			// FIXME : Yves, jfaceViewerClass and getControl, can be cached, or
			// is it OSGI constraint?
			jfaceViewerClass = Class
					.forName("org.eclipse.jface.viewers.Viewer"); //$NON-NLS-1$
			getControl = jfaceViewerClass.getMethod("getControl");
		} catch (Throwable e) {
		}
	}

	public void initializeIfNeed(Control control) {
		Display display = control.getDisplay();
		if (this.display != null && this.display == display) {
			// CSS engine was already initalized, return.
			return;

		}
		this.display = display;
		try {
			// Instantiate SWT CSS Engine
			if (engine == null) {
				// Search engine into Shell
				engine = CSSXWT.getCSSEngine(control);
				if (engine == null) {
					// Create it and register it into Shell
					engine = createCSSEngine(display);
					CSSXWT.setCSSEngine(control.getShell(), engine);
				}
			}
			engine.setErrorHandler(this);

			// Load style sheet content
			if (content != null) {
				// Style sheet come from String content
				engine.parseStyleSheet(new StringReader(content));
			} else {
				// Style sheet come from URL

				// Get URL
				Method urlResolver = null;
				try {
					Class<?> fileLocatorClass = loadClass("org.eclipse.core.runtime.FileLocator"); //$NON-NLS-1$
					urlResolver = fileLocatorClass.getMethod(
							"resolve", new Class[] { URL.class }); //$NON-NLS-1$
				} catch (Throwable e) {
				}

				URL contentURL = url;
				if (urlResolver != null) {
					try {
						contentURL = (URL) urlResolver.invoke(null,
								new Object[] { contentURL });
					} catch (Throwable e) {

					}
				}

				// Parse style sheet
				InputStream stream = contentURL.openStream();
				engine.parseStyleSheet(stream);
				stream.close();
			}
		} catch (Throwable e) {
			System.err
					.println("Warning - could not initialize CSS styling : " + e.toString()); //$NON-NLS-1$
		}
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		if (this.url == url || (this.url != null && this.url.equals(url))) {
			return;
		}
		this.url = url;
		reset();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (this.content == content
				|| (this.content != null && this.content.equals(content))) {
			return;
		}
		this.content = content;
		reset();
	}

	protected void reset() {
		display = null;
		if (engine != null) {
			engine.reset();
			engine = null;
		}
	}

	public void applyStyle(Object target) {
		if (url == null && content == null && engine == null) {
			return;
		}

		String name = XWT.getElementName(target);
		Control control = null;
		if (target instanceof Control) {
			control = (Control) target;
		} else if (getControl != null && jfaceViewerClass.isInstance(target)) {
			try {
				control = (Control) getControl.invoke(target);
			} catch (Throwable e) {
				throw new XWTException(e);
			}
		}
		if (control != null) {
			initializeIfNeed(control);
			if (name != null) {
				control.setData("org.eclipse.e4.ui.css.id", name);
			}
			try {
				engine.applyStyles(control, false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public CSSEngine getEngine() {
		return engine;
	}

	public void error(Exception e) {
		throw new XWTException(e);
	}

	protected Class<?> loadClass(String className)
			throws ClassNotFoundException {
		try {
			return Class.forName(className); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			return Thread.currentThread().getContextClassLoader().loadClass(
					className);
		}
	}

	/**
	 * Create CSS engine from {@link Display}.
	 * 
	 * @param display
	 * @return
	 */
	protected abstract CSSEngine createCSSEngine(Display display);
}
