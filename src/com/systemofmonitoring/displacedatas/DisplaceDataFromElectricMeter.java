package com.systemofmonitoring.displacedatas;


import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import com.systemofmonitoring.resultsclasses.GetDatasForElectricMeter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DisplaceDataFromElectricMeter extends DisplaceDataFromMeters {
    private Connection connection = getConnection();
    private static JSONObject jsonObject = new JSONObject();
    JSONArray date, time, active, passive;

    public DisplaceDataFromElectricMeter() throws SQLException {
    }

    private void InitArrays() throws JSONException {
        date = jsonObject.getJSONArray("date");
        if (jsonObject.has("time"))
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
                jsonObject =
                        GetDatasForElectricMeter.DisplaceResultForWeek(getConnection());
                break;
        }
        InitArrays();
    }

    private void DisplaceDatasForDay() throws JSONException, SQLException, ParseException {
        System.out.println("dis day");
        getDatasFromMeter("day");

        System.out.println(date.length());

        PreparedStatement preparedStatement;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-mm-dd");

        for (int i = 0; i < date.length(); i++) {
            String t = time.getString(i);
            if (t.length() == 1)
                t = "0" + t + ":00:00";
            else
                t += ":00:00";
            if (checkDuplicateForDays(new Date(simpleDateFormat.parse(date.getString(i)).getTime()),
                    Time.valueOf(t))) {
                StringBuilder query = new StringBuilder();
                query
                        .append("insert into \"ElectricMeterForDay\" ")
                        .append("values(nextval('emd_sequence'), ?, ?, ?, ?)");

                preparedStatement = connection.prepareStatement(query.toString());
                preparedStatement.setDate(1, new Date(simpleDateFormat.parse(date.getString(i)).getTime()));
                preparedStatement.setTime(2, Time.valueOf(t));
                preparedStatement.setDouble(3, active.getDouble(i));
                preparedStatement.setDouble(4, passive.getDouble(i));
                preparedStatement.execute();
            }
        }
    }

    private void DisplaceDatasForWeek() throws JSONException, SQLException, ParseException {
        System.out.println("dis week");
        getDatasFromMeter("week");

        PreparedStatement preparedStatement;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-mm-dd");

        for (int i = 0; i < date.length(); i++) {
            if (checkDuplicateForWeeks(new Date(simpleDateFormat.parse(date.getString(i)).getTime()))) {
                StringBuilder query = new StringBuilder();
                query
                        .append("insert into \"ElectricMeterForWeek\" ")
                        .append("values(nextval('emw_sequence'), ?, ?, ?)");

                preparedStatement = connection.prepareStatement(query.toString());
                preparedStatement.setDate(1, new Date(simpleDateFormat.parse(date.getString(i)).getTime()));
                preparedStatement.setDouble(2, active.getDouble(i));
                preparedStatement.setDouble(3, passive.getDouble(i));
                preparedStatement.execute();
            }
        }
    }

    @Override
    protected boolean checkDuplicateForDays(Date date, Time time) throws SQLException {
        Boolean flag = true;
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery
                .append("select * from \"ElectricMeterForDay\" ")
                .append("where sysdate = '")
                .append(date)
                .append("' and systime = '")
                .append(time)
                .append("'");
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                flag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    protected boolean checkDuplicateForWeeks(Date date) throws SQLException {
        Boolean flag = true;
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery
                .append("select * from \"ElectricMeterForWeek\" ")
                .append("where sysdate = '")
                .append(date)
                .append("'");
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                flag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void DoDisplace() throws JSONException, SQLException, ParseException {
        DisplaceDatasForDay();
        DisplaceDatasForWeek();
    }
}
