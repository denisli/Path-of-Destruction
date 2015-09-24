package physics;

public class PhysicsUtil {

	public static int dist(int x, int y) {
		return (int) Math.sqrt(x * x + y * y);
	}

	/**
	 * Rotates a point clockwise by angle (in degrees) about a given center
	 * point.
	 */
	public static Point rotate(Point center, Point rotated, double angle) {
		int translatedX = rotated.getX() - center.getX();
		int translatedY = rotated.getY() - center.getY();
		double cos = Math.cos(Math.toRadians(angle));
		double sin = Math.sin(Math.toRadians(angle));
		int translatedRotatedX = (int) (translatedX * cos - translatedY * sin);
		int translatedRotatedY = (int) (translatedX * sin + translatedY * cos);
		int rotatedX = translatedRotatedX + center.getX();
		int rotatedY = translatedRotatedY + center.getY();
		return new Point(rotatedX, rotatedY);
	}

}
