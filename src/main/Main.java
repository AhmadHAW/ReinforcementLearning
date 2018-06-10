package main;

import Environment.Environment;
import Fachwerte.Position;
import algorithms.QLearning;
import algorithms.ReinforcementLearningAlgorithm;

public class Main {

	public static void main(String[] args) {
		int[][] actualMap = { { 0, 0, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };
		int[] determinismus = { 70, 10, 10, 10 };
		Environment env = new Environment(actualMap, 1, -1, -0.1, Position.getField(0, 3), determinismus);
		ReinforcementLearningAlgorithm alg = new QLearning(0.7, 0.9, env.getGoalPosition(), 10, env);
		alg.train();
		alg.printMapOfAgent();
	}

}
