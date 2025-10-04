import java.awt.*;
import java.io.*;
import javax.swing.*;

public class CreaturesFromFileGUI {

    private ProcessCreatureFile process;
    private Creature selectedCreature;

    public CreaturesFromFileGUI() {
        try {
            process = new ProcessCreatureFile("creature-data.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage());
            return;
        }

        JFrame frame = new JFrame("Creatures From File GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        JTextField nameField = new JTextField(10);
        JTextField colorField = new JTextField(10);
        JTextField weightField = new JTextField(10);
        JTextField typeField = new JTextField(10);

        JButton saveButton = new JButton("Save");
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        JPanel middlePanel = new JPanel(new GridLayout(6, 2));
        middlePanel.add(new JLabel("Name:"));
        middlePanel.add(nameField);
        middlePanel.add(new JLabel("Color:"));
        middlePanel.add(colorField);
        middlePanel.add(new JLabel("Weight:"));
        middlePanel.add(weightField);
        middlePanel.add(new JLabel("Type:"));
        middlePanel.add(typeField);
        middlePanel.add(saveButton);
        middlePanel.add(addButton);
        middlePanel.add(deleteButton);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Creature c : process.getCreatures()) {
            listModel.addElement(c.getName());
        }
        JList<String> creatureList = new JList<>(listModel);
        creatureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        creatureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = creatureList.getSelectedIndex();
                if (index >= 0) {
                    selectedCreature = process.getCreature(index);
                    nameField.setText(selectedCreature.getName());
                    colorField.setText(selectedCreature.getColor());
                    weightField.setText(String.valueOf(selectedCreature.getWeight()));
                    typeField.setText(selectedCreature.getType());
                    displayArea.setText(selectedCreature.toString());
                }
            }
        });

        saveButton.addActionListener(e -> {
            int index = creatureList.getSelectedIndex();
            if (index >= 0 && selectedCreature != null) {
                try {
                    selectedCreature.setName(nameField.getText());
                    selectedCreature.setColor(colorField.getText());
                    selectedCreature.setWeight(Double.parseDouble(weightField.getText()));
                    selectedCreature.setType(typeField.getText());

                    process.updateCreature(index, selectedCreature);
                    process.writeToFile();

                    listModel.set(index, selectedCreature.getName());
                    displayArea.setText(selectedCreature.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input!");
                }
            }
        });

        addButton.addActionListener(e -> {
            try {
                Creature newCreature = new Creature(
                        nameField.getText(),
                        colorField.getText(),
                        Double.parseDouble(weightField.getText()),
                        typeField.getText()
                );
                process.addCreature(newCreature);
                process.writeToFile();
                listModel.addElement(newCreature.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
            }
        });

        deleteButton.addActionListener(e -> {
            int index = creatureList.getSelectedIndex();
            if (index >= 0) {
                process.deleteCreature(index);
                process.writeToFile();
                listModel.remove(index);
                displayArea.setText("");
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(creatureList), BorderLayout.WEST);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(new JScrollPane(displayArea), BorderLayout.EAST);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new CreaturesFromFileGUI();
    }
}