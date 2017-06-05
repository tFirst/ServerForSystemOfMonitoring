package com.systemofmonitoring.connecttodb;

/**
 * Created by Стас on 04.06.17.
 */
public class ConnectToDB {
    private ConnectToElectricMeterDB connectToElectricMeterDB =
            new ConnectToElectricMeterDB();
    private ConnectToGasMeterDB connectToGasMeterDB =
            new ConnectToGasMeterDB();
    private ConnectToWaterMeterDB connectToWaterMeterDB =
            new ConnectToWaterMeterDB();
    private ConnectToPressureMeterDB connectToPressureMeterDB =
            new ConnectToPressureMeterDB();
    private ConnectToTemperatureMeterDB connectToTemperatureMeterDB =
            new ConnectToTemperatureMeterDB();

    public void  Connect() {
        connectToElectricMeterDB.getDatas();
        connectToGasMeterDB.getDatas();
        connectToPressureMeterDB.getDatas();
        connectToTemperatureMeterDB.getDatas();
        connectToWaterMeterDB.getDatas();
    }
}
