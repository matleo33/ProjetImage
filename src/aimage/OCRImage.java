package aimage;

import java.util.ArrayList;

import ij.*;

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
		this.decision = '?';
		vect = new ArrayList<Double>();
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
