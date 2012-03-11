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
package org.eclipse.e4.xwt.tools.ui.designer.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.xwt.tools.ui.designer.core.editor.EditDomain;
import org.eclipse.e4.xwt.tools.ui.xaml.XamlNode;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class XWTPropertySourceProvider implements IPropertySourceProvider {
	private EditDomain editDomain;
	private Map<Object, IPropertySource> sourceMap = new HashMap<Object, IPropertySource>();

	private Adapter refresher = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			refresh();
		};
	};
	private PropertiesRefreshJob fPropertiesRefreshJob;
	private PropertySheetPage propertyPage;

	public XWTPropertySourceProvider(EditDomain editDomain, PropertySheetPage propertyPage) {
		this.editDomain = editDomain;
		this.propertyPage = propertyPage;
	}

	private void refresh() {
		if (propertyPage != null) {
			getPropertiesRefreshJob().doRefresh();
		}
	}

	private PropertiesRefreshJob getPropertiesRefreshJob() {
		if (fPropertiesRefreshJob == null) {
			fPropertiesRefreshJob = new PropertiesRefreshJob();
		}
		return fPropertiesRefreshJob;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySourceProvider#getPropertySource(java.lang.Object)
	 */
	public IPropertySource getPropertySource(Object object) {
		IPropertySource propertySource = sourceMap.get(object);
		if (propertySource == null) {
			propertySource = createPropertySource(object);
			sourceMap.put(object, propertySource);
		}
		return propertySource;
	}

	/**
	 * @param object
	 */
	private IPropertySource createPropertySource(Object object) {
		PropertyContext context = null;
		if (object instanceof EditPart) {
			EditPart editPart = (EditPart) object;
			context = new PropertyContext(editPart, null);
			if (editDomain == null) {
				editDomain = (EditDomain) ((EditPart) object).getViewer().getEditDomain();
			}
			context.setEditDomain(editDomain);
		} else if (object instanceof PropertyContext) {
			context = (PropertyContext) object;
		}
		if (context != null) {
			final XamlNode model = context.getNode();
			if (!model.eAdapters().contains(refresher)) {
				model.eAdapters().add(refresher);
			}
			return new XWTPropertySource(context) {
				public void setPropertyValue(Object id, Object value) {
					model.eAdapters().remove(refresher);
					super.setPropertyValue(id, value);
					model.eAdapters().add(refresher);
				}
			};
		}
		return null;
	}

	private class PropertiesRefreshJob extends UIJob {
		public static final int UPDATE_DELAY = 200;

		public PropertiesRefreshJob() {
			super("Refresh Properties Page.");
			setSystem(true);
			setPriority(Job.SHORT);
		}

		public void doRefresh() {
			schedule(UPDATE_DELAY);
		}

		public IStatus runInUIThread(IProgressMonitor monitor) {
			if ((propertyPage != null) && (propertyPage.getControl() != null) && !propertyPage.getControl().isDisposed()) {
				propertyPage.refresh();
			}
			return Status.OK_STATUS;
		}
	}
}
