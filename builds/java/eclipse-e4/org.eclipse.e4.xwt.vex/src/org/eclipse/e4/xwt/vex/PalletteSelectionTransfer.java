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
package org.eclipse.e4.xwt.vex;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.TransferData;

public class PalletteSelectionTransfer extends LocalSelectionTransfer {

	private static PalletteSelectionTransfer _instance = new PalletteSelectionTransfer();
	/**
	 * The get/set methods delegate to JFace's LocalSelectionTransfer to allow
	 * data to be exchanged freely whether the client uses this
	 * LocalSelectionTransfer or JFace's LocalSelectionTransfer. Protected
	 * methods such as getTypeIds() are handled via inheritance, not delegation
	 * due to visibility constraints.
	 */
	private org.eclipse.jface.util.LocalSelectionTransfer jfaceTransfer = org.eclipse.jface.util.LocalSelectionTransfer.getTransfer();

	private PalletteSelectionTransfer() {
	}

	/**
	 * Returns the singleton instance of the TextTransfer class.
	 * 
	 * @return the singleton instance of the TextTransfer class
	 */
	public static PalletteSelectionTransfer getInstance() {
		return _instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#getSelection()
	 */
	public ISelection getSelection() {
		return jfaceTransfer.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#getSelectionSetTime()
	 */
	public long getSelectionSetTime() {
		return jfaceTransfer.getSelectionSetTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection s) {
		jfaceTransfer.setSelection(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#setSelectionSetTime(long)
	 */
	public void setSelectionSetTime(long time) {
		jfaceTransfer.setSelectionSetTime(time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	public void javaToNative(Object object, TransferData transferData) {
		jfaceTransfer.javaToNative(object, transferData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.util.LocalSelectionTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	public Object nativeToJava(TransferData transferData) {
		return jfaceTransfer.nativeToJava(transferData);
	}
}