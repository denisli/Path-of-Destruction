package animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import physics.Orientation;

public class Animation {

	int frameIndex = 0;
	int timeRemainingInFrame;

	private final List<Frame> frames = new ArrayList<Frame>();

	public void addFrame(Image image, int duration) {
		if (frames.isEmpty()) {
			timeRemainingInFrame = duration;
		}
		frames.add(new Frame(image, duration));
	}

	public void update(int deltaTime) {
		if (frames.isEmpty()) {
			throw new IllegalStateException(
					"Cannot update when there are no frames.");
		}
		timeRemainingInFrame -= deltaTime;
		if (timeRemainingInFrame <= 0) {
			frameIndex = (frameIndex + 1) % frames.size();
			timeRemainingInFrame = frames.get(frameIndex).duration;
		}
	}

	public Image getImage() {
		return frames.get(frameIndex).image;
	}

	public void display(Graphics g, int x, int y, Orientation orientation,
			int pointOfRotationX, int pointOfRotationY) {
		Graphics2D graphics = (Graphics2D) g;
		Image image = frames.get(frameIndex).image;
		AffineTransform transform = graphics.getTransform();
		graphics.rotate(Math.toRadians(orientation.getAngle()),
				pointOfRotationX, pointOfRotationY);
		if (orientation.isReflected()) {
			g.drawImage(image, x + image.getWidth(null), y,
					-image.getWidth(null), image.getHeight(null), null);
		} else {
			g.drawImage(image, x, y, null);
		}

		graphics.setTransform(transform);
	}

	public void reset() {
		frameIndex = 0;
	}

	private static class Frame {
		private final Image image;
		private final int duration;

		public Frame(Image image, int duration) {
			this.image = image;
			this.duration = duration;
		}

	}

}
