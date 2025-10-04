import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class MultipleCreatureGUI {
    private ArrayList<Creature> creatures;
    private Creature selectedCreature;

    public MultipleCreatureGUI() {
        creatures = new ArrayList<>();
        creatures.add(new Creature("Dragon", "Red", 500, "Predator"));
        creatures.add(new Creature("Phoenix", "Orange", 50, "Predator"));
        creatures.add(new Creature("Unicorn", "White", 300, "Prey"));

        JFrame frame = new JFrame("Multiple Creature GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        JTextField nameField = new JTextField(10);
        JTextField colorField = new JTextField(10);
        JTextField weightField = new JTextField(10);
        JTextField typeField = new JTextField(10);

        JButton saveButton = new JButton("Save");
        JButton actionButton = new JButton("Growl");

        JPanel middlePanel = new JPanel(new GridLayout(5, 2));
        middlePanel.add(new JLabel("Name:"));
        middlePanel.add(nameField);
        middlePanel.add(new JLabel("Color:"));
        middlePanel.add(colorField);
        middlePanel.add(new JLabel("Weight:"));
        middlePanel.add(weightField);
        middlePanel.add(new JLabel("Type:"));
        middlePanel.add(typeField);
        middlePanel.add(saveButton);
        middlePanel.add(actionButton);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Creature c : creatures) {
            listModel.addElement(c.getName());
        }
        JList<String> creatureList = new JList<>(listModel);
        creatureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        creatureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = creatureList.getSelectedIndex();
                if (index >= 0) {
                    selectedCreature = creatures.get(index);
                    nameField.setText(selectedCreature.getName());
                    colorField.setText(selectedCreature.getColor());
                    weightField.setText(String.valueOf(selectedCreature.getWeight()));
                    typeField.setText(selectedCreature.getType());
                    displayArea.setText(selectedCreature.toString());
                }
            }
        });

        saveButton.addActionListener(e -> {
            if (selectedCreature != null) {
                try {
                    selectedCreature.setName(nameField.getText());
                    selectedCreature.setColor(colorField.getText());
                    selectedCreature.setWeight(Double.parseDouble(weightField.getText()));
                    selectedCreature.setType(typeField.getText());
                    displayArea.setText(selectedCreature.toString());

                    int index = creatureList.getSelectedIndex();
                    listModel.set(index, selectedCreature.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input!");
                }
            }
        });

        actionButton.addActionListener(e -> {
            if (selectedCreature != null) {
                System.out.println(selectedCreature.getName() + " growls loudly!");
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(creatureList), BorderLayout.WEST);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(new JScrollPane(displayArea), BorderLayout.EAST);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MultipleCreatureGUI();
    }
}