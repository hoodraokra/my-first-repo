package com.example.lemonade;

import java.util.Scanner;

public class Game {
    private Player player;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("Hi! Welcome to Lemonsville, California!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        player = new Player(name, 20); // starting cash
        player.getInventory().addLemons(5);
        player.getInventory().addSugar(5);
        player.getInventory().addIce(5);
        
        System.out.println(player.getName() + " has $" + player.getCash());
        System.out.println("Starting inventory: " + player.getInventory());
        
        // Example simple round
        Lemonade lemonade = new Lemonade(2,1,1);
        System.out.println(player.getName() + " makes lemonade: " + lemonade);
    }
}