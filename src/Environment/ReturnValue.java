package Environment;

import Fachwerte.Position;

public class ReturnValue {

	private final double returnValue;
	private final Position newPosition;
	private final int direction;

	public ReturnValue(double returnValue, Position newPosition, int dir) {
		super();
		this.returnValue = returnValue;
		this.newPosition = newPosition;
		this.direction = dir;
	}

	public double getReturnValue() {
		return returnValue;
	}

	public Position getNewPosition() {
		return newPosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newPosition == null) ? 0 : newPosition.hashCode());
		long temp;
		temp = Double.doubleToLongBits(returnValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReturnValue other = (ReturnValue) obj;
		if (newPosition == null) {
			if (other.newPosition != null)
				return false;
		} else if (!newPosition.equals(other.newPosition))
			return false;
		if (Double.doubleToLongBits(returnValue) != Double.doubleToLongBits(other.returnValue))
			return false;
		return true;
	}

	public int getDirection() {
		return direction;
	}

}
