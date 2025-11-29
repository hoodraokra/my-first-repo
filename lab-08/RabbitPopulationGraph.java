import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class RabbitPopulationGraph extends JFrame {
    private double r = 3.5;        // Growth rate
    private double x0 = 0.5;       // Initial population
    private int iterations = 200;  // Number of iterations

    private JTextField rField;
    private JTextField x0Field;
    private PopulationPanel graphPanel;

    public RabbitPopulationGraph() {
        setTitle("Rabbit Population - Logistic Map");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top input panel with FlowLayout for controls.
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputPanel.add(new JLabel("Growth Rate (r):"));
        rField = new JTextField(String.valueOf(r), 5);
        inputPanel.add(rField);

        inputPanel.add(new JLabel("Initial Population (x0):"));
        x0Field = new JTextField(String.valueOf(x0), 5);
        inputPanel.add(x0Field);

        JButton drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> {
            try {
                r = Double.parseDouble(rField.getText());
                x0 = Double.parseDouble(x0Field.getText());
                graphPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            }
        });
        inputPanel.add(drawButton);

        inputPanel.add(new JLabel("Iterations:"));
        JSlider iterationSlider = new JSlider(JSlider.HORIZONTAL, 50, 1000, iterations);
        iterationSlider.setMajorTickSpacing(150);
        iterationSlider.setMinorTickSpacing(50);
        iterationSlider.setPaintTicks(true);
        iterationSlider.setPaintLabels(true);
        iterationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!iterationSlider.getValueIsAdjusting()) {
                    iterations = iterationSlider.getValue();
                    graphPanel.repaint();
                }
            }
        });
        inputPanel.add(iterationSlider);

        add(inputPanel, BorderLayout.NORTH);

        // Graph panel
        graphPanel = new PopulationPanel();
        add(graphPanel, BorderLayout.CENTER);
    }

    private class PopulationPanel extends JPanel {
        private final int leftMargin = 60, rightMargin = 30, topMargin = 40, bottomMargin = 60;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGraph(g);
        }

        private void drawGraph(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            // Background
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            // Title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Rabbit Population (x) over Iterations", leftMargin, topMargin - 10);

            // Draw axes
            g2.drawLine(leftMargin, height - bottomMargin, width - rightMargin, height - bottomMargin); // x-axis
            g2.drawLine(leftMargin, height - bottomMargin, leftMargin, topMargin); // y-axis

            // Draw grid lines
            g2.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= 10; i++) {
                int y = map(i * 0.1, 0, 1, height - bottomMargin, topMargin);
                g2.drawLine(leftMargin, y, width - rightMargin, y);
            }

            // Compute logistic map values
            double[] population = new double[iterations];
            population[0] = x0;
            for (int i = 1; i < iterations; i++) {
                population[i] = r * population[i - 1] * (1 - population[i - 1]);
            }

            // Plot graph
            g2.setColor(Color.GREEN);
            for (int i = 1; i < iterations; i++) {
                int x1 = map(i - 1, 0, iterations, leftMargin, width - rightMargin);
                int y1 = map(population[i - 1], 0, 1, height - bottomMargin, topMargin);
                int x2 = map(i, 0, iterations, leftMargin, width - rightMargin);
                int y2 = map(population[i], 0, 1, height - bottomMargin, topMargin);
                g2.drawLine(x1, y1, x2, y2);

                // Draw small points for clarity
                g2.fillOval(x2 - 2, y2 - 2, 4, 4);
            }
        }

        private int map(double value, double srcMin, double srcMax, double dstMin, double dstMax) {
            return (int) (dstMin + (value - srcMin) * (dstMax - dstMin) / (srcMax - srcMin));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RabbitPopulationGraph frame = new RabbitPopulationGraph();
            frame.setVisible(true);
        });
    }
}