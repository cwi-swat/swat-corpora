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

package org.eclipse.e4.xwt.tools.ui.designer.core.ceditor;

import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.xwt.tools.ui.designer.core.component.CustomSashForm;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualRenderer;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.IVisualRenderer.Result;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.LoadingFigureController;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.Refresher;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.IModelBuilder;
import org.eclipse.e4.xwt.tools.ui.designer.core.model.ModelChangeListener;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.RefreshContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.problems.ProblemHandler;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.e4.xwt.tools.ui.palette.page.ContributePalettePage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public abstract class ConfigureDesigner extends GraphicalEditor {

	private PalettePage palettePage;
	private IPropertySheetPage propertySheetPage;
	private IContentOutlinePage contentOutlinePage;
	private ProblemHandler problemHandler;

	private MultiSourceEditor sourceEditor;

	private LoadingFigureController loadingFigure;
	private Display display;
	private Refresher refresher;
	private Object diagram;

	private IModelBuilder modelBuilder;
	private ModelChangeListener modelBuilderListener;
	private IVisualRenderer visualsRender;
	private boolean isCreatingVisuals = false;
	private boolean isModelChanged;

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setEditDomain(createEditDomain());
		super.init(site, input);
		display = site.getShell().getDisplay();
		loadingFigure = new LoadingFigureController();
		refresher = new Refresher(display);
	}

	protected EditDomain createEditDomain() {
		EditDomain ed = new EditDomain(this);
		ed.setCommandStack(new CommandStack());
		return ed;
	}

	public Refresher getRefresher() {
		return refresher;
	}

	public void createPartControl(Composite parent) {
		ViewForm diagramPart = new ViewForm(parent, SWT.FLAT);
		ToolBar toolBar = createToolBar(diagramPart);
		if (toolBar != null && !toolBar.isDisposed()) {
			diagramPart.setTopLeft(toolBar);
		}
		sourceEditor = new MultiSourceEditor(this);
		if (sourceEditor.testValid()) {
			CustomSashForm sashForm = new CustomSashForm(diagramPart,
					SWT.VERTICAL);
			super.createPartControl(sashForm);
			boolean createSourcePage = createSourcePage(sashForm);
			if (createSourcePage) {
				sashForm.setWeights(new int[] { 1, 1 });
				diagramPart.setContent(sashForm);
			} else {
				sashForm.dispose();
				sourceEditor = null;
				Composite content = new Composite(diagramPart, SWT.NONE);
				content.setLayout(new FillLayout());
				super.createPartControl(content);
				diagramPart.setContent(content);
			}
		} else {
			sourceEditor = null;
			Composite content = new Composite(diagramPart, SWT.NONE);
			content.setLayout(new FillLayout());
			super.createPartControl(content);
			diagramPart.setContent(content);
		}
		configureDesigner();
	}

	protected void configureDesigner() {
		loadDiagram();
	}

	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		firePropertyChange(PROP_DIRTY);
	}

	private void loadDiagram() {
		final IModelBuilder builder = getModelBuilder();
		if (builder == null) {
			return;
		}
		loadingFigure.showLoadingFigure(true);
		loadingFigure.startListener(getGraphicalViewer());
		UIJob loadingJob = new UIJob(display, "Setup") {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				boolean built = builder.doLoad(ConfigureDesigner.this, monitor);
				if (built) {
					setupDiagram(builder.getDiagram());
				}
				return Status.OK_STATUS;
			}
		};
		loadingJob.setPriority(Job.SHORT);
		loadingJob.schedule();
	}

	protected IModelBuilder getModelBuilder() {
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

	private void dispatchModelEvent(final Notification event) {
		if (event.isTouch() || isCreatingVisuals) {
			return;
		}
		if (display != null && display.getThread() == Thread.currentThread()) {
			updateVisuals(event);
		} else if (display != null) {
			DisplayUtil.asyncExec(display, new Runnable() {
				public void run() {
					updateVisuals(event);
				}
			});
		}
	}

	protected void updateVisuals(Notification event) {
		if (visualsRender == null || event.isTouch()) {
			return;
		}
		isCreatingVisuals = true;
		Result result;
		try {
			result = getVisualsRender().refreshVisuals(event);
		} catch (RuntimeException e) {
			return;
		} finally {
			isCreatingVisuals = false;
		}
		if (result == null || !result.refreshed) {
			return;
		}
		List<EditPartViewer> viewers = getEditDomain().getViewers();
		for (EditPartViewer viewer : viewers) {
			updateViewer(viewer, result.visuals);
		}
		isModelChanged = true;
		firePropertyChange(PROP_DIRTY);
	}

	protected void updateViewer(EditPartViewer viewer, Object model) {
		if (viewer == null) {
			return;
		}
		EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(model);
		if (editPart == null) {
			// editPart = viewer.getContents();
		}
		if (editPart != null) {
			refresher.refreshInJob(editPart);
			if (editPart.isSelectable()) {
				viewer.select(editPart);
			}
		}
	}

	private void setupDiagram(final Object diagram) {
		if (diagram == null) {
			return;
		}
		this.diagram = diagram;
		IVisualRenderer vr = getVisualsRender();
		if (vr == null) {
			return;
		}
		isCreatingVisuals = true;
		vr.createVisuals();
		isCreatingVisuals = false;

		// if (display != null && display.getThread() == Thread.currentThread())
		// {
		// setupViewers(diagram);
		// } else if (display != null) {
		DisplayUtil.asyncExec(display, new Runnable() {
			public void run() {
				setupViewers(diagram);
			}
		});
		// }

		loadingFigure.showLoadingFigure(false);
	}

	protected void setupViewers(Object diagram) {
		List<EditPartViewer> viewers = getEditDomain().getViewers();
		for (EditPartViewer editPartViewer : viewers) {
			editPartViewer.setContents(diagram);
			RootEditPart rootEditPart = editPartViewer.getRootEditPart();
			refresher.refresh(rootEditPart, RefreshContext.ALL());
		}
	}

	public Object getDiagram() {
		return diagram;
	}

	public IVisualRenderer getVisualsRender() {
		if (visualsRender == null) {
			visualsRender = createVisualsRender(getFile(), diagram);
			Assert.isNotNull(visualsRender);
		}
		return visualsRender;
	}

	protected ToolBar createToolBar(Composite parent) {
		return null;
	}

	private boolean createSourcePage(Composite parent) {
		if (sourceEditor == null) {
			return false;
		}
		try {
			sourceEditor.init(getEditorSite(), getEditorInput());
			sourceEditor.createPartControl(parent);
		} catch (PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public EditDomain getEditDomain() {
		return (EditDomain) super.getEditDomain();
	}

	public void doSave(IProgressMonitor monitor) {
		// save top - down.
		saveGraphicalEditor(monitor);

		if (sourceEditor != null && sourceEditor.isDirty()) {
			sourceEditor.doSave(monitor);
		}
		firePropertyChange(PROP_DIRTY);
	}

	protected void saveGraphicalEditor(IProgressMonitor monitor) {
		isModelChanged = false;
		getCommandStack().markSaveLocation();
	}

	public boolean isDirty() {
		// Model changed
		if (isModelChanged) {
			return true;
		}
		// CommandStack changed
		if (super.isDirty()) {
			return true;
		}
		// Source changed
		if (sourceEditor != null) {
			return sourceEditor.isDirty();
		}
		return super.isDirty();
	}

	public void dispose() {
		if (sourceEditor != null) {
			sourceEditor.dispose();
		}
		super.dispose();
	}

	public Object getAdapter(Class adapter) {
		if (PalettePage.class.isAssignableFrom(adapter)) {
			return getPalettePage();
		} else if (CommandStack.class == adapter) {
			return getCommandStack();
		} else if (adapter == IPropertySheetPage.class) {
			return getPropertySheetPage();
		} else if (adapter == IContentOutlinePage.class) {
			return getContentOutlinePage();
		} else if (adapter == ProblemHandler.class) {
			return getProblemHandler();
		} else if (adapter == IProject.class) {
			return getProject();
		} else if (adapter == IFile.class) {
			return getFile();
		}
		return super.getAdapter(adapter);
	}

	public PalettePage getPalettePage() {
		if (palettePage == null) {
			palettePage = createPalettePage();
		}
		return palettePage;
	}

	public IPropertySheetPage getPropertySheetPage() {
		boolean recreate = true;
		if (propertySheetPage != null) {
			Control control = propertySheetPage.getControl();
			recreate = control == null || control.isDisposed();
		}
		if (recreate) {
			propertySheetPage = createPropertySheetPage();
		}
		return propertySheetPage;
	}

	public IContentOutlinePage getContentOutlinePage() {
		if (contentOutlinePage == null) {
			contentOutlinePage = createContentOutlinePage();
		}
		return contentOutlinePage;
	}

	public ProblemHandler getProblemHandler() {
		if (problemHandler == null) {
			problemHandler = createProblemHandler();
		}
		return problemHandler;
	}

	public IProject getProject() {
		IFile file = getFile();
		if (file != null) {
			return file.getProject();
		}
		return null;
	}

	public IFile getFile() {
		IEditorInput editorInput = getEditorInput();
		if (editorInput == null || !(editorInput instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput) editorInput).getFile();
	}

	protected ProblemHandler createProblemHandler() {
		// return new ConfigurableProblemHandler(this);
		return null;
	}

	protected IContentOutlinePage createContentOutlinePage() {
		return null;
	}

	protected IPropertySheetPage createPropertySheetPage() {
		return null;
	}

	protected PalettePage createPalettePage() {
		return new ContributePalettePage(this, getEditDomain());
	}

	protected abstract IModelBuilder createModelBuilder();

	protected abstract IVisualRenderer createVisualsRender(IFile file,
			Object diagram);

}
