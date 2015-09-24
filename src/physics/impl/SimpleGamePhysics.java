package physics.impl;

import java.util.Set;

import characters.GameCharacter;
import characters.GroundCharacter;
import physics.GamePhysics;
import physics.Point;
import terrain.Terrain;
import gameobject.GameObject;
import gameobject.GameObjectPool;

public class SimpleGamePhysics implements GamePhysics {

	private static final float FALL_DOWN_SPEED = 0.2f;

	@Override
	public void apply(GameObjectPool gameObjectPool, int deltaTime) {
		Set<GroundCharacter> characters = gameObjectPool
				.getGameObjects(GroundCharacter.class);
		Set<Terrain> terrains = gameObjectPool.getGameObjects(Terrain.class);
		for (GroundCharacter character : characters) {
			int realDispX = Integer.MAX_VALUE, realDispY = Integer.MAX_VALUE;
			Point basePoint = character.getBasePoint();
			int x = basePoint.getX();
			int y = basePoint.getY();
			float speedX = getCharacterSpeedX(character);
			float speedY = getCharacterSpeedY(character);
			int maxDispX = (int) Math.floor(speedX * deltaTime);
			int maxDispY = (int) Math.floor(speedY * deltaTime);
			int signX = (int) Math.signum(maxDispX), signY = (int) Math
					.signum(maxDispY);
			int dispX = 0, dispY = 0;
			// Get furthest possible displace. First in x, then in y.
			if (signX != 0) {
				while (Math.abs(dispX) <= Math.abs(maxDispX)) {
					int possibleNewX = x + dispX;
					if (isPointWithinTerrains(possibleNewX, y, terrains)) {
						dispX -= signX;
						break;
					}
					dispX += signX;
				}
			}
			if (signY != 0) {
				while (Math.abs(dispY) <= Math.abs(maxDispY)) {
					int possibleNewY = y + dispY;
					if (isPointWithinTerrains(x + dispX, possibleNewY, terrains)) {
						dispY -= signY;
						break;
					}
					dispY += signY;
				}
			}
			realDispX = Math.abs(realDispX) > Math.abs(dispX) ? dispX
					: realDispX;
			realDispY = Math.abs(realDispY) > Math.abs(dispY) ? dispY
					: realDispY;
			character.setX(character.getX() + realDispX);
			character.setY(character.getY() + realDispY);
		}
		gameObjectPool.getGameObjects(GameObject.class).forEach(
				gameObject -> gameObject.animate(deltaTime));
	}

	private boolean isPointWithinTerrains(int x, int y, Set<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			if (terrain.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	private float getCharacterSpeedX(GameCharacter character) {
		if (character.isMovingRight()) {
			return character.getDefaultMovingSpeed();
		} else if (character.isMovingLeft()) {
			return -character.getDefaultMovingSpeed();
		} else {
			return 0;
		}
	}

	private float getCharacterSpeedY(GameCharacter character) {
		if (!character.isJumping()) {
			return FALL_DOWN_SPEED;
		} else {
			return character.getDefaultJumpingSpeed();
		}
	}

}
