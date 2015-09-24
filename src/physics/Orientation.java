package physics;

public class Orientation {

	// Reflected meaning that it is horizontally reflected.
	private final boolean reflected;
	// Angle is in degrees rotated clockwise.
	private final double angle;

	public Orientation(double angle) {
		this(false, angle);
	}

	public Orientation(boolean reflected, double angle) {
		this.reflected = reflected;
		this.angle = angle;
	}

	public boolean isReflected() {
		return reflected;
	}

	public double getAngle() {
		return angle;
	}

}
