function BundleData(bundleId, location, headers) {
	this._bundleId = bundleId;
	this._location = location;
	this._headers = headers;
}

BundleData.prototype = {
	getBundleId : function() {
		return this._bundleId;
	},
	getLocation : function() {
		return this._location;
	},
	getHeaders : function() {
		return this._headers;
	}
};
