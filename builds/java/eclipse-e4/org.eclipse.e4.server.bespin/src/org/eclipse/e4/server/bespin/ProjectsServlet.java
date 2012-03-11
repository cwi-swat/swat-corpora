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

//GET /project/info/[projectname]/ - returns a JSON dictionary with "owner" and "members" (the people authorized to work on the project) 
//POST /project/authorized/[projectname]/[username] - authorize username to access the project (only the project owner can do this) 
//DELETE /project/authorized/[projectname]/[username] - remove username's access to the project (only the project owner can do this) 
//GET /project/export/[projectname][.tgz|.zip] - export the project as a tarball or zipfile 
//POST /project/import/[projectname] - create/replace the project with the provided file. The data should be a multipart form with a filedata file upload field. NOTE: if the project already exists, this will delete the project and start over (and if the project already exists, only the owner of the project can call this. If the project does not exist, anyone can call this). 
//POST /project/fromurl/[projectname] - just like import above, but the body is a URL to download for import. The URL should be a zip file or gzipped tar file. 

public class ProjectsServlet extends HttpServlet {

}
