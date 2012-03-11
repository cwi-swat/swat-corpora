/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Simon Kaegi, Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.server.bespin;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

//GET /settings/ to list all settings for currently logged in user as json dict 
//GET /settings/[setting] to get the value for a single setting as json string 
//POST /settings/ with HTTP POST DATA (in standard form post syntax) to set the value for a collection of settings (all values are strings) 
//DELETE /settings/[setting] to delete a single setting 

public class SettingsServlet extends HttpServlet {
    private File users;

    public SettingsServlet() {
        users = Activator.bundleContext.getDataFile("users");
        users.mkdir();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        HttpSession session = req.getSession();
        if (session == null) {
            resp.setStatus(401);
            return;
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            resp.setStatus(401);
            return;
        }

        Map userMap = loadUserSettingsMap(username);
        if (userMap == null) {
            resp.setStatus(401);
            return;
        }
        if (pathInfo.equals("/")) {
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(JSONUtil.write(userMap));
            return;
        }

        String key = pathInfo.substring(1);
        String value = (String) userMap.get(key);

        Map jsonObject = new HashMap();
        jsonObject.put(key, value);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(JSONUtil.write(jsonObject));
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.equals("/")) {
            HttpSession session = req.getSession();
            if (session == null) {
                resp.setStatus(401);
                return;
            }
            String username = (String) session.getAttribute("username");
            if (username == null) {
                resp.setStatus(401);
                return;
            }

            Map userMap = loadUserSettingsMap(username);
            if (userMap == null) {
                resp.setStatus(401);
                return;
            }
            Enumeration parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                userMap.put(parameterName, req.getParameter(parameterName));
            }
            saveUserSettingsMap(username, userMap);
            return;
        }

        super.doPost(req, resp);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        HttpSession session = req.getSession();
        if (session == null) {
            resp.setStatus(401);
            return;
        }
        String username = (String) session.getAttribute("username");
        if (username == null) {
            resp.setStatus(401);
            return;
        }

        Map userMap = loadUserSettingsMap(username);
        if (userMap == null) {
            resp.setStatus(401);
            return;
        }
        if (pathInfo.equals("/")) {
            userMap.clear();
        }
        String key = pathInfo.substring(1);
        userMap.remove(key);
        saveUserSettingsMap(username, userMap);
        return;

    }

    private void saveUserSettingsMap(String username, Map userMap) {
        File userFile = new File(users, username + "_settings.properties");
        Properties props = (Properties) userMap;
        try {
            props.store(new FileOutputStream(userFile), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map loadUserSettingsMap(String username) {
        Properties props = new Properties();
        File userFile = new File(users, username + "_settings.properties");
        if (!userFile.isFile())
            return props;

        try {
            props.load(new FileInputStream(userFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

}
