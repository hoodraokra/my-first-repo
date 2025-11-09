package com.example.lemonade;

public class Inventory {
    private int lemons;
    private int sugar;
    private int ice;

    public void addLemons(int n) { lemons += n; }
    public void addSugar(int n) { sugar += n; }
    public void addIce(int n) { ice += n; }

    @Override
    public String toString() {
        return lemons + " lemons, " + sugar + " sugar, " + ice + " ice";
    }
}