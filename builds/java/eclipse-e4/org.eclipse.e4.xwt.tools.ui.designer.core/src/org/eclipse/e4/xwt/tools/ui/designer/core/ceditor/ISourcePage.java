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
package org.eclipse.e4.xwt.tools.ui.designer.core.ceditor;

import org.eclipse.ui.IEditorPart;

/**
 * @author Jin Liu(jin.liu@soyatec.com)
 */
public interface ISourcePage extends IEditorPart {

	String getPageName();

	boolean isEnabledFor(IEditorPart editorPart);

	void configureSourcePage(IEditorPart editorPart);

}
