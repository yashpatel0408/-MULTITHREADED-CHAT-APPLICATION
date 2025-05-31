import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clientWriters = new ConcurrentHashMap<>();
    private static Set<String> userNames = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to broadcast messages to all connected clients
    public static void broadcastMessage(String sender, String message) {
        System.out.println("Broadcasting from " + sender + ": " + message);
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String userName;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true); // true for auto-flush

                // Request a unique username from the client
                while (true) {
                    out.println("SUBMITNAME"); // Signal client to send username
                    userName = in.readLine();
                    if (userName == null) {
                        return; // Client disconnected or error
                    }
                    synchronized (userNames) {
                        if (!userName.isEmpty() && !userNames.contains(userName)) {
                            userNames.add(userName);
                            break;
                        } else {
                            out.println("NAME_TAKEN"); // Signal client that name is taken
                        }
                    }
                }

                System.out.println(userName + " has connected.");
                out.println("NAME_ACCEPTED " + userName);
                clientWriters.put(userName, out);
                broadcastMessage("SERVER", userName + " has joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/quit")) {
                        break; // Client wants to quit
                    }
                    broadcastMessage(userName, userName + ": " + message);
                }

            } catch (IOException e) {
                System.err.println("Error handling client " + userName + ": " + e.getMessage());
                // e.printStackTrace(); // Uncomment for detailed error debugging
            } finally {
                if (userName != null) {
                    System.out.println(userName + " has disconnected.");
                    userNames.remove(userName);
                    clientWriters.remove(userName);
                    broadcastMessage("SERVER", userName + " has left the chat.");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket for " + userName + ": " + e.getMessage());
                }
            }
        }
    }
}
