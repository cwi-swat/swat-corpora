/*
 * Copyright (c) 2002-@year@, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the University of Maryland nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Piccolo was written at the Human-Computer Interaction Laboratory www.cs.umd.edu/hcil by Jesse Grosjean
 * under the supervision of Ben Bederson. The Piccolo website is www.cs.umd.edu/hcil/piccolo.
 */
package edu.umd.cs.piccolo.event;

import org.eclipse.e4.tm.graphics.util.Dimension;
import org.eclipse.e4.tm.graphics.util.Events;
import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Event;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PComponent;
import edu.umd.cs.piccolo.PInputManager;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPickPath;

/**
 * <b>PInputEvent</b> is used to notify PInputEventListeners of keyboard and mouse
 * input. It has methods for normal event properties such as event modifier keys
 * and event canvas location.
 * <P>
 * In addition is has methods to get the mouse position and delta in a variety 
 * of coordinate systems.
 * <P>
 * Last of all it provides access to the dispatch manager that can be queried
 * to find the current mouse over, mouse focus, and keyboard focus.
 * <P>
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PInputEvent {

	private Event inputEvent;
	private PPickPath pickPath;
	private PInputManager inputManager;
	private boolean handled;
		
	public PInputEvent(PInputManager inputManager, Event event) {
		super();
		inputEvent = event;
		this.inputManager = inputManager;
	}

	public void pushCursor(Cursor cursor) {
		PComponent component = getTopCamera().getComponent();
		component.pushCursor(cursor);
	}
	
	public void popCursor() {
		PComponent component = getTopCamera().getComponent();
		component.popCursor();
	}
	
	//****************************************************************
	// Accessing Picked Objects - Methods to access the objects associated
	// with this event.
	// <p> 
	// Cameras can view layers that have 
	// other cameras on them, so events may be arriving through a stack
	// of many cameras. The getCamera() method returns the bottommost
	// camera on that stack. The getTopCamera method returns the topmost
	// camera on that stack, this is also the camera through which the
	// event originated.
	//****************************************************************
	
	/**
	 * Return the bottom most camera that is currently painting. If you are
	 * using internal cameras this may be different then what is returned by
	 * getTopCamera.
	 */
	public PCamera getCamera() {
		return getPath().getBottomCamera();
	}
	
	/**
	 * Return the topmost camera this is painting. This is the camera assocaited
	 * with the PCanvas that requested the current repaint.
	 */
	public PCamera getTopCamera() {
		return getPath().getTopCamera();
	}
	
	/**
	 * Get the canvas associated with the top camera. This is the canvas where the
	 * originating swing event came from.
	 */
	public PComponent getComponent() {
		return getTopCamera().getComponent();
	}
	
	/**
	 * Return the input manager that dispatched this event. You can use this input
	 * manager to find the current mouse focus, mouse over, and key focus nodes.
	 * You can also set a new key focus node.
	 */
	public PInputManager getInputManager() {
		return inputManager;
	}
	
	/**
	 * Return the PPickPath associated with this input event.
	 */
	public PPickPath getPath() {
		return pickPath;
	}
	
	public void setPath(PPickPath path) {
		pickPath = path;
	}
	
	/**
	 * Return the bottom node on the current pickpath, that is the picked node
	 * furthest from the root node.
	 */
	public PNode getPickedNode() {
		return pickPath.getPickedNode();
	}
	
	//****************************************************************
	// Basics
	//****************************************************************
	 
	public int getKeyCode() {
		if (isKeyEvent()) {
			return inputEvent.keyCode;
		}
		throw new IllegalStateException("Can't get keycode from mouse event");
	}

	public char getKeyChar() {
		if (isKeyEvent()) {
			return inputEvent.character;
		}
		throw new IllegalStateException("Can't get keychar from mouse event");
	}
	
	public int getKeyLocation() {
//		if (isKeyEvent()) {
//			KeyEvent e = (KeyEvent) inputEvent;
//			return e.getKeyLocation();
//		}
		throw new IllegalStateException("Can't get keylocation from mouse event");
	}
	
	public boolean isActionKey() {
//		if (isKeyEvent()) {
//			KeyEvent e = (KeyEvent) inputEvent;
//			return e.isActionKey();
//		}
		throw new IllegalStateException("Can't get isActionKey from mouse event");
	}
		
	public int getModifiers() {
		return inputEvent.stateMask;
//		throw new IllegalStateException("Can't get modifiers from focus event");
	}

	public int getModifiersEx() {
//		if (!isFocusEvent()) {
//			return inputEvent.getModifiersEx();
//		}
		throw new IllegalStateException("Can't get modifiers ex from focus event");
	}

	public int getClickCount() {
		if (isMouseEvent()) {
			return inputEvent.count;
		}
		throw new IllegalStateException("Can't get clickcount from key event");
	}
	
	public long getWhen() {
		if (!isFocusEvent()) {
			return inputEvent.time;
		}
		throw new IllegalStateException("Can't get when from focus event");
	}
	
	public boolean isAltDown() {
		try {
			return (getModifiers() & SWT.ALT) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get altdown from focus event");
		}
	}

	public boolean isControlDown() {
		try {
			return (getModifiers() & SWT.CTRL) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get controldown from focus event");
		}
	}

	public boolean isMetaDown() {
		try {
			return (getModifiers() & SWT.MOD4) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get metadown from focus event");
		}
	}

	public boolean isShiftDown() {
		try {
			return (getModifiers() & SWT.SHIFT) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get shiftdown from focus event");
		}
	}

	public boolean isLeftMouseButton() {
		try {
			return (getModifiers() & SWT.BUTTON1) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get isLeftMouseButton from focus event");
		}
	}

	public boolean isMiddleMouseButton() {
		try {
			return (getModifiers() & SWT.BUTTON2) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get isMiddleMouseButton from focus event");
		}
	}

	public boolean isRightMouseButton() {
		try {
			return (getModifiers() & SWT.BUTTON3) != 0;
		} catch (IllegalStateException ise) {
			throw new IllegalStateException("Can't get isRightMouseButton from focus event");
		}
	}

	/**
	 * Return true if another event handler has already handled this event. Event handlers should use
	 * this as a hint before handling the event themselves and possibly reject events that have 
	 * already been handled.
	 */
	public boolean isHandled() {
		return handled;
	}

	/**
	 * Set that this event has been handled by an event handler. This is a relaxed for of consuming events.
	 * The event will continue to get dispatched to event handlers even after it is marked as handled, but
	 * other event handlers that might conflict are expected to ignore events that have already been handled.
	 */
	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public int getButton() {
		if (isMouseEvent()) {
			return inputEvent.button;			
		}
		throw new IllegalStateException("Can't get button from key event");
	}

    public int getWheelRotation() {
//		if (isMouseWheelEvent()) {
//			return ((MouseWheelEvent) inputEvent).getWheelRotation();
//		}
		throw new IllegalStateException("Can't get wheel rotation from non-wheel event");
    }

	public Event getSourceSwtEvent() {
		return inputEvent;
	}

	//****************************************************************
	// Classification - Methods to distinguish between mouse and key
	// events.
	//****************************************************************
	 
	public boolean isKeyEvent() {
		switch (inputEvent.type) {
		case SWT.KeyDown: case SWT.KeyUp:
			return true;
		}
		return false;
	}

	public boolean isMouseEvent() {
		switch (inputEvent.type) {
		case SWT.MouseDoubleClick: case SWT.MouseDown: case SWT.MouseUp: case SWT.MouseEnter: case SWT.MouseExit: case SWT.MouseMove:case Events.MouseDrag: case SWT.MouseHover: case SWT.MouseWheel:
			return true;
		}
		return false;
	}
	
	public boolean isMouseWheelEvent() {
		switch (inputEvent.type) {
		case SWT.MouseWheel:
			return true;
		}
		return false;
	}
	
	public boolean isFocusEvent() {
		return inputEvent == null;
	}
	
	public boolean isMouseEnteredOrMouseExited() {
		switch (inputEvent.type) {
		case SWT.MouseEnter: case SWT.MouseExit:
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not this event is a popup menu trigger event for the
	 * platform. Must not be called if this event isn't a mouse event.
	 * <p><b>Note</b>: Popup menus are triggered differently on different
	 * systems. Therefore, <code>isPopupTrigger</code> should be checked in both
	 * <code>mousePressed</code> and <code>mouseReleased</code> for proper
	 * cross-platform functionality.
	 *
	 * @return boolean, true if this event triggers a popup menu for this
	 * platform
	 * @throws IllegalStateException if this event is not a mouse event
	 */
	public boolean isPopupTrigger() {
		if (isMouseEvent()) {
			return false; // ((MouseEvent) inputEvent).isPopupTrigger();
		}
		throw new IllegalStateException("Can't get clickcount from key event");
	}
		
	//****************************************************************
	// Coordinate Systems - Methods for getting mouse location data 
	// These methods are only designed for use with PInputEvents that 
	// return true to the isMouseEvent method.
	//****************************************************************
	
	/**
	 * Return the mouse position in PCanvas coordinates.
	 */
	public Point getCanvasPosition() {
		return (Point) inputManager.getCurrentCanvasPosition().clone();
	}

	/**
	 * Return the delta between the last and current mouse 
	 * position in PCanvas coordinates.
	 */
	public Dimension getCanvasDelta() {
		Point last = inputManager.getLastCanvasPosition();	
		Point current = inputManager.getCurrentCanvasPosition();
		return new Dimension(current.getX() - last.getX(), current.getY() - last.getY());
	}

	/**
	 * Return the mouse position relative to a given node on the pick path.
	 */
	public Point getPositionRelativeTo(PNode nodeOnPath) {
		Point r = getCanvasPosition();
		return pickPath.canvasToLocal(r, nodeOnPath);
	}
	
	/**
	 * Return the delta between the last and current mouse positions
	 * relative to a given node on the pick path.
	 */
	public Dimension getDeltaRelativeTo(PNode nodeOnPath) {
		Dimension r = getCanvasDelta();
		return (Dimension) pickPath.canvasToLocal(r, nodeOnPath);
	}
	
	/**
	 * Return the mouse position transformed through the view transform of
	 * the bottom camera.
	 */
	public Point getPosition() {
		Point r = getCanvasPosition();
		pickPath.canvasToLocal(r, getCamera());
		return getCamera().localToView(r);
	}

	/**
	 * Return the delta between the last and current mouse positions 
	 * transformed through the view transform of the bottom camera.
	 */
	public Dimension getDelta() {
		Dimension r = getCanvasDelta();
		pickPath.canvasToLocal(r, getCamera());
		return (Dimension) getCamera().localToView(r);
	}
	
	//****************************************************************
	// Debugging - methods for debugging
	//****************************************************************

	/**
	 * Returns a string representation of this object for debugging purposes.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append(super.toString().replaceAll(".*\\.", ""));
		result.append('[');
		if (handled) {
			result.append("handled");
		}
		result.append(']');

		return result.toString();
	}	
}
