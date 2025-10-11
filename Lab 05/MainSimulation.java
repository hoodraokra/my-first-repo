import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class MainSimulation {
    public static void main(String[] args) {
        try {
            Plant.defineSpecies("Moss", new Plant.SpeciesTemplate(true, false, true, "ground", false, Arrays.asList("cloning", "spores")));
            Plant.defineSpecies("FloweringPlant", new Plant.SpeciesTemplate(true, true, false, "upright", true, Arrays.asList("seeds")));
            Plant.defineSpecies("ClimbingPlant", new Plant.SpeciesTemplate(false, false, true, "climber", true, Arrays.asList("seeds", "cloning")));

            String json = new String(Files.readAllBytes(Paths.get("world.json")), "UTF-8");

            int rows = extractInt(json, "\"rows\"\\s*:\\s*(\\d+)", 2);
            int cols = extractInt(json, "\"cols\"\\s*:\\s*(\\d+)", 2);

            World world = new World(rows, cols);

             Pattern tilesPattern = Pattern.compile("\"tiles\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL);
            Matcher tilesMatcher = tilesPattern.matcher(json);
            if (tilesMatcher.find()) {
                String tilesBlock = tilesMatcher.group(1);
                 Pattern tileObj = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
                Matcher tileMatcher = tileObj.matcher(tilesBlock);
                while (tileMatcher.find()) {
                    String tileText = tileMatcher.group(1);
                    int row = extractInt(tileText, "\"row\"\\s*:\\s*(\\d+)", 0);
                    int col = extractInt(tileText, "\"col\"\\s*:\\s*(\\d+)", 0);
                    int water = extractInt(tileText, "\"water\"\\s*:\\s*(\\d+)", 5);
                    int temp = extractInt(tileText, "\"temperature\"\\s*:\\s*(\\d+)", 20);
                    int nutrients = extractInt(tileText, "\"nutrients\"\\s*:\\s*(\\d+)", 10);

                    Tile tile = new Tile(water, temp, nutrients);

                     Pattern creaturesPattern = Pattern.compile("\"creatures\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
                    Matcher creaturesMatcher = creaturesPattern.matcher(tileText);
                    if (creaturesMatcher.find()) {
                        String creaturesBlock = creaturesMatcher.group(1);
                         Matcher creatureMatcher = tileObj.matcher(creaturesBlock);
                        while (creatureMatcher.find()) {
                            String ctext = creatureMatcher.group(1);
                            String className = extractString(ctext, "\"class\"\\s*:\\s*\"([^\"]+)\"");
                            String species = extractString(ctext, "\"species\"\\s*:\\s*\"([^\"]+)\"");
                            String name = extractString(ctext, "\"name\"\\s*:\\s*\"([^\"]+)\"");
                            String color = extractString(ctext, "\"color\"\\s*:\\s*\"([^\"]+)\"");
                            double weight = extractDouble(ctext, "\"weight\"\\s*:\\s*([0-9]+(\\.[0-9]+)?)", 1.0);

                            Creature created = null;
                            if ("Plant".equalsIgnoreCase(className)) {
                                if (species != null && !species.isEmpty()) {
                                    created = new Plant(name, color, weight, species);
                                } else {
                                    created = new Plant(name, color, weight, "Plant");
                                }
                            } else if ("Moss".equalsIgnoreCase(className)) {
                                created = new Moss(name, color, weight);
                            } else if ("FloweringPlant".equalsIgnoreCase(className)) {
                                created = new FloweringPlant(name, color, weight);
                            } else if ("ClimbingPlant".equalsIgnoreCase(className)) {
                                created = new ClimbingPlant(name, color, weight);
                            } else if ("Bird".equalsIgnoreCase(className)) {
                                created = new Bird(name, color, weight);
                            } else if ("Mammal".equalsIgnoreCase(className)) {
                                created = new Mammal(name, color, weight);
                            } else if ("Fish".equalsIgnoreCase(className)) {
                                created = new Fish(name, color, weight);
                            } else {
                                // fallback: if class not provided, try type field
                                String typeField = extractString(ctext, "\"type\"\\s*:\\s*\"([^\"]+)\"");
                                if ("Bird".equalsIgnoreCase(typeField)) created = new Bird(name, color, weight);
                                else if ("Mammal".equalsIgnoreCase(typeField)) created = new Mammal(name, color, weight);
                                else if ("Fish".equalsIgnoreCase(typeField)) created = new Fish(name, color, weight);
                                else created = new Plant(name, color, weight, (species==null?"Plant":species));
                            }

                            if (created != null) tile.addCreature(created);
                        }
                    }

                     if (row >= 0 && row < rows && col >= 0 && col < cols) {
                        world.setTile(row, col, tile);
                    } else {
                        System.out.println("Skipping tile out of bounds: row=" + row + " col=" + col);
                    }
                }
            } else {
                System.out.println("No tiles found in world.json; creating a default world with one tile.");
                Tile t = new Tile(5,20,10);
                t.addCreature(new Moss("DefaultMoss","Green",0.1));
                world.setTile(0,0,t);
            }

             for (int turn = 1; turn <= 100; turn++) {
                System.out.println("\n=== Turn " + turn + " ===");
                world.takeTurn();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

     private static int extractInt(String text, String pattern, int fallback) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); }
            catch (Exception e) { }
        }
        return fallback;
    }
    private static double extractDouble(String text, String pattern, double fallback) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            try { return Double.parseDouble(m.group(1)); }
            catch (Exception e) { }
        }
        return fallback;
    }
    private static String extractString(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) return m.group(1);
        return "";
    }
}