package map;

import java.awt.Graphics;

/**
 * Represents a level in a game. A Map can be updated and rendered.
 */
public interface Map {

	public void update(int deltaTime);

	public void beRendered(Graphics g);

}
