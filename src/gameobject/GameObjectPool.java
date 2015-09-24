package gameobject;

import java.util.Set;

public interface GameObjectPool {

	public <T extends GameObject> Set<T> getGameObjects(Class<T> clazz);

	public void add(GameObject gameObject);

	public GameObject getGameObjectById(String id);

	public void remove(GameObject gameObject);

	public void remove(String id);

	public void clear();

}
