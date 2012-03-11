/*******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.server.bespin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.*;
import org.eclipse.team.internal.ccvs.core.connection.CVSRepositoryLocation;

public class CVS {

    /**
     * 
     * @param projectNameOrNames
     *            a String or an array of Strings, for example
     *            "org.eclipse.jface"
     * @param repo
     *            a String, for example
     *            ":pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse"
     * @param moduleNameOrNames
     *            a String or an array of Strings, for example
     *            "org.eclipse.jface"
     * @param tagName
     *            a String, for example "HEAD", or "I20060320-0800"
     */
    public void checkOut(final Object projectNameOrNames, final String repo, final Object moduleNameOrNames, final String tagName) {
        final String[] projectNames = asStringArray(projectNameOrNames);
        final String[] moduleNames = asStringArray(moduleNameOrNames);

        new ProgressRunnable(projectNames, repo, moduleNames, tagName).run(new NullProgressMonitor());
    }

    private String[] asStringArray(final Object stringOrStringArray) {
        String[] result;
        if (stringOrStringArray instanceof String) {
            result = new String[] {(String) stringOrStringArray};
        } else {
            Object[] strings = (Object[]) stringOrStringArray;
            result = new String[strings.length];
            System.arraycopy(strings, 0, result, 0, strings.length);
        }
        return result;
    }

    private static final class ProgressRunnable {
        private final String[] moduleNames;

        private final String[] projectNames;

        private final String tagName;

        private final String repo;

        private ProgressRunnable(String[] projectNames, String repo, String[] moduleNames, String tag) {
            this.moduleNames = moduleNames;
            this.projectNames = projectNames;
            this.tagName = tag;
            this.repo = repo;
        }

        public void run(IProgressMonitor monitor) {
            final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            final IProject[] projects = new IProject[projectNames.length];
            for (int i = 0; i < projectNames.length; i++) {
                projects[i] = root.getProject(projectNames[i]);
            }
            final CVSTag tag = tagName == null || tagName.equals("HEAD") ? new CVSTag() : new CVSTag(tagName, CVSTag.VERSION);
            try {
                ResourcesPlugin.getPlugin().getWorkspace().run(new WorkspaceRunnable(projects, repo, moduleNames, tag), root, 0, monitor);
            } catch (CoreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class WorkspaceRunnable implements IWorkspaceRunnable {
        private final CVSTag tag;

        private final IProject[] projects;

        private String repo;

        private String[] moduleNames;

        private WorkspaceRunnable(IProject[] projects, String repo, String[] moduleNames, CVSTag tag) {
            this.projects = projects;
            this.repo = repo;
            this.moduleNames = moduleNames;
            this.tag = tag;
        }

        public void run(IProgressMonitor monitor) throws CoreException {
            try {
                for (int i = 0; i < projects.length; i++) {
                    CVSProjectSetCapability.checkout(CVSRepositoryLocation.fromString(repo), projects[i], moduleNames[i], tag, monitor);
                }
            } catch (CVSException e) {
                throw new RuntimeException(e);
            } catch (TeamException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
