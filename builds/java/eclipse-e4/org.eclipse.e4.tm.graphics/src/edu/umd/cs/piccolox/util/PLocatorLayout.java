package edu.umd.cs.piccolox.util;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PLayout;
import edu.umd.cs.piccolo.util.PLocator;

public abstract class PLocatorLayout implements PLayout {

	protected PLocator nodeLocator;
	protected PLocator childLocator;

	public static class LayoutData {
		public PLocator nodeLocator;
		public PLocator childLocator;
	}

	public void layoutChildren(PNode parent) {
		for (int i = 0; i < parent.getChildrenCount(); i++) {
			layoutChild(parent, parent.getChild(i));
//			System.out.println(parent.getChild(i).getBoundsReference() + ": " + parent.getChild(i));
		}
	}

	protected boolean prepareLocators(PNode parent, PNode child) {
		if (child.getLayoutData() instanceof LayoutData) {
			this.childLocator = ((LayoutData)child.getLayoutData()).childLocator;
			return this.childLocator != null;
		}
		return false;
	}

	protected void layoutChild(PNode parent, PNode child) {
		if (prepareLocators(parent, child) && nodeLocator != null && childLocator != null) {
			double px = nodeLocator.locateX(), py = nodeLocator.locateY();
			double cx = childLocator.locateX(), cy = childLocator.locateY();
			child.setBounds(child.getX() + px - cx, child.getY() + py - cy, child.getWidth(), child.getHeight());
		}
	}
}
