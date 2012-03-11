/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.renderers.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.RunAndTrack;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.internal.workbench.ContributionsAnalyzer;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MCoreExpression;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.ExpressionContext;
import org.eclipse.e4.ui.workbench.swt.util.ISWTResourceUtilities;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Create a contribute part.
 */
public class MenuManagerRenderer extends SWTPartRenderer {
	private static final String NO_LABEL = "UnLabled"; //$NON-NLS-1$
	public static final String GROUP_MARKER = "org.eclipse.jface.action.GroupMarker.GroupMarker(String)"; //$NON-NLS-1$

	private Map<MMenu, MenuManager> modelToManager = new HashMap<MMenu, MenuManager>();
	private Map<MenuManager, MMenu> managerToModel = new HashMap<MenuManager, MMenu>();

	private Map<MMenuElement, IContributionItem> modelToContribution = new HashMap<MMenuElement, IContributionItem>();
	private Map<IContributionItem, MMenuElement> contributionToModel = new HashMap<IContributionItem, MMenuElement>();

	private Map<MMenuElement, ContributionRecord> modelContributionToRecord = new HashMap<MMenuElement, ContributionRecord>();
	private Map<MMenuElement, ArrayList<ContributionRecord>> sharedElementToRecord = new HashMap<MMenuElement, ArrayList<ContributionRecord>>();

	@Inject
	private Logger logger;

	@Inject
	private MApplication application;

	@Inject
	IEventBroker eventBroker;
	private EventHandler itemUpdater = new EventHandler() {
		public void handleEvent(Event event) {
			// Ensure that this event is for a MMenuItem
			if (!(event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MMenuItem))
				return;

			MMenuItem itemModel = (MMenuItem) event
					.getProperty(UIEvents.EventTags.ELEMENT);

			IContributionItem ici = getContribution(itemModel);
			if (ici == null) {
				return;
			}

			String attName = (String) event
					.getProperty(UIEvents.EventTags.ATTNAME);
			if (UIEvents.UILabel.LABEL.equals(attName)) {
				ici.update();
			} else if (UIEvents.UILabel.ICONURI.equals(attName)) {
				ici.update();
			}
		}
	};

	private EventHandler toBeRenderedUpdater = new EventHandler() {
		public void handleEvent(Event event) {
			Object element = event.getProperty(UIEvents.EventTags.ELEMENT);
			String attName = (String) event
					.getProperty(UIEvents.EventTags.ATTNAME);
			if (element instanceof MMenuItem) {
				MMenuItem itemModel = (MMenuItem) element;
				if (UIEvents.UIElement.TOBERENDERED.equals(attName)) {
					Object obj = itemModel.getParent();
					if (!(obj instanceof MMenu)) {
						return;
					}
					MenuManager parent = getManager((MMenu) obj);
					if (itemModel.isToBeRendered()) {
						if (parent != null) {
							modelProcessSwitch(parent, itemModel);
						}
					} else {
						IContributionItem ici = getContribution(itemModel);
						clearModelToContribution(itemModel, ici);
						if (ici != null && parent != null) {
							parent.remove(ici);
						}
						if (ici != null) {
							ici.dispose();
						}
					}
				}
			}
			if (UIEvents.UIElement.VISIBLE.equals(attName)) {
				if (element instanceof MMenu) {
					MMenu menuModel = (MMenu) element;
					MenuManager manager = getManager(menuModel);
					if (manager == null) {
						return;
					}
					manager.setVisible(menuModel.isVisible());
					if (manager.getParent() != null) {
						manager.getParent().markDirty();
					}
				} else if (element instanceof MMenuElement) {
					MMenuElement itemModel = (MMenuElement) element;
					Object obj = getContribution(itemModel);
					if (!(obj instanceof ContributionItem)) {
						return;
					}
					ContributionItem item = (ContributionItem) obj;
					item.setVisible(itemModel.isVisible());
					if (item.getParent() != null) {
						item.getParent().markDirty();
					}
				}
			}
		}
	};

	private EventHandler selectionUpdater = new EventHandler() {
		public void handleEvent(Event event) {
			// Ensure that this event is for a MToolItem
			if (!(event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MMenuItem))
				return;

			MMenuItem itemModel = (MMenuItem) event
					.getProperty(UIEvents.EventTags.ELEMENT);
			IContributionItem ici = getContribution(itemModel);
			if (ici != null) {
				ici.update();
			}
		}
	};

	private EventHandler enabledUpdater = new EventHandler() {
		public void handleEvent(Event event) {
			// Ensure that this event is for a MMenuItem
			if (!(event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MMenuItem))
				return;

			MMenuItem itemModel = (MMenuItem) event
					.getProperty(UIEvents.EventTags.ELEMENT);
			IContributionItem ici = getContribution(itemModel);
			if (ici != null) {
				ici.update();
			}
		}
	};

	private MenuManagerRendererFilter rendererFilter;

	@PostConstruct
	public void init() {
		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.UILabel.TOPIC),
				itemUpdater);
		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.Item.TOPIC,
				UIEvents.Item.SELECTED), selectionUpdater);
		eventBroker
				.subscribe(UIEvents.buildTopic(UIEvents.Item.TOPIC,
						UIEvents.Item.ENABLED), enabledUpdater);
		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.UIElement.TOPIC),
				toBeRenderedUpdater);

		context.set(MenuManagerRenderer.class, this);
		Display display = context.get(Display.class);
		rendererFilter = ContextInjectionFactory.make(
				MenuManagerRendererFilter.class, context);
		display.addFilter(SWT.Show, rendererFilter);
		display.addFilter(SWT.Hide, rendererFilter);
		display.addFilter(SWT.Dispose, rendererFilter);
		context.set(MenuManagerRendererFilter.class, rendererFilter);

	}

	@PreDestroy
	public void contextDisposed() {
		eventBroker.unsubscribe(itemUpdater);
		eventBroker.unsubscribe(selectionUpdater);
		eventBroker.unsubscribe(enabledUpdater);
		eventBroker.unsubscribe(toBeRenderedUpdater);

		context.remove(MenuManagerRendererFilter.class);
		Display display = context.get(Display.class);
		if (display != null && !display.isDisposed() && rendererFilter != null) {
			display.removeFilter(SWT.Show, rendererFilter);
			display.removeFilter(SWT.Hide, rendererFilter);
			display.removeFilter(SWT.Dispose, rendererFilter);
		}
		if (rendererFilter != null) {
			ContextInjectionFactory.uninject(rendererFilter, context);
			rendererFilter = null;
		}
		context.remove(MenuManagerRenderer.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer#createWidget
	 * (org.eclipse.e4.ui.model.application.ui.MUIElement, java.lang.Object)
	 */
	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MMenu))
			return null;

		final MMenu menuModel = (MMenu) element;
		Menu newMenu = null;
		MenuManager menuManager = null;
		boolean menuBar = false;

		if (parent instanceof Decorations) {
			MUIElement container = (MUIElement) ((EObject) element)
					.eContainer();
			if (container instanceof MWindow) {
				menuManager = getManager(menuModel);
				if (menuManager == null) {
					menuManager = new MenuManager(NO_LABEL,
							menuModel.getElementId());
					linkModelToManager(menuModel, menuManager);
				}
				newMenu = menuManager.createMenuBar((Decorations) parent);
				((Decorations) parent).setMenuBar(newMenu);
				newMenu.setData(menuManager);
				menuBar = true;
			} else {
				menuManager = getManager(menuModel);
				if (menuManager == null) {
					menuManager = new MenuManager(NO_LABEL,
							menuModel.getElementId());
					linkModelToManager(menuModel, menuManager);
				}
				newMenu = menuManager.createContextMenu((Control) parent);
				// we can't be sure this is the correct parent.
				// ((Control) parent).setMenu(newMenu);
				newMenu.setData(menuManager);
			}
		} else if (parent instanceof Menu) {
			// Object data = ((Menu) parent).getData();
			logger.debug(new Exception(), "Trying to render a sub menu " //$NON-NLS-1$
					+ menuModel + "\n\t" + parent); //$NON-NLS-1$
			return null;

		} else if (parent instanceof Control) {
			menuManager = getManager(menuModel);
			if (menuManager == null) {
				menuManager = new MenuManager(NO_LABEL,
						menuModel.getElementId());
				linkModelToManager(menuModel, menuManager);
			}
			newMenu = menuManager.createContextMenu((Control) parent);
			// we can't be sure this is the correct parent.
			// ((Control) parent).setMenu(newMenu);
			newMenu.setData(menuManager);
		}
		if (!menuManager.getRemoveAllWhenShown()) {
			processContributions(menuModel, menuBar,
					menuModel instanceof MPopupMenu);
		}
		if (newMenu != null) {
			newMenu.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					cleanUp(menuModel);
				}
			});
		}
		return newMenu;
	}

	/**
	 * @param menuModel
	 */
	public void cleanUp(MMenu menuModel) {
		Collection<ContributionRecord> vals = modelContributionToRecord
				.values();
		for (ContributionRecord record : vals
				.toArray(new ContributionRecord[vals.size()])) {
			if (record.menuModel == menuModel) {
				record.dispose();
				for (MMenuElement copy : record.generatedElements) {
					cleanUpCopy(record, copy);
				}
				for (MMenuElement copy : record.sharedElements) {
					cleanUpCopy(record, copy);
				}
				record.generatedElements.clear();
				record.sharedElements.clear();
			}
		}
	}

	public void cleanUpCopy(ContributionRecord record, MMenuElement copy) {
		modelContributionToRecord.remove(copy);
		if (copy instanceof MMenu) {
			MMenu menuCopy = (MMenu) copy;
			cleanUp(menuCopy);
			MenuManager copyManager = getManager(menuCopy);
			clearModelToManager(menuCopy, copyManager);
			if (copyManager != null) {
				record.getManagerForModel().remove(copyManager);
				copyManager.dispose();
			}
		} else {
			IContributionItem ici = getContribution(copy);
			clearModelToContribution(copy, ici);
			if (ici != null) {
				record.getManagerForModel().remove(ici);
			}
		}
	}

	/**
	 * @param menuModel
	 * @param isMenuBar
	 * @param isPopup
	 */
	public void processContributions(MMenu menuModel, boolean isMenuBar,
			boolean isPopup) {
		if (menuModel.getElementId() == null) {
			return;
		}
		final ArrayList<MMenuContribution> toContribute = new ArrayList<MMenuContribution>();
		ContributionsAnalyzer.XXXgatherMenuContributions(menuModel,
				application.getMenuContributions(), menuModel.getElementId(),
				toContribute, null, isPopup);
		generateContributions(menuModel, toContribute, isMenuBar);
		for (MMenuElement element : menuModel.getChildren()) {
			if (element instanceof MMenu) {
				processContributions((MMenu) element, false, isPopup);
			}
		}
	}

	/**
	 * @param menuModel
	 * @param toContribute
	 */
	private void generateContributions(MMenu menuModel,
			ArrayList<MMenuContribution> toContribute, boolean menuBar) {
		HashSet<String> existingMenuIds = new HashSet<String>();
		HashSet<String> existingSeparatorNames = new HashSet<String>();
		for (MMenuElement child : menuModel.getChildren()) {
			String elementId = child.getElementId();
			if (child instanceof MMenu && elementId != null) {
				existingMenuIds.add(elementId);
			} else if (child instanceof MMenuSeparator && elementId != null) {
				existingSeparatorNames.add(elementId);
			}
		}

		MenuManager manager = getManager(menuModel);
		boolean done = toContribute.size() == 0;
		while (!done) {
			ArrayList<MMenuContribution> curList = new ArrayList<MMenuContribution>(
					toContribute);
			int retryCount = toContribute.size();
			toContribute.clear();

			for (MMenuContribution menuContribution : curList) {
				if (!processAddition(menuModel, manager, menuContribution,
						existingMenuIds, existingSeparatorNames, menuBar)) {
					toContribute.add(menuContribution);
				}
			}

			// We're done if the retryList is now empty (everything done) or
			// if the list hasn't changed at all (no hope)
			done = (toContribute.size() == 0)
					|| (toContribute.size() == retryCount);
		}
	}

	/**
	 * @param menuModel
	 * @param manager
	 * @param menuContribution
	 * @return true if the menuContribution was processed
	 */
	private boolean processAddition(MMenu menuModel, final MenuManager manager,
			MMenuContribution menuContribution,
			final HashSet<String> existingMenuIds,
			HashSet<String> existingSeparatorNames, boolean menuBar) {
		final ContributionRecord record = new ContributionRecord(menuModel,
				menuContribution, this);
		if (!record.mergeIntoModel()) {
			return false;
		}
		if (menuBar) {
			final IEclipseContext parentContext = modelService
					.getContainingContext(menuModel);
			parentContext.runAndTrack(new RunAndTrack() {
				@Override
				public boolean changed(IEclipseContext context) {
					record.updateVisibility(parentContext.getActiveLeaf());
					manager.update(true);
					return true;
				}
			});
		}
		return true;
	}

	public ArrayList<ContributionRecord> getList(MMenuElement item) {
		ArrayList<ContributionRecord> tmp = sharedElementToRecord.get(item);
		if (tmp == null) {
			tmp = new ArrayList<ContributionRecord>();
			sharedElementToRecord.put(item, tmp);
		}
		return tmp;
	}

	void removeMenuContributions(final MMenu menuModel,
			final ArrayList<MMenuElement> menuContributionsToRemove) {
		for (MMenuElement item : menuContributionsToRemove) {
			menuModel.getChildren().remove(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer#processContents
	 * (org.eclipse.e4.ui.model.application.ui.MElementContainer)
	 */
	@Override
	public void processContents(MElementContainer<MUIElement> container) {
		// I can either simply stop processing, or we can walk the model
		// ourselves like the "old" days
		// EMF gives us null lists if empty
		if (container == null)
			return;

		// this is in direct violation of good programming
		MenuManager parentManager = getManager((MMenu) ((Object) container));
		if (parentManager == null) {
			return;
		}
		// Process any contents of the newly created ME
		List<MUIElement> parts = container.getChildren();
		if (parts != null) {
			MUIElement[] plist = parts.toArray(new MUIElement[parts.size()]);
			for (int i = 0; i < plist.length; i++) {
				MUIElement childME = plist[i];
				modelProcessSwitch(parentManager, (MMenuElement) childME);
			}
		}
		parentManager.update(false);
	}

	private void addToManager(MenuManager parentManager, MMenuElement model,
			IContributionItem menuManager) {
		MElementContainer<MUIElement> parent = model.getParent();
		// technically this shouldn't happen
		if (parent == null) {
			parentManager.add(menuManager);
		} else {
			int index = parent.getChildren().indexOf(model);
			// shouldn't be -1, but better safe than sorry
			if (index > parentManager.getSize() || index == -1) {
				parentManager.add(menuManager);
			} else {
				parentManager.insert(index, menuManager);
			}
		}
	}

	/**
	 * @param parentManager
	 * @param menuModel
	 */
	private void processMenu(MenuManager parentManager, MMenu menuModel) {
		MenuManager menuManager = getManager(menuModel);
		if (menuManager == null) {
			String menuText = getText(menuModel);
			ImageDescriptor desc = getImageDescriptor(menuModel);
			menuManager = new MenuManager(menuText, desc,
					menuModel.getElementId());
			linkModelToManager(menuModel, menuManager);
			menuManager.setVisible(menuModel.isVisible());
			addToManager(parentManager, menuModel, menuManager);
		}
		// processContributions(menuModel, false);
		List<MMenuElement> parts = menuModel.getChildren();
		if (parts != null) {
			MMenuElement[] plist = parts
					.toArray(new MMenuElement[parts.size()]);
			for (int i = 0; i < plist.length; i++) {
				MMenuElement childME = plist[i];
				modelProcessSwitch(menuManager, childME);
			}
		}
	}

	/**
	 * @param menuManager
	 * @param childME
	 */
	void modelProcessSwitch(MenuManager menuManager, MMenuElement childME) {
		if (!childME.isToBeRendered()) {
			return;
		}
		if (childME instanceof MRenderedMenuItem) {
			MRenderedMenuItem itemModel = (MRenderedMenuItem) childME;
			processRenderedItem(menuManager, itemModel);
		} else if (childME instanceof MHandledMenuItem) {
			MHandledMenuItem itemModel = (MHandledMenuItem) childME;
			processHandledItem(menuManager, itemModel);
		} else if (childME instanceof MDirectMenuItem) {
			MDirectMenuItem itemModel = (MDirectMenuItem) childME;
			processDirectItem(menuManager, itemModel, null);
		} else if (childME instanceof MMenuSeparator) {
			MMenuSeparator sep = (MMenuSeparator) childME;
			processSeparator(menuManager, sep);
			// } else if (childME instanceof MOpaqueMenu) {
			// I'm not sure what to do here
			// so I'll just take it out of the running
		} else if (childME instanceof MMenu) {
			MMenu itemModel = (MMenu) childME;
			processMenu(menuManager, itemModel);
		}
	}

	/**
	 * @param parentManager
	 * @param itemModel
	 */
	void processRenderedItem(MenuManager parentManager,
			MRenderedMenuItem itemModel) {
		IContributionItem ici = getContribution(itemModel);
		if (ici != null) {
			return;
		}
		Object obj = itemModel.getContributionItem();
		if (obj instanceof IContextFunction) {
			final IEclipseContext lclContext = getContext(itemModel);
			ici = (IContributionItem) ((IContextFunction) obj)
					.compute(lclContext);
			itemModel.setContributionItem(ici);
		} else if (obj instanceof IContributionItem) {
			ici = (IContributionItem) obj;
		} else {
			// TODO potentially log the state, we've got something we're not
			// happy with
			return;
		}
		ici.setVisible(itemModel.isVisible());
		addToManager(parentManager, itemModel, ici);
		linkModelToContribution(itemModel, ici);
	}

	/**
	 * @param menuManager
	 * @param itemModel
	 */
	private void processSeparator(MenuManager menuManager,
			MMenuSeparator itemModel) {
		IContributionItem ici = getContribution(itemModel);
		if (ici != null) {
			return;
		}
		AbstractGroupMarker marker = null;
		if (itemModel.getTags().contains(GROUP_MARKER)
				|| !itemModel.isVisible()) {
			if (itemModel.getElementId() != null) {
				marker = new GroupMarker(itemModel.getElementId());
			}
		} else {
			marker = new Separator();
			marker.setId(itemModel.getElementId());
		}
		if (marker == null) {
			return;
		}
		addToManager(menuManager, itemModel, marker);
		linkModelToContribution(itemModel, marker);
	}

	/**
	 * @param parentManager
	 * @param itemModel
	 * @param id
	 */
	void processDirectItem(MenuManager parentManager,
			MDirectMenuItem itemModel, String id) {
		IContributionItem ici = getContribution(itemModel);
		if (ici != null) {
			return;
		}
		final IEclipseContext lclContext = getContext(itemModel);
		DirectContributionItem ci = ContextInjectionFactory.make(
				DirectContributionItem.class, lclContext);
		ci.setModel(itemModel);
		ci.setVisible(itemModel.isVisible());
		addToManager(parentManager, itemModel, ci);
		linkModelToContribution(itemModel, ci);
	}

	/**
	 * @param parentManager
	 * @param itemModel
	 */
	void processHandledItem(MenuManager parentManager,
			MHandledMenuItem itemModel) {
		IContributionItem ici = getContribution(itemModel);
		if (ici != null) {
			return;
		}
		final IEclipseContext lclContext = getContext(itemModel);
		HandledContributionItem ci = ContextInjectionFactory.make(
				HandledContributionItem.class, lclContext);
		ci.setModel(itemModel);
		ci.setVisible(itemModel.isVisible());
		addToManager(parentManager, itemModel, ci);
		linkModelToContribution(itemModel, ci);
	}

	private String getText(MMenu menuModel) {
		String text = menuModel.getLocalizedLabel();
		if (text == null || text.length() == 0) {
			return NO_LABEL;
		}
		return text;
	}

	private ImageDescriptor getImageDescriptor(MUILabel element) {
		IEclipseContext localContext = context;
		String iconURI = element.getIconURI();
		if (iconURI != null && iconURI.length() > 0) {
			ISWTResourceUtilities resUtils = (ISWTResourceUtilities) localContext
					.get(IResourceUtilities.class.getName());
			return resUtils.imageDescriptorFromURI(URI.createURI(iconURI));
		}
		return null;
	}

	public MenuManager getManager(MMenu model) {
		return modelToManager.get(model);
	}

	public MMenu getMenuModel(MenuManager manager) {
		return managerToModel.get(manager);
	}

	public void linkModelToManager(MMenu model, MenuManager manager) {
		modelToManager.put(model, manager);
		managerToModel.put(manager, model);
	}

	public void clearModelToManager(MMenu model, MenuManager manager) {
		modelToManager.remove(model);
		managerToModel.remove(manager);
	}

	public IContributionItem getContribution(MMenuElement model) {
		return modelToContribution.get(model);
	}

	public MMenuElement getMenuElement(IContributionItem item) {
		return contributionToModel.get(item);
	}

	public void linkModelToContribution(MMenuElement model,
			IContributionItem item) {
		modelToContribution.put(model, item);
		contributionToModel.put(item, model);
	}

	public void clearModelToContribution(MMenuElement model,
			IContributionItem item) {
		modelToContribution.remove(model);
		contributionToModel.remove(item);
	}

	public ContributionRecord getContributionRecord(MMenuElement element) {
		return modelContributionToRecord.get(element);
	}

	public void linkElementToContributionRecord(MMenuElement element,
			ContributionRecord record) {
		modelContributionToRecord.put(element, record);
	}

	/**
	 * @param menuManager
	 * @param menuModel
	 */
	public void reconcileManagerToModel(MenuManager menuManager, MMenu menuModel) {
		List<MMenuElement> modelChildren = menuModel.getChildren();

		HashSet<MOpaqueMenuItem> oldModelItems = new HashSet<MOpaqueMenuItem>();
		HashSet<MOpaqueMenu> oldMenus = new HashSet<MOpaqueMenu>();
		HashSet<MOpaqueMenuSeparator> oldSeps = new HashSet<MOpaqueMenuSeparator>();
		for (MMenuElement itemModel : modelChildren) {
			if (itemModel instanceof MOpaqueMenuSeparator) {
				oldSeps.add((MOpaqueMenuSeparator) itemModel);
			} else if (itemModel instanceof MOpaqueMenuItem) {
				oldModelItems.add((MOpaqueMenuItem) itemModel);
			} else if (itemModel instanceof MOpaqueMenu) {
				oldMenus.add((MOpaqueMenu) itemModel);
			}
		}

		IContributionItem[] items = menuManager.getItems();
		for (int src = 0, dest = 0; src < items.length; src++, dest++) {
			IContributionItem item = items[src];
			if (item instanceof MenuManager) {
				MenuManager childManager = (MenuManager) item;
				MMenu childModel = getMenuModel(childManager);
				if (childModel == null) {
					MMenu legacyModel = MenuFactoryImpl.eINSTANCE
							.createOpaqueMenu();
					legacyModel.setElementId(childManager.getId());
					legacyModel.setVisible(childManager.isVisible());
					linkModelToManager(legacyModel, childManager);
					modelChildren.add(dest, legacyModel);
				} else {
					if (childModel instanceof MOpaqueMenu) {
						oldMenus.remove(childModel);
					}
					if (modelChildren.size() > dest) {
						if (modelChildren.get(dest) != childModel) {
							modelChildren.remove(childModel);
							modelChildren.add(dest, childModel);
						}
					} else {
						modelChildren.add(childModel);
					}
				}
			} else if (item.isSeparator() || item.isGroupMarker()) {
				MMenuElement menuElement = getMenuElement(item);
				if (menuElement == null) {
					MOpaqueMenuSeparator legacySep = MenuFactoryImpl.eINSTANCE
							.createOpaqueMenuSeparator();
					legacySep.setElementId(item.getId());
					legacySep.setVisible(item.isVisible());
					legacySep.setOpaqueItem(item);
					linkModelToContribution(legacySep, item);
					modelChildren.add(dest, legacySep);
				} else if (menuElement instanceof MOpaqueMenuSeparator) {
					MOpaqueMenuSeparator legacySep = (MOpaqueMenuSeparator) menuElement;
					oldSeps.remove(legacySep);
					if (modelChildren.size() > dest) {
						if (modelChildren.get(dest) != legacySep) {
							modelChildren.remove(legacySep);
							modelChildren.add(dest, legacySep);
						}
					} else {
						modelChildren.add(legacySep);
					}
				}
			} else {
				MMenuElement menuElement = getMenuElement(item);
				if (menuElement == null) {
					MOpaqueMenuItem legacyItem = MenuFactoryImpl.eINSTANCE
							.createOpaqueMenuItem();
					legacyItem.setElementId(item.getId());
					legacyItem.setVisible(item.isVisible());
					legacyItem.setOpaqueItem(item);
					linkModelToContribution(legacyItem, item);
					modelChildren.add(dest, legacyItem);
				} else if (menuElement instanceof MOpaqueMenuItem) {
					MOpaqueMenuItem legacyItem = (MOpaqueMenuItem) menuElement;
					oldModelItems.remove(legacyItem);
					if (modelChildren.size() > dest) {
						if (modelChildren.get(dest) != legacyItem) {
							modelChildren.remove(legacyItem);
							modelChildren.add(dest, legacyItem);
						}
					} else {
						modelChildren.add(legacyItem);
					}
				}
			}
		}
		if (!oldModelItems.isEmpty()) {
			modelChildren.removeAll(oldModelItems);
			for (MOpaqueMenuItem model : oldModelItems) {
				clearModelToContribution(model,
						(IContributionItem) model.getOpaqueItem());
			}
		}
		if (!oldMenus.isEmpty()) {
			modelChildren.removeAll(oldMenus);
			for (MOpaqueMenu oldMenu : oldMenus) {
				MenuManager oldManager = getManager(oldMenu);
				clearModelToManager(oldMenu, oldManager);
			}
		}
		if (!oldSeps.isEmpty()) {
			modelChildren.removeAll(oldSeps);
			for (MOpaqueMenuSeparator model : oldSeps) {
				clearModelToContribution(model,
						(IContributionItem) model.getOpaqueItem());
			}
		}
	}

	/**
	 * @param menuManager
	 * @param element
	 * @param evalContext
	 */
	public static void updateVisibility(MenuManager menuManager,
			MMenuElement element, ExpressionContext evalContext) {
		if (!(element.getVisibleWhen() instanceof MCoreExpression)) {
			return;
		}
		boolean val = ContributionsAnalyzer.isVisible(
				(MCoreExpression) element.getVisibleWhen(), evalContext);
		if (val != element.isVisible()) {
			element.setVisible(val);
			menuManager.markDirty();
		}
	}
}
