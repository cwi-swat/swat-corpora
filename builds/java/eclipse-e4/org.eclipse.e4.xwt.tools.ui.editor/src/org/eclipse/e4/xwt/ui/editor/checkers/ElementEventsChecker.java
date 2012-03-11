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
package org.eclipse.e4.xwt.ui.editor.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.xwt.vex.dom.DomHelper;
import org.eclipse.e4.xwt.vex.problems.Problem;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu
 * 
 */
public class ElementEventsChecker extends AbstractProblemChecker {

	protected void checkProblems(StructuredTextEditor textEditor, String javaName, Node node, List<Problem> problems, boolean includeChildren) {

		if (!isValid(node)) {
			return;
		}

		if (includeChildren) {
			NodeList childNodes = node.getChildNodes();
			if (childNodes != null) {
				int length = childNodes.getLength();
				for (int i = 0; i < length; i++) {
					Node item = childNodes.item(i);
					checkProblems(textEditor, javaName, item, problems, includeChildren);
				}
			}
		}

		String nodeName = getTagName(node);
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return;
		}
		int attrLength = attributes.getLength();
		List<String> events = getEvents(nodeName, DomHelper.lookupNamespaceURI(node));
		if (attrLength == 0 || events.isEmpty()) {
			return;
		}
		for (int j = 0; j < attrLength; j++) {
			Node attr = attributes.item(j);
			String attrName = getTagName(attr);
			if (attrName == null) {
				continue;
			}
			if (events.contains(attrName)) {
				String nodeValue = attr.getNodeValue();
				if (nodeValue != null) {
					List<String> methods = getJavaMethods(textEditor, javaName);
					if (!methods.contains(nodeValue)) {
						Problem problem = createProblem(textEditor, node, "Java method \"" + nodeValue + "\" as event of \"" + nodeName + "\" not found", Problem.ERROR);
						problems.add(problem);
					}
				}
			}
		}

	}

	private List<String> getJavaMethods(StructuredTextEditor textEditor, String hostClassName) {
		List<String> results = new ArrayList<String>();
		IType javeType = getJaveType(textEditor, hostClassName);
		if (javeType != null) {
			try {
				IMethod[] methods = javeType.getMethods();
				for (IMethod method : methods) {
					results.add(method.getElementName());
				}
			} catch (JavaModelException e) {
				return results;
			}
		}
		return results;
	}

	private IType getJaveType(StructuredTextEditor textEditor, String hostClassName) {
		IJavaProject javaProject = getJavaProject(textEditor);
		if (javaProject != null) {
			try {
				return javaProject.findType(hostClassName);
			} catch (JavaModelException e) {
				return null;
			}
		}
		return null;
	}

	private List<String> getEvents(String tagName, String ns) {
		List<String> events = new ArrayList<String>();
		// Metaclass metaclass = UPF.getMetaclass(tagName, ns);
		// if (metaclass == null) {
		// return Collections.EMPTY_LIST;
		// }
		// Collection<Event> allEvents = metaclass.getAllEvents();
		// for (Event event : allEvents) {
		// events.add(event.getName());
		// }
		// Collection<Event> allAttachedEvents = metaclass.getAllAttachedEvents();
		// for (Event event : allAttachedEvents) {
		// events.add(event.getName());
		// }
		return events;
	}

	@Override
	public boolean canChecked(StructuredTextEditor textEditor, String javaName) {
		return javaName != null && super.canChecked(textEditor, javaName);
	}
}
