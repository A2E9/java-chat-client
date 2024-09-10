package com.chatclient;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        int port = 8001;
        try {
            JavaClient client = new JavaClient(new InetSocketAddress(port));
            client.start();
            System.out.println("Client started on port: " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
