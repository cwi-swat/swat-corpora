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
package org.eclipse.e4.xwt.ui.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.metadata.IEvent;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.ui.editor.jdt.ASTHelper;
import org.eclipse.e4.xwt.vex.AbstractCodeSynchronizer;
import org.eclipse.e4.xwt.vex.dom.DomHelper;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * Generate InitializeComponent() method and all named elements;
 * 
 * @author jliu
 * 
 */
public class XWTCodeSynchronizer extends AbstractCodeSynchronizer {
	private static final String INIT_METHOD_NAME = "initializeComponent";

	private IType type;

	public XWTCodeSynchronizer(XWTEditor editor, IType type) {
		super(editor);
		this.type = type;
		if (type == null) {
			throw new NullPointerException("Java Source Type is Null!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.soyatec.eface.tools.ui.editors.jdt.CodeManager#remove(org.w3c.dom .Node)
	 */
	public void remove(final IDOMNode node) {
		final String remove = getNode2name().remove(node);
		if (remove != null) {
			Display display = Display.getDefault();
			if (display != null) {
				display.syncExec(new Runnable() {
					public void run() {
						String fullTypeName = getFullTypeName(node);
						if (fullTypeName == null) {
							return;
						}
						removeFields(fullTypeName, remove);
						buildInitialization();
						ASTHelper.removeUnusedImports(type);
					}
				});
			}
		}
	}

	protected String getFullTypeName(IDOMNode node) {
		String fullName = null;
		String nodeName = node.getNodeName();
		// Metaclass metaclass = UPF
		// .getMetaclass(nodeName, DomHelper.lookupNamespaceURI(node));
		// if (metaclass != null) {
		// fullName = metaclass.getType().getName();
		// }
		return fullName;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.soyatec.eface.tools.ui.editors.jdt.CodeManager#generateFields(java .lang.String, java.lang.String)
	 */
	public void generateFields(String fullTypeName, String argName) {
		ASTHelper.generateNamedFields(type, fullTypeName, argName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.soyatec.eface.tools.ui.editors.jdt.CodeManager#removeFields(java. lang.String, java.lang.String)
	 */
	public void removeFields(String fullTypeName, String argName) {
		try {
			IField[] fields = type.getFields();
			for (IField field : fields) {
				if (argName.equals(field.getElementName())) {
					field.delete(false, null);
				}
			}
		} catch (JavaModelException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.soyatec.eface.tools.ui.editors.jdt.CodeManager#buildInitialization()
	 */
	public void buildInitialization() {
		try {
			Set<String> forGens = new HashSet<String>(getNode2name().values());
			IField[] fields = type.getFields();
			// for (IField field : fields) {
			// String typeName = Signature.toString(field.getTypeSignature());
			// Metaclass metaclass = UPF.getMetaclass(typeName);
			// if (metaclass != null) {
			// forGens.add(field.getElementName());
			// }
			// }
			ASTHelper.generateInitialization(type, INIT_METHOD_NAME, forGens);
		} catch (Exception e) {
		}
	}

	/**
	 * TODO
	 * <p>
	 * sync XAML with Java
	 * </p>
	 * <ul>
	 * <li>field for each name element</li>
	 * <li>initializeComponent() with the findElement() to initialize the named Field</li>
	 * <li>add event handle</li>
	 * </ul>
	 */
	public boolean generateHandles() {
		StructuredTextViewer textViewer = getEditor().getTextEditor().getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);

		Node node = (Node) treeNode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode xmlnode = (IDOMNode) node;
		// generate java codes for named elements.
		updateCodeManager();

		String name = xmlnode.getNodeName();
		if (name.indexOf(":") > 0) {
			name = name.substring(name.indexOf(":") + 1);
		}
		IMetaclass metaclass = null;
		try {
			metaclass = XWT.getMetaclass(name, DomHelper.lookupNamespaceURI(xmlnode));
		} catch (Exception e1) {
			return false;
		}
		if (metaclass != null) {
			NamedNodeMap nodeMap = xmlnode.getAttributes();
			for (int i = nodeMap.getLength() - 1; i >= 0; i--) {
				IDOMAttr attrNode = (IDOMAttr) nodeMap.item(i);
				int endOffset = attrNode.getEndOffset();
				int startOffset = attrNode.getStartOffset();
				if (offset >= startOffset && offset <= endOffset) {
					String propertyName = attrNode.getName();
					IEvent event = metaclass.findEvent(propertyName);
					if (event == null) {
						int index = propertyName.indexOf('.');
						if (index != -1) {
							String typeName = propertyName.substring(0, index);
							String eventName = propertyName.substring(index + 1);
							metaclass = XWT.getMetaclass(typeName, DomHelper.lookupNamespaceURI(attrNode));
							if (metaclass != null) {
								event = metaclass.findEvent(eventName);
							}
						}
					}
					if (event != null) {
						try {
							ASTHelper.generateEventHandler(type, Event.class.getName(), attrNode.getValue());
							return true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
		return false;
	}

	public boolean openDefinition() {
		StructuredTextViewer textViewer = getEditor().getTextEditor().getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node node = (Node) treeNode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode xmlnode = (IDOMNode) node;

		String name = xmlnode.getNodeName();
		// Metaclass metaclass = UPF.getMetaclass(name, DomHelper.lookupNamespaceURI(node));
		// if (metaclass != null) {
		// NamedNodeMap nodeMap = xmlnode.getAttributes();
		// int length = nodeMap.getLength();
		// for (int i = length - 1; i >= 0; i--) {
		// IDOMAttr attrNode = (IDOMAttr) nodeMap.item(i);
		// String attrName = attrNode.getName();
		// String attrValue = attrNode.getValue();
		// int startOffset = attrNode.getStartOffset();
		// int endOffset = startOffset + attrName.length()
		// + attrValue.length() + 3;
		// // if (offset <= startOffset || offset >= endOffset) {
		// // }
		// if (offset >= startOffset && offset < endOffset) {
		// Event event = metaclass.findEvent(attrName);
		// if (event != null) {
		// String[] handlers = null;
		// IMethod[] methods = null;
		// try {
		// methods = type.getMethods();
		// handlers = new String[methods.length];
		// for (int j = 0; j < methods.length; j++) {
		// IMethod method = methods[j];
		// String methodName = method.getElementName();
		// if (methodName.equals(attrValue)) {
		// jumpToJavaMethod(methods, methodName);
		// return true;
		// }
		// handlers[j] = methodName;
		// }
		// } catch (JavaModelException e) {
		// }
		// }
		// }
		// }
		// }
		return false;
	}

	public boolean handleInputChanged(IDocument newInput) {
		String value = newInput.get();
		if (checkContent(value)) {
			return false;
		}

		StructuredTextViewer textViewer = getEditor().getTextEditor().getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node node = (Node) treeNode;
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		IDOMNode xmlnode = (IDOMNode) node;

		String name = xmlnode.getNodeName();
		// Metaclass metaclass = UPF.getMetaclass(name, DomHelper.lookupNamespaceURI(xmlnode));
		// if (metaclass != null) {
		// NamedNodeMap nodeMap = xmlnode.getAttributes();
		// int length = nodeMap.getLength();
		// if (length == 0) {
		// // remove
		// update(xmlnode);
		// }
		// for (int i = length - 1; i >= 0; i--) {
		// IDOMAttr attrNode = (IDOMAttr) nodeMap.item(i);
		// String attrName = attrNode.getName();
		// String attrValue = attrNode.getValue();
		// int startOffset = attrNode.getStartOffset();
		// int endOffset = startOffset + attrName.length()
		// + attrValue.length() + 3;
		// // if (offset <= startOffset || offset >= endOffset) {
		// // }
		// if (offset >= startOffset && offset < endOffset) {
		// final Node genNode = xmlnode;
		// Display.getDefault().syncExec(new Runnable() {
		// public void run() {
		// update((IDOMNode) genNode);
		// }
		// });
		//
		// Event event = metaclass.findEvent(attrName);
		// if (event != null) {
		// String[] handlers = null;
		// IMethod[] methods = null;
		// try {
		// methods = type.getMethods();
		// handlers = new String[methods.length];
		// for (int j = 0; j < methods.length; j++) {
		// IMethod method = methods[j];
		// String methodName = method.getElementName();
		// if (methodName.equals(attrValue)) {
		// return true;
		// }
		// handlers[j] = methodName;
		// }
		// } catch (JavaModelException e) {
		// e.printStackTrace();
		// }
		//
		// Display display = Display.getDefault();
		// String title = "New or rename handler";
		// EventHandlerDialog eventHandlerDialog = new EventHandlerDialog(
		// textViewer, getOldAttrValue(), attrValue,
		// handlers);
		// StyledText styledText = textViewer.getTextWidget();
		// Point localLocation = styledText.getLocationAtOffset(offset);
		//						
		// eventHandlerDialog.run(styledText.getShell(), title, styledText.toDisplay(localLocation));
		// EventHandlerDialog.Operation operation = eventHandlerDialog
		// .getOperation();
		// String inputHandler = eventHandlerDialog
		// .getInputHandler();
		// String argumentType = event.getType().getType()
		// .getName();
		// IProgressMonitor monitor = getProgressMonitor();
		// if (operation == EventHandlerDialog.Operation.New) {
		// attrNode.setValue(inputHandler);
		// try {
		// ASTHelper.generateEventHandler(type,
		// argumentType, inputHandler);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else if (operation == EventHandlerDialog.Operation.Rename) {
		// IMethod method = findMethod(methods,
		// getOldAttrValue());
		// modifyAllRelativeHandlerName(textViewer, 0,
		// inputHandler, getOldAttrValue());
		// attrNode.setValue(inputHandler);
		// if (method != null) {
		// try {
		// method.rename(inputHandler, true, monitor);
		// } catch (JavaModelException e) {
		// e.printStackTrace();
		// }
		// } else {
		// try {
		// ASTHelper.generateEventHandler(type,
		// argumentType, inputHandler);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// } else if (operation == EventHandlerDialog.Operation.Select) {
		// attrNode.setValue(inputHandler);
		// } else if (operation == EventHandlerDialog.Operation.Cancel) {
		// }
		// if (operation != EventHandlerDialog.Operation.Cancel) {
		// jumpToJavaMethod(methods, inputHandler);
		// }
		// setCacheContent(updateCacheContent(newInput));
		// return false;
		// }
		// }
		// }
		// }
		return true;
	}

	private void jumpToJavaMethod(IMethod[] methods, String methodName) {
		IJavaElement method = findMethod(methods, methodName);
		CompilationUnitEditor javaEditor = ((XWTEditor) getEditor()).getJavaEditor();
		if (method != null) {
			javaEditor.setSelection(method);
		}
		getEditor().setActiveEditor(javaEditor);
	}

	private IMethod findMethod(IMethod[] methods, String methodName) {
		for (int i = 0; i < methods.length; i++) {
			IMethod method = methods[i];
			if (method.getElementName().equals(methodName))
				return method;
		}
		return null;
	}

	private void modifyAllRelativeHandlerName(StructuredTextViewer textViewer, int offset, String inputHandler, String oldAttrValue) {
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node node = (Node) treeNode;
		modifyHandlerName(inputHandler, node, oldAttrValue);
	}

	private void modifyHandlerName(String inputHandler, Node node, String oldAttrValue) {
		while ((node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getParentNode() != null)) {
			node = node.getParentNode();
		}
		String name = node.getNodeName();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			// Metaclass metaclass = UPF
			// .getMetaclass(name, DomHelper.lookupNamespaceURI(node));
			// if (metaclass != null) {
			// NamedNodeMap nodeMap = node.getAttributes();
			// for (int j = nodeMap.getLength() - 1; j >= 0; j--) {
			// IDOMAttr attrNode = (IDOMAttr) nodeMap.item(j);
			// String attrName = attrNode.getName();
			// String attrValue = attrNode.getValue();
			// Event event = metaclass.findEvent(attrName);
			// if (event != null) {
			// if (attrValue.equals(oldAttrValue)) {
			// attrNode.setNodeValue(inputHandler);
			// }
			// }
			// }
			// }
		}
		NodeList nodes = node.getChildNodes();
		int length = nodes.getLength();
		for (int i = 0; i < length; i++) {
			Node childNode = nodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				modifyHandlerName(inputHandler, childNode, oldAttrValue);
			}
		}
	}
}
