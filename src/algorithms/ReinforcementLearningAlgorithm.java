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

	protected final double gamma; // discount rate
	protected final double alpha; // learn rate
	protected Map<Position, State> mapOfAgent = new HashMap<>();
	protected final Position startPosition;
	protected final Position goalPosition;
	protected Position actualPosition;
	protected final Environment environment;
	protected final double stepReward;
	protected final int reapeatsUntilTrained;
	protected final Random rd = new Random();

	protected int bestSteps = Integer.MAX_VALUE;
	protected int totalSteps = 0;
	protected int totalEpisodesUntilTrained = 0;
	protected int bestEpisodesUntilTrained = Integer.MAX_VALUE;
	protected String bestPath = "";

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

	protected boolean updateQValue(Position position, int direction, double rewardOfNextState, double reward) {
		double[] qValues = mapOfAgent.get(position).getqValues();
		int[] positionArray = getPositionArray(qValues);
		qValues[direction] = qValues[direction] + alpha * (reward + gamma * rewardOfNextState - qValues[direction]);
		int[] positionArrayAfter = getPositionArray(qValues);
		for (int i = 0; i < 4; ++i) {
			if (positionArray[i] != positionArrayAfter[i]) {
				return true;
			}
		}
		return false;
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
		int episodesUntilTrained = 0;
		boolean hasChanged = doEpisode();
		while ((hasChanged || !isTrained()) && episodesUntilTrained <= 100) {
			hasChanged = doEpisode();
			++episodesUntilTrained;
		}
		if (episodesUntilTrained > 100) {
			bestPath = "notSolveable";
		} else if (bestEpisodesUntilTrained > episodesUntilTrained) {
			bestEpisodesUntilTrained = episodesUntilTrained;
		}
		totalEpisodesUntilTrained += episodesUntilTrained;
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
				checkPositionInMap(Position.getField(x, y));
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

	public void reset() {
		resetBoard();
		bestSteps = Integer.MAX_VALUE;
		totalSteps = 0;
		totalEpisodesUntilTrained = 0;
		bestEpisodesUntilTrained = Integer.MAX_VALUE;
		bestPath = "";
	}

	public void resetSteps() {
		totalSteps = 0;
		bestSteps = Integer.MAX_VALUE;
		bestPath = "";
	}

	public void resetBoard() {
		mapOfAgent.clear();
		actualPosition = startPosition;
	}

	public abstract boolean doEpisodeWithoutTraining();

	public int getBestSteps() {
		return bestSteps;
	}

	public int getTotalSteps() {
		return totalSteps;
	}

	public int getTotalEpisodesUntilTrained() {
		return totalEpisodesUntilTrained;
	}

	public int getBestEpisodesUntilTrained() {
		return bestEpisodesUntilTrained;
	}

	public String getBestPath() {
		return bestPath;
	}

}