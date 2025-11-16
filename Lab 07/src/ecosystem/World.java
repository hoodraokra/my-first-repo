package ecosystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class World {
    private int width;
    private int height;
    private Cell[][] grid;

    // For reset
    private Cell[][] initialGrid;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        initialGrid = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell c = new Cell(x, y, "grass");
                grid[x][y] = c;
                initialGrid[x][y] = c.clone(); // store initial state
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        return grid[x][y];
    }

    public void takeTurn() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (Creature c : grid[x][y].getCreatures()) {
                    c.takeTurn();
                }
            }
        }
        System.out.println("World turn executed.");
    }

    public void resetToInitialState() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                grid[x][y] = initialGrid[x][y].clone();

        System.out.println("World reset to initial state.");
    }

    public void loadInitialState(File f) {
        loadFromFile(f);
        // Save this as initial state for reset
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                initialGrid[x][y] = grid[x][y].clone();

        System.out.println("Initial state loaded from: " + f.getAbsolutePath());
    }

    public void saveInitialState(File f) {
        saveToFile(f, initialGrid);
        System.out.println("Initial state saved to: " + f.getAbsolutePath());
    }

    public void saveCurrentState(File f) {
        saveToFile(f, grid);
        System.out.println("Current state saved to: " + f.getAbsolutePath());
    }

    private void loadFromFile(File f) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String sizeLine = br.readLine();
            String[] dims = sizeLine.split(",");
            int w = Integer.parseInt(dims[0]);
            int h = Integer.parseInt(dims[1]);

            grid = new Cell[w][h];
            width = w;
            height = h;

            String line;
            int y = 0;
            while ((line = br.readLine()) != null && y < height) {
                String[] cells = line.split(",");
                for (int x = 0; x < width && x < cells.length; x++) {
                    String[] parts = cells[x].split("\\|");
                    String type = parts[0];
                    Cell c = new Cell(x, y, type);

                    if (parts.length > 1) { // creatures exist
                        String[] creatures = parts[1].split(";");
                        for (String crStr : creatures) {
                            String[] crParts = crStr.split(":");
                            if (crParts.length == 3) {
                                String name = crParts[0];
                                int energy = Integer.parseInt(crParts[1]);
                                int age = Integer.parseInt(crParts[2]);
                                c.addCreature(new Creature(name, energy, age));
                            }
                        }
                    }
                    grid[x][y] = c;
                }
                y++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(File f, Cell[][] targetGrid) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println(width + "," + height);
            for (int y = 0; y < height; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    Cell c = targetGrid[x][y];
                    sb.append(c.getType());
                    List<Creature> creatures = c.getCreatures();
                    if (!creatures.isEmpty()) {
                        sb.append("|");
                        for (int i = 0; i < creatures.size(); i++) {
                            Creature cr = creatures.get(i);
                            sb.append(cr.getName()).append(":").append(cr.getEnergy()).append(":").append(cr.getAge());
                            if (i < creatures.size() - 1) sb.append(";");
                        }
                    }
                    if (x < width - 1) sb.append(",");
                }
                pw.println(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}