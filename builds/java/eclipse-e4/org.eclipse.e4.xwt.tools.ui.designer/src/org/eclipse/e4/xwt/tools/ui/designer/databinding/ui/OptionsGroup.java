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

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.BindingMode;
import org.eclipse.e4.xwt.internal.core.UpdateSourceTrigger;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IBindingInfo;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class OptionsGroup implements IGroup {

	private ComboViewer modeComboViewer;
	private ComboViewer triggersComboViewer;
	private Text converterText;

	private BindingMode bindingMode;
	private UpdateSourceTrigger updateSourceTrigger;

	public ExpandableComposite createGroup(final Composite parent, int style) {
		FormToolkit formToolkit = new FormToolkit(parent.getDisplay());
		// formToolkit.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final ExpandableComposite result = formToolkit.createExpandableComposite(parent, ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		result.setText("Options");
		Composite cilent = formToolkit.createComposite(result);
		cilent.setLayout(new GridLayout(3, false));
		result.setClient(cilent);
		result.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				parent.layout(new Control[] { result });
			}
		});

		formToolkit.createLabel(cilent, "BindingMode:");
		Combo modeCombo = new Combo(cilent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		modeCombo.setLayoutData(layoutData);
		formToolkit.adapt(modeCombo, true, true);
		modeComboViewer = createComboViewr(modeCombo);
		modeComboViewer.setInput(BindingMode.values());
		modeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				bindingMode = (BindingMode) selection.getFirstElement();
			}
		});

		formToolkit.createLabel(cilent, "UpdateSourceTrigger:");
		Combo triggerCombo = new Combo(cilent, SWT.DROP_DOWN | SWT.READ_ONLY);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		triggerCombo.setLayoutData(layoutData);
		formToolkit.adapt(triggerCombo, true, true);
		triggersComboViewer = createComboViewr(triggerCombo);
		triggersComboViewer.setInput(UpdateSourceTrigger.values());
		triggersComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				updateSourceTrigger = (UpdateSourceTrigger) selection.getFirstElement();
			}
		});

		formToolkit.createLabel(cilent, "Converter:");
		converterText = formToolkit.createText(cilent, "", SWT.BORDER);
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		converterText.setLayoutData(layoutData);
		formToolkit.adapt(converterText, true, true);

		Button button = formToolkit.createButton(cilent, "", SWT.NONE);
		button.setImage(ImageShop.get(ImageShop.IMAGE_OBSERVE_CUSTOM));
		button.setToolTipText("Choose a converter.");
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				chooseConverter();
			}
		});
		return result;
	}

	private ComboViewer createComboViewr(Combo combo) {
		ComboViewer comboViewer = new ComboViewer(combo);
		comboViewer.setContentProvider(new IStructuredContentProvider() {
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Object[]) {
					return (Object[]) inputElement;
				}
				return new Object[0];
			}
		});
		comboViewer.setLabelProvider(new LabelProvider());
		return comboViewer;
	}

	protected void chooseConverter() {

	}

	public BindingMode getBindingMode() {
		if (bindingMode == null) {
			bindingMode = BindingMode.TwoWay;
		}
		return bindingMode;
	}

	public UpdateSourceTrigger getUpdateSourceTrigger() {
		if (updateSourceTrigger == null) {
			updateSourceTrigger = UpdateSourceTrigger.Default;
		}
		return updateSourceTrigger;
	}

	public Class<?> getConverter() {
		String className = converterText.getText();
		if (className != null && !className.equals("")) {
			return (Class<?>) XWT.getLoadingContext().loadClass(className);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.databinding.ui.IGroup#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		String converter = null;
		if (input instanceof IBindingInfo) {
			bindingMode = ((IBindingInfo) input).getBindingMode();
			updateSourceTrigger = ((IBindingInfo) input).getTriggerMode();
			converter = ((IBindingInfo) input).getConverter();
		}
		if (modeComboViewer != null) {
			modeComboViewer.setSelection(new StructuredSelection(getBindingMode()));
		}
		if (updateSourceTrigger != null) {
			triggersComboViewer.setSelection(new StructuredSelection(getUpdateSourceTrigger()));
		}
		if (converter == null) {
			converter = "";
		}
		converterText.setText(converter);
	}
}
