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
package org.eclipse.e4.xwt.tools.ui.designer.editor.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.e4.xwt.IEventConstants;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTMaps;
import org.eclipse.e4.xwt.converters.StringToInteger;
import org.eclipse.e4.xwt.metadata.IEvent;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.SWTStyles;
import org.eclipse.e4.xwt.tools.ui.designer.core.style.StyleGroup;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.StringUtil;
import org.eclipse.e4.xwt.tools.ui.designer.editor.text.assist.XWTSelectionCompletionProposal;
import org.eclipse.e4.xwt.tools.ui.designer.resources.ImageShop;
import org.eclipse.e4.xwt.utils.NamedColorsUtil;
import org.eclipse.e4.xwt.utils.ResourceManager;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
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
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
@SuppressWarnings("restriction")
public class XWTContentAssistProcessor extends XMLContentAssistProcessor {

	static XWTSelectionCompletionProposal[] booleanProposals;
	static XWTSelectionCompletionProposal[] colorsProposals;
	static XWTSelectionCompletionProposal[] stylesProposals;
	static XWTSelectionCompletionProposal[] acceleratorsProposals;

	protected Comparator<ICompletionProposal> comparator = new Comparator<ICompletionProposal>() {

		public int compare(ICompletionProposal o1, ICompletionProposal o2) {
			return o1.getDisplayString().compareTo(o2.getDisplayString());
		}
	};

	static synchronized XWTSelectionCompletionProposal[] getBooleanProposals() {
		if (booleanProposals == null) {
			String[] values = new String[] { "true", "false" };
			Image image = ImageShop
					.get(XMLEditorPluginImages.IMG_OBJ_ATTRIBUTE);
			booleanProposals = new XWTSelectionCompletionProposal[values.length];
			for (int j = 0; j < values.length; j++) {
				String pattern = "\"" + values[j] + "\"";
				booleanProposals[j] = new XWTSelectionCompletionProposal(
						pattern, 0, 0, 1, values[j].length(), image, values[j],
						null, null);
			}
		}
		return booleanProposals;
	}

	static synchronized XWTSelectionCompletionProposal[] getColorsProposals() {
		if (colorsProposals == null) {
			Collection<String> names = XWTMaps.getColorKeys();
			String[] colorNames = NamedColorsUtil.getColorNames();
			colorsProposals = new XWTSelectionCompletionProposal[names.size()
					+ colorNames.length];

			int i = 0;
			for (String colorStr : names) {
				Color color = ResourceManager.resources.getColor(colorStr);
				XWTSelectionCompletionProposal p = createColorProposal(color,
						colorStr);
				if (p != null) {
					colorsProposals[i++] = p;
				}
			}
			for (String colorName : colorNames) {
				Color color = ResourceManager.resources.getColor(colorName);
				XWTSelectionCompletionProposal p = createColorProposal(color,
						colorName);
				if (p != null) {
					colorsProposals[i++] = p;
				}
			}
		}
		return colorsProposals;
	}

	static XWTSelectionCompletionProposal createColorProposal(Color color,
			String colorName) {
		if (color != null) {
			String pattern = "\"" + colorName + "\"";
			Image image = new Image(null, 16, 16);
			GC gc = new GC(image);
			gc.setBackground(color);
			gc.fillRectangle(0, 0, 16, 16);
			gc.dispose();
			return new XWTSelectionCompletionProposal(pattern, 0, 0, 1,
					colorName.length(), image, colorName, null, null);
		}
		return null;

	}

	static synchronized XWTSelectionCompletionProposal[] getStylesProposals(
			Class<?> type, String value) {
		stylesProposals = null;
		Image image = ImageShop.get(ImageShop.IMG_ELEMENT);
		if (value.startsWith("\"")) {
			value = value.replace("\"", "");
		}
		Collection<String> names = getStyleFromWidgetName(type, value);
		stylesProposals = new XWTSelectionCompletionProposal[names.size()];
		int i = 0;
		String replaceString = "";
		for (String string : names) {
			replaceString = getStyleCorrectReplacedPattern(string, value, type);
			String pattern = "\"" + replaceString + "\"";
			stylesProposals[i++] = new XWTSelectionCompletionProposal(pattern,
					0, 0, 1, replaceString.length(), image, string, null, null);
		}
		return stylesProposals;
	}

	private static String getStyleCorrectReplacedPattern(String newStyle,
			String oldStyle, Class<?> type) {
		int masterStyle = 0;
		String shouldDelete = "";
		boolean isInSameGroup = false;
		List<String> oldValues = new ArrayList<String>();
		StringTokenizer stk = new StringTokenizer(oldStyle, "|");
		while (stk.hasMoreTokens()) {
			oldValues.add(stk.nextToken().trim());
		}
		masterStyle = getMasterStyleIntegerFromStyles(oldValues);
		StyleGroup[] styles = SWTStyles.getStyles(type);
		for (StyleGroup styleGroup : styles) {
			if (!styleGroup.match(masterStyle)) {
				continue;
			}
			String[] items = styleGroup.getStyles();
			isInSameGroup = Arrays.asList(items).contains(newStyle);
			if (isInSameGroup) {
				if (!"default".equals(styleGroup.getGroupName())) {
					for (String item : items) {
						for (String oldValue : oldValues) {
							if (equalsIgnoreSWTStyle(oldValue, item)) {
								shouldDelete = oldValue;
							}
						}
					}
				}
				oldValues.remove(shouldDelete);
				oldValues.add(newStyle);
			}
		}
		String newStyleValue = StringUtil.format(
				oldValues.toArray(new String[oldValues.size()]), "|");
		return newStyleValue;
	}

	private static boolean equalsIgnoreSWTStyle(String value, String object) {
		if (value.contains("SWT.")) {
			value = value.replace("SWT.", "");
		}
		if (object.contains("SWT.")) {
			object = object.replace("SWT.", "");
		}
		if (value.equalsIgnoreCase(object)) {
			return true;
		}
		return false;
	}

	private static int getMasterStyleIntegerFromStyles(List<String> oldValues) {
		int masterStyle = 0;
		if (oldValues != null && oldValues.size() != 0) {
			for (String oldValue : oldValues) {
				masterStyle = masterStyle
						| (Integer) StringToInteger.instance.convert(oldValue);
			}
		}
		return masterStyle;
	}

	private static Collection<String> getStyleFromWidgetName(Class<?> type,
			String value) {
		Collection<String> collection = new HashSet<String>();
		collection.clear();
		int masterStyle = 0;
		List<String> oldValues = new ArrayList<String>();
		StringTokenizer stk = new StringTokenizer(value, "|");
		while (stk.hasMoreTokens()) {
			oldValues.add(stk.nextToken().trim());
		}

		masterStyle = getMasterStyleIntegerFromStyles(oldValues);

		StyleGroup[] styles = SWTStyles.getStyles(type);
		for (StyleGroup styleGroup : styles) {
			if (!styleGroup.match(masterStyle)) {
				continue;
			}
			String[] items = styleGroup.getStyles();
			for (String item : items) {
				if (!isContainsTheElement(oldValues, item)) {
					collection.add(item);
				}
			}
		}
		return collection;
	}

	private static boolean isContainsTheElement(List<String> oldValues,
			String style) {
		if (style.contains("SWT.")) {
			style = style.replace("SWT.", "");
		}
		for (String oldValue : oldValues) {
			if (oldValue.contains("SWT.")) {
				oldValue = oldValue.replace("SWT.", "");
			}
			if (oldValue.equalsIgnoreCase(style)) {
				return true;
			}
		}
		return false;
	}

	static synchronized XWTSelectionCompletionProposal[] getAcceleratorsProposals() {
		if (acceleratorsProposals == null) {
			Collection<String> names = XWTMaps.getAcceleratorKeys();
			acceleratorsProposals = new XWTSelectionCompletionProposal[names
					.size()];
			int i = 0;
			for (String string : names) {
				String pattern = "\"" + string + "\"";
				acceleratorsProposals[i++] = new XWTSelectionCompletionProposal(
						pattern, 0, 0, 1, string.length(), null, string, null,
						null);
			}
		}
		return acceleratorsProposals;
	}

	protected void addAttributeNameProposals(
			ContentAssistRequest contentAssistRequest) {
		addXAMLPropertyNameProposals(contentAssistRequest);
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

		IMetaclass metaclass = XWT.getMetaclass(name, node.getNamespaceURI());
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
					Image image = ImageShop
							.get(JavaPluginImages.IMG_FIELD_PUBLIC);
					XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
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
						&& (event.getName().equals(IEventConstants.XWT_LOADED) || event
								.getName().equals(
										IEventConstants.XWT_LOADED_EVENT))) {
					eventName = IEventConstants.XWT_LOADED;
				}

				if (!existing.contains(eventName)) {
					String replacementString = eventName + "=\"perform"
							+ eventName + "\" ";
					Image image = ImageShop.get(ImageShop.IMG_EVENT);
					XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
							replacementString, offset, replacementLength,
							eventName.length() + 2, eventName.length()
									+ "perform".length(), image, eventName,
							null, "Event: " + eventName);
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
					parentNode.getNamespaceURI());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor
	 * #addAttributeValueProposals
	 * (org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest)
	 */
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest) {
		addXAMLPropertyValueProposals(contentAssistRequest);
		super.addAttributeValueProposals(contentAssistRequest);
	}

	private void addXAMLPropertyValueProposals(
			ContentAssistRequest contentAssistRequest) {
		List<ICompletionProposal> proposalCollector = new ArrayList<ICompletionProposal>();
		List<ICompletionProposal> macrosCollector = new ArrayList<ICompletionProposal>();
		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();
		String namespaceURI = node.getNamespaceURI();
		String name = getNodeName(node);
		String value = "";
		IMetaclass metaclass = XWT.getMetaclass(name, namespaceURI);
		StyledText textWidget = fTextViewer.getTextWidget();
		if (metaclass != null) {
			// Find the attribute region and name for which this position should
			// have a value proposed
			IStructuredDocumentRegion open = node
					.getFirstStructuredDocumentRegion();
			ITextRegionList openRegions = open.getRegions();
			int m = openRegions.indexOf(contentAssistRequest.getRegion());
			int n = openRegions.indexOf(contentAssistRequest.getRegion());
			if (m < 0 || n < 0) {
				return;
			}
			ITextRegion nameRegion = null;
			ITextRegion valueRegion = null;
			while (m >= 0) {
				nameRegion = openRegions.get(m--);
				if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
					break;
				}
			}
			while (n >= 0) {
				valueRegion = openRegions.get(n--);
				if (valueRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {
					break;
				}
			}
			if (valueRegion != null) {
				value = open.getText(valueRegion);
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
						XWTSelectionCompletionProposal[] proposals = getAcceleratorsProposals();
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
						XWTSelectionCompletionProposal[] proposals = getStylesProposals(
								metaclass.getType(), value);
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
						XWTSelectionCompletionProposal[] proposals = getBooleanProposals();
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
							Image image = XMLEditorPluginImageHelper
									.getInstance().getImage(
											XMLEditorPluginImages.IMG_OBJ_ENUM);
							XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
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
						XWTSelectionCompletionProposal[] colorsProposals = getColorsProposals();
						for (XWTSelectionCompletionProposal proposal : colorsProposals) {
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
						XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
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

	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		addXAMLElementProposals(contentAssistRequest);
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
				List<XWTSelectionCompletionProposal> proposals = createPropertyNodeProposals(
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
		IMetaclass metaclass = XWT.getMetaclass(name, node.getNamespaceURI());
		String tagName = name + ".Resources";
		if (!addedTags.contains(tagName)
				&& (prefixed == null || tagName.toLowerCase().startsWith(
						prefixed))) {
			addedTags.add(tagName);

			String pattern = "<" + tagName + "></" + tagName + ">";
			Image image = ImageShop.get(ImageShop.IMG_RESOURCES);
			XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
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
			XWTSelectionCompletionProposal layoutProposal = createLayoutProposal(
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
			XWTSelectionCompletionProposal proposal = createLayoutDataProposal(
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
						Image image = ImageShop.get(ImageShop.IMG_ELEMENT);
						XWTSelectionCompletionProposal proposal = new XWTSelectionCompletionProposal(
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

	private List<XWTSelectionCompletionProposal> createPropertyNodeProposals(
			String tagName, int offset, int replacementLength) {
		int index = tagName.indexOf(".");
		if (index == -1) {
			return Collections.emptyList();
		}
		List<XWTSelectionCompletionProposal> proposals = new ArrayList<XWTSelectionCompletionProposal>();
		String property = tagName.substring(index + 1);
		if ("layout".equalsIgnoreCase(property)) {
			String[] layouts = new String[] { "GridLayout", "FillLayout",
					"RowLayout", "StackLayout", "FormLayout" };
			for (int i = 0; i < layouts.length; i++) {
				String pattern = "<" + layouts[i] + "/>";
				Image image = ImageShop.get(ImageShop.IMG_ELEMENT);
				XWTSelectionCompletionProposal p = new XWTSelectionCompletionProposal(
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
				Image image = ImageShop.get(ImageShop.IMG_ELEMENT);
				XWTSelectionCompletionProposal p = new XWTSelectionCompletionProposal(
						pattern, offset, replacementLength,
						layoutDatas[i].length() + 2, 0, image, layoutDatas[i],
						null, "Container LayoutData.");
				proposals.add(p);
			}
		}
		return proposals;
	}

	private XWTSelectionCompletionProposal createLayoutDataProposal(
			IMetaclass metaclass, String layoutData, int offset,
			int replacementLength) {
		if (!Control.class.isAssignableFrom(metaclass.getType())) {
			return null;
		}
		String pattern = "<" + layoutData + "></" + layoutData + ">";
		Image image = JavaPluginImages.get(JavaPluginImages.IMG_FIELD_PUBLIC);
		return new XWTSelectionCompletionProposal(pattern, offset,
				replacementLength, layoutData.length() + 2, 0, image,
				layoutData, null, "Control LayoutData.");
	}

	private XWTSelectionCompletionProposal createLayoutProposal(
			IMetaclass metaclass, String tagName, int offset,
			int replacementLength) {
		if (!Composite.class.isAssignableFrom(metaclass.getType())) {
			return null;
		}
		String pattern = "<" + tagName + "></" + tagName + ">";
		Image image = JavaPluginImages.get(JavaPluginImages.IMG_FIELD_PUBLIC);
		return new XWTSelectionCompletionProposal(pattern, offset,
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

	private List<String> getJavaMethods(JavaProject javaProject,
			String className) {
		if (javaProject == null || className == null) {
			return Collections.emptyList();
		}
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
