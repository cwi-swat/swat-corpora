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
package org.eclipse.e4.xwt.tools.ui.designer.editor.sash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

/**
 * Delete the elements and update the weights
 * 
 * @author jliu jin.liu@soyatec.com
 */
public class SashFormDeleteCommand extends Command {
	private List<XamlNode> deleteNodes;
	private Map<EObject, DeleteData> deletedObjects = new HashMap<EObject, DeleteData>(
			1);

	public SashFormDeleteCommand(List deleteNodes) {
		this.deleteNodes = convertNodes(deleteNodes);
	}

	public SashFormDeleteCommand(XamlNode singleNode) {
		this(Collections.singletonList(singleNode));
	}

	/**
	 * @param deleteNodes
	 * @return
	 */
	private List<XamlNode> convertNodes(List deleteNodes) {
		if (deleteNodes == null) {
			return null;
		}
		List<XamlNode> nodes = new ArrayList<XamlNode>();
		for (Iterator iterator = deleteNodes.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			if (obj instanceof EditPart) {
				obj = ((EditPart) obj).getModel();
			}
			if (obj instanceof XamlNode) {
				nodes.add((XamlNode) obj);
			}
		}
		return nodes;
	}

	public boolean canExecute() {
		return deleteNodes != null && !deleteNodes.isEmpty();
	}

	public void execute() {
		for (XamlNode deleted : deleteNodes) {
			DeleteData dd = new DeleteData();
			dd.parent = (EObject) deleted.eContainer();
			if (deleted instanceof XamlElement) {
				if (dd.parent instanceof XamlNode) {
					dd.index = ((XamlNode) dd.parent).getChildNodes().indexOf(
							deleted);
					if (dd.index > -1) {
						((XamlNode) dd.parent).getChildNodes().remove(dd.index);
					}
				} else if (dd.parent instanceof XamlDocument) {
					((XamlDocument) dd.parent).setRootElement(null);
				}
			} else if (deleted instanceof XamlAttribute
					&& dd.parent instanceof XamlNode) {
				dd.index = ((XamlNode) dd.parent).getAttributes().indexOf(
						deleted);
				if (dd.index > -1) {
					((XamlNode) dd.parent).getAttributes().remove(dd.index);
				}
			}
			deletedObjects.put(deleted, dd);
		}
	}

	public boolean canUndo() {
		return !deletedObjects.isEmpty();
	}

	public void undo() {
		Set<EObject> deletedKeys = deletedObjects.keySet();
		for (EObject deleted : deletedKeys) {
			DeleteData dd = deletedObjects.get(deleted);
			if (dd == null) {
				continue;
			}
			EObject parent = dd.parent;
			int index = dd.index;
			if (deleted instanceof XamlElement) {
				if (parent instanceof XamlNode) {
					if (index > -1) {
						((XamlNode) parent).getChildNodes().add(index,
								(XamlElement) deleted);
					} else {
						((XamlNode) parent).getChildNodes().add(
								(XamlElement) deleted);
					}
				} else if (parent instanceof XamlDocument) {
					((XamlDocument) parent)
							.setRootElement((XamlElement) deleted);
				}
			} else if (deleted instanceof XamlAttribute
					&& parent instanceof XamlNode) {
				if (index > -1) {
					((XamlNode) parent).getAttributes().add(index,
							(XamlAttribute) deleted);
				} else {
					((XamlNode) parent).getAttributes().add(
							(XamlAttribute) deleted);
				}
			}
		}
		deletedKeys.clear();
		deletedObjects.clear();
	}

	static class DeleteData {
		EObject parent;
		int index = -1;
	}
}