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
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class CVSServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.startsWith("/checkout/")) {
            // something like ":pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse,org.eclipse.core.databinding"
            String cvsPath = pathInfo.substring("/checkout/".length());
            int indexOfComma = cvsPath.indexOf(',');
            if (indexOfComma == -1) {
                resp.setStatus(400);
                return;
            }
            String repo = cvsPath.substring(0, indexOfComma);
            String projectname = cvsPath.substring(indexOfComma + 1);
            try {
                new CVS().checkOut(projectname, repo, projectname, "HEAD");
                return;
            } catch (Exception e) {
                e.printStackTrace(resp.getWriter());
                resp.setStatus(500);
                return;
            }
        }

        super.doPost(req, resp);
    }
}
