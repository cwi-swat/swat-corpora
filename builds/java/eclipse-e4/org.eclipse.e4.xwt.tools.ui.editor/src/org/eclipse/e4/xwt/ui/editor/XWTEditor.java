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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.ui.ExceptionHandle;
import org.eclipse.e4.xwt.ui.XWTUIPlugin;
import org.eclipse.e4.xwt.ui.editor.dnd.ImageDnDAdapter;
import org.eclipse.e4.xwt.ui.editor.dnd.PaletteDnDAdapter;
import org.eclipse.e4.xwt.ui.editor.dnd.UserDefinedDnDAdapter;
import org.eclipse.e4.xwt.ui.editor.render.XWTRender;
import org.eclipse.e4.xwt.ui.editor.treeviewer.XWTTableTreeViewer;
import org.eclipse.e4.xwt.ui.utils.DisplayUtil;
import org.eclipse.e4.xwt.ui.utils.ImageManager;
import org.eclipse.e4.xwt.ui.utils.ProjectContext;
import org.eclipse.e4.xwt.ui.views.XWTView;
import org.eclipse.e4.xwt.vex.VEXCodeSynchronizer;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.e4.xwt.vex.VEXFileChecker;
import org.eclipse.e4.xwt.vex.VEXFileFormator;
import org.eclipse.e4.xwt.vex.VEXRenderer;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.tabletree.IDesignViewer;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLTableTreeHelpContextIds;

public class XWTEditor extends VEXEditor {
	/** The Java editor. */
	private CompilationUnitEditor javaEditor;

	private IFile javaFile;
	private String className;

	private DropTargetListener dropTargetAdapter;

	private ToolItem previewTool;
	private ToolItem generateTool;
	private long modificationStamp;
	private IElementChangedListener elementChangedListener = new IElementChangedListener() {
		public void elementChanged(ElementChangedEvent event) {
			if (getFileChecker() != null) {
				long timeStamp = javaFile.getModificationStamp();
				if (modificationStamp == timeStamp) {
					getFileChecker().doCheck(render.getHostClassName());
				} else {
					modificationStamp = timeStamp;
				}
			}
		}
	};

	public XWTEditor() {
		super(new XWTUIContext());
	}

	public CompilationUnitEditor getJavaEditor() {
		return javaEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.xwt.vex.VEXEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		XWTUIPlugin.getDefault().openXWTPerspective();
	}

	@Override
	protected IDesignViewer createDesignPage() {
		XWTTableTreeViewer tableTreeViewer = new XWTTableTreeViewer(getContainer());
		// Set the default info pop for XML design viewer.
		XMLUIPlugin.getInstance().getWorkbench().getHelpSystem().setHelp(tableTreeViewer.getControl(), XMLTableTreeHelpContextIds.XML_DESIGN_VIEW_HELPID);
		return tableTreeViewer;
	}

	protected VEXRenderer createRender(Canvas container) {
		return new XWTRender(container, changeListener);
	}

	protected void createToolBar(CTabFolder tabFolder) {
		tabFolder.setTabPosition(SWT.TOP);
		tabFolder.setSimple(false);

		ToolBar toolBar = new ToolBar(tabFolder, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		// toolBar.setLayoutData(new GridData(GridData.FILL_BOTH));

		previewTool = new ToolItem(toolBar, SWT.PUSH);
		previewTool.setImage(ImageManager.get(ImageManager.IMG_PREVIEW));
		previewTool.setToolTipText("Preview");
		previewTool.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				handlePreview();
			}

			public void widgetSelected(SelectionEvent e) {
				handlePreview();
			}
		});

		generateTool = new ToolItem(toolBar, SWT.PUSH);
		generateTool.setImage(JavaPluginImages.get(JavaPluginImages.IMG_OBJS_CLASS));
		generateTool.setToolTipText("Generate Java codes");

		generateTool.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				generateCLRCodeAction();
			}

			public void widgetSelected(SelectionEvent e) {
				generateCLRCodeAction();
			}
		});
		generateTool.setEnabled(false);

		tabFolder.setTopRight(toolBar);
		tabFolder.setTabHeight(Math.max(toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT).y, tabFolder.getTabHeight()));
	}

	void handlePreview() {
		IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
		IJavaProject javaProject = JavaCore.create(file.getProject());
		if (!javaProject.exists()) {
			return;
		}
		try {
			XWT.setLoadingContext(ProjectContext.getContext(javaProject));
			DisplayUtil.open(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void setJavaEditor(String fullyQualifiedName) {
		IJavaProject javaProject = getJavaProject();
		removeElementChangedListener(javaProject);
		try {
			if (fullyQualifiedName == null) {
				return;
			}
			IType type = javaProject.findType(fullyQualifiedName);
			if (type == null || !type.exists()) {
				return;
			}
			javaFile = (IFile) type.getResource();
			FileEditorInput editorInput = new FileEditorInput(javaFile);
			if (javaEditor == null) {
				javaEditor = new CompilationUnitEditor();
				int javaPageIndex = addPage(javaEditor, editorInput);
				setPageText(javaPageIndex, "Java");
				setPageImage(javaPageIndex, JavaPluginImages.get(JavaPluginImages.IMG_OBJS_CLASS));

			} else {
				javaEditor.setInput(editorInput);
			}
			this.className = fullyQualifiedName;
			if (generateTool != null) {
				generateTool.setEnabled(true);
			}
			addElementChangedListener(javaProject);

			XWTCodeSynchronizer codeSynchronizer = (XWTCodeSynchronizer) getCodeSynchronizer();
			codeSynchronizer.setType(type);
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	protected IJavaProject getJavaProject() {
		IFile file = (IFile) getEditorInput().getAdapter(IFile.class);

		IJavaProject javaProject = JavaCore.create(file.getProject());
		if (!javaProject.exists()) {
			return null;
		}
		return javaProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor )
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (javaEditor != null) {
			javaEditor.doSave(monitor);
		}
	}

	protected boolean handleInputChanged(IDocument newInput) {
		String value = newInput.get();
		if (newInput == null) {
			return false;
		}
		boolean handling = super.handleInputChanged(newInput);
		if (handling) {
			String className = render.getHostClassName();
			if (this.className == null || !this.className.equals(className)) {
				setJavaEditor(className);
			}
		}
		refreshXWTView(value);
		return handling;
	}

	private void refreshXWTView(String value) {
		IFile file = (IFile) getEditorInput().getAdapter(IFile.class);

		if (file != null) {
			XWTUIPlugin.checkStartup();
			try {
				XWTView view = (XWTView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(XWTView.ID);
				if (view != null) {
					view.setContentWithException(value, file);
				}
			} catch (Exception e) {
				// No need to handle the refresh.s 
				// ExceptionHandle.handle(e, "");
			}
		}
	}

	@Override
	protected VEXFileChecker createFileChecker() {
		return new XWTFileChecker(getTextEditor());
	}

	protected VEXCodeSynchronizer createCodeSynchronizer() {
		try {
			IJavaProject javaProject = getJavaProject();
			if (javaProject == null || className == null) {
				return null;
			}
			IType findType = javaProject.findType(className);
			if (findType != null) {
				return new XWTCodeSynchronizer(this, findType);
			}
		} catch (JavaModelException e) {
		}
		return null;
	}

	public void addElementChangedListener(IJavaProject javaProject) {
		int eventMask = ElementChangedEvent.POST_CHANGE | ElementChangedEvent.POST_RECONCILE;
		JavaModelManager.getDeltaState().addElementChangedListener(elementChangedListener, eventMask);
	}

	public void removeElementChangedListener(IJavaProject javaProject) {
		int eventMask = ElementChangedEvent.POST_CHANGE | ElementChangedEvent.POST_RECONCILE;
		JavaModelManager.getDeltaState().removeElementChangedListener(elementChangedListener);
	}

	public void initializeDND(VEXEditor editor) {
		if (dropTargetAdapter == null) {
			dropTargetAdapter = createDropTargetListener();
		}
		update(editor);
	}

	private void update(VEXEditor vexEditor) {
		// clear up old
		StyledText styledText = vexEditor.getTextWidget();
		if (styledText != null) {
			DropTarget dropTarget = (DropTarget) styledText.getData(DND.DROP_TARGET_KEY);
			if (dropTarget != null) {
				dropTarget.removeDropListener(dropTargetAdapter);
			}
		}

		// setup new
		vexEditor.getTextWidget().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				StyledText styledText = (StyledText) e.widget;
				DropTarget dropTarget = (DropTarget) styledText.getData(DND.DROP_TARGET_KEY);
				if (dropTarget != null) {
					dropTarget.removeDropListener(dropTargetAdapter);
				}
			}
		});

		DropTarget dropTarget = (DropTarget) styledText.getData(DND.DROP_TARGET_KEY);
		if (dropTarget == null) {
			dropTarget = new DropTarget(styledText, DND.DROP_MOVE | DND.DROP_COPY);
			dropTarget.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
		}
		dropTarget.addDropListener(dropTargetAdapter);

		DragSource dragSource = (DragSource) styledText.getData(DND.DRAG_SOURCE_KEY);
		if (dragSource == null) {
			dragSource = new DragSource(styledText, DND.DROP_COPY | DND.DROP_MOVE);

			Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

			dragSource.setTransfer(types);
		}
		dragSource.addDragListener(dragSourceAdapter);
		// if (dropProxy == null) {
		// dropProxy = new XWTEditorDropProxy(this);
		// }
		final PackageExplorerPart part = PackageExplorerPart.getFromActivePerspective();
		if (part != null) {
			TreeViewer treeViewer = part.getTreeViewer();
			dragSource = (DragSource) treeViewer.getControl().getData(DND.DRAG_SOURCE_KEY);
			if (dragSource != null) {
				dragSource.addDragListener(new DragSourceAdapter() {
					public void dragStart(DragSourceEvent event) {
						part.setLinkingEnabled(false);
					}
				});
			}
		}
	}

	private IProgressMonitor getProgressMonitor() {
		ProgressManager progresManager = (ProgressManager) getSite().getWorkbenchWindow().getWorkbench().getProgressService();
		IProgressMonitor monitor = progresManager.getDefaultMonitor();
		return monitor;
	}

	private XWTDropTargetListener createDropTargetListener() {
		XWTDropTargetListener dropTargetListener = new XWTDropTargetListener() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			public void drop(DropTargetEvent event) {
				super.drop(event);
				IFile file = (IFile) getTextEditor().getEditorInput().getAdapter(IFile.class);

				VEXFileFormator formator = new VEXFileFormator();
				IDocument document = getTextEditor().getTextViewer().getDocument();
				try {
					formator.format(document, file.getContentDescription().getContentType().getId());
				} catch (CoreException e) {
					e.printStackTrace();
				}
				PackageExplorerPart part = PackageExplorerPart.getFromActivePerspective();
				if (part != null) {
					part.setLinkingEnabled(true);
				}
			}
		};
		dropTargetListener.addDnDAdapter(new PaletteDnDAdapter(this));
		dropTargetListener.addDnDAdapter(new UserDefinedDnDAdapter(this));
		dropTargetListener.addDnDAdapter(new ImageDnDAdapter(this));
		return dropTargetListener;
	}

	@Override
	public void setFocus() {
		super.setFocus();
		refreshXWTView(super.getTextEditor().getTextViewer().getDocument().get());
	}
}
