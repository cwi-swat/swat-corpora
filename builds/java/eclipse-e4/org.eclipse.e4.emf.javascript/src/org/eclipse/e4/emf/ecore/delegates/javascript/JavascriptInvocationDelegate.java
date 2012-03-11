package org.eclipse.e4.emf.ecore.delegates.javascript;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class JavascriptInvocationDelegate extends BasicInvocationDelegate implements InvocationDelegate {

	public JavascriptInvocationDelegate(EOperation operation) {
		super(operation);
	}

	static Object dynamicInvoke(EClass owner, EModelElement modelElement, String key, String opName, Iterator<?> params, EObject target, EList<?> arguments, JavascriptSupport javascriptSupport) throws InvocationTargetException {
		Scriptable ownerPrototype = javascriptSupport.getPrototype(owner);
		Object ownerFunction = ownerPrototype.get(opName, ownerPrototype);
		if (! (ownerFunction instanceof Function)) {
			if (key == null) {
				key = JavascriptSupport.JAVASCRIPT_EXTENSION;
			}
			String operationBody = EcoreUtil.getAnnotation(modelElement, JavascriptSupport.SCRIPTING_SOURCE_URI, key);
			ownerFunction = javascriptSupport.defineEClassifierOwnedFunction(owner, opName, params, operationBody, null);
		}
		return javascriptSupport.callMethod(target, (Function)ownerFunction, arguments, true);
	}

	static Object dynamicInvoke(EClass owner, String opName, Iterator<?> params, String operationBody, EObject target, EList<?> arguments, JavascriptSupport javascriptSupport) throws InvocationTargetException {
		if (! (javascriptSupport.getProperty(target, opName) instanceof Function)) {
			javascriptSupport.defineEClassifierOwnedFunction(owner, opName, params, operationBody, null);
		}
		return javascriptSupport.callMethod(target, opName, arguments, true);
	}

	static Object dynamicInvoke(EDataType owner, String opName, Iterator<?> params, String operationBody, List<?> arguments, JavascriptSupport javascriptSupport) throws InvocationTargetException {
		Scriptable ownerPrototype = javascriptSupport.getPrototype(owner);
		if (! (ownerPrototype.get(opName, ownerPrototype) instanceof Function)) {
			javascriptSupport.defineEClassifierOwnedFunction(owner, opName, params, operationBody, null);
		}
		return javascriptSupport.callMethod(ownerPrototype, opName, arguments, true);
	}

	public Object dynamicInvoke(InternalEObject target, EList<?> arguments) throws InvocationTargetException {
		return dynamicInvoke(eOperation.getEContainingClass(), eOperation, null, eOperation.getName(), eOperation.getEParameters().iterator(), target, arguments, getJavascriptSupport());
	}

	private JavascriptSupport getJavascriptSupport() {
		return JavascriptDelegateFactory.getJavascriptSupportFactory().getJavascriptSupport();
	}
}
