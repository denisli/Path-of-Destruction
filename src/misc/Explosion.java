package misc;

import gameobject.GameObject;
import gameobject.GameObjectPool;

public abstract class Explosion extends GameObject {

	/**
	 * @param x
	 *            x-coordinate of center of explosion
	 * @param y
	 *            y-coordinate of center of explosion
	 */
	public Explosion(GameObjectPool gameObjectPool, int x, int y) {
		super(gameObjectPool, x, y);
	}

	public abstract void phaseOut(int deltaTime);

	public abstract boolean contains(int x, int y);

	public abstract int getBoundingRadius();

	@Override
	public boolean isThrownOutOfGameWindow() {
		return false;
	}

}
