package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.listeners.ChatListener;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.manager.database.mongo.MongoManager;
import me.ryzeon.core.manager.database.redis.listener.RecievedMessagesListener;
import me.ryzeon.core.manager.database.redis.manager.Redis;
import me.ryzeon.core.manager.database.redis.manager.RedisCredentials;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.manager.player.PlayerDataLoad;
import me.ryzeon.core.manager.tags.TagManager;
import me.ryzeon.core.utils.RegisterHandler;
import me.ryzeon.core.utils.command.CommandFramework;
import me.ryzeon.core.utils.config.FileConfig;
import me.ryzeon.core.utils.lang.Lang;
import me.ryzeon.core.utils.menu.MenuListener;
import org.bukkit.Bukkit;
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

    private FileConfig tagsconfig;

    private TagManager tagManager;

    private MongoManager mongoManager;

    private Redis redis;

    private RedisCredentials redisCredentials;

    private ChatManager chatManager;

    private String disablemsg = "null";

    private static ZoomAPI zoomAPI;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesconfig = new FileConfig(this, "messages.yml");
        this.databaseconfig = new FileConfig(this, "database.yml");
        this.commandsconfig = new FileConfig(this, "commands.yml");
        this.settingsconfig = new FileConfig(this, "settings.yml");
        this.tagsconfig = new FileConfig(this, "tags.yml");
        this.mongoManager = new MongoManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        String redishost = Zoom.getInstance().getDatabaseconfig().getConfig().getString("redis.host");
        int redisport = Zoom.getInstance().getDatabaseconfig().getConfig().getInt("redis.port");
        this.redisCredentials = new RedisCredentials(redishost, redisport);
        this.redisCredentials.authenticate(Zoom.getInstance().getDatabaseconfig().getConfig().getString("redis.password"));
        this.redis = new Redis("Zoom", redisCredentials);
        loadRedisListeners();
        zoomAPI = new ZoomAPI();
        chatManager.load();
        mongoManager.connect();
        if (!mongoManager.isConnect()) return;
        getLogger().info("[Tags] Register tags...");
        tagManager.registerTags();
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Author§f: §aRyzeon");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Version§f: §av" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aDatabase§f:");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §aMongoDB§f: " + (mongoManager.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §cRedis§f: " + (redis.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fJoin to discord https://discord.gg/FXGQq96");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        loadCommands();
        loadMenuListener();
        loadListener();
        this.redis.subscribe();
    }

    @Override
    public void onDisable() {
        PlayerData.datas.forEach(PlayerData::saveData);
        shutdownmsg();
        mongoManager.disconnect();
        redis.close();
    }

    private void loadRedisListeners() {
        this.redis.registerListener(new RecievedMessagesListener());
    }

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

    private void loadMenuListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        //Important to load player data
        pluginManager.registerEvents(new MenuListener(), this);
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
