/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.server.bespin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.InstanceScope;

//GET /file/list/[path] gives a JSON list of files in the directory given by [path]. Path is relative to the projects directory. Directories will have "/" appended to their name. The list is sorted. Each item in the list is an object with a "name" property. Additionally, the objects have the data described in the /file/stats/ call. 
//GET /file/stats/[path] returns a JSON object with the stats for the file. Attributes on the object include size (integer of the file's size at last save), created (date/time of creation in YYYYMMDDTHHMMSS ISO format), modified (date/time of last save, in the same format as created), openedBy (list of usernames that currently have the file open). 
//GET /file/at/[path]?mode=[r|rw] to get the contents of a file. (raw text, not a JSON string!) if the file does not exist, an empty body will be returned; use list to determine if a file actually exists. the server will record you as having the file open with the given mode after this call. If mode is not specified, rw is used. 
//PUT /file/at/[path]?lastEdit=[n] to save a file, with the file contents as the PUT body. subdirectories will be created as needed. If the lastEdit parameter is excluded, the request will only succeed if the file is *not* open for editing. Otherwise, the lastEdit parameter must include the number of the last edit received by the client at the time the save was requested. 
//DELETE /file/at/[path] to delete a file. file must not be open by anyone. 
//POST /file/close/[path] to mark the file closed. The server will discard your edit history. 
//GET /file/listopen/ to list open files for the current user. a JSON dictionary of { project: { name: filename, mode: r|rw } } will be returned. For example, if subdir1/subdir2/test.py is open read/write, openfiles will return { "subdir1": { name: "somedir2/test.py", mode: "rw" } } 
//GET /preview/at/[path] to retrieve file contents for previewing. The raw file is served up with an appropriate content type, so the browser should be able to view it. Relative links will also work.

public class MarkersServlet extends HttpServlet {

    private IWorkspace workspace;

    private synchronized IWorkspace getWorkspace() {
        if (workspace == null) {
            // enable auto-refresh:
            new InstanceScope().getNode(ResourcesPlugin.PI_RESOURCES).putBoolean(ResourcesPlugin.PREF_AUTO_REFRESH, true);
            workspace = ResourcesPlugin.getWorkspace();
        }
        return workspace;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.startsWith("/at/")) {
            String wsPath = pathInfo.substring("/at".length());
            IResource resource = getWorkspace().getRoot().findMember(new Path(wsPath));
            if (resource.getType() != IResource.FILE) {
                resp.setStatus(405);
                return;
            }
            PrintWriter writer = resp.getWriter();
            try {
                IMarker[] markers = resource.findMarkers(null, true, IResource.DEPTH_ZERO);
                resp.setStatus(200);
                resp.setContentType("application/json; charset=UTF-8");
                List jsonArray = new ArrayList();
                for (int i = 0; i < markers.length; i++) {
                    Map jsonObject = new HashMap();
                    jsonObject.put("message", (markers[i].getAttribute(IMarker.MESSAGE) + "").replace('"', '\''));
                    jsonObject.put("severity", (markers[i].getAttribute(IMarker.SEVERITY) + "").replace('"', '\''));
                    jsonObject.put("line", markers[i].getAttribute(IMarker.LINE_NUMBER));
                    jsonArray.add(jsonObject);
                }
                writer.write(JSONUtil.write(jsonArray));
                return;
            } catch (Exception e) {
                e.printStackTrace(writer);
                resp.setStatus(500);
                return;
            }
        }

        super.doGet(req, resp);
    }
}
