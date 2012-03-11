package edu.umd.cs.piccolo.activities;

import org.eclipse.swt.widgets.Display;


/**
 * @author Lance Good
 */
public class Timer {
	
	private boolean notify = false;

    int     initialDelay, delay;
    boolean repeats = true, coalesce = true;

    Runnable doPostEvent = null;

	Display display = null;

    // These fields are maintained by TimerQueue.
    // eventQueued can also be reset by the TimerQueue, but will only ever
    // happen in applet case when TimerQueues thread is destroyed.
    long    expirationTime;
    Timer   nextTimer;
    boolean running;

    /**
     * DoPostEvent is a runnable class that fires actionEvents to 
     * the listeners on the EventDispatchThread, via invokeLater.
     * @see #post
     */
    class DoPostEvent implements Runnable
    {
        public void run() {

            if(notify) {
                runListener();
                if (coalesce) {
                    cancelEventOverride();
                }
            }
        }

        Timer getTimer() {
            return Timer.this;
        }
    }

    private Runnable listener;
    
    /**
	 * Constructor for SWTTimer.
	 * @param delay
	 * @param listener
	 */
	public Timer(Display display, int delay, Runnable runnable) {
		this.listener = runnable;
        this.delay = delay;
        this.initialDelay = delay;

        doPostEvent = new DoPostEvent();
        this.display = display;
	}

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  
     *
     * @param e the action event to fire
     */
    protected void runListener() {
    	listener.run();
    }

    /**
     * Returns the timer queue.
     */
    TimerQueue timerQueue() {
        return TimerQueue.sharedInstance(display);
    }


    /**
     * Sets the <code>Timer</code>'s delay, the number of milliseconds
     * between successive action events.
     *
     * @param delay the delay in milliseconds
     * @see #setInitialDelay
     */
    public void setDelay(int delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Invalid delay: " + delay);
        }
        else {
            this.delay = delay;
        }
    }


    /**
     * Returns the delay, in milliseconds, 
     * between firings of action events.
     *
     * @see #setDelay
     * @see #getInitialDelay
     */
    public int getDelay() {
        return delay;
    }


    /**
     * Sets the <code>Timer</code>'s initial delay,
     * which by default is the same as the between-event delay.
     * This is used only for the first action event.
     * Subsequent action events are spaced
     * using the delay property.
     * 
     * @param initialDelay the delay, in milliseconds, 
     *                     between the invocation of the <code>start</code>
     *                     method and the first action event
     *                     fired by this timer
     *
     * @see #setDelay
     */
    public void setInitialDelay(int initialDelay) {
        if (initialDelay < 0) {
            throw new IllegalArgumentException("Invalid initial delay: " +
                                               initialDelay);
        }
        else {
            this.initialDelay = initialDelay;
        }
    }


    /**
     * Returns the <code>Timer</code>'s initial delay.
     *
     * @see #setInitialDelay
     * @see #setDelay
     */
    public int getInitialDelay() {
        return initialDelay;
    }


    /**
     * If <code>flag</code> is <code>false</code>,
     * instructs the <code>Timer</code> to send only one
     * action event to its listeners.
     *
     * @param flag specify <code>false</code> to make the timer
     *             stop after sending its first action event
     */
    public void setRepeats(boolean flag) {
        repeats = flag;
    }


    /**
     * Returns <code>true</code> (the default)
     * if the <code>Timer</code> will send
     * an action event 
     * to its listeners multiple times.
     *
     * @see #setRepeats
     */
    public boolean isRepeats() {
        return repeats;
    }


    /**
     * Sets whether the <code>Timer</code> coalesces multiple pending
     * <code>ActionEvent</code> firings.
     * A busy application may not be able
     * to keep up with a <code>Timer</code>'s event generation,
     * causing multiple
     * action events to be queued.  When processed,
     * the application sends these events one after the other, causing the
     * <code>Timer</code>'s listeners to receive a sequence of
     * events with no delay between them. Coalescing avoids this situation
     * by reducing multiple pending events to a single event.
     * <code>Timer</code>s
     * coalesce events by default.
     *
     * @param flag specify <code>false</code> to turn off coalescing
     */
    public void setCoalesce(boolean flag) {
        boolean old = coalesce;
        coalesce = flag;
        if (!old && coalesce) {
            // We must do this as otherwise if the Timer once notified
            // in !coalese mode notify will be stuck to true and never
            // become false.
            cancelEventOverride();
        }
    }


    /**
     * Returns <code>true</code> if the <code>Timer</code> coalesces
     * multiple pending action events.
     *
     * @see #setCoalesce
     */
    public boolean isCoalesce() {
        return coalesce;
    }


    /**
     * Starts the <code>Timer</code>,
     * causing it to start sending action events
     * to its listeners.
     *
     * @see #stop
     */
    public void start() {
        timerQueue().addTimer(this,
                              System.currentTimeMillis() + getInitialDelay());
    }


    /**
     * Returns <code>true</code> if the <code>Timer</code> is running.
     *
     * @see #start
     */
    public boolean isRunning() {
        return timerQueue().containsTimer(this);
    }


    /**
     * Stops the <code>Timer</code>,
     * causing it to stop sending action events
     * to its listeners.
     *
     * @see #start
     */
    public void stop() {
        timerQueue().removeTimer(this);
        cancelEventOverride();
    }


    /**
     * Restarts the <code>Timer</code>,
     * canceling any pending firings and causing
     * it to fire with its initial delay.
     */
    public void restart() {
        stop();
        start();
    }


    /**
     * Resets the internal state to indicate this Timer shouldn't notify
     * any of its listeners. This does not stop a repeatable Timer from
     * firing again, use <code>stop</code> for that.
     */
    synchronized void cancelEventOverride() {
        notify = false;
    }


    synchronized void postOverride() {
        if (notify == false || !coalesce) {
            notify = true;
			display.asyncExec(doPostEvent);
        }
    }
		
}
