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

import java.util.HashMap;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;

public class LocalNamesRegistry {
	HashMap sourceDeclarations, targetNames;
	
	public static LocalNamesRegistry newInstance(AbstractMethodDeclaration methodDeclaration, LocalNamesRegistry registry) {
		LocalNamesRegistry newRegistry = new LocalNamesRegistry(registry);
		MethodBinding methodBinding = methodDeclaration.binding;
		if (methodBinding != null && !methodBinding.isConstructor() && !methodBinding.isStatic()) {
			ReferenceBinding referenceBinding = methodBinding.declaringClass;
			while (referenceBinding != null) {
				FieldBinding[] fields = referenceBinding.fields();
				for (int i = 0, max = fields.length; i < max; i++) {
					newRegistry.addTargetName(fields[i].name);
				}
				referenceBinding = referenceBinding.superclass();
			}
		}
		return newRegistry;
	}
	public LocalNamesRegistry() {
		this.sourceDeclarations = new HashMap();
		// pre-reserve AS keywords that are not Java keywords 
		this.targetNames = (HashMap) KeywordsRegistry.KEYWORDS.clone();
	}
	
	private LocalNamesRegistry(LocalNamesRegistry prototype) {
		this.sourceDeclarations = (HashMap) prototype.sourceDeclarations.clone();
		this.targetNames = (HashMap) prototype.targetNames.clone();
	}
	
	public void checkPackage(char[][] packageName, int significantTokensNb) {
		if (significantTokensNb != 0) {
			int start = CharOperation.equals(Util.INTRINSIC, packageName[0]) ? 1 : 0;
			if (significantTokensNb - start == 1) {
				this.targetNames.put(new String(packageName[start]), this /* non null */);
			}
		}
	}
	public String targetName(LocalVariableBinding binding) {
		char[] readableName = binding.readableName();
		if (CharOperation.isWhitespace(readableName[0])) {
			return targetName(binding, new String(readableName).trim());
		}
		return targetName(binding, new String(readableName));
	}
	// public for the sake of testing only
	public String targetName(Object key, String sourceName) {
		String targetName = (String) this.sourceDeclarations.get(key);
		if (targetName == null) {
			targetName = sourceName;
			if (this.targetNames.get(targetName) != null) {
				targetName += "_1"; //$NON-NLS-1$
				int l = targetName.lastIndexOf('_') + 1;
				for (int i = 1; this.targetNames.get(targetName) != null; i++) {
					targetName = targetName.substring(0, l) + i;
				}
			}
			this.sourceDeclarations.put(key, targetName);
			this.targetNames.put(targetName, key);
		}
		return targetName;
	}
	
	// add a known name into the use name for locals
	public void addTargetName(char[] name) {
		String s = new String(name);
		this.targetNames.put(s, s);
	}
}