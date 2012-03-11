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

import org.eclipse.e4.xwt.XWTMaps;
import org.eclipse.e4.xwt.converters.StringToColor;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ColorChooser;
import org.eclipse.e4.xwt.utils.NamedColorsUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class ColorSection extends AbstractAttributeSection {

	private Button colorSelector;
	private CCombo swtColorsCombo;
	private CCombo namedColorsCombo;
	private Point fExtent;
	private Image fImage;

	private String colorStr;
	private Color fColor;

	public ColorSection() {
		colorStr = defaultColor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createSection(Composite parent) {
		Composite control = getWidgetFactory().createComposite(parent);
		control.setLayout(new GridLayout(3, false));

		swtColorsCombo = getWidgetFactory()
				.createCCombo(control, SWT.READ_ONLY);
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			gridData.widthHint = 100;
			swtColorsCombo.setLayoutData(gridData);
		}
		swtColorsCombo.setItems(XWTMaps.getColorKeys().toArray(new String[0]));
		swtColorsCombo.addListener(SWT.Selection, this);

		namedColorsCombo = getWidgetFactory().createCCombo(control,
				SWT.READ_ONLY);
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL);
			gridData.widthHint = 100;
			namedColorsCombo.setLayoutData(gridData);
		}
		namedColorsCombo.setItems(NamedColorsUtil.getColorNames());
		namedColorsCombo.addListener(SWT.Selection, this);

		colorSelector = getWidgetFactory().createButton(control, "", SWT.NONE);
		fExtent = computeImageSize(parent);
		fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);
		GC gc = new GC(fImage);
		gc.setBackground(colorSelector.getBackground());
		gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
		gc.dispose();
		colorSelector.setImage(fImage);
		colorSelector.addListener(SWT.Selection, this);
		colorSelector.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				if (fImage != null) {
					fImage.dispose();
				}
			}
		});
		return control;
	}

	protected void updateColorImage() {
		if (colorSelector == null || colorSelector.isDisposed()) {
			return;
		}

		Display display = colorSelector.getDisplay();
		if (fImage != null) {
			fImage.dispose();
		}
		fImage = new Image(display, fExtent.x, fExtent.y);
		GC gc = new GC(fImage);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
		fColor = (Color) StringToColor.instance.convert(colorStr);
		if (fColor != null && !fColor.isDisposed()) {
			gc.setBackground(fColor);
		}
		gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
		gc.dispose();
		colorSelector.setImage(fImage);
	}

	public void doRefresh() {
		colorStr = getValue();
		if (colorStr == null) {
			colorStr = defaultColor();
			if (namedColorsCombo != null && !namedColorsCombo.isDisposed()) {
				namedColorsCombo.setText("");
			}
			if (swtColorsCombo != null && !swtColorsCombo.isDisposed()) {
				swtColorsCombo.setText("");
			}
		} else {
			if (swtColorsCombo != null && !swtColorsCombo.isDisposed()
					&& swtColorsCombo.indexOf(colorStr) != -1) {
				swtColorsCombo.removeListener(SWT.Selection, this);
				swtColorsCombo.select(swtColorsCombo.indexOf(colorStr));
				swtColorsCombo.addListener(SWT.Selection, this);
				namedColorsCombo.setText("");
			}
			if (namedColorsCombo != null && !namedColorsCombo.isDisposed()
					&& namedColorsCombo.indexOf(colorStr) != -1) {
				namedColorsCombo.removeListener(SWT.Selection, this);
				namedColorsCombo.select(namedColorsCombo.indexOf(colorStr));
				namedColorsCombo.addListener(SWT.Selection, this);
				swtColorsCombo.setText("");
			}
		}
		updateColorImage();
	}

	private Point computeImageSize(Control window) {
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get(
				JFaceResources.DIALOG_FONT);
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections.
	 * AbstractAttributeSection#getNewValue()
	 */
	protected String getNewValue(Event event) {
		if (event.widget == colorSelector) {
			colorStr = chooseColor();
		} else if (event.widget == swtColorsCombo) {
			colorStr = swtColorsCombo.getItem(swtColorsCombo
					.getSelectionIndex());
			namedColorsCombo.setText("");
		} else if (event.widget == namedColorsCombo) {
			colorStr = namedColorsCombo.getItem(namedColorsCombo
					.getSelectionIndex());
			swtColorsCombo.setText("");
		}
		updateColorImage();
		return colorStr;
	}

	private String chooseColor() {
		ColorChooser chooser = new ColorChooser(new Shell(), colorStr);
		if (chooser.open() == Window.OK) {
			return chooser.getColor();
		}
		return colorStr;
	}

	protected abstract String defaultColor();
}
