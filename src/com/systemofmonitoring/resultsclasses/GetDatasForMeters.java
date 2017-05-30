package com.systemofmonitoring.resultsclasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GetDatasForMeters {
    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval) throws JSONException {
        JSONArray dateArray = new JSONArray(),
                timeArray = new JSONArray(),
                valueArray = new JSONArray();
        System.out.println("Yess 4" + " " + interval);
        StringBuilder stringBuilderQuery =
                new StringBuilder();
        stringBuilderQuery
                .append("select * from \"")
                .append(tableName)
                .append("\" ")
                .append("where ")
                .append(IntervalDetermination(interval));
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    dateArray.put(resultSet.getString(2));
                    timeArray.put(resultSet.getString(3));
                    valueArray.put(resultSet.getDouble(4));
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new JSONObject()
                .put("date", dateArray)
                .put("time", timeArray)
                .put("value", valueArray);
    }

    public static JSONObject getResultFromMeter(Connection connection,
                                                String tableName,
                                                String interval,
                                                String date) throws JSONException {
        JSONArray dateArray = new JSONArray(),
                timeArray = new JSONArray(),
                valueArray = new JSONArray();
        System.out.println("Yess 4d" + " " + interval);
        StringBuilder stringBuilderQuery =
                new StringBuilder();
        stringBuilderQuery
                .append("select * from \"")
                .append(tableName)
                .append("\" ")
                .append("where ")
                .append(IntervalDetermination(interval, date));
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBuilderQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(2) != null) {
                    System.out.println("wawdaw" + " " + resultSet.getString(2));
                    dateArray.put(resultSet.getString(2));
                    timeArray.put(resultSet.getString(3));
                    valueArray.put(resultSet.getDouble(4));
                }
            }
            System.out.println(dateArray);
            System.out.println(timeArray);
            System.out.println(valueArray);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new JSONObject()
                .put("date", dateArray)
                .put("time", timeArray)
                .put("value", valueArray);
    }

    public static JSONObject DisplaceResultForDay(Connection connection) throws JSONException {
        JSONArray dateArray = new JSONArray(),
                timeArray = new JSONArray(),
                valueArray = new JSONArray();
        StringBuilder stringBufferQuery =
                new StringBuilder();
        stringBufferQuery
                .append("select sysdate, " +
                        "extract(hour from systime), " +
                        "sum(value_active), " +
                        "sum(value_passive) " +
                        "from \"ElectricMeter\" ")
                .append("where ")
                .append("sysdate = now()::date and ")
                .append("extract(hour from systime) = extract(hour from systime)")
                .append("group by sysdate, extract(hour from systime)");
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBufferQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    dateArray.put(resultSet.getString(1));
                    timeArray.put(resultSet.getString(2));
                    valueArray.put(resultSet.getDouble(3));
                }
                else {
                    dateArray.put("");
                    timeArray.put("");
                    valueArray.put("");
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new JSONObject()
                .put("date", dateArray)
                .put("time", timeArray)
                .put("value", valueArray);
    }

    public static JSONObject getResultForDay(Connection connection, String date) throws JSONException {
        JSONArray dateArray = new JSONArray(),
                timeArray = new JSONArray(),
                valueArray = new JSONArray();
        StringBuilder stringBufferQuery =
                new StringBuilder();
        stringBufferQuery
                .append("select sysdate, " +
                        "extract(hour from systime), " +
                        "sum(value_active), " +
                        "sum(value_passive) \"ElectricMeter\" ")
                .append("where ")
                .append("sysdate = ")
                .append(date)
                .append(" and ")
                .append("extract(hour from systime) = extract(hour from systime)")
                .append("group by sysdate, extract(hour from systime)");
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(stringBufferQuery.toString());
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString(3) != null) {
                    dateArray.put(resultSet.getString(2));
                    timeArray.put(resultSet.getString(3));
                    valueArray.put(resultSet.getDouble(4));
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new JSONObject()
                .put("date", dateArray)
                .put("time", timeArray)
                .put("value", valueArray);
    }

    public static StringBuffer IntervalDetermination(String interval) {
        StringBuffer stringBuffer = new StringBuffer();
        System.out.println("Yess 5" + " " + interval);
        switch (interval) {
            case "hour":
                stringBuffer
                        .append("sysdate = now()::date and ")
                        .append("systime between now()::time - (time '1:00') and now()::time");
                System.out.println("Hour");
                break;
            case "day":
                stringBuffer
                        .append("(sysdate = now()::date) and")
                        .append("(systime between now()::time - (time '24:00') and now()::time)");
                System.out.println("Day");
                break;
            case "week":
                stringBuffer
                        .append("(sysdate between now()::date - integer '7' and now()::date)");
                System.out.println("Week");
                break;
            case "month":
                stringBuffer
                        .append("(sysdate between now()::date - '1 month'::interval and now()::date)");
                System.out.println("Month");
                break;
            case "year":
                stringBuffer
                        .append("(sysdate between now()::date - '1 year'::interval and now()::date)");
                System.out.println("Year");
                break;
        }
        return stringBuffer;
    }

    public static StringBuilder IntervalDetermination(String interval, String date) {
        System.out.println("Yess 5d" + " " + interval);
        StringBuilder stringBuilder = new StringBuilder();
        switch (interval) {
            case "hour":
                stringBuilder
                        .append("sysdate = date '")
                        .append(date)
                        .append("' and ")
                        .append("systime between now()::time - (time '1:00') and now()::time");
                System.out.println("Hour");
                break;
            case "day":
                stringBuilder
                        .append("sysdate = date '")
                        .append(date)
                        .append("'");
                System.out.println("Day");
                break;
            case "week":
                stringBuilder
                        .append("(sysdate between date '")
                        .append(date)
                        .append("' - integer '7' and date '")
                        .append(date)
                        .append("')");
                System.out.println("Week " + date);
                break;
            case "month":
                stringBuilder
                        .append("(sysdate between date '")
                        .append(date)
                        .append("' - '1 month'::interval and ")
                        .append("sysdate between date '")
                        .append(date)
                        .append("')");
                System.out.println("Month");
                break;
            case "year":
                stringBuilder
                        .append("(sysdate between date '")
                        .append(date)
                        .append("' - '1year'::interval and ")
                        .append("sysdate between date '")
                        .append(date)
                        .append("')");
                System.out.println("Year");
                break;
        }
        return stringBuilder;
    }
}
