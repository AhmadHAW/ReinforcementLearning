package algorithms;

import Environment.Environment;
import Environment.ReturnValue;
import Fachwerte.Position;

public class Sarsa extends ReinforcementLearningAlgorithm {

	private ReturnValue nextReturnValue;

	public Sarsa(double gamma, double alpha, Position goalPosition, int repeats, Environment env) {
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
		nextReturnValue = environment.doStep(startPosition, rd.nextInt(4));
		String path = "";
		int steps = 0;
		boolean result = false;
		while (!actualPosition.equals(goalPosition)) {
			++steps;
			path += actualPosition + " -> ";
			// ab hier unterscheiden sich QLearning und Sarsa
			if (doStep()) {
				result = true;
			}
			path += actualPosition;
			// bis hier unterscheiden sich QLearning und Sarsa
		}
		if (bestSteps > steps) {
			bestSteps = steps;
			bestPath = path;
		}
		totalSteps += steps;
		return result;
	}

	private boolean doStep() {
		int maxDir = nextReturnValue.getDirection();
		checkPositionInMap(actualPosition);
		ReturnValue returnValueTupel = nextReturnValue;
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
		nextReturnValue = environment.doStep(nextPostition, maxDirNextState);
		maxQValueNextState = qValuesNextState[nextReturnValue.getDirection()];
		boolean result = updateQValue(actualPosition, maxDir, maxQValueNextState, returnValue);
		actualPosition = nextPostition;
		return result;
	}

	@Override
	protected boolean doEpisodeWithoutTraining() {
		actualPosition = startPosition;
		nextReturnValue = environment.doStep(startPosition, rd.nextInt(4));
		while (!actualPosition.equals(goalPosition)) {
			// ab hier unterscheiden sich QLearning und Sarsa
			if (doStepWithoutChanging()) {
				return true;
			}
			// bis hier unterscheiden sich QLearning und Sarsa
		}
		return false;
	}

	private boolean doStepWithoutChanging() {
		int maxDir = nextReturnValue.getDirection();
		ReturnValue returnValueTupel = nextReturnValue;
		double returnValue = returnValueTupel.getReturnValue();
		double[] qValues = mapOfAgent.get(actualPosition).getqValues();
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
		nextReturnValue = environment.doStep(nextPostition, maxDirNextState);
		maxQValueNextState = qValuesNextState[nextReturnValue.getDirection()];
		double oldQValue = qValues[maxDir];
		boolean result = updateQValue(actualPosition, maxDir, maxQValueNextState, returnValue);
		qValues[maxDir] = oldQValue;
		actualPosition = nextPostition;
		return result;
	}

}
