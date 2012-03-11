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
package org.eclipse.e4.xwt.tools.ui.designer.core.parts;

/**
 * @author jin.liu (jin.liu@soyatec.com)
 * 
 */
public class RefreshContext {

	public static final int REFRESH_IMAGE = 1;
	public static final int REFRESH_CHILDREN = REFRESH_IMAGE << 1;
	public static final int REFRESH_VISUALS = REFRESH_CHILDREN << 1;
	public static final int REFRESH_ALL = REFRESH_IMAGE | REFRESH_CHILDREN
			| REFRESH_VISUALS;

	private int refreshType = 0;

	/**
	 * Create a EditPartContext with given refreshType.
	 */
	RefreshContext(int refreshType) {
		this.refreshType = refreshType;
	}

	public boolean refreshImage() {
		return refresh(REFRESH_IMAGE);
	}

	public void setImageRefreshed() {
		setRefreshed(REFRESH_IMAGE);
	}

	public boolean refreshChildren() {
		return refresh(REFRESH_CHILDREN);
	}

	public void setVisualsRefreshed() {
		setRefreshed(REFRESH_VISUALS);
	}

	public void setChildrenRefreshed() {
		setRefreshed(REFRESH_CHILDREN);
	}

	public boolean refreshVisuals() {
		return refresh(REFRESH_VISUALS);
	}

	public int getRefreshType() {
		return refreshType;
	}

	public void setRefreshed(int type) {
		refreshType = refreshType ^ type;
	}

	public boolean refresh(int type) {
		return (refreshType & type) != 0;
	}

	public static final RefreshContext ALL() {
		return createRefreshContext(REFRESH_ALL);
	}

	public static final RefreshContext IMAGE() {
		return createRefreshContext(REFRESH_IMAGE);
	}

	public static final RefreshContext CHILDREN() {
		return createRefreshContext(REFRESH_CHILDREN);
	}

	public static final RefreshContext VISUALS() {
		return createRefreshContext(REFRESH_VISUALS);
	}

	public static final RefreshContext createRefreshContext(int refreshType) {
		return new RefreshContext(refreshType);
	}
}
