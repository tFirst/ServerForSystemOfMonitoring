package com.systemofmonitoring.displacedatas;

import org.json.JSONException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;


public class DisplaceDataFromMeters {
    protected void DoDisplace() throws JSONException, SQLException, ParseException {}
    protected boolean checkDuplicateForDays(Date date, Time time) throws SQLException {
        return false;
    }
    protected boolean checkDuplicateForWeeks(Date date) throws SQLException {
        return false;
    }
}
