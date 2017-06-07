package com.systemofmonitoring;

import com.systemofmonitoring.connecttodb.ConnectToElectricMeterDB;
import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import org.json.JSONException;
import org.json.JSONObject;

import javax.sound.midi.Soundbank;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ClientHandler extends Thread {
    private DataOutputStream sOut;
    private DataInputStream sIn;
    private Socket client;
    private String tableName, interval, date, action, meterName, resourceName;
    private JSONObject jsonObjectQuery, jsonObjectResult, dataForInsert;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            sIn = new DataInputStream(client.getInputStream());
            sOut = new DataOutputStream(client.getOutputStream());
            jsonObjectQuery = new JSONObject(sIn.readUTF());
            if (jsonObjectQuery.has("action")) {
                parseJSONForAction(jsonObjectQuery);
                if (action.equals("get meters")) {
                    System.out.println(action);
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getMetersNames();
                }
                else if (action.equals("get resources")) {
                    System.out.println(action);
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getResourcesTypes();
                }
                else if (action.equals("insert")) {
                    System.out.println(action);
                    jsonObjectResult =
                            new ConnectToPostgreSQL().insertInDatabase(meterName, dataForInsert);
                }
            }
            else {
                parseJSONForDatas(jsonObjectQuery);
                System.out.println(date);
                if (!jsonObjectQuery.has("date")) {
                    if (jsonObjectQuery.has("table"))
                        jsonObjectResult =
                                new ConnectToPostgreSQL().getResultFromMeter(tableName, interval);
                    else
                        jsonObjectResult =
                                new ConnectToPostgreSQL().getResultForResource(resourceName, interval);
                } else {
                    if (jsonObjectQuery.has("table"))
                        jsonObjectResult =
                                new ConnectToPostgreSQL().getResultFromMeter(tableName, interval, date);
                    else
                        jsonObjectResult =
                                new ConnectToPostgreSQL().getResultForResource(resourceName, interval, date);
                }
            }
            sOut.writeUTF(jsonObjectResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONForDatas(JSONObject jsonObject) throws JSONException {
        try {
            if (jsonObject.has("table"))
                this.tableName = jsonObject.getString("table");
            else
                this.resourceName = jsonObject.getString("resource");
            this.interval = jsonObject.getString("interval");
            if (jsonObject.has("date"))
                this.date = jsonObject.getString("date");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        System.out.println(tableName + " " + interval + " " + date);
    }

    private void parseJSONForAction(JSONObject jsonObject) throws JSONException {
        try {
            action = jsonObject.getString("action");
            if (jsonObject.has("meter"))
                meterName = jsonObject.getString("meter");
            if (jsonObject.has("data"))
                dataForInsert = jsonObject.getJSONObject("data");
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
