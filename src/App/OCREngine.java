package App;

import java.io.File;
import java.util.ArrayList;

import aimage.*;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.*;

public class OCREngine {
	private ArrayList<OCRImage> listeImg;
	
	public void createListeImage(String path, ArrayList<OCRImage> listeImg) {
		File[] files = listFiles(path);
		
		if (files.length != 0) {
			for (int i=0; i < files.length; i++) {
				ImagePlus tempImg = new ImagePlus(files[i].getAbsolutePath());
				new ImageConverter(tempImg).convertToGray8();
				listeImg.add(new OCRImage(tempImg, files[i].getName().substring(0,1).charAt(0),
						files[i].getAbsolutePath()));
			}
		}
	}
	
	public File[] listFiles(String directoryPath) {
		File[] files = null;
		File directoryToScan = new File(directoryPath);
		files = directoryToScan.listFiles();
		return files;
	}
}
