/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.ui.internal.workbench.swt;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;
import org.eclipse.e4.ui.model.application.descriptor.basic.impl.BasicPackageImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.AdvancedPackageImpl;
import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuPackageImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class GenTopic implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		try {
			processHeader();
			processLiterals(CommandsPackageImpl.Literals.class);
			processLiterals(BasicPackageImpl.Literals.class);
			processLiterals(ApplicationPackageImpl.Literals.class);
			processLiterals(AdvancedPackageImpl.Literals.class);
			processLiterals(org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl.Literals.class);
			processLiterals(UiPackageImpl.Literals.class);
			processLiterals(MenuPackageImpl.Literals.class);
			processFooter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IApplication.EXIT_OK;
	}

	private void processLiterals(Class<?> literals)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = literals.getFields();
		Map<String, EClass> classes = new TreeMap<String, EClass>();
		for (int i = 0; i < fields.length; i++) {
			Object value = fields[i].get(null);
			if (value instanceof EClass) {
				classes.put(((EClass) value).getName(), (EClass) value);
			}
		}
		for (EClass ec : classes.values()) {
			processEClass(ec);
		}
	}

	/**
	 * 
	 */
	private void processFooter() {
		System.out
				.println("\n\tpublic static String buildTopic(String topic) {" //$NON-NLS-1$
						+ "\n\t\treturn topic + TOPIC_SEP + ALL_SUB_TOPICS;" //$NON-NLS-1$
						+ "\n\t}" //$NON-NLS-1$
						+ "\n\tpublic static String buildTopic(String topic, String attrName) {" //$NON-NLS-1$
						+ "\n\t\treturn topic + TOPIC_SEP + attrName + TOPIC_SEP + ALL_SUB_TOPICS;" //$NON-NLS-1$
						+ "\n\t}" //$NON-NLS-1$
						+ "\n\tpublic static String buildTopic(String topic, String attrName, String eventType) {" //$NON-NLS-1$
						+ "\n\t\treturn topic + TOPIC_SEP + attrName + TOPIC_SEP + eventType;" //$NON-NLS-1$
						+ "\n\t}" + "\n}"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * 
	 */
	private void processHeader() {
		System.out
				.print("/*******************************************************************************\n * Copyright (c) 2009,2010 IBM Corporation and others.\n * All rights reserved. This program and the accompanying materials\n * are made available under the terms of the Eclipse Public License v1.0\n * which accompanies this distribution, and is available at\n * http://www.eclipse.org/legal/epl-v10.html\n *\n * Contributors:\n *     IBM Corporation - initial API and implementation\n ******************************************************************************/" //$NON-NLS-1$
						+ "\npackage org.eclipse.e4.ui.workbench;\n\npublic class UIEvents {" //$NON-NLS-1$
						+ "\n\tpublic static final String TOPIC_SEP = \"/\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\tpublic static final String ALL_SUB_TOPICS = \"*\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\tpublic static final String UITopicBase = \"org/eclipse/e4/ui/model\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\tpublic static interface EventTypes {" //$NON-NLS-1$
						+ "\n\t\tpublic static final String CREATE = \"CREATE\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String SET = \"SET\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String ADD = \"ADD\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String REMOVE = \"REMOVE\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t}" //$NON-NLS-1$
						+ "\n\tpublic static interface EventTags {" //$NON-NLS-1$
						+ "\n\t\tpublic static final String ELEMENT = \"ChangedElement\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String WIDGET = \"Widget\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String TYPE = \"EventType\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String ATTNAME = \"AttName\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String OLD_VALUE = \"OldValue\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t\tpublic static final String NEW_VALUE = \"NewValue\"; //$NON-NLS-1$" //$NON-NLS-1$
						+ "\n\t}"); //$NON-NLS-1$
	}

	private void processEClass(EClass eClass) {
		EList<EStructuralFeature> features = eClass.getEStructuralFeatures();
		if (features.isEmpty()) {
			return;
		}
		String pkgName = eClass.getEPackage().getName();
		String className = eClass.getName();
		System.out.print("\n\tpublic static interface " + className + " {" //$NON-NLS-1$ //$NON-NLS-2$
				+ "\n\t\tpublic static final String TOPIC = UITopicBase + \"/" //$NON-NLS-1$
				+ pkgName + '/' + className + "\"; //$NON-NLS-1$"); //$NON-NLS-1$
		Set<String> names = new TreeSet<String>();
		for (EStructuralFeature feature : features) {
			names.add(feature.getName());
		}
		for (String name : names) {
			System.out.print("\n\t\tpublic static final String " //$NON-NLS-1$
					+ name.toUpperCase() + " = \""); //$NON-NLS-1$
			System.out.print(name + "\"; //$NON-NLS-1$"); //$NON-NLS-1$
		}
		System.out.print("\n\t}"); //$NON-NLS-1$
	}

	public void stop() {
		// nothing to do
	}

}
