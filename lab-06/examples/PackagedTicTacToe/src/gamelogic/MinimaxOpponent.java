package gamelogic;

public class MinimaxOpponent implements Opponent {
    public int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me) {
        // Simple placeholder: choose first empty spot
        TicTacToeGame.Mark[] b = game.getBoard();
        for (int i = 0; i < b.length; i++)
            if (b[i] == TicTacToeGame.Mark.EMPTY) return i;
        return -1;
    }
}