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
package org.eclipse.e4.xwt.tools.ui.designer.databinding;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class BindingContext {

	private IObservable target;
	private Property targetProperty;
	private IObservable model;
	private Property modelProperty;

	private PropertyChangeSupport support = null;

	public BindingContext() {
		this(null, null, null, null);
	}

	public BindingContext(IObservable target, Property targetProperty, IObservable model, Property modelProperty) {
		this.target = target;
		this.targetProperty = targetProperty;
		this.model = model;
		this.modelProperty = modelProperty;
		support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public IObservable getTarget() {
		return target;
	}

	public void setTarget(IObservable target) {
		IObservable oldValue = this.target;
		this.target = target;
		support.firePropertyChange("target", oldValue, this.target);
	}

	public Property getTargetProperty() {
		return targetProperty;
	}

	public void setTargetProperty(Property targetProperty) {
		Property oldValue = this.targetProperty;
		this.targetProperty = targetProperty;
		support.firePropertyChange("targetProperty", oldValue, this.targetProperty);
	}

	public IObservable getModel() {
		return model;
	}

	public void setModel(IObservable model) {
		IObservable oldValue = this.model;
		this.model = model;
		support.firePropertyChange("model", oldValue, this.model);
	}

	public Property getModelProperty() {
		return modelProperty;
	}

	public void setModelProperty(Property modelProperty) {
		Property oldValue = this.modelProperty;
		this.modelProperty = modelProperty;
		support.firePropertyChange("modelProperty", oldValue, this.modelProperty);
	}

	public boolean isValid() {
		boolean valid = model != null && modelProperty != null && target != null && targetProperty != null;
		if (valid && modelProperty.equals(targetProperty)) {
			return !model.equals(target);
		}
		return valid;
	}
}
