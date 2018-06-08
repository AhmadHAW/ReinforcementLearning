package Fachwerte;

import java.util.HashSet;
import java.util.Set;

public class State {

	private final double[] qValues = new double[4];

	public double[] getqValues() {
		return qValues;
	}

	public void setValue(int index, double value) {
		qValues[index] = value;
	}

}
