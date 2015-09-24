package imageprocessing.impl;

import imageprocessing.ARGBImage;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class BufferedARGBImage implements ARGBImage {

	private final BufferedImage image;

	/**
	 * @param image
	 *            Must be an image in the 4Byte ARGB format
	 */
	public BufferedARGBImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public int getR(int x, int y) {
		return (image.getRGB(x, y) & (0x00ff0000)) >> 16;
	}

	@Override
	public void setR(int x, int y, int r) {
		int rgbWithoutR = image.getRGB(x, y) & (0xff00ffff);
		image.setRGB(x, y, rgbWithoutR + (r << 16));
	}

	@Override
	public int getG(int x, int y) {
		return (image.getRGB(x, y) & (0x0000ff00)) >> 8;
	}

	@Override
	public void setG(int x, int y, int g) {
		int rgbWithoutG = image.getRGB(x, y) & (0xffff00ff);
		image.setRGB(x, y, rgbWithoutG + (g << 8));
	}

	@Override
	public int getB(int x, int y) {
		return (image.getRGB(x, y) & (0x000000ff)) >> 4;
	}

	@Override
	public void setB(int x, int y, int b) {
		int rgbWithoutB = image.getRGB(x, y) & (0xffffff00);
		image.setRGB(x, y, rgbWithoutB + (b << 4));
	}

	@Override
	public int getAlpha(int x, int y) {
		if (image.getWidth() > x && x >= 0 && image.getHeight() > y && y >= 0) {
			return (image.getRGB(x, y) & (0xff000000)) >> 24;
		} else {
			return 0;
		}
	}

	@Override
	public void setAlpha(int x, int y, int a) {
		if (image.getWidth() > x && x >= 0 && image.getHeight() > y && y >= 0) {
			int rgbWithoutA = image.getRGB(x, y) & (0x00ffffff);
			image.setRGB(x, y, rgbWithoutA + (a << 24));
		}
	}
}
