import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class ChatServer extends JFrame {
    private JTextField portField;
    private JTextArea logArea;
    private JButton startButton, stopButton;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private boolean isRunning = false;

    public ChatServer() {
        setTitle("Chat Server");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        topPanel.add(new JLabel("Server Port:"));
        portField = new JTextField("12345");
        topPanel.add(portField);
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        topPanel.add(startButton);
        topPanel.add(stopButton);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
    }

    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText());
            serverSocket = new ServerSocket(port);
            isRunning = true;
            appendToLog("Server started on port " + port);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            new Thread(new ConnectionAcceptor()).start();

        } catch (Exception e) {
            appendToLog("Error starting server: " + e.getMessage());
        }
    }

    private void stopServer() {
        try {
            isRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler client : clients) {
                client.close();
            }
            clients.clear();
            appendToLog("Server stopped");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        } catch (IOException e) {
            appendToLog("Error stopping server: " + e.getMessage());
        }
    }

    private class ConnectionAcceptor implements Runnable {
        public void run() {
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                    appendToLog("New client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    if (isRunning) {
                        appendToLog("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientAddress;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.clientAddress = socket.getInetAddress().getHostAddress();
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                appendToLog("Error setting up client handler: " + e.getMessage());
            }
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message;
                    SwingUtilities.invokeLater(() -> {
                        broadcastMessage(finalMessage);
                        appendToLog("Message received: " + finalMessage);
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    appendToLog("Benutzer hat sich abgemeldet: " + clientAddress);
                    broadcastMessage("Benutzer hat sich abgemeldet: " + clientAddress);
                });
            } finally {
                close();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void close() {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                appendToLog("Error closing client connection: " + e.getMessage());
            }
            clients.remove(this);
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private void appendToLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatServer().setVisible(true));
    }
}
