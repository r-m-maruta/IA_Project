package breakout;

import utils.GameController;

import java.util.Random;

public class NeuralNetwork implements GameController {

    //atributos
    private int inputDim;
    private int hiddenDim;
    private int outputDim;

    private double[][] hiddenWeights;
    private double[] hiddenBiases;
    private double[][] outputWeights;
    private double[] outputBiases;


    public NeuralNetwork(int inputDim, int hiddenDim, int outputDim) {

        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;

        initializeParameters();// Chama o método para inicializar os pesos e biases
    }

    //Por exemplo, o vetor com os valores da rede neuronal da figura seria o seguinte:
    //[w1,1; w1,2; w2,1; w2,2; B1; B2; w1,o; w2,o; Bo]

    public NeuralNetwork(int inputDim, int hiddenDim, int outputDim, double[] values) {

        this.inputDim = inputDim;
        this.hiddenDim = hiddenDim;
        this.outputDim = outputDim;
        setNeuralNetwork(values);
        //setNetworkParameters(values); // Chama o método para definir os pesos e biases com valores específicos
        //initializeParameters();
    }
    private void setNeuralNetwork(double[] flatParameters) {
        this.hiddenWeights = new double[this.inputDim][this.hiddenDim];
        this.hiddenBiases = new double[this.hiddenDim];
        this.outputWeights = new double[this.hiddenDim][this.outputDim];
        this.outputBiases = new double[this.outputDim];

        int k = 0;

        // Reconstruct hiddenWeights
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = flatParameters[k++];
            }
        }

        // Reconstruct hiddenBiases
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = flatParameters[k++];
        }

        // Reconstruct outputWeights
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = flatParameters[k++];
            }
        }

        // Reconstruct outputBiases
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = flatParameters[k++];
        }
    }


    // Define os pesos e biases com valores específicos fornecidos
    private void setNetworkParameters(double[] values) {
        int k = 0;
        hiddenWeights = new double[inputDim][hiddenDim];
        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = values[k++];
            }
        }

        hiddenBiases = new double[hiddenDim];
        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = values[k++];
        }

        outputWeights = new double[hiddenDim][outputDim];
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = values[k++];
            }
        }

        outputBiases = new double[outputDim];
        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = values[k++];
        }
    }
    // Inicializa todos os pesos e biases com valores aleatórios entre -1 e 1
    private void initializeParameters(){

        Random random = new Random();

        hiddenWeights = new double[inputDim][hiddenDim];
        hiddenBiases = new double[hiddenDim];
        outputWeights = new double[hiddenDim][outputDim];
        outputBiases = new double[outputDim];

        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                hiddenWeights[i][j] = (random.nextDouble() * 2) - 1;
            }
        }

        for (int i = 0; i < hiddenDim; i++) {
            hiddenBiases[i] = (random.nextDouble() * 2) - 1;
        }

        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                outputWeights[i][j] = (random.nextDouble() * 2) - 1;
            }
        }

        for (int i = 0; i < outputDim; i++) {
            outputBiases[i] = (random.nextDouble() * 2) - 1;
        }
    }


    //função sigmoid
    public int forward(int[] inputValues) {
        double[] hiddenLayerOutputs = new double[hiddenDim];
        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < inputDim; j++) {
                hiddenLayerOutputs[i] += inputValues[j] * hiddenWeights[j][i];
            }
            hiddenLayerOutputs[i] += hiddenBiases[i];
            hiddenLayerOutputs[i] = sigmoid(hiddenLayerOutputs[i]);
        }

        double[] outputLayerOutputs = new double[outputDim];
        for (int i = 0; i < outputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                outputLayerOutputs[i] += hiddenLayerOutputs[j] * outputWeights[j][i];
            }
            outputLayerOutputs[i] += outputBiases[i];
            outputLayerOutputs[i] = sigmoid(outputLayerOutputs[i]);
        }

        if(outputLayerOutputs[0] > outputLayerOutputs[1]){
            return BreakoutBoard.RIGHT;
        }else{
            return BreakoutBoard.LEFT;
        }
    }
    // Função de ativação sigmoid
    private double sigmoid(double d) {
        return  (1 / (1 + Math.exp(-d)));
    }

    // Retorna um vetor que representa o conjunto de pesos e biases da rede
    public double[] getNeuralNetwork() {
        double[] networkParameters = new double[inputDim * hiddenDim + hiddenDim + hiddenDim * outputDim + outputDim];
        int k = 0;

        for (int i = 0; i < inputDim; i++) {
            for (int j = 0; j < hiddenDim; j++) {
                networkParameters[k++] = hiddenWeights[i][j];
            }
        }

        for (int i = 0; i < hiddenDim; i++) {
            networkParameters[k++] = hiddenBiases[i];
        }

        for (int i = 0; i < hiddenDim; i++) {
            for (int j = 0; j < outputDim; j++) {
                networkParameters[k++] = outputWeights[i][j];
            }
        }

        for (int i = 0; i < outputDim; i++) {
            networkParameters[k++] = outputBiases[i];
        }

        return networkParameters;
    }

    //Para efeitos de teste use o seguinte toString():
    @Override
    public String toString() {
        String result = "Neural Network: \nNumber of inputs: " + inputDim + "\n" + "Weights between input and hidden layer with " + hiddenDim + " neurons: \n";
        String hidden = "";
        for (int input = 0; input < inputDim; input++) {
            for (int i = 0; i < hiddenDim; i++) {
                hidden+= " w"+(input+1) + (i+1) +": "
                        + hiddenWeights[input][i] + "\n";
            }
        }
        result += hidden;
        String biasHidden = "Hidden biases: \n";
        for (int i = 0; i < hiddenDim; i++) {
            biasHidden += " b "+(i+1)+": " + hiddenBiases[i] +"\n";
        }
        result+= biasHidden;
        String output = "Weights between hidden and output layer with "
                + outputDim +" neurons: \n";
        for (int hiddenw = 0; hiddenw < hiddenDim; hiddenw++) {
            for (int i = 0; i < outputDim; i++) {
                output+= " w"+(hiddenw+1) +"o"+(i+1)+": "
                        + outputWeights[hiddenw][i] + "\n";
            }
        }
        result += output;
        String biasOutput = "Ouput biases: \n";
        for (int i = 0; i < outputDim; i++) {
            biasOutput += " bo"+(i+1)+": " + outputBiases[i] + "\n";
        }
        result+= biasOutput;
        return result;
    }



    @Override
    public int nextMove(int[] currentState) {
        return forward(currentState);
    }


}
