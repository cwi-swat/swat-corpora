/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Simon Kaegi, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.server.bespin;

import javax.servlet.http.HttpServlet;

//GET /edit/list/[path] gives a JSON list of edit actions from the last save to the current edit pointer. 
//GET /edit/recent/N/[path] gives a JSON list of edit actions, starting after N. edit/recent/1 would skip the first entry. 
//PUT /edit/at/[path], with the serialized JSON object representing the action as the PUT body. (you can start sending do's before your first save.) 
//POST /edit/reset/ blows away all edits 
//POST /edit/reset/[path] blows away edits for a specific path 
//edit queue is wiped when file is saved. (otherwise someone GETting the file and edits has no way of knowing the original state of the file from which to start applying edit actions) 

public class EditingServlet extends HttpServlet {

}
