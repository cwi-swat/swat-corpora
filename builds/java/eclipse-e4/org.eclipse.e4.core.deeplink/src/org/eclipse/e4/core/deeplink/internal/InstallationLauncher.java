/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.core.deeplink.internal;

import static org.eclipse.e4.core.functionalprog.optionmonad.None.none;
import static org.eclipse.e4.core.functionalprog.optionmonad.Some.some;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.e4.core.functionalprog.optionmonad.Option;

/**
 * Launch a particular deeplink aware application so that it can handle a
 * pending deeplink request.
 */
public class InstallationLauncher {

	private String rootFolder;
	private DeepLinkProperties properties;

	/**
	 * Construct an InstallationLauncher.
	 * 
	 * @param rootFolder The root folder/directory to search for installations
	 * @param properties The current system deep link configuration 
	 */
	public InstallationLauncher(String rootFolder, DeepLinkProperties properties) {
		this.rootFolder = rootFolder;
		this.properties = properties;
	}
	
	/**
	 * Start the named deeplink aware application, passing the specified
	 * deeplink URL on the command line.
	 * 
	 * @param installation
	 *            The installation name to launch
	 * @param deepLinkURL
	 *            The deeplink:// URL to put on the command line (this
	 *            capability is not used by the current implementation)
	 */
	public void startInstallation(String installation, String deepLinkURL) {
		String command = properties.getInstallationCommand(installation);
		Option<String> commandLine = computeStartupCommandLine(installation, command);
		if (!commandLine.hasValue()) {
			throw new IllegalArgumentException("Unable to launch installation: " + installation);
		}
		String commandDir = rootFolder + File.separator + installation;
		startApplication(commandLine.get(), commandDir);
	}

	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	        setDaemon(true);
	    }
	    
	    public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null)
	                System.out.println(type + ">" + line);    
	            } catch (IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	}

	private void startApplication(String command, String commandDir) {
		try {
			Process p = Runtime.getRuntime().exec(command, null, new File(commandDir));
			// For now, just throw away the data from the subprocess.  Possibly log it in the future?
			// See: http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
			new StreamGobbler(p.getInputStream(), "STDOUT").start();
			new StreamGobbler(p.getErrorStream(), "STDERR").start();
		} catch (IOException e) {
			throw new RuntimeException("Unable to launch: " + command, e);
		}
	}
	
	String[] startupCommands = new String[] {
			"eclipse.exe",
			"eclipse",
			"launcher.exe",
			"launcher"
	};

	private Option<String> computeStartupCommandLine(String installation,
			String command) {
		// Try whatever's in the properties file (if anything)
		Option<String> result = validateCommand(installation, command);
		if (result.hasValue()) {
			return result;
		}
		// Fall back to Eclipse default commands
		for (String possibleCommand : startupCommands) {
			Option<String> possibleResult = validateCommand(installation, possibleCommand);
			if (possibleResult.hasValue()) {
				return possibleResult;
			}
		}
		return none();
	}

	private Option<String> validateCommand(String installation, String command) {
		if (command != null) {
			String commandLine = rootFolder + File.separator + installation + File.separator + command;
			if (new File(commandLine).exists()) {
				return some(commandLine);
			} else {
				return none();
			}
		}
		return none();
	}
}
