package game;

import applet.PathOfDestructionApplet;
import scenes.Scene;

public class Game {

	private final PathOfDestructionApplet applet;

	private Scene scene;

	public Game(PathOfDestructionApplet applet) {
		this.applet = applet;
	}

	public void update(int deltaTime) {
		scene.update(deltaTime);
	}

	public void changeScene(Scene scene) {
		if (this.scene != null) {
			this.scene.getKeyListeners().forEach(
					keyListener -> applet.removeKeyListener(keyListener));
			this.scene.getMouseListeners().forEach(
					mouseListener -> applet.removeMouseListener(mouseListener));
		}
		this.scene = scene;
		scene.getKeyListeners().forEach(
				keyListener -> applet.addKeyListener(keyListener));
		scene.getMouseListeners().forEach(
				mouseListener -> applet.addMouseListener(mouseListener));
	}

	public Scene getScene() {
		return scene;
	}

}
