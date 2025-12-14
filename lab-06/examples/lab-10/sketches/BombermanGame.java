import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

//---------------------------------------------------------
// OptionsScreen: Let the user choose Local or Network play.
class OptionsScreen extends JFrame {
    private JRadioButton localButton;
    private JRadioButton networkButton;
    private JRadioButton hostButton;
    private JRadioButton joinButton;
    private JTextField ipField;
    private JButton startButton;
    
    public OptionsScreen() {
        super("Bomberman Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Mode selection: Local or Network.
        localButton = new JRadioButton("Local (Two-player on one PC)");
        networkButton = new JRadioButton("Network (LAN play)");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(localButton);
        modeGroup.add(networkButton);
        localButton.setSelected(true);
        panel.add(localButton);
        panel.add(networkButton);
        
        // For network: choose host or join.
        hostButton = new JRadioButton("Host");
        joinButton = new JRadioButton("Join");
        ButtonGroup netGroup = new ButtonGroup();
        netGroup.add(hostButton);
        netGroup.add(joinButton);
        hostButton.setSelected(true);
        JPanel netPanel = new JPanel();
        netPanel.add(hostButton);
        netPanel.add(joinButton);
        panel.add(netPanel);
        
        // For join: IP address.
        JPanel ipPanel = new JPanel();
        ipPanel.add(new JLabel("Host IP:"));
        ipField = new JTextField("localhost", 15);
        ipPanel.add(ipField);
        panel.add(ipPanel);
        
        // Start button.
        startButton = new JButton("Start Game");
        panel.add(startButton);
        
        add(panel);
        
        // Enable or disable network options based on mode.
        networkButton.addActionListener(e -> {
            hostButton.setEnabled(true);
            joinButton.setEnabled(true);
            ipField.setEnabled(joinButton.isSelected());
        });
        localButton.addActionListener(e -> {
            hostButton.setEnabled(false);
            joinButton.setEnabled(false);
            ipField.setEnabled(false);
        });
        joinButton.addActionListener(e -> ipField.setEnabled(true));
        hostButton.addActionListener(e -> ipField.setEnabled(false));
        
        // Start game on button press.
        startButton.addActionListener(e -> {
            boolean isLocal = localButton.isSelected();
            boolean isHost = hostButton.isSelected();
            String ipAddress = ipField.getText().trim();
            // Launch the game.
            SwingUtilities.invokeLater(() -> {
                JFrame gameFrame = new JFrame("Bomberman Clone");
                // BombermanGame constructor parameters:
                //   isLocalMode, isHost, ipAddress.
                BombermanGame gamePanel = new BombermanGame(isLocal, isHost, ipAddress);
                gameFrame.add(gamePanel);
                gameFrame.pack();
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
            });
            dispose();
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OptionsScreen().setVisible(true);
        });
    }
}

//---------------------------------------------------------
// BombermanGame: The main game panel.
class BombermanGame extends JPanel implements ActionListener, KeyListener {
    public final int CELL_SIZE = 32;
    public final int ROWS = 15;
    public final int COLS = 15;
    private Timer timer;
    
    // Game objects.
    Player localPlayer, remotePlayer;
    List<Bomb> bombs;
    Block[][] blocks;
    Random rand = new Random();
    
    // Networking and mode.
    NetworkManager networkManager; // Only used in network mode.
    boolean isLocalMode;
    boolean isHost; // Only relevant when not local.
    boolean gameOver = false;
    
    public BombermanGame(boolean isLocalMode, boolean isHost, String ipAddress) {
        this.isLocalMode = isLocalMode;
        this.isHost = isHost;
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        setFocusable(true);
        addKeyListener(this);
        
        bombs = new CopyOnWriteArrayList<>();
        initBlocks();
        
        // Initialize players.
        if (isLocalMode) {
            // In local mode, both players are on one machine.
            localPlayer = new Player(1, 1, 1, Color.BLUE);
            remotePlayer = new Player(2, COLS - 2, ROWS - 2, Color.RED);
        } else {
            // In network mode, assign based on host status.
            if (isHost) {
                localPlayer = new Player(1, 1, 1, Color.BLUE);
                remotePlayer = new Player(2, COLS - 2, ROWS - 2, Color.RED);
            } else {
                localPlayer = new Player(2, COLS - 2, ROWS - 2, Color.RED);
                remotePlayer = new Player(1, 1, 1, Color.BLUE);
            }
            networkManager = new NetworkManager(isHost, ipAddress, this);
        }
        
        // Start game loop (about 60 FPS).
        timer = new Timer(1000 / 60, this);
        timer.start();
    }
    
    // Initialize the grid blocks.
    private void initBlocks() {
        blocks = new Block[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Border: indestructible.
                if (i == 0 || i == ROWS - 1 || j == 0 || j == COLS - 1) {
                    blocks[i][j] = new Block(j, i, false);
                }
                // Internal pattern: even cells indestructible.
                else if (i % 2 == 0 && j % 2 == 0) {
                    blocks[i][j] = new Block(j, i, false);
                }
                else {
                    // Keep starting zones clear.
                    if ((i <= 2 && j <= 2) || (i >= ROWS - 3 && j >= COLS - 3)) {
                        blocks[i][j] = null;
                    } else {
                        // 70% chance for a destructible block.
                        if (rand.nextDouble() < 0.7) {
                            blocks[i][j] = new Block(j, i, true);
                        } else {
                            blocks[i][j] = null;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        for (Bomb bomb : bombs) {
            bomb.update(this);
        }
        // Remove bombs that have finished explosion effects.
        bombs.removeIf(Bomb::isFinished);
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw grid background.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.WHITE);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        // Draw blocks.
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (blocks[i][j] != null) {
                    blocks[i][j].draw(g, CELL_SIZE);
                }
            }
        }
        // Draw players.
        localPlayer.draw(g, CELL_SIZE);
        remotePlayer.draw(g, CELL_SIZE);
        // Draw bombs and explosions.
        for (Bomb bomb : bombs) {
            bomb.draw(g, CELL_SIZE);
        }
    }
    
    // Key handling.
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;
        int key = e.getKeyCode();
        
        // In local mode, process two players' controls.
        if (isLocalMode) {
            // Player 1 (blue) uses arrow keys and SPACE for bomb.
            if (key == KeyEvent.VK_UP) {
                localPlayer.move(0, -1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_DOWN) {
                localPlayer.move(0, 1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_LEFT) {
                localPlayer.move(-1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_RIGHT) {
                localPlayer.move(1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_SPACE) {
                Bomb bomb = new Bomb(localPlayer.x, localPlayer.y);
                bombs.add(bomb);
            }
            // Player 2 (red) uses WASD and F for bomb.
            if (key == KeyEvent.VK_W) {
                remotePlayer.move(0, -1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_S) {
                remotePlayer.move(0, 1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_A) {
                remotePlayer.move(-1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_D) {
                remotePlayer.move(1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_F) {
                Bomb bomb = new Bomb(remotePlayer.x, remotePlayer.y);
                bombs.add(bomb);
            }
        } else {
            // In network mode, only the local player is controlled.
            if (key == KeyEvent.VK_UP) {
                localPlayer.move(0, -1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_DOWN) {
                localPlayer.move(0, 1, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_LEFT) {
                localPlayer.move(-1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_RIGHT) {
                localPlayer.move(1, 0, COLS, ROWS, blocks);
            } else if (key == KeyEvent.VK_SPACE) {
                Bomb bomb = new Bomb(localPlayer.x, localPlayer.y);
                bombs.add(bomb);
                networkManager.sendMessage("BOMB," + localPlayer.id + "," + localPlayer.x + "," + localPlayer.y);
            }
            // Send move update if movement occurred.
            networkManager.sendMessage("MOVE," + localPlayer.id + "," + localPlayer.x + "," + localPlayer.y);
        }
    }
    
    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }
    
    // Called by NetworkManager when a message is received.
    public void processNetworkMessage(String message) {
        // Message format: COMMAND,playerId,x,y
        String[] parts = message.split(",");
        if (parts.length < 4) {
            // For messages like DEAD, only 2 parts.
            if (parts[0].equals("DEAD") && parts.length >= 2) {
                int pid = Integer.parseInt(parts[1]);
                if (pid == remotePlayer.id) {
                    // Remote player died.
                    JOptionPane.showMessageDialog(this, "You win!");
                    endGame();
                } else if (pid == localPlayer.id) {
                    JOptionPane.showMessageDialog(this, "You lose!");
                    endGame();
                }
            }
            return;
        }
        String command = parts[0];
        int playerId = Integer.parseInt(parts[1]);
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[3]);
        
        if (command.equals("MOVE")) {
            if (playerId == remotePlayer.id) {
                remotePlayer.x = x;
                remotePlayer.y = y;
            }
        } else if (command.equals("BOMB")) {
            if (playerId == remotePlayer.id) {
                Bomb bomb = new Bomb(x, y);
                bombs.add(bomb);
            }
        }
    }
    
    // Called by bombs when they explode.
    public void handleExplosion(Bomb bomb, List<Point> blastCells) {
        // Ignite bombs in the explosion's path.
        igniteBombs(blastCells, bomb);
        // Check if players are caught in the blast.
        for (Point p : blastCells) {
            if (localPlayer.isAlive() && localPlayer.x == p.x && localPlayer.y == p.y) {
                playerHit(localPlayer);
            }
            if (remotePlayer.isAlive() && remotePlayer.x == p.x && remotePlayer.y == p.y) {
                playerHit(remotePlayer);
            }
        }
    }
    
    // Chain reaction: ignite any bomb in the blast area (except the triggering bomb).
    public void igniteBombs(List<Point> blastCells, Bomb triggeringBomb) {
        for (Bomb b : bombs) {
            if (!b.exploded && b != triggeringBomb) {
                for (Point p : blastCells) {
                    if (b.x == p.x && b.y == p.y) {
                        b.ignite();
                        break;
                    }
                }
            }
        }
    }
    
    // Called when a player is hit by an explosion.
    public void playerHit(Player player) {
        if (!player.alive) return;
        player.alive = false;
        if (!isLocalMode && player == localPlayer) {
            networkManager.sendMessage("DEAD," + localPlayer.id);
        }
        endGame();
    }
    
    // Ends the game and displays the result.
    public void endGame() {
        if (gameOver) return;
        gameOver = true;
        timer.stop();
        String msg;
        if (isLocalMode) {
            if (localPlayer.alive && !remotePlayer.alive)
                msg = "Player 1 wins!";
            else if (!localPlayer.alive && remotePlayer.alive)
                msg = "Player 2 wins!";
            else
                msg = "Draw!";
        } else {
            if (localPlayer.alive && !remotePlayer.alive)
                msg = "You win!";
            else if (!localPlayer.alive && remotePlayer.alive)
                msg = "You lose!";
            else
                msg = "Draw!";
        }
        JOptionPane.showMessageDialog(this, msg);
    }
    
    public Block[][] getBlocks() {
        return blocks;
    }
}

//---------------------------------------------------------
// Player class.
class Player {
    int id, x, y;
    Color color;
    boolean alive = true;
    
    public Player(int id, int x, int y, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    // Move if target cell is within bounds and not blocked.
    public void move(int dx, int dy, int maxX, int maxY, Block[][] blocks) {
        int newX = x + dx;
        int newY = y + dy;
        if (newX >= 0 && newX < maxX && newY >= 0 && newY < maxY) {
            // Only move if cell is empty or the block there is destroyed.
            if (blocks[newY][newX] == null || blocks[newY][newX].isDestroyed()) {
                x = newX;
                y = newY;
            }
        }
    }

		public boolean isAlive() {
      return alive;
    }

    
    public void draw(Graphics g, int cellSize) {
        if (!alive) return; // Optionally, skip drawing if dead.
        g.setColor(color);
        g.fillOval(x * cellSize, y * cellSize, cellSize, cellSize);
    }
}

//---------------------------------------------------------
// Bomb class with explosion that can trigger other bombs and kill players.
class Bomb {
    int x, y;
    private int timer = 120;         // Countdown (approx. 2 seconds at 60 FPS).
    private int explosionTimer = 0;  // Duration to show explosion effect.
    private final int blastRange = 3;
    boolean exploded = false;
    private boolean finished = false;
    private List<Point> blastCells;
    
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
        blastCells = new CopyOnWriteArrayList<>();
    }
    
    // Force the bomb to explode (chain reaction).
    public void ignite() {
        if (!exploded) {
            timer = 0;
        }
    }
    
    // Update bomb state.
    public void update(BombermanGame game) {
        if (!exploded) {
            if (timer > 0) {
                timer--;
            } else {
                explode(game);
                explosionTimer = 20; // Explosion effect lasts 20 frames.
            }
        } else {
            if (explosionTimer > 0) {
                explosionTimer--;
            } else {
                finished = true;
            }
        }
    }
    
    // Compute explosion blast, affect blocks, chain-react with bombs, and check players.
    private void explode(BombermanGame game) {
        exploded = true;
        // Explosion covers the bomb's own cell.
        blastCells.add(new Point(x, y));
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        Block[][] blocks = game.getBlocks();
        
        for (int[] dir : directions) {
            for (int i = 1; i <= blastRange; i++) {
                int nx = x + dir[0] * i;
                int ny = y + dir[1] * i;
                if (nx < 0 || nx >= game.COLS || ny < 0 || ny >= game.ROWS)
                    break;
                if (blocks[ny][nx] != null && !blocks[ny][nx].isDestroyed()) {
                    if (blocks[ny][nx].isDestructible()) {
                        blocks[ny][nx].destroy();
                        blastCells.add(new Point(nx, ny));
                    }
                    break;
                } else {
                    blastCells.add(new Point(nx, ny));
                }
            }
        }
        // Let the game handle chain reactions and player hits.
        game.handleExplosion(this, blastCells);
        System.out.println("Bomb exploded at (" + x + "," + y + ")");
    }
    
    public void draw(Graphics g, int cellSize) {
        if (!exploded) {
            g.setColor(Color.BLACK);
            g.fillOval(x * cellSize, y * cellSize, cellSize, cellSize);
        } else {
            g.setColor(Color.ORANGE);
            for (Point p : blastCells) {
                g.fillRect(p.x * cellSize, p.y * cellSize, cellSize, cellSize);
            }
        }
    }
    
    public boolean isFinished() {
        return finished;
    }
}

//---------------------------------------------------------
// Block class.
class Block {
    private int x, y;
    private boolean destructible;
    private boolean destroyed = false;
    
    public Block(int x, int y, boolean destructible) {
        this.x = x;
        this.y = y;
        this.destructible = destructible;
    }
    
    public boolean isDestructible() {
        return destructible;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    public void destroy() {
        if (destructible) {
            destroyed = true;
        }
    }
    
    public void draw(Graphics g, int cellSize) {
        if (!destroyed) {
            if (destructible) {
                g.setColor(new Color(139, 69, 19)); // Brownish.
            } else {
                g.setColor(Color.DARK_GRAY);
            }
            g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
        }
    }
}

//---------------------------------------------------------
// NetworkManager for simple peer-to-peer networking.
class NetworkManager {
    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintWriter out;
    private BombermanGame game;
    
    public NetworkManager(boolean isHost, String ipAddress, BombermanGame game) {
        this.game = game;
        try {
            if (isHost) {
                serverSocket = new ServerSocket(5000);
                System.out.println("Waiting for a client connection...");
                socket = serverSocket.accept();
                System.out.println("Client connected.");
            } else {
                socket = new Socket(ipAddress, 5000);
                System.out.println("Connected to host.");
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this::listen).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(String msg) {
        out.println(msg);
    }
    
    private void listen() {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                game.processNetworkMessage(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

