/*******************************************************************************
 * Copyright (c) 2008 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui;

import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.builder.swt.SwtBuilder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ModelPartContent implements ModelContext.Listener {

	private Composite modelComposite;

	private ModelContext modelContext;

	public ModelPartContent(Composite modelComposite) {
		super();
		this.modelComposite = modelComposite;
	}

	public Composite getModelComposite() {
		return modelComposite;
	}

	public void setModelContext(ModelContext modelContext) {
		if (this.modelContext != null) {
			this.modelContext.removeModelContextListener(this);
		}
		this.modelContext = modelContext;
		if (this.modelContext != null) {
			this.modelContext.addModelContextListener(this);
		}
		updatePartContent();
	}

	private void updatePartContent() {
		EObject model = (modelContext != null ? modelContext.getModel() : null);
		if (model != null) {
			updateModel(model, modelComposite);
		}
	}

	public void contextChanged(ModelContext context) {
		updatePartContent();
	}
	
	private AbstractBuilder builder;

	public AbstractBuilder getBuilder() {
		if (builder == null) {
			builder = new SwtBuilder();
		}
		return builder;
	}

	private void updateModel(EObject model, Object parent) {
		dispose();
		if (model == null) {
			return;
		}
		buildModel(model);
	}

	protected void buildModel(EObject model) {
		if (modelComposite != null) {
			getBuilder().build(model, modelComposite);
			modelComposite.layout();
		}
	}

	public void dispose() {
		setModelContext(null);
		if (builder != null) {
			builder.dispose();
		}
		builder = null;
		if (! modelComposite.isDisposed()) {
			Control[] children = modelComposite.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (! children[i].isDisposed()) {
					children[i].dispose();
				}
			}
		}
	}
}