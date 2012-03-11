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

import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.impl.IrritantSet;

public class ASCompilerOptions extends org.eclipse.jdt.internal.compiler.impl.CompilerOptions {
	
	public static final String OPTION_ReportNonConstStaticFinalField = "org.eclipse.jdt.core.compiler.problem.as.nonConstStaticFinalField"; //$NON-NLS-1$
	public static final int NonConstStaticFinalField = IrritantSet.GROUP2 + ASTNode.Bit15; // offset behind base compiler irritants
	public static final String OPTION_DisableWarnings = "org.eclipse.jdt.core.compiler.problem.disableWarnings"; //$NON-NLS-1$

	// disable all warnings
	public boolean disableWarnings;

	public ASCompilerOptions() {
		super();
	}
	public ASCompilerOptions(Map settings) {
		super(settings);
	}
	
	protected void resetDefaults() {
		super.resetDefaults();
		this.warningThreshold.set(NonConstStaticFinalField);
	}
	
	public Map getMap() {
		Map optionsMap = super.getMap();
		optionsMap.put(OPTION_ReportNonConstStaticFinalField, getSeverityString(NonConstStaticFinalField));
		optionsMap.put(OPTION_DisableWarnings, this.disableWarnings ? DISABLED : ENABLED);
		return optionsMap;
	}

	public void set(Map optionsMap) {
		super.set(optionsMap);
		Object optionValue;
		if ((optionValue = optionsMap.get(ASCompilerOptions.OPTION_DisableWarnings)) != null) {
			if (ENABLED.equals(optionValue)) {
				this.disableWarnings = false;
			} else if (DISABLED.equals(optionValue)) {
				this.disableWarnings = true;
			}
		}
	}
}
