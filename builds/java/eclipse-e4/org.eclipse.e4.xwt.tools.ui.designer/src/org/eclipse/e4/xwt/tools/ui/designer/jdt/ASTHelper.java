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
package org.eclipse.e4.xwt.tools.ui.designer.jdt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.corext.codemanipulation.AddUnimplementedConstructorsOperation;
import org.eclipse.jdt.internal.corext.codemanipulation.AddUnimplementedMethodsOperation;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.text.edits.TextEdit;

public class ASTHelper {

	public static IType createType(IPackageFragment pack, String typeName, Collection<Class<?>> superInterfaces, Class<?> superClass) {
		try {
			IType createdType;
			ImportsManager imports;
			Set<String> existingImports;

			String lineDelimiter = StubUtility.getLineDelimiterUsed(pack.getJavaProject());

			String cuName = typeName + ".java";
			ICompilationUnit parentCU = pack.createCompilationUnit(cuName, "", false, null); //$NON-NLS-1$
			// create a working copy with a new owner

			parentCU.becomeWorkingCopy(null); // cu is now a (primary) working
			// copy

			IBuffer buffer = parentCU.getBuffer();

			String simpleTypeStub = constructSimpleTypeStub(typeName);
			String cuContent = constructCUContent(parentCU, simpleTypeStub, lineDelimiter);
			buffer.setContents(cuContent);

			CompilationUnit astRoot = createASTForImports(parentCU);
			existingImports = getExistingImports(astRoot);

			imports = new ImportsManager(astRoot);
			// add an import that will be removed again. Having this import
			// solves 14661
			imports.addImport(JavaModelUtil.concatenateName(pack.getElementName(), typeName));

			String typeContent = constructTypeStub(typeName, superInterfaces, superClass, parentCU, imports, lineDelimiter);
			int index = cuContent.lastIndexOf(simpleTypeStub);
			if (index == -1) {
				AbstractTypeDeclaration typeNode = (AbstractTypeDeclaration) astRoot.types().get(0);
				int start = ((ASTNode) typeNode.modifiers().get(0)).getStartPosition();
				int end = typeNode.getStartPosition() + typeNode.getLength();
				buffer.replace(start, end - start, typeContent);
			} else {
				buffer.replace(index, simpleTypeStub.length(), typeContent);
			}

			createdType = parentCU.getType(typeName);

			// add imports for superclass/interfaces, so types can be resolved
			// correctly

			ICompilationUnit cu = createdType.getCompilationUnit();

			imports.create(false, null);

			JavaModelUtil.reconcile(cu);

			// set up again
			astRoot = createASTForImports(imports.getCompilationUnit());
			imports = new ImportsManager(astRoot);

			createTypeMembers(createdType, imports);

			// add imports
			imports.create(false, null);

			removeUnusedImports(cu, existingImports, false);

			JavaModelUtil.reconcile(cu);

			format(createdType, lineDelimiter);

			return createdType;
		} catch (CoreException e) {
			XWTDesignerPlugin.logError(e);
			return null;
		}
	}

	/**
	 * @param type
	 * @param lineDelimiter
	 * @throws JavaModelException
	 */
	private static void format(IType type, String lineDelimiter) throws JavaModelException {
		ISourceRange range = type.getSourceRange();
		ICompilationUnit cu = type.getCompilationUnit();
		IBuffer buf = cu.getBuffer();
		String originalContent = buf.getText(range.getOffset(), range.getLength());

		String formattedContent = CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, originalContent, 0, lineDelimiter, type.getJavaProject());
		formattedContent = Strings.trimLeadingTabsAndSpaces(formattedContent);
		buf.replace(range.getOffset(), range.getLength(), formattedContent);
		cu.commitWorkingCopy(true, null);
	}

	public static IMethod createMethod(IType type, String methodName, Class<?> returnType, String contentReturnValue, List<Class<?>> arguments) {
		try {
			ICompilationUnit cu = type.getCompilationUnit();
			JavaModelUtil.reconcile(cu);
			CompilationUnit astUnit = createASTForImports(cu);
			ImportsManager imports = new ImportsManager(astUnit);
			Set existingImports = getExistingImports(astUnit);

			List<String> paraNames = new ArrayList<String>();
			if (arguments != null) {
				for (Class<?> arg : arguments) {
					paraNames.add(arg.getSimpleName());
					imports.addImport(arg.getName());
				}
			}

			String lineDelimiter = StubUtility.getLineDelimiterUsed(type.getJavaProject());
			StringBuilder contents = new StringBuilder();
			String comment = CodeGeneration.getMethodComment(type.getCompilationUnit(), type.getTypeQualifiedName('.'), methodName, paraNames.toArray(new String[paraNames.size()]), new String[0], Signature.createTypeSignature((returnType == null ? "void" : returnType.getSimpleName()), true), null, lineDelimiter); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (comment != null) {
				contents.append(comment);
				contents.append(lineDelimiter);
			}
			if (returnType != null) {
				imports.addImport(returnType.getName());
			}
			contents.append("public " + (returnType == null ? "void " : returnType.getSimpleName() + " ") + methodName + "("); //$NON-NLS-1$
			for (int i = 0; i < paraNames.size(); i++) {
				String arg = paraNames.get(i);
				if (i != 0) {
					contents.append(", ");
				}
				contents.append(arg + " " + Character.toLowerCase(arg.charAt(0)) + arg.substring(1));
			}
			contents.append(") {"); //$NON-NLS-1$
			contents.append(lineDelimiter);

			final String content = CodeGeneration.getMethodBodyContent(type.getCompilationUnit(), type.getTypeQualifiedName('.'), methodName, false, "", lineDelimiter); //$NON-NLS-1$ //$NON-NLS-2$
			if (content != null && content.length() != 0)
				contents.append("\t" + content);
			// nameMap.append("\n");
			if (returnType != null) {
				contents.append("\treturn " + contentReturnValue + ";");
				contents.append(lineDelimiter);
			}
			contents.append("}"); //$NON-NLS-1$

			IMethod method = type.createMethod(contents.toString(), null, true, null);

			// add imports
			imports.create(false, null);

			removeUnusedImports(cu, existingImports, true);

			JavaModelUtil.reconcile(cu);
			format(type, lineDelimiter);
			return method;
		} catch (CoreException e) {
		}
		return null;

	}

	protected static void createTypeMembers(IType type, ImportsManager imports) throws CoreException {
		createInheritedMethods(type, true, true, imports);
	}

	protected static IMethod[] createInheritedMethods(IType type, boolean doConstructors, boolean doUnimplementedMethods, ImportsManager imports) throws CoreException {
		final ICompilationUnit cu = type.getCompilationUnit();
		JavaModelUtil.reconcile(cu);
		IMethod[] typeMethods = type.getMethods();
		Set handleIds = new HashSet(typeMethods.length);
		for (int index = 0; index < typeMethods.length; index++)
			handleIds.add(typeMethods[index].getHandleIdentifier());
		ArrayList<IMethod> newMethods = new ArrayList<IMethod>();
		CodeGenerationSettings settings = JavaPreferencesSettings.getCodeGenerationSettings(type.getJavaProject());
		settings.createComments = false;
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setSource(cu);
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		final ITypeBinding binding = ASTNodes.getTypeBinding(unit, type);
		if (binding != null) {
			if (doUnimplementedMethods) {
				AddUnimplementedMethodsOperation operation = new AddUnimplementedMethodsOperation(unit, binding, null, -1, false, true, false);
				operation.setCreateComments(false);
				operation.run(null);
				createImports(imports, operation.getCreatedImports());
			}
			if (doConstructors) {
				AddUnimplementedConstructorsOperation operation = new AddUnimplementedConstructorsOperation(unit, binding, null, -1, false, true, false);
				operation.setOmitSuper(true);
				operation.setCreateComments(false);
				operation.run(null);
				createImports(imports, operation.getCreatedImports());
			}
		}
		JavaModelUtil.reconcile(cu);
		typeMethods = type.getMethods();
		for (int index = 0; index < typeMethods.length; index++)
			if (!handleIds.contains(typeMethods[index].getHandleIdentifier()))
				newMethods.add(typeMethods[index]);
		IMethod[] methods = new IMethod[newMethods.size()];
		newMethods.toArray(methods);
		return methods;
	}

	private static void createImports(ImportsManager imports, String[] createdImports) {
		for (int index = 0; index < createdImports.length; index++)
			imports.addImport(createdImports[index]);
	}

	public static void removeUnusedImports(IType type) {
		try {
			ICompilationUnit cu = type.getCompilationUnit();
			CompilationUnit astRoot = ASTHelper.createASTForImports(cu);
			Set existingImports = ASTHelper.getExistingImports(astRoot);
			ASTHelper.removeUnusedImports(cu, existingImports, true);
		} catch (CoreException e) {
		}
	}

	private static void removeUnusedImports(ICompilationUnit cu, Set existingImports, boolean needsSave) throws CoreException {
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

	private static Set /* String */getExistingImports(CompilationUnit root) {
		List imports = root.imports();
		Set res = new HashSet(imports.size());
		for (int i = 0; i < imports.size(); i++) {
			res.add(ASTNodes.asString((ImportDeclaration) imports.get(i)));
		}
		return res;
	}

	private static CompilationUnit createASTForImports(ICompilationUnit cu) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		parser.setResolveBindings(false);
		parser.setFocalPosition(0);
		return (CompilationUnit) parser.createAST(null);
	}

	protected static String constructCUContent(ICompilationUnit cu, String typeContent, String lineDelimiter) throws CoreException {
		String fileComment = "";
		String typeComment = "";
		IPackageFragment pack = (IPackageFragment) cu.getParent();
		String content = CodeGeneration.getCompilationUnitContent(cu, fileComment, typeComment, typeContent, lineDelimiter);
		if (content != null) {
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setProject(cu.getJavaProject());
			parser.setSource(content.toCharArray());
			CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			if ((pack.isDefaultPackage() || unit.getPackage() != null) && !unit.types().isEmpty()) {
				return content;
			}
		}
		StringBuffer buf = new StringBuffer();
		if (!pack.isDefaultPackage()) {
			buf.append("package ").append(pack.getElementName()).append(';'); //$NON-NLS-1$
		}
		buf.append(lineDelimiter).append(lineDelimiter);
		if (typeComment != null) {
			buf.append(typeComment).append(lineDelimiter);
		}
		buf.append(typeContent);
		return buf.toString();
	}

	/*
	 * Called from createType to construct the source for this type
	 */
	private static String constructTypeStub(String typeName, Collection<Class<?>> superInterfaces, Class<?> superClass, ICompilationUnit parentCU, ImportsManager imports, String lineDelimiter) throws CoreException {
		StringBuffer buf = new StringBuffer();

		int modifiers = Flags.AccPublic;
		buf.append(Flags.toString(modifiers));
		if (modifiers != 0) {
			buf.append(' ');
		}
		String type = "class ";//$NON-NLS-1$
		String templateID = CodeGeneration.CLASS_BODY_TEMPLATE_ID;
		buf.append(type);
		buf.append(typeName);
		writeSuperClass(buf, imports, superClass);
		writeSuperInterfaces(buf, imports, superInterfaces);

		buf.append(" {").append(lineDelimiter); //$NON-NLS-1$
		String typeBody = CodeGeneration.getTypeBody(templateID, parentCU, typeName, lineDelimiter);
		if (typeBody != null) {
			buf.append(typeBody);
		} else {
			buf.append(lineDelimiter);
		}
		buf.append('}').append(lineDelimiter);
		return buf.toString();
	}

	private static void writeSuperClass(StringBuffer buf, ImportsManager imports, Class<?> superType) {
		if (superType == null) {
			return;
		}
		String superclass = superType.getName();
		buf.append(" extends "); //$NON-NLS-1$
		buf.append(imports.addImport(superclass));
	}

	private static void writeSuperInterfaces(StringBuffer buf, ImportsManager imports, Collection<Class<?>> superInterfaces) {
		if (superInterfaces == null || superInterfaces.isEmpty()) {
			return;
		}
		List<String> interfaces = new ArrayList<String>();
		for (Class<?> t : superInterfaces) {
			String name = t.getName();
			interfaces.add(name);
			imports.addImport(name);
		}
		int last = interfaces.size() - 1;
		if (last >= 0) {
			buf.append(" implements "); //$NON-NLS-1$
			String[] intfs = (String[]) interfaces.toArray(new String[interfaces.size()]);
			ITypeBinding[] bindings;
			bindings = new ITypeBinding[intfs.length];
			for (int i = 0; i <= last; i++) {
				ITypeBinding binding = bindings[i];
				if (binding != null) {
					buf.append(imports.addImport(binding));
				} else {
					buf.append(imports.addImport(intfs[i]));
				}
				if (i < last) {
					buf.append(',');
				}
			}
		}
	}

	private static String constructSimpleTypeStub(String typeName) {
		StringBuffer buf = new StringBuffer("public class "); //$NON-NLS-1$
		buf.append(typeName);
		buf.append("{ }"); //$NON-NLS-1$
		return buf.toString();
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

	/**
	 * Class used in stub creation routines to add needed imports to a compilation unit.
	 */
	public static class ImportsManager {

		private ImportRewrite fImportsRewrite;

		public ImportsManager(CompilationUnit astRoot) {
			fImportsRewrite = CodeStyleConfiguration.createImportRewrite(astRoot, true);
		}

		public ICompilationUnit getCompilationUnit() {
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

		public void create(boolean needsSave, IProgressMonitor monitor) throws CoreException {
			TextEdit edit = fImportsRewrite.rewriteImports(monitor);
			JavaModelUtil.applyEdit(fImportsRewrite.getCompilationUnit(), edit, needsSave, null);
		}

		public void removeImport(String qualifiedName) {
			fImportsRewrite.removeImport(qualifiedName);
		}

		public void removeStaticImport(String qualifiedName) {
			fImportsRewrite.removeStaticImport(qualifiedName);
		}
	}
}
