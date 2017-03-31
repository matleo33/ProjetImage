package App;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;

import aimage.*;
import ij.ImagePlus;
import ij.process.*;

public class OCREngine {
	
	private ArrayList<OCRImage> listImg;
	
	OCREngine() {
		listImg = new ArrayList<OCRImage>();
	}
	
	public void createListImage(String path, ArrayList<OCRImage> listeImg) {
		File[] files = listFiles(path);
		
		if (files != null) {
			if (files.length != 0) {
				for (int i=0; i < files.length; i++) {
					ImagePlus tempImg = new ImagePlus(files[i].getAbsolutePath());
					new ImageConverter(tempImg).convertToGray8();
					listeImg.add(new OCRImage(tempImg, files[i].getName().substring(0,1).charAt(0),
							files[i].getAbsolutePath()));
				}
			}
		}
		else {
			System.out.println("Aucun dossier image trouvé");
		}
	}
	
	public File[] listFiles(String directoryPath) {
		File[] files = null;
		File directoryToScan = new File(directoryPath);
		files = directoryToScan.listFiles();
		return files;
	}
	
	public void logOCR(String pathOut)
    {
        int[][] matriceDeConfusion=new int[10][10];
        int ligne;
        int colonne;
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                matriceDeConfusion[i][j]=0;
            }
        }

        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
            	matriceDeConfusion[Integer.parseInt(String.valueOf(listImg.get(i*10+j).getLabel()))][Integer.parseInt(String.valueOf(listImg.get(i*10+j).getDecision()))]++;
            }
        }
        try{
            File file = new File(pathOut);
            file.createNewFile();
            FileWriter fileWriter=new FileWriter(file);
            String tmpStr;
            fileWriter.write("Test OCR effectué le " + new Date().toString() + "\r\n");
            
            for(int i=0;i<10;i++){
                fileWriter.write(Integer.toString(i) + " ");
            }
            
            fileWriter.write("\r\n********************\r\n");
            for(int i=0;i<10;i++)
            {
                for(int j=0;j<10;j++)
                {
                    tmpStr=Integer.toString(matriceDeConfusion[i][j]);
                    fileWriter.write(tmpStr);
                    fileWriter.write(" ");
                }
                fileWriter.write("\r\n");
            }
            
            fileWriter.write("\r\n");
            
            String plusieurs;
            if(getPerCent(matriceDeConfusion)*100.0 > 2)
                plusieurs = " pourcents de réussite.";
            else
                plusieurs = " pourcent de réussite.";
            fileWriter.write(Double.toString(getPerCent(matriceDeConfusion)*100.0)+ plusieurs);
            fileWriter.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public double getPerCent(int[][] matrice){
        double sum=0.0;
        for (int i=0; i<10; i++){
            sum+=(double)matrice[i][i];
        }
        return(sum/120.0);
    }

    public void resizeAll(){
        for(OCRImage ocr : this.listImg){
            ocr.resize(ocr.getImg(), 20, 20);
        }
    }


	public ArrayList<OCRImage> getListeImg() {
		return listImg;
	}
}
