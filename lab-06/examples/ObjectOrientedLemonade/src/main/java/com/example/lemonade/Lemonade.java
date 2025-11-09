package com.example.lemonade;

public class Lemonade {
    private int lemons;
    private int sugar;
    private int ice;

    public Lemonade(int lemons, int sugar, int ice) {
        this.lemons = lemons;
        this.sugar = sugar;
        this.ice = ice;
    }

    @Override
    public String toString() {
        return lemons + " lemons, " + sugar + " sugar, " + ice + " ice";
    }
}