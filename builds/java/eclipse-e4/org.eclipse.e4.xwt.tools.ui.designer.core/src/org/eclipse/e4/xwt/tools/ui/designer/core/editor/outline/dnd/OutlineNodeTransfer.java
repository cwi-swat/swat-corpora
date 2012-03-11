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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class OutlineNodeTransfer extends LocalSelectionTransfer {
	public static final OutlineNodeTransfer INSTANCE = new OutlineNodeTransfer();

	private LocalSelectionTransfer jfaceTransfer = LocalSelectionTransfer.getTransfer();

	private OutlineNodeTransfer() {
	}

	public static OutlineNodeTransfer getTransfer() {
		return INSTANCE;
	}

	public void setSelection(ISelection selection) {
		jfaceTransfer.setSelection(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#getSelection()
	 */
	public IStructuredSelection getSelection() {
		IStructuredSelection selection = (IStructuredSelection) jfaceTransfer.getSelection();
		if (selection == null) {
			selection = (IStructuredSelection) super.getSelection();
		}
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	public Object nativeToJava(TransferData transferData) {
		return jfaceTransfer.nativeToJava(transferData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	public void javaToNative(Object object, TransferData transferData) {
		jfaceTransfer.javaToNative(object, transferData);
	}
}
