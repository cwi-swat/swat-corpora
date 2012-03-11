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
package org.eclipse.e4.xwt.tools.ui.designer.editor.model;

import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class TextNotification {
	private INodeNotifier notifier;
	private int eventType;
	private Object changedFeature;
	private Object oldValue;
	private Object newValue;
	private int position;

	public TextNotification(INodeNotifier notifier, int eventType,
			Object changedFeature, Object oldValue, Object newValue, int pos) {
		this.setNotifier(notifier);
		this.setEventType(eventType);
		this.setChangedFeature(changedFeature);
		this.setOldValue(newValue);
		this.setNewValue(newValue);
		this.setPosition(pos);
	}

	/**
	 * @param notifier
	 *            the notifier to set
	 */
	public void setNotifier(INodeNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * @return the notifier
	 */
	public INodeNotifier getNotifier() {
		return notifier;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @param changedFeature
	 *            the changedFeature to set
	 */
	public void setChangedFeature(Object changedFeature) {
		this.changedFeature = changedFeature;
	}

	/**
	 * @return the changedFeature
	 */
	public Object getChangedFeature() {
		return changedFeature;
	}

	/**
	 * @param oldValue
	 *            the oldValue to set
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @param newValue
	 *            the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
}
