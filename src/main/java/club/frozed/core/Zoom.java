package club.frozed.core;

import club.frozed.core.manager.chat.ChatListener;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.database.redis.RedisManager;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.hooks.HookPlaceholderAPI;
import club.frozed.core.manager.hooks.ZoomVaultImplementationChat;
import club.frozed.core.manager.hooks.ZoomVaultImplementationPermission;
import club.frozed.core.manager.listener.BlockCommandListener;
import club.frozed.core.manager.listener.GeneralPlayerListener;
import club.frozed.core.manager.messages.MessageManager;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerDataLoad;
import club.frozed.core.manager.ranks.RankManager;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.manager.staff.StaffListener;
import club.frozed.core.manager.tags.TagManager;
import club.frozed.core.manager.tips.TipsRunnable;
import club.frozed.core.menu.grant.GrantListener;
import club.frozed.core.utils.CC;
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

    @Getter private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesConfig, databaseConfig, settingsConfig, tagsConfig, ranksConfig;

    private TagManager tagManager;
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private MessageManager messageManager;
    private ChatManager chatManager;

    private String disableMessage = "null";

    private static ZoomAPI zoomAPI;

    private RankManager rankManager;

    // Vault Support
    private ZoomVaultImplementationPermission permission;
    private ZoomVaultImplementationChat chat;

    @Override
    public void onEnable() {

        /*
         * TODO-LIST:
         *      -> Bans System
         *      -> Bans Commands
         */

        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.ranksConfig = new FileConfig(this, "ranks.yml");
        this.mongoManager = new MongoManager();
        this.redisManager = new RedisManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.messageManager = new MessageManager();
        this.rankManager = new RankManager();

        zoomAPI = new ZoomAPI();

        chatManager.load();
        mongoManager.connect();

        if (!mongoManager.isConnect()) return;

        redisManager.connect();

        getLogger().info("[Zoom-Ranks] Registering ranks...");
        rankManager.loadRanks();

        getLogger().info("[Zoom-Tags] Registering tags...");
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
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Authors§f: §aRyzeon§7, §aElb1to");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Version§f: §av" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7|-");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aDatabase§f:");
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §aMongoDB§f: " + (mongoManager.isConnect() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + " §7* §cRedis§f: " + (redisManager.isActive() ? "§aEnabled" : "§cDisabled"));
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");

        if (redisManager.isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "online").toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("NETWORK.SERVER-MANAGER.FORMAT")
                    .replace("<prefix>", CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("NETWORK.SERVER-MANAGER.PREFIX")))
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<status>", "&aonline"));
            StaffLang.sendRedisServerMsg(CC.translate(format));
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HookPlaceholderAPI(this).register();
            Bukkit.getConsoleSender().sendMessage(CC.translate(Lang.PREFIX + "&aPlaceholder API expansion successfully registered."));
        }
        if (getSettingsConfig().getConfig().getBoolean("SETTINGS.VAULT-SUPPORT")) {
            if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled() && Bukkit.getPluginManager().getPlugin("Vault") != null) {
                Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aEnabling Vault Support.");
                TaskUtil.runLater(this::loadVault,20 * 3);
            }
        }
    }

    public void loadVault() {
        permission = new ZoomVaultImplementationPermission();
        permission.register();
        chat = new ZoomVaultImplementationChat(permission);
        chat.register();
        Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§aSuccessfully enabling vault support.");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        }

        rankManager.saveRanks();

        if (Zoom.getInstance().getRedisManager().isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "offline").toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("server.format")
                    .replace("<prefix>", Lang.PREFIX)
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<status>", "&coffline"));
            StaffLang.sendRedisServerMsg(CC.translate(format));
        }

        mongoManager.disconnect();

        if (redisManager.isActive()) {
            redisManager.disconnect();
        }

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

        // Blocked Cmd Listener
        pluginManager.registerEvents(new BlockCommandListener(), this);

        //Grant Listeener
        pluginManager.registerEvents(new GrantListener(), this);
    }

    public void shutdownMessage() {
        if (disableMessage.equalsIgnoreCase("Error in MongoDB")) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Please check your database.yml");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fhttps://discord.frozed.club");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
        } else {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§fhttps://discord.frozed.club");
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
