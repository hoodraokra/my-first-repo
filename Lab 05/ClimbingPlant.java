public class ClimbingPlant extends Plant {
    public ClimbingPlant(String name, String color, double weight) {
        super(name, color, weight, "ClimbingPlant");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " (Climber) extends tendrils and climbs nearby support.");
    }
}