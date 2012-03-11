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

import java.lang.reflect.Constructor;
import java.net.URL;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.swt.widgets.Display;

/**
 * This class handles the CSS style for XWT element. It can be used in two ways:
 * <ol>
 * <ul>
 * 1. Global style<br/>
 * XWT.addDefaultStyle()
 * </ul>
 * <ul>
 * 2. Inline style
 * 
 * <pre>
 * &lt;Composite&gt;
 *   &lt;Composite.Resources&gt;
 *     &lt;CSSStyle x:Key=&quot;style&quot; url=&quot;/test/style.css&quot;/&gt;
 *   &lt;/Composite.Resources&gt;
 *   &lt;Label text=&quot;Hello&quot;/&gt;
 * &lt;/Composite&gt;
 * </pre>
 * 
 * </ul>
 * </ol>
 * 
 * @author yyang
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 */
public class CSSStyle extends AbstractCSSStyle {

	public CSSStyle() {
		super();
	}

	public CSSStyle(URL url) {
		super(url);
	}

	public CSSStyle(String content) {
		super(content);
	}

	public CSSStyle(CSSEngine engine) {
		super(engine);
	}

	protected CSSEngine createCSSEngine(Display display) {
		try {
			Class<?> engineClass = getEngineClass();
			Constructor<?> ctor = engineClass.getConstructor(new Class[] {
					Display.class, Boolean.TYPE });
			return (CSSEngine) ctor.newInstance(new Object[] { display,
					Boolean.FALSE });
		} catch (Throwable e) {
			throw new CSSEngineNotFoundException(e);
		}

	}

	private Class getEngineClass() throws ClassNotFoundException {
		Class engineClass = null;
		try {
			engineClass = loadClass("org.eclipse.e4.ui.css.nebula.engine.CSSNebulaEngineImpl"); //$NON-NLS-1$
		} catch (Throwable e) {
			engineClass = loadClass("org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl"); //$NON-NLS-1$
		}
		return engineClass;
	}
}
