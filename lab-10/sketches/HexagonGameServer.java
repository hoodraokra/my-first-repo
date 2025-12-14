import java.io.*;
import java.net.*;
import java.util.*;

public class HexagonGameServer {
    private static final int PORT = 55555;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static int currentTurn = 1; // 1 or 2

    public static void main(String[] args) {
        System.out.println("Hexagon Game Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Accept exactly two clients.
            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clients.size() + 1);
                clients.add(handler);
                new Thread(handler).start();
                System.out.println("Player " + handler.playerNumber + " connected.");
            }
            // Inform each client of their player number.
            broadcast("START 1");
            broadcast("START 2");
            // Inform both clients whose turn it is.
            broadcast("TURN " + currentTurn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send a message to all connected clients.
    public static synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Process a move sent by a client.
    public static synchronized void processMove(String moveMessage, int senderPlayer) {
        // Only accept a move if it comes from the player whose turn it is.
        if (senderPlayer != currentTurn) {
            System.out.println("Ignoring move from player " + senderPlayer + " (not their turn).");
            return;
        }
        // Broadcast the move message to both clients.
        broadcast(moveMessage);
        // Switch turns.
        currentTurn = (currentTurn == 1) ? 2 : 1;
        broadcast("TURN " + currentTurn);
    }

    static class ClientHandler implements Runnable {
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        int playerNumber;

        public ClientHandler(Socket socket, int playerNumber) {
            this.socket = socket;
            this.playerNumber = playerNumber;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received from player " + playerNumber + ": " + line);
                    if (line.startsWith("MOVE")) {
                        processMove(line, playerNumber);
                    }
                    // (Additional message types could be handled here.)
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch(IOException e) { }
            }
        }
    }
}

