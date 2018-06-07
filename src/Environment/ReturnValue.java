package Environment;

import Fachwerte.Position;

public class ReturnValue {

	private final int returnValue;
	private final Position newPosition;
	public ReturnValue(int returnValue, Position newPosition) {
		super();
		this.returnValue = returnValue;
		this.newPosition = newPosition;
	}
	public int getReturnValue() {
		return returnValue;
	}
	public Position getNewPosition() {
		return newPosition;
	}

}
