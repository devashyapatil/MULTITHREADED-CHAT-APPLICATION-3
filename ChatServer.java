package com.task3.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345; // Port on which the server will listen for connections
    private static Map<String, User> userDatabase = new HashMap<>(); // Stores registered user data
    private static Set<PrintWriter> clientWriters = new HashSet<>(); // Stores active client connections

    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Continuously listen for client connections
            while (true) {
                new ClientHandler(serverSocket.accept()).start(); // Handle each client in a new thread
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error details if the server fails
        }
    }

    // User class to store user information like username, password, and mobile number
    private static class User {
        String username;
        String password;
        String mobileNumber;

        User(String username, String password, String mobileNumber) {
            this.username = username;
            this.password = password;
            this.mobileNumber = mobileNumber;
        }
    }

    // ClientHandler class to manage interactions with a single client
    private static class ClientHandler extends Thread {
        private Socket socket; // Socket for communication with the client
        private PrintWriter out; // Output stream to send data to the client
        private BufferedReader in; // Input stream to receive data from the client
        private String username; // Stores the username of the connected client

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Set up input and output streams for communication
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    // Read input from the client
                    String input = in.readLine();
                    if (input == null) {
                        return; // If input is null, client has disconnected
                    }

                    // Split the input to identify the command and arguments
                    String[] parts = input.split(":");
                    String command = parts[0];

                    // Handle the command received from the client
                    switch (command) {
                        case "SIGNUP":
                            handleSignup(parts);
                            break;
                        case "SIGNIN":
                            handleSignin(parts);
                            break;
                        case "TYPING":
                            broadcast(username + " is typing...", out); // Notify others that this user is typing
                            break;
                        case "STOP_TYPING":
                            broadcast("", out); // Clear typing notifications
                            break;
                        default:
                            // Broadcast chat messages to all clients
                            broadcast(username + ": " + input, out);
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                // Handle client disconnection
                if (username != null) {
                    broadcast(username + " has left the chat.", out); // Notify others of the disconnection
                    synchronized (clientWriters) {
                        clientWriters.remove(out); // Remove the client's writer from the active list
                    }
                    System.out.println(username + " has disconnected.");
                }
                try {
                    socket.close(); // Close the socket connection
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Current number of connected clients: " + clientWriters.size());
            }
        }

        // Handle the signup process for a new user
        private void handleSignup(String[] parts) {
            if (parts.length < 4) {
                out.println("Invalid signup data."); // Inform the client of invalid input
                System.out.println("Signup attempt failed: Invalid data.");
                return;
            }

            String username = parts[1];
            String password = parts[2];
            String mobileNumber = parts[3];

            synchronized (userDatabase) {
                // Check if the username already exists
                if (userDatabase.containsKey(username)) {
                    out.println("Username already exists. Choose a different username.");
                    System.out.println("Signup attempt failed: Username already exists (" + username + ").");
                    return;
                }

                // Check if the mobile number is already registered
                for (User user : userDatabase.values()) {
                    if (user.mobileNumber.equals(mobileNumber)) {
                        out.println("Mobile number is already registered. Use a different number.");
                        System.out.println("Signup attempt failed: Mobile number already registered (" + mobileNumber + ").");
                        return;
                    }
                }

                // Register the new user
                userDatabase.put(username, new User(username, password, mobileNumber));
                out.println("Signup successful. Please sign in to join the chat.");
                System.out.println("Signup successful: " + username + " registered.");
            }
        }

        // Handle the signin process for an existing user
        private void handleSignin(String[] parts) {
            if (parts.length < 3) {
                out.println("Invalid signin data."); // Inform the client of invalid input
                System.out.println("Signin attempt failed: Invalid data.");
                return;
            }

            String username = parts[1];
            String password = parts[2];

            synchronized (userDatabase) {
                // Check if the user exists in the database
                User user = userDatabase.get(username);
                if (user == null) {
                    out.println("Account not found");
                    System.out.println("Signin attempt failed: Account not found for " + username);
                } else if (!user.password.equals(password)) {
                    // Check if the password matches
                    out.println("Incorrect password. Please try again.");
                    System.out.println("Signin attempt failed: Incorrect password for " + username);
                } else {
                    // Successful signin
                    this.username = username;
                    out.println("Signin successful");
                    synchronized (clientWriters) {
                        clientWriters.add(out); // Add the client to the active clients list
                    }
                    System.out.println("Signin successful: " + username);
                    broadcast(username + " has joined the chat.", out); // Notify others that this user joined
                }
            }
        }

        // Broadcast a message to all connected clients
        private void broadcast(String message, PrintWriter excludeWriter) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    if (writer != excludeWriter) {
                        writer.println(message); // Send the message to all clients except the sender
                    }
                }
            }
            System.out.println("Chat Message: " + message); // Log the message on the server
        }
    }
}
