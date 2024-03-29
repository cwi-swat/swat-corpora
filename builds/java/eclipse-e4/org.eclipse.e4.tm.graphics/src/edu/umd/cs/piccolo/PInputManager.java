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
package edu.umd.cs.piccolo;

import org.eclipse.e4.tm.graphics.util.Events;
import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPickPath;

/**
 * <b>PInputManager</b> is responsible for dispatching PInputEvents
 * to node's event listeners. Events are dispatched from PRoot's processInputs
 * method.
 * <P>
 * @see edu.umd.cs.piccolo.event.PInputEvent
 * @see PRoot
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PInputManager extends PBasicInputEventHandler implements PRoot.InputSource {

	private Point lastCanvasPosition;
	private Point currentCanvasPosition;

	private Event nextInput;
	private int nextType;
	private PCamera nextInputSource;

	private PPickPath mouseFocus;
	private PPickPath previousMouseFocus;
	private PPickPath mouseOver;
	private PPickPath previousMouseOver;
	private PInputEventListener keyboardFocus;

	private int pressedCount;
	
	public PInputManager() {
		super();
		lastCanvasPosition = new Point();
		currentCanvasPosition = new Point();
	}
		
	//****************************************************************
	// Basic
	//****************************************************************
	 
	/**
	 * Return the node that currently has the keyboard focus. This node
	 * receives the key events.
	 */
	public PInputEventListener getKeyboardFocus() {
		return keyboardFocus;
	}
	
	/**
	 * Set the node that should recive key events.
	 */	
	public void setKeyboardFocus(PInputEventListener eventHandler) {		
		PInputEvent focusEvent = new PInputEvent(this, null);
		
		if (keyboardFocus != null) {
			dispatchEventToListener(focusEvent, SWT.FocusOut, keyboardFocus);
		}
		
		keyboardFocus = eventHandler;
		
		if (keyboardFocus != null) {
			dispatchEventToListener(focusEvent, SWT.FocusIn, keyboardFocus);
		}
	}
	
	/**
	 * Return the node that currently has the mouse focus. This will return
	 * the node that received the current mouse pressed event, or null if the
	 * mouse is not pressed. The mouse focus gets mouse dragged events even
	 * what the mouse is not over the mouse focus.
	 */
	public PPickPath getMouseFocus() {
		return mouseFocus;
	}
	
	public void setMouseFocus(PPickPath path) {
		previousMouseFocus = mouseFocus;
		mouseFocus = path;
	}

	/**
	 * Return the node the the mouse is currently over.
	 */  
	public PPickPath getMouseOver() {
		return mouseOver;
	}
	
	public void setMouseOver(PPickPath path) {
		mouseOver = path;
	}
		
	public Point getLastCanvasPosition() {
		return lastCanvasPosition;
	}	

	public Point getCurrentCanvasPosition() {
		return currentCanvasPosition;
	}	
	
	//****************************************************************
	// Event Handling - Methods for handling events
	// 
	// The dispatch manager updates the focus nodes based on the
	// incoming events, and dispatches those events to the appropriate
	// focus nodes.
	//****************************************************************
	
	public void keyPressed(PInputEvent event) {
		dispatchEventToListener(event, SWT.KeyDown, keyboardFocus);
	}
	
	public void keyReleased(PInputEvent event) {
		dispatchEventToListener(event, SWT.KeyUp, keyboardFocus);
	}
	
//	public void keyTyped(PInputEvent event) {
//		dispatchEventToListener(event, KeyEvent.KEY_TYPED, keyboardFocus);
//	}
	
//	public void mouseClicked(PInputEvent event) {
//		dispatchEventToListener(event, MouseEvent.MOUSE_CLICKED, previousMouseFocus);
//	}
	
	public void mouseWheelRotated(PInputEvent event) {
		setMouseFocus(getMouseOver());
		dispatchEventToListener(event, SWT.MouseWheel, mouseOver);
	}
	
//	public void mouseWheelRotatedByBlock(PInputEvent event) {
//		setMouseFocus(getMouseOver());
//		dispatchEventToListener(event, SWT.MouseWheel, mouseOver);
//	}
	
	public void mouseEntered(PInputEvent event) {
		dispatchEventToListener(event, SWT.MouseEnter, mouseOver);
	}
	
	public void mouseExited(PInputEvent event) {
		dispatchEventToListener(event, SWT.MouseExit, previousMouseOver);
	}
	
	public void mouseMoved(PInputEvent event) {
		checkForMouseEnteredAndExited(event);
		dispatchEventToListener(event, SWT.MouseMove, mouseOver);
	}
	public void mouseDragged(PInputEvent event) {
		checkForMouseEnteredAndExited(event);
		dispatchEventToListener(event, Events.MouseDrag, mouseFocus);
	}
	
	public void mousePressed(PInputEvent event) {		
		if (pressedCount == 0) {
			setMouseFocus(getMouseOver());
		}
		pressedCount++;
		dispatchEventToListener(event, SWT.MouseDown, mouseFocus);
		if (pressedCount < 1 || pressedCount > 3) System.err.println("invalid pressedCount on mouse pressed: " + pressedCount);
	}
	
	public void mouseReleased(PInputEvent event) {
		pressedCount--;
		checkForMouseEnteredAndExited(event);		
		dispatchEventToListener(event, SWT.MouseUp, mouseFocus);
		if (pressedCount == 0) {
			setMouseFocus(null);
		}
		if (pressedCount < 0 || pressedCount > 2) System.err.println("invalid pressedCount on mouse released: " + pressedCount);
	}
	
	protected void checkForMouseEnteredAndExited(PInputEvent event) {		
		PNode c = (mouseOver != null) ? mouseOver.getPickedNode() : null; 
		PNode p = (previousMouseOver != null) ? previousMouseOver.getPickedNode() : null;
		
		if (c != p) {
			dispatchEventToListener(event, SWT.MouseExit, previousMouseOver);
			dispatchEventToListener(event, SWT.MouseEnter, mouseOver);
			previousMouseOver = mouseOver;
		}
	}
		
	//****************************************************************
	// Event Dispatch.
	//****************************************************************

	public void processInput() {
		if (nextInput == null) return;

		PInputEvent e = new PInputEvent(this, nextInput);
//		System.err.println("PInputManager: processInput " + nextInput + " + stateMask=" + nextInput.stateMask + " button=" + nextInput.button + " type=" + Events.getEventTypeName(nextInput.type));
		
		Point newCurrentCanvasPosition = null;
		Point newLastCanvasPosition = null;
		
		if (e.isMouseEvent()) {
			if (e.isMouseEnteredOrMouseExited()) {
				PPickPath aPickPath = nextInputSource.pick(nextInput.x, nextInput.y, 1);
				setMouseOver(aPickPath);
				previousMouseOver = aPickPath;
				newCurrentCanvasPosition = (Point) currentCanvasPosition.clone();
				newLastCanvasPosition = (Point) lastCanvasPosition.clone();
			} else {
				lastCanvasPosition.setLocation(currentCanvasPosition);
				currentCanvasPosition.setLocation(nextInput.x, nextInput.y);						
				PPickPath aPickPath = nextInputSource.pick(currentCanvasPosition.getX(), currentCanvasPosition.getY(), 1);
				setMouseOver(aPickPath);			
			}
		}

		nextInput = null;
		nextInputSource = null;
		
		this.processEvent(e, nextType);
		
		if (newCurrentCanvasPosition != null && newLastCanvasPosition != null) {
			currentCanvasPosition.setLocation(newCurrentCanvasPosition);
			lastCanvasPosition.setLocation(newLastCanvasPosition);
		}
	}

	public void processEventFromCamera(Event event, int type, PCamera camera) {
		// queue input
		nextInput = event;
		nextType = type;
		nextInputSource = camera;
		
		// tell root to process queued inputs
		camera.getRoot().processInputs();
	}
	
	private void dispatchEventToListener(PInputEvent event, int type, PInputEventListener listener) {
		if (listener != null) {
			// clear the handled bit since the same event object is used to send multiple events such as
			// mouseEntered/mouseExited and mouseMove.
			event.setHandled(false); 
			listener.processEvent(event, type);
		}		
	}
}

