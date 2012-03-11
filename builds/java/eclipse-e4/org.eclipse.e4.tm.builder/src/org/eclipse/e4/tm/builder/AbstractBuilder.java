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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.tm.stringconverter.StringConversion;
import org.eclipse.e4.tm.stringconverter.StringConverterContext;
import org.eclipse.e4.tm.stringconverters.AbstractClassStringConverter;
import org.eclipse.e4.tm.stringconverters.ClassStringConverter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public abstract class AbstractBuilder implements IBuilder, StringConverterContext, IBinderContext {

	protected Object getToolkitComposite(Object context, Class<?> c) {
		if (c.isInstance(context)) {
			return context;
		}
		if (context instanceof IAdaptable) {
			return ((IAdaptable)context).getAdapter(c);
		}
		return null;
	}

	private Object root = null;

	public <T> T getRootObject(Class<T> c) {
		return adapt(root, c);
	}

	public void build(EObject control, Object context) {
		root = context;
		update(control);
		fireObjectHandled(IBuilderListener.BUILD, control, context);
	}
	
	private URIConverter uriConverter;

	public URIConverter getUriConverter() {
		if (uriConverter == null) {
			uriConverter = new ExtensibleURIConverterImpl();
		}
		return uriConverter;
	}

	public void build(Resource res, Object context) {
		EObject eObject = res.getContents().get(0);
//		for (EObject eObject: res.getContents()) {
			build(eObject, context);
//		}
	}

	//

	private List<Object> disposables = new ArrayList<Object>();

	public void registerDisposable(Object disposable) {
		disposables.add(disposable);
	}

	private StringConversion stringConversion = new StringConversion();
	{
		stringConversion.setStringConverterContext(this);
	}

	public StringConversion getStringConverter() {
		return stringConversion;
	}


	public <T> T convert(String value, Class<T> klass) throws Exception {
		return stringConversion.convert(value, klass);
	}

	//

	private AbstractClassStringConverter classResolver = new ClassStringConverter();

	public AbstractClassStringConverter getClassResolver() {
		return classResolver;
	}

	public Class<?> resolve(String className) {
		return classResolver.resolve(className);
	}

	public void dispose() {
		for (Object disposable: disposables) {
			dispose(disposable);
		}
		for (EObject eObject: eObject2ObjectMap.keySet()) {
			dispose(eObject);
		}
		eObject2ObjectMap.clear();
	}

	static void dispose(Object disposable) {
		try {
			disposable.getClass().getMethod("dispose").invoke(disposable);
		} catch (Exception e) {
		}
	}

	//

	public static String getClassAnnotation(EClass eClass, String uri, String key, String def) {
		ArrayList<EClass> superClasses = new ArrayList<EClass>(eClass.getEAllSuperTypes());
		superClasses.add(0, eClass);
		for (Iterator<EClass> it = superClasses.iterator(); it.hasNext();) {
			EClass superClass = it.next();
			String value = EcoreUtil.getAnnotation(superClass, uri, key);
			if (value != null) {
				return value;
			}
		}
		return def;
	}

	public static String getFeatureAnnotation(EStructuralFeature feature, EClass realClass, String uri, String key, String def) {
		String value = null;
		EClass subClass = realClass;
		while (value == null && subClass != null && subClass != feature.eContainer()) {
			String altUri = uri + "#" + subClass.getName();
			value = EcoreUtil.getAnnotation(feature, altUri, key);
			List<EClass> superTypes = subClass.getESuperTypes();
			// try superclasses?
			subClass = (superTypes.size() > 0 ? superTypes.get(0) : null);
		}
		if (value == null) {
			value = EcoreUtil.getAnnotation(feature, uri, key);
		}
		if (value == null) {
			value = AbstractBuilder.getClassAnnotation(feature.getEContainingClass(), uri, key, def);
		}
		return value;
	}

	public static String getAnnotation(EModelElement element, String uri, String key, String def) {
		String value = EcoreUtil.getAnnotation(element, uri, key);
		return (value != null ? value : def);
	}

	public static String casify(String s, Boolean cas) {
		if (s.length() > 0 && cas != null) {
			char first = s.charAt(0);
			s = (cas.booleanValue() ? Character.toUpperCase(first) : Character.toLowerCase(first)) + s.substring(1);
		}
		return s;
	}

	//

	private Map<EObject, Object> eObject2ObjectMap = new HashMap<EObject, Object>();
	private Map<Object, EObject> object2EObjectMap = new HashMap<Object, EObject>();

	public <T> T getObject(EObject eObject, Class<T> c) {
		return getObject(eObject, c, false);
	}

	public EObject getEObject(Object object) {
		return object2EObjectMap.get(object);
	}

	public void putObject(EObject eObject, Object object) {
		eObject2ObjectMap.put(eObject, object);
		if (object != null) {
			object2EObjectMap.put(object, eObject);
		}
	}

	protected <T> T getObject(EObject eObject, Class<T> c, boolean searchContainers) {
		EObject eO = eObject;
		while ((eO == eObject || searchContainers) && eO != null) {
			Object o = eObject2ObjectMap.get(eO);
			if (! c.isInstance(o)) {
				// use eO to make sure we adapt using appropriate IBinder
				o = adapt(eO, o, c);
			}
			if (c.isInstance(o)) {
				return (T)o;
			}
			eO = eO.eContainer();
		}
		return null;
	}

	private List<IBuilderListener> builderListeners = new ArrayList<IBuilderListener>();

	public void addBuilderListener(IBuilderListener listener) {
		builderListeners.add(listener);
	}
	public void removeBuilderListener(IBuilderListener listener) {
		builderListeners.remove(listener);
	}

	public void fireObjectHandled(int id, EObject eObject, Object object) {
		for (IBuilderListener listener : builderListeners) {
			listener.objectHandled(id, eObject, object);
		}
	}

	public Object update(EObject eObject) {
		Object object = eObject2ObjectMap.get(eObject);
		IBinder binder = getBinder(eObject, true);
		Object newObject = binder.update(eObject, object, this);
		if (newObject != null) {
			eObject2ObjectMap.put(eObject, newObject);
		}
		fireObjectHandled(IBuilderListener.UPDATE, eObject, object);
		return newObject;
	}

	public void dispose(EObject eObject) {
		Object object = eObject2ObjectMap.get(eObject);
		if (object != null) {
			IBinder binder = getBinder(eObject, false);
			if (binder != null) {
				binder.dispose(eObject, object, this);
				fireObjectHandled(IBuilderListener.DISPOSE, eObject, object);
			}
		}
	}

	public void updateStyle(EObject eObject) {
		Object object = eObject2ObjectMap.get(eObject);
		IBinder binder = getBinder(eObject, false);
		if (binder != null) {
			binder.updateStyle(eObject, object, this);
		}
		fireObjectHandled(IBuilderListener.STYLED, eObject, object);
	}

	private BinderFactory binderFactory = new EClassNameBinderFactory(this);
	
	protected IBinder getBinder(EObject eObject, boolean throwException) {
		IBinder binder = binderFactory.getBinder(eObject);
		if (binder == null && throwException) {
			throw new RuntimeException("Couldn't get IBinder for eClass " + eObject.eClass().getName());
		}
		return binder;
	}

	// support for IBinderContext

	public <T> T adapt(EObject eObject, Object value, Class<T> c) {
		if (eObject == null) {
			return adapt(value, c);
		}
		IBinder binder = getBinder(eObject, true);
		Object o = binder.adapt(value, c);
		if (! c.isInstance(o)) {
			o = adapt(value, c);
		}
		return (T)(c.isInstance(o) ? o : null);
	}

	public <T> T adapt(Object value, Class<T> c) {
		if (c.isPrimitive()) {
			c = ClassStringConverter.getObjectClass(c);
		}
		if (value == null) {
			if (c == URIConverter.class) {
				return (T)getUriConverter();
			}
			return getRootObject(c);
		} else if (c.isInstance(value)) {
			return (T)value;
		} else if (value instanceof Collection && c.isArray()) {
			Collection<?> collection = (Collection<?>)value;
			Class<?> elementType = c.getComponentType();
			Object array = Array.newInstance(elementType, collection.size());
			int i = 0;
			for (Iterator<?> it = collection.iterator(); it.hasNext();) {
				Object o = it.next();
				Object o2 = adapt(o, elementType);
				Array.set(array, i++, o2);
			}
			return (T)array;
		} else if (value instanceof String) {
			return getStringConverter().convert((String)value, c);
		}
		return null;
	}

	public void invalidateFeature(EObject eObject, String featureName) {
		while (eObject != null) {
			IBinder binder = getBinder(eObject, false);
			if (binder != null) {
				if (binder.validateFeature(eObject, getObject(eObject, Object.class), featureName, this)) {
					break;
				}
			}
			eObject = eObject.eContainer();
		}
	}

	//
	
	private ReflectionSupport reflectionSupport = new ReflectionSupport(this);

	public Method getMethod(Object object, String signature) {
		return reflectionSupport.getMethod(object, signature);
	}

	public Object getGetterProperty(Object object, String name) {
		return reflectionSupport.getGetterProperty(object, name);
	}

	public Object getMethodProperty(Object object, String methodSpec, Object[] args) {
		return reflectionSupport.getMethodProperty(object, methodSpec, args);
	}

	public Object getFieldProperty(Object object, String name) {
		return reflectionSupport.getFieldProperty(object, name);
	}

	//

	public Exception setSetterProperty(Object object, String name, Object value) {
		return reflectionSupport.setSetterProperty(object, name, value);
	}

	public Exception setMethodProperty(Object object, String methodSpec, Object[] args) {
		return reflectionSupport.setMethodProperty(object, methodSpec, args);
	}

	public Exception setFieldProperty(Object object, String name, Object value) {
		return reflectionSupport.setFieldProperty(object, name, value);
	}

	public void setProperty(Object object, String name, Object value) throws Exception {
		reflectionSupport.setProperty(object, name, value);
	}
}
