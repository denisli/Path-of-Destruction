package gameobject.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import gameobject.GameObject;
import gameobject.GameObjectPool;

public class GameObjectPoolImpl implements GameObjectPool {

	private final Level topLevel = new Level(GameObject.class);
	private final Map<String, GameObject> idToGameObject = new HashMap<String, GameObject>();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends GameObject> Set<T> getGameObjects(Class<T> clazz) {
		if (GameObject.class.isAssignableFrom(clazz)) {
			Level level = findLevel(clazz);
			if (level != null) {
				Set<String> allIds = level.getAllIds();
				Set<T> gameObjects = new HashSet<T>();
				allIds.forEach(id -> gameObjects.add((T) idToGameObject.get(id)));
				return gameObjects;
			} else {
				return new HashSet<T>();
			}
		} else {
			throw new IllegalArgumentException("The class " + clazz
					+ " must be a subclass of GameObject");
		}
	}

	@Override
	public GameObject getGameObjectById(String id) {
		return idToGameObject.get(id);
	}

	@Override
	public void add(GameObject gameObject) {
		Stack<Class<?>> classPath = new Stack<Class<?>>();
		Class<?> type = gameObject.getClass();
		while (!type.equals(GameObject.class)) {
			classPath.add(type);
			type = type.getSuperclass();
		}
		Level currentLevel = topLevel;
		while (!classPath.isEmpty()) {
			Class<?> clazz = classPath.pop();
			if (!currentLevel.contains(clazz)) {
				Level childLevel = new Level(clazz);
				currentLevel.childLevels.put(clazz, childLevel);
				currentLevel = childLevel;
			} else {
				currentLevel = currentLevel.childLevels.get(clazz);
			}
			if (currentLevel.isClass(gameObject.getClass())) {
				currentLevel.ids.add(gameObject.getId());
				idToGameObject.put(gameObject.getId(), gameObject);
			}
		}
	}

	@Override
	public void remove(GameObject gameObject) {
		String id = gameObject.getId();
		remove(id);
	}

	@Override
	public void remove(String id) {
		GameObject gameObject = idToGameObject.get(id);
		idToGameObject.remove(id);
		Level level = findLevel(gameObject.getClass());
		level.ids.remove(id);
	}

	@Override
	public void clear() {
		idToGameObject.clear();
		topLevel.childLevels.clear();
		topLevel.ids.clear();
	}

	@Override
	public String toString() {
		return "GameObjectPoolImpl [topLevel=" + topLevel + ", idToGameObject="
				+ idToGameObject + "]";
	}

	private Level findLevel(Class<?> clazz) {
		if (!GameObject.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("The class " + clazz
					+ " is not a subclass of GameObject.");
		}
		// Get the class path up to the game object class.
		Stack<Class<?>> classPath = new Stack<Class<?>>();
		Class<?> type = clazz;
		while (!type.equals(GameObject.class)) {
			classPath.add(type);
			type = type.getSuperclass();
		}
		Level currentLevel = topLevel;
		while (!classPath.isEmpty()) {
			Class<?> childClass = classPath.pop();
			currentLevel = currentLevel.childLevels.getOrDefault(childClass,
					new Level(childClass));
		}
		return currentLevel;
	}

	private static class Level {
		private final Class<?> clazz;
		private final Map<Class<?>, Level> childLevels = new HashMap<Class<?>, Level>();
		private final Set<String> ids = new HashSet<String>();

		private Level(Class<?> clazz) {
			this.clazz = clazz;
		}

		private boolean isClass(Class<?> clazz) {
			return this.clazz.equals(clazz);
		}

		private boolean contains(Class<?> clazz) {
			return childLevels.containsKey(clazz);
		}

		/**
		 * Fetches all ids that this level has as well as all the ids of its
		 * descendant levels.
		 */
		public Set<String> getAllIds() {
			Set<String> allIds = new HashSet<String>();
			allIds.addAll(ids);
			if (childLevels.isEmpty()) {
				return allIds;
			} else {
				for (Level childLevel : childLevels.values()) {
					Set<String> childAllIds = childLevel.getAllIds();
					allIds.addAll(childAllIds);
				}
			}
			return allIds;
		}

		@Override
		public String toString() {
			return "Level [clazz=" + clazz + ", childLevels=" + childLevels
					+ ", ids=" + ids + "]";
		}

	}

}
