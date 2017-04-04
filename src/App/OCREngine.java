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



	void createListImage(String path, ArrayList<OCRImage> listeImg) {
		File[] files = listFiles(path);
		
		if (files != null) {
			if (files.length != 0) {
                for (File file : files) {
                    ImagePlus tempImg = new ImagePlus(file.getAbsolutePath());
                    new ImageConverter(tempImg).convertToGray8();
                    listeImg.add(new OCRImage(tempImg, file.getName().substring(0, 1).charAt(0),
                            file.getAbsolutePath()));
                }

                resizeAll();
			}
		}
		else {
			System.out.println("Aucun dossier image trouvé");
		}
	}
	
	private File[] listFiles(String directoryPath) {
		File[] files = null;
		File directoryToScan = new File(directoryPath);
		files = directoryToScan.listFiles();
		return files;
	}
	
	void logOCR(String pathOut)
    {
        int[][] matriceDeConfusion=new int[10][10];
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
                if (Integer.parseInt(String.valueOf(listImg.get(i*10+j).getDecision())) != -1)
            	    matriceDeConfusion[Integer.parseInt(String.valueOf(listImg.get(i*10+j).getLabel()))][Integer.parseInt(String.valueOf(listImg.get(i*10+j).getDecision()))]++;
            }
        }
        try{
            File file = new File(pathOut);
            FileWriter fileWriter=new FileWriter(file);
            String tmpStr;
            fileWriter.write("Test OCR effectué le " + new Date().toString() + "\r\n");
            
            for(int i=0;i<10;i++){
                if (i == 0) {
                    fileWriter.write("    ");
                }
                fileWriter.write(Integer.toString(i) + " ");
            }
            
            fileWriter.write("\r\n    ********************\r\n");
            for(int i=0;i<10;i++)
            {
                for(int j=0;j<10;j++)
                {
                    if (j == 0) {
                        tmpStr = i + " * " + Integer.toString(matriceDeConfusion[i][j]);
                    }
                    else {
                        tmpStr = Integer.toString(matriceDeConfusion[i][j]);
                    }

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

    private double getPerCent(int[][] matrice){
        double sum=0.0;
        for (int i=0; i<10; i++){
            sum+=(double)matrice[i][i];
        }
        return sum/100.0;
    }

    private void resizeAll(){
        for(OCRImage ocr : this.listImg){
            ocr.resize(ocr.getImg());
        }
    }

    private void setFeatureNdgVect() {
        for (OCRImage ocrImage : listImg) {
            ocrImage.setFeatureNdG();
        }
    }

    void compareNdG() {

        setFeatureNdgVect();

        ArrayList< ArrayList< Double > > vectCarac = new ArrayList< ArrayList < Double >  >();

        for (OCRImage aListImg : listImg) {
            vectCarac.add(aListImg.getVect());
        }

        int count = 0;
        for (OCRImage ocrImage : listImg) {
            int indice = CalculMath.PPV(ocrImage.getVect(), vectCarac, count);
            ocrImage.setDecision(listImg.get(indice).getLabel());

            count++;
        }
    }

    void compareProfilHV() {
        for (OCRImage image : listImg) {
            image.setFeatureProfilHV();
        }

        ArrayList< ArrayList< Double > > vectCarac = new ArrayList< ArrayList < Double >  >();

        for (OCRImage aListImg : listImg) {
            vectCarac.add(aListImg.getProfilHV());
        }

        int count = 0;
        for (OCRImage ocrImage : listImg) {
            int indice = CalculMath.PPV(ocrImage.getProfilHV(), vectCarac, count);
            ocrImage.setDecision(listImg.get(indice).getLabel());

            count++;
        }
    }

    void compareRapportIso() {
        for (OCRImage image : listImg) {
            image.setRapportIso();
        }

        ArrayList< ArrayList< Double > > vectCarac = new ArrayList< ArrayList < Double >  >();

        for (OCRImage aListImg : listImg) {
            vectCarac.add(aListImg.getVectRapportIso());
        }

        int count = 0;
        for (OCRImage ocrImage : listImg) {

            int indice = CalculMath.PPV(ocrImage.getVectRapportIso(), vectCarac, count);
            ocrImage.setDecision(listImg.get(indice).getLabel());
            count++;
        }
    }

    void compareZoning() {
        for (OCRImage image : listImg) {
            image.setZoning();
        }

        ArrayList< ArrayList< Double > > vectCarac = new ArrayList< ArrayList < Double >  >();

        for (OCRImage aListImg : listImg) {
            vectCarac.add(aListImg.getVectZoning());
        }

        int count = 0;
        for (OCRImage ocrImage : listImg) {

            int indice = CalculMath.PPV(ocrImage.getVectZoning(), vectCarac, count);
            ocrImage.setDecision(listImg.get(indice).getLabel());
            count++;
        }
    }

	ArrayList<OCRImage> getListeImg() {
		return listImg;
	}
}
