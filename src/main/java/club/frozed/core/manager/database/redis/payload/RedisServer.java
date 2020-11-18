package club.frozed.core.manager.database.redis.payload;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Ryzeon
 * Project: FrozedHubDeluxe
 * Date: 10/11/2020 @ 13:30
 */
public class RedisServer {

    private String uSeRBeRiF;
    private String server;
    private Plugin plugeeEn;
    private String v0lasDem0no = "vmLAyzmppLLDgvqMPFyHLSkWdyHYqRImNueC1OLK";
    private boolean dEeZBOoG = false;

    private boolean iSoNoTGEi = false;
    private ReturnType returnType;
    private String generatedBy;
    private String bErIfiEdTo;
    private String generatedIn;

    public RedisServer(String uSeRBeRiF, String server, Plugin plugeeEn) {
        this.uSeRBeRiF = uSeRBeRiF;
        this.server = server;
        this.plugeeEn = plugeeEn;
    }

    public void debug() {
        dEeZBOoG = true;
    }

    public void pEdiRV0lAs() {
        Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿(uSeRBeRiF);
        try {
            URL url = new URL(server + "/request.php");
            URLConnection connection = url.openConnection();
            if (dEeZBOoG) System.out.println("[DEBUG] Connecting to request server: " + server + "/request.php");
            connection.setRequestProperty("License-Key", uSeRBeRiF);
            connection.setRequestProperty("Plugin-Name", plugeeEn.getDescription().getName());
            connection.setRequestProperty("Request-Key", v0lasDem0no);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (dEeZBOoG) System.out.println("[DEBUG] Reading response");
            if (dEeZBOoG) System.out.println("[DEBUG] Converting to string");
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString();
            if (dEeZBOoG) System.out.println("[DEBUG] Converted");

            String[] responseSplited = response.split(";");
            if (responseSplited[0].equals("VALID")) {
                if (dEeZBOoG) System.out.println("[DEBUG] VALID LICENSE");
                iSoNoTGEi = true;
                returnType = ReturnType.valueOf(responseSplited[0]);
                generatedBy = responseSplited[2];
                generatedIn = responseSplited[3];
                bErIfiEdTo = responseSplited[1];
            } else {
                if (dEeZBOoG) System.out.println("[DEBUG] FAILED VALIDATION");
                iSoNoTGEi = false;
                returnType = ReturnType.valueOf(responseSplited[0]);

                if (dEeZBOoG) System.out.println("[DEBUG] FAILED WITH RESULT: " + returnType);
            }
        } catch (Exception ex) {
            if (dEeZBOoG) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isiSoNoTGEi() {
        return iSoNoTGEi;
    }

    public ReturnType getReturn() {
        return returnType;
    }

    public String getbErIfiEdTo() {
        return bErIfiEdTo;
    }

    public String getuSeRBeRiF() {
        return uSeRBeRiF;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public String getGeneratedIn() {
        return generatedIn;
    }

    public boolean checkIp(String ip) {
        try {
            String linkurl = "http://ryzeon.me/cheking/zoom/ip";
            URL url = new URL(linkurl);
            ArrayList<Object> lines = new ArrayList<>();
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (!linkurl.startsWith("http://ryzeon.me")) {
                Bukkit.shutdown();
                return false;
            }
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
            if (!lines.contains(Utils.getIP() + ":" + Zoom.getInstance().getServer().getPort())) {
                Zoom.getInstance().setDisableMessage("Not ip whitelist");
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public enum ReturnType {
        LICENSE_NOT_FOUND, PLUGIN_NAME_NOT_FOUND, REQUEST_KEY_NOT_FOUND, INVALID_REQUEST_KEY, INVALID_LICENSE, VALID;
    }
}
