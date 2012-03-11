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
package org.eclipse.e4.xwt.pde;

import java.lang.reflect.Array;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.xwt.ILogger;
import org.eclipse.e4.xwt.IMetaclassFactory;
import org.eclipse.e4.xwt.IStyle;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.input.ICommand;
import org.eclipse.e4.xwt.javabean.metadata.properties.DynamicProperty;
import org.eclipse.e4.xwt.metadata.IMetaclass;
import org.eclipse.e4.xwt.metadata.IProperty;
import org.eclipse.e4.xwt.metadata.ISetPostAction;

public class ExtensionService {
	public static final String METACLASS_REGISTER_ID = PDEPlugin.PLUGIN_ID
			+ "." + "metaclasses";
	public static final String CONVERTER_REGISTER_ID = PDEPlugin.PLUGIN_ID
			+ "." + "converters";
	public static final String LOGGER_REGISTER_ID = PDEPlugin.PLUGIN_ID + "."
			+ "logger";
	public static final String COMMANDS_REGISTER_ID = PDEPlugin.PLUGIN_ID + "."
			+ "commands";
	public static final String METACLASS_FACTORY_ID = PDEPlugin.PLUGIN_ID + "."
			+ "metaclassFactories";
	public static final String STYLES_REGISTER_ID = PDEPlugin.PLUGIN_ID + "."
			+ "styles";

	static final String METACLASS = "metaclass";
	static final String TYPE = "type";
	static final String CLASS = "class";
	static final String PROPERTY = "property";
	static final String EVENT = "event";
	static final String NAME = "name";
	static final String AMOUNT = "amount";
	static final String SINGLE = "single";
	static final String ARRAY = "array";
	static final String SETPOSTACTION = "SetPostAction";
	static final String OVERWRITE = "overwrite";
	static final String COMMAND = "command";
	static final String STYLE = "style";
	static final String TRUE = "true";

	static final String CONVERTERS = "converters";
	static final String CONVERTER = "converter";

	static final String METACLASSFACTORY = "metaclassFactory";

	static final String DYNAMIC_PROPERTY = "DynamicProperty";
	static final String TABLEITEM_PROPERTY = "TableItemProperty";
	static final String DATACONTEXT_PROPERTY = "DataContextProperty";
	static final String EXISTING_PROPERTY = "ExistingProperty";

	static final String TableItem = "org.eclipse.swt.widgets.TableItem";
	static final String Cells = "Cells";

	public static void initialize() {
		IConfigurationElement[] converterConfigurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						CONVERTER_REGISTER_ID);
		for (IConfigurationElement converterConfigurationElement : converterConfigurationElements) {
			if (CONVERTER.equals(converterConfigurationElement.getName())) {
				// register converters here
				try {
					IConverter newInstance = (IConverter) converterConfigurationElement
							.createExecutableExtension(CLASS);
					XWT.registerConvertor(newInstance);
				} catch (Exception e) {
					e.printStackTrace();
					PDEPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, PDEPlugin.PLUGIN_ID,
									"Converter registration error", e));
				}
			}

			IConfigurationElement[] metaclassConfigurationElements = Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							METACLASS_REGISTER_ID);
			for (IConfigurationElement configurationElement : metaclassConfigurationElements) {
				if (METACLASS.equals(configurationElement.getName())) {
					String componentName = configurationElement
							.getAttribute(TYPE);
					try {
						Class<?> newClass = Class.forName(componentName);
						IMetaclass newMetaclass = (IMetaclass) XWT
								.registerMetaclass(newClass);
						IConfigurationElement[] children = configurationElement
								.getChildren();
						if (children != null) {
							for (IConfigurationElement configurationElementChildern : children) {
								if (PROPERTY
										.equals(configurationElementChildern
												.getName())) {
									// property codes here
									String propertyName = configurationElementChildern
											.getAttribute(NAME);
									String className = configurationElementChildern
											.getAttribute(CLASS);
									String typeName = configurationElementChildern
											.getAttribute(TYPE);
									String propertyAmount = configurationElementChildern
											.getAttribute(AMOUNT);
									String overwrite = configurationElementChildern
											.getAttribute(OVERWRITE);

									IProperty property = newMetaclass
											.findProperty(propertyName);
									if (property == null
											|| overwrite.equals(TRUE)) {
										if (className != null) {
											property = (IProperty) configurationElementChildern
													.createExecutableExtension(CLASS);
											newMetaclass.addProperty(property);
										} else if (typeName != null) {
											if (SINGLE.equals(propertyAmount)) {
												property = new DynamicProperty(
														newClass,
														Class.forName(typeName),
														propertyName);

											} else {
												Object newInstance = Array
														.newInstance(
																Class
																		.forName(typeName),
																1);
												property = new DynamicProperty(
														newClass, newInstance
																.getClass(),
														propertyName);
											}
										}
										if (property != null) {
											// if ((className == null) &&
											// (typeName != null) &&
											// ARRAY.equals(propertyAmount)) {
											// newMetaclass.addArrayProperty(property);
											// } else {
											newMetaclass.addProperty(property);
											// }
										}
									}

									// add SetPostAction to property
									IConfigurationElement[] propertyChildren = configurationElementChildern
											.getChildren();
									if (propertyChildren != null) {
										for (IConfigurationElement propertyConfigurationElement : propertyChildren) {
											if (SETPOSTACTION
													.equals(propertyConfigurationElement)) {
												String setPostActionName = propertyConfigurationElement
														.getAttribute(SETPOSTACTION);
												property
														.addSetPostAction((ISetPostAction) Class
																.forName(
																		setPostActionName)
																.newInstance());
											}
										}
									}
								} else if (EVENT
										.equals(configurationElementChildern
												.getName())) {
									// event codes here
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						PDEPlugin.getDefault().getLog().log(
								new Status(IStatus.ERROR, PDEPlugin.PLUGIN_ID,
										"Type registration error", e));
					}
				}
			}
		}

		IExtensionRegistry regs = Platform.getExtensionRegistry();
		if (regs != null) {
			IExtensionPoint ep = regs.getExtensionPoint(LOGGER_REGISTER_ID);
			IExtension[] extensions = ep.getExtensions();
			if (extensions.length > 0) {
				IExtension extension = extensions[0];
				IConfigurationElement ce = extension.getConfigurationElements()[0];
				try {
					Object object = ce.createExecutableExtension("class");
					if (object != null) {
						XWT.setLogger((ILogger) object);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		IConfigurationElement[] commandsConfigurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						COMMANDS_REGISTER_ID);
		for (IConfigurationElement commandConfigurationElement : commandsConfigurationElements) {
			if (COMMAND.equals(commandConfigurationElement.getName())) {
				// register converters here
				String commandName = commandConfigurationElement
						.getAttribute(NAME);
				try {
					ICommand newInstance = (ICommand) commandConfigurationElement
							.createExecutableExtension(CLASS);
					XWT.registerCommand(commandName, newInstance);
				} catch (Exception e) {
					e.printStackTrace();
					PDEPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, PDEPlugin.PLUGIN_ID,
									"Converter registration error", e));
				}
			}
		}

		IConfigurationElement[] stylesConfigurationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						STYLES_REGISTER_ID);
		for (IConfigurationElement styleConfigurationElement : stylesConfigurationElements) {
			if (STYLE.equals(styleConfigurationElement.getName())) {
				try {
					IStyle newInstance = (IStyle) styleConfigurationElement
							.createExecutableExtension(CLASS);
					XWT.addDefaultStyle(newInstance);
				} catch (Exception e) {
					e.printStackTrace();
					PDEPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, PDEPlugin.PLUGIN_ID,
									"Converter registration error", e));
				}
			}
		}
	}

	public static IMetaclassFactory getMetaclassFactory(Class<?> type) {
		IConfigurationElement[] metaclassFactoryElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						METACLASS_FACTORY_ID);
		for (IConfigurationElement metaclassFactoryElement : metaclassFactoryElements) {
			if (METACLASSFACTORY.equals(metaclassFactoryElement.getName())) {
				// get metaclassFactory here
				String metaclassFactoryName = metaclassFactoryElement
						.getAttribute(NAME);
				try {
					IMetaclassFactory newInstance = (IMetaclassFactory) metaclassFactoryElement
							.createExecutableExtension(METACLASS);
					if (newInstance != null) {
						return newInstance;
					}
				} catch (Exception e) {
					e.printStackTrace();
					PDEPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, PDEPlugin.PLUGIN_ID,
									"get metaclassFactory here", e));
				}
			}
		}
		return null;
	}
}