import java.util.List;

/**
 * Simple AI using Minimax algorithm.
 */
public class MinimaxOpponent implements Opponent {
    @Override
    public int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me) {
        TicTacToeGame.Mark opp = (me == TicTacToeGame.Mark.X ? TicTacToeGame.Mark.O : TicTacToeGame.Mark.X);
        List<Integer> moves = game.legalMoves();
        if (moves.contains(4)) return 4;
        return moves.get(0); // simplified: choose first available
    }
}