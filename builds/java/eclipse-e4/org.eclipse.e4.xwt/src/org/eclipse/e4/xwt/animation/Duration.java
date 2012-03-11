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

public class Duration {
	enum DurationType {
		Automatic, Forever, TimeSpan;
	}

	/**
	 * Property getter.
	 * 
	 * @property(Automatic)
	 */
	public static Duration getAutomatic() {
		Duration duration1 = new Duration();
		duration1.durationType = DurationType.Automatic;
		return duration1;
	}

	/**
	 * Property getter.
	 * 
	 * @property(Forever)
	 */
	public static Duration getForever() {
		Duration duration1 = new Duration();
		duration1.durationType = DurationType.Forever;
		return duration1;
	}

	protected DurationType durationType;
	protected TimeSpan timeSpan;

	protected Duration() {
	}

	public Duration(TimeSpan timeSpan) {
		this.durationType = DurationType.TimeSpan;
		this.timeSpan = timeSpan;
	}

	/**
	 * Property getter.
	 * 
	 * @property(HasTimeSpan)
	 */
	public boolean hasTimeSpan() {
		return (this.durationType == DurationType.TimeSpan);
	}

	/**
	 * Property getter.
	 * 
	 * @property(TimeSpan)
	 */
	public TimeSpan getTimeSpan() {
		if (!this.hasTimeSpan()) {
			throw new UnsupportedOperationException();
		}
		return this.timeSpan;
	}
}
