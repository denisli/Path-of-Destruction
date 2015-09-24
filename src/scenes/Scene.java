package scenes;

import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Set;

public interface Scene {

	public void update(int deltaTime);

	public void beRendered(Graphics g);

	public Set<KeyListener> getKeyListeners();

	public Set<MouseListener> getMouseListeners();

}
