package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Environment.Environment;
import Fachwerte.Position;
import Fachwerte.State;

public abstract class ReinforcementLearningAlgorithm {

	protected final double gamma;
	protected final double alpha;
	protected List<List<State>> mapOfAgent = new ArrayList<>();
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

	public double getGamma() {
		return gamma;
	}

	public double getAlpha() {
		return alpha;
	}

	public List<List<State>> getMapOfAgent() {
		return mapOfAgent;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public Position getGoalPosition() {
		return goalPosition;
	}

	public Position getActualPosition() {
		return actualPosition;
	}

	public void setActualPosition(Position actualPosition) {
		this.actualPosition = actualPosition;
	}

	public double getBestResult() {
		return bestResult;
	}

	public void setBestResult(double bestResult) {
		this.bestResult = bestResult;
	}

	public int getBestSteps() {
		return bestSteps;
	}

	public void setBestSteps(int bestSteps) {
		this.bestSteps = bestSteps;
	}

	public double getAverageReward() {
		return averageReward;
	}

	public void setAverageReward(double averageReward) {
		this.averageReward = averageReward;
	}

	public double getAverageSteps() {
		return averageSteps;
	}

	public void setAverageSteps(double averageSteps) {
		this.averageSteps = averageSteps;
	}

	public String getBestPath() {
		return bestPath;
	}

	public void setBestPath(String bestPath) {
		this.bestPath = bestPath;
	}

	public int getReapeatsUntilTrained() {
		return reapeatsUntilTrained;
	}

	protected boolean updateQValue(Position position, int direction, double reward, double rewardOfNextState,
			double stepReward) {
		int maxDir = rd.nextInt(4);
		double[] qValues = mapOfAgent.get(position.getX()).get(position.getY()).getqValues();
		double maxValue = qValues[maxDir];
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxValue < value) {
				maxValue = value;
				maxDir = i;
			}
		}
		qValues[direction] = qValues[direction] + alpha * (stepReward + gamma * rewardOfNextState - qValues[direction]);
		for (int i = 0; i < 4; ++i) {
			double value = qValues[i];
			if (maxValue < value) {
				if (i != maxDir) {
					return true;
				}
				maxValue = value;
			}
		}
		return false;
	}

	protected boolean doAsIfUpdateQValue(Position position, int direction, double reward, double rewardOfNextState,
			double stepReward) {
		int maxDir = rd.nextInt(4);
		double[] qValues = mapOfAgent.get(position.getX()).get(position.getY()).getqValues();
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
		int xBoarder = 0;
		int yBoarder = 0;
		if (mapOfAgent.size() <= xBoarder) {
			// tobe continued
		}
	}

	public abstract boolean isTrained();

	public abstract void train();

	public abstract boolean doEpisode();

	public abstract void reset();

	protected abstract boolean doEpisodeWithoutTraining();

}