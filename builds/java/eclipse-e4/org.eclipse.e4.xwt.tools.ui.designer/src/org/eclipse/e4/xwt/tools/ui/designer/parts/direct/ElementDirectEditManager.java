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
package org.eclipse.e4.xwt.tools.ui.designer.parts.direct;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class ElementDirectEditManager extends DirectEditManager {
	protected VerifyListener verifyListener;
	protected IFigure figure;

	public ElementDirectEditManager(GraphicalEditPart source, Class editorType,
			CellEditorLocator locator, IFigure figure) {
		super(source, editorType, locator);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	protected void initCellEditor() {
		verifyListener = new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				CellEditor cellEditor = getCellEditor();
				if (cellEditor == null) {
					return;
				}
				Text text = (Text) cellEditor.getControl();
				if (text == null || text.isDisposed()) {
					return;
				}
				text.setFocus();
				String oldText = text.getText();
				String leftText = oldText.substring(0, event.start);
				String rightText = oldText.substring(event.end, oldText
						.length());
				GC gc = new GC(text);
				Point size = gc.textExtent(leftText + event.text + rightText);
				gc.dispose();
				// if (size.x != 0) {
				size = text.computeSize(50, SWT.DEFAULT);
				// }
				Display d = text.getDisplay();
				if (text != null && !text.isDisposed() && d != null
						&& !d.isDisposed()) {
					text.setSize(size.x, size.y);
				}
			}

		};

		Text text = (Text) getCellEditor().getControl();
		text.addVerifyListener(verifyListener);
		text.selectAll();
		text.setFocus();
	}

}
