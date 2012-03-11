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

import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.designer.utils.FontUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class FontSection extends AbstractAttributeSection {

	private CCombo fontFamilyCombo;
	private CCombo fontSizeCombo;
	private Button fontBoldButton;
	private Button fontItalicButton;

	private String fontName;
	private int height;
	private int style;

	public FontSection() {
		initDefaults();
	}

	private void initDefaults() {
		try {
			Font systemFont = Display.getCurrent().getSystemFont();
			if (systemFont != null) {
				FontData fd = systemFont.getFontData()[0];
				fontName = fd.getName();
				height = fd.getHeight();
				style = fd.getStyle();
			}
		} catch (Exception e) {
			System.out.println();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		Composite fontGroup = getWidgetFactory().createComposite(parent);
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 1;
		fontGroup.setLayout(layout);

		fontFamilyCombo = getWidgetFactory().createCCombo(fontGroup,
				SWT.READ_ONLY);
		fontFamilyCombo.setItems(FontUtil.getFontNames());
		fontFamilyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fontFamilyCombo.addListener(SWT.Selection, this);

		fontSizeCombo = getWidgetFactory().createCCombo(fontGroup,
				SWT.READ_ONLY);
		fontSizeCombo.setItems(FontUtil.getFontSizes());
		fontSizeCombo.addListener(SWT.Selection, this);

		fontBoldButton = getWidgetFactory().createButton(fontGroup, "",
				SWT.TOGGLE);
		fontBoldButton.setImage(ImageShop.get(ImageShop.IMG_BOLD));
		fontBoldButton.setToolTipText("font bold");
		fontBoldButton.addListener(SWT.Selection, this);

		fontItalicButton = getWidgetFactory().createButton(fontGroup, "",
				SWT.TOGGLE);
		fontItalicButton.setImage(ImageShop.get(ImageShop.IMG_ITALIC));
		fontItalicButton.setToolTipText("font italic");
		fontItalicButton.addListener(SWT.Selection, this);

		return fontGroup;
	}

	public void doRefresh() {
		FontData fd = FontUtil.getFontData(getValue());
		if (fd != null) {
			fontName = fd.getName();
			height = fd.getHeight();
			style = fd.getStyle();
		} else {
			initDefaults();
		}
		if (fontFamilyCombo != null && !fontFamilyCombo.isDisposed()) {
			fontFamilyCombo.removeListener(SWT.Selection, this);
			int index = fontFamilyCombo.indexOf(fontName);
			fontFamilyCombo.select(index);
			fontFamilyCombo.addListener(SWT.Selection, this);
		}
		if (fontSizeCombo != null && !fontSizeCombo.isDisposed()) {
			fontSizeCombo.removeListener(SWT.Selection, this);
			int index = fontSizeCombo.indexOf(Integer.toString(height));
			fontSizeCombo.select(index);
			fontSizeCombo.addListener(SWT.Selection, this);
		}
		if (fontBoldButton != null && !fontBoldButton.isDisposed()) {
			fontBoldButton.removeListener(SWT.Selection, this);
			fontBoldButton.setSelection((SWT.BOLD & style) != 0);
			fontBoldButton.addListener(SWT.Selection, this);
		}
		if (fontItalicButton != null && !fontItalicButton.isDisposed()) {
			fontItalicButton.removeListener(SWT.Selection, this);
			fontItalicButton.setSelection((SWT.ITALIC & style) != 0);
			fontItalicButton.addListener(SWT.Selection, this);
		}
		super.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getNewValue()
	 */
	protected String getNewValue(Event event) {
		Widget widget = event.widget;
		if (widget == fontFamilyCombo) {
			fontName = (fontFamilyCombo.getText());
		} else if (widget == fontSizeCombo) {
			String text = fontSizeCombo.getText();
			try {
				height = Integer.parseInt(text);
			} catch (NumberFormatException e) {
			}
		} else if (widget == fontBoldButton) {
			if (fontBoldButton.getSelection()) {
				style |= SWT.BOLD;
			} else {
				style ^= SWT.BOLD;
			}
		} else if (widget == fontItalicButton) {
			if (fontItalicButton.getSelection()) {
				style |= SWT.ITALIC;
			} else {
				style ^= SWT.ITALIC;
			}
		}
		return FontUtil.getFontStr(new FontData(fontName, height, style));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getAttributeName()
	 */
	protected String getAttributeName() {
		return "font";
	}

}
