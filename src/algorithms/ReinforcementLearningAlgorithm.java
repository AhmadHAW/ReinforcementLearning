package algorithms;

import java.util.ArrayList;
import java.util.List;

import Fachwerte.State;

public abstract class ReinforcementLearningAlgorithm {

	protected final double gamma;
	protected final double alpha;
	protected final List<List<State>> mapOfAgent = new ArrayList<>();

}
