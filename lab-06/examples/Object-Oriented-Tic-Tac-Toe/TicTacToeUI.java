/**
 * UI interface for Tic Tac Toe.
 */
public interface TicTacToeUI extends GameListener {
    void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs);
}