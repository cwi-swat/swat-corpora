package edu.umd.cs.piccolox.util;

import org.eclipse.e4.tm.graphics.util.Transform;


public interface MutablePoints extends Points
{
    public void setPoint(int i, double x, double y);
    public void addPoint(int pos, double x, double y);
    public void removePoints(int pos, int num);
    public void transformPoints(Transform t);
}
