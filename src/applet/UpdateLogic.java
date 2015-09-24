package applet;

import java.applet.Applet;

import game.Game;

public class UpdateLogic implements Runnable {

	private final Applet applet;
	private final Game game;

	private int targetTimeUntilNextFrame = 1000 / 60;
	private long timeOfLastRender;

	public UpdateLogic(Applet applet, Game game) {
		this.applet = applet;
		this.game = game;
	}

	@Override
	public void run() {
		timeOfLastRender = System.currentTimeMillis();
		while (true) {
			long timeNow = System.currentTimeMillis();
			game.update((int) (timeNow - timeOfLastRender));
			applet.repaint();
			timeOfLastRender = System.currentTimeMillis();
			try {
				Thread.sleep(targetTimeUntilNextFrame);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTargetFrameRate(int numFramesPerSecond) {
		if (numFramesPerSecond > 1000) {
			throw new IllegalArgumentException(
					"A target number of frames per second must be less than or equal to 1000. You wanted to set it to "
							+ numFramesPerSecond);
		}
		targetTimeUntilNextFrame = 1000 / numFramesPerSecond;
	}

}
