public class SporeReproduction implements ReproductionStrategy {
    @Override
    public void reproduce(String speciesName) {
        System.out.println(speciesName + " releases spores.");
    }
}