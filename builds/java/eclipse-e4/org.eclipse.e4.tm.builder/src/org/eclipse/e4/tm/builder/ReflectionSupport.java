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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.e4.tm.stringconverters.ClassStringConverter;

public class ReflectionSupport {

	private IBinderContext context;

	public ReflectionSupport(IBinderContext context) {
		this.context = context;
	}

	static boolean hasSingleParameterForValue(Class<?>[] parameterTypes, Object value, boolean ignoreType) {
		return (parameterTypes.length == 1 && (ignoreType || ClassStringConverter.getObjectClass(parameterTypes[0]).isInstance(value)));
	}

	private Map<String, Method> methodMap = new HashMap<String, Method>();

	public Method getMethod(Object object, String signature) {
		String key = object.getClass().getName() + '.' + signature;
		Method method = methodMap.get(key);
		if (method != null) {
			return method;
		}
		Class<?>[] types = null;
		int pos = signature.indexOf('(');
		try {
			if (pos >= 0) {
				StringTokenizer parameters = new StringTokenizer(signature.substring(pos + 1, signature.length() - 1), ",");
				signature = signature.substring(0, pos);
				types = new Class<?>[parameters.countTokens()];
				for (int i = 0; parameters.hasMoreTokens(); i++) {
					String typeSpec = parameters.nextToken();
					boolean isArray = false;
					if (typeSpec.endsWith("[]")) {
						isArray = true;
						typeSpec = typeSpec.substring(0, typeSpec.length() - 2);
					} else if (typeSpec.endsWith("*")) {
						isArray = true;
						typeSpec = typeSpec.substring(0, typeSpec.length() - 1);
					}
					Class<?> type = context.convert(typeSpec, Class.class);
					if (isArray) {
						type = Array.newInstance(type, 0).getClass();
					}
					types[i] = type;
				}
			}
			method = object.getClass().getMethod(signature, types);
			methodMap.put(key, method);
		} catch (Exception e1) {
		}
		return method;
	}

	private Map<Object, Method> getterMap = new HashMap<Object, Method>();

	public Method getGetterMethod(Object object, String name) {
		String key = object.getClass().getName() + '.' + name;
		Method method = getterMap.get(key);
		if (method != null) {
			return method;
		}
		Method[] methods = object.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			if (isPrefixedName(method.getName(), "get", name)) {
				if (method.getParameterTypes().length == 0) {
					getterMap.put(key, method);
					return method;
				}
			}
		}
		return null;
	}

	private boolean isPrefixedName(String name, String prefix, String propertyName) {
		return name.length() == 3 + propertyName.length() &&
		name.startsWith(prefix) &&
		name.charAt(prefix.length()) == Character.toUpperCase(propertyName.charAt(0)) &&
		name.endsWith(propertyName.substring(1));
	}

	private Map<Object, Method> setterMap = new HashMap<Object, Method>();

	public Method getSetterMethod(Object object, String name, Object value, boolean ignoreType) {
		String key = object.getClass().getName() + '.' + name;
		Method method = setterMap.get(key);
		if (method != null) {
			return method;
		}
		Method[] methods = object.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			if (isPrefixedName(method.getName(), "set", name)) {
				if (ReflectionSupport.hasSingleParameterForValue(method.getParameterTypes(), value, ignoreType)) {
					setterMap.put(key, method);
					return method;
				}
			}
		}
		return null;
	}

	private Map<Object, Field> fieldMap = new HashMap<Object, Field>();

	private Field getField(Object object, String name, Object value, boolean ignoreType) {
		String key = object.getClass().getName() + '.' + name;
		Field field = fieldMap.get(key);
		if (field != null) {
			return field;
		}
		try {
			field = object.getClass().getField(name);
			if (ignoreType || value == null || ClassStringConverter.getObjectClass(field.getType()).isInstance(value)) {
				fieldMap.put(key, field);
				return field;
			}
		} catch (Exception e) {
		}
		return null;
	}

	public Object getGetterProperty(Object object, String name) {
		Method getterMethod = getGetterMethod(object, name);
		Exception ex = null;
		if (getterMethod != null) {
			try {
				return getterMethod.invoke(object, (Object[])null);
			} catch (Exception e) {
				ex = e;
			}
		}
		throw new RuntimeException("Couldn't invoke getter for " + name, ex);
	}

	public Object getMethodProperty(Object object, String methodSpec, Object[] args) {
		Method method = getMethod(object, methodSpec);
		Exception ex = null;
		if (method != null) {
			try {
				return method.invoke(object, args);
			} catch (Exception e) {
				ex = e;
			}
		}
		throw new RuntimeException("Couldn't invoke method for " + methodSpec, ex);
	}

	public Object getFieldProperty(Object object, String name) {
		Field field = getField(object, name, null, true);
		Exception ex = null;
		if (field != null) {
			try {
				return field.get(object);
			} catch (Exception e) {
				ex = e;
			}
		}
		throw new RuntimeException("Couldn't get field for " + name, ex);
	}

	//

	public Exception setSetterProperty(Object object, String name, Object value) {
		Method setterMethod = getSetterMethod(object, name, value, false);
		if (setterMethod == null) {
			setterMethod = getSetterMethod(object, name, value, true);
		}
		if (setterMethod != null) {
			try {
				value = context.adapt(value, setterMethod.getParameterTypes()[0]);
				setterMethod.invoke(object, new Object[]{value});
				return null;
			} catch (Exception e) {
				return e;
			}
		}
		return new NoSuchMethodException("Couldn't invoke setter for " + name);
	}
	public Class<?> getSetterPropertyType(Object object, String name) {
		Method setterMethod = getSetterMethod(object, name, null, true);
		return (setterMethod != null ? setterMethod.getParameterTypes()[0] : null);
	}

	public Exception setMethodProperty(Object object, String methodSpec, Object[] args) {
		Method method = getMethod(object, methodSpec);
		if (method != null) {
			try {
				Class<?>[] parameterTypes = method.getParameterTypes();
				for (int i = 0; i < args.length; i++) {
					args[i] = context.adapt(args[i], parameterTypes[i]);
				}
				method.invoke(object, args);
				return null;
			} catch (Exception e) {
				return e;
			}
		}
		return new NoSuchMethodException("Couldn't invoke method for " + methodSpec);
	}
	public Class<?> getMethodPropertyType(Object object, String methodSpec) {
		Method method = getMethod(object, methodSpec);
		return (method != null ? method.getParameterTypes()[0] : null);
	}

	public Exception setFieldProperty(Object object, String name, Object value) {
		Field field = getField(object, name, value, true);
		if (field != null) {
			try {
				value = context.adapt(value, field.getType());
				field.set(object, value);
				return null;
			} catch (Exception e) {
				return e;
			}
		}
		return new NoSuchFieldException("No field for " + name);
	}
	public Class<?> getFieldPropertyType(Object object, String name) {
		Field field = getField(object, name, null, true);
		return (field != null ? field.getType() : null);
	}

	public void setProperty(Object object, String name, Object value) throws Exception {
		Exception ex = setSetterProperty(object, name, value);
		if (ex != null) {
			ex = setFieldProperty(object, name, value);
		}
		if (ex != null) {
			throw ex;
		}
	}
	public Class<?> getPropertyType(Object object, String name) {
		Class<?> c = getSetterPropertyType(object, name);
		if (c == null) {
			c = getFieldPropertyType(object, name);
		}
		return c;
	}
}
