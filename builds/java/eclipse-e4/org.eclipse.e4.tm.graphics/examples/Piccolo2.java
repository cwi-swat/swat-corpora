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
import org.eclipse.e4.tm.builder.AbstractBuilder;
import org.eclipse.e4.tm.builder.swt.SwtBuilder;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.e4.tm.graphics2d.Canvas;
import org.eclipse.e4.tm.graphics2d.Graphics2dFactory;
import org.eclipse.e4.tm.graphics2d.Graphics2dPackage;
import org.eclipse.e4.tm.graphics2d.Layer2d;
import org.eclipse.e4.tm.graphics2d.Rect2d;
import org.eclipse.e4.tm.graphics2d.Text2d;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Piccolo2 {

	public static void main(String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		shell.addListener(SWT.Paint, new Listener() {
		
			public void handleEvent(Event event) {
				org.eclipse.swt.graphics.Rectangle rect = shell.getClientArea();
				initScene(shell, rect);
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

	private static void initScene(Shell parent, org.eclipse.swt.graphics.Rectangle rect) {
		Graphics2dPackage graphicsPackage = Graphics2dPackage.eINSTANCE;
		Canvas eParent = (Canvas)Graphics2dFactory.eINSTANCE.create(graphicsPackage.getCanvas());
		Layer2d eLayer = (Layer2d)Graphics2dFactory.eINSTANCE.create(graphicsPackage.getLayer2d());
		eLayer.setBounds(new Rectangle());
		eParent.getLayers().add(eLayer);
		
		Rect2d eRectNode = (Rect2d)Graphics2dFactory.eINSTANCE.create(graphicsPackage.getRect2d());
		eRectNode.setBounds(new Rectangle(10, 10, rect.width - 20, rect.height - 20));
		eLayer.getChildren().add(eRectNode);
		
		Text2d eTextNode = (Text2d)Graphics2dFactory.eINSTANCE.create(graphicsPackage.getText2d());
		eTextNode.setBounds(new Rectangle());
		eTextNode.setText("A text node");
		eLayer.getChildren().add(eTextNode);

		AbstractBuilder builder = new SwtBuilder();
		builder.build(eParent, parent);
		
		Composite comp = (Composite)parent.getChildren()[0];
		comp.setBounds(0, 0, rect.width, rect.height);

//		PCanvas pCanvas = builder.getObject(eParent, PCanvas.class);
//		PLayer layer = pCanvas.getCamera().getLayer(0);
//		PNode pTextNode = layer.getChild(0);
//		PNode pRectNode = layer.getChild(1);
//
//		Piccolo1.print(pCanvas.getRoot(), 0);
	}
}

/*
PRoot@64f8d4[bounds=PBounds[EMPTY],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=2,fullBoundsInvalid,pickable,childrenPickable,visible]
--PCamera@c42b5[bounds=PBounds[EMPTY],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=0,pickable,childrenPickable,visible]
--PLayer@d591a6[bounds=PBounds[EMPTY],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=2,fullBoundsInvalid,pickable,childrenPickable,visible]
----PRect@a3ce3f[stroke=org.eclipse.swt.graphics.LineAttributes@ae8af4,strokePaint=Color {0, 0, 0},bounds=PBounds[x=10.0,y=10.0,width=1180.0,height=708.0],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
----PText@d006a7[text=A text node,font=null,bounds=PBounds[x=0.0,y=0.0,width=75.0,height=16.0],fullBounds=PBounds[EMPTY],transform=null,paint=null,transparency=1.0,childrenCount=0,fullBoundsInvalid,pickable,childrenPickable,visible]
*/
