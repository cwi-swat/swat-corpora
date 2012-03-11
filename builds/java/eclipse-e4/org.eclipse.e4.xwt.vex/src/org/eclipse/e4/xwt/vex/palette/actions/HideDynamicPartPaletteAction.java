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
package org.eclipse.e4.xwt.vex.palette.actions;

import org.eclipse.e4.xwt.vex.Activator;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.e4.xwt.vex.palette.CustomPalettePage;
import org.eclipse.e4.xwt.vex.palette.part.CustomizePaletteViewer;
import org.eclipse.e4.xwt.vex.palette.part.DynamicPaletteViewer;
import org.eclipse.e4.xwt.vex.swt.CustomSashForm;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

public class HideDynamicPartPaletteAction extends Action {

	private PaletteViewer paletteViewer;
	private DynamicPaletteViewer dynamicPaletteViewer;
	private CustomizePaletteViewer customizePaletteViewer;
	private CustomSashForm sashFormMain;
	private CustomSashForm dynamicAndCustomizeSashForm;
	private Composite dynamicComposite;

	public HideDynamicPartPaletteAction() {
		super("Hide/Show Contextual Tools", AS_CHECK_BOX);
		// TODO Auto-generated constructor stub
		setToolTipText("Hide Contextual Tools");
		setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/full/obj16/hide2.gif"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Object editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof VEXEditor) {
			paletteViewer = ((CustomPalettePage) ((VEXEditor) editor).getVEXEditorPalettePage()).getPaletteViewer();
		}

		Object mainForm = paletteViewer.getProperty("SashFormMain");
		if (mainForm instanceof CustomSashForm) {
			sashFormMain = (CustomSashForm) mainForm;
		}

		Object objectDynamicPalette = paletteViewer.getProperty("Dynamic_PaletteViewer");
		if (objectDynamicPalette instanceof DynamicPaletteViewer) {
			dynamicPaletteViewer = (DynamicPaletteViewer) objectDynamicPalette;
		}

		Object objectCustomizePalette = paletteViewer.getProperty("Customize_PaletteViewer");
		if (objectCustomizePalette instanceof CustomizePaletteViewer) {
			customizePaletteViewer = (CustomizePaletteViewer) objectCustomizePalette;
		}

		Object dynamicSashForm = dynamicPaletteViewer.getProperty("DynamicAndCustomizeSashForm");
		if (dynamicSashForm instanceof CustomSashForm) {
			dynamicAndCustomizeSashForm = (CustomSashForm) dynamicSashForm;
		}

		Object composite = dynamicPaletteViewer.getProperty("DynamicComposite");
		if (composite instanceof Composite) {
			dynamicComposite = (Composite) composite;
		}

		if (isChecked()) {
			dynamicPaletteViewer.setVisible(false);
			dynamicComposite.setVisible(false);
			dynamicAndCustomizeSashForm.setWeights((new int[] { 0, 1 }));
			if ((customizePaletteViewer.isVisible() == false) && (dynamicPaletteViewer.isVisible() == false)) {
				dynamicAndCustomizeSashForm.setVisible(false);
				sashFormMain.setWeights(new int[] { 1, 0 });
			}
			setToolTipText("Show Contextual Tools");
		} else {
			dynamicPaletteViewer.setVisible(true);
			dynamicComposite.setVisible(true);
			dynamicAndCustomizeSashForm.setVisible(true);
			dynamicAndCustomizeSashForm.setWeights((new int[] { 1, 1 }));
			sashFormMain.setWeights(new int[] { 2, 1 });
			setToolTipText("Hide Contextual Tools");
		}
	}
}
