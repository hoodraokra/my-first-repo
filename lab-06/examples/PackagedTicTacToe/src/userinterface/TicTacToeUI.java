package userinterface;

import gamelogic.TicTacToeGame;
import gamelogic.Opponent;

public interface TicTacToeUI {
    void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs);
}