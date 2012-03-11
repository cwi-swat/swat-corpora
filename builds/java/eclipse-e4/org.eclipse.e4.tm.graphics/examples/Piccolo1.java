/*******************************************************************************
 * Copyright (c) 2009 Hallvard Traetteberg.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Hallvard Traetteberg - initial API and implementation
 ******************************************************************************/
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.nodes.PRoundedRect;
import edu.umd.cs.piccolox.util.BoundsAlignmentLayout;
import edu.umd.cs.piccolox.util.PNodeLocator;

public class Piccolo1 {

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		shell.addListener(SWT.Paint, new Listener() {
		
			public void handleEvent(Event event) {
				org.eclipse.swt.graphics.Rectangle rect = shell.getClientArea();
				Composite parent = new Composite(shell, SWT.NONE);
				PCanvas canvas = new PCanvas(parent);
				initScene(canvas, rect);
			}
		});
//		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public static void print(PNode pNode, int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("--");
		}
		System.out.println(pNode);
		for (int i = 0; i < pNode.getChildrenCount(); i++) {
			print(pNode.getChild(i), level + 1);
		}
	}
	
	private static void initScene(PCanvas canvas, org.eclipse.swt.graphics.Rectangle rect) {
		canvas.setBounds(rect);
		PLayer layer = new PLayer();
		canvas.addLayer(layer);
		layer.setBounds(new Rectangle(rect.x, rect.y, rect.width, rect.height));
		
		PPath rectNode = PPath.createRectangle(10, 10, rect.width - 20, rect.height - 20);
		layer.addChild(rectNode);
		
		int centerX = rect.width / 2, centerY = rect.height / 2;
		PText textNode = new PText("Hallvard");
//		textNode.setBounds(0, 0, 80, 40);
		textNode.setText("Hallvard");
		BoundsAlignmentLayout.LayoutData textLayoutData = new BoundsAlignmentLayout.LayoutData();
		textLayoutData.nodeLocator = new PNodeLocator(rectNode, PNodeLocator.EAST, PNodeLocator.SOUTH);
		textLayoutData.childLocator = new PNodeLocator(textNode, PNodeLocator.EAST, PNodeLocator.SOUTH);
		textNode.setLayoutData(textLayoutData);
		layer.addChild(textNode);
		
		PImage imageNode = new PImage("/Users/hal/Pictures/hal-headonly-64x64.png");
//		imageNode.setTransform(new Transform().scale(2.0, 2.0));
		BoundsAlignmentLayout.LayoutData imageLayoutData = new BoundsAlignmentLayout.LayoutData();
		imageLayoutData.nodeLocator = new PNodeLocator(textNode, PNodeLocator.WEST, PNodeLocator.NORTH);
		imageLayoutData.childLocator = new PNodeLocator(imageNode, PNodeLocator.EAST, PNodeLocator.SOUTH);
		imageNode.setLayoutData(imageLayoutData);
		layer.addChild(imageNode);
		
		PPath roundedRectNode1 = PPath.createRoundedRectangle(centerX - 100, centerY + 50, 50, 80, 8);
		roundedRectNode1.setPaint(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		roundedRectNode1.setStrokePaint(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		roundedRectNode1.setStroke(new LineAttributes(5));
		layer.addChild(roundedRectNode1);
		PRoundedRect roundedRectNode2 = new PRoundedRect();
		roundedRectNode2.setBounds(centerX - 100, centerY + 150, 50, 80);
		roundedRectNode2.setCornerSize(8);
		roundedRectNode2.setPaint(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		roundedRectNode2.setStrokePaint(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
		roundedRectNode2.setStroke(new LineAttributes(12));
		layer.addChild(roundedRectNode2);
		
		layer.setLayout(new BoundsAlignmentLayout());
		
//		print(canvas.getRoot(), 0);
	}
}

/*
PRoot@2f729e[bounds=PBounds[EMPTY],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=2,fullBoundsInvalid,pickable,childrenPickable,visible]
--PCamera@deb5f[bounds=PBounds[x=0.0,y=0.0,width=1200.0,height=728.0],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
--PLayer@d7b7d9[bounds=PBounds[EMPTY],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=5,fullBoundsInvalid,pickable,childrenPickable,visible]
----PPath@671f95[path=Path {org.eclipse.swt.internal.cocoa.NSBezierPath{1618384}},stroke=org.eclipse.swt.graphics.LineAttributes@6917ee,strokePaint=Color {0, 0, 0},bounds=PBounds[x=9.5,y=9.5,width=1182.0,height=710.0],fullBounds=PBounds[EMPTY],transform=null,paint=Color {255, 255, 255},transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
----PText@4a9a7d[text=Hallvard,font=null,bounds=PBounds[x=600.0,y=242.0,width=55.0,height=16.0],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
----PImage@e4a47e[image=Image {org.eclipse.swt.internal.cocoa.NSImage{1618480}},bounds=PBounds[x=0.0,y=0.0,width=64.0,height=64.0],fullBounds=PBounds[EMPTY],transform=x'=2.0*x+0.0*y+600.0;y'=0.0*x+2.0*y+364.0,paint=null,transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
----PPath@eb5666[path=Path {org.eclipse.swt.internal.cocoa.NSBezierPath{1649280}},stroke=org.eclipse.swt.graphics.LineAttributes@754699,strokePaint=Color {0, 0, 255},bounds=PBounds[x=497.5,y=411.5,width=56.0,height=86.0],fullBounds=PBounds[EMPTY],transform=null,paint=Color {255, 255, 0},transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
----PRoundedRect@6e1dec[stroke=org.eclipse.swt.graphics.LineAttributes@6ea269,strokePaint=Color {0, 255, 0},bounds=PBounds[x=500.0,y=514.0,width=50.0,height=80.0],fullBounds=PBounds[EMPTY],transform=null,paint=Color {255, 0, 0},transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
*/
