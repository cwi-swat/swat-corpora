/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.tm.ui.builders;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.tm.ui.Activator;

public class TmNature implements IProjectNature {

	public void configure() throws CoreException {
		System.out.println("Configuring " + this + " for " + getProject());
		String pluginIdPrefix = Activator.getDefault().getBundle().getSymbolicName() + ".";
		ensureBuilders(Arrays.asList(new String[]{
				pluginIdPrefix + JavascriptBuilder.BUILDER_NAME,
		}));
	}

	private void ensureBuilders(List<String> builderIds) throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			builderIds.remove(commands[i].getBuilderName());
		}
		ICommand[] newCommands = new ICommand[commands.length + builderIds.size()];
		System.arraycopy(commands, 0, newCommands, builderIds.size(), commands.length);
		for (int i = 0; i < builderIds.size(); i++) {
			ICommand command = desc.newCommand();
			command.setBuilderName((String)builderIds.get(i));
//			System.out.println("Added " + command.getBuilderName());
			newCommands[i] = command;
		}
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
	}

	public void deconfigure() throws CoreException {
//		System.out.println("Deconfiguring " + this);
	}

	private IProject project;

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
