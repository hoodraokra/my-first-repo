import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GraphAnimationFromCSV extends JFrame {

    private List<DataPoint> dataPoints;

    public GraphAnimationFromCSV(String csvFile) {
        setTitle("Animated Graph from CSV");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dataPoints = readCSV(csvFile);
        add(new GraphPanel(dataPoints));
    }

    public static List<DataPoint> readCSV(String fileName) {
        List<DataPoint> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    points.add(new DataPoint(Integer.parseInt(tokens[0].trim()),
                                             Double.parseDouble(tokens[1].trim())));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            System.exit(1);
        }
        return points;
    }

    public static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : "simulation_output.csv";
        SwingUtilities.invokeLater(() -> new GraphAnimationFromCSV(inputFile).setVisible(true));
    }

    static class DataPoint {
        int iteration; double population;
        DataPoint(int iteration, double population) { this.iteration = iteration; this.population = population; }
    }

    class GraphPanel extends JPanel {
        private List<DataPoint> dataPoints;
        private int currentIndex = 0;
        private Timer timer;

        public GraphPanel(List<DataPoint> dataPoints) {
            this.dataPoints = dataPoints;

            timer = new Timer(50, e -> { // faster animation
                currentIndex++;
                if (currentIndex > dataPoints.size()) timer.stop();
                repaint();
            });
            timer.start();
        }

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

            if (dataPoints.isEmpty()) {
                g2.drawString("No data", width/2-50, height/2);
                return;
            }

            int minIter = dataPoints.get(0).iteration;
            int maxIter = dataPoints.get(dataPoints.size()-1).iteration;
            double minPop = Double.MAX_VALUE, maxPop = Double.MIN_VALUE;
            for (DataPoint dp : dataPoints) {
                if (dp.population < minPop) minPop = dp.population;
                if (dp.population > maxPop) maxPop = dp.population;
            }
            minPop = Math.min(0, minPop);

            int plotWidth = width - marginLeft - marginRight;
            int plotHeight = height - marginTop - marginBottom;

            int prevX = -1, prevY = -1;
            g2.setColor(Color.ORANGE); // changed color
            for (int i = 0; i < currentIndex && i < dataPoints.size(); i++) {
                DataPoint dp = dataPoints.get(i);
                int x = marginLeft + (int)((dp.iteration - minIter)*(double)plotWidth/(maxIter - minIter));
                int y = height - marginBottom - (int)((dp.population - minPop)*(double)plotHeight/(maxPop - minPop));
                g2.fillOval(x-3, y-3, 6, 6);
                if (prevX != -1) g2.drawLine(prevX, prevY, x, y);
                prevX = x; prevY = y;
            }

            g2.setColor(Color.BLACK);
            g2.drawString("Iteration", marginLeft + plotWidth/2, height-30);
            g2.drawString("Population", 20, marginTop + plotHeight/2);
        }
    }
}