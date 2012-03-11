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
package org.eclipse.e4.xwt.tools.ui.designer.properties.tabbed.sections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.IConstants;
import org.eclipse.e4.xwt.IXWTLoader;
import org.eclipse.e4.xwt.XWTException;
import org.eclipse.e4.xwt.XWTLoaderManager;
import org.eclipse.e4.xwt.core.IBinding;
import org.eclipse.e4.xwt.core.IDynamicBinding;
import org.eclipse.e4.xwt.internal.utils.LoggerManager;
import org.eclipse.e4.xwt.tools.ui.designer.commands.AddNewChildCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.ApplyAttributeSettingCommand;
import org.eclipse.e4.xwt.tools.ui.designer.commands.DeleteCommand;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.designer.loader.ResourceVisitor;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTClassLoaderUtil;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTVisualLoader;
import org.eclipse.e4.xwt.tools.ui.designer.model.RefreshAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.parts.WidgetEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTModelUtil;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlFactory;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public abstract class AbstractAttributeSection extends AbstractPropertySection
		implements Listener {

	private XamlNode parent;
	private XamlAttribute attribute;
	private WidgetEditPart editPart;
	private Label attrLable;
	static Map<String, Object> EMPTY_MAP = Collections.emptyMap();
	private boolean needToRefresh = false;

	// private RefreshAdapter refreshAdapter;
	private Map<XamlNode, RefreshAdapter> refresherMap;
	protected Display display;

	public boolean isNeedToRefresh() {
		return needToRefresh;
	}

	public void setNeedToRefresh(boolean needToRefresh) {
		this.needToRefresh = needToRefresh;
	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		_createControls(parent, aTabbedPropertySheetPage);

		Object layoutData = parent.getLayoutData();
		if (layoutData != null && layoutData instanceof GridData) {
			GridData gd = (GridData) layoutData;
			gd.verticalAlignment = GridData.BEGINNING;
			gd.grabExcessVerticalSpace = false;
		}

		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		parent.setLayout(layout);

		attrLable = getWidgetFactory().createLabel(parent, "");
		Control section = createSection(parent);
		display = section.getDisplay();
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createClearButton(parent);
	}

	protected void _createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
	 * shouldUseExtraSpace()
	 */
	public boolean shouldUseExtraSpace() {
		return true;
	}

	protected void handleModelChanged(Notification msg) {
		if (msg.isTouch()) {
			return;
		}
		Object notifier = msg.getNotifier();
		if (notifier == null) {
			return;
		}
		XamlAttribute attribute = getAttribute();
		if (notifier == attribute) {
			setNeedToRefresh(true);
		} else if (notifier == parent
				&& (attribute == msg.getOldValue() || attribute == msg
						.getNewValue())) {
			setNeedToRefresh(true);
		}
		refresh();
	}

	protected void createClearButton(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		toolBar.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		ToolItem clearAction = new ToolItem(toolBar, SWT.PUSH);
		clearAction.setImage(ImageShop.get(ImageShop.IMG_CLEAR_FILTER));
		clearAction.setToolTipText("Clear setting values.");
		clearAction.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				clearProperty();
			}
		});
	}

	protected void clearProperty() {
		executeCommand(new DeleteCommand(getAttribute()));
	}

	public XamlAttribute getAttribute() {
		// Maybe attribute had been created.
		if (attribute != null && attribute.eContainer() == null) {
			attribute = XWTModelUtil.getAdaptableAttribute(parent,
					getAttributeName(), IConstants.XWT_NAMESPACE);
		}
		if (attribute == null) {
			attribute = createAttribute(parent);
		}
		return attribute;
	}

	public XamlNode getParent() {
		return parent;
	}

	protected void applyEditPart(WidgetEditPart editPart) {
		if (this.editPart != null && this.editPart == editPart) {
			return;
		}
		setNeedToRefresh(true);
		this.editPart = editPart;
		if (attrLable != null && !attrLable.isDisposed()) {
			String name = getAttributeName();
			if (name != null && name.length() > 0) {
				attrLable.setText(Character.toUpperCase(name.charAt(0))
						+ name.substring(1) + ":");
				attrLable.getParent().layout();
			}
		}
		parent = editPart.getCastModel();
		attribute = createAttribute(parent);
		getRefresher().addListenedAttr(getAttributeName());
	}

	protected RefreshAdapter getRefresher() {
		if (refresherMap == null) {
			refresherMap = new HashMap<XamlNode, RefreshAdapter>();
		}
		RefreshAdapter refreshAdapter = refresherMap.get(parent);
		if (refreshAdapter == null) {
			refreshAdapter = new RefreshAdapter(parent) {
				protected void performRefresh(Notification msg) {
					handleModelChanged(msg);
				}
			};
			refresherMap.put(parent, refreshAdapter);
		}
		return refreshAdapter;
	}

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Object object = ((IStructuredSelection) selection).getFirstElement();
		if (object instanceof WidgetEditPart) {
			applyEditPart((WidgetEditPart) object);
		} else if (object instanceof EditPart) {
			IWorkbenchPage activePage = part.getSite().getWorkbenchWindow()
					.getActivePage();
			IEditorPart activeEditor = activePage.getActiveEditor();
			GraphicalViewer graphicalViewer = (GraphicalViewer) activeEditor
					.getAdapter(GraphicalViewer.class);
			if (graphicalViewer != null) {
				EditPart editPart = (EditPart) graphicalViewer
						.getEditPartRegistry().get(
								((EditPart) object).getModel());
				if (editPart instanceof WidgetEditPart) {
					applyEditPart((WidgetEditPart) editPart);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		XamlAttribute attribute = getAttribute();
		Command command = null;
		if (parent.getAttributes().contains(attribute)) {
			command = new ApplyAttributeSettingCommand(parent, attribute,
					getNewValue(event));
		} else {
			attribute.setValue(getNewValue(event));
			command = new AddNewChildCommand(parent, attribute);
		}
		executeCommand(command);
	}

	protected void executeCommand(Command command) {
		if (command == null || !command.canExecute()) {
			return;
		}
		getRefresher().setRefreshRequired(false);
		EditDomain.getEditDomain(editPart).getCommandStack().execute(command);
		getRefresher().setRefreshRequired(true);
	}

	protected String getValue() {
		XamlAttribute attribute = getAttribute();
		if (attribute == null || attribute.eContainer() == null) {
			return null;
		}
		return attribute.getValue();
	}

	protected void setTextValue(Text textWidget) {
		Object value = getValue();
		if (value == null) {
			XamlNode[] children = attribute.getChildNodes().toArray(
					new XamlNode[0]);
			IXWTLoader xwtLoader = XWTLoaderManager.getActive();
			for (XamlNode child : children) {
				String name = child.getName();
				String ns = child.getNamespace();
				if (name.equalsIgnoreCase(IConstants.XAML_X_STATIC)
						&& ns.equals(IConstants.XWT_X_NAMESPACE)) {
					value = (String) getStaticValue(child, xwtLoader);
				} else if (name.equals(IConstants.XAML_BINDING)) {
					Class<?> type = java.lang.String.class;
					ResourceVisitor resourceVisitor = new ResourceVisitor(
							(XWTVisualLoader) xwtLoader);
					try {
						value = resourceVisitor.doCreate(child.getParent(),
								(XamlElement) child, type, EMPTY_MAP);
						if (value != null) {
							if (value instanceof IDynamicBinding) {
								IDynamicBinding binding = (IDynamicBinding) value;
								binding.setType("text");
								binding.setControl(editPart.getWidget());
								binding.setHost(editPart.getModel());
							}
							if (!type.isAssignableFrom(value.getClass())
									|| value instanceof IBinding) {
								Object orginalValue = value;
								IConverter converter = ((XWTVisualLoader) xwtLoader)
										.findConvertor(value.getClass(), type);
								if (converter != null) {
									value = converter.convert(value);
									if (value != null
											&& orginalValue instanceof IBinding
											&& !type.isAssignableFrom(value
													.getClass())) {
										converter = ((XWTVisualLoader) xwtLoader)
												.findConvertor(
														value.getClass(), type);
										if (converter != null) {
											value = converter.convert(value);
										} else {
											LoggerManager
													.log(new XWTException(
															"Convertor "
																	+ value.getClass()
																			.getSimpleName()
																	+ "->"
																	+ type.getSimpleName()
																	+ " is not found"));
										}
									}
								}
							}
						}
					} catch (Exception e) {
						LoggerManager.log(e);
					}
				}
			}
		}
		textWidget.setText((value == null ? "" : (String) value));
	}

	protected Object getStaticValue(XamlNode child, IXWTLoader xwtLoader) {
		XamlNode[] children = child.getChildNodes().toArray(new XamlNode[0]);
		if (children.length == 1) {
			XamlElement element = (XamlElement) children[0];
			if (element != null) {
				return XWTClassLoaderUtil.loadStaticMember(
						((XWTVisualLoader) xwtLoader).getLoadingContext(),
						element);
			}
		}
		return null;
	}

	public WidgetEditPart getEditPart() {
		return editPart;
	}

	protected XamlAttribute createAttribute(XamlNode parent) {
		String attrName = getAttributeName();
		if (attrName == null) {
			throw new NullPointerException("Attribute name is null");
		}
		XamlAttribute attr = XWTModelUtil.getAdaptableAttribute(parent,
				attrName, IConstants.XWT_NAMESPACE);
		if (attr == null) {
			attr = XamlFactory.eINSTANCE.createAttribute(attrName,
					IConstants.XWT_NAMESPACE);
		}
		return attr;
	}

	public final void refresh() {
		if (!isNeedToRefresh()) {
			return;
		}
		setNeedToRefresh(false);
		DisplayUtil.syncExec(display, new Runnable() {
			public void run() {
				doRefresh();
			}
		});
	}

	protected abstract void doRefresh();

	protected abstract String getNewValue(Event event);

	protected abstract String getAttributeName();

	protected abstract Control createSection(Composite parent);
}
