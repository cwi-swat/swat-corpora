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
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTMaps;
import org.eclipse.e4.xwt.utils.NamedColorsUtil;
import org.eclipse.e4.xwt.utils.ResourceManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ColorChooser extends Dialog {
	private Canvas previewCanvas;
	private Text redText;
	private Text greenText;
	private Text blueText;
	private Text resultText;

	private Object initValue;
	private Map<Object, Color> colorsMap = new HashMap<Object, Color>();

	private String result;

	private static final Color BACKGROUND = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	private Control selection;

	public ColorChooser(Shell parent, Object initValue) {
		super(parent);
		this.initValue = initValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Color Chooser");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		dialogArea.setLayout(new GridLayout(1, true));

		createPreviewGrp(dialogArea);

		createColorGrp(dialogArea);
		if (initValue != null) {
			if (initValue instanceof String) {
				preview((String) initValue);
			} else if (initValue instanceof Color) {
				preview((Color) initValue);
			}
		}

		return dialogArea;
	}

	private void createColorGrp(Composite dialogArea) {
		Group colorGrp = new Group(dialogArea, SWT.NONE);
		colorGrp.setText("Color");
		colorGrp.setLayout(new GridLayout());
		colorGrp.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabFolder tabFolder = new TabFolder(colorGrp, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem systemTab = new TabItem(tabFolder, SWT.NONE);
		systemTab.setText("System Colors");
		systemTab.setControl(createSystemColorsControl(tabFolder));

		TabItem namedTab = new TabItem(tabFolder, SWT.NONE);
		namedTab.setText("Named Colors");
		namedTab.setControl(createNamedColorsControl(tabFolder));

		Button browserButton = new Button(colorGrp, SWT.PUSH);
		browserButton.setText("System Color Dialog");
		browserButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		browserButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ColorDialog colorDialog = new ColorDialog(getShell());
				RGB open = colorDialog.open();
				if (open != null) {
					preview(open);
				}
			}
		});
	}

	private Control createNamedColorsControl(Composite parent) {
		ScrolledComposite scrollable = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		Composite control = new Composite(scrollable, SWT.NONE);
		control.setLayout(new GridLayout(16, true));
		control.setBackground(BACKGROUND);
		String[] namedColors = NamedColorsUtil.getColorNames();
		for (String colorName : namedColors) {
			addNamedColor(control, colorName);
		}
		scrollable.setContent(control);
		scrollable.setExpandVertical(true);
		scrollable.setExpandHorizontal(true);
		scrollable.setMinSize(control.computeSize(-200, SWT.DEFAULT));
		scrollable.setBackground(BACKGROUND);
		return scrollable;
	}

	private void addNamedColor(Composite control, final String colorName) {
		final CLabel colorLabel = new CLabel(control, SWT.NONE);
		colorLabel.setBackground(BACKGROUND);
		// colorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorLabel.setToolTipText(colorName);
		// colorLabel.setText(colorName);
		Color color = NamedColorsUtil.getColor(colorName);
		if (color != null) {
			Display device = Display.getCurrent();
			Image image = new Image(device, 18, 18);
			GC gc = new GC(image);
			gc.setBackground(color);
			gc.fillRectangle(0, 0, 18, 18);
			gc.dispose();
			colorLabel.setImage(image);
		}
		final Display display = getShell().getDisplay();
		colorLabel.addListener(SWT.MouseExit, new Listener() {
			public void handleEvent(Event event) {
				if (colorLabel != selection) {
					colorLabel.setBackground(BACKGROUND);
				}
			}
		});

		colorLabel.addListener(SWT.MouseEnter, new Listener() {
			public void handleEvent(Event event) {
				if (colorLabel != selection) {
					colorLabel.setBackground(new Color[] { display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND) }, new int[] { 100 }, true);
				}
			}
		});

		colorLabel.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Control oldSelection = selection;
				if (oldSelection == null || oldSelection != colorLabel) {
					colorLabel.setBackground(new Color[] { display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_GRAY) }, new int[] { 100 }, true);
					preview(colorName);
					selection = colorLabel;
				} else {
					selection = null;
					preview(null);
				}
				if (oldSelection != null) {
					oldSelection.setBackground(BACKGROUND);
				}
			}
		});
	}

	private Control createSystemColorsControl(Composite parent) {
		Composite control = new Composite(parent, SWT.BORDER);
		control.setLayout(new GridLayout(16, true));
		control.setBackground(BACKGROUND);
		Collection<String> systemColors = XWTMaps.getColorKeys();
		for (String sysColor : systemColors) {
			addSystemColor(control, sysColor);
		}
		return control;
	}

	private void addSystemColor(Composite parent, final String systemColor) {
		final CLabel colorLabel = new CLabel(parent, SWT.NONE);
		colorLabel.setBackground(BACKGROUND);
		colorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorLabel.setToolTipText(systemColor);
		Color color = ResourceManager.resources.getColor(systemColor);
		if (color != null) {
			Display device = Display.getCurrent();
			Image image = new Image(device, 18, 18);
			GC gc = new GC(image);
			gc.setBackground(color);
			gc.fillRectangle(0, 0, 18, 18);
			gc.dispose();
			colorLabel.setImage(image);
		}
		final Display display = getShell().getDisplay();
		colorLabel.addListener(SWT.MouseExit, new Listener() {
			public void handleEvent(Event event) {
				if (colorLabel != selection) {
					colorLabel.setBackground(BACKGROUND);
				}
			}
		});

		colorLabel.addListener(SWT.MouseEnter, new Listener() {
			public void handleEvent(Event event) {
				if (colorLabel != selection) {
					colorLabel.setBackground(new Color[] { display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND) }, new int[] { 100 }, true);
				}
			}
		});

		colorLabel.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Control oldSelection = selection;
				if (oldSelection == null || oldSelection != colorLabel) {
					colorLabel.setBackground(new Color[] { display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_GRAY) }, new int[] { 100 }, true);
					preview(systemColor);
					selection = colorLabel;
				} else {
					selection = null;
					preview(null);
				}
				if (oldSelection != null) {
					oldSelection.setBackground(BACKGROUND);
				}
			}
		});
	}

	private Color getColor(Object object) {
		if (object == null) {
			return null;
		}
		Color color = colorsMap.get(object);
		if (color != null && !color.isDisposed()) {
			return color;
		}
		if (object instanceof String) {
			String colorStr = ((String) object).trim();
			IConverter c = XWT.findConvertor(String.class, Color.class);
			if (c != null) {
				color = (Color) c.convert(colorStr);
			}
		} else if (object instanceof RGB) {
			color = new Color(null, (RGB) object);
			colorsMap.put(object, color);
		} else if (object instanceof Color) {
			color = (Color) object;
		}
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	public boolean close() {
		Collection<Color> values = colorsMap.values();
		for (Color color : values) {
			if (color != null) {
				color.dispose();
			}
		}
		colorsMap.clear();
		return super.close();
	}

	private void preview(Object object) {
		Color color = getColor(object);
		if (color != null && !color.isDisposed()) {
			int red = color.getRed();
			int green = color.getGreen();
			int blue = color.getBlue();
			if (object instanceof String) {
				result = (String) object;
			} else {
				result = red + "," + green + "," + blue;
			}
			previewCanvas.setBackground(color);
			redText.setText("" + red);
			greenText.setText("" + green);
			blueText.setText("" + blue);
			resultText.setText(result);
		} else {
			previewCanvas.setBackground(null);
			redText.setText("");
			greenText.setText("");
			blueText.setText("");
			resultText.setText("");
		}
	}

	private void createPreviewGrp(Composite parent) {
		Group previewGrp = new Group(parent, SWT.NONE);
		previewGrp.setText("Selected Color");
		previewGrp.setLayout(new GridLayout(3, false));
		previewGrp.setLayoutData(new GridData(GridData.FILL_BOTH));

		previewCanvas = new Canvas(previewGrp, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.verticalSpan = 4;
		previewCanvas.setLayoutData(layoutData);

		redText = createTextGroup(previewGrp, "R:");
		greenText = createTextGroup(previewGrp, "G:");
		blueText = createTextGroup(previewGrp, "B:");
		resultText = createTextGroup(previewGrp, "Result:");
	}

	private Text createTextGroup(Composite parent, String name) {
		Label label = new Label(parent, SWT.CENTER);
		label.setText(name);
		final Text text = new Text(parent, SWT.READ_ONLY);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	public String getColor() {
		return result;
	}
}
