import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Ecosystem {
    private Wolf wolf;
    private ArrayList<Rabbit> rabbits;
    private ArrayList<Carrot> carrots;
    private ArrayList<Fungus> fungi;

    public Ecosystem() {
        wolf = new Wolf(400, 300);

        rabbits = new ArrayList<>();
        carrots = new ArrayList<>();
        fungi = new ArrayList<>();

        Random r = new Random();

        for (int i = 0; i < 5; i++)
            rabbits.add(new Rabbit(r.nextInt(800), r.nextInt(600)));

        for (int i = 0; i < 5; i++)
            carrots.add(new Carrot(r.nextInt(800), r.nextInt(600)));

        for (int i = 0; i < 3; i++)
            fungi.add(new Fungus(r.nextInt(800), r.nextInt(600)));
    }

    public void update() {
        wolf.update();
        rabbits.forEach(Rabbit::update);
        fungi.forEach(Fungus::update);

        // Wolf eats rabbits
        rabbits.removeIf(r ->
            new Rectangle(wolf.x, wolf.y, wolf.size, wolf.size)
            .intersects(new Rectangle(r.x, r.y, r.size, r.size))
        );

        // Fungus eats carrots
        carrots.removeIf(c ->
            fungi.stream().anyMatch(f ->
                new Rectangle(f.x, f.y, f.size, f.size)
                .intersects(new Rectangle(c.x, c.y, c.size, c.size))
            )
        );

        // Fungus kills wolf
        for (Fungus f : fungi) {
            if (new Rectangle(f.x, f.y, f.size, f.size)
                .intersects(new Rectangle(wolf.x, wolf.y, wolf.size, wolf.size))) {
                wolf.respawn();
            }
        }
    }

    public void draw(Graphics g) {
        wolf.draw(g);
        rabbits.forEach(r -> r.draw(g));
        carrots.forEach(c -> c.draw(g));
        fungi.forEach(f -> f.draw(g));
    }

    public Wolf getWolf() {
        return wolf;
    }
}