package org.eclipse.e4.internal.languages.javascript.junit;

import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.mozilla.javascript.ContextFactory;

public class DebugUtil {

	public static void debug(String rhinoDebug) {
		RhinoDebugger debugger = new RhinoDebugger(rhinoDebug);
		try {
			debugger.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ContextFactory.getGlobal().addListener(debugger);
	}

}
