function VersionRange(minVersion, includeMin, maxVersion, includeMax) {
	if (minVersion != null && !(minVersion instanceof Version))
		throw "invalid minVersion:" + minVersion;

	if (typeof includeMin !== "boolean")
		throw "invalid includeMin:" + includeMin;

	if (maxVersion != null && !(maxVersion instanceof Version))
		throw "invalid maxVersion:" + minVersion;

	if (typeof includeMax !== "boolean")
		throw "invalid includeMax:" + includeMax;

	this._minVersion = minVersion;
	this._includeMin = includeMin;
	this._maxVersion = maxVersion;
	this._includeMax = includeMax;
}

VersionRange.prototype = {
	getMinimum : function() {
		return this._minVersion;
	},
	getIncludeMinimum : function() {
		return this._includeMin;
	},
	getMaximum : function() {
		return this._maxVersion;
	},
	getIncludeMaximum : function() {
		return this._includeMax;
	},
	equals : function(other) {
		if (this === other)
			return true;

		if (!other instanceof VersionRange)
			return false;

		if (this._includeMin !== other._includeMin || this._includeMax !== other._includeMax)
			return false;

		if (this._minVersion === null) {
			if (other._minVersion !== null)
				return false;
		} else if (!this._minVersion.equals(other._minVersion))
			return false;

		if (this._maxVersion === null) {
			if (other._maxVersion !== null)
				return false;
		} else if (!this._maxVersion.equals(other._maxVersion))
			return false;

		return true;
	},
	isIncluded : function(version) {
		if (this._minVersion === null)
			return true;
		if (version === null)
			return false;
		var minCheck = this._includeMin ? 0 : 1;
		var maxCheck = this._includeMax ? 0 : -1;
		var maxVersion = this._maxVersion ? this._maxVersion : VersionRange._MAX_VERSION;
		return version.compareTo(this._minVersion) >= minCheck && version.compareTo(maxVersion) <= maxCheck;
	},
	toString : function() {
		if (this._minVersion === null)
			return Version.EMPTY_VERSION.toString();
		if (VersionRange._MAX_VERSION.equals(this._maxVersion))
			return this._minVersion.toString();

		return (this._includeMin ? '[' : '(') + this._minVersion + ',' + this._maxVersion + (this._includeMax ? ']' : ')');
	}
};

VersionRange._MAX_VERSION = new Version(Number.MAX_VALUE, Number.MAX_VALUE, Number.MAX_VALUE, null);
VersionRange.EMPTY_RANGE = new VersionRange(Version.EMPTY_VERSION, true, VersionRange._MAX_VERSION, true);

VersionRange.parseVersionRange = function(text) {
	if (text === null)
		return VersionRange.EMPTY_RANGE;

	if (typeof text !== "string")
		throw "invalid text:" + text;

	text = text.replace(/^\s+|\s+$/g, '');
	if (text.length === 0)
		return VersionRange.EMPTY_RANGE;

	var minVersion = null;
	var includeMin = false;
	var maxVersion = null;
	var includeMax = false;

	if (text.charAt(0) === '[' || text.charAt(0) == '(') {
		var comma = text.indexOf(',');
		if (comma === -1)
			throw "invalid text:" + text;
		var last = text.charAt(text.length - 1);
		if (last !== ']' && last !== ')')
			throw "invalid text:" + text;

		minVersion = Version.parseVersion(text.substring(1, comma).replace(/^\s+|\s+$/g, ''));
		includeMin = text.charAt(0) == '[';
		maxVersion = Version.parseVersion(text.substring(comma + 1, text.length() - 1).replace(/^\s+|\s+$/g, ''));
		includeMax = last == ']';
	} else {
		minVersion = Version.parseVersion(text);
		includeMin = true;
		maxVersion = VersionRange._MAX_VERSION;
		includeMax = true;
	}
	return new VersionRange(minVersion, includeMin, maxVersion, includeMax);
};
