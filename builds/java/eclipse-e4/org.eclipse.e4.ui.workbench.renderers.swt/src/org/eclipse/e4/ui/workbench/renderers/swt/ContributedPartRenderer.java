/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.renderers.swt;

import javax.inject.Inject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * Create a contribute part.
 */
public class ContributedPartRenderer extends SWTPartRenderer {

	@Inject
	private IPresentationEngine engine;

	@Optional
	@Inject
	private Logger logger;

	private MPart partToActivate;

	private Listener activationListener = new Listener() {
		public void handleEvent(Event event) {
			// we only want to activate the part if the activated widget is
			// actually bound to a model element
			MPart part = (MPart) event.widget.getData(OWNING_ME);
			if (part != null) {
				try {
					partToActivate = part;
					activate(partToActivate);
				} finally {
					partToActivate = null;
				}
			}
		}
	};

	public Object createWidget(final MUIElement element, Object parent) {
		if (!(element instanceof MPart) || !(parent instanceof Composite))
			return null;

		Widget parentWidget = (Widget) parent;
		Widget newWidget = null;
		final MPart part = (MPart) element;

		final Composite newComposite = new Composite((Composite) parentWidget,
				SWT.NONE) {

			/**
			 * Field to determine whether we are currently in the midst of
			 * granting focus to the part.
			 */
			private boolean beingFocused = false;

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.widgets.Composite#setFocus()
			 */
			@Override
			public boolean setFocus() {
				if (!beingFocused) {
					try {
						// we are currently asking the part to take focus
						beingFocused = true;
						// delegate an attempt to set the focus here to the
						// part's implementation (if there is one)
						Object object = part.getObject();
						if (object != null) {
							ContextInjectionFactory.invoke(object, Focus.class,
									part.getContext(), null);
							return true;
						}
						return super.setFocus();
					} finally {
						// we are done, unset our flag
						beingFocused = false;
					}
				}

				if (logger != null) {
					String id = part.getElementId();
					if (id == null) {
						logger.warn(new IllegalStateException(),
								"Blocked recursive attempt to activate part " //$NON-NLS-1$
										+ id);
					} else {
						logger.warn(new IllegalStateException(),
								"Blocked recursive attempt to activate part"); //$NON-NLS-1$
					}
				}

				// already being focused, likely some strange recursive call,
				// just return
				return true;
			}
		};

		newComposite.setLayout(new FillLayout(SWT.VERTICAL));

		newWidget = newComposite;
		bindWidget(element, newWidget);

		// Create a context for this part
		IEclipseContext localContext = part.getContext();
		localContext.set(Composite.class.getName(), newComposite);

		IContributionFactory contributionFactory = (IContributionFactory) localContext
				.get(IContributionFactory.class.getName());
		Object newPart = contributionFactory.create(part.getContributionURI(),
				localContext);
		part.setObject(newPart);

		return newWidget;
	}

	public static void setDescription(MPart part, String description) {
		if (!(part.getWidget() instanceof Composite))
			return;

		Composite c = (Composite) part.getWidget();

		// Do we already have a label?
		if (c.getChildren().length == 2) {
			Label label = (Label) c.getChildren()[0];
			if (description == null)
				description = ""; //$NON-NLS-1$
			label.setText(description);
			label.setToolTipText(description);
			c.layout();
		} else if (c.getChildren().length == 1) {
			c.setLayout(new Layout() {

				@Override
				protected Point computeSize(Composite composite, int wHint,
						int hHint, boolean flushCache) {
					return new Point(0, 0);
				}

				@Override
				protected void layout(Composite composite, boolean flushCache) {
					Rectangle bounds = composite.getBounds();
					if (composite.getChildren().length == 1) {
						composite.getChildren()[0].setBounds(composite
								.getBounds());
					} else if (composite.getChildren().length == 2) {
						Label label = (Label) composite.getChildren()[0];
						Control partCtrl = composite.getChildren()[1];

						int labelHeight = label.computeSize(bounds.width,
								SWT.DEFAULT).y;
						label.setBounds(0, 0, bounds.width, labelHeight);

						partCtrl.setBounds(0, labelHeight, bounds.width,
								bounds.height - labelHeight);
					}
				}
			});

			Control partCtrl = c.getChildren()[0];
			Label label = new Label(c, SWT.NONE);
			label.setText(description);
			label.setToolTipText(description);
			label.moveAbove(partCtrl);
			c.layout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer#requiresFocus
	 * (org.eclipse.e4.ui.model.application.ui.basic.MPart)
	 */
	@Override
	protected boolean requiresFocus(MPart element) {
		if (element == partToActivate) {
			return true;
		}
		return super.requiresFocus(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.renderers.swt.PartFactory#hookControllerLogic
	 * (org.eclipse.e4.ui.model.application.MPart)
	 */
	@Override
	public void hookControllerLogic(final MUIElement me) {
		super.hookControllerLogic(me);
		if (!(me instanceof MPart)) {
			return;
		}
		Widget widget = (Widget) me.getWidget();
		if (widget instanceof Composite) {
			widget.addListener(SWT.Activate, activationListener);
		}

	}

	@Override
	public Object getUIContainer(MUIElement element) {
		if (element instanceof MToolBar) {
			MUIElement container = (MUIElement) ((EObject) element)
					.eContainer();
			MUIElement parent = container.getParent();
			if (parent == null) {
				MPlaceholder placeholder = container.getCurSharedRef();
				if (placeholder != null) {
					return placeholder.getParent().getWidget();
				}
			} else {
				return parent.getWidget();
			}
		}
		return super.getUIContainer(element);
	}

	@Override
	public void disposeWidget(MUIElement element) {
		if (element instanceof MPart) {
			MPart part = (MPart) element;
			MToolBar toolBar = part.getToolbar();
			if (toolBar != null) {
				Widget widget = (Widget) toolBar.getWidget();
				if (widget != null) {
					unbindWidget(toolBar);
					widget.dispose();
				}
			}

			for (MMenu menu : part.getMenus()) {
				engine.removeGui(menu);
			}
		}
		super.disposeWidget(element);
	}
}
