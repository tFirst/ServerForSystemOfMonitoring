import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
import com.systemofmonitoring.workwithdb.DataEntryForDayElectricMeter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        DataEntryForDayElectricMeter dataEntryForDayElectricMeter =
                new DataEntryForDayElectricMeter();
        try {
            dataEntryForDayElectricMeter.DisplaceDatas();
            ServerSocket server = new ServerSocket(3333);
            while (true) {
                Socket client = server.accept();
                new ClientHandler(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler extends Thread {
        private DataOutputStream sOut;
        private DataInputStream sIn;
        private Socket client;
        private String tableName, interval, date;
        private JSONObject jsonObjectQuery, jsonObjectResult;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                sIn = new DataInputStream(client.getInputStream());
                sOut = new DataOutputStream(client.getOutputStream());
                jsonObjectQuery = new JSONObject(sIn.readUTF());
                parseJSON(jsonObjectQuery);
                System.out.println(date);
                if (date == null) {
                    System.out.println("Yess 1" + " " + interval);
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getResultFromMeter(tableName, interval);
                } else {
                    System.out.println("Yess 1d" + " " + interval);
                    jsonObjectResult =
                            new ConnectToPostgreSQL().getResultFromMeter(tableName, interval, date);
                }
                System.out.println(jsonObjectResult.toString());
                sOut.writeUTF(jsonObjectResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseJSON(JSONObject jsonObject) throws JSONException {
            try {
                this.tableName = jsonObject.getString("table");
                this.interval = jsonObject.getString("interval");
                if (jsonObject.has("date"))
                    this.date = jsonObject.getString("date");
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }
}
