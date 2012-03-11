package org.eclipse.e4.ui.workbench.renderers.swt;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.e4.ui.model.application.ui.MGenericTile;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class SashLayout extends Layout {

	int marginLeft = 0;
	int marginRight = 0;
	int marginTop = 0;
	int marginBottom = 0;
	int sashWidth = 4;

	MUIElement root;
	private Composite host;

	class SashRect {
		Rectangle rect;
		MGenericTile<?> container;
		MUIElement left;
		MUIElement right;

		public SashRect(Rectangle rect, MGenericTile<?> container,
				MUIElement left, MUIElement right) {
			this.container = container;
			this.rect = rect;
			this.left = left;
			this.right = right;
		}
	}

	List<SashRect> sashes = new ArrayList<SashRect>();

	boolean draggingSashes = false;
	List<SashRect> sashesToDrag;

	public SashLayout(final Composite host, MUIElement root) {
		this.root = root;
		this.host = host;

		host.addMouseTrackListener(new MouseTrackListener() {
			public void mouseHover(MouseEvent e) {
			}

			public void mouseExit(MouseEvent e) {
				host.setCursor(null);
			}

			public void mouseEnter(MouseEvent e) {
			}
		});

		host.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (!draggingSashes) {
					// Set the cursor feedback
					List<SashRect> sashList = getSashRects(e.x, e.y);
					if (sashList.size() == 0) {
						host.setCursor(host.getDisplay().getSystemCursor(
								SWT.CURSOR_ARROW));
					} else if (sashList.size() == 1) {
						if (sashList.get(0).container.isHorizontal())
							host.setCursor(host.getDisplay().getSystemCursor(
									SWT.CURSOR_SIZEWE));
						else
							host.setCursor(host.getDisplay().getSystemCursor(
									SWT.CURSOR_SIZENS));
					} else {
						host.setCursor(host.getDisplay().getSystemCursor(
								SWT.CURSOR_SIZEALL));
					}
				} else {
					adjustWeights(sashesToDrag, e.x, e.y);
					host.layout();
					host.update();
				}
			}
		});

		host.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				host.setCapture(false);
				draggingSashes = false;
			}

			public void mouseDown(MouseEvent e) {
				if (e.button != 1)
					return;

				sashesToDrag = getSashRects(e.x, e.y);
				if (sashesToDrag.size() > 0) {
					draggingSashes = true;
					host.setCapture(true);
				}
			}

			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		host.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				// for (SashRect sr : sashes) {
				// Color color;
				// if (sr.container.isHorizontal())
				// color = e.display.getSystemColor(SWT.COLOR_MAGENTA);
				// else
				// color = e.display.getSystemColor(SWT.COLOR_CYAN);
				// e.gc.setForeground(color);
				// e.gc.setBackground(color);
				// e.gc.fillRectangle(sr.rect);
				// }
			}
		});
	}

	public void setRootElemenr(MUIElement newRoot) {
		root = newRoot;
		host.layout(null, SWT.DEFER);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		if (root == null)
			return;

		Rectangle bounds = composite.getBounds();
		if (composite instanceof Shell)
			bounds = ((Shell) composite).getClientArea();
		else {
			bounds.x = 0;
			bounds.y = 0;
		}

		bounds.width -= (marginLeft + marginRight);
		bounds.height -= (marginTop + marginBottom);
		bounds.x += marginLeft;
		bounds.y += marginTop;

		sashes.clear();
		tileSubNodes(bounds, root);
	}

	protected void adjustWeights(List<SashRect> sashes, int curX, int curY) {
		for (SashRect sr : sashes) {
			int totalWeight = getWeight(sr.left) + getWeight(sr.right);
			int tenPctTotal = totalWeight / 10;

			Rectangle leftRect = getRectangle(sr.left);
			Rectangle rightRect = getRectangle(sr.right);
			if (leftRect == null || rightRect == null)
				continue;

			int leftWeight;
			int rightWeight;

			if (sr.container.isHorizontal()) {
				double left = leftRect.x;
				double right = rightRect.x + rightRect.width;
				double pct = (curX - left) / (right - left);
				leftWeight = (int) ((totalWeight * pct) + 0.5);
				if (leftWeight < tenPctTotal)
					leftWeight = tenPctTotal;
				if (leftWeight > (totalWeight - tenPctTotal))
					leftWeight = totalWeight - tenPctTotal;
				rightWeight = totalWeight - leftWeight;
			} else {
				double top = leftRect.y;
				double bottom = rightRect.y + rightRect.height;
				double pct = (curY - top) / (bottom - top);
				leftWeight = (int) ((totalWeight * pct) + 0.5);
				if (leftWeight < tenPctTotal)
					leftWeight = tenPctTotal;
				if (leftWeight > (totalWeight - tenPctTotal))
					leftWeight = totalWeight - tenPctTotal;
				rightWeight = totalWeight - leftWeight;
			}

			setWeight(sr.left, leftWeight);
			setWeight(sr.right, rightWeight);
		}
	}

	private void setWeight(MUIElement element, int weight) {
		element.setContainerData(Integer.toString(weight));
	}

	private Rectangle getRectangle(MUIElement element) {
		if (element instanceof MGenericTile<?>)
			return (Rectangle) element.getWidget();
		else if (element.getWidget() instanceof Control)
			return ((Control) (element.getWidget())).getBounds();
		return null;
	}

	protected List<SashRect> getSashRects(int x, int y) {
		List<SashRect> srs = new ArrayList<SashRect>();
		boolean inSash = false;
		for (SashRect sr : sashes) {
			if (sr.rect.contains(x, y))
				inSash = true;
		}
		if (!inSash)
			return srs;

		Rectangle target = new Rectangle(x - 5, y - 5, 10, 10);
		for (SashRect sr : sashes) {
			if (sr.rect.intersects(target))
				srs.add(sr);
		}
		return srs;
	}

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		return new Point(600, 400);
	}

	private int totalWeight(MGenericTile<?> node) {
		int total = 0;
		for (MUIElement subNode : node.getChildren()) {
			if (subNode.isToBeRendered() && subNode.isVisible())
				total += getWeight(subNode);
		}
		return total;
	}

	private void tileSubNodes(Rectangle bounds, MUIElement node) {
		if (node != root)
			setRectangle(node, bounds);

		if (!(node instanceof MGenericTile<?>))
			return;

		MGenericTile<?> sashContainer = (MGenericTile<?>) node;
		List<MUIElement> visibleChildren = getVisibleChildren(sashContainer);
		int childCount = visibleChildren.size();

		// How many pixels do we have?
		int availableWidth = sashContainer.isHorizontal() ? bounds.width
				: bounds.height;

		// Subtract off the room for the sashes
		availableWidth -= ((childCount - 1) * sashWidth);

		// Get the total of the weights
		double totalWeight = totalWeight(sashContainer);
		int tilePos = sashContainer.isHorizontal() ? bounds.x : bounds.y;

		MUIElement prev = null;
		for (MUIElement subNode : visibleChildren) {
			// Add a 'sash' between this node and the 'prev'
			if (prev != null) {
				Rectangle sashRect = sashContainer.isHorizontal() ? new Rectangle(
						tilePos, bounds.y, sashWidth, bounds.height)
						: new Rectangle(bounds.x, tilePos, bounds.width,
								sashWidth);
				sashes.add(new SashRect(sashRect, sashContainer, prev, subNode));
				host.redraw(sashRect.x, sashRect.y, sashRect.width,
						sashRect.height, false);
				tilePos += sashWidth;
			}

			// Calc the new size as a %'age of the total
			double ratio = getWeight(subNode) / totalWeight;
			int newSize = (int) ((availableWidth * ratio) + 0.5);

			Rectangle subBounds = sashContainer.isHorizontal() ? new Rectangle(
					tilePos, bounds.y, newSize, bounds.height) : new Rectangle(
					bounds.x, tilePos, bounds.width, newSize);
			tilePos += newSize;

			tileSubNodes(subBounds, subNode);
			prev = subNode;
		}
	}

	/**
	 * @param node
	 * @param bounds
	 */
	private void setRectangle(MUIElement node, Rectangle bounds) {
		if (node.getWidget() instanceof Control) {
			Control ctrl = (Control) node.getWidget();
			ctrl.setBounds(bounds);
		} else if (node instanceof MGenericTile<?>) {
			Rectangle newRect = new Rectangle(bounds.x, bounds.y, bounds.width,
					bounds.height);
			node.setWidget(newRect);
		}
	}

	private List<MUIElement> getVisibleChildren(MGenericTile<?> sashContainer) {
		List<MUIElement> visKids = new ArrayList<MUIElement>();
		for (MUIElement child : sashContainer.getChildren()) {
			if (child.isToBeRendered() && child.isVisible())
				visKids.add(child);
		}
		return visKids;
	}

	private static int getWeight(MUIElement element) {
		String info = element.getContainerData();
		if (info == null || info.length() == 0) {
			element.setContainerData(Integer.toString(100));
			info = element.getContainerData();
		}

		try {
			int value = Integer.parseInt(info);
			return value;
		} catch (NumberFormatException e) {
			return 500;
		}
	}
}
