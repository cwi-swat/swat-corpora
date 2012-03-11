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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.xwt.IEventConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTMaps;
import org.eclipse.e4.xwt.internal.utils.UserData;
import org.eclipse.e4.xwt.javabean.metadata.Metaclass;
import org.eclipse.e4.xwt.metadata.IEvent;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.ui.utils.ImageManager;
import org.eclipse.e4.xwt.utils.NamedColorsUtil;
import org.eclipse.e4.xwt.utils.ResourceManager;
import org.eclipse.e4.xwt.vex.contentassist.SelectionCompletionProposal;
import org.eclipse.e4.xwt.vex.contentassist.VEXTemplateCompletionProcessor;
import org.eclipse.e4.xwt.vex.dom.DomHelper;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMText;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.contentassist.AbstractContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.eclipse.wst.xml.ui.internal.preferences.XMLUIPreferenceNames;
import org.eclipse.wst.xml.ui.internal.templates.TemplateContextTypeIdsXML;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XWTContentAssistProcessor extends AbstractContentAssistProcessor
		implements IPropertyChangeListener {
	static final String HANDLER_PREFIX = "on";
	static SelectionCompletionProposal[] booleanProposals;
	static SelectionCompletionProposal[] colorsProposals;
	static SelectionCompletionProposal[] stylesProposals;
	static SelectionCompletionProposal[] acceleratorsProposals;

	protected IPreferenceStore fPreferenceStore = null;
	protected IResource fResource = null;
	private VEXTemplateCompletionProcessor fTemplateProcessor = null;
	private List<String> fTemplateContexts = new ArrayList<String>();

	protected Comparator<ICompletionProposal> comparator = new Comparator<ICompletionProposal>() {

		public int compare(ICompletionProposal o1, ICompletionProposal o2) {
			return o1.getDisplayString().compareTo(o2.getDisplayString());
		}
	};

	static SelectionCompletionProposal[] getBooleanProposals() {
		if (booleanProposals == null) {
			String[] values = new String[] { "True", "False" };
			Image image = ImageManager
					.get(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE);
			booleanProposals = new SelectionCompletionProposal[values.length];
			for (int j = 0; j < values.length; j++) {
				String pattern = "\"" + values[j] + "\"";
				booleanProposals[j] = new SelectionCompletionProposal(pattern,
						0, 0, 1, values[j].length(), image, values[j], null,
						null);
			}
		}
		return booleanProposals;
	}

	static SelectionCompletionProposal[] getColorsProposals() {
		if (colorsProposals == null) {
			Collection<String> names = XWTMaps.getColorKeys();
			String[] colorNames = NamedColorsUtil.getColorNames();
			colorsProposals = new SelectionCompletionProposal[names.size()
					+ colorNames.length];

			int i = 0;
			for (String colorStr : names) {
				Color color = ResourceManager.resources.getColor(colorStr);
				SelectionCompletionProposal p = createColorProposal(color,
						colorStr);
				if (p != null) {
					colorsProposals[i++] = p;
				}
			}
			for (String colorName : colorNames) {
				Color color = ResourceManager.resources.getColor(colorName);
				SelectionCompletionProposal p = createColorProposal(color,
						colorName);
				if (p != null) {
					colorsProposals[i++] = p;
				}
			}
		}
		return colorsProposals;
	}

	static SelectionCompletionProposal createColorProposal(Color color,
			String colorName) {
		if (color != null) {
			String pattern = "\"" + colorName + "\"";
			Image image = new Image(null, 16, 16);
			GC gc = new GC(image);
			gc.setBackground(color);
			gc.fillRectangle(0, 0, 16, 16);
			gc.dispose();
			return new SelectionCompletionProposal(pattern, 0, 0, 1,
					colorName.length(), image, colorName, null, null);
		}
		return null;

	}

	static SelectionCompletionProposal[] getStylesProposals() {
		if (stylesProposals == null) {
			Collection<String> names = XWTMaps.getStyleKeys();
			stylesProposals = new SelectionCompletionProposal[names.size()];
			int i = 0;
			for (String string : names) {
				String pattern = "\"" + string + "\"";
				stylesProposals[i++] = new SelectionCompletionProposal(pattern,
						0, 0, 1, string.length(), null, string, null, null);
			}
		}
		return stylesProposals;
	}

	static SelectionCompletionProposal[] getAcceleratorsProposals() {
		if (acceleratorsProposals == null) {
			Collection<String> names = XWTMaps.getAcceleratorKeys();
			acceleratorsProposals = new SelectionCompletionProposal[names
					.size()];
			int i = 0;
			for (String string : names) {
				String pattern = "\"" + string + "\"";
				acceleratorsProposals[i++] = new SelectionCompletionProposal(
						pattern, 0, 0, 1, string.length(), null, string, null,
						null);
			}
		}
		return acceleratorsProposals;
	}

	@Override
	protected void addAttributeNameProposals(
			ContentAssistRequest contentAssistRequest) {
		addXAMLPropertyNameProposals(contentAssistRequest);

		addTemplates(contentAssistRequest, TemplateContextTypeIdsXML.ATTRIBUTE);
		super.addAttributeNameProposals(contentAssistRequest);
	}

	private void addXAMLPropertyNameProposals(
			ContentAssistRequest contentAssistRequest) {
		List<ICompletionProposal> proposalCollector = new ArrayList<ICompletionProposal>();
		List<ICompletionProposal> macrosCollector = new ArrayList<ICompletionProposal>();

		//
		Node node = contentAssistRequest.getNode();
		String name = getNodeName(node);

		HashSet<String> existing = new HashSet<String>();
		NamedNodeMap namedNodeMap = node.getAttributes();
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node attributeNode = namedNodeMap.item(i);
			String attributeName = attributeNode.getNodeName();
			existing.add(attributeName);
		}
		boolean useProposalList = !contentAssistRequest.shouldSeparate();
		int offset = contentAssistRequest.getReplacementBeginPosition();
		int replacementLength = contentAssistRequest.getReplacementLength();
		IDocument document = fTextViewer.getDocument();
		String prefixed = null;
		try {
			prefixed = document.get(offset, replacementLength).toLowerCase();
		} catch (BadLocationException e1) {
		}

		IMetaclass metaclass = XWT.getMetaclass(name,
				DomHelper.lookupNamespaceURI(node));
		if (metaclass != null) {
			IProperty[] properties = metaclass.getProperties();
			for (IProperty property : properties) {
				Class<?> propertyType = property.getType();
				if (propertyType != null
						&& Control.class.isAssignableFrom(propertyType)) {
					continue;
				}

				String propertyName = property.getName();
				if (prefixed != null
						&& !propertyName.toLowerCase().startsWith(prefixed)) {
					continue;
				}

				if (!existing.contains(propertyName)) {
					String defaultValueString = "";
					if (propertyName.equalsIgnoreCase("style")) {
						propertyName = "x:style";
						defaultValueString = "SWT.NONE";
					}
					String replacementString = propertyName + "=\""
							+ defaultValueString + "\" ";
					Image image = JavaPluginImages
							.get(JavaPluginImages.IMG_FIELD_PUBLIC);
					SelectionCompletionProposal proposal = new SelectionCompletionProposal(
							replacementString, offset, replacementLength,
							propertyName.length() + 2,
							defaultValueString.length(), image, propertyName,
							null, "Property: " + propertyName);
					if (useProposalList) {
						proposalCollector.add(proposal);
					} else {
						macrosCollector.add(proposal);
					}
				}
			}

			IEvent[] events = metaclass.getEvents();
			for (IEvent event : events) {
				String eventName = event.getName();
				if (prefixed != null
						&& !eventName.toLowerCase().startsWith(prefixed)) {
					continue;
				}

				eventName = Character.toUpperCase(eventName.charAt(0))
						+ eventName.substring(1) + IEventConstants.SUFFIX;
				if (event.getName() != null
						&& (event.getName().equals(IEventConstants.XWT_LOADED) || 
								event.getName().equals(IEventConstants.XWT_LOADED_EVENT))) {
					eventName = IEventConstants.XWT_LOADED_EVENT;
				}

				if (!existing.contains(eventName)) {
					String replacementString = eventName + "=\""
							+ HANDLER_PREFIX + eventName + "\" ";
					Image image = ImageManager.get(ImageManager.IMG_EVENT);
					SelectionCompletionProposal proposal = new SelectionCompletionProposal(
							replacementString, offset, replacementLength,
							eventName.length() + 2, eventName.length()
									+ HANDLER_PREFIX.length(), image,
							eventName, null, "Event: " + eventName);
					if (useProposalList) {
						proposalCollector.add(proposal);
					} else {
						macrosCollector.add(proposal);
					}
				}
			}
		}

		Node parentNode = node.getParentNode();
		String parentName = getNodeName(parentNode);
		try {
			IMetaclass parentMetaclass = XWT.getMetaclass(parentName,
					DomHelper.lookupNamespaceURI(parentNode));
			if (parentMetaclass != null) {
				// Attached property
			}
		} catch (Exception e) {
		}

		Collections.sort(proposalCollector, comparator);
		Collections.sort(macrosCollector, comparator);

		for (ICompletionProposal proposal : proposalCollector) {
			contentAssistRequest.addProposal(proposal);
		}

		for (ICompletionProposal proposal : macrosCollector) {
			contentAssistRequest.addMacro(proposal);
		}
	}

	@Override
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest) {
		addXAMLPropertyValueProposals(contentAssistRequest);

		addTemplates(contentAssistRequest,
				TemplateContextTypeIdsXML.ATTRIBUTE_VALUE);
		super.addAttributeValueProposals(contentAssistRequest);
	}

	private void addXAMLPropertyValueProposals(
			ContentAssistRequest contentAssistRequest) {
		List<ICompletionProposal> proposalCollector = new ArrayList<ICompletionProposal>();
		List<ICompletionProposal> macrosCollector = new ArrayList<ICompletionProposal>();

		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();
		String name = getNodeName(node);
		IMetaclass metaclass = XWT.getMetaclass(name,
				DomHelper.lookupNamespaceURI(node));
		StyledText textWidget = fTextViewer.getTextWidget();
		if (metaclass != null) {
			// Find the attribute region and name for which this position should
			// have a value proposed
			IStructuredDocumentRegion open = node
					.getFirstStructuredDocumentRegion();
			ITextRegionList openRegions = open.getRegions();
			int m = openRegions.indexOf(contentAssistRequest.getRegion());
			if (m < 0) {
				return;
			}
			ITextRegion nameRegion = null;
			while (m >= 0) {
				nameRegion = openRegions.get(m--);
				if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
					break;
				}
			}

			// the name region is REQUIRED to do anything useful
			if (nameRegion != null) {
				// Retrieve the declaration
				CMElementDeclaration elementDecl = getCMElementDeclaration(node);

				// String attributeName = nameRegion.getText();
				String attributeName = open.getText(nameRegion);
				IProperty property = metaclass.findProperty(attributeName);
				if (attributeName.equalsIgnoreCase("x:style")
						|| property != null) {
					int offset = contentAssistRequest
							.getReplacementBeginPosition();
					int replacementLength = contentAssistRequest
							.getReplacementLength();
					boolean useProposalList = !contentAssistRequest
							.shouldSeparate();

					String prefixed = null;
					String prefixedQuote = "";
					boolean fullValue = true;
					try {
						int caretIndex = textWidget.getCaretOffset();
						IDocument document = fTextViewer.getDocument();
						prefixed = document.get(offset, caretIndex - offset)
								.toLowerCase();
						if (prefixed.equals("\"\"")) {
							prefixed = null;
						}
					} catch (BadLocationException e1) {
					}
					if (prefixed != null) {
						prefixedQuote = prefixed + "\"";
						fullValue = false;
					}

					// filter accelerators of menu element.
					if (attributeName.equalsIgnoreCase("accelerator")) {
						SelectionCompletionProposal[] proposals = getAcceleratorsProposals();
						for (int j = 0; j < proposals.length; j++) {
							String pattern = proposals[j]
									.getReplacementString();
							if (prefixed != null
									&& ((!pattern.toLowerCase().startsWith(
											prefixed) && !fullValue) || prefixedQuote
											.equalsIgnoreCase(pattern))) {
								continue;
							}

							proposals[j].setReplacementOffset(offset);
							proposals[j]
									.setReplacementLength(replacementLength);
							if (useProposalList) {
								proposalCollector.add(proposals[j]);
							} else {
								macrosCollector.add(proposals[j]);
							}
						}
						Collections.sort(proposalCollector, comparator);
						Collections.sort(macrosCollector, comparator);

						for (ICompletionProposal proposal : proposalCollector) {
							contentAssistRequest.addProposal(proposal);
						}

						for (ICompletionProposal proposal : macrosCollector) {
							contentAssistRequest.addMacro(proposal);
						}
						return;
					}

					// styles TODO: filter styles of each element.
					if (attributeName.equalsIgnoreCase("x:style")) {
						SelectionCompletionProposal[] proposals = getStylesProposals();
						for (int j = 0; j < proposals.length; j++) {
							String pattern = proposals[j]
									.getReplacementString();
							if (prefixed != null
									&& ((!pattern.toLowerCase().startsWith(
											prefixed) && !fullValue) || prefixedQuote
											.equalsIgnoreCase(pattern))) {
								continue;
							}

							proposals[j].setReplacementOffset(offset);
							proposals[j]
									.setReplacementLength(replacementLength);
							if (useProposalList) {
								proposalCollector.add(proposals[j]);
							} else {
								macrosCollector.add(proposals[j]);
							}
						}
						Collections.sort(proposalCollector, comparator);
						Collections.sort(macrosCollector, comparator);

						for (ICompletionProposal proposal : proposalCollector) {
							contentAssistRequest.addProposal(proposal);
						}

						for (ICompletionProposal proposal : macrosCollector) {
							contentAssistRequest.addMacro(proposal);
						}
						return;
					}
					Class<?> javaType = property.getType();
					if (javaType == Boolean.class || javaType == boolean.class) {
						SelectionCompletionProposal[] proposals = getBooleanProposals();
						for (int j = 0; j < proposals.length; j++) {
							String pattern = proposals[j]
									.getReplacementString();
							if (prefixed != null
									&& ((!pattern.toLowerCase().startsWith(
											prefixed) && !fullValue) || prefixedQuote
											.equalsIgnoreCase(pattern))) {
								continue;
							}

							proposals[j].setReplacementOffset(offset);
							proposals[j]
									.setReplacementLength(replacementLength);
							if (useProposalList) {
								proposalCollector.add(proposals[j]);
							} else {
								macrosCollector.add(proposals[j]);
							}
						}
					} else if (javaType != null && javaType.isEnum()) {
						Object[] objects = javaType.getEnumConstants();

						IConverter converter = XWT.findConvertor(javaType,
								String.class);
						for (int j = 0; j < objects.length; j++) {
							String valueString = "";
							if (converter != null) {
								Object stringValue = converter
										.convert(objects[j]);
								if (stringValue != null) {
									valueString = stringValue.toString();
								}
							} else {
								valueString = objects[j].toString();
							}
							String pattern = "\"" + valueString + "\"";
							if (prefixed != null
									&& ((!pattern.toLowerCase().startsWith(
											prefixed) && !fullValue) || prefixedQuote
											.equalsIgnoreCase(pattern))) {
								continue;
							}
							Image image = ImageManager
									.get(XMLEditorPluginImages.IMG_OBJ_ENUM);
							SelectionCompletionProposal proposal = new SelectionCompletionProposal(
									pattern, offset, replacementLength, 1,
									valueString.length(), image, valueString,
									null, null);
							if (useProposalList) {
								proposalCollector.add(proposal);
							} else {
								macrosCollector.add(proposal);
							}
						}
					} else if (javaType.isAssignableFrom(Color.class)) {
						SelectionCompletionProposal[] colorsProposals = getColorsProposals();
						for (SelectionCompletionProposal proposal : colorsProposals) {
							String pattern = proposal.getReplacementString();
							if (prefixed != null
									&& ((!pattern.toLowerCase().startsWith(
											prefixed) && !fullValue) || prefixedQuote
											.equalsIgnoreCase(pattern))) {
								continue;
							}

							proposal.setReplacementOffset(offset);
							proposal.setReplacementLength(replacementLength);
							if (useProposalList) {
								proposalCollector.add(proposal);
							} else {
								macrosCollector.add(proposal);
							}
						}
					}
				} else {
					IEvent[] allEvents = metaclass.getEvents();
					JavaProject javaProject = (JavaProject) textWidget
							.getData("javaProject");
					String className = (String) textWidget.getData("className");
					List<String> javaMethods = getJavaMethods(javaProject,
							className);
					int offset = contentAssistRequest
							.getReplacementBeginPosition();
					int caretIndex = textWidget.getCaretOffset();
					IDocument document = fTextViewer.getDocument();
					String prefixed = null;
					try {
						prefixed = document.get(offset + 1, caretIndex - offset
								- 1);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					int replacementLength = contentAssistRequest
							.getReplacementLength();
					boolean useProposalList = !contentAssistRequest
							.shouldSeparate();
					for (Iterator<String> iterator = javaMethods.iterator(); iterator
							.hasNext();) {
						String valueString = iterator.next();
						if (valueString.equals(prefixed))
							continue;
						String pattern = "\"" + valueString + "\"";
						SelectionCompletionProposal proposal = new SelectionCompletionProposal(
								pattern, offset, replacementLength, 1,
								valueString.length(), null, valueString, null,
								null);
						if (useProposalList) {
							proposalCollector.add(proposal);
						} else {
							macrosCollector.add(proposal);
						}
					}
				}
			}
		}

		Collections.sort(proposalCollector, comparator);
		Collections.sort(macrosCollector, comparator);

		for (ICompletionProposal proposal : proposalCollector) {
			contentAssistRequest.addProposal(proposal);
		}

		for (ICompletionProposal proposal : macrosCollector) {
			contentAssistRequest.addMacro(proposal);
		}
	}

	@Override
	protected void addEmptyDocumentProposals(
			ContentAssistRequest contentAssistRequest) {
		addTemplates(contentAssistRequest, TemplateContextTypeIdsXML.NEW);
		super.addEmptyDocumentProposals(contentAssistRequest);
	}

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		addXAMLElementProposals(contentAssistRequest);
		addTemplates(contentAssistRequest, TemplateContextTypeIdsXML.TAG);
		super.addTagInsertionProposals(contentAssistRequest, childPosition);
	}

	private void addXAMLElementProposals(
			ContentAssistRequest contentAssistRequest) {
		List<ICompletionProposal> proposalCollector = new ArrayList<ICompletionProposal>();
		List<ICompletionProposal> macrosCollector = new ArrayList<ICompletionProposal>();

		List<String> addedTags = new ArrayList<String>();

		boolean useProposalList = !contentAssistRequest.shouldSeparate();
		int offset = contentAssistRequest.getReplacementBeginPosition();
		int replacementLength = contentAssistRequest.getReplacementLength();
		String prefixed = null;
		try {
			IDocument document = fTextViewer.getDocument();
			prefixed = document.get(offset, replacementLength).toLowerCase();
		} catch (BadLocationException e1) {
		}

		Node node = contentAssistRequest.getNode();
		if (node == null) {
			return;
		}
		if (node instanceof IDOMText) {
			IDOMText text = (IDOMText) node;
			offset = text.getStartOffset();
			replacementLength = text.getLength();
			try {
				IDocument document = fTextViewer.getDocument();
				prefixed = document.get(offset, replacementLength).trim()
						.toLowerCase();
			} catch (BadLocationException e1) {
			}
		} else if (node instanceof IDOMNode) {
			IDOMNode domNode = (IDOMNode) node;
			NodeList children = domNode.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof IDOMText) {
					IDOMText text = (IDOMText) child;
					offset = text.getStartOffset();
					replacementLength = text.getLength();
					try {
						IDocument document = fTextViewer.getDocument();
						prefixed = document.get(offset, replacementLength)
								.trim().toLowerCase();
					} catch (BadLocationException e1) {
					}
					break;
				}
			}
		}

		while (node.getNodeType() != Node.ELEMENT_NODE) {
			node = node.getParentNode();
			if (node == null) {
				return;
			}
		}

		boolean containControls = false;
		String name = getNodeName(node);
		if (name.indexOf(".") != -1) {
			if (name.toLowerCase().endsWith(".control")) {
				name = "Control";// Add controls to TabItem, CoolItem...
				containControls = true;
			} else {
				List<SelectionCompletionProposal> proposals = createPropertyNodeProposals(
						name, offset, replacementLength);
				if (useProposalList) {
					proposalCollector.addAll(proposals);
				} else {
					macrosCollector.addAll(proposals);
				}
				Collections.sort(proposalCollector, comparator);
				Collections.sort(macrosCollector, comparator);

				for (ICompletionProposal proposal : proposalCollector) {
					contentAssistRequest.addProposal(proposal);
				}

				for (ICompletionProposal proposal : macrosCollector) {
					contentAssistRequest.addMacro(proposal);
				}
				return;
			}
		}
		IMetaclass metaclass = XWT.getMetaclass(name,
				DomHelper.lookupNamespaceURI(node));
		String tagName = name + ".Resources";
		if (!addedTags.contains(tagName)
				&& (prefixed == null || tagName.toLowerCase().startsWith(
						prefixed))) {
			addedTags.add(tagName);

			String pattern = "<" + tagName + "></" + tagName + ">";
			Image image = ImageManager.get(ImageManager.IMG_RESOURCES);
			SelectionCompletionProposal proposal = new SelectionCompletionProposal(
					pattern, offset, replacementLength, tagName.length() + 2,
					0, image, tagName, null, "Element resources");
			if (useProposalList) {
				proposalCollector.add(proposal);
			} else {
				macrosCollector.add(proposal);
			}
		}

		// layout
		String layout = name + ".layout";
		if (!addedTags.contains(layout)
				&& (prefixed == null || layout.startsWith(prefixed))) {
			SelectionCompletionProposal layoutProposal = createLayoutProposal(
					metaclass, layout, offset, replacementLength);
			if (layoutProposal != null) {
				if (useProposalList) {
					proposalCollector.add(layoutProposal);
				} else {
					macrosCollector.add(layoutProposal);
				}
			}
		}
		// layoutData
		String layoutData = name + ".layoutData";
		if (!addedTags.contains(layout)
				&& (prefixed == null || layout.startsWith(prefixed))) {
			SelectionCompletionProposal proposal = createLayoutDataProposal(
					metaclass, layoutData, offset, replacementLength);
			if (proposal != null) {
				if (useProposalList) {
					proposalCollector.add(proposal);
				} else {
					macrosCollector.add(proposal);
				}
			}
		}
		if (metaclass != null) {
			if (containControls
					|| Composite.class.isAssignableFrom(metaclass.getType())) {
				IMetaclass[] metaclasses = XWT.getAllMetaclasses();
				for (IMetaclass type : metaclasses) {
					if (Control.class.isAssignableFrom(type.getType())
							&& !type.isAbstract()) {

						String typeName = type.getName();
						if (prefixed != null
								&& !typeName.toLowerCase().startsWith(prefixed)) {
							continue;
						}
						if (addedTags.contains(typeName)) {
							continue;
						} else {
							addedTags.add(typeName);
						}
						String pattern = "<" + typeName + "></" + typeName
								+ ">";
						Image image = ImageManager
								.get(ImageManager.IMG_ELEMENT);
						SelectionCompletionProposal proposal = new SelectionCompletionProposal(
								pattern, offset, replacementLength,
								typeName.length() + 2, 0, image, typeName,
								null, null);
						if (useProposalList) {
							proposalCollector.add(proposal);
						} else {
							macrosCollector.add(proposal);
						}
					}
				}
			}
		}

		Collections.sort(proposalCollector, comparator);
		Collections.sort(macrosCollector, comparator);

		for (ICompletionProposal proposal : proposalCollector) {
			contentAssistRequest.addProposal(proposal);
		}

		for (ICompletionProposal proposal : macrosCollector) {
			contentAssistRequest.addMacro(proposal);
		}
	}

	private List<SelectionCompletionProposal> createPropertyNodeProposals(
			String tagName, int offset, int replacementLength) {
		int index = tagName.indexOf(".");
		if (index == -1) {
			return Collections.emptyList();
		}
		List<SelectionCompletionProposal> proposals = new ArrayList<SelectionCompletionProposal>();
		String property = tagName.substring(index + 1);
		if ("layout".equalsIgnoreCase(property)) {
			String[] layouts = new String[] { "GridLayout", "FillLayout",
					"RowLayout", "StackLayout", "FormLayout" };
			for (int i = 0; i < layouts.length; i++) {
				String pattern = "<" + layouts[i] + "/>";
				Image image = ImageManager.get(ImageManager.IMG_ELEMENT);
				SelectionCompletionProposal p = new SelectionCompletionProposal(
						pattern, offset, replacementLength,
						layouts[i].length() + 2, 0, image, layouts[i], null,
						"Container Layout.");
				proposals.add(p);
			}
		} else if ("layoutData".equalsIgnoreCase(property)) {
			String[] layoutDatas = new String[] { "GridData", "StackData",
					"FormData", "RowData" };
			for (int i = 0; i < layoutDatas.length; i++) {
				String pattern = "<" + layoutDatas[i] + "></" + layoutDatas[i]
						+ ">";
				Image image = ImageManager.get(ImageManager.IMG_ELEMENT);
				SelectionCompletionProposal p = new SelectionCompletionProposal(
						pattern, offset, replacementLength,
						layoutDatas[i].length() + 2, 0, image, layoutDatas[i],
						null, "Container LayoutData.");
				proposals.add(p);
			}
		}
		return proposals;
	}

	private SelectionCompletionProposal createLayoutDataProposal(
			IMetaclass metaclass, String layoutData, int offset,
			int replacementLength) {
		if (!Control.class.isAssignableFrom(metaclass.getType())) {
			return null;
		}
		String pattern = "<" + layoutData + "></" + layoutData + ">";
		Image image = JavaPluginImages.get(JavaPluginImages.IMG_FIELD_PUBLIC);
		return new SelectionCompletionProposal(pattern, offset,
				replacementLength, layoutData.length() + 2, 0, image,
				layoutData, null, "Control LayoutData.");
	}

	private SelectionCompletionProposal createLayoutProposal(
			IMetaclass metaclass, String tagName, int offset,
			int replacementLength) {
		if (!Composite.class.isAssignableFrom(metaclass.getType())) {
			return null;
		}
		String pattern = "<" + tagName + "></" + tagName + ">";
		Image image = JavaPluginImages.get(JavaPluginImages.IMG_FIELD_PUBLIC);
		return new SelectionCompletionProposal(pattern, offset,
				replacementLength, tagName.length() + 2, 0, image, tagName,
				null, "Container Layout.");
	}

	private String getNodeName(Node node) {
		String name = node.getNodeName();
		int index = name.indexOf(":");
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}

	/**
	 * Adds templates to the list of proposals
	 * 
	 * @param contentAssistRequest
	 * @param context
	 */
	private void addTemplates(ContentAssistRequest contentAssistRequest,
			String context) {
		addTemplates(contentAssistRequest, context,
				contentAssistRequest.getReplacementBeginPosition());
	}

	/**
	 * Adds templates to the list of proposals
	 * 
	 * @param contentAssistRequest
	 * @param context
	 * @param startOffset
	 */
	private void addTemplates(ContentAssistRequest contentAssistRequest,
			String context, int startOffset) {
		if (contentAssistRequest == null) {
			return;
		}

		// if already adding template proposals for a certain context type, do
		// not add again
		if (!fTemplateContexts.contains(context)) {
			fTemplateContexts.add(context);
			boolean useProposalList = !contentAssistRequest.shouldSeparate();

			if (getTemplateCompletionProcessor() != null) {
				getTemplateCompletionProcessor().setContextType(context);
				ICompletionProposal[] proposals = getTemplateCompletionProcessor()
						.computeCompletionProposals(fTextViewer, startOffset);
				for (int i = 0; i < proposals.length; ++i) {
					if (useProposalList) {
						contentAssistRequest.addProposal(proposals[i]);
					} else {
						contentAssistRequest.addMacro(proposals[i]);
					}
				}
			}
		}
	}

	@Override
	protected ContentAssistRequest computeCompletionProposals(
			int documentPosition, String matchString,
			ITextRegion completionRegion, IDOMNode treeNode, IDOMNode xmlnode) {
		ContentAssistRequest request = super.computeCompletionProposals(
				documentPosition, matchString, completionRegion, treeNode,
				xmlnode);
		// bug115927 use original document position for all/any region
		// templates
		addTemplates(request, TemplateContextTypeIdsXML.ALL, documentPosition);
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.xml.ui.contentassist.AbstractContentAssistProcessor#
	 * computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	@Override
	public ICompletionProposal[] computeCompletionProposals(
			ITextViewer textViewer, int documentPosition) {
		fTemplateContexts.clear();
		return super.computeCompletionProposals(textViewer, documentPosition);
	}

	protected IPreferenceStore getPreferenceStore() {
		if (fPreferenceStore == null) {
			fPreferenceStore = XMLUIPlugin.getDefault().getPreferenceStore();
		}
		return fPreferenceStore;
	}

	protected VEXTemplateCompletionProcessor getTemplateCompletionProcessor() {
		if (fTemplateProcessor == null) {
			fTemplateProcessor = new VEXTemplateCompletionProcessor();
		}
		return fTemplateProcessor;
	}

	@Override
	protected void init() {
		getPreferenceStore().addPropertyChangeListener(this);
		reinit();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();

		if ((property.compareTo(XMLUIPreferenceNames.AUTO_PROPOSE) == 0)
				|| (property.compareTo(XMLUIPreferenceNames.AUTO_PROPOSE_CODE) == 0)) {
			reinit();
		}
	}

	protected void reinit() {
		String key = XMLUIPreferenceNames.AUTO_PROPOSE;
		boolean doAuto = getPreferenceStore().getBoolean(key);
		if (doAuto) {
			key = XMLUIPreferenceNames.AUTO_PROPOSE_CODE;
			completionProposalAutoActivationCharacters = getPreferenceStore()
					.getString(key).toCharArray();
		} else {
			completionProposalAutoActivationCharacters = null;
		}
	}

	@Override
	public void release() {
		super.release();
		getPreferenceStore().removePropertyChangeListener(this);
	}

	private List<String> getJavaMethods(JavaProject javaProject,
			String className) {
		List<String> javaMethods = new ArrayList<String>();
		try {
			IType type = javaProject.findType(className);
			IMethod[] methods = type.getMethods();
			for (int i = 0; i < methods.length; i++) {
				IMethod method = methods[i];
				String methodName = method.getElementName();
				javaMethods.add(methodName);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return javaMethods;
	}
}