package terrain;

import gameobject.GameObject;
import gameobject.GameObjectPool;
import misc.Explosion;

public abstract class Terrain extends GameObject {

	/**
	 * x, y coordinates represent the top-left corner of the terrain, assuming
	 * no rotation
	 */
	public Terrain(GameObjectPool gameObjectPool, int x, int y) {
		super(gameObjectPool, x, y);
	}

	public abstract void deform(Explosion explosion);

	public abstract boolean contains(int x, int y);

}
