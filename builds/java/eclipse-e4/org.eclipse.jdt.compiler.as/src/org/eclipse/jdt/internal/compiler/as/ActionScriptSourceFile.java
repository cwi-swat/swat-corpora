/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;

public class ActionScriptSourceFile extends ClassFile {
	private static final String LINE_MAPPING_EXTENSION = ".map"; //$NON-NLS-1$
	ActionScriptScribe actionScriptContents;
	String name;
	String javaSourceFileName;
	ReferenceBinding binding;
	
	public ActionScriptSourceFile(String name, ReferenceBinding binding) {
		this.name = name;
		this.binding = binding;
		String sourceFileName = new String(binding.getFileName()).replace('/', File.separatorChar);		
		this.actionScriptContents = new ActionScriptScribe(sourceFileName);
		this.javaSourceFileName = sourceFileName;
	}

	public void writeToDisk(String outputPath) throws IOException {
		char fileSeparatorChar = File.separatorChar;
		String fileSeparator = File.separator;
		// First we ensure that the outputPath exists
		outputPath = outputPath.replace('/', fileSeparatorChar);
		String fileName = null;
		if (outputPath.endsWith(fileSeparator)) {
			fileName = outputPath + this.name;
		} else {
			fileName = outputPath + fileSeparator + this.name;
		}
		File file = new File(fileName);
		file.getParentFile().mkdirs();
		FileWriter output = new FileWriter(file);
		try {
			// if no IOException occurred, output cannot be null
			output.write(this.actionScriptContents.getContents());
			output.flush();
		} catch(IOException e) {
			throw e;
		} finally {
			output.close();
		}
		// dump the line number info
		// extract the file name without the .as extension
		int index = fileName.lastIndexOf('.');
		StringBuffer buffer = new StringBuffer();
		buffer.append(fileName.toCharArray(), 0, index).append(LINE_MAPPING_EXTENSION);
		fileName = String.valueOf(buffer);
		DataOutputStream outputStream = null;
		try {
			outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			writeSourceInformation(outputStream);
			Map map = this.actionScriptContents.getLineMapping();
			java.util.Set entrySet = map.keySet();
			int length = entrySet.size();
			Integer[] entries = new Integer[length];
			entrySet.toArray(entries);
			java.util.Arrays.sort(entries);
			outputStream.writeInt(length);
			for (int i = 0; i < length; i++) {
				outputStream.writeInt(entries[i].intValue());
				outputStream.writeInt(((Integer) map.get(entries[i])).intValue());
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch(IOException e) {
					// ignore
				}
			}
		}
// dump in System.out
//		System.out.println(this.name);
//		Util.dumpMapFile(fileName);
	}

	private void writeSourceInformation(DataOutputStream outputStream) throws IOException {
		// retrieve the outermost binding
		ReferenceBinding outerMostBinding = this.binding;
		if (this.binding.isNestedType()) {
			ReferenceBinding parent = outerMostBinding;
			while ((parent = parent.enclosingType()) != null) {
				outerMostBinding = parent;
			}
		}
		// extract the source root
		char[][] compoundName = outerMostBinding.compoundName;
		int length = compoundName.length;
		char[] chars = this.javaSourceFileName.toCharArray();
		int count = 0;
		int max = chars.length;
		for (int i = max - 1; i >=0; i--) {
			if (chars[i] == File.separatorChar) {
				count++;
				if (count == length) {
					outputStream.writeUTF(this.javaSourceFileName.substring(0, i));
					outputStream.writeUTF(this.javaSourceFileName.substring(i + 1, max));
				}
			}
		}
	}
}
