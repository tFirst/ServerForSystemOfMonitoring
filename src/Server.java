import com.systemofmonitoring.connecttodb.ConnectToPostgreSQL;
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
        try {
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
        private String tableName, interval;
        private JSONObject jsonObjectQuery, jsonObjectResult;
        StringBuffer stringQuery = new StringBuffer();

        public ClientHandler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                sIn = new DataInputStream(client.getInputStream());
                sOut = new DataOutputStream(client.getOutputStream());
                jsonObjectQuery = new JSONObject(sIn.readUTF());
                parseJSON(jsonObjectQuery);
                switch (tableName) {
                    case "ElectricMeter":
                        stringQuery
                                .append("select * from \"")
                                .append(tableName)
                                .append("\" ")
                                .append("where ");
                        IntervalDetermination(stringQuery);
                        jsonObjectResult =
                                new ConnectToPostgreSQL().getResultElectricMeter(stringQuery.toString());
                        break;
                    default:
                        stringQuery
                                .append("select *  from \"")
                                .append(tableName)
                                .append("\" ")
                                .append("where ");
                        break;
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
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        private void IntervalDetermination(StringBuffer stringBuffer) {
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
        }
    }
}
