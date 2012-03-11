/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import java.util.HashMap;

/** 
 * A registry for AS keywords that are not Java keywords, excluding a few that
 * do not seem to cause grief. 
 */ 
public class KeywordsRegistry {
	public static final HashMap KEYWORDS = new HashMap();
	static {
		KEYWORDS.put("cast", "cast" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("debugger", "debugger" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("delete", "delete" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("dynamic", "dynamic" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("each", "each" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("export", "export" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("function", "function" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("get", "get" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("in", "in" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("include", "include" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("internal", "internal" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("intrinsic", "intrinsic" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("is", "is" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("namespace", "namespace" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("override", "override" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("prototype", "prototype" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("set", "set" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("staticas", "staticas" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("to", "to" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
//		KEYWORDS.put("type", "type" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
// 		type is a future reserved word, and we use it in native code, hence we won't exclude it for now
		KEYWORDS.put("typeof", "typeof" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("use", "use" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("var", "var" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("virtual", "virtual" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
		KEYWORDS.put("with", "with" /* non null */); //$NON-NLS-1$ //$NON-NLS-2$
	}
}