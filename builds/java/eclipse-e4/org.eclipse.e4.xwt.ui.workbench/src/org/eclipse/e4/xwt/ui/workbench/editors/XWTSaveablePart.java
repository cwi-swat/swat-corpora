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
package org.eclipse.e4.xwt.ui.workbench.editors;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.ui.workbench.views.XWTStaticPart;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.swt.widgets.Control;

/**
 * This class defines the common infrastructure as editor part of eclipse workbench.
 * 
 * @author yyang <yves.yang@soyatec.com>
 */
public abstract class XWTSaveablePart extends XWTStaticPart {
	@Inject
	private MDirtyable dirtyable;
	
	@Inject
	private MUILabel uiItem;
	
	private DirtyManager dirtyManager = new DirtyManager();
	
	class DirtyManager implements IChangeListener {
		public void handleChange(ChangeEvent event) {
			if (dirtyable.isDirty()) {
				return;
			}
			Object source = event.getSource();
			if (!(source instanceof ISWTObservable)) {
				setDirty(true);
			}
		}
	}

	// TBD is this the right place for the @Persist tag?
	@Persist
	public abstract void doSave(@Optional IProgressMonitor monitor) throws IOException,
	InterruptedException;
 	
	public boolean isSaveOnCloseNeeded() {
		return true;
	}
	
	@Override
	protected void refresh(URL url, Object dataContext, ClassLoader loader) {
		Control loadedRoot = null;
		Control[] children = getParent().getChildren();
		if (children.length > 0) {
			loadedRoot = children[0];
		}
		if (loadedRoot != null) {
			XWT.removeObservableChangeListener(loadedRoot, dirtyManager);			
		}
		
		super.refresh(url, dataContext, loader);
		
		dirtyable.setDirty(false);
		
		children = getParent().getChildren();
		if (children.length > 0) {
			loadedRoot = children[0];
		}
		if (loadedRoot != null) {
			XWT.addObservableChangeListener(loadedRoot, dirtyManager);
		}
	}
	
	protected void updatePartTitle(String title) {
		uiItem.setLabel(title.toString());
	}
		
	public boolean isDirty() {
		return dirtyable.isDirty();
	}
	
	public void setDirty(Boolean dirty) {
		dirtyable.setDirty(dirty);
	}
}