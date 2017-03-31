package App;

import java.io.File;
import java.util.ArrayList;

import aimage.*;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.*;

public class OCREngine implements PlugInFilter {
	private ArrayList<OCRImage> listeImg;
	
	@Override
	public void run(ImageProcessor ip) {
		
		IJ.showMessage("Moyenne de gris : ");
		
		ArrayList < Double > tab0 = new ArrayList< Double >(); 
		tab0.add(new Double (1.0)) ; 
		tab0.add(new Double (1.0));
		ArrayList< Double > tab1 = new ArrayList< Double >(); 
		tab1.add(new Double (5.0)); 
		tab1 . add (new Double( -1.0));
		ArrayList< Double > tab2 = new ArrayList< Double >(); 
		tab2.add (new Double(2.0)); 
		tab2.add(new Double(1.0));
		ArrayList< Double > tab3 = new ArrayList< Double >(); 
		tab3.add(new Double( -1.0)); tab3.add(new Double(0.0));
		ArrayList< ArrayList< Double > > myList = new ArrayList< ArrayList< Double > >();
		myList.add(tab1); 
		myList.add(tab2); 
		myList.add(tab3) ;
		IJ.showMessage("dist = " + CalculMath.PPV(tab0, myList, -1));
	}
	
	public static void main(String [] args) {
		ArrayList < Double > tab0 = new ArrayList< Double >(); 
		tab0.add(new Double (1.0)) ; 
		tab0.add(new Double (1.0));
		ArrayList< Double > tab1 = new ArrayList< Double >(); 
		tab1.add(new Double (5.0)); 
		tab1 . add (new Double( -1.0));
		ArrayList< Double > tab2 = new ArrayList< Double >(); 
		tab2.add (new Double(2.0)); 
		tab2.add(new Double(1.0));
		ArrayList< Double > tab3 = new ArrayList< Double >(); 
		tab3.add(new Double( -1.0)); tab3.add(new Double(0.0));
		ArrayList< ArrayList< Double > > myList = new ArrayList< ArrayList< Double > >();
		myList.add(tab1); 
		myList.add(tab2); 
		myList.add(tab3) ;
		System.out.println(CalculMath.PPV(tab0, myList, -1));
	}
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			return DONE;
		}
		
		new ImageConverter(imp).convertToGray8();
		return DOES_8G + DOES_STACKS + SUPPORTS_MASKING;
	}
	
	
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
