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
package org.eclipse.e4.xwt.tools.ui.designer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CoolBarHelper {

	public static void layout(CoolBar coolBar) {
		if (coolBar == null || coolBar.isDisposed()) {
			return;
		}
		Composite composit = coolBar.getParent();
		Layout layout = composit.getLayout();
		if (layout instanceof RowLayout) {
			if (coolBar.getLayoutData() == null) {
				coolBar.setLayoutData(new RowData(5, 5));
			}
		}
		if (layout instanceof GridLayout) {
			if (coolBar.getLayoutData() == null || coolBar.getItems().length == 0) {
				GridData g = new GridData(GridData.FILL_BOTH);
				coolBar.setLayoutData(g);
			}
		}
		CoolItem[] coolItems = coolBar.getItems();
		for (int i = 0; i < coolItems.length; i++) {
			CoolItem coolItem = coolItems[i];
			// Point oldSize = coolItem.getSize();
			Control aControl = coolItem.getControl();
			if (aControl != null) {
				Point size = aControl.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point coolSize = coolItem.computeSize(size.x, size.y);
				if (aControl.getSize().x == 0 || aControl.getSize().y == 0) {
					aControl.setSize(coolSize);
				}
				if (aControl instanceof ToolBar) {
					ToolBar bar = (ToolBar) aControl;
					if (bar.getItemCount() > 0) {
						if ((coolBar.getStyle() & SWT.VERTICAL) != 0) {
							size.y = bar.getItem(0).getBounds().height;
						} else {
							size.x = bar.getItem(0).getWidth();
						}
					}
				}
				coolItem.setMinimumSize(size);
				// coolItem.setPreferredSize(coolSize);
				coolItem.setSize(aControl.getSize());
			}
		}
	}
}
