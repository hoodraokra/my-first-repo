public class Creature {
    private String name;
    private String color;
    private double weight;
    private String type;

    public Creature(String name, String color, double weight, String type) {
        this.name = name;
        this.color = color;
        this.weight = weight;
        this.type = type;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return name + "," + color + "," + weight + "," + type;
    }

    public static Creature fromString(String str) {
        String[] parts = str.split(" ");
        String name = "", color = "", type = "";
        double weight = 0;
        for (String part : parts) {
            String[] kv = part.split(":");
            switch (kv[0].toLowerCase()) {
                case "name": name = kv[1]; break;
                case "color": color = kv[1]; break;
                case "weight": weight = Double.parseDouble(kv[1]); break;
                case "type": type = kv[1]; break;
            }
        }
        return new Creature(name, color, weight, type);
    }
}