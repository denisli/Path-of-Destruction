package misc.impl;

import gameobject.GameObjectPool;
import imageprocessing.ARGBImage;
import imageprocessing.impl.BufferedARGBImage;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import animation.Animation;
import misc.Explosion;

public class PlayerProjectileExplosion extends Explosion {

	private final Animation animation = new Animation();
	private ARGBImage image;

	private int lifeTimeRemaining = 500;

	public PlayerProjectileExplosion(GameObjectPool gameObjectPool, int x, int y) {
		super(gameObjectPool, x, y);
		try {
			image = new BufferedARGBImage(ImageIO.read(new File(
					"../res/player_projectile_explosion.png")));
			animation.addFrame(image.getImage(), 10000);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void animate(int deltaTime) {

	}

	@Override
	public void beRendered(Graphics g) {
		animation.display(g, getX() - animation.getImage().getWidth(null) / 2,
				getY() - animation.getImage().getHeight(null) / 2,
				getOrientation(), getX(), getY());
	}

	@Override
	public boolean contains(int x, int y) {
		return image.getAlpha(x - getX() + animation.getImage().getWidth(null)
				/ 2, y - getY() + animation.getImage().getHeight(null) / 2) != 0;
	}

	@Override
	public int getBoundingRadius() {
		return Math.max(animation.getImage().getWidth(null) / 2 + 1, animation
				.getImage().getHeight(null) / 2 + 1);
	}

	@Override
	public void phaseOut(int deltaTime) {
		lifeTimeRemaining -= deltaTime;
		if (lifeTimeRemaining <= 0) {
			getGameObjectPool().remove(this);
		}
	}

}
