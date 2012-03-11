function Bundle(framework, bundleData) {
	this._framework = framework;
	this._bundleData = bundleData;
	this._symbolicName = null;
	this._version = null;
	this._imports = [];
	this._requires = [];
	this._exports = [];
	this._singleton = false;
	this._markedStarted = false;
	this._state = INSTALLED;
	this._scope = {};
	this._bundleContext = null;
	this._activator = null;

	var headers = bundleData.getHeaders();
	_parseSymbolicName(headers[Constants.BUNDLE_SYMBOLIC_NAME_HEADER]);
	_parseVersion(headers[Constants.BUNDLE_VERSION_HEADER]);
	_parseImports(headers[Constants.IMPORTS_HEADER]);
	_parseExports(headers[Constants.EXPORTS_HEADER]);
	_parseRequires(headers[Constants.REQUIRES_HEADER]);
}

Bundle.UNINSTALLED = 1;
Bundle.INSTALLED = 2;
Bundle.RESOLVED = 4;
Bundle.STARTING = 8;
Bundle.STOPPING = 16;
Bundle.ACTIVE = 32;

Bundle.prototype = {
	getSymbolicName : function() {
		return this._symbolicName;
	},
	getVersion : function() {
		return this._version;
	},
	getBundleId : function() {
		return this._bundleData.getBundleId();
	},
	getLocation : function() {
		return this._bundleData.getLocation();
	},
	getHeaders : function() {
		return this._bundleData.getHeaders();
	},
	getState : function() {
		return this._state;
	},
	isSingleton : function() {
		return this._singleton;
	},
	isMarkedStarted : function() {
		return this._markedStarted;
	},
	equals : function(other) {
		if (this === other)
			return true;

		if (!other instanceof Bundle)
			return false;

		if (this._symbolicName !== other._symbolicName)
			return false;
		if (this._version === null) {
			if (other._version !== null)
				return false;
		} else if (!this._version.equals(other._version))
			return false;
		return true;
	},
	uninstall : function() {
		this._state = Bundle.UNINSTALLED;
	},
	start : function() {
		this._markedStarted = true;
		if (this._state !== Bundle.RESOLVED)
			return;

		this._state = Bundle.STARTING;
		this._bundleContext = this._framework.getBundleContext(this);
		this._activator = this._createActivatorInstance();
		if (this._activator) {
			this._activator.start(this._bundleContext);
		}
		this._state = Bundle.ACTIVE;
	},
	stop : function() {
		markedStarted = false;
		if (this._state !== Bundle.ACTIVE)
			return;

		this._state = Bundle.STOPPING;
		if (this._activator) {
			this._activator.stop(this._bundleContext);
		}
		this._state = Bundle.RESOLVED;
	},
	_createActivatorInstance : function() {
		var activatorName = this.getHeaders()[Constants.ACTIVATOR_HEADER];
		if (activatorName === null)
			return null;

		var activatorFunction = this.lookup(activatorName);
		if (typeof activatorFunction !== "function")
			throw "" + activatorName + " is not a function.";
		return new activatorFunction();
	},
	_resolve : function() {
		if (this._state != Bundle.INSTALLED)
			return;

		var namedExports = new Object(); // FIXME: jsdt bug for using {} with hasOwnProperty
		var jsExport = null;
		var i = 0;
		// process imports (first)
		for (i = 0; i < this._imports.length; i++) {
			var jsImport = this._imports[i];
			jsExport = jsImport.getWiredExport();
			if (!namedExports.hasOwnProperty(jsExport.getName()))
				namedExports[jsExport.getName()] = jsExport;
		}

		// process requires
		for (i = 0; i < this._requires.length; i++) {
			var jsRequire = this._requires[i];
			var wiredBundle = jsRequire.getWiredBundle();
			var wiredBundleExports = wiredBundle.getExports();
			for (var j=0; j < wiredBundleExports.length; j++) {
				jsExport = wiredBundleExports[j];
				if (!namedExports.hasOwnProperty(jsExport.getName()))
					namedExports[jsExport.getName()] = jsExport;
			}
		}
		
		var names = [];
		var exportName = null;
		for (exportName in namedExports) {
			if (namedExports.hasOwnProperty(exportName))
				names.push(exportName);
		}

		// this sorts the set of names we'll be importing alphabetically and
		// perhaps more importantly will allow us to create the shorter/parent dotted entries first
		names.sort();
		
		var newScope = {};
		for (i = 0; i < names.length; i++) {
			exportName = names[i];
			jsExport = namedExports[exportName];
			jsExport.addToScope(newScope);
		}

		this._evalScript(newScope);
		this._scope = newScope;
		this._state = Bundle.RESOLVED;
	},
	_evalScript : function(scope) {
		var scriptPath = this._bundleData.getHeaders()[Constants.SCRIPT_PATH_HEADER];
		if (scriptPath === null)
			scriptPath = Constants.SCRIPT_PATH_DOT;

		var script = null;
		var finalScript = "";
		var tokens = scriptPath.split(Constants.SCRIPT_PATH_DELIMITER);
		for (var i = 0; i < tokens.length; i++) {
			var token = tokens[i].replace(/^\s+|\s+$/g, '');
			if (token === Constants.SCRIPT_PATH_DOT) {
				script = this._bundleData.getHeaders()[Constants.SCRIPT_HEADER];
			} else {
				var scriptURI = this._bundleData.getEntry(token);
				script = this._framework.fetch(scriptURI);
			}
			if (script !== null)
				finalScript += script;
			
			//TODO: figure out exports
			with(scope) {
				eval(finalScript);
			}
		}
	},
	_unresolve : function() {
		if (this._state == Bundle.ACTIVE) {
			this.stop();
			markedStarted = true;
		}
		this._scope = null;

		if (this._state == Bundle.RESOLVED)
			this._state = Bundle.INSTALLED;
	},
	_parseSymbolicName : function(header) {
		var tokens = header.split(Constants.PARAMETER_DELIMITER);
		this._symbolicName = tokens[0].replace(/^\s+|\s+$/g, '');
		for ( var i = 1; i < tokens.length; i++) {
			var token = tokens[i];
			if (token.indexOf(Constants.ATTRIBUTE_EQUALS) != -1) {
				var index = token.indexOf(Constants.ATTRIBUTE_EQUALS);
				var attributeName = token.substring(0, index).replace(/^\s+|\s+$/g, '');
				if (attributeName.length === 0)
					throw "bad syntax: " + token + " in " + header;

				if (attributeName !== Constants.SINGLETON_ATTRIBUTE)
					continue;
				var value = token.substring(index + Constants.ATTRIBUTE_EQUALS.length).replace(/^\s+|\s+$/g, '');
				if (value.toLowerCase() === "true")
					this._singleton = true;
			} else
				throw "bad syntax: " + token + " in " + header;
		}
	},
	_parseVersion : function(header) {
		this._version = Version.parseVersion(header);
	},
	_parseRequires : function(header) {
		if (header === null)
			return;

		var tokens = header.split(Constants.CLAUSE_DELIMITER);
		for ( var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			var jsRequire = new JSRequire(token);
			if (jsRequire !== null)
				this._requires.push(jsRequire);
		}
	},
	_parseExports : function(header) {
		if (header === null)
			return;
		var tokens = header.split(Constants.CLAUSE_DELIMITER);
		for ( var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			var jsExport = new JSExport(token, this);
			if (jsExport !== null)
				this._exports.push(jsExport);
		}
	},
	_parseImports : function(header) {
		if (header === null)
			return;

		var tokens = header.split(Constants.CLAUSE_DELIMITER);
		for ( var i = 0; i < tokens.length; i++) {
			var token = tokens[i];
			var jsImport = new JSImport(token);
			if (jsImport !== null)
				this._imports.push(jsImport);
		}
	},
	lookup : function(name) {
		var value = this._scope;
		if (value === null || value === this.undefined_jsdtbug) { //FIXME jsdt undefined bug
			return undefined;
		}
		var names = name.split(".");
		for ( var i = 0; i < names.length; i++) {
			if (value === null || value === this.undefined_jsdtbug) { //FIXME jsdt undefined bug
				return undefined;
			}
			value = value[names[i]];
		}
		return value;
	}
};
