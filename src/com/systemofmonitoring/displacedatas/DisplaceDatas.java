package com.systemofmonitoring.displacedatas;

import org.json.JSONException;

import java.sql.SQLException;


public class DisplaceDatas {
    public void Displace() throws JSONException, SQLException {
        new DisplaceDataFromElectricMeter().DoDisplace();
        new DisplaceDataFromGasMeter().DoDisplace();
        new DisplaceDataFromWaterMeter().DoDisplace();
        new DisplaceDataFromPressureMeter().DoDisplace();
        new DisplaceDataFromTemperatureMeter().DoDisplace();
    }
}
