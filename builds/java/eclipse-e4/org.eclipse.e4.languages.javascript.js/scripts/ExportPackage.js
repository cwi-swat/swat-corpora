function ExportPackage(header, exportingBundle) {
	if (header === null)
		throw "header cannot be null";

	if (exportingBundle === null)
		throw "exportingBundle cannot be null";

	this._name = null;
	this._version = Version.EMPTY_VERSION;
	this._attributes = {};
	this._directives = {};
	this._mandatory = [];
	this._exportingBundle = exportingBundle;

	_parseExport(header);
}

ExportPackage.prototype = {
	_parseExport : function(header) {
		var tokens = header.split(Constants.PARAMETER_DELIMITER);
		this._name = tokens[0].replace(/^\s+|\s+$/g, '');
		for ( var i = 1; i < tokens.length; i++) {
			var token = tokens[i];
			if (token.indexOf(Constants.DIRECTIVE_EQUALS) !== -1)
				this._parseDirective(token);
			else if (token.indexOf(Constants.ATTRIBUTE_EQUALS) !== -1)
				this._parseAttribute(token);
			else
				throw "bad export syntax: " + token + " in " + header;
		}
	},
	_parseAttribute : function(token) {
		var index = token.indexOf(Constants.ATTRIBUTE_EQUALS);
		var attributeName = token.substring(0, index).replace(/^\s+|\s+$/g, '');
		if (attributeName.length === 0)
			return;

		var value = token.substring(index + Constants.ATTRIBUTE_EQUALS.length).replace(/^\s+|\s+$/g, '');

		if (attributeName === Constants.VERSION_ATTRIBUTE)
			this._version = Version.parseVersion(value);

		this._attributes[attributeName] = value;
	},
	_parseDirective : function(token) {
		var index = token.indexOf(Constants.DIRECTIVE_EQUALS);
		var directiveName = token.substring(0, index).replace(/^\s+|\s+$/g, '');
		if (directiveName.length === 0)
			return;

		var value = token.substring(index + Constants.DIRECTIVE_EQUALS.length).replace(/^\s+|\s+$/g, '');
		if (directiveName === Constants.MANDATORY_DIRECTIVE)
			this._parseMandatory(value);

		this._directives[directiveName] = value;
	},
	_parseMandatory : function(value) {
		var tokens = value.split(Constants.MANDATORY_DELIMITER);
		for ( var i = 0; i < tokens.length; i++) {
			var token = tokens[i].replace(/^\s+|\s+$/g, '');
			if (token.length > 0)
				this._mandatory.push(token);
		}
	},
	getName : function() {
		return this._name;
	},
	getVersion : function() {
		return this._version;
	},
	getBundleId : function() {
		return this._exportingBundle.getBundleId();
	},
	getBundleSymbolicName : function() {
		return this._exportingBundle.getSymbolicName();
	},
	getBundleVersion : function() {
		return this._exportingBundle.getVersion();
	},
	getAttributes : function() {
		this._attributes;
	},
	getDirectives : function() {
		return this._directives;
	},
	getExportingbundle : function() {
		return this._exportingBundle;
	},
	getMandatory : function() {
		return this._mandatory;
	},
	_addToScope : function (scope) {
		var value = this._exportingBundle.lookup(this._name);
		var tokens = this._name.split(".");
		var i = 0;
		while (true) {
			var token = tokens[i++];
			var current = null;
			if (scope.hasOwnProperty(token)) {
				current = scope[token];
			}
			if (i === tokens.length) {
				if (current === null || current === undefined) {
					if (typeof value === "object") {
						var wrappedValue = {};
						wrappedValue.prototype = value;
						value = wrappedValue;
					}
					scope[token]= value;
					return;
				}
				throw "Resolve error: " + this._name + " already exists for " + this.toString();				
			}
			if (current === null || current === undefined) {
				current = scope[token];
				if (current === null || current === undefined) {
					current = {};
				} else if (typeof current === "object") {
					var wrappedCurrent = {};
					wrappedCurrent.prototype = current;
					current = wrappedCurrent;
				} else
					throw "Resolve error: " + this._name + "-" + token + " already exists for " + this.toString(); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				scope[token] = current;
			}
			scope = current;
		}
	}
};
