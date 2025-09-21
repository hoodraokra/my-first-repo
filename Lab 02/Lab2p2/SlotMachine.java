import java.util.Random;

public class SlotMachine {
    private char s1, s2, s3;
    private double moneyPot = 1_000_000;

    public double pullLever(double bet) {
        char[] symbols = {'☺', '♥', '7'};
        Random r = new Random();
        s1 = symbols[r.nextInt(3)];
        s2 = symbols[r.nextInt(3)];
        s3 = symbols[r.nextInt(3)];

        if (s1 == s2 && s2 == s3) {
            double winnings = bet * 10;
            moneyPot -= winnings;
            return winnings;
        }
        return 0;
    }

    public String toString() {
        return "" + s1 + " " + s2 + " " + s3;
    }

    public double getMoneyPot() {
        return moneyPot;
    }
}