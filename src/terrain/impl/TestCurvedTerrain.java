package terrain.impl;

import gameobject.GameObjectPool;
import imageprocessing.ARGBImage;
import imageprocessing.impl.BufferedARGBImage;
import imageprocessing.impl.ImageUtil;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import applet.PathOfDestructionApplet;
import misc.Explosion;
import terrain.Terrain;

public class TestCurvedTerrain extends Terrain {

	private ARGBImage image;

	public TestCurvedTerrain(GameObjectPool gameObjectPool) {
		super(gameObjectPool, 0, 0);
		this.image = null;
		try {
			this.image = new BufferedARGBImage(ImageUtil.getScaledImage(
					ImageIO.read(new File("../res/test_curved_terrain.png")),
					PathOfDestructionApplet.WINDOW_WIDTH,
					PathOfDestructionApplet.WINDOW_HEIGHT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deform(Explosion explosion) {
		int boundingRadius = explosion.getBoundingRadius();
		int centerX = explosion.getX();
		int centerY = explosion.getY();
		for (int dispX = -boundingRadius; dispX <= boundingRadius; dispX++) {
			for (int dispY = -boundingRadius; dispY <= boundingRadius; dispY++) {
				int x = centerX + dispX;
				int y = centerY + dispY;
				if (explosion.contains(x, y) && this.contains(x, y)) {
					image.setAlpha(x, y, 0);
				}
			}
		}
	}

	@Override
	public boolean contains(int x, int y) {
		if (image.getImage().getWidth(null) <= x || x < 0) {
			return false;
		}
		if (image.getImage().getHeight(null) <= y || y < 0) {
			return false;
		}
		return image.getAlpha(x, y) != 0;
	}

	@Override
	public void animate(int deltaTime) {

	}

	@Override
	public void beRendered(Graphics g) {
		g.drawImage(image.getImage(), 0, 0, null);
	}

	@Override
	public boolean isThrownOutOfGameWindow() {
		return false;
	}

}
