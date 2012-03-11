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
package org.eclipse.e4.languages.internal.javascript.debug.ui;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.ui.IDebugModelPresentationExtension;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.e4.languages.javascript.debug.connect.JSONConstants;
import org.eclipse.e4.languages.javascript.debug.model.JSDIBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDIFunctionBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDILineBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDIProperty;
import org.eclipse.e4.languages.javascript.debug.model.JSDIScriptLoadBreakpoint;
import org.eclipse.e4.languages.javascript.debug.model.JSDIValue;
import org.eclipse.e4.languages.javascript.debug.model.JSDIVariable;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.jsdt.core.Signature;

/**
 * Default model presentation for JSDI model elements
 * 
 * @since 0.9
 */
public class JSDIModelPresentation extends LabelProvider implements IDebugModelPresentationExtension {

	/**
	 * Qualified names presentation property (value <code>"DISPLAY_QUALIFIED_NAMES"</code>).
	 * When <code>DISPLAY_QUALIFIED_NAMES</code> is set to <code>True</code>,
	 * this label provider should use fully qualified type names when rendering elements.
	 * When set to <code>False</code>, this label provider should use simple
	 * names when rendering elements.
	 * @see #setAttribute(String, Object)
	 */
	static final String DISPLAY_QUALIFIED_NAMES = "DISPLAY_QUALIFIED_NAMES"; //$NON-NLS-1$
	
	/**
	 * Map of attributes set from the debug model - i.e. things like if qualified names are being shown or not
	 */
	HashMap attributes = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.IDebugModelPresentationExtension#requiresUIThread(java.lang.Object)
	 */
	public boolean requiresUIThread(Object element) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.IDebugModelPresentation#computeDetail(org.eclipse.debug.core.model.IValue, org.eclipse.debug.ui.IValueDetailListener)
	 */
	public void computeDetail(IValue value, IValueDetailListener listener) {
		JSDIValue jsdivalue = (JSDIValue) value;
		listener.detailComputed(value, jsdivalue.getDetailString());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.IDebugModelPresentation#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String attribute, Object value) {
		if(this.attributes == null) {
			this.attributes = new HashMap();
		}
		this.attributes.put(attribute, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
	 */
	public void dispose() {
		if(this.attributes != null) {
			this.attributes.clear();
			this.attributes = null;
		}
		super.dispose();
	}
	
	/**
	 * @return true is qualified names are being shown in the various debug views
	 */
	boolean showQualifiedNames() {
		if(this.attributes != null) {
			Boolean show = (Boolean) this.attributes.get(DISPLAY_QUALIFIED_NAMES);
			if(show != null) {
				return show.booleanValue();
			}
		}
		//TODO hack to always return qualified names until the toggle action is platform available
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		try {
			if(element instanceof IDebugTarget) {
				return ((IDebugTarget)element).getName();
			}
			if(element instanceof IStackFrame) {
				return ((IStackFrame)element).getName();
			}
			if(element instanceof IThread) {
				return ((IThread)element).getName();
			}
			if(element instanceof IVariable) {
				return ((IVariable)element).getName();
			}
			if(element instanceof IValue) {
				return ((IValue)element).getValueString();
			}
			if(element instanceof JSDILineBreakpoint) {
				return getLineBreakpointText((JSDILineBreakpoint) element);
			}
			if(element instanceof JSDIFunctionBreakpoint) {
				return getFunctionBreakpointText((JSDIFunctionBreakpoint) element);
			}
			if(element instanceof JSDIScriptLoadBreakpoint) {
				return getScriptLoadBreakpointText((JSDIScriptLoadBreakpoint) element);
			}
		}
		catch(CoreException ce) {
			//TODO log this
			ce.printStackTrace();
		}
		return element.toString();
	}
	
	/**
	 * Returns the text for a line breakpoint
	 * @param breakpoint
	 * @return the breakpoint text
	 * @throws CoreException
	 */
	String getLineBreakpointText(JSDILineBreakpoint breakpoint) throws CoreException {
		String path = getElementPath(breakpoint.getScriptPath());
		StringBuffer buffer = new StringBuffer();
		buffer.append(path).append(" [line: ").append(breakpoint.getLineNumber()).append("]");
		int hitcount = breakpoint.getHitCount();
		if(hitcount > 0) {
			buffer.append(" [hit count: ").append(hitcount).append("]");
		}
		if(breakpoint.isConditionEnabled()) {
			buffer.append(" [conditional]");
		}
		if(breakpoint.getSuspendPolicy() == JSDIBreakpoint.SUSPEND_TARGET) {
			buffer.append(" [Suspend VM]");
		}
		return buffer.toString();
	}
	
	/**
	 * Returns the text for a method breakpoint
	 * @param breakpoint
	 * @return the breakpoint text
	 * @throws CoreException
	 */
	String getFunctionBreakpointText(JSDIFunctionBreakpoint breakpoint) throws CoreException {
		String path = getElementPath(breakpoint.getScriptPath());
		StringBuffer buffer = new StringBuffer();
		String method = Signature.toString(breakpoint.getSignature(), breakpoint.getFunctionName(), null, false, false);
		buffer.append(path).append(" - ").append(method);
		if(breakpoint.isEntry()) {
			if(breakpoint.isExit()) {
				buffer.append(" [entry and exit]");
			}
			else {
				buffer.append(" [entry]");
			}
		}
		else if(breakpoint.isExit()) {
			buffer.append(" [exit]");
		}
		int hitcount = breakpoint.getHitCount();
		if(hitcount > 0) {
			buffer.append(" [hit count: ").append(hitcount).append("]");
		}
		if(breakpoint.isConditionEnabled()) {
			buffer.append(" [conditional]");
		}
		if(breakpoint.getSuspendPolicy() == JSDIBreakpoint.SUSPEND_TARGET) {
			buffer.append(" [Suspend VM]");
		}
		return buffer.toString();
	}
	
	/**
	 * Returns the text for a script load breakpoint
	 * @param breakpoint
	 * @return the breakpoint text
	 * @throws CoreException
	 */
	String getScriptLoadBreakpointText(JSDIScriptLoadBreakpoint breakpoint) throws CoreException {
		String path = getElementPath(breakpoint.getScriptPath());
		StringBuffer buffer = new StringBuffer();
		buffer.append(path);
		int hitcount = breakpoint.getHitCount();
		if(hitcount > 0) {
			buffer.append(" [hit count: ").append(hitcount).append("]");
		}
		if(breakpoint.getSuspendPolicy() == JSDIBreakpoint.SUSPEND_TARGET) {
			buffer.append(" [Suspend VM]");
		}
		return buffer.toString();
	}
	
	/**
	 * Returns the path of the element based on if qualified names are being shown
	 * @param path
	 * @return
	 */
	String getElementPath(String path) {
		IPath epath = new Path(path);
		if(showQualifiedNames()) {
			return epath.toOSString();
		}
		return epath.lastSegment();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		try {
			if(element instanceof JSDIVariable) {
				JSDIVariable var = (JSDIVariable) element;
				return getImageFromType(var.getReferenceTypeName());
			}
			if(element instanceof JSDIProperty) {
				JSDIProperty property = (JSDIProperty) element;
				return getImageFromType(property.getReferenceTypeName());
			}
			if(element instanceof JSDILineBreakpoint || element instanceof JSDIFunctionBreakpoint) {
				JSDIBreakpoint breakpoint = (JSDIBreakpoint) element;
				int flags = computeBreakpointAdornmentFlags(breakpoint);
				if(breakpoint.isEnabled()) {
					return JSDIImageRegistry.getImage(new JSDIImageDescriptor(JSDIImageRegistry.getSharedImage(ISharedImages.IMG_BRKP), flags));
				}
				return JSDIImageRegistry.getImage(new JSDIImageDescriptor(JSDIImageRegistry.getSharedImage(ISharedImages.IMG_BRKP_DISABLED), flags));
			}
			if(element instanceof JSDIScriptLoadBreakpoint) {
				JSDIBreakpoint breakpoint = (JSDIBreakpoint) element;
				int flags = computeBreakpointAdornmentFlags(breakpoint);
				if(breakpoint.isEnabled()) {
					return JSDIImageRegistry.getImage(new JSDIImageDescriptor(JSDIImageRegistry.getSharedImage(ISharedImages.IMG_SCRIPTBRKP), flags));
				}
				return JSDIImageRegistry.getImage(new JSDIImageDescriptor(JSDIImageRegistry.getSharedImage(ISharedImages.IMG_BRKP_DISABLED), flags));
			}
		}
		catch(DebugException de) {
			//TODO log this
			de.printStackTrace();
		}
		catch(CoreException ce) {
			//TODO log this
			ce.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Computes the flags for overlay adornments for a breakpoint image for the given breakpoint
	 * @param breakpoint
	 * @return the or'd set of flags describing the required overlays for the given breakpoint
	 * @see {@link JSDIImageDescriptor} for the complete list of accepted flags
	 */
	int computeBreakpointAdornmentFlags(JSDIBreakpoint breakpoint)  {
		int flags = 0;
		try {
			if (breakpoint.isEnabled()) {
				flags |= JSDIImageDescriptor.ENABLED;
			}
			if (breakpoint.isInstalled()) {
				flags |= JSDIImageDescriptor.INSTALLED;
			}
			if (breakpoint instanceof JSDILineBreakpoint) {
				if (((JSDILineBreakpoint)breakpoint).isConditionEnabled()) {
					flags |= JSDIImageDescriptor.CONDITIONAL;
				}
			}
			if (breakpoint instanceof JSDIFunctionBreakpoint) {
				JSDIFunctionBreakpoint mBreakpoint= (JSDIFunctionBreakpoint)breakpoint;
				if (mBreakpoint.isEntry()) {
					flags |= JSDIImageDescriptor.ENTRY;
				}
				if (mBreakpoint.isExit()) {
					flags |= JSDIImageDescriptor.EXIT;
				}
			}
		} 
		catch (CoreException e) {}
		return flags;
	}
	
	/**
	 * Returns the specific image for the given type
	 * @param type
	 * @return the image
	 */
	Image getImageFromType(String type) {
		if (type.equalsIgnoreCase(JSONConstants.FUNCTION)) {
			return JSDIImageRegistry.getSharedImage(ISharedImages.IMG_SCRIPT);
		}
		return JSDIImageRegistry.getSharedImage(ISharedImages.IMG_LOCAL_VAR);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ISourcePresentation#getEditorId(org.eclipse.ui.IEditorInput, java.lang.Object)
	 */
	public String getEditorId(IEditorInput input, Object element) {
		try {
			IEditorDescriptor descriptor = IDE.getEditorDescriptor(input.getName());
			return descriptor.getId();
		} catch (PartInitException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ISourcePresentation#getEditorInput(java.lang.Object)
	 */
	public IEditorInput getEditorInput(Object element) {
		if(element instanceof File) {
			return new FileStoreEditorInput(EFS.getLocalFileSystem().fromLocalFile((File) element));
		}
		if(element instanceof IFile) {
			return new FileEditorInput((IFile) element);
		}
		if(element instanceof JSDIScriptLoadBreakpoint) {
			try {
				JSDIScriptLoadBreakpoint bp = (JSDIScriptLoadBreakpoint) element;
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(bp.getScriptPath()));
				if(resource.getType() == IResource.FILE) {
					return new FileEditorInput((IFile) resource);
				}
			}
			catch(CoreException ce) {
				//TODO log this
				ce.printStackTrace();
			}
		}
		if(element instanceof JSDIBreakpoint) {
			IResource resource = ((JSDIBreakpoint)element).getMarker().getResource();
			if(resource.getType() == IResource.FILE) {
				return new FileEditorInput((IFile) resource);
			}
		}
		return null;
	}

}
