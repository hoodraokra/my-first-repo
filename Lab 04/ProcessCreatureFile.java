import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProcessCreatureFile {
    private ArrayList<Creature> creatures;
    private String fileName;

    public ProcessCreatureFile(String fileName) throws IOException {
        this.fileName = fileName;
        creatures = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) return; // no file yet

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length == 4) {
                String name = parts[0];
                String color = parts[1];
                double weight = Double.parseDouble(parts[2]);
                String type = parts[3];
                creatures.add(new Creature(name, color, weight, type));
            }
        }
        sc.close();
    }

    public void writeToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Creature c : creatures) {
                pw.println(c.toString());
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    public Creature getCreature(int index) {
        return creatures.get(index);
    }

    public void addCreature(Creature c) {
        creatures.add(c);
    }

    public void updateCreature(int index, Creature c) {
        creatures.set(index, c);
    }

    public void deleteCreature(int index) {
        creatures.remove(index);
    }
}