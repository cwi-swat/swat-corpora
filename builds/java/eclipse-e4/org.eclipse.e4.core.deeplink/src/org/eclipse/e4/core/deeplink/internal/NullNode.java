/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package org.eclipse.e4.core.deeplink.internal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * A null object pattern implementation of a W3C DOM Node.  Used when parsing
 * the deep link result XML's DOM. 
 */
public class NullNode implements Node {

	public static final String UNDEFINED_VALUE = "undefined";

	public Node appendChild(Node newChild) throws DOMException {
		return null;
	}

	public Node cloneNode(boolean deep) {
		return null;
	}

	public short compareDocumentPosition(Node other) throws DOMException {
		return 0;
	}

	public NamedNodeMap getAttributes() {
		return null;
	}

	public String getBaseURI() {
		return null;
	}

	public NodeList getChildNodes() {
		return null;
	}

	public Object getFeature(String feature, String version) {
		return null;
	}

	public Node getFirstChild() {
		return null;
	}

	public Node getLastChild() {
		return null;
	}

	public String getLocalName() {
		return UNDEFINED_VALUE;
	}

	public String getNamespaceURI() {
		return UNDEFINED_VALUE;
	}

	public Node getNextSibling() {
		return null;
	}

	public String getNodeName() {
		return UNDEFINED_VALUE;
	}

	public short getNodeType() {
		return 0;
	}

	public String getNodeValue() throws DOMException {
		return UNDEFINED_VALUE;
	}

	public Document getOwnerDocument() {
		return null;
	}

	public Node getParentNode() {
		return null;
	}

	public String getPrefix() {
		return UNDEFINED_VALUE;
	}

	public Node getPreviousSibling() {
		return null;
	}

	public String getTextContent() throws DOMException {
		return UNDEFINED_VALUE;
	}

	public Object getUserData(String key) {
		return null;
	}

	public boolean hasAttributes() {
		return false;
	}

	public boolean hasChildNodes() {
		return false;
	}

	public Node insertBefore(Node newChild, Node refChild)
			throws DOMException {
		return null;
	}

	public boolean isDefaultNamespace(String namespaceURI) {
		return false;
	}

	public boolean isEqualNode(Node arg) {
		return false;
	}

	public boolean isSameNode(Node other) {
		return false;
	}

	public boolean isSupported(String feature, String version) {
		return false;
	}

	public String lookupNamespaceURI(String prefix) {
		return UNDEFINED_VALUE;
	}

	public String lookupPrefix(String namespaceURI) {
		return UNDEFINED_VALUE;
	}

	public void normalize() {
	}

	public Node removeChild(Node oldChild) throws DOMException {
		return null;
	}

	public Node replaceChild(Node newChild, Node oldChild)
			throws DOMException {
		return null;
	}

	public void setNodeValue(String nodeValue) throws DOMException {
		
	}

	public void setPrefix(String prefix) throws DOMException {
		
	}

	public void setTextContent(String textContent) throws DOMException {
		
	}

	public Object setUserData(String key, Object data,
			UserDataHandler handler) {
		return null;
	}
	
}