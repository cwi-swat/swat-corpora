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

import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.ExternalizeStringsWizard;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class TextSection extends AbstractAttributeSection {
	protected TabbedPropertySheetPage tabbedPropertySheetPage;
	private Text textWidget;
	// private Button externalizeButton;
	private Runnable delayRunnable;

	protected void _createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super._createControls(parent, aTabbedPropertySheetPage);
		this.tabbedPropertySheetPage = aTabbedPropertySheetPage;
	}

	protected Control createSection(Composite parent) {
		Composite control = getWidgetFactory().createComposite(parent);
		control.setLayout(new GridLayout(2, false));

		textWidget = getWidgetFactory().createText(control, "", SWT.BORDER);
		textWidget.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textWidget.addListener(SWT.Modify, this);

		// externalizeButton = getWidgetFactory().createButton(control, "",
		// SWT.PUSH);
		// externalizeButton.setImage(ImageShop
		// .get(ImageShop.IMAGE_OBSERVE_CUSTOM));
		// externalizeButton.setToolTipText("Externalize String");
		// externalizeButton.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event event) {
		// externalizeText();
		// }
		// });
		return control;
	}

	private void externalizeText() {
		if (getValue() != null) {
			XWTDesigner designer = (XWTDesigner) getPart();
			TextValueModel textValueEntrys = new TextValueModel();
			textValueEntrys
					.add(new TextValueEntry(textWidget.getText(), "" + 0));
			ExternalizeStringsWizard wizard = new ExternalizeStringsWizard(
					textValueEntrys, designer);
			wizard.init(PlatformUI.getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), wizard);
			dialog.open();
		} else {
			String dialogMessage = "No Strings to externalize found.";
			String[] dialogButtonLabels = { "Ok" };
			MessageDialog messageDialog = new MessageDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Externalize Strings", null, dialogMessage,
					MessageDialog.INFORMATION, dialogButtonLabels, 0);
			messageDialog.open();
		}

	}

	public void doRefresh() {
		if (textWidget == null || textWidget.isDisposed()
				|| textWidget.isFocusControl() || getAttribute() == null) {
			return;
		}
		textWidget.removeListener(SWT.Modify, this);
		setTextValue(textWidget);
		textWidget.addListener(SWT.Modify, this);
		refreshTitleBar();
	}

	protected String getNewValue(Event event) {
		if (textWidget == null || textWidget.isDisposed()) {
			return null;
		}
		return textWidget.getText();
	}

	protected String getAttributeName() {
		return "text";
	}

	private void refreshTitleBar() {
		DisplayUtil.asyncExec(new Runnable() {
			public void run() {
				if (tabbedPropertySheetPage != null) {
					tabbedPropertySheetPage.labelProviderChanged(null);
				}
			}
		});
	}

	// public void handleEvent(final Event event) {
	// if (System.currentTimeMillis() - lastEvent < 1000 && !delayed) {
	// Runnable delayRunnable = new Runnable() {
	// public void run() {
	// TextSection.super.handleEvent(event);
	// refreshTitleBar();
	// delayed = false;
	// lastEvent = -1;
	// }
	// };
	// DisplayUtil.timerExec(display, 1000, delayRunnable);
	// delayed = true;
	// }
	// lastEvent = System.currentTimeMillis();
	// }
	public void handleEvent(final Event event) {
		if (!delayed) {
			if (delayRunnable == null) {
				delayRunnable = new Runnable() {
					public void run() {
						long current = System.currentTimeMillis();
						if (current - lastEvent < 300) {
							lastEvent = current;
							DisplayUtil.timerExec(display, 300, delayRunnable);
							return;
						}
						TextSection.super.handleEvent(event);
						refreshTitleBar();
						delayed = false;
						lastEvent = -1;
					}
				};
			}
			DisplayUtil.timerExec(display, 300, delayRunnable);
			delayed = true;
		}
		lastEvent = System.currentTimeMillis();
	}

	private long lastEvent = -1;
	private boolean delayed = false;
}
