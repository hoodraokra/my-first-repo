/**
 * AI or human opponent interface.
 */
public interface Opponent {
    /** Return move 0..8 when called on opponent's turn. */
    int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me);
}