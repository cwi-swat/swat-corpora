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
package org.eclipse.e4.xwt.vex.templates;

import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.text.templates.TemplateVariableType;

public class VEXTemplateVariable extends TemplateVariable {

	public VEXTemplateVariable(String type, String defaultValue, int[] offsets) {
		super(type, defaultValue, offsets);
	}

	public VEXTemplateVariable(String type, String[] values, int[] offsets) {
		super(type, values, offsets);
	}

	public VEXTemplateVariable(String type, String name, String defaultValue, int[] offsets) {
		super(type, name, defaultValue, offsets);
	}

	public VEXTemplateVariable(TemplateVariableType type, String name, String defaultValue, int[] offsets) {
		super(type, name, defaultValue, offsets);
	}

	public VEXTemplateVariable(String type, String name, String[] values, int[] offsets) {
		super(type, name, values, offsets);
	}

}
