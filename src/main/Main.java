package main;

import Environment.Environment;
import Fachwerte.Position;
import algorithms.QLearning;
import algorithms.ReinforcementLearningAlgorithm;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static final String[] HEADER = { "Algorithm", "Determinismus", "GoalReward", "CliffReward", "StepReward",
			"Gamma", "Alpha", "Average Episodes Until Trained", "Best Episodes Until Trained",
			"Average Steps Once Trained", "Best Steps Once Trained", "Best Path Once Trained" };
	private static final String CSV_FILENAME = "src/testResults/result-" + System.currentTimeMillis() + ".csv";
	private static final CellProcessor[] processors = new CellProcessor[] { new Optional(), // Algortihm
			new Optional(), // Determinismus
			new Optional(), // goalReward
			new Optional(), // CliffReward
			new Optional(), // StepReward
			new Optional(), // Gamma
			new Optional(), // Alpha
			new Optional(), // avg_Episodes_Until_Trained
			new Optional(), // Best Episodes Until Trained
			new Optional(), // Average Steps Once Trained
			new Optional(), // Best Steps Once Trained
			new Optional() // Best Path Once Trained
	};
	private static int tests = 0;

	public static void main(String[] args) {
		int[][] actualMap = { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, -1 }, { 0, 0, 0, 0, -1 }, { 0, 0, 0, 0, -1 },
				{ 0, 0, 0, 0, 1 } };

		int[][] determinismusList = { { 100, 0, 0, 0 }, { 97, 1, 1, 1 }, { 85, 5, 5, 5 }, { 70, 15, 15, 0 },
				{ 70, 10, 10, 10 }, { 25, 25, 25, 25 }, { 0, 15, 15, 70 } };
		double[] goalRewards = { 100, 1000, 0, -100, -10000000, 10000000 };
		double[] cliffRewards = { -100, -0.000001, -1, -10000000 };
		double[] stepRewards = { -100, -0.0000001, -100, -10000000 };
		double[] gammas = { 0.7d, 0.4d, 0.99d, 0.01d };
		double[] alphas = { 0.7d, 0.4d, 0.99d, 0.01d };
		tests = determinismusList.length * goalRewards.length * cliffRewards.length * stepRewards.length * gammas.length
				* alphas.length;
		doTestQLearning(actualMap, determinismusList, Position.getField(0, 4), goalRewards, cliffRewards, stepRewards,
				gammas, alphas, 500, 10);
	}

	private static void doTestQLearning(int[][] actualMap, int[][] determinismusList, Position startingPosition,
			double[] goalRewards, double[] cliffRewards, double[] stepRewards, double[] gammas, double[] alphas,
			int reapeatsUntilTrained, int repeatTesting) {
		int test = 0;
		ICsvListWriter listWriter = null;
		try {
            File outputFile = new File(CSV_FILENAME);
            outputFile.createNewFile();
		    listWriter = new CsvListWriter(new FileWriter(CSV_FILENAME, true),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            // write the header
            listWriter.writeHeader(HEADER);
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			closeWriter(listWriter);
		}
		for (int[] determinismus : determinismusList) {
			for (double goalReward : goalRewards) {
				for (double cliffReward : cliffRewards) {
					for (double stepReward : stepRewards) {
						for (double gamma : gammas) {
							for (double alpha : alphas) {
								// System.out.println("determinismus: " +
								// determinismus + " goalReward: " + goalReward
								// + " cliffReward: " + cliffReward + "
								// stepReward: " + stepReward + " gamma: "
								// + gamma + " alpha:" + alpha);
								System.out.flush();
								System.out.println(test + "/" + tests);

								Environment env = new Environment(actualMap, goalReward, cliffReward, stepReward,
										Position.getField(0, 4), determinismus);
								ReinforcementLearningAlgorithm alg = new QLearning(gamma, alpha, env.getGoalPosition(),
										reapeatsUntilTrained, env);
								int bestSteps = Integer.MAX_VALUE;
								int totalSteps = 0;
								String bestPath = "";
								for (int repeat = 0; repeat < repeatTesting; ++repeat) {
									alg.resetBoard();
									alg.train();
									alg.resetSteps();

									for (int i = 0; i < repeatTesting; ++i) {
										alg.doEpisodeWithoutTraining();
										if (bestSteps > alg.getBestSteps()) {
											bestSteps = alg.getBestSteps();
											bestPath = alg.getBestPath();
										}
										totalSteps += alg.getTotalSteps();
									}
								}
								// { "Algorithm", "Average Episodes Until
								// Trained", "Best Episodes Until Trained",
								// "Average Steps Once Trained" "Best Steps
								// Once
								// Trained", "Best Path Once Trained" };
								// System.out.println("bestEpiUntilTrained:
								// " +
								// alg.getBestEpisodesUntilTrained() + " "
								// + "averageEpiUntilTrained: "
								// + alg.getTotalEpisodesUntilTrained() /
								// (repeatTesting) + " " + "bestSteps: "
								// + bestSteps + " " + "averageSteps: "
								// + totalSteps / (repeatTesting *
								// repeatTesting) + " " + "bestPath: " +
								// bestPath);
								listWriter = null;
								try {
									listWriter = new CsvListWriter(new FileWriter(CSV_FILENAME, true),
											CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
									String det = "" + determinismus[0] + ", " + determinismus[1] + ", "
											+ determinismus[2] + ", " + determinismus[3];
									List<Object> row = Arrays.asList(new Object[] { "QLearning", det, goalReward,
											cliffReward, stepReward, gamma, alpha,
											alg.getTotalEpisodesUntilTrained() / (repeatTesting),
											alg.getBestEpisodesUntilTrained(),
											totalSteps / (repeatTesting * repeatTesting), bestSteps, bestPath });
									listWriter.write(row, processors);
								} catch (IOException e) {
									System.out.println(e);
									e.printStackTrace();
								} finally {
									closeWriter(listWriter);
								}
								++test;
							}
						}
					}
				}
			}
		}

	}

	private static void closeWriter(ICsvListWriter listWriter) {
		if (listWriter != null) {
			try {
				listWriter.close();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

}
