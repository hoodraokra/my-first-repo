import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Rabbit extends Entity {
    private Image sprite;
    private int frame = 0;
    private int dx, dy;

    public Rabbit(int x, int y) {
        super(x,y,24);
        sprite = new ImageIcon("sprites/rabbit.png").getImage();
        Random r = new Random();
        dx = r.nextInt(5)-2;
        dy = r.nextInt(5)-2;
    }

    public void update() {
        x += dx; y += dy;
        frame = (frame + 1) % 4;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite,
            x, y, x+size, y+size,
            frame*24, 0, frame*24+24, 24,
            null);
    }
}