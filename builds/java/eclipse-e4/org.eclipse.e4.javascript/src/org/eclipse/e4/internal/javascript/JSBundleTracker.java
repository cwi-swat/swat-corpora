package org.eclipse.e4.internal.javascript;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.e4.javascript.*;
import org.eclipse.e4.javascript.Constants;
import org.osgi.framework.*;
import org.osgi.util.tracker.BundleTracker;

public class JSBundleTracker extends BundleTracker {
	private JSFrameworkImpl framework;
	private Map jsBundleMap = new HashMap();

	public JSBundleTracker(BundleContext context, JSFrameworkImpl framework) {
		super(context, Bundle.RESOLVED | Bundle.ACTIVE | Bundle.STARTING | Bundle.STOPPING, null);
		this.framework = framework;
	}

	public JSBundle getJSBundle(Bundle bundle) {
		return (JSBundle) jsBundleMap.get(bundle);
	}

	public Object addingBundle(Bundle bundle, BundleEvent event) {
		String jsBundleHeader = (String) bundle.getHeaders().get(Constants.JAVA_SCRIPT_BUNDLE);
		if (jsBundleHeader == null)
			return null;

		URL jsBundleEntry = bundle.getEntry(jsBundleHeader.trim());
		if (jsBundleEntry == null)
			return null;

		try {
			JSBundle jsBundle = framework.installBundle(jsBundleEntry.toString(), bundle);
			jsBundleMap.put(bundle, jsBundle);
			framework.resolve();
			if (event == null && bundle.getState() == Bundle.ACTIVE)
				jsBundle.start();
			return jsBundle;
		} catch (JSBundleException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		if (event == null)
			return;

		JSBundle jsBundle = (JSBundle) object;
		switch (event.getType()) {
			case BundleEvent.STARTED :
				jsBundle.start();
				break;
			case BundleEvent.STOPPING :
				jsBundle.stop();
		}
	}

	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		JSBundle jsBundle = (JSBundle) object;
		jsBundle.uninstall();
		jsBundleMap.remove(bundle);
		framework.refresh();
	}

}
