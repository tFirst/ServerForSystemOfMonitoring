package com.systemofmonitoring.connecttodb;

import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConnectToElectricMeterDB extends ParentConnectToDB {
    private File file;

    public JSONObject getTablesFromAccess() throws IOException, JSONException {
        file = new File("C:\\JavaProjects\\ServerForSystemOfMonitoring\\databases\\SET.mdb");
        ArrayList<String> listTables = new ArrayList<>(DatabaseBuilder.open(file).getTableNames());
        return new JSONObject()
                .put("tables", listTables);
    }

    public JSONObject getColumnsFromTable
            (String tableName) throws IOException, JSONException {
        System.out.println(tableName);
        Table t = DatabaseBuilder.open(file).getTable(tableName);
        ArrayList<Object> listColumns = new ArrayList<>(t.getColumns());
        System.out.println(listColumns);
        ArrayList<JSONObject> listJSONObject = new ArrayList<>();
        for (int i = 0; i < listColumns.size(); i++) {
            listJSONObject.add(i, new JSONObject(listColumns.get(i)));
        }
        return new JSONObject()
                .put("columns", listJSONObject);
    }

    @Override
    public JSONObject getDatas() {
        return new JSONObject();
    }
}
