package com.systemofmonitoring;

import com.sun.deploy.ui.DialogTemplate;
import com.systemofmonitoring.connecttodb.ConnectToDB;
import com.systemofmonitoring.displacedatas.*;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private DisplaceDatas displaceDatas =
            new DisplaceDatas();
    private ConnectToDB connectToDB =
            new ConnectToDB();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            //displaceDatas.Displace();
            //connectToDB.Connect();
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