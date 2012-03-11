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
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.BreakStatement;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ContinueStatement;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.EmptyStatement;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ExtendedStringLiteral;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.FloatLiteral;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PrefixExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedThisReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Reference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodVerifier;
import org.eclipse.jdt.internal.compiler.lookup.NestedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SignatureWrapper;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.SyntheticArgumentBinding;
import org.eclipse.jdt.internal.compiler.lookup.SyntheticMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

class ActionScriptSourceGenerator extends ActionScriptSourceGeneratorCatcher {
	
	private Stack actionScriptSourceFiles = new Stack();
	ActionScriptSourceFile actionScriptSourceFile = null;
	IActionScriptSourcesCollector actionScriptSourceFilesCollector;
	protected ActionScriptScribe scriptOutput = null;
	LocalNamesRegistry localNamesRegistry, compilationUnitLocalNamesRegistry;
	private Stack localNamesRegistries = new Stack();
	protected ImportRegistry importRegistry;
	private ImportReference currentPackage;
	private FieldNamesRegistry fieldNamesRegistry;

	int[] lineSeparators = null;
	int lineNumberStart;
	int lineNumberEnd;

//	PREMATURE reconsider: private TypeReference lastVariableTypeReference;
	protected Statement unIndentedStatement;

	static final double DOUBLE_MIN_VALUE = 1.0e-323; // PREMATURE conservative value, needs more work
	static final double DOUBLE_MAX_VALUE = 1.79769313486231e+308;
	static final long LONG_MIN_VALUE = -9007199254740992L;
	static final long LONG_MAX_VALUE = 9007199254740992L;
	int localDeclarationForStatus = NOT_IN_FOR;
	static final int 
			NOT_IN_FOR = -1,
			FIRST_IN_FOR = 0,
			NTH_IN_FOR = 1;
	private static final char[] ARG0 = "arg0".toCharArray(); //$NON-NLS-1$
	private static final char[] ARG1 = "arg1".toCharArray(); //$NON-NLS-1$
	private static final char[] TAB = "tab".toCharArray(); //$NON-NLS-1$
	private static final char[] TAB2 = "tab2".toCharArray(); //$NON-NLS-1$
	private static final String INTERFACE_FIELDS = "$$fields"; //$NON-NLS-1$

	public ActionScriptSourceGenerator(IActionScriptSourcesCollector actionScriptSourceFilesCollector, FieldNamesRegistry fieldNamesRegistry) {
		this.actionScriptSourceFilesCollector = actionScriptSourceFilesCollector;
		this.fieldNamesRegistry = fieldNamesRegistry;
	}
	boolean generatingConstructor;
	ActionScriptConstructorGenerator constructorGenerator;
	Stack constructorGenerators = new Stack();
	Statement lastStatement;
	
	private void appendMethod(AbstractMethodDeclaration methodDeclaration, ClassScope scope) {
		printIndent();
		if (methodDeclaration.selector == TypeConstants.CLINIT) {
			this.scriptOutput.append("static private function __clinit(): void"); //$NON-NLS-1$
			return;
		}
		MethodBinding method = methodDeclaration.binding;
		boolean inInterface = method.declaringClass.isInterface();
		int intrinsic = Util.checkIntrinsic(method);
		if (intrinsic == Util.ERROR) {
			reportError(scope, "conflicting intrinsic/non intrinsic declarations for method " + new String(method.readableName()) + " in superinterfaces", methodDeclaration);  //$NON-NLS-1$//$NON-NLS-2$
		}
		String nativeSource = null;
		if (inInterface) {
			if (method.isOverriding() || method.isImplementing()) {
				this.scriptOutput.append("// skip overriding "); //$NON-NLS-1$
				this.scriptOutput.append(methodDeclaration.selector);
				this.scriptOutput.appendNewLine();
				return;
			}
		} else {
			if (method.isPublic() || intrinsic != Util.IS_INTRINSIC && method.isProtected() || 
					method.isConstructor() && method.declaringClass.isNestedType()) {
				this.scriptOutput.append("public ");//$NON-NLS-1$
			} else if (method.isProtected()) {
				this.scriptOutput.append("protected ");//$NON-NLS-1$
			} else if (!this.generatingConstructor) {
				if (method.isPrivate()) {
					this.scriptOutput.append("private ");//$NON-NLS-1$
				} else  {
					this.scriptOutput.append("public ");  //$NON-NLS-1$
				}
			}
			if (method.isNative()) {
				CompilationUnitDeclaration unit = scope.referenceCompilationUnit();
				int methodStart = methodDeclaration.declarationSourceStart;
				int methodEnd = methodDeclaration.declarationSourceEnd;
				for (int i = 0, l = unit.comments.length; i < l; i++) {
					int commentEnd = -unit.comments[i][1];
					if (commentEnd < 0) continue; // javadoc comment
					int commentStart = unit.comments[i][0];
					if (commentStart > methodStart && commentEnd <= methodEnd) {
						char[] source = unit.compilationResult.compilationUnit.getContents();
						// /*-( or /*{  represent the beginning of code we want to append
						if (commentEnd - commentStart < 7) break;
						if (source[commentStart] != '/' || source[commentStart + 1] != '*') break;
						if (source[commentStart + 2] == '{') {
							if (source[commentEnd - 1] != '/' || source[commentEnd - 2] != '*' || source[commentEnd - 3] != '}')
								break;
							// found /*{ to start and }*/ at end
							nativeSource = String.copyValueOf(source, commentStart + 3, commentEnd - commentStart - 6);
						} else if (source[commentStart + 2] == '-' && source[commentStart + 3] == '{') {
							if (source[commentEnd - 1] != '/' || source[commentEnd - 2] != '*' || source[commentEnd - 3] != '-' || source[commentEnd - 4] != '}')
								break;
							// found /*-( to start and )-*/ at end
							nativeSource = String.copyValueOf(source, commentStart + 4, commentEnd - commentStart - 8);
						} else {
							break;
						}
						break;
					}
				}
			}
			if (method.isStatic()) {
				this.scriptOutput.append("static ");//$NON-NLS-1$
			} else if (Util.isOverriding(method)) {
				this.scriptOutput.append("override "); //$NON-NLS-1$
			}
		}
		this.scriptOutput.append("function ");//$NON-NLS-1$
		switch (Util.checkIntrinsic(method)) {
			case Util.IS_INTRINSIC:
				Util.appendMethodName(this.scriptOutput, method, true, this.generatingConstructor);
				break;
			case Util.IS_NOT_INTRINSIC:
				Util.appendMethodName(this.scriptOutput, method, false, this.generatingConstructor);
				break;
		}
		this.scriptOutput.append("(");//$NON-NLS-1$
		boolean needExtraComma = false;
		if (method.isConstructor()) {
			if (method.declaringClass.isNestedType()) {
				NestedTypeBinding nestedType = (NestedTypeBinding) method.declaringClass; 
				SyntheticArgumentBinding[] syntheticArgs = nestedType.syntheticEnclosingInstances();
				SyntheticArgumentBinding syntheticArgument;
				int syntheticArgsCount = syntheticArgs == null ? 0 : syntheticArgs.length;
				if (syntheticArgsCount > 0) {
					for (int i = 0; i < syntheticArgsCount; i++) {
						syntheticArgument = syntheticArgs[i];
						expandMethodArgument(syntheticArgument.name, syntheticArgument.type, i > 0);
					}
					needExtraComma = true;
				}
				syntheticArgs = nestedType.syntheticOuterLocalVariables();
				syntheticArgsCount = syntheticArgs == null ? 0 : syntheticArgs.length;
				if (syntheticArgsCount > 0) {
					for (int i = 0; i < syntheticArgsCount; i++) {
						syntheticArgument = syntheticArgs[i];
						expandMethodArgument(syntheticArgument.name, syntheticArgument.type, needExtraComma || i > 0);
					}
					needExtraComma = true;
				}
			}
			if (method.declaringClass.isEnum()) {
				this.localNamesRegistry.addTargetName(ARG0);
				expandMethodArgument(ARG0, scope.getJavaLangString(), needExtraComma);
				needExtraComma = true;
				this.localNamesRegistry.addTargetName(ARG1);
				expandMethodArgument(ARG1, TypeBinding.INT, needExtraComma);
				needExtraComma = true;
			}
		}
		int argumentsCount = methodDeclaration.arguments == null ? 0 : methodDeclaration.arguments.length;
		for (int i = 0; i < argumentsCount; i++) {
			Argument argument = methodDeclaration.arguments[i];
			expandMethodArgument(
					this.localNamesRegistry.targetName(argument.binding).toCharArray(),
					argument.binding.type, needExtraComma || i > 0);
		}
		if (method.isConstructor()) {
			if (Util.checkIntrinsic(method) != Util.IS_NOT_INTRINSIC) {
				reportError(scope, "non IS_NOT_INTRINSIC constructor" , methodDeclaration);//$NON-NLS-1$
				return;
			}
			this.scriptOutput.append(')');
			if (!this.generatingConstructor) {
				this.scriptOutput.append(": ").append(TypeConstants.OBJECT);//$NON-NLS-1$
			}
		} else {
			this.scriptOutput.append("): ");//$NON-NLS-1$
			// try to retrieve the corresponding bridge synthetic method if any
			MethodBinding highestOverridenMethod = null;
			if (method.parameters.length == 0
					&& (method.isOverriding() || method.isImplementing())) {
				highestOverridenMethod = getHighestOverridenMethod(method, scope.environment()).original();
			}
			MethodBinding original = method.original();
			if (highestOverridenMethod != null && highestOverridenMethod != original && highestOverridenMethod.returnType != original.returnType) {
				Util.appendTypeName(this.scriptOutput, highestOverridenMethod.returnType, this.importRegistry.isAmbiguous(highestOverridenMethod.returnType), true);
			} else {
				Util.appendTypeName(this.scriptOutput, method.returnType, this.importRegistry.isAmbiguous(method.returnType), true);
			}
		}
		if (method.isNative()) {
			if (nativeSource != null) {
				this.scriptOutput.append(' ');
				this.scriptOutput.append('{');
				this.scriptOutput.appendNewLine();
				String convertInlinedJavaCalls = convertInlinedJavaCalls(nativeSource, scope);
				appendNativeMethodBody(convertInlinedJavaCalls);
				this.scriptOutput.appendNewLine();
				this.scriptOutput.printIndent();
				this.scriptOutput.append('}');
				this.scriptOutput.appendNewLine();
			} else {
				this.scriptOutput.append(" { // native").appendNewLine(); //$NON-NLS-1$
				indent();
				printIndent();
				switch (method.returnType.id) {
					case TypeIds.T_boolean:
						this.scriptOutput.append("return false;").appendNewLine();//$NON-NLS-1$
						break;
					case TypeIds.T_double:
					case TypeIds.T_float:
						this.scriptOutput.append("return 0.0;").appendNewLine();//$NON-NLS-1$
						break;
					case TypeIds.T_byte:
					case TypeIds.T_short:
					case TypeIds.T_char:
					case TypeIds.T_int:
					case TypeIds.T_long:
						this.scriptOutput.append("return 0;").appendNewLine();//$NON-NLS-1$
						break;
					case TypeIds.T_void:
						this.scriptOutput.append("return;").appendNewLine();//$NON-NLS-1$
						break;
					default:
						this.scriptOutput.append("return null;").appendNewLine();//$NON-NLS-1$
						break;
				}
				outdent();
				printIndent();
				this.scriptOutput.append('}').appendNewLine();
			}
		} else if (methodDeclaration.isAbstract() || (methodDeclaration.modifiers & ExtraCompilerModifiers.AccSemicolonBody) != 0) {
			if (inInterface) {
				this.scriptOutput.append(';').appendNewLine();
			} else if (method.returnType.id == TypeIds.T_void) {
				this.scriptOutput.append(" { } // abstract").appendNewLine();//$NON-NLS-1$
			} else {
				this.scriptOutput.append(" { // abstract").appendNewLine();//$NON-NLS-1$
				indent();
				printIndent();
				switch (method.returnType.id) {
					case TypeIds.T_boolean:
						this.scriptOutput.append("return false;").appendNewLine();//$NON-NLS-1$
						break;
					case TypeIds.T_double:
					case TypeIds.T_float:
						this.scriptOutput.append("return 0.0;").appendNewLine();//$NON-NLS-1$
						break;
					case TypeIds.T_byte:
					case TypeIds.T_short:
					case TypeIds.T_char:
					case TypeIds.T_int:
					case TypeIds.T_long:
						this.scriptOutput.append("return 0;").appendNewLine();//$NON-NLS-1$
						break;
					default:
						this.scriptOutput.append("return null;").appendNewLine();//$NON-NLS-1$
						break;
				}
				outdent();
				printIndent();
				this.scriptOutput.append('}').appendNewLine();
			}
		}
	}

	private void appendNativeMethodBody(String convertInlinedJavaCalls) {
		LineNumberReader reader = null;
		this.scriptOutput.indent();
		try {
			reader = new LineNumberReader(new StringReader(convertInlinedJavaCalls.trim()));
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				if (i > 0) {
					this.scriptOutput.appendNewLine();
				}
				this.scriptOutput.printIndent();
				this.scriptOutput.append(line.trim());
				i++;
			}
		} catch (IOException e) {
			// ignore
		} finally {
			this.scriptOutput.outdent();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	void appendMethod(MethodBinding method, ClassScope scope) {
		printIndent();
		this.scriptOutput.append("public ");//$NON-NLS-1$
		if (Util.isOverriding(method)) {
			this.scriptOutput.append("override "); //$NON-NLS-1$
		}
		this.scriptOutput.append("function ");//$NON-NLS-1$
		switch (Util.checkIntrinsic(method)) {
			case Util.IS_INTRINSIC:
				Util.appendMethodName(this.scriptOutput, method, true, this.generatingConstructor);
				break;
			case Util.IS_NOT_INTRINSIC:
				Util.appendMethodName(this.scriptOutput, method, false, this.generatingConstructor);
				break;
			case Util.ERROR:
				reportError(scope, "conflicting intrinsic/non intrinsic declarations for method " + new String(method.readableName()) + " in superinterfaces", scope.referenceContext);  //$NON-NLS-1$//$NON-NLS-2$
		}
		int argumentsCount = method.parameters == null ? 0 : method.parameters.length;
		this.scriptOutput.append("(");//$NON-NLS-1$
		for (int i = 0; i < argumentsCount; i++) {
			TypeBinding argument = method.parameters[i];
			if (i > 0) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
			}
			this.scriptOutput.append("arg"); //$NON-NLS-1$
			this.scriptOutput.append(i);
			this.scriptOutput.append(": ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, argument, this.importRegistry.isAmbiguous(argument), true);
		}
		this.scriptOutput.append("): ");//$NON-NLS-1$
		Util.appendTypeName(this.scriptOutput, method.returnType, this.importRegistry.isAmbiguous(method.returnType), true);
		if (method.returnType.id == TypeIds.T_void) {
			this.scriptOutput.append(" { } // abstract").appendNewLine();//$NON-NLS-1$
		} else {
			this.scriptOutput.append(" { // abstract").appendNewLine();//$NON-NLS-1$
			indent();
			printIndent();
			switch (method.returnType.id) {
				case TypeIds.T_boolean:
					this.scriptOutput.append("return false;").appendNewLine();//$NON-NLS-1$
					break;
				case TypeIds.T_double:
				case TypeIds.T_float:
					this.scriptOutput.append("return 0.0;").appendNewLine();//$NON-NLS-1$
					break;
				case TypeIds.T_byte:
				case TypeIds.T_short:
				case TypeIds.T_char:
				case TypeIds.T_int:
				case TypeIds.T_long:
					this.scriptOutput.append("return 0;").appendNewLine();//$NON-NLS-1$
					break;
				default:
					this.scriptOutput.append("return null;").appendNewLine();//$NON-NLS-1$
					break;
			}
			outdent();
			printIndent();
			this.scriptOutput.append('}').appendNewLine();
		}
	}

	private String convertInlinedJavaCalls(String nativeSource, Scope scope) {
		// replace each inlined call of the form @p1.p2.X::fieldName; or @p1.p2.X::methodSignature(Ljava.lang.String;)("hello");
		while (true) {
			int start = nativeSource.indexOf('@');
			if (start == -1) return nativeSource; // no more inlined calls
	
			int doubleColon = nativeSource.indexOf("::", start + 1); //$NON-NLS-1$
			if (doubleColon == -1) return nativeSource; // did not find ::
			String typeName = nativeSource.substring(start + 1, doubleColon).replace('$', '.');
			Binding binding = scope.getTypeOrPackage(CharOperation.splitOn('.', typeName.toCharArray()));
			if (!binding.isValidBinding() || !(binding instanceof ReferenceBinding)) return nativeSource;
			ReferenceBinding declaringClass = (ReferenceBinding) binding;
			String newTypeName = Util.typeName(declaringClass, true);
	
			int length = nativeSource.length();
			int memberStart = doubleColon + 2;
			int memberNameEnd = memberStart;
			while (memberNameEnd < length && Character.isJavaIdentifierPart(nativeSource.charAt(memberNameEnd)))
				memberNameEnd++;
			int openingBracket = nativeSource.indexOf('(', memberStart);
			boolean isFieldRef = openingBracket == -1 || openingBracket > memberNameEnd;
			StringBuffer replacement = new StringBuffer(length);
			replacement.append(nativeSource.substring(0, start));
			if (isFieldRef) {
				String signature = nativeSource.substring(memberStart, memberNameEnd);
				// replace signature with registered fieldName
				FieldBinding field = declaringClass.getField(signature.toCharArray(), true);
				if (field == null || !field.isValidBinding()) return nativeSource;
				String registeredFieldName = this.fieldNamesRegistry.targetName(field);
				if (field.isStatic()) {
					replacement.append(newTypeName);
					replacement.append('.');
				}
				replacement.append(registeredFieldName);
				replacement.append(nativeSource.substring(memberNameEnd, length));
			} else {
				int closingBracket = nativeSource.indexOf(')', memberStart);
				if (closingBracket == -1) return nativeSource; // did not find )
				// replace qualified method signature with registered method selector
				char[] selector = nativeSource.substring(memberStart, openingBracket).toCharArray();
				String methodSignature = nativeSource.substring(openingBracket, closingBracket + 1);
				TypeBinding[] argumentTypes = convertSignatureToParameters(methodSignature, scope);
				boolean isConstructor = CharOperation.equals(selector, "new".toCharArray()); //$NON-NLS-1$
				MethodBinding method = isConstructor
					? declaringClass.getExactConstructor(argumentTypes)
					: declaringClass.getExactMethod(selector, argumentTypes, null);
				if (method == null && isConstructor && declaringClass.isNestedType()) {
					TypeBinding[] noSytheticArgumentTypes = new TypeBinding[argumentTypes.length - 1];
					System.arraycopy(argumentTypes, 1, noSytheticArgumentTypes, 0, noSytheticArgumentTypes.length);
					method = declaringClass.getExactConstructor(noSytheticArgumentTypes);
				}
				if (method == null)
					method = Util.findExactMethod(declaringClass, selector, argumentTypes);
				if (method == null || !method.isValidBinding()) return nativeSource;
				if (method.isStatic() || nativeSource.charAt(closingBracket + 1) != '(') {
					// add typeName if static reference or a reference to a function
					replacement.append(newTypeName);
					replacement.append('.');
				}
				if (method.isConstructor()) {
					replacement.append("new "); //$NON-NLS-1$
					replacement.append(newTypeName);
				} else {
					Util.appendMethodName(replacement, method, false, false); // would like a way to get the method selector
				}
				replacement.append(nativeSource.substring(closingBracket + 1, length));
			}
			nativeSource = replacement.toString();
		}
	}

	private TypeBinding[] convertSignatureToParameters(String methodSignature, Scope scope) {
		// MethodTypeSignature = ParameterPart(optional) '(' TypeSignatures ')' return_typeSignature ['^' TypeSignature (optional)]
		if (methodSignature.length() <= 2) return new TypeBinding[0];
		SignatureWrapper wrapper = new SignatureWrapper(methodSignature.toCharArray());
		TypeBinding[] parameters = new TypeBinding[0];
		if (wrapper.signature[wrapper.start] == '(') {
			wrapper.start++; // skip '('
			if (wrapper.signature[wrapper.start] == ')') {
				wrapper.start++; // skip ')'
			} else {
				java.util.ArrayList types = new java.util.ArrayList(2);
				LookupEnvironment env = scope.environment();
				while (wrapper.signature[wrapper.start] != ')') {
					TypeBinding type = env.getTypeFromTypeSignature(wrapper, null, null, null);
					type = BinaryTypeBinding.resolveType(type, env, true /* raw conversion */);
					types.add(type);
				}
				wrapper.start++; // skip ')'
				int numParam = types.size();
				parameters = new TypeBinding[numParam];
				types.toArray(parameters);
			}
		}
		return parameters;
	}

	private void expandMethodArgument(char[] name, TypeBinding type, boolean prepandComma) {
		if (prepandComma) {
			this.scriptOutput.append(", "); //$NON-NLS-1$
		}
		this.scriptOutput.append(name);
		this.scriptOutput.append(": "); //$NON-NLS-1$
		Util.appendTypeName(this.scriptOutput, type, this.importRegistry.isAmbiguous(type), true);
	}

	void flushImports(CompilationUnitScope scope) {
		CompilationUnitDeclaration referenceCompilationUnit = scope.referenceCompilationUnit();
		ImportReference[] imports = referenceCompilationUnit.imports;
		char[][] javaLangSystemAsTokens = { 
			"java".toCharArray(), //$NON-NLS-1$
			"lang".toCharArray(), //$NON-NLS-1$
			"System".toCharArray() //$NON-NLS-1$
		};
		char[] javaLangSystemAsChars = "java.lang.System".toCharArray(); //$NON-NLS-1$
		// PREMATURE factorize this properly
		boolean importJavaLangSystem = this.importRegistry.importJavaLangSystem;
		this.compilationUnitLocalNamesRegistry = new LocalNamesRegistry();
		if (this.currentPackage != null) {
			this.compilationUnitLocalNamesRegistry.checkPackage(this.currentPackage.tokens, this.currentPackage.tokens.length);
		}
		if (this.localNamesRegistry != null) {
			this.localNamesRegistries.push(this.localNamesRegistry);
		}
		this.localNamesRegistry = this.compilationUnitLocalNamesRegistry;
		if (imports != null) {
			for (int i = 0, length = imports.length; i < length; i++) {
				ImportReference importRef = imports[i];
				if (importRef == this.currentPackage || importRef.isStatic()) // already processed in unit visit or a static import
					continue;
				char[][] tokens = importRef.tokens;
				int importLength = tokens.length;
				int start = 0;
				if (CharOperation.equals(Util.INTRINSIC, tokens[0])) {
					switch (importLength) {
						case 1:
							continue;
						case 2:
							if ((importRef.bits & ASTNode.OnDemand) == 0) {
								continue;
							}
					}
					start = 1;
				}
				Binding importBinding = scope.getTypeOrPackage(tokens);
				ReferenceBinding importedType = importBinding instanceof ReferenceBinding 
					? (ReferenceBinding) importBinding
					: null;
				if (importedType == null) {
					this.scriptOutput.append("import ");//$NON-NLS-1$
					for (int j = start; j < importLength; j++) {
						this.scriptOutput.append(tokens[j]);
						this.scriptOutput.append('.');
					}
					this.scriptOutput.append('*');
					this.compilationUnitLocalNamesRegistry.checkPackage(tokens, importLength);
					this.scriptOutput.append(';').appendNewLine();
				} else if (!importedType.isAnnotationType() && importedType.isValidBinding()) {
					this.scriptOutput.append("import ");//$NON-NLS-1$
					Util.appendTypeName(this.scriptOutput, importedType, true);
					this.compilationUnitLocalNamesRegistry.checkPackage(tokens, importLength - 1);
					this.scriptOutput.append(';').appendNewLine();
				}
				if (CharOperation.equals(javaLangSystemAsTokens, tokens)) {
					importJavaLangSystem = false;
				}
			}
		}
		Iterator missingImports = this.importRegistry.getUnimportedTypes();
		PackageBinding unitPackageBinding = scope.fPackage;
		while (missingImports.hasNext()) {
			TypeBinding type = (TypeBinding) missingImports.next();
			PackageBinding packageBinding = type.getPackage();
			if (packageBinding == null || packageBinding == unitPackageBinding ||
					type.isAnnotationType()) 
				continue;
			char[][] tokens = packageBinding.compoundName;
			if (tokens.length == 1 && CharOperation.equals(Util.INTRINSIC, tokens[0])) {
				continue;
			}
			this.compilationUnitLocalNamesRegistry.checkPackage(tokens, tokens.length - 1);
			this.scriptOutput.append("import ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, type, true);
			this.scriptOutput.append(';').appendNewLine();
			if (CharOperation.equals(javaLangSystemAsChars, type.readableName())) {
				importJavaLangSystem = false;
			}
		}
		if (this.importRegistry.importJavaLangArguments) {
			this.scriptOutput.append("import java.lang.Arguments;").appendNewLine(); //$NON-NLS-1$
		}
		if (this.importRegistry.importJavaLangClass) {
			this.scriptOutput.append("import java.lang.Class__;").appendNewLine(); //$NON-NLS-1$
		}
		if (this.importRegistry.importJavaLangEnum) {
			this.scriptOutput.append("import java.lang.Enum;").appendNewLine(); //$NON-NLS-1$
		}
		if (this.importRegistry.importJavaLangJavaArray) {
			this.scriptOutput.append("import java.lang.JavaArray;").appendNewLine(); //$NON-NLS-1$
		}
		if (importJavaLangSystem) {
			this.scriptOutput.append("import java.lang.System;").appendNewLine(); //$NON-NLS-1$
		}
		if (!this.localNamesRegistries.empty()) {
			this.localNamesRegistry = (LocalNamesRegistry) this.localNamesRegistries.pop();
		}
	}
	
	private void generateOuterAccess(Object[] mappingSequence, TypeBinding implicitThisType) {
		if (mappingSequence != null) {
			if (mappingSequence == BlockScope.EmulationPathToImplicitThis) {
				if (implicitThisType != null) {
					Util.appendTypeName(this.scriptOutput, implicitThisType, false);
					this.scriptOutput.append('(');
				}
				this.scriptOutput.append("this"); //$NON-NLS-1$
				if (implicitThisType != null) {
					this.scriptOutput.append(')');
				}
			} else {
				generateOuterAccess(mappingSequence, 0, mappingSequence.length - 1);
			}
		}
	}
	private void generateOuterAccess(Object[] mappingSequence) {
		generateOuterAccess(mappingSequence, null);
	}
	// WORK simplify if mappingSequences have controlled depth; else the . management will have to be fixed
	private void generateOuterAccess(Object[] mappingSequence, int start, int end) {
		if (end >= start) {
			if (mappingSequence[end] instanceof MethodBinding) {
				MethodBinding method = (MethodBinding) mappingSequence[end];
				Util.appendTypeName(this.scriptOutput, method.declaringClass, this.importRegistry.isAmbiguous(method.declaringClass));
				this.scriptOutput.append('.');
				this.scriptOutput.append(method.selector);
				this.scriptOutput.append('(');
				generateOuterAccess(mappingSequence, start, end - 1);
				this.scriptOutput.append(')');
			} else if (mappingSequence[end] instanceof FieldBinding) {
				generateOuterAccess(mappingSequence, start, end - 1);
//				this.scriptOutput.append('.');
				this.scriptOutput.append(
					this.fieldNamesRegistry.targetName((FieldBinding) mappingSequence[end]));
			} else {
				this.scriptOutput.append(
					this.localNamesRegistry.targetName((LocalVariableBinding) mappingSequence[end]));
			}
		}
	}
	
	/**
	 * Return the highest method/constructor in supertype hierarchy with same selector and arguments
	 */
	private MethodBinding getHighestOverridenMethod(MethodBinding methodBinding, LookupEnvironment environment) {
		MethodBinding bestMethod = methodBinding;
		ReferenceBinding currentType = methodBinding.declaringClass;
		if (methodBinding.isConstructor()) {
			// walk superclasses - only
			do {
				MethodBinding superMethod = currentType.getExactConstructor(methodBinding.parameters);
				if (superMethod != null) {
					bestMethod = superMethod;
				}
			} while ((currentType = currentType.superclass()) != null);
			return bestMethod;
		}
		MethodVerifier verifier = environment.methodVerifier();
		// walk superclasses
		ReferenceBinding[] interfacesToVisit = null;
		int nextPosition = 0;
		do {
			MethodBinding[] superMethods = currentType.getMethods(methodBinding.selector);
			for (int i = 0, length = superMethods.length; i < length; i++) {
				if (verifier.doesMethodOverride(methodBinding, superMethods[i])) {
					bestMethod = superMethods[i];
					break;
				}
			}
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				if (interfacesToVisit == null) {
					interfacesToVisit = itsInterfaces;
					nextPosition = interfacesToVisit.length;
				} else {
					int itsLength = itsInterfaces.length;
					if (nextPosition + itsLength >= interfacesToVisit.length)
						System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
					nextInterface : for (int a = 0; a < itsLength; a++) {
						ReferenceBinding next = itsInterfaces[a];
						for (int b = 0; b < nextPosition; b++)
							if (next == interfacesToVisit[b]) continue nextInterface;
						interfacesToVisit[nextPosition++] = next;
					}
				}
			}
		} while ((currentType = currentType.superclass()) != null);
		if (bestMethod.declaringClass.id == TypeIds.T_JavaLangObject) {
			return bestMethod;
		}
		// walk superinterfaces
		for (int i = 0; i < nextPosition; i++) {
			currentType = interfacesToVisit[i];
			MethodBinding[] superMethods = currentType.getMethods(methodBinding.selector);
			for (int j = 0, length = superMethods.length; j < length; j++) {
				MethodBinding superMethod = superMethods[j];
				if (verifier.doesMethodOverride(methodBinding, superMethod)) {
					TypeBinding bestReturnType = bestMethod.returnType;
					if (bestReturnType == superMethod.returnType
							|| bestMethod.returnType.findSuperTypeOriginatingFrom(superMethod.returnType) != null) {
						bestMethod = superMethod;
					}
					break;
				}
			}
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				int itsLength = itsInterfaces.length;
				if (nextPosition + itsLength >= interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
				nextInterface : for (int a = 0; a < itsLength; a++) {
					ReferenceBinding next = itsInterfaces[a];
					for (int b = 0; b < nextPosition; b++)
						if (next == interfacesToVisit[b]) continue nextInterface;
					interfacesToVisit[nextPosition++] = next;
				}
			}
		}	
		return bestMethod;
	}

	public void indent() {
		this.scriptOutput.indent();
	}
	
	public void outdent() {
		this.scriptOutput.outdent();
	}

	public void printIndent() {
		this.scriptOutput.printIndent();
	}

	protected void reportError(Scope scope, String errorMessage, ASTNode location) {
		String[] arguments = new String[] {"ActionScript3.0: " + errorMessage}; //$NON-NLS-1$
		ProblemReporter reporter = scope.problemReporter();
		reporter.handle(
				IProblem.Unclassified,
				arguments,
				0,
				arguments,
				ProblemSeverities.Error | ProblemSeverities.Fatal,
				location == null ? 0 : location.sourceStart,
				location == null ? 0 : location.sourceEnd,
				reporter.referenceContext, 
				reporter.referenceContext == null ? null : reporter.referenceContext.compilationResult());
	}

	protected void reportWarning(Scope scope, String message, ASTNode location) {
		String[] arguments = new String[] {"ActionScript3.0: " + message}; //$NON-NLS-1$
		org.eclipse.jdt.internal.compiler.as.ProblemReporter reporter = (org.eclipse.jdt.internal.compiler.as.ProblemReporter) scope.problemReporter();
		if (((ASCompilerOptions) reporter.options).disableWarnings) return;
		reporter.handle(
				IProblem.Unclassified,
				arguments,
				0,
				arguments,
				ProblemSeverities.Warning,
				location == null ? 0 : location.sourceStart,
				location == null ? 0 : location.sourceEnd,
				reporter.referenceContext, 
				reporter.referenceContext == null ? null : reporter.referenceContext.compilationResult());
	}
	public String toString() {
		return this.scriptOutput.toString() + "[END]"; //$NON-NLS-1$
	}
	
	// PREMATURE duplicated from org.eclipse.jdt.internal.core.util.Util to
	//			 remove dependency
	String toString(char[][] c) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0, max = c.length; i < max; ++i) {
			if (i != 0)
				sb.append('.');
			sb.append(c[i]);
		}
		return sb.toString();
	}

	public boolean visit(AllocationExpression allocationExpression, BlockScope scope) {
		ReferenceBinding allocatedType =  allocationExpression.binding.declaringClass;
		boolean generateExtraComma = false, generateExtraParenthesis = false;
		if (allocatedType.id == TypeIds.T_JavaLangString) {
			if (allocationExpression.arguments != null && 
					allocationExpression.arguments.length > 0) {
				this.scriptOutput.append("Object(String).");//$NON-NLS-1$
				Util.appendMethodName(this.scriptOutput, allocationExpression.binding, false, false);
				this.scriptOutput.append('(');
			} else {
				this.scriptOutput.append("new String(");//$NON-NLS-1$
			}
		} else if (Util.isSingleConstructor(allocationExpression.binding) ||
				allocationExpression.binding.declaringClass.id == TypeIds.T_JavaLangObject ||
				Util.isIntrinsic(allocatedType)) {
			this.scriptOutput.append("new "); //$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, allocatedType, this.importRegistry.isAmbiguous(allocatedType));
			this.scriptOutput.append('(');
		} else {
			this.scriptOutput.append("new ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, allocatedType, this.importRegistry.isAmbiguous(allocatedType));
			this.scriptOutput.append("(new Arguments(");//$NON-NLS-1$
			Util.dumpConstructorFieldReference(this.scriptOutput, allocationExpression.binding);
			this.scriptOutput.append(", [");//$NON-NLS-1$
			generateExtraParenthesis = true;
		}
		ReferenceBinding[] syntheticArgumentTypes;
		if ((syntheticArgumentTypes = allocatedType.syntheticEnclosingInstanceTypes()) != null) {
			generateExtraComma = true;
			for (int i = 0, max = syntheticArgumentTypes.length; i < max; i++) {
				generateOuterAccess(scope.getEmulationPath(
						syntheticArgumentTypes[i], 
						false /*not only exact match (that is, allow compatible)*/,
						false),
						scope instanceof MethodScope && ((MethodScope) scope).isInsideInitializer() ? syntheticArgumentTypes[i] : null);
			}
		}
		SyntheticArgumentBinding syntheticArguments[];
		if ((syntheticArguments = allocatedType.syntheticOuterLocalVariables()) != null) {
			if (generateExtraComma) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
			} else {
				generateExtraComma = true;
			}
			generateOuterAccess(scope.getEmulationPath(syntheticArguments[0].actualOuterLocalVariable));
			for (int i = 1, max = syntheticArguments.length; i < max; i++) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
				generateOuterAccess(scope.getEmulationPath(syntheticArguments[i].actualOuterLocalVariable));
			}
		}
		FieldDeclaration enumConstant = allocationExpression.enumConstant;
		if (enumConstant != null) {
			// this is an enum constant initialization
			// we need to insert the name and ordinal value
			this.scriptOutput.append('\"');
			this.scriptOutput.append(enumConstant.name);
			this.scriptOutput.append("\", ");//$NON-NLS-1$
			this.scriptOutput.append(Util.getOrdinalValue(enumConstant));
			generateExtraComma = true;
		}
		if (allocationExpression.arguments != null) {
			if (generateExtraComma) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
			}
			allocationExpression.arguments[0].traverse(this, scope);
			for (int i = 1, length = allocationExpression.arguments.length; i < length; i++) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
				allocationExpression.arguments[i].traverse(this, scope);
			}
		}
		if (generateExtraParenthesis) {
			this.scriptOutput.append("]))");//$NON-NLS-1$
		} else {
			this.scriptOutput.append(')');
		}
		return false;
	}
	
	public boolean visit(AND_AND_Expression and_and_Expression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, and_and_Expression, scope);
		and_and_Expression.left.traverse(this, scope);
		this.scriptOutput.append(' ').append(and_and_Expression.operatorToString()).append(' ');
		and_and_Expression.right.traverse(this, scope);
		Util.appendClosingParenthesis(this.scriptOutput, and_and_Expression, scope);
		return false;
	}
	
	public boolean visit(ArrayAllocationExpression arrayAllocationExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, arrayAllocationExpression, scope);
		ArrayInitializer initializer = arrayAllocationExpression.initializer;
		Expression[] dimensions = arrayAllocationExpression.dimensions;
		this.scriptOutput.append("new JavaArray(\""); //$NON-NLS-1$
		this.scriptOutput.append(arrayAllocationExpression.resolvedType.signature());
		this.scriptOutput.append("\")"); //$NON-NLS-1$
		if (initializer != null && initializer.expressions != null) {
			this.scriptOutput.append(".values("); //$NON-NLS-1$
			int valuesCountMinusOne = initializer.expressions.length - 1; 
			for (int i = 0; i < valuesCountMinusOne; i++) {
				initializer.expressions[i].traverse(this, scope);
				this.scriptOutput.append(", "); //$NON-NLS-1$
			}
			initializer.expressions[valuesCountMinusOne].traverse(this, scope);
			this.scriptOutput.append(")"); //$NON-NLS-1$
		} else if (dimensions != null) {
			this.scriptOutput.append(".lengths("); //$NON-NLS-1$
			for (int i = 0; i < dimensions.length; i++) {
				if (dimensions[i] == null) break;
				if (i != 0) this.scriptOutput.append(", "); //$NON-NLS-1$
				dimensions[i].traverse(this, scope);
			}
			this.scriptOutput.append(")"); //$NON-NLS-1$
		}
		Util.appendClosingParenthesis(this.scriptOutput, arrayAllocationExpression, scope);
		return false;
	}
	
	public boolean visit(ArrayInitializer arrayInitializer, BlockScope scope) {
		this.scriptOutput.append("new JavaArray(\""); //$NON-NLS-1$
		this.scriptOutput.append(arrayInitializer.resolvedType.signature());
		this.scriptOutput.append("\")"); //$NON-NLS-1$
		if (arrayInitializer.expressions != null) {
			this.scriptOutput.append(".values("); //$NON-NLS-1$
			int valuesCountMinusOne = arrayInitializer.expressions.length - 1; 
			for (int i = 0; i < valuesCountMinusOne; i++) {
				arrayInitializer.expressions[i].traverse(this, scope);
				this.scriptOutput.append(", "); //$NON-NLS-1$
			}
			arrayInitializer.expressions[valuesCountMinusOne].traverse(this, scope);
			this.scriptOutput.append(")"); //$NON-NLS-1$
		}
		return false;
	}

	public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, arrayQualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(arrayQualifiedTypeReference.resolvedType));
		return false;
	}
	
	public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, arrayQualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(arrayQualifiedTypeReference.resolvedType));
		return false;
	}

	public boolean visit(ArrayReference arrayReference, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, arrayReference, scope);
		arrayReference.receiver.traverse(this, scope);
		this.scriptOutput.append('[');
		arrayReference.position.traverse(this, scope);
		this.scriptOutput.append(']');
		Util.appendClosingParenthesis(this.scriptOutput, arrayReference, scope);
		return false;
	}

	public boolean visit(ArrayTypeReference arrayTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, arrayTypeReference.resolvedType, this.importRegistry.isAmbiguous(arrayTypeReference.resolvedType));
		return false;
	}

	public boolean visit(ArrayTypeReference arrayTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, arrayTypeReference.resolvedType, this.importRegistry.isAmbiguous(arrayTypeReference.resolvedType));
		return false;
	}
	public boolean visit(AssertStatement assertStatement, BlockScope scope) {
		// need to inject java.lang.AssertionError in the compiler loop
		Util.mapLine(this, this.scriptOutput, assertStatement);
		scope.environment().getType(TypeConstants.JAVA_LANG_ASSERTIONERROR);
		this.printIndent();
		this.scriptOutput.append("if (!("); //$NON-NLS-1$
		visitAsExpression(assertStatement.assertExpression, scope);
		this.scriptOutput.append(")) {").appendNewLine(); //$NON-NLS-1$
		indent();
		Expression exceptionArgument = assertStatement.exceptionArgument;
		this.printIndent();
		this.scriptOutput.append("throw "); //$NON-NLS-1$
		// we don't want to hard-code the constructor index
		if (exceptionArgument == null) {
			this.scriptOutput.append("new java.lang.AssertionError(new Arguments(0, []));").appendNewLine(); //$NON-NLS-1$
		} else {
			MethodBinding constructor = Util.retrieveConstructor(exceptionArgument.resolvedType, scope.getJavaLangAssertionError());
			if (constructor == null) {
				reportError(scope, "A constructor must be found for AssertionError", assertStatement); //$NON-NLS-1$
			}
			this.scriptOutput.append("new java.lang.AssertionError(new Arguments(");//$NON-NLS-1$
			Util.dumpConstructorFieldReference(this.scriptOutput, constructor);
			this.scriptOutput.append(", [");//$NON-NLS-1$
			if (exceptionArgument != null) {
				visitAsExpression(exceptionArgument, scope);
			}
			this.scriptOutput.append("]));").appendNewLine(); //$NON-NLS-1$
		}
		outdent();
		this.printIndent();
		this.scriptOutput.append('}');
		return false;
	}

	public boolean visit(Assignment assignment, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, assignment, scope);
		Expression lhs = assignment.lhs;
		try {
			if (lhs instanceof Reference) {
				if (lhs instanceof FieldReference) {
					FieldReference fieldReference = (FieldReference) lhs;
					MethodBinding[] syntheticAccessors = fieldReference.syntheticAccessors;
					if (syntheticAccessors != null) {
						MethodBinding methodBinding = syntheticAccessors[FieldReference.WRITE];
						if (methodBinding != null) {
							// generate a synthetic access
							ReferenceBinding declaringClass = methodBinding.declaringClass;
							Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
							this.scriptOutput.append('.');
							this.scriptOutput.append(methodBinding.selector);
							this.scriptOutput.append('(');
							if (!fieldReference.binding.isStatic()) {
								generateOuterAccess(scope.getEmulationPath(fieldReference.fieldBinding().original().declaringClass, false, false));
								this.scriptOutput.append(", "); //$NON-NLS-1$
							}
							assignment.expression.traverse(this, scope);
							this.scriptOutput.append(')');
						}
						return false;
					}
				} else if (lhs instanceof QualifiedNameReference) {
					QualifiedNameReference qualifiedNameReference = (QualifiedNameReference) lhs;
					MethodBinding methodBinding = qualifiedNameReference.syntheticWriteAccessor;
					if (methodBinding != null) {
						// generate a synthetic access
						ReferenceBinding declaringClass = methodBinding.declaringClass;
						boolean fullyQualified = Util.shouldFullyQualified(qualifiedNameReference);
						Util.appendTypeName(this.scriptOutput, declaringClass, fullyQualified);
						this.scriptOutput.append('.');
						this.scriptOutput.append(methodBinding.selector);
						this.scriptOutput.append('(');
						switch (qualifiedNameReference.bits & ASTNode.RestrictiveFlagMASK) {
							case Binding.FIELD : {
								FieldBinding fieldBinding = qualifiedNameReference.fieldBinding();
								if (fieldBinding != null && !fieldBinding.isStatic()) {
									// PREMATURE may have issues with accessors (bug 22)
									MethodBinding syntheticAccessor = qualifiedNameReference.syntheticReadAccessors == null ? null :
										qualifiedNameReference.syntheticReadAccessors[0]; // further read accessors supposedly match other bindings
									if (syntheticAccessor != null) {
										Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, fullyQualified);
										this.scriptOutput.append('.');
										this.scriptOutput.append(syntheticAccessor.selector);
										this.scriptOutput.append('(');
										generateOuterAccess(scope.getEmulationPath(fieldBinding.original().declaringClass, false, false));
										this.scriptOutput.append(')');
									} else {
										if ((qualifiedNameReference.bits & ASTNode.DepthMASK) != 0) {
											if (((FieldBinding)qualifiedNameReference.binding).isStatic()) {
												Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, true);
											} else {
												generateOuterAccess(scope.getEmulationPath(fieldBinding.original().declaringClass, false, false));
											}
											this.scriptOutput.append('.');
										} else if (qualifiedNameReference.indexOfFirstFieldBinding > 1) {
											if (qualifiedNameReference.actualReceiverType.isInterface()) {
												Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, true);
												this.scriptOutput.append(INTERFACE_FIELDS);
											} else {
												Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, fullyQualified);
											}
											this.scriptOutput.append('.');
										}
										this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldBinding));
									}
									this.scriptOutput.append(", "); //$NON-NLS-1$
								}
								break;
							}
							case Binding.LOCAL : {
								LocalVariableBinding variableBinding = (LocalVariableBinding) qualifiedNameReference.binding;
								this.scriptOutput.append(this.localNamesRegistry.targetName(variableBinding));
								this.scriptOutput.append(", "); //$NON-NLS-1$
							}
						}
						assignment.expression.traverse(this, scope);
						this.scriptOutput.append(')');
						return false;
					}
				} else if (lhs instanceof SingleNameReference) {
					SingleNameReference singleNameReference = (SingleNameReference) lhs;
					MethodBinding[] syntheticAccessors = singleNameReference.syntheticAccessors;
					if (syntheticAccessors != null) {
						MethodBinding methodBinding = syntheticAccessors[FieldReference.WRITE];
						if (methodBinding != null) {
							// generate a synthetic access
							ReferenceBinding declaringClass = methodBinding.declaringClass;
							Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
							this.scriptOutput.append('.');
							this.scriptOutput.append(methodBinding.selector);
							this.scriptOutput.append('(');
							FieldBinding fieldBinding = singleNameReference.fieldBinding();
							if (!fieldBinding.isStatic()) {
								generateOuterAccess(scope.getEmulationPath(fieldBinding.original().declaringClass, false, false));
								this.scriptOutput.append(", "); //$NON-NLS-1$
							}
							assignment.expression.traverse(this, scope);
							this.scriptOutput.append(')');
						}
						return false;
					}
				}
			}
			lhs.traverse(this, scope);
			this.scriptOutput.append(" = ");//$NON-NLS-1$
			assignment.expression.traverse(this, scope);
		} finally {
			Util.appendClosingParenthesis(this.scriptOutput, assignment, scope);
		}
		return false;
	}
	public boolean visit(BinaryExpression binaryExpression, BlockScope scope) {
		if (binaryExpression.resolvedType.id == TypeIds.T_JavaLangString) {
			return visitStringConcatenation(binaryExpression, scope);
		}
		boolean castToInt = false;
		switch (binaryExpression.resolvedType.id) {
			case TypeIds.T_short:
			case TypeIds.T_int:
			case TypeIds.T_byte:
			case TypeIds.T_char:
				castToInt = (binaryExpression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT == OperatorIds.DIVIDE;
				// PREMATURE could tune down to only cast within other expressions (not when implicit cast is already here)
				break;
		}
		Util.appendOpeningParenthesis(this, this.scriptOutput, binaryExpression, scope);
		if (castToInt) {
			this.scriptOutput.append("int("); //$NON-NLS-1$
		}
		ifboolean: switch (binaryExpression.resolvedType.id) {
			case TypeIds.T_boolean:
				switch ((binaryExpression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT) { 
					case OperatorIds.AND:
						this.scriptOutput.append('(');
						binaryExpression.left.traverse(this, scope);
						this.scriptOutput.append(") ? ("); //$NON-NLS-1$
						binaryExpression.right.traverse(this, scope);
						this.scriptOutput.append(") : ("); //$NON-NLS-1$
						binaryExpression.right.traverse(this, scope);
						this.scriptOutput.append(", false)"); //$NON-NLS-1$
						break ifboolean;
					case OperatorIds.OR:
						this.scriptOutput.append('(');
						binaryExpression.left.traverse(this, scope);
						this.scriptOutput.append(") ? ("); //$NON-NLS-1$
						binaryExpression.right.traverse(this, scope);
						this.scriptOutput.append(", true) : ("); //$NON-NLS-1$
						binaryExpression.right.traverse(this, scope);
						this.scriptOutput.append(')');
						break ifboolean;
					case OperatorIds.XOR:
						this.scriptOutput.append('(');
						binaryExpression.left.traverse(this, scope);
						this.scriptOutput.append(") != ("); //$NON-NLS-1$
						binaryExpression.right.traverse(this, scope);
						this.scriptOutput.append(')');
						break ifboolean;
				}
				//$FALL-THROUGH$
			default:
				binaryExpression.left.traverse(this, scope);
				this.scriptOutput.append(' ').append(binaryExpression.operatorToString()).append(' ');
				binaryExpression.right.traverse(this, scope);
		}
		if (castToInt) {
			this.scriptOutput.append(")"); //$NON-NLS-1$
		}
		Util.appendClosingParenthesis(this.scriptOutput, binaryExpression, scope);
		return false;
	}
	public boolean visit(Block block, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, block);
		if (block != this.unIndentedStatement) printIndent();
		this.scriptOutput.append('{').appendNewLine(); 
		indent();
		Statement[] blockStatements = block.statements;
		for (int i = 0, length = blockStatements == null ? 0 : blockStatements.length; i < length; i++) {
			Statement node = blockStatements[i];
			if (Util.isUnreachable(node)) {
				break;
			}
			visitAsStatement(node, block.scope);
			this.scriptOutput.appendNewLine(); 
		}
		outdent();
		printIndent();
		this.scriptOutput.append('}');
		this.lastStatement = null;
		return false;
	}

	public boolean visit(BreakStatement breakStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, breakStatement);
		if (breakStatement != this.unIndentedStatement) printIndent();
		if (breakStatement.label == null) {
			this.scriptOutput.append("break;");//$NON-NLS-1$
		} else {
			this.scriptOutput.append("break ").append(breakStatement.label).append(';'); //$NON-NLS-1$ 
		}
		return false;
	}
	
	public boolean visit(CaseStatement caseStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, caseStatement);
		if (caseStatement != this.unIndentedStatement) printIndent();
		Expression constantExpression = caseStatement.constantExpression;
		if (constantExpression == null) {
			this.scriptOutput.append("default :"); //$NON-NLS-1$
		} else {
			this.scriptOutput.append("case "); //$NON-NLS-1$
			if (constantExpression.resolvedType.isEnum()) {
				// we generate the ordinal value of the enum constant
				this.scriptOutput.append(Util.getOrdinalValue((FieldBinding) ((SingleNameReference) constantExpression).binding));
			} else {
				constantExpression.traverse(this, scope);
			}
			this.scriptOutput.append(" :"); //$NON-NLS-1$
		}
		return false;
	}
	
	public boolean visit(CastExpression castExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, castExpression, scope);
		Util.appendTypeName(this.scriptOutput, castExpression.resolvedType, this.importRegistry.isAmbiguous(castExpression.resolvedType));
		this.scriptOutput.append('(');
		castExpression.expression.traverse(this, scope);
		this.scriptOutput.append(')');
		Util.appendClosingParenthesis(this.scriptOutput, castExpression, scope);
		return false;
	}
	
	public boolean visit(CharLiteral charLiteral, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, charLiteral, scope);
		this.scriptOutput.append(charLiteral.constant.intValue());
		Util.appendClosingParenthesis(this.scriptOutput, charLiteral, scope);
		return false;
	}

	public boolean visit(ClassLiteralAccess classLiteral, BlockScope scope) {
		this.scriptOutput.append("Class__.forName__Ljava_lang_String_2(\""); //$NON-NLS-1$
		Util.appendTypeName(this.scriptOutput, classLiteral.targetType, true);
		this.scriptOutput.append("\")"); //$NON-NLS-1$
		return false;
	}	
	
	public boolean visit(Clinit clinit, ClassScope scope) {
		boolean clinitCreated = false;
		TypeDeclaration typeDeclaration = scope.referenceType();
		if (typeDeclaration.fields != null) {
			int length = typeDeclaration.fields.length;
			for (int i = 0; i < length; i++) {
				FieldDeclaration field = typeDeclaration.fields[i];
				if (field.getKind() != AbstractVariableDeclaration.INITIALIZER) // only inline initializers
					continue;
				if (!field.isStatic())
					continue;
				if (!clinitCreated) {
					clinitCreated = true;
					setLineNumberBounds(clinit);
					this.localNamesRegistries.push(this.localNamesRegistry);
					this.localNamesRegistry = LocalNamesRegistry.newInstance(clinit, this.compilationUnitLocalNamesRegistry);
					appendMethod(clinit, scope);
					this.scriptOutput.append(" {").appendNewLine(); //$NON-NLS-1$
					indent();
				}
				field.traverse(this, typeDeclaration.staticInitializerScope);
			}
		}
		if (clinitCreated) {
			outdent();
			printIndent();
			this.scriptOutput.append('}').appendNewLine();
			this.localNamesRegistry = (LocalNamesRegistry) this.localNamesRegistries.pop();
		}
		return false;
	}
	
	// PREMATURE consider pushing the import registry initialization down to the types
	public boolean visit(CompilationUnitDeclaration compilationUnitDeclaration, CompilationUnitScope scope) {
		this.currentPackage = compilationUnitDeclaration.currentPackage;
		if (this.currentPackage != null &&
				CharOperation.equals(Util.INTRINSIC, this.currentPackage.tokens[0])) {
			return false; // skip entire packages starting with 'intrinsic'
		}
		this.importRegistry = new ImportRegistry();
		this.importRegistry.initialize(compilationUnitDeclaration);
		this.lineSeparators = compilationUnitDeclaration.compilationResult.getLineSeparatorPositions();
		return true;
	}

	public boolean visit(CompoundAssignment compoundAssignment, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, compoundAssignment, scope);
		compoundAssignment.lhs.traverse(this, scope);
		if (compoundAssignment.expression.resolvedType.id == TypeIds.T_boolean) {
			this.scriptOutput.append(" = ("); //$NON-NLS-1$
			compoundAssignment.lhs.traverse(this, scope);
			this.scriptOutput.append(") ? ("); //$NON-NLS-1$
			compoundAssignment.expression.traverse(this, scope);
			switch (compoundAssignment.operator) { 
				case OperatorIds.AND:
					this.scriptOutput.append(") : ("); //$NON-NLS-1$
					compoundAssignment.expression.traverse(this, scope);
					this.scriptOutput.append(", false)"); //$NON-NLS-1$
					break;
				case OperatorIds.OR:
					this.scriptOutput.append(", true) : ("); //$NON-NLS-1$
					compoundAssignment.expression.traverse(this, scope);
					this.scriptOutput.append(')');
					break;
			}	
		} else {
			this.scriptOutput.append(' ').append(compoundAssignment.operatorToString()).append(' ');
			compoundAssignment.expression.traverse(this, scope);
		}
		Util.appendClosingParenthesis(this.scriptOutput, compoundAssignment, scope);
		return false;
	}

	public boolean visit(ConditionalExpression conditionalExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, conditionalExpression, scope);
		conditionalExpression.condition.traverse(this, scope);
		this.scriptOutput.append(" ? ");//$NON-NLS-1$
		conditionalExpression.valueIfTrue.traverse(this, scope);
		this.scriptOutput.append(" : ");//$NON-NLS-1$
		conditionalExpression.valueIfFalse.traverse(this, scope);
		Util.appendClosingParenthesis(this.scriptOutput, conditionalExpression, scope);
		return false;
	}
	
	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		if (constructorDeclaration.ignoreFurtherInvestigation) return false;
		if (constructorDeclaration.binding == null) return false;
		setLineNumberBounds(constructorDeclaration);
		MethodBinding constructor = constructorDeclaration.binding;
		FieldBinding[] syntheticFields = null;
		ReferenceBinding declaringClass = constructor.declaringClass;
		if (declaringClass.isNestedType() || declaringClass.isEnum()) {
			syntheticFields = ((SourceTypeBinding) declaringClass).syntheticFields();
		}
		TypeDeclaration typeDeclaration = scope.referenceType();
		boolean hasFieldInitializers = Util.hasFieldInitializers(typeDeclaration);
		Argument[] arguments = constructorDeclaration.arguments;
		if (constructorDeclaration.isDefaultConstructor()
				&& !declaringClass.isEnum()
				&& syntheticFields == null
				&& (arguments == null || arguments.length == 0)
				&& !hasFieldInitializers) {
			Util.registerDefaultConstructor(constructorDeclaration.binding);
			return false;
		}
		if (!this.generatingConstructor &&
				Util.isSingleConstructor(constructorDeclaration.binding)) {
			this.generatingConstructor = true;
			visit(constructorDeclaration, scope);
			this.generatingConstructor = false;
			return false;
		}
		this.localNamesRegistries.push(this.localNamesRegistry);
		this.localNamesRegistry = LocalNamesRegistry.newInstance(constructorDeclaration, this.compilationUnitLocalNamesRegistry);
		this.constructorGenerator.add(constructorDeclaration); // WORK recursion?
		appendMethod(constructorDeclaration, scope);
		if (!constructor.isNative()) {
			this.scriptOutput.append(" {").appendNewLine();//$NON-NLS-1$
			indent();
			if (declaringClass.isEnum()) { // insert String name,int ordinal
				this.printIndent();
				if (declaringClass.superclass().original() == scope.getJavaLangEnum()) {
					// java.lang.Enum has only two arguments (String : the enum constant name, int: the ordinal value)
					this.scriptOutput.append("super(arg0, arg1);").appendNewLine(); //$NON-NLS-1$
				} else {
					this.scriptOutput.append("super(arg0, arg1"); //$NON-NLS-1$
					if (arguments != null) {
						for (int i = 0, max = arguments.length; i < max; i++) {
							this.scriptOutput.append(", "); //$NON-NLS-1$
							this.scriptOutput.append(this.localNamesRegistry.targetName(arguments[i].binding).toCharArray());
						}
					}
					this.scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
				}
			} else {
				if (declaringClass.isNestedType() && syntheticFields != null) {
					for (int i = 0, l = syntheticFields.length;	i < l; i++) {
						FieldBinding fieldBinding = syntheticFields[i];
						if (Util.isExcluded(fieldBinding)) {
							continue;
						}
						printIndent();
						this.scriptOutput.append("this.");//$NON-NLS-1$
						this.scriptOutput.append(fieldBinding.name);
						this.scriptOutput.append(" = ");//$NON-NLS-1$
						this.scriptOutput.append(fieldBinding.name);
						this.scriptOutput.append(';').appendNewLine(); 
					}
				}
				// PREMATURE clean this up once we have the test cases we want
				if (this.generatingConstructor &&
						constructorDeclaration.constructorCall != null &&
						(constructorDeclaration.constructorCall.binding.parameters.length > 0 ||
							constructorDeclaration.constructorCall.binding.declaringClass.isNestedType())) {
					constructorDeclaration.constructorCall.traverse(this, constructorDeclaration.scope);
					this.scriptOutput.append(';').appendNewLine(); 
				}
			}
			if (hasFieldInitializers) {
				int length = typeDeclaration.fields.length;
				for (int i = 0; i < length; i++) {
					FieldDeclaration field = typeDeclaration.fields[i];
					if (field.isStatic()) {
						continue;
					}
					if (field.getKind() != AbstractVariableDeclaration.INITIALIZER) {
						if (field.initialization != null
								&& Util.containsThisReference(field, typeDeclaration.initializerScope)) {
							// only inline initializers and fields that reference this 
							this.printIndent();
							this.scriptOutput.append(this.fieldNamesRegistry.targetName(field.binding));
							this.scriptOutput.append(" = ");//$NON-NLS-1$
							field.initialization.traverse(this, typeDeclaration.initializerScope);
							this.scriptOutput.append(';').appendNewLine();
						}
						continue;
					}
					field.traverse(this, typeDeclaration.initializerScope);
				}
			}
			Statement[] statements = constructorDeclaration.statements;
			if (statements != null) {
				int statementsLength = statements.length;
				for (int i = 0; i < statementsLength; i++) {
					Statement statement = statements[i];
					if (Util.isUnreachable(statement)) break;
					visitAsStatement(statement, constructorDeclaration.scope);
					if (!(statement instanceof TypeDeclaration)) this.scriptOutput.appendNewLine(); 
				}
			}
		}
		if (((constructorDeclaration.bits & ASTNode.NeedFreeReturn) != 0) || !(this.lastStatement instanceof ReturnStatement)) {
			printIndent();
			if (this.generatingConstructor) {
				this.scriptOutput.append("return;").appendNewLine();//$NON-NLS-1$
			} else {
				this.scriptOutput.append("return this;").appendNewLine();//$NON-NLS-1$
			}
		}
		outdent();
		printIndent();
		this.scriptOutput.append('}').appendNewLine();
		this.localNamesRegistry = (LocalNamesRegistry) this.localNamesRegistries.pop();
		return false;
	}
	
	public boolean visit(ContinueStatement continueStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, continueStatement);
		if (continueStatement != this.unIndentedStatement) printIndent();
		if (continueStatement.label == null) {
			this.scriptOutput.append("continue;");//$NON-NLS-1$
		} else {
			this.scriptOutput.append("continue ").append(continueStatement.label).append(';'); //$NON-NLS-1$ 
		}
		return false;
	}
	
	public boolean visit(DoStatement doStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, doStatement);
		if (doStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("do"); //$NON-NLS-1$
		Statement action = doStatement.action;
		boolean isActionBlock = action instanceof Block;
		if (isActionBlock) {
			this.scriptOutput.append(' ');
			this.unIndentedStatement = action;
			visitAsExpression(action, scope); // avoid trailing line break
			this.scriptOutput.append(' ');
		} else {
			this.scriptOutput.appendNewLine();
			indent();
			visitAsStatement(action, scope);
			outdent();
			this.scriptOutput.appendNewLine();
			printIndent();
		}
		this.scriptOutput.append("while ("); //$NON-NLS-1$
		Expression condition = doStatement.condition;
		condition.traverse(this, scope);
		this.scriptOutput.append(");"); //$NON-NLS-1$
		return false;
	}

	public boolean visit(DoubleLiteral doubleLiteral, BlockScope scope) {
		double value = doubleLiteral.constant.doubleValue();
		if (value > 0) {
			if (value < DOUBLE_MIN_VALUE) {
				reportWarning(scope, "double literal underflow", doubleLiteral); //$NON-NLS-1$
			} else if (value > DOUBLE_MAX_VALUE) {
				reportWarning(scope, "double literal overflow", doubleLiteral); //$NON-NLS-1$
			}
		} else if (value < 0) {
			if (value > - DOUBLE_MIN_VALUE) {
				reportWarning(scope, "double literal underflow", doubleLiteral); //$NON-NLS-1$
			} else if (value < - DOUBLE_MAX_VALUE) {
				reportWarning(scope, "double literal overflow", doubleLiteral); //$NON-NLS-1$
			}
		}
		Util.appendOpeningParenthesis(this, this.scriptOutput, doubleLiteral, scope);
		this.scriptOutput.append(value);
		Util.appendClosingParenthesis(this.scriptOutput, doubleLiteral, scope);
		return false;
	}
	
	public boolean visit(EmptyStatement emptyStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, emptyStatement);
		if (emptyStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append(';'); 
		return false;
	}
	
	public boolean visit(EqualExpression equalExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, equalExpression, scope);
		equalExpression.left.traverse(this, scope);
		if (equalExpression.left.resolvedType.isBaseType()) {
			this.scriptOutput.append(' ').append(equalExpression.operatorToString()).append(' ');
		} else {
			this.scriptOutput.append(' ').append(Util.operatorToString(equalExpression)).append(' ');
		}
		equalExpression.right.traverse(this, scope);
		Util.appendClosingParenthesis(this.scriptOutput, equalExpression, scope);
		return false;
	}

	public boolean visit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
		if (Util.isIntrinsic(explicitConstructor.binding.declaringClass) && !Util.isSingleConstructor(explicitConstructor.binding)) {
			return false; // WORK may want to call intrinsic constructors as well, expecting the implementation to deal appropriately with it
		}
		if (!explicitConstructor.isSuperAccess()) {
			return false; // WORK may be wrong; check that we visit this code
		}
		Util.mapLine(this, this.scriptOutput, explicitConstructor);
		printIndent();
		this.scriptOutput.append("super("); //$NON-NLS-1$
		boolean allocateArguments = !
			(Util.isSingleConstructor(explicitConstructor.binding) ||
				explicitConstructor.binding.declaringClass.id == TypeIds.T_JavaLangObject ||
				Util.isIntrinsic(explicitConstructor.binding.declaringClass));
		if (allocateArguments) {
			this.scriptOutput.append("new Arguments(");//$NON-NLS-1$
			Util.dumpConstructorFieldReference(this.scriptOutput, explicitConstructor.binding);
			this.scriptOutput.append(", [");//$NON-NLS-1$
		}
		ReferenceBinding[] syntheticArgumentTypes;
		boolean generateExtraComma = false;
		ReferenceBinding allocatedType = explicitConstructor.binding.declaringClass;
		if (allocatedType != null &&
				(syntheticArgumentTypes = allocatedType.syntheticEnclosingInstanceTypes()) != null) {
			generateExtraComma = true;
			for (int i = 0, max = syntheticArgumentTypes.length; i < max; i++) {
				generateOuterAccess(scope.getEmulationPath(
						syntheticArgumentTypes[i], 
						false /*not only exact match (that is, allow compatible)*/,
						false));
			}
		}
		SyntheticArgumentBinding syntheticArguments[];
		if (allocatedType != null &&
				(syntheticArguments = allocatedType.syntheticOuterLocalVariables()) != null) {
			generateExtraComma = true;
			generateOuterAccess(scope.getEmulationPath(syntheticArguments[0].actualOuterLocalVariable));
			for (int i = 1, max = syntheticArguments.length; i < max; i++) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
				generateOuterAccess(scope.getEmulationPath(syntheticArguments[i].actualOuterLocalVariable));
			}
		}
		if (explicitConstructor.arguments != null) {
			if (generateExtraComma) {
				this.scriptOutput.append(", "); //$NON-NLS-1$
			}
			explicitConstructor.arguments[0].traverse(this, scope);
			int argumentsCount = explicitConstructor.arguments.length;
			for (int i = 1; i < argumentsCount; i++) {
				this.scriptOutput.append(", ");//$NON-NLS-1$
				explicitConstructor.arguments[i].traverse(this, scope);
			}
		}
		if (allocateArguments) {
			this.scriptOutput.append("])");//$NON-NLS-1$
		}
		this.scriptOutput.append(')');
		return false;
	}

	public boolean visit(ExtendedStringLiteral extendedStringLiteral, BlockScope scope) {
		return visit(new StringLiteral(extendedStringLiteral.constant.stringValue().toCharArray(),
				extendedStringLiteral.sourceStart, extendedStringLiteral.sourceEnd,
				1 /* should be line number */), scope);
		// PREMATURE this is a workaround for ExtendedStringLiteral misbehaving 
		//           for printExpression
	}

	public boolean visit(FalseLiteral falseLiteral, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, falseLiteral);
		this.scriptOutput.append("false"); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		Util.mapLine(this, this.scriptOutput, fieldDeclaration, 1, this.lineSeparators == null ? 1 : this.lineSeparators.length);
		if ((fieldDeclaration.binding.modifiers & ClassFileConstants.AccEnum) != 0) {
			// first generate the private synthetic field
			this.printIndent();
			this.scriptOutput.append("private static var "); //$NON-NLS-1$
			char[] syntheticFieldName = Util.getSyntheticFieldName(fieldDeclaration);
			this.scriptOutput.append(syntheticFieldName);
			this.scriptOutput.append(" : "); //$NON-NLS-1$
			char[] typeName = Util.getTypeName(fieldDeclaration.binding.declaringClass,  this.importRegistry.isAmbiguous(fieldDeclaration.binding.declaringClass), true);
			this.scriptOutput.append(typeName);
			this.scriptOutput.append(';').appendNewLine(); 
			this.printIndent();
			this.scriptOutput.append("public static function get "); //$NON-NLS-1$
			this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldDeclaration.binding));
			this.scriptOutput.append("() : "); //$NON-NLS-1$
			this.scriptOutput.append(typeName);
			this.scriptOutput.append(" {").appendNewLine(); //$NON-NLS-1$
			this.indent();
			this.printIndent();
			this.scriptOutput.append("if ("); //$NON-NLS-1$
			this.scriptOutput.append(syntheticFieldName);
			this.scriptOutput.append("=== null) {").appendNewLine(); //$NON-NLS-1$
			this.indent();
			this.printIndent();
			this.scriptOutput.append(syntheticFieldName);
			this.scriptOutput.append(" = "); //$NON-NLS-1$
			// invoke constructor from the enum
			fieldDeclaration.initialization.traverse(this, scope);
			this.scriptOutput.append(';').appendNewLine(); 
			this.outdent();
			this.printIndent();
			this.scriptOutput.append('}').appendNewLine(); 
			this.printIndent();
			this.scriptOutput.append("return "); //$NON-NLS-1$
			this.scriptOutput.append(syntheticFieldName);
			this.scriptOutput.append(';').appendNewLine(); 
			this.outdent();
			this.printIndent();
			this.scriptOutput.append('}').appendNewLine(); 
			return false;
		}
		// PREMATURE reconsider the opportunities to group field declarations
//		if (fieldDeclaration.type != this.lastVariableTypeReference) {
//			if (this.lastVariableTypeReference != null) {
//				this.scriptOutput.append(';').appendNewLine(); //$NON-NLS-1$
//			}
//			this.lastVariableTypeReference = fieldDeclaration.type;
			printIndent();
			if (fieldDeclaration.binding.declaringClass.isInterface() && !this.generatingInterfaceFields) {
				this.scriptOutput.append("// "); //$NON-NLS-1$
			}
			if (fieldDeclaration.binding.isPublic() || fieldDeclaration.binding.isProtected() 
					|| fieldDeclaration.binding.declaringClass.isNestedType()
					|| fieldDeclaration.isStatic()) {
				this.scriptOutput.append("public ");//$NON-NLS-1$
//			} else if (fieldDeclaration.binding.isProtected()) {
//				this.scriptOutput.append("protected ");//$NON-NLS-1$
			} else if (fieldDeclaration.binding.isPrivate()) {
				this.scriptOutput.append("private ");//$NON-NLS-1$
			} else {
				this.scriptOutput.append("internal "); //$NON-NLS-1$
			}
			if (fieldDeclaration.binding.isStatic()) {
				this.scriptOutput.append("static ");//$NON-NLS-1$
			}
//		} else {
//			this.scriptOutput.append(", "); //$NON-NLS-1$
//		}
		if (fieldDeclaration.binding.isFinal()) {
			if (fieldDeclaration.binding.constant() == Constant.NotAConstant) {
				if (!this.generatingInterfaceFields) { // report only once
					reportWarning(scope, "final field should be initialized to a constant value to be generated as a const", fieldDeclaration); //$NON-NLS-1$
				}
				this.scriptOutput.append("var ");//$NON-NLS-1$
			} else {
				this.scriptOutput.append("const ");//$NON-NLS-1$
			}
		} else {
			this.scriptOutput.append("var ");//$NON-NLS-1$
		}
		this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldDeclaration.binding));
		this.scriptOutput.append(": ");//$NON-NLS-1$
		Util.appendTypeName(this.scriptOutput, fieldDeclaration.type.resolvedType, this.importRegistry.isAmbiguous(fieldDeclaration.type.resolvedType), true);
		if (fieldDeclaration.initialization == null) {
			switch (fieldDeclaration.binding.type.id) {
				case TypeIds.T_long:
				case TypeIds.T_float:
				case TypeIds.T_double:
					this.scriptOutput.append(" = 0"); //$NON-NLS-1$
			}
		} else if (!Util.containsThisReference(fieldDeclaration, scope)) {
			this.scriptOutput.append(" = ");//$NON-NLS-1$
			fieldDeclaration.initialization.traverse(this, scope);
		}
		this.scriptOutput.append(';').appendNewLine(); 
		return false;
	}

	public boolean visit(FieldReference fieldReference, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, fieldReference);
		Expression receiver = fieldReference.receiver;
		MethodBinding syntheticAccessor = fieldReference.syntheticAccessors == null ? null :
			fieldReference.syntheticAccessors[0];
		if (fieldReference.fieldBinding().isStatic()) {
			if (!this.generatingConstructor /* avoid reporting twice */ &&
					receiver != null && !receiver.isImplicitThis() && !receiver.isTypeReference()) {
				reportError(scope, "cannot handle receiver for static field access, should be eliminated", fieldReference.receiver); //$NON-NLS-1$
			}
			Util.appendTypeName(this.scriptOutput, fieldReference.binding.declaringClass, this.importRegistry.isAmbiguous(fieldReference.binding.declaringClass));
			this.scriptOutput.append('.');
		} else {
			if (syntheticAccessor != null) {
				Util.appendTypeName(this.scriptOutput, fieldReference.binding.declaringClass, this.importRegistry.isAmbiguous(fieldReference.binding.declaringClass));
				this.scriptOutput.append('.');
				this.scriptOutput.append(syntheticAccessor.selector);
				this.scriptOutput.append('(');
				fieldReference.receiver.traverse(this, scope);
				this.scriptOutput.append(')');
			} else {
				if (receiver != null) {
					fieldReference.receiver.traverse(this, scope);
					this.scriptOutput.append('.');
				}
			}
		}
		if (syntheticAccessor == null) {
			this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldReference.fieldBinding()));
		}
		return false;
	}

	public boolean visit(FloatLiteral floatLiteral, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, floatLiteral, scope);
		// PREMATURE check that floats are all in range
		this.scriptOutput.append(floatLiteral.constant.doubleValue());
		Util.appendClosingParenthesis(this.scriptOutput, floatLiteral, scope);
		return false;
	}

	public boolean visit(ForeachStatement forStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, forStatement);
		if (forStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("for ("); //$NON-NLS-1$
		Expression expression = forStatement.collection;
		boolean isArray = expression.resolvedType.isArrayType();
		if (isArray) {
			// iterate over an array
			// for (int i = 0; i < array.length; i++) {
			// Object e = a[i];
			this.scriptOutput.append("var ");//$NON-NLS-1$
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append(": ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, forStatement.indexVariable.type, this.importRegistry.isAmbiguous(forStatement.indexVariable.type));
			this.scriptOutput.append(" = 0, ");//$NON-NLS-1$
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.maxVariable));
			this.scriptOutput.append(": ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, forStatement.maxVariable.type, this.importRegistry.isAmbiguous(forStatement.maxVariable.type));
			this.scriptOutput.append(" = ");//$NON-NLS-1$
			visitAsExpression(expression, scope);
			this.scriptOutput.append(".length");//$NON-NLS-1$
			this.scriptOutput.append("; ");//$NON-NLS-1$

			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append(" < ");//$NON-NLS-1$
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.maxVariable));
			
			this.scriptOutput.append("; ");//$NON-NLS-1$
			// generate an increment
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append("++)");//$NON-NLS-1$
		} else {
			// iterate over a collection
			// for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
			// ...				"    for (var iterator: Iterator = list.iterator__(); iterator.hasNext__();) {" + 
			//"      var s: String = String(iterator.next__());" + 
			// inject java.util.Iterator in the loop
			scope.environment().getType(TypeConstants.JAVA_UTIL_ITERATOR);
			this.scriptOutput.append("var ");//$NON-NLS-1$
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append(": ");//$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, forStatement.indexVariable.type, this.importRegistry.isAmbiguous(forStatement.indexVariable.type));
			this.scriptOutput.append(" = ");//$NON-NLS-1$
			visitAsExpression(expression, scope);
			this.scriptOutput.append(".iterator__(); "); //$NON-NLS-1$
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append(".hasNext__(); )"); //$NON-NLS-1$
		}
		Statement action = forStatement.action;
		boolean isActionBlock = action instanceof Block;
		this.scriptOutput.append(" {").appendNewLine(); //$NON-NLS-1$
		indent();
		printIndent();
		// insert current element initialization
		visitAsExpression(forStatement.elementVariable, scope);
		this.scriptOutput.append('=');
		if (isArray) {
			// tab[i]
			visitAsExpression(expression, scope);
			this.scriptOutput.append('[');
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append("];").appendNewLine();//$NON-NLS-1$
		} else {
			// iterator.next();
			Util.appendTypeName(this.scriptOutput, forStatement.elementVariable.type.resolvedType, this.importRegistry.isAmbiguous(forStatement.elementVariable.type.resolvedType));
			this.scriptOutput.append('(');
			this.scriptOutput.append(
					this.localNamesRegistry.targetName(forStatement.indexVariable));
			this.scriptOutput.append(".next__());").appendNewLine(); //$NON-NLS-1$
		}
		if (isActionBlock) {
			Statement[] blockStatements = ((Block) action).statements;
			for (int i = 0, length = blockStatements == null ? 0 : blockStatements.length; i < length; i++) {
				visitAsStatement(blockStatements[i], scope);
				this.scriptOutput.appendNewLine();
			}
		} else {
			visitAsStatement(action, scope);
			this.scriptOutput.appendNewLine();
		}
		outdent();
		printIndent();
		this.scriptOutput.append('}'); 
		return false;
	}
	public boolean visit(ForStatement forStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, forStatement);
		if (forStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("for ("); //$NON-NLS-1$
		Statement[] initializations = forStatement.initializations;
		if (initializations != null) {
			this.localDeclarationForStatus = FIRST_IN_FOR;
			for (int i = 0, length = initializations.length; i < length; i++) {
				if (i > 0) this.scriptOutput.append(", "); //$NON-NLS-1$
				visitAsExpression(initializations[i], scope);
				this.localDeclarationForStatus = NTH_IN_FOR;
			}
			this.localDeclarationForStatus = NOT_IN_FOR;
		}
		this.scriptOutput.append(';');
		Expression condition = forStatement.condition;
		if (condition != null) {
			this.scriptOutput.append(' ');
			condition.traverse(this, scope);
		}
		this.scriptOutput.append(';');
		Statement[] increments = forStatement.increments;
		if (increments != null) {
			this.scriptOutput.append(' ');
			for (int i = 0, length = increments.length; i < length; i++) {
				if (i > 0) this.scriptOutput.append(", "); //$NON-NLS-1$
				visitAsExpression(increments[i], scope);
			}
		}
		this.scriptOutput.append(')');
		Statement action = forStatement.action;
		boolean isActionBlock = action instanceof Block;
		if (isActionBlock) {
			this.scriptOutput.append(' ');
			this.unIndentedStatement = action;
			visitAsStatement(action, scope);
		} else {
			this.scriptOutput.appendNewLine();
			indent();
			visitAsStatement(action, scope);
			outdent();
		}
		return false;
	}

	public boolean visit(IfStatement ifStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, ifStatement);
		Statement thenStatement = ifStatement.thenStatement;
		boolean isThenBlock = thenStatement instanceof Block;
		Statement elseStatement = ifStatement.elseStatement;
		if (ifStatement.condition.constant != Constant.NotAConstant) {
			if (Util.isUnreachable(thenStatement)) {
				if (elseStatement != null) {
					if (!Util.isUnreachable(elseStatement)) {
						if (elseStatement instanceof IfStatement
								|| elseStatement instanceof Block) {
							if (ifStatement != this.unIndentedStatement) printIndent();
							this.unIndentedStatement = elseStatement;
							visitAsStatement(elseStatement, scope);
						} else {
							visitAsStatement(elseStatement, scope);
						}
					}
				}
			} else if (isThenBlock) {
				if (ifStatement != this.unIndentedStatement) printIndent();
				this.unIndentedStatement = thenStatement;
				visitAsStatement(thenStatement, scope);
			} else {
				visitAsStatement(thenStatement, scope);
			}
			return false;
		}
		if (ifStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("if ("); //$NON-NLS-1$
		ifStatement.condition.traverse(this, scope);
		this.scriptOutput.append(")");//$NON-NLS-1$
		if (isThenBlock) {
			this.scriptOutput.append(' ');
			visitAsExpression(thenStatement, scope); // avoid trailing linebreak
		} else {
			this.scriptOutput.appendNewLine();
			indent();
			visitAsStatement(thenStatement, scope);
			outdent();
		}
		if (elseStatement != null) {
			if (isThenBlock) {
				this.scriptOutput.append(' ');
			} else {
				this.scriptOutput.appendNewLine();
				printIndent();
			}
			this.scriptOutput.append("else"); //$NON-NLS-1$
			if (elseStatement instanceof IfStatement
					|| elseStatement instanceof Block) {
				this.scriptOutput.append(' ');
				this.unIndentedStatement = elseStatement;
				visitAsStatement(elseStatement, scope);
			} else {
				this.scriptOutput.appendNewLine(); 
				indent();
				visitAsStatement(elseStatement, scope);
				outdent();
			} 
		}
		return false;
	}
	
	public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
		return false;
	}
	
	public boolean visit(Initializer initializer, MethodScope scope) {
		Util.mapLine(this, this.scriptOutput, initializer);
		initializer.block.traverse(this, scope);
		this.scriptOutput.appendNewLine(); 
		return false;
	}

	public boolean visit(InstanceOfExpression instanceOfExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, instanceOfExpression, scope);
		instanceOfExpression.expression.traverse(this, scope);
		this.scriptOutput.append(" is "); //$NON-NLS-1$
		TypeBinding type = instanceOfExpression.type.resolvedType;
		Util.appendTypeName(this.scriptOutput, type, this.importRegistry.isAmbiguous(type));
		Util.appendClosingParenthesis(this.scriptOutput, instanceOfExpression, scope);
		return false;
	}
	
	public boolean visit(IntLiteral intLiteral, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, intLiteral, scope);
		this.scriptOutput.append(intLiteral.source());
		Util.appendClosingParenthesis(this.scriptOutput, intLiteral, scope);
		return false;
	}

	public boolean visit(LabeledStatement labeledStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, labeledStatement);
		if (labeledStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append(labeledStatement.label).append(':');
		Statement statement = labeledStatement.statement;
		if (statement instanceof Block
				|| statement instanceof WhileStatement
				|| statement instanceof ForStatement
				|| statement instanceof SwitchStatement
				|| statement instanceof DoStatement) {
			this.scriptOutput.append(' ');
			this.unIndentedStatement = statement;
			visitAsStatement(statement, scope);
		} else {
			this.scriptOutput.appendNewLine();
			indent();
			visitAsStatement(statement, scope);
			outdent();
		}
		return false;
	}
	
	public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
		// PREMATURE field declarations implement a mechanism to avoid
		//           the systematic use of one line per declaration; it is not
		//			 directly reusable for locals, hence we generate one line
		//			 per local decl; consider doing more/better
		Util.mapLine(this, this.scriptOutput, localDeclaration);
		if (this.localDeclarationForStatus != NTH_IN_FOR) {
			this.scriptOutput.append("var ");//$NON-NLS-1$
		}
		this.scriptOutput.append(
				this.localNamesRegistry.targetName(localDeclaration.binding));
		this.scriptOutput.append(": ");//$NON-NLS-1$
		Util.appendTypeName(this.scriptOutput, localDeclaration.type.resolvedType, this.importRegistry.isAmbiguous(localDeclaration.type.resolvedType));
		if (localDeclaration.initialization == null) {
			switch (localDeclaration.binding.type.id) {
				case TypeIds.T_long:
				case TypeIds.T_float:
				case TypeIds.T_double:
					this.scriptOutput.append(" = 0"); //$NON-NLS-1$
			}	
		} else {
			this.scriptOutput.append(" = ");//$NON-NLS-1$
			localDeclaration.initialization.traverse(this, scope);
		}
		return false;
	}

	public boolean visit(LongLiteral longLiteral, BlockScope scope) {
		long value = longLiteral.constant.longValue();
		if (value < LONG_MIN_VALUE) {
			reportWarning(scope, "long literal underflow", longLiteral); //$NON-NLS-1$
		} else if (value > LONG_MAX_VALUE) {
			reportWarning(scope, "long literal overflow", longLiteral); //$NON-NLS-1$
		}
		Util.appendOpeningParenthesis(this, this.scriptOutput, longLiteral, scope);
		this.scriptOutput.append(value);
		Util.appendClosingParenthesis(this.scriptOutput, longLiteral, scope);
		return false;
	}

	public boolean visit(MessageSend messageSend, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, messageSend, scope);
		Expression receiver = messageSend.receiver;
		MethodBinding method = messageSend.binding;
		ReferenceBinding declaringClass = method.declaringClass;
		boolean castReceiverToObject = 
			declaringClass.id == TypeIds.T_JavaLangString ||
			declaringClass.id == TypeIds.T_JavaLangObject ||
			declaringClass.isInterface() &&
				scope.getJavaLangObject().getExactMethod(
					method.selector, method.parameters, null) != null;
		boolean castResultToResolvedType =
			method instanceof ParameterizedMethodBinding &&
				messageSend.resolvedType.id != TypeIds.T_void &&
				!method.original().returnType.isBaseType() &&
				(method.original().returnType.isTypeVariable()
					|| getHighestOverridenMethod(method, scope.environment()).original().returnType != method.original().returnType);
		if (castResultToResolvedType) {
			char[] typeName = null;
			typeName = Util.getTypeName(messageSend.resolvedType, this.importRegistry.isAmbiguous(messageSend.resolvedType), false);
			if (CharOperation.equals(typeName, TypeConstants.OBJECT)) {
				castResultToResolvedType = false;
			} else {
				this.scriptOutput.append(typeName);
				this.scriptOutput.append('(');
			}
		}
		if (castReceiverToObject) {
			this.scriptOutput.append("Object("); //$NON-NLS-1$
		}
		if (messageSend.syntheticAccessor != null) {
			if (receiver.isSuper()) {
				receiver.traverse(this, scope);
			} else {
				Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
			}
			this.scriptOutput.append('.');
		} else {
			if (receiver.isSuper()) {
				if (declaringClass.id == TypeIds.T_JavaLangObject) {
					this.scriptOutput.append("Object.prototype"); //$NON-NLS-1$
				} else {
					this.scriptOutput.append("super");//$NON-NLS-1$
				}
				if (castReceiverToObject) {
					this.scriptOutput.append(")"); //$NON-NLS-1$
				}
				this.scriptOutput.append('.');
			} else if (method.isStatic()) {
				if (receiver != null && !receiver.isImplicitThis() && !receiver.isTypeReference()) {
					reportError(scope, "cannot handle receiver for static method invocation, should be eliminated", receiver); //$NON-NLS-1$
				}
				TypeBinding currentType = scope.classScope().referenceContext.binding;
				if (currentType != declaringClass || !scope.methodScope().isStatic) {
					Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
					if (castReceiverToObject) {
						this.scriptOutput.append(")"); //$NON-NLS-1$
					}
					this.scriptOutput.append('.');
				}
			} else {
				if (receiver.isThis() &&
						receiver.resolvedType.enclosingType() != null && 
						declaringClass != receiver.resolvedType) {
					generateOuterAccess(scope.getEmulationPath(declaringClass, false, false));
				} else {
					receiver.traverse(this, scope);
				}
				if (castReceiverToObject) {
					this.scriptOutput.append(")"); //$NON-NLS-1$
				}
				this.scriptOutput.append('.');
			}
		}
		if (messageSend.syntheticAccessor != null) {
			this.scriptOutput.append(messageSend.syntheticAccessor.selector);
		} else {
			switch (Util.checkIntrinsic(method)) {
				case Util.IS_INTRINSIC:
					Util.appendMethodName(this.scriptOutput, method, true, this.generatingConstructor);
					break;
				case Util.IS_NOT_INTRINSIC:
					if (method instanceof SyntheticMethodBinding) {
						this.scriptOutput.append(method.selector);
					} else {
						Util.appendMethodName(this.scriptOutput, method, false, this.generatingConstructor);
					}
					break;
				case Util.ERROR:
					reportError(scope, "conflicting intrinsic/non intrinsic declarations for method " + new String(method.readableName()) + " in superinterfaces", messageSend);  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		if (receiver.isSuper() && declaringClass.id == TypeIds.T_JavaLangObject) {
			this.scriptOutput.append(".call(this"); //$NON-NLS-1$
			if (messageSend.arguments != null && messageSend.arguments.length > 0) {
				this.scriptOutput.append(", "); //$NON-NLS-1$
			}
		} else {
			this.scriptOutput.append('(');
		}
		boolean extraComma = false;
		if (messageSend.syntheticAccessor != null && !method.isStatic() && !receiver.isSuper()) {
			if (receiver.isThis() &&
					receiver.resolvedType.enclosingType() != null && 
					declaringClass != receiver.resolvedType) {
				generateOuterAccess(scope.getEmulationPath(declaringClass, false, false));
			} else {
				receiver.traverse(this, scope);
			}
			extraComma = true;
		}
		if (messageSend.arguments != null) {
			if (extraComma) {
				this.scriptOutput.append(", "); //$NON-NLS-1$
			}
			messageSend.arguments[0].traverse(this, scope);
			for (int i = 1, l = messageSend.arguments.length; i < l; i++) {
				this.scriptOutput.append(", "); //$NON-NLS-1$
				messageSend.arguments[i].traverse(this, scope);
			}
		}
		this.scriptOutput.append(')');
		if (castResultToResolvedType) {
			this.scriptOutput.append(')');
		}
		Util.appendClosingParenthesis(this.scriptOutput, messageSend, scope);
		return false;
	}

	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		if (methodDeclaration.ignoreFurtherInvestigation) return false;
		if (methodDeclaration.binding == null) return false;
		if (this.generatingInterfaceFields) {
			return false;
		}
		setLineNumberBounds(methodDeclaration);
		this.localNamesRegistries.push(this.localNamesRegistry);
		this.localNamesRegistry = LocalNamesRegistry.newInstance(methodDeclaration, this.compilationUnitLocalNamesRegistry);
		appendMethod(methodDeclaration, scope);
		MethodBinding method = methodDeclaration.binding;
		if (!method.isNative() && !method.isAbstract() && (methodDeclaration.modifiers & ExtraCompilerModifiers.AccSemicolonBody) == 0) {
			this.scriptOutput.append(" {").appendNewLine();//$NON-NLS-1$
			indent();
//			if (methodDeclaration.thrownExceptions != null) {
//				int thrownExceptionsLength = methodDeclaration.thrownExceptions.length;
//				for (int i = 0; i < thrownExceptionsLength; i++)
//					methodDeclaration.thrownExceptions[i].traverse(this, methodDeclaration.scope);
//			}
			if (methodDeclaration.statements != null) {
				int statementsLength = methodDeclaration.statements.length;
				for (int i = 0; i < statementsLength; i++) {
					Statement statement = methodDeclaration.statements[i];
					if (Util.isUnreachable(statement)) break;
					visitAsStatement(statement, methodDeclaration.scope);
					if (!(statement instanceof TypeDeclaration)) this.scriptOutput.appendNewLine(); 
				}
				if (!(this.lastStatement instanceof ReturnStatement)) {
					printIndent();
					this.scriptOutput.append("return"); //$NON-NLS-1$
					switch (methodDeclaration.binding.returnType.id) {
						case TypeIds.T_void:
							break;
						case TypeIds.T_byte:
						case TypeIds.T_char:
						case TypeIds.T_short:
						case TypeIds.T_long:
						case TypeIds.T_double:
						case TypeIds.T_float:
						case TypeIds.T_int:
							this.scriptOutput.append(" 0"); //$NON-NLS-1$ // some number types could do with null
							break;
						case TypeIds.T_boolean:
							this.scriptOutput.append(" false"); //$NON-NLS-1$ // could do with null
							break;
						default:
							this.scriptOutput.append(" null"); //$NON-NLS-1$
					}
					this.scriptOutput.append(';').appendNewLine(); 
				}
			}
//			if ((methodDeclaration.bits & ASTNode.NeedFreeReturn) != 0) {
//				printIndent();
//				this.out.append("return;").appendNewLine(); //$NON-NLS-1$
//			}			
			outdent();
			printIndent();
			this.scriptOutput.append('}').appendNewLine();
		}
		this.localNamesRegistry = (LocalNamesRegistry) this.localNamesRegistries.pop();
		return false;
	}

	private void setLineNumberBounds(AbstractMethodDeclaration methodDeclaration) {
		Util.mapLine(this, this.scriptOutput, methodDeclaration, 1, this.lineSeparators == null ? 1 : this.lineSeparators.length);
		if (this.lineSeparators != null) {
			int length = this.lineSeparators.length;
			int lineSeparatorPositionsEnd = length - 1;
			if (methodDeclaration.isClinit()
					|| methodDeclaration.isConstructor()) {
				this.lineNumberStart = 1;
				this.lineNumberEnd = length == 0 ? 1 : length;
			} else {
				int start = org.eclipse.jdt.internal.compiler.util.Util.getLineNumber(methodDeclaration.bodyStart, this.lineSeparators, 0, lineSeparatorPositionsEnd);
				this.lineNumberStart = start;
				if (start > lineSeparatorPositionsEnd) {
					this.lineNumberEnd = start;
				} else {
					int end = org.eclipse.jdt.internal.compiler.util.Util.getLineNumber(methodDeclaration.bodyEnd, this.lineSeparators, start - 1, lineSeparatorPositionsEnd);
					if (end >= lineSeparatorPositionsEnd) {
						end = length;
					}
					this.lineNumberEnd = end == 0 ? 1 : end;
				}
			}
		}
	}

	public boolean visit(NullLiteral nullLiteral, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, nullLiteral);
		this.scriptOutput.append("null"); //$NON-NLS-1$
		return false;
	}

	public boolean visit(OR_OR_Expression or_or_Expression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, or_or_Expression, scope);
		or_or_Expression.left.traverse(this, scope);
		this.scriptOutput.append(' ').append(or_or_Expression.operatorToString()).append(' ');
		or_or_Expression.right.traverse(this, scope);
		Util.appendClosingParenthesis(this.scriptOutput, or_or_Expression, scope);
		return false;
	}

	public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, parameterizedQualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(parameterizedQualifiedTypeReference.resolvedType));
		return false;
	}
	
	public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, parameterizedQualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(parameterizedQualifiedTypeReference.resolvedType));
		return false;
	}

	public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, parameterizedSingleTypeReference.resolvedType, this.importRegistry.isAmbiguous(parameterizedSingleTypeReference.resolvedType));
		return false;
	}

	public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, parameterizedSingleTypeReference.resolvedType, this.importRegistry.isAmbiguous(parameterizedSingleTypeReference.resolvedType));
		return false;
	}

	public boolean visit(PostfixExpression postfixExpression, BlockScope scope) {
		if (Util.isUnBoxing(postfixExpression) || Util.isUnBoxing(postfixExpression.lhs)) {
			// i++ where 'i' is an integer
			int numberOfParenthesis = (postfixExpression.bits & ASTNode.ParenthesizedMASK) >> ASTNode.ParenthesizedSHIFT;
			for (int i = 0; i < numberOfParenthesis; i++) {
				this.scriptOutput.append('(');
			}
			TypeBinding unboxingType = scope.environment().computeBoxingType(postfixExpression.resolvedType);
			MethodBinding constructor = Util.retrieveConstructor(unboxingType, (ReferenceBinding) postfixExpression.resolvedType);
			if (constructor == null) return false;
			Util.dumpWrapperType(this.scriptOutput, postfixExpression.resolvedType, constructor);
			postfixExpression.lhs.traverse(this, scope);
			switch(postfixExpression.operator) {
			case OperatorIds.PLUS :
				this.scriptOutput.append('+');
				break;
			case OperatorIds.MINUS :
				this.scriptOutput.append('-');
			}
			this.scriptOutput.append('1');
			this.scriptOutput.append("]))"); //$NON-NLS-1$
			Util.appendClosingParenthesis(this.scriptOutput, postfixExpression, scope);
		} else {
			Util.appendOpeningParenthesis(this, this.scriptOutput, postfixExpression, scope);
			postfixExpression.lhs.traverse(this, scope);
			this.scriptOutput.append(postfixExpression.operatorToString());
			Util.appendClosingParenthesis(this.scriptOutput, postfixExpression, scope);
		}
		return false;
	}

	public boolean visit(PrefixExpression prefixExpression, BlockScope scope) {
		if (Util.isUnBoxing(prefixExpression)) {
			// ++i where 'i' is an integer
			TypeBinding unboxingType = scope.environment().computeBoxingType(prefixExpression.resolvedType);
			MethodBinding constructor = Util.retrieveConstructor(unboxingType, (ReferenceBinding) prefixExpression.resolvedType);
			if (constructor == null) return false;
			Util.dumpWrapperType(this.scriptOutput, prefixExpression.resolvedType, constructor);
			prefixExpression.lhs.traverse(this, scope);
			switch(prefixExpression.operator) {
			case OperatorIds.PLUS :
				this.scriptOutput.append('+');
				break;
			case OperatorIds.MINUS :
				this.scriptOutput.append('-');
			}
			this.scriptOutput.append('1');
			this.scriptOutput.append("]))"); //$NON-NLS-1$
			Util.appendClosingParenthesis(this.scriptOutput, prefixExpression, scope);
		} else {
			Util.appendOpeningParenthesis(this, this.scriptOutput, prefixExpression, scope);
			this.scriptOutput.append(prefixExpression.operatorToString());
			prefixExpression.lhs.traverse(this, scope);
			Util.appendClosingParenthesis(this.scriptOutput, prefixExpression, scope);
		}
		return false;
	}
	
	public boolean visit(QualifiedAllocationExpression allocationExpression, BlockScope scope) {
		if (allocationExpression.anonymousType != null) {
			visit(allocationExpression.anonymousType, scope.compilationUnitScope());
		}
		if (allocationExpression.enclosingInstance == null || allocationExpression.enclosingInstance.isThis()) {
			return visit((AllocationExpression) allocationExpression, scope);
		} else {
			ReferenceBinding allocatedType =  allocationExpression.binding.declaringClass;
			boolean generateExtraComma = false, generateExtraParenthesis = false;
			this.scriptOutput.append("new "); //$NON-NLS-1$
			Util.appendTypeName(this.scriptOutput, allocatedType, this.importRegistry.isAmbiguous(allocatedType));
			this.scriptOutput.append('(');
			allocationExpression.enclosingInstance.traverse(this, scope);
			SyntheticArgumentBinding syntheticArguments[];
			if ((syntheticArguments = allocatedType.syntheticOuterLocalVariables()) != null) {
				Object[] emulationPath = scope.getEmulationPath(syntheticArguments[0].actualOuterLocalVariable);
				if (emulationPath.length > 0) {
					generateOuterAccess(emulationPath);
					generateExtraComma = true;
				}
				for (int i = 1, max = syntheticArguments.length; i < max; i++) {
					if (generateExtraComma) {
						this.scriptOutput.append(", ");//$NON-NLS-1$
					} else {
						generateExtraComma = true;
					}
					generateOuterAccess(scope.getEmulationPath(syntheticArguments[i].actualOuterLocalVariable));
				}
			}
			if (allocationExpression.arguments != null) {
				for (int i = 0, length = allocationExpression.arguments.length; i < length; i++) {
					if (generateExtraComma) {
						this.scriptOutput.append(", ");//$NON-NLS-1$
					} else {
						generateExtraComma = true;
					}
					allocationExpression.arguments[i].traverse(this, scope);
				}
			}
			this.scriptOutput.append(')');
			if (generateExtraParenthesis) {
				this.scriptOutput.append(')');
			}
		}
		return false;
	}
	
	public boolean visit(QualifiedNameReference qualifiedNameReference, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, qualifiedNameReference, scope);
		try {
			char[][] receiverPackageName = Util.split(qualifiedNameReference.actualReceiverType.qualifiedPackageName(), '.');
			boolean fullyQualified = Util.shouldFullyQualified(qualifiedNameReference);
			int bindingKind = qualifiedNameReference.binding.kind();
			boolean fieldOrLocal = bindingKind == Binding.FIELD || bindingKind == Binding.LOCAL;
			if (!fieldOrLocal) {
				Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, fullyQualified);
			}
			if (fieldOrLocal) {
				// PREMATURE should be able to optimize by leveraging the tokens
				Constant constant = ((VariableBinding) qualifiedNameReference.binding).constant();
				if (constant != Constant.NotAConstant && !Util.isIntrinsic(receiverPackageName)) {
					String errorMessage = Util.appendConstant(this.scriptOutput, constant);
					if (errorMessage != null) {
	//					reportError(scope, errorMessage, qualifiedNameReference);
						reportWarning(scope, "SHOULD BE ERROR - " + errorMessage, qualifiedNameReference); //$NON-NLS-1$
					}
					return false;
				}
				if (qualifiedNameReference.binding.kind() == Binding.LOCAL) {
					if ((qualifiedNameReference.bits & ASTNode.DepthMASK) != 0) {
						generateOuterAccess(scope.getEmulationPath((LocalVariableBinding) qualifiedNameReference.binding));
					} else {
						this.scriptOutput.append(this.localNamesRegistry.targetName(
							(LocalVariableBinding) qualifiedNameReference.binding));
					}
				} else {
					// PREMATURE may have issues with accessors (bug 22)
					MethodBinding syntheticAccessor = qualifiedNameReference.syntheticReadAccessors == null ? null :
						qualifiedNameReference.syntheticReadAccessors[0]; // further read accessors supposedly match other bindings
					if (syntheticAccessor != null) {
						Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, fullyQualified);
						this.scriptOutput.append('.');
						this.scriptOutput.append(syntheticAccessor.selector);
						this.scriptOutput.append('(');
						generateOuterAccess(scope.getEmulationPath(qualifiedNameReference.fieldBinding().original().declaringClass, false, false));
						this.scriptOutput.append(')');
					} else {
						if ((qualifiedNameReference.bits & ASTNode.DepthMASK) != 0) {
							if (((FieldBinding)qualifiedNameReference.binding).isStatic()) {
								Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, true);
							} else {
								generateOuterAccess(scope.getEmulationPath(qualifiedNameReference.fieldBinding().original().declaringClass, false, false));
							}
							this.scriptOutput.append('.');
						} else if (qualifiedNameReference.indexOfFirstFieldBinding > 1) {
							if (qualifiedNameReference.actualReceiverType.isInterface()) {
								Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, true);
								this.scriptOutput.append(INTERFACE_FIELDS);
							} else {
								Util.appendTypeName(this.scriptOutput, qualifiedNameReference.actualReceiverType, fullyQualified);
							}
							this.scriptOutput.append('.');
						}
						this.scriptOutput.append(this.fieldNamesRegistry.targetName((FieldBinding) qualifiedNameReference.binding));
					}
				}
				if (qualifiedNameReference.otherBindings != null) {
					for (int i = 0, l = qualifiedNameReference.otherBindings.length; i < l; i++) {
						this.scriptOutput.append('.');
						this.scriptOutput.append(qualifiedNameReference.otherBindings[i].shortReadableName());
					}
				}
			}
		} finally {
			Util.appendClosingParenthesis(this.scriptOutput, qualifiedNameReference, scope);
		}
		return false;
	}
	
	public boolean visit(QualifiedSuperReference qualifiedSuperReference, BlockScope scope) {
		generateOuterAccess(scope.getEmulationPath((ReferenceBinding) qualifiedSuperReference.resolvedType, false, false));
		return false;
	}
	
	public boolean visit(QualifiedThisReference qualifiedThisReference, BlockScope scope) {
		generateOuterAccess(scope.getEmulationPath((ReferenceBinding) qualifiedThisReference.resolvedType, false, false));
		return false;
	}
	
	public boolean visit(QualifiedTypeReference qualifiedTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, qualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(qualifiedTypeReference.resolvedType));
		return false;
	}

	public boolean visit(QualifiedTypeReference qualifiedTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, qualifiedTypeReference.resolvedType, this.importRegistry.isAmbiguous(qualifiedTypeReference.resolvedType));
		return false;
	}

	public boolean visit(ReturnStatement returnStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, returnStatement);
		if (returnStatement != this.unIndentedStatement) printIndent();
		if (returnStatement.expression == null) {
			AbstractMethodDeclaration method = scope.methodScope().referenceMethod();
			if (method.isConstructor() && !this.generatingConstructor) {
				this.scriptOutput.append("return this;");//$NON-NLS-1$
			} else {
				this.scriptOutput.append("return;");//$NON-NLS-1$
			}
		} else {
			this.scriptOutput.append("return ");//$NON-NLS-1$
			returnStatement.expression.traverse(this, scope);
			this.scriptOutput.append(';'); 
		}
		this.lastStatement = returnStatement;
		return false;
	}

	public boolean visit(SingleNameReference singleNameReference, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, singleNameReference, scope);
		try {
			// we need to address constants since their emulation path is null
			if (singleNameReference.constant != Constant.NotAConstant) {
				String errorMessage = Util.appendConstant(this.scriptOutput, singleNameReference.constant);
				if (errorMessage != null) {
	//				reportError(scope, errorMessage, singleNameReference);
					reportWarning(scope, "SHOULD BE ERROR - " + errorMessage, singleNameReference); //$NON-NLS-1$
				}
				return false;
			}
			switch (singleNameReference.binding.kind()) {
				case Binding.FIELD : // reading a field
					// shortcut for interface fields
					FieldBinding fieldBinding = singleNameReference.fieldBinding().original();
					if (fieldBinding.declaringClass.isInterface()) {
						Util.appendTypeName(this.scriptOutput, fieldBinding.declaringClass, true);
						this.scriptOutput.append(INTERFACE_FIELDS);
						this.scriptOutput.append('.');
						this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldBinding));
						return false;
					}
					// fields from regular classes
					fieldBinding = singleNameReference.fieldBinding().original();
					// PREMATURE reconsider optimizing access to constants
	//				Constant fieldConstant = fieldBinding.constant();
	//				if (fieldConstant != Constant.NotAConstant) {
	//				}
					if (fieldBinding.isStatic()) {
						MethodBinding syntheticAccessor = singleNameReference.syntheticAccessors == null ? null :
							singleNameReference.syntheticAccessors[SingleNameReference.READ];
						if (syntheticAccessor == null) {
							if ((singleNameReference.bits & ASTNode.DepthMASK) != 0) {
								// generate absolute path
								Util.appendTypeName(this.scriptOutput, fieldBinding.declaringClass, true);
								this.scriptOutput.append('.');
							} else if (fieldBinding.declaringClass != scope.referenceType().binding) {
								Util.appendTypeName(this.scriptOutput, fieldBinding.declaringClass, this.importRegistry.isAmbiguous(fieldBinding.declaringClass));
								this.scriptOutput.append('.');
							}
							this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldBinding));
						} else {
							TypeBinding declaringClass = syntheticAccessor.declaringClass;
							Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
							this.scriptOutput.append('.');
							this.scriptOutput.append(syntheticAccessor.selector);
							this.scriptOutput.append("()"); //$NON-NLS-1$
						}
					} else {
						MethodBinding syntheticAccessor = singleNameReference.syntheticAccessors == null ? null :
							singleNameReference.syntheticAccessors[SingleNameReference.READ];
						// managing enclosing instance access - PREMATURE only works for read access for now, would have to rework assignment etc.
						if ((singleNameReference.bits & ASTNode.DepthMASK) != 0) {
							if (syntheticAccessor != null) {
								TypeBinding declaringClass = syntheticAccessor.declaringClass;
								Util.appendTypeName(this.scriptOutput, declaringClass, this.importRegistry.isAmbiguous(declaringClass));
								this.scriptOutput.append('.');
								this.scriptOutput.append(syntheticAccessor.selector);
								this.scriptOutput.append('(');
							}
							generateOuterAccess(scope.getEmulationPath(singleNameReference.fieldBinding().original().declaringClass, false, false));
							if (syntheticAccessor == null) {
								this.scriptOutput.append('.');
							} else {
								this.scriptOutput.append(')');
							}
						}
						if (syntheticAccessor == null) {
							this.scriptOutput.append(this.fieldNamesRegistry.targetName(fieldBinding));
						}
					}
					break;
				case Binding.LOCAL : // reading a local
					if ((singleNameReference.bits & ASTNode.DepthMASK) != 0) {
						generateOuterAccess(scope.getEmulationPath(singleNameReference.localVariableBinding()));
					} else {
						this.scriptOutput.append(this.localNamesRegistry.targetName(
							(LocalVariableBinding) singleNameReference.binding));
					}
					break;
				default: // type
			}
		} finally {
			Util.appendClosingParenthesis(this.scriptOutput, singleNameReference, scope);
		}
		return false;
	}

	public boolean visit(SingleTypeReference singleTypeReference, BlockScope scope) {
		Util.appendTypeName(this.scriptOutput, singleTypeReference.resolvedType, this.importRegistry.isAmbiguous(singleTypeReference.resolvedType));
		return false;
	}

	public boolean visit(SingleTypeReference singleTypeReference, ClassScope scope) {
		Util.appendTypeName(this.scriptOutput, singleTypeReference.resolvedType, this.importRegistry.isAmbiguous(singleTypeReference.resolvedType));
		return false;
	}

	public boolean visit(StringLiteral stringLiteral, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, stringLiteral);
		if (stringLiteral.constant == null) {
			stringLiteral.computeConstant();
		}
		Util.appendString(this.scriptOutput, stringLiteral.constant.stringValue());
		return false;
	}

	public boolean visit(SwitchStatement switchStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, switchStatement);
		if (switchStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("switch ("); //$NON-NLS-1$
		Expression expression = switchStatement.expression;
		expression.traverse(this, scope);
		if (expression.resolvedType.isEnum()) {
			this.scriptOutput.append(".ordinal__()"); //$NON-NLS-1$
		}
		this.scriptOutput.append(") {").appendNewLine(); //$NON-NLS-1$
		Statement[] statements = switchStatement.statements;
		int caseIndex = 0, caseCount = switchStatement.caseCount;
		for (int i = 0, length = statements == null ? 0 : statements.length; i < length; i++) {
			Statement statement = statements[i];
			if (Util.isUnreachable(statement)) break;
			if ((caseIndex < caseCount && statement == switchStatement.cases[caseIndex])  // statement is a case:
					|| statement == switchStatement.defaultCase) { // statement is default:
				caseIndex++;
				indent();
				visitAsStatement(statement, switchStatement.scope);
				outdent();
			} else {
				indent();
				indent();
				visitAsStatement(statement, switchStatement.scope);
				outdent();
				outdent();
			}
			this.scriptOutput.appendNewLine(); 
		}
		printIndent();
		this.scriptOutput.append('}'); 
		return false;
	}

	// PREMATURE consider whether we want to do better than skipping the whole sync story
	public boolean visit(SynchronizedStatement synchronizedStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, synchronizedStatement);
		synchronizedStatement.block.traverse(this, scope);
		return false;
	}
	public boolean visit(ThisReference thisReference, BlockScope scope) {
		this.scriptOutput.append("this");//$NON-NLS-1$
		return false;
	}

	public boolean visit(ThrowStatement throwStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, throwStatement);
		if (throwStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("throw "); //$NON-NLS-1$
		throwStatement.exception.traverse(this, scope);
		this.scriptOutput.append(';'); 
		return false;
	}
	
	public boolean visit(TrueLiteral truelLiteral, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, truelLiteral);
		this.scriptOutput.append("true"); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TryStatement tryStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, tryStatement);
		if (tryStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("try"); //$NON-NLS-1$
		Block tryBlock = tryStatement.tryBlock;
		this.scriptOutput.append(' ');
		this.unIndentedStatement = tryBlock;
		visitAsExpression(tryBlock, scope); // avoid trailing line break
		if (tryStatement.catchArguments != null) {
			for (int i = 0, length = tryStatement.catchArguments.length; i < length; i++) {
				Argument catchArgument = tryStatement.catchArguments[i];
				Block catchBlock = tryStatement.catchBlocks[i];
				this.scriptOutput.append(" catch ("); //$NON-NLS-1$
				this.scriptOutput.append(this.localNamesRegistry.targetName(catchArgument.binding));
				this.scriptOutput.append(" : "); //$NON-NLS-1$
				Util.appendTypeName(this.scriptOutput, catchArgument.type.resolvedType, this.importRegistry.isAmbiguous(catchArgument.type.resolvedType));
				this.scriptOutput.append(") "); //$NON-NLS-1$
				this.unIndentedStatement = catchBlock;
				visitAsExpression(catchBlock, scope); // avoid trailing line break
			}
		} 
		if (tryStatement.finallyBlock != null) {
				this.scriptOutput.append(" finally "); //$NON-NLS-1$
				this.unIndentedStatement = tryStatement.finallyBlock;
				visitAsStatement(tryStatement.finallyBlock, scope); 
		}
		return false;
	}

	public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
		return visit(localTypeDeclaration, scope.enclosingClassScope());
	}

	public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
		return visit(memberTypeDeclaration, scope.compilationUnitScope());
	}
	boolean generatingInterfaceFields = false; // PREMATURE will need a stack for inner types
	public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
		if (typeDeclaration.ignoreFurtherInvestigation) return false;
		if (typeDeclaration.binding == null) return false;
		
		switch (typeDeclaration.binding.id) {
			case TypeIds.T_JavaLangObject:
			case TypeIds.T_JavaLangString:
				return false;
		}
		int loops = 1;
		int kind = TypeDeclaration.kind(typeDeclaration.modifiers);
		switch (kind) {
			case TypeDeclaration.INTERFACE_DECL :
				this.generatingInterfaceFields = true;
				loops++;
				//$FALL-THROUGH$
			case TypeDeclaration.CLASS_DECL :
				break;
			case TypeDeclaration.ANNOTATION_TYPE_DECL:
				return false;
			case TypeDeclaration.ENUM_DECL:
				break;
		}
		if (this.actionScriptSourceFile != null) {
			this.actionScriptSourceFiles.push(this.actionScriptSourceFile);
		}
		if (this.constructorGenerator != null) {
			this.constructorGenerators.push(this.constructorGenerator);
		}
		this.constructorGenerator = new ActionScriptConstructorGenerator(this);
		while (loops-- > 0) {
			this.actionScriptSourceFile = new ActionScriptSourceFile(
					Util.typeName(typeDeclaration.binding, true).replace('.', File.separatorChar)
						+ (this.generatingInterfaceFields ? INTERFACE_FIELDS + ".as" : ".as"), //$NON-NLS-1$ //$NON-NLS-2$
					typeDeclaration.binding);
			this.scriptOutput = this.actionScriptSourceFile.actionScriptContents;
			this.scriptOutput.append("package ");//$NON-NLS-1$
			if (this.currentPackage != null) {
				char[][] tokens = this.currentPackage.tokens;
				this.scriptOutput.append(tokens[0]);
				for (int i = 1, length = tokens.length; i < length; i++) {
					this.scriptOutput.append('.');
					this.scriptOutput.append(tokens[i]);
				}
				this.scriptOutput.append(' ');
			}
			this.scriptOutput.append("{").appendNewLine();//$NON-NLS-1$
			flushImports(scope);
			Util.mapLine(this, this.scriptOutput, typeDeclaration, 1, this.lineSeparators == null ? 1 : this.lineSeparators.length);
			if (kind == TypeDeclaration.CLASS_DECL) {
				this.scriptOutput.append("dynamic ");//$NON-NLS-1$
			}
			// PREMATURE all classes made public; will need to revisit for inner classes
			this.scriptOutput.append("public ");//$NON-NLS-1$
			if (kind == TypeDeclaration.INTERFACE_DECL && !this.generatingInterfaceFields) {
				this.scriptOutput.append("interface ");//$NON-NLS-1$
			} else {
				this.scriptOutput.append("class ");//$NON-NLS-1$
			}
			Util.appendTypeName(this.scriptOutput, typeDeclaration.binding, false);
			if (this.generatingInterfaceFields) {
				this.scriptOutput.append(INTERFACE_FIELDS);
			} else {
				if (typeDeclaration.binding.superclass.id != TypeIds.T_JavaLangObject) {
					this.scriptOutput.append(" extends ");//$NON-NLS-1$
					Util.appendTypeName(this.scriptOutput, typeDeclaration.binding.superclass, this.importRegistry.isAmbiguous(typeDeclaration.binding.superclass));
				}
				if (typeDeclaration.binding.superInterfaces != Binding.NO_SUPERINTERFACES) {
					if (kind == TypeDeclaration.INTERFACE_DECL) {
						this.scriptOutput.append(" extends "); //$NON-NLS-1$
					} else {
						this.scriptOutput.append(" implements "); //$NON-NLS-1$		
					}
					for (int i = 0, length = typeDeclaration.binding.superInterfaces.length; i < length; i++) {
						if (i > 0) this.scriptOutput.append(", "); //$NON-NLS-1$
						Util.appendTypeName(this.scriptOutput, typeDeclaration.binding.superInterfaces[i], this.importRegistry.isAmbiguous(typeDeclaration.binding.superInterfaces[i]));
					}
				}
			}
			this.scriptOutput.append(" {").appendNewLine();//$NON-NLS-1$
			indent();
			if (!this.generatingInterfaceFields) {
				if (typeDeclaration.memberTypes != null) {
					int length = typeDeclaration.memberTypes.length;
					for (int i = 0; i < length; i++)
						typeDeclaration.memberTypes[i].traverse(this, typeDeclaration.scope);
				}
			}
			boolean needClinit = false;
			if (typeDeclaration.fields != null) {
				int length = typeDeclaration.fields.length;
				for (int i = 0; i < length; i++) {
					FieldDeclaration field = typeDeclaration.fields[i];
					if (field.getKind() == AbstractVariableDeclaration.INITIALIZER) {// initializers to be inlined in constructors
						if (field.isStatic()) needClinit = true;
						continue;
					}
					if (field.isStatic()) {
						field.traverse(this, typeDeclaration.staticInitializerScope);
					} else {
						field.traverse(this, typeDeclaration.initializerScope);
					}
				}
				// PREMATURE reconsider field declaration grouping
			}
			if (!this.generatingInterfaceFields) {
				FieldBinding[] syntheticFields = typeDeclaration.binding.syntheticFields();
				for (int i = 0, l = syntheticFields == null ? 0 : syntheticFields.length; i < l; i++) {
					FieldBinding fieldBinding = syntheticFields[i];
					if (Util.isExcluded(fieldBinding)) {
						continue;
					}
					printIndent();
					this.scriptOutput.append("private ");//$NON-NLS-1$
					if (fieldBinding.isStatic()) {
						this.scriptOutput.append("static ");//$NON-NLS-1$
					}
					this.scriptOutput.append("var ");//$NON-NLS-1$
					this.scriptOutput.append(fieldBinding.name);
					this.scriptOutput.append(": ");//$NON-NLS-1$
					Util.appendTypeName(this.scriptOutput, fieldBinding.type, this.importRegistry.isAmbiguous(fieldBinding.type));
					switch (fieldBinding.type.id) {
						case TypeIds.T_long:
						case TypeIds.T_float:
						case TypeIds.T_double:
							this.scriptOutput.append(" = 0"); //$NON-NLS-1$
					}	
					this.scriptOutput.append(';').appendNewLine(); 
				}
				// copy methods from type comment which MUST BE the first comment inside a type declaration
				CompilationUnitDeclaration unit = scope.referenceCompilationUnit();
				int typeStart = typeDeclaration.declarationSourceStart;
				int typeEnd = typeDeclaration.declarationSourceEnd;
				for (int i = 0, l = unit.comments.length; i < l; i++) {
					int commentEnd = -unit.comments[i][1];
					if (commentEnd < 0) continue; // javadoc comment
					int commentStart = unit.comments[i][0];
					if (commentStart > typeStart && commentEnd < typeEnd) {
						char[] source = unit.compilationResult.compilationUnit.getContents();
						// /*+ represent the beginning of code we want to append AND +*/ is the end
						if (commentEnd - commentStart < 7) break;
						if (source[commentStart] != '/' || source[commentStart + 1] != '*' || source[commentStart + 2] != '+') break;
						if (source[commentEnd - 1] != '/' || source[commentEnd - 2] != '*' || source[commentEnd - 3] != '+') break;
						String nativeSource = String.copyValueOf(source, commentStart + 3, commentEnd - commentStart - 6);
						this.scriptOutput.append(nativeSource);
						this.scriptOutput.appendNewLine();
						break;
					}
				}
			
				final HashMap definedMethods;
				if (typeDeclaration.binding.isAbstract() &&
						!typeDeclaration.binding.isInterface() &&
						typeDeclaration.superInterfaces != null &&
						typeDeclaration.superInterfaces.length > 0) { // PREMATURE one of the two tests here might be enough
					definedMethods = new HashMap();
					ReferenceBinding binding = typeDeclaration.binding;
					while (binding != null) {
						MethodBinding[] methodBindings = binding.methods();
						if (methodBindings != null) {
							for (int i = 0; i < methodBindings.length; i++) {
								String key = Util.methodKey(methodBindings[i]);
								definedMethods.put(key, key);
							}
						}
						binding = binding.superclass();
					}
				} else {
					definedMethods = null;
				}
				AbstractMethodDeclaration[] methods = typeDeclaration.methods;
				if (methods != null) {
					int length = typeDeclaration.methods.length;
					if (needClinit) {
						this.scriptOutput.appendNewLine(); 
						printIndent();
						this.scriptOutput.append("__clinit();").appendNewLine().appendNewLine(); //$NON-NLS-1$
					}
					for (int i = 0; i < length; i++) {
						methods[i].traverse(this, typeDeclaration.scope);
					}
				}
				// all remaining code is related to synthetic methods
				this.scriptOutput.setCurrentMappedLineNumber(0);
				if (definedMethods != null) {
					class AbstractMethodsGenerator {
						ReferenceBinding abstractType;
						AbstractMethodsGenerator(ReferenceBinding abstractType) {
							this.abstractType = abstractType;
						}
						public boolean visit(ReferenceBinding type, ClassScope scope_) {
							if (type.isInterface()) {
								MethodBinding[] availableMethods = type.availableMethods(); // PREMATURE check whether methods can be null or not
								for (int i = 0, l = availableMethods == null ? 0 : availableMethods.length; i < l; i++) {
									String key = Util.methodKey(availableMethods[i]);
									// PREMATURE consider interning these
									if (definedMethods.get(key) ==  null) {
										MethodBinding method = availableMethods[i];
										// PREMATURE trick; if we had an AS AST, we could add appropriate methods on it and generate
										method.declaringClass = this.abstractType;
										appendMethod(method, scope_);
										method.declaringClass = type;
										definedMethods.put(key, key);
									}
								}
							}
							ReferenceBinding[] superInterfaces = type.superInterfaces();
							for (int i = 0, l = superInterfaces == null ? 0 : superInterfaces.length; i < l; i++) {
								visit(superInterfaces[i], scope_);
							}
							return false;
						}
					}
					new AbstractMethodsGenerator(typeDeclaration.binding).visit(typeDeclaration.binding, typeDeclaration.scope);
				}
				SyntheticMethodBinding[] syntheticMethods = typeDeclaration.binding.syntheticMethods();
				for (int i = 0, l = syntheticMethods == null ? 0 : syntheticMethods.length; i < l; i++) {
					SyntheticMethodBinding synthetic = syntheticMethods[i];
					if (synthetic.parameters.length == 0 && !synthetic.isStatic())
						if (synthetic.targetMethod != null && synthetic.declaringClass == synthetic.targetMethod.declaringClass)
							continue; // CANNOT add bridge method if only return type is different
					switch(synthetic.purpose) {
						case SyntheticMethodBinding.SwitchTable :
							continue;
					}
					if (!synthetic.isConstructor()) {
						printIndent();
						if (synthetic.purpose != SyntheticMethodBinding.SuperMethodAccess) {
							if (synthetic.purpose == SyntheticMethodBinding.BridgeMethod) {
								this.scriptOutput.append("public "); //$NON-NLS-1$
								if (Util.isOverriding(synthetic)) {
									this.scriptOutput.append("override "); //$NON-NLS-1$
								}
								this.scriptOutput.append("function "); //$NON-NLS-1$
								Util.appendMethodName(this.scriptOutput, synthetic, false, false);
							} else {
								this.scriptOutput.append("public static function "); //$NON-NLS-1$
								this.scriptOutput.append(synthetic.selector);
							}
							this.scriptOutput.append('(');
							int argumentsCount = synthetic.parameters == null ? 0 : synthetic.parameters.length;
							for (int j = 0; j < argumentsCount; j++) {
								TypeBinding argument = synthetic.parameters[j];
								if (j > 0) {
									this.scriptOutput.append(", ");//$NON-NLS-1$
								}
								this.scriptOutput.append("arg"); //$NON-NLS-1$
								this.scriptOutput.append(j);
								this.scriptOutput.append(": ");//$NON-NLS-1$
								Util.appendTypeName(this.scriptOutput, argument, this.importRegistry.isAmbiguous(argument));
							}
							this.scriptOutput.append("): "); //$NON-NLS-1$
							Util.appendTypeName(this.scriptOutput, synthetic.returnType, this.importRegistry.isAmbiguous(synthetic.returnType));
							this.scriptOutput.append(" {").appendNewLine(); //$NON-NLS-1$
							indent();
							switch (synthetic.purpose) {
								case SyntheticMethodBinding.FieldReadAccess:
									printIndent();
									this.scriptOutput.append(argumentsCount == 0 ? "return " : "return arg0."); //$NON-NLS-1$  //$NON-NLS-2$
									this.scriptOutput.append(synthetic.targetReadField.name);
									this.scriptOutput.append(';').appendNewLine(); 
									break;
								case SyntheticMethodBinding.FieldWriteAccess:
									// generate a method info to emulate an writing access to
									// a non-accessible field
									printIndent();
									FieldBinding targetWriteField = synthetic.targetWriteField;
									if (targetWriteField.isStatic()) {
										this.scriptOutput.append(targetWriteField.name);
										this.scriptOutput.append(" = "); //$NON-NLS-1$
										this.scriptOutput.append("arg0;").appendNewLine(); //$NON-NLS-1$
									} else {
										this.scriptOutput.append("arg0."); //$NON-NLS-1$
										this.scriptOutput.append(targetWriteField.name);
										this.scriptOutput.append(" = "); //$NON-NLS-1$
										this.scriptOutput.append("arg1;").appendNewLine(); //$NON-NLS-1$
									}
									break;
								case SyntheticMethodBinding.MethodAccess:
								case SyntheticMethodBinding.BridgeMethod:
									// generate a method info to emulate an access to a non-accessible method
									printIndent();
									if (synthetic.targetMethod.returnType.id != TypeIds.T_void) {
										this.scriptOutput.append("return "); //$NON-NLS-1$
									}
									int argumentsStart;
									if (synthetic.targetMethod.isStatic()) {
										argumentsStart = 0;
									} else {
										this.scriptOutput.append("arg0."); //$NON-NLS-1$
										argumentsStart = 1;
									}
									Util.appendMethodName(this.scriptOutput, synthetic.targetMethod, false, false);
									this.scriptOutput.append('(');
									if (argumentsCount > argumentsStart) {
										this.scriptOutput.append("arg" + argumentsStart); //$NON-NLS-1$
										for (int j = argumentsStart + 1; j < argumentsCount; j++) {
											this.scriptOutput.append(", arg"); //$NON-NLS-1$
											this.scriptOutput.append(j);
										}
									}
									this.scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
									break;
								case SyntheticMethodBinding.ConstructorAccess:
									// generate a method info to emulate an access to a non-accessible constructor
				//					addSyntheticConstructorAccessMethod(syntheticMethod);
									break;
								case SyntheticMethodBinding.EnumValues:
									// generate a method info to define <enum>#values()
									// generate each enum constant initialization
									FieldBinding[] enumConstants = Util.getEnumConstants(typeDeclaration.binding);
									int numberOfEnumConstants = enumConstants.length;
									this.printIndent();
									this.scriptOutput.append("if (ENUM$VALUES === null) {").appendNewLine(); //$NON-NLS-1$
									this.indent();
									this.printIndent();
									this.scriptOutput.append("var "); //$NON-NLS-1$
									this.scriptOutput.append(TAB);
									this.scriptOutput.append(" : JavaArray = new JavaArray(\"[L"); //$NON-NLS-1$
									this.scriptOutput.append(typeDeclaration.binding.constantPoolName());
									this.scriptOutput.append(";\").lengths("); //$NON-NLS-1$
									this.scriptOutput.append(numberOfEnumConstants);
									this.scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
									for (int j = 0, max = numberOfEnumConstants; j < max; j++) {
										FieldBinding fieldBinding = enumConstants[j];
										this.printIndent();
										this.localNamesRegistry.addTargetName(TAB);
										this.scriptOutput.append(TAB);
										this.scriptOutput.append('[');
										this.scriptOutput.append(j);
										this.scriptOutput.append("] = "); //$NON-NLS-1$
										this.scriptOutput.append(fieldBinding.name);
										this.scriptOutput.append(';').appendNewLine(); 
									}
									this.printIndent();
									this.scriptOutput.append("ENUM$VALUES = "); //$NON-NLS-1$
									this.scriptOutput.append(TAB);
									this.scriptOutput.append(';').appendNewLine(); 
									this.outdent();
									this.printIndent();
									this.scriptOutput.append('}').appendNewLine(); 
									this.printIndent();
									this.scriptOutput.append("var length: int = ENUM$VALUES.length;").appendNewLine(); //$NON-NLS-1$
									this.printIndent();
									this.scriptOutput.append("var "); //$NON-NLS-1$
									this.scriptOutput.append(TAB2);
									this.scriptOutput.append(" : JavaArray = new JavaArray(\"[L"); //$NON-NLS-1$
									this.scriptOutput.append(typeDeclaration.binding.constantPoolName());
									this.scriptOutput.append(";\").lengths("); //$NON-NLS-1$
									this.scriptOutput.append(numberOfEnumConstants);
									this.scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
									this.printIndent();
									this.scriptOutput.append("System.arraycopy__Ljava_lang_Object_2ILjava_lang_Object_2II(ENUM$VALUES, 0, "); //$NON-NLS-1$
									this.scriptOutput.append(TAB2);
									this.scriptOutput.append(", 0, length);").appendNewLine(); //$NON-NLS-1$
									this.printIndent();
									this.scriptOutput.append("return "); //$NON-NLS-1$
									this.scriptOutput.append(TAB2);
									this.scriptOutput.append(';').appendNewLine(); 
									break;
								case SyntheticMethodBinding.EnumValueOf:
									// generate a method info to define <enum>#valueOf(String)
									this.printIndent();
									this.scriptOutput.append("return "); //$NON-NLS-1$
									char[] name = typeDeclaration.binding.constantPoolName();
									name = CharOperation.replaceOnCopy(name, '/', '.');
									this.scriptOutput.append(name);
									this.scriptOutput.append("(Enum.valueOf__Ljava_lang_Class_2Ljava_lang_String_2(Class__.forName__Ljava_lang_String_2(\""); //$NON-NLS-1$
									this.scriptOutput.append(name);
									this.scriptOutput.append("\"), arg0));").appendNewLine();  //$NON-NLS-1$
									break;
								case SyntheticMethodBinding.SwitchTable:
									// generate a method info to define the switch table synthetic method
				//					addSyntheticSwitchTable(syntheticMethod);
									break;
							}
						} else {
							this.scriptOutput.append("public function "); //$NON-NLS-1$
							this.scriptOutput.append(synthetic.selector);
							this.scriptOutput.append('(');
							int argumentsCount = synthetic.parameters == null ? 0 : synthetic.parameters.length;
							for (int j = 1; j < argumentsCount; j++) {
								TypeBinding argument = synthetic.parameters[j];
								if (j > 1) {
									this.scriptOutput.append(", ");//$NON-NLS-1$
								}
								this.scriptOutput.append("arg"); //$NON-NLS-1$
								this.scriptOutput.append(j);
								this.scriptOutput.append(": ");//$NON-NLS-1$
								Util.appendTypeName(this.scriptOutput, argument, this.importRegistry.isAmbiguous(argument));
							}
							this.scriptOutput.append("): "); //$NON-NLS-1$
							Util.appendTypeName(this.scriptOutput, synthetic.returnType, this.importRegistry.isAmbiguous(synthetic.returnType));
							this.scriptOutput.append(" {").appendNewLine(); //$NON-NLS-1$
							indent();
							printIndent();
							if (synthetic.targetMethod.returnType.id != TypeIds.T_void) {
								this.scriptOutput.append("return "); //$NON-NLS-1$
							}
							this.scriptOutput.append("super."); //$NON-NLS-1$
							Util.appendMethodName(this.scriptOutput, synthetic.targetMethod, false, false);
							this.scriptOutput.append('(');
							if (argumentsCount > 1) {
								this.scriptOutput.append("arg1"); //$NON-NLS-1$
								for (int j = 2; j < argumentsCount; j++) {
									this.scriptOutput.append(", arg"); //$NON-NLS-1$
									this.scriptOutput.append(j);
								}
							}
							this.scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
						}
						outdent();
						printIndent();
						this.scriptOutput.append('}').appendNewLine(); 
					}
				}
				this.constructorGenerator.generate();
			}
			outdent();
			this.scriptOutput.append('}').appendNewLine().append('}').appendNewLine();
			this.actionScriptSourceFilesCollector.accept(this.actionScriptSourceFile);
			this.generatingInterfaceFields = false;
		}
		if (! this.actionScriptSourceFiles.empty()) {
			this.actionScriptSourceFile = (ActionScriptSourceFile) this.actionScriptSourceFiles.pop();
			this.scriptOutput = this.actionScriptSourceFile.actionScriptContents;
		} else {
			this.actionScriptSourceFile = null;
		}
		this.constructorGenerator = this.constructorGenerators.empty() ?
			null :
			(ActionScriptConstructorGenerator) this.constructorGenerators.pop();
		return false;
	}

	public boolean visit(UnaryExpression unaryExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, unaryExpression, scope);
		this.scriptOutput.append(unaryExpression.operatorToString());
		unaryExpression.expression.traverse(this, scope);
		Util.appendClosingParenthesis(this.scriptOutput, unaryExpression, scope);
		return false;
	}	

	public boolean visit(WhileStatement whileStatement, BlockScope scope) {
		Util.mapLine(this, this.scriptOutput, whileStatement);
		if (whileStatement != this.unIndentedStatement) printIndent();
		this.scriptOutput.append("while ("); //$NON-NLS-1$
		Expression condition = whileStatement.condition;
		condition.traverse(this, scope);
		this.scriptOutput.append(')');
		Statement action = whileStatement.action;
		boolean isActionBlock = action instanceof Block;
		if (isActionBlock) {
			this.scriptOutput.append(' ');
			this.unIndentedStatement = action;
			visitAsStatement(action, scope);
		} else {
			this.scriptOutput.appendNewLine();
			indent();
			visitAsStatement(action, scope);
			outdent();
		}
		return false;
	}

	protected void visitAsExpression(Statement statement, BlockScope scope) {
		int start = this.scriptOutput.length();
		statement.traverse(this, scope);
		int end = this.scriptOutput.length() - 1;
		this.scriptOutput.trimEnd(start, end);
	}
	
	protected void visitAsStatement(Statement statement, BlockScope scope) {
		if (statement instanceof Expression || statement instanceof LocalDeclaration) {
			printIndent();
			statement.traverse(this, scope);
			this.scriptOutput.append(';'); 
		} else {
			statement.traverse(this, scope);
		}
	}

	private boolean visitStringConcatenation(BinaryExpression binaryExpression, BlockScope scope) {
		Util.appendOpeningParenthesis(this, this.scriptOutput, binaryExpression, scope);
		switch (binaryExpression.left.resolvedType.id) {
			case TypeIds.T_char:
				this.scriptOutput.append("String.fromCharCode("); //$NON-NLS-1$
				binaryExpression.left.traverse(this, scope);
				this.scriptOutput.append(')');
				break;
			case TypeIds.T_byte:
			case TypeIds.T_short:
			case TypeIds.T_boolean:
//			case T_void: - should not happen
			case TypeIds.T_long:
			case TypeIds.T_double:
			case TypeIds.T_float:
			case TypeIds.T_int:
			case TypeIds.T_JavaLangString:
			case TypeIds.T_null:
				binaryExpression.left.traverse(this, scope);
				break;
			default:
				int mode = Util.isIntrinsic(binaryExpression.left.resolvedType) ?
						0 : binaryExpression.left.isThis() ? 1 : 2;
				if (mode > 1) {
					this.scriptOutput.append("java.lang.System.stringValueOf("); //$NON-NLS-1$
				}
				binaryExpression.left.traverse(this, scope);
				switch (mode) {
					case 1:
						this.scriptOutput.append(".toString__()"); //$NON-NLS-1$
						break;
					case 2:
						this.scriptOutput.append(')');
				}
		}
		this.scriptOutput.append(' ').append(binaryExpression.operatorToString()).append(' ');
		switch (binaryExpression.right.resolvedType.id) {
			case TypeIds.T_char:
				this.scriptOutput.append("String.fromCharCode("); //$NON-NLS-1$
				binaryExpression.right.traverse(this, scope);
				this.scriptOutput.append(')');
				break;
			case TypeIds.T_byte:
			case TypeIds.T_short:
			case TypeIds.T_boolean:
//			case T_void: - should not happen
			case TypeIds.T_long:
			case TypeIds.T_double:
			case TypeIds.T_float:
			case TypeIds.T_int:
			case TypeIds.T_JavaLangString:
			case TypeIds.T_null:
				binaryExpression.right.traverse(this, scope);
				break;
			default:
				int mode = Util.isIntrinsic(binaryExpression.right.resolvedType) ?
						0 : binaryExpression.right.isThis() ? 1 : 2;
			if (mode > 1) {
				this.scriptOutput.append("java.lang.System.stringValueOf("); //$NON-NLS-1$
			}
			binaryExpression.right.traverse(this, scope);
			switch (mode) {
				case 1:
					this.scriptOutput.append(".toString__()"); //$NON-NLS-1$
					break;
				case 2:
					this.scriptOutput.append(')');
			}
		}
		Util.appendClosingParenthesis(this.scriptOutput, binaryExpression, scope);
		return false;
	}
}
