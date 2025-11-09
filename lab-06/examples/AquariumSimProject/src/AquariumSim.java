public class AquariumSim {
    public static void main(String[] args) {
        Tank tank = new Tank();
        Fish fish1 = new Fish("Goldie");
        Fish fish2 = new Fish("Bubbles");

        tank.addFish(fish1);
        tank.addFish(fish2);

        tank.feed(new Food("Flakes"));
        tank.showStatus();
    }
}