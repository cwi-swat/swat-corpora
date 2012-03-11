/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java.hover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.jsdt.ui.text.java.hover.IJavaEditorTextHover;


/**
 * Proxy for JavaEditorTextHovers.
 */
public class JavaEditorTextHoverProxy extends AbstractJavaEditorTextHover implements IInformationProviderExtension2 {

	private JavaEditorTextHoverDescriptor fHoverDescriptor;
	private IJavaEditorTextHover fHover;

	public JavaEditorTextHoverProxy(JavaEditorTextHoverDescriptor descriptor, IEditorPart editor) {
		fHoverDescriptor= descriptor;
		setEditor(editor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#setEditor(org.eclipse.ui.IEditorPart)
	 */
	public void setEditor(IEditorPart editor) {
		super.setEditor(editor);

		if (fHover != null)
			fHover.setEditor(getEditor());
	}

	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		if (ensureHoverCreated())
			return fHover.getHoverRegion(textViewer, offset);

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (ensureHoverCreated())
			return fHover.getHoverInfo(textViewer, hoverRegion);

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 * @since 3.3
	 */
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		if (ensureHoverCreated()) {
			if (fHover instanceof ITextHoverExtension2) {
				return ((ITextHoverExtension2) fHover).getHoverInfo2(textViewer, hoverRegion);
			}
			else {
				return fHover.getHoverInfo(textViewer, hoverRegion);
			}
		}
		return null;
	}
	
	private boolean ensureHoverCreated() {
		if (!isEnabled() || fHoverDescriptor == null)
			return false;
		return isCreated() || createHover();
	}

	private boolean isCreated() {
		return fHover != null;
	}

	private boolean createHover() {
		fHover= fHoverDescriptor.createTextHover();
		if (fHover != null)
			fHover.setEditor(getEditor());
		return isCreated();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#getHoverControlCreator()
	 */
	public IInformationControlCreator getHoverControlCreator() {
		if (ensureHoverCreated() && (fHover instanceof ITextHoverExtension))
			return ((ITextHoverExtension)fHover).getHoverControlCreator();

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.information.IInformationProviderExtension2#getInformationPresenterControlCreator()
	 */
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (ensureHoverCreated() && (fHover instanceof IInformationProviderExtension2))
			return ((IInformationProviderExtension2)fHover).getInformationPresenterControlCreator();

		return null;
	}
}
