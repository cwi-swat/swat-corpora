package org.eclipse.e4.ui.workbench.addons.swt;

import java.util.List;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.impl.ApplicationFactoryImpl;

public class DnDProcessor {
	@Execute
	void addDnDAddon(MApplication app) {
		List<MAddon> addons = app.getAddons();

		// Prevent multiple copies
		for (MAddon addon : addons) {
			if (addon.getContributionURI().contains("ui.workbench.addons.dndaddon.DnDAddon"))
				return;
		}

		// Insert the addon into the system
		MAddon dndAddon = ApplicationFactoryImpl.eINSTANCE.createAddon();
		dndAddon.setElementId("DnDAddon"); //$NON-NLS-1$
		dndAddon.setContributionURI("platform:/plugin/org.eclipse.e4.ui.workbench.addons.swt/org.eclipse.e4.ui.workbench.addons.dndaddon.DnDAddon"); //$NON-NLS-1$
		app.getAddons().add(dndAddon);
	}
}
