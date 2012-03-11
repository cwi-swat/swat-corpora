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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.xwt.tools.ui.designer.editor.model.Synchronizer.EventType;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class ModelAdapter extends EContentAdapter {

	private BuilderContext fContext;
	private ReverseJob reverseJob;

	public ModelAdapter(BuilderContext mapper) {
		this.fContext = mapper;
	}

	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (notification.isTouch()) {
			return;
		}
		Synchronizer synch = fContext.getSynchronizer();
		// if (EventType.LoadingEvent != synch.getEventType()) {
		XWTModelBuilder modelBuilder = fContext.getModelBuilder();
		modelBuilder.dispatchEvent(notification);
		// }
		if (synch.isFree()) {
			reverse(notification);
		}
	}

	private void reverse(Notification msg) {
		if (reverseJob == null) {
			reverseJob = new ReverseJob();
		}
		reverseJob.reverse(msg);
	}

	private void tryToUpdateText(Notification msg) {
		Synchronizer synch = fContext.getSynchronizer();
		synchronized (synch) {
			synch.setEventType(EventType.ModelEvent);
			Object notifier = msg.getNotifier();
			Object oldValue = msg.getOldValue();
			Object newValue = msg.getNewValue();
			if (oldValue != null && oldValue.equals(newValue)) {
				return;
			}

			IDOMNode textNode = fContext.getTextNode(notifier);
			IDOMNode oldNode = fContext.getTextNode(oldValue);
			IDOMNode newNode = fContext.getTextNode(newValue);

			int eventType = msg.getEventType();
			switch (eventType) {
			case Notification.ADD: {
				if (textNode == null && notifier instanceof EObject) {
					EObject eContainer = ((EObject) notifier).eContainer();
					IDOMNode superParent = fContext.getTextNode(eContainer);
					if (superParent != null) {
						if (notifier instanceof XamlAttribute) {
							reverseAttr(superParent, (XamlAttribute) notifier);
						} else if (notifier instanceof XamlElement) {
							reverseNode(superParent, (XamlElement) newValue);
						}
					}
					textNode = fContext.getTextNode(notifier);
				}
				if (textNode == null
						|| (!(textNode instanceof IDOMElement) && !(textNode instanceof IDOMAttr))) {
					break;
				}
				if (notifier instanceof XamlAttribute
						&& textNode instanceof IDOMAttr
						&& newValue instanceof XamlElement) {
					validatePrefix((XamlElement) newValue);
					String flatValue = ((XamlElement) newValue).getFlatValue();
					if (flatValue == null) {
						flatValue = "";
					}
					((IDOMAttr) textNode).setValue("{" + flatValue + "}");
				} else if (newValue instanceof XamlAttribute) {
					reverseAttr(textNode, (XamlAttribute) newValue);
				} else if (newValue instanceof XamlElement) {
					reverseNode(textNode, (XamlElement) newValue);
				}
				break;
			}
			case Notification.ADD_MANY: {
				System.err.println("ADD_MANY");
				break;
			}
			case Notification.MOVE: {
				int oldPos = ((Integer) oldValue);
				int newPos = msg.getPosition();
				IDOMNode parentNode = textNode;
				IDOMNode moveable = newNode;
				if (parentNode == null || moveable == null
						|| parentNode != moveable.getParentNode()) {
					break;
				}
				List<IDOMElement> eles = fContext.getChildNodes(parentNode);
				int offset = newPos - oldPos;
				int oldIndex = eles.indexOf(moveable);
				int newIndex = offset > 0 ? oldIndex + offset + 1 : oldIndex
						+ offset;
				Node nextSibling = moveable.getNextSibling();
				parentNode.removeChild(moveable);
				if (nextSibling instanceof Text) {
					// String nodeValue = ((Text)
					// nextSibling).getNodeValue();
					// if ((nodeValue == null || nodeValue.equals(""))) {
					parentNode.removeChild(nextSibling);
					// }
				}
				if (newIndex >= 0 && newIndex <= eles.size() - 1) {
					IDOMElement insert = eles.get(newIndex);
					parentNode.insertBefore(moveable, insert);
				} else {
					parentNode.appendChild(moveable);
				}
				break;
			}
			case Notification.REMOVE: {
				if (textNode != null) {
					if (oldNode instanceof IDOMElement) {
						NodeList nodelist = textNode.getChildNodes();
						// before remove, we must check if there is the
						// oldNode
						// in the textNode.Because the oldNode may have be
						// deleted.
						for (int i = 0; i < nodelist.getLength(); i++) {
							if (nodelist.item(i) == oldNode) {
								textNode.removeChild(oldNode);
							}
						}
					} else if (textNode instanceof IDOMElement
							&& oldNode instanceof IDOMAttr) {
						if (fContext.contains(textNode, (Attr) oldNode)) {
							((IDOMElement) textNode)
									.removeAttributeNode((Attr) oldNode);
						}
					}
					fContext.remove(oldNode);
				}
				break;
			}
			case Notification.REMOVE_MANY: {
				System.err.println("REMOVE_MANY");
				break;
			}
			case Notification.SET:
			case Notification.UNSET:
				if (textNode != null) {
					if (textNode instanceof Attr) {
						String value = newValue == null ? "" : newValue
								.toString();
						Attr attr = (Attr) textNode;
						if (!value.equals(attr.getNodeValue())) {
							attr.setNodeValue(value);
						}
					} else if (newValue != null && oldValue != null
							&& oldValue.equals(fContext.getContent(textNode))) {
						reverseContent(textNode, newValue.toString());
					}
				}
				break;
			}
			synch.setFree();
		}
	}

	protected void reverseAttr(IDOMNode parent, XamlAttribute model) {
		IDOMDocument textDocument = fContext.getTextRoot();
		validatePrefix(model);
		String localName = parent.getLocalName();
		String value = model.getValue();
		String name = model.getName();
		if (value == null && model.isUseFlatValue()) {
			String flatValue = model.getFlatValue();
			if (flatValue != null && !"".equals(flatValue)) {
				value = "{" + flatValue + "}";
			}
		}
		if (value != null && parent instanceof IDOMElement) {
			IDOMAttr attr = (IDOMAttr) textDocument.createAttribute(name);
			attr.setNodeValue(value);
			attr.setPrefix(model.getPrefix());
			((IDOMElement) parent).setAttributeNode(attr);
			fContext.map(model, attr);
		} else {
			String childName = localName + "." + name;
			while (localName != null && localName.indexOf(".") != -1) {
				/*
				 * If the parent like: "TableViewer.table", we should add the
				 * new attribute("layoutData") to TableViewer element directly,
				 * the new element should be "TableViewer.table.layoutData" and
				 * its parent should be "TableViewer" but not
				 * "TableViewer.table".
				 */
				parent = (IDOMElement) parent.getParentNode();
				localName = parent.getLocalName();
			}
			EList<XamlElement> childNodes = model.getChildNodes();
			EList<XamlAttribute> attributes = model.getAttributes();
			if (!attributes.isEmpty()) {
				for (XamlAttribute attr : attributes) {
					if (attr.getValue() != null) {
						IDOMElement node = (IDOMElement) textDocument
								.createElement(childName);
						reverseAttr(node, attr);
						parent.appendChild(node);
						fContext.map(model, node);
					} else {
						IDOMElement node = (IDOMElement) textDocument
								.createElement(childName);
						EList<XamlElement> childNodes2 = attr.getChildNodes();
						for (XamlElement child : childNodes2) {
							reverseNode(node, child);
						}
						parent.appendChild(node);
						fContext.map(model, node);
					}
				}
			} else if (!childNodes.isEmpty()) {
				IDOMElement node = (IDOMElement) textDocument
						.createElement(childName);
				for (XamlElement child : childNodes) {
					reverseNode(node, child);
				}
				parent.appendChild(node);
				fContext.map(model, node);
			} else {
				IDOMElement node = (IDOMElement) textDocument
						.createElement(childName);
				parent.appendChild(node);
				fContext.map(model, node);
			}
		}
	}

	private String validatePrefix(XamlNode node) {
		IDOMDocument textDocument = fContext.getTextRoot();
		XamlDocument modelRoot = fContext.getModelRoot();
		String prefix = node.getPrefix();
		String namespace = node.getNamespace();
		if (prefix == null && namespace != null) {
			prefix = fContext.getPrefix(namespace);
		}
		if ((prefix != null && !prefix.equals(node.getPrefix()))
				|| (prefix == null && node.getPrefix() != null)) {
			node.setPrefix(prefix);
		}
		if (prefix != null) {
			Element root = textDocument.getDocumentElement();
			Attr prefixNode = root.getAttributeNode(prefix);
			if (prefixNode == null) {
				root.setAttribute("xmlns:" + prefix, namespace);
				modelRoot.addDeclaredNamespace(prefix, namespace);
			}
		}
		for (XamlAttribute attr : node.getAttributes()) {
			validatePrefix(attr);
		}
		for (XamlElement child : node.getChildNodes()) {
			validatePrefix(child);
		}
		return prefix;
	}

	protected void reverseNode(IDOMNode parent, XamlElement element) {
		String name = element.getName();
		IDOMDocument textDocument = fContext.getTextRoot();
		IDOMElement node = (IDOMElement) textDocument.createElement(name);
		EList<XamlAttribute> attributes = element.getAttributes();
		EList<XamlElement> childnodes = element.getChildNodes();
		for (XamlAttribute attribute : attributes) {
			reverseAttr(node, attribute);
		}
		for (XamlElement child : childnodes) {
			reverseNode(node, child);
		}
		String prefix = validatePrefix(element);
		if (prefix != null) {
			node.setPrefix(prefix);
		}
		XamlNode next = null;
		XamlNode container = (XamlNode) element.eContainer();
		if (container != null) {
			int i = container.getChildNodes().indexOf(element);
			try {
				next = container.getChildNodes().get(i + 1);
			} catch (Exception e) {
			}
		}
		if (next != null) {
			IDOMNode nextNode = fContext.getTextNode(next);
			parent.insertBefore(node, nextNode);
		} else {
			parent.appendChild(node);
		}
		String value = element.getValue();
		if (value != null) {
			reverseContent(node, value);
		}
		fContext.map(element, node);
	}

	protected void reverseContent(IDOMNode node, String value) {
		String content = fContext.getContent(node);
		if (value == null && content == null || value != null
				&& value.equals(content)) {
			return;
		}
		value = value == null ? "" : value;
		if (content != null) {
			List<Text> contentNodes = fContext.getContentNodes(node);
			for (Text text : contentNodes) {
				String nodeValue = text.getNodeValue();
				if (nodeValue == null
						|| fContext.filter(nodeValue).length() == 0) {
					continue;
				}
				text.setData(value);
			}
		} else {
			IDOMDocument textDocument = fContext.getTextRoot();
			Text textNode = textDocument.createTextNode(value == null ? ""
					: value);
			node.appendChild(textNode);
		}
	}

	class ReverseJob extends Job {
		private List<Notification> reverseJobs;
		private long timestamp = -1;

		public ReverseJob() {
			super("Reverse");
			setPriority(SHORT);
			setSystem(true);
			ISchedulingRule rule = new ISchedulingRule() {
				public boolean isConflicting(ISchedulingRule rule) {
					return getClass() == rule.getClass();
				}

				public boolean contains(ISchedulingRule rule) {
					return getClass() == rule.getClass();
				}
			};
			setRule(rule);
		}

		protected IStatus run(IProgressMonitor monitor) {
			List<Notification> jobs = new ArrayList<Notification>(reverseJobs);
			for (Notification event : jobs) {
				tryToUpdateText(event);
			}
			synchronized (reverseJobs) {
				reverseJobs.removeAll(jobs);
				if (!reverseJobs.isEmpty()) {
					schedule(1000);
				}
			}
			timestamp = -1;
			return Status.OK_STATUS;
		}

		public void reverse(Notification event) {
			if (reverseJobs == null) {
				reverseJobs = new ArrayList<Notification>();
			}
			reverseJobs.add(event);
			if (timestamp == -1) {
				schedule(1000);
			}
			timestamp = System.currentTimeMillis();
		}
	}
}
