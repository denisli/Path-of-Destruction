package applet;

import game.Game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import scenes.impl.Adventure;

/**
 * Class which provides applet initialization and rendering.
 * 
 * Personal notes: In order to keep re-painting, in update() you must call
 * paint() on the graphics of the image created from this applet. In doing so,
 * repaint() will recognize to call the paint() method that we implement.
 */
public class PathOfDestructionApplet extends Applet {

	private static final long serialVersionUID = 1L;
	private static final Dimension DIMENSION = Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static final int WINDOW_WIDTH = (int) (2 * DIMENSION.getWidth() / 3);
	public static final int WINDOW_HEIGHT = (int) (2 * DIMENSION.getHeight() / 3);

	private final Game game = new Game(this);

	private BufferedImage gameImage;
	private UpdateLogic gameFlowLogic;

	@Override
	public void init() {
		// Initialize the window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setBackground(Color.WHITE);
		setFocusable(true);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Path of Destruction");

		// Initialize PathOfDestructionApplet properties
		gameFlowLogic = new UpdateLogic(this, game);
		game.changeScene(new Adventure());

		// Set up the graphics
		gameImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public void start() {
		Thread thread = new Thread(gameFlowLogic);
		thread.start();
	}

	@Override
	public void update(Graphics g) {
		paint(gameImage.getGraphics());
		g.drawImage(gameImage, 0, 0, this);
	}

	@Override
	public void stop() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void paint(Graphics g) {
		game.getScene().beRendered(g);
	}

}
