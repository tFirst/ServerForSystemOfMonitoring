package com.systemofmonitoring.connecttodb;

import com.systemofmonitoring.resultsclasses.ResultForElectricMeter;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectToPostgreSQL {
    private static Connection connection;

    public ConnectToPostgreSQL() throws SQLException{
        connection = getConnection();
    }

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        return DriverManager.getConnection
                ("jdbc:postgresql://127.0.0.1:5432/Monitoring",
                "postgres",
                "cbhtytdtymrbq");
    }

    public JSONObject getResultFromMeter(String tableName, String interval) throws JSONException {
        JSONObject jsonObjectResult = new JSONObject();
        switch (tableName) {
            case "ElectricMeter":
                ResultForElectricMeter resultForElectricMeter =
                        new ResultForElectricMeter();
                jsonObjectResult =
                        resultForElectricMeter.getResultFromMeter(connection, interval);
                break;
            case "GasMeter":
                break;
        }
        return jsonObjectResult;
    }

}
