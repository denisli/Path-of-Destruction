package imageprocessing;

import java.awt.Image;

public interface ARGBImage {

	public Image getImage();

	public int getR(int x, int y);

	public void setR(int x, int y, int r);

	public int getG(int x, int y);

	public void setG(int x, int y, int g);

	public int getB(int x, int y);

	public void setB(int x, int y, int b);

	public int getAlpha(int x, int y);

	public void setAlpha(int x, int y, int a);

}