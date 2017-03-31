package App;

public class Main {
	
	public static void main(String [] args) {
		/* FONCTIONNE
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
		System.out.println(CalculMath.PPV(tab0, myList, -1));*/
		
		OCREngine ocrEngine = new OCREngine();
		ocrEngine.createListImage("../../../data-tp3/", ocrEngine.getListeImg());
		
		ocrEngine.logOCR("../../../MatriceConfusion");
	}
}
