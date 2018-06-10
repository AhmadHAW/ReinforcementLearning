package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Environment.Environment;
import Fachwerte.Position;
import Fachwerte.State;

public abstract class ReinforcementLearningAlgorithm {

	protected final double gamma;
	protected final double alpha;
	protected Map<Position, State> mapOfAgent = new HashMap<>();
	protected final Position startPosition;
	protected final Position goalPosition;
	protected Position actualPosition;
	protected double bestResult = -Double.MAX_VALUE;
	protected int bestSteps = Integer.MAX_VALUE;
	protected double averageReward = 0;
	protected double averageSteps = 0;
	protected String bestPath = "";
	protected final Environment environment;
	protected final double stepReward;

	protected final int reapeatsUntilTrained;
	protected final Random rd = new Random();

	public ReinforcementLearningAlgorithm(double gamma, double alpha, Position goalPosition, int repeats,
			Environment env) {
		super();
		this.gamma = gamma;
		this.alpha = alpha;
		this.startPosition = env.getStartPosition();
		this.goalPosition = goalPosition;
		this.reapeatsUntilTrained = repeats;
		this.environment = env;
		stepReward = environment.getStepReward();
	}

	protected boolean updateQValue(Position position, int direction, double reward, double rewardOfNextState,
			double stepReward) {
		double[] qValues = mapOfAgent.get(position).getqValues();
		int[] positionArray = getPositionArray(qValues);
		qValues[direction] = qValues[direction] + alpha * (stepReward + gamma * rewardOfNextState - qValues[direction]);
		int[] positionArrayAfter = getPositionArray(qValues);
		return positionArray.equals(positionArrayAfter);
	}

	private int[] getPositionArray(double[] qValues) {
		qValues = qValues.clone();
		int[] result = new int[4];
		for (int i = 0; i < 4; ++i) {
			int index = findMax(qValues);
			result[i] = index;
			qValues[index] = Integer.MIN_VALUE;
		}
		return result;
	}

	private int findMax(double[] qValues) {
		int index = 0;
		double value = qValues[index];
		for (int j = 0; j < qValues.length; ++j) {
			if (qValues[j] > value) {
				value = qValues[j];
				index = j;
			}
		}
		return index;
	}

	protected boolean doAsIfUpdateQValue(Position position, int direction, double reward, double rewardOfNextState,
			double stepReward) {
		int maxDir = rd.nextInt(4);
		double[] qValues = mapOfAgent.get(position).getqValues();
		double maxValue = qValues[maxDir];
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxValue < value) {
				maxValue = value;
				maxDir = i;
			}
		}
		double oldQValue = qValues[direction];
		qValues[direction] = qValues[direction] + alpha * (stepReward + gamma * rewardOfNextState - qValues[direction]);
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxValue < value) {
				if (i != maxDir) {
					qValues[direction] = oldQValue;
					return true;
				}
				maxValue = value;
			}
		}
		qValues[direction] = oldQValue;
		return false;
	}

	protected void checkPositionInMap(Position position) {
		if (!mapOfAgent.containsKey(position)) {
			mapOfAgent.put(position, new State());
		}
	}

	public void train() {
		int count = 0;
		boolean isTrained = doEpisode();
		System.out.println("doEpisode");
		while (!isTrained || !isTrained()) {
			doEpisode();
			if ((count % 1000000) == 0) {
				printMapOfAgent();
			}
			++count;
		}
		// for (int i = 0; i < 100; ++i) {
		// doEpisode();
		// }
		// printMapOfAgent();
	}

	public void printMapOfAgent() {
		String result = "";
		int maxLength = 0;
		int maxX = 0;
		int maxY = 0;
		for (int x = 0; x < environment.getActualMap().length; ++x) {
			for (int y = 0; y < environment.getActualMap()[x].length; ++y) {
				Position pos = Position.getField(x, y);
				if (!mapOfAgent.containsKey(pos)) {
					mapOfAgent.put(pos, new State());
				}
				for (double qValue : mapOfAgent.get(pos).getqValues()) {
					if (("" + qValue).length() > maxLength) {
						maxLength = ("" + qValue).length();
					}
				}
				if (y > maxY) {
					maxY = y;
				}
			}
			if (x > maxX) {
				maxX = x;
			}
		}
		maxLength += 2;
		if (maxLength < 6) {
			maxLength = 6;
		}
		for (int y = 0; y < environment.getActualMap().length; ++y) {
			String subResult = "";
			for (int x = 0; x < environment.getActualMap().length; ++x) {
				State actualState = mapOfAgent.get(Position.getField(x, y));
				// obere Zeile
				subResult = "";
				for (int length = 0; length < maxLength; ++length) {
					subResult = " " + subResult;
				}
				result += subResult;
				subResult = "" + actualState.getqValues()[0];
				subResult = normiereSubResult(subResult, maxLength);
				result += subResult;
				for (int length = 0; length < maxLength; ++length) {
					result = result + " ";
				}
			}
			result += "\n";
			// mittlere Zeile
			for (int x = 0; x < environment.getActualMap().length; ++x) {

				// links
				State actualState = mapOfAgent.get(Position.getField(x, y));
				subResult = "" + actualState.getqValues()[3];
				subResult = normiereSubResult(subResult, maxLength);
				result = result + subResult;
				// Pfeil
				subResult = "";

				if (actualState.getMaxQValueDirection()[0] == 1) {
					subResult += "N";
				}
				if (actualState.getMaxQValueDirection()[1] == 1) {
					subResult += "O";
				}
				if (actualState.getMaxQValueDirection()[2] == 1) {
					subResult += "S";
				}
				if (actualState.getMaxQValueDirection()[3] == 1) {
					subResult += "W";
				}
				subResult = normiereSubResult(subResult, maxLength);
				result += subResult;
				// rechts
				subResult = "" + actualState.getqValues()[1];
				subResult = normiereSubResult(subResult, maxLength);
				result += subResult;

			}
			result += "\n";
			for (int x = 0; x < environment.getActualMap().length; ++x) {
				// unten
				State actualState = mapOfAgent.get(Position.getField(x, y));
				subResult = "";
				for (int length = 0; length < maxLength; ++length) {
					subResult = " " + subResult;
				}
				result += subResult;
				subResult = "" + actualState.getqValues()[2];
				subResult = normiereSubResult(subResult, maxLength);
				result += subResult;
				for (int length = 0; length < maxLength; ++length) {
					result = result + " ";
				}
			}
			result += "\n--\n";
		}
		System.out.print(result);
	}

	private String normiereSubResult(String subResult, int maxLength) {
		if ((maxLength - subResult.length()) % 2 == 1) {
			subResult += " ";
		}
		while (subResult.length() < maxLength) {
			subResult = " " + subResult + " ";
		}
		return subResult;
	}

	public abstract boolean isTrained();

	public abstract boolean doEpisode();

	public abstract void reset();

	protected abstract boolean doEpisodeWithoutTraining();

}