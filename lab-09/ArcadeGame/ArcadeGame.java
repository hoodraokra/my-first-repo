import javax.swing.*;
import java.awt.*;

public class ArcadeGame extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Ecosystem ecosystem;

    public ArcadeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0,150,0));
        setFocusable(true);

        ecosystem = new Ecosystem();
        addKeyListener(new InputHandler(ecosystem.getWolf()));

        new Timer(30, e -> {
            ecosystem.update();
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ecosystem.draw(g);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ecosystem");
        frame.add(new ArcadeGame());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}