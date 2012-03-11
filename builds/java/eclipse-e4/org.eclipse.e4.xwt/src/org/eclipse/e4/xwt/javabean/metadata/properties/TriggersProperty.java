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
package org.eclipse.e4.xwt.javabean.metadata.properties;

import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.core.IUserDataConstants;
import org.eclipse.e4.xwt.core.TriggerBase;
import org.eclipse.e4.xwt.metadata.ILoadingType;

public class TriggersProperty extends DataProperty {

	public TriggersProperty(ILoadingType loadingType) {
		super(IConstants.XAML_TRIGGERS,
				IUserDataConstants.XWT_TRIGGERS_KEY, TriggerBase[].class, TriggerBase.EMPTY_ARRAY, loadingType);
	}

	@Override
	public void setValue(Object target, Object value) {
		super.setValue(target, value);
		
		TriggerBase[] triggers = (TriggerBase[]) value;
		for (TriggerBase triggerBase : triggers) {
			if (triggerBase != null) {
				triggerBase.prepare(target);
			}
		}
		for (TriggerBase triggerBase : triggers) {
			if (triggerBase != null) {
				triggerBase.on(target);
			}
		}
	}
}
