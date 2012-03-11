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
package org.eclipse.e4.xwt.vex.palette.part;

import java.util.List;

import org.eclipse.e4.xwt.vex.EditorMessages;
import org.eclipse.e4.xwt.vex.VEXEditor;
import org.eclipse.e4.xwt.vex.palette.PaletteRootFactory;
import org.eclipse.e4.xwt.vex.palette.customize.CustomerPaletteContextMenuProvider;
import org.eclipse.e4.xwt.vex.palette.customize.InvokeType;
import org.eclipse.e4.xwt.vex.palette.customize.dialogs.CustomizePaletteDialog;
import org.eclipse.e4.xwt.vex.swt.CustomSashForm;
import org.eclipse.e4.xwt.vex.toolpalette.impl.EntryImpl;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;

/**
 * @author BOB
 */
public class ToolPaletteViewerProvider extends PaletteViewerProvider {

	private IEditorPart editorPart;
	private PaletteViewer toolPaletteViewer;
	private DynamicPaletteViewer dynamicPaletteViewer;
	private CustomizePaletteViewer customizePaletteViewer;
	private CustomSashForm sashFormMain;
	private CustomSashForm dynamicAndCustomizeSashForm;

	public ToolPaletteViewerProvider(EditDomain graphicalViewerDomain, IEditorPart editorPart) {
		super(graphicalViewerDomain);
		this.editorPart = editorPart;
	}

	public PaletteViewer createPaletteViewer(Composite parent) {
		PageBook pageBook = (PageBook) parent;

		sashFormMain = new CustomSashForm(pageBook, SWT.VERTICAL);

		// sashForm.
		CustomSashForm toolSashForm = new CustomSashForm(sashFormMain, SWT.VERTICAL);

		toolPaletteViewer = new ToolPaletteViewer(this.getEditDomain());
		toolPaletteViewer.createControl(toolSashForm);
		configurePaletteViewer(toolPaletteViewer);
		hookPaletteViewer(toolPaletteViewer);
		dynamicAndCustomizeSashForm = new CustomSashForm(sashFormMain, SWT.VERTICAL);

		Composite dynamicComposite = new Composite(dynamicAndCustomizeSashForm, SWT.BORDER);
		dynamicComposite.setLayout(new FillLayout());
		Composite customizeComposite = new Composite(dynamicAndCustomizeSashForm, SWT.BORDER);
		customizeComposite.setLayout(new FillLayout());

		// dynamic part
		dynamicPaletteViewer = new DynamicPaletteViewer();
		dynamicPaletteViewer.createControl(dynamicComposite);
		configurePaletteViewer(dynamicPaletteViewer);
		dynamicPaletteViewer.setPaletteRoot(PaletteRootFactory.createDynamicPalette(editorPart));

		// customize part
		customizePaletteViewer = new CustomizePaletteViewer();
		customizePaletteViewer.createControl(customizeComposite);
		configurePaletteViewer(customizePaletteViewer);
		customizePaletteViewer.setPaletteRoot(PaletteRootFactory.createCustomizePalette(editorPart));

		DropTarget dropTarget = new DropTarget(customizeComposite, DND.DROP_COPY | DND.DROP_MOVE);
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		dropTarget.setTransfer(types);
		dropTarget.addDropListener(dropTargetAdapter);

		sashFormMain.setWeights(new int[] { 2, 1 });
		dynamicAndCustomizeSashForm.setWeights(new int[] { 1, 1 });

		// Show the sashForm manually.
		pageBook.showPage(sashFormMain);

		toolPaletteViewer.setProperty(EditorMessages.CustomizeComponentFactory_VIEWER_EDITOR, editorPart); //$NON-NLS-1$
		toolPaletteViewer.setProperty("Dynamic_PaletteViewer", dynamicPaletteViewer);
		toolPaletteViewer.setProperty("Customize_PaletteViewer", customizePaletteViewer);
		toolPaletteViewer.setProperty("ToolSashForm", toolSashForm);
		toolPaletteViewer.setProperty("SashFormMain", sashFormMain);
		dynamicPaletteViewer.setProperty("DynamicAndCustomizeSashForm", dynamicAndCustomizeSashForm);
		dynamicPaletteViewer.setProperty("DynamicComposite", dynamicComposite);
		customizePaletteViewer.setProperty("DynamicAndCustomizeSashForm", dynamicAndCustomizeSashForm);
		customizePaletteViewer.setProperty("CustomizeComposite", customizeComposite);
		return toolPaletteViewer;
	}

	public DynamicPaletteViewer getDynamicPaletteViewer() {
		return dynamicPaletteViewer;
	}

	public CustomizePaletteViewer getCustomizePaletteViewer() {
		return customizePaletteViewer;
	}

	protected DropTargetListener dropTargetAdapter = new DropTargetListener() {

		public void dragEnter(DropTargetEvent event) {
		}

		public void dragLeave(DropTargetEvent event) {
		}

		public void dragOperationChanged(DropTargetEvent event) {
		}

		public void dragOver(DropTargetEvent event) {
		}

		public void drop(DropTargetEvent event) {
			Dialog customizeDialog;
			customizeDialog = new CustomizePaletteDialog(InvokeType.DragAdd, null, event.data.toString());
			customizeDialog.open();
		}

		public void dropAccept(DropTargetEvent event) {
		}

	};

	/*
	 * override the configurePaletteViewer by using CustomerPaletteContextMenuProvider so that we can add customize popup
	 * 
	 * @see org.eclipse.gef.ui.palette.PaletteViewerProvider#configurePaletteViewer (org.eclipse.gef.ui.palette.PaletteViewer)
	 */
	protected void configurePaletteViewer(final PaletteViewer viewer) {
		// super.configurePaletteViewer(viewer);
		viewer.setContextMenu(new CustomerPaletteContextMenuProvider(viewer));
		viewer.addDragSourceListener(new ToolTransferDragSourceListener(viewer));
		viewer.getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				super.mouseDoubleClick(e);
				List<?> selectedEditParts = viewer.getSelectedEditParts();
				CombinedTemplateCreationEntry selectedEntry = null;
				for (Object object : selectedEditParts) {
					selectedEntry = (CombinedTemplateCreationEntry) ((EditPart) object).getModel();
				}

				EntryImpl entryTemplate = (EntryImpl) selectedEntry.getTemplate();

				VEXEditor currentVEXEditor;
				IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				if (activeEditor instanceof VEXEditor) {
					currentVEXEditor = (VEXEditor) activeEditor;
					currentVEXEditor.defaultCreation(entryTemplate);
				}
			}

		});
	}
}
