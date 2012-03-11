/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.deeplink;

import static org.eclipse.e4.core.functionalprog.optionmonad.Nulls.valueOrSubstitute;
import static org.eclipse.e4.core.functionalprog.optionmonad.Nulls.valueOrThrow;

import static org.apache.commons.lang.StringEscapeUtils.unescapeXml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.e4.core.deeplink.internal.NullNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A DeepLinkResult (a) parses a result XML stream (that comes from invoking a
 * deep link) into its components and provides public fields for accessing these
 * results.
 */
public class DeepLinkResult {
	private static final String RESULT_ELEMENT_NAME = "result";
	private static final String CALLBACK_RAN_ELEMENT_NAME = "callbackRan";
	private static final String LOADED_ELEMENT_NAME = "loaded";
	private static final String EXCEPTION_ELEMENT_NAME = "exception";
	
	private static final String OUTPUTDATA_ELEMENT_NAME = "outputData";
	private static final String OUTPUTDATA_RESULT_KEY = "key";
	
	/**
	 * Some deep links may activate some an of the RCP framework and may or
	 * may not also map to any explicit callback method.
	 * <p>
	 * If the specified RCP element (such as a perspective, Command) could be 
	 * loaded/focused/executed, etc., did this happen?  This value is True if
	 * the deep link refers to an RCP element and that RCP element was loaded/
	 * focused/executed, etc.  This value is False if no RCP element was loaded/
	 * focused/executed, etc.  This can happen either because the element
	 * could not be found or because this particular type of deep link 
	 * does not map to any RCP element. 
	 * <p>
	 * If the specified deep link TYPE could not be found, this value will
	 * be null.  For example, if someone links to 
	 * <ul>
	 * <li>deeplink://app/undefinedType/anyID/anyAction
	 * </ul>
	 * even though the syntax of the deep link is valid, this value will be 
	 * null because undefinedType does not refer to any registered deep link 
	 * type handler.
	 */
	public final Boolean loaded;
	
	/**
	 * Did a callback run?  True if a callback associated with the deep link
	 * ran; false otherwise.	 
	 * <p>
	 * If the specified deep link TYPE could not be found, this value will
	 * be null.  For example, if someone links to 
	 * <ul>
	 * <li>deeplink://app/undefinedType/anyID/anyAction
	 * </ul>
	 * even though the syntax of the deep link is valid, this value will be 
	 * null because undefinedType does not refer to any registered deep link 
	 * type handler.
	 */
	public final Boolean callbackRan;
	
	/**
	 * If an exception was thrown during callback execution, this value will
	 * be the exception's error message string.  It will be null otherwise.
	 */
	public final String exception;
	
	/**
	 * If a callback is defined, it can return a Map<String, String> as a
	 * result.  If this done, that Map is automatically unmarshalled and put here.
	 * If no callback exists or the callback returned no results, this Map
	 * will simply be empty.
	 */
	public final Map<String, String> outputData = new HashMap<String, String>();

	/**
	 * (non-api) Construct a DeepLinkResult.  This constructor is only called
	 * internally to the framework.
	 * 
	 * @param resultStream
	 * @throws DeepLinkResultException
	 */
	public DeepLinkResult(InputStream resultStream) throws DeepLinkResultException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		Document doc = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(resultStream);
		} catch (Exception e) {
			throw new DeepLinkResultException("Failure reading deep link result XML", e);
		}
		
		doc.getDocumentElement().normalize();
		NodeList results = valueOrThrow(doc.getElementsByTagName(RESULT_ELEMENT_NAME), resultFailure("No deep link result XML element"));

		if (results.getLength() != 1) {
			throw resultFailure("Incorrect number of result values: " + results.getLength());
		}
		
		Node resultNode = valueOrThrow(results.item(0), resultFailure("index out of bounds"));
		NamedNodeMap attributes = valueOrThrow(resultNode.getAttributes(), resultFailure("No attributes on <result> tag?"));
		String loaded = valueOrSubstitute(attributes.getNamedItem(LOADED_ELEMENT_NAME), new NullNode()).getNodeValue();
		String callbackRan = valueOrSubstitute(attributes.getNamedItem(CALLBACK_RAN_ELEMENT_NAME), new NullNode()).getNodeValue();
		String exception = valueOrSubstitute(attributes.getNamedItem(EXCEPTION_ELEMENT_NAME), new NullNode()).getNodeValue();

		this.loaded = NullNode.UNDEFINED_VALUE.equals(loaded) ? null : Boolean.parseBoolean(loaded);
		this.callbackRan = NullNode.UNDEFINED_VALUE.equals(callbackRan) ? null : Boolean.parseBoolean(callbackRan);
		this.exception = NullNode.UNDEFINED_VALUE.equals(exception) ? null : exception;
		
		NodeList outputDataXML = doc.getElementsByTagName(OUTPUTDATA_ELEMENT_NAME);
		Node outputNode = outputDataXML.item(0);
		if (outputNode == null) {
			return;
		}
		NodeList childNodes = outputNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node outputItem = childNodes.item(i);
			if ("#text".equals(outputItem.getNodeName())) {
				continue;
			}
			attributes = valueOrThrow(outputItem.getAttributes(), resultFailure("No attributes on <outputData> tag?"));
			String key = valueOrThrow(attributes.getNamedItem(OUTPUTDATA_RESULT_KEY), resultFailure("No key in output <element...> tag?")).getNodeValue();
			key = unescapeXml(key);
			String value = outputItem.getTextContent();
			outputData.put(key, value);
		}
	}

	private DeepLinkResultException resultFailure(String message) {
		return new DeepLinkResultException(message);
	}
	
}
