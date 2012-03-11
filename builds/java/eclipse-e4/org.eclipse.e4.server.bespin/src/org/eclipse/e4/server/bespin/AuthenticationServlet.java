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

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

//POST /register/login/username returns a JSON object with a "project" attribute that gives the key for a user's private files project. The body of the POST is the user's password. 
//POST /register/new/username returns a JSON object with the "project" attribute. The body of the POST should include the user's password and email address, encoded the same as form inputs (password, email). 
//POST (or GET) /register/logout/ 
//GET /register/userinfo/ for debugging, it will tell you who it thinks you are logged in as and will also give you the name of the user project (this is in JSON format). The user's quota and amountUsed are provided in bytes. 

public class AuthenticationServlet extends HttpServlet {

    private File users;

    public AuthenticationServlet() {
        users = Activator.bundleContext.getDataFile("users");
        users.mkdir();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/userinfo/")) {
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

            Map userMap = loadUserMap(username);
            if (userMap == null) {
                resp.setStatus(401);
                return;
            }
            String project = (String) userMap.get("project");
            resp.setContentType("application/json; charset=UTF-8");
            Map jsonObject = new HashMap();
            jsonObject.put("project", new Integer(project.hashCode()));
            jsonObject.put("username", username);
            resp.getWriter().write(JSONUtil.write(jsonObject));
            return;
        }

        if (pathInfo.equals("/logout/")) {
            HttpSession session = req.getSession();
            if (session != null)
                session.invalidate();
            return;
        }

        super.doGet(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.startsWith("/new/")) {
            String username = pathInfo.substring("/new/".length());

            Map userMap = loadUserMap(username);
            if (userMap != null) {
                resp.getWriter().write("Username " + username + " is already in use");
                resp.setStatus(409);
                return;
            }

            userMap = createUserMap();
            String password = req.getParameter("password");
            userMap.put("password", password);
            String email = req.getParameter("email");
            userMap.put("email", email);
            String project = username + "_Project";
            userMap.put("project", project);

            saveUserMap(username, userMap);

            req.getSession(true).setAttribute("username", username);
            resp.setContentType("application/json; charset=UTF-8");
            Map jsonObject = new HashMap();
            jsonObject.put("project", new Integer(project.hashCode()));
            resp.getWriter().write(JSONUtil.write(jsonObject));
            return;
        }

        if (pathInfo.startsWith("/login/")) {
            String username = pathInfo.substring("/login/".length());

            Map userMap = loadUserMap(username);
            if (userMap == null) {
                resp.setStatus(401);
                return;
            }

            String project = (String) userMap.get("project");
            req.getSession(true).setAttribute("username", username);
            resp.setContentType("application/json; charset=UTF-8");
            Map jsonObject = new HashMap();
            jsonObject.put("project", new Integer(project.hashCode()));
            resp.getWriter().write(JSONUtil.write(jsonObject));
            return;
        }

        super.doPost(req, resp);
    }

    private Map createUserMap() {
        return new Properties();
    }

    private void saveUserMap(String username, Map userMap) {
        File userFile = new File(users, username + ".properties");
        Properties props = (Properties) userMap;
        try {
            props.store(new FileOutputStream(userFile), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map loadUserMap(String username) {
        File userFile = new File(users, username + ".properties");
        if (!userFile.isFile())
            return null;

        Properties props = new Properties();
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
