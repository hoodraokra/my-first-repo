import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import ecosystem.Cell;
import ecosystem.Creature;
import ecosystem.World;

public class EcosystemGUI {

    private JFrame frame;
    private JPanel gridPanel;
    private JButton[][] cellButtons;
    private JTextArea sidebarArea;
    private JTextArea logArea;
    private JPanel controlPanel;

    private World world;
    private Timer playTimer;
    private boolean isPlaying = false;
    private final int DEFAULT_PLAY_INTERVAL_MS = 400;

    public EcosystemGUI(World world) {
        this.world = world;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        frame = new JFrame("Ecosystem GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(6, 6));
        frame.getContentPane().setBackground(Color.DARK_GRAY);

        // Grid panel
        gridPanel = new JPanel();
        gridPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        initGridFromWorld();

        // Sidebar panel
        sidebarArea = new JTextArea();
        sidebarArea.setEditable(false);
        sidebarArea.setLineWrap(true);
        sidebarArea.setWrapStyleWord(true);
        JScrollPane sidebarScroll = new JScrollPane(sidebarArea);
        sidebarScroll.setPreferredSize(new Dimension(300, 500));
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBorder(BorderFactory.createTitledBorder("Cell & Creatures Info"));
        sidebarPanel.add(sidebarScroll, BorderLayout.CENTER);

        // Bottom panel: controls + log
        JPanel bottomPanel = new JPanel(new BorderLayout(6, 6));
        logArea = new JTextArea(8, 80);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Turn Log / stdout"));

        controlPanel = new JPanel();
        JButton stepBtn = new JButton("Step");
        JButton playBtn = new JButton("Play");
        JButton resetBtn = new JButton("Reset");
        JButton loadInitBtn = new JButton("Load Initial");
        JButton saveInitBtn = new JButton("Save Initial");
        JButton saveCurBtn = new JButton("Save Current");
        JButton loadSavedBtn = new JButton("Load Saved"); // Step 10 challenge

        controlPanel.add(stepBtn);
        controlPanel.add(playBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(loadInitBtn);
        controlPanel.add(saveInitBtn);
        controlPanel.add(saveCurBtn);
        controlPanel.add(loadSavedBtn);

        bottomPanel.add(controlPanel, BorderLayout.NORTH);
        bottomPanel.add(logScroll, BorderLayout.CENTER);

        // Button Actions
        stepBtn.addActionListener(e -> doStep());
        playBtn.addActionListener(e -> {
            if (!isPlaying) { startPlaying(); playBtn.setText("Pause"); }
            else { stopPlaying(); playBtn.setText("Play"); }
        });
        resetBtn.addActionListener(e -> {
            world.resetToInitialState();
            appendLog("World reset to initial state.");
            refreshGrid();
        });
        loadInitBtn.addActionListener(e -> doLoadInitialFile());
        saveInitBtn.addActionListener(e -> doSaveInitialFile());
        saveCurBtn.addActionListener(e -> doSaveCurrentFile());
        loadSavedBtn.addActionListener(e -> doLoadSavedFile()); // Fixed

        playTimer = new Timer(DEFAULT_PLAY_INTERVAL_MS, e -> doStep());

        // Add panels
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(sidebarPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Redirect stdout/stderr to log
        redirectSystemStreamsToLog();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initGridFromWorld() {
        int w = world.getWidth();
        int h = world.getHeight();
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(h, w, 1, 1));
        cellButtons = new JButton[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(22, 22));
                btn.setOpaque(true);
                btn.setBorderPainted(true);
                final int fx = x, fy = y;
                btn.addActionListener(e -> onCellClicked(fx, fy));
                cellButtons[y][x] = btn;
                gridPanel.add(btn);
            }
        }
        refreshGrid();
    }

    private void onCellClicked(int x, int y) {
        Cell c = world.getCell(x, y);
        if (c == null) {
            sidebarArea.setText("Cell (" + x + "," + y + ") = null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cell (").append(x).append(",").append(y).append(")\n\n");
        sb.append(c.getInfo()).append("\n");

        List<Creature> creatures = c.getCreatures();
        if (!creatures.isEmpty()) {
            sb.append("Creatures:\n");
            for (Creature cr : creatures) {
                sb.append("- ").append(cr.getInfo()).append("\n");
            }
        } else sb.append("No creatures in this cell.\n");

        sidebarArea.setText(sb.toString());
    }

    private void refreshGrid() {
        int w = world.getWidth();
        int h = world.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                JButton b = cellButtons[y][x];
                Cell c = world.getCell(x, y);
                if (c == null) { b.setBackground(Color.LIGHT_GRAY); b.setText(""); }
                else {
                    if (!c.getCreatures().isEmpty()) {
                        b.setBackground(Color.ORANGE);
                        b.setText(String.valueOf(c.getCreatures().size()));
                    } else {
                        b.setBackground(c.getColor());
                        b.setText("");
                    }
                }
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void doStep() {
        world.takeTurn();
        appendLog("Turn executed.");
        refreshGrid();
    }

    private void startPlaying() { isPlaying = true; playTimer.start(); appendLog("Auto-play started."); }
    private void stopPlaying() { isPlaying = false; playTimer.stop(); appendLog("Auto-play stopped."); }

    private void doLoadInitialFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            world.loadInitialState(chooser.getSelectedFile());
            appendLog("Loaded initial state.");
            refreshGrid();
        }
    }

    private void doSaveInitialFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            world.saveInitialState(chooser.getSelectedFile());
            appendLog("Saved initial state.");
        }
    }

    private void doSaveCurrentFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            world.saveCurrentState(chooser.getSelectedFile());
            appendLog("Saved current state.");
        }
    }

    private void doLoadSavedFile() { // Fixed to use loadInitialState
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            world.loadInitialState(chooser.getSelectedFile());
            appendLog("Loaded saved state (actually initial state).");
            refreshGrid();
        }
    }

    private void appendLog(String s) {
        logArea.append(s + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
        System.out.println(s);
    }

    private void redirectSystemStreamsToLog() {
        OutputStream out = new OutputStream() {
            private final StringBuilder buffer = new StringBuilder();
            @Override
            public void write(int b) {
                if (b == '\r') return;
                if (b == '\n') {
                    final String text = buffer.toString();
                    SwingUtilities.invokeLater(() -> appendLog("[stdout] " + text));
                    buffer.setLength(0);
                } else buffer.append((char)b);
            }
        };
        PrintStream ps = new PrintStream(out, true);
        System.setOut(ps);
        System.setErr(ps);
    }

    public static void main(String[] args) {
        World w = new World(10,10);
        w.getCell(2,3).addCreature(new Creature("Rabbit",10,5));
        w.getCell(5,5).addCreature(new Creature("Fox",15,10));
        new EcosystemGUI(w);
    }
}