/**
 * Starts Tic Tac Toe game.
 */
public class Main {
    public static void main(String[] args) {
        boolean useCLI = true;
        boolean vsAI = true;
        boolean aiFirst = false;

        TicTacToeGame game = new TicTacToeGame();
        Opponent ai = new MinimaxOpponent();
        TicTacToeGame.Mark aiAs = aiFirst ? TicTacToeGame.Mark.X : TicTacToeGame.Mark.O;

        TicTacToeUI ui = new CLIUI();
        ui.start(game, vsAI, ai, aiAs);
    }
}