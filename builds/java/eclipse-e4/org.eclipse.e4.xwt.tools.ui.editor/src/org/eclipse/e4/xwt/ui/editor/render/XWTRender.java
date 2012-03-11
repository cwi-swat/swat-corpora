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
package org.eclipse.e4.xwt.ui.editor.render;

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.e4.xwt.vex.VEXRenderer;
import org.eclipse.e4.xwt.vex.swt.ImageCapture;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class XWTRender implements VEXRenderer {
	private String hostClassName;
	private Map<String, Object> options; 

	/**
	 * CanvasManager is used to draw the captured image and manage the ScrollBars.
	 */
	private CanvasManager canvasManager;
	private ProjectContext projectContext;
	private PropertyChangeListener changeListener;

	private Shell shell;

	/**
	 * This is a count of the browser loading.
	 */
	private int time = 0;

	public XWTRender(Canvas container, PropertyChangeListener changeListener) {
		this.changeListener = changeListener;
		canvasManager = new CanvasManager(container);
		
		options = new HashMap<String, Object>();
		options.put(IXWTLoader.DESIGN_MODE_PROPERTY, Boolean.TRUE);
	}

	public void dispose() {
		if (canvasManager != null) {
			canvasManager.dispose();
			canvasManager = null;
		}
		if (shell != null) {
			shell.dispose();
		}
	}

	public boolean updateView(String code, IFile file) {
		if (shell != null) {
			shell.dispose();
		}
		Control control = Display.getCurrent().getFocusControl();
		try {
			IJavaProject javaProject = JavaCore.create(file.getProject());
			if (!javaProject.exists()) {
				return false;
			}

			if (projectContext != null) {
				projectContext.removePropertyChangeListener(changeListener);
			}

			projectContext = ProjectContext.getContext(javaProject);
			XWT.setLoadingContext(projectContext);
			Object rootElement;
			try {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(code.getBytes());
				rootElement = XWT.loadWithOptions(inputStream, file.getLocationURI().toURL(), options);
			} catch (Exception e) {
				return false;
			}

			if (rootElement != null) {
				Rectangle rectangle = new Rectangle(0, 0, 0, 0);
				for (Monitor monitor : Display.getDefault().getMonitors()) {
					Rectangle bounds = monitor.getBounds();
					rectangle = rectangle.union(bounds);
				}

				shell = XWT.findShell(rootElement);
				if (shell == null) {
					throw new XWTException("Root element is a control.");
				}
				shell.setFocus();
//				shell.pack();
				shell.setLocation(rectangle.x + rectangle.width + 200, rectangle.y + rectangle.height + 200);
				shell.open();

				Object hostClr = XWT.getCLR(rootElement);
				if (hostClr != null) {
					hostClassName = hostClr.getClass().getName();
				}

				final List<Browser> browsers = new ArrayList<Browser>();
				findBrowser(rootElement, browsers);
				if (!browsers.isEmpty()) {
					/* Fixed the bug of browser: 1. Captured the image from browser after document loading finished. 2. Closed the parent shell when all documents loading finished. */
					time = 0;
					final int total = browsers.size();
					ProgressListener listener = new ProgressListener() {
						public void changed(ProgressEvent event) {
							if (canvasManager == null || shell == null || shell.isDisposed()) {
								return;
							}
							Image image = ImageCapture.getImageCapture().captureImage(shell);
							canvasManager.setImage(image);
						}

						public void completed(ProgressEvent event) {
							if (canvasManager == null || shell == null || shell.isDisposed()) {
								return;
							}
							Image image = ImageCapture.getImageCapture().captureImage(shell);
							canvasManager.setImage(image);
							time++;
							if (time == total) {
								shell.close();
							}
						}
					};
					for (Browser browser : browsers) {
						browser.addProgressListener(listener);
					}
				} else if (shell != null && !shell.isDisposed()) {
					Image image = ImageCapture.getImageCapture().defaultCapture(shell);
					canvasManager.setImage(image);
					shell.close();
				}

				projectContext.addPropertyChangeListener(changeListener);
			}
		} finally {
			if (control != null) {
				control.setFocus();
			}
		}
		return true;
	}

	private void findBrowser(Object control, List<Browser> browsers) {
		if (control instanceof Composite) {
			Composite parent = (Composite) control;
			Control[] children = parent.getChildren();
			for (Control child : children) {
				if (child instanceof Browser) {
					browsers.add((Browser) child);
				}
				findBrowser(child, browsers);
			}
		}
	}

	public String getHostClassName() {
		return hostClassName;
	}

	public void setHostClassName(String hostClassName) {
		this.hostClassName = hostClassName;
	}
}
