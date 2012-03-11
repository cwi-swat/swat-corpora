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
import org.eclipse.e4.xwt.vex.swt.CustomSashForm;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

public class HideToolPartPaletteAction extends Action {

	private PaletteViewer paletteViewer;
	private CustomSashForm toolSashForm;
	private CustomSashForm sashFormMain;

	public HideToolPartPaletteAction() {
		super("Hide/Show Standard Tools", AS_CHECK_BOX);
		// TODO Auto-generated constructor stub
		setToolTipText("Hide Standard Tools");
		setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/full/obj16/hide3.gif"));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Object editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof VEXEditor) {
			paletteViewer = ((CustomPalettePage) ((VEXEditor) editor).getVEXEditorPalettePage()).getPaletteViewer();
		}

		Object toolForm = paletteViewer.getProperty("ToolSashForm");
		if (toolForm instanceof CustomSashForm) {
			toolSashForm = (CustomSashForm) toolForm;
		}

		Object mainForm = paletteViewer.getProperty("SashFormMain");
		if (mainForm instanceof CustomSashForm) {
			sashFormMain = (CustomSashForm) mainForm;
		}

		if (isChecked()) {
			toolSashForm.setVisible(false);
			sashFormMain.setWeights(new int[] { 0, 1 });
			setToolTipText("Show Standard Tools");
		} else {
			toolSashForm.setVisible(true);
			sashFormMain.setWeights(new int[] { 2, 1 });
			setToolTipText("Hide Standard Tools");
		}
	}

}
