/*package breakout;

import utils.GameController;

public class BreakoutGame {
    private final GameController controller;
    private final BreakoutBoard board;
    private int score;

    // Constructor to initialize the game with or without GUI based on the need
    public BreakoutGame(GameController controller, int seed, boolean withGui) {
        this.controller = controller;
        this.board = new BreakoutBoard(controller, withGui, seed); // Configure GUI as needed
    }

    // Main game loop that runs until the game is over
    public void play() {
        boolean isRunning = true;
        while (isRunning) {
            int[] gameState = board.getState();
            int move = controller.nextMove(gameState);
            board.makeMove(move);
            board.updateGame();
            if (board.gameOver()) {
                isRunning = false;
                this.score = board.getScore();
            }
            // During training (no GUI), it might be beneficial to include a small delay
            // to avoid overloading the CPU unnecessarily (commented out if not needed).
            // try {
            //     Thread.sleep(10); // sleep for 10 milliseconds
            // } catch (InterruptedException e) {
            //     Thread.currentThread().interrupt();
            //     return;
            // }
        }
    }

    // Reset the game to its initial state for another run or replay
    public void resetGame() {
        board.reset();
        this.score = 0;
    }

    // Get the current game score
    public int getScore() {
        return this.score;
    }
}*/
