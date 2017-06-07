package com.systemofmonitoring.connecttodb;

import com.systemofmonitoring.insertindb.InsertInDBForElectric;
import com.systemofmonitoring.resultsclasses.GetDatasForElectricMeter;
import com.systemofmonitoring.resultsclasses.GetDatasForMeters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.text.ParseException;


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
                    GetDatasForElectricMeter.getResultFromMeter(connection, tableName, interval);
        else if (tableName.contains("Gas"))
            jsonObjectResult =
                    GetDatasForMeters.getResultFromMeter(connection, tableName, interval);

        connection.close();

        return jsonObjectResult;
    }

    public JSONObject getResultFromMeter(String tableName, String interval, String date) throws JSONException, SQLException {
        System.out.println("Yess 2d" + " " + interval);
        JSONObject jsonObjectResult = new JSONObject();
        if (tableName.contains("Electric"))
            jsonObjectResult =
                    GetDatasForElectricMeter.getResultFromMeter(connection, tableName, interval, date);
        else if (tableName.contains("Gas"))
            jsonObjectResult =
                    GetDatasForMeters.getResultFromMeter(connection, tableName, interval, date);

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

    public JSONObject insertInDatabase(String meterName, JSONObject data) throws JSONException, ParseException {
        String answer = "";
        if (meterName.contains("Electric")) {
            answer = new InsertInDBForElectric().insert(connection, meterName, data);
        }
        return new JSONObject()
                .put("answer", answer);
    }

    public JSONObject getResourcesTypes() throws SQLException, JSONException {
        System.out.println("Yess 3");
        JSONArray resourcesTypes = new JSONArray();
        PreparedStatement preparedStatement =
                connection.prepareStatement("select type from \"MetersTypes\"");
        ResultSet resultSet =
                preparedStatement.executeQuery();
        while (resultSet.next()) {
            resourcesTypes.put(resultSet.getString(1));
        }

        connection.close();

        return new JSONObject()
                .put("resources", resourcesTypes);
    }

    public JSONObject getResultForResource(String resourceName, String interval) {
        return null;
    }

    public JSONObject getResultForResource(String resourceName, String interval, String date) {
        return null;
    }
}
