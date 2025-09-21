import java.util.Scanner;

public class GoodCasino {

    public static double play(Customer c, SlotMachine sm, double bet) {
        double spent = c.spend(bet);
        double winnings = sm.pullLever(spent);
        c.receive(winnings);
        return winnings;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Customer customer = new Customer();
        SlotMachine slot = new SlotMachine();

        while (true) {
            System.out.println("\nYou have $" + customer.checkWallet());
            System.out.print("Enter bet amount (or 0 to quit): ");
            double bet = sc.nextDouble();
            if (bet <= 0 || customer.checkWallet() <= 0) break;

            double won = play(customer, slot, bet);
            System.out.println("Slot Machine: " + slot.toString());
            System.out.println("You won: $" + won);
        }

        System.out.println("Game over! You have $" + customer.checkWallet());
        sc.close();
    }
}