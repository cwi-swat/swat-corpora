function Version(major, minor, micro, qualifier) {
	if (typeof major !== "number" || major < 0)
		throw "invalid major:" + major;

	if (typeof minor !== "number" || minor < 0)
		throw "invalid minor:" + minor;

	if (typeof micro !== "number" || micro < 0)
		throw "invalid micro:" + micro;

	if (qualifier === null || qualifier === undefined)
		qualifier = "";
	else if (typeof qualifier !== "string" || !qualifier.match(/[\w\-]*/))
		throw "invalid qualifier: " + qualifier;

	this._major = major;
	this._minor = minor;
	this._micro = micro;
	this._qualifier = qualifier;
};

Version.prototype = {
	getMajor : function() {
		return this._major;
	},
	getMinor : function() {
		return this._minor;
	},
	getMicro : function() {
		return this._micro;
	},
	getQualifier : function() {
		return this._qualifier;
	},
	toString : function() {
		var result = "" + this._major + "." + this._minor + "." + this._micro;
		if (this._qualifier.length > 0)
			result += "." + this._qualifier;
		return result;
	},
	equals : function(other) {
		return other instanceof Version && this._major === other._major && this._minor === other._minor && this._micro === other._micro && this._qualifier === other._qualifier;
	},
	compareTo : function(other) {
		if (this === other)
			return 0;

		var result = this._major - other._major;
		if (result !== 0)
			return result;

		result = this._minor - other._minor;
		if (result !== 0)
			return result;

		result = this._micro - other._micro;
		if (result !== 0)
			return result;

		if (this._qualifier === other._qualifier)
			return 0;

		return this._qualifier > other._qualifier ? 1 : -1;
	}
};

Version.EMPTY_VERSION = new Version(0, 0, 0, null);
Version.parseVersion = function(text) {
	if (text === null)
		return Version.EMPTY_VERSION;

	if (typeof text !== "string")
		throw "invalid text:" + text;

	text = text.replace(/^\s+|\s+$/g, '');
	if (text.length === 0 || text === "0.0.0")
		return Version.EMPTY_VERSION;

	var tokens = text.split(".");
	if (tokens.length > 4)
		throw "invalid format: " + text;

	if (!tokens[0].matches(/[\d]+/))
		throw "invalid format: " + text;

	var major = parseInt(tokens[0]);
	var minor = 0;
	var micro = 0;
	var qualifier = "";

	if (tokens.length > 1) {
		if (!tokens[1].matches(/[\d]+/))
			throw "invalid format: " + text;
		minor = parseInt(tokens[1]);

		if (tokens.length > 2) {
			if (!tokens[2].matches(/[\d]+/))
				throw "invalid format: " + text;
			micro = parseInt(tokens[2]);

			if (tokens.length > 3)
				qualifier = tokens[3];
		}
	}
	return new Version(major, minor, micro, qualifier);
};
