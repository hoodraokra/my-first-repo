import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 * This class implements a Swing-based graphical user interface (GUI)
 * for the Tic Tac Toe game.
 * 
 * <p>It updates the board display, handles player input, and optionally
 * interacts with an AI opponent.</p>
 */
public class SwingUI implements TicTacToeUI {

    private TicTacToeGame game;
    private boolean vsAI;
    private Opponent ai;
    private TicTacToeGame.Mark aiAs;

    private final JButton[] cells = new JButton[9];
    private final JLabel status = new JLabel(" ");
    private final JFrame frame = new JFrame("Tic-Tac-Toe");

    /**
     * Starts the game GUI with the specified configuration.
     *
     * @param game the Tic Tac Toe game instance
     * @param vsAI true if playing against an AI
     * @param ai the AI opponent
     * @param aiPlaysAs the mark (X or O) the AI plays as
     */
    @Override
    public void start(TicTacToeGame game, boolean vsAI, Opponent ai, TicTacToeGame.Mark aiPlaysAs) {
        this.game = game;
        this.vsAI = vsAI;
        this.ai = ai;
        this.aiAs = aiPlaysAs;

        SwingUtilities.invokeLater(() -> {
            initUi();
            game.addListener(this);
            updateFromBoard();
            frame.setVisible(true);
            maybeAIMove(); // in case AI starts
        });
    }

    /**
     * Initializes the Swing GUI components including the game board,
     * status label, and reset button.
     */
    private void initUi() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(8,8));

        JPanel grid = new JPanel(new GridLayout(3,3,6,6));
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 42);

        for (int i = 0; i < 9; i++) {
            final int idx = i;
            JButton btn = new JButton(" ");
            btn.setFont(f);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> onCell(idx));
            cells[i] = btn;
            grid.add(btn);
        }

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        status.setFont(status.getFont().deriveFont(Font.BOLD));
        top.add(status);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton reset = new JButton(new AbstractAction("Reset") {
            @Override public void actionPerformed(ActionEvent e) { game.reset(); maybeAIMove(); }
        });
        bottom.add(reset);

        frame.add(top, BorderLayout.NORTH);
        frame.add(grid, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setSize(360, 420);
        frame.setLocationByPlatform(true);
    }

    /**
     * Handles a player clicking a cell on the board.
     *
     * @param idx the index of the clicked cell (0-8)
     */
    private void onCell(int idx) {
        if (game.isGameOver()) return;
        if (vsAI && game.getCurrent() == aiAs) return; // wait for AI
        if (game.play(idx)) { maybeAIMove(); }
    }

    /**
     * Executes the AI move if it is the AI's turn.
     */
    private void maybeAIMove() {
        if (!vsAI || game.isGameOver()) return;
        if (game.getCurrent() != aiAs) return;
        Timer t = new Timer(200, e -> {
            int move = ai.chooseMove(game, aiAs);
            game.play(move);
        });
        t.setRepeats(false);
        t.start();
    }

    /**
     * Updates the board display and status label based on the current game state.
     */
    private void updateFromBoard() {
        var b = game.getBoard();
        for (int i = 0; i < 9; i++) {
            String text = switch (b[i]) { case X -> "X"; case O -> "O"; default -> " "; };
            cells[i].setText(text);
            cells[i].setEnabled(!game.isGameOver() && b[i] == TicTacToeGame.Mark.EMPTY);
        }
        if (game.isGameOver()) {
            if (game.getWinner() == TicTacToeGame.Mark.EMPTY) status.setText("Draw. Click Reset.");
            else status.setText("Winner: " + game.getWinner() + ". Click Reset.");
        } else {
            status.setText("Turn: " + game.getCurrent() + (vsAI ? (", AI as " + aiAs) : ""));
        }
    }

    // GameListener methods

    /** Called when a move is made; updates the board display. */
    @Override public void onMove(int index, TicTacToeGame.Mark who) { updateFromBoard(); }

    /** Called when the game ends; updates the board display. */
    @Override public void onGameOver(TicTacToeGame.Mark winner) { updateFromBoard(); }

    /** Called when the game is reset; updates the board display. */
    @Override public void onReset(TicTacToeGame.Mark starting) { updateFromBoard(); }
}