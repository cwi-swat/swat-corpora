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

import java.beans.PropertyChangeEvent;

import org.eclipse.e4.xwt.tools.ui.designer.databinding.BindingContext;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.IObservable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Observable;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.ObservableUtil;
import org.eclipse.e4.xwt.tools.ui.designer.databinding.Property;
import org.eclipse.e4.xwt.tools.ui.designer.parts.ViewerEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ObserveTargetGroup extends ObserveGroup {

	public ObserveTargetGroup(BindingContext bindingContext, EditPart rootEditPart) {
		super(bindingContext, rootEditPart);
	}

	protected String computeMessage() {
		String message = "Target:";
		BindingContext bc = getBindingContext();
		IObservable target = bc.getTarget();
		Property targetProperty = bc.getTargetProperty();
		if (target != null) {
			message += target.getDisplayName();
			if (targetProperty != null) {
				message += "." + targetProperty;
			}
		} else {
			message += "<empty>";
		}
		return message;
	}

	protected void handleSelection(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		BindingContext bindingContext = getBindingContext();
		if (element == null) {
			bindingContext.setTarget(null);
			bindingContext.setTargetProperty(null);
		} else if (element instanceof IObservable) {
			bindingContext.setTarget((IObservable) element);
			bindingContext.setTargetProperty(null);
		} else if (element instanceof Property) {
			bindingContext.setTargetProperty((Property) element);
		}
	}

	protected void selectAndReveal(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();
		if ("target".equals(evt.getPropertyName())) {
			TreeViewer treeViewer = observableList.getTreeViewer();
			treeViewer.setExpandedElements(new Object[] { newValue });
			treeViewer.setSelection(createSelection(newValue), true);
		} else if ("targetProperty".equals(evt.getPropertyName())) {
			propertiesTree.setSelection(createSelection(newValue), true);
		}
	}

	protected IObservable[] computeObservables(EditPart rootEditPart) {
		if (rootEditPart == null) {
			return new IObservable[0];
		}
		Object root = null;
		if (rootEditPart instanceof WidgetEditPart) {
			root = ((WidgetEditPart) rootEditPart).getWidget();
		} else if (rootEditPart instanceof ViewerEditPart) {
			root = ((ViewerEditPart) rootEditPart).getViewer();
		}
		if (root == null) {
			return new IObservable[0];
		}
		Observable widgets = ObservableUtil.getObservable(root);
		if (widgets != null) {
			return new IObservable[] { widgets };
		}
		return new IObservable[0];
	}

}
