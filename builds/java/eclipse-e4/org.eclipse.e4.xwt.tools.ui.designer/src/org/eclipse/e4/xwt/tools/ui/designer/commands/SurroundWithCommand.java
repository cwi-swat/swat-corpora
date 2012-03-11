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
package org.eclipse.e4.xwt.tools.ui.designer.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutType;
import org.eclipse.e4.xwt.tools.ui.designer.parts.CompositeEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.SashFormEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.ILayoutEditPolicy;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutPolicyHelper;
import org.eclipse.e4.xwt.tools.ui.designer.policies.layout.grid.GridLayoutPolicyHelper.GridComponent;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.e4.xwt.tools.ui.xaml.tools.AnnotationTools;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.widgets.Group;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SurroundWithCommand extends Command {

	protected List<EditPart> selectedObjects;
	protected List<WidgetEditPart> surroundings;
	protected Class<?> type;
	protected EditPart parent;
	protected LayoutType layoutType;

	protected Command executeCommand;

	public SurroundWithCommand(List<EditPart> selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (selectedObjects == null || selectedObjects.isEmpty()) {
			return false;
		}

		// Check parent, all selected objects should have a same parent.
		for (EditPart child : selectedObjects) {
			if (parent == null) {
				parent = child.getParent();
			} else if (parent != child.getParent()) {
				return false;
			}
			
			Object model = child.getModel();
			if (model instanceof EObject) {
				EObject object = (EObject) model;
				if (object.eContainer() == null) {
					return false;
				}
			}
			
		}
		if (parent == null) {
			return false;
		}
		ILayoutEditPolicy layoutEdiPolicy = (ILayoutEditPolicy) parent.getAdapter(ILayoutEditPolicy.class);
		if (layoutEdiPolicy == null) {
			return false;
		}
		layoutType = layoutEdiPolicy.getType();

		surroundings = new ArrayList<WidgetEditPart>();

		// do quickly check
		if (selectedObjects.size() == 1) {
			EditPart editPart = selectedObjects.get(0);
			if (!(editPart instanceof WidgetEditPart)) {
				return false;
			} else if (((WidgetEditPart) editPart).isRoot()) {
				return false;
			}
			surroundings.add((WidgetEditPart) editPart);
			return true;
		}

		if (layoutType == LayoutType.GridLayout) {
			//
			// TODO: make a smart check.
			//
			int min = -1, max = -1;
			XamlNode parentNode = (XamlNode) parent.getModel();
			EList<XamlElement> childNodes = parentNode.getChildNodes();
			for (EditPart child : selectedObjects) {
				XamlNode node = (XamlNode) child.getModel();
				int index = childNodes.indexOf(node);
				if (min == -1) {
					min = index;
				} else {
					min = Math.min(min, index);
				}
				if (max == -1) {
					max = index;
				} else {
					max = Math.max(max, index);
				}
			}
			for (int i = min; i <= max; i++) {
				XamlElement child = childNodes.get(i);
				Object childEp = parent.getViewer().getEditPartRegistry().get(child);
				if (AnnotationTools.isAnnotated(child, GridLayoutPolicyHelper.FILLER_DATA)) {
					surroundings.add((WidgetEditPart) childEp);
				} else if (selectedObjects.contains(childEp)) {
					surroundings.add((WidgetEditPart) childEp);
				} else {
					return false;
				}
			}
		} else {
			for (EditPart child : selectedObjects) {
				if (child instanceof WidgetEditPart) {
					surroundings.add((WidgetEditPart) child);
				}
			}
		}
		return layoutType != null && !surroundings.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		XamlNode parentModel = (XamlNode) parent.getModel();
		sort(surroundings, parentModel.getChildNodes());
		CompoundCommand commands = new CompoundCommand();
		XamlElement newParent = createParent();
		if (parent instanceof SashFormEditPart) {
			int index = -1;
			for (WidgetEditPart child : surroundings) {
				XamlElement castModel = (XamlElement) child.getCastModel();
				if (index == -1) {
					index = parentModel.getChildNodes().indexOf(castModel);
				}
				
				XamlElement newChild = (XamlElement) EcoreUtil.copy(castModel);
				newParent.getChildNodes().add(newChild);
			}

			commands.add(new AddNewChildCommand(parentModel, newParent, index));
		}
		else if (LayoutType.NullLayout == layoutType) {
			int x = -1, y = -1, right = 0, bottom = 0;
			for (WidgetEditPart child : surroundings) {
				Rectangle r = child.getVisualInfo().getBounds();
				if (x == -1) {
					x = r.x;
				} else {
					x = Math.min(x, r.x);
				}
				if (y == -1) {
					y = r.y;
				} else {
					y = Math.min(y, r.y);
				}
				right = Math.max(right, r.right());
				bottom = Math.max(bottom, r.bottom());
			}
			XamlAttribute boundsAttr = XamlFactory.eINSTANCE.createAttribute("bounds", IConstants.XWT_NAMESPACE);
			boundsAttr.setValue(StringUtil.format(new Rectangle(x, y, right - x, bottom - y)));
			newParent.getAttributes().add(boundsAttr);

			for (WidgetEditPart child : surroundings) {
				XamlElement castModel = (XamlElement) child.getCastModel();

				Rectangle r = child.getVisualInfo().getBounds().getCopy();
				r.translate(-x, -y);
				XamlElement newChild = (XamlElement) EcoreUtil.copy(castModel);
				ChangeConstraintCommand cmd = new ChangeConstraintCommand(newChild, r);
				cmd.execute();

				newParent.getChildNodes().add(newChild);
			}

			commands.add(new AddNewChildCommand(parentModel, newParent));
		} else if (LayoutType.GridLayout == layoutType) {
			int numRows = -1, numColumns = 0;
			if (surroundings.size() == 1) {
				numRows = numColumns = 1;
			} else {
				GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper();
				helper.setHost((CompositeEditPart) parent);
				GridComponent[][] layoutTable = helper.getLayoutTable();
				for (int i = 0; i < layoutTable.length; i++) {
					GridComponent[] cells = layoutTable[i];
					int row = 0;
					boolean recorded = false;
					for (int j = 0; j < cells.length; j++) {
						GridComponent cell = cells[j];
						XamlNode model = cell.getModel();
						Object childEp = parent.getViewer().getEditPartRegistry().get(model);
						if (!surroundings.contains(childEp)) {
							continue;
						}
						if (!recorded) {
							int span = cell.getSpanWidth();
							if (span > 1) {
								numColumns += span;
							} else {
								numColumns++;
							}
							recorded = true;
						}
						int spanHeight = cell.getSpanHeight();
						if (spanHeight > 1) {
							row += spanHeight;
						} else {
							row++;
						}
					}
					numRows = Math.max(numRows, row);
				}
			}

			int index = -1;
			EList<XamlElement> childNodes = parentModel.getChildNodes();
			for (WidgetEditPart child : surroundings) {
				XamlElement castModel = (XamlElement) child.getCastModel();
				int i = childNodes.indexOf(castModel);
				if (index == -1) {
					index = i;
				} else {
					index = Math.min(index, i);
				}
				XamlElement newChild = (XamlElement) EcoreUtil.copy(castModel);
				newParent.getChildNodes().add(newChild);
			}

			// GridLayout
			XamlElement gridLayout = XamlFactory.eINSTANCE.createElement("GridLayout", IConstants.XWT_NAMESPACE);
			XamlAttribute numColumnsAttr = XamlFactory.eINSTANCE.createAttribute("numColumns", IConstants.XWT_NAMESPACE);
			numColumnsAttr.setValue(Integer.toString(numColumns));
			gridLayout.getAttributes().add(numColumnsAttr);

			XamlAttribute gridLayoutAttr = XamlFactory.eINSTANCE.createAttribute("layout", IConstants.XWT_NAMESPACE);
			gridLayoutAttr.getChildNodes().add(gridLayout);

			newParent.getAttributes().add(gridLayoutAttr);

			// GridData
			XamlElement gridData = XamlFactory.eINSTANCE.createElement("GridData", IConstants.XWT_NAMESPACE);
			createAttr(gridData, "horizontalAlignment", "SWT.FILL");
			createAttr(gridData, "verticalAlignment", "SWT.FILL");
			// createAttr(gridData, "grabExcessHorizontalSpace", "true");
			// createAttr(gridData, "grabExcessVerticalSpace", "true");
			createAttr(gridData, "horizontalSpan", Integer.toString(numColumns));
			createAttr(gridData, "verticalSpan", Integer.toString(numRows));

			XamlAttribute gridDataAttr = XamlFactory.eINSTANCE.createAttribute("layoutData", IConstants.XWT_NAMESPACE);
			gridDataAttr.getChildNodes().add(gridData);

			newParent.getAttributes().add(gridDataAttr);

			commands.add(new AddNewChildCommand(parentModel, newParent, index));
		} else if (LayoutType.RowLayout == layoutType || LayoutType.FillLayout == layoutType) {
			XamlAttribute a = parentModel.getAttribute("layout");
			if (a != null) {
				XamlAttribute layoutAttr = (XamlAttribute) EcoreUtil.copy(a);
				newParent.getAttributes().add(layoutAttr);
			}
			int index = -1;
			EList<XamlElement> childNodes = parentModel.getChildNodes();
			for (WidgetEditPart child : surroundings) {
				XamlElement castModel = (XamlElement) child.getCastModel();
				int i = childNodes.indexOf(castModel);
				if (index == -1) {
					index = i;
				} else {
					index = Math.min(index, i);
				}
				XamlElement newChild = (XamlElement) EcoreUtil.copy(castModel);
				newParent.getChildNodes().add(newChild);
			}
			commands.add(new AddNewChildCommand(parentModel, newParent, index));
		}

		List<XamlNode> forDelete = new ArrayList<XamlNode>();
		for (WidgetEditPart node : surroundings) {
			forDelete.add(node.getCastModel());
		}

		commands.add(new DeleteCommand(forDelete));

		executeCommand = commands.unwrap();
		if (executeCommand.canExecute()) {
			executeCommand.execute();
		}
	}

	private void sort(List<WidgetEditPart> surroundings, final List<XamlElement> childNodes) {
		Collections.sort(surroundings, new Comparator<WidgetEditPart>() {
			public int compare(WidgetEditPart o1, WidgetEditPart o2) {
				int i1 = childNodes.indexOf(o1.getCastModel());
				int i2 = childNodes.indexOf(o2.getCastModel());
				return i1 - i2;
			}

		});
	}

	private XamlAttribute createAttr(XamlNode parent, String name, String value) {
		XamlAttribute attr = XamlFactory.eINSTANCE.createAttribute(name, IConstants.XWT_NAMESPACE);
		attr.setValue(value);

		parent.getAttributes().add(attr);

		return attr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		executeCommand.undo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return executeCommand != null && executeCommand.canUndo();
	}

	protected XamlElement createParent() {
		XamlElement newParent = XamlFactory.eINSTANCE.createElement(type.getSimpleName(), IConstants.XWT_NAMESPACE);
		if (Group.class == type) {
			XamlAttribute textAttr = XamlFactory.eINSTANCE.createAttribute("text", IConstants.XWT_NAMESPACE);
			textAttr.setValue("New Group");
			newParent.getAttributes().add(textAttr);
		}
		return newParent;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	static class Counter {
		List<Integer> indexes;
		int count = 0;

		void add(int index, int span) {
			if (indexes == null) {
				indexes = new ArrayList<Integer>();
			}
			if (indexes.contains(index)) {
				return;
			}
			indexes.add(index);
			if (span == 0) {
				count++;
			} else {
				count += span;
			}
		}
	}
}
