import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class CircleSizeSlider extends JFrame {
    private JSlider slider;
    private CirclePanel circlePanel;

    public CircleSizeSlider() {
        setTitle("Circle Size Slider");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add the custom panel that draws the circle first
        circlePanel = new CirclePanel();
        add(circlePanel, BorderLayout.CENTER);

        // Create a slider with a range for the circle diameter
        slider = new JSlider(JSlider.HORIZONTAL, 10, 300, 50);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Listen to changes on the slider
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int diameter = slider.getValue();
                circlePanel.setCircleDiameter(diameter); // update size
                circlePanel.repaint();                   // redraw circle
            }
        });

        add(slider, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CircleSizeSlider frame = new CircleSizeSlider();
            frame.setVisible(true); // make sure visible AFTER adding components
        });
    }
}

// Custom panel to draw a circle
class CirclePanel extends JPanel {
    private int circleDiameter = 50; // default diameter

    public void setCircleDiameter(int diameter) {
        this.circleDiameter = diameter;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (getWidth() - circleDiameter) / 2;
        int y = (getHeight() - circleDiameter) / 2;
        g.setColor(Color.BLUE);
        g.fillOval(x, y, circleDiameter, circleDiameter);
    }
}