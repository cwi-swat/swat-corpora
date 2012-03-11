package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.pushingpixels.trident.Timeline;

public class DefaultAccessor {
	private float value;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		SimpleDateFormat sdf = new SimpleDateFormat("ss:SSS");
		float oldValue = this.value;
		System.out.println(sdf.format(new Date()) + " : " + oldValue + " -> "
				+ value);
		this.value = value;
	}

	public static void main(String[] args) {
		final DefaultAccessor helloWorld = new DefaultAccessor();
		Timeline timeline = new Timeline(helloWorld);

		helloWorld.value = 50f;

		timeline.addPropertyToInterpolate(Timeline.<Float> property("value")
				.fromCurrent().to(100.0f));
		timeline.setDuration(300);
		timeline.play();

		try {
			Thread.sleep(1000);
		} catch (Exception exc) {
		}
	}
}
