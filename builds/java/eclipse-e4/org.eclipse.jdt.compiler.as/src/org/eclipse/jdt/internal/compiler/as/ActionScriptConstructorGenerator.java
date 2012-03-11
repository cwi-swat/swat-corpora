/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.NestedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SyntheticArgumentBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

public class ActionScriptConstructorGenerator {
	ActionScriptSourceGenerator parent;
	ArrayList constructors = new ArrayList();
	Map bindingToIndex = new HashMap();
	boolean needSuperCall = false;
	int superArgsCount = 0;
	ActionScriptConstructorGenerator(ActionScriptSourceGenerator parent) {
		this.parent = parent;
	}
	void add(ConstructorDeclaration constructor) {
		this.constructors.add(constructor);
		this.bindingToIndex.put(constructor.binding, new Integer(this.constructors.size() - 1));
	}
	void generate() {
		int constructorsCount = this.constructors.size(); 
		if (constructorsCount < 2) {
			return;
		}
		ActionScriptScribe scriptOutput = this.parent.scriptOutput;
		ReferenceBinding type = ((ConstructorDeclaration) this.constructors.get(0)).binding.declaringClass;
		SyntheticArgumentBinding[] syntheticArgs = type.isNestedType()
			? ((NestedTypeBinding) type).syntheticEnclosingInstances()
			: null;
		int syntheticArgsCount = syntheticArgs == null ? 0 : syntheticArgs.length;
		// function start
		this.parent.printIndent();
		scriptOutput.append("public function "); //$NON-NLS-1$
		Util.appendTypeName(scriptOutput, type, false);
		scriptOutput.append("(... vargs) {").appendNewLine(); //$NON-NLS-1$
		this.parent.indent();
		// declare arguments manipulation locals
		this.parent.printIndent();
		scriptOutput.append("var args: Array;").appendNewLine(); //$NON-NLS-1$
		this.parent.printIndent();
		scriptOutput.append("var id: int;").appendNewLine(); //$NON-NLS-1$
		if (syntheticArgsCount > 0) {
			this.parent.printIndent();
			scriptOutput.append("var syntheticCount: int;").appendNewLine(); //$NON-NLS-1$
		}
		// analyze arguments 
		this.parent.printIndent();
		scriptOutput.append("if (vargs.length == 1 && vargs[0] is Arguments) {").appendNewLine(); //$NON-NLS-1$
		this.parent.indent();
		this.parent.printIndent();
		scriptOutput.append("args = Arguments(vargs[0]).args;").appendNewLine(); //$NON-NLS-1$
		this.parent.printIndent();
		scriptOutput.append("id = Arguments(vargs[0]).id;").appendNewLine(); //$NON-NLS-1$
		if (syntheticArgsCount > 0) {
			this.parent.printIndent();
			scriptOutput.append("syntheticCount = "); //$NON-NLS-1$
			scriptOutput.append(syntheticArgsCount);
			scriptOutput.append(';').appendNewLine(); 
		}
		this.parent.outdent();
		this.parent.printIndent();
		scriptOutput.append("} else {").appendNewLine(); //$NON-NLS-1$
		this.parent.indent();
		if (syntheticArgsCount > 0) {
			this.parent.printIndent();
			scriptOutput.append("syntheticCount = 0;").appendNewLine(); //$NON-NLS-1$
		}
		for (int i = 0; i < constructorsCount; i++) {
			// PREMATURE do better than linear, systematic signature matching
			// TODO due to be buggy in presence of types that extend others (must work upon a 'most specific' selection)
			MethodBinding constructor = ((ConstructorDeclaration) this.constructors.get(i)).binding;
			if (i == 0)
				this.parent.printIndent();
			else
				scriptOutput.append("} else "); //$NON-NLS-1$
			scriptOutput.append("if (vargs.length == "); //$NON-NLS-1$
			if (constructor.parameters == null || constructor.parameters.length == 0) {
				scriptOutput.append(0);
				scriptOutput.append(") {").appendNewLine(); //$NON-NLS-1$
				this.parent.indent();
			} else {
				scriptOutput.append(constructor.parameters.length);
				this.parent.indent();
				for (int j = 0, l = constructor.parameters.length; j < l; j++) {
					scriptOutput.append(" && vargs["); //$NON-NLS-1$
					scriptOutput.append(j);
					scriptOutput.append("] is "); //$NON-NLS-1$
					TypeBinding parameterType = constructor.parameters[j];
					Util.appendTypeName(scriptOutput, parameterType, this.parent.importRegistry.isAmbiguous(parameterType));
				}
				scriptOutput.append(") {").appendNewLine(); //$NON-NLS-1$
				this.parent.printIndent();
				scriptOutput.append("args = vargs;").appendNewLine(); //$NON-NLS-1$
			}
			this.parent.printIndent();
			scriptOutput.append("id = "); //$NON-NLS-1$
			scriptOutput.append(i);
			scriptOutput.append(';').appendNewLine(); 
			this.parent.outdent();
			this.parent.printIndent();
		}
		scriptOutput.append('}').appendNewLine();
		this.parent.outdent();
		this.parent.printIndent();
		scriptOutput.append('}').appendNewLine();
		// prepare arguments and call functions
		this.parent.printIndent();
		scriptOutput.append("switch (id) {").appendNewLine(); //$NON-NLS-1$
		this.parent.indent();
		for (int i = 0; i < constructorsCount; i++) {
			ConstructorDeclaration constructor = (ConstructorDeclaration) this.constructors.get(i);
			this.parent.printIndent();
			scriptOutput.append("case "); //$NON-NLS-1$
			scriptOutput.append(i);
			scriptOutput.append(':').appendNewLine(); 
			this.parent.indent();
			generateArguments(scriptOutput, constructor, syntheticArgsCount, i, null, -1);
			this.parent.printIndent();
			scriptOutput.append("break;").appendNewLine(); //$NON-NLS-1$
			this.parent.outdent();
		}
		this.parent.outdent();
		this.parent.printIndent();
		scriptOutput.append('}').appendNewLine(); 
		// call super
		if (this.needSuperCall) {
			this.parent.printIndent();
			scriptOutput.append("super("); //$NON-NLS-1$
			switch (this.superArgsCount) {
				case -1:
					scriptOutput.append("superArgs"); //$NON-NLS-1$
					break;
				case 0:
					if (syntheticArgsCount == 1 && type.superclass().id != TypeIds.T_JavaLangObject) // ***** HACK for now *****
						scriptOutput.append("args[0]"); //$NON-NLS-1$
					break;
				default:
					scriptOutput.append("superArgs[0]"); //$NON-NLS-1$
					for (int i = 1; i < this.superArgsCount; i++) {
						scriptOutput.append(", superArgs["); //$NON-NLS-1$
						scriptOutput.append(i);
						scriptOutput.append(']');
					}
			}
			scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
		}
		// call init functions as needed
		this.parent.printIndent();
		scriptOutput.append("switch (id) {").appendNewLine(); //$NON-NLS-1$
		this.parent.indent();
		for (int i = 0; i < constructorsCount; i++) {
			ConstructorDeclaration constructor = (ConstructorDeclaration) this.constructors.get(i);
			this.parent.printIndent();
			scriptOutput.append("case "); //$NON-NLS-1$
			scriptOutput.append(i);
			scriptOutput.append(':').appendNewLine(); 
			this.parent.indent();
			generateFunctionCalls(scriptOutput, constructor, syntheticArgsCount, i);
			this.parent.printIndent();
			scriptOutput.append("break;").appendNewLine(); //$NON-NLS-1$
			this.parent.outdent();
		}
		this.parent.outdent();
		this.parent.printIndent();
		scriptOutput.append('}').appendNewLine(); 
		
		// function end 
		this.parent.outdent();
		this.parent.printIndent();
		scriptOutput.append('}').appendNewLine(); 
		// add constant fields for each constructor
		for (int i = 0; i < constructorsCount; i++) {
			ConstructorDeclaration constructor = (ConstructorDeclaration) this.constructors.get(i);
			this.parent.printIndent();
			scriptOutput.append("public static const "); //$NON-NLS-1$
			Util.appendMethodName(scriptOutput, constructor.binding, false, false);
			scriptOutput.append(" : int = "); //$NON-NLS-1$
			Object o = this.bindingToIndex.get(constructor.binding);
			if (o == null) {
				this.parent.reportError(constructor.scope, "could not find constructor binding", constructor); //$NON-NLS-1$
			}
			scriptOutput.append(o);
			scriptOutput.append(';').appendNewLine(); 
		}
	}
	private void generateArguments(ActionScriptScribe scriptOutput, ConstructorDeclaration constructor, int syntheticArgsCount, int currentConstructorIndex, ExplicitConstructorCall constructorCall, int upperConstructorIndex) {
		int argsCount = syntheticArgsCount +
			(constructor.binding.parameters == null ? 0 : constructor.binding.parameters.length);
		if (argsCount > 0) {
			this.parent.printIndent();
			if (constructorCall == null) {
				scriptOutput.append("var "); //$NON-NLS-1$
			}
			scriptOutput.append("args"); //$NON-NLS-1$
			scriptOutput.append(currentConstructorIndex);
			if (constructorCall == null) {
				scriptOutput.append(": Array"); //$NON-NLS-1$
			}
			scriptOutput.append(" = ["); //$NON-NLS-1$
			if (constructorCall == null) { // upper call
				scriptOutput.append("args[0]"); //$NON-NLS-1$
				for (int i = 1; i < argsCount; i++) {
					scriptOutput.append(", args["); //$NON-NLS-1$
					scriptOutput.append(i);
					scriptOutput.append(']');
				}
			} else {
				this.parent.localNamesRegistry = new ParameterNamesRegistry((ConstructorDeclaration) this.constructors.get(upperConstructorIndex), upperConstructorIndex);
				Expression[] arguments = constructorCall.arguments;
				arguments[0].traverse(this.parent, constructor.scope);
				for (int i = 1, max = arguments.length; i < max; i++) {
					scriptOutput.append(", "); //$NON-NLS-1$
					arguments[i].traverse(this.parent, constructor.scope);
				}
			}
			scriptOutput.append("];").appendNewLine(); //$NON-NLS-1$
		}
		ExplicitConstructorCall constructorCall2 = constructor.constructorCall;
		if (constructorCall2 != null) {
			if (constructorCall2.isSuperAccess()) {
				boolean allocateArguments = !(
					constructorCall2.binding.declaringClass.id == TypeIds.T_JavaLangString ||
					Util.isSingleConstructor(constructorCall2.binding) ||
					constructorCall2.binding.declaringClass.id == TypeIds.T_JavaLangObject ||
					Util.isIntrinsic(constructorCall2.binding.declaringClass));
				Expression[] arguments = constructorCall2.arguments;
				argsCount = arguments == null ? 0 : arguments.length;
				if (!this.needSuperCall) {
					this.needSuperCall = true;
					if (allocateArguments) {
						this.parent.printIndent();
						scriptOutput.append("var superArgs: Arguments;").appendNewLine(); //$NON-NLS-1$
					} else if (argsCount > 0) {
						this.parent.printIndent();
						scriptOutput.append("var superArgs: Array;").appendNewLine(); //$NON-NLS-1$
					}
				}
				if (allocateArguments || argsCount > 0) {
					this.parent.printIndent();
					scriptOutput.append("superArgs = "); //$NON-NLS-1$
				}
				if (allocateArguments) {
					scriptOutput.append("new Arguments(");//$NON-NLS-1$
					Util.dumpConstructorFieldReference(scriptOutput, constructorCall2.binding);
					scriptOutput.append(", [");//$NON-NLS-1$
				} else if (argsCount > 0) {
					scriptOutput.append('[');
				}
				if (argsCount > 0) {
					this.parent.localNamesRegistry = new ParameterNamesRegistry(constructor, currentConstructorIndex);
					arguments[0].traverse(this.parent, constructor.scope);
					for (int i = 1; i < argsCount; i++) {
						scriptOutput.append(", "); //$NON-NLS-1$
						arguments[i].traverse(this.parent, constructor.scope);
					}
				}
				if (allocateArguments) {
					this.superArgsCount = -1;
					scriptOutput.append("]);").appendNewLine(); //$NON-NLS-1$
				} else if (argsCount > 0) {
					this.superArgsCount = argsCount;
					scriptOutput.append("];").appendNewLine(); //$NON-NLS-1$
				}
			} else {
				Object index = this.bindingToIndex.get(constructorCall2.binding.original());
				if (index == null) {
					this.parent.reportError(constructor.scope, "could not find constructor binding", constructor); //$NON-NLS-1$
				} else {
					int targetIndex = ((Integer) index).intValue();
					generateArguments(scriptOutput, (ConstructorDeclaration) this.constructors.get(targetIndex),
						syntheticArgsCount, targetIndex, constructorCall2, currentConstructorIndex);
				}
			}
		}
	}
	private void generateFunctionCalls(ActionScriptScribe scriptOutput, ConstructorDeclaration constructor, int syntheticArgsCount, int currentConstructorIndex) {
		int argsCount = syntheticArgsCount +
			(constructor.binding.parameters == null ? 0 : constructor.binding.parameters.length);
		if (constructor.constructorCall != null && !constructor.constructorCall.isSuperAccess()) {
			Integer index = (Integer) this.bindingToIndex.get(constructor.constructorCall.binding.original());
			if (index == null) {
				this.parent.reportError(constructor.scope, "could not find constructor binding", constructor); //$NON-NLS-1$
				return;
			}
			int targetIndex = index.intValue();
			generateFunctionCalls(scriptOutput, (ConstructorDeclaration) this.constructors.get(targetIndex), syntheticArgsCount, targetIndex);
		}
		this.parent.printIndent();
		Util.appendMethodName(scriptOutput, constructor.binding, false, false);
		scriptOutput.append('(');
		if (argsCount > 0) {
			String args = "args" + currentConstructorIndex; //$NON-NLS-1$
			scriptOutput.append(args);
			scriptOutput.append("[0]"); //$NON-NLS-1$
			for (int i = 1; i < argsCount; i++) {
				scriptOutput.append(", "); //$NON-NLS-1$
				scriptOutput.append(args);
				scriptOutput.append('[');
				scriptOutput.append(i);
				scriptOutput.append(']');
			}
		}
		scriptOutput.append(");").appendNewLine(); //$NON-NLS-1$
	}
}
class ParameterNamesRegistry extends LocalNamesRegistry {
	Map parameters = new HashMap();
	ParameterNamesRegistry(ConstructorDeclaration constructor, int constructorIndex) {
		String seed = "args" + String.valueOf(constructorIndex) + "["; //$NON-NLS-1$ //$NON-NLS-2$
		int argsCount = constructor.arguments == null ? 0 : constructor.arguments.length;
		for (int i = 0; i < argsCount; i++) {
			this.parameters.put(constructor.arguments[i].binding, seed + i + "]"); //$NON-NLS-1$
		}
	}
	public String targetName(LocalVariableBinding binding) {
		return (String) this.parameters.get(binding);
	}
}