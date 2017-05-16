package com.systemofmonitoring.resultsclasses;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;


public class ResultForElectricMeter extends GetDatasForElectricMeter {
    public ResultForElectricMeter() {
    }

    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval) throws JSONException {
        return GetDatasForElectricMeter.getResultFromMeter(connection, tableName, interval);
    }

    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval,
                                                String date) throws JSONException {
        return GetDatasForElectricMeter.getResultFromMeter(connection, tableName, interval, date);
    }
}
