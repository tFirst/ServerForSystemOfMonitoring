package com.systemofmonitoring.displacedatas;

import org.json.JSONException;

import java.sql.SQLException;
import java.text.ParseException;


public class DisplaceDatas {
    private DisplaceDataFromElectricMeter displaceDataFromElectricMeter =
            new DisplaceDataFromElectricMeter();
    private DisplaceDataFromGasMeter displaceDataFromGasMeter =
            new DisplaceDataFromGasMeter();
    private DisplaceDataFromWaterMeter displaceDataFromWaterMeter =
            new DisplaceDataFromWaterMeter();
    private DisplaceDataFromPressureMeter displaceDataFromPressureMeter =
            new DisplaceDataFromPressureMeter();
    private DisplaceDataFromTemperatureMeter displaceDataFromTemperatureMeter =
            new DisplaceDataFromTemperatureMeter();

    public DisplaceDatas() throws SQLException {
    }

    public void Displace() throws JSONException, SQLException, ParseException {
        displaceDataFromElectricMeter.DoDisplace();
        /**displaceDataFromGasMeter.DoDisplace();
        displaceDataFromWaterMeter.DoDisplace();
        displaceDataFromPressureMeter.DoDisplace();
        displaceDataFromTemperatureMeter.DoDisplace();*/
    }
}
