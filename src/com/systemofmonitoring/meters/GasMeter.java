package com.systemofmonitoring.meters;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Stanislav Trushin on 27.02.17.
 */
public class GasMeter {
    Statement statement;

    public void Init(Statement statement) {
        this.statement = statement;
        GetResult();
    }

    public void GetResult() {
        try {
            ResultSet resultSet = statement.executeQuery("select * from \"public\".\"GasMeter\"");
            while (resultSet.next()) {
                String string = resultSet.getString(1) +
                        " : " + resultSet.getString(2) +
                        " : " + resultSet.getString(3) +
                        " : " + resultSet.getString(4);
                System.out.println(string);
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
