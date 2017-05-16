package com.systemofmonitoring;

import com.systemofmonitoring.workwithdb.DataEntryForDayElectricMeter;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        DataEntryForDayElectricMeter dataEntryForDayElectricMeter =
                new DataEntryForDayElectricMeter();
        try {
            dataEntryForDayElectricMeter.DisplaceDatasForDay();
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