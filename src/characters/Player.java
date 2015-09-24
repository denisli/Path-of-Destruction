package characters;

import gameobject.GameObjectPool;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import physics.Orientation;
import physics.Point;
import physics.PhysicsUtil;
import animation.Animation;
import applet.PathOfDestructionApplet;

public class Player extends GroundCharacter implements KeyListener,
		MouseListener {

	private final Animation standingAnimation = new Animation();
	private final Animation movingAnimation = new Animation();
	private BufferedImage standingImage;
	private BufferedImage movingImage1;
	private BufferedImage movingImage2;
	private BufferedImage movingImage3;
	private Animation currentAnimation;

	public Player(GameObjectPool gameObjectPool, int x, int y) {
		super(gameObjectPool, x, y, 0.3f, -0.6f);

		try {
			standingImage = ImageIO.read(new File(
					"../res/stickman_standing_face_right.png"));
			standingAnimation.addFrame(standingImage, 10000);
			movingImage1 = ImageIO.read(new File(
					"../res/stickman_moving_right1.png"));
			movingImage2 = ImageIO.read(new File(
					"../res/stickman_moving_right2.png"));
			movingImage3 = ImageIO.read(new File(
					"../res/stickman_moving_right3.png"));
			movingAnimation.addFrame(movingImage1, 200);
			movingAnimation.addFrame(movingImage2, 300);
			movingAnimation.addFrame(movingImage3, 200);
			currentAnimation = standingAnimation;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void animate(int deltaTime) {
		if (isMovingRight()) {
			changeAnimation(movingAnimation);
		} else if (isMovingLeft()) {
			changeAnimation(movingAnimation);
		} else {
			changeAnimation(standingAnimation);
		}
		currentAnimation.update(deltaTime);
	}

	@Override
	public void beRendered(Graphics g) {
		Image animationImage = currentAnimation.getImage();
		currentAnimation.display(g, getX() - animationImage.getWidth(null) / 2,
				getY() - animationImage.getHeight(null), getOrientation(),
				getX(), getY());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (isCanJump()) {
				setJumping(true);
			}
			break;
		case KeyEvent.VK_LEFT:
			setOrientation(new Orientation(true, getOrientation().getAngle()));
			setMovingLeft(true);
			setMovingRight(false);
			break;
		case KeyEvent.VK_RIGHT:
			setOrientation(new Orientation(false, getOrientation().getAngle()));
			setMovingRight(true);
			setMovingLeft(false);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_LEFT:
			setMovingLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			setMovingRight(false);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private void changeAnimation(Animation animation) {
		if (currentAnimation != animation) {
			currentAnimation.reset();
			currentAnimation = animation;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		setLoadingShot(true);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		setLoadingShot(false);
		setFireTowardsX(arg0.getX());
		setFireTowardsY(arg0.getY());
		setFiredShot(true);
	}

	@Override
	public int getFireProjectileX() {
		return getFireProjectilePoint().getX();
	}

	@Override
	public int getFireProjectileY() {
		return getFireProjectilePoint().getY();
	}

	private Point getFireProjectilePoint() {
		return PhysicsUtil.rotate(new Point(getX(), getY()), new Point(getX()
				+ currentAnimation.getImage().getWidth(null) * 3 / 5
				* (getOrientation().isReflected() ? -1 : 1), getY()
				- currentAnimation.getImage().getHeight(null) / 2),
				getOrientation().getAngle());
	}

	@Override
	public int getMaxJumpingDuration() {
		return 300;
	}

	@Override
	public boolean isThrownOutOfGameWindow() {
		boolean belowWindow = getY()
				- currentAnimation.getImage().getHeight(null) > PathOfDestructionApplet.WINDOW_HEIGHT + 100;
		if (belowWindow) {
			return true;
		} else {
			boolean outsideWalls = getX()
					+ currentAnimation.getImage().getWidth(null) < -50
					|| getX() - currentAnimation.getImage().getWidth(null) > PathOfDestructionApplet.WINDOW_WIDTH + 50;
			return outsideWalls;
		}
	}
}
