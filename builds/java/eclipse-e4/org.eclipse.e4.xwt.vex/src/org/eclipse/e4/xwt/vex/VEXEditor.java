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
package org.eclipse.e4.xwt.vex;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.xwt.vex.palette.CustomPalettePage;
import org.eclipse.e4.xwt.vex.palette.PaletteResourceManager;
import org.eclipse.e4.xwt.vex.palette.PaletteViewManager;
import org.eclipse.e4.xwt.vex.palette.customize.CustomizeComponentFactory;
import org.eclipse.e4.xwt.vex.palette.customize.model.CustomizeComponent;
import org.eclipse.e4.xwt.vex.palette.part.DynamicPaletteViewer;
import org.eclipse.e4.xwt.vex.swt.AnimatedImage;
import org.eclipse.e4.xwt.vex.swt.CustomSashForm;
import org.eclipse.e4.xwt.vex.toolpalette.Entry;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPalette;
import org.eclipse.e4.xwt.vex.toolpalette.ToolPaletteFactory;
import org.eclipse.e4.xwt.vex.util.ImageHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author BOB
 * 
 */
public abstract class VEXEditor extends XMLMultiPageEditorPart {
	static boolean toolViewShown = false;
	protected VEXContext context;

	private VEXCodeSynchronizer codeSynchronizer;
	private VEXFileChecker fileChecker;

	protected VEXRenderer render;

	protected Canvas container;
	protected ScrolledComposite scrolledComposite;
	protected SashForm sashForm;

	protected AnimatedImage loadingMessage;

	/** The text editor. */
	private StructuredTextEditor fTextEditor;
	private Refresher refresher = new Refresher();

	/** Tools Palette objects */
	private PaletteResourceManager tResourceManager;
	private static PalettePage palettePage;

	private IStatusLineManager slManager;

	protected PropertyChangeListener changeListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			invokeInputChanged();
		}
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {

		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			handleKeyEvent(e);
		}
	};

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseDown(MouseEvent e) {
			super.mouseDown(e);
			handleMouseEvent(e);
		}
	};

	/**
	 * @param e
	 */
	public void handleMouseEvent(MouseEvent e) {
		doDynamicPalette(new Point(e.x, e.y));
	}

	/**
	 * @param e
	 */
	public void handleKeyEvent(KeyEvent e) {
		StyledText control = (StyledText) e.widget;
		Caret caret = control.getCaret();
		if (caret != null) {
			doDynamicPalette(caret.getLocation());
		}
	}

	/**
	 * @param point
	 */
	public void doDynamicPalette(Point point) {
		IDOMNode node = getDOMNode(point);
		if (palettePage == null) {
			return;
		}

		if (node != null) {
			Node parentNode = null;
			if (node instanceof IDOMText) {
				parentNode = node.getParentNode();
			} else {
				parentNode = node;
			}

			tResourceManager = getPaletteResourceManager();
			Resource dynamicResource = tResourceManager.getDynamicResource();
			Resource resource = tResourceManager.getResource();
			// EMF model
			ToolPalette toolPalette = (ToolPalette) resource.getContents().get(0);
			EList<Entry> entries = toolPalette.getEntries();

			// ToolPalette dynamicPalette = (ToolPalette) dynamicResource.getContents().get(0);
			// EList<Entry> dynamicEntries = dynamicPalette.getEntries();

			Entry dynamicEntryRoot = null;
			for (Entry entry : entries) {
				if (entry.getName().equals(EditorMessages.VEXEditor_Dynamic)) {
					dynamicEntryRoot = entry;
					break;
				}
			}
			if (dynamicEntryRoot != null) {
				entries.remove(dynamicEntryRoot);
			}
			dynamicEntryRoot = ToolPaletteFactory.eINSTANCE.createEntry();
			dynamicEntryRoot.setName(EditorMessages.VEXEditor_Dynamic);
			dynamicEntryRoot.setToolTip(EditorMessages.VEXEditor_DynamicCategory);
			entries.add(dynamicEntryRoot);

			PaletteViewer paletteViewer = ((CustomPalettePage) palettePage).getPaletteViewer();

			DynamicPaletteViewer dynamicPaletteViewer = null;
			Object objectPaletteViewer = paletteViewer.getProperty("Dynamic_PaletteViewer");
			if (objectPaletteViewer instanceof DynamicPaletteViewer) {
				dynamicPaletteViewer = (DynamicPaletteViewer) objectPaletteViewer;
			}
			if (dynamicPaletteViewer == null) {
				return;
			}
			PaletteRoot root = dynamicPaletteViewer.getPaletteRoot();
			List paletteChildren = root.getChildren();
			PaletteGroup dynamicPaletteGroup = null;
			for (Object object : paletteChildren) {
				if (((PaletteGroup) object).getLabel().equals(EditorMessages.VEXEditor_Dynamic)) {
					dynamicPaletteGroup = (PaletteGroup) object;
					break;
				}
			}
			if (dynamicPaletteGroup == null) {
				dynamicPaletteGroup = new PaletteGroup(EditorMessages.VEXEditor_Dynamic);
			}
			List children = dynamicPaletteGroup.getChildren();
			int count = children.size();
			for (int i = 0; i < count; i++) {
				dynamicPaletteGroup.remove((PaletteEntry) children.get(0));
			}
			root.remove(dynamicPaletteGroup);

			// add the dynamic palette
			if (parentNode.getLocalName() != null && !parentNode.getLocalName().equals("")) { //$NON-NLS-1$

				List<Entry> insert = getSubEntries(parentNode, entries);
				// insert.addAll(customizeComponentList);
				if (insert != null) {
					for (Entry ent : insert) {
						if (ent.getScope() != null && ent.getScope().equals(parentNode.getLocalName())) {
							Entry subEntry = (Entry) EcoreUtil.copy(ent);
							// add sub entry
							dynamicEntryRoot.getEntries().add(subEntry);

							CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(subEntry.getName(), subEntry.getToolTip(), subEntry, new SimpleFactory(dynamicResource.getClass()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getIcon()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getLargeIcon()));
							dynamicPaletteGroup.add(component);

						}
					}
				}
				List<Entry> customizeComponentList = getCustomizeEntries();
				for (Entry entry : customizeComponentList) {
					if (entry.getScope() != null && entry.getScope().equals(parentNode.getLocalName())) {
						Entry subEntry = (Entry) EcoreUtil.copy(entry);
						// add sub entry
						dynamicEntryRoot.getEntries().add(subEntry);
						CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(subEntry.getName(), subEntry.getToolTip(), subEntry, new SimpleFactory(dynamicResource.getClass()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getIcon()), ImageHelper.getImageDescriptor(tResourceManager, subEntry.getLargeIcon()));
						dynamicPaletteGroup.add(component);
					}
				}
			}
			root.add(dynamicPaletteGroup);
			RootEditPart rootEditPart = dynamicPaletteViewer.getRootEditPart();
			refreshAllEditParts(rootEditPart);
		}
	}

	public List<Entry> getCustomizeEntries() {
		List<Entry> result = new ArrayList<Entry>();
		List<String> customizeComponentNameList = CustomizeComponentFactory.getCustomizeComponentFactory().getCustomizeComponentNameList();
		if (customizeComponentNameList != null) {
			Entry entry = null;
			for (int i = 0; i < customizeComponentNameList.size(); i++) {
				CustomizeComponent customizeComponent = CustomizeComponentFactory.getCustomizeComponentFactory().getCustomizeComponentByName(customizeComponentNameList.get(i));
				if (customizeComponent != null) {
					entry = ToolPaletteFactory.eINSTANCE.createEntry();
					entry.setName(customizeComponent.getName());
					entry.setScope(customizeComponent.getScope());
					entry.setIcon(customizeComponent.getIcon());
					entry.setLargeIcon(customizeComponent.getLargeIcon());
					entry.setToolTip(customizeComponent.getTooptip());
					entry.setContent(customizeComponent.getContent());
				}
				if (entry != null) {
					result.add(entry);
				}
			}
		}
		return result;
	}

	private void refreshAllEditParts(EditPart part) {
		part.refresh();
		List children = part.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EditPart child = (EditPart) iter.next();
			refreshAllEditParts(child);
		}
	}

	/**
	 * @param point
	 * @return
	 */
	private IDOMNode getDOMNode(Point point) {
		// to get dom node based on current insertion point for text
		StructuredTextViewer textViewer = fTextEditor.getTextViewer();
		StyledText styledText = textViewer.getTextWidget();
		Point absolutePosition = new Point(point.x, point.y);

		int widgetOffset = 0;
		try {
			Method method = StyledText.class.getDeclaredMethod("getOffsetAtPoint", int.class, int.class, int[].class, //$NON-NLS-1$
					boolean.class);
			method.setAccessible(true);

			int[] trailing = new int[1];
			widgetOffset = (Integer) method.invoke(styledText, absolutePosition.x, absolutePosition.y, trailing, false);
			widgetOffset += trailing[0];
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		IDOMNode node = VEXTextEditorHelper.getNode(textViewer, widgetOffset);

		return node;
	}

	private List<Entry> getSubEntries(Node node, List<Entry> entries) {
		List<Entry> result = null;
		for (Entry entry : entries) {
			if (entry.getName().equalsIgnoreCase(node.getLocalName())) {
				result = entry.getEntries();
			}
			if (result == null) {
				result = getSubEntries(node, entry.getEntries());
			}
		}
		return result;
	}

	class Refresher implements Runnable {
		private IDocument document;
		private long time = -1;

		public void run() {
			if ((System.currentTimeMillis() - time) < 800 || Display.getDefault().getActiveShell() == null) {
				Display.getDefault().timerExec(1000, this);
				return;
			}
			try {
				handleInputChanged(document);
			} finally {
				document = null;
				time = -1;
			}
		}

		public IDocument getDocument() {
			return document;
		}

		public void setDocument(IDocument document) {
			this.document = document;
			this.time = System.currentTimeMillis();
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}
	}

	public VEXEditor(VEXContext context) {
		this.context = context;
	}

	public IProject getProject() {
		IResource resource = (IResource) getEditorInput().getAdapter(IResource.class);
		if (resource != null) {
			return resource.getProject();
		}
		throw new IllegalStateException();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		IWorkbenchPage activePage = getSite().getWorkbenchWindow().getActivePage();
		if (activePage != null && !toolViewShown) {
			activePage.showView(context.getToolViewID());
			toolViewShown = true;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (fileChecker != null) {
			fileChecker.deleteMarkers();
		}
		if (render != null) {
			render.dispose();
		}
	}

	@Override
	protected Composite createPageContainer(Composite parent) {
		sashForm = new CustomSashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setBackgroundMode(SWT.INHERIT_DEFAULT);

		scrolledComposite = new org.eclipse.swt.custom.ScrolledComposite(sashForm, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		scrolledComposite.setBackground(scrolledComposite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		container = new Canvas(scrolledComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		container.setBackgroundMode(SWT.INHERIT_DEFAULT);

		container.setLayout(new GridLayout());

		loadingMessage = new AnimatedImage(container, SWT.CENTER);
		loadingMessage.setLayoutData(new GridData(GridData.FILL_BOTH));
		loadingMessage.setHorizontalAlignment(SWT.CENTER);
		loadingMessage.setVerticalAlignment(SWT.CENTER);
		loadingMessage.setBackgroundMode(SWT.INHERIT_DEFAULT);
		loadingMessage.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		scrolledComposite.setContent(container);
		try {
			loadingMessage.setImageFile(ResourceManager.getImageURL(ResourceManager.URL_PATH_BUSY));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Composite composite = super.createPageContainer(sashForm);

		render = VEXRenderRegistry.getRender(container);
		if (render == null) {
			render = createRender(container);
		}
		slManager = getEditorSite().getActionBars().getStatusLineManager();
		return composite;
	}

	protected VEXRenderer createRender(Canvas container) {
		return null;
	}

	protected abstract VEXFileChecker createFileChecker();

	public VEXFileChecker getFileChecker() {
		if (fileChecker == null) {
			fileChecker = createFileChecker();
		}
		return fileChecker;
	}

	class DocumentListener implements IDocumentListener {
		public void documentChanged(DocumentEvent event) {
			invokeInputChanged();
		}

		public void documentAboutToBeChanged(DocumentEvent event) {
			codeAboutToBeChanged();
		}
	}

	protected void codeAboutToBeChanged() {
		VEXCodeSynchronizer codeSync = getCodeSynchronizer();
		if (codeSync != null) {
			codeSync.codeAboutToBeChanged();
		}
	}

	protected void invokeInputChanged() {
		long previous = refresher.getTime();
		if (fTextEditor.getTextViewer() != null) {
			refresher.setDocument(fTextEditor.getTextViewer().getDocument());
			if (previous == -1) {
				Display.getDefault().timerExec(1000, refresher);
			}
		}
	}

	@Override
	public int addPage(IEditorPart editor, IEditorInput input) throws PartInitException {
		if (editor instanceof StructuredTextEditor) {
			fTextEditor = (StructuredTextEditor) editor;
		}
		sashForm.setWeights(new int[] { 60, 40 });
		return super.addPage(editor, input);
	}

	@Override
	protected void createPages() {
		super.createPages();

		if (fTextEditor != null) {
			StructuredTextViewer textViewer = fTextEditor.getTextViewer();

			textViewer.getTextWidget().addMouseListener(mouseAdapter);
			textViewer.getTextWidget().addKeyListener(keyAdapter);

			textViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					StructuredTextViewer textViewer = fTextEditor.getTextViewer();
					StyledText control = (StyledText) textViewer.getTextWidget();
					Caret caret = control.getCaret();
					if (caret != null) {
						Point location = caret.getLocation();
						// if (!isExecuted) {
						doDynamicPalette(location);
						// }
						// isExecuted = false;
					}
				}
			});

			textViewer.addTextListener(new ITextListener() {
				public void textChanged(TextEvent event) {
					// TODO Auto-generated method stub
					StructuredTextViewer textViewer = fTextEditor.getTextViewer();
					StyledText control = (StyledText) textViewer.getTextWidget();
					Caret caret = control.getCaret();
					if (caret != null) {
						Point location = caret.getLocation();
						// updatePasteActionState(location);
					}
				}
			});

			IDocument document = textViewer.getDocument();
			document.addDocumentListener(new DocumentListener());
			invokeInputChanged();
			initializeDND(this);

			Composite composite = getContainer();
			if (composite instanceof CTabFolder) {
				CTabFolder tabFolder = (CTabFolder) composite;
				createToolBar(tabFolder);
			}

			// TODO
			for (int i = 0; i < getPageCount(); i++) {
				if (getPageText(i).equals("")) { //$NON-NLS-1$
					setPageImage(i, ResourceManager.getImage(ResourceManager.IMG_ELEMENT));
				}
				if (getPageText(i).equals("")) { //$NON-NLS-1$
					setPageImage(i, ResourceManager.getImage(ResourceManager.IMG_TABLE));
				}
			}
		}
	}

	public void initializeDND(VEXEditor editor) {

		StyledText styledText = this.getTextWidget();
		if (styledText != null) {
			DragSource dragSource = (DragSource) styledText.getData(DND.DRAG_SOURCE_KEY);
			if (dragSource != null) {
				dragSource.removeDragListener(dragSourceAdapter);
			}
		}

		DragSource dragSource = (DragSource) styledText.getData(DND.DRAG_SOURCE_KEY);
		if (dragSource == null) {
			dragSource = new DragSource(styledText, DND.DROP_COPY | DND.DROP_MOVE);

			Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

			dragSource.setTransfer(types);
		}
		dragSource.addDragListener(dragSourceAdapter);

	}

	protected DragSourceListener dragSourceAdapter = new DragSourceListener() {

		private String dragDataText;

		public void dragFinished(DragSourceEvent event) {
			dragDataText = null;
		}

		public void dragSetData(DragSourceEvent event) {
			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = dragDataText;
			}
		}

		public void dragStart(DragSourceEvent event) {
			dragDataText = getTextWidget().getSelectionText();
		}

	};

	public Object getAdapter(Class type) {
		if (type == PalettePage.class) {
			return getPalettePage();
		}
		if (type == PaletteResourceManager.class) {
			return getPaletteResourceManager();
		}
		return super.getAdapter(type);
	}

	/**
	 * @return
	 */
	public PaletteResourceManager getPaletteResourceManager() {
		if (tResourceManager == null) {
			tResourceManager = new PaletteResourceManager(this);
		}

		return tResourceManager;
	}

	private PalettePage getPalettePage() {
		if (palettePage == null) {
			PaletteViewManager manager = new PaletteViewManager(this);
			palettePage = new CustomPalettePage(manager.getPaletteViewerProvider());
		}
		return palettePage;
	}

	public PalettePage getVEXEditorPalettePage() {
		return palettePage;
	}

	protected abstract void createToolBar(CTabFolder tabFolder);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor )
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			updateCodeManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fTextEditor.doSave(monitor);
	}

	protected boolean handleInputChanged(IDocument newInput) {
		VEXCodeSynchronizer codeSynchronizer = getCodeSynchronizer();
		if (codeSynchronizer != null) {
			boolean handling = codeSynchronizer.handleInputChanged(newInput);
			if (!handling) {
				return false;
			}
		}

		// View synchronize
		try {
			ApplicationWindow applicationWindow = (ApplicationWindow) getSite().getPage().getWorkbenchWindow();
			MenuManager menuManager = applicationWindow.getMenuBarManager();
			if (menuManager != null) {
				menuManager.updateAll(true);
			}

			String value = newInput.get();
			IFile file = (IFile) getEditorInput().getAdapter(IFile.class);

			if (container.getDisplay() != null) {
				container.setCursor(container.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
				while (!container.getDisplay().readAndDispatch()) {
				}
				if (render != null && render.updateView(value, file)) {
					if (loadingMessage != null && !loadingMessage.isDisposed()) {
						loadingMessage.dispose();
						loadingMessage = null;
					}
					return true;
				}
				if (loadingMessage != null && !loadingMessage.isDisposed()) {
					loadingMessage.stop();
					loadingMessage.setText("No window found or an error occurs."); //$NON-NLS-1$
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			container.setCursor(null);
			if (render != null) {
				getFileChecker().doCheck(render.getHostClassName());
			}
		}
		return false;
	}

	protected VEXCodeSynchronizer getCodeSynchronizer() {
		if (codeSynchronizer == null) {
			codeSynchronizer = createCodeSynchronizer();
		}
		return codeSynchronizer;
	}

	protected abstract VEXCodeSynchronizer createCodeSynchronizer();

	public void generateCLRCodeAction() {
		getCodeSynchronizer().generateHandles();
	}

	public void openDefinitionAction() {
		getCodeSynchronizer().openDefinition();
	}

	protected void updateCodeManager() {
		StructuredTextViewer textViewer = fTextEditor.getTextViewer();
		int offset = textViewer.getTextWidget().getCaretOffset();
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer, offset);
		Node root = (Node) treeNode;
		while ((root != null) && (root.getParentNode() != null) && !(root.getParentNode() instanceof Document)) {
			root = root.getParentNode();
		}
		updateCodeManager((IDOMNode) root);
	}

	protected void updateCodeManager(IDOMNode parentNode) {
		VEXCodeSynchronizer generator = getCodeSynchronizer();
		if (generator == null) {
			return;
		}
		generator.update(parentNode);
	}

	public StyledText getTextWidget() {
		if (fTextEditor == null || fTextEditor.getTextViewer() == null) {
			return null;
		}
		return fTextEditor.getTextViewer().getTextWidget();
	}

	private String updateCacheContent(IDocument newInput) {
		StringBuffer buffer = new StringBuffer();
		String content = newInput.get();
		for (char c : content.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public StructuredTextEditor getTextEditor() {
		return fTextEditor;
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
		IDOMNode domNode = (IDOMNode) node;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if (context.hasType(domNode)) {
				NamedNodeMap nodeMap = node.getAttributes();
				for (int j = nodeMap.getLength() - 1; j >= 0; j--) {
					IDOMAttr attrNode = (IDOMAttr) nodeMap.item(j);
					String attrName = attrNode.getName();
					String attrValue = attrNode.getValue();
					if (context.isEventHandle(domNode, attrName)) {
						if (attrValue.equals(oldAttrValue)) {
							attrNode.setNodeValue(inputHandler);
						}
					}
				}
			}
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

	public VEXContext getContext() {
		return context;
	}

	/**
	 * Using for add an entry to VEX editor
	 * 
	 * @author BOB
	 * @param entry
	 */
	public void defaultCreation(Entry entry) {
		StyledText control = fTextEditor.getTextViewer().getTextWidget();
		Caret caret = control.getCaret();
		if (caret != null) {
			Point location = caret.getLocation();
			IDOMNode node = getDOMNode(location);
			IStructuredDocumentRegion refionDocumentRegion = node.getFirstStructuredDocumentRegion();
			int caretOffset = getAddEntryPosition(refionDocumentRegion, location, node, entry);
			if (caretOffset >= 0) {
				insertEntry(caretOffset, entry);
				updateStatusBarMessage(EditorMessages.VEXEditor_NODE + entry.getName() + EditorMessages.VEXEditor_SUBFIX_OK);
			} else {
				updateStatusBarMessage(EditorMessages.VEXEditor_NODE + entry.getName() + EditorMessages.VEXEditor_SUBFIX_FAIL);
			}
			adjustAfterDefaultCreation(node);
		}

	}

	/**
	 * @param node
	 */
	public void adjustAfterDefaultCreation(Node node) {
		Point location = getCurrentLocation();
		doDynamicPalette(location);
	}

	/**
	 * @return
	 */
	public Point getCurrentLocation() {
		StructuredTextEditor textEditor = getTextEditor();
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		StyledText control = (StyledText) textViewer.getTextWidget();
		Caret caret = control.getCaret();
		Point location = caret.getLocation();
		return location;
	}

	/**
	 * get the insert position from the given start location, using for add an node to VEX editor
	 * 
	 * @param refionDocumentRegion
	 * @param startLocation
	 * @param addNode
	 * @param addEntry
	 * @return
	 */
	protected int getAddEntryPosition(IStructuredDocumentRegion refionDocumentRegion, Point startLocation, IDOMNode addNode, Entry addEntry) {
		StructuredTextEditor textEditor = getTextEditor();
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		int cursor = VEXTextEditorHelper.getOffsetAtPoint(textViewer, startLocation);
		if (addNode.getParentNode() == null) {
			return -1;
		}
		int caretOffset = context.findDropPosition(addNode, addEntry, cursor);
		if (caretOffset < 0) {
			IStructuredDocumentRegion refionDocumentRegion2 = refionDocumentRegion.getNext();
			int starat = 0;
			int end = 0;
			if (refionDocumentRegion2 != null) {
				starat = refionDocumentRegion2.getStartOffset();
				end = refionDocumentRegion2.getEndOffset();
			}
			Point point = new Point(starat, end);
			IStructuredDocumentRegion endStructuredDocumentRegion = addNode.getEndStructuredDocumentRegion();
			int insertPosition = -1;
			if (endStructuredDocumentRegion != null) {
				insertPosition = endStructuredDocumentRegion.getEnd();
			} else {
				IStructuredDocumentRegion startStructuredDocumentRegion = addNode.getStartStructuredDocumentRegion();
				if (startStructuredDocumentRegion != null) {
					insertPosition = startStructuredDocumentRegion.getEnd();
				}
			}
			addNode = (IDOMNode) addNode.getParentNode();
			int firstValue = getAddEntryPosition(refionDocumentRegion2, point, addNode, addEntry);
			if (firstValue > insertPosition) {
				return firstValue;
			}
			if (firstValue == -1) {
				return -1;
			}
			return insertPosition;
		}
		return caretOffset;
	}

	/**
	 * insert node at special location
	 * 
	 * @param caretOffset
	 * @param addEntry
	 */
	private void insertEntry(int caretOffset, Entry addEntry) {
		IDocument document = getTextEditor().getTextViewer().getDocument();
		Template template = new Template(addEntry.getName(), "", addEntry.getContext().getName(), addEntry.getContent(), true); //$NON-NLS-1$
		ContextTypeRegistry registry = XMLUIPlugin.getDefault().getTemplateContextRegistry();
		if (registry != null) {
			TemplateContextType type = registry.getContextType(template.getContextTypeId());

			int length = 0;

			DocumentTemplateContext templateContext = new DocumentTemplateContext(type, document, new Position(caretOffset, length));
			if (templateContext.canEvaluate(template)) {
				try {
					// fix bug about dnd element undo
					IRewriteTarget target = null;
					StructuredTextEditor textEditor = getTextEditor();
					StructuredTextViewer textViewer = textEditor.getTextViewer();
					if (textViewer instanceof ITextViewerExtension) {
						ITextViewerExtension extension = (ITextViewerExtension) textViewer;
						target = extension.getRewriteTarget();
					}
					if (target != null)
						target.beginCompoundChange();

					TemplateBuffer templateBuffer = templateContext.evaluate(template);
					String templateString = templateBuffer.getString();
					document.replace(caretOffset, length, templateString);

					StyledText styledText = getTextWidget();
					int position = getCursorOffset(templateBuffer) + caretOffset;
					styledText.setCaretOffset(position);
					styledText.setFocus();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		IFile file = (IFile) getTextEditor().getEditorInput().getAdapter(IFile.class);

		VEXFileFormator formator = new VEXFileFormator();
		try {
			formator.format(document, file.getContentDescription().getContentType().getId());
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param buffer
	 * @return
	 */
	private int getCursorOffset(TemplateBuffer buffer) {
		TemplateVariable[] variables = buffer.getVariables();
		for (int i = 0; i != variables.length; i++) {
			TemplateVariable variable = variables[i];
			if (variable.getType().equals(GlobalTemplateVariables.Cursor.NAME))
				return variable.getOffsets()[0];
		}

		return buffer.getString().length();
	}

	/**
	 * update eclipse workbench status bar message
	 * 
	 * @author BOB
	 * @param message
	 */
	private void updateStatusBarMessage(String message) {
		if (slManager != null) {
			if (slManager instanceof SubStatusLineManager) {
				((SubStatusLineManager) slManager).setVisible(true);
			}
			slManager.setMessage(message);
		}
	}
}
