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

package org.eclipse.e4.ui.workbench.modeling;

import java.util.List;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

/**
 * @since 1.0
 */
public interface EModelService {
	// Insertion constants
	public static final int ABOVE = 0;
	public static final int BELOW = 1;
	public static final int LEFT_OF = 2;
	public static final int RIGHT_OF = 3;

	// Search modifiers / Location Constants
	public static final int NOT_IN_UI = 0x00;
	public static final int OUTSIDE_PERSPECTIVE = 0x01;
	public static final int IN_ACTIVE_PERSPECTIVE = 0x02;
	public static final int IN_ANY_PERSPECTIVE = 0x04;
	public static final int IN_SHARED_AREA = 0x08;
	public static final int IN_TRIM = 0x10;

	// 'Standard' searches
	public static final int UI_LAYOUT = OUTSIDE_PERSPECTIVE | IN_ACTIVE_PERSPECTIVE
			| IN_SHARED_AREA;
	public static final int PRESENTATION = OUTSIDE_PERSPECTIVE | IN_ACTIVE_PERSPECTIVE
			| IN_SHARED_AREA;
	public static final int ANYWHERE = OUTSIDE_PERSPECTIVE | IN_ANY_PERSPECTIVE | IN_SHARED_AREA
			| IN_TRIM;
	public static final int GLOBAL = OUTSIDE_PERSPECTIVE | IN_SHARED_AREA;

	/**
	 * Return a list of any elements that match the given search criteria. The search is recursive
	 * and includes the specified search root. Any of the search parameters may be specified as
	 * <code>null</code> in which case that field will always 'match'.
	 * <p>
	 * NOTE: This is a generically typed method with the List's generic type expected to be the
	 * value of the 'clazz' parameter. If the 'clazz' parameter is null then the returned list is
	 * untyped but may safely be assigned to List&lt;MUIElement&gt;.
	 * </p>
	 * 
	 * @param <T>
	 *            The generic type of the returned list
	 * @param searchRoot
	 *            The element at which to start the search. This element must be non-null and is
	 *            included in the search.
	 * @param id
	 *            The ID of the element. May be null to omit the test for this field.
	 * @param clazz
	 *            The class specifier determining the 'instanceof' type of the elements to be found.
	 *            If specified then the returned List will be generically specified as being of this
	 *            type.
	 * @param tagsToMatch
	 *            The list of tags to match. All the tags specified in this list must be defined in
	 *            the search element's tags in order to be a match.
	 * @param searchFlags
	 *            A bitwise combination of the following constants:
	 *            <ul>
	 *            <li><b>OUTSIDE_PERSPECTIVE</b> Include the elements in the window's model that are
	 *            not in a perspective</;i>
	 *            <li><b>IN_ANY_PERSPECTIVE</b> Include the elements in all perspectives</;i>
	 *            <li><b>IN_ACTIVE_PERSPECTIVE</b> Include the elements in the currently active
	 *            perspective only</;i>
	 *            <li><b>IN_SHARED_AREA</b> Include the elements in the shared area</;i>
	 *            <li><b>IN_TRIM</b> Include the elements in the window's trim</;i>
	 *            </ul>
	 *            Note that you may omit both perspective flags but still define
	 *            <b>IN_SHARED_AREA</b>; the flags <b>OUTSIDE_PERSPECTIVE | IN_SHARED_AREA</b> for
	 *            example will search the presentation <i>excluding</i> the elements in perspective
	 *            stacks.
	 * @return The generically typed list of matching elements.
	 */
	public <T> List<T> findElements(MUIElement searchRoot, String id, Class<T> clazz,
			List<String> tagsToMatch, int searchFlags);

	/**
	 * Return a list of any elements that match the given search criteria. The search is recursive
	 * and includes the specified search root. Any of the search parameters may be specified as
	 * <code>null</code> in which case that field will always 'match'.
	 * <p>
	 * NOTE: This is a generically typed method with the List's generic type expected to be the
	 * value of the 'clazz' parameter. If the 'clazz' parameter is null then the returned list is
	 * untyped but may safely be assigned to List&lt;MUIElement&gt;.
	 * </p>
	 * 
	 * @param <T>
	 *            The generic type of the returned list
	 * @param searchRoot
	 *            The element at which to start the search. This element must be non-null and is
	 *            included in the search.
	 * @param id
	 *            The ID of the element. May be null to omit the test for this field.
	 * @param clazz
	 *            The class specifier determining the 'instanceof' type of the elements to be found.
	 *            If specified then the returned List will be generically specified as being of this
	 *            type.
	 * @param tagsToMatch
	 *            The list of tags to match. All the tags specified in this list must be defined in
	 *            the search element's tags in order to be a match.
	 * 
	 * @return The generically typed list of matching elements.
	 */
	public <T> List<T> findElements(MUIElement searchRoot, String id, Class<T> clazz,
			List<String> tagsToMatch);

	/**
	 * Returns the first element, recursively searching under the specified search root (inclusive)
	 * 
	 * @param id
	 *            The id to search for, must not be null
	 * @param searchRoot
	 *            The element at which to start the search, must not be null
	 * @return The first element with a matching id or <code>null</code> if one is not found
	 */
	public MUIElement find(String id, MUIElement searchRoot);

	/**
	 * Locate the context that is closest to the given element in the parent hierarchy. It does not
	 * include the context of the supplied element (should it have one).
	 * 
	 * @param element
	 *            the element to locate parent context for
	 * @return the containing context for this element
	 */
	public IEclipseContext getContainingContext(MUIElement element);

	/**
	 * Brings the specified element to the top of its containment structure. If the specified
	 * element is a top-level window, then it will be selected as the application's currently active
	 * window. Otherwise, the element may merely be brought up to be seen by the user but not
	 * necessarily have its containing window become the application's active window.
	 * 
	 * @param element
	 *            The element to bring to the top
	 */
	public void bringToTop(MUIElement element);

	/**
	 * Clones the element, creating a deep copy of its structure.
	 * 
	 * @param element
	 *            The element to clone
	 * @param cloneId
	 *            The id to give the newly coned element
	 * @param saveAsSnippet
	 *            iff <code>true</code> saves the newly created clone into the app's
	 *            'clonableSnippets' list
	 * @return The newly cloned element
	 */
	public MUIElement cloneElement(MUIElement element, String cloneId, boolean saveAsSnippet);

	/**
	 * If a snippet with the given id exists a clone is created and returned. returns
	 * <code>null</code> if no snippet can be found.
	 * 
	 * @param app
	 *            The application whose snippets are to be used
	 * @param snippetId
	 *            The element id of the snippet to clone
	 * @return The cloned snippet or <code>null</code> if no snippet with the given id can be found
	 */
	public MUIElement cloneSnippet(MApplication app, String snippetId);

	/**
	 * Return the count of the children whose 'toBeRendered' flag is true
	 * 
	 * @param element
	 *            The element to test
	 * @return the number of children with 'toBeRendered' == true
	 */
	public int countRenderableChildren(MUIElement element);

	/**
	 * Given a containing MWindow find the MPlaceholder that is currently being used to host the
	 * given element (if any)
	 * 
	 * @param window
	 *            The containing window
	 * @param element
	 *            The element to find the MPlaceholder for
	 * @return the MPlaceholder or null if none is found
	 */
	public MPlaceholder findPlaceholderFor(MWindow window, MUIElement element);

	/**
	 * Move the element to a new location. The element will be placed at the end of the new parent's
	 * list of children.
	 * 
	 * @param element
	 *            The element to move
	 * @param newParent
	 *            The new parent for the element.
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent);

	/**
	 * Move the element to a new location. The element will be placed at the end of the new parent's
	 * list of children. If 'leavePlaceholder is true then an instance of MPlaceholder will be
	 * inserted into the model at the element's original location.
	 * 
	 * @param element
	 *            The element to move
	 * @param newParent
	 *            The new parent for the element.
	 * @param leavePlaceholder
	 *            true if a placeholder for the element should be added
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent,
			boolean leavePlaceholder);

	/**
	 * Move the element to a new location. The element will be placed at the specified index in the
	 * new parent's list of children.
	 * 
	 * @param element
	 *            The element to move
	 * @param newParent
	 *            The new parent for the element.
	 * @param index
	 *            The index to insert the element at; -1 means at the end
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent, int index);

	/**
	 * Move the element to a new location. The element will be placed at the end of the new parent's
	 * list of children.
	 * 
	 * @param element
	 *            The element to move
	 * @param newParent
	 *            The new parent for the element.
	 * @param index
	 *            The index to insert the element at; -1 means at the end
	 * @param leavePlaceholder
	 *            true if a placeholder for the element should be added
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent, int index,
			boolean leavePlaceholder);

	/**
	 * Inserts the given element into the UI Model by either creating a new sash or augmenting an
	 * existing sash if the orientation permits.
	 * 
	 * @param toInsert
	 *            The element to insert
	 * @param relTo
	 *            The element that the new one is to be relative to
	 * @param where
	 *            An SWT constant indicating where the inserted element should be placed
	 * @param ratio
	 *            The percentage of the area to be occupied by the inserted element
	 */
	public void insert(MPartSashContainerElement toInsert, MPartSashContainerElement relTo,
			int where, int ratio);

	/**
	 * Created a separate (detached) window containing the given element.
	 * 
	 * @param mPartSashContainerElement
	 *            The element to detach
	 * @param x
	 *            The X position of the new window
	 * @param y
	 *            The Y position of the new window
	 * @param width
	 *            The Width of the new window
	 * @param height
	 *            The Height of the new window
	 */
	public void detach(MPartSashContainerElement mPartSashContainerElement, int x, int y,
			int width, int height);

	/**
	 * Get the top-level window containing this UI element. A <code>null</code> return value
	 * indicates that the element is not directly contained in the UI model (but may, for example,
	 * be a model snippet hosted in a Dialog...)
	 * 
	 * @param element
	 *            The element to get the window for
	 * 
	 * @return the top-level window containing this UI element. A <code>null</code> return value
	 *         indicates that the element is not directly contained in the UI model (but may, for
	 *         example, be a model snippet hosted in a Dialog...)
	 */
	public MWindow getTopLevelWindowFor(MUIElement element);

	/**
	 * @param element
	 *            The element to get the perspective for
	 * @return The MPerspective containing this element or <code>null</code> if the element is not
	 *         in a perspective
	 */
	public MPerspective getPerspectiveFor(MUIElement element);

	/**
	 * Returns the window's MTrimBar for the specified side. If necessary the bar will be created.
	 * 
	 * @param window
	 *            The window to get the trim bar for
	 * @param sv
	 *            The value for the specified side
	 * 
	 * @return The appropriate trim bar
	 */
	public MTrimBar getTrim(MTrimmedWindow window, SideValue sv);

	/**
	 * Return the active perspective for the given window. This is a convenience method that just
	 * returns the MPerspectiveStack's selectedElement.
	 * 
	 * @param window
	 *            The window to determine the active perspective for.
	 * 
	 * @return The active perspective or <code>null</code> if there is no MPerspectiveStack, it's
	 *         empty or has no selected element.
	 */
	public MPerspective getActivePerspective(MWindow window);

	/**
	 * This is a convenience method that will clean the model of all traces of a given perspective.
	 * There may be elements (i.e. minimized stacks...) in the window's trim that are associated
	 * with a perspective as well as the need to properly clean up any detached windows associated
	 * with the perspective.
	 * 
	 * @param persp
	 *            the perspective to remove
	 * @param window
	 *            the window to remove it from
	 */
	public void resetPerspectiveModel(MPerspective persp, MWindow window);

	/**
	 * Remove the given perspective completely from the model.
	 * 
	 * @param persp
	 *            the perspective to remove
	 * @param window
	 *            the window to remove it from
	 */
	public void removePerspectiveModel(MPerspective persp, MWindow window);

	/**
	 * Count the number of 'toBeRendered' children
	 * 
	 * @param container
	 *            The container to check
	 * @return The number of children whose toBeRendered flag is <code>true</code>
	 * 
	 */
	public int toBeRenderedCount(MElementContainer<?> container);

	/**
	 * Get the container of the given element. This is a convenience method that will always return
	 * the actual container for the element, even where the element's 'getParent' might return null
	 * (trim, detached windows...)
	 * 
	 * @param element
	 *            The element to get the container for
	 * @return The element's container. This may be <code>null</code> if the element being checked
	 *         is a snippet unattached to the UI Model itself.
	 */
	public MUIElement getContainer(MUIElement element);

	/**
	 * Given an element this method responds with information about where the element exists within
	 * the current UI Model. This is used in cases where it is necessary to know if an element is in
	 * the 'shared area' or outside of any perspective.
	 * 
	 * @param element
	 * @return The location of the element in the UI, will be one of:
	 *         <ul>
	 *         <li><b>NOT_IN_UI:</b> The element is not in the UI model at all</li>
	 *         <li><b>OUTSIDE_PERSPECTIVE:</b> The element not within a perspective stack</li>
	 *         <li><b>IN_ACTIVE_PERSPECTIVE:</b> The element is within the currently active
	 *         perspective</li>
	 *         <li><b>IN_ANY_PERSPECTIVE:</b> The element is within a perspective but not the active
	 *         one</li>
	 *         <li><b>IN_SHARED_AREA:</b> The element is within an area that is shared between
	 *         different perspectives</li>
	 *         </ul>
	 */
	public int getElementLocation(MUIElement element);

	/**
	 * This method ensures that there will never be two placeholders for the same referenced element
	 * visible in the presentation at the same time. It does this by hiding placeholders which are
	 * contained in any MPerspective if there is a placeholder for the element in any 'shared' area
	 * (i.e. visible regardless of which perspective is visible) by setting its 'toBeRendered' state
	 * to <code>false</code>.
	 * 
	 * @param window
	 *            The window to modify
	 * @param perspective
	 *            if non-null specifies the specific perspective to modify, otherwise all
	 *            perspectives in the window are checked
	 */
	public void hideLocalPlaceholders(MWindow window, MPerspective perspective);

	/**
	 * Returns <code>true</code> iff the supplied element represents the single visible element in
	 * the shared area. This method is used to test for this condition since (by convention) there
	 * must be at least one stack in the shared area at all times.
	 * 
	 * @param stack
	 *            The element to test
	 * @return <code>true</code> iff the element is the last visible stack
	 */
	public boolean isLastEditorStack(MUIElement stack);
}
