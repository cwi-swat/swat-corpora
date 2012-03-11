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

import java.util.HashMap;

import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class FieldNamesRegistry {
	HashMap targetNames = new HashMap();
	
	public void fieldHiding(FieldBinding mask) {
		if (!mask.isStatic()) {
			this.targetNames.put(mask, new String(mask.readableName()) + typeName(mask.declaringClass));
		}
	}
	
	public String targetName(FieldBinding binding) {
		String targetName = (String) this.targetNames.get(binding);
		if (targetName == null) {
			targetName = new String(binding.readableName());
			if (KeywordsRegistry.KEYWORDS.get(targetName) != null) {
				targetName += typeName(binding.declaringClass);
			}
			this.targetNames.put(binding, targetName);
		}
		return targetName;
	}
	
	private String typeName(TypeBinding binding) {
		return "_" + Util.typeName(binding, true).replaceAll("\\.", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}