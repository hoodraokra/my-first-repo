package gamelogic;

public interface GameListener {
    void onMove(int index, TicTacToeGame.Mark who);
    void onGameOver(TicTacToeGame.Mark winner);
    void onReset(TicTacToeGame.Mark starting);
}