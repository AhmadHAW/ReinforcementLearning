package Fachwerte;

import java.util.HashSet;
import java.util.Set;

public class State {

	private final int[] qValues = new int[4];

	public int[] getqValues() {
		return qValues;
	}

	public void setValue(int index, int value) {
		qValues[index] = value;
	}

}
