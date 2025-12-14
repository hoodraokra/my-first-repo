import java.awt.event.*;

public class InputHandler implements KeyListener {
    private Wolf wolf;

    public InputHandler(Wolf wolf) {
        this.wolf = wolf;
    }

    public void keyPressed(KeyEvent e) {
        wolf.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        wolf.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {}
}