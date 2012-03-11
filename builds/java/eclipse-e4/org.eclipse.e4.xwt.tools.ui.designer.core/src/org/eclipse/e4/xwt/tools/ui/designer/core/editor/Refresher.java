/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.core.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.RefreshContext;
import org.eclipse.e4.xwt.tools.ui.designer.core.parts.VisualEditPart;
import org.eclipse.e4.xwt.tools.ui.designer.core.util.DisplayUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.widgets.Display;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public class Refresher {
	static int TIMEOUT = 300;

	private Display display;
	private InternalJob job;

	public Refresher(Display uiDisplay) {
		this.display = uiDisplay;
	}

	public void refresh(EditPart editPart, RefreshContext context) {
		if (editPart == null) {
			return;
		}
		try {
			if (editPart instanceof VisualEditPart) {
				((VisualEditPart) editPart).refresh(context);
			} 
			else {
				editPart.refresh();
			}
			List<?> children = editPart.getChildren();
			for (Object object : children) {
				refresh((EditPart) object, context);
			}
		} catch (Exception e) {
		}
	}

	public void refreshAsynchronous(final EditPart editPart) {
		if (editPart == null) {
			return;
		}
		DisplayUtil.asyncExec(getDisplay(), new Runnable() {
			public void run() {
				refresh(editPart, RefreshContext.ALL());
			}
		});
	}

	public void refreshInJob(EditPart editPart) {
		if (editPart == null) {
			return;
		}
		if (job == null) {
			job = new InternalJob();
		}
		job.refresh(editPart);
	}

	private Display getDisplay() {
		if (display == null) {
			display = Display.getCurrent();
		}
		return display;
	}

	class InternalJob extends Job {

		private long timestamp = -1;

		private Set<EditPart> refreshList;

		public InternalJob() {
			super("Refresh Job");
		}

		private boolean isPosterity(EditPart parent, EditPart child) {
			if (parent == null || child == null) {
				return false;
			}
			EditPart childParent = child.getParent();
			if (parent == childParent) {
				return true;
			}
			return isPosterity(parent, childParent);
		}

		private synchronized void refresh(EditPart editPart) {
			if (refreshList == null) {
				refreshList = new HashSet<EditPart>();
			}
			refreshList.add(editPart);
			if (timestamp == -1) {
				schedule(TIMEOUT);// waiting other more jobs.
			}
			timestamp = System.currentTimeMillis();
		}

		protected IStatus run(IProgressMonitor monitor) {
			synchronized (refreshList) {
				long localTimestamp = System.currentTimeMillis();
				if (localTimestamp - timestamp < TIMEOUT) {
					schedule(TIMEOUT);
					timestamp = localTimestamp;
					return Status.OK_STATUS;
				}

				List<EditPart> parts = new ArrayList<EditPart>();
				List<EditPart> doingJobs = new ArrayList<EditPart>(refreshList);
				try {
					for (EditPart ep : doingJobs) {
						if (parts.isEmpty()) {
							parts.add(ep);
						} else {
							boolean contains = false;
							for (EditPart editPart : parts) {
								if (isPosterity(editPart, ep)) {
									contains = true;
									break;
								}
							}
							if (contains) {
								continue;
							}
							for (Iterator<EditPart> iterator = parts.iterator(); iterator
									.hasNext();) {
								EditPart editPart = iterator.next();
								if (isPosterity(ep, editPart)) {
									iterator.remove();
								}
							}
							parts.add(ep);
						}
					}
				} finally {
					refreshList.removeAll(doingJobs);
				}
				if (!parts.isEmpty()) {
					for (EditPart ep : parts) {
						try {
							refreshAsynchronous(ep);
						} catch (Exception e) {
						}
					}
				}
				if (!refreshList.isEmpty()) {
					schedule(TIMEOUT);
					timestamp = System.currentTimeMillis();
					return Status.OK_STATUS;
				}
			}
			timestamp = -1;
			return Status.OK_STATUS;
		}
	}
}
