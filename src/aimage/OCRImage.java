package aimage;

import java.util.ArrayList;
import java.util.Date;

import ij.*;
import ij.process.ImageProcessor;

public class OCRImage {
	private ImagePlus img;
	private char label;

	private String path;
	private char decision;

	private ArrayList< Double > vect;

	private ArrayList< Double > profilHV ;

	private ArrayList< Double > vectRapportIso;

	private ArrayList< Double > vectZoning;
	
	public OCRImage(ImagePlus img, char label, String path) {
		this.img = img;
		this.label = label;
		this.path = path;
		
		int val = (int) (Math.random()*10.0);
		String i = Integer.toString(val);
		this.decision = i.charAt(0);
		
		vect = new ArrayList<Double>();

		profilHV = new ArrayList< Double >();

		vectRapportIso = new ArrayList< Double >();

        vectZoning = new ArrayList< Double >();
    }
	
	private double averageNdG() {
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

	private double averageNdG(ArrayList< Integer > arrayListPixels) {

		double sum = 0;

		for (Integer valuePixel : arrayListPixels) {
			sum += valuePixel;
		}

		return sum/arrayListPixels.size();
	}

	public void setFeatureNdG() {
		vect.add(averageNdG());
	}
	
	public void resize(ImagePlus img) {

		ImageProcessor ip = img.getProcessor();
		ip.setInterpolate(true) ;
		ip = ip.resize(21, 21) ;
		getImg().setProcessor(null, ip);
	}

	private ArrayList< Double > setFeatureProfilH() {

		ArrayList< Double > profilH = new ArrayList< Double >();
		ImageProcessor imageProcessor = this.img.getProcessor();

		byte[] pixels = (byte[]) imageProcessor.getPixels();

		ArrayList< Integer > rowPixel = new ArrayList< Integer >();
		for (int col = 0; col < imageProcessor.getHeight(); ++col) {
			for (int row = 0; row < imageProcessor.getWidth(); ++row) {
				rowPixel.add(pixels[(row * imageProcessor.getWidth()) + col] & 0xff);
			}

            // De se qu'on a compris on n'ajoute que la moyenne de NdG dans la matrice
			profilH.add(averageNdG(rowPixel));
			rowPixel.clear();
		}

		return profilH;
	}

	private ArrayList< Double > setFeatureProfilV() {

		ArrayList< Double > profilV = new ArrayList< Double >();
		ImageProcessor imageProcessor = this.img.getProcessor();

		byte[] pixels = (byte[]) imageProcessor.getPixels();

		ArrayList< Integer > colPixel = new ArrayList< Integer >();
		for (int col = 0; col < imageProcessor.getWidth(); ++col) {
			for (int row = 0; row < imageProcessor.getHeight()*imageProcessor.getWidth(); row += imageProcessor.getWidth()) {
				colPixel.add(pixels[row + col] & 0xff);
			}

			// De se qu'on a compris on n'ajoute que la moyenne de NdG dans la matrice
			profilV.add(averageNdG(colPixel));
			colPixel.clear();
		}

		return profilV;
	}

	public void setFeatureProfilHV() {

		profilHV.addAll(setFeatureProfilV());
		profilHV.addAll(setFeatureProfilH());
	}

	public void setRapportIso() {
		
		ImageProcessor ip = this.img.getProcessor();
		byte[] pixels = (byte[]) ip.getPixels();
		
		int surface = 0;
		int perimeter = 0;
		int threshold = 90; // Seuil en desous du quel on considére que c'est des nuances de gris tendant vers le noir
		for(int col = 0; col < ip.getWidth(); ++col){
			for(int row = 0; row < ip.getHeight(); ++row){

			    int temp = (pixels [(row * ip.getWidth()) + col] & 0xff);
				if (temp < threshold){
					
					surface++;
					if (checkWhiteNeighbors(pixels, col, row, ip.getHeight(), ip.getWidth(), threshold)){
						perimeter++;
					}
				}
			}
		}

		vectRapportIso.add((4 * Math.PI * surface)/Math.pow(perimeter, 2));
	}

	private boolean checkWhiteNeighbors(byte[] pixels, int col, int row, int height, int width, int threshold) {

		boolean found = false;

        if (row != 0 && (pixels[((row * width) + col) - height] & 0xff) > threshold) { // On vérifie en haut
			found = true;
		}
		else if (row != height - 1 && (pixels[((row * width) + col) + height] & 0xff) > threshold) { // On vérifie en bas
			found = true;
		}
		else if (col != 0 && (pixels[((row * width) + col) - 1] & 0xff) > threshold) { // On vérifie à gauche
			found = true;
		}
		else if (col != height - 1 && (pixels[((row * width) + col) + 1] & 0xff) > threshold) { // On vérifie à droite
			found = true;
		}

		return found;
	}


	public void setZoning() {
        ImageProcessor ip = img.getChannelProcessor();
        byte[] pixels = (byte[]) ip.getPixels();
        int height = img.getHeight();
        int width = img.getWidth();


        ArrayList< Integer > block_1 = new ArrayList< Integer >();
        ArrayList< Integer > block_2 = new ArrayList< Integer >();
        ArrayList< Integer > block_3 = new ArrayList< Integer >();
        ArrayList< Integer > block_4 = new ArrayList< Integer >();
        ArrayList< Integer > block_5 = new ArrayList< Integer >();
        ArrayList< Integer > block_6 = new ArrayList< Integer >();
        ArrayList< Integer > block_7 = new ArrayList< Integer >();
        ArrayList< Integer > block_8 = new ArrayList< Integer >();
        ArrayList< Integer > block_9 = new ArrayList< Integer >();

        for (int col = 0; col < height; col++) {
            for (int row = 0; row < width; row++) {
                int pix = pixels[col * height + row] & 0xff;
                if (col <= height / 3) {
                    if (row < width / 3) {
                        block_1.add(pix);
                    } else if (row > width / 3 && row <= (2 * width / 3)) {
                        block_2.add(pix);
                    } else {
                        block_3.add(pix);
                    }
                } else if (col > height / 3 && col < (2 * height / 3)) {
                    if (row < width / 3) {
                        block_4.add(pix);
                    } else if (row > width / 3 && row <= (2 * width / 3)) {
                        block_5.add(pix);
                    } else {
                        block_6.add(pix);
                    }
                } else {
                    if (row < width / 3) {
                        block_7.add(pix);
                    } else if (row > width / 3 && row <= (2 * width / 3)) {
                        block_8.add(pix);
                    } else {
                        block_9.add(pix);
                    }
                }
            }
        }

        vectZoning.add(averageNdG(block_1));
        vectZoning.add(averageNdG(block_2));
        vectZoning.add(averageNdG(block_3));
        vectZoning.add(averageNdG(block_4));
        vectZoning.add(averageNdG(block_5));
        vectZoning.add(averageNdG(block_6));
        vectZoning.add(averageNdG(block_7));
        vectZoning.add(averageNdG(block_8));
        vectZoning.add(averageNdG(block_9));
    }

	// GETTER AND SETTER

	public char getLabel() {
		return label;
	}

	public char getDecision() {
		return decision;
	}

	public void setDecision(char decision) {
		this.decision = decision;
	}

	public ImagePlus getImg() {
		return img;
	}

	public ArrayList<Double> getVect() {
		return vect;
	}

	public ArrayList<Double> getProfilHV() {
		return profilHV;
	}

	public ArrayList<Double> getVectRapportIso() {
		return vectRapportIso;
	}

    public ArrayList<Double> getVectZoning() {
        return vectZoning;
    }
}
