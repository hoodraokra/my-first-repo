public class Hex {
    public static void main(String[] args) {
        System.out.println("Program has started.");

        if (args.length == 0) {
            System.out.println("Please provide a hexadecimal value.");
            return;
        }

        String hexVal = args[0];
        int decimal = 0;

        for (char c : hexVal.toCharArray()) {
            int hexDigit = 0;
            switch (c) {
                case '0': hexDigit = 0; break;
                case '1': hexDigit = 1; break;
                case '2': hexDigit = 2; break;
                case '3': hexDigit = 3; break;
                case '4': hexDigit = 4; break;
                case '5': hexDigit = 5; break;
                case '6': hexDigit = 6; break;
                case '7': hexDigit = 7; break;
                case '8': hexDigit = 8; break;
                case '9': hexDigit = 9; break;
                case 'a': case 'A': hexDigit = 10; break;
                case 'b': case 'B': hexDigit = 11; break;
                case 'c': case 'C': hexDigit = 12; break;
                case 'd': case 'D': hexDigit = 13; break;
                case 'e': case 'E': hexDigit = 14; break;
                case 'f': case 'F': hexDigit = 15; break;
                default:
                    System.out.println("Invalid character: " + c);
                    return;
            }
            decimal = decimal * 16 + hexDigit;
        }

        System.out.println("Decimal value: " + decimal);
        System.out.println("Program has ended.");
    }
}