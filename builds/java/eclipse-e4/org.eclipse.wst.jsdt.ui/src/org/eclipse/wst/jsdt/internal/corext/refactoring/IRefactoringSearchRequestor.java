/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring;

import org.eclipse.wst.jsdt.core.search.SearchMatch;

/**
 * Interface for search requestors used in conjunction with {@link org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringSearchEngine2}.
 * 
 * 
 */
public interface IRefactoringSearchRequestor {

	/**
	 * Can the search match be accepted?
	 * 
	 * @param match the search match to test
	 * @return The accepted match, or <code>null</code> if not accepted
	 */
	public SearchMatch acceptSearchMatch(SearchMatch match);
}
