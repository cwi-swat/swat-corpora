/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.languages.javascript.jsdi.rhino.request;

import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.jsdi.ThreadReference;
import org.eclipse.e4.languages.javascript.jsdi.request.StepRequest;
import org.eclipse.e4.languages.javascript.jsdi.rhino.ThreadReferenceImpl;
import org.eclipse.e4.languages.javascript.jsdi.rhino.VirtualMachineImpl;

public class StepRequestImpl extends EventRequestImpl implements StepRequest {

	private final ThreadReferenceImpl thread;
	private final int step;

	public StepRequestImpl(VirtualMachineImpl vm, ThreadReference thread,
			int step) {
		super(vm);
		this.thread = (ThreadReferenceImpl) thread;
		this.step = step;
	}

	public int step() {
		return step;
	}

	public ThreadReference thread() {
		return thread;
	}

	public synchronized void setEnabled(boolean enabled) {
		checkDeleted();
		if (this.enabled == enabled)
			return;

		if (enabled) {
			String stepType = null;
			if (step == STEP_INTO)
				stepType = JSONConstants.STEP_IN;
			else if (step == STEP_OVER)
				stepType = JSONConstants.STEP_NEXT;
			else if (step == STEP_OUT)
				stepType = JSONConstants.STEP_OUT;

			if (thread.getStep() != null)
				throw new IllegalStateException("duplicate step"); //$NON-NLS-1$

			thread.setStep(stepType);
		} else {
			thread.setStep(null);
		}
		this.enabled = enabled;
	}
}
