package org.eclipse.e4.emf.ecore.delegates.javascript;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;

public class JavascriptDelegateFactory extends JavascriptSupportFactory implements
	EStructuralFeature.Internal.SettingDelegate.Factory,
	EOperation.Internal.InvocationDelegate.Factory
	{

	public SettingDelegate createSettingDelegate(EStructuralFeature structuralFeature) {
		return new JavascriptSettingDelegate(structuralFeature);
	}

	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		return new JavascriptInvocationDelegate(operation);
	}
}
