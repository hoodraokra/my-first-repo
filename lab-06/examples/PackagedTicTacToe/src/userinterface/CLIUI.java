package userinterface;

import gamelogic.TicTacToeGame;
import gamelogic.GameListener;
import gamelogic.Opponent;

import java.util.Scanner;

public class CLIUI implements TicTacToeUI, GameListener {
    private TicTacToeGame game;

    public void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs) {
        this.game = game;
        game.addListener(this);
        Scanner scanner = new Scanner(System.in);

        while (!game.isGameOver()) {
            System.out.println("Enter your move (0-8): ");
            int move = scanner.nextInt();
            if (!game.play(move)) System.out.println("Invalid move!");
        }
    }

    public void onMove(int index, TicTacToeGame.Mark who) { System.out.println(who + " moved to " + index); }
    public void onGameOver(TicTacToeGame.Mark winner) { System.out.println("Game Over! Winner: " + winner); }
    public void onReset(TicTacToeGame.Mark starting) { System.out.println("Game reset. " + starting + " starts."); }
}