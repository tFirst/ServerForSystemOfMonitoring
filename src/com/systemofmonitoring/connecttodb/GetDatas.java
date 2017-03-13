package com.systemofmonitoring.connecttodb;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

public class GetDatas {
    public GetDatas() {
    }

    public JSONObject getResultFromMeter(Connection connection,
                                         String interval) throws JSONException {
        return new JSONObject();
    }

    public StringBuffer IntervalDetermination(String interval) {
        StringBuffer stringBuffer = new StringBuffer();
        switch (interval) {
            case "hour":
                stringBuffer
                        .append("sysdate = now()::date and ")
                        .append("systime between now()::time - (time '1:00') and now()::time");
                System.out.println("Hour");
                break;
            case "day":
                stringBuffer
                        .append("(sysdate between now()::date - integer '1' and now()::date) and ");
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
}
