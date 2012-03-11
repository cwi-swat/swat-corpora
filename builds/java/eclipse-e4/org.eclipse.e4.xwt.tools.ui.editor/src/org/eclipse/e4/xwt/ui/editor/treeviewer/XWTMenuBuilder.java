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
package org.eclipse.e4.xwt.ui.editor.treeviewer;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.xml.ui.internal.actions.MenuBuilder;

public class XWTMenuBuilder extends MenuBuilder {
	protected ImageDescriptor image;

	public XWTMenuBuilder() {
	}

	protected void createAlphebeticalGrouping(IMenuManager menu, List actionList) {
		Object[] array = actionList.toArray();
		if (array.length > 0) {
			Arrays.sort(array, comparator);
		}

		int groupSize = 15;
		int minGroupSize = 5;
		int numberOfGroups = (array.length / groupSize) + ((array.length % groupSize > minGroupSize) ? 1 : 0);

		for (int i = 0; i < numberOfGroups; i++) {
			boolean isLastGroup = (i == (numberOfGroups - 1));
			int firstIndex = i * groupSize;
			int lastIndex = isLastGroup ? array.length - 1 : i * groupSize + groupSize - 1;
			Action firstAction = (Action) array[firstIndex];
			Action lastAction = (Action) array[lastIndex];
			ImageMenuManager submenu = new ImageMenuManager(firstAction.getText() + " - " + lastAction.getText(), image); //$NON-NLS-1$
			menu.add(submenu);
			for (int j = firstIndex; j <= lastIndex; j++) {
				submenu.add((Action) array[j]);
			}
		}
	}

	public ImageDescriptor getImageDescriptor() {
		return image;
	}

	public void setImageDescriptor(ImageDescriptor image) {
		this.image = image;
	}
}
