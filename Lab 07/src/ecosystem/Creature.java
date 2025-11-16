package ecosystem;

public class Creature {
    private String name;
    private int energy;
    private int age;

    public Creature(String name, int energy, int age) {
        this.name = name;
        this.energy = energy;
        this.age = age;
    }

    public void takeTurn() {
        energy -= 1; // Example logic
        age += 1;
    }

    public String getInfo() {
        return name + " (Energy: " + energy + ", Age: " + age + ")";
    }

    // Add these getters so World.java can access individual fields
    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    // Step 7: Copy method
    public Creature copy() {
        return new Creature(name, energy, age);
    }

    @Override
    public Creature clone() {
        return copy();
    }
}