package com.chatclient;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.logging.Logger;
import java.util.logging.Level;

public class App {
    public static void main(String[] args) throws InterruptedException {
        final Logger logger = Logger.getLogger(JavaClient.class.getName());
        int port = 8001;
        JavaClient server = null;
        try {
            server = new JavaClient(new InetSocketAddress(port));
            server.start();
            logger.info("WebSocket server started on port: " + port);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start server", e);
            if (server != null) {
                server.shutdown();
            }
        }
    }
}
