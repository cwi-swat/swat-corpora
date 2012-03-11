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
package org.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ImageDialog;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ImageSection extends AbstractAttributeSection {

	private Text imageText;
	private Button chooseButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		Composite control = getWidgetFactory().createComposite(parent);
		control.setLayout(new GridLayout(2, false));
		imageText = getWidgetFactory().createText(control, "",
				SWT.BORDER | SWT.READ_ONLY);
		imageText.setBackground(control.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		imageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		imageText.addListener(SWT.Modify, this);

		chooseButton = getWidgetFactory().createButton(control, "", SWT.PUSH);
		chooseButton.setImage(ImageShop.get(ImageShop.IMAGE_OBSERVE_CUSTOM));
		chooseButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				chooseImage();
			}
		});
		return control;
	}

	public void doRefresh() {
		if (imageText == null || imageText.isDisposed()) {
			return;
		}
		String value = getValue();
		imageText.removeListener(SWT.Modify, this);
		imageText.setText(value == null ? "" : value);
		imageText.addListener(SWT.Modify, this);
	}

	protected void chooseImage() {
		IWorkbenchPart part = getPart();
		Shell shell = part.getSite().getShell();
		IFile file = (IFile) part.getAdapter(IFile.class);
		ImageDialog dialog = new ImageDialog(shell, file);
		if (dialog.open() == Window.OK) {
			String imagePath = dialog.getImagePath();
			if (imagePath != null) {
				imageText.setText(imagePath);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getAttributeName()
	 */
	protected String getAttributeName() {
		return "image";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getNewValue(org.eclipse.swt.widgets.Event)
	 */
	protected String getNewValue(Event event) {
		return imageText.getText();
	}

}
