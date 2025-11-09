package com.example.lemonade;

public class Player {
    private String name;
    private int cash;
    private Inventory inventory;

    public Player(String name, int startingCash) {
        this.name = name;
        this.cash = startingCash;
        this.inventory = new Inventory();
    }

    public String getName() { return name; }
    public int getCash() { return cash; }
    public void addCash(int amount) { cash += amount; }
    public Inventory getInventory() { return inventory; }
} 
    

