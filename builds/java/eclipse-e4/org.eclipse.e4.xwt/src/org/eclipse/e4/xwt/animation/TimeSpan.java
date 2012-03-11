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

import java.io.Serializable;

import org.eclipse.e4.xwt.XWTException;

public class TimeSpan implements Serializable, Comparable {

	public static final long TicksPerMillisecond = 10000; // scale

	private static final double MillisecondsPerTick = 0.0001;

	public static final long TicksPerSecond = 0x989680;

	private static final double SecondsPerTick = 1E-07;

	public static final long TicksPerMinute = 0x23C34600;

	private static final double MinutesPerTick = 1.6666666666666667E-09;

	public static final long TicksPerHour = Long.valueOf("861c46800", 16);

	private static final double HoursPerTick = 2.7777777777777777E-11;

	// Represents the number of ticks in 1 day

	public static final long TicksPerDay = Long.valueOf("c92a69c000", 16);

	private static final double DaysPerTick = 1.1574074074074074E-12;

	private static final int MillisPerSecond = 1000;

	private static final int MillisPerMinute = 60000;

	private static final int MillisPerHour = 3600000;

	private static final int MillisPerDay = 86400000;

	private static final long MaxSeconds = Long.valueOf("d6bf94d5e5", 16);

	private static final long MinSeconds = Long.valueOf("-922337203685", 10);

	private static final long MaxMilliSeconds = Long.valueOf("346dc5d638865",
			16);

	private static final long MinMilliSeconds = Long.valueOf(
			"-922337203685477", 10);

	public static final long SencondsPerHour = 3600;

	public static final long HoursPerDay = 24;

	public static final TimeSpan Zero;

	public static final TimeSpan MaxValue;

	/*
	 * Represents the maximum TimeSpanvalue
	 */
	public static final TimeSpan MinValue;

	public static final String TimeSpan = null;

	public long ticks;

	public TimeSpan(long ticks) {
		this.ticks = ticks;
	}

	/*
	 * Initializes a new TimeSpan to a specified number of hours, minutes, and
	 * seconds.
	 */
	public TimeSpan(int hours, int minutes, int seconds) throws Exception {
		this.ticks = timeToTicks(hours, minutes, seconds);
	}

	/*
	 * Initializes a new TimeSpan to a specified number of days, hours, minutes,
	 * and seconds.
	 */
	public TimeSpan(int days, int hours, int minutes, int seconds)
			throws Exception {
		this(days, hours, minutes, seconds, 0);
	}

	/*
	 * Initializes a new TimeSpan to a specified number of days, hours, minutes,
	 * seconds, and milliseconds. calcualte total ticks
	 */

	public TimeSpan(int days, int hours, int minutes, int seconds,
			int milliseconds) throws Exception {
		long num = (MillisPerSecond * (((((SencondsPerHour * (long) days) * HoursPerDay) + ((long) hours * SencondsPerHour)) + ((long) minutes * 60)) + seconds))
				+ milliseconds;
		if ((num > Long.valueOf(MaxMilliSeconds))
				|| (num < Long.valueOf(MinMilliSeconds))) {
			throw new Exception("Overflow_TimeSpanTooLong");
		}
		this.ticks = num * TicksPerMillisecond;
	}

	public long getTicks() {
		return this.ticks;
	}

	public int getDays() {
		return (int) (this.ticks / Long.valueOf(TicksPerDay));
	}

	// not total hours, just hours not more than 24
	public int getHours() {
		return (int) ((this.ticks / Long.valueOf(TicksPerHour)) % ((long) HoursPerDay));
	}

	public int getMilliseconds() {
		return (int) ((this.ticks / ((long) TicksPerMillisecond)));
	}

	public int getMinutes() {
		return (int) ((this.ticks / ((long) TicksPerMinute)) % ((long) 60));
	}

	public int getSeconds() {
		return (int) ((this.ticks / ((long) TicksPerSecond)) % ((long) 60));
	}

	public double getTotalDays() {
		return (this.ticks * DaysPerTick);
	}

	public double getTotalHours() {
		return (this.ticks * HoursPerTick);
	}

	/*
	 * Gets the value of the current TimeSpan structure expressed in whole and
	 * fractional milliseconds.
	 */
	public double getTotalMilliseconds() {

		double num = this.ticks * MillisecondsPerTick;
		if (num > Long.valueOf(MaxMilliSeconds)) {
			return Long.valueOf(MaxMilliSeconds);
		}
		if (num < Long.valueOf(MinMilliSeconds)) {
			return Long.valueOf(MinMilliSeconds);
		}
		return num;
	}

	public double getTotalMinutes() {
		return (this.ticks * MinutesPerTick);
	}

	public double getTotalSeconds() {
		return (this.ticks * 1E-07);
	}

	/* Adds the specified TimeSpan to this instance. */
	public TimeSpan add(TimeSpan ts) throws Exception {
		long ticks = this.ticks + ts.ticks;
		// less than MinValue or more than MaxValue, exception
		if (((this.ticks >> 0x3f) == (ts.ticks >> 0x3f))
				&& ((this.ticks >> 0x3f) != (ticks >> 0x3f))) {
			throw new Exception("Overflow_TimeSpan Too Long");
		}
		return new TimeSpan(ticks);
	}

	/*
	 * Compares two TimeSpan values and returns an integer that indicates their
	 * relationship.
	 */
	public static int compare(TimeSpan t1, TimeSpan t2) {
		if (t1.ticks > t2.ticks) {
			return 1;
		}
		if (t1.ticks < t2.ticks) {
			return -1;
		}
		return 0;
	}

	/*
	 * Overloaded. Compares this instance to a specified object or TimeSpan and
	 * returns an indication of their relative values.
	 */
	public int compareTo(Object value) {
		if (value == null) {
			return 1;
		}
		if (!(value instanceof TimeSpan)) {
			throw new RuntimeException("Arg_MustBeTimeSpan");
		}
		long num = ((TimeSpan) value).ticks;
		if (this.ticks > num) {
			return 1;
		}
		if (this.ticks < num) {
			return -1;
		}
		return 0;
	}

	/*
	 * Returns a TimeSpan that represents a specified number of days, where the
	 * specification is accurate to the nearest millisecond.
	 */
	public static TimeSpan fromDays(double value) throws Exception {
		return interval(value, MillisPerDay);
	}

	/*
	 * Returns a new TimeSpan object whose value is the absolute value of the
	 * current TimeSpan object
	 */
	public TimeSpan duration() throws Exception {
		if (this.ticks == MinValue.ticks) {
			throw new Exception("Overflow_Duration");
		}
		return new TimeSpan((this.ticks >= 0) ? this.ticks : -this.ticks);
	}

	/*
	 * Overloaded. Overridden. Returns a value indicating whether two instances
	 * of TimeSpan are equal.
	 */
	public boolean equals(Object value) {
		if (value instanceof TimeSpan) {
			return (this.ticks == ((TimeSpan) value).ticks);
		}
		return false;
	}

	public static boolean equals(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks == t2.ticks);
	}

	public int hashCode() {
		return (((int) this.ticks) ^ ((int) (this.ticks >> 0x20)));
	}

	/*
	 * Returns a TimeSpan that represents a specified number of hours, where the
	 * specification is accurate to the nearest millisecond.
	 */
	public static TimeSpan fromHours(double value) throws Exception {
		return interval(value, 0x36ee80);
	}

	/* calculate time interval in ticks */
	private static TimeSpan interval(double value, int scale) throws Exception {
		if (Double.isNaN(value)) {
			throw new Exception("Arg_CannotBeNaN");
		}

		double num = value * scale;
		if ((num > Long.valueOf(MaxMilliSeconds))
				|| (num < Long.valueOf(MinMilliSeconds))) {
			throw new Exception("Overflow_TimeSpanTooLong");
		}
		return new TimeSpan((long) ((num) * TicksPerMillisecond));
	}

	/* Returns a TimeSpan that represents a specified number of milliseconds. */
	public static TimeSpan fromMilliseconds(double value) throws Exception {
		return interval(value, 1);
	}

	/*
	 * Returns a TimeSpan that represents a specified number of minutes, where
	 * the specification is accurate to the nearest millisecond.
	 */
	public static TimeSpan fromMinutes(double value) throws Exception {
		return interval(value, MillisPerMinute);
	}

	/* Returns a TimeSpan whose value is the negated value of this instance. */
	public TimeSpan negate() throws Exception {
		if (this.ticks == MinValue.ticks) {
			throw new Exception("Overflow_NegateTwosCompNum");
		}
		return new TimeSpan(-this.ticks);
	}

	/*
	 * Constructs a new TimeSpan object from a time interval specified in a
	 * string.
	 */
	public static TimeSpan parse(String s) {

		StringParser parser = new StringParser();

		return new TimeSpan(parser.parse(s));
	}

	/*
	 * Constructs a new TimeSpan object from a time interval specified in a
	 * string.
	 */
	public static boolean tryParse(String s, OutParameter result) {
		long num = 0;
		OutParameter out = new OutParameter(num);
		StringParser parser = new StringParser();
		if (parser.tryParse(s, out)) {
			result.timeSpan = new TimeSpan(out.value);
			return true;
		}
		result.timeSpan = new TimeSpan((long) 0);
		return false;
	}

	public static TimeSpan fromSeconds(double value) throws Exception {
		return interval(value, MillisPerSecond);
	}

	/* Subtracts a specified TimeSpan from another specified TimeSpan. */
	public TimeSpan subtract(TimeSpan ts) throws Exception {
		long ticks = this.ticks - ts.ticks;
		if (((this.ticks >> 0x3f) != (ts.ticks >> 0x3f))
				&& ((this.ticks >> 0x3f) != (ticks >> 0x3f))) {
			throw new Exception("Overflow_TimeSpanTooLong");
		}
		return new TimeSpan(ticks);
	}

	public static TimeSpan fromTicks(long value) {
		return new TimeSpan(value);
	}

	public static long timeToTicks(int hour, int minute, int second)
			throws Exception {
		long num = ((SencondsPerHour * (long) hour) + ((long) minute * 60)) + (long) second;
		if ((num > Long.valueOf(MaxSeconds))
				|| (num < Long.valueOf(MinSeconds))) {
			throw new Exception("Overflow_TimeSpanTooLong");
		}
		return (num * TicksPerSecond);
	}

	private String intToString(int n, int digits) {
		return ParseNumbers.intToString(n, 10, digits, (char) 0, 0);
	}

	/*
	 * Overridden. Returns the string representation of the value of this
	 * instance.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int num = (int) (this.ticks / Long.valueOf(TicksPerDay)); // days
		long num2 = this.ticks % Long.valueOf(TicksPerDay); // Ticks in one
		// days
		if (this.ticks < 0) {
			builder.append("-");
			num = -num;
			num2 = -num2;
		}
		if (num != 0) {// if day exists
			builder.append(num);
			builder.append(".");// append "."beween days and hours
		}

		// hours, ranging from 0 to 23
		builder.append(this.intToString((int) ((num2 / Long
				.valueOf(TicksPerHour)) % ((long) HoursPerDay)), 2));
		builder.append(":");

		// munites, ranging from 0 to 59
		builder.append(this.intToString(
				(int) ((num2 / ((long) TicksPerMinute)) % ((long) 60)), 2));
		builder.append(":");
		// seconds, ranging from 0 to 59
		builder.append(this.intToString(
				(int) ((num2 / ((long) TicksPerSecond)) % ((long) 60)), 2));
		int n = (int) (num2 % ((long) TicksPerSecond));
		if (n != 0) {
			builder.append(".");
			builder.append(this.intToString(n, 7));
		}
		return builder.toString();
	}

	/**
	 * imitate
	 * 
	 * 
	 */
	public static TimeSpan operatorMinus(TimeSpan t) throws Exception {
		if (t.ticks == MinValue.ticks) {
			throw new Exception("Overflow_NegateTwosCompNum");
		}
		return new TimeSpan(-t.ticks);
	}

	public static TimeSpan operatorMinus(TimeSpan t1, TimeSpan t2)
			throws Exception {
		return t1.subtract(t2);
	}

	public static TimeSpan operatorPlus(TimeSpan t) {
		return t;
	}

	public static TimeSpan operatorPlus(TimeSpan t1, TimeSpan t2)
			throws Exception {
		return t1.add(t2);
	}

	public static boolean operatorEqual(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks == t2.ticks);
	}

	public static boolean operatorNotEqual(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks != t2.ticks);
	}

	public static boolean operatorLess(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks < t2.ticks);
	}

	public static boolean operatorLessEqual(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks <= t2.ticks);
	}

	public static boolean operatorMore(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks > t2.ticks);
	}

	public static boolean operatorMoreEqual(TimeSpan t1, TimeSpan t2) {
		return (t1.ticks >= t2.ticks);
	}

	static {
		Zero = new TimeSpan((long) 0);
		MaxValue = new TimeSpan(Long.valueOf("7fffffffffffffff", 16));
		MinValue = new TimeSpan(Long.valueOf(MinMilliSeconds));
	}
}

/**
 * DOC x class global comment. Detailled comment <br/>
 * 
 */
class StringParser {

	public static final long TicksPerDay = Long.valueOf("c92a69c000", 16);

	public static final long TicksPerHour = Long.valueOf("861c46800", 16);

	public static final long TicksPerMinute = 0x23C34600;

	public static final long TicksPerSecond = 0x989680;

	String str;

	char ch;

	int pos;

	int len;

	private ParseError error;

	/* get the next char position and corresponding value */
	private void nextChar() {
		if (this.pos < this.len) {
			this.pos++;
		}
		this.ch = (this.pos < this.len) ? this.str.charAt(this.pos) : '\0';
	}

	private char nextNonDigit() {

		for (int i = this.pos; i < this.len; i++) {
			char ch = this.str.charAt(i);
			if ((ch < '0') || (ch > '9')) {
				return ch;
			}
		}
		return '\0';
	}

	/*
	 * Constructs a new TimeSpan object from a time interval specified in a
	 * string.
	 */
	public long parse(String s) {
		long num = 0;
		OutParameter out = new OutParameter(num);

		if (this.tryParse(s, out)) {
			return out.value;
		}
		switch (this.error) {
		case Format:
			throw new XWTException("Format_InvalidString");

		case Overflow:
			throw new XWTException("Overflow_TimeSpanTooLong");

		case OverflowHoursMinutesSeconds:
			throw new XWTException("Overflow_TimeSpanElementTooLarge");

		case ArgumentNull:
			throw new XWTException("s");
		}
		return (long) 0;
	}

	/*
	 * Constructs a new TimeSpan object from a time interval specified in a
	 * string. Parameters specify the time interval and the variable where the
	 * new TimeSpan object is returned.
	 */

	/*
	 * this piece of code is not correct, because in .net it support "out",
	 * while in Java , it dose not
	 */
	public boolean tryParse(String s, OutParameter out) {// TODO,

		long time = 0;
		out.value = 0;

		OutParameter timeOut = new OutParameter(time);

		if (s == null) {
			this.error = ParseError.ArgumentNull;
			return false;
		}

		this.str = s;
		this.len = s.length();
		this.pos = -1;
		this.nextChar();
		this.skipBlanks();
		boolean flag = false;

		if (this.ch == '-') { // year, month, date
			flag = true;
			this.nextChar();
		}

		if (this.nextNonDigit() == ':') {// hour, minutes, seconds
			if (!this.parseTime(timeOut)) {
				return false;
			}
		} else {

			int i = 0;
			OutParameter iOut = new OutParameter(i);

			if (!this.parseInt(0xa2e3ff, iOut)) {
				return false;
			}
			timeOut.value = iOut.value * 0xc92a69c000L;
			if (this.ch == '.') {
				long num3 = 0;
				OutParameter num3Out = new OutParameter(num3);
				this.nextChar();
				if (!this.parseTime(num3Out)) {
					return false;
				}
				timeOut.value += num3Out.value;
			}
		}
		if (flag) {
			timeOut.value = -timeOut.value;
			if (timeOut.value > 0) {
				this.error = ParseError.Overflow;
				return false;
			}
		} else if (timeOut.value < 0) {
			this.error = ParseError.Overflow;
			return false;
		}

		this.skipBlanks();

		if (this.pos < this.len) {
			this.error = ParseError.Format;
			return false;
		}

		out.value = timeOut.value;
		return true;
	}

	/* max stands hours, minutes 59 */
	private boolean parseInt(int max, OutParameter out) {// i is munites <60,
		// decide minutes
		// are whether
		// overflow
		out.value = 0;
		int pos = this.pos;

		while ((this.ch >= '0') && (this.ch <= '9')) {

			if ((((long) out.value) & 0xf0000000L) != 0) {
				this.error = ParseError.Overflow;
				return false;
			}

			out.value = ((out.value * 10) + this.ch) - 0x30;

			if (out.value < 0) {
				this.error = ParseError.Overflow;
				return false;
			}
			this.nextChar();
		}
		if (pos == this.pos) {
			this.error = ParseError.Format;
			return false;
		}
		if (out.value > max) {
			this.error = ParseError.Overflow;
			return false;
		}
		return true;
	}

	private boolean parseTime(OutParameter out) {
		int i = 0;
		out.value = 0;

		OutParameter iOut = new OutParameter(i);

		if (!this.parseInt(0x17, iOut)) {// parse hours 0-23
			if (this.error == ParseError.Overflow) {
				this.error = ParseError.OverflowHoursMinutesSeconds;
			}
			return false;
		}

		out.value = iOut.value * 0x861c46800L;

		if (this.ch != ':') {// not time format
			this.error = ParseError.Format;
			return false;
		}

		this.nextChar();

		if (!this.parseInt(0x3b, iOut)) {// parse minutes 0-59
			if (this.error == ParseError.Overflow) {
				this.error = ParseError.OverflowHoursMinutesSeconds;
			}
			return false;
		}

		out.value += (iOut.value * 0x23c34600L);

		if (this.ch == ':') {
			this.nextChar();
			if (this.ch != '.') {

				if (!this.parseInt(0x3b, iOut)) {// parse seconds 0-59
					if (this.error == ParseError.Overflow) {
						this.error = ParseError.OverflowHoursMinutesSeconds;
					}
					return false;
				}
				out.value += (iOut.value * 0x989680L);
			}

			if (this.ch == '.') {

				this.nextChar();
				int num2 = 0x989680;

				while (((num2 > 1) && (this.ch >= '0')) && (this.ch <= '9')) {
					num2 /= 10;
					out.value += (this.ch - '0') * num2;
					this.nextChar();
				}
			}
		}
		return true;
	}

	private void skipBlanks() {
		while ((this.ch == ' ') || (this.ch == '\t')) {
			this.nextChar();
		}
	}

	private enum ParseError {

		Format,

		Overflow,

		OverflowHoursMinutesSeconds,

		ArgumentNull;
	}
}

class OutParameter {

	long value = 0;

	TimeSpan timeSpan = null;

	/**
	 * DOC x OutParameter constructor comment.
	 * 
	 * @param value
	 */
	public OutParameter(long value) {
		super();
		this.value = value;
	}

	public OutParameter(TimeSpan timeSpan) {
		super();
		this.timeSpan = timeSpan;
	}
}

