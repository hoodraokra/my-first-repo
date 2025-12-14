import java.awt.*;

public class Fungus extends Entity {
    public Fungus(int x, int y) {
        super(x,y,20);
    }

    public void update() {
        // slow spread
        x += Math.random()*2 - 1;
        y += Math.random()*2 - 1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillOval(x,y,size,size);
    }
}