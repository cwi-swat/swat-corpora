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
package org.eclipse.e4.xwt.forms;

import org.eclipse.e4.xwt.forms.metaclass.FormMetaclass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 
 * @author yyang (yves.yang@soyaetc.com)
 */
public class ToolKitUtil {
	private static final String FORM_SIGNATURE_KEY = XWTForms.class.getName();

	static public synchronized FormToolkit getToolkit(Control c) {
		FormToolkit tk = findToolkit(c);
		if (tk == null) {
			tk = new FormToolkit(c.getDisplay());
			c.getDisplay().setData(FormMetaclass.class.getName(), tk);
		}
		return tk;
	}

	static public synchronized FormToolkit findToolkit(Control c) {
		return (FormToolkit) c.getDisplay().getData(
				FormMetaclass.class.getName());
	}

	public static void tagForm(Control control) {
		control.setData(FORM_SIGNATURE_KEY, Boolean.TRUE);
	}

	/**
	 * Adapts the form toolkit colors to non-form controls
	 * 
	 * @param root
	 *            widget hierarchy
	 */
	public static void adapt(Control element) {
		FormToolkit tk = getToolkit(element);
		if (tk != null && element.getData(FORM_SIGNATURE_KEY) != Boolean.TRUE) {
			tk.adapt(element, true, true);
		}
	}

	/**
	 * depth-first adaptation of form toolkit colors
	 */
	private static void adaptRecursive(Control root, FormToolkit tk) {
		if (root instanceof Composite) {
			if (root.getData(FORM_SIGNATURE_KEY) == Boolean.TRUE) {
				tk = getToolkit(root);
			}
			else if (tk != null) {
				tk.adapt((Composite) root);
			}
			
			Control[] children = ((Composite) root).getChildren();
			if (root instanceof ExpandableComposite) {
				boolean firstLabel = true;
				for (int i = 0; i < children.length; i++) {
					Control child = children[i];
					// it seems a bug of Section, the background of the title
					// becomes opaque, so we have to ignore to adapt it
					if (firstLabel) {
						if (child instanceof Label) {
							firstLabel = false;
						}
						continue;
					}
					adaptRecursive(child, tk);
				}				
			}
			else {
				for (Control child : children) {
					adaptRecursive(child, tk);
				}
			}
		} else {
			if (root.getData(FORM_SIGNATURE_KEY) != Boolean.TRUE) {
				tk.adapt(root, true, true);
			}
		}
	}
}
