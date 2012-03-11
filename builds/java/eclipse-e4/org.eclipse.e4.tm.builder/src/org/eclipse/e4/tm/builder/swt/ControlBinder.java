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
package org.eclipse.e4.tm.builder.swt;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.eclipse.e4.tm.builder.IBinderContext;
import org.eclipse.e4.tm.styles.Style;
import org.eclipse.e4.tm.styles.StyleItem;
import org.eclipse.e4.tm.styles.StyleSelector;
import org.eclipse.e4.tm.styles.Styled;
import org.eclipse.e4.tm.widgets.AbstractComposite;
import org.eclipse.e4.tm.widgets.Composite;
import org.eclipse.e4.tm.widgets.Control;
import org.eclipse.e4.tm.widgets.WidgetsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class ControlBinder extends SwtBinder implements Listener {

	public <T> T adapt(Object value, Class<T> c) {
		if (value instanceof Widget) {
			Widget widget = (Widget)value;
			for (Class<?> sc = c; sc != null && sc != Object.class; sc = sc.getSuperclass()) {
				Object o = widget.getData(sc.getName());
				if (c.isInstance(o)) {
					return (T)o;
				}
			}
		}
		return super.adapt(value, c);
	}

	private final static String VIEWER_CONTROL_DATA_KEY = "viewer";

	public void dispose(EObject eObject, Object object, IBinderContext context) {
		if (object instanceof org.eclipse.swt.widgets.Control) {
			org.eclipse.swt.widgets.Control swtControl = (org.eclipse.swt.widgets.Control)object;
			swtControl.dispose();
		}
		super.dispose(eObject, object, context);
	}

	private final static Class<?>[] constructorParameterTypes = new Class[]{org.eclipse.swt.widgets.Composite.class, int.class};

	protected int getSwtStyle(String styleString) {
		int style = SWT.NONE;
		StringTokenizer styles = SwtBuilder.getSeparatedTokens(styleString);
		while (styles.hasMoreTokens()) {
			style |= SwtBuilder.getStaticField(SWT.class, styles.nextToken(), Integer.class, SWT.NONE);
		}
		return style;
	}

	protected int getSwtStyle(EObject control) {
		String styleString = getClassAnnotation(control);
		return getSwtStyle(styleString);
	}

//	protected boolean shouldntHandleFeature(EStructuralFeature feature) {
//		if (feature == StylesPackage.eINSTANCE.getStyled_Style()) {
//			return true;
//		}
//		return super.shouldntHandleFeature(feature);
//	}

	protected void update(EObject control, EStructuralFeature feature, Object object, boolean isInit) {
		super.update(control, feature, object, isInit);
		if (control instanceof AbstractComposite && feature == WidgetsPackage.eINSTANCE.getAbstractComposite_Controls()) {
			org.eclipse.swt.widgets.Composite swtComposite = context.getObject(control, org.eclipse.swt.widgets.Composite.class);
			if (swtComposite != null) {
				swtComposite.layout();
			}
		}
		if (isInit) {
			updateStyle(control, object, context);
		}
	}

	protected void invalidateFeature(EObject eObject, EStructuralFeature feature, Object object, boolean isEvent) {
		if (eObject instanceof AbstractComposite && feature == WidgetsPackage.eINSTANCE.getAbstractComposite_Controls()) {
			AbstractComposite<? extends Control> composite = (AbstractComposite<? extends Control>)eObject;
			org.eclipse.swt.widgets.Composite swtComposite = context.getObject(composite, org.eclipse.swt.widgets.Composite.class);
			if (swtComposite != null) {
				org.eclipse.swt.widgets.Control[] swtControls = swtComposite.getChildren();
				for (int i = 0; i < swtControls.length; i++) {
					swtControls[i].dispose();
				}
			}
			for (Iterator<? extends Control> it = composite.getControls().iterator(); it.hasNext();) {
				Control control = it.next();
				context.dispose(control);
			}
		}
		super.invalidateFeature(eObject, feature, object, isEvent);
	}

	public final static String CONTROL_DATA_URI_KEY = "modelUri";

	protected Object create(EObject control) {
		int style = getSwtStyle(control);
		Class<?> swtClass = getToolkitClass(control, true);
		org.eclipse.swt.widgets.Composite swtComposite = getParent(control, org.eclipse.swt.widgets.Composite.class);
		if (swtComposite != null && swtComposite.getLayout() == null) {
			setDefaultLayout(swtComposite);
		}
		Object swtObject = null;
		try {
			Object[] constructorArguments = new Object[]{swtComposite, style};
			swtObject = swtClass.getConstructor(ControlBinder.constructorParameterTypes).newInstance(constructorArguments);
			if (swtObject instanceof org.eclipse.swt.widgets.Control) {
				org.eclipse.swt.widgets.Control swtControl = (org.eclipse.swt.widgets.Control)swtObject;
				swtControl.setData(CONTROL_DATA_URI_KEY, EcoreUtil.getURI(control));
			}
		} catch (Exception e) {
			System.err.println("Exception when creating widget for " + this + ": " + e);
		}
		return swtObject;
	}

	protected void handleEventFeature(final EObject control, final EStructuralFeature feature, Object object) {
		if (! (object instanceof org.eclipse.swt.widgets.Control)) {
			return;
		}
		org.eclipse.swt.widgets.Control swtControl = (org.eclipse.swt.widgets.Control)object;
		String name = getRealName(feature);
		int eventType = SwtBuilder.getStaticField(SWT.class, name, Integer.class, SWT.NONE);
		swtControl.addListener(eventType, this);
		//		new Listener() {
		//			public void handleEvent(Event event) {
		//				//					System.out.println("Event: " + event);
		//				control.eSet(feature, event);
		//			}
		//		});
	}

	public void handleEvent(Event event) {
		EObject control = context.getEObject(event.widget);
		if (control == null) {
			return;
		}
		// find out which feature this event is for
		for (Iterator<EAttribute> it = control.eClass().getEAllAttributes().iterator(); it.hasNext();) {
			EStructuralFeature feature = it.next();
			if ("event".equals(getAccessMethod(feature, control.eClass()))) {
				String name = getRealName(feature);
				if (event.type == SwtBuilder.getStaticField(SWT.class, name, Integer.class, SWT.NONE)) {
					control.eSet(feature, event);
				}
			}
		}
	}

	public boolean validateFeature(EObject eObject, Object object, String featureName, IBinderContext context) {
		if ("Style".equals(featureName) && eObject instanceof Styled) {
			updateStyle(eObject, object, context);
			return true;
		}
		return super.validateFeature(eObject, object, featureName, context);
	}

	public void updateStyle(EObject eObject, Object object, IBinderContext context) {
		if (eObject instanceof Styled && object instanceof org.eclipse.swt.widgets.Control) {
			updateStyle((Styled)eObject, ((org.eclipse.swt.widgets.Control)object), context);
		}
	}

	protected void updateStyle(Styled control, org.eclipse.swt.widgets.Control swtControl, IBinderContext context) {
		Style style = control.getStyle();
		if (style != null) {
			for (StyleSelector styleSelector: style.getStyleSelectors()) {
				applyStyleSelector(style, styleSelector, swtControl);
			}
		}
		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getControls()) {
				context.updateStyle(child);
			}
		}
	}

	private void applyStyleSelector(Style style, StyleSelector styleSelector, org.eclipse.swt.widgets.Control object) {
		String property = styleSelector.getProperty();
		String selector = styleSelector.getSelector();
		Object value = getStyleValue(style.eContainer(), selector);
		try {
			context.setProperty(object, property, value);
		} catch (Exception e) {
		}
	}

	private Object getStyleValue(EObject eObject, String selector) {
		while (eObject != null) {
			if (eObject instanceof Styled) {
				Style style = ((Styled)eObject).getStyle();
				if (style != null) {
					Object value = getStyleValue(style, selector);
					if (value != null) {
						return value;
					}
				}
			}
			eObject = eObject.eContainer();
		}
		return null;
	}
	
	private Object getStyleValue(Style style, String selector) {
		for (StyleItem styleItem: style.getStyleItems()) {
			if (selector.equals(styleItem.getName())) {
				Class c = getToolkitClass(styleItem, true);
				Object value = context.getObject(styleItem, c);
				if (value == null) {
					try {
						value = context.convert(styleItem.getSource(), c);
						context.putObject(styleItem, value);
					} catch (Exception e) {
					}
				}
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}
}
