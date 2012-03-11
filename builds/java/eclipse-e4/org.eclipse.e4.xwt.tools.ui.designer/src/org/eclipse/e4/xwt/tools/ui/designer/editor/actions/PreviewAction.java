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
package org.eclipse.e4.xwt.tools.ui.designer.editor.actions;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class PreviewAction extends SelectionAction {

	public static final String ACTION_ID = "org.eclipse.e4.xwt.tools.ui.designer.editor.actions.PreviewAction";

	private XWTDesigner part;
	private static Point defaultSize;

	public PreviewAction(XWTDesigner part) {
		super(part);
		this.part = part;
		setId(ACTION_ID);
		setText("Test/Preview");
		setToolTipText("Quickly test/preview with XWT loader");
		setImageDescriptor(ImageShop.getImageDescriptor(ImageShop.IMG_PREVIEW));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		try {
			IFile inputFile = part.getFile();
			String content = part.getDocument().get();
			Object element = XWT.load(new ByteArrayInputStream(content.getBytes()), inputFile.getLocationURI().toURL());
			if (!(element instanceof Control)) {
				throw new XWTException("Root element is a control.");
			}
			Control control = (Control) element;
			if (control == null || control.isDisposed()) {
				return;
			}
			Shell shell = control.getShell();
			Point size = shell.getSize();
			Point dSize = defaultSize();
			if (size.x == dSize.x && size.y == dSize.y) {
				Point computeSize = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point defaultSize = XWTProxy.DEFAULT_SIZE;
				int x = Math.max(computeSize.x, defaultSize.x);
				int y = Math.max(computeSize.y, defaultSize.y);
				boolean pack = false;
				if (!(control instanceof Shell)) {
					shell.setLayout(null);
					pack = true;
				}
				control.setSize(x, y);
				if (pack) {
					shell.pack();
				}
			}
			shell.open();
		} catch (Exception e) {
		}
	}

	private static Point defaultSize() {
		if (defaultSize == null) {
			defaultSize = new Shell().getSize();
		}
		return defaultSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		return part != null && part.getDocument() != null;
	}
}
