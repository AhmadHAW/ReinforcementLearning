package algorithms;

import java.util.ArrayList;
import java.util.List;

import Environment.Environment;
import Environment.ReturnValue;
import Fachwerte.Position;
import Fachwerte.State;

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
		boolean result = false;
		while (!actualPosition.equals(goalPosition)) {
			// ab hier unterscheiden sich QLearning und Sarsa
			result = result || doStep();
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
		boolean result = updateQValue(actualPosition, maxDir, returnValue, maxQValueNextState, stepReward);
		actualPosition = nextPostition;
		return result;
	}

	@Override
	protected boolean doEpisodeWithoutTraining() {
		actualPosition = startPosition;
		boolean result = false;
		while (!actualPosition.equals(goalPosition)) {
			// ab hier unterscheiden sich QLearning und Sarsa
			result = result || doStepWithoutChanging();
			// bis hier unterscheiden sich QLearning und Sarsa
		}
		return result;
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
		boolean result = updateQValue(actualPosition, maxDir, returnValue, maxQValueNextState, stepReward);
		qValues[maxDir] = oldQValue;
		actualPosition = nextPostition;
		return result;
	}

	@Override
	public void reset() {
		mapOfAgent.clear();
		actualPosition = startPosition;
		bestResult = -Double.MAX_VALUE;
		bestSteps = Integer.MAX_VALUE;
		averageReward = 0;
		averageSteps = 0;
		bestPath = "";

	}

}
