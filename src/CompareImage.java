import java.io.File;
import ij.*;
import ij.process.*;
import ij.plugin.filter.PlugInFilter;

public class CompareImage implements PlugInFilter {
	
	public void run(ImageProcessor ip) {
		String path = "ImageJ/plugins/data-tp3/";
		File[] files = listFiles(path);
		if (files != null) {
			IJ.showMessage("ON A DES IMAGES");
			double gap = Double.MAX_VALUE;
			double avgIp = meanImage(ip);
			String pathBestImage = null;
			
			
			for (int i=0; i < files.length; i++) {
				if (!files[i].isHidden() ) {
					String filePath = files[i].getAbsolutePath();
					ImagePlus tempImg = new ImagePlus(filePath);
					
					new ImageConverter(tempImg).convertToGray8();
					
					ImageProcessor ipTemp = tempImg.getProcessor();
					
					double avgTemp = meanImage(ipTemp);
					double dif = Math.abs(avgIp - avgTemp);
					
					if (dif < gap) {
						gap = dif;
						pathBestImage = filePath;
					}
				}
			}
			
			String closestImageName = pathBestImage;
			IJ.showMessage("L'image la plus proche est " + closestImageName + " avec une distance de " + gap + ".");
		}
		else
			IJ.showMessage("J'ai pas d'images mec");
	}
	
	public File[] listFiles(String directoryPath) {
		File[] files = null;
		File directoryToScan = new File(directoryPath);
		files = directoryToScan.listFiles();
		return files;
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
