package com.systemofmonitoring;

import com.systemofmonitoring.connecttodb.ConnectToElectricMeterDB;
import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ClientHandler extends Thread {
    private DataOutputStream sOut;
    private DataInputStream sIn;
    private Socket client;
    private String tableName, interval, date, action, database, tableNameAdmin;
    private JSONObject jsonObjectQuery, jsonObjectResult;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            sIn = new DataInputStream(client.getInputStream());
            sOut = new DataOutputStream(client.getOutputStream());
            jsonObjectQuery = new JSONObject(sIn.readUTF());
            if (jsonObjectQuery.has("action")) {
                parseJSONForAdmin(jsonObjectQuery);
                if (action.equals("get tables"))
                    if (database.equals("electric"))
                        jsonObjectResult =
                                new ConnectToElectricMeterDB().getTablesFromAccess();
                    else if (database.equals("gas")) {}
                else if (action.equals("get columns")) {
                        System.out.println(tableNameAdmin);
                        if (database.equals("electric"))
                            jsonObjectResult =
                                    new ConnectToElectricMeterDB().getColumnsFromTable(tableNameAdmin);
                        else if (database.equals("gas")) {
                        }
                    }
            }
            else {
                parseJSONForDatas(jsonObjectQuery);
                System.out.println(date);
                if (!jsonObjectQuery.has("date")) {
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getResultFromMeter(tableName, interval);
                } else {
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getResultFromMeter(tableName, interval, date);
                }
            }
            sOut.writeUTF(jsonObjectResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJSONForDatas(JSONObject jsonObject) throws JSONException {
        try {
            this.tableName = jsonObject.getString("table");
            this.interval = jsonObject.getString("interval");
            if (jsonObject.has("date"))
                this.date = jsonObject.getString("date");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        System.out.println(tableName + " " + interval + " " + date);
    }

    private void parseJSONForAdmin(JSONObject jsonObject) throws JSONException {
        try {
            this.action = jsonObject.getString("action");
            this.database = jsonObject.getString("database");
            if (jsonObject.has("tableName"))
                this.tableNameAdmin = jsonObject.getString("tableName");
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
