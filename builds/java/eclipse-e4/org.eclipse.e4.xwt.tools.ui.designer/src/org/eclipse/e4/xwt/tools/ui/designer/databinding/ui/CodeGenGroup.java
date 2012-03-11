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
package org.eclipse.e4.xwt.tools.ui.designer.databinding.ui;

import org.eclipse.e4.xwt.tools.ui.designer.databinding.CodeStyles;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class CodeGenGroup implements IGroup {

	private static final String STYLE_FLAT = "Flat";
	private static final String STYLE_TREE = "Tree";
	private static final String DATACONTEXT = "DataContext";
	private static final String STATICRESOURCE = "StaticResource";

	private static final String ATTRIBUTE_VALUE_FLAT = "<Label text=\"{Binding path=\"[PATH]\", mode=\"TwoWay\"}\">";
	private static final String ATTRIBUTE_VALUE_TREE = "<Lable.text>\n\r\t<Binding path=\"[PATH]\">\n\r\t\t<Binding.mode>TwoWay</Binding.mode>\n\r\t</Binding>\n\r</Lable.text>";
	private static final String CODE_DATACONTEXT = "<Shell DataContext=\"{StaticResource myData}\">\n\r\t<Shell.Resources>\n\r\t\t<y:Person x:key=\"myData\"/>\n\r\t</Shell.Resources>\n\r</Shell>\n<Label text=\"{Binding path=\"name\", mode=\"TwoWay\"}\">";
	private static final String CODE_STATICRESOURCE = "{Binding source=\"StaticResource myData\" path=\"name\"}";
	private Text bindingPreview;
	private Text pathPreview;

	private Button flatValueRadio;
	private Button treeValueRadio;

	private Button dataContextRadio;
	private Button staticResourceRadio;

	private CodeStyles codeStyles;

	public ExpandableComposite createGroup(final Composite parent, int style) {
		FormToolkit formToolkit = new FormToolkit(parent.getDisplay());
		// formToolkit.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final ExpandableComposite result = formToolkit.createExpandableComposite(parent, ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		result.setText("Code Gen Settings");
		Composite cilent = formToolkit.createComposite(result);
		cilent.setLayout(new GridLayout());
		result.setClient(cilent);
		result.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				parent.layout(new Control[] { result });
			}
		});
		{
			Group bindComp = new Group(cilent, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			layout.marginWidth = 0;
			bindComp.setLayout(layout);
			bindComp.setLayoutData(new GridData(GridData.FILL_BOTH));
			bindComp.setText("Code of Binding:");
			formToolkit.adapt(bindComp);

			flatValueRadio = formToolkit.createButton(bindComp, STYLE_FLAT, SWT.RADIO);
			flatValueRadio.setData(STYLE_FLAT);
			flatValueRadio.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));

			bindingPreview = formToolkit.createText(bindComp, ATTRIBUTE_VALUE_FLAT, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
			bindingPreview.setLayoutData(layoutData);

			treeValueRadio = formToolkit.createButton(bindComp, STYLE_TREE, SWT.RADIO);
			treeValueRadio.setData(STYLE_TREE);
			treeValueRadio.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));

			Listener listener = new Listener() {
				public void handleEvent(Event event) {
					Button button = (Button) event.widget;
					if (button.getSelection())
						previewBindingCodes(event.widget.getData().toString());
				}
			};
			flatValueRadio.addListener(SWT.Selection, listener);
			treeValueRadio.addListener(SWT.Selection, listener);

		}
		{
			Group pathComp = new Group(cilent, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			layout.marginWidth = 0;
			pathComp.setLayout(layout);
			pathComp.setLayoutData(new GridData(GridData.FILL_BOTH));
			pathComp.setText("Code of Path:");
			formToolkit.adapt(pathComp);

			dataContextRadio = formToolkit.createButton(pathComp, DATACONTEXT, SWT.RADIO);
			dataContextRadio.setData(DATACONTEXT);
			dataContextRadio.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));

			pathPreview = formToolkit.createText(pathComp, CODE_DATACONTEXT, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
			pathPreview.setLayoutData(layoutData);

			staticResourceRadio = formToolkit.createButton(pathComp, STATICRESOURCE, SWT.RADIO);
			staticResourceRadio.setData(STATICRESOURCE);
			staticResourceRadio.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));

			Listener listener = new Listener() {
				public void handleEvent(Event event) {
					previewPathCodes(event.widget.getData().toString());
				}
			};
			dataContextRadio.addListener(SWT.Selection, listener);
			staticResourceRadio.addListener(SWT.Selection, listener);

		}
		return result;
	}

	protected void previewPathCodes(String style) {
		if (DATACONTEXT.equals(style)) {
			pathPreview.setText(CODE_DATACONTEXT);
			getCodeStyles().useDataContext = true;
		} else if (STATICRESOURCE.equals(style)) {
			pathPreview.setText(CODE_STATICRESOURCE);
			getCodeStyles().useDataContext = false;
		}
	}

	protected void previewBindingCodes(String style) {
		if (STYLE_FLAT.equals(style)) {
			bindingPreview.setText(ATTRIBUTE_VALUE_FLAT);
			getCodeStyles().useFlatVlaue = true;
		} else if (STYLE_TREE.equals(style)) {
			bindingPreview.setText(ATTRIBUTE_VALUE_TREE);
			getCodeStyles().useFlatVlaue = false;
		}
	}

	public CodeStyles getCodeStyles() {
		if (codeStyles == null) {
			codeStyles = new CodeStyles();
		}
		return codeStyles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		if (input != null && input instanceof CodeStyles) {
			codeStyles = (CodeStyles) input;
		} else {
			codeStyles = new CodeStyles();
		}
		previewCodeStyles();
	}

	private void previewCodeStyles() {
		CodeStyles settings = getCodeStyles();
		if (dataContextRadio != null || flatValueRadio != null) {
			flatValueRadio.setSelection(settings.useFlatVlaue);
			treeValueRadio.setSelection(!settings.useFlatVlaue);
			previewBindingCodes(settings.useFlatVlaue ? STYLE_FLAT : STYLE_TREE);
			dataContextRadio.setSelection(settings.useDataContext);
			previewPathCodes(settings.useDataContext ? DATACONTEXT : STATICRESOURCE);
		}
	}
}
