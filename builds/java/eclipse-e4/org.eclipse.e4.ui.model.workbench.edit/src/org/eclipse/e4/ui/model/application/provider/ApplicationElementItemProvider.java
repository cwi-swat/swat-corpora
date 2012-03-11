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
package org.eclipse.e4.ui.model.application.provider;


import java.util.Collection;
import java.util.List;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.ui.MUiFactory;
import org.eclipse.e4.ui.model.application.ui.advanced.MAdvancedFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.e4.ui.model.application.MApplicationElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ApplicationElementItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ApplicationElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addElementIdPropertyDescriptor(object);
			addTagsPropertyDescriptor(object);
			addContributorURIPropertyDescriptor(object);
			addTransientDataPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Element Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addElementIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ApplicationElement_elementId_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ApplicationElement_elementId_feature", "_UI_ApplicationElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__ELEMENT_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Tags feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTagsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ApplicationElement_tags_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ApplicationElement_tags_feature", "_UI_ApplicationElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__TAGS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Contributor URI feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContributorURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ApplicationElement_contributorURI_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ApplicationElement_contributorURI_feature", "_UI_ApplicationElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CONTRIBUTOR_URI,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Transient Data feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTransientDataPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ApplicationElement_transientData_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ApplicationElement_transientData_feature", "_UI_ApplicationElement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__TRANSIENT_DATA,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ApplicationElement.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ApplicationElement")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = null; //((MApplicationElement)object).getElementId();
		return label == null || label.length() == 0 ?
			getString("_UI_ApplicationElement_type") : //$NON-NLS-1$
			getString("_UI_ApplicationElement_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(MApplicationElement.class)) {
			case ApplicationPackageImpl.APPLICATION_ELEMENT__ELEMENT_ID:
			case ApplicationPackageImpl.APPLICATION_ELEMENT__TAGS:
			case ApplicationPackageImpl.APPLICATION_ELEMENT__CONTRIBUTOR_URI:
			case ApplicationPackageImpl.APPLICATION_ELEMENT__TRANSIENT_DATA:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ApplicationPackageImpl.APPLICATION_ELEMENT__CLONABLE_SNIPPETS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MApplicationFactory.INSTANCE.createApplication()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MApplicationFactory.INSTANCE.createAddon()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createBindingContext()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createBindingTable()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createCommand()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createCommandParameter()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createHandler()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createKeyBinding()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createParameter()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MCommandsFactory.INSTANCE.createCategory()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MUiFactory.INSTANCE.createCoreExpression()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createMenuSeparator()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createMenu()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createMenuContribution()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createPopupMenu()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createDirectMenuItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createHandledMenuItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createToolBar()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createToolControl()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createHandledToolItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createDirectToolItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createToolBarSeparator()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createRenderedMenu()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createRenderedToolBar()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createToolBarContribution()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createTrimContribution()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createRenderedMenuItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createOpaqueToolItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createOpaqueMenuItem()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createOpaqueMenuSeparator()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MMenuFactory.INSTANCE.createOpaqueMenu()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createPart()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createInputPart()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createPartStack()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createPartSashContainer()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createWindow()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createTrimmedWindow()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MBasicFactory.INSTANCE.createTrimBar()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MAdvancedFactory.INSTANCE.createPlaceholder()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MAdvancedFactory.INSTANCE.createPerspective()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MAdvancedFactory.INSTANCE.createPerspectiveStack()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 MAdvancedFactory.INSTANCE.createArea()));

		newChildDescriptors.add
			(createChildParameter
				(ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__CLONABLE_SNIPPETS,
				 org.eclipse.e4.ui.model.application.descriptor.basic.MBasicFactory.INSTANCE.createPartDescriptor()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return UIElementsEditPlugin.INSTANCE;
	}

}
