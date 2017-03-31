import ij.*;
import ij.process.*;
import ij.plugin.filter.PlugInFilter;

public class MonScript implements PlugInFilter {
	
	public void run(ImageProcessor ip) {
		IJ.showMessage("Moyenne de gris : " + meanImage(ip));
		binarize(ip, 127);
	}
	
	public void binarize(ImageProcessor ip, int threshold) {
		//Question 6
		// ndg[i][j] = p[i*width + j]
		byte[] pixels = (byte[]) ip.getPixels();
		
		int height = ip.getHeight();
		int width = ip.getWidth();
		
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				int pix = pixels[(i * width) + j] & 0xff;
				
				if (pix < threshold) {
					pixels[(i * width) + j] = (byte) 0;
				}
				else {
					pixels[(i * width) + j] = (byte) 255;
				}
			}
		}
	}
	
	public double meanImage(ImageProcessor ip) {
		byte[] pixels = (byte[]) ip.getPixels();
		
		int height = ip.getHeight();
		int width = ip.getWidth();
		
		double sum = 0;
		
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				sum += pixels[(i * width) + j] & 0xff;
			}
		}
		
		return sum/(width * height);
	}
	
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			return DONE;
		}
		
		new ImageConverter(imp).convertToGray8();
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	}
}
