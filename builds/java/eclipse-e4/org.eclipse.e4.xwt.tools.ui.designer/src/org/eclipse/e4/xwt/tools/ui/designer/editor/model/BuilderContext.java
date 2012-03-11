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
package org.eclipse.e4.xwt.tools.ui.designer.editor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class BuilderContext {

	private static final String CHARACTERS = "abcdefghigklmnopqrstuvwxyz";

	private final Map<XamlNode, IDOMNode> model2text = new HashMap<XamlNode, IDOMNode>(
			1);
	private final Map<IDOMNode, XamlNode> text2model = new HashMap<IDOMNode, XamlNode>(
			1);

	private XWTModelBuilder modelBuilder;
	private IDOMDocument textRoot;
	private XamlDocument modelRoot;
	private Synchronizer synchronizer;

	private Adapter modelAdapter;
	private INodeAdapter nodeAdapter;

	public BuilderContext(XWTModelBuilder modelBuilder,
			IDOMDocument textDocument, XamlDocument modelDocument,
			Synchronizer synchronizer) {
		Assert.isNotNull(textDocument);
		Assert.isNotNull(modelDocument);
		this.modelBuilder = modelBuilder;
		this.textRoot = textDocument;
		this.modelRoot = modelDocument;
		this.synchronizer = synchronizer;
	}

	public XWTModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public IDOMDocument getTextRoot() {
		return textRoot;
	}

	public XamlDocument getModelRoot() {
		return modelRoot;
	}

	public Synchronizer getSynchronizer() {
		if (synchronizer == null) {
			synchronizer = new Synchronizer();
		}
		return synchronizer;
	}

	public void map(XamlNode model, IDOMNode textNode) {
		model2text.put(model, textNode);
		text2model.put(textNode, model);
		if (model != null && modelAdapter != null
				&& !model.eAdapters().contains(modelAdapter)) {
			model.eAdapters().add(modelAdapter);
		}
		if (textNode != null && nodeAdapter != null
				&& !textNode.getAdapters().contains(nodeAdapter)) {
			textNode.addAdapter(nodeAdapter);
		}
	}

	public void remove(Object obj) {
		IDOMNode removedText = model2text.remove(obj);
		if (removedText != null) {
			text2model.remove(removedText);
			if (nodeAdapter != null) {
				removedText.removeAdapter(nodeAdapter);
			}
		}
		XamlNode removedModel = text2model.remove(obj);
		if (removedModel != null) {
			model2text.remove(removedModel);
			if (modelAdapter != null) {
				removedModel.eAdapters().remove(modelAdapter);
			}
		}
	}

	public void clear() {
		model2text.clear();
		text2model.clear();
	}

	public XamlNode getModel(Object textNode) {
		return text2model.get(textNode);
	}

	public IDOMNode getTextNode(Object model) {
		return model2text.get(model);
	}

	public List<IDOMElement> getChildNodes(IDOMNode parent) {
		if (parent == null) {
			return Collections.emptyList();
		}
		List<IDOMElement> childNodes = new ArrayList<IDOMElement>();
		Node child = parent.getFirstChild();
		while (child != null) {
			if (child instanceof IDOMElement
					&& child.getLocalName().indexOf(".") == -1) {
				childNodes.add((IDOMElement) child);
			}
			child = child.getNextSibling();
		}
		return childNodes;
	}

	public List<Text> getContentNodes(IDOMNode node) {
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		if (length == 0) {
			return Collections.emptyList();
		}
		List<Text> contentTexts = new ArrayList<Text>();
		for (int i = 0; i < length; i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.TEXT_NODE) {
				contentTexts.add((Text) item);
			}
		}
		return contentTexts;
	}

	/**
	 * Return content of a Node, "<j:String>hello world</j:String>"
	 */
	public String getContent(IDOMNode parent) {
		List<Text> textNodes = getContentNodes(parent);
		if (textNodes.isEmpty()) {
			return null;
		}
		StringBuilder content = new StringBuilder();
		for (Text text : textNodes) {
			String value = text.getNodeValue();
			if (value == null) {
				continue;
			}
			value = filter(value.trim());
			if (value.length() != 0) {
				content.append(value);
			}
		}
		return content.length() > 0 ? content.toString() : null;
	}

	public String filter(String value) {
		value = value.replace("\n", "");
		value = value.replace("\t", "");
		value = value.replace("\r", "");
		return value;
	}

	public List<IDOMNode> getAttributes(IDOMNode parent) {
		if (parent == null) {
			return Collections.emptyList();
		}
		List<IDOMNode> attributes = new ArrayList<IDOMNode>();
		NamedNodeMap attrMap = parent.getAttributes();
		if (attrMap != null) {
			int length = attrMap.getLength();
			for (int i = 0; i < length; i++) {
				IDOMAttr item = (IDOMAttr) attrMap.item(i);
				String name = item.getLocalName();
				String prefix = item.getPrefix();
				if (name.indexOf(".") != -1) {
					name = name.substring(name.indexOf(".") + 1);
					prefix = null;
				}
				if ("xmlns".equals(name)) {
					continue;
				}
				if ("xmlns".equals(prefix)) {
					continue;
				}
				attributes.add(item);
			}
		}
		NodeList children = parent.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node item = children.item(i);
				String localName = item.getLocalName();
				if (item instanceof IDOMElement && localName.indexOf(".") != -1) {
					attributes.add((IDOMElement) item);
				}
			}
		}
		return attributes;
	}

	public boolean contains(Node parent, Attr attr) {
		NamedNodeMap attributes = parent.getAttributes();
		for (int i = attributes.getLength() - 1; i >= 0; i--) {
			if (attr == attributes.item(i)) {
				return true;
			}
		}

		return false;
	}

	protected String getPrefix(String namespace) {
		EMap<String, String> declaredNamespaces = modelRoot
				.getDeclaredNamespaces();
		Set<String> existings = declaredNamespaces.keySet();
		for (String p : existings) {
			if (namespace.equals(declaredNamespaces.get(p))) {
				return p;
			}
		}
		char[] c = CHARACTERS.toCharArray();
		for (char d : c) {
			String prefix = Character.toString(d);
			if (!existings.contains(prefix)) {
				return prefix;
			}
		}
		return "j";
	}

	/**
	 * @param modelAdapter
	 *            the modelAdapter to set
	 */
	public void setModelAdapter(Adapter modelAdapter) {
		this.modelAdapter = modelAdapter;
	}

	/**
	 * @return the modelAdapter
	 */
	public Adapter getModelAdapter() {
		return modelAdapter;
	}

	/**
	 * @param nodeAdapter
	 *            the nodeAdapter to set
	 */
	public void setNodeAdapter(INodeAdapter nodeAdapter) {
		this.nodeAdapter = nodeAdapter;
	}

	/**
	 * @return the nodeAdapter
	 */
	public INodeAdapter getNodeAdapter() {
		return nodeAdapter;
	}
}
