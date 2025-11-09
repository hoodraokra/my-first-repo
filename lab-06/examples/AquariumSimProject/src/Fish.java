public class Fish {
    private String name;
    private boolean isHungry;

    public Fish(String name) {
        this.name = name;
        this.isHungry = true;
    }

    public void eat(Food food) {
        System.out.println(name + " is eating " + food.getType());
        isHungry = false;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public String getName() {
        return name;
    }
}