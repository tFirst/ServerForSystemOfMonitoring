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

        public ClientHandler(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                sIn = new DataInputStream(client.getInputStream());
                sOut = new DataOutputStream(client.getOutputStream());
                jsonObjectQuery = new JSONObject(sIn.readUTF());
                parseJSON(jsonObjectQuery);
                jsonObjectResult =
                        new ConnectToPostgreSQL().getResultFromMeter(tableName, interval);
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
    }
}
