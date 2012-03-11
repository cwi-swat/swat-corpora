package org.eclipse.e4.xwt;

public interface IXWTInitializer {
	void initialize(IXWTLoader loader);
	
	boolean isInitialized();
}
