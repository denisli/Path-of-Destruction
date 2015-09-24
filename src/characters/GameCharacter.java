package characters;

import misc.Projectile;
import gameobject.GameObject;
import gameobject.GameObjectPool;
import physics.Orientation;

public abstract class GameCharacter extends GameObject {

	// Speeds are measured in number of pixels moved per millisecond.
	private final float defaultMovingSpeed;
	private final float defaultJumpingSpeed;
	private boolean movingRight;
	private boolean movingLeft;
	private boolean jumping;
	private boolean canJump;
	private int jumpingDuration;

	// Projectile related properties
	private int loadingTime;
	private boolean loadingShot;
	private boolean firedShot;
	private int fireTowardsX;
	private int fireTowardsY;

	public GameCharacter(GameObjectPool gameObjectPool, int x, int y,
			float defaultMovingSpeed, float defaultJumpingSpeed) {
		super(gameObjectPool, x, y, new Orientation(0));
		this.defaultMovingSpeed = defaultMovingSpeed;
		this.defaultJumpingSpeed = defaultJumpingSpeed;
	}

	public void fireProjectile(Projectile projectile) {
		getGameObjectPool().add(projectile);
		firedShot = false;
	}

	public float getDefaultMovingSpeed() {
		return defaultMovingSpeed;
	}

	public float getDefaultJumpingSpeed() {
		return defaultJumpingSpeed;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public int getLoadingTime() {
		return loadingTime;
	}

	public boolean isLoadingShot() {
		return loadingShot;
	}

	public void setLoadingTime(int pullingTime) {
		this.loadingTime = pullingTime;
	}

	public void setLoadingShot(boolean firingShot) {
		this.loadingShot = firingShot;
	}

	public boolean isFiredShot() {
		return firedShot;
	}

	public void setFiredShot(boolean firedShot) {
		this.firedShot = firedShot;
	}

	public abstract int getFireProjectileX();

	public abstract int getFireProjectileY();

	public int getFireTowardsX() {
		return fireTowardsX;
	}

	public void setFireTowardsX(int fireTowardsX) {
		this.fireTowardsX = fireTowardsX;
	}

	public int getFireTowardsY() {
		return fireTowardsY;
	}

	public void setFireTowardsY(int fireTowardsY) {
		this.fireTowardsY = fireTowardsY;
	}

	public boolean isCanJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public int getJumpingDuration() {
		return jumpingDuration;
	}

	public void setJumpingDuration(int jumpingDuration) {
		this.jumpingDuration = jumpingDuration;
	}

	public abstract int getMaxJumpingDuration();

}
