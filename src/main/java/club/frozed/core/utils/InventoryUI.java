package club.frozed.core.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ryzeon
 * Project: FrozedHubDeluxe
 * Date: 10/11/2020 @ 13:30
 */

@Getter
@Setter
public class InventoryUI {

    private String inventoryId;
    private String ip;
    private Plugin plugin;
    private String apiKey;
    private String server;
    private ErrorType errorType;

    private String buyer;
    private String generateDate;

    private boolean valid = false;

    private boolean debug = false;

    public InventoryUI(String server, String inventoryId, String ip, Plugin plugin, String apiKey) {
        this.server = server;
        this.inventoryId = inventoryId;
        this.ip = ip;
        this.plugin = plugin;
        this.apiKey = apiKey;
    }

    public void request() {
        try {
            String pluginName = plugin.getDescription().getName();
            URL url = new URL(server + "/api/check/request/licenses?keyAPI=" + apiKey + "&license=" + inventoryId + "&plugin=" + pluginName + "&ip=" + ip);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            String response = builder.toString();
            
            if (response.equalsIgnoreCase("API_KEY_NOT_VALID")) {
                errorType = ErrorType.API_KEY_NOT_VALID;
            } else if (response.equalsIgnoreCase("INVALID_LICENSE")) {
                errorType = ErrorType.INVALID_LICENSE;
            } else if (response.equalsIgnoreCase("INVALID_PLUGIN_NAME")) {
                errorType = ErrorType.INVALID_PLUGIN_NAME;
            } else if (response.equalsIgnoreCase("INVALID_IP")) {
                errorType = ErrorType.INVALID_IP;
            } else if (response.startsWith("VALID")) {
                errorType = ErrorType.VALID;
                valid = true;
                String[] split = response.split(";");
                this.buyer = split[1];
                this.generateDate = split[3];
            } else {
                errorType = ErrorType.PAGE_ERROR;
            }
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
            valid = false;
            errorType = ErrorType.PAGE_ERROR;
        }
    }

    public enum ErrorType {
        PAGE_ERROR,
        API_KEY_NOT_VALID,
        INVALID_LICENSE,
        INVALID_PLUGIN_NAME,
        INVALID_IP,
        VALID;
    }
}

