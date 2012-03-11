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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;

public class Main extends org.eclipse.jdt.internal.compiler.batch.Main {
	
ArrayList extraProblems;

public static void main(String[] argv) {
	try {
		new Main(new PrintWriter(System.out), new PrintWriter(System.err), true, null).compile(argv);
	} catch (Throwable t) {
		t.printStackTrace(System.err);
		System.exit(99);
	}
}

public Main(PrintWriter outWriter, PrintWriter errWriter,
		boolean systemExitWhenFinished, Map customDefaultOptions) {
	super(outWriter, errWriter, systemExitWhenFinished, customDefaultOptions, null /* progress */);
}

public void addExtraProblems(CategorizedProblem problem) {
	if (this.extraProblems == null) {
		this.extraProblems = new ArrayList();
	}
	this.extraProblems.add(problem);
}

public String extractDestinationPathFromSourceFile(String fileName, String typeName) {
	typeName = typeName.substring(0, typeName.length() - 3); // get rid of .as
	int lastIndex = fileName.indexOf(typeName);
	if (lastIndex == -1) {
		lastIndex = fileName.lastIndexOf(File.separatorChar);
	}
	if (lastIndex != -1) {
		final String outputPathName = fileName.substring(0, lastIndex);
		final File output = new File(outputPathName);
		if (output.exists() && output.isDirectory()) {
			return outputPathName;
		}
	}
	return System.getProperty("user.dir"); //$NON-NLS-1$
}

// PREMATURE could redefine less than that
public void performCompilation() {
	this.startTime = System.currentTimeMillis();

	FileSystem environment = getLibraryAccess();
	this.options.put(ASCompilerOptions.OPTION_ReportNonConstStaticFinalField, org.eclipse.jdt.internal.compiler.impl.CompilerOptions.IGNORE);
	this.compilerOptions = new ASCompilerOptions(this.options);
	this.compilerOptions.performMethodsFullRecovery = false;
	this.compilerOptions.performStatementsRecovery = false;
	final ArrayList actionScriptSourceFiles = new ArrayList();
	this.batchCompiler =
		new ASCompiler(
			environment,
			getHandlingPolicy(),
			(ASCompilerOptions) this.compilerOptions,
			getBatchRequestor(),
			new IActionScriptSourcesCollector () {
				public void accept(ActionScriptSourceFile actionScriptSourceFile) {
					if (actionScriptSourceFile != null) {
						actionScriptSourceFiles.add(actionScriptSourceFile);
					}
				}
			},
			getProblemFactory(),
			this.out);

	// set the non-externally configurable options.
	this.compilerOptions.verbose = this.verbose;
	this.compilerOptions.produceReferenceInfo = this.produceRefInfo;
	// TODO e4 need a cleaner solution
	String savedDestinationPath = this.destinationPath;
	try {
		this.destinationPath = NONE;
		this.logger.startLoggingSources();
		this.batchCompiler.compile(getCompilationUnits());
	} finally {
		this.logger.endLoggingSources();
		this.destinationPath = savedDestinationPath;
	}

	// index source files
	Map resolvedDestinationPaths = new Hashtable();
	for (int i = 0, l = this.filenames.length; i < l; i++) {
		if (this.destinationPaths[i] != null) {
			resolvedDestinationPaths.put(this.filenames[i], this.destinationPaths[i]);
		}
	}
	Iterator sourcesIterator = actionScriptSourceFiles.iterator();
	while (sourcesIterator.hasNext()) {
		ActionScriptSourceFile actionScriptSourceFile =
			((ActionScriptSourceFile) sourcesIterator.next());
		String sourceFile = actionScriptSourceFile.javaSourceFileName;
		String currentDestinationPath = (String) resolvedDestinationPaths.get(sourceFile);
		if (currentDestinationPath == null) {
			if (this.destinationPath == null) {
				currentDestinationPath =
					extractDestinationPathFromSourceFile(sourceFile, actionScriptSourceFile.name);
			} else if (this.destinationPath != NONE) {
				currentDestinationPath = this.destinationPath;
			} // else leave currentDestinationPath null
		}
		if (currentDestinationPath != NONE && 
				currentDestinationPath != null) {
			try {
				actionScriptSourceFile.writeToDisk(currentDestinationPath);
			} catch(IOException e) {
				this.logger.logException(e);
			}
		}
	}
	
	if (this.extraProblems != null) {
		this.logger.loggingExtraProblems(this);
		this.extraProblems = null;
	}
	this.logger.printStats();

	// cleanup
	environment.cleanup();
}
protected void disableWarnings() {
	super.disableWarnings();
	this.options.put(ASCompilerOptions.OPTION_DisableWarnings, org.eclipse.jdt.internal.compiler.impl.CompilerOptions.DISABLED);
}
}
