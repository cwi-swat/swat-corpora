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
package org.eclipse.e4.ui.workbench.modeling;

import java.util.Collection;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

/**
 * The part service provides clients with the functionalities of showing and hiding parts. Part
 * events can also be tracked via the part service.
 * <p>
 * It is expected that any methods that are exposed by this service that takes an <code>MPart</code>
 * as an argument be a part that is actually being managed by this service.
 * </p>
 * 
 * @since 1.0
 */
public interface EPartService {

	/**
	 * Applicable states that a part can be in. This will be used in conjunction with
	 * {@link EPartService#showPart(String, PartState)}.
	 */
	public enum PartState {

		/**
		 * Part state that indicates the part should be made visible and activated.
		 */
		ACTIVATE,

		/**
		 * Part state that indicates the part should be made visible though it may not necessarily
		 * be granted focus. If the part will be displayed in the same stack as the currently active
		 * part, then this has the same effect as <code>ACTIVATE</code>.
		 */
		VISIBLE,

		/**
		 * Part state that indicates the part should be created but not necessarily made visible.
		 */
		CREATE
	}

	public static final String PART_SERVICE_ROOT = "partServiceRoot"; //$NON-NLS-1$

	/**
	 * A tag on a part to indicate that it should be removed from the model when it is hidden.
	 * 
	 * @see #hidePart(MPart)
	 */
	public static final String REMOVE_ON_HIDE_TAG = "removeOnHide"; //$NON-NLS-1$

	/**
	 * Adds the given listener for part lifecycle events. Has no effect if an identical listener has
	 * already been registered.
	 * <p>
	 * <b>Note:</b> Listeners should be removed when no longer necessary.
	 * </p>
	 * 
	 * @param listener
	 *            the listener to attach
	 */
	public void addPartListener(IPartListener listener);

	/**
	 * Removes the given listener so that it will no longer be notified of part lifecycle events.
	 * Has no effect if an identical listener has not been registered.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removePartListener(IPartListener listener);

	/**
	 * Activates the given part. The part will be brought to top (if necessary) and granted focus.
	 * 
	 * @param part
	 *            the part to activate, must not be <code>null</code>
	 */
	public void activate(MPart part);

	/**
	 * Activates the given part. The part will be brought to top (if necessary) and, if
	 * {@code requiresFocus} is true, then granted focus.
	 * 
	 * @param part
	 *            the part to activate, must not be <code>null</code>
	 * @param requiresFocus
	 *            if true, then also cause the part to acquire focus
	 */
	public void activate(MPart part, boolean requiresFocus);

	/**
	 * Ask the service to assign activation to a valid part.
	 */
	public void requestActivation();

	/**
	 * Brings this part to the top so that it will become visible to the end user. This does not
	 * imply that the part will be granted focus.
	 * 
	 * @param part
	 *            the part to bring to top
	 */
	public void bringToTop(MPart part);

	/**
	 * Finds and returns a part with the given id.
	 * 
	 * @param id
	 *            the id of the part to search for, must not be <code>null</code>
	 * @return the part with the specified id, or <code>null</code> if no such part could be found
	 */
	public MPart findPart(String id);

	/**
	 * Returns a collection of all the parts that are being managed by this part service.
	 * 
	 * @return a collection of parts that are being managed by this service, never <code>null</code>
	 */
	public Collection<MPart> getParts();

	/**
	 * Returns the active part.
	 * 
	 * @return an active part within the scope of this service, or <code>null</code> if no part is
	 *         currently active
	 */
	public MPart getActivePart();

	/**
	 * Returns whether the specified part is currently visible to the end user.
	 * 
	 * @param part
	 *            the part to check
	 * @return <code>true</code> if the part is currently visible, <code>false</code> otherwise
	 */
	public boolean isPartVisible(MPart part);

	/**
	 * Creates a new part of the given id.
	 * 
	 * @param id
	 *            the identifier of the part, must not be <code>null</code>
	 * @return a new part of the given id, or <code>null</code> if no part descriptors can be found
	 *         that match the specified id
	 */
	public MPart createPart(String id);

	/**
	 * Creates a new placeholder for a part of the given id.
	 * 
	 * @param id
	 *            the identifier of the part, must not be <code>null</code>
	 * @return a new part of the given id, or <code>null</code> if no part descriptors can be found
	 *         that match the specified id
	 */
	public MPlaceholder createSharedPart(String id);

	/**
	 * Creates a new placeholder for a part of the given id.
	 * 
	 * @param id
	 *            the identifier of the part, must not be <code>null</code>
	 * @param force
	 *            <code>true</code> if a new part should be created, <code>false</code> if the
	 *            window should be queried for a shared part first
	 * @return a new part of the given id, or <code>null</code> if no part descriptors can be found
	 *         that match the specified id
	 */
	public MPlaceholder createSharedPart(String id, boolean force);

	/**
	 * Shows a part with the identified by the given id. In the event that there are multiple parts
	 * with the specified id, the client is recommended to use {@link #getParts()} and iterate over
	 * the collection to find the interested part and invoke {@link #showPart(MPart, PartState)} on
	 * it. The behavior of this method is dictated by the supplied state.
	 * <ul>
	 * <li>If <code>ACTIVATE</code> is supplied, then the part is made visible and granted focus.</li>
	 * <li>If <code>VISIBLE</code> is supplied, then the part will be made visible and possibly be
	 * granted focus depending on where it is relative to the active part. If it is in the same
	 * stack as the currently active part, then it will be granted focus.</li>
	 * <li>If <code>CREATE</code> is supplied, then the part will be instantiated though its
	 * contents may not necessarily be visible to the end user. visible to the end user.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param id
	 *            the identifier of the part, must not be <code>null</code>
	 * @param partState
	 *            the desired state of the shown part to be in
	 * @return the shown part, or <code>null</code> if no parts or part descriptors can be found
	 *         that match the specified id
	 */
	public MPart showPart(String id, PartState partState);

	/**
	 * Shows the given part.
	 * <p>
	 * <ul>
	 * <li>If there cannot be multiple parts of this type and a part already exists, the already
	 * existing part will be shown and returned.</li>
	 * <li>If multiple parts of this type is allowed, then the provided part will be shown and
	 * returned</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The behavior of this method is dictated by the supplied state.
	 * <ul>
	 * <li>If <code>ACTIVATE</code> is supplied, then the part is made visible and granted focus.</li>
	 * <li>If <code>VISIBLE</code> is supplied, then the part will be made visible and possibly be
	 * granted focus depending on where it is relative to the active part. If it is in the same
	 * stack as the currently active part, then it will be granted focus.</li>
	 * <li>If <code>CREATE</code> is supplied, then the part will be instantiated though its
	 * contents may not necessarily be visible to the end user. visible to the end user.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param part
	 *            the part to show
	 * @param partState
	 *            the desired state of the shown part to be in
	 * @return the shown part
	 */
	public MPart showPart(MPart part, PartState partState);

	/**
	 * Hides the given part. The part must be a part managed by this service.
	 * <p>
	 * If the part has been tagged with the {@link #REMOVE_ON_HIDE_TAG} tag, it will be removed from
	 * the model when the service hides it.
	 * </p>
	 * 
	 * @param part
	 *            the part to hide
	 */
	public void hidePart(MPart part);

	/**
	 * Hides the given part. The part must be a part managed by this service.
	 * <p>
	 * If <code>force</code> is <code>true</code> or the part has been tagged with the
	 * {@link #REMOVE_ON_HIDE_TAG} tag, it will be removed from the model when the service hides it.
	 * </p>
	 * 
	 * @param part
	 *            the part to hide
	 * @param force
	 *            if the part should be removed from the model regardless of its
	 *            {@link #REMOVE_ON_HIDE_TAG} tag
	 */
	public void hidePart(MPart part, boolean force);

	/**
	 * Returns a collection of all the dirty parts that are being managed by this service.
	 * 
	 * 
	 * @return a collection of dirty parts that are being managed by this service, never
	 *         <code>null</code>
	 */
	public Collection<MPart> getDirtyParts();

	/**
	 * Saves the contents of the part if it is dirty and returns whether the operation completed.
	 * 
	 * @param part
	 *            the part to save
	 * @param confirm
	 *            <code>true</code> if the user should be prompted prior to saving the changes, and
	 *            <code>false</code> to save changes without asking
	 * @return <code>true</code> if the operation completed successfully, <code>false</code> if the
	 *         user canceled the operation or if an error occurred while saving the changes
	 */
	public boolean savePart(MPart part, boolean confirm);

	/**
	 * Saves the contents of all dirty parts and returns whether the operation completed.
	 * 
	 * @param confirm
	 *            <code>true</code> if the user should be prompted prior to saving the changes, and
	 *            <code>false</code> to save changes without asking
	 * @return <code>true</code> if the operation completed successfully, <code>false</code> if the
	 *         user canceled the operation or if an error occurred while saving the changes
	 */
	public boolean saveAll(boolean confirm);

	/**
	 * Returns a collection of all {@link MInputPart} with the inputURI-Attribute set to the given
	 * value
	 * 
	 * @param inputUri
	 *            the input uri to search for, must not be <code>null</code>
	 * @return list of parts or an empty collection
	 * @throws AssertionFailedException
	 *             if null passed as argument
	 */
	public Collection<MInputPart> getInputParts(String inputUri);

	/**
	 * Switch to the specified perspective. It will be selected and brought to top (if necessary).
	 * It may not necessarily be granted focus if there is another active window present.
	 * 
	 * @param perspective
	 *            the perspective to switch to, must not be <code>null</code> and it must be a
	 *            perspective that's being managed by this service
	 * @noreference This method is not intended to be referenced by clients as it may be removed or
	 *              moved to another interface.
	 */
	public void switchPerspective(MPerspective perspective);
}