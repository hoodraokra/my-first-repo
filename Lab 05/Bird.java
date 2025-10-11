public class Bird extends Creature {
    public Bird(String name, String color, double weight) {
        super(name, color, weight, "Bird");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " the bird flies and searches for food.");
    }
}