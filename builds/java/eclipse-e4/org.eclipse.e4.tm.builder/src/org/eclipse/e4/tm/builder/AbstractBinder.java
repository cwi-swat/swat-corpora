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
package org.eclipse.e4.tm.builder;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.e4.tm.builder.swt.SwtBuilder;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class AbstractBinder extends AdapterImpl implements IBinder {

	public final static String ANNOTATION_URI = SwtBuilder.getAnnotationUri();

	protected IBinderContext context = null;

	// IBinder methods

	public Object update(EObject eObject, Object object, IBinderContext context) {
		this.context = context;
		try {
			if (object == null) {
				object = create(eObject);
				context.putObject(eObject, object);
				context.fireObjectHandled(IBuilderListener.BUILD, eObject, object);
				if (object != null) {
					EList<Adapter> adapters = eObject.eAdapters();
					if (adapters.contains(this)) {
						System.err.println("Duplicate adapter for " + eObject);
					}
					adapters.add(this);
					if (eObject != object) {
						updateFeatures(eObject, object);
					} else {
						for (EObject child: eObject.eContents()) {
							context.update(child);
						}
					}
				}
			} else {
				context.fireObjectHandled(IBuilderListener.UPDATE, eObject, object);
			}
		} finally {
			context = null;
		}
		return object;
	}

	public void dispose(EObject eObject, Object object, IBinderContext context) {
		eObject.eAdapters().remove(this);
		context.putObject(eObject, null);
		for (EObject child: eObject.eContents()) {
			context.dispose(child);
		}
		if (object != null) {
			AbstractBuilder.dispose(object);
		}
	}

	public <T> T adapt(Object value, Class<T> c) {
		return (c.isInstance(value) ? (T)value : null);
	}

	protected <T> T getParent(EObject eObject, Class<T> c) {
		EObject parent =  eObject.eContainer();
		if (parent == null) {
			return context.getRootObject(c);
		} else {
			return context.getObject(parent, c);
		}
	}

	//

	protected void updateFeatures(EObject eObject, Object object) {
		List<EStructuralFeature> features = eObject.eClass().getEAllStructuralFeatures();
		for (Iterator<EStructuralFeature> it = features.iterator(); it.hasNext();) {
			EStructuralFeature feature = it.next();
			if (shouldHandleFeature(feature) && (! shouldntHandleFeature(feature))) {
				update(eObject, feature, object, true);
			}
		}
	}

	protected void update(EObject control, EStructuralFeature feature, Object object, boolean isInit) {
		if (feature instanceof EAttribute) {
			copyFeatureValue2Property(control, control.eGet(feature), feature, object, isInit);
		} else if (feature.isMany()) {
			EList<EObject> values = (EList<EObject>)control.eGet(feature);
			for (Iterator<EObject> it = values.iterator(); it.hasNext();) {
				EObject value = it.next();
				if (value != null) {
					context.update(value);
				}
			}
		} else {
			EObject featureValue = (EObject)control.eGet(feature);
			if (featureValue != null) {
				Object value = context.update(featureValue);
				copyFeatureValue2Property(control, value, feature, object, isInit);
			}
		}
	}

	protected Object convertValue(EStructuralFeature feature, Object value) {
		return value;
	}

	protected Exception copyFeatureValue2Property(EObject eObject, Object value, EStructuralFeature feature, Object object, boolean isInit) {
		String access = getAccessMethod(feature, eObject.eClass());
		String name = getRealName(feature);
		value = convertValue(feature, value);
		boolean equals = featureEqualsProperty(feature, eObject.eClass(), value, object);
		Exception ex = null;
		if ("field".equals(access)) {
			if (! equals) {
				ex = context.setFieldProperty(object, name, value);
			}
		} else if ("property".equals(access)) {
			if (! equals) {
				ex = context.setSetterProperty(object, name, value);
			}
		} else if ("event".equals(access)) {
			if (isInit && feature != null) {
				handleEventFeature(eObject, feature, object);
			}
		} else if ("binder".equals(access)) {
			// implicitly handled by the specific binder
		} else if (access != null && access.length() > 0) {
			if (! equals) {
				// getter setter
				int pos = access.indexOf(' ');
				if (pos >= 0) {
					String setter = access.substring(pos + 1).trim();
					ex = context.setMethodProperty(object, setter, new Object[]{value});
				}
			}
		}
		if (ex != null) {
			System.err.println("Couldn't set " + name + " " + access + " of " + object + ": " + ex);
			Throwable cause = ex.getCause();
			if (cause != null && cause != ex) {
				System.err.println("Cause: " + cause);
			}
		}
		return ex;
	}

	protected String getAccessMethod(EStructuralFeature feature, EClass realClass) {
		return AbstractBuilder.getFeatureAnnotation(feature, realClass, ANNOTATION_URI, "access", null);
	}

	protected boolean featureEqualsProperty(EStructuralFeature feature, EClass realClass, Object value, Object object) {
		return (! feature.isMany()) && equals(value, getPropertyValue(feature, realClass, object, UNDEFINED));
	}

	protected boolean equals(Object value, Object oldValue) {
		return oldValue == value || (oldValue != null && oldValue.equals(value));
	}

	protected boolean shouldHandleFeature(EStructuralFeature feature) {
		return true;
	}

	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
		if (feature instanceof EReference && ((EReference)feature).isContainer()) {
			return true;
		}
		return false;
	}

	protected void handleEventFeature(final EObject control, final EStructuralFeature feature, Object object) {
	}

	protected String getRealName(final EStructuralFeature feature) {
		return AbstractBuilder.getAnnotation(feature, ANNOTATION_URI, "realName", feature.getName());
	}

	protected String getClassAnnotation(EObject control) {
		return AbstractBuilder.getClassAnnotation(control.eClass(), ANNOTATION_URI, "style", null);
	}

	protected Class<?> getToolkitClass(EObject eObject, boolean warn) {
		String eClassName = eObject.eClass().getName();
		String className = AbstractBuilder.getClassAnnotation(eObject.eClass(), ANNOTATION_URI, "realName", null);
		if (className == null) {
			className = AbstractBuilder.getClassAnnotation(eObject.eClass(), ANNOTATION_URI, "javaClass", eClassName);
		}
		String packageName = AbstractBuilder.getClassAnnotation(eObject.eClass(), ANNOTATION_URI, "javaPackage", null);
		if (packageName == null) {
			packageName = AbstractBuilder.getAnnotation(eObject.eClass().getEPackage(), ANNOTATION_URI, "javaPackage", null);
		}
		className = (packageName != null ? (packageName + ".") : "") + (className.indexOf('.') < 0 ? Character.toUpperCase(className.charAt(0)) + className.substring(1) : className);
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			if (warn) {
				System.err.println("Exception when getting toolkit class for " + this + ": " + e);
			}
		}
		return null;
	}

	protected Object create(EObject control) {
		boolean genericBinder = this.getClass() == AbstractBinder.class;
		Class<?> toolkitClass = getToolkitClass(control, ! genericBinder);
		if (toolkitClass == null && genericBinder) {
			return control;
		}
		try {
			return createForClass(toolkitClass);
		} catch (Exception e) {
			System.err.println("Exception when creating toolkit object of " + toolkitClass + ": " + e);
		}
		return null;
	}

	protected Object createForClass(Class<?> swtClass) throws Exception {
		Exception e = null;
		try {
			return swtClass.newInstance();
		} catch (InstantiationException ie1) {
			e = ie1;
			Constructor<?>[] constructors = swtClass.getConstructors();
			for (int i = 0; i < constructors.length; i++) {
				try {
					return createWithConstructor(constructors[i]);
				} catch (InstantiationException ie2) {
					e = ie2;
				}
			}
		}
		throw e;
	}

	protected Object createWithConstructor(Constructor<?> constructor) throws Exception {
		Class<?>[] types = constructor.getParameterTypes();
		Object[] arguments = new Object[types.length];
		for (int i = 0; i < types.length; i++) {
			arguments[i] = getDefaultFor(types[i]);
		}
		return constructor.newInstance(arguments);
	}

	protected Object getDefaultFor(Class<?> c) {
		if (Number.class.isAssignableFrom(c)) {
			return 0;
		} else if (c == int.class || c == short.class || c == byte.class) {
			return 0;
		} else if (c == double.class || c == float.class) {
			return 0.0;
		} else if (c == char.class) {
			return '\0';
		} else if (c == boolean.class) {
			return false;
		}
		return null;
	}

	protected Object UNDEFINED = new Object();

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (isEvent) {
			Object value = getPropertyValue(feature, eObject.eClass(), object, UNDEFINED);
			if (value != UNDEFINED) {
				setFeatureValue(eObject, feature, value);
			}
		} else {
			updateInvalidFeature(eObject, feature, object);
		}
	}

	protected void updateInvalidFeature(EObject eObject, EStructuralFeature feature, Object object) {
		update(eObject, feature, object, false);
	}

	protected Object getPropertyValue(EStructuralFeature feature, EClass realClass, Object object, Object def) {
		String access = getAccessMethod(feature, realClass);
		Object value = def;
		if (access != null) {
			String name = getRealName(feature);
			try {
				if ("field".equals(access)) {
					value = context.getFieldProperty(object, name);
				} else if ("property".equals(access)) {
					value = context.getGetterProperty(object, name);
				} else if ("binder".equals(access)) {
				} else if (access != null) {
					/* getter setter */
					int pos = access.indexOf(' ');
					if (pos >= 0) {
						String getter = access.substring(0, pos).trim();
						value = context.getMethodProperty(object, getter, null);
					}
				} else {
				}
			} catch (Exception e) {
			}
		}
		return value;
	}

	protected void setFeatureValue(EObject eObject, EStructuralFeature feature, Object value) {
		Class<?> type = feature.getEType().getInstanceClass();
		if (feature.isMany()) {
			List<Object> values = Collections.EMPTY_LIST;
			if (value instanceof List) {
				values = (List<Object>)value;
			} else if (value instanceof Collection) {
				values = new ArrayList<Object>((Collection<?>)value);
			} else if (value.getClass().isArray()) {
				values = new ArrayList<Object>();
				int length = Array.getLength(value);
				for (int i = 0; i < length; i++) {
					Object o = Array.get(value, i);
					Object o2 = context.adapt(o, type);
					values.add(o2);
				}
			}
			EList<Object> eList = (EList<Object>)eObject.eGet(feature);
			ECollections.setEList(eList, values);
		} else {
			Object value2 = context.adapt(value, type);
			eObject.eSet(feature, value2);
		}
	}

	// notification handling

	protected void notifyChanged(EObject eObject, EStructuralFeature feature, Notification notification) {
		Object object = context.getObject(eObject, Object.class);
		boolean isEvent = "event".equals(getAccessMethod(feature, eObject.eClass()));
		invalidateFeature(eObject, feature, object, isEvent);
		String invalidates = AbstractBuilder.getFeatureAnnotation(feature, null, ANNOTATION_URI, "invalidates", null);
		for (StringTokenizer tokens = SwtBuilder.getSeparatedTokens(invalidates); tokens.hasMoreTokens();) {
			String featureName = tokens.nextToken();
			if (Character.isUpperCase(featureName.charAt(0))) {
				// propagate up
				context.invalidateFeature(eObject, featureName);
			} else {
				EStructuralFeature invalidatedFeature = eObject.eClass().getEStructuralFeature(featureName);
				if (invalidatedFeature != null) {
					invalidateFeature(eObject, invalidatedFeature, object, isEvent);
				}
			}
		}
	}

	public void notifyChanged(Notification notification) {
		if (notification.getNotifier() instanceof EObject && notification.getFeature() instanceof EStructuralFeature) {
			notifyChanged((EObject)notification.getNotifier(), (EStructuralFeature)notification.getFeature(), notification);
		}
	}

	//

	public boolean validateFeature(EObject eObject, Object object, String featureName, IBinderContext context) {
		if ("Object".equals(featureName)) {
			context.dispose(eObject);
			context.update(eObject);
			return true;
		}
		return false;
	}

	public void updateStyle(EObject eObject, Object object, IBinderContext context) {
	}
}
