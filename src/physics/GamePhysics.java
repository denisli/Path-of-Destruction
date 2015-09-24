package physics;

import gameobject.GameObjectPool;

/**
 * Resolves collisions given the state and motion of each object in game pool
 * object.
 */
public interface GamePhysics {

	public void apply(GameObjectPool gameObjectPool, int deltaTime);

}
