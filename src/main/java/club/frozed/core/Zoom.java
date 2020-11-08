package club.frozed.core;

import club.frozed.core.manager.chat.ChatListener;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.database.redis.RedisManager;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.hooks.HookPlaceholderAPI;
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
import club.frozed.core.utils.menu.ButtonListener;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.lang.Lang;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter @Setter
public final class Zoom extends JavaPlugin {

    @Getter private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesConfig, databaseConfig, settingsConfig, tagsConfig, ranksConfig, punishmentConfig;

    private TagManager tagManager;
    private MongoManager mongoManager;
    private RedisManager redisManager;
    private MessageManager messageManager;
    private ChatManager chatManager;

    private String disableMessage = "null";

    private RankManager rankManager;

    private boolean joinable = false;

    @Override
    public void onEnable() {

        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesConfig = new FileConfig(this, "messages.yml");
        this.databaseConfig = new FileConfig(this, "database.yml");
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.tagsConfig = new FileConfig(this, "tags.yml");
        this.ranksConfig = new FileConfig(this, "ranks.yml");
        this.punishmentConfig = new FileConfig(this,"punishments.yml");
        this.mongoManager = new MongoManager();
        this.redisManager = new RedisManager();
        this.chatManager = new ChatManager();
        this.tagManager = new TagManager();
        this.messageManager = new MessageManager();
        this.rankManager = new RankManager();
        ItemCreator.registerGlow();

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

        if (Zoom.getInstance().getSettingsConfig().getConfig().getBoolean("SETTINGS.TIPS.ENABLED")) {
            TaskUtil.runTaskTimerAsynchronously(new TipsRunnable(), Zoom.getInstance().getSettingsConfig().getConfig().getInt("SETTINGS.TIPS.DELAY"));
            //Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§6Tips enabled§f -> §6Mode§f -> §6" + Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.TIPS.MODE"));
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
            Bukkit.getConsoleSender().sendMessage(CC.translate(Lang.PREFIX + "&aPlaceholderAPI hook successfully registered."));
        }

        PlayerData.startTask();
        TaskUtil.runLaterAsync(() -> setJoinable(true), 5 * 20);
    }

    @Override
    public void onDisable() {
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
        pluginManager.registerEvents(new PlayerDataLoad(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new ButtonListener(), this);
        pluginManager.registerEvents(new StaffListener(), this);
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
