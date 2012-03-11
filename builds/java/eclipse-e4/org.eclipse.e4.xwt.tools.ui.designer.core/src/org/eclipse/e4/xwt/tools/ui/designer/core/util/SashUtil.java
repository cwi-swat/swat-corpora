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
package org.eclipse.e4.xwt.tools.ui.designer.core.util;

public class SashUtil {

	public static String removeWeights(String weights, int index) {
		String[] values = weightsSplit(weights);
		StringBuilder stringBuilder = new StringBuilder();
		String value = null;
		for (int i = 0; i < values.length; i++) {
			if (i != 0 && i != index) {
				stringBuilder.append(",");
			}
			if (value != null){
				int item1 = Integer.parseInt(value);
				int item2 = Integer.parseInt(values[i]);
				values[i] = "" + (item1 + item2);
				value = null;
			}
			if (i != index) {
				stringBuilder.append(values[i]);
			}
			else {
				value = values[i];
			}
		}
		return stringBuilder.toString();
	}

	public static String weightsValue(int[] weights) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < weights.length; i++) {
			if (i != 0) {
				stringBuilder.append(",");				
			}
			stringBuilder.append(weights[i]);
		}
		return stringBuilder.toString();
	}

	public static String weightsDisplayString(int[] weights) {
		return "[" + weightsValue(weights) + "]";
	}
	
	public static String[] weightsSplit(String value) {
		return value.split(",");
	}

	public static int[] toWeights(String value) {
		String[] segments = weightsSplit(value);
		int [] weights = new int [segments.length];
		for(int i = 0; i< segments.length; i++) {
			try {
				weights[i] = Integer.parseInt(segments[i]);
			} catch (NumberFormatException e) {
				weights[i] = 0;
			}
		}
		return weights;
	}

	public static String updateWeightsLengh(int[] segments, int length) {
		if (length == segments.length) {
			return weightsValue(segments);			
		}
		
		int[] values = new int[length];
		if (length > segments.length) {
			for(int i = 0; i< segments.length; i++) {
				values[i] = segments[i];
			}			
		}
		else if (length < segments.length) {
			for(int i = 0; i< values.length; i++) {
				values[i] = segments[i];
			}
		}
		return weightsValue(values);			
	}
}

