package physics.impl;

import java.util.Set;

import applet.PathOfDestructionApplet;
import misc.Explosion;
import misc.Projectile;
import misc.impl.PlayerProjectile;
import characters.GameCharacter;
import characters.GroundCharacter;
import gameobject.GameObject;
import gameobject.GameObjectPool;
import physics.GamePhysics;
import physics.Orientation;
import physics.Point;
import physics.PhysicsUtil;
import physics.Vector;
import terrain.Terrain;

/**
 * Curved-terrain physics algorithm: Horizontal movement before vertical
 * movement. (Reasoning behind this is so that characters are still
 * realistically on the ground when moving horizontally down slope.
 * 
 * Here is the algorithm assuming we are moving right. For moving to the left,
 * the algorithm is simply reversed.
 * 
 * Horizontal movement: 1. Get desired distance towards the right. 2. Move right
 * as much as possible (up to the desired distance or until hits terrain) 3. If
 * necessary, move left one pixel, then move up from there. Check if the
 * distance is reachable within the remaining desired distance before moving up.
 * You should stop if you cannot move up that far. 4. Repeat 2. and 3. until
 * desired distance remaining towards right is 0.
 * 
 * Vertical movement: Move down as much as possible.
 * 
 * Setting orientation: If character is directly above a surface point, then .
 * That is how the character will stand erect.
 */
public class CurvedTerrainPhysics implements GamePhysics {

	private static final float FALL_DOWN_SPEED = 0.8f;
	private static final float PROJECTILE_GRAVITY = -0.001f;

	@Override
	public void apply(GameObjectPool gameObjectPool, int deltaTime) {
		Set<GroundCharacter> characters = gameObjectPool
				.getGameObjects(GroundCharacter.class);
		Set<Explosion> explosions = gameObjectPool
				.getGameObjects(Explosion.class);
		Set<Projectile> projectiles = gameObjectPool
				.getGameObjects(Projectile.class);
		Set<Terrain> terrains = gameObjectPool.getGameObjects(Terrain.class);

		updateCharacterStates(characters, terrains, deltaTime);
		updateCharactersFiringProjectiles(characters, deltaTime);
		updateProjectileStates(gameObjectPool, projectiles, terrains, deltaTime);
		phaseOutExplosions(explosions, deltaTime);
		updateAnimation(gameObjectPool, deltaTime);

		// render in the appropriate order
		throwOutGameObjects(gameObjectPool);
	}

	private void updateCharacterStates(Set<GroundCharacter> characters,
			Set<Terrain> terrains, int deltaTime) {
		for (GroundCharacter character : characters) {
			Point basePoint = character.getBasePoint();
			int basePointX = basePoint.getX();
			int basePointY = basePoint.getY();
			// Set the jumping status of the character.
			if (!isNotTerrainPoint(basePointX, basePointY + 1, terrains)) {
				character.setCanJump(true);
			} else {
				character.setCanJump(false);
				if (character.isJumping()) {
					character.setJumpingDuration(character.getJumpingDuration()
							+ deltaTime);
					if (character.getJumpingDuration() >= character
							.getMaxJumpingDuration()) {
						character.setJumping(false);
						character.setJumpingDuration(0);
					}
				}
			}
			int desiredDispX = getDesiredDispX(character, deltaTime);
			int signDesiredDispX = (int) Math.signum(desiredDispX);
			int absDesiredDispXRemaining = Math.abs(desiredDispX);
			int actualAbsDispX = 0;
			int rollUpDispY = 0;
			moveHorizontally: while (absDesiredDispXRemaining > 0) {
				if (isNotTerrainPoint(basePointX + (actualAbsDispX + 1)
						* signDesiredDispX, basePointY + rollUpDispY, terrains)) {
					actualAbsDispX++;
					absDesiredDispXRemaining--;
				} else {
					int elevation = -1;
					while (!isNotTerrainPoint(basePointX + actualAbsDispX + 1,
							basePointY + rollUpDispY + elevation, terrains)) {
						elevation--;
						if (Math.abs(elevation) > absDesiredDispXRemaining) {
							break moveHorizontally;
						}
					}
					int distToElevation = PhysicsUtil.dist(elevation, 1);
					if (absDesiredDispXRemaining < PhysicsUtil.dist(elevation,
							1)) {
						break moveHorizontally;
					} else {
						absDesiredDispXRemaining -= distToElevation;
						rollUpDispY += elevation;
					}
				}
			}
			int afterHorizontalMoveX = basePointX + signDesiredDispX
					* actualAbsDispX;
			int afterHorizontalMoveY = basePointY + rollUpDispY;
			character.setX(afterHorizontalMoveX);
			character.setY(afterHorizontalMoveY);

			int actualDispY = 0;
			if (character.isJumping()) {
				character.setSpeedY(character.getDefaultJumpingSpeed());
				int desiredDispYRemaining = (int) (character.getSpeedY() * deltaTime);
				while (desiredDispYRemaining < 0) {
					if (isNotTerrainPoint(afterHorizontalMoveX,
							afterHorizontalMoveY + actualDispY - 1, terrains)) {
						actualDispY--;
						desiredDispYRemaining++;
					} else {
						break;
					}
				}
			} else {
				int desiredDispYRemaining = getDesiredDispY(character,
						deltaTime);
				while (desiredDispYRemaining > 0) {
					if (isNotTerrainPoint(afterHorizontalMoveX,
							afterHorizontalMoveY + actualDispY + 1, terrains)) {
						actualDispY++;
						desiredDispYRemaining--;
					} else {
						break;
					}
				}
			}
			character.setY(afterHorizontalMoveY + actualDispY);
			Vector normal = getNormal(character.getBasePoint(), terrains);
			Orientation newOrientation = getOrientation(character, normal);
			character.setOrientation(newOrientation);
		}
	}

	private void updateCharactersFiringProjectiles(
			Set<? extends GameCharacter> characters, int deltaTime) {
		characters
				.forEach(character -> {
					if (character.isFiredShot()) {
						// at best angle and full power, the bullet should hit
						// 1.5 times screen width away.
						float magnitude = (float) Math.sqrt(1.5
								* PathOfDestructionApplet.WINDOW_WIDTH
								* -PROJECTILE_GRAVITY);
						float loadedPower = Math.min(
								character.getLoadingTime() / 1000f, 1f);
						int dispX = character.getFireTowardsX()
								- character.getFireProjectileX();
						int dispY = character.getFireTowardsY()
								- character.getFireProjectileY();
						float normalizingFactor = (loadedPower * magnitude)
								/ PhysicsUtil.dist(dispX, dispY);
						Projectile projectile = new PlayerProjectile(character
								.getGameObjectPool(), character
								.getFireProjectileX(), character
								.getFireProjectileY(), dispX
								* normalizingFactor, dispY * normalizingFactor,
								character);
						character.fireProjectile(projectile);
						character.setLoadingTime(0);
						character.setFiredShot(false);
					}
					if (character.isLoadingShot()) {
						character.setLoadingTime(character.getLoadingTime()
								+ deltaTime);
					}
				});
	}

	private void updateProjectileStates(GameObjectPool gameObjectPool,
			Set<Projectile> projectiles, Set<Terrain> terrains, int deltaTime) {
		projectiles.forEach(projectile -> {
			projectile.setX(projectile.getX()
					+ (int) (deltaTime * projectile.getSpeedX()));
			projectile.setY(projectile.getY()
					+ (int) (deltaTime * projectile.getSpeedY()));
			projectile.setSpeedY(projectile.getSpeedY() - deltaTime
					* PROJECTILE_GRAVITY);
			if (!isNotTerrainPoint(projectile.getDetonationX(),
					projectile.getDetonationY(), terrains)) {
				Explosion explosion = projectile.explode();
				gameObjectPool.add(explosion);
				gameObjectPool.remove(projectile);
				for (Terrain terrain : terrains) {
					terrain.deform(explosion);
				}
			}
		});
	}

	private void phaseOutExplosions(Set<Explosion> explosions, int deltaTime) {
		explosions.forEach(explosion -> explosion.phaseOut(deltaTime));
	}

	private void updateAnimation(GameObjectPool gameObjectPool, int deltaTime) {
		gameObjectPool.getGameObjects(GameObject.class).forEach(
				gameObject -> gameObject.animate(deltaTime));
	}

	private int getDesiredDispY(GameCharacter character, int deltaTime) {
		if (character.isJumping()) {
			character.setSpeedY(character.getDefaultJumpingSpeed());
		} else {
			character.setSpeedY(FALL_DOWN_SPEED);
		}
		float dispY = character.getSpeedY() * deltaTime;
		return (int) (Math.signum(dispY) * Math.floor(Math.abs(dispY)));
	}

	private int getDesiredDispX(GameCharacter character, int deltaTime) {
		if (character.isMovingLeft()) {
			character.setSpeedX(-character.getDefaultMovingSpeed());
		} else if (character.isMovingRight()) {
			character.setSpeedX(character.getDefaultMovingSpeed());
		} else {
			character.setSpeedX(0);
		}
		float dispX = character.getSpeedX() * deltaTime;
		return (int) (Math.signum(dispX) * Math.floor(Math.abs(dispX)));
	}

	private boolean isNotTerrainPoint(int x, int y, Set<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			if (terrain.contains(x, y)) {
				return false;
			}
		}
		return true;
	}

	private Orientation getOrientation(GameCharacter character, Vector normal) {
		boolean reflected = character.getOrientation().isReflected();
		if (character.isMovingRight()) {
			reflected = false;
		} else if (character.isMovingLeft()) {
			reflected = true;
		}

		if (normal.getX() == 0 && normal.getY() == 0) {
			return new Orientation(character.getOrientation().isReflected(), 0);
		}
		return new Orientation(reflected, 90 - Math.toDegrees(Math.atan2(
				-normal.getY(), normal.getX())));
	}

	private Vector getNormal(Point point, Set<Terrain> terrains) {
		int pointX = point.getX(), pointY = point.getY();
		int normalX = 0, normalY = 0;
		int scanLength = 5;
		for (int dispX = -scanLength; dispX <= scanLength; dispX++) {
			for (int dispY = -scanLength; dispY <= scanLength; dispY++) {
				if (!isNotTerrainPoint(pointX + dispX, pointY + dispY, terrains)) {
					normalX -= dispX;
					normalY -= dispY;
				}
			}
		}
		return new Vector(normalX, normalY);
	}

	private void throwOutGameObjects(GameObjectPool gameObjectPool) {
		gameObjectPool.getGameObjects(GameObject.class).forEach(gameObject -> {
			if (gameObject.isThrownOutOfGameWindow()) {
				gameObjectPool.remove(gameObject);
			}
		});
	}
}
