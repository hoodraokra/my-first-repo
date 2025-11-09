package gamelogic;

public interface Opponent {
    int chooseMove(TicTacToeGame game, TicTacToeGame.Mark me);
}