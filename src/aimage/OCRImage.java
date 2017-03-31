package aimage;

import java.util.ArrayList;

import ij.*;
import ij.process.ImageProcessor;

public class OCRImage {
	private ImagePlus img;
	private char label;
	
	private String path;
	private char decision;
	private ArrayList<Double> vect;
	
	public OCRImage(ImagePlus img, char label, String path) {
		this.img = img;
		this.label = label;
		this.path = path;
		
		int val = (int) (Math.random()*10.0);
		String i = Integer.toString(val);
		char a = i.charAt(0);
		this.decision = a;
		
		vect = new ArrayList<Double>();
	}
	
	public double averageNdG() {
		ImageProcessor ip = img.getProcessor();
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
	
	public void setFeatureNdG() {
		vect.add(averageNdG());
	}
	
	public void resize(ImagePlus img, int width, int height) {
		ImageProcessor ip = img.getProcessor();
		ip.setInterpolate(true) ;
		ip = ip.resize(width, height) ;
		getImg().setProcessor(null, ip);
	}

	public char getLabel() {
		return label;
	}


	public char getDecision() {
		return decision;
	}


	public ImagePlus getImg() {
		return img;
	}

	public void setImg(ImagePlus img) {
		this.img = img;
	}

	public Double getVect(int i) {
		return vect.get(i);
	}

	public void setVect(int i, double val) {
		this.vect.set(i, val);
	}
}
