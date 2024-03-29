package club.frozed.core;

import club.frozed.core.manager.chat.ChatListener;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.database.redis.RedisManager;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.hooks.HookPlaceholderAPI;
import club.frozed.core.manager.hooks.callback.AbstractCallback;
import club.frozed.core.manager.hooks.callback.CallbackReason;
import club.frozed.core.manager.impl.VaultImpl;
import club.frozed.core.manager.listener.BlockCommandListener;
import club.frozed.core.manager.listener.GeneralPlayerListener;
import club.frozed.core.manager.messages.MessageManager;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerDataLoad;
import club.frozed.core.manager.ranks.RankManager;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.manager.staff.StaffListener;
import club.frozed.core.manager.staff.freeze.FreezeHandlerListener;
import club.frozed.core.manager.staff.freeze.FreezeListener;
import club.frozed.core.manager.tags.TagManager;
import club.frozed.core.manager.tips.TipsRunnable;
import club.frozed.core.menu.grant.GrantListener;
import club.frozed.core.menu.punishments.button.PunishmentCheckButton;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.grant.GrantUtil;
import club.frozed.core.utils.lang.Lang;
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
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
public final class Zoom extends JavaPlugin {

    @Getter private static Zoom instance;
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
    private boolean passed = false;
    private boolean debug;

    public static String serverName = "%%__USER__%%";

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDescription().getName().equals("Zoom") || !this.getDescription().getWebsite().equals("www.frozed.club")
                || !this.getDescription().getDescription().equals("All-in-One Server Core developed on Frozed Club Development.")
                || !this.getDescription().getAuthors().contains("Elb1to") || !this.getDescription().getAuthors().contains("Ryzeon")) {
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            for (int i = 0; i < 100; i++) {
                Bukkit.getServer().getConsoleSender().sendMessage("Wat Doink Vro!?!?!?");
            }
        }

        this.zoomAPI = new ZoomAPI();
        this.frozedLib = new FrozedLib(this);
        this.punishmentCheckButton = new PunishmentCheckButton();
        if (!frozedLib.checkAuthors("Ryzeon", "Elb1to")) {
            return;
        }
        this.commandsFile = new FileConfig(this, "commands.yml");
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.ranksConfig = new FileConfig(this, "ranks.yml");
        this.punishmentConfig = new FileConfig(this, "punishments.yml");
        if (!settingsConfig.getConfiguration().contains("SETTINGS.LICENSE")) {
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cWhere is the license? XDXD"));
            restartInventoryID();
        }
        AbstractCallback abstractCallback = (AbstractCallback) GrantUtil.check(settingsConfig.getString("SETTINGS.LICENSE"));
        if (abstractCallback == null) {
            Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(false, null);
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cInvalid License"));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cJoin our Discord Server for Support."));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&chttps://discord.frozed.club"));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cError Code&f: No valid"));
            Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
            Zoom.getInstance().restartInventoryID();
            System.exit(0);
            return;
        }

        abstractCallback.check();
        if (abstractCallback.getCallbackReason() == null || abstractCallback.getCallbackReason() != CallbackReason.VALID) {
            Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(false, abstractCallback);
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cInvalid License"));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cJoin our Discord Server for Support."));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&chttps://discord.frozed.club"));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cError Code&f: " + abstractCallback.getCallbackReason().name()));
            Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
            Zoom.getInstance().restartInventoryID();
            System.exit(0);
            abstractCallback.reCheck();
            return;
        }
        Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(true, abstractCallback);
        Zoom.getInstance().setPassed(true);
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&aLicense Validated"));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&bUser&f: " + abstractCallback.getCallbackReason().object1()));
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&bGenerated&f: " + abstractCallback.getCallbackReason().object2()));
        Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
        if (abstractCallback.getCallbackReason() == CallbackReason.VALID) {
            kuukausi();
        }
    }

    private void kuukausi() {
        this.debug = settingsConfig.getBoolean("SETTINGS.DEBUG");
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
        }

        Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(CC.translate("&bZoom &8- &fv" + getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &bDevelopers&f: Elb1to & Ryzeon"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &bMongo&f: " + (mongoManager.isConnected() ? "&aenabled" : "&cdisabled")));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7 • &bRedis&f: " + (redisManager.isActive() ? "&aenabled" : "&cdisabled")));
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

        if (this.getServer().getPluginManager().getPlugin("Vault") != null) {
            VaultImpl vault = new VaultImpl();
            vault.register();

            Bukkit.getConsoleSender().sendMessage(CC.translate(Lang.PREFIX + "&aVault implementation successfully performed."));
        }

        PlayerData.startTask();
        TaskUtil.runLaterAsync(() -> setJoinable(true), 5 * 20);
    }

    @Override
    public void onDisable() {
        if (!passed) {
            shutdownMessage();
            return;
        }
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

    public void restartInventoryID() {
        Bukkit.shutdown();
        Bukkit.getPluginManager().disablePlugin(this);
        System.exit(0);
    }

    public void reloadTags() {
        this.tagsConfig = new FileConfig(this, "tags.yml");
    }

    private void loadCommands() {
        frozedLib.setExcludeCommandConfig(settingsConfig, "SETTINGS.DISABLE-COMMANDS");
        frozedLib.setDisableCommandMessage(CC.translate(Lang.PREFIX + "&b<command> &fcommand was not registered because it was disabled in the configuration."));
        frozedLib.loadCommandsFromPackage("club.frozed.core.command");
        frozedLib.loadCommandsInFile();
    }

    private void loadListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        boolean staffJoinMessages = new ConfigCursor(messagesConfig, "NETWORK.STAFF-ALERTS").exists("ENABLED") ? messagesConfig.getBoolean("NETWORK.STAFF-ALERTS.ENABLED") : true;
        if (staffJoinMessages) {
            pluginManager.registerEvents(new StaffListener(), this);
        }
        registerListeners(
                new PlayerDataLoad(),
                new ChatListener(),
                new ButtonListener(),

                new GeneralPlayerListener(),
                new BlockCommandListener(),
                new GrantListener(),
                new FreezeListener(),
                new FreezeHandlerListener()
        );
//        pluginManager.registerEvents(new PlayerDataLoad(), this);
//        pluginManager.registerEvents(new ChatListener(), this);
//        pluginManager.registerEvents(new ButtonListener(), this);
//
//        pluginManager.registerEvents(new GeneralPlayerListener(), this);
//        pluginManager.registerEvents(new BlockCommandListener(), this);
//        pluginManager.registerEvents(new GrantListener(), this);
//        pluginManager.registerEvents(new FreezeListener(), this);
//        pluginManager.registerEvents(new FreezeHandlerListener());
    }

    private void registerListeners(Listener... listeners){
        Arrays.stream(listeners).forEach(listener -> {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(listener, this);
        });
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
        } else if (disableMessage.equalsIgnoreCase("Not ip whitelist")) {
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Zoom &8- &fv" + getDescription().getVersion()));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7Developed on &bFrozed Club Development"));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cAn error has occurred while verify ip whitelist"));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cIP: " + Utils.getIP() + ":" + this.getServer().getPort()));
            Bukkit.getConsoleSender().sendMessage(CC.translate(" "));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Do you need support? &fhttps://discord.frozed.club"));
            Bukkit.getConsoleSender().sendMessage(CC.CHAT_BAR);
        } else {
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
