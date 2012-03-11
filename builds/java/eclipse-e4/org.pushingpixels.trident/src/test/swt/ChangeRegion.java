package test.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class ChangeRegion {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display, SWT.NO_TRIM);
		final Rectangle pDisplayBounds = shell.getDisplay().getBounds();
		final int width = 400;
		final int height = 200;
		shell.setBounds((pDisplayBounds.width - width) / 2,
				(pDisplayBounds.height - height) / 2, width, height);

		shell.setLayout(new FillLayout());

		Canvas canvas = new Canvas(shell, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_RED));

		Timeline timeline = new Timeline(shell);
		timeline.addCallback(new UIThreadTimelineCallbackAdapter() {
			@Override
			public void onTimelinePulse(float durationFraction,
					float timelinePosition) {
				int currWidth = (int) (width / (1 + timelinePosition));
				int currHeight = (int) (height / (1 + timelinePosition));
				Region region = new Region();
				region.add((width - currWidth) / 2, (height - currHeight) / 2,
						currWidth, currHeight);
				shell.setRegion(region);
				region.dispose();
			}
		});
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
