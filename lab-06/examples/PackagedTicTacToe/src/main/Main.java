package main;

import gamelogic.TicTacToeGame;
import gamelogic.MinimaxOpponent;
import userinterface.CLIUI;

public class Main {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        CLIUI ui = new CLIUI();
        ui.start(game, false, new MinimaxOpponent(), TicTacToeGame.Mark.O);
    }
}