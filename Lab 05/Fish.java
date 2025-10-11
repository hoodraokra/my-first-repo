public class Fish extends Creature {
    public Fish(String name, String color, double weight) {
        super(name, color, weight, "Fish");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " the fish swims and searches for nutrients.");
    }
}