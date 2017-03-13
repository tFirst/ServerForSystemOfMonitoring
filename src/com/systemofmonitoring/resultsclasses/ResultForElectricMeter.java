package com.systemofmonitoring.resultsclasses;

import com.systemofmonitoring.connecttodb.GetDatas;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ResultForElectricMeter extends GetDatas {
    private StringBuffer stringBufferQuery = new StringBuffer();
    private JSONArray dateArray = new JSONArray();
    private JSONArray timeArray = new JSONArray();
    private JSONArray activeValueArray = new JSONArray();
    private JSONArray passiveValueArray = new JSONArray();

    public ResultForElectricMeter() {
    }

    @Override
    public JSONObject getResultFromMeter(Connection connection, String interval) throws JSONException {
        stringBufferQuery
                .append("select * from \"ElectricMeter\" ")
                .append("where ")
                .append(IntervalDetermination(interval));
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBufferQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    dateArray.put(resultSet.getString(2));
                    timeArray.put(resultSet.getString(3));
                    activeValueArray.put(resultSet.getDouble(4));
                    passiveValueArray.put(resultSet.getDouble(5));
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new JSONObject()
                .put("date", dateArray)
                .put("time", timeArray)
                .put("activeValue", activeValueArray)
                .put("passiveValue", passiveValueArray);
    }
}
