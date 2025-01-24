package com.task3.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private static final String SERVER_ADDRESS = "localhost"; // Server's address
    private static final int SERVER_PORT = 12345; // Port number the server is listening on

    // GUI components
    private JFrame frame; // Main application window
    private JTextArea chatArea; // Displays chat messages
    private JTextField messageField; // Input field for typing messages
    private JLabel typingLabel; // Shows "typing..." status
    private JButton sendButton; // Button to send messages
    private PrintWriter out; // Sends data to the server
    private BufferedReader in; // Receives data from the server
    private String username; // The username of the logged-in user
    private boolean isTyping = false; // Tracks if the user is currently typing
    private Timer typingTimer; // Timer to manage typing indicator

    // Commands for communication with the server
    private static final String SIGNUP = "SIGNUP";
    private static final String SIGNIN = "SIGNIN";
    private static final String TYPING_MESSAGE = "TYPING";
    private static final String STOP_TYPING_MESSAGE = "STOP_TYPING";

    public static void main(String[] args) {
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatClientGUI().startChat(); // Initialize and start the chat client
            } catch (IOException e) {
                e.printStackTrace(); // Log any errors during startup
            }
        });
    }

    public ChatClientGUI() {
        // Initialize GUI components
        frame = new JFrame("Chat Client");
        chatArea = new JTextArea(20, 50); // Chat display area
        chatArea.setEditable(false); // Prevent user from editing the chat area
        messageField = new JTextField(50); // Message input field
        typingLabel = new JLabel(); // Label to show typing status
        sendButton = new JButton("Send"); // Button to send messages

        // Panel for the chat area and typing label
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER); // Scrollable chat area
        panel.add(typingLabel, BorderLayout.NORTH); // Typing label at the top

        // Panel for message input and send button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER); // Message input field in the center
        bottomPanel.add(sendButton, BorderLayout.EAST); // Send button on the right

        // Add components to the main frame
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        frame.pack(); // Adjust size to fit components
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false); // Initially hidden until login/signup is successful
    }

    public void startChat() throws IOException {
        // Establish connection with the server
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream from server
        out = new PrintWriter(socket.getOutputStream(), true); // Output stream to server

        // Handle user authentication (signup/signin)
        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to Sign-In or Sign-Up?", // Prompt message
                    "Login Options", // Window title
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Sign-In", "Sign-Up"}, // Options
                    "Sign-In" // Default selection
            );

            if (choice == 0) { // User selected "Sign-In"
                if (handleSignin()) {
                    frame.setVisible(true); // Show chat window on successful signin
                    break;
                }
            } else if (choice == 1) { // User selected "Sign-Up"
                if (handleSignup()) {
                    JOptionPane.showMessageDialog(null, "Signup successful! Please sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        listenForMessages(); // Start listening for messages from the server

        // Add typing indicator functionality
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String currentText = messageField.getText().trim();
                if (!currentText.isEmpty() && !isTyping) {
                    sendTypingIndicator(); // Notify server that user is typing
                } else if (currentText.isEmpty() && isTyping) {
                    stopTypingIndicator(); // Notify server that user stopped typing
                }
            }
        });

        messageField.addActionListener(e -> sendMessage()); // Send message on pressing Enter
        sendButton.addActionListener(e -> sendMessage()); // Send message on button click

        typingTimer = new Timer(1000, e -> stopTypingIndicator()); // Timer to stop typing indicator after inactivity
        typingTimer.setRepeats(false);
    }

    private boolean handleSignup() {
        // Gather user details for signup
        String username = JOptionPane.showInputDialog(frame, "Enter a unique username:");
        String password = JOptionPane.showInputDialog(frame, "Enter a password:");
        String mobileNumber;

        while (true) {
            mobileNumber = JOptionPane.showInputDialog(frame, "Enter a unique 10-digit mobile number:");
            if (mobileNumber != null && mobileNumber.matches("\\d{10}")) {
                break; // Valid mobile number
            } else {
                JOptionPane.showMessageDialog(frame, "Mobile number must be exactly 10 digits.", "Invalid Mobile Number", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Send signup details to the server
        out.println(SIGNUP + ":" + username + ":" + password + ":" + mobileNumber);
        try {
            String response = in.readLine();
            if ("Signup successful. Please sign in to join the chat.".equals(response)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(frame, response, "Signup Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean handleSignin() {
        // Gather user credentials for signin
        String username = JOptionPane.showInputDialog(frame, "Enter your username:");
        String password = JOptionPane.showInputDialog(frame, "Enter your password:");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Both fields are required.", "Signin Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Send signin details to the server
        out.println(SIGNIN + ":" + username + ":" + password);
        try {
            String response = in.readLine();
            if ("Signin successful".equals(response)) {
                this.username = username; // Save username for future use
                return true;
            } else {
                JOptionPane.showMessageDialog(frame, response, "Signin Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void listenForMessages() {
        // Continuously listen for messages from the server in a separate thread
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.contains("is typing...")) {
                        typingLabel.setText(message); // Update typing status
                    } else if (message.isEmpty()) {
                        typingLabel.setText(""); // Clear typing status
                    } else {
                        chatArea.append(message + "\n"); // Display the message
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Connection lost. Please restart the application.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void sendMessage() {
        // Send the message entered by the user
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append("You: " + message + "\n"); // Display locally
            out.println(message); // Send to server
            messageField.setText(""); // Clear input field
            stopTypingIndicator(); // Stop typing indicator
        }
    }

    private void sendTypingIndicator() {
        if (!isTyping) {
            out.println(TYPING_MESSAGE); // Notify server
            isTyping = true;
        }
        typingTimer.restart(); // Reset typing timer
    }

    private void stopTypingIndicator() {
        if (isTyping) {
            out.println(STOP_TYPING_MESSAGE); // Notify server
            isTyping = false;
        }
    }
}
