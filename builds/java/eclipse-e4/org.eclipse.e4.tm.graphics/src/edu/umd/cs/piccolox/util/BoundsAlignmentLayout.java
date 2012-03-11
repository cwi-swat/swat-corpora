package edu.umd.cs.piccolox.util;

import edu.umd.cs.piccolo.PNode;

public class BoundsAlignmentLayout extends PLocatorLayout {

	public BoundsAlignmentLayout() {
		super();
	}

	protected boolean prepareLocators(PNode parent, PNode child) {
		if (! (child.getLayoutData() instanceof LayoutData)) { 
			return false;
		}
		LayoutData layoutData = (LayoutData)child.getLayoutData();
		nodeLocator = layoutData.nodeLocator;
		childLocator = layoutData.childLocator;
		return true;
	}
}
