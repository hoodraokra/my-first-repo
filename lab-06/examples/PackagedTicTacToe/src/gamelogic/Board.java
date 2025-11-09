import java.util.Arrays;

/**
 * Represents the Tic Tac Toe board.
 * Handles the board state and basic moves.
 */
public class Board {

    /** The board positions: 0..8. EMPTY means unoccupied. */
    private Mark[] board;

    /** The current player's mark (X or O). */
    private Mark current;

    /** Possible marks. */
    public enum Mark { X, O, EMPTY }

    /** Constructor: creates a new empty board and sets starting player to X. */
    public Board() {
        board = new Mark[9];
        Arrays.fill(board, Mark.EMPTY);
        current = Mark.X;
    }

    /** Resets the board. */
    public void reset() {
        Arrays.fill(board, Mark.EMPTY);
        current = Mark.X;
    }

    /** Defensive copy of the board. */
    public Mark[] getBoard() {
        return board.clone();
    }

    /** Returns current player. */
    public Mark getCurrent() {
        return current;
    }

    /** Attempts to play at given index. */
    public boolean play(int index) {
        if (index < 0 || index > 8 || board[index] != Mark.EMPTY) return false;
        board[index] = current;
        current = (current == Mark.X ? Mark.O : Mark.X);
        return true;
    }

    /** Checks if board is full. */
    public boolean isFull() {
        for (Mark m : board) if (m == Mark.EMPTY) return false;
        return true;
    }
}