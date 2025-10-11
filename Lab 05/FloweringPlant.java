public class FloweringPlant extends Plant {
    public FloweringPlant(String name, String color, double weight) {
        super(name, color, weight, "FloweringPlant");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " (Flowering) may bloom or produce fruit depending on season.");
    }
}