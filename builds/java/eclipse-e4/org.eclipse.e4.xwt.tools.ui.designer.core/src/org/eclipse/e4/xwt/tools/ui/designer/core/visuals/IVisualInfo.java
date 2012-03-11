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
 * $RCSfile: IVisualInfo.java,v $ $Revision: 1.4 $ $Date: 2010/06/18 00:15:24 $
 */
package org.eclipse.e4.xwt.tools.ui.designer.core.visuals;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.xwt.tools.ui.designer.core.images.IImageNotifier;

/**
 * @author jliu jin.liu@soyatec.com
 */
public interface IVisualInfo extends IImageNotifier {

	public Object getVisualObject();

	public void setVisualObject(Object visualObject);

	public boolean isRoot();

	public Rectangle getBounds();

	public Rectangle getClientArea();

}
