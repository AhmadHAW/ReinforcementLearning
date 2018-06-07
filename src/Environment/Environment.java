package Environment;

import java.util.Random;

import Fachwerte.Position;

public class Environment {
	// pos 0 = actionToGo, pos 1 = left, pos2 = right, pos 3 = oppositeDirectionToGo
	private final int[] determinismus = new int[4];
	// -1 = cliff, 0 = normalField, +1 = goal
	private final Integer[][] actualMap;
	private final int goalReward;
	private final int cliffReward;
	private final int stepReward;
	private final Position startPosition;
	private final Position goalPosition;
	private final Random rd = new Random();

	public Environment(Integer[][] actualMap, int goalReward, int cliffReward, int stepReward, Position startPosition,
			Position goalPosition) {
		super();
		this.actualMap = actualMap;
		this.goalReward = goalReward;
		this.cliffReward = cliffReward;
		this.stepReward = stepReward;
		this.startPosition = startPosition;
		this.goalPosition = goalPosition;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public Position getGoalPosition() {
		return goalPosition;
	}

	public int[] getDeterminismus() {
		return determinismus;
	}

	public Integer[][] getActualMap() {
		return actualMap;
	}

	public int getGoalReward() {
		return goalReward;
	}

	public int getCliffReward() {
		return cliffReward;
	}

	public int getStepReward() {
		return stepReward;
	}

	public int getReward(Position pos) {
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
		int returnValue = stepReward;
		if (actualMap.length <= newPosition.getX() || actualMap[newPosition.getX()].length <= newPosition.getY()) {
			newPosition = pos;
		} else if (actualMap[newPosition.getX()][newPosition.getY()] == -1) {
			newPosition = startPosition;
			returnValue = cliffReward;
		} else if (newPosition.equals(goalPosition)) {
			returnValue = goalReward;
		}
		return new ReturnValue(returnValue, newPosition);

	}
}
