import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private BufferedReader in;
    private PrintWriter out;
    private String userName;
    private Socket socket;
    private Scanner consoleScanner;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.startClient();
    }

    public void startClient() {
        consoleScanner = new Scanner(System.in);
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Handle username submission
            while (true) {
                String line = in.readLine();
                if (line.startsWith("SUBMITNAME")) {
                    System.out.print("Enter your username: ");
                    userName = consoleScanner.nextLine();
                    out.println(userName);
                } else if (line.startsWith("NAME_ACCEPTED")) {
                    this.userName = line.substring(14); // Extract accepted username
                    System.out.println("Welcome, " + userName + "!");
                    break;
                } else if (line.startsWith("NAME_TAKEN")) {
                    System.out.println("Username taken. Please choose another.");
                }
            }

            // Start a separate thread for receiving messages
            new Thread(new IncomingReader()).start();

            // Main thread handles sending messages
            String message;
            System.out.println("You can start chatting. Type /quit to exit.");
            while (true) {
                message = consoleScanner.nextLine();
                if (message.equalsIgnoreCase("/quit")) {
                    out.println(message); // Send quit message to server
                    break;
                }
                out.println(message);
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                if (consoleScanner != null) consoleScanner.close();
            } catch (IOException e) {
                System.err.println("Error closing client resources: " + e.getMessage());
            }
        }
    }

    // Thread to continuously read incoming messages from the server
    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            } finally {
                System.out.println("Disconnected from server.");
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close(); // Ensure socket is closed on disconnect
                    }
                } catch (IOException e) {
                    System.err.println("Error closing socket in IncomingReader: " + e.getMessage());
                }
            }
        }
    }
}
