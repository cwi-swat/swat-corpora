/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui.javascript;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class JavaProjectClassLoader extends URLClassLoader {

    public ClassLoader mainLoader;

    public JavaProjectClassLoader(IProject project, ClassLoader mainLoader) throws RuntimeException {
        super(new URL[]{});
        this.mainLoader = mainLoader;
        try {
            project.open(null);
            IJavaProject javaProject = JavaCore.create(project);
            IPath outputPath = javaProject.getOutputLocation();
			URI projectLocation = project.getLocationURI();
			String outputUri = projectLocation.toString() + IPath.SEPARATOR + outputPath.removeFirstSegments(1) + IPath.SEPARATOR;
			URL outputFolder = new URL(outputUri);
            addURL(outputFolder);
        } catch (JavaModelException jme) {
            throw new RuntimeException("Couldn't initialize IJavaProject: ", jme);
        } catch (CoreException ce) {
        	throw new RuntimeException("Couldn't initialize IProject: ", ce);
        } catch (MalformedURLException mue) {
        	throw new RuntimeException("Couldn't create output folder: ", mue);
        }
    }

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (NoClassDefFoundError classNotFoundException) {
            return mainLoader.loadClass(name);
        } catch (ClassNotFoundException classNotFoundException) {
            return mainLoader.loadClass(name);
        } catch (Exception e) {
            throw new RuntimeException("Exception when loading class " + name + ": ", e);
        }
    }
}
