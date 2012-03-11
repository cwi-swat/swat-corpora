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
package org.eclipse.e4.xwt.tools.ui.designer.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.xwt.internal.xml.Attribute;
import org.eclipse.e4.xwt.internal.xml.DocumentObject;
import org.eclipse.e4.xwt.internal.xml.Element;
import org.eclipse.e4.xwt.internal.xml.ElementManager;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class XamlDocumentProvider {

	private IFile file;
	private XamlDocument document;
	private boolean needReload = false;

	private XamlDocumentProvider(IFile file) {
		Assert.isTrue(file != null && file.exists());
		this.file = file;
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new IResourceChangeListener() {
					public void resourceChanged(IResourceChangeEvent event) {
						performResourceChanged(event);
					}
				});
	}

	private void performResourceChanged(IResourceChangeEvent event) {
		if (file == null || !file.exists()) {
			return;
		}
		IResourceDelta delta = event.getDelta();
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					if (resource != null
							&& file.getFullPath()
									.equals(resource.getFullPath())) {
						needReload = true;
						return false;// break
					}
					return true;
				}
			});
		} catch (CoreException e) {
		}
	}

	public XamlDocument getDocument() {
		if (document == null) {
			document = XamlFactory.eINSTANCE.createXamlDocument();
		}
		boolean reload = needReload || document.getRootElement() == null;
		if (reload) {
			reload();
			needReload = false;
		}
		return document;
	}

	public boolean reload() {
		if (document == null) {
			return false;
		}
		try {
			URL url = file.getLocationURI().toURL();
			if (url == null) {
				return false;
			}
			ElementManager manager = new ElementManager();
			Element element = manager.load(file.getContents(), url);

			XamlElement rootElement = document.getRootElement();
			if (rootElement == null) {
				rootElement = XamlFactory.eINSTANCE.createXamlElement();
			}
			updateNode(rootElement, element);
			if (rootElement.eContainer() == null) {
				document.setRootElement(rootElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void updateNode(XamlNode node, DocumentObject obj) {
		if (node == null || obj == null) {
			throw new NullPointerException();
		}
		String namespace = obj.getNamespace();
		// 1. basic settings
		node.setId(obj.getId());
		node.setName(obj.getName());
		node.setNamespace(namespace);
		node.setValue(obj.getContent());

		// 2. Attributes
		if (obj instanceof Element) {
			Element element = (Element) obj;
			List<XamlAttribute> oldAttributes = new ArrayList<XamlAttribute>(
					node.getAttributes());
			oldAttributes.removeAll(updateAttributes(node, element, null));

			String[] attributeNamespaces = element.attributeNamespaces();
			for (String ns : attributeNamespaces) {
				oldAttributes.removeAll(updateAttributes(node, element, ns));
			}
			for (XamlAttribute forRemove : oldAttributes) {
				node.getAttributes().remove(forRemove);
			}
		}

		// 3. Children
		List<XamlElement> oldChildren = new ArrayList<XamlElement>(node
				.getChildNodes());
		oldChildren.removeAll(updateChildNodes(node, obj));
		for (XamlElement forRemove : oldChildren) {
			node.getChildNodes().remove(forRemove);
		}
	}

	private List<XamlElement> updateChildNodes(XamlNode node, DocumentObject obj) {
		List<XamlElement> updates = new ArrayList<XamlElement>();
		DocumentObject[] children = obj.getChildren();
		for (int index = 0; index < children.length; index++) {
			DocumentObject documentObject = children[index];
			XamlElement child = node.getChild(index);
			if (child == null) {
				child = XamlFactory.eINSTANCE.createXamlElement();
			}
			updateNode(child, documentObject);
			if (child.eContainer() == null) {
				node.getChildNodes().add(child);
			}
			updates.add(child);
		}
		return updates;
	}

	private List<XamlAttribute> updateAttributes(XamlNode node,
			Element element, String namespace) {
		if (node == null || element == null) {
			throw new NullPointerException();
		}
		List<XamlAttribute> attrs = new ArrayList<XamlAttribute>();
		String[] attributeNames = namespace == null ? element.attributeNames()
				: element.attributeNames(namespace);
		for (String attrName : attributeNames) {
			Attribute attribute = namespace == null ? element
					.getAttribute(attrName) : element.getAttribute(namespace,
					attrName);
			if (attribute == null) {
				continue;
			}
			XamlAttribute xa = node.getAttribute(attrName, namespace);
			if (xa == null) {
				xa = XamlFactory.eINSTANCE.createAttribute(attrName, namespace);
			}
			updateNode(xa, attribute);
			if (xa.eContainer() == null) {
				node.getAttributes().add(xa);
			}
			attrs.add(xa);
		}
		return attrs;
	}

	public static XamlDocument getDocument(IFile file) {
		return getProvider(file).getDocument();
	}

	public static XamlDocumentProvider getProvider(IFile file) {
		XamlDocumentProvider p = providers.get(file);
		if (p == null) {
			providers.put(file, p = new XamlDocumentProvider(file));
		}
		return p;
	}

	private static final Map<IFile, XamlDocumentProvider> providers = new HashMap<IFile, XamlDocumentProvider>(
			1);
}
