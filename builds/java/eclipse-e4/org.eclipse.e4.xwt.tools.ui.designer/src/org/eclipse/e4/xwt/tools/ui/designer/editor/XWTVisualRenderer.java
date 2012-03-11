/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.editor;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.AbstractRenderer;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.SashUtil;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTProxy;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Widget;

/**
 * @author bo.zhou
 * @author jliu
 */
public class XWTVisualRenderer extends AbstractRenderer {
	private final XWTProxy proxy;

	public XWTVisualRenderer(IFile file, XamlDocument document) {
		super(file, document);
		if (document == null) {
			throw new RuntimeException("XamlDocument is null");
		}

		document.eAdapters().add(new AdapterImpl() {
			public void notifyChanged(Notification msg) {
				recreate();
			}
		});
		proxy = new XWTProxy(file);
	}

	public Object getVisual(EObject model, boolean loadOnDemand) {
		return proxy.getComponent(model, loadOnDemand);
	}

	public Object getVisual(EObject model) {
		return proxy.getComponent((XamlNode) model);
	}

	/**
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public Result updateControls(Notification msg) {
		if (proxy.isDisposed() || msg.isTouch()) {
			return null;
		}
		Object notifier = msg.getNotifier();
		Object oldValue = msg.getOldValue();
		Object newValue = msg.getNewValue();

		if (notifier == null || (oldValue == null && newValue == null)) {
			return null;
		}
		if ((oldValue != null && oldValue != newValue)
				|| (oldValue == null && newValue != null)) {
			return applyNewValue(msg);
		}
		return null;
	}

	private boolean canRender(XamlNode node) {
		if (node == null) {
			return false;
		}
		String name = node.getName();
		if (IConstants.XWT_X_NAMESPACE.equals(node.getNamespace())) {
			return false;
		}
		try {
			IMetaclass metaclass = XWT.getMetaclass(Character.toUpperCase(name
					.charAt(0))
					+ name.substring(1), node.getNamespace());
			if (metaclass == null) {
				return false;
			}
			Class<?> type = metaclass.getType();
			return Widget.class.isAssignableFrom(type)
					|| Viewer.class.isAssignableFrom(type);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Update controls from model.
	 */
	private Result applyNewValue(Notification msg) {

		// For building Result.
		boolean updated = false;
		XamlElement updateObj = null;

		Object notifier = msg.getNotifier();
		Object newValue = msg.getNewValue();
		Object oldValue = msg.getOldValue();

		if (notifier instanceof XamlDocument && newValue instanceof XamlElement) {
			proxy.doCreate((XamlElement) newValue);
			return new Result(notifier, true);
		}

		EObject notifyEObject = (EObject) notifier;
		Object parent = proxy.getComponent(notifyEObject);
		while (parent == null) {
			notifier = notifyEObject;
			notifyEObject = notifyEObject.eContainer();
			if (notifyEObject == null) {
				break;
			}
			parent = proxy.getComponent(notifyEObject);
		}
		
		if (notifyEObject != null && notifyEObject instanceof XamlElement) {
			updateObj = (XamlElement) notifyEObject;
		}
		switch (msg.getEventType()) {
		case Notification.ADD:
			if (proxy.isNull(parent)) {
				break;
			}
			// in case of SashFrom 
			if (parent instanceof SashForm) {
				XamlAttribute attribute = updateObj.getAttribute("weights", IConstants.XWT_NAMESPACE);
				if (attribute != null) {
					String value = attribute.getValue();
					if (value != null && value.length() > 0 ) {
						int[] segments = SashUtil.toWeights(value);
						
						SashForm sashForm = (SashForm) parent;
						Control[] children = sashForm.getChildren();
						int c = 0;
						for (int i = 0; i < children.length; i++) {
							if (children[i] instanceof Sash) {
								c++;
							}
						}
						if (children.length > 0) {
							c++;
						}
						if (c != segments.length) {
							value = SashUtil.updateWeightsLengh(segments, c);
							attribute.setValue(value);
						}
					}
				}
			}
			
			if (newValue instanceof XamlElement && parent != null
					&& canRender((XamlElement) newValue)) {
				if (parent instanceof Widget) {
					updated = proxy.recreate((Widget) parent);
					updateObj = null;// If updated, we can refresh all visuals.
				}
			} else if (notifier instanceof XamlAttribute) {
				XamlAttribute attribute = (XamlAttribute) notifier;
				updated = applyAttribute(parent, updateObj, attribute);
			} else if (newValue instanceof XamlAttribute) {
				XamlAttribute attribute = (XamlAttribute) newValue;
				updated = applyAttribute(parent, updateObj, attribute);
			}
			break;
		case Notification.SET:
		case Notification.UNSET:
			if (proxy.isNull(parent)) {
				break;
			}
			if (notifier instanceof XamlAttribute) {
				XamlAttribute attribute = (XamlAttribute) notifier;
				updated = applyAttribute(parent, updateObj, attribute);
				if (updateObj != null && parent instanceof Item) {
					EObject eContainer = updateObj.eContainer();
					if (eContainer != null && eContainer instanceof XamlElement) {
						updateObj = (XamlElement) eContainer;
					}
				}
			}
			break;
		case Notification.REMOVE:
			if (oldValue instanceof XamlElement) {
				Object removeWidget = getVisual((XamlElement) oldValue);
				
				// in case of SashFrom 
				if (parent instanceof SashForm) {
					XamlAttribute attribute = updateObj.getAttribute("weights", IConstants.XWT_NAMESPACE);
					if (attribute != null) {
						String value = attribute.getValue();
						if (value != null && value.length() > 0 ) {
							SashForm sashForm = (SashForm) parent;
							Control[] children = sashForm.getChildren();
							int index = 0;
							int c = 0;
							
							for (int i = 0; i < children.length; i++) {
								if (removeWidget == children[i]) {
									index = c;
								}
								if (children[i] instanceof Sash) {
									c++;
								}
							}
							if (children.length > 0) {
								c++;
							}

							if (c <= 2) {
								updateObj.getAttributes().remove(attribute);
							}
							else {
								value = SashUtil.removeWeights(value, index);
								attribute.setValue(value);
							}
						}
					}
				}

				if (removeWidget != null) {
					updated = proxy.destroy(removeWidget);
				}
				if (parent instanceof Composite) {
					IMetaclass metaclass = XWTUtility
							.getMetaclass((XamlElement) oldValue);
					if (metaclass != null
							&& Layout.class.isAssignableFrom(metaclass
									.getType())) {
						updated = proxy.removeLayout((Composite) parent);
					}
					proxy.layout((Composite) parent);
				}
			} else if (notifier instanceof XamlAttribute) {
				XamlAttribute attribute = (XamlAttribute) notifier;
				updated = applyAttribute(parent, updateObj, attribute);
			} else if (oldValue instanceof XamlAttribute) {
				XamlAttribute attribute = (XamlAttribute) oldValue;
				updated = removeAttribute(parent, updateObj, attribute);
			}
			break;
		case Notification.MOVE: {
			if (proxy.isNull(parent)) {
				break;
			}
			if (parent instanceof Widget) {
				updated = proxy.recreate((Widget) parent);
				updateObj = null;// If updated, we can refresh all visuals.
			}
			if (parent instanceof Composite) {
				proxy.layout((Composite) parent);
			}
			break;
		}
		case Notification.ADD_MANY: {
			// TODO:add many:
		}
		case Notification.REMOVE_MANY: {
			List<?> removed = (List<?>) oldValue;
			for (Object object : removed) {
				if (object instanceof XamlElement) {
					Object widget = getVisual((XamlElement) object);
					proxy.destroy(widget);
					updated = true;
				}
			}
			if (parent != null && parent instanceof Composite) {
				proxy.layout((Composite) parent);
			}
		}
		}
		return new Result(updateObj, updated);
	}

	private boolean applyAttribute(Object parent, XamlElement element,
			XamlAttribute attribute) {
		if (proxy.isNull(parent) || attribute == null) {
			return false;
		}
		if ("style".equalsIgnoreCase(attribute.getName())
				&& IConstants.XWT_X_NAMESPACE.equals(attribute.getNamespace())) {
			if (needRecreate(parent, attribute)) {
				return proxy.recreate((Widget) parent);
			}
			return false;
		} else {
			boolean updated = proxy.initAttribute(parent, attribute);
			if (element != null && parent instanceof Item) {
				EObject eContainer = element.eContainer();
				if (eContainer != null && eContainer instanceof XamlElement) {
					element = (XamlElement) eContainer;
					if (proxy.getComponent(element) instanceof Control) {
						Control control = (Control) proxy.getComponent(element);
						proxy.layout(control);
					}
				}
			}// if the parent is Item,it must get the parent of this item and
				// layout his parent
			if (parent instanceof Control) {
				Control control = (Control) parent;
				proxy.layout(control);
			}
			return updated;
		}
	}

	private boolean needRecreate(Object parent, XamlAttribute attr) {
		if (parent == null || !(parent instanceof Widget) || attr == null) {
			return false;
		}
		Widget widget = (Widget) parent;
		int oldStyle = widget.getStyle();
		String newStyleStr = attr.getValue();
		int newStyle = (Integer) XWT.convertFrom(Integer.class, newStyleStr);
		return newStyle != oldStyle;
		// if (newStyleStr != null) {
		// if (newStyle == SWT.NONE) {
		// if ("SWT.NONE".equalsIgnoreCase(newStyleStr) ||
		// "NONE".equalsIgnoreCase(newStyleStr) ||
		// "0".equals(newStyleStr.trim())) {
		// Object defaultStyle = proxy.getDefaultValue(parent, attr);
		// return defaultStyle != null && defaultStyle instanceof Integer &&
		// ((newStyle | (Integer) defaultStyle) != oldStyle);
		// }
		// return false;
		// } else {
		// List<String> styles = new ArrayList<String>();
		// StringTokenizer stk = new StringTokenizer(newStyleStr, "|");
		// while (stk.hasMoreElements()) {
		// styles.add(stk.nextToken());
		// }
		// boolean result = false;
		// for (String aStyle : styles) {
		// newStyle = (Integer) XWT.convertFrom(Integer.class, aStyle);
		// if (newStyle == SWT.NONE &&
		// (!("SWT.NONE".equalsIgnoreCase(newStyleStr) ||
		// !"NONE".equalsIgnoreCase(newStyleStr) ||
		// !"0".equals(newStyleStr.trim())))) {
		// return false;
		// }
		// result |= (oldStyle & newStyle) == 0;
		// }
		// return result;
		// }
		// }
		// return false;
	}

	private boolean removeAttribute(Object parent, XamlElement element,
			XamlAttribute attribute) {
		if (proxy.isNull(parent) || attribute == null) {
			return false;
		}

		if ("style".equalsIgnoreCase(attribute.getName())
				&& IConstants.XWT_X_NAMESPACE.equals(attribute.getNamespace())) {
			Object defaultValue = proxy.getDefaultValue(parent, attribute);
			if (defaultValue != null && defaultValue instanceof Integer) {
				int oldStyle = (Integer) XWT.convertFrom(Integer.class,
						attribute.getValue());
				if ((oldStyle & (Integer) defaultValue) == 0) {
					return proxy.recreate((Widget) parent);
				}
			}
			return false;
		} else {
			boolean updated = proxy.removeValue(parent, attribute);
			if (parent instanceof Control) {
				Control control = (Control) parent;
				proxy.layout(control);
			}
			return updated;
		}
	}

	public void dispose() {
		proxy.dispose();
	}

	public String getHostClassName() {
		String clr = proxy.getClr();
		XamlDocument documentRoot = (XamlDocument) getDocumentRoot();
		if (clr == null && documentRoot != null) {
			XamlElement root = documentRoot.getRootElement();
			XamlAttribute attribute = root.getAttribute(
					IConstants.XAML_X_CLASS, IConstants.XWT_X_NAMESPACE);
			if (attribute != null) {
				return attribute.getValue();
			}
		}
		return clr;
	}

	/**
	 * Recreate all controls.
	 */
	public void recreate() {
		if (getDocumentRoot() == null) {
			return;
		}
		proxy.reset();
		createVisuals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.designer.editor.render.IVisualsRender#createVisuals()
	 */
	public Result createVisuals() {
		return new Result(proxy.load((XamlDocument) getDocumentRoot()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.designer.editor.render.IVisualsRender#refreshVisuals
	 * (java.lang.Object)
	 */
	public Result refreshVisuals(Object source) {
		if (source instanceof Notification) {
			return updateControls((Notification) source);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.render.IVisualsRender#getRoot()
	 */
	public Object getRoot() {
		return proxy.getRoot();
	}

}
