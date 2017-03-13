package com.systemofmonitoring.resultsclasses;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;


public class ResultForElectricMeter extends GetDatas {
    public ResultForElectricMeter() {
    }

    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval) throws JSONException {
        System.out.println("Yess 3" + " " + interval);
        return GetDatas.getResultFromMeter(connection, tableName, interval);
    }

    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval,
                                                String date) throws JSONException {
        System.out.println("Yess 3d" + " " + interval);
        return GetDatas.getResultFromMeter(connection, tableName, interval, date);
    }
}
