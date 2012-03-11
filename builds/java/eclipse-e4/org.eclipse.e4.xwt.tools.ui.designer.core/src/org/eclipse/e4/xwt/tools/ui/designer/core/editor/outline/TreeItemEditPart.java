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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class TreeItemEditPart extends TreeEditPart {

	private final ITreeContentProvider contentProvider;

	public TreeItemEditPart(Object model) {
		this(model, null, null);
	}

	public TreeItemEditPart(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		super(model, labelProvider);
		this.contentProvider = contentProvider;
	}

	protected List getModelChildren() {
		if (contentProvider != null) {
			Object[] children = contentProvider.getChildren(getModel());
			if (children != null && children.length > 0) {
				return new ArrayList(Arrays.asList(children));
			}
		}
		return super.getModelChildren();
	}
}
