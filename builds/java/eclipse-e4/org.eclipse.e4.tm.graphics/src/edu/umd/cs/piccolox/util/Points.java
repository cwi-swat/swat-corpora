package edu.umd.cs.piccolox.util;

import org.eclipse.e4.tm.graphics.util.Point;
import org.eclipse.e4.tm.graphics.util.Rectangle;

public interface Points
{
    public int getPointCount();

    public double getX(int i);
    public double getY(int i);
    public Point getPoint(int i, Point dst);

    public Rectangle getBounds(Rectangle dst);
}
