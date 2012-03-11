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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.OperatorExpression;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;

public class Util {

	private static final char[] NUMBER = "Number".toCharArray(); //$NON-NLS-1$
	private static final char[] UINT = "uint".toCharArray(); //$NON-NLS-1$
	private static final char[] BOOLEAN = "Boolean".toCharArray(); //$NON-NLS-1$
	private static final char[] JAVA_ARRAY = "JavaArray".toCharArray(); //$NON-NLS-1$
	public static final char[] INTRINSIC = "intrinsic".toCharArray(); //$NON-NLS-1$
	static final char[] INTRINSIC_SIGNATURE_START = "Lintrinsic/".toCharArray(); //$NON-NLS-1$
	private static final HashMap defaultConstructorsRegistry = new HashMap();
	private static final char[] JAVASCRIPT_OBJECT = "JavaScriptObject".toCharArray(); //$NON-NLS-1$
	private static final char[] CLASS$0 = "class$0".toCharArray(); //$NON-NLS-1$

	public static final int
		IS_INTRINSIC = 0,
		IS_NOT_INTRINSIC = 1,
		ERROR = 2;
	// PREMATURE need to cache results to speed up calling point checks
	public static int checkIntrinsic(MethodBinding method) {
		if (isIntrinsic(method.declaringClass)) {
			return IS_INTRINSIC;
		}
		int matchingIntrinsicSuperTypesCount = 0,
		matchingNonIntrinsicSuperTypesCount = 0;
		if (method.isOverriding()) {
			MethodBinding overriden = method.declaringClass.superclass().getExactMethod(method.selector, method.parameters, null);
			if (overriden != null) { // null PREMATURE should never happen
				switch (checkIntrinsic(overriden)) {
					case IS_INTRINSIC:
						matchingIntrinsicSuperTypesCount++;
						break;
					case IS_NOT_INTRINSIC:
						matchingNonIntrinsicSuperTypesCount++;
						break;
					default:
						return ERROR;
				}
			}
		}
		if (method.isImplementing()) {
			MethodBinding implemented;
			ReferenceBinding[] superInterfaces = method.declaringClass.superInterfaces();
			if (superInterfaces != null) {
				int l = superInterfaces.length;
				for (int i = 0; i < l; i++) {
					implemented = superInterfaces[i].getExactMethod(method.selector, method.parameters, null);
					if (implemented != null && method != implemented) {
						switch (checkIntrinsic(implemented)) {
							case IS_INTRINSIC:
								matchingIntrinsicSuperTypesCount++;
								break;
							case IS_NOT_INTRINSIC:
								matchingNonIntrinsicSuperTypesCount++;
								break;
							default:
								return ERROR;
						}
					}
				}
			}
		}
		if (matchingIntrinsicSuperTypesCount > 0) {
			if (matchingNonIntrinsicSuperTypesCount > 0) {
				return ERROR;
			} else {
				return IS_INTRINSIC;
			}
		} else {
			return IS_NOT_INTRINSIC;
		}
	}
	public static void dumpConstructorFieldReference(ActionScriptScribe scriptOutput, MethodBinding constructor) {
		appendTypeName(scriptOutput, constructor.declaringClass, true);
		scriptOutput.append('.');
		Util.appendMethodName(scriptOutput, constructor, false, false);
	}
	public static boolean isIntrinsic(char[][] packageName) {
		return packageName.length > 0 && CharOperation.equals(INTRINSIC, packageName[0]);
	}
	public static boolean isIntrinsic(TypeBinding type) {
		PackageBinding packageBinding = type.getPackage();
		if (packageBinding != null) {
			return isIntrinsic(packageBinding.compoundName);
		}
		return false;
	}
	
	public static boolean isOverriding(MethodBinding method) {
		ReferenceBinding declaringClass = method.declaringClass;
		if (method.isOverriding()) {
			MethodBinding overridenMethod = null;
			ReferenceBinding superclass =  (ReferenceBinding) declaringClass.superclass().original();
			while (overridenMethod == null && superclass.id != TypeIds.T_JavaLangObject) {
				overridenMethod = findExactMethod(superclass, method);
				superclass = (ReferenceBinding) superclass.superclass().original();
			}
			// WORK this may raise errors, but we have no simple test case at hand
			//      yet; may be present in case of former errors only
			if (overridenMethod != null && overridenMethod.declaringClass.id != TypeIds.T_JavaLangObject) {
				// check the overridenMethod declaring simple name
				TypeBinding overridenReturnType= overridenMethod.returnType;
				TypeBinding returnType= method.returnType;
				return overridenReturnType == returnType || overridenReturnType.erasure() == returnType.erasure();
			}
		} else if (method.isBridge()) {
			MethodBinding overridenMethod = null;
			ReferenceBinding superclass =  (ReferenceBinding) declaringClass.superclass().original();
			while (overridenMethod == null && superclass.id != TypeIds.T_JavaLangObject) {
				overridenMethod = findExactMethod(superclass, method);
				superclass = (ReferenceBinding) superclass.superclass().original();
			}
			if (overridenMethod == null) {
				if (method.isImplementing() || declaringClass.isAbstract()) {
					superclass = declaringClass.isInterface() ? declaringClass : declaringClass.superclass();
					while (overridenMethod == null && superclass.id != TypeIds.T_JavaLangObject) {
						ReferenceBinding[] allSuperInterfaces = Util.getAllInterfaces(superclass);
						loop: for (int i = 0, max = allSuperInterfaces.length; i < max; i++) {
							ReferenceBinding superInterface = allSuperInterfaces[i];
							overridenMethod = findExactMethod(superInterface, method);
							if (overridenMethod != null) break loop;
						}
						superclass = superclass.superclass();
					}
				}
			}
			// then an abstract class lies between the implementing method and the interface
			// or we are in pure interfaces scenario
			if (overridenMethod != null && overridenMethod.declaringClass.id != TypeIds.T_JavaLangObject) {
				TypeBinding overridenReturnType= overridenMethod.returnType;
				TypeBinding returnType= method.returnType;
				return overridenReturnType == returnType || overridenReturnType.erasure() == returnType.erasure();
			}
		} else if (method.isImplementing() || declaringClass.isAbstract()) {
			MethodBinding overridenMethod = null;
			ReferenceBinding superclass = declaringClass.isInterface() ? declaringClass : declaringClass.superclass();
			while (overridenMethod == null && superclass.id != TypeIds.T_JavaLangObject) {
				ReferenceBinding[] allSuperInterfaces = Util.getAllInterfaces(superclass);
				loop: for (int i = 0, max = allSuperInterfaces.length; i < max; i++) {
					ReferenceBinding superInterface = allSuperInterfaces[i];
					overridenMethod = findExactMethod(superInterface, method);
					if (overridenMethod != null) break loop;
				}
				superclass = superclass.superclass();
			}
			// then an abstract class lies between the implementing method and the interface
			// or we are in pure interfaces scenario
			if (overridenMethod != null && overridenMethod.declaringClass.id != TypeIds.T_JavaLangObject) {
				TypeBinding overridenReturnType= overridenMethod.returnType;
				TypeBinding returnType= method.returnType;
				return overridenReturnType == returnType || overridenReturnType.erasure() == returnType.erasure();
			}
		} else if (method.isConstructor()) {
			// we have to check if one of the super types has the same simple name
			// java.sql.Date -> java.util.Date
			if (!Util.isSingleConstructor(method)) {
				MethodBinding overridenMethod = null;
				ReferenceBinding superclass =  (ReferenceBinding) declaringClass.superclass().original();
				while (overridenMethod == null && superclass.id != TypeIds.T_JavaLangObject) {
					overridenMethod = findExactMethod(superclass, method);
					superclass = (ReferenceBinding) superclass.superclass().original();
				}
				if (overridenMethod != null && !Util.isSingleConstructor(overridenMethod)) {
					// check the overridenMethod declaring simple name
					return CharOperation.equals(overridenMethod.declaringClass.sourceName(), declaringClass.sourceName());
				}
			}
		}
		return false;
	}
	private static ReferenceBinding[] getAllInterfaces(ReferenceBinding declaringClass) {
		Set allInterfaces = new HashSet();
		collectAllInterfaces0(declaringClass, allInterfaces);
		return (ReferenceBinding[]) allInterfaces.toArray(new ReferenceBinding[allInterfaces.size()]);
	}
	private static void collectAllInterfaces0(ReferenceBinding declaringClass, Set allInterfaces) {
		ReferenceBinding[] superInterfaces = declaringClass.superInterfaces();
		for (int i = 0, max = superInterfaces.length; i < max; i++) {
			allInterfaces.add(superInterfaces[i]);
			collectAllInterfaces0(superInterfaces[i], allInterfaces);
		}
	}
	/**
	 * Output a Java signature using JNI syntax
	 */
	public static ActionScriptScribe appendSignature(ActionScriptScribe output, char[] signature) {
		int length = signature.length;
		final int intrinsicSignatureStartLength = INTRINSIC_SIGNATURE_START.length; 
		int start = 0;
		if (length > intrinsicSignatureStartLength &&
				CharOperation.indexOf(INTRINSIC_SIGNATURE_START, signature, true, 0, intrinsicSignatureStartLength) != -1) {
			output.append('L');
			start = intrinsicSignatureStartLength;
		}
		for (int i = start; i < length; i++) {
			switch (signature[i]) {
				case '/':
					output.append('_');
					break;
				case '_':
					output.append("_1"); //$NON-NLS-1$
					break;
				case ';':
					output.append("_2"); //$NON-NLS-1$
					break;
				case '[':
					output.append("_3"); //$NON-NLS-1$
					break;
				default:
					output.append(signature[i]);
			}
		}
		return output;
	}
	/**
	 * Output a Java signature using JNI syntax
	 */
	public static StringBuffer appendSignature(StringBuffer output, char[] signature) {
		int length = signature.length;
		final int intrinsicSignatureStartLength = INTRINSIC_SIGNATURE_START.length; 
		int start = 0;
		if (length > intrinsicSignatureStartLength &&
				CharOperation.indexOf(INTRINSIC_SIGNATURE_START, signature, true, 0, intrinsicSignatureStartLength) != -1) {
			output.append('L');
			start = intrinsicSignatureStartLength;
		}
		for (int i = start; i < length; i++) {
			switch (signature[i]) {
				case '/':
					output.append('_');
					break;
				case '_':
					output.append("_1"); //$NON-NLS-1$
					break;
				case ';':
					output.append("_2"); //$NON-NLS-1$
					break;
				case '[':
					output.append("_3"); //$NON-NLS-1$
					break;
				default:
					output.append(signature[i]);
			}
		}
		return output;
	}
	/**
	 * Output a type name
	 */
	public static ActionScriptScribe appendTypeName(ActionScriptScribe output, TypeBinding type, boolean fullyQualified) {
		return appendTypeName(output, type, fullyQualified, false);
	}
	/**
	 * Output a type name
	 */
	public static ActionScriptScribe appendTypeName(ActionScriptScribe output, TypeBinding type, boolean fullyQualified, boolean handleJavaScriptObject) {
		output.append(getTypeName(type, fullyQualified, handleJavaScriptObject));
		return output;
	}
	/**
	 * Output a type name
	 */
	public static char[] getTypeName(TypeBinding type, boolean fullyQualified, boolean handleJavaScriptObject) {
		if (type.isArrayType()) {
			return JAVA_ARRAY;
		}
		while (type.isTypeVariable()) {
			type = ((TypeVariableBinding) type).upperBound();
		}
		switch (type.id) {
			case TypeIds.T_JavaLangObject:
			case TypeIds.T_JavaLangString:
				fullyQualified = false; // get rid of java.lang.
				break;
			case TypeIds.T_boolean:
				return BOOLEAN;
			case TypeIds.T_short:
			case TypeIds.T_byte:
			case TypeIds.T_int:
				return TypeConstants.INT;
			case TypeIds.T_char:
				return UINT;
			case TypeIds.T_long:
			case TypeIds.T_float:
			case TypeIds.T_double:
				return NUMBER;
		}
		char[] shortTypeName = shortTypeName(type);
		if (handleJavaScriptObject && CharOperation.equals(JAVASCRIPT_OBJECT, shortTypeName)) {
			return TypeConstants.OBJECT;
		} else {
			if (fullyQualified) {
				PackageBinding packageBinding = type.getPackage();
				if (packageBinding != null) {
					char[][] packageName = packageBinding.compoundName;
					if (packageName.length > 0) {
						int start = CharOperation.equals(INTRINSIC, packageName[0]) ? 1 : 0;
						return CharOperation.concat(
								CharOperation.concatWith(
										CharOperation.subarray(packageName, start, packageName.length),
										'.'), shortTypeName
										,'.');
					}
				}
			}
			return shortTypeName;
		}
	}
	/**
	 * Output a method name
	 */
	public static ActionScriptScribe appendMethodName(ActionScriptScribe output, MethodBinding method, boolean isIntrinsic, boolean generatingPrivilegedConstructor) {
		if (isIntrinsic) {
			output.append(method.selector);
		} else {
			if (method.isConstructor()) {
				if (generatingPrivilegedConstructor) {
					appendTypeName(output, method.declaringClass, false);
					return output;
				}
				if (method.declaringClass.id == TypeIds.T_JavaLangString) {
					output.append("__init"); //$NON-NLS-1$
				} else {
					appendTypeName(output, method.declaringClass, false);
				}
			} else {
				output.append(method.selector);
			}
			output.append("__"); //$NON-NLS-1$
			if (method instanceof ParameterizedMethodBinding) {
				method = method.original();
			}
			for (int i = 0, length = method.parameters.length; i < length; i++) {
				appendSignature(output, method.parameters[i].signature());
			}
		}
		return output;
	}
	
	/**
	 * Output a method name
	 */
	public static StringBuffer appendMethodName(StringBuffer output, MethodBinding method, boolean isIntrinsic, boolean generatingPrivilegedConstructor) {
		if (isIntrinsic) {
			output.append(method.selector);
		} else {
			if (method.isConstructor()) {
				if (generatingPrivilegedConstructor) {
					output.append(getTypeName(method.declaringClass, false, false));
					return output;
				}
				if (method.declaringClass.id == TypeIds.T_JavaLangString) {
					output.append("__init"); //$NON-NLS-1$
				} else {
					output.append(getTypeName(method.declaringClass, false, false));
				}
			} else {
				output.append(method.selector);
			}
			output.append("__"); //$NON-NLS-1$
			if (method instanceof ParameterizedMethodBinding) {
				method = method.original();
			}
			for (int i = 0, length = method.parameters.length; i < length; i++) {
				appendSignature(output, method.parameters[i].signature());
			}
		}
		return output;
	}

	public static void mapLine(ActionScriptSourceGenerator generator, ActionScriptScribe output, ASTNode node) {
		mapLine(generator, output, node, generator.lineNumberStart, generator.lineNumberEnd);
	}

	public static void mapLine(ActionScriptSourceGenerator generator, ActionScriptScribe output, ASTNode node, int lowerBound, int higherBound) {
		int lineNumber;
		if (lowerBound == higherBound) {
			lineNumber = lowerBound;
		} else {
			lineNumber = org.eclipse.jdt.internal.compiler.util.Util.getLineNumber(
				node.sourceStart(),
				generator.lineSeparators,
				lowerBound - 1,
				higherBound - 1);
		}
		if (lineNumber > output.currentMappedLineNumber) {
			output.setCurrentMappedLineNumber(lineNumber);
		}
	}

	public static void appendOpeningParenthesis(ActionScriptSourceGenerator generator, ActionScriptScribe output, Expression expression, BlockScope scope) {
		mapLine(generator, output, expression);
		int numberOfParenthesis = (expression.bits & ASTNode.ParenthesizedMASK) >> ASTNode.ParenthesizedSHIFT;
		for (int i = 0; i < numberOfParenthesis; i++) {
			output.append('(');
		}
		if (Util.isBoxing(expression)) {
			// generate wrapper type constructor
			boxedExpression(output, expression, scope);
		}
	}
	public static void boxedExpression(ActionScriptScribe output, Expression expression, BlockScope scope) {
		TypeBinding boxingType = scope.environment().computeBoxingType(expression.resolvedType);
		MethodBinding constructor = Util.retrieveConstructor(expression.resolvedType, (ReferenceBinding) boxingType);
		if (constructor == null) {
			// should not happen
			return;
		}
		dumpWrapperType(output, boxingType, constructor);
	}
	public static void dumpWrapperType(ActionScriptScribe output, TypeBinding wrapperType, MethodBinding constructor) {
		output.append("new "); //$NON-NLS-1$
		char[] constantPoolName = wrapperType.constantPoolName();
		constantPoolName = CharOperation.replaceOnCopy(constantPoolName, '/', '.');
		output.append(constantPoolName);
		output.append("(new java.lang.Arguments("); //$NON-NLS-1$
		Util.dumpConstructorFieldReference(output, constructor);
		output.append(", ["); //$NON-NLS-1$
	}

	public static void appendClosingParenthesis(ActionScriptScribe output, Expression expression, BlockScope scope) {
		if (Util.isBoxing(expression)) {
			// closing wrapper type generation
			output.append("]))"); //$NON-NLS-1$
		} else if (Util.isUnBoxing(expression)) {
			unboxedExpression(output, expression, scope);
		}
		int numberOfParenthesis = (expression.bits & ASTNode.ParenthesizedMASK) >> ASTNode.ParenthesizedSHIFT;
		for (int i = 0; i < numberOfParenthesis; i++) {
			output.append(')');
		}
	}
	public static void unboxedExpression(ActionScriptScribe output, Expression expression, BlockScope scope) {
		TypeBinding unboxingType = scope.environment().computeBoxingType(expression.resolvedType);
		switch (unboxingType.id) {
			case TypeIds.T_byte :
				// invokevirtual: byteValue()
				output.append(".byteValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_short :
				// invokevirtual: shortValue()
				output.append(".shortValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_char :
				// invokevirtual: charValue()
				output.append(".charValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_int :
				// invokevirtual: intValue()
				output.append(".intValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_long :
				// invokevirtual: longValue()
				output.append(".longValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_float :
				// invokevirtual: floatValue()
				output.append(".floatValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_double :
				// invokevirtual: doubleValue()
				output.append(".doubleValue__()"); //$NON-NLS-1$
				break;
			case TypeIds.T_boolean :
				// invokevirtual: booleanValue()
				output.append(".booleanValue__()"); //$NON-NLS-1$
		}
	}
	public static String appendConstant(ActionScriptScribe output, Constant constant) {
		switch (constant.typeID()) {
			case TypeIds.T_char:
			case TypeIds.T_byte:
			case TypeIds.T_short:
			case TypeIds.T_int:
				output.append(constant.intValue());
				break;
			case TypeIds.T_long:
				long value = constant.longValue();
				if (value < ActionScriptSourceGenerator.LONG_MIN_VALUE) {
					output.append(-1);
					return "long literal underflow"; //$NON-NLS-1$
				} else if (value > ActionScriptSourceGenerator.LONG_MAX_VALUE) {
					output.append(-1);
					return "long literal overflow"; //$NON-NLS-1$
				} else {
					output.append(value);
				}
				break;
			case TypeIds.T_boolean:
				if (constant.booleanValue()) {
					output.append("true"); //$NON-NLS-1$
				} else {
					output.append("false"); //$NON-NLS-1$
				}
				break;
//			case T_void: - should not happen
			case TypeIds.T_double:
				double dv = constant.doubleValue();
				if (dv > 0) {
					if (dv < ActionScriptSourceGenerator.DOUBLE_MIN_VALUE) {
						output.append(-1);
						return "double constant underflow"; //$NON-NLS-1$
					} else if (dv > ActionScriptSourceGenerator.DOUBLE_MAX_VALUE) {
						output.append(-1);
						return "double constant overflow"; //$NON-NLS-1$
					}
				} else if (dv < 0) {
					if (dv > - ActionScriptSourceGenerator.DOUBLE_MIN_VALUE) {
						output.append(-1);
						return "double constant underflow"; //$NON-NLS-1$
					} else if (dv < - ActionScriptSourceGenerator.DOUBLE_MAX_VALUE) {
						output.append(-1);
						return "double constant overflow"; //$NON-NLS-1$
					}
				}
				output.append(dv);
				break;
			case TypeIds.T_float:
				// PREMATURE check that floats are all in range
				output.append(constant.doubleValue());
				break;
			case TypeIds.T_JavaLangString:
				Util.appendString(output, constant.stringValue());
				break;
			case TypeIds.T_null:
				output.append("null"); //$NON-NLS-1$
				break;
			default:
		}
		return null;
	}
	public static void appendString(ActionScriptScribe output, String string) {
		// @see org.eclipse.jdt.internal.compiler.ast.StringLiteral#printExpression(...)
		// further tuned to project octal escapes to unicode escapes
		char[] source = string.toCharArray();
		// handle some special char.....
		output.append('\"');
		for (int i = 0; i < source.length; i++) {
			char c;
			switch (c = source[i]) {
				case '\b' :
					output.append("\\b"); //$NON-NLS-1$
					break;
				case '\t' :
					output.append("\\t"); //$NON-NLS-1$
					break;
				case '\n' :
					output.append("\\n"); //$NON-NLS-1$
					break;
				case '\f' :
					output.append("\\f"); //$NON-NLS-1$
					break;
				case '\r' :
					output.append("\\r"); //$NON-NLS-1$
					break;
				case '\"' :
					output.append("\\\""); //$NON-NLS-1$
					break;
				case '\'' :
					output.append("\\'"); //$NON-NLS-1$
					break;
				case '\\' : //take care not to display the escape as a potential real char
					output.append("\\\\"); //$NON-NLS-1$
					break;
				default :
					if (c <= '\u000f') {
						output.append("\\u000"); //$NON-NLS-1$
						output.append(Integer.toHexString(c));
					} else if (c < '\u0020') {
						output.append("\\u00"); //$NON-NLS-1$
						output.append(Integer.toHexString(c));
					} else if (c < '\u0080') {
						output.append(c);
					} else if (c < '\u0100') {
						output.append("\\u00"); //$NON-NLS-1$
						output.append(Integer.toHexString(c));
					} else if (c < '\u1000') {
						output.append("\\u0"); //$NON-NLS-1$
						output.append(Integer.toHexString(c));
					} else {
						output.append("\\u"); //$NON-NLS-1$
						output.append(Integer.toHexString(c));
					}
			}
		}
		output.append('\"'); 
	}
	static boolean hasFieldInitializers(TypeDeclaration typeDeclaration) {
		for (int i = 0, l = typeDeclaration.fields == null ? 0 : typeDeclaration.fields.length; i < l; i++) {
			FieldDeclaration field = typeDeclaration.fields[i];
			if (field.getKind() == AbstractVariableDeclaration.INITIALIZER &&
					!field.isStatic()) {
				return true;
			}
			if (Util.containsThisReference(field, typeDeclaration.initializerScope)) {
				return true;
			}
		}
		return false;
	}
	static boolean isDefaultConstructor(MethodBinding constructor) {
		Boolean known = (Boolean) defaultConstructorsRegistry.get(constructor);
		if (known != null) {
			return known.booleanValue();
		}
		try {
		AbstractMethodDeclaration constructorDeclaration = constructor.sourceMethod();
			if (constructorDeclaration != null &&
					constructorDeclaration.isDefaultConstructor() && 
					!constructorDeclaration.binding.declaringClass.isNestedType()) {
				defaultConstructorsRegistry.put(constructor, Boolean.TRUE);
				return true;
			}
		} catch (NullPointerException e) {
			// PREMATURE this is dirty, at best
		}
		defaultConstructorsRegistry.put(constructor, Boolean.FALSE);
		return false;
	}
	// PREMATURE we need to cache results here, at least at the type level
	public static boolean isSingleConstructor(MethodBinding method) {
		if (!method.isConstructor()) {
			return false;
		}
		MethodBinding[] methods = method.declaringClass.methods();
		for (int i = 0, l = methods.length, constructorsCount = 0; i < l; i++) {
			if (methods[i].isConstructor()) {
				if (++constructorsCount > 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static String methodKey(MethodBinding method) {
		StringBuffer result = new StringBuffer();
		result.append(method.selector);
		result.append(method.signature());
		return result.toString();
	}
	
	public static String operatorToString(OperatorExpression expression) {
		switch ((expression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT) {
			case OperatorIds.EQUAL_EQUAL:
				return "==="; //$NON-NLS-1$
			case OperatorIds.NOT_EQUAL :
				return "!=="; //$NON-NLS-1$
		}
		return expression.operatorToString();
	}
	
	private static final HashMap typeNamesRegistry = new HashMap(); // PREMATURE not scalable
	private static final HashMap recodedTypeNames = new HashMap();
	static {
		String[] recodedNames = new String [] {
			"ArgumentError", //$NON-NLS-1$
			"arguments", //$NON-NLS-1$
			"Array", //$NON-NLS-1$
			"Boolean", //$NON-NLS-1$
			"Class", //$NON-NLS-1$
			"Date", //$NON-NLS-1$
			"DefinitionError", //$NON-NLS-1$
			"Error", //$NON-NLS-1$
			"EvalError", //$NON-NLS-1$
			"Function", //$NON-NLS-1$
			// no - int cannot be redefined anyway "int", //$NON-NLS-1$
			"Math", //$NON-NLS-1$
			"Namespace", //$NON-NLS-1$
			"Number", //$NON-NLS-1$
			// NO - "Object", //$NON-NLS-1$
			"QName", //$NON-NLS-1$
			"RangeError", //$NON-NLS-1$
			"ReferenceError", //$NON-NLS-1$
			"RegExp", //$NON-NLS-1$
			"SecurityError", //$NON-NLS-1$
			// NO - "String", //$NON-NLS-1$
			"SyntaxError", //$NON-NLS-1$
			"TypeError", //$NON-NLS-1$
			"uint", //$NON-NLS-1$
			"URIError", //$NON-NLS-1$
			"VerifyError", //$NON-NLS-1$
			"XML", //$NON-NLS-1$
			"XMLList", //$NON-NLS-1$
		};
		for (int i = 0, l = recodedNames.length; i < l ; i++) {
			char[] currentName = recodedNames[i].toCharArray();
			char[] currentRecodedName = new char[currentName.length + 2];
			System.arraycopy(currentName, 0, currentRecodedName, 0, currentName.length);
			currentRecodedName[currentName.length] = currentRecodedName[currentName.length + 1] = '_';
			recodedTypeNames.put(recodedNames[i], currentRecodedName);
		}
	}
	public static void registerDefaultConstructor(MethodBinding constructor) {
		defaultConstructorsRegistry.put(constructor, Boolean.TRUE);
	}
	public static char[] shortTypeName(TypeBinding type) {
		char[] result = (char[]) typeNamesRegistry.get(type);
		if (result == null) {
			if (type.isTypeVariable()) {
				type = ((TypeVariableBinding) type).upperBound();
			}
			if (type.isNestedType()) {
				result = CharOperation.lastSegment(type.constantPoolName(), '/');
			} else {
				result = type.shortReadableName();
			}
			CharOperation.replace(result, '.', '$'); // WORK should go away once inner types have been unified back into regular types
			if (CharOperation.indexOf('<', result) != -1) {
				// PREMATURE optimize to get a single pass and avoid conversions
				int l = result.length;
				int numBrackets = 0;
				char c;
				StringBuffer stripped = new StringBuffer(l);
				for (int i = 0; i < l; i++) {
					if ((c = result[i]) == '<') {
						numBrackets++;
					} else if ((c = result[i]) == '>') {
						numBrackets--;
					} else if (numBrackets == 0) {
						stripped.append(c);
					}
				}
				result = stripped.toString().toCharArray();
			}
			if (!isIntrinsic(type)) {
				char[] transcodedValue = (char[]) recodedTypeNames.get(new String(result));
				if (transcodedValue != null) {
					result = transcodedValue;
				}
			}
			typeNamesRegistry.put(type, result);
		}
		return result;
	}
	public static char[][] split(char[] text, char separator) {
		int length = text.length;
		char[][] words = new char[length][]; // max number of occurrences
		int i, j, k, l;
		for (i = 0, j = 0, k = 0; i < length; i++) {
			if (text[i] == separator) {
				l = i - k;
				if (l > 0) {
					char[] word = new char[l];
					System.arraycopy(text, k, word, 0, l);
					words[j++] = word;
				}
				k = i + 1;
			}
		}
		l = i - k;
		if (l > 0) {
			char[] word = new char[l];
			System.arraycopy(text, k, word, 0, l);
			words[j++] = word;
		}
		if (j < length) {
			System.arraycopy(words, 0, words = new char[j][], 0, j);
		}
		return words;
	}
	
	public static int compareTo(char[] a, char[] b) {
		return new String(a).compareTo(new String(b)); // PREMATURE optimize if used
	}
	
	public static MethodBinding findExactMethod(ReferenceBinding declaringClass, char[] selector, TypeBinding[] argumentTypes) {
		MethodBinding[] methods = declaringClass.getMethods(selector);
		switch (methods.length) {
			case 0 : return null;
			case 1 : return methods[0];
			default :
				int argLength = argumentTypes.length;
				nextMethod : for (int i = 0, l = methods.length; i < l; i++) {
					MethodBinding possible = methods[i]; 
					if (possible.parameters.length == argLength) {
						for (int j = argLength; --j >= 0;) {
							if (possible.parameters[j] != argumentTypes[j])
								continue nextMethod;
						}
						return possible;
					}
				}
				return null;
		}
	}

	public static MethodBinding findExactMethod(ReferenceBinding declaringClass, MethodBinding methodBinding) {
		MethodBinding[] methods = declaringClass.getMethods(methodBinding.selector);
		switch (methods.length) {
			case 0 :
				return null;
			case 1 :
				if (methodBinding.areParameterErasuresEqual(methods[0])) {
					return methods[0];
				}
				return null;
			default :
				nextMethod : for (int i = 0, l = methods.length; i < l; i++) {
					MethodBinding possible = methods[i]; 
					if (methodBinding.areParameterErasuresEqual(possible)) {
						return possible;
					}
					continue nextMethod;
				}
				return null;
		}
	}

	public static String typeName(TypeBinding type, boolean fullyQualified) {
		return new String(getTypeName(type, fullyQualified, false));
	}


	public static boolean isBoxing(Expression expression) {
		return (expression.implicitConversion & TypeIds.BOXING) != 0;
	}

	public static boolean isUnBoxing(Expression expression) {
		return (expression.implicitConversion & TypeIds.UNBOXING) != 0;
	}

	public static MethodBinding retrieveConstructor(TypeBinding parameterType, ReferenceBinding typeBinding) {
		MethodBinding[] methods = typeBinding.methods();
		for (int i = 0, max = methods.length; i < max; i++) {
			MethodBinding binding = methods[i];
			if (binding.isConstructor()) {
				TypeBinding[] parameterTypes = binding.parameters;
				if (parameterType == null) {
					if (parameterTypes.length == 0) {
						return binding;
					}
					continue;
				}
				if (parameterTypes.length != 1) {
					continue;
				}
				TypeBinding parameter = parameterTypes[0];
				if (parameter.isBaseType()) {
					// check equality
					if (parameter == parameterType) {
						return binding;
					}
				} else if (parameterType != null && parameter.isCompatibleWith(parameterType)) {
					// take Object
					return binding;
				}
			}
		}
		return null;
	}
	public static int getOrdinalValue(FieldDeclaration enumConstant) {
		FieldBinding binding = enumConstant.binding;
		ReferenceBinding declaringClass = binding.declaringClass;
		FieldBinding[] fields = declaringClass.fields();
		for (int i = 0, max = fields.length; i < max; i++) {
			FieldBinding fieldBinding = fields[i];
			if (fieldBinding == binding) {
				return i;
			}
		}
		return 0;
	}
	public static FieldBinding[] getEnumConstants(SourceTypeBinding binding) {
		ArrayList enumConstants = new ArrayList();
		FieldBinding[] fields = binding.fields();
		for (int i = 0, max = fields.length; i < max; i++) {
			FieldBinding fieldBinding = fields[i];
			if ((fieldBinding.modifiers & ClassFileConstants.AccEnum) != 0) {
				enumConstants.add(fieldBinding);
			}
		}
		FieldBinding[] result = new FieldBinding[enumConstants.size()];
		enumConstants.toArray(result);
		return result;
	}
	public static int getOrdinalValue(FieldBinding binding) {
		ReferenceBinding declaringClass = binding.declaringClass;
		FieldBinding[] fields = declaringClass.fields();
		for (int i = 0, max = fields.length; i < max; i++) {
			if (fields[i] == binding) {
				return i;
			}
		}
		return -1;
	}
	public static boolean isExcluded(FieldBinding fieldBinding) {
		if (CharOperation.equals(fieldBinding.name, CLASS$0)) {
			return true;
		}
		if (fieldBinding.declaringClass.isEnum() && !CharOperation.equals(fieldBinding.name, TypeConstants.SYNTHETIC_ENUM_VALUES)) {
			return true;
		}
		if (CharOperation.indexOf(TypeConstants.SYNTHETIC_SWITCH_ENUM_TABLE, fieldBinding.name, true) != -1) {
			return true;
		}
		if (CharOperation.equals(TypeConstants.SYNTHETIC_ASSERT_DISABLED, fieldBinding.name)) {
			return true;
		}
		if (CharOperation.indexOf(TypeConstants.SYNTHETIC_CLASS, fieldBinding.name, true) != -1) {
			return true;
		}
		return false;
	}
	public static boolean containsThisReference(FieldDeclaration field, MethodScope scope) {
		if (field.initialization == null || field.binding.isStatic()) return false;
		class ThisReferenceVisitor extends ASTVisitor {
			boolean found = false;
			public boolean visit(ThisReference reference, BlockScope blockScope) {
				this.found = true;
				return false;
			}
		}
		ThisReferenceVisitor referenceVisitor = new ThisReferenceVisitor();
		field.initialization.traverse(referenceVisitor, scope);
		return referenceVisitor.found;
	}
	/**
	 * Returns a unique field name that doesn't collide with existing fields.
	 * @param fieldDeclaration the given field declaration
	 * @return a char[]
	 */
	public static char[] getSyntheticFieldName(FieldDeclaration fieldDeclaration) {
		char[] fieldName = CharOperation.concat(new char[] { '_' }, fieldDeclaration.name);
		FieldBinding[] fields = fieldDeclaration.binding.declaringClass.fields();
		boolean collides;
		do {
			collides = false;
			loop: for (int i = 0; i < fields.length; i++) {
				FieldBinding fieldBinding = fields[i];
				if (CharOperation.equals(fieldName, fieldBinding.name)) {
					collides = true;
					break loop;
				}
			}
			if (collides) {
				fieldName = CharOperation.concat(new char[] { '_' }, fieldName);
			}
		} while (collides);
		return fieldName;
	}
	
	public static void dumpMapFile(String fileName) {
		DataInputStream inputStream = null;

		try {
			inputStream = new DataInputStream(
					new BufferedInputStream(
							new FileInputStream(fileName)));
			System.out.println("java source folder : " + inputStream.readUTF()); //$NON-NLS-1$
			System.out.println("java qualified name : " + inputStream.readUTF()); //$NON-NLS-1$
			int length = inputStream.readInt();
			for (int i = 0; i < length; i++) {
				System.out.print(" as line number : "); //$NON-NLS-1$
				System.out.print(inputStream.readInt());
				System.out.print(" java line number : "); //$NON-NLS-1$
				System.out.println(inputStream.readInt());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch(IOException e) {
					// ignore
				}
			}
		}
	}
	
	public static boolean shouldFullyQualified(QualifiedNameReference qualifiedNameReference) {
		boolean fullyQualified = true;
		char[][] receiverPackageName = Util.split(qualifiedNameReference.actualReceiverType.qualifiedPackageName(), '.');
		char[][] receiverSourceName = Util.split(qualifiedNameReference.actualReceiverType.qualifiedSourceName(), '.');
		int currentToken;
		// PREMATURE optimize loops here if we do not switch to another implementation
		for (currentToken = 0; currentToken < receiverPackageName.length; currentToken++) {
			if (currentToken >= qualifiedNameReference.tokens.length ||
					Util.compareTo(receiverPackageName[currentToken], qualifiedNameReference.tokens[currentToken]) != 0) {
				fullyQualified = false;
				break;
			}
		}
		if (fullyQualified) {
			for (int i = 0; i < receiverSourceName.length; i++) {
				if (currentToken >= qualifiedNameReference.tokens.length ||
						Util.compareTo(receiverSourceName[i], qualifiedNameReference.tokens[currentToken]) != 0) {
					fullyQualified = false;
					break;
				}
			}
		}
		return fullyQualified;
	}
	public static boolean isUnreachable(ASTNode node) {
		if (node == null) return false;
		if (node instanceof Block) {
			Block block = (Block) node;
			Statement[] statements = block.statements;
			if (statements == null || statements.length == 0) {
				return false;
			}
			return (statements[0].bits & ASTNode.IsReachable) == 0;
		}
		return (node.bits & ASTNode.IsReachable) == 0;
	}
}
