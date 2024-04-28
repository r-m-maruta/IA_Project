package breakout;

import utils.Commons;
import utils.GameController;

public class BreakoutGamecontroler implements GameController {
    NeuralNetwork nn;

    public BreakoutGamecontroler(double[] Ffnn){
        this.nn = new NeuralNetwork(Commons.BREAKOUT_STATE_SIZE, Commons.HIDDEN_LAYERS, Commons.BREAKOUT_NUM_ACTIONS, Ffnn);
    }

    @Override
    public int nextMove(int[] currentState) {
        return nn.forward(currentState);
    }
}
