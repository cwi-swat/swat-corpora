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

import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.Binding;

// The intent of this class is to use emitted errors to supplement existing
// information on bindings and other internal structures. Was triggered by 
// the fact that neither field declarations nor field bindings remember field
// hiding issues.
public class ProblemReporter extends org.eclipse.jdt.internal.compiler.problem.ProblemReporter {
	private FieldNamesRegistry fieldNamesRegistry;
	public ProblemReporter(IErrorHandlingPolicy policy, CompilerOptions options, IProblemFactory problemFactory, FieldNamesRegistry fieldNamesRegistry) {
		super(policy, options, problemFactory);
		this.fieldNamesRegistry = fieldNamesRegistry;
	}
	public void fieldHiding(FieldDeclaration fieldDecl, Binding hiddenVariable) {
		super.fieldHiding(fieldDecl, hiddenVariable);
		this.fieldNamesRegistry.fieldHiding(fieldDecl.binding);
	}
}
