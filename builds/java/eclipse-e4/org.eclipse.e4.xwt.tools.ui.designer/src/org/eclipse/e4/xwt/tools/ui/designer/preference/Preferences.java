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
package org.eclipse.e4.xwt.tools.ui.designer.preference;

import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class Preferences {
	public static final String PROMPT_DURING_CREATION = "Prompt for widget name during creation.";
	public static final String DEFAULT_LAYOUT = "Default Layout";

	public static IPreferenceStore getPreferenceStore() {
		IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
		return preferenceStore;
	}

}
