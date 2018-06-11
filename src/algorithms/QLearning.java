package algorithms;

import Environment.Environment;
import Environment.ReturnValue;
import Fachwerte.Position;

public class QLearning extends ReinforcementLearningAlgorithm {

	public QLearning(double gamma, double alpha, Position goalPosition, int repeats, Environment env) {
		super(gamma, alpha, goalPosition, repeats, env);
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
	public boolean doEpisode() {
		actualPosition = startPosition;
		int steps = 0;
		boolean result = false;
		while (!actualPosition.equals(goalPosition) && steps <= 100) {
			++steps;

			// ab hier unterscheiden sich QLearning und Sarsa
			if (doStep()) {
				result = true;
			}
			// bis hier unterscheiden sich QLearning und Sarsa
		}
		return result;
	}

	private boolean doStep() {
		int maxDir = rd.nextInt(4);
		checkPositionInMap(actualPosition);
		double[] qValues = mapOfAgent.get(actualPosition).getqValues();
		double maxQValue = qValues[maxDir];
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxQValue < value) {
				maxDir = i;
				maxQValue = value;
			}
		}
		ReturnValue returnValueTupel = environment.doStep(actualPosition, maxDir);
		double returnValue = returnValueTupel.getReturnValue();
		Position nextPostition = returnValueTupel.getNewPosition();
		checkPositionInMap(nextPostition);
		int maxDirNextState = rd.nextInt(4);
		double[] qValuesNextState = mapOfAgent.get(nextPostition).getqValues();
		double maxQValueNextState = qValuesNextState[maxDirNextState];
		for (int i = 0; i < 4; ++i) {
			double value = qValuesNextState[i];
			if (maxQValueNextState < value) {
				maxDirNextState = i;
				maxQValueNextState = value;
			}
		}
		boolean result = updateQValue(actualPosition, maxDir, maxQValueNextState, returnValue);
		actualPosition = nextPostition;
		return result;
	}

	@Override
	public boolean doEpisodeWithoutTraining() {
		actualPosition = startPosition;
		int steps = 0;
		String path = "" + actualPosition;

		while (!actualPosition.equals(goalPosition) && steps <= 100) {
			// ab hier unterscheiden sich QLearning und Sarsa
			if (doStepWithoutChanging()) {
				return true;
			}

			path += "->" + actualPosition;
			++steps;
			// bis hier unterscheiden sich QLearning und Sarsa
		}
		if (bestSteps > steps) {
			bestSteps = steps;
			bestPath = path;
		}
		totalSteps += steps;
		return false;
	}

	private boolean doStepWithoutChanging() {
		int maxDir = rd.nextInt(4);
		checkPositionInMap(actualPosition);
		double[] qValues = mapOfAgent.get(actualPosition).getqValues();
		double maxQValue = qValues[maxDir];
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxQValue < value) {
				maxDir = i;
				maxQValue = value;
			}
		}
		ReturnValue returnValueTupel = environment.doStep(actualPosition, maxDir);
		double returnValue = returnValueTupel.getReturnValue();
		Position nextPostition = returnValueTupel.getNewPosition();
		checkPositionInMap(nextPostition);
		int maxDirNextState = rd.nextInt(4);
		double[] qValuesNextState = mapOfAgent.get(nextPostition).getqValues();
		double maxQValueNextState = qValuesNextState[maxDirNextState];
		for (int i = 0; i < 4; ++i) {
			double value = qValuesNextState[i];
			if (maxQValueNextState < value) {
				maxDirNextState = i;
				maxQValueNextState = value;
			}
		}
		double oldQValue = qValues[maxDir];
		boolean result = updateQValue(actualPosition, maxDir, maxQValueNextState, returnValue);
		qValues[maxDir] = oldQValue;
		actualPosition = nextPostition;
		return result;
	}

}
