package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.manager.chat.ChatListener;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.manager.database.mongo.MongoManager;
import me.ryzeon.core.manager.listener.GeneralPlayerListener;
import me.ryzeon.core.manager.messages.MessageManager;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.manager.player.PlayerDataLoad;
import me.ryzeon.core.manager.staff.StaffListener;
import me.ryzeon.core.manager.tags.TagManager;
import me.ryzeon.core.manager.tips.TipsRunnable;
import me.ryzeon.core.utils.RegisterHandler;
import me.ryzeon.core.utils.TaskUtil;
import me.ryzeon.core.utils.command.CommandFramework;
import me.ryzeon.core.utils.config.FileConfig;
import me.ryzeon.core.utils.lang.Lang;
import me.ryzeon.core.utils.menu.MenuListener;
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

    private FileConfig settingsconfig;

    private FileConfig tagsconfig;

    private TagManager tagManager;

    private MongoManager mongoManager;

    private MessageManager messageManager;

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
        this.messageManager = new MessageManager();
        zoomAPI = new ZoomAPI();
        chatManager.load();
        mongoManager.connect();
        if (!mongoManager.isConnect()) return;
        getLogger().info("[Tags] Register tags...");
        tagManager.registerTags();
        loadCommands();
        loadListener();
        if (Zoom.getInstance().getSettingsconfig().getConfig().getBoolean("tips.enabled")) {
            TaskUtil.runTaskTimerAsynchronously(new TipsRunnable(), Zoom.getInstance().getSettingsconfig().getConfig().getInt("tips.delay"));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Tips enabled§f -> §6Mode§f -> §6" + Zoom.getInstance().getSettingsconfig().getConfig().getString("tips.mode"));
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
//            String format = Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("server.format")
//                    .replace("<prefix>", Lang.PREFIX)
//                    .replace("<server>", Lang.SERVER_NAME)
//                    .replace("<status>", "&aonline"));
//            StaffLang.sendRedisServerMsg(Color.translate(format));
//        }
    }

    @Override
    public void onDisable() {
//        if (Zoom.getInstance().getRedis().isActive()) {
//            Zoom.getInstance().getRedis().write("SERVER_MANAGER", new GsonUtil()
//                    .addProperty("SERVER", Lang.SERVER_NAME)
//                    .addProperty("STATUS", "offline").get());
//        }else {
//            String format = Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("server.format")
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
        shutdownmsg();
    }

    public void reloadTags() {
        this.tagsconfig = new FileConfig(this, "tags.yml");
    }

    private void loadCommands() {
        RegisterHandler.loadCommandsFromPackage(this, "me.ryzeon.core.command");
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
        /*
        Player Join Msg
         */
        pluginManager.registerEvents(new GeneralPlayerListener(), this);
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

    public void reloadFile() {
        this.messagesconfig = new FileConfig(this, "messages.yml");
        this.databaseconfig = new FileConfig(this, "database.yml");
        this.settingsconfig = new FileConfig(this, "settings.yml");
        this.tagsconfig = new FileConfig(this, "tags.yml");
    }
}
