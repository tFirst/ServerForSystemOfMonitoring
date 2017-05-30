package com.systemofmonitoring;

import com.sun.deploy.ui.DialogTemplate;
import com.systemofmonitoring.displacedatas.*;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            new DisplaceDatas().Displace();
            ServerSocket server = new ServerSocket(3333);
            while (true) {
                Socket client = server.accept();
                new ClientHandler(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}