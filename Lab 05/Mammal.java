public class Mammal extends Creature {
    public Mammal(String name, String color, double weight) {
        super(name, color, weight, "Mammal");
    }

    @Override
    public void takeTurn() {
        System.out.println(getName() + " the mammal walks around and looks for food.");
    }
}