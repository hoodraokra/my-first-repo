import java.awt.*;

public class Carrot extends Entity {
    public Carrot(int x, int y) {
        super(x,y,10);
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x,y,size,size);
    }
}