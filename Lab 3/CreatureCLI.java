public class CreatureCLI {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java CreatureCLI <csvfile> <command> [args]");
            System.exit(1);
        }

        String fileName = args[0];
        String command = args[1];

        try {
            CreatureRegistry reg = new CreatureRegistry(fileName);

            switch (command.toLowerCase()) {
                case "create":
                    reg.addCreature(Creature.fromString(args[2]));
                    reg.saveToFile();
                    System.out.println("Creature added.");
                    break;

                case "read":
                    int readIndex = Integer.parseInt(args[2]);
                    System.out.println(reg.getCreature(readIndex));
                    break;

                case "update":
                    int updateIndex = Integer.parseInt(args[2]);
                    reg.updateCreature(updateIndex, Creature.fromString(args[3]));
                    reg.saveToFile();
                    System.out.println("Creature updated.");
                    break;

                case "delete":
                    int delIndex = Integer.parseInt(args[2]);
                    reg.deleteCreature(delIndex);
                    reg.saveToFile();
                    System.out.println("Creature deleted.");
                    break;

                default:
                    System.out.println("Unknown command.");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}