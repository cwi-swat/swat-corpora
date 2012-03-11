package org.eclipse.e4.internal.javascript;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.e4.javascript.JSBundle;
import org.eclipse.e4.javascript.JSBundleException;
import org.mozilla.javascript.Scriptable;

public class JSBundleContext {

	private final JSBundle jsBundle;
	private final JSFrameworkImpl framework;

	public JSBundleContext(JSBundle jsBundle, JSFrameworkImpl framework) {
		this.jsBundle = jsBundle;
		this.framework = framework;
	}

	public JSBundle getBundle() {
		return jsBundle;
	}

	public JSBundle[] getBundles() {
		return framework.getBundles();
	}

	public String getProperty(String name) {
		return framework.getProperty(name);
	}

	public JSBundle installBundle(String location) throws JSBundleException {
		return framework.installBundle(location);
	}

	public JSBundle installBundle(String location, Scriptable object) throws JSBundleException {
		Map headers = new HashMap();
		Object[] ids = object.getIds();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] instanceof String) {
				String key = (String) ids[i];
				Object value = object.get(key, object);
				if (value instanceof String)
					headers.put(key, value);
			}
		}
		return framework.installBundle(location, headers);
	}
}
