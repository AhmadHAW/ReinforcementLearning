package test.Environment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Environment.Environment;
import Environment.ReturnValue;
import Fachwerte.Position;
import javafx.geometry.Pos;

public class EnvironmentTest {

	Environment envDet;
	Environment envNonDet;
	int[][] actualMap = { { 0, 0, 0, 0 }, { 0, 0, 0, -1 }, { 0, 0, 0, -1 }, { 0, 0, 0, 1 } };
	int[] determinismus = { 70, 10, 10, 10 };
	int[] nonDeterminismus = { 100, 0, 0, 0 };

	@Before
	public void setUp() throws Exception {
		envDet = new Environment(actualMap, 1, -1, -0.5, Position.getField(0, 3), determinismus);
		envNonDet = new Environment(actualMap, 1, -1, -0.5, Position.getField(0, 3), nonDeterminismus);
	}

	// nonDetEnv move on normal Field.
	@Test
	public void testMoveOnNormalField() {
		ReturnValue result = envNonDet.doStep(Position.getField(0, 0), 1);
		assertEquals(new ReturnValue(0, Position.getField(1, 0)), result);
	}

	// nonDetEnv move on Wall West.
	@Test
	public void testMoveOnWallWest() {
		ReturnValue result = envNonDet.doStep(Position.getField(0, 0), 3);
		assertEquals(new ReturnValue(0, Position.getField(0, 0)), result);
	}

	// nonDetEnv move on Wall East.
	@Test
	public void testMoveOnWallEast() {
		ReturnValue result = envNonDet.doStep(Position.getField(3, 0), 1);
		assertEquals(new ReturnValue(0, Position.getField(3, 0)), result);
	}

	// nonDetEnv move on Wall North.
	@Test
	public void testMoveOnWallNorth() {
		ReturnValue result = envNonDet.doStep(Position.getField(0, 0), 0);
		assertEquals(new ReturnValue(0, Position.getField(0, 0)), result);
	}

	// nonDetEnv move on Wall North.
	@Test
	public void testMoveOnWallSouth() {
		ReturnValue result = envNonDet.doStep(Position.getField(0, 3), 2);
		assertEquals(new ReturnValue(0, Position.getField(0, 3)), result);
	}

	// nonDetEnv move on cliff.
	@Test
	public void testMoveOnCliff() {
		ReturnValue result = envNonDet.doStep(Position.getField(1, 2), 2);
		assertEquals(new ReturnValue(-1.0, Position.getField(0, 3)), result);
	}

	// nonDetEnv move on cliff.
	@Test
	public void testMoveOnGoal() {
		ReturnValue result = envNonDet.doStep(Position.getField(3, 2), 2);
		assertEquals(new ReturnValue(1, Position.getField(3, 3)), result);
	}

	// play with Det
	@Test
	public void testMoveBoard() {
		for (int x = 0; x < actualMap.length; ++x) {
			for (int y = 0; y < actualMap[x].length; ++y) {
				for (int i = 0; i < 100; ++i) {
					for (int dir = 0; dir < 4; ++dir) {
						Position pos = envDet.doStep(Position.getField(x, y), dir).getNewPosition();
						assertTrue(pos.getX() >= 0 && pos.getX() < actualMap.length && pos.getY() >= 0
								&& pos.getY() < actualMap.length);
					}
				}
			}
		}
	}
}
