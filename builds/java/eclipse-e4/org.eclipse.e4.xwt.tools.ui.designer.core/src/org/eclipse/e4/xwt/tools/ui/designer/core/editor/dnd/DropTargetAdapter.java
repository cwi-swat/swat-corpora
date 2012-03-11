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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class DropTargetAdapter implements DropTargetListener {

	private List<DropAdapter> dropAdapters = new ArrayList<DropAdapter>();
	private DropAdapter acceptAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		updateAcceptAdapter();
		if (acceptAdapter != null) {
			acceptAdapter.dragOver(event);
		}
	}

	/**
	 * @param event
	 */
	private void updateAcceptAdapter() {
		for (DropAdapter drop : dropAdapters) {
			if (drop.isAccept()) {
				acceptAdapter = drop;
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		if (acceptAdapter != null) {
			acceptAdapter.dropAccept(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		if (acceptAdapter != null) {
			acceptAdapter.drop(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		updateAcceptAdapter();
		if (acceptAdapter != null) {
			acceptAdapter.dragEnter(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		updateAcceptAdapter();
		if (acceptAdapter != null) {
			acceptAdapter.dragOperationChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		if (acceptAdapter != null) {
			acceptAdapter.dragLeave(event);
		}
	}

	public void addDropAdapter(DropAdapter adapter) {
		dropAdapters.add(adapter);
	}

	/**
	 * @param dndAdapters
	 *            the dndAdapters to set
	 */
	public void setDropAdapters(List<DropAdapter> dndAdapters) {
		this.dropAdapters = dndAdapters;
	}

	/**
	 * @return the dndAdapters
	 */
	public List<DropAdapter> getDropAdapters() {
		return dropAdapters;
	}
}
