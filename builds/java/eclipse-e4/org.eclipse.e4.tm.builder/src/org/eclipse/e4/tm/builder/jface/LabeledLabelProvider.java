/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.builder.jface;

import org.eclipse.e4.tm.util.Labeled;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class LabeledLabelProvider extends LabelProvider {

	private Labeled label;
	
	public LabeledLabelProvider(Labeled label) {
		super();
		this.label = label;
	}

	public Image getImage(Object element) {
		prepareDataObject(element);
		return null; // label.getImage();
	}

	private void prepareDataObject(Object element) {
		label.setDataObject(element);
	}

	public String getText(Object element) {
		// default to either format or toString
		label.setText(getLabeledText(label, element));
		prepareDataObject(element);
		return label.getText();
	}

	public static String getLabeledText(Labeled label, Object element) {
		String format = label.getFormat();
		return (format != null ? String.format(format, element) : String.valueOf(element));
	}
}
