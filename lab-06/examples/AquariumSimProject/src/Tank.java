import java.util.ArrayList;
import java.util.List;

public class Tank {
    private List<Fish> fishes;

    public Tank() {
        fishes = new ArrayList<>();
    }

    public void addFish(Fish fish) {
        fishes.add(fish);
        System.out.println(fish.getName() + " has been added to the tank.");
    }

    public void feed(Food food) {
        for (Fish fish : fishes) {
            if (fish.isHungry()) {
                fish.eat(food);
            }
        }
    }

    public void showStatus() {
        System.out.println("Tank Status:");
        for (Fish fish : fishes) {
            System.out.println(fish.getName() + " is " + (fish.isHungry() ? "hungry" : "full"));
        }
    }
}