public class Customer {
    private double wallet = 500;

    public double spend(double amount) {
        if (amount > wallet) {
            double temp = wallet;
            wallet = 0;
            return temp;
        }
        wallet -= amount;
        return amount;
    }

    public void receive(double amount) {
        wallet += amount;
    }

    public double checkWallet() {
        return wallet;
    }
}