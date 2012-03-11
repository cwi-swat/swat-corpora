function Framework() {
	this._installOrderBundles = [];
	this._resolveOrderBundles = [];
	this._exports = {};
	this._requiredBundles = {};
	this._properties = {};
	this._currentBundleId = 0;
}

Framework.prototype = {
	getBundles : function() {
		return this._installOrderBundles.slice();
	},
	getBundleContext : function(bundle) {
		return new BundleContext(bundle, this);
	},
	getProperty : function(name) {
		return this._properties[name];
	},
	setProperty : function(name, value) {
		this._properties[name] = value;
	},
	installBundle : function(location, headers) {
		if (location == null)
			throw "location cannot be null";

		var bundleData = new BundleData(this._currentBundleId, location, headers);
		var bundle = new Bundle(this, bundleData);

		for ( var i = 0; i < this._installOrderBundles.length; i++) {
			if (bundle.equals(this._installOrderBundles[i]))
				return this._installOrderBundles[i];
		}
		this._installOrderBundles[this._currentBundleId] = bundle;
		this._currentBundleId++;

		return bundle;
	},
	refresh : function() {
		var uninstalledBundleFound = false;
		var resolvedBundles = this._resolveOrderBundles.slice();
		var i = 0;
		var bundle = null;
		for (i = 0; i < resolvedBundles.length; i++) {
			bundle = resolvedBundles[i];
			if (bundle.getState() === Bundle.UNINSTALLED) {
				uninstalledBundleFound = true;
			}

			if (uninstalledBundleFound) {
				this._unresolveBundle(bundle);
			}
		}
		if (uninstalledBundleFound) {
			var newInstallOrderBundles = [];
			for (i = 0; i < this._installOrderBundles.length; i++) {
				bundle = this._installOrderBundles[i];
				if (bundle.getState() !== Bundle.UNINSTALLED) {
					newInstallOrderBundles.push(bundle);
				}
			}
			this._installOrderBundles = newInstallOrderBundles;

			var newResolveOrderBundles = [];
			for (i = 0; i < this._resolveOrderBundles.length; i++) {
				bundle = this._resolveOrderBundles[i];
				if (bundle.getState() === Bundle.RESOLVED || bundle.getState() === Bundle.ACTIVE) {
					newResolveOrderBundles.push(bundle);
				}
			}
			this._resolveOrderBundles = newResolveOrderBundles;

			this.resolve();
		}
	},
	resolve : function() {
		var unresolvedBundles = [];
		var i = 0;
		for (i = 0; i < this._installOrderBundles.length; i++) {
			var bundle = this._installOrderBundles[i];
			if (bundle.getState() === Bundle.INSTALLED)
				unresolvedBundles.push(bundle);
		}

		var resolvedBundles = [];
		var resolvedBundle = null;
		while (unresolvedBundles.length !== 0) {
			resolvedBundle = this._stepResolver(unresolvedBundles);
			if (resolvedBundle !== null)
				resolvedBundles.push(resolvedBundle);
			else
				break;
		}
		for (i = 0; i < resolvedBundles; i++) {
			resolvedBundle = resolvedBundles[i];
			if (resolvedBundle.isMarkedStarted())
				resolvedBundle.start();
		}
	},
	_stepResolver : function(unresolvedBundles) {
		for (var i = 0; i < unresolvedBundles.length; i++) {
			var bundle = unresolvedBundles[i];
			if (this._resolveBundle(bundle)) {
				this._resolveOrderBundles.push(bundle);
				unresolvedBundles.splice(i,1);
				return bundle;
			}
		}
		return null;
	},
	_resolveBundle: function(bundle) {
		if (bundle.isSingleton() && this._requiredBundles[bundle.getSymbolicName()])
			return false;

		if (this._wire(bundle)) {
			bundle.resolve();
			this._addExports(bundle);
			this._addRequiredBunde(bundle);
			return true;
		}
		this._unwire(bundle);
		return false;
	},
	_wire : function(bundle) {
		return this._wireRequires(bundle) && this._wireImports(bundle);
	},
	_wireRequires : function(bundle) {
		var requires = bundle.getRequires();
		for (var i = 0; i < requires.length; i++) {
			var jsRequire = requires[i];
			var name = jsRequire.getName();
			var candidates = this._requiredBundles[name];
			if (! candidates)
				return false;

			var satisfied = false;
			for (var j = 0; j < candidates.length; j++) {
				var candidate = candidates[j];
				satisfied = jsRequire.wire(candidate);
				if (satisfied)
					break;
			}
			if (!satisfied && !jsRequire.isOptional())
				return false;
		}
		return true;
	},
	_wireImports : function(bundle) {
		var imports = bundle.getImports();
		for (var i = 0; i < imports.length; i++) {
			var jsImport = imports[i];
			var name = jsImport.getName();
			var candidates = this._exports[name];
			if (!candidates)
				return false;

			var satisfied = false;
			for (var j = 0; j < candidates.length; j++) {
				var candidate = candidates[j];
				satisfied = jsImport.wire(candidate);
				if (satisfied)
					break;
			}
			if (!satisfied && !jsImport.isOptional())
				return false;
		}
		return true;
	},
	_addExports : function(bundle) {
		for (var i = 0; i < bundle.getExports().length; i++) {
			var jsExport = bundle.getExports()[i];
			var name = jsExport.getName();
			var namedExports = this._exports[name];
			if (!namedExports) {
				namedExports = [];
				this._exports[name] = namedExports;
			}
			namedExports.push(jsExport);
			namedExports.sort(Framework._exportsComparator);
		}
	},
	_addRequiredBunde : function(bundle) {
		var name = bundle.getSymbolicName();
		var namedBundles = this._requiredBundles[name];
		if (! namedBundles) {
			namedBundles = [];
			this._requiredBundles[name]= namedBundles;
		}
		namedBundles.push(bundle);
		namedBundles.sort(Framework._requireBundlesComparator);
	},
	_unresolveBundle : function(bundle) {
		this._removeExports(bundle);
		this._removeRequiredBunde(bundle);
		bundle._unresolve();
		this._unwire(bundle);
	},
	_removeExports : function(bundle) {
		var exports = bundle._getExports();
		for ( var i = 0; i < exports.length; i++) {
			var jsExport = exports[i];
			var name = jsExport.getName();
			var namedExports = this._exports[name];
			if (!namedExports)
				continue;

			for ( var j = 0; j < namedExports.length; j++) {
				if (namedExports[j] === jsExport) {
					namedExports.splice(j, 1);
					break;
				}
			}
			if (namedExports.length === 0)
				delete this._exports[name];
		}
	},
	_removeRequiredBunde : function(bundle) {
		var name = bundle.getSymbolicName();
		var namedBundles = this._requiredBundles[name];
		if (!namedBundles)
			return;

		for ( var i = 0; i < namedExports.length; i++) {
			if (namedBundles[i] === bundle) {
				namedBundles.splice(i, 1);
				break;
			}
		}
		if (namedBundles.length === 0)
			delete this._requiredBundles[name];
	},
	_unwire : function(bundle) {
		this._unwireImports(bundle);
		this._unwireRequires(bundle);
	},
	_unwireImports : function(bundle) {
		var imports = bundle.getImports();
		for ( var i = 0; i < imports.length; i++) {
			var jsImport = imports[i];
			jsImport.unwire();
		}
	},
	_unwireRequires : function(bundle) {
		var requires = bundle.getRequires();
		for ( var i = 0; i < requires.length; i++) {
			var jsRequires = requires[i];
			jsRequires.unwire();
		}
	},
	shutdown : function() {
		var reversed = this._installOrderBundles.slice().reverse();
		for ( var i = 0; i < reversed.length; i++) {
			var bundle = reversed[i];
			bundle.stop();
			bundle.uninstall();
		}
		this.refresh();
	}
};

Framework._exportsComparator = function(export0, export1) {
	// order switched for descending order
	var result = export1.getVersion().compareTo(export0.getVersion());
	if (result == 0)
		result = export0.getBundleId() - export1.getBundleId();

	return result;
};

Framework._requireBundlesComparator = function(bundle0, bundle1) {
	// order switched for descending order
	var result = bundle1.getVersion().compareTo(bundle0.getVersion());
	if (result == 0)
		result = bundle0.getBundleId() - bundle1.getBundleId();

	return result;
};
