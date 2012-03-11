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
package org.eclipse.e4.xwt.tools.ui.designer.core.style;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.e4.xwt.tools.ui.designer.core.DesignerPlugin;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class SWTStyles {
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle("org.eclipse.e4.xwt.tools.ui.designer.core.style.styles");

	private static StyleGroup[] DEFINED_GROUPS;
	private static final Map<Class<?>, StyleGroup[]> stylesCache = new HashMap<Class<?>, StyleGroup[]>(1);

	public synchronized static StyleGroup[] getDefinedGroups() {
		if (DEFINED_GROUPS == null) {
			List<StyleGroup> groups = new ArrayList<StyleGroup>();
			Field[] feilds = StyleGroup.class.getDeclaredFields();
			for (Field field : feilds) {
				try {
					Object object = field.get(null);
					if (object != null && object instanceof StyleGroup) {
						groups.add((StyleGroup) object);
					}
				} catch (Exception e) {
					DesignerPlugin.logInfo(e);
				}
			}
			DEFINED_GROUPS = groups.toArray(new StyleGroup[0]);
		}
		return DEFINED_GROUPS;
	}

	public static StyleGroup[] getStyles(Class<?> type) {
		StyleGroup[] styleGroups = stylesCache.get(type);
		if (styleGroups == null) {
			List<StyleGroup> groups = new ArrayList<StyleGroup>();
			List<String> styles = new ArrayList<String>(styles(type));

			Collections.sort(styles);

			StyleGroup[] definedGroups = getDefinedGroups();

			for (StyleGroup grp : definedGroups) {
				if (grp.match(type, styles)) {
					groups.add(grp);
				}
			}
			for (StyleGroup added : groups) {
				styles.removeAll(Arrays.asList(added.getStyles()));
			}
			if (!styles.isEmpty()) {
				groups.add(new StyleGroup(type, "default", styles.toArray(new String[styles.size()])));
			}
			styleGroups = groups.toArray(new StyleGroup[groups.size()]);
			stylesCache.put(type, styleGroups);
		}
		return styleGroups;
	}

	public static StyleGroup getStyles(Class<?> type, String groupName) {
		if (type == null || groupName == null) {
			return null;
		}
		StyleGroup[] styles = getStyles(type);
		for (StyleGroup styleGroup : styles) {
			if (groupName.equals(styleGroup.getGroupName())) {
				return styleGroup;
			}
		}
		return null;
	}

	private static Set<String> styles(Class<?> type) {
		Set<String> result = new HashSet<String>();
		String key = type.getName();
		if (RESOURCE_BUNDLE.getObject(key) == null){
			return Collections.emptySet();
		}
		String value = RESOURCE_BUNDLE.getString(key);
		if (value != null) {
			StringTokenizer stk = new StringTokenizer(value, ",");
			while (stk.hasMoreElements()) {
				result.add(stk.nextToken().trim());
			}
		} else {
			Class<?> superclass = type.getSuperclass();
			if (superclass != null && superclass != Object.class) {
				result.addAll(styles(superclass));
			}
		}
		return result;
	}
}
