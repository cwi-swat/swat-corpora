/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, Simon Kaegi, IBM Corporation - initial API and implementation
 *******************************************************************************/
Bespin.Commands.add({
    name: 'problems',
    takes: ['filename'],
    preview: 'check the current file for problems',
    completeText: 'add the filename to check a particular file for problems, or check the current file',
    execute: function(self, filename) {
	    filename = filename || _editSession.path; // default to what you have
	
        var project = _editSession.project || '';
        var url = Bespin.Path.combine('/markers/at', project, filename);
        _server.request('GET', url, null, { call: function(markers) {
	        if (!markers) {
	            document.fire("bespin:cmdline:showinfo", { msg: "Failed checking file: " + filename + " for problems."});
	        } else {
	            if (markers.length == 0) {
	                document.fire("bespin:cmdline:showinfo", { msg: "<u>No problems found in " + filename + ".</u>" });
	                return;
	        	}
	        	
	        	var severityString = ["i", "!", "x"];
	        	var severityStyle = ["color:green", "color:#ffc85a", "color:#f13f53"];
	
	
	            var result = "<u>" + markers.length + " problem(s) found in " + filename + ".</u><hr/>";
	            var partialResult = "";
	            var i;
	            for (i = 0; i < markers.length; i++) {
	                result += "<span style='" + severityStyle[markers[i].severity] + "'>" + severityString[markers[i].severity] + "</span> <a href=\"javascript:document.fire('bespin:cmdline:execute', {name:'goto', args:'" + markers[i].line + "'}); void(0)\">line " + markers[i].line + "</a>: " + markers[i].message + "<br/>";
	                if (i == 4)
	                    partialResult += result;
	            }
	            if (i > 4) {
	            	document.tempResult = result;
	                partialResult += "<br/><a href=\"javascript:document.fire('bespin:cmdline:showinfo', {msg: document.tempResult});delete document.tempResult; void(0)\">(Show All)</a><br/>";
	                document.fire("bespin:cmdline:showinfo", { msg: partialResult});
	                return;
	            }
	            document.fire("bespin:cmdline:showinfo", { msg: result});
	        }
	    }, evalJSON: true });
    }
});
Bespin.Commands.add({
    name: 'newproject',
    takes: ['projectname'],
    preview: 'Creates a new Java project. Use multi.segment.names to create a plug-in project.',
    completeText: 'add the name of the project to create',
    execute: function(self, projectname) {
    	if (projectname == null || projectname == "") {
            self.showInfo("You need to provide a project name");
    		return;
    	}
    	var url = Bespin.Path.combine('/file/list', projectname);
        document.fire("bespin:cmdline:showinfo", { msg: "Creating project: " + projectname + ", please wait..."});
    	var callback = function() {
    		Bespin.Navigate.dashboard();
    		document.fire("bespin:cmdline:showinfo", { msg: "Project created: " + projectname});
    	};
        _server.request('PUT', url, null, { call: callback, evalJSON: false });
    }
});
Bespin.Commands.add({
    name: 'cvsco',
    takes: ['cvslocation'],
    preview: 'Imports a project from CVS. Example: cvsco :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse,org.eclipse.core.databinding.',
    completeText: 'enter the CVS location, e.g. :pserver:anonymous@dev.eclipse.org:/cvsroot/eclipse,org.eclipse.core.databinding',
    execute: function(self, cvslocation) {
    	if (cvslocation == null || cvslocation == "") {
            self.showInfo("You need to provide a cvs location");
    		return;
    	}
    	var url = Bespin.Path.combine('/cvs/checkout', cvslocation);
        document.fire("bespin:cmdline:showinfo", { msg: "Importing project: " + cvslocation + ", please wait..."});
    	var callback = function() {
    		Bespin.Navigate.dashboard();
    		document.fire("bespin:cmdline:showinfo", { msg: "Project imported: " + cvslocation});
    	};
        _server.request('POST', url, null, { call: callback, evalJSON: false });
    }
});
