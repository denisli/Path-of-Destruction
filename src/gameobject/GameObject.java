package gameobject;

import java.awt.Graphics;
import java.util.UUID;

import physics.Orientation;

public abstract class GameObject {

	private final GameObjectPool gameObjectPool;
	private final String id;
	private int x;
	private int y;
	private Orientation orientation;
	private float speedX;
	private float speedY;

	/**
	 * GameObject is represented by a reference point, orientation, and a given
	 * id. The reference point does not have to be any specific point. It could
	 * be top-left corner or center. Similarly, the orientation does not need to
	 * be a rotation/reflection about any specific point. The definition of
	 * reference point and orientation should be documented in the subclasses.
	 * 
	 * @param gameObjectPool
	 *            pool that this object will go into
	 * @param x
	 *            x-coordinate of reference point
	 * @param y
	 *            y-coordinate of reference point
	 * @param orientation
	 *            orientation of the object
	 * @param id
	 *            unique identifier for the object
	 */
	public GameObject(GameObjectPool gameObjectPool, int x, int y,
			Orientation orientation) {
		this.gameObjectPool = gameObjectPool;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.id = UUID.randomUUID().toString();
		// Be sure to add to the object pool only at the end. This is because we
		// need the ID in order to correctly insert to object pool.
		gameObjectPool.add(this);
	}

	public GameObject(GameObjectPool gameObjectPool, int x, int y) {
		this(gameObjectPool, x, y, new Orientation(0));
	}

	public GameObjectPool getGameObjectPool() {
		return gameObjectPool;
	}

	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public float getSpeedX() {
		return speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	/**
	 * Animates the object
	 */
	public abstract void animate(int deltaTime);

	/**
	 * Renders the game object with the given graphics.
	 */
	public abstract void beRendered(Graphics g);

	/**
	 * Checks whether or not the game object is thrown out of the game window.
	 */
	public abstract boolean isThrownOutOfGameWindow();

}
