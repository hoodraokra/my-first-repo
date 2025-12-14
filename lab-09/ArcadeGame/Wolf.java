import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Wolf {
    int x, y;
    int size = 32;
    boolean up, down, left, right;

    private Image sprite;
    private int frame = 0;

    public Wolf(int x, int y) {
        this.x = x;
        this.y = y;
        sprite = new ImageIcon("sprites/wolf.png").getImage();
    }

    public void update() {
        if (up) y -= 4;
        if (down) y += 4;
        if (left) x -= 4;
        if (right) x += 4;
        frame = (frame + 1) % 4;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite,
            x, y, x+size, y+size,
            frame*32, 0, frame*32+32, 32,
            null);
    }

    public boolean collides(Entity e) {
        return new Rectangle(x,y,size,size)
                .intersects(e.getBounds());
    }

    public void respawn() {
        x = 400; y = 300;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_DOWN -> down = true;
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_RIGHT -> right = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_DOWN -> down = false;
            case KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_RIGHT -> right = false;
        }
    }
}