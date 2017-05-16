package com.systemofmonitoring.connecttodb;

import com.systemofmonitoring.resultsclasses.ResultForElectricMeter;
import com.systemofmonitoring.resultsclasses.ResultForMeters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;


public class ConnectToPostgreSQL {
    private static Connection connection;

    public ConnectToPostgreSQL() throws SQLException {
        connection = getConnection();
    }

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        return DriverManager.getConnection
                ("jdbc:postgresql://127.0.0.1:5432/Monitoring",
                        "postgres",
                        "cbhtytdtymrbq");
    }

    public JSONObject getResultFromMeter(String tableName, String interval) throws JSONException, SQLException {
        System.out.println("Yess 2" + " " + interval);
        JSONObject jsonObjectResult = new JSONObject();
        if (tableName.contains("Electric"))
            jsonObjectResult =
                    ResultForElectricMeter.getResultFromMeter(connection, tableName, interval);
        else if (tableName.contains("Gas"))
            jsonObjectResult =
                    ResultForMeters.getResultFromMeter(connection, tableName, interval);

        connection.close();

        return jsonObjectResult;
    }

    public JSONObject getResultFromMeter(String tableName, String interval, String date) throws JSONException, SQLException {
        System.out.println("Yess 2d" + " " + interval);
        JSONObject jsonObjectResult = new JSONObject();
        if (tableName.contains("Electric"))
            jsonObjectResult =
                    ResultForElectricMeter.getResultFromMeter(connection, tableName, interval, date);
        else if (tableName.contains("Gas"))
            jsonObjectResult =
                    ResultForMeters.getResultFromMeter(connection, tableName, interval, date);

        connection.close();

        return jsonObjectResult;
    }

    public JSONObject getMetersNames() throws SQLException, JSONException {
        System.out.println("Yess 3");
        JSONArray metersNames = new JSONArray();
        PreparedStatement preparedStatement =
                connection.prepareStatement("select name from \"Meters\"");
        ResultSet resultSet =
                preparedStatement.executeQuery();
        while (resultSet.next()) {
            metersNames.put(resultSet.getString(1));
        }

        connection.close();

        return new JSONObject()
                .put("meters", metersNames);
    }

    public JSONObject getTablesForMeter(String database) throws SQLException, JSONException {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Yess 3");
        JSONArray tablesForMeter = new JSONArray();
        stringBuilder
                .append("select tablename from pg_tables ")
                .append("where schemaname = 'public' and tablename like '")
                .append(database)
                .append("%'");
        PreparedStatement preparedStatement =
                connection.prepareStatement(stringBuilder.toString());
        ResultSet resultSet =
                preparedStatement.executeQuery();
        while (resultSet.next()) {
            tablesForMeter.put(resultSet.getString(1));
        }

        connection.close();

        return new JSONObject()
                .put("tables", tablesForMeter);
    }

    public JSONObject getColumnsFromTable(String tableName) throws SQLException, JSONException {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Yess 3");
        JSONArray columnsForTable = new JSONArray();
        stringBuilder
                .append("select column_name from information_schema.columns ")
                .append("where table_schema = 'public' and table_name like '")
                .append(tableName)
                .append("'");
        PreparedStatement preparedStatement =
                connection.prepareStatement(stringBuilder.toString());
        ResultSet resultSet =
                preparedStatement.executeQuery();
        while (resultSet.next()) {
            columnsForTable.put(resultSet.getString(1));
        }

        connection.close();

        return new JSONObject()
                .put("columns", columnsForTable);
    }

}
