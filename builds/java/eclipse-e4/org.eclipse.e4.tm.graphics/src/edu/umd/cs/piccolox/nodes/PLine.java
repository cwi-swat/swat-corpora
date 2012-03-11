package edu.umd.cs.piccolox.nodes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PUtil;
import edu.umd.cs.piccolox.util.MutablePoints;
import edu.umd.cs.piccolox.util.Points;
import edu.umd.cs.piccolox.util.XYArray;

/** 
 * <b>PLine</b> a class for drawing multisegment lines. 
 * Submitted by Hallvard Traetteberg.
 */
public class PLine extends PNode {	
	
	private static final PAffineTransform TEMP_TRANSFORM = new PAffineTransform();
	private static final LineAttributes DEFAULT_STROKE = new LineAttributes(1.0f);
	private static final RGB DEFAULT_STROKE_PAINT = new RGB(0, 0, 0);
	
	private transient MutablePoints line;
	private LineAttributes stroke;
	private Color strokePaint;

	public PLine(MutablePoints line) {
		strokePaint = createColor(DEFAULT_STROKE_PAINT);
		stroke = DEFAULT_STROKE;
        if (line == null) {
            line = new XYArray();
        }
		this.line = line;
	}

    public PLine() {
        this(null);
    }

	public PLine(MutablePoints line, LineAttributes aStroke) {
		this(line);
		stroke = aStroke;
	}
	
	//****************************************************************
	// Stroke
	//****************************************************************
	
	public Color getStrokePaint() {
		return strokePaint;
	}

	public void setStrokePaint(Color aPaint) {
		Color old = strokePaint;
		strokePaint = aPaint;
		invalidatePaint();
		firePropertyChange(PPath.PROPERTY_CODE_STROKE_PAINT, PPath.PROPERTY_STROKE_PAINT, old, strokePaint);
	}
	
	public LineAttributes getStroke() {
		return stroke;
	}

	public void setStroke(LineAttributes aStroke) {
		LineAttributes old = stroke;
		stroke = aStroke;
		updateBoundsFromLine();
		invalidatePaint();
		firePropertyChange(PPath.PROPERTY_CODE_STROKE, PPath.PROPERTY_STROKE, old, stroke);
	}
		
	//****************************************************************
	// Bounds
	//****************************************************************

	public boolean setBounds(double x, double y, double width, double height) {
		if (line == null || !super.setBounds(x, y, width, height)) {
			return false;
		}

		Rectangle lineBounds = line.getBounds(new Rectangle());
		Rectangle lineStrokeBounds = getLineBoundsWithStroke();
		double strokeOutset = Math.max(lineStrokeBounds.getWidth() - lineBounds.getWidth(), 
                                       lineStrokeBounds.getHeight() - lineBounds.getHeight());
		
		x += strokeOutset / 2;
		y += strokeOutset / 2;
		width -= strokeOutset;
		height -= strokeOutset;
		
		TEMP_TRANSFORM.setToIdentity();
		TEMP_TRANSFORM.translate(x, y);
		TEMP_TRANSFORM.scale(width / lineBounds.getWidth(), height / lineBounds.getHeight());
		TEMP_TRANSFORM.translate(-lineBounds.getX(), -lineBounds.getY());		
        line.transformPoints(TEMP_TRANSFORM);
		
		return true;
	}

	//TODO
	
	private Rectangle getLineBounds(Points line, Rectangle dest, LineAttributes stroke) {
		if (dest == null) {
			dest = new Rectangle();
		}
		line.getBounds(dest);
		PPath.adjustForStroke(dest, stroke);
		return dest;
	}

	public Rectangle getLineBoundsWithStroke() {
		return getLineBounds(line, null, stroke);
	}
			
	public void updateBoundsFromLine() {
		if (line.getPointCount() == 0) {
			resetBounds();
		} else {
			super.setBounds(getLineBoundsWithStroke());
		}
	}
	
	//****************************************************************
	// Painting
	//****************************************************************
	
	protected void paint(PPaintContext paintContext) {
		GC g2 = paintContext.getGraphics();
		
		if (stroke != null && strokePaint != null && line.getPointCount() > 0) {
			g2.setForeground(strokePaint);
			g2.setLineAttributes(stroke);
			double x1 = line.getX(0), y1 = line.getY(0);
			for (int i = 1; i < line.getPointCount(); i++) {
				double x2 = line.getX(i), y2 = line.getY(i);
				g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
				x1 = x2;
				y1 = y2;
			}
		}		
	}

	public Points getLineReference() {
		return line;
	}

    public int getPointCount() {
        return line.getPointCount();
    }

    public Point getPoint(int i, Point dst) {
        if (dst == null) {
            dst = new Point();
        }
        return line.getPoint(i, dst);
    }

    protected void lineChanged() {
        firePropertyChange(PPath.PROPERTY_CODE_PATH, PPath.PROPERTY_PATH, null, line);
        updateBoundsFromLine();
        invalidatePaint();
    }
    
	public void setPoint(int i, double x, double y) {
        line.setPoint(i, x, y);
        lineChanged();
	}

    public void addPoint(int i, double x, double y) {
        line.addPoint(i, x, y);
        lineChanged();
    }

    public void removePoints(int i, int n) {
        line.removePoints(i, n);
        lineChanged();
    }

	public void removeAllPoints() {
        line.removePoints(0, line.getPointCount());
        lineChanged();
	}
	
	//****************************************************************
	// Serialization
	//****************************************************************
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		PUtil.writeStroke(stroke, out); 	   
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();		
		stroke = PUtil.readStroke(in);
	}
}
