package club.frozed.core;

import club.frozed.core.manager.chat.ChatListener;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.database.redis.RedisManager;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.hooks.HookPlaceholderAPI;
import club.frozed.core.manager.hooks.vault.ZoomVaultImplementationChat;
import club.frozed.core.manager.hooks.vault.ZoomVaultImplementationPermission;
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
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.manager.database.redis.payload.RedisServer;
import club.frozed.core.menu.punishments.button.PunishmentCheckButton;
import club.frozed.lib.FrozedLib;
import club.frozed.lib.chat.CC;
import club.frozed.lib.config.ConfigCursor;
import club.frozed.lib.config.FileConfig;
import club.frozed.lib.menu.ButtonListener;
import club.frozed.lib.task.TaskUtil;
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

    private ZoomAPI zoomAPI;

    private FileConfig messagesConfig, databaseConfig, settingsConfig, tagsConfig, ranksConfig, punishmentConfig, commandsFile;

    private FrozedLib frozedLib;

    private TagManager tagManager;
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private MessageManager messageManager;
    private ChatManager chatManager;
    private PunishmentCheckButton punishmentCheckButton;

    private String disableMessage = "null";

    private RankManager rankManager;

    private boolean joinable = false;

    private ZoomVaultImplementationPermission permission;

    private ZoomVaultImplementationChat chat;

    private boolean passed = false;

    public static String ринокuseridm = "%%__USER__%%";

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDescription().getName().equals("Zoom") || !this.getDescription().getDescription().equals("Server Core")
            || !this.getDescription().getAuthors().contains("Elb1to") || !this.getDescription().getAuthors().contains("Ryzeon")) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            for (int i = 0; i < 100; i++) {
                Bukkit.getServer().getConsoleSender().sendMessage("Wat Doink Vro!?!?!?");
            }
        }

        this.zoomAPI = new ZoomAPI();
        this.frozedLib = new FrozedLib(this);
        this.punishmentCheckButton = new PunishmentCheckButton();
        if (!frozedLib.checkAuthors("Ryzeon", "Elb1to")) return;
        this.commandsFile = new FileConfig(this, "commands.yml");
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.ranksConfig = new FileConfig(this, "ranks.yml");
        this.punishmentConfig = new FileConfig(this, "punishments.yml");
        RedisServer redisServer = new RedisServer(settingsConfig.getString("SETTINGS.LICENSE"),"http://system.ryzeon.me",this);
        Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
        redisServer.pEdiRV0lAs();

        if (redisServer.isiSoNoTGEi()) {
            punishmentCheckButton.阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(true, redisServer);
            passed = true;
            Bukkit.getServer().getConsoleSender().sendMessage("§aLicense is valid! :D");
            Bukkit.getServer().getConsoleSender().sendMessage("");
            Bukkit.getServer().getConsoleSender().sendMessage("§eWelcome: §a"+ redisServer.getbErIfiEdTo());
            Bukkit.getServer().getConsoleSender().sendMessage("§aThank you for purchasing the plugin §4❤");
            Bukkit.getServer().getConsoleSender().sendMessage(" ");
            Bukkit.getServer().getConsoleSender().sendMessage("§eLicense generated in: "+ redisServer.getGeneratedIn());
            Bukkit.getServer().getConsoleSender().sendMessage("§eLicense generated by: "+ redisServer.getGeneratedBy());
            Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
            kuukausi();
        } else {
            punishmentCheckButton.阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(false, redisServer);
            Bukkit.getServer().getConsoleSender().sendMessage("§eYour License is no valid");
            Bukkit.getServer().getConsoleSender().sendMessage("§9§lCOMPRA LICENCIA GIL");
            Bukkit.getServer().getConsoleSender().sendMessage("");
            Bukkit.getServer().getConsoleSender().sendMessage("§eContact me:");
            Bukkit.getServer().getConsoleSender().sendMessage("§eTwitter: §a@Ryzeon_");
            Bukkit.getServer().getConsoleSender().sendMessage("§eDiscord Support: §fhttps://discord.gg/FXGQq96");
            Bukkit.getServer().getConsoleSender().sendMessage("§eReturn error §a"+ redisServer.getReturn());
            Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
            Bukkit.shutdown();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        boolean xdxd = redisServer.checkIp(Utils.getIP() + ":" + this.getServer().getPort());
        punishmentCheckButton.阿阿阿(xdxd);
        if (!xdxd){
            passed = false;
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void kuukausi(){
        this.mongoManager = new MongoManager();
        this.redisManager = new RedisManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.messageManager = new MessageManager();
        this.rankManager = new RankManager();

        this.chatManager.load();

        this.mongoManager.connect();
        if (!this.mongoManager.isConnected()) {
            return;
        }

        redisManager.connect();

        this.getLogger().info("[Zoom-Ranks] Registering ranks...");
        rankManager.loadRanks();

        this.getLogger().info("[Zoom-Tags] Registering tags...");
        tagManager.registerTags();

        this.loadCommands();
        this.loadListener();

        if (Zoom.getInstance().getSettingsConfig().getBoolean("SETTINGS.TIPS.ENABLED")) {
            TaskUtil.runTaskTimerAsynchronously(new TipsRunnable(), Zoom.getInstance().getSettingsConfig().getInt("SETTINGS.TIPS.DELAY"));
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Tips enabled§f -> §6Mode§f -> §6" + Zoom.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.TIPS.MODE"));
        }

        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(CC.translate("&6Zoom &8- &fv" + getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&6 • &eDevelopers: &fElb1to & Ryzeon"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&6 • &eMongo: &f" + (mongoManager.isConnected() ? "&aenabled" : "&cdisabled")));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&6 • &eRedis: &f" + (redisManager.isActive() ? "&aenabled" : "&cdisabled")));
        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);

        if (redisManager.isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "online").toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Zoom.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.FORMAT")
                    .replace("<prefix>", CC.translate(Zoom.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.PREFIX")))
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<status>", "&aonline"));
            StaffLang.sendRedisServerMsg(CC.translate(format));
        }

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HookPlaceholderAPI(this).register();
            Bukkit.getConsoleSender().sendMessage(CC.translate(Lang.PREFIX + "&aPlaceholderAPI hook successfully registered."));
        }
        if (settingsConfig.getConfiguration().getBoolean("SETTINGS.VAULT-SUPPORT")) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                permission = new ZoomVaultImplementationPermission();
                permission.register();
                chat = new ZoomVaultImplementationChat(permission);
                chat.register();
                Bukkit.getConsoleSender().sendMessage("§7[§eZoom§7] §aSuccessfully enabling vault support.");
            }
        }

        PlayerData.startTask();
        TaskUtil.runLaterAsync(() -> setJoinable(true), 5 * 20);
    }

    @Override
    public void onDisable() {
        if (!passed) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            if (playerData != null) {
                playerData.saveData();
            }
        }

        rankManager.saveRanks();

        if (Zoom.getInstance().getRedisManager().isActive()) {
            String json = new RedisMessage(Payload.SERVER_MANAGER).setParam("SERVER", Lang.SERVER_NAME).setParam("STATUS", "offline").toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            String format = CC.translate(Zoom.getInstance().getMessagesConfig().getConfiguration().getString("server.format")
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
        frozedLib.setExcludeCommandConfig(settingsConfig, "SETTINGS.DISABLE-COMMANDS");
        frozedLib.setDisableCommandMessage(CC.translate(Lang.PREFIX + "&e<command> &fcommand was not registered because it was disabled in the configuration."));
        frozedLib.loadCommandsFromPackage("club.frozed.core.command");
        frozedLib.loadCommandsInFile();
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean staffJoinMessages = new ConfigCursor(messagesConfig, "NETWORK.STAFF-ALERTS").exists("ENABLED") ? messagesConfig.getBoolean("NETWORK.STAFF-ALERTS.ENABLED") : true;
        pluginManager.registerEvents(new PlayerDataLoad(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new ButtonListener(), this);
        if (staffJoinMessages) {
            pluginManager.registerEvents(new StaffListener(), this);
        }
        pluginManager.registerEvents(new GeneralPlayerListener(), this);
        pluginManager.registerEvents(new BlockCommandListener(), this);
        pluginManager.registerEvents(new GrantListener(), this);
    }

    public void shutdownMessage() {
        if (disableMessage.equalsIgnoreCase("Error in MongoDB")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Zoom &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while connecting to MongoDB"));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cMake sure that your database.yml has the right data."));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Do you need support? &fhttps://discord.frozed.club"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        } else if (disableMessage.equalsIgnoreCase("Not ip whitelist")){
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Zoom &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while verify ip whitelist"));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cIP: " + Utils.getIP() + ":" + this.getServer().getPort()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Do you need support? &fhttps://discord.frozed.club"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        }
        else {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Zoom &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while loading Zoom."));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cPlease join our Discord for support."));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6 ▶ &fhttps://discord.frozed.club"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
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
