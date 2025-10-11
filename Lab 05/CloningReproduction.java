public class CloningReproduction implements ReproductionStrategy {
    @Override
    public void reproduce(String speciesName) {
        System.out.println(speciesName + " reproduces by cloning (rhizomes/ runners).");
    }
}