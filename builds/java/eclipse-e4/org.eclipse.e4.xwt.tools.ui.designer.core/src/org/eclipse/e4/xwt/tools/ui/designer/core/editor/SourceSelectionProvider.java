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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class SourceSelectionProvider implements ISelectionProvider {

	private Designer designer;
	private StructuredTextEditor textEditor;
	private List<ISelectionChangedListener> listeners;
	private IStructuredSelection fSelection;

	public SourceSelectionProvider(Designer designer,
			StructuredTextEditor textEditor) {
		this.designer = designer;
		this.textEditor = textEditor;
		ISelectionProvider selectionProvider = textEditor
				.getSelectionProvider();
		if (selectionProvider != null) {
			selectionProvider
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							Object selection = event.getSelection();
							if (selection instanceof TextSelection) {
								TextSelection textSelection = (TextSelection) selection;
								if (textSelection.getOffset() == 0) {
									return;
								}
							}
							handleTextSelection(event);
						}
					});
		}
	}

	private List getSelectedNodes() {
		ISelection selection = textEditor.getSelectionProvider().getSelection();
		if (selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).toList();
		}
		return null;
	}

	protected void handleTextSelection(SelectionChangedEvent event) {
		List selectedNodes = getSelectedNodes();
		if (selectedNodes == null || selectedNodes.isEmpty()) {
			return;
		}
		fSelection = null;
		if (selectedNodes != null && !selectedNodes.isEmpty()) {
			List<EditPart> editParts = new ArrayList<EditPart>();
			for (Object object : selectedNodes) {
				EObject model = designer.getModelBuilder().getModel(object);
				if (model == null) {
					continue;
				}
				// EditPart ep = designer.getEditPart(model);
				// if (ep == null) {
					continue;
				// }
				// editParts.add(ep);
			}
			if (!editParts.isEmpty()) {
				fSelection = new StructuredSelection(editParts);
			}
		}
		if (fSelection == null) {
			return;
		}
		final SelectionChangedEvent newEvent = new SelectionChangedEvent(this,
				fSelection);
		for (int i = 0; i < listeners.size(); ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners
					.get(i);
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(newEvent);
				}
			});
		}
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ISelectionChangedListener>();
		}
		listeners.add(listener);
	}

	public ISelection getSelection() {
		return fSelection;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public void setSelection(ISelection selection) {
		// 1. highlight TextEditor.
		StructuredTextViewer textViewer = textEditor.getTextViewer();
		StyledText styledText = textViewer.getTextWidget();
		if (Display.getDefault().getFocusControl() == styledText) {
			return;
		}

		List newSelection = new ArrayList();
		Object[] array = ((IStructuredSelection) selection).toArray();
		for (Object object : array) {
			if (object instanceof EditPart) {
				EditPart editPart = (EditPart) object;
				Object model = editPart.getModel();
				if (model instanceof EObject) {
					EObject node = (EObject) model;
					IDOMNode textNode = designer.getModelBuilder().getTextNode(
							node);
					if (textNode != null) {
						newSelection.add(textNode);
					}
				}
			}
		}
		List selectedNodes = getSelectedNodes();
		if (selectedNodes != null && selectedNodes.containsAll(newSelection)
				&& newSelection.containsAll(selectedNodes)) {
			return;
		}
		String content = styledText.getText();
		int startOffset = -1;
		int endOffset = 0;
		for (Object object : newSelection) {
			IDOMNode textNode = (IDOMNode) object;
			int nodeStartOffset = textNode.getStartOffset();
			int nodeEndOffset = textNode.getEndOffset();
			if (startOffset == -1) {
				startOffset = nodeStartOffset;
				endOffset = nodeEndOffset;
			} else {
				if (nodeStartOffset > startOffset) {
					if (nodeStartOffset < endOffset) {
						continue;
					}
					String segment = content.substring(endOffset,
							nodeStartOffset).trim();
					if (segment.length() == 0) {
						endOffset = nodeEndOffset;
					} else {
						startOffset = 0;
						endOffset = 0;
						break;
					}
				} else {
					if (nodeEndOffset > startOffset) {
						continue;
					}
					String segment = content.substring(nodeEndOffset,
							startOffset).trim();
					if (segment.length() == 0) {
						startOffset = nodeStartOffset;
					} else {
						startOffset = 0;
						endOffset = 0;
						break;
					}

				}
			}
		}
		if (startOffset == -1) {
			startOffset = 0;
		}
		int length = endOffset - startOffset;

		textViewer.setRangeIndication(startOffset, length, false);
		textEditor.selectAndReveal(startOffset, length);
	}

}
