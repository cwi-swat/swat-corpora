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
package org.eclipse.e4.xwt.tools.ui.designer.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.xwt.XWTLoaderManager;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualRenderer;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.DesignerOutlinePage;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.IModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.BindingLayerAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.ChangeTextAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.CopyElementAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.CutElementAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.LayoutAssistantAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.OpenBindingDialogAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.OpenExternalizeStringsAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.PasteElementAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.PreviewAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.actions.SurroundWithAction;
import org.eclipse.e4.xwt.tools.ui.designer.editor.dnd.XWTDropContext;
import org.eclipse.e4.xwt.tools.ui.designer.editor.dnd.XWTGraphicalViewerDropListener;
import org.eclipse.e4.xwt.tools.ui.designer.editor.event.EventHandler;
import org.eclipse.e4.xwt.tools.ui.designer.editor.model.XWTModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.editor.outline.TreeEditPartFactory;
import org.eclipse.e4.xwt.tools.ui.designer.loader.XWTVisualLoader;
import org.eclipse.e4.xwt.tools.ui.designer.parts.XWTEditPartFactory;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlDocument;
import org.eclipse.e4.xwt.ui.XWTPerspectiveFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
@SuppressWarnings("restriction")
public class XWTDesigner extends Designer implements
		ITabbedPropertySheetPageContributor {

	public static final String EDITOR_ID = "org.eclipse.e4.xwt.tools.ui.designer.XWTDesigner";

	private XWTJavaEditor javaEditor;
	private EventHandler eventHandler;
	private ToolItem generateTool;
	private XWTVisualLoader xwtLoader;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		// Initialize to setup an active IXWTLoader.
		IFile inputFile = getFile();
		if (inputFile != null) {
			xwtLoader = new XWTVisualLoader(inputFile);
			XWTLoaderManager.setActive(xwtLoader, true);
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		EditDomain domain = getEditDomain();
		domain.setDefaultTool(new XWTSelectionTool());
		domain.loadDefaultTool();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.Designer#dispose()
	 */
	public void dispose() {
		super.dispose();
		// Dispose the active IXWTLoader.
		if (xwtLoader != null) {
			XWTLoaderManager.setActive(xwtLoader, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.XAMLDesigner#initializeActions()
	 */
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;
		action = new CopyElementAction(this);
		registry.registerAction(action);
		action = new PasteElementAction(this);
		registry.registerAction(action);
		action = new CutElementAction(this);
		registry.registerAction(action);

		action = new PreviewAction(this);
		registry.registerAction(action);

		action = new ChangeTextAction(this);
		registry.registerAction(action);

		action = new LayoutAssistantAction(this);
		registry.registerAction(action);

		action = new OpenBindingDialogAction(this);
		registry.registerAction(action);

		action = new BindingLayerAction(this);
		registry.registerAction(action);

		// add by xrchen 2009/9/22
		action = new OpenExternalizeStringsAction(this);
		registry.registerAction(action);

		action = new SurroundWithAction(this);
		registry.registerAction(action);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.Designer#createModelBuilder()
	 */
	protected IModelBuilder createModelBuilder() {
		return new XWTModelBuilder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.designer.editor.XAMLDesigner#setupGraphicalViewer()
	 */
	protected void setupGraphicalViewer() {
		((XWTEditPartFactory) getEditPartFactory())
				.setVisualFactory(getVisualsRender());
		super.setupGraphicalViewer();
		setupJavaEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.designer.editor.Designer#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
				true);
	}

	protected void setupGraphicalViewerDropCreation(GraphicalViewer viewer) {
		viewer.addDropTargetListener(new XWTGraphicalViewerDropListener(viewer));
	}

	/**
	 * Setup JavaEditor.
	 */
	private void setupJavaEditor() {
		String hostClassName = getVisualsRender().getHostClassName();
		if (hostClassName != null && javaEditor == null) {
			javaEditor = new XWTJavaEditor(getJavaProject(), hostClassName);
			IType type = javaEditor.getType();
			if (type != null) {
				try {
					int javaPageIndex = addPage(javaEditor, javaEditor
							.getEditorInput());
					setPageText(javaPageIndex, "Java");
					setPageImage(javaPageIndex, JavaPluginImages
							.get(JavaPluginImages.IMG_OBJS_CLASS));
					eventHandler = new EventHandler(this, type);

					if (generateTool != null && !generateTool.isDisposed()) {
						generateTool.setEnabled(true);
					}
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	protected IJavaProject getJavaProject() {
		IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
		IJavaProject javaProject = JavaCore.create(file.getProject());
		if (!javaProject.exists()) {
			return null;
		}
		return javaProject;
	}

	protected ToolBar createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT);
		final ToolBarManager toolBarManager = new ToolBarManager(toolbar);
		toolBarManager.add(getActionRegistry().getAction(
				ActionFactory.COPY.getId()));
		toolBarManager.add(getActionRegistry().getAction(
				ActionFactory.PASTE.getId()));
		toolBarManager.add(getActionRegistry().getAction(
				ActionFactory.CUT.getId()));
		toolBarManager.add(getActionRegistry().getAction(
				ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(
				PreviewAction.ACTION_ID));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(
				LayoutAssistantAction.ID));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(
				OpenBindingDialogAction.ID));
		toolBarManager.update(true);
		toolbar.pack();

		return toolbar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.soyatec.tools.designer.editor.XAMLDesigner#configureContainerToolBar
	 * (org.eclipse.swt.widgets.ToolBar)
	 */
	protected void configureContainerToolBar(ToolBar toolBar) {
		ToolItem previewTool = new ToolItem(toolBar, SWT.PUSH);
		previewTool.setImage(ImageShop.get(ImageShop.IMG_PREVIEW));
		previewTool.setToolTipText("Preview");
		previewTool.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new PreviewAction(XWTDesigner.this).run();
			}
		});

		generateTool = new ToolItem(toolBar, SWT.PUSH);
		generateTool.setImage(JavaPluginImages
				.get(JavaPluginImages.IMG_OBJS_CLASS));
		generateTool.setToolTipText("Create event handlers");
		generateTool.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (eventHandler != null) {
					eventHandler.createHandlers();
				}
			}
		});
		generateTool.setEnabled(false);
	}

	protected ContextMenuProvider createMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		return new XWTDesignerMenuProvider(viewer, actionRegistry, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.tools.ui.designer.core.editor.Designer#
	 * createEditPartFactory()
	 */
	protected EditPartFactory createEditPartFactory() {
		return new XWTEditPartFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.XAMLDesigner#createOutlinePage()
	 */
	protected DesignerOutlinePage createOutlinePage() {
		DesignerOutlinePage designerOutlinePage = new DesignerOutlinePage(
				getEditDomain(), new TreeEditPartFactory());
		TreeViewer treeViewer = designerOutlinePage.getTreeViewer();
		ContextMenuProvider outlineMenu = createMenuProvider(treeViewer,
				getActionRegistry());
		if (outlineMenu != null) {
			treeViewer.setContextMenu(outlineMenu);
		}
		return designerOutlinePage;
	}

	protected void setContent(EObject diagram) {
		super.setContent(diagram);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.XAMLDesigner#getDropContext()
	 */
	protected DropContext getDropContext() {
		return new XWTDropContext();
	}

	/**
	 * Open a special perspective.
	 */
	public void openPerspective() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		int count = workbench.getWorkbenchWindowCount();
		if (count == 0) {
			return;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			window = workbench.getWorkbenchWindows()[0];
		}
		IPerspectiveDescriptor pers = workbench
				.getPerspectiveRegistry()
				.findPerspectiveWithId(XWTPerspectiveFactory.XWT_PERSPECTIVE_ID);
		if (pers == null) {
			return;
		}
		try {
			workbench.showPerspective(XWTPerspectiveFactory.XWT_PERSPECTIVE_ID,
					window);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.XAMLDesigner#createPropertyPage()
	 */
	protected IPropertySheetPage createPropertyPage() {
		TabbedPropertySheetPage propertyPage = new TabbedPropertySheetPage(this);
		// propertyPage.setPropertySourceProvider(new
		// XWTPropertySourceProvider(getEditDomain(), propertyPage));
		return propertyPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.soyatec.tools.designer.editor.XAMLDesigner#createVisualsRender()
	 */
	protected IVisualRenderer createVisualsRender() {
		return new XWTVisualRenderer(getFile(),
				(XamlDocument) getDocumentRoot());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor
	 * #getContributorId()
	 */
	public String getContributorId() {
		return getSite().getId();
	}
}
