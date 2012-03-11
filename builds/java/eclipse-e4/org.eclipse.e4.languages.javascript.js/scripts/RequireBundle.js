function RequireBundle(header) {
	if (header === null)
		throw "header cannot be null";

	this._name = null;
	this._attributes = {};
	this._directives = {};
	this._optional = false;
	this._bundleVersionRange = VersionRange.emptyRange;
	this._wiredBundle = null;

	_parseRequire(header);
}

RequireBundle.prototype = {
	_parseRequire : function(header) {
		var tokens = header.split(Constants.PARAMETER_DELIMITER);
		this._name = tokens[0].replace(/^\s+|\s+$/g, '');
		for ( var i = 1; i < tokens.length; i++) {
			var token = tokens[i];
			if (token.indexOf(Constants.DIRECTIVE_EQUALS) !== -1)
				this._parseDirective(token);
			else if (token.indexOf(Constants.ATTRIBUTE_EQUALS) !== -1)
				this._parseAttribute(token);
			else
				throw "bad import syntax: " + token + " in " + header;
		}
	},
	_parseAttribute : function(token) {
		var index = token.indexOf(Constants.ATTRIBUTE_EQUALS);
		var attributeName = token.substring(0, index).replace(/^\s+|\s+$/g, '');
		if (attributeName.length === 0)
			return;

		var value = token.substring(index + Constants.ATTRIBUTE_EQUALS.length).replace(/^\s+|\s+$/g, '');

		if (attributeName === Constants.BUNDLE_VERSION_ATTRIBUTE)
			this._bundleVersionRange = VersionRange.parseVersionRange(value);

		this._attributes[attributeName] = value;
	},
	_parseDirective : function(token) {
		var index = token.indexOf(Constants.DIRECTIVE_EQUALS);
		var directiveName = token.substring(0, index).replace(/^\s+|\s+$/g, '');
		if (directiveName.length === 0)
			return;

		var value = token.substring(index + Constants.DIRECTIVE_EQUALS.length).replace(/^\s+|\s+$/g, '');
		if (directiveName === Constants.RESOLUTION_DIRECTIVE && value === Constants.RESOLUTION_OPTIONAL)
			this._optional = true;
		this._directives[directiveName] = value;
	},
	getName : function() {
		return this._name;
	},
	getBundleVersionRange : function() {
		return this._bundleVersionRange;
	},
	isOptional : function() {
		return this._optional;
	},
	getAttributes : function() {
		this._attributes;
	},
	getDirectives : function() {
		return this._directives;
	},
	wire : function(candidate) {
		if (this._name !== candidate.getSymbolicName())
			return false;

		if (!this._checkAttributes(candidate))
			return false;

		this._wiredExport = candidate;
		return true;
	},
	_checkAttributes : function(candidate) {
		for ( var key in this._attributes) {
			if (key === Constants.BUNDLE_VERSION_ATTRIBUTE) {
				if (!this._bundleVersionRange.isIncluded(candidate.getVersion()))
					return false;
			}
		}
		return true;
	},
	getWiredExport : function() {
		return this._wiredExport;
	},
	unwire : function() {
		this._wiredExport = null;
	}
};
