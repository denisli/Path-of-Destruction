package terrain.impl;

import gameobject.GameObjectPool;

import java.awt.Color;
import java.awt.Graphics;

import misc.Explosion;
import terrain.Terrain;

public class TestFlatTerrain extends Terrain {

	public TestFlatTerrain(GameObjectPool gameObjectPool) {
		super(gameObjectPool, 0, 0);
	}

	@Override
	public void deform(Explosion explosion) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(int x, int y) {
		if (0 < x && x <= 100) {
			return y > 450;
		} else if (100 < x && x <= 200) {
			return y > 425;
		} else if (200 < x && x <= 300) {
			return y > 400;
		} else if (300 < x && x <= 400) {
			return y > 375;
		} else if (400 < x && x <= 500) {
			return y > 350;
		} else {
			return y > 450;
		}
	}

	@Override
	public void animate(int deltaTime) {

	}

	@Override
	public void beRendered(Graphics g) {
		int[] floors = { 450, 425, 400, 375, 350 };
		g.setColor(Color.BLACK);
		for (int floor : floors) {
			g.drawLine(0, floor, 800, floor);
		}
		int[] hor = { 100, 200, 300, 400, 500 };
		g.setColor(Color.GRAY);
		for (int h : hor) {
			g.drawLine(h, 0, h, 480);
		}
	}

	@Override
	public boolean isThrownOutOfGameWindow() {
		return false;
	}

}
