package breakout;

import utils.Commons;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.io.*;
import java.util.stream.IntStream;

public class Training {

    private final int popSize = 2000;
    public double[][] popNN;
    private static double[] fitnessScores;
    private int maxGenerations = 200;
    private double[] bestOverallNetwork;
    private double bestOverallScore = Double.MIN_VALUE;
    private int totalSizeGenome = 62;

    public Training() {
        initializePop();
    }

    public Training(int maxGenerations) {
        this.maxGenerations = maxGenerations;
        initializePop();
    }

    /*public void train() {
        for (int generation = 0; generation < maxGenerations && !shouldStop(); generation++) {
            double[][] newPop = new double[popSize][];

            for (int i = 0; i < popSize; i++) {
                double[] parent1 = tournamentSelection();
                double[] parent2 = tournamentSelection();
                double[] child = crossoverUniform(parent1, parent2);
                child = bitFlipMutation(child, 0.01); // 1% mutation rate
                newPop[i] = child;
            }

            popNN = newPop; // Replace old population with new one
            // Recalculate fitness scores here
        }
    }*/


    private void initializePop() {
        this.popNN = new double[popSize][];
        for (int i = 0; i < popSize; i++) {
            NeuralNetwork nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS);
            this.popNN[i] = nn.getNeuralNetwork();
        }
    }


    // Tournament Selection
    /*private double[] tournamentSelection() {
        Random rand = new Random();2
        int tournamentSize = 5;
        double highScoreThreshold = 800000; // Adjust this threshold based on observed scores
        double averageScore = Arrays.stream(fitnessScores).average().orElse(0);

        if (averageScore > highScoreThreshold) {
            tournamentSize = 2; // More selective: chooses among the best performers
        }

        int bestIndex = -1;
        double bestFitness = Double.MIN_VALUE;

        for (int i = 0; i < tournamentSize; i++) {
            int currentIndex = rand.nextInt(popSize);
            double currentFitness = fitnessScores[currentIndex];
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                bestIndex = currentIndex;
            }
        }

        return Arrays.copyOf(popNN[bestIndex], popNN[bestIndex].length);
    }*/

    private double[][] selectTwo() {
        Random rand = new Random();
        double highScoreThreshold = 800000; // Score threshold
        //double bestScore = Arrays.stream(fitnessScores).max().orElse(Double.NEGATIVE_INFINITY);
        double bestScore = Arrays.stream(fitnessScores).max().getAsDouble();
        //System.out.println(Arrays.toString(fitnessScores));
        //todo sasha
        double[][] selectedIndividuals = new double[2][];

        if (bestScore > highScoreThreshold) {
            // Select the two best unique individuals
            int bestIndex = -1;
            double bestFitness = Double.NEGATIVE_INFINITY; // Changed to NEGATIVE_INFINITY for max score comparison
            int secondBestIndex = -1;
            double secondBestFitness = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < popSize; i++) {
                double currentFitness = fitnessScores[i];
                if (currentFitness > bestFitness) {
                    // Update second best
                    secondBestIndex = bestIndex;
                    secondBestFitness = bestFitness;
                    // Update best
                    bestIndex = i;
                    bestFitness = currentFitness;
                } else if (currentFitness > secondBestFitness && i != bestIndex) {
                    secondBestIndex = i;
                    secondBestFitness = currentFitness;
                }
            }

            selectedIndividuals[0] = Arrays.copyOf(popNN[bestIndex], popNN[bestIndex].length);
            selectedIndividuals[1] = Arrays.copyOf(popNN[secondBestIndex], popNN[secondBestIndex].length);
        } else {
            // Select two random and unique individuals
            int index1 = rand.nextInt(popSize);
            int index2 = index1;
            while (index2 == index1) {
                index2 = rand.nextInt(popSize);
            }

            selectedIndividuals[0] = Arrays.copyOf(popNN[index1], popNN[index1].length);
            selectedIndividuals[1] = Arrays.copyOf(popNN[index2], popNN[index2].length);
        }

        return selectedIndividuals;
    }


    // Uniform Crossover
    private double[] crossoverUniform(double[] parent1, double[] parent2) {
        Random rand = new Random();
        double[] child = new double[parent1.length];

        for (int i = 0; i < parent1.length; i++) {
            child[i] = rand.nextBoolean() ? parent1[i] : parent2[i];
        }
        return child;
    }

    // Bit Flip Mutation
    private double[] bitFlipMutation(double[] genes) {
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            if (rand.nextDouble() < 0.2) {
                genes[i] += rand.nextGaussian() * 0.2; // Small Gaussian noise
            }
        }
        return genes;
    }


    private boolean shouldStop() {
        return false;
    }


    private void displayBestNetwork(int bestIndex) {
        NeuralNetwork bestNN = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, popNN[bestIndex]);
        new Breakout(bestNN, 5); //(int) System.currentTimeMillis()
    }

    private double runSimulation(double[] neuralNetworkWeights) {
        NeuralNetwork nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, neuralNetworkWeights);
        BreakoutBoard gameBoard = new BreakoutBoard(nn, false, 5); //(int) System.currentTimeMillis()
        gameBoard.runSimulation();
        return gameBoard.getFitness();
    }

    private void saveBestNetwork(double[] bestNetwork) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bestNetwork.txt"))) {
            out.writeObject(bestNetwork);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    public void train() {
        for (int generation = 0; generation < maxGenerations; generation++) {
            fitnessScores = new double[popSize]; // Recalculate fitness scores for the new generation

            for (int i = 0; i < popSize; i++) {
                fitnessScores[i] = runSimulation(popNN[i]);
            }

            double[][] newPop = new double[popSize][];
            double maxScoreThisGen = 0.0;
            int bestIndexThisGen = 0;

            double[][] parents = selectTwo();
            // Using selectTwo to fetch two parents at a time
            for (int i = 0; i < popSize; i += 2) {
                double[] child1 = crossoverUniform(parents[0], parents[1]);
                double[] child2 = crossoverUniform(parents[0], parents[1]);

                // Mutate children
                bitFlipMutation(child1);
                bitFlipMutation(child2);

                // Add children to new population
                newPop[i] = child1;
                if (i + 1 < popSize) {
                    newPop[i + 1] = child2;
                }

                // Track the best score in this generation
                fitnessScores[i] = runSimulation(child1);
                if (i + 1 < popSize) {
                    fitnessScores[i + 1] = runSimulation(child2);
                }

                if (fitnessScores[i] > maxScoreThisGen) {
                    maxScoreThisGen = fitnessScores[i];
                    bestIndexThisGen = i;
                }
                if (i + 1 < popSize && fitnessScores[i + 1] > maxScoreThisGen) {
                    maxScoreThisGen = fitnessScores[i + 1];
                    bestIndexThisGen = i + 1;
                }
                System.out.println(Arrays.toString(fitnessScores));
            }

            // Replace the old population with the new one
            popNN = newPop;

            System.out.println("Generation " + generation + " Best Score: " + maxScoreThisGen);
            if (maxScoreThisGen > bestOverallScore) {
                bestOverallScore = maxScoreThisGen;
                bestOverallNetwork = Arrays.copyOf(popNN[bestIndexThisGen], popNN[bestIndexThisGen].length);
            }
        }

        saveBestNetwork(bestOverallNetwork);
    }

     */


    public double[] getBestOverallNetwork() {
        return bestOverallNetwork;
    }

    private void train(){
        double[] fitnesPop = new double[popSize];
        int positionBest = 0;
        int positionBest1 = 0;

        for(int i = 0; i < popSize; i++) {
            NeuralNetwork nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, this.popNN[i]);
            BreakoutBoard gameBoard = new BreakoutBoard(nn, false, 5);
            gameBoard.runSimulation();
            double fitness = gameBoard.getFitness();
            fitnesPop[i] = fitness;
            if (fitness > fitnesPop[positionBest1]) {
                if (fitness > fitnesPop[positionBest]) {
                    positionBest = i;
                } else {
                    positionBest1 = i;
                }
            }
        }
        double[] best = Arrays.copyOf(popNN[positionBest], popNN[positionBest].length);
        double[] best1 = Arrays.copyOf(popNN[positionBest1], popNN[positionBest1].length);
        double[][] nextPop = new double[popSize][];
        System.out.println(fitnesPop[positionBest]);
        System.out.println(fitnesPop[positionBest1]);
        double sum = 0;
        for(int i = 0; i< popSize; i++){
            sum += fitnesPop[i];
        }
        System.out.println(sum/popSize);
        System.out.println("--------");

        for(int j = 0; j < popSize; j++){
            double[] child;
            if(j < 0.4 * popSize) {
                child = crossoverUniform(best, best1);
            }else{
                child = crossoverUniform(popNN[randomMax(popSize)],popNN[randomMax(popSize)]);
            }
            nextPop[j] = bitFlipMutation(child);
        }
        this.popNN = nextPop;



    }
    private int randomMax(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }

    public static void main(String[] args) {
        Training training = new Training();
        for(int i = 0; i <1000; i++){
            training.train();
        }
        /*
        NeuralNetwork nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, training.popNN[0]);
        System.out.println(Arrays.toString(nn.hiddenBiases));
        System.out.println(Arrays.toString(nn.outputBiases));
        System.out.println(Arrays.deepToString(nn.hiddenWeights));
        System.out.println(Arrays.deepToString(nn.outputWeights));

        NeuralNetwork nnl = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, training.popNN[2]);
        Breakout bord = new Breakout(nnl,5);

         */
        //bord.runSimulation();
        //System.out.println(bord.getFitness());
        /*NeuralNetwork nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, training.popNN[2]);
        BreakoutBoard gameBoard = new BreakoutBoard(nn, false, 5); //(int) System.currentTimeMillis()
        gameBoard.runSimulation();
        System.out.println(gameBoard.getFitness());
        /*
        Training training = new Training(); // You can also pass maxGenerations here
        training.train(); // This starts the training process and eventually saves the best network
        System.out.println("Training completed. Best network saved.");
        */

    }
}