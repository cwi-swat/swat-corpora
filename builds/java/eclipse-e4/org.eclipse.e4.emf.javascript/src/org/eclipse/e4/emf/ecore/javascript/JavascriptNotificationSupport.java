/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.emf.ecore.javascript;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.mozilla.javascript.Function;

public class JavascriptNotificationSupport extends EContentAdapter {

	private JavascriptSupport javascriptSupport;

	private JavascriptNotificationSupport(JavascriptSupport javascriptSupport) {
		this.javascriptSupport = javascriptSupport;
	}

	JavascriptNotificationSupport(JavascriptSupport javascriptSupport, Notifier notifier) {
		this(javascriptSupport);
		addAdapter(notifier);
	}
	
	@Override
	protected void addAdapter(Notifier notifier) {
		if (! notifier.eAdapters().contains(this)) {
			super.addAdapter(notifier);
		} else {
			System.err.println("Duplicate JavascriptNotificationSupport adapter for " + notifier);
		}
	}

	protected void setTarget(EObject target) {
		// trigger initialization
//		javascriptSupport.wrap(target);
		super.setTarget(target);
	}

	private static String getEventName(int eventType) {
		String eventName = "Change";
		switch (eventType) {
		case Notification.SET: 			eventName = "Set"; 			break;
		case Notification.UNSET: 		eventName = "Unset"; 		break;
		case Notification.ADD: 			eventName = "Add"; 			break;
		case Notification.REMOVE: 		eventName = "Remove"; 		break;
		case Notification.ADD_MANY: 	eventName = "AddMany"; 		break;
		case Notification.REMOVE_MANY: 	eventName = "RemoveMany"; 	break;
		case Notification.MOVE: 		eventName = "Move"; 		break;
		}
		return eventName;
	}

	private static StringBuilder methodName = new StringBuilder();
	
	private static String getMethodName(EStructuralFeature feature, int eventType) {
		methodName.setLength(0);
		String eventName = JavascriptNotificationSupport.getEventName(eventType);
		String featureName = feature.getName();
		methodName.append("on");
		methodName.append(eventName);
		char first = featureName.charAt(0);
		if (Character.isUpperCase(first)) {
			methodName.append(featureName);
		} else {
			methodName.append(Character.toUpperCase(first));
			methodName.append(featureName, 1, featureName.length());
		}
		return methodName.toString().intern();
	}

	private boolean rethrowException = false;
	
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		Object source = notification.getNotifier();
		if (source instanceof EObject) {
			JavascriptNotificationSupport.notifyChanged(notification, (EObject)source, javascriptSupport, rethrowException);
		}
	}

	static void notifyChanged(Notification notification, EObject handler, JavascriptSupport javascriptSupport, boolean rethrowException) {
		Object feature = notification.getFeature();
		if (feature instanceof EStructuralFeature) {
			String specificMethodName = JavascriptNotificationSupport.getMethodName((EStructuralFeature)feature, notification.getEventType());
			String genericMethodName = JavascriptNotificationSupport.getMethodName((EStructuralFeature)feature, -1);
			Object[] methodArgs = new Object[]{notification};
			if (javascriptSupport.getProperty(handler, specificMethodName) instanceof Function) {
//			System.out.println("Notifying " + javascriptSupport.toString(handler) + " with " + specificMethodName);
				javascriptSupport.callMethod(handler, specificMethodName, methodArgs, rethrowException);
			} else if (javascriptSupport.getProperty(handler, genericMethodName) instanceof Function) {
//			System.out.println("Notifying " + javascriptSupport.toString(handler) + " with " + genericMethodName);
				javascriptSupport.callMethod(handler, genericMethodName, methodArgs, rethrowException);
			} else if (javascriptSupport.getVariable(handler.eResource(), specificMethodName) instanceof Function) {
//				System.out.println("Notifying change of " + javascriptSupport.toString(handler) + " with " + specificMethodName);
				javascriptSupport.callFunction(handler.eResource(), specificMethodName, methodArgs, rethrowException);
			} else if (javascriptSupport.getVariable(handler.eResource(), genericMethodName) instanceof Function) {
//			System.out.println("Notifying change of " + javascriptSupport.toString(handler) + " with " + genericMethodName);
				javascriptSupport.callFunction(handler.eResource(), genericMethodName, methodArgs, rethrowException);
			}
		} else {
//			System.out.println("Notified by " + javascriptSupport.toString(handler) + ", but feature is " + notification.getFeature());
		}
	}

//	private static Boolean methodNameCase = Boolean.TRUE;

//	private static String casify(String name) {
//		if (JavascriptNotificationSupport.methodNameCase != null) {
//			char first = name.charAt(0);
//			name = (JavascriptNotificationSupport.methodNameCase.booleanValue() ? Character.toUpperCase(first) : Character.toLowerCase(first)) + name.substring(1);
//		}
//		return name;
//	}
}
