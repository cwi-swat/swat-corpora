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
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import java.lang.reflect.Field;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;
import org.eclipse.e4.xwt.tools.ui.designer.core.component.CustomSashForm;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualRenderer.Result;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.commandstack.CombinedCommandStack;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.DropTargetAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.GraphicalViewerDropCreationListener;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.dnd.palette.PaletteDropAdapter;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.outline.DesignerOutlinePage;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.text.StructuredTextHelper;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.IModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.ModelChangeListener;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.RefreshContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.root.DesignerRootEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.core.problems.ConfigurableProblemHandler;
import org.eclipse.e4.xwt.tools.ui.designer.core.problems.ProblemHandler;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.palette.page.ContributePalettePage;
import org.eclipse.e4.xwt.tools.ui.palette.page.CustomPalettePage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;
import org.eclipse.wst.sse.core.internal.text.JobSafeStructuredDocument;
import org.eclipse.wst.sse.core.internal.undo.StructuredTextUndoManager;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
@SuppressWarnings("restriction")
public abstract class Designer extends MultiPageEditorPart implements
		ISelectionChangedListener, CommandStackListener {

	public static final String DESIGNER_INPUT = "DESIGNER INPUT";
	public static final String DESIGNER_TEXT_EDITOR = "DESIGNER TEXT EDITOR";
	public static final String DEFAULT_DESIGNER_CONTEXT_MENU_ID = "#DesignerContext"; //$NON-NLS-1$

	// UI editor.
	protected CustomSashForm pageContainer;
	private CustomPalettePage palettePage;
	private IPropertySheetPage propertyPage;
	private DesignerOutlinePage outlinePage;
	private ProblemHandler problemHandler;

	// GEF editor.
	private GraphicalViewer graphicalViewer;
	private EditDomain editDomain;
	private ISelectionSynchronizer selectionSynchronizer;
	private ActionRegistry actionRegistry;
	private ActionGroup actionGroup;

	// Source editor.
	private StructuredTextEditor fTextEditor;
	private IPropertyListener fPropertyListener;
	private DropTargetAdapter dropListener;

	private EObject documentRoot;
	private IVisualRenderer visualsRender;

	private CombinedCommandStack commandStack = new CombinedCommandStack();

	private IModelBuilder modelBuilder;
	private boolean installed = false;
	private ModelChangeListener modelBuilderListener;
	protected Display display;
	private LoadingFigureController loadingFigureController;

	private EditPartFactory editPartFactory;

	private KeyHandler fSharedKeyHandler;

	private Refresher refresher;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.MultiPageEditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		site.setSelectionProvider(null);// clear multi-page selection provider.
		display = site.getShell().getDisplay();
		loadingFigureController = new LoadingFigureController();
		editDomain = new EditDomain(this);
		editDomain.setCommandStack(commandStack.getCommandStack4GEF());
		setPartName(input.getName());
		getEditDomain().setData(DESIGNER_INPUT, input);
		getCommandStack().getCommandStack4GEF().addCommandStackListener(this);
		createActions();
		configureActions();

		refresher = new Refresher(display);
	}

	public Refresher getRefresher() {
		return refresher;
	}

	protected void configureActions() {
		actionGroup.updateActions(ActionGroup.PROPERTY_GRP);
		actionGroup.updateActions(ActionGroup.STACK_GRP);

		if (graphicalViewer != null) {
			Iterator<?> actions = getActionRegistry().getActions();
			while (actions.hasNext()) {
				Object object = (Object) actions.next();
				if (object instanceof SelectionAction) {
					((SelectionAction) object)
							.setSelectionProvider(graphicalViewer);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#setFocus()
	 */
	public void setFocus() {
		if (graphicalViewer != null && graphicalViewer.getControl() != null) {
			graphicalViewer.getControl().setFocus();
		}
		super.setFocus();
	}

	private void dispatchModelEvent(final Notification event) {
		if (event.isTouch() || !installed) {
			return;
		}
		if (display != null && display.getThread() == Thread.currentThread()) {
			performModelChanged(event);
		} else if (display != null) {
			DisplayUtil.asyncExec(display, new Runnable() {
				public void run() {
					performModelChanged(event);
				}
			});
		}
	}

	protected void performModelChanged(Notification event) {
		if (visualsRender == null || event.isTouch()) {
			return;
		}
		Result result;
		try {
			result = getVisualsRender().refreshVisuals(event);
		} catch (RuntimeException e) {
			return;
		}
		if (result == null || !result.refreshed) {
			return;
		}
		EditPart toRefresh = null;
		Object notifier = result.visuals;
		// When the eventType is ADD, we need to refresh all children.
		if (notifier == null) {
			toRefresh = getGraphicalViewer().getRootEditPart();
		} else {
			toRefresh = getEditPart(notifier);
			if (toRefresh == null) {
				while (notifier != null && notifier instanceof EObject) {
					Object parentNode = ((EObject) notifier).eContainer();
					while (getEditPart(parentNode) != null) {
						toRefresh = getEditPart(parentNode).getParent();
						break;
					}
					notifier = parentNode;
				}
			}
		}
		if (toRefresh == null) {
			return;
		}
		refresher.refreshInJob(toRefresh);

		getOutlinePage().refresh(toRefresh);

		// highlight changed one.
		if (toRefresh.isSelectable()) {
			graphicalViewer.setSelection(new StructuredSelection(toRefresh));
		}
	}

	public void refresh(EditPart editPart, RefreshContext context) {
		refresher.refresh(editPart, context);
		getOutlinePage().refresh(editPart);
	}

	public EditPart getEditPart(Object model) {
		return (EditPart) graphicalViewer.getEditPartRegistry().get(model);
	}

	private void setDocumentRoot(EObject document) {
		this.documentRoot = document;
	}

	public EObject getDocumentRoot() {
		return documentRoot;
	}

	/**
	 * Start loading models.
	 */
	private void tryToLoadModels() {
		loadingFigureController.showLoadingFigure(true);
		loadingFigureController.startListener(getGraphicalViewer());
		UIJob setupJob = new UIJob("Setup") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (modelBuilder.doLoad(Designer.this, monitor)) {
					setDocumentRoot(modelBuilder.getDiagram());
				}
				if (!isDisposed()) {
					try {
						setupGraphicalViewer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return Status.OK_STATUS;
			}
		};
		setupJob.setDisplay(display);
		setupJob.setPriority(Job.SHORT);
		final IModelBuilder modelBuilder = getModelBuilder();
		if (getDocument() != null && modelBuilder != null) {
			setupJob.schedule();
		}
	}

	protected void runWithDialog(IRunnableWithProgress runnable) {
		try {
			ProgressMonitorDialog d = new ProgressMonitorDialog(getSite()
					.getShell());
			d.run(true, false, runnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setupGraphicalViewer() {
		IVisualRenderer vr = getVisualsRender();
		if (vr != null) {
			vr.createVisuals();
			getEditDomain().setViewerData(getGraphicalViewer(),
					IVisualRenderer.KEY, vr);
		}
		setContent(getDocumentRoot());
		loadingFigureController.showLoadingFigure(false);

		installed = true;
	}

	protected void setContent(EObject diagram) {
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		if (graphicalViewer != null) {
			EditPart diagramEp = getDiagramEditPart();
			graphicalViewer.setContents(diagramEp);
			if (diagramEp != null) {
				refresher.refreshAsynchronous(diagramEp);
			}
		}

		DesignerOutlinePage outlinePage = getOutlinePage();
		if (outlinePage != null) {
			outlinePage.setContents(diagram);
		}
	}

	public IVisualRenderer getVisualsRender() {
		if (visualsRender == null) {
			visualsRender = createVisualsRender();
		}
		return visualsRender;
	}

	/**
	 * @return
	 */
	private EditPart getDiagramEditPart() {
		return getEditPartFactory().createEditPart(
				getGraphicalViewer().getRootEditPart(), getDocumentRoot());
	}

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	protected synchronized boolean isDisposed() {
		return editDomain == null;
	}

	public IFile getFile() {
		IEditorInput editorInput = getEditorInput();
		return (IFile) editorInput.getAdapter(IFile.class);
	}

	/**
	 * @return the modelBuilder
	 */
	public IModelBuilder getModelBuilder() {
		if (modelBuilder == null) {
			modelBuilder = createModelBuilder();
		}
		if (modelBuilderListener == null) {
			modelBuilderListener = new ModelChangeListener() {
				public void notifyChanged(final Notification event) {
					dispatchModelEvent(event);
				}
			};
		}
		if (!modelBuilder.hasListener(modelBuilderListener)) {
			modelBuilder.addModelListener(modelBuilderListener);
		}
		return modelBuilder;
	}

	protected abstract IModelBuilder createModelBuilder();

	/**
	 * Initialize and create actions.
	 */
	protected void createActions() {
		if (actionGroup == null) {
			actionGroup = new ActionGroup(this);
		}
		actionGroup.createActions();
	}

	/**
	 * Lazily creates and returns the action registry.
	 * 
	 * @return the action registry
	 */
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.MultiPageEditorPart#createPageContainer(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected Composite createPageContainer(Composite parent) {
		ViewForm diagramPart = new ViewForm(parent, SWT.FLAT);

		ToolBar toolBar = createToolBar(diagramPart);
		if (toolBar != null && !toolBar.isDisposed()) {
			diagramPart.setTopLeft(toolBar);
		}

		pageContainer = new CustomSashForm(diagramPart, SWT.VERTICAL);
		pageContainer.setBackgroundMode(SWT.INHERIT_DEFAULT);
		createGraphicalViewer(pageContainer);
		diagramPart.setContent(pageContainer);
		return pageContainer;
	}

	protected boolean isEditable() {
		return true;
	}

	/**
	 * Create Graphical Viewer for GEF Editor.
	 * 
	 * @param parent
	 */
	private void createGraphicalViewer(Composite parent) {
		graphicalViewer = new ScrollingGraphicalViewer();
		graphicalViewer.createControl(parent);
		graphicalViewer.getControl().setBackground(
				ColorConstants.listBackground);
		configureGraphicalViewer();
	}

	/**
	 * Configure GraphicalViewer
	 */
	protected void configureGraphicalViewer() {
		graphicalViewer.addSelectionChangedListener(this);
		editDomain.addViewer(graphicalViewer);

		getSite().setSelectionProvider(graphicalViewer);
		getSelectionSynchronizer().addProvider(graphicalViewer);

		graphicalViewer.setEditPartFactory(getEditPartFactory());
		ContextMenuProvider menuProvider = createMenuProvider(graphicalViewer,
				getActionRegistry());
		if (menuProvider != null) {
			graphicalViewer.setContextMenu(menuProvider);
			menuProvider.setRemoveAllWhenShown(true);
			getSite()
					.registerContextMenu(
							getClass().getSimpleName() + ".contextMenu", menuProvider, graphicalViewer); //$NON-NLS-1$
		}

		DesignerRootEditPart rootEditPart = new DesignerRootEditPart();
		graphicalViewer.setRootEditPart(rootEditPart);

		graphicalViewer.setKeyHandler(new GraphicalViewerKeyHandler(
				graphicalViewer).setParent(getCommonKeyHandler()));

		setupGraphicalViewerDropCreation(graphicalViewer);

		Iterator<?> actions = getActionRegistry().getActions();
		while (actions.hasNext()) {
			Object object = (Object) actions.next();
			if (object instanceof SelectionAction) {
				((SelectionAction) object)
						.setSelectionProvider(graphicalViewer);
			}
		}
	}

	protected void setupGraphicalViewerDropCreation(GraphicalViewer viewer) {
		viewer.addDropTargetListener(new GraphicalViewerDropCreationListener(
				viewer));
	}

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and
	 * Graphical Views. For example, delete is a common action.
	 */
	protected KeyHandler getCommonKeyHandler() {
		if (fSharedKeyHandler == null) {
			fSharedKeyHandler = new KeyHandler();
			fSharedKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, SWT.DEL, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
			fSharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
					getActionRegistry().getAction(
							GEFActionConstants.DIRECT_EDIT));
		}
		return fSharedKeyHandler;
	}

	protected ContextMenuProvider createMenuProvider(EditPartViewer viewer,
			ActionRegistry actionRegistry) {
		return new DesignerMenuProvider(viewer, actionRegistry);
	}

	/**
	 * Returns the selection syncronizer object. The synchronizer can be used to
	 * sync the selection of 2 or more EditPartViewers.
	 * 
	 * @return
	 */
	public ISelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null) {
			selectionSynchronizer = createSelectionSynchronizer();
		}
		return selectionSynchronizer;
	}

	protected ISelectionSynchronizer createSelectionSynchronizer() {
		return new SelectionSynchronizer();
	}

	/**
	 * Create a ToolBar for editor with global actions.
	 * 
	 * @param parent
	 * @return
	 */
	protected ToolBar createToolBar(Composite parent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	protected void createPages() {
		createSourcePage();
		createExternalPages();

		updateContainer();
	}

	/**
	 * Update TabFolder.
	 */
	private void updateContainer() {
		Composite container = getContainer();
		if (container == null || !(container instanceof CTabFolder)) {
			return;
		}
		CTabFolder tabFolder = (CTabFolder) container;
		tabFolder.setTabPosition(SWT.TOP);
		tabFolder.setSimple(false);
		ToolBar toolBar = new ToolBar(tabFolder, SWT.FLAT | SWT.WRAP
				| SWT.RIGHT);
		configureContainerToolBar(toolBar);
		tabFolder.setTopRight(toolBar);
		tabFolder.setTabHeight(Math.max(toolBar.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y, tabFolder.getTabHeight()));
	}

	/**
	 * ToolBar of container, it is really at the middle of the editor.
	 */
	protected void configureContainerToolBar(ToolBar toolBar) {

	}

	protected void createExternalPages() {

	}

	/**
	 * Create and add a StructuredTextEditor as a Source Page.
	 */
	private void createSourcePage() {
		// Subclass of StructuredTextEditor is not allowed.
		fTextEditor = new StructuredTextEditor();
		final StructuredTextUndoManager undoManager = new StructuredTextUndoManager(
				commandStack);
		TextFileDocumentProvider provider = new TextFileDocumentProvider() {
			public IDocument getDocument(Object element) {
				JobSafeStructuredDocument document = (JobSafeStructuredDocument) super
						.getDocument(element);
				if (document != null) {
					try {
						Field fUndoManager = BasicStructuredDocument.class
								.getDeclaredField("fUndoManager");
						fUndoManager.setAccessible(true);
						Object object = fUndoManager.get(document);
						if (object != null && object != undoManager) {
							fUndoManager.set(document, null);
							document.setUndoManager(undoManager);
						} else if (object == null) {
							document.setUndoManager(undoManager);
						}
					} catch (Exception e) {
						DesignerPlugin.logError(e);
					}
				}
				return document;
			}
		};
		fTextEditor.initializeDocumentProvider(provider);
		fTextEditor.setEditorPart(this);
		if (fPropertyListener == null) {
			fPropertyListener = new PropertyListener();
		}
		fTextEditor.addPropertyListener(fPropertyListener);
		try {
			int fSourcePageIndex = addPage(fTextEditor, getEditorInput());
			setPageText(fSourcePageIndex, "Source");
			firePropertyChange(PROP_TITLE);
			tryToLoadModels();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		SourceSelectionProvider selectionProvider = new SourceSelectionProvider(
				this, fTextEditor);
		getSelectionSynchronizer().addProvider(selectionProvider);
		fTextEditor.setAction(ITextEditorActionConstants.DELETE, null);
		if (pageContainer != null) {
			pageContainer.setWeights(new int[] { 1, 1 });
		}
		configureTextEditor();
	}

	protected IEditorSite createSite(IEditorPart editor) {
		IEditorSite site = null;
		if (editor == fTextEditor) {
			site = new MultiPageEditorSite(this, editor) {

				public String getId() {
					// sets this id so nested editor is considered xml source
					// page
					return ContentTypeIdForXML.ContentTypeID_XML + ".source"; //$NON-NLS-1$;
				}
			};
		} else {
			site = super.createSite(editor);
		}
		return site;
	}

	/**
	 * Configure Text Editor,
	 */
	protected void configureTextEditor() {
		if (getPalettePage() == null) {
			return;
		}
		if (dropListener == null) {
			dropListener = new DropTargetAdapter();
			DropContext dropContext = getDropContext();
			if (dropContext != null) {
				dropListener.addDropAdapter(new PaletteDropAdapter(this,
						dropContext));
			}
		}
		StyledText styledText = getTextWidget();
		if (styledText == null || dropListener == null) {
			return;
		}
		DropTarget dropTarget = (DropTarget) styledText
				.getData(DND.DROP_TARGET_KEY);
		if (dropTarget != null) {
			dropTarget.removeDropListener(dropListener);
			dropTarget.dispose();
		}

		dropTarget = new DropTarget(styledText, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { getPalettePage()
				.getPaletteTransfer() });
		dropTarget.addDropListener(dropListener);

		styledText.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				StyledText styledText = (StyledText) e.widget;
				DropTarget dropTarget = (DropTarget) styledText
						.getData(DND.DROP_TARGET_KEY);
				if (dropTarget != null) {
					dropTarget.removeDropListener(dropListener);
				}
			}
		});

		getProblemHandler().handle();
	}

	public void format() {
		StructuredTextViewer textViewer = getTextViewer();
		if (textViewer != null
				&& textViewer
						.canDoOperation(StructuredTextViewer.FORMAT_DOCUMENT)) {
			textViewer.doOperation(StructuredTextViewer.FORMAT_DOCUMENT);
		} else if (getDocument() != null) {
			StructuredTextHelper.format(getDocument());
		}
	}

	public void formatWithCompound(Runnable runnable) {
		if (runnable == null) {
			format();
			return;
		}
		StructuredTextViewer textViewer = getTextViewer();
		if (textViewer == null) {
			runnable.run();
			return;
		}
		IRewriteTarget rewriteTarget = textViewer.getRewriteTarget();
		if (rewriteTarget != null) {
			rewriteTarget.beginCompoundChange();
		}
		runnable.run();
		format();
		if (rewriteTarget != null) {
			rewriteTarget.endCompoundChange();
		}
	}

	public StyledText getTextWidget() {
		if (getTextViewer() != null) {
			return getTextViewer().getTextWidget();
		}
		return null;
	}

	public StructuredTextViewer getTextViewer() {
		if (fTextEditor != null) {
			return fTextEditor.getTextViewer();
		}
		return null;
	}

	public IDocument getDocument() {
		IDocument document = null;
		if (fTextEditor != null) {
			IDocumentProvider documentProvider = fTextEditor
					.getDocumentProvider();
			IEditorInput editorInput = fTextEditor.getEditorInput();
			if (documentProvider == null || editorInput == null) {
				return null;
			}
			document = documentProvider.getDocument(editorInput);
		}
		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		fTextEditor.doSave(monitor);
		if (modelBuilder != null) {
			modelBuilder.doSave(monitor);
		}
		// getCommandStack().flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		fTextEditor.doSaveAs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#dispose()
	 */
	public void dispose() {
		getModelBuilder().removeModelListener(modelBuilderListener);
		getModelBuilder().dispose();
		getCommandStack().getCommandStack4GEF()
				.removeCommandStackListener(this);
		getCommandStack().flush();
		getGraphicalViewer().removeSelectionChangedListener(this);
		if (modelBuilder != null) {
			modelBuilder.dispose();
		}
		if (visualsRender != null) {
			visualsRender.dispose();
		}
		fSharedKeyHandler = null;
		getActionRegistry().dispose();
		getProblemHandler().clear();
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.MultiPageEditorPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (ActionRegistry.class == adapter) {
			return getActionRegistry();
		} else if (org.eclipse.gef.EditDomain.class.isAssignableFrom(adapter)) {
			return getEditDomain();
		} else if (PalettePage.class.isAssignableFrom(adapter)) {
			return getPalettePage();
		} else if (CommandStack.class == adapter) {
			return getCommandStack().getCommandStack4GEF();
		} else if (GraphicalViewer.class == adapter) {
			return getGraphicalViewer();
		} else if (adapter == IPropertySheetPage.class) {
			return getPropertySheetPage();
		} else if (adapter == IContentOutlinePage.class) {
			return getOutlinePage();
		} else if (adapter == ProblemHandler.class) {
			return getProblemHandler();
		} else if (adapter == EditPart.class && getGraphicalViewer() != null) {
			return getGraphicalViewer().getRootEditPart();
		} else if (adapter == IFigure.class && getGraphicalViewer() != null) {
			return ((GraphicalEditPart) getGraphicalViewer().getRootEditPart())
					.getFigure();
		} else if (adapter == IProject.class) {
			return getProject();
		} else if (adapter == IFile.class) {
			return getFile();
		}
		return super.getAdapter(adapter);
	}

	public ProblemHandler getProblemHandler() {
		if (problemHandler == null) {
			problemHandler = new ConfigurableProblemHandler(this);
		}
		return problemHandler;
	}

	/**
	 * @return
	 */
	protected DesignerOutlinePage getOutlinePage() {
		if (outlinePage == null) {
			outlinePage = createOutlinePage();
			if (outlinePage != null) {
				getSelectionSynchronizer().addProvider(
						outlinePage.getTreeViewer());
			}
		}
		return outlinePage;
	}

	protected abstract DesignerOutlinePage createOutlinePage();

	public IPropertySheetPage getPropertySheetPage() {
		if (propertyPage == null || propertyPage.getControl() == null
				|| propertyPage.getControl().isDisposed()) {
			propertyPage = createPropertyPage();
		}
		return propertyPage;
	}

	protected IPropertySheetPage createPropertyPage() {
		return new PropertySheetPage();
	}

	/**
	 * @return
	 */
	private CustomPalettePage getPalettePage() {
		if (palettePage == null) {
			palettePage = new ContributePalettePage(this, editDomain);
		}
		return palettePage;
	}

	public CombinedCommandStack getCommandStack() {
		return commandStack;
	}

	/**
	 * @return the editDomain
	 */
	public EditDomain getEditDomain() {
		return editDomain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return fTextEditor.isSaveAsAllowed();
	}

	protected EditPart convert(EditPartViewer viewer, EditPart part) {
		Object temp = viewer.getEditPartRegistry().get(part.getModel());
		EditPart newPart = null;
		if (temp != null) {
			newPart = (EditPart) temp;
		}
		return newPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public final void selectionChanged(SelectionChangedEvent event) {
		Object source = event.getSource();
		if (source == null) {
			return;
		}
		while (Display.getDefault().readAndDispatch())
			;
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if (Designer.this.equals(activeEditor)) {
			performSelectionChanged(event);
		}
	}

	/**
	 * Perform Selection event.
	 * 
	 * @param event
	 */
	protected void performSelectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		DisplayUtil.asyncExec(new Runnable() {
			public void run() {
				if (actionGroup != null) {
					actionGroup.updateActions(ActionGroup.SELECTION_GRP);
				}
				ActionRegistry actionRegistry = getActionRegistry();
				Iterator<?> actions = actionRegistry.getActions();
				while (actions.hasNext()) {
					Object object = (Object) actions.next();
					if (object instanceof SelectionAction) {
						((SelectionAction) object).update();
					}
				}
			}
		});
	}

	public void gotoDefinition(EObject node) {
		EditPart editPart = getEditPart(node);
		if (editPart != null) {
			graphicalViewer.setSelection(new StructuredSelection(editPart));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java
	 * .util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		if (actionGroup != null) {
			actionGroup.updateActions(ActionGroup.STACK_GRP);
		}
	}

	public StructuredTextEditor getTextEditor() {
		return fTextEditor;
	}

	/*
	 * This method is just to make firePropertyChanged accessible from some
	 * (anonomous) inner classes.
	 */
	void _firePropertyChange(int property) {
		super.firePropertyChange(property);
	}

	public IProject getProject() {
		return getFile().getProject();
	}

	/**
	 * EditPart Factory.
	 * 
	 * @return
	 */
	public EditPartFactory getEditPartFactory() {
		if (editPartFactory == null) {
			editPartFactory = createEditPartFactory();
		}
		return editPartFactory;
	}

	protected abstract EditPartFactory createEditPartFactory();

	protected abstract IVisualRenderer createVisualsRender();

	/**
	 * DropContext.
	 * 
	 * @return
	 */
	protected abstract DropContext getDropContext();

	/**
	 * Internal IPropertyListener
	 */
	class PropertyListener implements IPropertyListener {
		public void propertyChanged(Object source, int propId) {
			switch (propId) {
			// had to implement input changed "listener" so that
			// StructuredTextEditor could tell it containing editor that
			// the input has change, when a 'resource moved' event is
			// found.
			case IEditorPart.PROP_INPUT: {
			}
			case IEditorPart.PROP_DIRTY: {
				if (source == getTextEditor()) {
					if (getTextEditor().getEditorInput() != getEditorInput()) {
						setInput(getTextEditor().getEditorInput());
						/*
						 * title should always change when input changes. create
						 * runnable for following post call
						 */
						Runnable runnable = new Runnable() {
							public void run() {
								_firePropertyChange(IWorkbenchPart.PROP_TITLE);
							}
						};
						/*
						 * Update is just to post things on the display queue
						 * (thread). We have to do this to get the dirty
						 * property to get updated after other things on the
						 * queue are executed.
						 */
						((Control) getTextEditor().getAdapter(Control.class))
								.getDisplay().asyncExec(runnable);
					}
				}
				break;
			}
			case IWorkbenchPart.PROP_TITLE: {
				// update the input if the title is changed
				if (source == getTextEditor()) {
					if (getTextEditor().getEditorInput() != getEditorInput()) {
						setInput(getTextEditor().getEditorInput());
					}
				}
				break;
			}
			default: {
				// propagate changes. Is this needed? Answer: Yes.
				if (source == getTextEditor()) {
					_firePropertyChange(propId);
				}
				break;
			}
			}

		}
	}

}
