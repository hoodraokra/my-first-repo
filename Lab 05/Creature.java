public abstract class Creature implements TurnTaker {
    protected String name;
    protected String color;
    protected double weight;
    protected String type;

    public Creature(String name, String color, double weight, String type) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.type = type;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
    public double getWeight() { return weight; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return type + " " + name + " (" + color + ", " + weight + ")";
    }
}