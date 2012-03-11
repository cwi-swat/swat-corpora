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

import org.eclipse.e4.xwt.databinding.BindingMode;
import org.eclipse.e4.xwt.internal.core.UpdateSourceTrigger;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public interface IBindingInfo {

	IObservable getTarget();

	Property getTargetProperty();

	IObservable getModel();

	Property getModelProperty();

	String getElementName();

	/**
	 * The URL of a IConverter,
	 */
	String getConverter();

	/**
	 * One of {@link BindingMode}, <code>TwoWay</code>, <code>OneWay</code>, <code>OneTime</code>;
	 */
	BindingMode getBindingMode();

	/**
	 * One of {@link UpdateSourceTrigger}, <code>Default</code>, <code>PropertyChanged</code>, <code>LostFocus</code>;
	 */
	UpdateSourceTrigger getTriggerMode();

}
