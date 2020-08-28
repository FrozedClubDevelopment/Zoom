package club.frozed.core;

import club.frozed.core.manager.chat.ChatListener;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.listener.GeneralPlayerListener;
import club.frozed.core.manager.messages.MessageManager;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerDataLoad;
import club.frozed.core.manager.staff.StaffListener;
import club.frozed.core.manager.tags.TagManager;
import club.frozed.core.manager.tips.TipsRunnable;
import club.frozed.core.utils.RegisterHandler;
import club.frozed.core.utils.TaskUtil;
import club.frozed.core.utils.command.CommandFramework;
import club.frozed.core.utils.config.FileConfig;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.menu.MenuListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
@Setter
public final class Zoom extends JavaPlugin {

    @Getter
    private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesConfig;
    private FileConfig databaseConfig;
    private FileConfig settingsConfig;
    private FileConfig tagsConfig;

    private TagManager tagManager;
    private MongoManager mongoManager;
    private MessageManager messageManager;
    private ChatManager chatManager;

    private String disableMessage = "null";

    private static ZoomAPI zoomAPI;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.mongoManager = new MongoManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.messageManager = new MessageManager();

        zoomAPI = new ZoomAPI();

        chatManager.load();
        mongoManager.connect();

        if (!mongoManager.isConnect()) return;

        getLogger().info("[Zoom Tags] Registering tags...");
        tagManager.registerTags();

        loadCommands();
        loadListener();

        if (Zoom.getInstance().getSettingsConfig().getConfig().getBoolean("SETTINGS.TIPS.ENABLED")) {
            TaskUtil.runTaskTimerAsynchronously(new TipsRunnable(), Zoom.getInstance().getSettingsConfig().getConfig().getInt("SETTINGS.TIPS.DELAY"));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Tips enabled§f -> §6Mode§f -> §6" + Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.TIPS.MODE"));
        }

        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Author§f: §aRyzeon");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Version§f: §av" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aDatabase§f:");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §aMongoDB§f: " + (mongoManager.isConnect() ? "§aEnabled" : "§cDisabled"));
//        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §cRedis§f: " + (redis.isActive() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fJoin to discord https://discord.gg/FXGQq96");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
//        if (redis.isActive()) {
//            servermanagerMSG();
//        } else {
//            String format = Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("server.format")
//                    .replace("<prefix>", Lang.PREFIX)
//                    .replace("<server>", Lang.SERVER_NAME)
//                    .replace("<status>", "&aonline"));
//            StaffLang.sendRedisServerMsg(Color.translate(format));
//        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
//        if (Zoom.getInstance().getRedis().isActive()) {
//            Zoom.getInstance().getRedis().write("SERVER_MANAGER", new GsonUtil()
//                    .addProperty("SERVER", Lang.SERVER_NAME)
//                    .addProperty("STATUS", "offline").get());
//        }else {
//            String format = Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("server.format")
//                    .replace("<prefix>", Lang.PREFIX)
//                    .replace("<server>", Lang.SERVER_NAME)
//                    .replace("<status>", "&coffline"));
//            StaffLang.sendRedisServerMsg(Color.translate(format));
//        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        }
        mongoManager.disconnect();

        shutdownMessage();
    }

    public void reloadTags() {
        this.tagsConfig = new FileConfig(this, "tags.yml");
    }

    private void loadCommands() {
        RegisterHandler.loadCommandsFromPackage(this, "club.frozed.core.command");
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

        // Player Join Message
        pluginManager.registerEvents(new GeneralPlayerListener(), this);
    }

    public void shutdownMessage() {
        if (disableMessage.equalsIgnoreCase("Error in MongoDB")) {
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

    public void reloadFile() {
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
    }

    public StringBuilder getAuthors() {
        StringBuilder format = new StringBuilder("");
        List<String> authors = getDescription().getAuthors();
        int size = authors.size();
        for (String s : authors) {
            size--;
            if (size == 1) {
                format.append(s);
            } else {
                format.append(", ").append(s);
            }
        }

        return format;
    }
}
