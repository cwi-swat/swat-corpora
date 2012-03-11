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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.text.StructuredTextHelper;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.AbstractModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.IModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.editor.XWTDesigner;
import org.eclipse.e4.xwt.tools.ui.designer.editor.model.Synchronizer.EventType;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu jin.liu@soyatec.com
 */
@SuppressWarnings("restriction")
public class XWTModelBuilder extends AbstractModelBuilder implements
		IModelBuilder {

	private XamlDocument document;
	private IDocument jfaceDom;
	private IFile input;
	private static Random RANDOM = new Random();
	private BraceHandler braceHandler;

	private BuilderContext fContext;
	private Synchronizer synch;
	private ModelAdapter modelAdapter;

	private ReloadJob reloadJob;
	private NodeAdapter nodeAdapter = new NodeAdapter();

	public static String generateID(String typeName) {
		return typeName + RANDOM.nextInt(Integer.MAX_VALUE);
	}

	public boolean doLoad(IEditorPart designer, final IProgressMonitor monitor) {
		if (designer == null) {
			return false;
		}
		input = ((XWTDesigner) designer).getFile();
		jfaceDom = ((XWTDesigner) designer).getDocument();
		if (jfaceDom == null) {
			return false;
		}
		if (input != null) {
			document = ModelCacheUtility.doLoadFromCache(input, monitor);
		}
		if (document == null) {
			document = XamlFactory.eINSTANCE.createXamlDocument();
		}
		IDOMDocument textDocument = getTextDocument(jfaceDom);
		if (textDocument == null) {
			return false;
		}
		if (monitor != null) {
			monitor.beginTask("Loading Document", 100);
		}
		jfaceDom.addDocumentListener(new IDocumentListener() {
			public void documentAboutToBeChanged(DocumentEvent event) {
			}

			public void documentChanged(DocumentEvent event) {
				// Revert File.
				if (event.getOffset() == 0 && event.getLength() != 0
						&& event.getText() != null) {
					new ReloadJob().schedule();
				}
			}
		});

		if (synch == null) {
			synch = new Synchronizer();
		}
		fContext = new BuilderContext(this, textDocument, document, synch);
		fContext.setNodeAdapter(nodeAdapter);
		modelAdapter = new ModelAdapter(fContext);
		if (!document.eAdapters().contains(modelAdapter)) {
			document.eAdapters().add(modelAdapter);
		}

		final IDOMElement textElement = (IDOMElement) textDocument
				.getDocumentElement();

		loadingModel(textElement, monitor);
		return true;
	}

	private BraceHandler getBraceHandler() {
		if (braceHandler == null) {
			braceHandler = createBraceHandler(document);
		}
		return braceHandler;
	}

	protected BraceHandler createBraceHandler(XamlDocument document) {
		return new BraceHandler(document);
	}

	/**
	 * @param textDocument
	 * @param document
	 */
	protected void handleDeclaredNamespaces(String prefix, String namespace) {
		if (document != null) {
			document.getDeclaredNamespaces().put(prefix, namespace);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.IDiagramModelBuilder#getModelRoot()
	 */
	public XamlDocument getDiagram() {
		return document;
	}

	protected void loadingModel(IDOMElement text, IProgressMonitor monitor) {
		if (text == null || !synch.isFree()) {
			return;
		}
		synchronized (synch) {
			synch.setEventType(EventType.LoadingEvent);

			try {
				String name = text.getLocalName();
				String nsURI = text.getNamespaceURI();
				String prefix = text.getPrefix();
				XamlElement rootElement = document.getRootElement();

				boolean isNew = false;
				if (rootElement == null || !name.equals(rootElement.getName())) {
					rootElement = XamlFactory.eINSTANCE.createElement(name,
							nsURI);
					isNew = true;
				}
				if (monitor != null) {
					monitor.subTask("Load element " + name);
					monitor.worked(1);
				}
				rootElement.setPrefix(prefix);
				createChild(rootElement, text, monitor);

				fContext.map(rootElement, text);
				if (isNew) {
					document.setRootElement(rootElement);
				}
			} finally {
				synch.setFree();
			}
		}
	}

	protected void createChild(XamlNode parent, IDOMElement text,
			IProgressMonitor monitor) {
		NamedNodeMap attrMap = text.getAttributes();
		if (attrMap != null) {
			int length = attrMap.getLength();
			for (int i = 0; i < length; i++) {
				IDOMAttr item = (IDOMAttr) attrMap.item(i);
				String localName = item.getLocalName();
				String value = item.getNodeValue();
				String prefix = item.getPrefix();
				if ("xmlns".equals(localName)) {
					handleDeclaredNamespaces(null, value);
				}
				if ("xmlns".equals(prefix)) {
					handleDeclaredNamespaces(localName, value);
				}
			}
		}

		List<IDOMNode> attributes = fContext.getAttributes(text);
		List<XamlAttribute> oldAttrs = new ArrayList<XamlAttribute>(parent
				.getAttributes());

		for (int i = 0; i < attributes.size(); i++) {
			IDOMNode attr = attributes.get(i);
			oldAttrs.remove(createAttribute(parent, attr, i));
			if (monitor != null) {
				monitor.subTask("Load attr " + attr.getNodeName());
				monitor.worked(1);
			}
		}
		if (!oldAttrs.isEmpty()) {
			parent.getAttributes().removeAll(oldAttrs);
		}

		List<IDOMElement> childNodes = fContext.getChildNodes(text);
		List<XamlElement> oldChildren = new ArrayList<XamlElement>(parent
				.getChildNodes());
		for (int i = 0; i < childNodes.size(); i++) {
			IDOMElement child = childNodes.get(i);
			oldChildren.remove(createElement(parent, child, i));
			if (monitor != null) {
				monitor.subTask("Load child " + child.getNodeName());
				monitor.worked(1);
			}
		}
		if (!oldChildren.isEmpty()) {
			parent.getChildNodes().removeAll(oldChildren);
		}

		String content = fContext.getContent(text);
		if (content != null) {
			parent.setValue(content);
		}
	}

	private String normalizeName(String tagName) {
		if (tagName == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();

		boolean isH = false;
		for (int i = 0, len = tagName.length(); i < len; i++) {
			char c = tagName.charAt(i);
			if (i == 0) {
				buffer.append(Character.toUpperCase(c));
			} else {
				switch (c) {
				case '-':
					isH = true;
					break;
				case '.':
					isH = true;
					buffer.append(c);
					break;
				default:
					if (isH) {
						buffer.append(Character.toUpperCase(c));
						isH = false;
					} else {
						buffer.append(c);
					}
					break;
				}
			}
		}
		return buffer.toString();
	}

	protected XamlElement createElement(XamlNode parent, IDOMElement child,
			int index) {
		String name = child.getLocalName();
		String prefix = child.getPrefix();
		String ns = document.getDeclaredNamespace(prefix);
		XamlElement element = parent.getChild(index);
		if (element == null || element.getName() == null
				|| element.getNamespace() == null
				|| !element.getName().equals(name)
				|| !element.getNamespace().equals(ns)) {
			element = XamlFactory.eINSTANCE.createElement(normalizeName(name),
					ns);
			element.setId(generateID(name));
		}
		fContext.map(element, child);
		element.setPrefix(prefix);
		createChild(element, child, null);
		return addChild(parent, element, index);
	}

	protected XamlNode createAttribute(XamlNode parent, IDOMNode attr, int index) {
		if (attr == null) {
			return null;
		}
		String localName = attr.getLocalName();
		String value = attr.getNodeValue();
		String prefix = attr.getPrefix();
		String ns = document.getDeclaredNamespace(prefix);

		String name = localName;
		int i = localName.indexOf(".");
		if (i != -1) {
			name = localName.substring(i + 1);
		}

		if (name == null || "".equals(name)) {
			return null;
		}
		if ("xmlns".equals(name)) {
			handleDeclaredNamespaces(null, value);
			return null;
		}
		if ("xmlns".equals(prefix)) {
			handleDeclaredNamespaces(name, value);
			return null;
		}

		XamlAttribute nameAttr = getAttribute(parent, name, ns);
		if (nameAttr == null) {
			nameAttr = XamlFactory.eINSTANCE.createAttribute(
					normalizeName(name), ns);
			nameAttr.setId(generateID(name));
		}
		// a.setGroupName(groupName);
		if (attr instanceof IDOMElement) {
			IDOMElement child = (IDOMElement) attr;
			createChild(nameAttr, child, null);
		}
		nameAttr.setPrefix(prefix);
		if (handleBraces(value)) {
			XamlNode valueNode = getBraceHandler().parse(nameAttr, value);
			if (valueNode != null && valueNode instanceof XamlElement) {
				addChild(nameAttr, (XamlElement) valueNode, -1);
			}
			for (Iterator<XamlElement> iterator = nameAttr.getChildNodes()
					.iterator(); iterator.hasNext();) {
				if (iterator.next() != valueNode) {
					iterator.remove();
				}
			}
			nameAttr.setValue(null);
		} else {
			nameAttr.setValue(value);
			if (value != null) {
				nameAttr.getChildNodes().clear();
			}
		}
		fContext.map(nameAttr, attr);
		return addAttribute(parent, nameAttr, index);
	}

	private boolean handleBraces(String value) {
		return value != null && value.startsWith("{") && value.endsWith("}");
	}

	protected XamlAttribute getAttribute(XamlNode parent, String attrName,
			String namespace) {
		return XWTModelUtil.getAdaptableAttribute(parent, attrName, namespace);
	}

	protected XamlElement addChild(XamlNode parent, XamlElement n, int index) {
		if (parent == null || n == null) {
			return n;
		}
		EList<XamlElement> childNodes = parent.getChildNodes();
		int size = childNodes.size();
		if (!parent.getChildNodes().contains(n)) {
			if (index < 0 || index > size) {
				index = size - 1;
			}
			if (index < 0) {
				index = 0;
			}
			childNodes.add(index, n);
		} else if (index >= 0 && index < size) {
			int oldIndex = childNodes.indexOf(n);
			if (oldIndex != index) {
				childNodes.move(index, oldIndex);
			}
		}
		return n;
	}

	protected XamlAttribute addAttribute(XamlNode parent, XamlAttribute a,
			int index) {
		if (parent == null || a == null) {
			return null;
		}
		EList<XamlAttribute> attributes = parent.getAttributes();
		int size = attributes.size();
		if (!parent.getAttributes().contains(a)) {
			if (index < 0 || index > size) {
				index = size - 1;
			}
			if (index < 0) {
				index = 0;
			}
			attributes.add(index, a);
		} else if (index >= 0 && index < size) {
			int oldIndex = attributes.indexOf(a);
			if (oldIndex != index) {
				attributes.move(index, oldIndex);
			}
		}
		return a;
	}

	protected IDOMDocument getTextDocument(IDocument doc) {
		IStructuredModel model = StructuredModelManager.getModelManager()
				.getExistingModelForRead(doc);
		if (model != null && model instanceof IDOMModel) {
			return ((IDOMModel) model).getDocument();
		}

		return null;
	}

	/**
	 * Format Text of Editor.
	 */
	protected void format() {
		// Format in main thread.
		if (jfaceDom != null) {
			IDOMDocument textDocument = getTextDocument(jfaceDom);
			if (textDocument != null) {
				IDOMElement textElement = (IDOMElement) textDocument
						.getDocumentElement();
				formatRemoveEmpty(textElement);
			}
			StructuredTextHelper.format(jfaceDom);
		}
	}

	/**
	 * @param textElement
	 */
	protected void formatRemoveEmpty(Node node) {
		if (node == null) {
			return;
		}
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.TEXT_NODE) {
					TextImpl t = (TextImpl) item;
					if (t.isWhitespace() || t.isInvalid()) {
						Node next = t.getNextSibling();
						while (next != null
								&& next.getNodeType() == Node.TEXT_NODE
								&& (((TextImpl) next).isWhitespace() || ((TextImpl) next)
										.isInvalid())) {
							node.removeChild(next);
							next = next.getNextSibling();
						}
					}
				} else {
					formatRemoveEmpty(item);
				}
			}
		}
	}

	protected boolean contains(Node parent, Attr attr) {
		NamedNodeMap attributes = parent.getAttributes();
		for (int i = attributes.getLength() - 1; i >= 0; i--) {
			if (attr == attributes.item(i)) {
				return true;
			}
		}

		return false;
	}

	public void reload() {
		if (!synch.isFree()) {
			return;
		}
		if (document == null || jfaceDom == null) {
			return;
		}
		IDOMDocument textDocument = getTextDocument(jfaceDom);
		if (textDocument == null) {
			return;
		}
		IDOMElement textElement = (IDOMElement) textDocument
				.getDocumentElement();
		loadingModel(textElement, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.IDiagramModelBuilder#dispose()
	 */
	public void dispose() {
		doSave(null);// save to cache;
		document = null;
		jfaceDom = null;
		fContext.clear();
	}

	private Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null || PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		return display;
	}

	protected boolean isValidThread() {
		return getDisplay() == null
				|| getDisplay().getThread() == Thread.currentThread();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.xaml.ve.editor.IDiagramModelBuilder#saveCache()
	 */
	public void doSave(IProgressMonitor monitor) {
		ModelCacheUtility.doSaveCache(document, input, monitor);
	}

	public XamlNode getModel(Object textNode) {
		return fContext.getModel(textNode);
	}

	public IDOMNode getTextNode(Object model) {
		return fContext.getTextNode(model);
	}

	public void handleNodeChanged(TextNotification msg) {
		synchronized (synch) {
			if (!synch.isFree()) {
				return;
			}
			INodeNotifier notifier = msg.getNotifier();
			final Object changedFeature = msg.getChangedFeature();
			int eventType = msg.getEventType();
			final Object newValue = msg.getNewValue();
			final XamlNode parentNode = fContext.getModel(notifier);
			final XamlNode changedNode = fContext.getModel(changedFeature);
			if (eventType == INodeNotifier.CHANGE && changedFeature != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						if (changedNode != null && newValue == null) {
							// remove.
							parentNode.getAttributes().remove(changedNode);
						} else if (parentNode != null
								&& changedFeature instanceof IDOMAttr) {
							createAttribute(parentNode,
									(IDOMAttr) changedFeature, -1);
						}
						synch.setFree();
					}
				};
				synch.setEventType(EventType.SourceEvent);
				DisplayUtil.asyncExec(runnable);
			} else if (eventType == INodeNotifier.REMOVE && parentNode != null
					&& changedNode != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						if (changedNode instanceof XamlElement) {
							parentNode.getChildNodes().remove(changedNode);
						} else if (changedNode instanceof XamlAttribute) {
							parentNode.getAttributes().remove(changedNode);
						}
						synch.setFree();
					}
				};
				synch.setEventType(EventType.SourceEvent);
				DisplayUtil.asyncExec(runnable);
			} else if (eventType == INodeNotifier.STRUCTURE_CHANGED) {
				IJobManager jobManager = Job.getJobManager();
				Job currentJob = jobManager.currentJob();
				if (!(currentJob instanceof ReloadJob)
						&& (reloadJob == null || reloadJob.getResult() != null)) {
					reloadJob = new ReloadJob();
					reloadJob.setRule(new ISchedulingRule() {
						public boolean contains(ISchedulingRule rule) {
							return getClass() == rule.getClass();
						}

						public boolean isConflicting(ISchedulingRule rule) {
							return getClass() == rule.getClass();
						}
					});
					reloadJob.schedule(200);
				}
			}
		}
	}

	private class NodeAdapter implements INodeAdapter {

		public boolean isAdapterForType(Object type) {
			return type == NodeAdapter.class;
		}

		public void notifyChanged(INodeNotifier notifier, int eventType,
				Object changedFeature, Object oldValue, Object newValue, int pos) {
			TextNotification msg = new TextNotification(notifier, eventType,
					changedFeature, oldValue, newValue, pos);
			handleNodeChanged(msg);
		}

	}

	private class ReloadJob extends Job {
		public ReloadJob() {
			super("Reload");
			setPriority(SHORT);
			setSystem(true);
		}

		protected IStatus run(IProgressMonitor monitor) {
			reload();
			return Status.OK_STATUS;
		}
	}

}
