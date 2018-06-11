package Fachwerte;

import java.util.HashMap;
import java.util.Map;

public class Position {

	// map die xCord auf yCord auf das Feld [xCord, yCord] mappt.
	private static Map<Integer, Map<Integer, Position>> positions = new HashMap<>();
	private final int x;
	private final int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * singleton Pattern auf die Klasse positions. Bestehende Felder werden
	 * gehalten. Eine Position x y existiert nur einmal
	 * 
	 * @param x
	 *            - x-koordinate
	 * @param y
	 *            - y-koordinate
	 * @return eine Referenz auf ein Feld mit x-und y-koordinate
	 */
	public static Position getField(int x, int y) {
		// wenn x schon gemappt wurde
		if (positions.containsKey(x)) {
			// dazu ein y existiert
			if (positions.get(x).containsKey(y)) {
				// gebe das bereits existierende Feld aus
				return positions.get(x).get(y);
			}
			// wenn x bereits gemappt wurde, aber nicht y
			else {
				// erstelle das Feld, lege es in der map ab und gebe es raus
				Position field = new Position(x, y);
				positions.get(x).put(y, field);
				return field;
			}
		}
		// wenn x und y noch nicht gemappt wurden
		else {
			// erstelle das Feld, lege das Mapping an und gebe es aus.
			Map<Integer, Position> pos = new HashMap<>();
			Position field = new Position(x, y);
			pos.put(y, field);
			positions.put(x, pos);
			return field;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "P[" + x + ", " + y + "]";
	}

}
