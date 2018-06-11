package Environment;

import java.util.Random;

import Fachwerte.Position;

public class Environment {
	// pos0 = actionToGo, pos1 = left, pos2 = right, pos3 =
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

	// pos0 = actionToGo, pos1 = left, pos2 = right, pos3 =
	// oppositeDirectionToGo
	// -1 = cliff, 0 = normalField, +1 = goal
	public ReturnValue doStep(Position pos, int direction) {
		int randomValue = rd.nextInt(100);
		if (randomValue < determinismus[0]) {

		} else if (randomValue < determinismus[1]) {
			direction = (direction + 3) % 3;
		} else if (randomValue < determinismus[2]) {
			direction = (direction + 1) % 3;
		} else {
			direction = (direction + 2) % 3;
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
		double returnValue = stepReward;
		int x = newPosition.getX();
		int y = newPosition.getY();
		// wall
		if (x < 0 || y < 0 || x >= actualMap.length || y >= actualMap[x].length) {
			newPosition = pos;
			// cliff
		} else if (actualMap[x][y] == -1) {
			newPosition = startPosition;
			returnValue = cliffReward;
			// goal
		} else if (actualMap[x][y] == 1) {
			returnValue = goalReward;
		}
		return new ReturnValue(returnValue, newPosition, direction);

	}
}
