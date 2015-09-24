package misc.impl;

import gameobject.GameObjectPool;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import physics.Point;
import physics.PhysicsUtil;
import misc.Explosion;
import misc.Projectile;
import animation.Animation;
import applet.PathOfDestructionApplet;
import characters.GameCharacter;

public class PlayerProjectile extends Projectile {

	private final Animation animation = new Animation();
	private Image projectileImage;

	public PlayerProjectile(GameObjectPool gameObjectPool, int x, int y,
			float speedX, float speedY, GameCharacter owner) {
		super(gameObjectPool, x, y, speedX, speedY, owner);

		try {
			projectileImage = ImageIO.read(new File(
					"../res/player_projectile.png"));
			animation.addFrame(projectileImage, 10000);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void animate(int deltaTime) {

	}

	@Override
	public void beRendered(Graphics g) {
		Image image = animation.getImage();
		animation.display(g, getX(), getY() - image.getHeight(null) / 2,
				getOrientation(), getX(), getY());
	}

	@Override
	public Explosion explode() {
		return new PlayerProjectileExplosion(getGameObjectPool(), getX(),
				getY());
	}

	@Override
	public int getDetonationX() {
		return getDetonationPoint().getX();
	}

	@Override
	public int getDetonationY() {
		return getDetonationPoint().getY();
	}

	private Point getDetonationPoint() {
		return PhysicsUtil.rotate(new Point(getX(), getY()), new Point(getX()
				+ animation.getImage().getWidth(null) / 2, getY()),
				getOrientation().getAngle());
	}

	@Override
	public boolean isThrownOutOfGameWindow() {
		boolean belowWindow = getY() - animation.getImage().getHeight(null) > PathOfDestructionApplet.WINDOW_HEIGHT + 100;
		if (belowWindow) {
			return true;
		} else {
			boolean outsideWalls = getX() + animation.getImage().getWidth(null) < -50
					|| getX() - animation.getImage().getWidth(null) > PathOfDestructionApplet.WINDOW_WIDTH + 50;
			return outsideWalls;
		}
	}

}
