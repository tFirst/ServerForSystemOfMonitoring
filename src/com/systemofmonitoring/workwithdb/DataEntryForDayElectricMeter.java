package com.systemofmonitoring.workwithdb;

import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import com.systemofmonitoring.resultsclasses.GetDatasForElectricMeter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataEntryForDayElectricMeter {
    private Connection connection;
    private static JSONObject jsonObject = new JSONObject();
    JSONArray date, time, active, passive;
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

    private void getDatasFromMeter(String interval) throws JSONException, SQLException {
        switch (interval) {
            case "day":
                jsonObject =
                        GetDatasForElectricMeter.DisplaceResultForDay(getConnection());
                break;
            case "week":
                jsonObject = GetDatasForElectricMeter.DisplaceResultForWeek(getConnection());
                break;
        }
        InitArrays();
    }

    public void DisplaceDatasForDay() throws JSONException, SQLException {
        getDatasFromMeter("day");

        PreparedStatement preparedStatement;

        for (int i = 0; i < date.length(); i++) {
            if (checkDuplicate(time.getString(i))) {
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

    public void DisplaceDatasForWeek() throws JSONException, SQLException {
        getDatasFromMeter("week");

        PreparedStatement preparedStatement;

        for (int i = 0; i < date.length(); i++) {
            if (checkDuplicate(time.getString(i))) {
                query.append("insert into \"ElectricMeterForWeek\" ")
                        .append("(id, sysdate, value_active, value_passive)")
                        .append("values")
                        .append("(nextval('emw_sequence'), ?, ?, ?");

                preparedStatement = connection.prepareStatement(query.toString());
                preparedStatement.setString(1, date.getString(i));
                preparedStatement.setDouble(2, active.getDouble(i));
                preparedStatement.setDouble(3, passive.getDouble(i));
                preparedStatement.execute();
            }
            else
                i++;
        }
    }

    private boolean checkDuplicate(String date) throws SQLException {
        int i = 0;
        PreparedStatement preparedStatement =
                connection.prepareStatement("select * from \"ElectricMeterForDay\" " +
                        "where systime = ?");
        preparedStatement.setString(1, date);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            i++;
        }

        return i == 0;
    }
}
