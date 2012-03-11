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

/**
 * 
 * @author yyang
 */
public class RepeatBehavior {
	// Fields
	private double count = 1;
	private Duration repeatDuration;
	private RepeatBehaviorType type;
	
	private static RepeatBehavior forever;
	static {
		forever = new RepeatBehavior();
		forever.type = RepeatBehaviorType.Forever;
	}
	
	// Nested Types
	enum RepeatBehaviorType {
		IterationCount, RepeatDuration, Forever;
	}

	public static final RepeatBehavior once = new RepeatBehavior(1);

	interface IFormatProvider {
	}

	RepeatBehavior() {
	}

	// Methods
	public RepeatBehavior(double count) {
		if ((Double.isInfinite(count) || Double.isNaN(count)) || (count < 0)) {
			throw new IllegalArgumentException("count: " + count);
		}
		this.repeatDuration = new Duration(new TimeSpan(0L));
		this.count = count;
		this.type = RepeatBehaviorType.IterationCount;
	}

	public RepeatBehavior(Duration duration) {
		this.count = 0;
		this.repeatDuration = duration;
		this.type = RepeatBehaviorType.RepeatDuration;
	}

	public boolean equals(Object value) {
		if ((value instanceof RepeatBehavior)) {
			return this.equals((RepeatBehavior) value);
		}
		return false;
	}

	public boolean equals(RepeatBehavior repeatBehavior) {
		if (this.type == repeatBehavior.type) {
			switch (this.type) {
			case IterationCount: {
				return (this.count == repeatBehavior.count);
			}
			case RepeatDuration: {
				return (this.repeatDuration == repeatBehavior.repeatDuration);
			}
			case Forever: {
				return true;
			}
			}
		}
		return false;
	}

	public static boolean equals(RepeatBehavior repeatBehavior1,
			RepeatBehavior repeatBehavior2) {
		return repeatBehavior1.equals(repeatBehavior2);
	}

	public int hashCode() {
		switch (this.type) {
		case IterationCount: {
			return (int) this.count;
		}
		case RepeatDuration: {
			return this.repeatDuration.hashCode();
		}
		case Forever: {
			return 2147483605;
		}
		}
		return super.hashCode();
	}

	String internalToString(String format, IFormatProvider formatProvider) {
		switch (this.type) {
		case IterationCount: {
			StringBuilder builder1 = new StringBuilder();
			// builder1.append(formatProvider, "{0:", format, "}x"), new
			// Object[] {
			// this.iterationCount
			// });
			return builder1.toString();
		}
		case RepeatDuration: {
			return this.repeatDuration.toString();
		}
		case Forever: {
			return "Forever";
		}
		}
		return null;
	}

	public static boolean opEquality(RepeatBehavior repeatBehavior1,
			RepeatBehavior repeatBehavior2) {
		return repeatBehavior1.equals(repeatBehavior2);
	}

	public static boolean opInequality(RepeatBehavior repeatBehavior1,
			RepeatBehavior repeatBehavior2) {
		return !repeatBehavior1.equals(repeatBehavior2);
	}

	public String toString(String format, IFormatProvider formatProvider) {
		return this.internalToString(format, formatProvider);
	}

	public String toString() {
		return this.internalToString(null, null);
	}

	public String toString(IFormatProvider formatProvider) {
		return this.internalToString(null, formatProvider);
	}

	// Properties
	/**
	 * Property getter.
	 * 
	 * @property(Count)
	 */
	public double getCount() {
		if (this.type != RepeatBehaviorType.IterationCount) {
			throw new UnsupportedOperationException();
		}
		return this.count;
	}

	/**
	 * Property getter.
	 * 
	 * @property(Duration)
	 */
	public Duration getDuration() {
		if (this.type != RepeatBehaviorType.RepeatDuration) {
			throw new UnsupportedOperationException();
		}
		return this.repeatDuration;
	}

	/**
	 * Property getter.
	 * 
	 * @property(Forever)
	 */
	public static RepeatBehavior getForever() {
		return forever;
	}

	/**
	 * Property getter.
	 * 
	 * @property(HasCount)
	 */
	public boolean getHasCount() {
		return (this.type == RepeatBehaviorType.IterationCount);
	}

	/**
	 * Property getter.
	 * 
	 * @property(HasDuration)
	 */
	public boolean getHasDuration() {
		return (this.type == RepeatBehaviorType.RepeatDuration);
	}

	public RepeatBehaviorType getRepeatBehaviorType() {
		return type;
	}	
	
	public static RepeatBehavior parse(String string) {
		if (string.toLowerCase().equals("forever")) {
			return getForever();
		}
		if (string.toLowerCase().endsWith("x")) {
			// count
			string = string.substring(0, string.length() - 1);
			double count = Double.parseDouble(string);
			return new RepeatBehavior(count);
		}
		
		return new RepeatBehavior(new Duration(TimeSpan.parse(string)));
	}
}
