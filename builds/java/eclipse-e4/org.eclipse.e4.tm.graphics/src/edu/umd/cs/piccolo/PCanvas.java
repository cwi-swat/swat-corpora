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
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import edu.umd.cs.piccolo.activities.Timer;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDebug;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PStack;

/**
 * <b>PCanvas</b> is a simple Swing component that can be used to embed
 * Piccolo into a Java Swing application. Canvases view the Piccolo scene graph
 * through a camera. The canvas manages screen updates coming from this camera,
 * and forwards swing mouse and keyboard events to the camera.
 * <P>
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PCanvas implements PComponent {

	public static final String INTERATING_CHANGED_NOTIFICATION = "INTERATING_CHANGED_NOTIFICATION";

	public static PCanvas CURRENT_ZCANVAS = null;

	private PCamera camera;
	private PStack cursorStack;
	private int interacting;
	private int defaultRenderQuality;
	private int animatingRenderQuality;
	private int interactingRenderQuality;
	private PPanEventHandler panEventHandler;
	private PZoomEventHandler zoomEventHandler;
	private boolean paintingImmediately;
	private boolean animatingOnLastPaint;
	private Listener genericListener;
	private PaintListener paintListener;
	private ControlListener controlListener;

	private Composite control;

	public final static String PCANVAS_CONTROL_DATA_KEY = "edu.umd.cs.piccolo.PCanvas";
	
	/**
	 * Construct a canvas with the basic scene graph consisting of a
	 * root, camera, and layer. Event handlers for zooming and panning
	 * are automatically installed.
	 */
	public PCanvas(Composite parent) {
		control = parent;
		// to be able to find the PCanvas for a Composite
		control.setData(PCANVAS_CONTROL_DATA_KEY, this);
		CURRENT_ZCANVAS = this;
		cursorStack = new PStack();
		setCamera(createDefaultCamera());
		installInputSources();		
		setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		setAnimatingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
		setInteractingRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
		setPanEventHandler(new PPanEventHandler());
		setZoomEventHandler(new PZoomEventHandler());
	}

	public void addLayer(PLayer layer) {
		getRoot().addChild(layer);
		getCamera().addLayer(layer);
	}
	
	protected PCamera createDefaultCamera() {
		PRoot r = new PRoot(control.getDisplay());
		PCamera c = new PCamera();
		r.addChild(c); 
		return c;		
	}

	//

	//****************************************************************
	// Basic - Methods for accessing common piccolo nodes.
	//****************************************************************

	/**
	 * Get the pan event handler associated with this canvas. This event handler
	 * is set up to get events from the camera associated with this canvas by 
	 * default.
	 */
	public PPanEventHandler getPanEventHandler() {
		return panEventHandler;
	}

	/**
	 * Set the pan event handler associated with this canvas.
	 * @param handler the new zoom event handler
	 */
	public void setPanEventHandler(PPanEventHandler handler) {
		if(panEventHandler != null) {
			removeInputEventListener(panEventHandler);
		}

		panEventHandler = handler;

		if(panEventHandler != null) {
			addInputEventListener(panEventHandler);
		}
	}

	/**
	 * Get the zoom event handler associated with this canvas. This event handler
	 * is set up to get events from the camera associated with this canvas by 
	 * default.
	 */
	public PZoomEventHandler getZoomEventHandler() {
		return zoomEventHandler;
	}

	/**
	 * Set the zoom event handler associated with this canvas.
	 * @param handler the new zoom event handler
	 */
	public void setZoomEventHandler(PZoomEventHandler handler) {
		if(zoomEventHandler != null) {
			removeInputEventListener(zoomEventHandler);
		}

		zoomEventHandler = handler;

		if(zoomEventHandler != null) {
			addInputEventListener(zoomEventHandler);
		}
	}

	/**
	 * Return the camera associated with this canvas. All input events from this canvas
	 * go through this camera. And this is the camera that paints this canvas.
	 */
	public PCamera getCamera() {
		return camera;
	}

	/**
	 * Set the camera associated with this canvas. All input events from this canvas
	 * go through this camera. And this is the camera that paints this canvas.
	 */
	public void setCamera(PCamera newCamera) {
		if (camera != null) {
			camera.setComponent(null);
		}

		camera = newCamera;

		if (camera != null) {
			camera.setComponent(this);
			org.eclipse.swt.graphics.Rectangle swtRectangle = control.getBounds(); 
			camera.setBounds(new Rectangle(swtRectangle.x, swtRectangle.y, swtRectangle.width, swtRectangle.height));
		}
	}
	public void setBounds(org.eclipse.swt.graphics.Rectangle swtRectangle) {
		control.setBounds(swtRectangle);
		camera.setBounds(new Rectangle(swtRectangle.x, swtRectangle.y, swtRectangle.width, swtRectangle.height));
	}
	
	/**
	 * Return root for this canvas.
	 */
	public PRoot getRoot() {
		return camera.getRoot();
	}

	/**
	 * Return layer for this canvas.
	 */
	public PLayer getLayer() {
		return camera.getLayer(0);
	}

	/**
	 * Add an input listener to the camera associated with this canvas.
	 */ 	
	public void addInputEventListener(PInputEventListener listener) {
		getCamera().addInputEventListener(listener);
	}

	/**
	 * Remove an input listener to the camera associated with this canvas.
	 */ 	
	public void removeInputEventListener(PInputEventListener listener) {
		getCamera().removeInputEventListener(listener);
	}

	//****************************************************************
	// Painting
	//****************************************************************

	/**
	 * Return true if this canvas has been marked as interacting. If so
	 * the canvas will normally render at a lower quality that is faster.
	 */
	public boolean getInteracting() {
		return interacting > 0;
	}

	/**
	 * Return true if any activities that respond with true to the method
	 * isAnimating were run in the last PRoot.processInputs() loop. This
	 * values is used by this canvas to determine the render quality
	 * to use for the next paint.
	 */
	public boolean getAnimating() {
		return getRoot().getActivityScheduler().getAnimating();
	}

	/**
	 * Set if this canvas is interacting. If so the canvas will normally 
	 * render at a lower quality that is faster. Also repaints the canvas if the
	 * render quality should change.
	 */
	public void setInteracting(boolean isInteracting) {
		boolean wasInteracting = getInteracting();

		if (isInteracting) {
			interacting++;
		} else {
			interacting--;
		}

		if (!getInteracting()) { // determine next render quality and repaint if it's greater then the old
			// interacting render quality.
			int nextRenderQuality = defaultRenderQuality;
			if (getAnimating()) nextRenderQuality = animatingRenderQuality;
			if (nextRenderQuality > interactingRenderQuality) {
				control.redraw();
			}
		}

		isInteracting = getInteracting();

		if (wasInteracting != isInteracting) {
			//TODO
			//			firePropertyChange(INTERATING_CHANGED_NOTIFICATION, wasInteracting, isInteracting);
		}
	}

	/**
	 * Set the render quality that should be used when rendering this canvas
	 * when it is not interacting or animating. The default value is
	 * PPaintContext. HIGH_QUALITY_RENDERING.
	 * 
	 * @param requestedQuality supports PPaintContext.HIGH_QUALITY_RENDERING or PPaintContext.LOW_QUALITY_RENDERING
	 */
	public void setDefaultRenderQuality(int requestedQuality) {
		defaultRenderQuality = requestedQuality;
		control.redraw();
	}

	/**
	 * Set the render quality that should be used when rendering this canvas
	 * when it is animating. The default value is PPaintContext.LOW_QUALITY_RENDERING.
	 * 
	 * @param requestedQuality supports PPaintContext.HIGH_QUALITY_RENDERING or PPaintContext.LOW_QUALITY_RENDERING
	 */
	public void setAnimatingRenderQuality(int requestedQuality) {
		animatingRenderQuality = requestedQuality;
		if (getAnimating()) control.redraw();
	}

	/**
	 * Set the render quality that should be used when rendering this canvas
	 * when it is interacting. The default value is PPaintContext.LOW_QUALITY_RENDERING.
	 * 
	 * @param requestedQuality supports PPaintContext.HIGH_QUALITY_RENDERING or PPaintContext.LOW_QUALITY_RENDERING
	 */
	public void setInteractingRenderQuality(int requestedQuality) {
		interactingRenderQuality = requestedQuality;
		if (getInteracting()) control.redraw();
	}

	/**
	 * Set the canvas cursor, and remember the previous cursor on the
	 * cursor stack.
	 */ 
	public void pushCursor(Cursor cursor) {
		cursorStack.push(control.getCursor());
		control.setCursor(cursor);
	}

	/**
	 * Pop the cursor on top of the cursorStack and set it as the 
	 * canvas cursor.
	 */ 
	public void popCursor() {
		control.setCursor((Cursor)cursorStack.pop());
	}	

	//****************************************************************
	// Code to manage connection to Swing. There appears to be a bug in
	// swing where it will occasionally send to many mouse pressed or mouse
	// released events. Below we attempt to filter out those cases before
	// they get delivered to the Piccolo framework.
	//****************************************************************	

	private boolean isButton1Pressed;
	private boolean isButton2Pressed;
	private boolean isButton3Pressed;	

	private int[] swtEventTypes = {
			SWT.KeyDown,
			SWT.KeyUp,
			SWT.MouseDown,
			SWT.MouseUp,
			SWT.MouseMove,
//			SWT.MouseHover,
//			SWT.MouseDoubleClick,
			SWT.MouseEnter,
			SWT.MouseExit,
			SWT.MouseWheel,
			SWT.FocusIn,
			SWT.FocusOut,
	};

	/**
	 * This method installs mouse and key listeners on the canvas that forward
	 * those events to piccolo.
	 */
	protected void installInputSources() {

		if (genericListener == null) {
			genericListener = new Listener(){

				public void handleEvent(Event event) {
					if (! control.isEnabled()) {
						return;
					}
//					System.err.println("PCanvas: handleEvent " + event + " + stateMask=" + event.stateMask + " button=" + event.button + " type=" + Events.getEventTypeName(event.type));
					
					// fix stateMask to include mouse button that generated the event
					if (event.type == SWT.MouseDown) {
						if (event.button == 1) {
							event.stateMask |= SWT.BUTTON1;
						} else if (event.button == 2) {
							event.stateMask |= SWT.BUTTON2;
						} else if (event.button == 3) {
							event.stateMask |= SWT.BUTTON3;
						}
					}
					
					if (event.type == SWT.MouseEnter) {
						Event simulated = createUntypedEvent(event);
						simulated.type = SWT.MouseMove;

						sendInputEventToInputManager(event, SWT.MouseEnter);
						sendInputEventToInputManager(simulated, simulated.type);
					} else if (event.type == SWT.MouseExit) {
						Event simulated = createUntypedEvent(event);
						simulated.type = SWT.MouseMove;

						sendInputEventToInputManager(simulated, simulated.type);
						sendInputEventToInputManager(event, SWT.MouseExit);
					} else if (event.type == SWT.MouseDown) {
						control.setFocus(); // requestFocus();

						boolean shouldBalanceEvent = false;

						//					if (e.getButton() == MouseEvent.NOBUTTON) {
						//						if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
						//							e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_PRESSED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON1);
						//						}
						//						else if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == MouseEvent.BUTTON2_MASK) {
						//							e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_PRESSED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON2);
						//						}
						//						else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
						//							e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_PRESSED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON3);
						//						}					
						//					}
						if ((event.stateMask & SWT.BUTTON1) != 0) {
							if (isButton1Pressed) {
								shouldBalanceEvent = true;
							}
							isButton1Pressed = true;
						} else if ((event.stateMask & SWT.BUTTON2) != 0) {
							if (isButton2Pressed) {
								shouldBalanceEvent = true;
							}
							isButton2Pressed = true;
						} else if ((event.stateMask & SWT.BUTTON3) != 0) {
							if (isButton3Pressed) {
								shouldBalanceEvent = true;
							}
							isButton3Pressed = true;
						}

						if (shouldBalanceEvent) {
							Event balanceEvent = createUntypedEvent(event);
							balanceEvent.type = SWT.MouseUp;
							sendInputEventToInputManager(balanceEvent, SWT.MouseUp);
						}

						sendInputEventToInputManager(event, SWT.MouseDown);
					} else if (event.type == SWT.MouseUp) {
						boolean shouldBalanceEvent = false;

						//						if (e.getButton() == MouseEvent.NOBUTTON) {
						//							if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
						//								e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_RELEASED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON1);
						//							}
						//							else if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == MouseEvent.BUTTON2_MASK) {
						//								e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_RELEASED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON2);
						//							}
						//							else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
						//								e = new MouseEvent((Component)e.getSource(),MouseEvent.MOUSE_RELEASED,e.getWhen(),e.getModifiers(),e.getX(),e.getY(),e.getClickCount(),e.isPopupTrigger(),MouseEvent.BUTTON3);
						//							}					
						//						}

						if ((event.stateMask & SWT.BUTTON1) != 0) {
							if (isButton1Pressed) {
								shouldBalanceEvent = true;
							}
							isButton1Pressed = false;
						} else if ((event.stateMask & SWT.BUTTON2) != 0) {
							if (isButton2Pressed) {
								shouldBalanceEvent = true;
							}
							isButton2Pressed = false;
						} else if ((event.stateMask & SWT.BUTTON3) != 0) {
							if (isButton3Pressed) {
								shouldBalanceEvent = true;
							}
							isButton3Pressed = false;
						}

						if (shouldBalanceEvent) {
							Event balanceEvent = createUntypedEvent(event);
							balanceEvent.type = SWT.MouseDown;
							sendInputEventToInputManager(balanceEvent, SWT.MouseDown);
						}

						sendInputEventToInputManager(event, SWT.MouseUp);
					} else if (event.type == SWT.MouseMove) {
						if (isButton1Pressed | isButton2Pressed | isButton3Pressed) {
							event.type = Events.MouseDrag;
						}
						sendInputEventToInputManager(event, event.type);
					} else {
						sendInputEventToInputManager(event, event.type);
					}
				}
			};
			for (int i = 0; i < swtEventTypes.length; i++) {
				control.addListener(swtEventTypes[i], genericListener);
			}
		}

		if (paintListener == null) {
			paintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					paintComponent(e.gc);
				}
			};
			control.addPaintListener(paintListener);
		}
		if (controlListener == null) {
			controlListener = new ControlListener() {
				public void controlMoved(ControlEvent e) {
				}
				public void controlResized(ControlEvent e) {
					Point size = control.getSize();
					camera.setBounds(camera.getX(), camera.getY(), size.x, size.y);
				}
			};
			control.addControlListener(controlListener);
		}
	}		

	/**
	 * This method removes mouse and key listeners on the canvas that forward
	 * those events to piccolo.
	 */
	protected void removeInputSources() {
		if (controlListener != null) {
			control.removeControlListener(controlListener);
			controlListener = null;
		}
		if (paintListener != null) {
			control.removePaintListener(paintListener);
			paintListener = null;
		}
		if (genericListener != null) {
			for (int i = 0; i < swtEventTypes.length; i++) {
				control.removeListener(swtEventTypes[i], genericListener);
			}
			genericListener = null;
		}
	}

	private Event createUntypedEvent(Event e) {
		Event untypedEvent = new Event();
		untypedEvent.display = e.display;
		untypedEvent.widget = e.widget;
		untypedEvent.time = e.time;
		untypedEvent.type = e.type;
		untypedEvent.data = e.data;
		untypedEvent.button = e.button;
		untypedEvent.keyCode = e.keyCode;
		untypedEvent.character = e.character;
		untypedEvent.count = e.count;
		untypedEvent.x = e.x;
		untypedEvent.y = e.x;
		untypedEvent.stateMask = e.stateMask;
		return untypedEvent;
	}

	protected void sendInputEventToInputManager(Event e, int type) {
		getRoot().getDefaultInputManager().processEventFromCamera(e, type, getCamera());
	}

	public void repaint(PBounds bounds) {
		PDebug.processRepaint(control.getDisplay());

		bounds.expandNearestIntegerDimensions();
		bounds.inset(-1, -1);

		control.redraw((int)bounds.x, (int)bounds.y, (int)bounds.width, (int)bounds.height, true);
	}

	private void paintComponent(GC g) {
		PDebug.startProcessingOutput();

		GC g2 = g;
		g2.setBackground(control.getBackground());
		g2.fillRectangle(control.getBounds());

		// create new paint context and set render quality to lowest common 
		// denominator render quality.
		PPaintContext paintContext = new PPaintContext(g2);
		if (getInteracting() || getAnimating()) {
			if (interactingRenderQuality < animatingRenderQuality) {
				paintContext.setRenderQuality(interactingRenderQuality);
			} else {
				paintContext.setRenderQuality(animatingRenderQuality);
			}
		} else {
			paintContext.setRenderQuality(defaultRenderQuality);
		}

		// paint piccolo
		camera.fullPaint(paintContext);

		// if switched state from animating to not animating invalidate the entire
		// screen so that it will be drawn with the default instead of animating 
		// render quality.
		if (!getAnimating() && animatingOnLastPaint) {
			control.redraw();
		}
		animatingOnLastPaint = getAnimating();

		PDebug.endProcessingOutput(g2);				
	}		

	public void paintImmediately() {
//		System.out.println("PCanvas: paintImmediately");
		if (paintingImmediately) {
			return;
		}

		paintingImmediately = true;
		//TODO
		//		RepaintManager.currentManager(this).paintDirtyRegions();
		paintingImmediately = false;
	}	

	public Timer createTimer(int delay, Runnable runnable) {
		return new Timer(control.getDisplay(), delay, runnable);
	}		
}