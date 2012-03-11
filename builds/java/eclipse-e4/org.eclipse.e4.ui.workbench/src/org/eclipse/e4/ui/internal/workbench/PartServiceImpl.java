/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.ui.internal.workbench;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MGenericStack;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.AdvancedFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler.Save;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PartServiceImpl implements EPartService {

	private EventHandler selectedHandler = new EventHandler() {
		public void handleEvent(Event event) {
			// no need to do anything if we have no listeners
			if (!listeners.isEmpty()) {
				Object oldSelected = event.getProperty(UIEvents.EventTags.OLD_VALUE);
				if (oldSelected instanceof MPlaceholder) {
					oldSelected = ((MPlaceholder) oldSelected).getRef();
				}
				Object selected = event.getProperty(UIEvents.EventTags.NEW_VALUE);
				if (selected instanceof MPlaceholder) {
					selected = ((MPlaceholder) selected).getRef();
				}

				MPart oldSelectedPart = oldSelected instanceof MPart ? (MPart) oldSelected : null;
				MPart selectedPart = selected instanceof MPart ? (MPart) selected : null;

				if (oldSelectedPart != null && getParts().contains(selectedPart)) {
					firePartHidden(oldSelectedPart);
				}

				if (selectedPart != null && selectedPart.isToBeRendered()
						&& getParts().contains(selectedPart)) {
					MPlaceholder placeholder = selectedPart.getCurSharedRef();
					// ask the renderer to create this part
					if (placeholder == null) {
						if (selectedPart.getParent().getRenderer() != null) {
							engine.createGui(selectedPart);
							firePartVisible(selectedPart);
							firePartBroughtToTop(selectedPart);
						}
					} else if (placeholder.getParent().getRenderer() != null) {
						engine.createGui(placeholder);
						firePartVisible(selectedPart);
						firePartBroughtToTop(selectedPart);
					}
				}
			}
		}
	};

	private MApplication application;

	/**
	 * Might be null if this part service is created for the application
	 */
	private MWindow workbenchWindow;

	@Inject
	private IPresentationEngine engine;

	@Inject
	private EModelService modelService;

	@Inject
	private Logger logger;

	@Inject
	@Optional
	private ISaveHandler saveHandler;

	@Inject
	private IEventBroker eventBroker;

	private PartActivationHistory partActivationHistory;

	private MPart activePart;

	private ListenerList listeners = new ListenerList();

	private boolean constructed = false;

	@Inject
	public PartServiceImpl(MApplication application, @Optional MWindow window) {
		// no need to track changes:
		this.application = application;
		workbenchWindow = window;
	}

	private void log(String unidentifiedMessage, String identifiedMessage, String id, Exception e) {
		if (id == null || id.length() == 0) {
			logger.error(e, unidentifiedMessage);
		} else {
			logger.error(e, NLS.bind(identifiedMessage, id));
		}
	}

	@Inject
	void setPart(@Optional @Named(IServiceConstants.ACTIVE_PART) MPart p) {
		if (activePart != p) {
			MPart lastActivePart = activePart;
			activePart = p;

			// no need to do anything if we have no listeners
			if (constructed && !listeners.isEmpty()) {
				if (lastActivePart != null && lastActivePart != activePart) {
					firePartDeactivated(lastActivePart);
				}

				if (activePart != null) {
					firePartActivated(activePart);
				}
			}
		}
	}

	@PostConstruct
	void postConstruct() {
		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.ElementContainer.TOPIC,
				UIEvents.ElementContainer.SELECTEDELEMENT), selectedHandler);
		constructed = true;
		partActivationHistory = new PartActivationHistory(this, modelService);
		if (activePart != null) {
			partActivationHistory.prepend(activePart);
		}
	}

	@PreDestroy
	void preDestroy() {
		constructed = false;
		eventBroker.unsubscribe(selectedHandler);
		partActivationHistory.clear();
	}

	private void firePartActivated(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partActivated(part);
		}
	}

	private void firePartDeactivated(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partDeactivated(part);
		}
	}

	private void firePartHidden(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partHidden(part);
		}
	}

	private void firePartVisible(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partVisible(part);
		}
	}

	private void firePartBroughtToTop(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partBroughtToTop(part);
		}
	}

	public void addPartListener(IPartListener listener) {
		listeners.add(listener);
	}

	public void removePartListener(IPartListener listener) {
		listeners.remove(listener);
	}

	private MWindow getWindow() {
		if (workbenchWindow != null)
			return workbenchWindow;
		if (application.getSelectedElement() != null)
			return application.getSelectedElement();
		List<MWindow> windows = application.getChildren();
		if (windows.size() != 0)
			return windows.get(0);
		return null;
	}

	private MContext getParentWithContext(MUIElement part) {
		MElementContainer<MUIElement> parent = part.getParent();
		MUIElement intermediate = parent;
		if (intermediate == null) {
			intermediate = part;
		} else {
			while (parent != null) {
				if (parent instanceof MContext) {
					if (((MContext) parent).getContext() != null)
						return (MContext) parent;
				}
				intermediate = parent;
				parent = parent.getParent();
			}
		}

		MPlaceholder placeholder = modelService.findPlaceholderFor(getWindow(), intermediate);
		parent = placeholder.getParent();
		while (parent != null) {
			if (parent instanceof MContext) {
				if (((MContext) parent).getContext() != null)
					return (MContext) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public void bringToTop(MPart part) {
		if (isInContainer(part)) {
			MUIElement currentElement = part;
			MElementContainer<MUIElement> parent = part.getParent();
			if (parent == null) {
				currentElement = modelService.findPlaceholderFor(getWindow(), part);
				parent = currentElement.getParent();
			}

			MUIElement oldSelectedElement = parent.getSelectedElement();

			modelService.bringToTop(part);

			// check to make sure that the currently selected element is actually valid
			if (oldSelectedElement != currentElement
					&& parent.getChildren().contains(oldSelectedElement)
					&& parent instanceof MGenericStack<?>) {
				if (oldSelectedElement instanceof MPlaceholder) {
					oldSelectedElement = ((MPlaceholder) oldSelectedElement).getRef();
				}
				internalFixContext(part, oldSelectedElement);
			}
		}
	}

	private IEclipseContext getSubContext(MUIElement element) {
		if (element instanceof MContext) {
			return ((MContext) element).getContext();
		} else if (element instanceof MElementContainer<?>) {
			Object selectedElement = ((MElementContainer<?>) element).getSelectedElement();
			if (selectedElement instanceof MContext) {
				return ((MContext) selectedElement).getContext();
			} else if (selectedElement instanceof MElementContainer<?>) {
				return getSubContext((MUIElement) selectedElement);
			}
		}
		return null;
	}

	private void internalFixContext(MPart part, MUIElement oldSelectedElement) {
		if (oldSelectedElement == null) {
			return;
		}

		MContext parentPart = getParentWithContext(oldSelectedElement);
		if (parentPart == null) {
			// technically this shouldn't happen as there should be an MWindow somewhere
			return;
		}
		IEclipseContext parentContext = parentPart.getContext();
		IEclipseContext oldContext = getSubContext(oldSelectedElement);
		Object child = parentContext.getActiveChild();
		if (child == null || oldContext == null || child == oldContext) {
			if (part == null) {
				// TBD this should not be necessary; deactivation is missing somewhere
				IEclipseContext currentActive = parentContext.getActiveChild();
				if (currentActive != null)
					currentActive.deactivate();
			} else
				part.getContext().activate();
		}
	}

	public MPart findPart(String id) {
		List<MPart> parts = getParts(MPart.class, id);
		return parts.size() > 0 ? parts.get(0) : null;
	}

	private <T> List<T> getParts(Class<T> cls, String id) {
		return modelService.findElements(workbenchWindow, id, cls, null,
				EModelService.OUTSIDE_PERSPECTIVE | EModelService.IN_ACTIVE_PERSPECTIVE
						| EModelService.IN_SHARED_AREA);
	}

	public Collection<MPart> getParts() {
		return getParts(MPart.class, null);
	}

	public boolean isPartVisible(MPart part) {
		if (isInContainer(part)) {
			MUIElement element = part;
			MElementContainer<?> parent = part.getParent();
			if (parent == null) {
				// might be a shared part
				element = part.getCurSharedRef();
				if (element == null) {
					return false;
				}

				parent = element.getParent();
				if (parent == null) {
					return false;
				}
			}

			if (parent instanceof MPartStack) {
				return parent.getSelectedElement() == element;
			}

			return element.isVisible();
		}
		return false;
	}

	private boolean isInContainer(MUIElement element) {
		List<MUIElement> allPerspectiveElements = modelService.findElements(workbenchWindow, null,
				MUIElement.class, null, EModelService.PRESENTATION);
		return allPerspectiveElements.contains(element);
	}

	boolean isInContainer(MElementContainer<?> container, MUIElement element) {
		for (Object object : container.getChildren()) {
			if (object == element) {
				return true;
			} else if (object instanceof MElementContainer<?>) {
				if (isInContainer((MElementContainer<?>) object, element)) {
					return true;
				}
			} else if (object instanceof MPlaceholder) {
				MUIElement ref = ((MPlaceholder) object).getRef();
				if (ref == element) {
					return true;
				} else if (ref instanceof MElementContainer<?>) {
					if (isInContainer((MElementContainer<?>) ref, element)) {
						return true;
					}
				}
			} else if (object instanceof MPerspective) {
				MPerspective persp = (MPerspective) object;
				for (MWindow dw : persp.getWindows()) {
					if (isInContainer(dw, element))
						return true;
				}
			} else if (object instanceof MWindow) {
				MWindow win = (MWindow) object;
				for (MWindow dw : win.getWindows()) {
					if (isInContainer(dw, element))
						return true;
				}
			}
		}

		if (container instanceof MWindow) {
			MWindow win = (MWindow) container;
			for (MWindow dw : win.getWindows()) {
				if (isInContainer(dw, element))
					return true;
			}
		}

		if (container instanceof MPerspective) {
			MPerspective persp = (MPerspective) container;
			for (MWindow dw : persp.getWindows()) {
				if (isInContainer(dw, element))
					return true;
			}
		}

		return false;
	}

	MPlaceholder getLocalPlaceholder(MUIElement part) {
		return modelService.findPlaceholderFor(getWindow(), part);
	}

	public void switchPerspective(MPerspective perspective) {
		Assert.isNotNull(perspective);
		MWindow window = getWindow();
		if (window != null && isInContainer(window, perspective)) {
			perspective.getParent().setSelectedElement(perspective);
			List<MPart> newPerspectiveParts = modelService.findElements(perspective, null,
					MPart.class, null);
			// if possible, keep the same active part across perspective switches
			if (newPerspectiveParts.contains(activePart)
					&& partActivationHistory.isValid(perspective, activePart)) {
				MPart target = activePart;
				IEclipseContext activeChild = activePart.getContext().getParent().getActiveChild();
				if (activeChild != null) {
					activeChild.deactivate();
				}
				perspective.getContext().activate();
				modelService.bringToTop(target);
				activate(target, true, false);
				return;
			}

			MPart newActivePart = perspective.getContext().getActiveLeaf().get(MPart.class);
			if (newActivePart == null) {
				// whatever part was previously active can no longer be found, find another one
				MPart candidate = partActivationHistory.getActivationCandidate(perspective);
				if (candidate != null) {
					modelService.bringToTop(candidate);
					partActivationHistory.activate(candidate, false);
					return;
				}
			}

			// there seems to be no parts in this perspective, just activate it as is then
			if (newActivePart == null) {
				modelService.bringToTop(perspective);
				perspective.getContext().activate();
			} else {
				activate(newActivePart, true, false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EPartService#activate(org.eclipse.e4.ui.model.application
	 * .MPart)
	 */
	public void activate(MPart part) {
		activate(part, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EPartService#activate(org.eclipse.e4.ui.model.application
	 * .MPart,boolean)
	 */
	public void activate(MPart part, boolean requiresFocus) {
		activate(part, requiresFocus, true);
	}

	private void activate(MPart part, boolean requiresFocus, boolean activateBranch) {
		// only activate parts that is under our control
		if (!isInContainer(part)) {
			return;
		}

		MWindow window = getWindow();
		IEclipseContext windowContext = window.getContext();
		// check if the active part has changed or if we are no longer the active window
		if (windowContext.getParent().getActiveChild() == windowContext && part == activePart) {
			// insert it in the beginning of the activation history, it may not have been inserted
			// pending when this service was instantiated
			partActivationHistory.prepend(part);
			return;
		}

		// record any sibling into the activation history if necessary, this will allow it to be
		// reselected again in the future as it will be an activation candidate in the future, this
		// prevents other unrendered elements from being selected arbitrarily which would cause
		// unwanted bundle activation
		recordStackActivation(part);

		modelService.bringToTop(part);
		window.getParent().setSelectedElement(window);

		Object object = part.getObject();
		if (object != null && requiresFocus) {
			try {
				ContextInjectionFactory.invoke(object, Focus.class, part.getContext(), null);
			} catch (InjectionException e) {
				log("Failed to grant focus to part", "Failed to grant focus to part ({0})", //$NON-NLS-1$ //$NON-NLS-2$
						part.getElementId(), e);
			} catch (RuntimeException e) {
				log("Failed to grant focus to part via DI", //$NON-NLS-1$
						"Failed to grant focus via DI to part ({0})", part.getElementId(), e); //$NON-NLS-1$
			}
		}

		partActivationHistory.activate(part, activateBranch);
	}

	/**
	 * Records the specified parent part's selected element in the activation history if the parent
	 * is a stack.
	 * 
	 * @param part
	 *            the part whose parent's selected element should be checked for activation history
	 *            recording
	 */
	private void recordStackActivation(MPart part) {
		MElementContainer<? extends MUIElement> parent = part.getParent();
		if (parent instanceof MGenericStack) {
			recordSelectedActivation(parent);
		} else if (parent == null) {
			MPlaceholder placeholder = part.getCurSharedRef();
			if (placeholder != null) {
				parent = placeholder.getParent();
				if (parent instanceof MGenericStack) {
					recordSelectedActivation(parent);
				}
			}
		}
	}

	/**
	 * Records the specified parent 's selected element in the activation history.
	 * 
	 * @param parent
	 *            the element whose selected element should be checked for activation history
	 *            recording
	 */
	private void recordSelectedActivation(MElementContainer<? extends MUIElement> parent) {
		MUIElement selectedElement = parent.getSelectedElement();
		if (selectedElement instanceof MPart) {
			partActivationHistory.append((MPart) selectedElement);
		} else if (selectedElement instanceof MPlaceholder) {
			MUIElement ref = ((MPlaceholder) selectedElement).getRef();
			if (ref instanceof MPart) {
				partActivationHistory.append((MPart) ref);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.workbench.modeling.EPartService#getActivePart()
	 */
	public MPart getActivePart() {
		return activePart;
	}

	private MPartDescriptor findDescriptor(String id) {
		for (MPartDescriptor descriptor : application.getDescriptors()) {
			if (descriptor.getElementId().equals(id)) {
				return descriptor;
			}
		}
		return null;
	}

	private MPart createPart(MPartDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		MPart part = BasicFactoryImpl.eINSTANCE.createPart();
		part.setElementId(descriptor.getElementId());
		part.getMenus().addAll(EcoreUtil.copyAll(descriptor.getMenus()));
		if (descriptor.getToolbar() != null) {
			part.setToolbar((MToolBar) EcoreUtil.copy((EObject) descriptor.getToolbar()));
		}
		part.setContributorURI(descriptor.getContributorURI());
		part.setCloseable(descriptor.isCloseable());
		part.setContributionURI(descriptor.getContributionURI());
		part.setLabel(descriptor.getLabel());
		part.setIconURI(descriptor.getIconURI());
		part.setTooltip(descriptor.getTooltip());
		part.getHandlers().addAll(EcoreUtil.copyAll(descriptor.getHandlers()));
		part.getTags().addAll(descriptor.getTags());
		part.getBindingContexts().addAll(descriptor.getBindingContexts());
		return part;
	}

	public MPart createPart(String id) {
		MPartDescriptor descriptor = findDescriptor(id);
		return createPart(descriptor);
	}

	public MPlaceholder createSharedPart(String id) {
		return createSharedPart(id, false);
	}

	public MPlaceholder createSharedPart(String id, boolean force) {
		MWindow sharedWindow = getWindow();
		// Do we already have the part to share?
		MPart sharedPart = null;

		// check for existing parts if necessary
		if (!force) {
			for (MUIElement element : sharedWindow.getSharedElements()) {
				if (element.getElementId().equals(id)) {
					sharedPart = (MPart) element;
					break;
				}
			}
		}

		if (sharedPart == null) {
			MPartDescriptor descriptor = findDescriptor(id);
			sharedPart = createPart(descriptor);
			if (sharedPart == null) {
				return null;
			}

			sharedWindow.getSharedElements().add(sharedPart);
		}

		return createSharedPart(sharedPart);
	}

	private MPlaceholder createSharedPart(MPart sharedPart) {
		// Create and return a reference to the shared part
		MPlaceholder sharedPartRef = AdvancedFactoryImpl.eINSTANCE.createPlaceholder();
		sharedPartRef.setElementId(sharedPart.getElementId());
		sharedPartRef.setRef(sharedPart);
		return sharedPartRef;
	}

	/**
	 * Adds a part to the current container if it isn't already in the container. The part may still
	 * be added to the container if the part supports having multiple copies of itself in a given
	 * container.
	 * 
	 * @param providedPart
	 *            the part to add
	 * @param localPart
	 *            a part that shares attributes with <code>providedPart</code>, for example, it may
	 *            have been backed by the same part descriptor, this part may already be in the
	 *            current container
	 * @return a part that has been added to the current container, note that this may not
	 *         necessarily be <code>providedPart</code>
	 * @see MPartDescriptor#isAllowMultiple()
	 */
	private MPart addPart(MPart providedPart, MPart localPart) {
		MPartDescriptor descriptor = findDescriptor(providedPart.getElementId());
		if (descriptor == null) {
			// there is no part descriptor backing the provided part, just add it to the container
			// if it's not already there
			if (!isInContainer(providedPart)) {
				adjustPlaceholder(providedPart);
				addToLastContainer(null, providedPart);
			}
		} else {
			if (providedPart != localPart && !descriptor.isAllowMultiple()) {
				// multiple copies of this part are not allowed, just return the local one
				return localPart;
			}

			// already in the container, return as is
			if (isInContainer(providedPart)) {
				return providedPart;
			}

			// corrects this part's placeholder if necessary
			adjustPlaceholder(providedPart);

			String category = descriptor.getCategory();
			if (category == null) {
				// no category, just add it to the end
				addToLastContainer(null, providedPart);
			} else {
				if ("org.eclipse.e4.primaryDataStack".equals(category)) { //$NON-NLS-1$
					MElementContainer<MUIElement> container = getContainer();
					MUIElement area = modelService.find("org.eclipse.ui.editorss", container); //$NON-NLS-1$
					List<MPartStack> sharedStacks = modelService.findElements(area, null,
							MPartStack.class, null);
					if (sharedStacks.size() > 0) {
						for (MPartStack stack : sharedStacks) {
							if (stack.isToBeRendered()) {
								stack.getChildren().add(providedPart);
								break;
							}
						}
					} else {
						addToLastContainer(null, providedPart);
					}
				} else {
					List<MElementContainer> containers = modelService.findElements(getContainer(),
							null, MElementContainer.class, Collections.singletonList(category));
					if (containers.isEmpty()) {
						// couldn't find any containers with the specified tag, just add it to the
						// end
						addToLastContainer(category, providedPart);
					} else {
						// add the part to the container
						MElementContainer container = containers.get(0);
						MPlaceholder placeholder = providedPart.getCurSharedRef();
						if (placeholder == null) {
							container.getChildren().add(providedPart);
						} else {
							container.getChildren().add(placeholder);
						}
					}
				}
			}
		}
		return providedPart;
	}

	private void adjustPlaceholder(MPart part) {
		if (isShared(part)) {
			MPlaceholder placeholder = part.getCurSharedRef();
			// if this part doesn't have any placeholders, we need to make one
			if (placeholder == null
			// alternatively, if it has one but it's not in the current container, then we
			// need to spawn another one as we don't want to reuse the same one and end up
			// shifting that placeholder to the current container during the add operation
					|| (placeholder.getParent() != null && !isInContainer(placeholder))) {
				placeholder = createSharedPart(part);
				part.setCurSharedRef(placeholder);
			}
		}
	}

	private boolean isShared(MPart part) {
		return getWindow().getSharedElements().contains(part);
	}

	private void addToLastContainer(String category, MPart part) {
		MElementContainer<?> lastContainer = getLastContainer();
		MPlaceholder placeholder = part.getCurSharedRef();
		if (placeholder == null) {
			((List) lastContainer.getChildren()).add(part);
		} else {
			((List) lastContainer.getChildren()).add(placeholder);
		}

		if (category != null) {
			lastContainer.getTags().add(category);
		}
	}

	private MElementContainer<?> getLastContainer() {
		MElementContainer<MUIElement> searchRoot = getContainer();
		List<MUIElement> children = searchRoot.getChildren();
		if (children.size() == 0) {
			MPartStack stack = BasicFactoryImpl.eINSTANCE.createPartStack();
			searchRoot.getChildren().add(stack);
			return stack;
		}

		MElementContainer<?> lastContainer = getLastContainer(searchRoot, children);
		if (lastContainer == null) {
			MPartStack stack = BasicFactoryImpl.eINSTANCE.createPartStack();
			searchRoot.getChildren().add(stack);
			return stack;
		} else if (!(lastContainer instanceof MPartStack)) {
			MPartStack stack = BasicFactoryImpl.eINSTANCE.createPartStack();
			((List) lastContainer.getChildren()).add(stack);
			return stack;
		}
		return lastContainer;
	}

	private MElementContainer<?> getLastContainer(MElementContainer<?> container, List<?> children) {
		if (children.isEmpty()) {
			return null;
		}

		for (int i = children.size() - 1; i > -1; i--) {
			Object muiElement = children.get(i);
			if (muiElement instanceof MElementContainer<?>) {
				MElementContainer<?> childContainer = (MElementContainer<?>) muiElement;
				MElementContainer<?> lastContainer = getLastContainer(childContainer,
						childContainer.getChildren());
				if (lastContainer != null) {
					return lastContainer;
				}
			}
		}
		return container;
	}

	/**
	 * Returns the parent container of the specified element. If one cannot be found, a check will
	 * be performed to see whether the element is being represented by a placeholder, if it is, the
	 * placeholder's parent will be returned, if any.
	 * 
	 * @param element
	 *            the element to query
	 * @return the element's parent container, or the parent container of the specified element's
	 *         current placeholder, if it has one
	 */
	private MElementContainer<MUIElement> getParent(MUIElement element) {
		MElementContainer<MUIElement> parent = element.getParent();
		if (parent == null) {
			MPlaceholder placeholder = element.getCurSharedRef();
			if (placeholder == null) {
				MElementContainer<MUIElement> container = getContainer();
				return findContainer(container, element);
			}
			return placeholder.getParent();
		}
		return parent;
	}

	private MElementContainer<MUIElement> findContainer(MElementContainer<?> container,
			MUIElement element) {
		for (Object child : container.getChildren()) {
			if (child == element) {
				return (MElementContainer<MUIElement>) container;
			} else if (child instanceof MPlaceholder) {
				MPlaceholder placeholder = (MPlaceholder) child;
				MUIElement ref = placeholder.getRef();
				if (ref == element) {
					return (MElementContainer<MUIElement>) container;
				} else if (ref instanceof MElementContainer<?>) {
					MElementContainer<MUIElement> match = findContainer((MElementContainer<?>) ref,
							element);
					if (match != null) {
						return match;
					}
				}
			} else if (child instanceof MElementContainer<?>) {
				MElementContainer<MUIElement> match = findContainer((MElementContainer<?>) child,
						element);
				if (match != null) {
					return match;
				}
			}
		}
		return null;
	}

	private MUIElement getRemoveTarget(MPart part) {
		MPlaceholder placeholder = getLocalPlaceholder(part);
		return placeholder == null ? part : placeholder;
	}

	public MPart addPart(MPart part) {
		Assert.isNotNull(part);
		MPart localPart = findPart(part.getElementId());
		return addPart(part, localPart == null ? part : localPart);
	}

	public MPart showPart(String id, PartState partState) {
		Assert.isNotNull(id);
		Assert.isNotNull(partState);

		MPart part = findPart(id);
		if (part == null) {
			MPartDescriptor descriptor = findDescriptor(id);
			part = createPart(descriptor);
			if (part == null) {
				return null;
			}
		}
		return showPart(addPart(part), partState);
	}

	public MPart showPart(MPart part, PartState partState) {
		Assert.isNotNull(part);
		Assert.isNotNull(partState);

		MPart addedPart = addPart(part);
		MPlaceholder localPlaceholder = getLocalPlaceholder(addedPart);
		// correct the placeholder setting if necessary
		if (localPlaceholder != null && addedPart.getCurSharedRef() != localPlaceholder) {
			addedPart.setCurSharedRef(localPlaceholder);
		}

		switch (partState) {
		case ACTIVATE:
			activate(addedPart);
			return addedPart;
		case VISIBLE:
			MPart activePart = getActivePart();
			if (activePart == null || getParent(activePart) == getParent(addedPart)) {
				activate(addedPart);
			} else {
				bringToTop(addedPart);
			}
			return addedPart;
		case CREATE:
			MPlaceholder placeholder = addedPart.getCurSharedRef();
			if (placeholder != null) {
				addedPart.setToBeRendered(true);
				createElement(placeholder);
			} else {
				createElement(addedPart);
			}
			return addedPart;
		}
		return addedPart;
	}

	private void createElement(MUIElement element) {
		// render this element
		element.setToBeRendered(true);
		// render all of its parents
		MElementContainer<MUIElement> parent = element.getParent();
		while (parent != null) {
			parent.setToBeRendered(true);
			parent = parent.getParent();
		}
		// ask the engine to create the element
		engine.createGui(element);

		parent = element.getParent();
		if (parent.getChildren().size() == 1) {
			// if we're the only child, set ourselves as the selected element
			parent.setSelectedElement(element);
		}
	}

	public void requestActivation() {
		if (activePart == null) {
			MPart candidate = partActivationHistory.getActivationCandidate(getParts());
			if (candidate != null) {
				activate(candidate);
			}
		} else if (!getParts().contains(activePart)) {
			MPart candidate = partActivationHistory.getNextActivationCandidate(getParts(),
					activePart);
			if (candidate != null) {
				activate(candidate);
			}
		}
	}

	public void hidePart(MPart part) {
		hidePart(part, false);
	}

	public void hidePart(MPart part, boolean force) {
		if (isInContainer(part)) {
			MPlaceholder sharedRef = part.getCurSharedRef();
			MUIElement toBeRemoved = getRemoveTarget(part);
			MElementContainer<MUIElement> parent = getParent(toBeRemoved);
			List<MUIElement> children = parent.getChildren();

			// check if we're a placeholder but not actually the shared ref of the part
			if (toBeRemoved != part && toBeRemoved instanceof MPlaceholder
					&& sharedRef != toBeRemoved) {
				toBeRemoved.setToBeRendered(false);

				// if so, not much to do, remove ourselves if necessary but that's it
				if (force || part.getTags().contains(REMOVE_ON_HIDE_TAG)) {
					parent.getChildren().remove(toBeRemoved);
				}
				return;
			}

			boolean isActiveChild = isActiveChild(part);
			MPart activationCandidate = null;
			// check if we're the active child
			if (isActiveChild) {
				// get the activation candidate if we are
				activationCandidate = partActivationHistory.getNextActivationCandidate(getParts(),
						part);
			}

			if (parent.getSelectedElement() == toBeRemoved) {
				// if we're the selected element and we're going to be hidden, need to select
				// something else
				MUIElement candidate = partActivationHistory.getSiblingSelectionCandidate(part);
				candidate = candidate == null ? null
						: candidate.getCurSharedRef() == null ? candidate : candidate
								.getCurSharedRef();
				if (candidate != null && children.contains(candidate)) {
					parent.setSelectedElement(candidate);
				} else {
					for (MUIElement child : children) {
						if (child != toBeRemoved && child.isToBeRendered()) {
							parent.setSelectedElement(child);
							break;
						}
					}
				}
			}

			if (activationCandidate == null) {
				// nothing else to activate and we're the active child, deactivate
				if (isActiveChild) {
					part.getContext().deactivate();
				}
			} else {
				// activate our candidate
				activate(activationCandidate);
			}

			if (toBeRemoved != null) {
				toBeRemoved.setToBeRendered(false);
			} else {
				part.setToBeRendered(false);
			}

			if (parent.getSelectedElement() == toBeRemoved) {
				parent.setSelectedElement(null);
			}

			if (force || part.getTags().contains(REMOVE_ON_HIDE_TAG)) {
				children.remove(toBeRemoved);
			}
			// remove ourselves from the activation history also since we're being hidden
			partActivationHistory.forget(getWindow(), part, toBeRemoved == part);
		}
	}

	private boolean isActiveChild(MPart part) {
		IEclipseContext context = part.getContext();
		return context != null && context.getParent().getActiveChild() == context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.workbench.modeling.EPartService#getDirtyParts()
	 */
	public Collection<MPart> getDirtyParts() {
		List<MPart> dirtyParts = new ArrayList<MPart>();
		for (MPart part : getParts()) {
			if (part.isDirty()) {
				dirtyParts.add(part);
			}
		}
		return dirtyParts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EPartService#save(org.eclipse.e4.ui.model.application.
	 * MSaveablePart, boolean)
	 */
	public boolean savePart(MPart part, boolean confirm) {
		if (!part.isDirty()) {
			return true;
		}

		if (confirm && saveHandler != null) {
			switch (saveHandler.promptToSave(part)) {
			case NO:
				return true;
			case CANCEL:
				return false;
			}
		}

		Object client = part.getObject();
		try {
			ContextInjectionFactory.invoke(client, Persist.class, part.getContext());
		} catch (InjectionException e) {
			log("Failed to persist contents of part", "Failed to persist contents of part ({0})", //$NON-NLS-1$ //$NON-NLS-2$
					part.getElementId(), e);
			return false;
		} catch (RuntimeException e) {
			log("Failed to persist contents of part via DI", //$NON-NLS-1$
					"Failed to persist contents of part ({0}) via DI", part.getElementId(), e); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	public boolean saveAll(boolean confirm) {
		Collection<MPart> dirtyParts = getDirtyParts();
		if (dirtyParts.isEmpty()) {
			return true;
		}

		if (confirm && saveHandler != null) {
			List<MPart> dirtyPartsList = Collections.unmodifiableList(new ArrayList<MPart>(
					dirtyParts));
			Save[] decisions = saveHandler.promptToSave(dirtyPartsList);
			for (Save decision : decisions) {
				if (decision == Save.CANCEL) {
					return false;
				}
			}

			for (int i = 0; i < decisions.length; i++) {
				if (decisions[i] == Save.YES) {
					if (!savePart(dirtyPartsList.get(i), false)) {
						return false;
					}
				}
			}
			return true;
		}

		for (MPart dirtyPart : dirtyParts) {
			if (!savePart(dirtyPart, false)) {
				return false;
			}
		}
		return true;
	}

	public Collection<MInputPart> getInputParts(String inputUri) {
		Assert.isNotNull(inputUri, "Input uri must not be null"); //$NON-NLS-1$

		Collection<MInputPart> rv = new ArrayList<MInputPart>();

		for (MInputPart p : getParts(MInputPart.class, null)) {
			if (inputUri.equals(p.getInputURI())) {
				rv.add(p);
			}
		}

		return rv;
	}

	/**
	 * "Container" here is: 1) a selected MPerspective, or, if none available 2) the MWindow for
	 * which this part service is created, or, if not available, 3) the MApplication.
	 */
	private MElementContainer<MUIElement> getContainer() {
		MElementContainer<? extends MUIElement> outerContainer = (workbenchWindow != null) ? workbenchWindow
				: application;

		// see if we can narrow it down to the active perspective
		for (MElementContainer<?> container = outerContainer; container != null;) {
			if (container instanceof MPerspective)
				return (MElementContainer<MUIElement>) container;
			Object child = container.getSelectedElement();
			if (child == null)
				break;
			if (child instanceof MElementContainer<?>)
				container = (MElementContainer<?>) child;
			else
				break;
		}
		return (MElementContainer<MUIElement>) outerContainer;
	}

}
