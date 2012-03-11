/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 */
package org.eclipse.e4.ui.model.application.ui.menu.util;

import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContributions;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MOpaqueToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContributions;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContributions;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuPackageImpl;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.ui.model.application.ui.menu.impl.MenuPackageImpl
 * @generated
 */
public class MenuAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MenuPackageImpl modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MenuAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MenuPackageImpl.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MenuSwitch<Adapter> modelSwitch =
		new MenuSwitch<Adapter>() {
			@Override
			public Adapter caseItem(MItem object) {
				return createItemAdapter();
			}
			@Override
			public Adapter caseHandledItem(MHandledItem object) {
				return createHandledItemAdapter();
			}
			@Override
			public Adapter caseMenuElement(MMenuElement object) {
				return createMenuElementAdapter();
			}
			@Override
			public Adapter caseMenuItem(MMenuItem object) {
				return createMenuItemAdapter();
			}
			@Override
			public Adapter caseMenuSeparator(MMenuSeparator object) {
				return createMenuSeparatorAdapter();
			}
			@Override
			public Adapter caseMenu(MMenu object) {
				return createMenuAdapter();
			}
			@Override
			public Adapter caseMenuContribution(MMenuContribution object) {
				return createMenuContributionAdapter();
			}
			@Override
			public Adapter casePopupMenu(MPopupMenu object) {
				return createPopupMenuAdapter();
			}
			@Override
			public Adapter caseDirectMenuItem(MDirectMenuItem object) {
				return createDirectMenuItemAdapter();
			}
			@Override
			public Adapter caseHandledMenuItem(MHandledMenuItem object) {
				return createHandledMenuItemAdapter();
			}
			@Override
			public Adapter caseToolItem(MToolItem object) {
				return createToolItemAdapter();
			}
			@Override
			public Adapter caseToolBar(MToolBar object) {
				return createToolBarAdapter();
			}
			@Override
			public Adapter caseToolBarElement(MToolBarElement object) {
				return createToolBarElementAdapter();
			}
			@Override
			public Adapter caseToolControl(MToolControl object) {
				return createToolControlAdapter();
			}
			@Override
			public Adapter caseHandledToolItem(MHandledToolItem object) {
				return createHandledToolItemAdapter();
			}
			@Override
			public Adapter caseDirectToolItem(MDirectToolItem object) {
				return createDirectToolItemAdapter();
			}
			@Override
			public Adapter caseToolBarSeparator(MToolBarSeparator object) {
				return createToolBarSeparatorAdapter();
			}
			@Override
			public Adapter caseMenuContributions(MMenuContributions object) {
				return createMenuContributionsAdapter();
			}
			@Override
			public Adapter caseRenderedMenu(MRenderedMenu object) {
				return createRenderedMenuAdapter();
			}
			@Override
			public Adapter caseRenderedToolBar(MRenderedToolBar object) {
				return createRenderedToolBarAdapter();
			}
			@Override
			public Adapter caseToolBarContribution(MToolBarContribution object) {
				return createToolBarContributionAdapter();
			}
			@Override
			public Adapter caseToolBarContributions(MToolBarContributions object) {
				return createToolBarContributionsAdapter();
			}
			@Override
			public Adapter caseTrimContribution(MTrimContribution object) {
				return createTrimContributionAdapter();
			}
			@Override
			public Adapter caseTrimContributions(MTrimContributions object) {
				return createTrimContributionsAdapter();
			}
			@Override
			public Adapter caseRenderedMenuItem(MRenderedMenuItem object) {
				return createRenderedMenuItemAdapter();
			}
			@Override
			public Adapter caseOpaqueToolItem(MOpaqueToolItem object) {
				return createOpaqueToolItemAdapter();
			}
			@Override
			public Adapter caseOpaqueMenuItem(MOpaqueMenuItem object) {
				return createOpaqueMenuItemAdapter();
			}
			@Override
			public Adapter caseOpaqueMenuSeparator(MOpaqueMenuSeparator object) {
				return createOpaqueMenuSeparatorAdapter();
			}
			@Override
			public Adapter caseOpaqueMenu(MOpaqueMenu object) {
				return createOpaqueMenuAdapter();
			}
			@Override
			public Adapter caseApplicationElement(MApplicationElement object) {
				return createApplicationElementAdapter();
			}
			@Override
			public Adapter caseUIElement(MUIElement object) {
				return createUIElementAdapter();
			}
			@Override
			public Adapter caseUILabel(MUILabel object) {
				return createUILabelAdapter();
			}
			@Override
			public <T extends MUIElement> Adapter caseElementContainer(MElementContainer<T> object) {
				return createElementContainerAdapter();
			}
			@Override
			public Adapter caseContext(MContext object) {
				return createContextAdapter();
			}
			@Override
			public Adapter caseContribution(MContribution object) {
				return createContributionAdapter();
			}
			@Override
			public Adapter caseTrimElement(MTrimElement object) {
				return createTrimElementAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MItem <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MItem
	 * @generated
	 */
	public Adapter createItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MHandledItem <em>Handled Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MHandledItem
	 * @generated
	 */
	public Adapter createHandledItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenuElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenuElement
	 * @generated
	 */
	public Adapter createMenuElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenuItem <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenuItem
	 * @generated
	 */
	public Adapter createMenuItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator <em>Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator
	 * @generated
	 */
	public Adapter createMenuSeparatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenu <em>Menu</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenu
	 * @generated
	 */
	public Adapter createMenuAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution <em>Contribution</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution
	 * @generated
	 */
	public Adapter createMenuContributionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu <em>Popup Menu</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu
	 * @generated
	 */
	public Adapter createPopupMenuAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem <em>Direct Menu Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem
	 * @generated
	 */
	public Adapter createDirectMenuItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem <em>Handled Menu Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem
	 * @generated
	 */
	public Adapter createHandledMenuItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolItem <em>Tool Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolItem
	 * @generated
	 */
	public Adapter createToolItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolBar <em>Tool Bar</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolBar
	 * @generated
	 */
	public Adapter createToolBarAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement <em>Tool Bar Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement
	 * @generated
	 */
	public Adapter createToolBarElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolControl <em>Tool Control</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolControl
	 * @generated
	 */
	public Adapter createToolControlAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem <em>Handled Tool Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem
	 * @generated
	 */
	public Adapter createHandledToolItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem <em>Direct Tool Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem
	 * @generated
	 */
	public Adapter createDirectToolItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator <em>Tool Bar Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator
	 * @generated
	 */
	public Adapter createToolBarSeparatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MMenuContributions <em>Contributions</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MMenuContributions
	 * @generated
	 */
	public Adapter createMenuContributionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenu <em>Rendered Menu</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenu
	 * @generated
	 */
	public Adapter createRenderedMenuAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MRenderedToolBar <em>Rendered Tool Bar</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MRenderedToolBar
	 * @generated
	 */
	public Adapter createRenderedToolBarAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution <em>Tool Bar Contribution</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution
	 * @generated
	 */
	public Adapter createToolBarContributionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MToolBarContributions <em>Tool Bar Contributions</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MToolBarContributions
	 * @generated
	 */
	public Adapter createToolBarContributionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution <em>Trim Contribution</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution
	 * @generated
	 */
	public Adapter createTrimContributionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MTrimContributions <em>Trim Contributions</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MTrimContributions
	 * @generated
	 */
	public Adapter createTrimContributionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem <em>Rendered Menu Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem
	 * @generated
	 */
	public Adapter createRenderedMenuItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MOpaqueToolItem <em>Opaque Tool Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MOpaqueToolItem
	 * @generated
	 */
	public Adapter createOpaqueToolItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuItem <em>Opaque Menu Item</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuItem
	 * @generated
	 */
	public Adapter createOpaqueMenuItemAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuSeparator <em>Opaque Menu Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenuSeparator
	 * @generated
	 */
	public Adapter createOpaqueMenuSeparatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenu <em>Opaque Menu</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.menu.MOpaqueMenu
	 * @generated
	 */
	public Adapter createOpaqueMenuAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.MApplicationElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.MApplicationElement
	 * @generated
	 */
	public Adapter createApplicationElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MUIElement <em>UI Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.MUIElement
	 * @generated
	 */
	public Adapter createUIElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MUILabel <em>UI Label</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.MUILabel
	 * @generated
	 */
	public Adapter createUILabelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MElementContainer <em>Element Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.MElementContainer
	 * @generated
	 */
	public Adapter createElementContainerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.MContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.MContext
	 * @generated
	 */
	public Adapter createContextAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.MContribution <em>Contribution</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.MContribution
	 * @generated
	 */
	public Adapter createContributionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.e4.ui.model.application.ui.basic.MTrimElement <em>Trim Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.e4.ui.model.application.ui.basic.MTrimElement
	 * @generated
	 */
	public Adapter createTrimElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MenuAdapterFactory
