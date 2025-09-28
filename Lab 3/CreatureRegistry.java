import java.io.*;
import java.util.*;

public class CreatureRegistry {
    private ArrayList<Creature> creatures;
    private String fileName;

    public CreatureRegistry(String fileName) throws IOException {
        this.fileName = fileName;
        creatures = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() throws IOException {
        File f = new File(fileName);
        if (!f.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length == 4)
                creatures.add(new Creature(fields[0], fields[1], Double.parseDouble(fields[2]), fields[3]));
        }
        br.close();
    }

    public void saveToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (Creature c : creatures) {
            bw.write(c.toString());
            bw.newLine();
        }
        bw.close();
    }

    public void addCreature(Creature c) { creatures.add(c); }
    public Creature getCreature(int index) { return creatures.get(index); }
    public void updateCreature(int index, Creature c) { creatures.set(index, c); }
    public void deleteCreature(int index) { creatures.remove(index); }
    public int count() { return creatures.size(); }

    public static void main(String[] args) throws IOException {
        CreatureRegistry reg = new CreatureRegistry("creature-data.csv");
        System.out.println("Loaded " + reg.count() + " creatures.");
        reg.addCreature(new Creature("Phoenix", "Red", 50, "predator"));
        reg.saveToFile();
        System.out.println("Added Phoenix and saved file.");
    }
}