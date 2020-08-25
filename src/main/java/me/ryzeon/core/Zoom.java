package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.listeners.ChatListener;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.manager.database.mongo.MongoManager;
import me.ryzeon.core.manager.database.redis.Redis;
import me.ryzeon.core.manager.database.redis.handler.Payload;
import me.ryzeon.core.manager.database.redis.manager.JedisSettings;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.manager.player.PlayerDataLoad;
import me.ryzeon.core.manager.staff.StaffListener;
import me.ryzeon.core.manager.tags.TagManager;
import me.ryzeon.core.utils.GsonUtil;
import me.ryzeon.core.utils.RegisterHandler;
import me.ryzeon.core.utils.command.CommandFramework;
import me.ryzeon.core.utils.config.FileConfig;
import me.ryzeon.core.utils.lang.Lang;
import me.ryzeon.core.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public final class Zoom extends JavaPlugin {
    @Getter
    private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesconfig;

    private FileConfig databaseconfig;

    private FileConfig settingsconfig;

    private FileConfig tagsconfig;

    private TagManager tagManager;

    private MongoManager mongoManager;

    private Redis redis;

    private ChatManager chatManager;

    private String disablemsg = "null";

    private static ZoomAPI zoomAPI;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesconfig = new FileConfig(this, "messages.yml");
        this.databaseconfig = new FileConfig(this, "database.yml");
        this.settingsconfig = new FileConfig(this, "settings.yml");
        this.tagsconfig = new FileConfig(this, "tags.yml");
        this.mongoManager = new MongoManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        zoomAPI = new ZoomAPI();
        chatManager.load();
        mongoManager.connect();
        if (!mongoManager.isConnect()) return;
        getLogger().info("[Tags] Register tags...");
        tagManager.registerTags();
        loadCommands();
        loadListener();
        JedisSettings settings = new JedisSettings(Zoom.getInstance().getDatabaseconfig().getConfig().getString("redis.host"), Zoom.getInstance().getDatabaseconfig().getConfig().getInt("redis.port"), Zoom.getInstance().getDatabaseconfig().getConfig().getString("redis.password"));
        this.redis = new Redis(settings);
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Author§f: §aRyzeon");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Version§f: §av" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aDatabase§f:");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §aMongoDB§f: " + (mongoManager.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §cRedis§f: " + (redis.isActive() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fJoin to discord https://discord.gg/FXGQq96");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        if (redis.isActive()) {
            servermanagerMSG();
        } else {

        }
    }

    @Override
    public void onDisable() {
        if (Zoom.getInstance().getRedis().isActive()) {
            Zoom.getInstance().getRedis().write(Payload.SERVER_MANAGER, new GsonUtil()
                    .addProperty("SERVER", Lang.SERVER_NAME)
                    .addProperty("STATUS", "offline").get());
        }
        if (!PlayerData.datas.isEmpty()) {
            PlayerData.datas.forEach(PlayerData::saveData);
        }
        mongoManager.disconnect();
        Zoom.getInstance().getLogger().info("[DB] Disconnecting...");
        this.redis.getPool().destroy();
        Zoom.getInstance().getLogger().info("[DB] Disconnecting Successfully");
        shutdownmsg();
    }

    private void loadCommands() {
        RegisterHandler.loadCommandsFromPackage(this, "me.ryzeon.core.command");
        // To load commands in file to view commands with permisison && usage
    }

    private void servermanagerMSG() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Zoom.getInstance().getRedis().write(Payload.SERVER_MANAGER, new GsonUtil()
                        .addProperty("SERVER", Lang.SERVER_NAME)
                        .addProperty("STATUS", "online").get());
            }
        }.runTaskLater(this, 5);
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        //Important to load player data
        pluginManager.registerEvents(new PlayerDataLoad(), this);
        //General Players Listener
        pluginManager.registerEvents(new ChatListener(), this);
        //Important to work menus
        pluginManager.registerEvents(new MenuListener(), this);
        // Staff Listener
        pluginManager.registerEvents(new StaffListener(), this);
    }

    public void shutdownmsg() {
        if (disablemsg.equalsIgnoreCase("Error in mongodb")) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Please check your database.yml");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        } else {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        }
    }
}
