package Environment;

import java.util.Random;

import Fachwerte.Position;

public class Environment {
	// pos 0 = actionToGo, pos 1 = left, pos2 = right, pos 3 =
	// oppositeDirectionToGo
	private final int[] determinismus;
	// -1 = cliff, 0 = normalField, +1 = goal
	private final int[][] actualMap;
	private final double goalReward;
	private final double cliffReward;
	private final double stepReward;
	private final Position startPosition;
	private final Random rd = new Random();

	public Environment(int[][] actualMap, double goalReward, double cliffReward, double stepReward,
			Position startPosition, int[] determinismus) {
		super();
		this.actualMap = actualMap;
		this.goalReward = goalReward;
		this.cliffReward = cliffReward;
		this.stepReward = stepReward;
		this.startPosition = startPosition;
		this.determinismus = determinismus;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public int[] getDeterminismus() {
		return determinismus;
	}

	public int[][] getActualMap() {
		return actualMap;
	}

	public double getGoalReward() {
		return goalReward;
	}

	public double getCliffReward() {
		return cliffReward;
	}

	public double getStepReward() {
		return stepReward;
	}

	public Position getGoalPosition() {
		for (int x = 0; x < actualMap.length; ++x) {
			for (int y = 0; y < actualMap[x].length; ++y) {
				if (actualMap[x][y] == 1) {
					return Position.getField(x, y);
				}
			}
		}
		return null;
	}

	public double getReward(Position pos) {
		switch (actualMap[pos.getX()][pos.getY()]) {
		case (1):
			return goalReward;
		case (-1):
			return cliffReward;
		}
		return stepReward;
	}

	public ReturnValue doStep(Position pos, int direction) {
		int randomValue = rd.nextInt(100);
		if (randomValue > determinismus[0] + determinismus[1] + determinismus[2]) {
			direction = 3;
		} else if (randomValue > determinismus[0] + determinismus[1]) {
			direction = 2;
		} else if (randomValue > determinismus[0]) {
			direction = 1;
		}
		Position newPosition = null;
		switch (direction) {
		case (0):
			newPosition = Position.getField(pos.getX(), pos.getY() - 1);
			break;
		case (1):
			newPosition = Position.getField(pos.getX() + 1, pos.getY());
			break;
		case (2):
			newPosition = Position.getField(pos.getX(), pos.getY() + 1);
			break;
		case (3):
			newPosition = Position.getField(pos.getX() - 1, pos.getY());
			break;
		}
		double returnValue = 0;
		if (newPosition.getX() < 0 || newPosition.getY() < 0 || actualMap.length <= newPosition.getX()
				|| actualMap[newPosition.getX()].length <= newPosition.getY()) {
			newPosition = pos;
		} else if (actualMap[newPosition.getX()][newPosition.getY()] == -1) {
			newPosition = startPosition;
			returnValue = cliffReward;
		} else if (actualMap[newPosition.getX()][newPosition.getY()] == 1) {
			returnValue = goalReward;
		}
		return new ReturnValue(returnValue, newPosition);

	}
}
