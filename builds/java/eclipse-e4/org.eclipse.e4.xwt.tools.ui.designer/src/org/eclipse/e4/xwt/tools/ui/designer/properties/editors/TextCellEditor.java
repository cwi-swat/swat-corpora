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
package org.eclipse.e4.xwt.tools.ui.designer.properties.editors;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.XWTDesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.dialogs.ExternalizeTextDialog;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.properties.PropertyContext;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.ExternalizeStringsWizard;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class TextCellEditor extends AbstractCellEditor {

	private boolean canExternalize;
	private PropertyContext fContext;
	private IProperty property;
	private XamlAttribute attribute;

	public TextCellEditor(PropertyContext fContext, IProperty property, Composite parent) {
		super(parent);
		this.fContext = fContext;
		this.property = property;
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		XWTDesigner designer = XWTDesignerPlugin.getDefault().getActiveDesigner();
		attribute = createAttribute(fContext.getNode());
		String oldValue = (String) getValue();
		String value = attribute.getValue();

		if (designer != null) {
			if (value == null) {
				canExternalize = false;
			} else {
				canExternalize = true;
			}
			ExternalizeTextDialog dialog = new ExternalizeTextDialog(cellEditorWindow.getShell(), oldValue, canExternalize, property.getName());
			int returnValue = dialog.open();
			if (Window.OK == returnValue) {
				return dialog.getTextValue();
			} else if (2 == returnValue) {
				boolean isOK = externalizeText(dialog.getTextValue());
				if (isOK) {
					return dialog.getTextValue();
				} else {
					return oldValue;
				}
			}
		}
		return null;
	}

	protected XamlAttribute createAttribute(XamlNode parent) {
		String attrName = property.getName();
		if (attrName == null) {
			throw new NullPointerException("Attribute name is null");
		}
		XamlAttribute attr = XWTModelUtil.getAdaptableAttribute(parent, attrName, IConstants.XWT_NAMESPACE);
		if (attr == null) {
			attr = XamlFactory.eINSTANCE.createAttribute(attrName, IConstants.XWT_NAMESPACE);
		}
		return attr;
	}

	private boolean externalizeText(String textValue) {
		final XWTDesigner designer = XWTDesignerPlugin.getDefault().getActiveDesigner();
		String oldValue = attribute.getValue();
		// Externalize the value of the attribute.
		TextValueModel textValueEntrys = new TextValueModel();
		textValueEntrys.add(new TextValueEntry(textValue, "" + 0));
		ExternalizeStringsWizard wizard = new ExternalizeStringsWizard(textValueEntrys, designer);
		wizard.init(PlatformUI.getWorkbench(), null);
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		int returnValue = dialog.open();
		if (Window.CANCEL == returnValue) {
			attribute.setValue(oldValue);
			return false;
		}
		return true;
	}
}
