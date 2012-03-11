function BundleContext(bundle, framework) {
	this._bundle = bundle;
	this._framework = framework;
}

BundleContext.prototype = {
	getBundle : function() {
		return this._bundle;
	},
	getBundles : function() {
		return this._framework.getBundles();
	},
	getProperty : function(name) {
		return this._framework.getProperty(name);
	},
	installBundle : function(location, headers) {
		return this._framework.installBundle(location, headers);
	}
};