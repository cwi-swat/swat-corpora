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
package org.eclipse.e4.xwt.vex.palette.customize;

import org.eclipse.e4.xwt.vex.palette.customize.actions.AddCustomizePaletteAction;
import org.eclipse.e4.xwt.vex.palette.customize.actions.DeleteCustomizeComponentAction;
import org.eclipse.e4.xwt.vex.palette.customize.actions.ExportCustomizeComponentsAction;
import org.eclipse.e4.xwt.vex.palette.customize.actions.ImportCustomizeComponentsAction;
import org.eclipse.e4.xwt.vex.palette.customize.actions.ModifyCustomizeComponentAction;
import org.eclipse.e4.xwt.vex.palette.part.CustomizePaletteViewer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.IMenuManager;

public class CustomerPaletteContextMenuProvider extends PaletteContextMenuProvider {

	public CustomerPaletteContextMenuProvider(PaletteViewer palette) {
		super(palette);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		super.buildContextMenu(menu);
		PaletteViewer paletteViewer = getPaletteViewer();
		if (paletteViewer instanceof CustomizePaletteViewer) {
			EditPart selectedPart = (EditPart) paletteViewer.getSelectedEditParts().get(0);
			// add the add customize menu to the popup menu
			menu.appendToGroup(GEFActionConstants.GROUP_REST, new AddCustomizePaletteAction(paletteViewer, null, InvokeType.MenuAdd));
			menu.appendToGroup(GEFActionConstants.GROUP_SAVE, new ExportCustomizeComponentsAction());
			menu.appendToGroup(GEFActionConstants.GROUP_SAVE, new ImportCustomizeComponentsAction());
			if (selectedPart instanceof ToolEntryEditPart) {
				String selectedComponentName = ((CombinedTemplateCreationEntry) selectedPart.getModel()).getLabel();
				String selectComponentName = selectedComponentName;
				// add modify menu
				menu.appendToGroup(GEFActionConstants.GROUP_REST, new ModifyCustomizeComponentAction(paletteViewer, selectComponentName));

				// add delete menu
				menu.appendToGroup(GEFActionConstants.GROUP_REST, new DeleteCustomizeComponentAction(paletteViewer, selectComponentName));
			}
		}
	}
}
