package imageprocessing.impl;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
 */
public class ImageUtil {

	public static BufferedImage getScaledImage(BufferedImage image,
			int newWidth, int newHeight) {
		int beforeWidth = image.getWidth();
		int beforeHeight = image.getHeight();
		BufferedImage after = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(((double) newWidth) / beforeWidth, ((double) newHeight)
				/ beforeHeight);
		AffineTransformOp scaleOp = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(image, after);
		return after;
	}
}
