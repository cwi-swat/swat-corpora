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
package org.eclipse.e4.xwt.tools.ui.designer.wizards.contents;

import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueEntry;
import org.eclipse.e4.xwt.tools.ui.designer.wizards.models.TextValueModel;
import org.eclipse.jdt.core.IPackageFragmentRoot;

/**
 * @author xrchen (xiaoru.chen@soyatec.com)
 */
public class SourceContents {
	private StringBuffer contents = new StringBuffer();
	private TextValueModel checkedItems;
	private String path;
	private String name;
	private String porpertyFileName;
	private String propertyFilePath;
	private StringBuffer historyContent;
	private String header = "/*******************************************************************************" + "\r" + "* Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others." + "\r" + "* All rights reserved. This program and the accompanying materials" + "\r" + "* are made available under the terms of the Eclipse Public License v1.0" + "\r" + "* which accompanies this distribution, and is available at" + "\r" + "* http://www.eclipse.org/legal/epl-v10.html" + "\r" + "* " + "\r" + "* Contributors:" + "\r" + "*     Soyatec - initial API and implementation" + "\r" + "*******************************************************************************/";
	private String sourceKey;

	public SourceContents(TextValueModel checkedItems, String path, String propertyFilePath, String name, String porpertyFileName, StringBuffer historyContent, IPackageFragmentRoot root) {
		this.checkedItems = checkedItems;
		this.path = path;
		this.propertyFilePath = propertyFilePath;
		this.porpertyFileName = porpertyFileName;
		this.historyContent = historyContent;
		sourceKey = root.getResource().toString();
		if (name != null) {
			this.name = name.substring(0, name.length() - 5);
		}
	}

	public String getSourceContents() {
		if (historyContent == null) {
			String packagePath = getPackagePath(path);
			String porpertypackagePath = getPackagePath(propertyFilePath);
			contents.append(header);
			appendStr("");
			if (!("").equals(packagePath)) {
				appendStr("package " + packagePath + ";");
			}
			appendStr("");
			appendStr("import org.eclipse.osgi.util.NLS;");
			appendStr("");
			appendStr("public class " + name + " extends NLS {");
			if (!("").equals(packagePath)) {
				appendStr("\t" + "private static final String BUNDLE_NAME = \"" + porpertypackagePath + "." + porpertyFileName + "\";");
			} else {
				appendStr("\t" + "private static final String BUNDLE_NAME = \"" + porpertyFileName + "\";");
			}
			appendStr("");
		} else {
			contents.append(historyContent);
		}
		appendkeys();
		appendStr("\t" + "static {");
		appendStr("\t" + "\t" + "NLS.initializeMessages(BUNDLE_NAME, " + name + ".class);");
		appendStr("\t" + "}");
		appendStr("}");

		return contents.toString();
	}

	private String getPackagePath(String filePath) {
		String soureKey = sourceKey.substring(sourceKey.lastIndexOf("/") + 1);
		int index = filePath.indexOf("/" + soureKey + "/");
		String packagePath = "";
		if (index != -1) {
			packagePath = (filePath.substring(index + soureKey.length() + 2)).replace("/", ".");
		}
		return packagePath;
	}

	/**
	 * append string with ";" and "\r".
	 * 
	 * @param str
	 */
	private void appendStr(String str) {
		contents.append(str + "\r");
	}

	/**
	 * append key's definition
	 */
	private void appendkeys() {
		int itemsLen = checkedItems.elements().length;
		for (int i = 0; i < itemsLen; i++) {
			if (checkedItems.elements()[i] instanceof TextValueEntry) {
				String key = ((TextValueEntry) checkedItems.elements()[i]).getKey();
				appendStr("\t" + "public static String " + key + ";");
				appendStr("");
			}
		}
	}
}
