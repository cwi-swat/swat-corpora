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
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import java.util.Map;

import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.LayoutsHelper;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.AssistancePageFactory;
import org.eclipse.e4.xwt.tools.ui.designer.layouts.pages.IAssistantPage;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.designer.utils.XWTUtility;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlAttribute;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlElement;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author rui.ban rui.ban@soyatec.com
 */
public class LayoutAssistantWindow extends Window {

	public static final String NOLAYOUT_TAB = "Info";
	public static final String LAYOUT_TAB = "Layout";
	public static final String PARENTLAYOUT_TAB = "Parent Layout";
	public static final String LAYOUTDATA_TAB = "Layout Data";

	private EditPart editPart;

	protected CTabFolder tabFolder;

	private CTabItem layoutTab;
	private CTabItem layoutDataTab;

	protected Composite layoutPage;
	protected StackLayout layoutPageLayout;
	protected Composite layoutDataPage;
	protected StackLayout layoutDataPageLayout;

	protected Composite noLayoutPage;
	protected Composite noComponentPage;

	private Map<Object, IAssistantPage> pageMaps = AssistancePageFactory.newPages();

	private boolean isClosed;
	private boolean isOpened;

	/**
	 * @param parentShell
	 */
	public LayoutAssistantWindow(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Layout Assistant");
	}

	/**
	 * @param editPart
	 */
	public void setEditPart(EditPart editPart) {
		EditPart oldEp = this.editPart;
		if (editPart != null && editPart != oldEp) {
			this.editPart = editPart;
			refresh();
		}
	}

	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		// Create the Layout tab.
		String tabName = getLayoutTabName();
		layoutTab = new CTabItem(tabFolder, SWT.NONE);
		layoutTab.setText(tabName);
		layoutTab.setToolTipText("layout options");

		layoutPage = new Composite(tabFolder, SWT.NONE);
		layoutPageLayout = new StackLayout();
		layoutPage.setLayout(layoutPageLayout);
		layoutTab.setControl(layoutPage);

		if (tabName.equals(NOLAYOUT_TAB)) {
			// create info page for model which does not contain any layout attribute.
			createNoneLayoutPage();
		} else if (tabName.equals(LAYOUT_TAB)) {
			// Create layout page for composite or shell and its subclasses.
			createParentLayoutPage(editPart);
		} else if (tabName.equals(PARENTLAYOUT_TAB)) {
			// Create parent layout page and layoutData page for control and its subclasses.
			createControlLayoutPage();
		}
		tabFolder.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				tabFolder = null;
			}
		});

		ToolBar toolBar = new ToolBar(tabFolder, SWT.NONE);
		ToolItem defaultTool = new ToolItem(toolBar, SWT.PUSH);
		defaultTool.setImage(ImageShop.get(ImageShop.IMG_CLEAR_FILTER));
		defaultTool.setToolTipText("Clear setting values.");
		defaultTool.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				performDefault();
			}
		});
		tabFolder.setTopRight(toolBar);
		tabFolder.setTabHeight(22);
		Dialog.applyDialogFont(tabFolder);
		return tabFolder;
	}

	protected void performDefault() {
		CTabItem selection = tabFolder.getSelection();
		if (selection == null) {
			return;
		}
		Control control = null;
		if (selection == layoutTab) {
			control = layoutPageLayout.topControl;
		} else if (selection == layoutDataTab) {
			control = layoutDataPageLayout.topControl;
		}
		if (control != null && control.getData() != null && control.getData() instanceof IAssistantPage) {
			((IAssistantPage) control.getData()).performDefault();
		}
	}

	/**
	 * create info page for model which does not contain any layout attribute.
	 */
	private void createNoneLayoutPage() {
		if (noLayoutPage == null) {
			noLayoutPage = new Composite(layoutPage, SWT.NONE);
			noLayoutPage.setLayout(new GridLayout());
			Label noLayoutLabel = new Label(noLayoutPage, SWT.WRAP);
			noLayoutLabel.setText("Selection does not provide any layout assistance.");
			GridData gd = new GridData();
			gd.widthHint = 200;
			noLayoutLabel.setLayoutData(gd);
		}
		layoutPageLayout.topControl = noLayoutPage;
		layoutPage.layout();
	}

	/**
	 * Create parent layout page and layoutData page for editpart.
	 */
	private void createControlLayoutPage() {
		EditPart parentEditPart = getParent(editPart);
		if (parentEditPart != null) {
			createParentLayoutPage(parentEditPart);
		}
		IAssistantPage dataPage = pageMaps.get(LayoutsHelper.getLayoutDataType(editPart));
		if (dataPage != null) {
			// Create the layoutData tab if it not created.
			if (layoutDataTab == null || layoutDataTab.isDisposed()) {
				createLayoutDataTab();
			}
			createPageControl(dataPage, layoutDataPage, layoutDataPageLayout);
		} else {
			if (layoutDataTab != null && !layoutDataTab.isDisposed()) {
				layoutDataTab.setControl(null);
				layoutDataTab.dispose();
			}
		}
	}

	/**
	 * Create the layoutData tab.
	 */
	private void createLayoutDataTab() {
		layoutDataTab = new CTabItem(tabFolder, SWT.NONE);
		layoutDataTab.setText(LAYOUTDATA_TAB);
		layoutDataTab.setToolTipText("layoutData options");

		if (layoutDataPage == null) {
			layoutDataPage = new Composite(tabFolder, SWT.NONE);
			layoutDataPageLayout = new StackLayout();
			layoutDataPage.setLayout(layoutDataPageLayout);
		}
		layoutDataTab.setControl(layoutDataPage);
	}

	/**
	 * Create layout page for composite and its subclasses.
	 */
	private void createParentLayoutPage(EditPart editPart) {
		IAssistantPage page = pageMaps.get(LayoutsHelper.getLayoutType(editPart));
		if (page != null) {
			createPageControl(page, layoutPage, layoutPageLayout);
			page.setEditPart(editPart);
		}
	}

	private void createPageControl(IAssistantPage assistantPage, Composite parentComposite, StackLayout parentLayout) {
		Control control = assistantPage.getControl(parentComposite);
		if (control != null) {
			parentLayout.topControl = control;
			parentComposite.layout();
			assistantPage.setEditPart(editPart);

			// Now resize to handle the new page.
			Point size = getInitialSize();
			getShell().setSize(size.x, size.y);
		}
	}

	/**
	 * if model changed refresh this dialog.
	 */
	public void refresh() {
		if (layoutTab == null || layoutTab.isDisposed()) {
			return;
		}
		String tabName = getLayoutTabName();
		layoutTab.setText(tabName);
		if (tabName.equals(NOLAYOUT_TAB)) {
			createNoneLayoutPage();
		} else if (tabName.equals(LAYOUT_TAB)) {
			createParentLayoutPage(editPart);
			if (layoutDataTab != null && !layoutDataTab.isDisposed()) {
				layoutDataTab.setControl(null);
				layoutDataTab.dispose();
			}
		} else if (tabName.equals(PARENTLAYOUT_TAB)) {
			createControlLayoutPage();
		}
	}

	private String getLayoutTabName() {
		if (editPart != null) {
			EditPart parent = getParent(editPart);
			XamlAttribute layoutAttr = null;
			if (parent == null) {
				layoutAttr = getLayoutAttr(editPart.getModel());
				if (layoutAttr != null) {
					return LAYOUT_TAB;
				}
			} else {
				layoutAttr = getLayoutAttr(parent.getModel());
				if (layoutAttr != null) {
					return PARENTLAYOUT_TAB;
				}
			}
		}
		return NOLAYOUT_TAB;
	}

	/**
	 * Get layout attribute from the element.
	 * 
	 * @param model
	 * @return
	 */
	private XamlAttribute getLayoutAttr(Object model) {
		XamlAttribute attribute = null;
		if (model instanceof XamlElement) {
			XamlElement element = (XamlElement) model;
			attribute = element.getAttribute("layout");
		}
		return attribute;
	}

	private Class<?> getType(EditPart editPart) {
		if (editPart == null) {
			return null;
		}
		Object model = editPart.getModel();
		if (model instanceof XamlElement) {
			XamlElement element = (XamlElement) model;
			IMetaclass metaclass = XWTUtility.getMetaclass(element);
			if (metaclass != null) {
				return metaclass.getType();
			}
		}
		return null;
	}

	private EditPart getParent(EditPart editPart) {
		if (editPart != null) {
			EditPart parent = editPart.getParent();
			if (parent.getModel() instanceof XamlDocument) {
				return null;
			}
			Class<?> type = getType(parent);
			if (type == null) {
				return null;
			}
			if (Composite.class.isAssignableFrom(type) || Shell.class.isAssignableFrom(type)) {
				return parent;
			}
			return getParent(parent);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open() {
		int open = super.open();
		isOpened = open == Window.OK;
		return open;
	}

	/**
	 * @see org.eclipse.jface.window.Window#close()
	 */
	public boolean close() {
		isClosed = super.close();
		if (tabFolder != null) {
			tabFolder.dispose();
		}
		// AssistanceManager.dispose();
		return isClosed;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean isClosed() {
		return isClosed;
	}

	protected Point getInitialSize() {
		Point initSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// Set it to a minimum. If above size is bigger, great.
		if (initSize.x < 100)
			initSize.x = 100;
		if (initSize.x < 90)
			initSize.y = 90;
		return initSize;
	}
}
