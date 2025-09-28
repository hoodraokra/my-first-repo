import java.io.*;
import java.util.*;

public class ProcessCreatureFile {
    public static void main(String[] args) {
        String filename = "creature-data.csv";
        ArrayList<Creature> creatures = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                creatures.add(new Creature(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        System.out.println("Original creatures:");
        for (Creature c : creatures) System.out.println(c);

        creatures.add(new Creature("Hydra", "Green", 300, "predator"));

        if (!creatures.isEmpty()) creatures.remove(0);

        if (creatures.size() > 1) {
            Creature c = creatures.get(1);
            c.setWeight(c.getWeight() + 20);
            c.setColor("Golden");
        }

        System.out.println("\nUpdated creatures:");
        for (Creature c : creatures) System.out.println(c);

        try (PrintWriter pw = new PrintWriter(new File(filename))) {
            for (Creature c : creatures) {
                pw.println(c);
            }
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}