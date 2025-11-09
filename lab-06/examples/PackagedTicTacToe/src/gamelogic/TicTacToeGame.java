package gamelogic;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame {
    public enum Mark { X, O, EMPTY }

    private Mark[] board;
    private Mark current;
    private Mark winner;
    private boolean gameOver;
    private List<GameListener> listeners;

    public TicTacToeGame() {
        listeners = new ArrayList<>();
        reset();
    }

    public void reset() {
        board = new Mark[9];
        for (int i = 0; i < 9; i++) board[i] = Mark.EMPTY;
        current = Mark.X;
        winner = null;
        gameOver = false;
    }

    public boolean play(int index) {
        if (board[index] != Mark.EMPTY || gameOver) return false;
        board[index] = current;
        for (GameListener l : listeners) l.onMove(index, current);

        checkWinner();
        if (!gameOver) current = (current == Mark.X) ? Mark.O : Mark.X;
        return true;
    }

    private void checkWinner() {
        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] w : wins) {
            if (board[w[0]] != Mark.EMPTY &&
                board[w[0]] == board[w[1]] &&
                board[w[1]] == board[w[2]]) {
                winner = board[w[0]];
                gameOver = true;
                for (GameListener l : listeners) l.onGameOver(winner);
                return;
            }
        }
        boolean full = true;
        for (Mark m : board) if (m == Mark.EMPTY) full = false;
        if (full) {
            gameOver = true;
            for (GameListener l : listeners) l.onGameOver(null);
        }
    }

    public void addListener(GameListener l) { listeners.add(l); }
    public Mark[] getBoard() { return board.clone(); }
    public Mark getCurrent() { return current; }
    public Mark getWinner() { return winner; }
    public boolean isGameOver() { return gameOver; }
}