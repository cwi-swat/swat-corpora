/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.       *
 * All rights reserved. This program and the accompanying materials            *
 * are made available under the terms of the Eclipse Public License v1.0       *
 * which accompanies this distribution, and is available at                    *
 * http://www.eclipse.org/legal/epl-v10.html                                   *  
 * Contributors:                                                               *  
 *     Soyatec - initial API and implementation                                * 
 *******************************************************************************/
package org.eclipse.e4.xwt.ui.editor.dnd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.xwt.ui.editor.XWTEditor;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.e4.xwt.vex.VEXTextEditorHelper;
import org.eclipse.e4.xwt.vex.toolpalette.ContextType;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu jin.liu@soyatec.com
 */
public class UserDefinedDnDAdapter extends DnDAdapterImpl {

	private Map<String, String> nsURIs = new HashMap<String, String>();
	private Map<String, Boolean> prefixStatus = new HashMap<String, Boolean>();
	private String name;
	private String namespace;
	private String prefix;

	/**
	 * @param editor
	 */
	public UserDefinedDnDAdapter(XWTEditor editor) {
		super(editor);
		updateNsURIs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#isAccept(java.lang.Object)
	 */
	public boolean isAccept(Object obj) {
		if (obj instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) obj;
			IType type = unit.findPrimaryType();
			String name = type.getElementName();
			IJavaProject javaProject = unit.getJavaProject();
			String fullyQualifiedName = type.getFullyQualifiedName();
			try {
				ProjectContext context = ProjectContext.getContext(javaProject);
				Class<?> clazz = context.getClassLoader().loadClass(fullyQualifiedName);
				this.name = clazz.getSimpleName();
				this.namespace = "clr-namespace" + ":" + type.getPackageFragment().getElementName();
				return Composite.class.isAssignableFrom(clazz) && clazz.getResource(name + ".xwt") != null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (obj instanceof IFile) {
			IFile file = (IFile) obj;
			String ext = file.getFileExtension();
			String name = file.getName();

			if ("xwt".equals(ext)) {
				IResource java = file.getParent().findMember(name.replace("xwt", "java"));
				if (java.exists()) {
					IProject project = file.getProject();
					IJavaProject javaProject = JavaCore.create(project);
					try {
						ICompilationUnit unit = (ICompilationUnit) JavaCore.create(java);
						IType type = unit.findPrimaryType();
						String fullyQualifiedName = type.getFullyQualifiedName();
						ProjectContext context = ProjectContext.getContext(javaProject);
						Class<?> clazz = context.getClassLoader().loadClass(fullyQualifiedName);
						this.name = clazz.getSimpleName();
						this.namespace = "clr-namespace" + ":" + type.getPackageFragment().getElementName();
						return Composite.class.isAssignableFrom(clazz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public String getName() {
		String p = getPrefix();
		return p == null ? name : p + ":" + name;
	}

	private void updateNsURIs() {
		StructuredTextEditor textEditor = getEditor().getTextEditor();
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		IDOMNode node = VEXTextEditorHelper.getNode(textViewer, 0);
		updateNsURIs(node, nsURIs);
	}

	private String genPrefix(Collection<String> existings) {
		char[] c = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'c', 'v', 'b', 'n', 'm' };
		Random random = new Random();
		String prefix = Character.toString(c[Math.abs(random.nextInt()) % c.length]);
		while (existings.contains(prefix)) {
			prefix = Character.toString(c[Math.abs(random.nextInt()) % c.length]);
		}
		return prefix;
	}

	public void updateNsURIs(Object obj, Map<String, String> nsURIs) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			String p = node.getPrefix();
			String ns = node.getNamespaceURI();
			if (ns != null) {
				nsURIs.put(ns, p);
			}
			NamedNodeMap attributes = node.getAttributes();
			if (attributes != null) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Node attr = attributes.item(i);
					String nodeName = attr.getNodeName();
					if (nodeName.startsWith("xmlns:")) {
						p = nodeName.substring("xmlns:".length());
						ns = attr.getNodeValue();
						if (ns != null) {
							nsURIs.put(ns, p);
						}
					}
				}
			}
			NodeList childNodes = node.getChildNodes();
			if (childNodes != null) {
				for (int i = 0; i < childNodes.getLength(); i++) {
					updateNsURIs(childNodes.item(i), nsURIs);
				}
			}
		}
	}

	public String getContent() {
		return "<" + getName() + "/>";
	}

	public String getPrefix() {
		updateNsURIs();
		prefix = nsURIs.get(namespace);
		if (prefix == null) {
			prefix = genPrefix(nsURIs.values());
			prefixStatus.put(prefix, Boolean.TRUE);
			nsURIs.put(namespace, prefix);
		}
		return prefix;
	}

	public boolean isNsURINew() {
		String p = getPrefix();
		if (p == null) {
			return false;
		}
		return Boolean.TRUE.equals(prefixStatus.get(p));
	}

	public String getNamespace() {
		return namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#createTemplate(java.lang.Object)
	 */
	protected Template createTemplate(Object selection) {
		return new Template(name, "", ContextType.XML_TAG.getName(), getContent(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapterImpl#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		if (!isAccept()) {
			return;
		}
		String prefix = getPrefix();
		if (prefix != null && isNsURINew()) {
			String pattern = "xmlns:" + prefix + "=\"" + getNamespace() + "\" ";
			Template tem = new Template("xmlns:" + prefix, "", ContextType.XML_ATTRIBUTE.getName(), pattern, true);
			StructuredTextEditor textEditor = getEditor().getTextEditor();
			StructuredTextViewer textViewer = textEditor.getTextViewer();
			IDOMNode node = VEXTextEditorHelper.getNode(textViewer, 0);
			NamedNodeMap attributes = node.getAttributes();
			int dropNsIndex = 0;
			for (int i = 0; i < attributes.getLength(); i++) {
				IDOMAttr attr = (IDOMAttr) attributes.item(i);
				String nodeName = attr.getNodeName();
				if ("xmlns:x".equals(nodeName)) {
					dropNsIndex = attr.getEndOffset();
					break;
				}
			}
			setDropCaretOffset(getDropCaretOffset() + pattern.length());
			drop(tem, dropNsIndex, 0);
			prefixStatus.put(prefix, Boolean.FALSE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getContextType()
	 */
	public ContextType getContextType() {
		return ContextType.XML_TAG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.ui.editor.dnd.DnDAdapter#getScope()
	 */
	public String getScope() {
		return "Composite";
	}

}
