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
/*
 *  $RCSfile: IImageListener.java,v $
 *  $Revision: 1.2 $  $Date: 2010/06/18 00:15:23 $ 
 */
package org.eclipse.e4.xwt.tools.ui.designer.core.images;

import java.util.EventListener;

import org.eclipse.swt.graphics.Image;

/**
 * @author jliu jin.liu@soyatec.com
 */
public interface IImageListener extends EventListener {

	/**
	 * The image of this object has changed. The new image data is sent along. If it is null, then there is no image to render. This could happen because the size was (0,0) for example. In this case the listener would probably want to handle no image.
	 * 
	 * @param image
	 */
	public void imageChanged(Image image);
}
