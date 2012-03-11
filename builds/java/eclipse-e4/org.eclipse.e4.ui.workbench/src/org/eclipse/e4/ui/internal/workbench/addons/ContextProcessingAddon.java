/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.internal.workbench.addons;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.eclipse.core.commands.contexts.Context;
import org.eclipse.core.commands.contexts.ContextManager;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.Activator;
import org.eclipse.e4.ui.internal.workbench.Policy;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Process contexts in the model, feeding them into the command service.
 */
public class ContextProcessingAddon {

	@Inject
	private MApplication application;

	@Inject
	private IEventBroker broker;

	@Inject
	private ContextManager contextManager;

	private EventHandler additionHandler;

	@PostConstruct
	public void init() {
		defineContexts();
		registerModelListeners();
	}

	private void defineContexts() {
		Activator.trace(Policy.DEBUG_CMDS, "Initialize contexts and parents from model", null); //$NON-NLS-1$
		for (MBindingContext root : application.getRootContext()) {
			defineContexts(null, root);
		}
	}

	private void defineContexts(MBindingContext parent, MBindingContext current) {
		Context context = contextManager.getContext(current.getElementId());
		if (!context.isDefined()) {
			context.define(current.getName(), current.getDescription(), parent == null ? null
					: parent.getElementId());
		}
		for (MBindingContext child : current.getChildren()) {
			defineContexts(current, child);
		}
	}

	private void undefineContext(MBindingContext current) {
		Context context = contextManager.getContext(current.getElementId());
		context.undefine();
	}

	@PreDestroy
	public void dispose() {
		unregsiterModelListeners();
	}

	private void registerModelListeners() {
		additionHandler = new EventHandler() {
			public void handleEvent(Event event) {
				Object elementObj = event.getProperty(UIEvents.EventTags.ELEMENT);
				if (elementObj instanceof MBindingContext) {
					Object newObj = event.getProperty(UIEvents.EventTags.NEW_VALUE);
					Object oldObj = event.getProperty(UIEvents.EventTags.OLD_VALUE);
					if (UIEvents.EventTypes.ADD.equals(event.getProperty(UIEvents.EventTags.TYPE))
							&& newObj instanceof MBindingContext) {
						MBindingContext newCtx = (MBindingContext) newObj;
						defineContexts((MBindingContext) elementObj, newCtx);
					} else if (UIEvents.EventTypes.REMOVE.equals(event
							.getProperty(UIEvents.EventTags.TYPE))
							&& oldObj instanceof MBindingContext) {
						MBindingContext oldCtx = (MBindingContext) oldObj;
						undefineContext(oldCtx);
					}
				}
			}
		};
		broker.subscribe(UIEvents.buildTopic(UIEvents.BindingContext.TOPIC,
				UIEvents.BindingContext.CHILDREN), additionHandler);
	}

	private void unregsiterModelListeners() {
		broker.unsubscribe(additionHandler);
	}
}
