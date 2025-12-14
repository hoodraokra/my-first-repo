import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    int x, y, size;

    public Entity(int x, int y, int size) {
        this.x = x; this.y = y; this.size = size;
    }

    public Rectangle getBounds() {
        return new Rectangle(x,y,size,size);
    }

    public boolean collides(Entity e) {
        return getBounds().intersects(e.getBounds());
    }

    public abstract void draw(Graphics g);
}