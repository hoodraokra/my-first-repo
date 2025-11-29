import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphFromCSV extends JFrame {

    private List<DataPoint> dataPoints;

    public GraphFromCSV(String inputFileName) {
        setTitle("Rabbit Simulation Graph");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dataPoints = readCSV(inputFileName);
        add(new GraphPanel());
    }

    private List<DataPoint> readCSV(String fileName) {
        List<DataPoint> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    points.add(new DataPoint(Integer.parseInt(tokens[0].trim()),
                                             (int)Double.parseDouble(tokens[1].trim())));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV: " + e.getMessage());
        }
        return points;
    }

    public static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : "simulation_output.csv";
        SwingUtilities.invokeLater(() -> new GraphFromCSV(inputFile).setVisible(true));
    }

    class DataPoint {
        int generation, population;
        DataPoint(int generation, int population) {
            this.generation = generation;
            this.population = population;
        }
    }

    class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int marginLeft = 80, marginRight = 30, marginTop = 30, marginBottom = 80;
            int width = getWidth(), height = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            g2.setColor(Color.BLACK);
            g2.drawLine(marginLeft, height - marginBottom, width - marginRight, height - marginBottom);
            g2.drawLine(marginLeft, height - marginBottom, marginLeft, marginTop);

            if (dataPoints == null || dataPoints.isEmpty()) {
                g2.drawString("No data", width / 2 - 50, height / 2);
                return;
            }

            int minGen = dataPoints.get(0).generation;
            int maxGen = dataPoints.get(dataPoints.size()-1).generation;
            int minPop = Integer.MAX_VALUE, maxPop = Integer.MIN_VALUE;
            for (DataPoint dp : dataPoints) {
                if (dp.population < minPop) minPop = dp.population;
                if (dp.population > maxPop) maxPop = dp.population;
            }
            minPop = Math.min(0, minPop);

            int plotWidth = width - marginLeft - marginRight;
            int plotHeight = height - marginTop - marginBottom;

            int prevX = -1, prevY = -1;
            g2.setColor(Color.GREEN); // changed color
            for (DataPoint dp : dataPoints) {
                int x = marginLeft + (int)((dp.generation - minGen) * (double)plotWidth / (maxGen - minGen));
                int y = height - marginBottom - (int)((dp.population - minPop) * (double)plotHeight / (maxPop - minPop));

                g2.fillOval(x - 3, y - 3, 6, 6);
                if (prevX != -1) g2.drawLine(prevX, prevY, x, y);
                prevX = x; prevY = y;
            }

            g2.setColor(Color.BLACK);
            g2.drawString("Generation", marginLeft + plotWidth / 2, height - 30);
            g2.drawString("Population", 20, marginTop + plotHeight / 2);
        }
    }
}