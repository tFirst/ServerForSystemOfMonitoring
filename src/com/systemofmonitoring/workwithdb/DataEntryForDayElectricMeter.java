package com.systemofmonitoring.workwithdb;


import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import com.systemofmonitoring.resultsclasses.GetDatas;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataEntryForDayElectricMeter {
    private Connection connection;
    private static JSONObject jsonObject = new JSONObject();
    JSONArray date, time, active, passive;
    ArrayList<String> dateArray, timeArray;
    ArrayList<Double> activeArray, passiveArray;
    private StringBuffer query;

    public void InitArrays() throws JSONException {
        date = jsonObject.getJSONArray("date");
        time = jsonObject.getJSONArray("time");
        active = jsonObject.getJSONArray("activeValue");
        passive = jsonObject.getJSONArray("passiveValue");
    }

    private static Connection getConnection() throws SQLException {
        return ConnectToPostgreSQL.getConnection();
    }

    private void getDatasFromMeter() throws JSONException, SQLException {
        jsonObject =
                GetDatas.DisplaceResultForDay(getConnection());
        InitArrays();
    }

    public void DisplaceDatas() throws JSONException, SQLException {
        getDatasFromMeter();

        PreparedStatement preparedStatement;

        for (int i = 0; i < date.length(); i++) {
            if (checkDuplicate(date.getString(i))) {
                query.append("insert into \"ElectricMeterForDay\" ")
                        .append("(id, sysdate, systime, value_active, value_passive) ")
                        .append("values")
                        .append("(nextval('emd_sequence'), ?, ?, ?, ?)");

            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setString(1, date.getString(i));
            preparedStatement.setString(2, time.getString(i));
            preparedStatement.setDouble(3, active.getDouble(i));
            preparedStatement.setDouble(4, passive.getDouble(i));
            preparedStatement.execute();
            }
            else
                i++;
        }
    }

    private boolean checkDuplicate(String date) throws SQLException {
        int i = 0;
        PreparedStatement preparedStatement =
                connection.prepareStatement("select * from \"ElectricMeterForDay\" where sysdate = ?");
        preparedStatement.setString(1, date);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            i++;
        }

        return i == 0;
    }
}
