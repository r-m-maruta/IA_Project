package breakout;

import javax.swing.JFrame;

import utils.Commons;
import utils.GameController;
import java.io.*;

//import static breakout.Training.loadBestNetwork;
import static utils.Commons.*;

public class Breakout extends JFrame {


	public Breakout(GameController network, int i) {
		add(new BreakoutBoard(network, true, i));
		setTitle("Breakout");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		double[] bestNetworkWeights = loadBestNetwork();
		if (bestNetworkWeights != null) {
			NeuralNetwork bestNetwork = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, bestNetworkWeights);
			new Breakout(bestNetwork, 5); //(int) System.currentTimeMillis()
		} else {
			System.out.println("Failed to load the best network. Check if the training has been completed successfully and the network was saved.");
		}
	}
	/*
	public static void main(String[] args) {
		// Create an instance of Training
		Training training = new Training(); // You can also pass maxGenerations here
		training.train(); // This starts the training process

		// Retrieve the best network directly from the Training instance
		double[] bestNetworkWeights = training.getBestOverallNetwork();

		// Assuming there is a getter method in Training to retrieve the best network
		if (bestNetworkWeights != null) {
			NeuralNetwork bestNetwork = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, bestNetworkWeights);
			new Breakout(bestNetwork, 5); // Run the best neural network
		} else {
			System.out.println("No best network found. Check if the training has been completed successfully.");
		}
	}

	 */


	private static double[] loadBestNetwork() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("bestNetwork.txt"))) {
			return (double[]) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null; // Handle this case appropriately in real applications
		}
	}





}
