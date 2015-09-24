package misc;

import physics.Orientation;
import characters.GameCharacter;
import gameobject.GameObject;
import gameobject.GameObjectPool;

public abstract class Projectile extends GameObject {

	private GameCharacter owner;

	/**
	 * Reference point of projectile is along the middle of its butt end.
	 * Orientation is about that reference point.
	 */
	public Projectile(GameObjectPool gameObjectPool, int x, int y,
			float speedX, float speedY, GameCharacter owner) {
		super(gameObjectPool, x, y,
				new Orientation(owner.getOrientation().isReflected(),
						90 - Math.toDegrees(Math.atan2(speedY, speedX))));
		setSpeedX(speedX);
		setSpeedY(speedY);
		this.owner = owner;
	}

	public GameCharacter getOwner() {
		return owner;
	}

	public void setOwner(GameCharacter owner) {
		this.owner = owner;
	}

	@Override
	public Orientation getOrientation() {
		boolean isReflected = super.getOrientation().isReflected();
		if (isReflected) {
			return new Orientation(isReflected, Math.toDegrees(Math.atan2(
					-getSpeedY(), -getSpeedX())));
		} else {
			return new Orientation(isReflected, -Math.toDegrees(Math.atan2(
					-getSpeedY(), getSpeedX())));
		}
	}

	@Override
	public void setOrientation(Orientation orientation) {
		// nop;
	}

	public abstract Explosion explode();

	public abstract int getDetonationX();

	public abstract int getDetonationY();

}
