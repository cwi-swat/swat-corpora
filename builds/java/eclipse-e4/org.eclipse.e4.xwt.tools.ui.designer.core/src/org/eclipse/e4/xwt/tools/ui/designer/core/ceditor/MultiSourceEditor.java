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
package org.eclipse.e4.xwt.tools.ui.designer.core.ceditor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class MultiSourceEditor extends MultiPageEditorPart {

	private ISourcePage[] sourcePages;

	private IEditorPart primaryEditor;

	public MultiSourceEditor(IEditorPart primaryEditor) {
		this.primaryEditor = primaryEditor;
		sourcePages = SourcePageRegistry.getSourcePages(primaryEditor.getSite()
				.getId());
	}

	protected void createPages() {
		if (!testValid()) {
			return;
		}
		for (ISourcePage sourcePage : sourcePages) {
			if (!sourcePage.isEnabledFor(primaryEditor)) {
				continue;
			}
			createAddPage(sourcePage);
		}

		Composite container = getContainer();
		if (container instanceof CTabFolder) {
			CTabFolder ctf = ((CTabFolder) container);
			ctf.setTabPosition(SWT.TOP);
			ctf.setSimple(false);
		}
	}

	private void createAddPage(ISourcePage sourcePage) {
		try {
			int pageIndex = addPage(sourcePage, getEditorInput());
			String pageName = sourcePage.getPageName();
			setPageText(pageIndex, pageName == null ? "" : pageName);
			sourcePage.configureSourcePage(this);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public void doSave(IProgressMonitor monitor) {
		IEditorPart activeEditor = getActiveEditor();
		if (activeEditor != null) {
			activeEditor.doSave(monitor);
		}
	}

	protected void setActivePage(int pageIndex) {
		if (pageIndex < 0 || pageIndex >= getPageCount()) {
			return;
		}
		super.setActivePage(pageIndex);
	}

	public void doSaveAs() {
		IEditorPart activeEditor = getActiveEditor();
		if (activeEditor != null) {
			activeEditor.doSaveAs();
		}
	}

	public boolean isSaveAsAllowed() {
		IEditorPart activeEditor = getActiveEditor();
		if (activeEditor != null) {
			return activeEditor.isSaveAsAllowed();
		}
		return false;
	}

	public boolean testValid() {
		if (sourcePages == null) {
			sourcePages = SourcePageRegistry.getSourcePages(primaryEditor
					.getSite().getId());
		}
		if (sourcePages.length <= 0) {
			return false;
		}
		boolean enabled = false;
		for (ISourcePage page : sourcePages) {
			enabled = enabled || page.isEnabledFor(null);
		}
		return enabled;
	}

	public Object getAdapter(Class adapter) {
		if (Designer.class == adapter) {
			return primaryEditor;
		}
		return super.getAdapter(adapter);
	}
}
