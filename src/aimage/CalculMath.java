package aimage;

import java.util.ArrayList;

public class CalculMath {

	public static double distEucli(ArrayList<Double> vect1, ArrayList<Double> vect2) {
		double sum=0;
		if (vect1.size() == vect2.size()) {
			for (int i = 0; i < vect1.size(); ++i) {
				sum += Math.pow(vect1.get(i) - vect2.get(i), 2);
			}
		}
		return Math.sqrt(sum);
	}
}
