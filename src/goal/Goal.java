package goal;

import gameobject.GameObject;
import gameobject.GameObjectPool;

public abstract class Goal extends GameObject {

	public Goal(GameObjectPool gameObjectPool, int x, int y) {
		super(gameObjectPool, x, y);
	}

}
