package com.systemofmonitoring.resultsclasses;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

/**
 * Created by Stanislav Trushin on 24.03.17.
 */
public class ResultForMeters extends GetDatas{
    public ResultForMeters() {
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
