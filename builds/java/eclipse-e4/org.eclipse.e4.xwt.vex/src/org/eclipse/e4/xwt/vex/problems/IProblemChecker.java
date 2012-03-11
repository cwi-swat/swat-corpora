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
package org.eclipse.e4.xwt.vex.problems;

import java.util.Collections;
import java.util.List;

import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @author jliu
 * 
 */
public interface IProblemChecker {

	List<Problem> EMPTY = Collections.emptyList();

	List<Problem> checkProblems(StructuredTextEditor textEditor, String javaClassName);

	boolean canChecked(StructuredTextEditor textEditor, String javaClassName);
}
