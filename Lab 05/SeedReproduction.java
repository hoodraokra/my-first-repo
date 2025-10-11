public class SeedReproduction implements ReproductionStrategy {
    @Override
    public void reproduce(String speciesName) {
        System.out.println(speciesName + " reproduces via seeds.");
    }
}