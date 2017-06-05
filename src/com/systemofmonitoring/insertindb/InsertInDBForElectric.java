package com.systemofmonitoring.insertindb;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class InsertInDBForElectric {
    public String insert(Connection connection, String meterName, JSONObject data) throws JSONException, ParseException {
        JSONArray
                date = data.getJSONArray("date"),
            time = data.getJSONArray("time"),
            active = data.getJSONArray("active"),
            passive = data.getJSONArray("passive");
        String answer = "OK";
        String tableName = getTableName(connection, meterName);

        for (int i = 0; i < date.length(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.applyPattern("yyyy-mm-dd");
            if (checkInput(connection, tableName,
                    new Date(simpleDateFormat.parse(date.getString(i)).getTime()),
                    Time.valueOf(time.getString(i)))) {
                StringBuilder stringBuilderQuery = new StringBuilder();
                stringBuilderQuery
                        .append("insert into \"")
                        .append(tableName)
                        .append("\"")
                        .append("values(nextval('em_sequence'), ?, ?, ?, ?)");

                try {
                    PreparedStatement preparedStatement =
                            connection.prepareStatement(stringBuilderQuery.toString());
                    preparedStatement.setDate(1, new Date(simpleDateFormat.parse(date.getString(i)).getTime()));
                    preparedStatement.setTime(2, Time.valueOf(time.getString(i)));
                    preparedStatement.setDouble(3, active.getDouble(i));
                    preparedStatement.setDouble(4, passive.getDouble(i));
                    preparedStatement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                    answer = "LOSE";
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return answer;
    }

    private String getTableName(Connection connection, String meterName) {
        String tableName = "";
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery
                .append("select id ")
                .append("from \"Meters\" ")
                .append("where name = '")
                .append(meterName)
                .append("'");
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            Integer meterId = 0;
            while (resultSet.next()) {
                meterId = resultSet.getInt(1);
            }

            stringBuilderQuery = new StringBuilder();
            stringBuilderQuery
                    .append("select table_name ")
                    .append("from \"MeterTable\" ")
                    .append("where meter_id = ")
                    .append(meterId);

            preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            resultSet =
                    preparedStatement.executeQuery();
            ArrayList<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            tableName = tables.get(0);

            for (String table : tables) {
                if (table.length() < tableName.length())
                    tableName = table;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    private Boolean checkInput(Connection connection, String tableName, Date date, Time time) {
        Boolean flag = true;
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery
                .append("select * from \"")
                .append(tableName)
                .append("\"")
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
}
