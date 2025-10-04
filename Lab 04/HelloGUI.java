import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class HelloGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HelloGUI");
        JLabel label = new JLabel("", SwingConstants.CENTER);
        JButton button = new JButton("Greet");
        String[] greetings = {"Hello!", "Hi!", "Welcome!", "Greetings!", "Good to see you!"};
        Random rand = new Random();

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(greetings[rand.nextInt(greetings.length)]);
            }
        });

        frame.add(label, "Center");
        frame.add(button, "South");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}