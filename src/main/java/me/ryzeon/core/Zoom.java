package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.listeners.ChatListener;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.manager.database.MongoManager;
import me.ryzeon.core.manager.database.RedisManager;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.manager.player.PlayerDataLoad;
import me.ryzeon.core.utils.RegisterHandler;
import me.ryzeon.core.utils.command.CommandFramework;
import me.ryzeon.core.utils.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public final class Zoom extends JavaPlugin {
    @Getter
    private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesconfig;

    private FileConfig databaseconfig;

    private FileConfig commandsconfig;

    private FileConfig settingsconfig;

    private MongoManager mongoManager;

    private RedisManager redisManager;

    private ChatManager chatManager;

    private String disablemsg = "null";

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesconfig = new FileConfig(this, "messages.yml");
        this.databaseconfig = new FileConfig(this, "database.yml");
        this.commandsconfig = new FileConfig(this, "commands.yml");
        this.settingsconfig = new FileConfig(this, "settings.yml");
        this.mongoManager = new MongoManager();
        this.redisManager = new RedisManager();
        this.chatManager = new ChatManager();
        chatManager.load();
        mongoManager.connect();
        if (!mongoManager.isConnect()) return;
        redisManager.connect();
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        Bukkit.getConsoleSender().sendMessage("§6Zoom Core");
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§6Author§f: §aRyzeon");
        Bukkit.getConsoleSender().sendMessage("§6Version§f: §av" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§aDatabase§f:");
        Bukkit.getConsoleSender().sendMessage(" §7* §aMongoDB§f: " + (mongoManager.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(" §7* §cRedis§f: " + (redisManager.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
        Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        loadCommands();
        loadListener();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        }
        shutdownmsg();
        mongoManager.disconnect();
        redisManager.disconnect();
    }

    // Load Commands
    private void loadCommands() {
        RegisterHandler.loadCommandsFromPackage(this, "me.ryzeon.core.command");
        // To load commands in file to view commands with permisison && usage
        commandFramework.loadCommandsInFile();
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        //Important to load player data
        pluginManager.registerEvents(new PlayerDataLoad(), this);
        pluginManager.registerEvents(new ChatListener(), this);
    }

    public void shutdownmsg() {
        if (disablemsg.equalsIgnoreCase("Error in mongodb")) {
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage("§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§6Please check your database.yml");
            Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        } else {
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage("§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        }
    }
}
