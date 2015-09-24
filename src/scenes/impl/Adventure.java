package scenes.impl;

import gameobject.GameObjectPool;
import gameobject.impl.GameObjectPoolImpl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import misc.Explosion;
import misc.Projectile;
import characters.GameCharacter;
import characters.Player;
import physics.GamePhysics;
import physics.impl.CurvedTerrainPhysics;
import scenes.Scene;
import terrain.Terrain;
import terrain.impl.TestCurvedTerrain;

public class Adventure implements Scene {

	private final GameObjectPool gameObjectPool;
	private GamePhysics gamePhysics;
	private final Player player;
	private Image backgroundImage;

	public Adventure() {
		this.gameObjectPool = new GameObjectPoolImpl();
		this.gamePhysics = new CurvedTerrainPhysics();
		this.player = new Player(gameObjectPool, 40, 40);
		new TestCurvedTerrain(gameObjectPool);

		try {
			backgroundImage = ImageIO.read(new File(
					"../res/cloud_background.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void update(int deltaTime) {
		gamePhysics.apply(gameObjectPool, deltaTime);
	}

	@Override
	public void beRendered(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		gameObjectPool.getGameObjects(GameCharacter.class).forEach(
				gameCharacter -> {
					gameCharacter.beRendered(g);
				});
		gameObjectPool.getGameObjects(Projectile.class).forEach(projectile -> {
			projectile.beRendered(g);
		});
		gameObjectPool.getGameObjects(Explosion.class).forEach(explosion -> {
			explosion.beRendered(g);
		});
		gameObjectPool.getGameObjects(Terrain.class).forEach(terrain -> {
			terrain.beRendered(g);
		});
	}

	@Override
	public Set<KeyListener> getKeyListeners() {
		return new HashSet<KeyListener>(Arrays.asList(player));
	}

	@Override
	public Set<MouseListener> getMouseListeners() {
		return new HashSet<MouseListener>(Arrays.asList(player));
	}

}
