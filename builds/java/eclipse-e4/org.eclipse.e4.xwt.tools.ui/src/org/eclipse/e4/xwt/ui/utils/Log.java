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

package org.eclipse.e4.xwt.ui.utils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.xwt.ILogger;
import org.eclipse.e4.xwt.Tracking;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author zl
 * 
 */
public class Log implements ILogger {

	private MessageConsoleStream messageConsoleStream = null;
	private Map<Tracking, String> messageMap = new HashMap<Tracking, String>();
	private MessageConsole console;

	private MessageConsoleStream getConsoleStream() {
		if (messageConsoleStream == null) {
			ConsolePlugin plugin = ConsolePlugin.getDefault();
			if (plugin == null) {
				return null;
			}
			console = new MessageConsole("XWT Application [XWT Application]", null);
			// add console
			IConsoleManager manager = (IConsoleManager) plugin.getConsoleManager();
			manager.addConsoles(new IConsole[] { console });
			// show console
			manager.showConsoleView(console);
			// return console stream
			messageConsoleStream = console.newMessageStream();
			messageConsoleStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		}
		return messageConsoleStream;
	}

	public void printInfo(String message, Tracking tracking, Set<Tracking> trackTypes) {
		String printMessage = "";

		if (trackTypes != null && trackTypes.size() > 0) {
			if (trackTypes.contains(tracking)) {
				printMessage = (String) messageMap.get(tracking);
			}
		}
		MessageConsoleStream msgStream = getConsoleStream();
		console.clearConsole();
		if (msgStream != null)
			msgStream.print(printMessage);
	}

	public void addMessage(String message, Tracking tracking) {
		if (messageMap.containsKey(tracking)) {
			messageMap.remove(tracking);
		}
		messageMap.put(tracking, message);
	}

	public void removeMessage(Tracking tracking) {
		if (messageMap.containsKey(tracking)) {
			messageMap.remove(tracking);
		}
	}

	public void error(Throwable e) {
		MessageConsoleStream msgStream = getConsoleStream();
		if (msgStream != null) {
			Color color = msgStream.getColor();
			msgStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			PrintStream printStream = new PrintStream(msgStream);
			e.printStackTrace(printStream);
			msgStream.setColor(color);
		}
	}

	public void error(Throwable e, String message) {
		MessageConsoleStream msgStream = getConsoleStream();
		if (msgStream != null) {
			Color color = msgStream.getColor();
			msgStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			PrintStream printStream = new PrintStream(msgStream);
			msgStream.print(message);
			e.printStackTrace(printStream);
			msgStream.setColor(color);
		}
	}

	public void message(String message) {
		MessageConsoleStream msgStream = getConsoleStream();
		if (msgStream != null) {
			msgStream.print(message);
		}
	}

	public void warning(String message) {
		message(message);
	}
}
