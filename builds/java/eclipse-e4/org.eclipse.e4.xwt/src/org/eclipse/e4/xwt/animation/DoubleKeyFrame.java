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
package org.eclipse.e4.xwt.animation;

import org.pushingpixels.trident.Timeline;

public abstract class DoubleKeyFrame {
	public static final DoubleKeyFrame[] EMPTY_ARRAY = {}; 
	private KeyTime keyTime;
	private Double value;

	public KeyTime getKeyTime() {
		return keyTime;
	}

	public void setKeyTime(KeyTime keyTime) {
		this.keyTime = keyTime;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	protected void start(Timeline timeline, Object target) {
	}
}
