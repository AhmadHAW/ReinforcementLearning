package algorithms;

import java.util.ArrayList;
import java.util.Map;

import Fachwerte.Position;
import Fachwerte.State;

public class Sarsa extends ReinforcementLearningAlgorithm {

	public Sarsa(double gamma, double alpha, Position startPosition, Position goalPosition, int repeats) {
		//super(gamma, alpha, goalPosition, repeats, null);
		super(gamma, alpha, startPosition, goalPosition, repeats);
	}

	@Override
	public boolean isTrained() {
		for (int i = 0; i < reapeatsUntilTrained; ++i) {
			if (doEpisodeWithoutTraining()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void train() {
		while (!isTrained()) {
			doEpisode();
		}
	}

	@Override
	public boolean doEpisode() {
		return true;
	}

	@Override
	protected boolean doEpisodeWithoutTraining() {
		return false;
	}

	@Override
	public void reset() {
		mapOfAgent = (Map<Position, State>) new ArrayList<>();
		actualPosition = startPosition;
		bestResult = -Double.MAX_VALUE;
		bestSteps = Integer.MAX_VALUE;
		averageReward = 0;
		averageSteps = 0;
		bestPath = "";

	}
}
