package com.systemofmonitoring.resultsclasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ResultForElectricMeter {
    JSONArray timeArray = new JSONArray();
    JSONArray activeValueArray = new JSONArray();
    JSONArray passiveValueArray = new JSONArray();

    public ResultForElectricMeter() {
    }

    public JSONObject getForHour(Connection connection, String query) throws JSONException {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    timeArray.put(resultSet.getString(3));
                    activeValueArray.put(resultSet.getDouble(4));
                    passiveValueArray.put(resultSet.getDouble(5));
                }

                //System.out.println(timeArray.get(index) + " " + activeValueArray.get(index) + " " + passiveValueArray.get(index));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", timeArray);
        jsonObject.put("activeValue", activeValueArray);
        jsonObject.put("passiveValue", passiveValueArray);

        return jsonObject;
    }
}
