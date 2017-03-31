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
	
	public void resize(ImagePlus img, int larg, int haut) {
		ImageProcessor ip2 = img.getProcessor();
		ip2.setInterpolate(true) ;
		ip2 = ip2.resize(larg, haut) ;
		getImg().setProcessor(null, ip2);
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
