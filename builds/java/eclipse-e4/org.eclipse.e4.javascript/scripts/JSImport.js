function JSImport(header) {
	if (header === null)
		throw "header cannot be null";

	this._name = null;
	this._versionRange = VersionRange.EMPTY_RANGE;
	this._bundleSymbolicName = null;
	this._bundleVersionRange = VersionRange.EMPTY_RANGE;
	this._optional = false;
	this._attributes = {};
	this._directives = {};
	this._wiredExport = null;

	_parseImport(header);
}

JSImport.prototype = {
	_parseImport : function(header) {
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

		if (attributeName === Constants.VERSION_ATTRIBUTE)
			this._versionRange = VersionRange.parseVersionRange(value);
		else if (attributeName === Constants.BUNDLE_SYMBOLIC_NAME_ATTRIBUTE)
			this._bundleSymbolicName = value;
		else if (attributeName === Constants.BUNDLE_VERSION_ATTRIBUTE)
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
	getVersionRange : function() {
		return this._versionRange;
	},
	getBundleSymbolicName : function() {
		return this._bundleSymbolicName;
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
		if (this._name !== candidate.getName())
			return false;

		if (!this._checkAttributes(candidate))
			return false;

		if (!this._checkMandatory(candidate))
			return false;

		this._wiredExport = candidate;
		return true;
	},
	_checkAttributes : function(candidate) {
		for ( var key in this._attributes) {
			if (key === Constants.VERSION_ATTRIBUTE) {
				if (!this._versionRange.isIncluded(candidate.getVersion()))
					return false;
			} else if (key === Constants.BUNDLE_SYMBOLIC_NAME_ATTRIBUTE) {
				if (!this._bundleSymbolicName === candidate.getBundleSymbolicName())
					return false;
			} else if (key === Constants.BUNDLE_VERSION_ATTRIBUTE) {
				if (!this._bundleVersionRange.isIncluded(candidate.getBundleVersion()))
					return false;
			} else {
				var value = this._attributes[key];
				var attributeValue = candidate.getAttributes()[key];
				if (attributeValue !== value)
					return false;
			}
		}
		return true;
	},
	_checkMandatory : function(candidate) {
		for ( var i = 0; i < candidate.getMandatory().length; i++) {
			var key = candidate.getMandatory()[i];
			var mandatoryValue = candidate.getAttributes()[key];
			var value = this._attributes[key];
			if (value !== mandatoryValue)
				return false;
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
