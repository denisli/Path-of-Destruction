package characters;

import gameobject.GameObjectPool;
import physics.Point;

public abstract class GroundCharacter extends GameCharacter {

	/**
	 * The reference point is the base point. Base point could be considered the
	 * center between the character's two feet. Orientation is with respect to
	 * that point.
	 * 
	 * @param x
	 * @param y
	 * @param defaultMovingSpeed
	 * @param defaultJumpingSpeed
	 */
	public GroundCharacter(GameObjectPool gameObjectPool, int x, int y,
			float defaultMovingSpeed, float defaultJumpingSpeed) {
		super(gameObjectPool, x, y, defaultMovingSpeed, defaultJumpingSpeed);
	}

	public Point getBasePoint() {
		return new Point(getX(), getY());
	}

}
