package com.systemofmonitoring.insertindb;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class InsertInDB {
    protected Boolean checkInput(Connection connection, String tableName, Date date, Time time) throws SQLException {
        return false;
    }
}
