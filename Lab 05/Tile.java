import java.util.ArrayList;
import java.util.List;

public class Tile {
    private int water;
    private int temperature;
    private int nutrients;
    private final List<Creature> creatures = new ArrayList<>();

    public Tile(int water, int temperature, int nutrients) {
        this.water = water;
        this.temperature = temperature;
        this.nutrients = nutrients;
    }

    public void addCreature(Creature c) {
        creatures.add(c);
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void takeTurn() {
        System.out.println("Tile turn: water=" + water + ", temp=" + temperature + ", nutrients=" + nutrients);
        for (Creature c : new ArrayList<>(creatures)) {
            c.takeTurn();
        }
    }

    public int getWater() { return water; }
    public int getTemperature() { return temperature; }
    public int getNutrients() { return nutrients; }
}