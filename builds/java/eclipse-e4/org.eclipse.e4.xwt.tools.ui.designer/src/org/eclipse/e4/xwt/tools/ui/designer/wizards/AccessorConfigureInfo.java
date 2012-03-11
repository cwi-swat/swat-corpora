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
package org.eclipse.e4.xwt.tools.ui.designer.wizards;

import org.eclipse.jdt.core.IPackageFragmentRoot;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class AccessorConfigureInfo {

	private String classFilePath;

	private String sourceFolder;

	private String sourcePackage;

	private String className;

	private String propertyFilePath;

	private String propertyFolder;

	private String propertyPackage;

	private String propertyName;

	private IPackageFragmentRoot classRoot;

	private IPackageFragmentRoot propertyRoot;

	public String getClassFilePath() {
		return classFilePath;
	}

	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	public String getPropertyFilePath() {
		return propertyFilePath;
	}

	public void setPropertyFilePath(String propertyFilePath) {
		this.propertyFilePath = propertyFilePath;
	}

	public IPackageFragmentRoot getPropertyRoot() {
		return propertyRoot;
	}

	public void setPropertyRoot(IPackageFragmentRoot propertyRoot) {
		this.propertyRoot = propertyRoot;
	}

	public IPackageFragmentRoot getClassRoot() {
		return classRoot;
	}

	public void setClassRoot(IPackageFragmentRoot classRoot) {
		this.classRoot = classRoot;
	}

	public String getSourceFolder() {
		return sourceFolder;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public String getSourcePackage() {
		return sourcePackage;
	}

	public void setSourcePackage(String sourcePackag) {
		this.sourcePackage = sourcePackag;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPropertyFolder() {
		return propertyFolder;
	}

	public void setPropertyFolder(String propertyFolder) {
		this.propertyFolder = propertyFolder;
	}

	public String getPropertyPackage() {
		return propertyPackage;
	}

	public void setPropertyPackage(String propertyPackag) {
		this.propertyPackage = propertyPackag;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
