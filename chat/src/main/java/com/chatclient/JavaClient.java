package com.chatclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class JavaClient extends WebSocketServer {
    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DataInputStream receiver;
    private CopyOnWriteArraySet<WebSocket> connections;

    public JavaClient(InetSocketAddress address) throws IOException {
        super(address);
        this.serverSocket = new Socket("10.15.7.64", 4005);
        this.out = new PrintWriter(serverSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        this.receiver = new DataInputStream(serverSocket.getInputStream());
        this.connections = new CopyOnWriteArraySet<>();

        new Thread(this::readFromSocketAndBroadcast).start();
    }

    private void readFromSocketAndBroadcast() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String broadcastMessage = message;
                broadcast(broadcastMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket conn, int arg1, String arg2, boolean arg3) {
        System.out.println("Closed connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.remove(conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error during Websocket connection: "
                + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ":" + ex);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from Websocket: " + message);
        out.println(message);
        try {
            String response = in.readLine();
            conn.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake arg1) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        connections.add(conn);
    }

    @Override
    public void onStart() {
        System.out.println("Websocket server running...");
    }

    public void broadcast(String message) {
        for (WebSocket conn : connections) {
            conn.send(message);
        }
    }
}