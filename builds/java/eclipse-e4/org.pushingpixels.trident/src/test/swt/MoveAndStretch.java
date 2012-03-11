package test.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;

public class MoveAndStretch {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Rectangle pDisplayBounds = shell.getDisplay().getBounds();
		int width = 200;
		int height = 100;
		shell.setBounds((pDisplayBounds.width - width) / 2,
				(pDisplayBounds.height - height) / 2, width, height);

		shell.setLayout(new FillLayout());

		Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_RED));

		Timeline timeline = new Timeline(shell);
		timeline.addPropertyToInterpolate(Timeline.<Point> property("location")
				.fromCurrent().to(
						new Point(pDisplayBounds.width / 2 - width,
								pDisplayBounds.height / 2 - height)));
		timeline.addPropertyToInterpolate(Timeline.<Point> property("size")
				.fromCurrent().to(new Point(2 * width, 2 * height)));
		timeline.setDuration(1000);
		timeline.playLoop(RepeatBehavior.REVERSE);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

}
