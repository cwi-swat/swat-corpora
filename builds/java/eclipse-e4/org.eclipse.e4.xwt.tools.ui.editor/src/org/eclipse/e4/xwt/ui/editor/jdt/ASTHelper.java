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
package org.eclipse.e4.xwt.ui.editor.jdt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.text.edits.TextEdit;

public class ASTHelper {
	public static class ImportsManager {

		private ImportRewrite fImportsRewrite;

		/* package */ImportsManager(CompilationUnit astRoot) throws CoreException {
			fImportsRewrite = CodeStyleConfiguration.createImportRewrite(astRoot, true);
		}

		/* package */ICompilationUnit getCompilationUnit() {
			return fImportsRewrite.getCompilationUnit();
		}

		/**
		 * Adds a new import declaration that is sorted in the existing imports. If an import already exists or the import would conflict with an import of an other type with the same simple name, the import is not added.
		 * 
		 * @param qualifiedTypeName
		 *            The fully qualified name of the type to import (dot separated).
		 * @return Returns the simple type name that can be used in the code or the fully qualified type name if an import conflict prevented the import.
		 */
		public String addImport(String qualifiedTypeName) {
			return fImportsRewrite.addImport(qualifiedTypeName);
		}

		/**
		 * Adds a new import declaration that is sorted in the existing imports. If an import already exists or the import would conflict with an import of an other type with the same simple name, the import is not added.
		 * 
		 * @param typeBinding
		 *            the binding of the type to import
		 * 
		 * @return Returns the simple type name that can be used in the code or the fully qualified type name if an import conflict prevented the import.
		 */
		public String addImport(ITypeBinding typeBinding) {
			return fImportsRewrite.addImport(typeBinding);
		}

		/**
		 * Adds a new import declaration for a static type that is sorted in the existing imports. If an import already exists or the import would conflict with an import of an other static import with the same simple name, the import is not added.
		 * 
		 * @param declaringTypeName
		 *            The qualified name of the static's member declaring type
		 * @param simpleName
		 *            the simple name of the member; either a field or a method name.
		 * @param isField
		 *            <code>true</code> specifies that the member is a field, <code>false</code> if it is a method.
		 * @return returns either the simple member name if the import was successful or else the qualified name if an import conflict prevented the import.
		 * 
		 * @since 3.2
		 */
		public String addStaticImport(String declaringTypeName, String simpleName, boolean isField) {
			return fImportsRewrite.addStaticImport(declaringTypeName, simpleName, isField);
		}

		/* package */void create(boolean needsSave, IProgressMonitor monitor) throws CoreException {
			TextEdit edit = fImportsRewrite.rewriteImports(monitor);
			JavaModelUtil.applyEdit(fImportsRewrite.getCompilationUnit(), edit, needsSave, null);
		}

		/* package */void removeImport(String qualifiedName) {
			fImportsRewrite.removeImport(qualifiedName);
		}

		/* package */void removeStaticImport(String qualifiedName) {
			fImportsRewrite.removeStaticImport(qualifiedName);
		}
	}

	public static void generateEventHandler(final IType type, final String argumentType, final String handleName) throws Exception {
		final ASTHelper helper = new ASTHelper();
		Runnable typeRunner = new Runnable() {
			public void run() {
				try {
					helper.createEventHandlers(type, handleName, argumentType);
				} catch (CoreException e) {
				}
			}
		};
		helper.generateTypeMembers(type, argumentType, true, typeRunner);
	}

	public static void generateInitialization(final IType type, final String method, final Set<String> fieldNames) throws Exception {
		final ASTHelper helper = new ASTHelper();
		Runnable typeRunner = new Runnable() {
			public void run() {
				try {
					helper.createInitializeMembers(type, method, fieldNames);
				} catch (CoreException e) {
				}
			}
		};
		helper.generateTypeMembers(type, null, true, typeRunner);
	}

	public void generateTypeMembers(IType type, String argumentType, boolean needsSave, Runnable typeRunner) throws Exception {
		int indent = 0;
		IProgressMonitor monitor = new NullProgressMonitor();

		Set /* String (import names) */existingImports;

		String lineDelimiter = StubUtility.getLineDelimiterUsed(type.getJavaProject());
		ICompilationUnit cu = type.getCompilationUnit();
		// create a working copy with a new owner

		cu.becomeWorkingCopy(monitor);

		CompilationUnit astRoot = createASTForImports(cu);
		existingImports = getExistingImports(astRoot);

		ImportsManager imports = new ImportsManager(astRoot);
		if (argumentType != null) {
			imports.addImport(argumentType);
		}
		typeRunner.run();
		// add imports
		imports.create(false, monitor);

		removeUnusedImports(cu, existingImports, false);

		JavaModelUtil.reconcile(cu);

		ISourceRange range = type.getSourceRange();

		IBuffer buf = cu.getBuffer();
		String originalContent = buf.getText(range.getOffset(), range.getLength());

		String formattedContent = CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, indent, lineDelimiter, type.getJavaProject());
		formattedContent = Strings.trimLeadingTabsAndSpaces(formattedContent);
		buf.replace(range.getOffset(), range.getLength(), formattedContent);

		if (needsSave) {
			cu.commitWorkingCopy(true, new SubProgressMonitor(monitor, 1));
		} else {
			monitor.worked(1);
		}
	}

	private CompilationUnit createASTForImports(ICompilationUnit cu) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		parser.setResolveBindings(false);
		parser.setFocalPosition(0);
		return (CompilationUnit) parser.createAST(null);
	}

	private Set /* String */getExistingImports(CompilationUnit root) {
		List imports = root.imports();
		Set res = new HashSet(imports.size());
		for (int i = 0; i < imports.size(); i++) {
			res.add(ASTNodes.asString((ImportDeclaration) imports.get(i)));
		}
		return res;
	}

	public static void removeUnusedImports(IType type) {
		try {
			ASTHelper helper = new ASTHelper();
			ICompilationUnit cu = type.getCompilationUnit();
			CompilationUnit astRoot = helper.createASTForImports(cu);
			Set existingImports = helper.getExistingImports(astRoot);
			helper.removeUnusedImports(cu, existingImports, true);
		} catch (CoreException e) {
		}
	}

	private void removeUnusedImports(ICompilationUnit cu, Set existingImports, boolean needsSave) throws CoreException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		parser.setResolveBindings(true);

		CompilationUnit root = (CompilationUnit) parser.createAST(null);
		if (root.getProblems().length == 0) {
			return;
		}

		List importsDecls = root.imports();
		if (importsDecls.isEmpty()) {
			return;
		}
		ImportsManager imports = new ImportsManager(root);

		int importsEnd = ASTNodes.getExclusiveEnd((ASTNode) importsDecls.get(importsDecls.size() - 1));
		IProblem[] problems = root.getProblems();
		for (int i = 0; i < problems.length; i++) {
			IProblem curr = problems[i];
			if (curr.getSourceEnd() < importsEnd) {
				int id = curr.getID();
				if (id == IProblem.UnusedImport || id == IProblem.NotVisibleType) { // not visible
					// problems hide
					// unused -> remove
					// both
					int pos = curr.getSourceStart();
					for (int k = 0; k < importsDecls.size(); k++) {
						ImportDeclaration decl = (ImportDeclaration) importsDecls.get(k);
						if (decl.getStartPosition() <= pos && pos < decl.getStartPosition() + decl.getLength()) {
							if (existingImports.isEmpty() || !existingImports.contains(ASTNodes.asString(decl))) {
								String name = decl.getName().getFullyQualifiedName();
								if (decl.isOnDemand()) {
									name += ".*"; //$NON-NLS-1$
								}
								if (decl.isStatic()) {
									imports.removeStaticImport(name);
								} else {
									imports.removeImport(name);
								}
							}
							break;
						}
					}
				}
			}
		}
		imports.create(needsSave, null);
	}

	protected void createEventHandlers(IType type, String method, String argumentType) throws CoreException {
		int lastIndexOf = argumentType.lastIndexOf(".");
		String shortArgTypeName = argumentType.substring(lastIndexOf + 1);
		StringBuffer buf = new StringBuffer();
		final String lineDelim = "\n"; // OK, since content is formatted
		// afterwards //$NON-NLS-1$
		String comment = CodeGeneration.getMethodComment(type.getCompilationUnit(), type.getTypeQualifiedName('.'), method, new String[] { "event" }, new String[0], Signature.createTypeSignature("void", true), null, lineDelim); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (comment != null) {
			buf.append(comment);
			buf.append(lineDelim);
		}
		buf.append("public void " + method + "( "); //$NON-NLS-1$
		buf.append(shortArgTypeName); //$NON-NLS-1$
		buf.append(" event) {"); //$NON-NLS-1$
		buf.append(lineDelim);

		final String content = CodeGeneration.getMethodBodyContent(type.getCompilationUnit(), type.getTypeQualifiedName('.'), method, false, "", lineDelim); //$NON-NLS-1$ //$NON-NLS-2$
		if (content != null && content.length() != 0)
			buf.append(content);
		buf.append(lineDelim);
		buf.append("}"); //$NON-NLS-1$
		type.createMethod(buf.toString(), null, false, null);
	}

	protected void createInitializeMembers(IType type, String method, Set<String> fieldNames) throws CoreException {
		IMethod init = null;
		IMethod[] methods = type.getMethods();
		for (IMethod method2 : methods) {
			if (method2.getElementName().equals(method)) {
				init = method2;
				break;
			}
		}
		if (init != null) {
			init.delete(false, null);
		}
		StringBuffer buf = new StringBuffer();
		final String lineDelim = "\n";
		String comment = "/**\n  * Generated Initialization.\n */";
		if (comment != null) {
			buf.append(comment);
			buf.append(lineDelim);
		}
		buf.append("protected void " + method + " () {"); //$NON-NLS-1$
		buf.append(lineDelim);

		if (fieldNames.isEmpty()) {
			final String content = CodeGeneration.getMethodBodyContent(type.getCompilationUnit(), type.getTypeQualifiedName('.'), method, false, "", lineDelim); //$NON-NLS-1$ //$NON-NLS-2$
			if (content != null && content.length() != 0) {
				buf.append(content);
			}
		}
		for (String fieidName : fieldNames) {
			IField field = type.getField(fieidName);
			if (field != null) {
				String initialize = fieidName + " = (" + Signature.toString(field.getTypeSignature()) + ") findElement(\"" + fieidName + "\");";
				buf.append(initialize);
			}
		}
		buf.append(lineDelim);
		buf.append("}"); //$NON-NLS-1$
		type.createMethod(buf.toString(), null, false, null);
	}

	public static void generateNamedFields(IType type, String fieldType, String fieldName) {
		try {
			ASTHelper helper = new ASTHelper();
			helper.createNamedFields(type, fieldType, fieldName, true);
		} catch (Exception e) {
		}
	}

	public void createNamedFields(IType type, String fieldType, String fieldName, boolean needSave) throws Exception {
		if (fieldType == null || fieldName == null) {
			return;
		}
		IProgressMonitor monitor = new NullProgressMonitor();

		Set /* String (import names) */existingImports;

		String lineDelimiter = StubUtility.getLineDelimiterUsed(type.getJavaProject());
		ICompilationUnit cu = type.getCompilationUnit();
		// create a working copy with a new owner

		cu.becomeWorkingCopy(monitor);

		CompilationUnit astRoot = createASTForImports(cu);
		existingImports = getExistingImports(astRoot);

		ImportsManager imports = new ImportsManager(astRoot);
		imports.addImport(fieldType);
		createField(type, fieldType, fieldName);

		// add imports
		imports.create(false, monitor);

		removeUnusedImports(cu, existingImports, false);

		JavaModelUtil.reconcile(cu);

		ISourceRange range = type.getSourceRange();

		IBuffer buf = cu.getBuffer();
		String originalContent = buf.getText(range.getOffset(), range.getLength());

		String formattedContent = CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, 0, lineDelimiter, type.getJavaProject());
		formattedContent = Strings.trimLeadingTabsAndSpaces(formattedContent);
		buf.replace(range.getOffset(), range.getLength(), formattedContent);

		if (needSave) {
			cu.commitWorkingCopy(true, new SubProgressMonitor(monitor, 1));
		} else {
			monitor.worked(1);
		}
	}

	private void createField(IType type, String paraType, String paraName) throws Exception {
		int lastIndexOf = paraType.lastIndexOf(".");
		String property = paraType.substring(lastIndexOf + 1);
		if (property == null || property.equals("")) {
			property = paraType;
		}
		String contents = ("protected " + property + " " + paraName + ";");
		type.createField(contents, null, false, null);
	}
}
