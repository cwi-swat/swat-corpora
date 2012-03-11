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
package org.eclipse.e4.emf.ecore.javascript;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.e4.emf.ecore.javascript.functions.AdaptTo;
import org.eclipse.e4.emf.ecore.javascript.functions.BindingApply;
import org.eclipse.e4.emf.ecore.javascript.functions.LoadEPackageFunction;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

public class JavascriptSupport extends WrapFactory {

	private Logger log = Logger.getLogger(Activator.PLUGIN_ID);

	public final static String JAVASCRIPT_EXTENSION = "js";

	private final URI ecoreJsUri = URI.createURI(String.valueOf(getClass().getResource("Ecore.js")));

	private URIConverter uriConverter;

	public static URI createParentFolderUri(URI uri) {
		String lastSegment = uri.lastSegment();
		return (lastSegment == null || lastSegment.length() == 0 ? uri : uri.trimSegments(1).appendSegment(""));
	}

	public void setUriConverter(URIConverter uriConverter) {
		initUriMap(uriConverter.getURIMap());
		this.uriConverter = uriConverter;
	}

	private void initUriMap(Map<URI, URI> uriMap) {
		if (Activator.getDefault() != null) {
			Activator.getDefault().addGenmodelUris(uriMap);
		}
		uriMap.put(createParentFolderUri(JavascriptSupport.ECORE_SCRIPT_URI), createParentFolderUri(ecoreJsUri));
	}

	public URIConverter getURIConverter() {
		if (uriConverter == null) {
			setUriConverter(new ExtensibleURIConverterImpl());
		}
		return uriConverter;
	}

	public final static String SCRIPTING_SOURCE_URI = "http://www.eclipse.org/e4/emf/ecore/javascript/source";
	public final static String SCRIPTING_SOURCE_FEATURE_URI = "http://www.eclipse.org/e4/emf/ecore/javascript/sourceFeature";
	public final static String SCRIPTING_EXTERNAL_SOURCE_URI = "http://www.eclipse.org/e4/emf/ecore/javascript/externalSource";

	private Context context;
	private ScriptableObject rootScope;

	public JavascriptSupport() {
		// force loading of EmfContextFactory class, to ensure it is set as
		// global ContextFactory
		this.context = EmfContextFactory.getEmfContextFactory().makeContext();
//		Context context = EmfContext.enter();
		context.setWrapFactory(this);
		// don't wrap String objects as JavaNativeObject, but use their JS equivalent
		setJavaPrimitiveWrap(false);
		URI ecoreJsUri = URI.createURI(String.valueOf(getClass().getResource("Ecore.js")));
		rootScope = createScope(ecoreJsUri);
		context.initStandardObjects(rootScope);
		initStandardObjects(rootScope);
	}

	private Context enterContext() {
		Context context = Context.enter(this.context);
		context.setWrapFactory(this);
		return context;
	}
	private void exitContext() {
		Context.exit();
	}

	public void setApplicationClassLoader(ClassLoader classLoader) {
		Context context = enterContext();
		context.setApplicationClassLoader(classLoader);
		exitContext();
	}
	
	private void initStandardObjects(Scriptable scope) {
		ScriptableObject.putProperty(scope, "out", System.out);
		ScriptableObject.putProperty(scope, "err", System.err);
		ScriptableObject.putProperty(scope, "loadEPackage", new LoadEPackageFunction(this));
		ScriptableObject.putProperty(scope, "adaptTo", new AdaptTo());
		ScriptableObject.putProperty(scope, "applyAsBinding", new BindingApply(null));
	}

	private Object evaluate(String script, String name, Scriptable scope, boolean rethrowException) {
		Context context = enterContext();
		Object result = null;
		try {
			result = context.evaluateString(scope, script, name, -1, null);
		} catch (RuntimeException re) {
			log.log(Level.SEVERE, "Exception when evaluating " + script + ": " + re, re);
			if (rethrowException) {
				throw re;
			}
		} finally {
			exitContext();
		}
		return unwrap(result);
	}

	public Object evaluate(String script, Object scope, boolean rethrowException) {
		return evaluate(script, null, (scope instanceof Scriptable ? (Scriptable)scope : getScope(scope)), rethrowException);
	}

	public Scriptable getScope(Object object) {
		if (object instanceof Resource) {
			return getResourceScope((Resource)object);
		} else if (object instanceof EObject) {
			return getResourceScope((EObject)object);
		} else {
			return rootScope;
		}
	}

	public Object wrap(Object object) {
		enterContext();
		try {
			return Context.javaToJS(object, getScope(object));
		} finally {
			exitContext();
		}
	}

	public Object unwrap(Object value) {
		return unwrapTo(value, Object.class);
	}

	public Object unwrapTo(Object value, Class<?> c) {
		enterContext();
		try {
			return Context.jsToJava(value, c != null ? c : Object.class);
		} finally {
			exitContext();
		}
	}

	public Scriptable newObject(String constructorName, Object args[]) {
		Context context = enterContext();
		Scriptable result = null;
		try {
			result = context.newObject(getScope(null), constructorName, args);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Exception when invoking newObject: " + e, e);
		} finally {
			exitContext();
		}
		return result;
	}

	public Object getVariable(Resource res, String name) {
		return unwrap(getVariable(name, getResourceScope(res)));
	}
	public Object getProperty(EObject eObject, String name) {
		return unwrap(getProperty(name, (Scriptable) wrap(eObject)));
	}
	public void setVariable(Resource res, String name, Object value) {
		setProperty(name, getResourceScope(res), wrap(value));
	}
	public void setProperty(EObject eObject, String name, Object value) {
		setProperty(name, (Scriptable)wrap(eObject), wrap(value));
	}

	private Object getProperty(String name, Scriptable scope) {
		return ScriptableObject.getProperty(scope, name);
	}
	private void setProperty(String name, Scriptable scope, Object value) {
		ScriptableObject.putProperty(scope, name, value);
	}
	private Object getVariable(String name, Scriptable scope) {
		Object result = (scope != null ? scope.get(name, scope) : null);
		if (result == Scriptable.NOT_FOUND) {
			result = getVariable(name, scope.getParentScope());
		}
		return result;
	}

	public Object callMethod(Object object, String methodName, Object[] args, boolean rethrowException) {
		return call(object, methodName, args, object, rethrowException);
	}
	public Object callMethod(Object object, String methodName, Collection<?> args, boolean rethrowException) {
		return call(object, methodName, args, object, rethrowException);
	}
	public Object callMethod(Object object, Function method, Collection<?> args, boolean rethrowException) {
		return call(object, method, args, object, rethrowException);
	}
	public Object callFunction(Resource res, String funName, Object[] args, boolean rethrowException) {
		return call(res, funName, args, null, rethrowException);
	}
	public Object callFunction(Resource res, String funName, Collection<?> args, boolean rethrowException) {
		return call(res, funName, args, null, rethrowException);
	}

	Object call(Object scopeObject, Object funObject, Object args, Object thisEObject, boolean rethrowException) {
		Context context = enterContext();
		Scriptable scope = getScope(scopeObject);
		Object result = null;
		try {
			Scriptable thisObject = scope;
			if (thisEObject instanceof Scriptable) {
				thisObject = (Scriptable)thisEObject;
			} else if (thisEObject != null) {
				thisObject = (Scriptable)wrap(thisEObject);
			}
			Object fun = funObject;
			if (fun instanceof String) {
				fun = (thisEObject != null ? getProperty((String)fun, thisObject) : getVariable((String)fun, scope));
			}
			if (fun instanceof Function) {
				result = ((Function) fun).call(context, scope, thisObject, wrap(args, context, scope));
			} else {
				log.log(Level.SEVERE, funObject + " not found for " + thisObject);
				result = Scriptable.NOT_FOUND;
			}
		} catch (RuntimeException re) {
			String objectRef = toString(thisEObject);
			log.log(Level.SEVERE, "Exception when calling " + funObject + " on " + objectRef + ": " + re, re);
			if (rethrowException) {
				throw re;
			}
		} finally {
			exitContext();
		}
		return unwrap(result);
	}
	
	String toString(Object thisObject) {
		String objectRef = "";
		if (thisObject instanceof EObject) {
			EObject thisEObject = (EObject)thisObject;
			objectRef = "a " + thisEObject.eClass().getName();
			EStructuralFeature nameFeature = thisEObject.eClass().getEStructuralFeature("name");
			if (nameFeature != null) {
				objectRef += " named " + thisEObject.eGet(nameFeature);
			}
		} else {
			String.valueOf(objectRef);
		}
		return objectRef;
	}

	private Object[] wrap(Object args, Context context, Scriptable scope) {
		if (args == null) {
			return new Object[0];
		}
		Object[] wrappedArgs = null;
		if (args.getClass().isArray()) {
			wrappedArgs = new Object[Array.getLength(args)];
			for (int i = 0; i < wrappedArgs.length; i++) {
				wrappedArgs[i] = wrap(context, scope, Array.get(args, i), Object.class);
			}
		} else if (args instanceof Collection<?>) {
			Collection<?> col = (Collection<?>)args;
			wrappedArgs = new Object[col.size()];
			int i = 0;
			for (Object element: col) {
				wrappedArgs[i++] = wrap(context, scope, element, Object.class);
			}
		}
		if (wrappedArgs == null) {
			throw new IllegalArgumentException("Cannot wrap args passed as " + args);
		}
		return wrappedArgs;
	}

	//

	public FunctionCall createFunctionCall(Resource res, String funName, Object[] args) {
		return new FunctionCall(res, funName, args);
	}

	public FunctionCall createMethodCall(EObject eObject, String methodName, Object[] args) {
		return new FunctionCall(eObject.eClass().eResource(), eObject, methodName, args);
	}

	public Object call(FunctionCall functionCall, boolean rethrowException) {
		return functionCall.call(this, rethrowException);
	}

	//

	public final static URI ECORE_URI = URI.createURI(EcorePackage.eINSTANCE.getNsURI());
	public final static URI ECORE_SCRIPT_URI = ECORE_URI.appendFileExtension(JavascriptSupport.JAVASCRIPT_EXTENSION);

	public NativeObject createScope(URI uri) {
		return createScope(uri.toString());
	}

	public NativeObject createScope(final String name) {
		return new NativeObject() {
			// useful when debugging
			private String scopeName = name;

			public String toString() {
				return "[JS + " + scopeName + "]";
			}
		};
	}

	//

	private NameSupport nameSupport = new NameSupport(this);

	EStructuralFeature getNameFeature(EObject eObject) {
		return nameSupport.getNameFeature(eObject, false);
	}

	public String getName(EObject eObject) {
		return nameSupport.getName(eObject, false);
	}
	public boolean hasName(String name, EObject eObject) {
		return nameSupport.hasName(name, eObject, false);
	}
	public String getNamePropertyName(EObject eObject) {
		return nameSupport.getNamePropertyName(getName(eObject));
	}

	//

	private Map<URI, Script> resourceScripts = new HashMap<URI, Script>();

	private Scriptable getResourceScope(EObject eObject) {
		return getResourceScope(eObject.eResource());
	}

	public Scriptable getResourceScope(Resource res) {
		if (res == null) {
			return rootScope;
		}
		Scriptable scope = getResourceScope(res.getURI());
		if (!(scope.get("resource", scope) instanceof Function)) {
			defineConstantFunction("resource", res, null, scope);
			nameSupport.handleResource(res);
		}
		// scope.put("contents", scope, createConstantFunction("contents",
		// res.getContents()));
		return scope;
	}

	public void defineConstantFunction(String name, Object constant, String property, Scriptable scope) {
		scope.put(name, scope, createConstantFunction(name, constant, property));
	}

	private Scriptable getResourceScope(URI uri) {
		if (uri == null) {
			return rootScope;
		}
		Script script = getScript(uri, null);
		return script.scope;
	}

	private ScriptClassLoader scriptClassLoader = null;

	Script getScript(URI uri, Scriptable scope) {
		// only reuse scripts that provided their own scope
		Script script = (scope == null ? (Script) resourceScripts.get(uri) : null);
		if (script == null) {
			URI scriptUri = uri.trimFileExtension().appendFileExtension(JavascriptSupport.JAVASCRIPT_EXTENSION);
			if (scope == null) {
				scope = (scriptUri.equals(JavascriptSupport.ECORE_SCRIPT_URI) ? rootScope : createScope(scriptUri));
			}
			if (scope != rootScope) {
				scope.setParentScope(getResourceScope(ECORE_URI));
			}
			script = new Script(scriptUri, scope);
			if (scriptClassLoader == null) {
				Context context = enterContext();
				scriptClassLoader = new ScriptClassLoader(context.getApplicationClassLoader());
				exitContext();
			}
			script.loadScript(getURIConverter(), scriptClassLoader);
			resourceScripts.put(uri, script);
			resourceScripts.put(scriptUri, script);
		} else if (script.shouldLoadScript()) {
			script.loadScript(getURIConverter(), scriptClassLoader);
		}
		return script;
	}

	private Function createConstantFunction(final String name, final Object constant, final String property) {
		return new BaseFunction() {
			public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
				Object result = wrap(constant);
				if (property != null && result instanceof Scriptable) {
					result = ((Scriptable) result).get(property, (Scriptable)result);
				}
				return result;
			}

			public int getArity() {
				return 0;
			}

			public String getFunctionName() {
				return name;
			}
		};
	}

	private abstract class InstancePrototype extends ScriptableObject {
		public Object[] getIds() {
			Object[] ids1 = super.getIds();
			Object[] ids2 = getPrototype().getIds();
			Object[] ids = new Object[ids1.length + ids2.length];
			System.arraycopy(ids1, 0, ids, 0, ids1.length);
			System.arraycopy(ids2, 0, ids, ids1.length, ids2.length);
			return ids;
		}
	}

	private Map<Object, Scriptable> classPrototypes = new IdentityHashMap<Object, Scriptable>();
	private Map<Object, Scriptable> instancePrototypes = new IdentityHashMap<Object, Scriptable>();

	void initWrapper(JavascriptSupportWrapper wrapper, Scriptable scope, final Object wrappedObject, EClassifier prototypeClass) {
		// must make sure we reuse the wrapper and avoid infinite recursion
		wrappers.put(wrappedObject, wrapper);
		Scriptable prototype = instancePrototypes.get(wrappedObject);
		if (prototype == null) {
			prototype = new InstancePrototype() {
				public String toString() {
					return JavascriptSupportWrapper.toString(
							"JSPrototypeWrapper", wrappedObject);
				}
				public String getClassName() {
					return "JSPrototypeWrapper";
				}
			};
			instancePrototypes.put(wrappedObject, prototype);
			
			prototype.setPrototype(getPrototype(prototypeClass));
			prototype.setParentScope(scope);
			if (wrappedObject instanceof EObject) {
				EObject eObject = (EObject) wrappedObject;
				evaluateInstanceScript(eObject, prototype);
			}
			wrapper.setPrototype(prototype);
			Object initFun = ScriptableObject.getProperty(wrapper, "init");
			if (initFun instanceof Function) {
				Context context = enterContext();
				try {
					Object[] initFunArgs = {};
					((Function)initFun).call(context, scope, wrapper, initFunArgs);
				} catch (RuntimeException e) {
					log.log(Level.SEVERE, "Exception when calling init() on " + wrappedObject + ": " + e, e);
				} finally {
					exitContext();
				}
			}
		}
	}

	public static EAttribute getScriptSourceAttribute(EObject eObject, String lang) {
		for (Iterator<EAttribute> attributes = eObject.eClass().getEAllAttributes().iterator(); attributes.hasNext();) {
			final EAttribute attrFeature = attributes.next();
			String sourceAttrAnnotation = getAnnotation(attrFeature, JavascriptSupport.SCRIPTING_SOURCE_FEATURE_URI, lang, null);
			if (sourceAttrAnnotation != null) {
				return attrFeature;
			}
		}
		return null;
	}

	private void evaluateInstanceScript(final EObject eObject,
			final Scriptable scope) {
		for (Iterator<EAttribute> attributes = eObject.eClass().getEAllAttributes().iterator(); attributes.hasNext();) {
			final EAttribute attrFeature = attributes.next();
			String sourceAttrAnnotation = getAnnotation(attrFeature, JavascriptSupport.SCRIPTING_SOURCE_FEATURE_URI, JavascriptSupport.JAVASCRIPT_EXTENSION, null);
			if (sourceAttrAnnotation == null) {
				continue;
			}
			if (sourceAttrAnnotation.contains("eval")) {
				evaluateInstanceScript(eObject, attrFeature, scope);
			}
			// listen to changes to the scripting source and re-evaluate
			if (sourceAttrAnnotation.contains("listen")) {
				eObject.eAdapters().add(new AdapterImpl() {
					public void notifyChanged(Notification notification) {
						if (notification.getFeature() == attrFeature) {
							evaluateInstanceScript(eObject, attrFeature, scope);
						}
					}
				});
			}
		}
	}
	private void evaluateInstanceScript(EObject eObject, EAttribute attrFeature, Scriptable scope) {
		Object attrValue = eObject.eGet(attrFeature);
		if (attrValue != null) {
			evaluate(String.valueOf(attrValue), attrFeature.getName(), scope, true);
		}
	}

	private Scriptable loadPrototypeScript(EClassifier prototypeClass, Scriptable scope) {
		URI prototypeScriptUri = null;
		String sourceUri = getAnnotation(prototypeClass, JavascriptSupport.SCRIPTING_EXTERNAL_SOURCE_URI, JavascriptSupport.JAVASCRIPT_EXTENSION, null);
		if (sourceUri != null) {
			prototypeScriptUri = URI.createURI(sourceUri);
		}
		if (prototypeScriptUri == null) {
			prototypeScriptUri = getEClassifierUri(prototypeClass);
		}
		Script script = getScript(prototypeScriptUri, scope);
		return script.scope;
	}

	private Scriptable getPrototype(List<EClass> prototypeClasses) {
		// single inheritance
		if (prototypeClasses.size() == 1) {
			return getPrototype(prototypeClasses.get(0));
		}
		Scriptable prototype = (Scriptable)classPrototypes.get(prototypeClasses);
		if (prototype == null) {
			// create a new scope for this set of classes
			prototype = createScope(prototypeClasses.toString());
			for (EClassifier prototypeClass: prototypeClasses) {
				prototype = loadPrototypeScript(prototypeClass, prototype);
			}
			EClassifier prototypeClass = prototypeClasses.get(0);
			if (prototypeClass instanceof EClass) {
				initEClassPrototype(prototype, (EClass)prototypeClass);
			}
			classPrototypes.put(prototypeClasses, prototype);
		}
		return prototype;
	}

	public Scriptable getPrototype(EClassifier prototypeClass) {
		Scriptable prototype = (Scriptable) classPrototypes.get(prototypeClass);
		if (prototype == null) {
			prototype = loadPrototypeScript(prototypeClass, null);
			classPrototypes.put(prototypeClass, prototype);
			if (prototypeClass instanceof EClass) {
				EClass prototypeEClass = (EClass)prototypeClass;
				initEClassPrototype(prototype, prototypeEClass);
				addEPackageVariable(prototypeClass);
			}
		}
		return prototype;
	}

	private void addEPackageVariable(EClassifier prototypeClass) {
		EPackage ePack = prototypeClass.getEPackage();
		addEPackageVariable(ePack);
	}

	public EPackage loadEPackage(String packageUri, String schemaUri) {
		EPackage ePack = getEPackage(packageUri, schemaUri);
		if (ePack == null) {
			throw new IllegalArgumentException("No package with URI " + packageUri + (schemaUri != null ? " @ " + schemaUri : "") + " found");
		}
		if (schemaUri != null) {
			registerSchemaUri(ePack, schemaUri);
		}
		addEPackageVariable(ePack);
		return ePack;
	}
	private void registerSchemaUri(EPackage ePack, String schemaUriString) {
		URI packageUri = URI.createURI(ePack.getNsURI());
		URI schemaUri = getURIConverter().normalize(schemaUriString != null ? URI.createURI(schemaUriString) : packageUri);
		if (schemaUri != null && (! schemaUri.equals(packageUri))) {
			getURIConverter().getURIMap().put(createParentFolderUri(packageUri), createParentFolderUri(schemaUri));
		}
	}

	private ResourceSet packagesResourceSet = null;
	
	private EPackage getEPackage(String packageUriString, String schemaUriString) {
		Registry ePackageRegistry = EPackage.Registry.INSTANCE;
		EPackage ePack = ePackageRegistry.getEPackage(packageUriString);
		if (ePack != null) {
			return ePack;
		}
		if (packagesResourceSet == null) {
			packagesResourceSet = new ResourceSetImpl();
			packagesResourceSet.setURIConverter(getURIConverter());
		}
		for (Resource packageResource: packagesResourceSet.getResources()) {
			ePack = getResourcePackage(packageResource, packageUriString);
			if (ePack != null) {
				return ePack;
			}
		}
		URI packageUri = URI.createURI(packageUriString);
		URI schemaUri = getURIConverter().normalize(schemaUriString != null ? URI.createURI(schemaUriString) : packageUri);
		Resource packageResource = packagesResourceSet.getResource(schemaUri, true);
		ePack = getResourcePackage(packageResource, packageUriString);
		if (ePack != null) {
			registerPackage(ePack, schemaUriString);
		}
		return ePack;
	}

	public void registerPackage(EPackage ePack, String schemaUri) {
		Registry ePackageRegistry = EPackage.Registry.INSTANCE;
		ePackageRegistry.put(ePack.getNsURI(), ePack);
		registerSchemaUri(ePack, schemaUri);
	}

	private EPackage getResourcePackage(Resource packageResource, String packageUri) {
		if (packageResource != null && packageResource.getContents().size() > 0) {
			EObject eObject = packageResource.getContents().get(0);
			if (eObject instanceof EPackage) {
				EPackage ePack = (EPackage)eObject;
				if (packageUri == null || packageUri.equals(ePack.getNsURI())) {
					return ePack;
				}
			}
		}
		return null;
	}
	
	private void addEPackageVariable(EPackage ePack) {
		String packVariableName = getNamePropertyName(ePack);
		if (packVariableName != null && getVariable(null, packVariableName) == null) {
			// indicate loading state to prevent recursive call
			setVariable(null, packVariableName, ePack.getNsURI());
			// start loading
			setVariable(null, packVariableName, ePack);
		}
	}

	private String getScriptSourceCodeAnnotation(EOperation op) {
		return getAnnotation(op,
				JavascriptSupport.SCRIPTING_SOURCE_URI,
				JavascriptSupport.JAVASCRIPT_EXTENSION, null);
	}

	private static String getAnnotation(EModelElement modelElement, String uri, String key, String def) {
		String annotation = EcoreUtil.getAnnotation(modelElement, uri, key);
		return annotation != null && annotation.trim().length() > 0 ? annotation : def;
	}

	//	private EOperation getScriptOperation(EClass eClass, String opName) {
	//		for (EOperation op: eClass.getEOperations()) {
	//			if (opName == null || opName.equals(op.getName())) {
	//				if (getScriptSourceCodeAnnotation(op) != null) {
	//					return op;
	//				}
	//			}
	//		}
	//		return null;
	//	}

	protected void initEClassPrototype(Scriptable prototype, EClass prototypeClass) {
		addEOperationFunctions(prototype, prototypeClass);
		if (prototypeClass != EcorePackage.eINSTANCE.getEObject()) {
			List<EClass> superClasses = prototypeClass.getESuperTypes();
			if (superClasses.size() == 0) {
				superClasses = Collections.singletonList(EcorePackage.eINSTANCE.getEObject()); // EcorePackage.eINSTANCE.getEClass()
			}
			Scriptable prototype2 = getPrototype(superClasses);
			if (prototype2 == prototype) {
				System.err.println("Circular prototype chain!!!");
			}
			prototype.setPrototype(prototype2);
		}
		prototype.setParentScope(getResourceScope(prototypeClass));
	}

	private void addEOperationFunctions(Scriptable prototype, EClass prototypeClass) {
		for (EOperation op: prototypeClass.getEOperations()) {
			String source = getScriptSourceCodeAnnotation(op);
			if (source != null) {
				defineEClassifierOwnedFunction(op.getEContainingClass(), op.getName(), op.getEParameters().iterator(), source, prototype);
			}
		}
	}

	protected URI getEClassifierUri(EClassifier prototypeClass) {
		URI uri = URI.createURI(prototypeClass.getEPackage().getNsURI());
		if (ECORE_URI.equals(uri)) {
			uri = JavascriptSupport.ECORE_SCRIPT_URI;
		}
		return uri.trimSegments(1).appendSegment(prototypeClass.getName());
	}

	public String functionSource(String name, Iterator<?> params, String source) {
		if (!source.startsWith("function ")) {
			if (!source.startsWith("{")) {
				source = "{\n" + source + "\n}";
			}
			String argList = "";
			int i = 0;
			while (params != null && params.hasNext()) {
				Object param = params.next();
				String paramName = null;
				if (param instanceof String) {
					paramName = (String)param;
				} else if (param instanceof ENamedElement) {
					paramName = ((ENamedElement)param).getName();
				} else {
					param = "p" + i;
				}
				if (argList.length() == 0) {
					argList = paramName;
				} else {
					argList += "," + paramName;
				}
				i++;
			}
			source = "function " + name + "(" + argList + ")" + source;
		}
		return source;
	}

	public BaseFunction defineEClassifierOwnedFunction(EClassifier owner, String name, Iterator<?> params, String source, Scriptable scope) {
		if (scope == null) {
			scope = getPrototype(owner);
		}
		try {
			Context context = enterContext();
			String def = functionSource(name, params, source);
			context.evaluateString(scope, def, name, -1, null);
			Object fun = scope.get(name, scope);
			if (fun instanceof BaseFunction) {
				return (BaseFunction) fun;
			} else {
				throw new IllegalArgumentException(source + " evaluated to "
						+ fun + ", which is not a BaseFunction");
			}
		} finally {
			exitContext();
		}
	}

	protected class CurriedFunction extends BaseFunction {

		private BaseFunction fun;
		private EObject arg1;

		public CurriedFunction(Scriptable scope, BaseFunction fun, EObject arg1) {
			super(scope, ScriptableObject.getFunctionPrototype(scope));
			this.fun = fun;
			this.arg1 = arg1;
		}

		public Object call(Context cx, Scriptable scope, Scriptable thisObj,
				Object[] args) {
			Object[] realArgs = new Object[1 + args.length];
			realArgs[0] = arg1;
			System.arraycopy(args, 0, realArgs, 1, args.length);
			return fun.call(cx, scope, thisObj, realArgs);
		}

		public int getArity() {
			return fun.getArity() - 1;
		}
	}

	private Map<Object, Scriptable> wrappers = new IdentityHashMap<Object, Scriptable>();

	public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
		Scriptable wrapper = wrappers.get(javaObject);
		if (wrapper != null) {
			return wrapper;
		}
		if (javaObject instanceof EObject) {
			wrapper = new EObjectWrapper(this, scope, (EObject) javaObject, staticType);
		} else if (javaObject instanceof Resource) {
			wrapper = new ResourceWrapper(this, scope, (Resource) javaObject, staticType);
		} else if (javaObject instanceof ResourceSet) {
			wrapper = new ResourceSetWrapper(this, scope, (ResourceSet) javaObject, staticType);
		} else if (javaObject instanceof List<?>) {
			wrapper = new ListWrapper(this, scope, (List<?>) javaObject, staticType);
		} else if (javaObject instanceof Map<?, ?>) {
			wrapper = new MapWrapper(this, scope, (Map<?, ?>) javaObject, staticType);
		} else if (javaObject instanceof EMap<?, ?>) {
			wrapper = new MapWrapper(this, scope, (EMap<?, ?>) javaObject, staticType);
		} else {
			return super.wrapAsJavaObject(cx, scope, javaObject, staticType);
		}
		return wrapper;
	}

	//

	public JavascriptNotificationSupport supportNotifications(Notifier notifier) {
		return new JavascriptNotificationSupport(this, notifier);
	}
}
