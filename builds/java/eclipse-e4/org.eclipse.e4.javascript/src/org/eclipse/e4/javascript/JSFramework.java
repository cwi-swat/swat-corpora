package org.eclipse.e4.javascript;

import java.util.Map;

public interface JSFramework {
	public JSBundle installBundle(String location) throws JSBundleException;

	public JSBundle installBundle(String location, Map headers) throws JSBundleException;

	public JSBundle[] getBundles();

	public void refresh();

	public void resolve();

	public String getProperty(String name);

	public void setProperty(String name, String value);
}