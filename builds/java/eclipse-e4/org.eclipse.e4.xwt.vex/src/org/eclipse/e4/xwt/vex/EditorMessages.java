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
package org.eclipse.e4.xwt.vex;

import org.eclipse.osgi.util.NLS;

public class EditorMessages extends NLS {

	public static String CustomizeComponentFactory_Customize;
	public static String CustomizeComponentFactory_CustomizeCategory;
	public static String CustomizeComponentFactory_VIEWER_EDITOR;
	public static String CustomizePaletteDialog_BrowseIcon;
	public static String CustomizePaletteDialog_BrowseLargeIcon;
	public static String CustomizePaletteDialog_CodeTemplate;
	public static String CustomizePaletteDialog_Content;
	public static String CustomizePaletteDialog_ContentError;
	public static String CustomizePaletteDialog_ErrorMessage;
	public static String CustomizePaletteDialog_ErrorTitle;
	public static String CustomizePaletteDialog_Header;
	public static String CustomizePaletteDialog_Icon;
	public static String CustomizePaletteDialog_LargeIcon;
	public static String CustomizePaletteDialog_Message;
	public static String CustomizePaletteDialog_Name;
	public static String CustomizePaletteDialog_NameError;
	public static String CustomizePaletteDialog_Scope;
	public static String CustomizePaletteDialog_ScopeError;
	public static String CustomizePaletteDialog_SelectionDialog_LargeText;
	public static String CustomizePaletteDialog_SelectionDialog_LargeTitle;
	public static String CustomizePaletteDialog_SelectionDialog_Text;
	public static String CustomizePaletteDialog_SelectionDialog_Title;
	public static String CustomizePaletteDialog_Title;
	public static String CustomizePaletteDialog_ToolTip;

	public static String CustomizePaletteDialog_Name_ToolTip;
	public static String CustomizePaletteDialog_Scope_ToolTip;
	public static String CustomizePaletteDialog_ToolTip_ToolTip;
	public static String CustomizePaletteDialog_Content_ToolTip;
	public static String CustomizePaletteDialog_Icon_ToolTip;
	public static String CustomizePaletteDialog_LargeIcon_ToolTip;

	public static String EventHandleDialogTitle;
	public static String EventHandleDialog_Operation;
	public static String EventHandleDialog_New_Handler;
	public static String EventHandleDialog_New_Handler_ToolTip;
	public static String EventHandleDialog_Rename_Handler;
	public static String EventHandleDialog_Rename_Handler_ToolTip;
	public static String EventHandleDialog_Select_Existing_Handler;
	public static String EventHandleDialog_Select_Existing_Handler_ToolTip;
	public static String EventHandleDialog_Select_Handler;
	public static String EventHandleDialog_Input_Handler_Name;

	public static String EventHandleDialog_Error_Handler_Name_Null;
	public static String EventHandleDialog_Error_Handler_Name_Exists;
	public static String EventHandleDialog_Question_Handler_Name_Refactoring;
	public static String PaletteResourceManager_icons;
	public static String PaletteResourceManager_toolkit;
	public static String PaletteRootFactory_Customize;
	public static String PaletteRootFactory_Dynamic;
	public static String PaletteRootFactory_Shapes;
	public static String VEXEditor_Dynamic;
	public static String VEXEditor_DynamicCategory;

	public static String VEXEditor_NODE;
	public static String VEXEditor_SUBFIX_FAIL;
	public static String VEXEditor_SUBFIX_OK;

	public static String ImportCustomizeComponentsAction_ActionText;
	public static String ImportCustomizeComponentsAction_DialogText;
	public static String ImportCustomizeComponentsAction_ERROR;
	public static String ImportCustomizeComponentsAction_ERROR_INFORMATION;
	public static String ExportCustomizeComponentsAction_ActionText;
	public static String ExportCustomizeComponentsAction_DialogText;
	public static String ExportCustomizeComponentsAction_INFORMATION;
	public static String ExportCustomizeComponentsAction_SUCCESS;

	private static final String BUNDLE_NAME = EditorMessages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$
	static {
		NLS.initializeMessages(BUNDLE_NAME, EditorMessages.class);
	}
}
