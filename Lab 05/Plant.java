import java.util.*;

public class Plant extends Creature {
    public static class SpeciesTemplate {
        public final boolean seasonal;
        public final boolean woodyStem;
        public final boolean evergreen;
        public final String growthHabit; 
        public final boolean hasFlowers;
        public final List<String> reproductionMethods; 
        public SpeciesTemplate(boolean seasonal, boolean woodyStem, boolean evergreen,
                               String growthHabit, boolean hasFlowers, List<String> reproductionMethods) {
            this.seasonal = seasonal;
            this.woodyStem = woodyStem;
            this.evergreen = evergreen;
            this.growthHabit = growthHabit;
            this.hasFlowers = hasFlowers;
            this.reproductionMethods = new ArrayList<>(reproductionMethods);
        }
    }

     private static final Map<String, SpeciesTemplate> speciesRegistry = new HashMap<>();

    private final String species;
    private boolean seasonal;
    private boolean woodyStem;
    private boolean evergreen;
    private String growthHabit;
    private boolean hasFlowers;
    private List<ReproductionStrategy> reproductionStrategies = new ArrayList<>();

     public Plant(String name, String color, double weight, String species) {
        super(name, color, weight, "Plant");
        this.species = species;
        SpeciesTemplate t = speciesRegistry.get(species);
        if (t == null) {
             this.seasonal = true;
            this.woodyStem = false;
            this.evergreen = false;
            this.growthHabit = "short";
            this.hasFlowers = false;
            this.reproductionStrategies.add(new SeedReproduction());
        } else {
            this.seasonal = t.seasonal;
            this.woodyStem = t.woodyStem;
            this.evergreen = t.evergreen;
            this.growthHabit = t.growthHabit;
            this.hasFlowers = t.hasFlowers;
            for (String m : t.reproductionMethods) {
                switch (m.toLowerCase()) {
                    case "seeds": this.reproductionStrategies.add(new SeedReproduction()); break;
                    case "spores": this.reproductionStrategies.add(new SporeReproduction()); break;
                    case "cloning": this.reproductionStrategies.add(new CloningReproduction()); break;
                }
            }
        }
    }

     public Plant(String name, String color, double weight, String species,
                 boolean seasonal, boolean woodyStem, boolean evergreen,
                 String growthHabit, boolean hasFlowers, List<String> reproductionMethodNames) {
        super(name, color, weight, "Plant");
        this.species = species;
        this.seasonal = seasonal;
        this.woodyStem = woodyStem;
        this.evergreen = evergreen;
        this.growthHabit = growthHabit;
        this.hasFlowers = hasFlowers;
        for (String m : reproductionMethodNames) {
            switch (m.toLowerCase()) {
                case "seeds": this.reproductionStrategies.add(new SeedReproduction()); break;
                case "spores": this.reproductionStrategies.add(new SporeReproduction()); break;
                case "cloning": this.reproductionStrategies.add(new CloningReproduction()); break;
            }
        }
    }

     public static void defineSpecies(String name, SpeciesTemplate template) throws Exception {
        if (speciesRegistry.containsKey(name)) {
            throw new Exception("Species '" + name + "' already exists.");
        }
        speciesRegistry.put(name, template);
    }

    public static SpeciesTemplate getSpeciesTemplate(String name) {
        return speciesRegistry.get(name);
    }

    public String getSpecies() { return species; }

     public void grow() {
        System.out.println(name + " (" + species + ") grows slowly.");
    }
    public void grow(int sunlight) {
        System.out.println(name + " (" + species + ") grows with sunlight level " + sunlight + ".");
    }

    @Override
    public void takeTurn() {
         System.out.println(name + " (" + species + ") performs plant actions:");
        if (!reproductionStrategies.isEmpty()) {
             reproductionStrategies.get(0).reproduce(species);
        } else {
            System.out.println(species + " has no reproduction strategy defined.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " species=" + species + " seasonal=" + seasonal + " woodyStem=" + woodyStem + " growth=" + growthHabit + " flowers=" + hasFlowers;
    }
}