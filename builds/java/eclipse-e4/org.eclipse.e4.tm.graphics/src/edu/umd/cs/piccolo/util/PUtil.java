/*
 * Copyright (c) 2002-@year@, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the University of Maryland nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Piccolo was written at the Human-Computer Interaction Laboratory www.cs.umd.edu/hcil by Jesse Grosjean
 * under the supervision of Ben Bederson. The Piccolo website is www.cs.umd.edu/hcil/piccolo.
 */
package edu.umd.cs.piccolo.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.widgets.Display;

/**
 * <b>PUtil</b> util methods for the Piccolo framework.
 * <P>
 * @version 1.0
 * @author Jesse Grosjean
 */
public class PUtil {
	
	public static Iterator NULL_ITERATOR = Collections.EMPTY_LIST.iterator();
	public static Enumeration NULL_ENUMERATION = new Enumeration() {
        public boolean hasMoreElements() { return false; }
        public Object nextElement() { return null; }
	};
	public static long DEFAULT_ACTIVITY_STEP_RATE = 20;
	public static int ACTIVITY_SCHEDULER_FRAME_DELAY = 10;
		
	public static OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
		public void close() { }
		public void flush() { }
		public void write(byte[] b) { }
		public void write(byte[] b, int off, int len) { }
		public void write(int b) { }
	};	
	
	public static void writeStroke(LineAttributes aStroke, ObjectOutputStream out) throws IOException {
		if (aStroke instanceof Serializable) {
			out.writeBoolean(true);
			out.writeObject(aStroke);
		} else {
			out.writeBoolean(false);
			float[] dash = aStroke.dash;
			
			if (dash == null) {
				out.write(0);
			} else {
				out.write(dash.length);
				for (int i = 0; i < dash.length; i++) {
					out.writeFloat(dash[i]);
				}
			}
						
			out.writeFloat(aStroke.width);
			out.writeInt(aStroke.cap);
			out.writeInt(aStroke.join);
			out.writeInt(aStroke.style);
			out.writeFloat(aStroke.miterLimit);			
			out.writeFloat(aStroke.dashOffset);
		}
	}
	
	public static LineAttributes readStroke(ObjectInputStream in) throws IOException, ClassNotFoundException {
		boolean serializedStroke = in.readBoolean();
		if (serializedStroke) {
			return (LineAttributes) in.readObject();
		} else {
			float[] dash = null;
			int dashLength = in.read();
			
			if (dashLength != 0) {
				dash = new float[dashLength];
				for (int i = 0; i < dashLength; i++) {
					dash[i] = in.readFloat();
				}
			}
			
			float lineWidth = in.readFloat();
			int endCap = in.readInt();
			int lineJoin = in.readInt();
			int style = in.readInt();
			float miterLimit = in.readFloat();
			float dashPhase = in.readFloat();
			
			return new LineAttributes(lineWidth, endCap, lineJoin, style, dash, dashPhase, miterLimit);
		}
	}
	
	private static final int PATH_IS_DONE = -1;

	public static Path readPath(ObjectInputStream in) throws IOException, ClassNotFoundException {
		PathData pathData = new PathData();
		pathData.types = new byte[in.readInt()];
		for (int i = 0; i < pathData.types.length; i++) {
			pathData.types[i] = in.readByte();
		}
		pathData.points = new float[in.readInt()];
		for (int i = 0; i < pathData.points.length; i++) {
			pathData.points[i] = in.readFloat();
		}
		return new Path(Display.getCurrent());
	}
	
	public static void writePath(Path path, ObjectOutputStream out) throws IOException {
		PathData pathData = path.getPathData();
		out.writeInt(pathData.types.length);
		for (int i = 0; i < pathData.types.length; i++) {
			out.writeByte(pathData.types[i]);
		}
		out.writeInt(pathData.points.length);
		for (int i = 0; i < pathData.points.length; i++) {
			out.writeFloat(pathData.points[i]);
		}
	}
}
