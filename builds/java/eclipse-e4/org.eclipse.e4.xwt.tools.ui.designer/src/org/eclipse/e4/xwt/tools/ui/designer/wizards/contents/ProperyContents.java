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
package org.eclipse.e4.xwt.tools.ui.designer.wizards.contents;

import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class ProperyContents {
	private TextValueModel checkedItems;

	public ProperyContents(TextValueModel checkedItems) {
		this.checkedItems = checkedItems;
	}

	public String getPropertyContents() {
		StringBuffer contents = new StringBuffer();
		String value = "";
		String key = "";
		int itemsLen = checkedItems.elements().length;
		for (int i = 0; i < itemsLen; i++) {
			if (checkedItems.elements()[i] instanceof TextValueEntry) {
				key = ((TextValueEntry) checkedItems.elements()[i]).getKey();
				value = ((TextValueEntry) checkedItems.elements()[i]).getValue();
				contents.append(key + " = " + value + "\r");
			}
		}
		return contents.toString();
	}
}
