/**
 * 
 */
package org.eclipse.e4.emf.javascript.ui;

import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

class PlatformLoggerHandler extends Handler {
	
	private Bundle bundle;
	
	public PlatformLoggerHandler(Bundle bundle) {
		super();
		this.bundle = bundle;
	}

	public void flush() {
	}

	public void close() throws SecurityException {
	}

	public void publish(LogRecord record) {
		int severity = IStatus.OK;
		if (record.getLevel() == Level.SEVERE) {
			severity = IStatus.ERROR;
		} else if (record.getLevel() == Level.WARNING) {
			severity = IStatus.WARNING;
		} else if (record.getLevel() == Level.INFO) {
			severity = IStatus.INFO;
		} 
		Throwable throwable = record.getThrown();
		String message = record.getMessage();
		if (message != null) {
			if (record.getParameters() != null) {
				message = MessageFormat.format(record.getMessage(), record.getParameters());
			}
		} else if (throwable != null) {
			if (throwable.getMessage() != null) {
				message = throwable.getMessage();
			}
		}
		final Status status = new Status(IStatus.ERROR, record.getLoggerName(), severity, message, throwable);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Platform.getLog(bundle).log(status);
			}
		});
	}
}