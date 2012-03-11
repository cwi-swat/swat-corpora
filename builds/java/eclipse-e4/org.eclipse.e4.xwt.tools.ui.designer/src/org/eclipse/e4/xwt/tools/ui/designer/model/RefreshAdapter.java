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
package org.eclipse.e4.xwt.tools.ui.designer.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class RefreshAdapter extends AdapterImpl {

	private XamlNode source;
	private boolean isRefreshRequired = true;
	private List<String> notifyAttributeNames = new ArrayList<String>();
	private List<XamlAttribute> notifiedAttributes = new ArrayList<XamlAttribute>();

	public RefreshAdapter(XamlNode source, String affectedAttr) {
		this(source);
		if (affectedAttr != null) {
			notifyAttributeNames.add(affectedAttr);
			listenAttrs();
		}
	}

	public RefreshAdapter(XamlNode source) {
		Assert.isNotNull(source);
		this.source = source;
		source.eAdapters().add(this);
	}

	public void setRefreshRequired(boolean isRefreshRequired) {
		this.isRefreshRequired = isRefreshRequired;
	}

	private void listenAttrs() {
		for (String attrName : notifyAttributeNames) {
			listenAttr(source.getAttribute(attrName));
		}
	}

	private void listenAttr(XamlAttribute attribute) {
		if (attribute == null || attribute.eAdapters().contains(this)) {
			return;
		}
		attribute.eAdapters().add(this);
		notifiedAttributes.add(attribute);
	}

	public void addListenedAttr(String attrName) {
		if (!notifyAttributeNames.contains(attrName)) {
			notifyAttributeNames.add(attrName);
			listenAttrs();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse
	 * .emf.common.notify.Notification)
	 */
	public final void notifyChanged(Notification msg) {
		if (msg.isTouch()) {
			return;
		}
		if (notifyAttributeNames.isEmpty()) {
			return;
		}
		Object notifier = msg.getNotifier();
		if (source == notifier) {
			Object oldValue = msg.getOldValue();
			Object newValue = msg.getNewValue();
			if (notifiedAttributes.contains(oldValue)) {
				if (isRefreshRequired) {
					performRefresh(msg);
				}
			} else if (newValue instanceof XamlAttribute) {
				String name = ((XamlAttribute) newValue).getName();
				if (notifyAttributeNames.contains(name)) {
					listenAttr((XamlAttribute) newValue);
					if (isRefreshRequired) {
						performRefresh(msg);
					}
				}
			}
		} else if (notifiedAttributes.contains(notifier)) {
			if (isRefreshRequired) {
				performRefresh(msg);
			}
		}
	}

	public void dispose() {
		source.eAdapters().remove(this);
		for (XamlAttribute attr : notifiedAttributes) {
			attr.eAdapters().remove(this);
		}
		notifiedAttributes.clear();
		notifyAttributeNames.clear();
	}

	protected abstract void performRefresh(Notification msg);
}
