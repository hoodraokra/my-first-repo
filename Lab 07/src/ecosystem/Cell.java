package ecosystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x, y;
    private String type;
    private List<Creature> creatures;

    public Cell(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.creatures = new ArrayList<>();
    }

    public String getType() { return type; }
    public List<Creature> getCreatures() { return creatures; }
    public void addCreature(Creature c) { creatures.add(c); }

    public String getInfo() {
        return "Cell type: " + type + ", Creatures: " + creatures.size();
    }

    public Color getColor() {
        switch(type.toLowerCase()) {
            case "grass": return Color.GREEN;
            case "water": return Color.BLUE;
            case "sand": return Color.YELLOW;
            default: return Color.LIGHT_GRAY;
        }
    }

    // Make a deep copy of the cell
    public Cell copy() {
        Cell c = new Cell(x, y, type);
        for (Creature cr : creatures) {
            c.addCreature(cr.copy());
        }
        return c;
    }

    // Add this for compatibility with World
    @Override
    public Cell clone() {
        return copy();
    }
}