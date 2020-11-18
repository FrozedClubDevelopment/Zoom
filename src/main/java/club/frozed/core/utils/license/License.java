package club.frozed.core.utils.license;

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

public class License {

    private String license;
    private String server;
    private Plugin plugin;
    // This MUST be the same as the REQUEST_KEY defined in config.php
    private String requestKey = "vmLAyzmppLLDgvqMPFyHLSkWdyHYqRImNueC1OLK";
    private boolean debug = false;

    private boolean valid = false;
    private ReturnType returnType;
    private String generatedBy;
    private String licensedTo;
    private String generatedIn;


    public License(String license, String server, Plugin plugin) {
        this.license = license;
        this.server = server;
        this.plugin = plugin;
    }

    public void debug() {
        debug = true;
    }

    public void request() {
        Zoom.getInstance().getWb().阿阿阿阿阿(license);
        try {
            URL url = new URL(server + "/request.php");
            URLConnection connection = url.openConnection();
            if (debug) System.out.println("[DEBUG] Connecting to request server: " + server + "/request.php");
            connection.setRequestProperty("License-Key", license);
            connection.setRequestProperty("Plugin-Name", plugin.getDescription().getName());
            connection.setRequestProperty("Request-Key", requestKey);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (debug) System.out.println("[DEBUG] Reading response");
            if (debug) System.out.println("[DEBUG] Converting to string");
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString();
            if (debug) System.out.println("[DEBUG] Converted");

            String[] responseSplited = response.split(";");
            if (responseSplited[0].equals("VALID")) {
                if (debug) System.out.println("[DEBUG] VALID LICENSE");
                valid = true;
                returnType = ReturnType.valueOf(responseSplited[0]);
                generatedBy = responseSplited[2];
                generatedIn = responseSplited[3];
                licensedTo = responseSplited[1];
            } else {
                if (debug) System.out.println("[DEBUG] FAILED VALIDATION");
                valid = false;
                returnType = ReturnType.valueOf(responseSplited[0]);

                if (debug) System.out.println("[DEBUG] FAILED WITH RESULT: " + returnType);
            }
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isValid() {
        return valid;
    }

    public ReturnType getReturn() {
        return returnType;
    }

    public String getLicensedTo() {
        return licensedTo;
    }

    public String getLicense() {
        return license;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public String getGeneratedIn() {
        return generatedIn;
    }

    public enum ReturnType {
        LICENSE_NOT_FOUND, PLUGIN_NAME_NOT_FOUND, REQUEST_KEY_NOT_FOUND, INVALID_REQUEST_KEY, INVALID_LICENSE, VALID;
    }

    public boolean checkIp(String ip){
        try {
            String linkurl = "http://ryzeon.me/cheking/zoom/ip";
            URL url = new URL(linkurl);
            ArrayList<Object> lines = new ArrayList<>();
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            if (!linkurl.startsWith("http://ryzeon.me")){
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
}
