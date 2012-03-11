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
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.*;
import org.eclipse.pde.internal.core.ClasspathComputer;
import org.eclipse.pde.internal.core.natures.PDE;

//GET /file/list/[path] gives a JSON list of files in the directory given by [path]. Path is relative to the projects directory. Directories will have "/" appended to their name. The list is sorted. Each item in the list is an object with a "name" property. Additionally, the objects have the data described in the /file/stats/ call. 
//GET /file/stats/[path] returns a JSON object with the stats for the file. Attributes on the object include size (integer of the file's size at last save), created (date/time of creation in YYYYMMDDTHHMMSS ISO format), modified (date/time of last save, in the same format as created), openedBy (list of usernames that currently have the file open). 
//GET /file/at/[path]?mode=[r|rw] to get the contents of a file. (raw text, not a JSON string!) if the file does not exist, an empty body will be returned; use list to determine if a file actually exists. the server will record you as having the file open with the given mode after this call. If mode is not specified, rw is used. 
//PUT /file/at/[path]?lastEdit=[n] to save a file, with the file contents as the PUT body. subdirectories will be created as needed. If the lastEdit parameter is excluded, the request will only succeed if the file is *not* open for editing. Otherwise, the lastEdit parameter must include the number of the last edit received by the client at the time the save was requested. 
//DELETE /file/at/[path] to delete a file. file must not be open by anyone. 
//POST /file/close/[path] to mark the file closed. The server will discard your edit history. 
//GET /file/listopen/ to list open files for the current user. a JSON dictionary of { project: { name: filename, mode: r|rw } } will be returned. For example, if subdir1/subdir2/test.py is open read/write, openfiles will return { "subdir1": { name: "somedir2/test.py", mode: "rw" } } 
//GET /preview/at/[path] to retrieve file contents for previewing. The raw file is served up with an appropriate content type, so the browser should be able to view it. Relative links will also work.

public class FilesServlet extends HttpServlet {

    private static final boolean CHEAT = false;
    private IWorkspace workspace;

    private synchronized IWorkspace getWorkspace() {
        if (workspace == null) {
            // enable auto-refresh:
            new InstanceScope().getNode(ResourcesPlugin.PI_RESOURCES).putBoolean(ResourcesPlugin.PREF_AUTO_REFRESH, true);
            workspace = ResourcesPlugin.getWorkspace();
        }
        return workspace;
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.startsWith("/at/")) {
            try {
                String wsPath = pathInfo.substring("/at".length());
                IFile file = getWorkspace().getRoot().getFile(new Path(wsPath));
                InputStream inputStream = req.getInputStream();
                if (file.exists()) {
                    file.setContents(inputStream, false, true, null);
                } else {
                    file.create(inputStream, false, null);
                }
                resp.setStatus(200);
                pipe(file, resp.getOutputStream());
                return;
            } catch (Exception e) {
                e.printStackTrace(resp.getWriter());
                resp.setStatus(500);
                return;
            }
        }

        if (pathInfo.startsWith("/list/")) {
            String projectname = pathInfo.substring("/list/".length());
            boolean pde = projectname.indexOf('.') != -1;
            try {
                createProject(projectname, pde);
                return;
            } catch (Exception ex) {
                ex.printStackTrace(resp.getWriter());
                resp.setStatus(500);
                return;
            }
        }
        super.doPut(req, resp);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.startsWith("/at/")) {
            String wsPath = pathInfo.substring("/at".length());
            IResource resource = getWorkspace().getRoot().findMember(new Path(wsPath));
            if (resource == null) {
                resp.getWriter().println("Resource '" + wsPath + "' does not exist");
                resp.setStatus(404);
                return;
            }
            try {
                resource.delete(false, null);
                resp.setStatus(204);
                return;
            } catch (CoreException e) {
                e.printStackTrace(resp.getWriter());
                resp.setStatus(500);
                return;
            }
        }
        super.doDelete(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.startsWith("/at/")) {
            OutputStream outputStream = resp.getOutputStream();
            String wsPath = pathInfo.substring("/at".length());
            IResource resource = getWorkspace().getRoot().findMember(new Path(wsPath));
            if (resource == null) {
                resp.setStatus(404);
                return;
            }
            if (resource.getType() != IResource.FILE) {
                resp.setStatus(405);
                return;
            }
            IFile file = (IFile) resource;
            try {
                pipe(file, outputStream);
                return;
            } catch (CoreException e) {
                e.printStackTrace(new PrintStream(outputStream, true));
                resp.setStatus(500);
                return;
            }
        }

        PrintWriter writer = resp.getWriter();
        if (pathInfo.startsWith("/list/")) {
            String wsPath = pathInfo.substring("/list".length());
            IResource resource = getWorkspace().getRoot().findMember(new Path(wsPath));
            if (resource == null) {
                resp.setStatus(404);
                return;
            }
            if (resource.getType() == IResource.FILE) {
                resp.setStatus(405);
                return;
            }
            IContainer container = (IContainer) resource;
            IResource[] members;
            try {
                members = container.members();
                resp.setStatus(200);
                resp.setContentType("application/json; charset=UTF-8");
                List jsonArray = new ArrayList();
                for (int i = 0; i < members.length; i++) {
                    Map jsonObject = new HashMap();
                    String name = members[i].getName();
                    if (members[i].getType() != IResource.FILE)
                        name += "/";
                    jsonObject.put("name", name);
                    jsonArray.add(jsonObject);
                }
                writer.write(JSONUtil.write(jsonArray));
                return;
            } catch (CoreException e) {
                e.printStackTrace(writer);
                resp.setStatus(500);
                return;
            }
        }

        if (pathInfo.equals("/listopen/")) {
            resp.setStatus(200);
            resp.setContentType("application/json; charset=UTF-8");
            writer.print("{} ".replace('\'', '"'));
            return;
        }

        if (CHEAT) {
            resp.setStatus(200);
            resp.setContentType("text/html; charset=UTF-8");
            writer.print("hello world");
            return;
        }
        super.doGet(req, resp);
    }

    private void pipe(IFile file, OutputStream outputStream) throws CoreException, IOException {
        InputStream contents = file.getContents();
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = contents.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        contents.close();
    }

    private void createProject(String projectname, boolean pde) throws CoreException, JavaModelException {
        IProject project = getWorkspace().getRoot().getProject(projectname);
        project.create(null);
        project.open(null);
        if (pde) {
            addNatureToProject(project, PDE.PLUGIN_NATURE, null);
            setupJava(project, true);
            IFolder metaFolder = project.getFolder("META-INF");
            metaFolder.create(true, true, null);
            createManifest(metaFolder, projectname);
            createBuildProperties(project);
        } else {
            setupJava(project, false);
        }
    }

    private void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor) throws CoreException {
        IProjectDescription description = proj.getDescription();
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = natureId;
        description.setNatureIds(newNatures);
        proj.setDescription(description, monitor);
    }

    private void setupJava(IProject project, boolean pde) throws CoreException, JavaModelException {
        addNatureToProject(project, JavaCore.NATURE_ID, null);
        IFolder srcFolder = project.getFolder("src");
        srcFolder.create(true, true, null);
        IFolder binFolder = project.getFolder("bin");
        if (!binFolder.exists()) {
            binFolder.create(true, true, null);
        }
        IJavaProject javaProject = JavaCore.create(project);
        javaProject.setOutputLocation(binFolder.getFullPath(), null);
        IClasspathEntry[] entries = new IClasspathEntry[pde ? 3 : 1];
        if (pde) {
            String executionEnvironment = "J2SE-1.5";
            ClasspathComputer.setComplianceOptions(javaProject, executionEnvironment);
            entries[0] = ClasspathComputer.createJREEntry(executionEnvironment);
            entries[1] = ClasspathComputer.createContainerEntry();
        }
        entries[entries.length - 1] = JavaCore.newSourceEntry(srcFolder.getFullPath());
        javaProject.setRawClasspath(entries, null);
    }

    private void createManifest(IFolder metaFolder, String name) throws CoreException {
        IFile manifest = metaFolder.getFile("MANIFEST.MF");
        StringBuffer contents = new StringBuffer();
        contents.append("Manifest-Version: 1.0\n");
        contents.append("Bundle-ManifestVersion: 2\n");
        contents.append("Bundle-Name: " + name + "\n");
        contents.append("Bundle-SymbolicName: " + name + "\n");
        contents.append("Bundle-Version: 1.0.0\n");
        contents.append("Bundle-RequiredExecutionEnvironment: J2SE-1.5\n");
        contents.append("\n");
        manifest.create(new ByteArrayInputStream(contents.toString().getBytes()), false, null);
    }

    private void createBuildProperties(IProject project) throws CoreException {
        IFile buildProperties = project.getFile("build.properties");
        StringBuffer contents = new StringBuffer();
        contents.append("source.. = src/\n");
        contents.append("output.. = bin/\n");
        contents.append("bin.includes = META-INF/,\\n");
        contents.append("               .\n");
        buildProperties.create(new ByteArrayInputStream(contents.toString().getBytes()), false, null);
    }

}
