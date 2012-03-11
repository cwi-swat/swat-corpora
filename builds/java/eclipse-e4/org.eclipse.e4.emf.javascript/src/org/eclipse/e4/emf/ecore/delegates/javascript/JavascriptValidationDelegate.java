package org.eclipse.e4.emf.ecore.delegates.javascript;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.emf.ecore.javascript.JavascriptSupport;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EValidator.ValidationDelegate;

public class JavascriptValidationDelegate implements ValidationDelegate {

	private JavascriptSupport getJavascriptSupport() {
		return JavascriptDelegateFactory.getJavascriptSupportFactory().getJavascriptSupport();
	}

	protected boolean booleanValue(Object value) {
		return Boolean.TRUE.equals(value);
	}

	/*
	 * from https://bugs.eclipse.org/bugs/show_bug.cgi?id=255786#c19
EPackage : ?
  EAnnotation : Ecore
    validationDelegates-><uri1> <uri2>

EClass : ?
  EAnnotation : Ecore
    constraints-><c1> <c2>
  EAnnotation : <uri1>
    <c1>-><body1>
    <c2>-><body2>

EOperation : ?
  EAnnotation : <uri2>
    body-><body>
	 */

	// invariant, defined by means of an annotated EOperation
	public boolean validate(EClass eClass, EObject eObject, Map<Object, Object> context, EOperation invariant, String expression) {
		try {
			Object result = JavascriptInvocationDelegate.dynamicInvoke(eClass, invariant.getName(), invariant.getEParameters().iterator(), expression, eObject, null, getJavascriptSupport());
			return booleanValue(result);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Couldn't validate invariant " + invariant.getName(), e);
		}
	}

	// constraint, defined by means of annotation on eClass
	public boolean validate(EClass eClass, EObject eObject, Map<Object, Object> context, String constraint, String expression) {
		try {
			Object result = JavascriptInvocationDelegate.dynamicInvoke(eClass, eClass, constraint, "_check_" + constraint, null, eObject, null, getJavascriptSupport());
			return booleanValue(result);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Couldn't validate invariant " + constraint, e);
		}
	}

	// constraint, defined by means of annotation on eDatatype
	public boolean validate(EDataType eDataType, Object value, Map<Object, Object> context, String constraint, String expression) {
		List<?> params = Collections.singletonList(eDataType.getName());
		try {
			Object result = JavascriptInvocationDelegate.dynamicInvoke(eDataType, "_check_" + constraint, params.iterator(), expression, Collections.singletonList(value), getJavascriptSupport());
			return booleanValue(result);		
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Couldn't validate invariant " + constraint, e);
		}
//		return booleanValue(getJavascriptSupport().evaluate(expression, value, true));
	}
}
