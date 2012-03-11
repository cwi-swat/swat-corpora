/*******************************************************************************
 * Copyright (c) 2007-2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import java.io.PrintWriter;

import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.core.util.CommentRecorderParser;

public class ASCompiler extends org.eclipse.jdt.internal.compiler.Compiler {
	
	public IActionScriptSourcesCollector actionScriptSourceFilesCollector;
	private IErrorHandlingPolicy policy;
	private IProblemFactory problemFactory;
	private FieldNamesRegistry fieldNamesRegistry = new FieldNamesRegistry();
	public ASCompiler(
			INameEnvironment environment,
			IErrorHandlingPolicy policy,
			ASCompilerOptions options,
			final ICompilerRequestor requestor,
			final IActionScriptSourcesCollector actionScriptSourceFilesCollector,
			IProblemFactory problemFactory,
			PrintWriter out) {
		super(environment, policy, options, requestor, problemFactory, out, null /* progress */);
		this.actionScriptSourceFilesCollector = actionScriptSourceFilesCollector;
		this.policy = policy;
		this.problemFactory = problemFactory;
		initializeParser();
	}
	
	public void initializeParser() {
		if (this.policy != null) { // skip calls that happen before we are ready, especially from super constructor
			this.parser = new CommentRecorderParser(this.problemReporter = new ProblemReporter(this.policy, this.options, this.problemFactory, this.fieldNamesRegistry), this.options.parseLiteralExpressionsAsConstants);
		}
	}
	
	public void process(CompilationUnitDeclaration unit, int i) {
		super.process(unit, i);
		if (! unit.hasErrors()) {
			ActionScriptSourceGenerator visitor = new ActionScriptSourceGenerator(this.actionScriptSourceFilesCollector, this.fieldNamesRegistry);
			// PREMATURE allocate an ActionScriptSourceGenerator when we're done
			unit.traverse(visitor, unit.scope);
		}
	}
}