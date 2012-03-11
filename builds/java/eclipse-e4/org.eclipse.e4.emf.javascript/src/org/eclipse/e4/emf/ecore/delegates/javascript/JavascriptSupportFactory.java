package org.eclipse.e4.emf.ecore.delegates.javascript;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;

public class JavascriptSupportFactory {

	private static JavascriptSupportFactory singleton;
	
	public static JavascriptSupportFactory getJavascriptSupportFactory() {
		if (singleton == null) {
			singleton = new JavascriptSupportFactory();
		}
		return singleton;
	}
	
	private JavascriptSupport javascriptSupport;
	
	public JavascriptSupport getJavascriptSupport() {
		if (javascriptSupport == null) {
			javascriptSupport = new JavascriptSupport();
		}
		return javascriptSupport;
	}
}
