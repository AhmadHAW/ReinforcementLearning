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

	public int[] getMaxQValueDirection() {
		double maxQValue = -Double.MAX_VALUE;
		int[] result = new int[4];
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxQValue < value) {
				result = new int[4];
				result[i] = 1;
				maxQValue = value;
			} else if (maxQValue == value) {
				result[i] = 1;
			}
		}
		return result;
	}

}
