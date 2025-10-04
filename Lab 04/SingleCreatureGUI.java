import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SingleCreatureGUI {
    private Creature creature;

    public SingleCreatureGUI() {
        creature = new Creature("Dragon", "Red", 500, "Predator");

        JFrame frame = new JFrame("Single Creature GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setText(creature.toString());

        JTextField nameField = new JTextField(creature.getName(), 10);
        JTextField colorField = new JTextField(creature.getColor(), 10);
        JTextField weightField = new JTextField(String.valueOf(creature.getWeight()), 10);
        JTextField typeField = new JTextField(creature.getType(), 10);

        JButton saveButton = new JButton("Save");
        JButton actionButton = new JButton("Growl");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    creature.setName(nameField.getText());
                    creature.setColor(colorField.getText());
                    creature.setWeight(Double.parseDouble(weightField.getText()));
                    creature.setType(typeField.getText());
                    displayArea.setText(creature.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input!");
                }
            }
        });

        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(creature.getName() + " growls loudly!");
            }
        });

        JPanel leftPanel = new JPanel(new GridLayout(5, 2));
        leftPanel.add(new JLabel("Name:"));
        leftPanel.add(nameField);
        leftPanel.add(new JLabel("Color:"));
        leftPanel.add(colorField);
        leftPanel.add(new JLabel("Weight:"));
        leftPanel.add(weightField);
        leftPanel.add(new JLabel("Type:"));
        leftPanel.add(typeField);
        leftPanel.add(saveButton);
        leftPanel.add(actionButton);

        frame.setLayout(new BorderLayout());
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SingleCreatureGUI();
    }
}