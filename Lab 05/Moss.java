
public class Moss extends Plant {
    public Moss(String name, String color, double weight) {
         super(name, color, weight, "Moss");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " (Moss) spreads across the ground and absorbs moisture.");
    }
}