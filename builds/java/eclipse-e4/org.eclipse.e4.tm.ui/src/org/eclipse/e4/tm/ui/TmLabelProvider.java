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
package org.eclipse.e4.tm.ui;

import java.text.MessageFormat;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class TmLabelProvider extends BaseLabelProvider implements ILabelProvider {

	private ILabelProvider defaultLabelProvider;
	
	public TmLabelProvider(ILabelProvider defaultLabelProvider) {
		super();
		this.defaultLabelProvider = defaultLabelProvider;
	}

	public Image getImage(Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject)object;
			EClass eClass = eObject.eClass();
			while (eClass != null) {
				Image image = getImageForEClass(eClass, eObject.eClass());
				if (image != null) {
					return image;
				}
				EList<EClass> eSuperTypes = eClass.getESuperTypes();
				eClass = (eSuperTypes.size() > 0 ? eSuperTypes.get(0) : null);
			}
		}
		return defaultLabelProvider.getImage(object);
	}

	private String getImageKey(EClass eClass) {
		String imageKey = EcoreUtil.getAnnotation(eClass, Activator.PLUGIN_ID, "imageUri");
		if (imageKey == null) {
			imageKey = "icons/obj16/" + eClass.getName() + ".gif";
		}
		return imageKey;
	}
	private Image getImageForEClass(EClass eClass, EClass forClass) {
		ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		String imageKey = getImageKey(eClass);
		Image image = imageRegistry.get(imageKey);
		if (image == null) {
			String pluginId = Activator.PLUGIN_ID;
			String imageName = imageKey;
			int pos = imageKey.indexOf('#');
			if (pos > 0) {
				pluginId = imageKey.substring(0, pos);
				imageName = imageKey.substring(pos + 1);
			}
			ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imageName);
			if (imageDescriptor != null) {
				image = imageDescriptor.createImage();
				imageRegistry.put(imageKey, image);
				if (forClass != null && forClass != eClass) {
					imageRegistry.put(getImageKey(forClass), image);
				}
			}
		}
		return image;
	}

	private Object tryTextFeature(Object textValue, EObject eObject, String featureName, String format) {
		if (textValue == null && featureName != null) {
			EStructuralFeature textFeature = eObject.eClass().getEStructuralFeature(featureName);
			if (textFeature != null) {
				textValue = eObject.eGet(textFeature);
				if (textValue instanceof String && ((String)textValue).length() == 0) {
					textValue = null;
				}
				if (textValue != null && format != null) {
					textValue = MessageFormat.format(format, new Object[]{textValue});
				}
			}
		}
		return textValue;
	}
	
	private final static int MAX_TEXT_LENGTH = 50;

	public String getText(Object object) {
		String text = null;
		if (object instanceof EObject) {
			EObject eObject = (EObject)object;
			EClass eClass = eObject.eClass();
			text = eClass.getName();
			Object textValue = null;
			textValue = tryTextFeature(textValue, eObject, EcoreUtil.getAnnotation(eClass, Activator.PLUGIN_ID, "labelFeature"), EcoreUtil.getAnnotation(eClass, Activator.PLUGIN_ID, "labelFormat"));
			textValue = tryTextFeature(textValue, eObject, "name", null); // defined in Control
			textValue = tryTextFeature(textValue, eObject, "text", "\"{0}\""); // defined in Labeled
			textValue = tryTextFeature(textValue, eObject, "id", "#{0}"); // currently not used
			if (textValue != null) {
				text += " " + textValue;
			}
		}
		if (text != null) {
			if (text.length() > MAX_TEXT_LENGTH) {
				String truncSuffix = "...";
				text = text.substring(0, MAX_TEXT_LENGTH - truncSuffix.length()) + truncSuffix;
			}
			return text;
		}
		return defaultLabelProvider.getText(object);
	}
}
