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

/**
 * Describes images that are in the image registry
 * 
 * @since 0.9
 */
public interface ISharedImages {

	//ELCL
	/**
	 * Default script image
	 */
	public static final String IMG_SCRIPT = "IMG_SCRIPT";
	
	/**
	 * Image for a local variable
	 */
	public static final String IMG_LOCAL_VAR = "IMG_LOCAL_VAR";
	
	/**
	 * Image for a script load breakpoint
	 */
	public static final String IMG_SCRIPTBRKP = "IMG_SCRIPTBRKP";
	
	/**
	 * Image for a normal breakpoint
	 */
	public static final String IMG_BRKP = "IMG_BRKP";

	/**
	 * Image for the connect icon
	 */
	public static final String IMG_CONNECT = "IMG_CONNECT";
	
	//DLCL
	/**
	 * Image for a disabled breakpoint
	 */
	public static final String IMG_BRKP_DISABLED = "IMG_BRKP_DISABLED"; 
	
	//overlays
	/**
	 * Image for the conditional breakpoint overlay
	 */
	public static final String IMG_OVR_CONDITIONAL = "IMG_OVR_CONDITIONAL";
	
	/**
	 * Image for the disabled conditional breakpoint overlay 
	 */
	public static final String IMG_OVR_CONDITIONAL_DISABLED = "IMG_OVR_CONDITIONAL_DISABLED";
	
	/**
	 * Image for the function entry breakpoint overlay 
	 */
	public static final String IMG_OVR_ENTRY = "IMG_OVR_ENTRY";
	
	/**
	 * Image for the disabled function entry breakpoint overlay 
	 */
	public static final String IMG_OVR_ENTRY_DISABLED = "IMG_OVR_ENTRY_DISABLED";
	
	/**
	 * Image for the function exit breakpoint overlay
	 */
	public static final String IMG_OVR_EXIT = "IMG_OVR_EXIT";
	
	/**
	 * Image for the disabled function exit breakpoint overlay
	 */
	public static final String IMG_OVR_EXIT_DISABLED = "IMG_OVR_EXIT_DISABLED";
	
	/**
	 * Image for the installed breakpoint overlay
	 */
	public static final String IMG_OVR_INSTALLED = "IMG_OVR_INSTALLED";
	
	/**
	 * Image for the disabled installed breakpoint overlay
	 */
	public static final String IMG_OVR_INSTALLED_DISABLED = "IMG_OVR_INSTALLED_DISABLED";
	
	/**
	 * Image for the scoped breakpoint overlay
	 */
	public static final String IMG_OVR_SCOPED = "IMG_OVR_SCOPED";
	
	/**
	 * Image for the disabled scoped breakpoint overlay
	 */
	public static final String IMG_OVR_SCOPED_DISABLED = "IMG_OVR_SCOPED_DISABLED";
}
