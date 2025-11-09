import java.util.ArrayList;
import java.util.List;

/**
 * Tic Tac Toe game logic.
 */
public class TicTacToeGame {
    public enum Mark { X, O, EMPTY }

    private final Mark[] board = new Mark[9];
    private Mark current = Mark.X;
    private boolean gameOver = false;
    private Mark winner = Mark.EMPTY;
    private final List<GameListener> listeners = new ArrayList<>();

    public TicTacToeGame() { reset(); }

    public void reset() {
        for (int i=0;i<9;i++) board[i] = Mark.EMPTY;
        current = Mark.X;
        gameOver = false;
        winner = Mark.EMPTY;
        fireReset(current);
    }

    public Mark[] getBoard() { return board.clone(); }
    public Mark getCurrent() { return current; }
    public boolean isGameOver() { return gameOver; }
    public Mark getWinner() { return winner; }

    public boolean play(int index) {
        if (gameOver || index<0 || index>8 || board[index]!=Mark.EMPTY) return false;
        board[index] = current;
        fireMove(index,current);
        checkWinner();
        if (!gameOver) current = (current==Mark.X?Mark.O:Mark.X);
        return true;
    }

    public List<Integer> legalMoves() {
        List<Integer> moves = new ArrayList<>();
        if (gameOver) return moves;
        for (int i=0;i<9;i++) if (board[i]==Mark.EMPTY) moves.add(i);
        return moves;
    }

    public void addListener(GameListener l) { listeners.add(l); }

    private void fireMove(int idx, Mark who) { for (var l:listeners) l.onMove(idx,who); }
    private void fireGameOver(Mark winner) { for (var l:listeners) l.onGameOver(winner); }
    private void fireReset(Mark starting) { for (var l:listeners) l.onReset(starting); }

    private void checkWinner() {
        int[][] lines = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (var line:lines) {
            if (board[line[0]]!=Mark.EMPTY && board[line[0]]==board[line[1]] && board[line[1]]==board[line[2]]) {
                winner = board[line[0]];
                gameOver = true;
                fireGameOver(winner);
                return;
            }
        }
        boolean full = true;
        for (Mark m:board) if (m==Mark.EMPTY) full=false;
        if (full) { winner=Mark.EMPTY; gameOver=true; fireGameOver(winner);}
    }
}