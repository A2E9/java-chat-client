package com.chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;
import org.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.logging.Logger;
import java.util.logging.Level;

public class JavaClient extends WebSocketServer {
    private static final Logger logger = Logger.getLogger(JavaClient.class.getName());
    private Socket serverSocket;
    private PrintWriter outputServer;
    private BufferedReader inputServer;
    private CopyOnWriteArraySet<WebSocket> connections;

    public JavaClient(InetSocketAddress address) {
        super(address);
        this.connections = new CopyOnWriteArraySet<>();
    }

    private void initializeServerConnection() {
        try {
            this.serverSocket = new Socket("localhost", 55555);
            this.outputServer = new PrintWriter(serverSocket.getOutputStream(), true);
            this.inputServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            new Thread(this::readFromSocketAndBroadcast).start();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize server connection", e);
        }
    }

    private void readFromSocketAndBroadcast() {
        try {
            String message;
            while ((message = inputServer.readLine()) != null) {
                final String broadcastMessage = message;
                broadcast(broadcastMessage);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading from socket", e);
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info("Closed connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("Received message from WebSocket: " + message);
        JSONObject messageObj = new JSONObject();
        messageObj.put("sender", conn.getRemoteSocketAddress().getAddress().getHostAddress());
        messageObj.put("message", message);
        outputServer.println(message);
        try {
            String response = inputServer.readLine();
            if (response == null)
                response = "";
            messageObj.put("response", response);
            logger.info("Sending response: " + messageObj.toString());
            conn.send(messageObj.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error processing message", e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.log(Level.SEVERE, "Error during WebSocket connection: "
                + (conn != null ? conn.getRemoteSocketAddress().getAddress().getHostAddress() : "unknown"), ex);
    }

    @Override
    public void onStart() {
        logger.info("WebSocket server running...");
        initializeServerConnection();
    }

    public void broadcast(String message) {
        for (WebSocket conn : connections) {
            conn.send(message);
        }
    }

    public void shutdown() throws InterruptedException {
        try {
            super.stop();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (outputServer != null) {
                outputServer.close();
            }
            if (inputServer != null) {
                inputServer.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during shutdown", e);
        }
    }

   
}

// package com.chatclient;

// import java.io.BufferedReader;
// // import java.io.DataInputStream;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.InetSocketAddress;
// import java.net.Socket;
// import java.util.concurrent.CopyOnWriteArraySet;
// import org.json.JSONObject;
// import org.json.JSONArray;
// import org.java_websocket.WebSocket;
// import org.java_websocket.handshake.ClientHandshake;
// import org.java_websocket.server.WebSocketServer;

// public class JavaClient extends WebSocketServer {
//     private Socket serverSocket;
//     private PrintWriter outputServer;
//     private BufferedReader inputServer;
//     private CopyOnWriteArraySet<WebSocket> connections;

//     public JavaClient(InetSocketAddress address) throws IOException {
//         super(address);
//         this.serverSocket = new Socket("localhost", 55555);
//         this.outputServer = new PrintWriter(serverSocket.getOutputStream(), true);
//         this.inputServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
//         this.connections = new CopyOnWriteArraySet<>();

//         new Thread(this::readFromSocketAndBroadcast).start();
//     }

//     private void readFromSocketAndBroadcast() {
//         try {
//             String message;
//             while ((message = inputServer.readLine()) != null) {
//                 final String broadcastMessage = message;
//                 broadcast(broadcastMessage);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public void onClose(WebSocket conn, int arg1, String arg2, boolean arg3) {
//         System.out.println("Closed connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
//         connections.remove(conn);
//     }

//     @Override
//     public void onError(WebSocket conn, Exception ex) {
//         System.err.println("Error during Websocket connection: "
//                 + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":" + ex);
//     }

//     @Override
//     public void onMessage(WebSocket conn, String message) {
//         System.out.println("Received message from Websocket: " + message);
//         JSONObject messageObj = new JSONObject();
//         messageObj.put("sender", conn.getRemoteSocketAddress().getAddress().getHostAddress());
//         messageObj.put("message", message);
//         outputServer.println(message);
//         try {
//             String response = inputServer.readLine();
//             if (response == null)
//                 response = "";
//             messageObj.put("response", response);
//             System.out.println("Sending response: " + messageObj.toString());
//             conn.send(messageObj.toString());
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public void onOpen(WebSocket conn, ClientHandshake arg1) {
//         System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
//         connections.add(conn);
//     }

//     @Override
//     public void onStart() {
//         System.out.println("Websocket server running...");
//     }

//     public void broadcast(String message) {
//         for (WebSocket conn : connections) {
//             conn.send(message);
//         }
//     }
// }